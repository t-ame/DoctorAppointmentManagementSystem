# Health Care Management Application
Base repository containing links to the different repositories for the micro services and angular application that make up this application.

## Outline:
* [Description](#description)
* [Features](#features)
* [Services](#services)
* [Usage](#usage)


## Description
This application allows patients to find doctors around them and book and manage appointments. The application provides multiple functionalities, including:
- Patient can search for doctors based on the doctor's clinic location and the doctor's specialty.
- patient can book an appointment with a doctor
- patient cancel, view and edit appointments
- doctor cannot register directly, must be added by admin for security reasons
- doctor can update availability schedule
- doctor can apply for leave and view leave status
- doctor can view calendar and view appointments
- admin can add/edit/delete a doctor
- admin can view all upcoming appointments
- admin can view/approve/reject leave applications
- patients receive email notifications when an appointment is booked successfully or cancelled successfully

## Features
This application is implemented using micro services and angularCLI and has the following features:
- the application is done using `spring boot`
- The micro services are hosted on `eureka server`,
- a `config server` is used for service configuration files
- `zuul` is used for api gateway
- `sleuth` and `zipkin` are used for monitoring cross-service activities
- `ehcache` is used for service level caching
- `hystrix` is used to provide fall back methods for the controller handlers
- `spring boot data rest` is used to automatically expose the rest api for the jpa repositories
- `activeMQ` is used as a broker between the appointment service and notification service
- `JMS` is used to send and recieve appointment status change between application and notification sevices
- emails are sent using `spring email` ***
- `AWS S3` is used to store the profile images of the doctors and patient records ***
- `AWS RDS` is used for the sql database


## Services
- [Eureka service](https://github.com/tamechi/DoctorAppointmentManagementSystem/tree/master/EurekaService) 
- [Config service](https://github.com/tamechi/DoctorAppointmentManagementSystem/tree/master/ConfigService) 
- [Config server](#) - `Configuration server`
- [ApiGateway service](https://github.com/tamechi/DoctorAppointmentManagementSystem/tree/master/ApiGatewayService)  
- [User service](https://github.com/tamechi/DoctorAppointmentManagementSystem/tree/master/UserService) 
- [Admin service](https://github.com/tamechi/DoctorAppointmentManagementSystem/tree/master/AdminService)
- [Doctor service](https://github.com/tamechi/DoctorAppointmentManagementSystem/tree/master/DoctorService) 
- [Patient service](https://github.com/tamechi/DoctorAppointmentManagementSystem/tree/master/PatientService)
- [Appointment service](https://github.com/tamechi/DoctorAppointmentManagementSystem/tree/master/AppointmentService)
- [Notification service](https://github.com/tamechi/DoctorAppointmentManagementSystem/tree/master/NotificationService) 
- [Leave service](https://github.com/tamechi/DoctorAppointmentManagementSystem/tree/master/LeaveService)
- [Angular code](#)


## Usage
Steps must be followed chronologically as some services depend on the others
- Fork the repositories using the links provided in the [links](#repository-links) section of this document (you do not need to clone the Config server repository as this just holds the configuration files for the micro services), and clone them into your computer.
- open the `bootstrap.yml` file in the config service and change the spring.cloud.config.server.git.url to the url of your forked copy of the config server.
```
spring:
  application:
    name: config-service
  config:
    name: config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/tamechi/DoctorBookingApplicationConfigServer
```
- Add the database properties for your databases to the config files in your config server
- If you don't have [lombok](https://projectlombok.org/setup/eclipse) installed, install it in your system. I used Eclipse.
- start [activeMQ](http://activemq.apache.org/getting-started.html); used by the appointment and notification services
- If you want to view inter-service communication statistics, download and start [zipkin](https://zipkin.io/pages/quickstart.html), then open the UI page on your browser using the provided port number.
- Start the Erureka service
- Start the config service
- Start the ApiGateway service
- Start all the other services
- Start the angular project
- Start the application by opening the page using your angular port on your browser
- Shuting down : shut down the angular application, shut down all the services in no particular order, shut down your activeMQ

