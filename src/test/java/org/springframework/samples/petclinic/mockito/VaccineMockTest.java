package org.springframework.samples.petclinic.mockito;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.Repository.VaccinationRepository;
import org.springframework.samples.petclinic.Repository.VaccineTimePeriodRepository;
import org.springframework.samples.petclinic.UserController.VaccineController;
import org.springframework.samples.petclinic.model.Vaccination;
import org.springframework.samples.petclinic.model.VaccineTimePeriod;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.PetType;

import org.springframework.samples.petclinic.service.VaccinationService;

@SpringBootTest
public class VaccineMockTest {

	@Mock
	PetRepository petrepo;
	
	@Mock
	VaccinationRepository vaccinrepo;
	@Mock
	OwnerRepository ownerrepo;
	@Mock
	VaccineTimePeriodRepository vtprepo;
	
	@InjectMocks
	VaccinationService vs;
	
	@BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
	
	  private static final Logger LOGGER = LoggerFactory.getLogger(VaccineMockTest.class);
	
	@Test
	public void TestVaccinationSetForPet() {
		int loopcount=1;
		Owner owner = new Owner();
		owner.setId(999);
		owner.setFirstName("test");
		owner.setLastName("test");
		owner.setAddress("Street-1");
		owner.setCity("ggn");
		owner.setTelephone("1234567");
		Pet pet = new Pet();
		PetType dog = new PetType();
		dog.setName("dog");
		pet.setType(dog);
		pet.setName("Petdog");
		pet.setBirthDate(LocalDate.now());
		owner.addPet(pet);
		this.ownerrepo.save(owner);
		LOGGER.info("owner and pet data saved{}",owner.getId());
		
		VaccineTimePeriod vtp = new VaccineTimePeriod();
		Vaccination vaccine = new Vaccination(1,"abc",40,5,40,"mmHg",Collections.EMPTY_LIST);
		SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
		 this.vaccinrepo.save(vaccine);
		  vtp.setStartDate("11/04/2023");
		  Date date=null;
		try {
			date = DateFor.parse(vtp.getStartDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, vaccine.getTimePeriod());
			Date endDate = calendar.getTime();
			vtp.setEndDate(endDate);
			int shotinterval = vaccine.getTimePeriod() / vaccine.getNumberOfShots();
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);
			List<Date> shotDates = new ArrayList<>();
			LOGGER.info("dated set intiated");
			do

				if (calendar1.getTime().before(endDate)) {
					shotDates.add(calendar1.getTime());
					calendar1.add(Calendar.DAY_OF_MONTH, shotinterval);
					loopcount++;
				}
			while (loopcount <= vaccine.getNumberOfShots());

		vtp.setVaccination(vaccine);
		vtp.setPet(pet);
	this.vtprepo.save(vtp);
	List<VaccineTimePeriod> savedvtp = new ArrayList<>();
	savedvtp.add(vtp);
	LOGGER.info("vtp data saved {}",vtp.getEndDate());
		  when(ownerrepo.findById(anyInt())).thenReturn(owner);
		  when(vaccinrepo.findByVaccineName(anyString())).thenReturn(vaccine);
		  when(petrepo.save(any(Pet.class))).thenReturn(pet);
		  LOGGER.info("fetching pet data ");
		 
		  when(vaccinrepo.save(any(Vaccination.class))).thenReturn(vaccine);
		   when(vtprepo.save(any(VaccineTimePeriod.class))).thenReturn(vtp);
		 
		 
		  LOGGER.info("calling service method");
 VaccineTimePeriod actualVTP = vs.setVaccinationDetailForPet(999, "Petdog", "abc", vtp.getStartDate());
 LOGGER.info("data returned from method{}",actualVTP.getStartDate());
 assertEquals(vtp.getEndDate(),actualVTP.getEndDate());

	}
		
		
	
}
