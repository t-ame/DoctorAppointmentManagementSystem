package com.java.dto;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@DynamicUpdate
public class Schedule {

	@Embedded
	@Builder.Default
	private AvailableTime defaultAvailableTimes = new AvailableTime();
	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private Map<DayOfWeek, AvailableTime> weekSchedule = new HashMap<>();
	@Builder.Default
	private boolean saturdayAvailable = false;
	@Builder.Default
	private boolean sundayAvailable = false;

	public Schedule initializeWeekSchedule() {
		for (int i = 1; i < 6; ++i) {
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

}
