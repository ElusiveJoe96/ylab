//package ru.ylab.auth;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import ru.ylab.domain.model.User;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.time.Duration;
//import java.util.Date;
//
///**
// * Service for handling JSON Web Token (JWT) operations.
// * <p>
// * This service provides methods for generating and validating JWT tokens used for user authentication.
// * The token contains user details such as name, role, and user ID, and it has an expiration time.
// * </p>
// */
//@Service
//public class JwtService {
//
//    /**
//     * The secret key used for signing JWT tokens.
//     * <p>
//     * This key is injected from application properties and is used to create a {@link SecretKey}
//     * instance for signing and verifying JWT tokens.
//     * </p>
//     */
//    @Value("${secret.key}")
//    private String SECRET_KEY;
//
//    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//    private final long EXPIRATION_TIME = Duration.ofHours(1).toMillis();
//
//    /**
//     * Generates a JWT token for the given user.
//     * <p>
//     * The generated token includes the user's name, role, and ID as claims. The token is signed with
//     * a secret key and includes an expiration time.
//     * </p>
//     *
//     * @param user the user for whom the token is generated
//     * @return the generated JWT token as a {@link String}
//     */
//    public String generateToken(User user) {
//        return Jwts.builder()
//                .setSubject(user.getName())
//                .claim("role", user.getRole().name())
//                .claim("userId", user.getId())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(key)
//                .compact();
//    }
//
//    /**
//     * Validates a JWT token and extracts its claims.
//     * <p>
//     * This method parses the token, verifies its signature using the secret key, and returns the claims
//     * contained in the token. If the token is invalid or expired, an exception will be thrown.
//     * </p>
//     *
//     * @param token the JWT token to validate
//     * @return the claims extracted from the token as a {@link Claims} object
//     */
//    public Claims validateToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
