package xyz.cetacea.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IGoogleClient {
    Payload verify(String idToken) throws GeneralSecurityException, IOException;
}
