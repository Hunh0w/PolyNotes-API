package fr.hunh0w.polynotes.auth;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class GenerateToken {

    public static void exec() throws Exception {

        Instant expiration = new Date()
                .toInstant()
                .plus(Duration.ofHours(1));

        String token = Jwt.issuer("https://polynotes.fr/issuer")
                        .upn("vfont@polynotes.fr")
                        .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                        .claim(Claims.email, "2001-07-13")
                        .expiresAt(expiration)
                        .sign();
        System.out.println(token);
    }

}
