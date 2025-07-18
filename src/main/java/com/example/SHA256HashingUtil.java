package com.example;

import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

      for (byte b : hashBtye) {
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

  // creates a digital signature of the input string using the private key
  // converts message into a digital signature using the private key
  public static byte[] applyDigitalSig(PrivateKey privateKey, String input) {
    // provides functionality of of a digital signature algorithm
    Signature dsa;
    byte[] output = new byte[0];
    try {
      dsa = Signature.getInstance("ECDSA", "BC");
      dsa.initSign(privateKey); // initialises the object for signing
      byte[] strByte = input.getBytes();
      dsa.update(strByte);
      byte[] realSig = dsa.sign(); // creates the digital signature
      output = realSig;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return output;
  }

  // verifies if the digital signature is valid
  public static boolean verifyDigitalSig(PublicKey publicKey, String data, byte[] signature) {
    try {
      Signature verify = Signature.getInstance("ECDSA", "BC");
      verify.initVerify(publicKey); // initialises the object for verficiation
      verify.update(data.getBytes()); //
      return verify.verify(signature); // verifies the signature byte against the message and public key
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  public static String getStringFromKey(Key key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

  // calculates merkle root of a list of transactions
  public static String getMerkleRoot(List<Transaction> transactions) {
    // represents current layer of hashes
    List<String> prevLayer = new ArrayList<>();
    // goes through every transaction object in the transactions list
    // adds it transactionId to prevLayer
    for(Transaction transaction : transactions) {
      prevLayer.add(transaction.transactionId);
    }
    // keeps building layers until one hash left - the root
    while (prevLayer.size() > 1) {
      // if number of hashes is odd, duplicates last hash
      if (prevLayer.size() % 2 != 0) {
        prevLayer.add(prevLayer.get(prevLayer.size() - 1));
      }
      // holds hashes of the next upper layer
      List<String> treeLayer = new ArrayList<>();
      // for each pair, hashes the result and adds to treeLayer
      for(int i = 0; i < prevLayer.size(); i+= 2) {
        treeLayer.add(applyHash(prevLayer.get(i) + prevLayer.get(i + 1)));
      }
      // sets prevLayer to treeLayer
      // moves up one level
      prevLayer = treeLayer;
    }

    return prevLayer.size() == 1 ? prevLayer.get(0) : "";
  }
}
