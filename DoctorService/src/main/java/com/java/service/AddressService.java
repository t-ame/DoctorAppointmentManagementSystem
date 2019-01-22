package com.java.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.dao.AddressRepository;
import com.java.dto.Address;

@Service
@Transactional
public class AddressService {

	@Autowired
	AddressRepository addRep;

	public Address findAddressById(int id) {
		Optional<Address> optional = addRep.findById(id);
		return optional.get();
	}

}
