package com.java.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import com.java.dto.Appointment;
import com.java.dto.Booking;
import com.java.service.AppointmentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@BasePathAwareController
public class AppointmentController {

	@Autowired
	AppointmentService apptService;

	@Autowired
	RestTemplate template;

	@HystrixCommand(fallbackMethod = "fallbackMethodForAddAppt", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")

	})
	@PostMapping("appointments")
	public ResponseEntity<?> addNewAppointment(@RequestBody Appointment appointment) {
		appointment = apptService.addAppointment(appointment);
		if (appointment != null) {
			Booking booking = new Booking();
			booking.setAppointmentId(appointment.getAppointmentId());
			booking.setBookingStartDate(appointment.getAppointmentStartTime().toLocalDate());
			booking.setBookingStartTime(appointment.getAppointmentStartTime().toLocalTime());
			booking.setBookingEndDate(appointment.getAppointmentEndTime().toLocalDate());
			booking.setBookingEndTime(appointment.getAppointmentEndTime().toLocalTime());
			template.postForEntity(
					"http://doctor-service/doctors/" + appointment.getDoctorEmailId() + "/add-appointment", booking,
					Booking.class);
			Link link = linkTo(methodOn(AppointmentController.class).deleteAppointment(appointment.getAppointmentId()))
					.withSelfRel();
			Link link2 = linkTo(methodOn(AppointmentController.class).addNewAppointment(appointment))
					.withRel("addresses");
			appointment.add(link);
			appointment.add(link2);
			return ResponseEntity.ok(appointment);
		}
		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Overlap with existing appointment");
	}

	@HystrixCommand(fallbackMethod = "fallbackMethodForUpdateAppt", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")

	})
	@PutMapping("appointments/{id}")
	public ResponseEntity<?> updateAppointment(@PathVariable("id") int id, @RequestBody Booking booking) {

		Appointment appointment = Appointment.appointment().appointmentId(id)
				.appointmentStartTime(LocalDateTime.of(booking.getBookingStartDate(), booking.getBookingEndTime()))
				.appointmentStartTime(LocalDateTime.of(booking.getBookingEndDate(), booking.getBookingEndTime()))
				.build();
		appointment = apptService.updateAppointment(appointment);
		if (appointment != null) {
			booking.setAppointmentId(id);
			template.put("http://doctor-service/doctors/" + appointment.getDoctorEmailId() + "/update-appointment",
					booking);
			Link link = linkTo(methodOn(AppointmentController.class).deleteAppointment(appointment.getAppointmentId()))
					.withSelfRel();
			Link link2 = linkTo(methodOn(AppointmentController.class).addNewAppointment(appointment))
					.withRel("addresses");
			appointment.add(link);
			appointment.add(link2);
			return ResponseEntity.ok(appointment);
		}

		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
				.body("Invalid appointment Id or Overlap with existing appointment");
	}

	@HystrixCommand(fallbackMethod = "fallbackMethodForDeleteAppt", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")

	})
	@DeleteMapping("appointments/{id}")
	public ResponseEntity<?> deleteAppointment(@PathVariable("id") int id) {
		Appointment appointment = apptService.findAppointmentById(id);
		if (appointment != null) {
			apptService.cancelAppointment(appointment);
			template.delete(
					"http://doctor-service/doctors/" + appointment.getDoctorEmailId() + "/delete-appointment/" + id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("Invalid appointment id");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@HystrixCommand(fallbackMethod = "fallbackMethodForDeleteAppt", commandProperties = {
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "10"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "50000"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")

	})
	@GetMapping("appointments")
	public ResponseEntity<?> getAppointments(@RequestParam("page") int page, @RequestParam("size") int size) {
		Page allBooked = apptService.findAllBooked(page, size);
		if (allBooked != null) {
			for (Appointment appointment : (Page<Appointment>) allBooked) {
				Link link = linkTo(
						methodOn(AppointmentController.class).deleteAppointment(appointment.getAppointmentId()))
								.withSelfRel();
				appointment.add(link);
			}
			Link link = Link
					.valueOf("http://localhost:9000/appointment-service/appointments?page=" + page + "&size=" + size)
					.withRel("self");
			Link link2 = Link.valueOf("http://localhost:9000/appointment-service/appointments?(page,size)")
					.withRel("page");
			Resources<Page> result = new Resources<Page>(allBooked, link, link2);
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Currently unable to access database");
	}

//	================================= FALL BACK METHODS ===================================================

	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForAddAppt(Appointment appointment) {

		return null;
	}

	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForUpdateAppt(Appointment appointment) {

		return null;
	}

	@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Currently unable to access authentication server")
	public ResponseEntity<?> fallbackMethodForDeleteAppt(Appointment appointment) {

		return null;
	}

}
