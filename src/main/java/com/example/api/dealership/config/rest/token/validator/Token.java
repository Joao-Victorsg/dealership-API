package com.example.api.dealership.config.rest.token.validator;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Token {

    //jwtToken = base64(header) + "." + base64(payload) + "." + signature;
    private static final String SECRET_KEY = "VASCO_SECRET_KEY";
    private static final String ISSUER = "vasco";
    private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private JSONObject payload = new JSONObject();
    private String signature; //Signing uses hmacsh256(base64(header) + "." base64(payload), secret);
    private String encodedHeader;

    private Token(){
        encodedHeader = encode(new JSONObject(JWT_HEADER));
    }

    public Token(JSONObject payload){
        this(payload.getString("sub"), payload.getLong("exp"));
    }

    public Token(String user, Long expiration){
        this();
        payload.put("sub",user);
        payload.put("exp",expiration);
        payload.put("iat", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        payload.put("iss",ISSUER);
        this.signature = hmacSha256(encodedHeader + "." + encode(payload), SECRET_KEY);
    }

    private static String encode(JSONObject jsonObject){
        return encode(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String encode(byte[] bytes){
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public Token(String token) throws NoSuchAlgorithmException {
        this();

        final var parts = token.split("\\.");

        if(parts.length != 3)
            throw new IllegalArgumentException("Invalid Token format");

        if(encodedHeader.equals(parts[0])){
            encodedHeader = parts[0];
        }else{
            throw new NoSuchAlgorithmException("Token Header is Incorrect: " + parts[0]);
        }

        payload = new JSONObject(decode(parts[1]));
        if(payload.isEmpty())
            throw new JSONException("Payload is empty: ");

        if(!payload.has("exp"))
            throw new JSONException("Payload doesn't contain expiration time " + payload);

        signature = parts[2];
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }

    public boolean isValid() {
        return payload.getLong("exp") > (LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) //token not expired
                && signature.equals(hmacSha256(encodedHeader + "." + encode(payload), SECRET_KEY)); //signature matched
    }

    public String getSubject() {
        return payload.getString("sub");
    }

    @Override
    public String toString() {
        return encodedHeader + "." + encode(payload) + "." + signature;
    }

    private String hmacSha256(String data, String secret) {
        try {

            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);

            final var sha256Hmac = Mac.getInstance("HmacSHA256");
            final var secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);

            final var signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encode(signedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }


}
