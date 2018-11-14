package com.java.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestBody;

import com.java.dto.Login;
import com.java.dto.UserRole;

@RepositoryRestResource(path = "/users", exported = true)
public interface LoginRepository extends JpaRepository<Login, String> {

//	DO AUTHENTICATION IN CONTROLLER

	@RestResource(exported = false)
//	@RestResource(path = "/find-username-password-role", exported = true)
	Login findByUserNameAndUserPasswordAndUserRole(String username, String password, UserRole role);

	@RestResource(path = "/find-username-password-role", exported = true)
	default Login loginUser(@RequestBody Login login) {
		return findByUserNameAndUserPasswordAndUserRole(login.getUserName(), login.getUserPassword(),
				login.getUserRole());
	}

}
