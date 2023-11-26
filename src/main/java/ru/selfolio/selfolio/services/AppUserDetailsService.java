package ru.selfolio.selfolio.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.selfolio.selfolio.models.AppUser;
import ru.selfolio.selfolio.repositories.AppUserRepository;
import ru.selfolio.selfolio.security.AppUserDetails;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> foundUser = appUserRepository.findByUsername(username);
        if (foundUser.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        return new AppUserDetails(foundUser.get());
    }
}
