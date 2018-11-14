package com.java.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "address")
@Entity
public class Address {

	@Id
	@GeneratedValue
	private int addressId;

	private String line1;
	private String line2;
	private String city;
	private String state;
	private String country;
	@Length(min = 5, max = 5)
	private String zipcode;

}
