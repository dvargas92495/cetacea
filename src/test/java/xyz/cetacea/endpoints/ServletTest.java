package xyz.cetacea.endpoints;

import org.eclipse.jetty.http.HttpMethod;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.cetacea.CetaceaTest;
import xyz.cetacea.util.Endpoint;
import xyz.cetacea.util.Param;
import xyz.cetacea.util.Repository;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

abstract class ServletTest extends CetaceaTest {
    // TODO - delete this setup when Queries have been refactored to non static
    @BeforeAll
    protected static void setupAll() {
        Repository.host = "jdbc:postgresql://localhost:5432/postgres";
        Repository.password = "";
    }

    abstract BaseServlet getServlet();

    @Test
    void noPublicMethodsWithoutUniqueEndpointAnnotation() {
        List<Method> methods = getDeclaredPublicMethods();
        methods.forEach(m -> assertNotNull(m.getAnnotation(Endpoint.class),
                String.format("Method %s missing @Endpoint annotation", m.getName())));
        Set<HttpMethod> httpMethods = methods.stream().map(m -> m.getAnnotation(Endpoint.class).value()).collect(Collectors.toSet());
        assertEquals(methods.size(), httpMethods.size(), "Not all methods are annotated uniquely");
    }

    @Test
    void noEndpointMethodInputsWithoutParamAnnotation() {
        List<Method> methods = getDeclaredPublicMethods();
        methods.forEach(m -> Arrays.stream(m.getParameters()).forEach(p -> assertNotNull(p.getAnnotation(Param.class),
                String.format("Input %s missing @Param annotation", p.getName()))));
    }

    private List<Method> getDeclaredPublicMethods() {
        return Arrays.stream(getServlet().getClass().getDeclaredMethods())
                     .filter(m -> Modifier.isPublic(m.getModifiers()))
                     .collect(Collectors.toList());
    }
}
