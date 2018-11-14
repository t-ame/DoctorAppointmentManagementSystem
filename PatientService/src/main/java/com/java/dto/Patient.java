package com.java.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@SQLDelete(sql = "update Patient set active=false where patientId= ?")
@JsonFilter("filterName")
public class Patient extends ResourceSupport {
	@Id
	@GeneratedValue
	int patientId;

	@Email
	@NotBlank
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
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime dob;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Builder.Default
	boolean active = true;
	@ManyToMany
	List<Address> addresses;

//	// NOT SURE IF I NEED THIS...
//	@ElementCollection
//	@Builder.Default
//	private List<File> medicalRecords = new ArrayList<>();

	public static SimpleFilterProvider filterOutPassword() {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("email", "firstName", "lastName",
				"mobileNumber", "dob", "gender");
		return new SimpleFilterProvider().addFilter("filterName", filter);
	}

	public static SimpleFilterProvider filterOutExtras() {
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("email", "firstName", "lastName");
		return new SimpleFilterProvider().addFilter("filterName", filter);
	}

}
