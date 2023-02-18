package com.flab.modu.users.encoder;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    public String encrypt(String password) {
        try {
            byte[] salt = getSalt(password);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            byte[] hash = factory.generateSecret(spec).getEncoded();
            String encode = Base64.getEncoder().encodeToString(hash);
            System.out.println("encode = " + encode);
            return encode;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getSalt(String password) {
        String substring = password.substring(0, 8);
        StringBuilder sb = new StringBuilder();
        String saltString = sb.append(substring).append(substring).toString();
        return saltString.getBytes(StandardCharsets.UTF_8);
    }
}
