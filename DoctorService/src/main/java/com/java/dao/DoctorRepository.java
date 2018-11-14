package com.java.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.java.dto.Doctor;

@Repository
@Transactional
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

	@RestResource(path = "firstName")
	List<Doctor> findByFirstName(String firstName);

	@RestResource(path = "find-speciality-zipcode")
	Set<Doctor> findBySpecialties_NameAndAddresses_Zipcode(String name, int zipcode);
}
