package org.springframework.samples.petclinic.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.entity.Appointment;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer>{

	
}
