package com.java.dto;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class Doctor {
	@Id
	@GeneratedValue
	@JsonIgnore
	int id;
	@NotEmpty
	private String firstName;
	@NotEmpty
	private String lastName;
	@Email
	private String emailId;
	@Size(min= 7, max= 11)
	private long phoneNumber;
	@Past
	//@JsonFormat(pattern="yyyy-Mon-dd")//serializer deserializer
	private LocalDate dob;
	@ManyToMany(fetch= FetchType.EAGER)
	@JoinTable(name="doctor_speciality", joinColumns= @JoinColumn(name="doctor_id"),
	inverseJoinColumns=@JoinColumn(name="speciality_id"))
	private Set<Speciality> specialities= new HashSet<>();
	
	
}

//medical history