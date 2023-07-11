package com.filament.measurement.Permission;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.filament.measurement.Permission.Permission.*;

@RequiredArgsConstructor
public enum Role {
    commonUser(Collections.emptySet()),
    changerUser(
            Set.of(
                    CHANGER_USER_READ,
                    CHANGER_USER_UPDATE,
                    CHANGER_USER_CREATE,
                    CHANGER_USER_DELETE
            )
    ),
    masterUser(
            Set.of(
            CHANGER_USER_READ,
            CHANGER_USER_UPDATE,
            CHANGER_USER_CREATE,
            CHANGER_USER_DELETE,
            MASTER_USER_READ,
            MASTER_USER_UPDATE,
            MASTER_USER_CREATE,
            MASTER_USER_DELETE
            )
    ),
    ;
    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities(){
        List<SimpleGrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }

}
