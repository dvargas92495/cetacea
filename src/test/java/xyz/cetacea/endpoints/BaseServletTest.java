package xyz.cetacea.endpoints;

import org.eclipse.jetty.http.HttpMethod;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import xyz.cetacea.CetaceaTest;
import xyz.cetacea.mocks.MockHttpServletRequest;
import xyz.cetacea.mocks.MockHttpServletResponse;
import xyz.cetacea.util.Endpoint;
import xyz.cetacea.util.Param;

import javax.servlet.http.HttpServletResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BaseServletTest extends CetaceaTest {

    public static class MockServlet extends BaseServlet {
        @Endpoint(HttpMethod.GET)
        public MockResponse get(@Param("intParam") int intParam, @Param("date_time") OffsetDateTime dateTime, @Param("stringParam") String stringParam, @Param("booleanParam") boolean booleanParam) {
            return new MockResponse(intParam, dateTime, stringParam, booleanParam);
        }
        @Endpoint(HttpMethod.POST)
        public MockResponse post(@Param("intParam") int intParam, @Param("date_time") OffsetDateTime dateTime, @Param("stringParam") String stringParam, @Param("booleanParam") boolean booleanParam) {
            return new MockResponse(intParam, dateTime, stringParam, booleanParam);
        }
        @Endpoint(HttpMethod.PUT)
        public List<MockResponse> put(@Param("intParam") int intParam, @Param("date_time") OffsetDateTime dateTime, @Param("stringParam") String stringParam, @Param("booleanParam") boolean booleanParam) {
            return Collections.singletonList(new MockResponse(intParam, dateTime, stringParam, booleanParam));
        }
        @Endpoint(HttpMethod.DELETE)
        public int delete(@Param("intParam") int intParam) {
            return intParam;
        }
    }

    @SuppressWarnings("unused")
    public static class MockResponse {
        private int intParam;
        private boolean booleanParam;
        private String stringParam;
        private OffsetDateTime offsetDateTime;
        private List<SubClassResponse> subClassResponsesParam;
        MockResponse(int intParam, OffsetDateTime dateTime, String stringParam, boolean booleanParam) {
            this.intParam = intParam;
            this.booleanParam = booleanParam;
            this.stringParam = stringParam;
            this.offsetDateTime = dateTime;
            this.subClassResponsesParam = Arrays.asList(new SubClassResponse(), new SubClassResponse());
        }
        public class SubClassResponse {
            private int innerInt;
            private String innerString;
            SubClassResponse() {
                innerInt = 2;
                innerString = "Inner";
            }
            public int getInnerInt() { return innerInt; }
            public String getInnerString() { return innerString; }
        }
        public int getIntParam() { return intParam; }
        public boolean getBooleanParam() { return booleanParam; }
        public String getStringParam() { return stringParam; }
        public OffsetDateTime getOffsetDateTime() { return offsetDateTime; }
        public List<SubClassResponse> getSubClassResponsesParam() { return subClassResponsesParam; }
    }

    private static MockServlet mockServlet;
    private static MockHttpServletRequest request;
    private static MockHttpServletResponse response;
    private static final String EXPECTED =
            "{\"stringParam\":\"Value\","+
            "\"booleanParam\":true,"+
            "\"offsetDateTime\":\"2019-02-10T15:34Z\","+
            "\"intParam\":5,"+
            "\"subClassResponsesParam\":[{\"innerInt\":2,\"innerString\":\"Inner\"},{\"innerInt\":2,\"innerString\":\"Inner\"}]}";

    @BeforeAll
    static void setupAll() {
        mockServlet = new MockServlet();
    }

    @Test
    void testGet() {
        givenMethod(HttpMethod.GET);
        mockServlet.doGet(request, response);
        verifyContent(EXPECTED);
    }

    @Test
    void testPost() {
        givenMethod(HttpMethod.POST);
        mockServlet.doPost(request, response);
        verifyContent(EXPECTED);
    }

    @Test
    void testPut() {
        givenMethod(HttpMethod.PUT);
        mockServlet.doPut(request, response);
        verifyContent(String.format("[%s]", EXPECTED));
    }

    @Test
    void testDelete() {
        givenMethod(HttpMethod.DELETE);
        mockServlet.doDelete(request, response);
        assertEquals("5", response.getContent());
    }

    private void givenMethod(HttpMethod method)
    {
        request = new MockHttpServletRequest(method);
        request.setAttribute("intParam", 5);
        request.setAttribute("stringParam", "Value");
        request.setAttribute("booleanParam", true);
        request.setAttribute("date_time", OffsetDateTime.of(2019, 2, 10, 15, 34, 0, 0, ZoneOffset.UTC));
        response = new MockHttpServletResponse();
    }

    private void verifyContent(String expected)
    {
        assertEquals(expected, response.getContent());
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
}
