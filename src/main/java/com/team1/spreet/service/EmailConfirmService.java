package com.team1.spreet.service;

import com.team1.spreet.entity.EmailConfirm;
import com.team1.spreet.repository.EmailConfirmRepository;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailConfirmService {

    private EmailConfirmRepository emailConfirmRepository;
    private RMapCache<String, EmailConfirm> emailConfirmRMapCache;

    @Autowired
    public EmailConfirmService(EmailConfirmRepository emailConfirmRepository, RMapCache<String, EmailConfirm> emailConfirmRMapCache) {
        this.emailConfirmRepository = emailConfirmRepository;
        this.emailConfirmRMapCache = emailConfirmRMapCache;
    }

    public void save(EmailConfirm emailConfirm) {
        emailConfirmRMapCache.fastPut(emailConfirm.getEmail(), emailConfirm, 180, TimeUnit.SECONDS);
    }

    public EmailConfirm findByEmail(String email) {
        return this.emailConfirmRMapCache.get(email);
    }

    public void update(String email, String confirmCode) throws Exception {
        EmailConfirm emailConfirm = emailConfirmRMapCache.get(email);
        emailConfirm.setConfirmCode(confirmCode);
        emailConfirmRMapCache.fastPut(emailConfirm.getEmail(), emailConfirm, 180, TimeUnit.SECONDS);
    }

    public void delete(String id) {
        emailConfirmRMapCache.remove(id);
    }
}
