package com.marco.mcshop.auth.service;

import com.marco.mcshop.auth.entity.CustomerEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomerService {

    CustomerEntity create(CustomerEntity customerEntity);

    boolean isExist(String id);

    CustomerEntity getCurrentCustomer() throws Exception;

    UserDetails findUserDetailsByEmail(String email);
}
