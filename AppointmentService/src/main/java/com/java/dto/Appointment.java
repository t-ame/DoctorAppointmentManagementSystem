package com.java.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.SQLDelete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.util.StatusConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/*I'm using spring-boot-starter-parent and spring-boot-starter-data-jpa (version 1.1.9). I found some strange behaviour on entities. When I specify @column(name = "myName") then jpa is generating sql query containing "my_name" column (so it changes the name I provide).

Other examples:

@Column(name = "aa") -> sql query uses column "aa" -> correct
@Column(name = "aA") -> sql query uses column "aa" -> incorrect
@Column(name = "aAa") -> sql query uses column "a_aa" -> incorrect

I found the issue it is in ImprovedNamingStrategy if you want to solve it without details use DefaultNamingStrategy instead ImprovedNamingStrategy as below

spring.jpa.hibernate.naming-strategy=org.hibernate.cfg.DefaultNamingStrategy
Details:
There are 2 methods in NamingStrategy interface propertyToColumnName and columnName, first one called if you didn't specify name in @Column and 2nd one called if you specify name in @Column.

in ImprovedNamingStrategy both change the name from camel to _ format while in DefaultNamingStrategy it is returning name as it is in 2nd method which is from my point of view the correct behavior.*/
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName="appointment")
@Data
@Entity
@SQLDelete(sql="update Appointment set status = 'Cancelled' where appointmentId = ?")
public class Appointment implements Serializable{
	private static final long serialVersionUID = 1342910184120806871L;
	@Id
	@GeneratedValue
	@JsonIgnore
	@Column(name="appointmentId")
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
	private String speciality="General Physician";
	@Builder.Default
//	@Enumerated(EnumType.STRING)
	@Convert(converter=StatusConverter.class)
	private AppointmentStatus status=AppointmentStatus.BOOKED; 
}

//medical history