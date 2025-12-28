package com.evanesce.repository;

import com.evanesce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmailAndSecurityQuesAndSecurityAns(
            String email,
            String securityQues,
            String securityAns
    );

    boolean existsByEmail(String email);
}
