package com.example;
import java.security.MessageDigest;

// hash's a string using SHA-256
public class SHA256HashingUtil {
  public static String applyHash(String input) {

    try {
      // returns messageDigest object that implements SHA-256
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      // performs hash
      byte[] hashBtye = digest.digest(input.getBytes());
      // converts bytex to hex format, will contain our hash
      StringBuilder hexString = new StringBuilder();

      for(byte b : hashBtye) {
        String hex = Integer.toString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
