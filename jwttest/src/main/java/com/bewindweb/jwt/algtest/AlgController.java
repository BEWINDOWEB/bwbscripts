package com.bewindweb.jwt.algtest;

import cn.hutool.core.util.RandomUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@RestController
@Slf4j
public class AlgController {

    private static final int ENCODING = 0;
    private static final int DECODING = 1;

    @GetMapping(value = "/test")
    public void test(@RequestParam(required = false) String algo, @RequestParam(required = false) Integer hashLen,
                     @RequestParam(required = false) Integer secretKeyBitLen, @RequestParam(required = false) Integer times) throws IOException {
        if (algo.equals("hmac")) {
            hmac(hashLen, secretKeyBitLen, times);
        } else if (algo.equals("rsa")) {
            rsa(hashLen, secretKeyBitLen, times);
        } else if (algo.equals("ecdsa")) {
            ec(hashLen, times);
        } else {
            log.warn("not support");
        }
    }

    private void hmac(Integer hashLen, Integer secretKeyBitLen, Integer times) {

        String alg = "";
        if (hashLen == 256) {
            alg = "HS256";
        } else if (hashLen == 384) {
            alg = "HS384";
        } else if (hashLen == 512) {
            alg = "HS512";
        } else {
            log.warn("not support {}", hashLen);
        }
        Map<String, Object> header = genHeader(alg);

        String secret = RandomUtil.randomString(secretKeyBitLen/8);

        Algorithm algorithm = null;
        if (hashLen == 256) {
            algorithm = Algorithm.HMAC256(secret);
        } else if (hashLen == 384) {
            algorithm = Algorithm.HMAC384(secret);
        } else if (hashLen == 512) {
            algorithm = Algorithm.HMAC512(secret);
        } else {
            log.warn("not support {}", hashLen);
        }

        testAlgo(ENCODING, header, algorithm, alg, null, hashLen, secretKeyBitLen, times);
        String jwt = genTokenOnce(header, algorithm);
        testAlgo(DECODING, header, algorithm, alg, jwt, hashLen, secretKeyBitLen, times);
    }

    private void rsa(Integer hashLen, Integer secretKeyBitLen, Integer times) throws IOException {

        // select header
        String alg = "";
        if (hashLen == 256) {
            alg = "RSA256";
        } else if (hashLen == 384) {
            alg = "RSA384";
        } else if (hashLen == 512) {
            alg = "RSA512";
        } else {
            log.warn("not support {}", hashLen);
        }
        Map<String, Object> header = genHeader(alg);

        // choose pem file
        String rsaPubKeyPath = "";
        String rsaPriKeyPath = "";
        if (secretKeyBitLen == 512) {
            rsaPubKeyPath = "RSA_512_pub.txt";
            rsaPriKeyPath = "RSA_512_pri.txt";
        } else if (secretKeyBitLen == 1024) {
            rsaPubKeyPath = "RSA_1024_pub.txt";
            rsaPriKeyPath = "RSA_1024_pri.txt";
        } else if (secretKeyBitLen == 2048) {
            rsaPubKeyPath = "RSA_2048_pub.txt";
            rsaPriKeyPath = "RSA_2048_pri.txt";
        } else {
            log.warn("not support {}", hashLen);
        }
        RSAPublicKey publicKey = (RSAPublicKey) PemUtils.readPublicKeyFromFile(rsaPubKeyPath, "RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) PemUtils.readPrivateKeyFromFile(rsaPriKeyPath, "RSA");

        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);

        testAlgo(ENCODING, header, algorithm, alg, null, hashLen, secretKeyBitLen, times);
        String jwt = genTokenOnce(header, algorithm);
        testAlgo(DECODING, header, algorithm, alg, jwt, hashLen, secretKeyBitLen, times);

    }

    private void ec(Integer hashLen, Integer times) throws IOException {

        // select header
        String alg = "";
        if (hashLen == 256) {
            alg = "ES256";
        } else if (hashLen == 384) {
            alg = "ES384";
        } else if (hashLen == 512) {
            alg = "ES512";
        } else {
            log.warn("not support {}", hashLen);
        }
        Map<String, Object> header = genHeader(alg);

        // choose pem file
        String rsaPubKeyPath = "ECDSA_p256_pub.txt";
        String rsaPriKeyPath = "ECDSA_p256_pri.txt";
        ECPublicKey publicKey = (ECPublicKey) PemUtils.readPublicKeyFromFile(rsaPubKeyPath, "EC");
        ECPrivateKey privateKey = (ECPrivateKey) PemUtils.readPrivateKeyFromFile(rsaPriKeyPath, "EC");
        Algorithm algorithm = Algorithm.ECDSA256(publicKey, privateKey);

        testAlgo(ENCODING, header, algorithm, alg, null, hashLen, 0, times);
        String jwt = genTokenOnce(header, algorithm);
        testAlgo(DECODING, header, algorithm, alg, jwt, hashLen, 0, times);
    }




    private Map<String, Object> genHeader(String alg) {
        Map<String, Object> header = new HashMap<>();
        header.put("alg", alg);
        header.put("typ", "JWT");
        header.put("kid", "randomKID");
        return header;
    }

    private long genToken(Map<String, Object> header, Algorithm algorithm) {
        Date nowDate = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(nowDate);
        cal.add(Calendar.SECOND, 86400);
        Date expireDate = cal.getTime();

        long startTime = System.currentTimeMillis();
        String token = JWT.create()
                .withHeader(header)
                .withIssuer("issuer")
                .withAudience("audience")
                .withSubject("clientid123")
                .withIssuedAt(nowDate)
                .withExpiresAt(expireDate)
                .withClaim("url", "www.bewindoweb.com")
                .sign(algorithm);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private String genTokenOnce(Map<String, Object> header, Algorithm algorithm) {
        Date nowDate = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(nowDate);
        cal.add(Calendar.SECOND, 86400);
        Date expireDate = cal.getTime();

        String token = JWT.create()
                .withHeader(header)
                .withIssuer("issuer")
                .withAudience("audience")
                .withSubject("clientid123")
                .withIssuedAt(nowDate)
                .withExpiresAt(expireDate)
                .withClaim("url", "www.bewindoweb.com")
                .sign(algorithm);
        log.info("signlen={}, token for {} is {}", token.split("\\.")[2].length(), algorithm.getName(), token);
        return token;
    }

    private long verifyToken(String jwt, Algorithm algorithm) {
        long startTime = System.currentTimeMillis();
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(jwt);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private void testAlgo(int mode, Map<String, Object> header, Algorithm algorithm, String name, String jwt,
                          Integer hashLen, Integer secretKeyBitLen, Integer times) {
        long costTime = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            if (mode == 0) {
                costTime += genToken(header, algorithm);
            } else {
                costTime += verifyToken(jwt, algorithm);
            }
        }
        String modeStr = mode == ENCODING ? "encoding" : "decoding";
        log.info("{} {}, hashLen={}, secretKeyBitLen={}, startTime={}, costTime={}", name, modeStr, hashLen,
                secretKeyBitLen, startTime, 1.0*costTime/ times);
    }


}
