package org.springframework.samples.petclinic.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.VaccineTimePeriod;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineTimePeriodRepository extends JpaRepository<VaccineTimePeriod,Integer> {
	


}
