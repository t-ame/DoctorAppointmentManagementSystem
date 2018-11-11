package com.java.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import com.java.dto.Appointment;
import com.java.dto.AppointmentStatus;

/*POST-ing a Spring Data REST resource, emits a BeforeCreateEvent.
 *  To catch this event, the method handleBeforeSave must be annotated 
 *  with @HandleBeforeCreate instead of @HandleBeforeSave (the latter 
 *  gets invoked on PUT and PATCH HTTP calls).*/
@Component
@RepositoryEventHandler(Appointment.class)
public class BookingEventHandler {

	{
		System.out.println("handler");
	}

	@Autowired
	JmsTemplate template;

	/*
	 * https://www.baeldung.com/spring-data-rest-events
	 */
	@HandleAfterDelete
	public void handleAppointmentAfterDelete(Appointment appointment) {
		System.out.println("invoked");
		GenericMessage<Appointment> message = new GenericMessage<Appointment>(appointment);
		appointment.setStatus(AppointmentStatus.CANCELLED);
		template.convertAndSend("appointment-queue", message);
	}
	
	@HandleAfterCreate
	public void handleAppointmentAfterCreate(Appointment appointment) {
		System.out.println("invoked");
		GenericMessage<Appointment> message = new GenericMessage<Appointment>(appointment);
		template.convertAndSend("appointment-queue", message);
	}

}

//Notification service: mails to the recipients| doctor & patient| mail ids
