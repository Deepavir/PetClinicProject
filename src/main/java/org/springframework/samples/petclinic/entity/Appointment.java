package org.springframework.samples.petclinic.entity;

import java.util.Date;

import org.springframework.samples.petclinic.owner.Owner;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private boolean isMailTriggered;
	private boolean isConfirmed;
	private Date appointmentdate;
	@ManyToOne(fetch=FetchType.EAGER)
	private Owner owner;
}
