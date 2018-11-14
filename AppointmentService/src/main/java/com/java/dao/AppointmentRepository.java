package com.java.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.java.dto.Appointment;
import com.java.dto.AppointmentStatus;

@Repository
//@Transactional
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	@RestResource(path = "/find-doctor-date-status", exported = true)
	List<Appointment> findByDoctorEmailIdAndDateOfAppointmentAndStatus(String doctorEmail,
			LocalDate dateOfAppointment, AppointmentStatus status);

	@RestResource(path = "/find-doctor-status", exported = true)
	List<Appointment> findByDoctorEmailIdAndStatus(String doctorEmail, AppointmentStatus status);

	@RestResource(path = "/find-patientemail", exported = true)
	List<Appointment> findByPatientEmailId(String patientEmailId);

	@RestResource(path = "/find-patientemail-status", exported = true)
	List<Appointment> findByPatientEmailIdAndStatus(String patientEmailId, AppointmentStatus status);

}
