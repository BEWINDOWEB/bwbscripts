package com.bewindweb.jwt.algtest;


import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class PemUtils {
    private static final String LINE = "-----";
    private static final String BEGIN = LINE + "BEGIN ";
    private static final String END = LINE + "END ";

    private static byte[] parsePEMFile(String pemFile) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(pemFile);
        InputStream inputStream = classPathResource.getInputStream();
        String fileContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        if (fileContent != null && fileContent.startsWith(BEGIN)) {
            fileContent = fileContent.substring(BEGIN.length());
            int index = fileContent.indexOf('-');
            String type = fileContent.substring(0, index);
            fileContent = fileContent.substring(index);
            if (index > 0) {
                fileContent = fileContent.replaceAll("\r", "");
                fileContent = fileContent.replaceAll("\n", "");
                fileContent = fileContent.replaceAll( END + type, "");
                fileContent = fileContent.replaceAll(LINE, "");
                return Base64.decode(fileContent);
            }
        }
        throw new RuntimeException("BAD pem file " + pemFile);
    }

    private static PublicKey getPublicKey(byte[] keyBytes, String algorithm) {
        PublicKey publicKey = null;
        try {
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            publicKey = kf.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not reconstruct the public key, the given algorithm could not be found.");
        } catch (InvalidKeySpecException e) {
            System.out.println("Could not reconstruct the public key");
        }

        return publicKey;
    }

    private static PrivateKey getPrivateKey(byte[] keyBytes, String algorithm) {
        PrivateKey privateKey = null;
        try {
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            privateKey = kf.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not reconstruct the private key, the given algorithm could not be found.");
        } catch (InvalidKeySpecException e) {
            System.out.println("Could not reconstruct the private key");
        }

        return privateKey;
    }

    public static PublicKey readPublicKeyFromFile(String filepath, String algorithm) throws IOException {
        byte[] bytes = PemUtils.parsePEMFile(filepath);
        return PemUtils.getPublicKey(bytes, algorithm);
    }

    public static PrivateKey readPrivateKeyFromFile(String filepath, String algorithm) throws IOException {
        byte[] bytes = PemUtils.parsePEMFile(filepath);
        return PemUtils.getPrivateKey(bytes, algorithm);
    }

}
