package com.kduytran.authmanagementservice.handler;

import com.kduytran.authmanagementservice.entity.UserEntity;
import com.kduytran.authmanagementservice.exception.ResourceNotFoundException;
import com.kduytran.authmanagementservice.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String userId = authentication.getName();
//        UserEntity user = userRepository.findById(Integer.parseInt(userId)).orElseThrow(
//                () -> new ResourceNotFoundException("User with id [%d] not found".formatted(Integer.parseInt
//                (userId))));
//
//        List<String> roles = new ArrayList<>();
//        user.getRoles().forEach(role -> roles.add(role.getName()));
//
//        String token = jwtUtil.issueToken(String.valueOf(user.getId()), roles);
//
//        String refreshToken = jwtUtil.issueRefreshToken(String.valueOf(user.getId()), roles);
//
//        try {
//            jwtUtil.verifyToken(token);
//        } catch (ParseException | JOSEException e) {
//            throw new RuntimeException(e);
//        }
//
//        Cookie accessTokenCookie = new Cookie("accessToken", token);
//        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setSecure(false); // true in production
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(10 * 60); // 10 minutes
//
//        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(false); // true in production
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);
//
//        response.addCookie(accessTokenCookie);
//        response.addCookie(refreshTokenCookie);
//
//        response.sendRedirect("<http://localhost:3000>");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
