package ru.selfolio.selfolio.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.selfolio.selfolio.models.AppUser;
import ru.selfolio.selfolio.repositories.AppUserRepository;
import ru.selfolio.selfolio.security.AppUserDetails;

import java.util.Optional;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetails personDetails = (AppUserDetails) authentication.getPrincipal();
        return getAppUser(personDetails.getUsername());
    }

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUser getAppUser(String username){
        return appUserRepository.findByUsername(username).orElse(null);
    }
}
