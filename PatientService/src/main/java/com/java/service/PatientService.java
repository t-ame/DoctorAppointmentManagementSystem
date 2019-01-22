package com.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.dao.PatientRepository;
import com.java.dto.Address;
import com.java.dto.Patient;

@Service
@Transactional
public class PatientService {

	@Autowired
	PatientRepository rep;

	@SuppressWarnings("unused")
	private static final String PATIENT_LIST_KEY = "patient_list";

	@Caching(evict = { @CacheEvict(value = "patientsCache", allEntries = true),
			@CacheEvict(value = "addressCache", key = "#id", condition = "#id>0") }, put = {
					@CachePut(value = "patientCache", key = "#id", unless = "#result==null || #id<=0") })
	public Patient updatePatient(int id, Patient patient) {
		return rep.save(patient);
	}

	@Caching(evict = { @CacheEvict(value = "patientsCache", allEntries = true),
			@CacheEvict(value = "addressCache", key = "#id", condition = "#id>0") }, put = {
					@CachePut(value = "patientCache", key = "#id", unless = "#result==null || #id<=0") })
	public Patient patchUpdatePatient(int id, Patient patient) {
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

	@Caching(evict = { @CacheEvict(value = "patientsCache", allEntries = true) }, put = {
			@CachePut(value = "patientCache", key = "#result.patientId", unless = "#result==null") })
	public Patient addPatient(Patient patient) {
		return rep.save(patient);
	}

	@Caching(evict = { @CacheEvict(condition = "#id>0", value = "patientsCache", allEntries = true),
			@CacheEvict(value = "patientCache", allEntries = true),
			@CacheEvict(value = "addressCache", key = "#id", condition = "#id>0") })
	public void deletePatient(int id, Patient patient) {
		rep.delete(patient);
	}

	@Cacheable(value = "patientsCache", key = "#root.target.PATIENT_LIST_KEY", unless = "#result==null")
	public List<Patient> findAllActive() {
		return rep.findByEnabledIs(true);
	}

	public List<Patient> findAllDeleted() {
		return rep.findByEnabledIs(false);
	}

	@Cacheable(value = "patientsCache", key = "#id", unless = "#result==null")
	public Patient findById(int id) {
		Optional<Patient> p = rep.findById(id);
		return p.get();
	}

	@Cacheable(value = "addressCache", key = "#id", unless = "#result==null")
	public List<Address> findAddresses(int id) {
		return rep.findPatientAddresses(id);
	}

}
