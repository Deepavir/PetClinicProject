package org.springframework.samples.petclinic.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Repository.VaccinationRepository;
import org.springframework.samples.petclinic.Repository.VaccineTimePeriodRepository;
import org.springframework.samples.petclinic.model.Vaccination;
import org.springframework.samples.petclinic.model.VaccineTimePeriod;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class VaccinationService {

	@Autowired
	private VaccinationRepository vaccinrepo;

	@Autowired
	private OwnerRepository ownerrepo;

	@Autowired
	private PetRepository petrepo;
	
	@Autowired
	private VaccineTimePeriodRepository vtprepo;

	/**
	 * This method sets the vaccination detail for the pet of the owner.
	 * 
	 * @param id          -ownerid to get owner data
	 * @param petname     -to get pet data from owner.
	 * @param vaccinename - to get vaccine detail.
	 * @param startdate   - to get when owner wants to start vaccination
	 */
	public void setVaccinationDetailForPet(int id, String petname, String vaccinename, String startdate) {

		Owner owner = this.ownerrepo.findById(id);

		Pet pet = owner.getPet(petname);

		if (pet != null) {
			System.out.println("into method =pet found" + pet.getName());

			Vaccination vaccine = this.vaccinrepo.findByVaccineName(vaccinename);

			VaccineTimePeriod vtp = new VaccineTimePeriod();
		

			vtp.setStartDate(startdate);

			boolean isVaccineDataSaved = saveVaccineData(vaccine, vtp);

			if (isVaccineDataSaved) {

		      vtp.setVaccination(vaccine);
				this.petrepo.save(pet);
				vtp.setPet(pet);
				this.vtprepo.save(vtp);

			}
		}

	} 

	/**This method set endate and dates of vaccine shot as per provided start date from owner.
	 * @param vaccine-to get vaccine data
	 * @param vtp - to get vaccine start date and set the remaining data of vaccinetime period entity
	 * @return true if information saved successfully .
	 */
	private boolean saveVaccineData(Vaccination vaccine, VaccineTimePeriod vtp) {
		int loopcount = 1;
		boolean f;

		
		SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
		try {

			Date date = DateFor.parse(vtp.getStartDate());
			System.out.println("Date : " + date);
			// setting enddate after parsing time period of that vaccine
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, vaccine.getTimePeriod());
			Date endDate = calendar.getTime();
			vtp.setEndDate(endDate);
			// while startdate and enddate is set need to set shot dates
			int shotinterval = vaccine.getTimePeriod() / vaccine.getNumberOfShots();
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);
			List<Date> shotDates = new ArrayList<>();

			do

				if (calendar1.getTime().before(endDate)) {
					shotDates.add(calendar1.getTime());
					calendar1.add(Calendar.DAY_OF_MONTH, shotinterval);
					loopcount++;
				}
			while (loopcount <= vaccine.getNumberOfShots());

			// add the last shot date if it falls within the range
			/*
			 * if (calendar1.getTime().getTime() <= endDate.getTime()) {
			 * shotDates.add(calendar1.getTime()); }
			 */

			// set the shot dates in the VaccineTime model
			vtp.setVaccineDate(shotDates);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		// vaccine.setVaccineName("abcd");
		// accine.setVolume(20);
		this.vtprepo.save(vtp);
	List<VaccineTimePeriod> vtp2=  vaccine.getVaccineTimes();
		vtp2.add(vtp);
		
		vaccine.setVaccineTimes(vtp2);
		this.vaccinrepo.save(vaccine);
		f = true;
		// set endDate as the end date in the VaccineTime model
		return f;
	}

}
