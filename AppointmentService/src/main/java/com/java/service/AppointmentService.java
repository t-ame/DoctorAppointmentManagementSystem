package com.java.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.dao.AppointmentRepository;
import com.java.dto.Appointment;

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
			LocalDate dateOfAppointment, String status) {
		return rep.findByDoctorEmailIdAndDateOfAppointmentAndStatus(doctorEmailId, dateOfAppointment, status);
	}

	List<Appointment> findByPatientEmailId(String patientEmailId) {
		return rep.findByPatientEmailId(patientEmailId);
	}

	List<Appointment> findByPatientEmailIdAndStatus(String patientEmailId, String status) {
		return rep.findByPatientEmailIdAndStatus(patientEmailId, status);
	}

}
