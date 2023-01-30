package com.team1.spreet.global.infra.email.repository;

import com.team1.spreet.global.infra.email.model.EmailConfirm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfirmRepository extends CrudRepository<EmailConfirm, String> {
    Optional<EmailConfirm> findByEmail(String email);
}
