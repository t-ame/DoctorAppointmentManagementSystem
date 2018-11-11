package com.java.util;

import javax.persistence.AttributeConverter;

import org.springframework.stereotype.Component;

import com.java.dto.AppointmentStatus;

@Component
public class StatusConverter implements AttributeConverter<AppointmentStatus, String>{

	@Override
	public String convertToDatabaseColumn(AppointmentStatus attribute) {
		return attribute.name();
	}

	@Override
	public AppointmentStatus convertToEntityAttribute(String dbData) {
		return AppointmentStatus.valueOf(dbData);
	}

}
