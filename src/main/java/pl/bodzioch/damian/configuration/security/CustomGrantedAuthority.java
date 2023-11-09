package pl.bodzioch.damian.configuration.security;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@ToString
public class CustomGrantedAuthority implements GrantedAuthority {

    private String role;

    @Override
    public String getAuthority() {
        return role;
    }
}
