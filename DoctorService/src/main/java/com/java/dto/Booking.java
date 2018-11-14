package com.java.dto;

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
public class Booking {

	private LocalDate bookingDate;
	private LocalTime bookingTime;

}
