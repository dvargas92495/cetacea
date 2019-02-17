package xyz.cetacea.endpoints;

import org.eclipse.jetty.http.HttpMethod;
import org.jooq.tools.json.JSONArray;
import org.jooq.tools.json.JSONObject;
import org.jooq.tools.json.JSONValue;
import xyz.cetacea.util.Endpoint;
import xyz.cetacea.util.Param;
import xyz.cetacea.util.RequestHelper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseServlet extends HttpServlet {

    private Map<HttpMethod, Method> servletMethods = new HashMap<>();
    private Map<HttpMethod, Parameter[]> servletRequests = new HashMap<>();
    private static final String GETTER_PREFIX = "get";

    BaseServlet() {
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Endpoint annotation = method.getAnnotation(Endpoint.class);
            if (annotation != null) {
                servletMethods.put(annotation.value(), method);
                Parameter[] params = method.getParameters();
                servletRequests.put(annotation.value(), params);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response, HttpMethod.GET);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response, HttpMethod.POST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response, HttpMethod.PUT);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response, HttpMethod.DELETE);
    }

    private void execute(HttpServletRequest request, HttpServletResponse response, HttpMethod methodType) {
        try {
            Method method = servletMethods.get(methodType);
            if (method == null) {
                throw new RuntimeException(String.format("No %s Method for %s", methodType, this.getClass().getSimpleName()));
            }
            Parameter[] params = servletRequests.get(methodType);
            Function<String, String> fieldMapper = getFieldMapper(request);
            Object[] inputs = Arrays.stream(params).map(p -> {
                Param annotation = p.getAnnotation(Param.class);
                if (annotation == null) {
                    throw new RuntimeException(String.format("Parameter %s of method %s is missing @Param annotation", p.getName(), method.getName()));
                }
                String value = fieldMapper.apply(annotation.value());
                if (int.class.isAssignableFrom(p.getType())) {
                    return Integer.parseInt(value);
                } else if (boolean.class.isAssignableFrom(p.getType())) {
                    return Boolean.parseBoolean(value);
                } else if (OffsetDateTime.class.isAssignableFrom(p.getType())) {
                    return OffsetDateTime.parse(value);
                } else {
                    return value;
                }
            }).toArray();

            Object output = method.invoke(this, inputs);
            response.setStatus(HttpServletResponse.SC_OK);
            if (Integer.class.isAssignableFrom(output.getClass())) {
                response.getWriter().println(JSONValue.toJSONString(output));
            } else if (Collection.class.isAssignableFrom(output.getClass())) {
                List<HashMap<String, Object>> outputList = ((Collection<?>)output).stream().map(this::getOutputMap).collect(Collectors.toList());
                response.getWriter().println(JSONArray.toJSONString(outputList));
            } else {
                HashMap<String, Object> outputMap = getOutputMap(output);
                response.getWriter().println(JSONObject.toJSONString(outputMap));
            }
        } catch (IllegalAccessException | InvocationTargetException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Function<String, String> getFieldMapper(HttpServletRequest request) {
        if (request.getMethod().equals(HttpMethod.GET.name())) {
            return request::getParameter;
        } else if (request.getMethod().equals(HttpMethod.POST.name()) ||
                request.getMethod().equals(HttpMethod.PUT.name()) ||
                request.getMethod().equals(HttpMethod.DELETE.name())) {
            Map<String, String> body = RequestHelper.getBodyAsMap(request);
            return body::get;
        } else {
            throw new RuntimeException(String.format("HttpMethod %s not supported", request.getMethod()));
        }
    }

    private String getFieldKey(String methodGetter) {
        StringBuilder sb = new StringBuilder();
        String methodNameWithoutPrefix = methodGetter.substring(GETTER_PREFIX.length());
        sb.append(methodNameWithoutPrefix.substring(0,1).toLowerCase())
          .append(methodNameWithoutPrefix.substring(1));
        return sb.toString();
    }

    private HashMap<String, Object> getOutputMap(Object obj) {
        HashMap<String, Object> outputMap = new HashMap<>();
        Arrays.stream(obj.getClass().getDeclaredMethods())
              .filter(m -> m.getName().startsWith(GETTER_PREFIX))
              .sorted(Comparator.comparing(Method::getName))
              .forEach(m -> {
                  String key = getFieldKey(m.getName());
                  try {
                      Object value = m.invoke(obj);
                      if (List.class.isAssignableFrom(value.getClass())) {
                          List<?> valueList = (List<?>) value;
                          List<HashMap<String, Object>> valueMapList = valueList.stream().map(this::getOutputMap).collect(Collectors.toList());
                          outputMap.put(key, valueMapList);
                      } else {
                          outputMap.put(key, value);
                      }
                  } catch (IllegalAccessException | InvocationTargetException e) {
                      throw new RuntimeException(String.format("Failed to get value from field %s", m.getName()));
                  }
              });
        return outputMap;
    }
}
