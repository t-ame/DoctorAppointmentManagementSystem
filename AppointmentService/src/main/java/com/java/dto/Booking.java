package com.java.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class Booking implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2130461122080469842L;
	private int appointmentId;
	private LocalDate bookingStartDate;
	private LocalTime bookingStartTime;
	private LocalDate bookingEndDate;
	private LocalTime bookingEndTime;

}
