package com.marco.mcshop.auth.service.impl;

import com.marco.mcshop.auth.entity.CustomerEntity;
import com.marco.mcshop.auth.exception.customer.CustomerNotFoundException;
import com.marco.mcshop.auth.repository.CustomerRepository;
import com.marco.mcshop.auth.service.CustomerService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerEntity create(CustomerEntity customerEntity) {
        return customerRepository.save(customerEntity);
    }

    @Override
    public boolean isExist(String id) {
        return customerRepository.existsById(id);
    }


    @Override
    public CustomerEntity getCurrentCustomer() throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails) {
            UserDetails currentCustomer = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return customerRepository.findByEmail(currentCustomer.getUsername()).orElseThrow(
                    () -> new CustomerNotFoundException(currentCustomer.getUsername())
            );
        }

        throw new JwtException("Authentication failed");
    }

    @Override
    public UserDetails findUserDetailsByEmail(String email) {
        CustomerEntity customerFound = customerRepository.findByEmail(email).orElseThrow(
                () -> new CustomerNotFoundException(email)
        );

        return User.builder()
                .username(customerFound.getEmail())
                .password(customerFound.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .authorities(List.of())
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
