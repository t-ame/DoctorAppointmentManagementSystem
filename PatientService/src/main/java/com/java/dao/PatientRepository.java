package com.java.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.java.dto.Patient;

@Repository
@Transactional
public interface PatientRepository extends JpaRepository<Patient, Integer> {

	@RestResource(path = "/findemail", exported = true)
	Patient findByEmail(String email);

//	@SuppressWarnings("unchecked")
//	@RestResource(exported = false)
//	@Override
//	Patient save(Patient patient);

	@Modifying
	@Query("update Patient set mobileNumber = ?1 where patientId = ?2")
	Patient updateMobileNumber(long mobile, int patientId);

	@Modifying
	@Query("update Patient set firstName = ?1 where patientId = ?2")
	Patient updateFirstName(String firstName, int patientId);

	@Modifying
	@Query("update Patient set lastName = ?1 where patientId = ?2")
	Patient updateLastName(String lastName, int patientId);

	@RestResource(path = "/findactive", exported = true)
	List<Patient> findByActiveIs(boolean active);

}
