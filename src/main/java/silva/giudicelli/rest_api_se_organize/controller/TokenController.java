package silva.giudicelli.rest_api_se_organize.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import silva.giudicelli.rest_api_se_organize.controller.request.LoginRequest;
import silva.giudicelli.rest_api_se_organize.controller.response.LoginResponse;
import silva.giudicelli.rest_api_se_organize.model.Role;
import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.repository.UserRepository;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder,
                           JwtDecoder jwtDecoder,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado."));

        if (!user.isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new BadCredentialsException("Senha inválida!");
        }

        return ResponseEntity.ok(createLoginResponse(user));
    }
    
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        try {
            Jwt jwt = jwtDecoder.decode(token);
            
            return ResponseEntity.ok(Map.of(
                "valid", true,
                "subject", jwt.getSubject(),
                "expiresAt", jwt.getExpiresAt()
            ));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("valid", false, "message", e.getMessage()));
        }
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        
        Jwt jwt = jwtDecoder.decode(refreshToken);
        Long userId = Long.parseLong(jwt.getSubject());
        
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("Token inválido ou usuário inexistente."));

        return ResponseEntity.ok(createLoginResponse(user));
    }

    private LoginResponse createLoginResponse(User user) {
        var now = Instant.now();
        var accessExpiresIn = 3600L;
        var refreshExpiresIn = 604800L;

        var scopes = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var accessClaims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(accessExpiresIn))
                .claim("scope", scopes)
                .build();

        var refreshClaims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(refreshExpiresIn))
                .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(accessClaims)).getTokenValue();
        String refreshToken = jwtEncoder.encode(JwtEncoderParameters.from(refreshClaims)).getTokenValue();

        return new LoginResponse(accessToken, accessExpiresIn, refreshToken, user);
    }
}