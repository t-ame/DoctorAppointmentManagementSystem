package com.java.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity
@DynamicUpdate
@SQLDelete(sql = "update Appointment set status = 'CANCELLED' where appointmentId = ?")
public class Appointment extends ResourceSupport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7166504413724346172L;
	@Id
	@GeneratedValue
	@JsonIgnore
	@Column(name = "appointmentId")
	private int appointmentId;
	@Future
	private LocalDateTime appointmentStartTime;
	@Future
	private LocalDateTime appointmentEndTime;
	@NotNull
	private String patientEmailId;
	@NotNull
	private String doctorEmailId;
	@NotNull
	private String clinicAddress;
	@NotNull
	private String doctorName;
	@Builder.Default
	@Convert(converter = StatusConverter.class)
	private AppointmentStatus status = AppointmentStatus.BOOKED;

}
