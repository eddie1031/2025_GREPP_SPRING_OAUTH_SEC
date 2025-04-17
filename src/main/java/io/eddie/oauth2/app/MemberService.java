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

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User = {}", oAuth2User);

        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        MemberDetails memberDetails = MemberDetailsFactory.memberDetails(provider, oAuth2User);

        Optional<Member> memberOptional = memberRepository.findByEmail(memberDetails.getEmail());

        Member findMember = memberOptional.orElseGet(
                () -> {
                    Member saved = Member.builder()
                            .name(memberDetails.getName())
                            .email(memberDetails.getEmail())
                            .provider(provider)
                            .build();
                    return memberRepository.save(saved);
                }
        );

        if ( findMember.getProvider().equals(provider) ) {
            return memberDetails.setRole(findMember.getRole());
        } else {
            throw new RuntimeException();
        }

    }

}
