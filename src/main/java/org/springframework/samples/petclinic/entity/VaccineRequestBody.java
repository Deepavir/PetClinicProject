package org.springframework.samples.petclinic.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineRequestBody {
	
	private String petname;
	private String vaccinename;
	private String startdate;

}
