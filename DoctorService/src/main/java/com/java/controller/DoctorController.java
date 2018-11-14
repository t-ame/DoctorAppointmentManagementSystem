package com.java.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.java.dto.Login;
import com.java.dto.Patient;
import com.java.exception.DoctorRegisterException;
import com.java.service.DoctorService;

@RestController
@RequestMapping(path = "/patients", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class DoctorController {

	@Autowired
	DoctorService ptService;

	@Autowired
	RestTemplate template;

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public MappingJacksonValue updatePatientOne(@PathVariable("id") int id, @RequestBody Patient patient1) {

		HttpHeaders header = new HttpHeaders();
		List<String> values = Arrays.asList("application/json", "application/xml");
		header.addAll("content-type", values);

		patient1.setPatientId(id);
		Patient patient = ptService.updatePatient(patient1);
		if (patient == null) {
			throw new DoctorRegisterException("Unable to register new patient");
		}
		Login login = new Login();
		login.setUserPassword(patient1.getPassword());
		login.setUserName(patient1.getEmail());
		login.setUserRole("PATIENT");
		login.setUserId(patient1.getPatientId());
		try {
			template.put("http://user-service/users/" + patient1.getEmail(), login);
		} catch (RestClientException e) {
			throw new DoctorRegisterException("Unable to update patient in authentication server");
		}

		MappingJacksonValue patientData = new MappingJacksonValue(patient);
		patientData.setFilters(Patient.filterOutPassword());

		return patientData;
	}

	@PatchMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public MappingJacksonValue updatePatientTwo(@PathVariable("id") int id, @RequestBody Patient patient1) {

		HttpHeaders header = new HttpHeaders();
		List<String> values = Arrays.asList("application/json", "application/xml");
		header.addAll("content-type", values);

		patient1.setPatientId(id);
		Patient patient = ptService.patchUpdatePatient(patient1);
		if (patient == null) {
			throw new DoctorRegisterException("Unable to register new patient");
		}

		if (patient1.getPassword() != null || patient1.getEmail() != null) {
			Login login = new Login();
			login.setUserId(id);
			if (patient1.getPassword() != null)
				login.setUserPassword(patient1.getPassword());
			if (patient1.getEmail() != null)
				login.setUserName(patient1.getEmail());
			try {
				Login loginResponse = template.patchForObject("http://user-service/users/" + patient1.getEmail(), login,
						Login.class);
				if (loginResponse == null) {
					throw new DoctorRegisterException("Unable to update patient in authentication server");
				}
			} catch (RestClientException e) {
				throw new DoctorRegisterException("Unable to update patient in authentication server");
			}
		}
		MappingJacksonValue patientData = new MappingJacksonValue(patient);
		patientData.setFilters(Patient.filterOutPassword());

		return patientData;
	}

	@PostMapping(path = "/add", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public MappingJacksonValue addPatient(@RequestBody Patient patient) {

		HttpHeaders header = new HttpHeaders();
		List<String> values = Arrays.asList("application/json", "application/xml");
		header.addAll("content-type", values);
		patient = ptService.addPatient(patient);
		if (patient == null) {
			throw new DoctorRegisterException("Unable to register new patient");
		}
		Login login = new Login();
		login.setUserPassword(patient.getPassword());
		login.setUserName(patient.getEmail());
		login.setUserRole("PATIENT");
		login.setUserId(patient.getPatientId());
		try {
			ResponseEntity<Login> loginResponse = template.postForEntity("http://user-service/users", login,
					Login.class);
			if (loginResponse == null
					|| (loginResponse != null && loginResponse.getStatusCode() != HttpStatus.CREATED)) {
				throw new DoctorRegisterException("Unable to add patient to authentication server");
			}
		} catch (RestClientException e) {
			throw new DoctorRegisterException("Unable to update patient in authentication server");
		}

		MappingJacksonValue patientData = new MappingJacksonValue(patient);
		patientData.setFilters(Patient.filterOutPassword());

		return patientData;
	}

}
