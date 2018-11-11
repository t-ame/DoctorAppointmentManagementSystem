package com.java.dto;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "patient")
@Entity
@DynamicUpdate
@SQLDelete(sql = "update Patient set active=false where patientId= ?")
public class Patient {
	@Id
	@GeneratedValue
	int patientId;

	private String email;
	private String password;

	private String firstName;
	private String lastName;
	private long mobileNumber;
	private LocalDateTime dob;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Builder.Default
	boolean active = true;
	@OneToMany
	Address address;

	// NOT SURE IF I NEED THIS...
	@ElementCollection
	@Builder.Default
	private List<File> medicalRecords = new ArrayList<>();
}
