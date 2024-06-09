package com.common.poc;

import com.common.auth.SessionIdGenerator;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;

public class MinimalJwtGenerator {

    public static void main(String[] args) throws Exception {
        // Shared secret key (use a secure method to generate and store this key)

        // Print the JWT
        System.out.println("JWT: " + SessionIdGenerator.createSessionId("shriram"));
    }
}
