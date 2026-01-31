package com.example.springwebproject.config.auth;

import com.example.springwebproject.config.auth.dto.OAuthAttributes;
import com.example.springwebproject.config.auth.dto.SessionUser;
import com.example.springwebproject.domain.user.User;
import com.example.springwebproject.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)
            throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
                new DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(request);

        // social 서비스 구분 (Google, Naver)
        String registrationId =
                request.getClientRegistration().getRegistrationId();

        // Google OAuth2 로그인 진행 시 키가 되는 필드값, 네이버/카카오는 지원 안함
        String userNameAttributeName =
                request.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        // User 속성 저장
        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId,
                userNameAttributeName,
                oAuth2User.getAttributes()
        );

        User user = saveOrUpdate(attributes);

        // SessionUser는 세션에 사용자 정보를 저장하기 위함
        // 왜 User클래스를 안쓰고 새로 만들어서 사용하는가 -> 생각해보기
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(user.getRoleKey())
                ),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity ->
                        entity.update(
                                attributes.getName(),
                                attributes.getPicture()
                        )
                )
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
