package com.java.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.dto.Admin;

//@RepositoryRestResource(path = "admin")
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

//	@RestResource(path = "login")
	Admin findByEmailAndPassword(String email, String password);

}
