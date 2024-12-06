package com.socs.workersManagement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// import static com.socs.security.user.Permission.ADMIN_CREATE;
// import static com.socs.security.user.Permission.ADMIN_DELETE;
// import static com.socs.security.user.Permission.ADMIN_READ;
// import static com.socs.security.user.Permission.ADMIN_UPDATE;
// import static com.socs.security.user.Permission.MANAGER_CREATE;
// import static com.socs.security.user.Permission.MANAGER_DELETE;
// import static com.socs.security.user.Permission.MANAGER_READ;
// import static com.socs.security.user.Permission.MANAGER_UPDATE;

@RequiredArgsConstructor
public enum Role {

        USER(Collections.emptySet()),
        ADMIN(Set.of()),
        CM(Set.of()),
        TICKER(Set.of()),
        LRM(Set.of()),
        LR(Set.of()),
        PM(Set.of()),
        PROCTOR(Set.of()),
        SCM(Set.of()),
        SCG(Set.of()),
        STUDENT(Set.of());

        @Getter
        private final Set<Permission> permissions;

        public List<SimpleGrantedAuthority> getAuthorities() {
                var authorities = getPermissions()
                                .stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                                .collect(Collectors.toList());
                authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
                return authorities;
        }
}
