package org.springframework.samples.petclinic.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.Vaccination;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination,Integer>{

	Vaccination findByVaccineName(String vaccinename);

}
