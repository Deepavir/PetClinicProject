package org.springframework.samples.petclinic.service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Repository.AppointmentRepository;
import org.springframework.samples.petclinic.Repository.VaccineTimePeriodRepository;
import org.springframework.samples.petclinic.entity.Appointment;
import org.springframework.samples.petclinic.model.Vaccination;
import org.springframework.samples.petclinic.model.VaccineTimePeriod;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepositoryy;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private OwnerRepositoryy ownerrepo;
	@Autowired
	private AppointmentRepository appointmentrepo;

	@Autowired
	private VaccineTimePeriodRepository vtprepo;
	@Autowired
	private PetRepository petrepo;

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

	public boolean sendConfirmationEmail(String email, Date appdate) {
		boolean isMailSent = false;
		String to = email;
		// variable for gmail host
		String from = "virmani.deepa@gmail.com";

		String host = "smtp.gmail.com";
		// get system property
		Properties properties = System.getProperties();
		System.out.println(properties);
		// setting imp imformation to properties

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		// Step-1:to get session object
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("virmani.deepa@gmail.com", "mwgeibgrrrqsdvpw");
			}
		});
		session.setDebug(true);
		// compose the message
		MimeMessage msg = new MimeMessage(session);
		// from email id
		try {
			msg.setFrom(from);
			// adding recipient to msg
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// adding subject to message
			msg.setSubject("Confirmation of appointment");
			// adding text
			msg.setText("your appointment is confirmed");  
			// send the message using transport class
			Transport.send(msg);
			System.out.println("sent successfully");
			isMailSent = true;

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isMailSent;
	}

	/**
	 * This method is scheduled to run every 4 minutes to send appointment
	 * confirmation email to owner whose isconfirmed is set true and mailtrigger is
	 * set to false.
	 */
	// @Scheduled(cron = "0 0/4 * * * *")
	public void sendEmailService() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		String strDate = sdf.format(now);
		log.info("Java cron job expression::{} ", strDate);
		log.info("Complete appointment data being fetched from database");

		List<Appointment> completeAppointmentData = this.appointmentrepo.findAll();

		for (int datacount = 0; datacount < completeAppointmentData.size(); datacount++) {

			log.info("inside the loop");

			Appointment Appointmentdatabyindex = new Appointment();
			Appointmentdatabyindex = completeAppointmentData.get(datacount);

			if (Appointmentdatabyindex.isConfirmed() && Appointmentdatabyindex.isMailTriggered() == false) {

				Owner owner = Appointmentdatabyindex.getOwner();
				log.info("mail inititated for user {}", owner.getFirstName());
				// boolean mailsent = sendConfirmationEmail(owner.getEmail(),
				// Appointmentdatabyindex.getAppointmentdate());

				/*
				 * if (mailsent) { log.info("mail sent successfully");
				 * Appointmentdatabyindex.setMailTriggered(true); owner.setNotifiedDate(new
				 * Date()); }
				 */
				List<Appointment> finalAppointmentData = new ArrayList<>();
				finalAppointmentData.add(Appointmentdatabyindex);
				owner.setAppointment(finalAppointmentData);
				this.ownerrepo.save(owner);

				log.info("user data saved successully {}", owner.getFirstName());

			} else {
				log.info("No appointment data to notify");
			}
		}
		log.info("cron job completed succussfully");

	}

	public boolean sendReminderEmail(String email ,String ownername,String petname ,Date  vaccinedate) {
		boolean isMailSent = false;
		String to = email;
		// variable for gmail host
		String from = "noreply@petclinic.com";

		String host = "smtp.gmail.com";
		// get system property
		Properties properties = System.getProperties();
		System.out.println(properties);
		// setting imp imformation to properties

		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		// Step-1:to get session object
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("virmani.deepa@gmail.com", "mwgeibgrrrqsdvpw");
			}
		});
		session.setDebug(true);
		// compose the message
		MimeMessage msg = new MimeMessage(session);
		// from email id
		try {
			msg.setFrom(from);
			// adding recipient to msg
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			// adding subject to message
			msg.setSubject("Appointment Reminder");
			// adding text
			msg.setContent(ConstantProperty.getMailBodyFirstPart(email, vaccinedate,petname,ownername),"text/html");  
			// send the message using transport class
			Transport.send(msg);
			System.out.println("sent successfully");
			isMailSent = true;

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isMailSent;
	}

	// @Scheduled(cron = "0 0/2 * * * *")
	/**
	 * This method scheduled to send reminder mail to owner if pet vaccination date
	 * and current date difference is 2 days ,mail need to be sent.
	 * 
	 */
	//@Scheduled(cron = "0 0/2 * * * *")
	public void sendReminderMailForVaccination() {
		List<VaccineTimePeriod> vtp = this.vtprepo.findAll();
		log.info("list of all vaccinetimeperiod fetched ");
		for (int i = 0; i < vtp.size(); i++) {

			Vaccination vaccine =vtp.get(i).getVaccination();
			log.info("vaccine data fetched for {}",vtp.get(i).getVaccination().getVaccineName());
			Pet pet = vtp.get(i).getPet();
			log.info("Pet name  is {}", pet.getName());
			List<Date> vaccinedate = vtp.get(i).getVaccineDate();
			log.info("vaccine dates fetched for the vaccine time period {}",i);
			Owner owner = pet.getOwner();
			log.info("owner email is {}", owner.getEmail());
					for (Date vaccinedate1 : vaccinedate) {
						
						log.info("vaccinedate is {}", vaccinedate1);

						Date currentdate = new Date();
						long diffInMillies = Math.abs(vaccinedate1.getTime() - currentdate.getTime());
						long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

						log.info("get difference for date {} and diff is {}", vaccinedate1, diffInDays);
						if (diffInDays == 2) {
							log.info("mail sending  to owner {}", owner.getEmail());
							String name = owner.getFirstName() +owner.getLastName();
							boolean mailsent = sendReminderEmail(owner.getEmail(), name,pet.getName(),vaccinedate1); 

						} else {
							log.info("mail not sent.day difference is not 2");
						}
					
				}
		}log.info("scheduling completed");

			
		
	}
}
	
	
