package com.java.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.java.dto.Doctor;
@RepositoryRestResource(path="doctors")
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
	
	@RestResource(path="firstName")
	List<Doctor> findByFirstName(@Param("name")String firstName);
	
	@RestResource(path="speciality")
	 Set<Doctor> findBySpecialities_Name(String  name);
}
