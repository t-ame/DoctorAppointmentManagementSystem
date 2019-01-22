package com.java.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Convert;

import org.springframework.hateoas.ResourceSupport;

import com.java.util.StatusConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "appointment")
@Data
@EqualsAndHashCode(callSuper = true)
public class Appointment extends ResourceSupport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7166504413724346172L;
	private int appointmentId;
	private LocalDateTime appointmentStartTime;
	private LocalDateTime appointmentEndTime;
	private String patientEmailId;
	private String doctorEmailId;
	private String clinicAddress;
	private String doctorName;

	@Builder.Default
	@Convert(converter = StatusConverter.class)
	private AppointmentStatus status = AppointmentStatus.BOOKED;

}
