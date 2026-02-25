package com.jicd.stockmanager.service;

import com.jicd.stockmanager.Enums.ROL_TYPE;
import com.jicd.stockmanager.dto.AuthResDTO;
import com.jicd.stockmanager.dto.UserReqDTO;
import com.jicd.stockmanager.exceptions.EmailExistException;
import com.jicd.stockmanager.exceptions.UserNotFoundException;
import com.jicd.stockmanager.model.User;
import com.jicd.stockmanager.repository.UserRepository;
import com.jicd.stockmanager.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthResDTO login(UserReqDTO userDto) throws UserNotFoundException {

        if (userDto == null) throw new UserNotFoundException("User not found");

        //Autenticar al usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.username(),
                        userDto.password()
                )
        );

        String token = jwtUtils.generateToken(authentication);


        //Detalles de la respuesta
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull) // --> Para evitar nullPointerException
                .findFirst()
                .orElse("ROLE_USER");

        return new AuthResDTO(token, userDetails.getUsername(), role);

    }

    public String register(UserReqDTO userDto) {

        if (userRepository.existsByEmail(userDto.email())) {
            throw new EmailExistException("Error: Email already exist");
        }

        User user = User.builder()
                .username(userDto.username())
                .email(userDto.email())
                .password(passwordEncoder.encode(userDto.password()))
                .rol(ROL_TYPE.valueOf(userDto.role().toUpperCase()))
                .build();

        userRepository.save(user);
        return "User registered successfully";


    }


}
