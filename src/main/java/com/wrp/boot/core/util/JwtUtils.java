package com.wrp.boot.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wrp.boot.core.context.UserInfo;
import com.wrp.boot.core.exception.BusinessException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

/**
 * @author wrp
 * @since 2024年10月14日 13:59
 */
@Component
public class JwtUtils {
    @Value("${secret.current}")
    private String SECRET;

    private static final String AUTH_PREFIX = "Bearer ";

    @Resource
    private ObjectMapper objectMapper;

    public String getTokenFromRequest(HttpServletRequest request) {
        String token;
        if (null != (token = request.getHeader(HttpHeaders.AUTHORIZATION))) {
            return token;
        } else if (null != (token = request.getParameter(HttpHeaders.AUTHORIZATION))) {
            return token;
        } else {
            return null;
        }
    }

    public UserInfo parseUserInfo(String token) throws Exception {
        if (token == null || token.trim().isEmpty() || token.length() < 8 || !token.startsWith(AUTH_PREFIX)) {
            return null;
        }
        token = token.replaceFirst(AUTH_PREFIX, "");
        String[] parts = token.split("\\.");
        if (parts.length != 2) {
            return null;
        }
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        hmacSHA256.init(new SecretKeySpec(SECRET.getBytes(), 0, SECRET.getBytes().length, "HmacSHA256"));
        byte[] bs = hmacSHA256.doFinal(parts[0].getBytes(StandardCharsets.UTF_8));
        String base1 = Base64.getEncoder().encodeToString(bs);
        String base2 = Base64.getEncoder().encodeToString(base1.getBytes(StandardCharsets.UTF_8));
        String s = base2.replaceAll("=", "").replace("\n", "");
        if (!parts[1].equals(s)) {
            return null;
        }
        String str = deMix(parts[0]);
        StringBuilder newstr = new StringBuilder(str.replace('-', '+').replace('_', '/'));
        int rem = newstr.length() % 4;
        if (rem != 0) {
            newstr.append("=".repeat(4 - rem));
        }
        byte[] bytes = Base64.getDecoder().decode(newstr.toString().getBytes(StandardCharsets.UTF_8));
        String user = new String(bytes, StandardCharsets.UTF_8);
        return objectMapper.readValue(user,UserInfo.class);
    }

    private String deMix(String str) {
        char[] arr = str.toCharArray();
        int n = arr[0] - 'a';

        char[] l = new char[arr.length - 1];
        for (int i = 1; i < arr.length; i++) {
            char c = arr[i];
            if (c <= 'Z' && c >= 'A') {
                int v = c - n;
                l[i - 1] = (char) (v < 'A' ? v + 26 : v);
            } else if (c <= 'z' && c >= 'a') {
                int v = c - n;
                l[i - 1] = (char) (v < 'a' ? v + 26 : v);
            } else if (c <= '9' && c >= '0') {
                int v = c - n % 10;
                l[i - 1] = (char) (v < '0' ? v + 10 : v);
            } else {
                l[i - 1] = c;
            }
        }

        return String.valueOf(l);
    }

    public String generateToken(UserInfo userInfo) {
        try {
            return AUTH_PREFIX + encrypt(objectMapper.writeValueAsString(userInfo));
        } catch (NoSuchAlgorithmException e) {
            throw BusinessException.of("生成token出错" + e.getMessage());
        } catch (InvalidKeyException e) {
            throw BusinessException.of("生成token出错:" + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String encrypt(String str) throws NoSuchAlgorithmException, InvalidKeyException {
        String v = mix(urlEncode(str));

        byte[] keyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        byte[] messageBytes = v.getBytes(StandardCharsets.UTF_8);

        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        hmacSHA256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256"));

        return v + "." + urlEncode(Base64.getEncoder().encodeToString(hmacSHA256.doFinal(messageBytes)));
    }

    private String mix(String str) {
        Random r = new Random();
        int n = r.nextInt(25);
        char[] arr = str.toCharArray();
        char[] rr = new char[arr.length];

        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (c <= 'Z' && c >= 'A') {
                int v = c + n;
                rr[i] = (char) (v > 'Z' ? v - 26 : v);
            } else if (c <= 'z' && c >= 'a') {
                int v = c + n;
                rr[i] = (char) (v > 'z' ? v - 26 : v);
            } else if (c <= '9' && c >= '0') {
                int v = c + n % 10;
                rr[i] = (char) (v > '9' ? v - 10 : v);
            } else {
                rr[i] = c;
            }
        }
        return (char) (n + 'a') + new String(rr);
    }

    private String urlEncode(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        String strn = Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
        strn = strn.replace('+', '-').replace('/', '_').replace("=", "");
        return strn.replaceAll("\n", "");
    }

}