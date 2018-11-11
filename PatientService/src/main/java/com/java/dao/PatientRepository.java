package com.java.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.java.dto.Patient;
@RepositoryRestResource(path="patients")
public interface PatientRepository extends JpaRepository<Patient, Integer> {
	
	@RestResource(path="firstName")
	List<Patient> findByFirstName(@Param("name")String firstName);

	 
}
