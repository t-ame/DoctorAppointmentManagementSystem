package com.java.dto;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Patient {
	@Id
	@GeneratedValue
	int id;
	private String firstName;
	private String lastName;
	private String emailId;
	private long phoneNumber;
	private LocalDateTime dob;
	//multipart file upload
	
	@ElementCollection
	private List<File> medicalRecords= new ArrayList<>();
}

//medical history