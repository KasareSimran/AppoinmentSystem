package com.appointmentBooking.appointmentBooking.Service;


import com.appointmentBooking.appointmentBooking.Repository.UserRepo;
import com.appointmentBooking.appointmentBooking.Entity.User;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo repo;

    public CustomUserDetailsService(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        User user = repo.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getPhone(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}

