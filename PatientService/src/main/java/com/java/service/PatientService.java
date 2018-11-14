package com.java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.dao.PatientRepository;
import com.java.dto.Patient;

@Service
@Transactional
public class PatientService {

	@Autowired
	PatientRepository rep;

	public Patient updatePatient(Patient patient) {
		return rep.save(patient);
	}

	public Patient patchUpdatePatient(Patient patient) {
		Patient p = null;
		if (patient.getMobileNumber() != -1) {
			p = rep.updateMobileNumber(patient.getMobileNumber(), patient.getPatientId());
		}
		if (patient.getFirstName() != null) {
			p = rep.updateFirstName(patient.getFirstName(), patient.getPatientId());
		}
		if (patient.getLastName() != null) {
			p = rep.updateLastName(patient.getLastName(), patient.getPatientId());
		}
		return p;
	}

	public Patient addPatient(Patient patient) {
		return rep.save(patient);
	}

	public void deletePatient(Patient patient) {
		rep.delete(patient);
	}

}
