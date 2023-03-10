package org.springframework.samples.petclinic.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.entity.VaccineRequestBody;
import org.springframework.samples.petclinic.service.VaccinationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class VaccineController {

	@Autowired
	private VaccinationService vs;

	@PostMapping("/vaccine/{id}")
	public VaccineRequestBody addVaccineDetail(@PathVariable int id,
			@RequestBody VaccineRequestBody vaccinerequestbody) {

		this.vs.setVaccinationDetailForPet(id, vaccinerequestbody.getPetname(), vaccinerequestbody.getVaccinename(),
				vaccinerequestbody.getStartdate());
		return vaccinerequestbody;
	}

}
