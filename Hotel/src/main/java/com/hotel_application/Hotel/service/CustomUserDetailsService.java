package com.hotel_application.Hotel.service;

import com.hotel_application.Hotel.entity.User;
import com.hotel_application.Hotel.entity.UserPrincipal;
import com.hotel_application.Hotel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException("Nie znaleziono użytkownika "));

        return new UserPrincipal(user);
    }

}
