package com.java.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import com.java.dto.Appointment;

@Transactional
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	
	@RestResource(path="appointmentId")
	List<Appointment> findByAppointmentId(@Param("appointmentId")int appointmentId);
	
	
	
	
}
