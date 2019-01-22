package com.java.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.java.dao.AppointmentRepository;
import com.java.dto.Appointment;
import com.java.dto.AppointmentStatus;

@Service
@Transactional
public class AppointmentService {

	@Autowired
	AppointmentRepository rep;

	/**
	 * Finds an appointment by Id.
	 *
	 * @param id must not be null.
	 * @return the appointment with the given id or Optional#empty() if none found.
	 * 
	 * @author Toya Amechi
	 */
	@Cacheable(value = "appointmentCache", key = "#id", unless = "#result==null")
	public Appointment findAppointmentById(int id) {
		Optional<Appointment> optional = rep.findById(id);
		return optional.get();
	}

	/**
	 * Checks if a new {@link Appointment} overlaps with a pre-existing one and if
	 * not, saves the {@link Appointment} to the database.
	 *
	 * @param appt new {@link Appointment} to be added. Must not be {@literal null}.
	 * @return {@literal null} if there is an overlap and otherwise, the saved
	 *         {@link Appointment}.
	 * 
	 * @author Toya Amechi
	 */

	@Caching(evict = { @CacheEvict(condition="#result!=null", value = "appointmentsCache", allEntries = true) }, put = {
			@CachePut(value = "appointmentCache", key = "#result.appointmentId", unless = "#result==null") })
	public Appointment addAppointment(Appointment appt) {
		if (rep.findByTimeOverlap(appt.getAppointmentStartTime(), appt.getAppointmentEndTime()).size() > 0) {
			return null;
		}
		return rep.save(appt);
	}

	/**
	 * Fallback for adding a new {@link Appointment} to the database.
	 *
	 * @param appt new {@link Appointment} to be added. Must not be {@literal null}.
	 * @return {@literal null} if there is an overlap and otherwise, the saved
	 *         {@link Appointment}.
	 * 
	 * @author Toya Amechi
	 */

	@Cacheable(value = "appointmentCache", key = "#appt.appointmentId", unless = "#result==null")
	public Appointment addAppointment2(Appointment appt) {
		return addAppointment(appt);
	}

	/**
	 * Cancel an existing {@link Appointment}.
	 *
	 * @param appt new {@link Appointment} to be deleted. Must not be
	 *             {@literal null}.
	 * @return {@literal void}.
	 * 
	 * @author Toya Amechi
	 */

	@Caching(evict = { @CacheEvict(value = "appointmentCache", key = "#appt.appointmentId"),
			@CacheEvict(condition="#appt.appointmentId>0", value = "appointmentsCache", allEntries = true) })
	public void cancelAppointment(Appointment appt) {
		rep.delete(appt);
	}

	/**
	 * Update the start and end time of an existing {@link Appointment}.
	 *
	 * @param appt new {@link Appointment} to be updated. Must not be
	 *             {@literal null}.
	 * @return {@literal null} if the appointment Id is not valid or if there is an
	 *         overlap with the new time, else the updated {@link Appointment}.
	 * 
	 * @author Toya Amechi
	 */

	@Caching(evict = { @CacheEvict(condition="#result!=null", value = "appointmentsCache", allEntries = true) }, put = {
			@CachePut(value = "appointmentCache", key = "#result.appointmentId", unless = "#result==null") })
	public Appointment updateAppointment(Appointment appt) {
		if (appt.getAppointmentId() <= 0)
			return null;

		Optional<Appointment> optional = rep.findById(appt.getAppointmentId());
		Appointment oldAppt = optional.get();
		if (oldAppt == null)
			return null;
		if (appt.getAppointmentStartTime() != null) {
			oldAppt.setAppointmentStartTime(appt.getAppointmentStartTime());
		}
		if (appt.getAppointmentEndTime() != null) {
			oldAppt.setAppointmentEndTime(appt.getAppointmentEndTime());
		}

		return addAppointment(oldAppt);
	}
	

	/**
	 * Fallback method for updating the start and end time of an existing {@link Appointment}.
	 *
	 * @param appt new {@link Appointment} to be updated. Must not be
	 *             {@literal null}.
	 * @return {@literal null} if the appointment Id is not valid or if there is an
	 *         overlap with the new time, else the updated {@link Appointment}.
	 * 
	 * @author Toya Amechi
	 */

	@Cacheable(value = "appointmentCache", key = "#appt.appointmentId", unless = "#result==null")
	public Appointment updateAppointment2(Appointment appt) {
		return updateAppointment(appt);
	}

	/**
	 * Fetches a {@link Page} of booked appointments, based on the doctor email Id
	 * and the start date of the appointment, sorted by the appointment time.
	 *
	 * @param doctorEmailId     email Id of doctor.
	 * @param dateOfAppointment appointment date.
	 * @param page              zero-based page index.
	 * @param size              the size of the page to be returned.
	 * @return {@link Page} of appointments.
	 * 
	 * @author Toya Amechi
	 */
	public Page<Appointment> findByDoctorEmailIdAndDateOfAppointment(String doctorEmailId, LocalDate dateOfAppointment,
			int page, int size) {
		return rep.findByDoctorEmailIdAndAppointmentDateAndStatus(doctorEmailId, dateOfAppointment,
				AppointmentStatus.BOOKED,
				PageRequest.of(page, size, new Sort(Sort.Direction.ASC, "appointmentStartTime")));
	}

	/**
	 * Fetches a {@link Page} of booked appointments, based on the patient email Id,
	 * sorted by the appointment time.
	 *
	 * @param patientEmailId email Id of patient.
	 * @param page           zero-based page index.
	 * @param size           the size of the page to be returned.
	 * @return {@link Page} of appointments.
	 * 
	 * @author Toya Amechi
	 */
	@Cacheable(value = "appointmentsCache", unless = "#result==null")
	public Page<Appointment> findPatientBooked(String patientEmailId, int page, int size) {
		return rep.findByPatientEmailIdAndStatus(patientEmailId, AppointmentStatus.BOOKED,
				PageRequest.of(page, size, new Sort(Sort.Direction.ASC, "appointmentStartTime")));
	}

	/**
	 * Fetches a {@link Page} of non-cancelled appointments, based on the patient
	 * email Id, sorted by the appointment time.
	 *
	 * @param patientEmailId email Id of patient.
	 * @param page           zero-based page index.
	 * @param size           the size of the page to be returned.
	 * @return {@link Page} of appointments.
	 * 
	 * @author Toya Amechi
	 */
	public Page<Appointment> findAllPatient(String patientEmailId, int page, int size) {
		return rep.findByPatientEmailIdAndStatusNot(patientEmailId, AppointmentStatus.CANCELLED,
				PageRequest.of(page, size, new Sort(Sort.Direction.ASC, "appointmentStartTime")));
	}

	/**
	 * Fetches a {@link Page} of all booked appointments.
	 *
	 * @param page zero-based page index.
	 * @param size the size of the page to be returned.
	 * @return {@link Page} of appointments.
	 * 
	 * @author Toya Amechi
	 */
	@Cacheable(value = "appointmentsCache", unless = "#result==null")
	public Page<Appointment> findAllBooked(int page, int size) {
		return rep.findByStatusIs(AppointmentStatus.BOOKED,
				PageRequest.of(page, size, new Sort(Sort.Direction.ASC, "appointmentStartTime")));
	}

}
