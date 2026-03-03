package org.uteq.resoluciones.resoluciones.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.uteq.resoluciones.resoluciones.entities.User;
import org.uteq.resoluciones.resoluciones.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User u = userRepo.findByUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Si aún no manejas roles/permisos en SecurityContext, deja una authority básica:
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        return new org.springframework.security.core.userdetails.User(
                u.getUser(),
                u.getPassword(),
                u.getState() != null ? u.getState() : true,  // enabled
                true, true, true,
                authorities
        );
    }
}