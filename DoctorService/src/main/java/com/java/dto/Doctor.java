package com.java.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

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
@Builder(builderMethodName = "doctor")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@SQLDelete(sql = "update Doctor set active=false where doctorId= ?")
@JsonFilter("filterName")
@Entity
public class Doctor extends ResourceSupport {
	@Id
	@GeneratedValue
//	@JsonIgnore
	int doctorId;
	@NotEmpty
	private String firstName;
	@NotEmpty
	private String lastName;
	@Email
	private String email;
	@Transient
	private String password;
	@Size(min = 7, max = 11)
	@Builder.Default
	private long mobileNumber = -1;
	@Past
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "doctor_speciality", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "speciality_id"))
	@Builder.Default
	private Set<Speciality> specialties = new HashSet<>();
	@ManyToMany
	List<Address> addresses;
	@Builder.Default
	boolean active = true;

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
