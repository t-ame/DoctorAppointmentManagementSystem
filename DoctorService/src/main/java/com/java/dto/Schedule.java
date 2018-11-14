package com.java.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Schedule {

	@Embedded
	private AvailableTime defaultAvailableTimes = new AvailableTime();
	@ElementCollection(fetch = FetchType.EAGER)
	private Map<DayOfWeek, AvailableTime> weekSchedule = new HashMap<>();
	private boolean saturdayAvailable;
	private boolean sundayAvailable;

	public Schedule initializeWeekSchedule() {
		for (int i = 2; i <= 6; ++i) {
			weekSchedule.put(DayOfWeek.of(i), defaultAvailableTimes);
		}
		if (saturdayAvailable) {
			weekSchedule.put(DayOfWeek.SATURDAY, defaultAvailableTimes);
		} else {
			weekSchedule.put(DayOfWeek.SATURDAY, new AvailableTime());
		}
		if (sundayAvailable) {
			weekSchedule.put(DayOfWeek.SUNDAY, defaultAvailableTimes);
		} else {
			weekSchedule.put(DayOfWeek.SUNDAY, new AvailableTime());
		}
		return this;
	}

	public void removeAvailableDay(int day) {
		weekSchedule.put(DayOfWeek.of(day), new AvailableTime());
	}

	public void setAvailbleDay(int day) {
		weekSchedule.put(DayOfWeek.of(day), defaultAvailableTimes);
	}

	/*
	 * @TODO
	 */
	public void clearDays(LocalDate start, LocalDate end) {

	}

	/*
	 * @TODO
	 */
	public void bookTime(LocalTime apptTime) {

	}

	/*
	 * @TODO
	 */
	public void clearTime(LocalTime apptTime) {

	}

}
