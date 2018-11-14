package com.java.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.dto.Admin;
import com.java.service.AdminService;

@RestController
@RequestMapping("/admins")
public class AdminController {
	
	@Autowired
	AdminService adminService;

	@PostMapping(path = "/login", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Admin> authenticate(@RequestBody Admin admin) {

		HttpHeaders header = new HttpHeaders();
		List<String> values = Arrays.asList("application/json", "application/xml");
		header.addAll("content-type", values);
		admin = adminService.authenticateUser(admin);
		
		//IMPLEMENT PROPERLY!!!
		
		if(admin == null) {
			return new ResponseEntity<>(admin, header, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(admin, header, HttpStatus.OK);
		}
		
	}

}
