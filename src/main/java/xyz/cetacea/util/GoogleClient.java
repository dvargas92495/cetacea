package xyz.cetacea.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleClient implements IGoogleClient {
    private static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
            .setAudience(Collections.singletonList(Environment.GOOGLE_CLIENT))
            .build();
    @Override
    public Payload verify(String idToken) throws GeneralSecurityException, IOException {
        return verifier.verify(idToken).getPayload();
    }
}
