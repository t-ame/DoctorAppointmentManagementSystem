package com.java.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "address")
@Entity
@DynamicUpdate
public class Address {

	@Id
	@GeneratedValue
	private long addressId;

	private String line1;
	@Column(nullable = true)
	private String line2;
	private String city;
	private String state;
	private String country;
	private String zipcode;

}
