package com.java.service;

import java.util.List;
import java.util.Optional;

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
		Optional<Patient> p = rep.findById(patient.getPatientId());
		Patient newPatient = p.get();

		if (newPatient != null) {

			if (patient.getMobileNumber() != -1) {
				newPatient.setMobileNumber(patient.getMobileNumber());
			}
			if (patient.getFirstName() != null) {
				newPatient.setFirstName(patient.getFirstName());
			}
			if (patient.getLastName() != null) {
				newPatient.setLastName(patient.getLastName());
			}
			if (patient.getEmail() != null) {
				newPatient.setEmail(patient.getEmail());
			}
			if (patient.getDob() != null) {
				newPatient.setDob(patient.getDob());
			}
			if (patient.getGender() != null) {
				newPatient.setGender(patient.getGender());
			}
			if (patient.getAddresses() != null && patient.getAddresses().size() > 0) {
				newPatient.getAddresses().addAll(patient.getAddresses());
			}

			newPatient = rep.save(newPatient);
		}
		return newPatient;
	}

	public Patient addPatient(Patient patient) {
		return rep.save(patient);
	}

	public void deletePatient(Patient patient) {
		rep.delete(patient);
	}

	public List<Patient> findAllActive() {
		return rep.findByEnabledIs(true);
	}

	public List<Patient> findAllDeleted() {
		return rep.findByEnabledIs(false);
	}

}
