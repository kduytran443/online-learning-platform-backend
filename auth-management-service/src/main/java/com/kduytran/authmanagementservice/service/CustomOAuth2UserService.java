package com.kduytran.authmanagementservice.service;

import com.kduytran.authmanagementservice.entity.CustomOAuth2User;
import com.kduytran.authmanagementservice.entity.RoleEntity;
import com.kduytran.authmanagementservice.entity.UserEntity;
import com.kduytran.authmanagementservice.exception.ResourceNotFoundException;
import com.kduytran.authmanagementservice.repository.RoleRepository;
import com.kduytran.authmanagementservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = delegate.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");

        UserEntity user = userRepository.findByEmail(email).orElseGet(() -> {
            RoleEntity role = roleRepository.findByName("USER")
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            String registrationId = userRequest.getClientRegistration().getRegistrationId();

            return userRepository.save(UserEntity.builder()
                    .email(email)
                    .username(email)
                    .name(oauth2User.getAttribute("name"))
                    .roles(Set.of(role))
                    .oauthId(registrationId)
                    .build());
        });

        return new CustomOAuth2User(user, oauth2User.getAuthorities());
    }
}
