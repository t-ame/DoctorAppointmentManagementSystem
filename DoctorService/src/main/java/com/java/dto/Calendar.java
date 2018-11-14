package com.java.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.hateoas.ResourceSupport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@Entity
public class Calendar extends ResourceSupport {

	private int apptDurationMinutes = 15;

	@OneToOne
	private Schedule schedule = new Schedule().initializeWeekSchedule();

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Booking> bookings = new HashSet<>();

}
