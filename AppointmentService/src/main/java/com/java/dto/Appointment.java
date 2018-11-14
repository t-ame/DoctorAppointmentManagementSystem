package com.java.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.util.StatusConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "appointment")
@Data
@Entity
@DynamicUpdate
@SQLDelete(sql = "update Appointment set status = 'CANCELLED' where appointmentId = ?")
public class Appointment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2286838562279317949L;
	@Id
	@GeneratedValue
	@JsonIgnore
	@Column(name = "appointmentId")
	private int appointmentId;
	@Future
	private LocalDateTime dateOfAppointment;
	@NotEmpty
	private String patientEmailId;
	@NotEmpty
	private String clinicAddress;
	@NotEmpty
	private String doctorEmailId;
	@NotEmpty
	private String doctorName;
	@NotEmpty
	@Builder.Default
	private String speciality = "General Physician";
	@Builder.Default
	@Convert(converter = StatusConverter.class)
	private AppointmentStatus status = AppointmentStatus.BOOKED;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Builder.Default
	private Set<Specialty> specialties = new HashSet<>();

}

//medical history