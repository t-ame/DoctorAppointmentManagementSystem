package com.java.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.dao.AppointmentRepository;
import com.java.dto.Appointment;
import com.java.dto.AppointmentStatus;

@Service
@Transactional
public class AppointmentService {

	@Autowired
	AppointmentRepository rep;

	public Appointment addAppointment(Appointment appt) {
		return rep.save(appt);
	}

	public void cancelAppointment(Appointment appt) {
		rep.delete(appt);
	}

	// IMPLEMENT
	public void updateAppointment(Appointment appt) {
		rep.save(appt);
	}

	List<Appointment> findByDoctorEmailIdAndDateOfAppointmentAndStatus(String doctorEmailId,
			LocalDate dateOfAppointment, AppointmentStatus status) {
		return rep.findByDoctorEmailIdAndDateOfAppointmentAndStatus(doctorEmailId, dateOfAppointment, status);
	}

	List<Appointment> findByPatientEmailId(String patientEmailId) {
		return rep.findByPatientEmailId(patientEmailId);
	}

	List<Appointment> findByPatientEmailIdAndStatus(String patientEmailId, AppointmentStatus status) {
		return rep.findByPatientEmailIdAndStatus(patientEmailId, status);
	}

}
