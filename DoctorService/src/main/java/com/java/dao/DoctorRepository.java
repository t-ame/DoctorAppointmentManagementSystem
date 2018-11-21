package com.java.dao;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.java.dto.Calendar;
import com.java.dto.Doctor;

@Repository
@Transactional
public interface DoctorRepository extends PagingAndSortingRepository<Doctor, Integer> {

	@RestResource(exported = false)
	Doctor findByEmail(String email);

	@RestResource(exported = false)
	Page<Doctor> findBySpecialties_SpecialtyNameAndAddresses_ZipcodeAndEnabledIs(String specialty, int zipcode,
			boolean enabled, Pageable pageable, Sort sort);

	@RestResource(exported = false)
	Page<Doctor> findByEnabledIs(boolean enabled, Pageable pageable, Sort sort);

	@RestResource(exported = false)
	@Query("select calendar from Doctor where doctorId = ?1")
	Calendar findDoctorCalendar(int id);
}
