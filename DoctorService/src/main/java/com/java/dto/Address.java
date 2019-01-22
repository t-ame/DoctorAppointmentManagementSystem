package com.java.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.ResourceSupport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "address")
@Entity
public class Address  extends ResourceSupport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2617361815987269346L;

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
