package com.java.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.java.dto.Address;
import com.java.dto.Gender;
import com.java.dto.Login;
import com.java.dto.Patient;
import com.java.dto.UserRole;
import com.java.exception.DoctorRegisterException;
import com.java.exception.PatientRegisterException;
import com.java.service.DoctorService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
@RequestMapping(path = "/patients", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
public class DoctorController {

	@Autowired
	DoctorService ptService;

	@Autowired
	RestTemplate template;

//	================================= HANDLER METHODS ===================================================

	@GetMapping(path = "patients")
	public Resources<MappingJacksonValue> findAllActivePatients() {
		List<Patient> patients = ptService.findAllActive();

		if (patients == null) {
			patients = new ArrayList<>();
		}
		List<MappingJacksonValue> list = patients.stream().map(x -> {
			x.add(linkTo(methodOn(PatientController.class).updatePatientOne(x.getPatientId(), x)).withSelfRel());
			x.add(linkTo(methodOn(PatientController.class).addPatientAddress(x.getPatientId())).withRel("addresses"));
			MappingJacksonValue patientData = new MappingJacksonValue(x);
			patientData.setFilters(Patient.filterOutPassword());
			return patientData;
		}).collect(Collectors.toList());

		Link link = linkTo(methodOn(PatientController.class).findAllActivePatients()).withRel("patients");
		Resources<MappingJacksonValue> result = new Resources<MappingJacksonValue>(list, link);
		return result;
	}

	@HystrixCommand(fallbackMethod = "fallbackMethodForPutUpdate", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")

	})
	@PutMapping(path = "patients/{id}")
	public ResponseEntity<?> updatePatientOne(@PathVariable("id") int id, @RequestBody Patient patient1) {
		patient1.setPatientId(id);
		Patient patient = ptService.updatePatient(id, patient1);
		if (patient == null) {
			throw new PatientRegisterException("Unable to register new patient");
		}
		Login login = new Login();
		login.setUserPassword(patient1.getPassword());
		login.setUserName(patient1.getEmail());
		login.setUserRole(UserRole.ROLE_PATIENT);
		login.setUserId(patient1.getPatientId());
		template.put("http://user-service/users/" + patient1.getEmail(), login);
		Link link = linkTo(methodOn(PatientController.class).updatePatientOne(id, patient1)).withSelfRel();
		patient.add(link);
		MappingJacksonValue patientData = new MappingJacksonValue(patient);
		patientData.setFilters(Patient.filterOutPassword());

		return ResponseEntity.ok().body(patientData);
	}

	@HystrixCommand(fallbackMethod = "fallbackMethodForPatchUpdate", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")

	})
	@PatchMapping(path = "patients/{id}")
	public ResponseEntity<?> updatePatientTwo(@PathVariable("id") int id, @RequestBody String data) {

		JSONObject object = new JSONObject(data);
		Patient patient1 = getPatient(object);
		patient1.setPatientId(id);
		Patient patient = ptService.patchUpdatePatient(id, patient1);
		if (patient == null) {
			throw new PatientRegisterException("Unable to register new patient");
		}

		if (patient1.getPassword() != null || patient1.getEmail() != null) {
			Login login = new Login();
			login.setUserId(id);
			if (patient1.getPassword() != null)
				login.setUserPassword(patient1.getPassword());
			Login loginResponse = template.patchForObject("http://user-service/users/" + patient1.getEmail(), login,
					Login.class);
			if (loginResponse == null) {
				throw new PatientRegisterException("Unable to update patient in authentication server");
			}
		}
		Link link = linkTo(methodOn(PatientController.class).updatePatientOne(patient.getPatientId(), patient))
				.withSelfRel();
		patient.add(link);
		MappingJacksonValue patientData = new MappingJacksonValue(patient);
		patientData.setFilters(Patient.filterOutPassword());

		return ResponseEntity.ok().body(patientData);
	}

	@HystrixCommand(fallbackMethod = "fallbackMethodForAdd", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")

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
		Link link = linkTo(methodOn(PatientController.class).updatePatientOne(patient.getPatientId(), patient))
				.withSelfRel();
		patient.add(link);

		MappingJacksonValue patientData = new MappingJacksonValue(patient);
		patientData.setFilters(Patient.filterOutPassword());

		return ResponseEntity.ok().body(patientData);
	}

	@PatchMapping(path = "patients/{id}/addresses")
	public ResponseEntity<?> addPatientAddress(@PathVariable("id") int id) {
		return ResponseEntity.badRequest().build();
	}

	@HystrixCommand(fallbackMethod = "fallbackMethodForDelete", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")

	})
	@DeleteMapping(path = "patients/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT, reason = "Patient deleted successfully!")
	public ResponseEntity<?> deletePatient(@PathVariable("id") int id) {

		Patient patient = ptService.findById(id);
		if (patient != null) {
			ptService.deletePatient(id, patient);
			template.delete("http://user-service/users/" + patient.getEmail());
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

//	================================= FALL BACK METHODS ===================================================

	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForAdd(Patient patient) {
		patient = ptService.addPatient(patient);
		if (patient != null) {
			MappingJacksonValue patientData = new MappingJacksonValue(patient);
			patientData.setFilters(Patient.filterOutPassword());
			return ResponseEntity.ok().body(patientData);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForPutUpdate(int id, Patient patient) {
		patient.setPatientId(id);
		patient = ptService.updatePatient(id, patient);
		if (patient != null) {
			return ResponseEntity.ok().body(patient);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForPatchUpdate(int id, Patient patient) {
		patient.setPatientId(id);
		patient = ptService.patchUpdatePatient(id, patient);
		if (patient != null) {
			return ResponseEntity.ok().body(patient);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForDelete(int id) {
		Patient patient = new Patient();
		patient.setPatientId(id);
		ptService.deletePatient(id, patient);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

//	================================= JSON MAPPER METHODS ===================================================

	private Patient getPatient(JSONObject object) {
		Gson gson = new Gson();
		Patient patient = new Patient();
		patient.setEmail("");
		patient.setFirstName(object.getString("firstName") == null ? "" : object.getString("firstName"));
		patient.setLastName(object.getString("lastName") == null ? "" : object.getString("lastName"));
		patient.setMobileNumber(
				object.getNumber("mobileNumber") == null ? -1 : (long) object.getNumber("mobileNumber"));
		patient.setPassword(object.getString("password") == null ? "" : object.getString("password"));
		patient.setDob(object.getJSONObject("dob") == null ? null
				: gson.fromJson(object.getJSONObject("dob").toString(), LocalDate.class));
		patient.setGender(object.getString("gender") == null ? null : Gender.valueOf(object.getString("gender")));
		return patient;
	}

}
