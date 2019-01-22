package com.java.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.java.dto.Address;
import com.java.dto.Calendar;
import com.java.dto.Doctor;

@Repository
@Transactional
public interface DoctorRepository extends PagingAndSortingRepository<Doctor, Integer> {

	@RestResource(exported = false)
	Doctor findByEmail(String email);

	@RestResource(exported = false)
	Page<Doctor> findBySpecialties_SpecialtyNameAndAddresses_ZipcodeAndEnabledIs(String specialty, int zipcode,
			boolean enabled, Pageable pageable);

	@RestResource(exported = false)
	Page<Doctor> findByEnabledIs(boolean enabled, Pageable pageable);

	@RestResource(exported = false)
	@Query("select calendar from Doctor where doctorId = ?1")
	Calendar findDoctorCalendar(int id);

	@Query("select addresses from Doctor where doctorId = ?1")
	List<Address> findDoctorAddresses(int id);
}
