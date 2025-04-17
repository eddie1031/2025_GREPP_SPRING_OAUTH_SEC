package io.eddie.oauth2.app;

import io.eddie.oauth2.dto.MemberDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

public class MemberDetailsFactory {

    public static MemberDetails memberDetails(String provider, OAuth2User oAuth2User) {

        Map<String, Object> attributes = oAuth2User.getAttributes();

        switch ( provider.toUpperCase().trim() ) {

            case "GOOGLE" -> {
                return MemberDetails.builder()
                        .name(attributes.get("name").toString())
                        .eamil(attributes.get("email").toString())
                        .attributes(attributes)
                        .build();
            }

            case "NAVER" -> {
                Map<String, String> properties = (Map<String, String>) attributes.get("response");
                return MemberDetails.builder()
                        .name(properties.get("name"))
                        .eamil(properties.get("id") + "@naver.com")
                        .attributes(attributes)
                        .build();
            }

            default -> throw new IllegalArgumentException("지원하지 않는 제공자 : " + provider);

        }

    }

}
