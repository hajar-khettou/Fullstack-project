package com.gameboard.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultConverter =
            new JwtGrantedAuthoritiesConverter();

    // Ce claim doit correspondre exactement à ce que tu configures dans Auth0
    private static final String ROLES_CLAIM = "https://boardgame.com/roles";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> defaultAuthorities = defaultConverter.convert(jwt);

        Collection<GrantedAuthority> roleAuthorities = extractRoles(jwt);

        Collection<GrantedAuthority> allAuthorities = Stream
                .concat(defaultAuthorities.stream(), roleAuthorities.stream())
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, allAuthorities, jwt.getSubject());
    }

    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList(ROLES_CLAIM);

        if (roles == null) return List.of();

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }
}