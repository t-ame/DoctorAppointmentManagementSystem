package com.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.dao.CalendarRepository;
import com.java.dao.DoctorRepository;
import com.java.dto.Address;
import com.java.dto.AvailableTime;
import com.java.dto.Booking;
import com.java.dto.Calendar;
import com.java.dto.Doctor;
import com.java.dto.Patient;
import com.java.dto.Schedule;

@Service
@Transactional
public class DoctorService {

	@Autowired
	DoctorRepository rep;

	@Autowired
	CalendarRepository calRep;

	public Doctor updateDoctor(Doctor doctor) {
		return rep.save(doctor);
	}

	public Doctor patchUpdateDoctor(Doctor doctor) {

		Optional<Doctor> d = rep.findById(doctor.getDoctorId());
		Doctor newDoctor = d.get();

		if (newDoctor != null) {

			if (doctor.getMobileNumber() != -1) {
				newDoctor.setMobileNumber(doctor.getMobileNumber());
			}
			if (doctor.getFirstName() != null) {
				newDoctor.setFirstName(doctor.getFirstName());
			}
			if (doctor.getLastName() != null) {
				newDoctor.setLastName(doctor.getLastName());
			}
			if (doctor.getEmail() != null) {
				newDoctor.setEmail(doctor.getEmail());
			}
			if (doctor.getDob() != null) {
				newDoctor.setDob(doctor.getDob());
			}
			if (doctor.getGender() != null) {
				newDoctor.setGender(doctor.getGender());
			}
			if (doctor.getAddresses() != null && doctor.getAddresses().size() > 0) {
				newDoctor.getAddresses().addAll(doctor.getAddresses());
			}
			if (doctor.getSpecialties() != null && doctor.getSpecialties().size() > 0) {
				newDoctor.getSpecialties().addAll(doctor.getSpecialties());
			}

			newDoctor = rep.save(newDoctor);

		}

		return newDoctor;
	}

	public void addBooking(int id, Booking booking) {
		Calendar calendar = rep.findDoctorCalendar(id);
		calendar.getBookings().add(booking);
		calRep.save(calendar);
	}

	public void removeBooking(int id, Booking booking) {
		Calendar calendar = rep.findDoctorCalendar(id);
		calendar.getBookings().remove(booking);
		calRep.save(calendar);
	}

	public Calendar updateSchedule(int id, AvailableTime availableTimes) {
		Calendar calendar = rep.findDoctorCalendar(id);
		calendar.setSchedule(Schedule.builder().defaultAvailableTimes(availableTimes).build().initializeWeekSchedule());
		calRep.save(calendar);
		return null;
	}

	public Calendar updateBooking(int id, Booking booking) {
		Calendar calendar = rep.findDoctorCalendar(id);
		for(Booking book : calendar.getBookings()){
			if(book.getAppointmentId() == booking.getAppointmentId()) {
				book = booking;
			}
		}
		calRep.save(calendar);
		return null;
	}

	public void deletePatient(int id, Patient patient) {
		rep.delete(patient);
	}

	public Page<Doctor> findAllActive(int page, int size) {
		return rep.findByEnabledIs(true, PageRequest.of(page, size, new Sort(Sort.Direction.ASC, "lastName")));
	}

	public Page<Doctor> findAllDeleted(int page, int size) {
		return rep.findByEnabledIs(false, PageRequest.of(page, size, new Sort(Sort.Direction.ASC, "lastName")));
	}

	public Doctor findById(int id) {
		Optional<Doctor> p = rep.findById(id);
		return p.get();
	}

	public List<Address> findAddresses(int id) {
		return rep.findDoctorAddresses(id);
	}

}
