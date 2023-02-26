package fr.hunh0w.polynotes.auth;

import fr.hunh0w.polynotes.entities.User;
import fr.hunh0w.polynotes.ranks.BuiltinRank;
import fr.hunh0w.polynotes.ranks.Rank;
import io.smallrye.jwt.build.Jwt;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.Claims;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class TokenGenerator {

    public static String getToken(User user) {
        Instant expiration = new Date()
                .toInstant()
                .plus(Duration.ofHours(1));

        Rank rank = User.getRank(user);

        List<String> ranks = new ArrayList<>(Arrays.asList("User"));
        if(!rank.name.equalsIgnoreCase(BuiltinRank.USER.getName()))
            ranks.add(StringUtils.capitalize(rank
                    .name
                    .toLowerCase(Locale.ROOT)
            ));
        return Jwt.issuer("https://polynotes.fr/issuer")
                        .upn("vincent.font@etu.umontpellier.fr")
                        .groups(new HashSet<>(ranks))
                        .claim(Claims.given_name, user.firstname)
                        .claim(Claims.family_name, user.lastname)
                        .claim(Claims.email, user.email)
                        .expiresAt(expiration)
                        .sign();
    }

}
