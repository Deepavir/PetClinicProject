package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepositoryy extends JpaRepository<Owner, Integer> {
	List<Owner> findByfirstNameOrderByIdDesc(String firstname);

	Page<Owner> findAllByOrderByCreatedDate(Pageable pageable);
	  Page<Owner> findAllByOrderByFirstNameAscCreatedDateAsc(Pageable pageable);
}
