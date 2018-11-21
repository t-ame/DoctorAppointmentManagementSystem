package com.java.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.java.dto.Appointment;
import com.java.dto.AppointmentStatus;

@Repository
@Transactional
public interface AppointmentRepository extends PagingAndSortingRepository<Appointment, Integer> {

	@RestResource(exported = false)
	Page<Appointment> findByDoctorEmailId(String doctorEmail, Pageable pageable, Sort sort);

	@RestResource(exported = false)
	Page<Appointment> findByDoctorEmailIdAndStatus(String doctorEmail, AppointmentStatus status, Pageable pageable);

	@RestResource(exported = false)
	Page<Appointment> findByStatusIs(AppointmentStatus status, Pageable pageable);

	@RestResource(exported = false)
	@Query("select * from Appointment x where doctorEmailId = ?1 and between x.appointmentStartTime.date = ?2 and status = ?3 ")
	Page<Appointment> findByDoctorEmailIdAndAppointmentDateAndStatus(String doctorEmail, LocalDate dateOfAppointment,
			AppointmentStatus status, Pageable pageable);

	@RestResource(exported = false)
	Page<Appointment> findByPatientEmailIdAndStatusNot(String patientEmail, AppointmentStatus status,
			Pageable pageable);

	@RestResource(exported = false)
	Page<Appointment> findByPatientEmailIdAndStatus(String patientEmailId, AppointmentStatus status, Pageable pageable);

	@RestResource(exported = false)
	@Query("select * from Appointment x where ?1 between x.appointmentStartTime and x.appointmentEndTime or ?2 between x.appointmentStartTime and x.appointmentEndTime")
	List<Appointment> findByTimeOverlap(LocalDateTime start, LocalDateTime end);

}
