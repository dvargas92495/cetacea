package xyz.cetacea.mocks;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import xyz.cetacea.util.IGoogleClient;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MockGoogleClient implements IGoogleClient {
    @Override
    public Payload verify(String idToken) throws GeneralSecurityException, IOException {
        Payload mockPayload = new Payload();
        mockPayload.set("given_name", "David");
        mockPayload.set("family_name", "Vargas");
        mockPayload.setSubject(idToken);
        mockPayload.setEmail("dvargas92495@gmail.com");
        return mockPayload;
    }
}
