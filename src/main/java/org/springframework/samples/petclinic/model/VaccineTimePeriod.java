package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "vaccinetimeperiod")
public class VaccineTimePeriod {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String startDate;
	private Date endDate;
	@ElementCollection(fetch=FetchType.EAGER)
	private List<Date> vaccineDate = new ArrayList();
	@ManyToOne(fetch=FetchType.EAGER)
	private Vaccination vaccination;

}
