package ru.selfolio.selfolio.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.selfolio.selfolio.models.AppUser;
import ru.selfolio.selfolio.repositories.AppUserRepository;

@Service
public class RegistrationService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    public RegistrationService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void saveAppUser(AppUser appUser){
        appUser.setRole("ROLE_USER");
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);
    }
}
