package com.example;


import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
  public PublicKey publicKey; // will act as address
  public PrivateKey privateKey;

  public Wallet() {
    generateKeyPair();
  }

  // generates pairs of public and private keys
  // uses Elliptic-curve cryptography algorithm
  private void generateKeyPair() {
    try {
      // creates KeyPairGenerator object that will make public/private keys
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
    
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      //  The curve is defined over a prime field, with a prime number as the modulus
      ECGenParameterSpec eSpec = new ECGenParameterSpec("prime192v1");

      // initialise key generator and generate a keyPair
      keyPairGenerator.initialize(eSpec, random);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();

      privateKey = keyPair.getPrivate();
      publicKey = keyPair.getPublic();

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
