package org.springframework.samples.petclinic.service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
			msg.setText("Your appointment confirmed for " + appdate);
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
				//boolean mailsent = sendConfirmationEmail(owner.getEmail(), Appointmentdatabyindex.getAppointmentdate());

			/*	if (mailsent) {
					log.info("mail sent successfully");
					Appointmentdatabyindex.setMailTriggered(true);
					owner.setNotifiedDate(new Date());
				}*/
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

	//@Scheduled(cron = "0 0/2 * * * *")
	/**This method scheduled to send reminder mail to owner 
	 * if pet vaccination date and current date difference is 2 days ,mail need to be sent.
	 * 
	 */
	//fetch complete vtp data =>get all vaccinedates from vtp(i) . 
	public void sendReminderMailForVaccination() {
		log.info("into sendReminderMailForVaccination");

		List<VaccineTimePeriod> vtp2 = this.vtprepo.findAll();

		log.info("list of complete vaccinetimeperiod data fetched ");

		for (int datacount = 0; datacount < vtp2.size(); datacount++) {
			
		//	log.info("inside loop to iterate through the vaccinetimeperiod data");
		   Vaccination vaccine = vtp2.get(datacount).getVaccination();
		  
			List<Date> vaccinedate = vtp2.get(datacount).getVaccineDate();

			log.info("Fetched list of vaccindedate data from vaccinetimeperiod");
			//Vaccination vaccine = vtp2.get(datacount).getVaccination();
			log.info("vaccine id {} for vaccinetime period{}", vaccine.getId(), vtp2.get(datacount).getId());

			List<Pet> pet = vaccine.getPet();
			Owner owner = null;
			log.info("getting pet data for the specified vaccine {} ,", pet.toString());
			for (int i = 0; i < pet.size(); i++) {
				log.info(" pet {}", pet.get(i).getName());
				 owner = pet.get(i).getOwner();
				log.info("getting owner data for the specified owner  {} of pet {} ,", owner.getFirstName(),
						pet.get(i).getName());
			

				log.info("List of vaccination dated for vaccine {}", vtp2.get(datacount).getId());
				//logic to get day difference of 2 days
				for (Date vaccinedate1 : vaccinedate) {
					log.info("vaccinedate is{}", vaccinedate1);
					LocalDate date = LocalDate.ofInstant(vaccinedate1.toInstant(), ZoneId.systemDefault());
					Period period = Period.between(date, LocalDate.now());
					int days = Math.abs(period.getDays());
					log.info("get difference for date{} and diff is {}", vaccinedate1, days);
					if (days == 2) {
						log.info("mail sending  to owner {}", owner.getEmail());
						boolean mailsent = sendConfirmationEmail(owner.getEmail(), vaccinedate1);

					} else {
						log.info("mail not sent.day difference is not 2");
					}
				}
				}

			
		}

		log.info("scheduling completed");
	}

}
