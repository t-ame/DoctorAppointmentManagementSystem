package com.java.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
public class Speciality {
	@Id
	@GeneratedValue
	private int id;
	@NotEmpty

	private String name;
}
