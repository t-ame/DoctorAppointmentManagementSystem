package com.java.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.java.service.EmailService;
import com.java.util.Email;

@Component
public class MyListener {

	@Autowired
	EmailService service;

	@JmsListener(destination = "appointment-queue")
	public void receiveAppointmentMessage(Message message) throws JMSException {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			System.out.println("Received: " + textMessage.getText());
			JSONObject object = new JSONObject(textMessage.getText());
			String content = getAppointment(object);
			Email patientEmail = new Email(object.getString("patientEmailId"),
					"Appointment " + object.getString("status"), content);
			service.sendEmail(patientEmail);
		} else {
			System.out.println("Invalid");
		}
	}

	@JmsListener(destination = "doctor-leave-queue")
	public void receiveDoctorRegistrationMessage(Message message) throws JMSException {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			System.out.println("Received: " + textMessage.getText());
			JSONObject object = new JSONObject(textMessage.getText());
			String content = getAppointment(object);
			Email patientEmail = new Email(object.getString("patientEmailId"),
					"Appointment " + object.getString("status"), content);
			service.sendEmail(patientEmail);
		} else {
			System.out.println("Invalid");
		}
	}

	private String getAppointment(JSONObject object) {
		Gson gson = new Gson();
		String content = "Appointment Id: " + object.getNumber("appointmentId") + "\n" + "Appointment Time: "
				+ gson.fromJson(
						object.getJSONObject("dateOfAppointment").getJSONObject("date").toString(), LocalDate.class)
				+ " "
				+ gson.fromJson(object.getJSONObject("dateOfAppointment").getJSONObject("time").toString(),
						LocalTime.class).format(DateTimeFormatter.ofPattern("hh:mm a"))
				+ "\n" + "Clinic address: " + object.getString("clinicAddress") + "\n" + "Doctor name: "
				+ object.getString("doctorName") + "\n" + "Speciality: " + object.getString("speciality");
		return content;
	}



	private String getRegistration(JSONObject object) {
		Gson gson = new Gson();
		String content = "Appointment Id: " + object.getNumber("appointmentId") + "\n" + "Appointment Time: "
				+ gson.fromJson(
						object.getJSONObject("dateOfAppointment").getJSONObject("date").toString(), LocalDate.class)
				+ " "
				+ gson.fromJson(object.getJSONObject("dateOfAppointment").getJSONObject("time").toString(),
						LocalTime.class).format(DateTimeFormatter.ofPattern("hh:mm a"))
				+ "\n" + "Clinic address: " + object.getString("clinicAddress") + "\n" + "Doctor name: "
				+ object.getString("doctorName") + "\n" + "Speciality: " + object.getString("speciality");
		return content;
	}
	
	
}
