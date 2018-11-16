package com.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.java.dao.PatientRepository;
import com.java.dto.Login;
import com.java.dto.Patient;
import com.java.dto.UserRole;
import com.java.exception.PatientRegisterException;
import com.java.service.PatientService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@BasePathAwareController
public class PatientController {

	@Autowired
	PatientService ptService;

	@Autowired
	RestTemplate template;

	@GetMapping(path = "patients")
	public ResponseEntity<?> findAllActivePatients() {

		return null;
	}

	@HystrixCommand(fallbackMethod = "fallbackMethodForPutUpdate", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1")

	})
	@PutMapping(path = "patients/{id}")
	public ResponseEntity<?> updatePatientOne(@PathVariable("id") int id, @RequestBody Patient patient1) {

		patient1.setPatientId(id);
		Patient patient = ptService.updatePatient(patient1);
		if (patient == null) {
			throw new PatientRegisterException("Unable to register new patient");
		}
		Login login = new Login();
		login.setUserPassword(patient1.getPassword());
		login.setUserName(patient1.getEmail());
		login.setUserRole(UserRole.ROLE_PATIENT);
		login.setUserId(patient1.getPatientId());
		try {
			template.put("http://user-service/users/" + patient1.getEmail(), login);
		} catch (RestClientException e) {
			throw new PatientRegisterException("Unable to update patient in authentication server");
		}

		MappingJacksonValue patientData = new MappingJacksonValue(patient);
		patientData.setFilters(Patient.filterOutPassword());

		return ResponseEntity.ok().body(patientData);
	}

	@HystrixCommand(fallbackMethod = "fallbackMethodForPatchUpdate", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1")

	})
	@PatchMapping(path = "patients/{id}")
	public ResponseEntity<?> updatePatientTwo(@PathVariable("id") int id, @RequestBody Patient patient1) {

		patient1.setPatientId(id);
		Patient patient = ptService.patchUpdatePatient(patient1);
		if (patient == null) {
			throw new PatientRegisterException("Unable to register new patient");
		}

		if (patient1.getPassword() != null || patient1.getEmail() != null) {
			Login login = new Login();
			login.setUserId(id);
			if (patient1.getPassword() != null)
				login.setUserPassword(patient1.getPassword());
			try {
				Login loginResponse = template.patchForObject("http://user-service/users/" + patient1.getEmail(), login,
						Login.class);
				if (loginResponse == null) {
					throw new PatientRegisterException("Unable to update patient in authentication server");
				}
			} catch (RestClientException e) {
				throw new PatientRegisterException("Unable to update patient in authentication server");
			}
		}
		MappingJacksonValue patientData = new MappingJacksonValue(patient);
		patientData.setFilters(Patient.filterOutPassword());

		return ResponseEntity.ok().body(patientData);
	}

	@HystrixCommand(fallbackMethod = "fallbackMethodForAdd", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1")

	})
	@PostMapping(path = "patients")
	public ResponseEntity<?> addPatient(@RequestBody Patient patient) {

		patient = ptService.addPatient(patient);
		if (patient == null) {
			throw new PatientRegisterException("Unable to register new patient");
		}
		Login login = new Login();
		login.setUserPassword(patient.getPassword());
		login.setUserName(patient.getEmail());
		login.setUserRole(UserRole.ROLE_PATIENT);
		login.setUserId(patient.getPatientId());
		ResponseEntity<Login> loginResponse = template.postForEntity("http://user-service/users", login, Login.class);
		if (loginResponse == null || (loginResponse != null && loginResponse.getStatusCode() != HttpStatus.CREATED)) {
			throw new PatientRegisterException("Unable to add patient to authentication server");
		}

		Link link = ControllerLinkBuilder.linkTo(PatientRepository.class).slash(patient.getPatientId()).withSelfRel();

		MappingJacksonValue patientData = new MappingJacksonValue(patient);
		patientData.setFilters(Patient.filterOutPassword());

		return ResponseEntity.ok().body(patientData);
	}

	@HystrixCommand(fallbackMethod = "fallbackMethodForDelete", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1")

	})
	@DeleteMapping(path = "patients/{id}")
	public ResponseEntity<?> deletePatient(@PathVariable("id") int id) {
		Patient patient = new Patient();
		patient.setPatientId(id);
		ptService.deletePatient(patient);
		template.delete("http://user-service/users/"+patient.getEmail());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForAdd(Patient patient) {
		patient = ptService.addPatient(patient);
		if (patient != null) {
			MappingJacksonValue patientData = new MappingJacksonValue(patient);
			patientData.setFilters(Patient.filterOutPassword());
			return ResponseEntity.ok().body(patientData);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForPutUpdate(int id, Patient patient) {
		patient.setPatientId(id);
		patient = ptService.updatePatient(patient);
		if (patient != null) {
			return ResponseEntity.ok().body(patient);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForPatchUpdate(int id, Patient patient) {
		patient.setPatientId(id);
		patient = ptService.patchUpdatePatient(patient);
		if (patient != null) {
			return ResponseEntity.ok().body(patient);
		} else {
			return ResponseEntity.noContent().build();
		}
	}


	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForDelete(int id) {
		Patient patient = new Patient();
		patient.setPatientId(id);
		ptService.deletePatient(patient);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	
}
