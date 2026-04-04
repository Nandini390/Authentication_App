package org.example.authapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.authapp.Repositories.RoleRepository;
import org.example.authapp.Repositories.UserRepository;
import org.example.authapp.Repositories.refreshTokenRepository;
import org.example.authapp.config.AppConstants;
import org.example.authapp.entities.Provider;
import org.example.authapp.entities.RefreshToken;
import org.example.authapp.entities.Role;
import org.example.authapp.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final CookieService cookieService;
    private final refreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;

    @Value("${app.auth.frontend.success-redirect}")
    private String frontendSuccessUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
         logger.info("success authentication");
         logger.info(authentication.toString());

        OAuth2User OAuth2User=(OAuth2User) authentication.getPrincipal();
        String registrationId="unknown";
        if(authentication instanceof OAuth2AuthenticationToken token){
            registrationId=token.getAuthorizedClientRegistrationId();
        }
        logger.info("registrationId: "+registrationId);
        logger.info("user: "+OAuth2User.getAttributes().toString());

        User user;
        switch (registrationId){
            case "google"-> {
                String googleId = OAuth2User.getAttributes().getOrDefault("sub", "").toString();
                String email = OAuth2User.getAttributes().getOrDefault("email", "").toString();
                String name = OAuth2User.getAttributes().getOrDefault("name", "").toString();
                String picture = OAuth2User.getAttributes().getOrDefault("picture", "").toString();
                User newUser = User.builder()
                        .email(email)
                        .name(name)
                        .image(picture)
                        .enable(true)
                        .provider(Provider.GOOGLE)
                        .build();
                //add role to Oauth user
                Role role= roleRepository.findByName("ROLE_"+ AppConstants.GUEST_ROLE).orElse(null);
                if(newUser.getRoles() == null){
                    newUser.setRoles(new HashSet<>());
                }
                newUser.getRoles().add(role);
               user=userRepository.findByEmail(email).orElseGet(()->userRepository.save(newUser));
            }
            default->{
                throw new RuntimeException("Invalid Registration Id");
            }
        }

        String jti= UUID.randomUUID().toString();
        RefreshToken refreshTokenOb =RefreshToken.builder()
                .jti(jti)
                .user(user)
                .revoked(false)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .build();
        refreshTokenRepository.save(refreshTokenOb);
        String accessToken= jwtService.generateAccessToken(user);
        String refreshToken= jwtService.generateRefreshToken(user,refreshTokenOb.getJti());
        cookieService.attachRefreshCookie(response,refreshToken,(int)jwtService.getRefreshTtlSeconds());

//         response.getWriter().write("Login successful");
        response.sendRedirect(frontendSuccessUrl);
    }
}
