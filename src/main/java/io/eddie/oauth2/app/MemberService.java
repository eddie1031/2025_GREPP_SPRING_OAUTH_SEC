package io.eddie.oauth2.app;

import io.eddie.oauth2.dao.MemberRepository;
import io.eddie.oauth2.domain.Member;
import io.eddie.oauth2.dto.MemberDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String provider = userRequest.getClientRegistration().getRegistrationId();
        log.info("userRequest = {}", provider);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User = {}", oAuth2User);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String findName = attributes.get("name").toString();
        String email = attributes.get("email").toString();

        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        Member member = memberOptional.orElseGet(
                () -> {
                    Member saved = Member.builder()
                            .name(findName)
                            .email(email)
                            .provider(provider)
                            .build();
                    return memberRepository.save(saved);
                }
        );

        return MemberDetails.builder()
                .name(member.getName())
                .role(member.getRole())
                .attributes(attributes)
                .build();
    }

}
