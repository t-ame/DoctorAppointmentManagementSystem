package com.java.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.dto.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

}
