package com.java.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "specialty")
@Data
@Entity
@DynamicUpdate
public class Specialty {

	@Id
	@GeneratedValue
	private int specialtyId;
	@NotBlank
	private String name;

}
