package io.eddie.oauth2.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Accessors(chain = true)
public class MemberDetails implements OAuth2User {

    private String name;
    private String email;
    private Map<String, Object> attributes;

    @Setter
    private String role;

    @Builder
    public MemberDetails(String name, String eamil, String role, Map<String, Object> attributes) {
        this.name = name;
        this.email = eamil;
        this.role = role;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

}
