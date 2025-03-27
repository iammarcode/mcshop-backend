package com.marco.mcshop.auth.repository;

import com.marco.mcshop.auth.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {
    boolean existsByEmail(String email);

    Optional<CustomerEntity> findByEmail(String email);
}
