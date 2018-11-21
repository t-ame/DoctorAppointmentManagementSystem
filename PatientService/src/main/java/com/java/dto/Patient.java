package com.java.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "patient")
@Entity
@DynamicUpdate
@SQLDelete(sql = "update Patient set enabled=false where patientId= ?")
@JsonFilter("filterName")
public class Patient extends ResourceSupport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3954113168734569845L;

	@Id
	@GeneratedValue
	int patientId;

	@Email
	@NotNull
	private String email;
	@Transient
	private String password;
	@NotBlank
	private String firstName;
	@NotBlank
	private String lastName;
	@Builder.Default
	private long mobileNumber = -1;
	@Past
	private LocalDate dob;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Builder.Default
	private boolean enabled = true;
	@ManyToMany(cascade = CascadeType.PERSIST)
	@Builder.Default
	List<Address> addresses = new ArrayList<>();

//	================================= FILTER DEFINITIONS ===================================================

	public static SimpleFilterProvider filterOutPassword() {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("email", "firstName", "lastName",
				"mobileNumber", "dob", "gender", "links", "enabled");
		return new SimpleFilterProvider().addFilter("filterName", filter);
	}

	public static SimpleFilterProvider filterOutExtras() {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("email", "firstName", "lastName",
				"links", "enabled");
		return new SimpleFilterProvider().addFilter("filterName", filter);
	}

}
