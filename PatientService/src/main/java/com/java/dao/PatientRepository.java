package com.java.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.java.dto.Address;
import com.java.dto.Patient;

@Repository
@Transactional
public interface PatientRepository extends JpaRepository<Patient, Integer> {

	@RestResource(exported = false)
	Patient findByEmail(String email);

	@RestResource(exported = false)
	List<Patient> findByEnabledIs(boolean active);

	@RestResource(exported = false)
	@Query("select addresses from Patient where patientId = ?1")
	List<Address> findPatientAddresses(int patientId);

}
