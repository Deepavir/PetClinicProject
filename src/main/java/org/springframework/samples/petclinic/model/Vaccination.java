package org.springframework.samples.petclinic.model;

import java.util.*;

import org.springframework.samples.petclinic.owner.Pet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vaccination")
public class Vaccination {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String vaccineName;
	private int timePeriod;
	private int numberOfShots;
	private int volume;
	private String unitOfMeasure;
	@OneToMany(cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
	@JoinColumn(name = "vaccination_id")
	private Set<VaccineTimePeriod> vaccineTimes;
	
	@ManyToMany(mappedBy = "vaccinationdata",fetch = FetchType.EAGER)
	private List<Pet> pet;
	
	

}
