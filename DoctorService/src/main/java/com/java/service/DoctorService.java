package com.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.dao.DoctorRepository;
import com.java.dto.Doctor;

@Service
@Transactional
public class DoctorService {

	@Autowired
	DoctorRepository rep;

	public Doctor updatePatient(Doctor doctor) {
		return rep.save(doctor);
	}

	public Doctor patchUpdatePatient(Doctor doctor) {
		Doctor d = null;
		if (patient.getMobileNumber() != -1) {
			d = rep.updateMobileNumber(patient.getMobileNumber(), patient.getPatientId());
		}
		if (patient.getFirstName() != null) {
			d = rep.updateFirstName(patient.getFirstName(), patient.getPatientId());
		}
		if (patient.getLastName() != null) {
			d = rep.updateLastName(patient.getLastName(), patient.getPatientId());
		}
		return d;
	}

	public Doctor addPatient(Doctor doctor) {
		return rep.save(doctor);
	}

	public void deletePatient(Doctor doctor) {
		rep.delete(doctor);
	}

}
