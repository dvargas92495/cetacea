package xyz.cetacea.queries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.cetacea.data.tables.pojos.Users;
import xyz.cetacea.util.Repository;

import javax.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersQueriesTest {

    @BeforeEach
    void setup()
    {
        Repository.host = "jdbc:postgresql://localhost:5432/postgres";
        Repository.password = "";
    }

    @Test
    void test1() throws ServletException {
        Users createdUser = UsersQueries.createUser("first", "last", "first.last@gmail.com", "oauthId");
        assertEquals("first", createdUser.getFirstName());
    }

}

