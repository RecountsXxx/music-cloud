package org.deus.src.services;

import org.deus.src.exceptions.data.DataProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class GravatarService {
    private static final String GRAVATAR_URL = "https://www.gravatar.com/avatar/";

    @Value("${gravatar.extension}")
    private String gravatarExtension;
    @Value("${gravatar.size}")
    private String gravatarSize;
    @Value("${gravatar.default.template}")
    private String gravatarDefaultTemplate;

    public String getGravatarUrl(String email) throws DataProcessingException {
        String hash;

        try {
            hash = this.getMd5Hash(email.trim().toLowerCase());
        } catch (NoSuchAlgorithmException e) {
            throw new DataProcessingException(e);
        }

        return GRAVATAR_URL + hash + gravatarExtension + "?s=" + gravatarSize + "&d=" + gravatarDefaultTemplate;
    }

    private String getMd5Hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());

        byte[] digest = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
    }
}