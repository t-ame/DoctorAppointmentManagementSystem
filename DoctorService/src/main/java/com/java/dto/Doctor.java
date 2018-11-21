package com.java.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
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
@Builder(builderMethodName = "doctor")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@SQLDelete(sql = "update Doctor set enabled=false where doctorId= ?")
@JsonFilter("filterName")
@Entity
public class Doctor extends ResourceSupport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6455355432258831892L;

	@Id
	@GeneratedValue
	private int doctorId;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@Email
	@NotNull
	private String email;
	@Transient
	private String password;
	@Builder.Default
	private long mobileNumber = -1;
	@Past
	private LocalDate dob;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@Builder.Default
	private Set<Specialty> specialties = new HashSet<>();
	@ManyToMany(cascade = CascadeType.PERSIST)
	@Builder.Default
	List<Address> addresses = new ArrayList<>();
	@Builder.Default
	private boolean enabled = true;
	@OneToOne(cascade = CascadeType.PERSIST)
	private Calendar calendar;

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
