package xyz.cetacea;

import org.joda.time.DateTime;

import java.util.Random;

public class CetaceaTest {
    private static final Random random = new Random(DateTime.now().getMillis());
    private static final int MIN_VALUE = 48;
    private static final int MAX_VALUE = 122;

    protected static String FIRST_NAME = getRandomString(8);
    protected static String LAST_NAME = getRandomString(12);
    protected static String USER_EMAIL = getRandomString(16).toLowerCase();
    protected static String OAUTH_ID = getRandomString(20);
    protected static String GROUP_NAME = getRandomString(8);
    protected static String GROUP_DESCRIPTION = getRandomString(16);
    protected static String ENTRY = getRandomString(32);

    protected static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = (char) (random.nextInt(MAX_VALUE-MIN_VALUE)+MIN_VALUE);
            sb.append(c);
        }
        return sb.toString();
    }

    protected static boolean getRandomBoolean() {
        return random.nextBoolean();
    }
}
