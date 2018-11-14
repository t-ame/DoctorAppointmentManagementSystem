package com.java.dto;

import java.time.LocalTime;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class AvailableTime {

	private LocalTime morningStartTime;
	private LocalTime morningEndTime;
	private LocalTime afternoonStartTime;
	private LocalTime afternoonEndTime;

}
