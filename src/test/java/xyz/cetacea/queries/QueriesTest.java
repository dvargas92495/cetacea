package xyz.cetacea.queries;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeAll;
import xyz.cetacea.util.Repository;

import java.nio.charset.Charset;
import java.util.Random;

public class QueriesTest {

    private static Random random = new Random(DateTime.now().getMillis());

    @BeforeAll
    protected static void setupAll() {
        Repository.host = "jdbc:postgresql://localhost:5432/postgres";
        Repository.password = "";
    }

    static String getRandomString(int length) {
        byte[] byteArray = new byte[length];
        random.nextBytes(byteArray);
        return new String(byteArray, Charset.forName("UTF-8"));
    }
}
