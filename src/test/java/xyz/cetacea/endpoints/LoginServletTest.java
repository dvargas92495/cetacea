package xyz.cetacea.endpoints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.mocks.MockGoogleClient;
import xyz.cetacea.queries.UsersQueries;
import xyz.cetacea.util.DependencyManager;

import javax.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginServletTest extends ServletTest {

    private static LoginServlet loginServlet;

    BaseServlet getServlet() { return loginServlet; }

    @BeforeEach
    void setup() {
        DependencyManager.setGoogleClient(new MockGoogleClient());
        loginServlet = new LoginServlet();
    }

    @Test
    void testCreateUser() throws ServletException {
        user = loginServlet.createUser(getRandomString(32));
        assertNotNull(user);

        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testCreateUserMultipleTimesFail() throws ServletException {
        String idToken = getRandomString(32);
        user = loginServlet.createUser(idToken);
        assertThrows(RuntimeException.class, () -> loginServlet.createUser(idToken));

        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testLoginUser() throws ServletException {
        givenUser();
        Users loggedInUser = loginServlet.loginUser(user.getOauthId());
        assertEquals(user, loggedInUser);

        UsersQueries.deleteUserById(user.getId());
    }

    @Test
    void testLoginUserDoesNotExist() {
        assertThrows(RuntimeException.class, () -> loginServlet.loginUser(getRandomString(32)));
    }
}
