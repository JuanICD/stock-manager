package com.jicd.stockmanager.repository;

import com.jicd.stockmanager.Enums.ROL_TYPE;
import com.jicd.stockmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //Para el login
    public Optional<User> findByUsername(String username);

    //Verificar si existe el usuario
    boolean existsByUsername(String username);

    //Buscar por email
    public User findByEmail(String userEmail);


    //Verificar si existe el usuario
    boolean existsByEmail(String userEmail);

    //Buscar por rol
    List<User> findByRol(ROL_TYPE rol);
}
