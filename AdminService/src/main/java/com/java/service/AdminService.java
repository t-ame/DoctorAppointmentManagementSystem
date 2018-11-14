package com.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.dao.AdminRepository;
import com.java.dto.Admin;

@Service
public class AdminService {

	@Autowired
	AdminRepository rep;
	
	public Admin authenticateUser(Admin admin) {
		return rep.findByEmailAndPassword(admin.getEmail(), admin.getPassword());
	}
	
}
