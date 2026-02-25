package com.jicd.stockmanager.model;

import com.jicd.stockmanager.Enums.ROL_TYPE;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    @Column(name = "user_email",unique = true, nullable = false)
    private String email;

    @Column(name = "user_password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_rol")
    private ROL_TYPE rol;

    @OneToMany(mappedBy = "user")
    private List<Sale> sales;

    /**
     * Devuelve las autoridades concedidas al usuario.
     * En este caso, convierte el rol del usuario en una autoridad de Spring Security.
     *
     * @return una colección de autoridades (roles)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retornamos el rol con el prefijo ROLE_ por convención de Spring Security
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
