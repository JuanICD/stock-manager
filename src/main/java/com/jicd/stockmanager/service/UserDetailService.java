package com.jicd.stockmanager.service;

import com.jicd.stockmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Carga un usuario de la base de datos por su nombre de usuario.
     * Metodo requerido por la interfaz UserDetailsService de Spring Security.
     *
     * @param username El nombre de usuario a buscar.
     * @return UserDetails el objeto de usuario que implementa UserDetails.
     * @throws UsernameNotFoundException si el usuario no existe.
     */
    @Override
    @NonNull
    public UserDetails  loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
