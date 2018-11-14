package com.java.util;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
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

	@Autowired
	ConnectionFactory factory;

	/*
	 * https://www.baeldung.com/spring-data-rest-events
	 */
//	@HandleAfterDelete
//	public GenericMessage<Appointment> handleAppointmentAfterDelete(Appointment appointment) {
//		System.out.println("delete invoked");
//		GenericMessage<Appointment> message = new GenericMessage<Appointment>(appointment);
//
////		MyMessage m = new MyMessage();
////		m.setHead(appointment.getPatientEmailId());
////		m.setBody(appointment.getDoctorName());
////		GenericMessage<MyMessage> message = new GenericMessage<MyMessage>(m);
//
//		appointment.setStatus(AppointmentStatus.CANCELLED);
//		template.convertAndSend("appointment-queue", message);
//
//		return message;
//
//	}

//	@HandleAfterCreate
//	public GenericMessage<Appointment> handleAppointmentAfterCreate(Appointment appointment) {
//		System.out.println("create invoked");
//		GenericMessage<Appointment> message = new GenericMessage<Appointment>(appointment);
//
////		MyMessage m = new MyMessage();
////		m.setHead(appointment.getPatientEmailId());
////		m.setBody(appointment.getDoctorName());
////		GenericMessage<MyMessage> message = new GenericMessage<MyMessage>(m);
//
//		template.convertAndSend("appointment-queue", message);
//
//
//		return message;
//	}

	@HandleAfterCreate
	@HandleAfterDelete
	@HandleAfterSave
	public void handleAppointmentAfterCreate(Appointment appointment) {
		Gson gson = new Gson();
//		template.convertAndSend("appointment.queue", gson.toJson(appointment));
		System.out.println("created");
		template.send("appointment-queue", new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(gson.toJson(appointment));
			}
		});
	}

}
