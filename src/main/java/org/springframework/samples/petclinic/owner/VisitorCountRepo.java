package org.springframework.samples.petclinic.owner;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.VisitorCount;
import org.springframework.stereotype.Repository;
@Repository
public interface VisitorCountRepo extends JpaRepository<VisitorCount ,Integer>{

	Optional<VisitorCount> findById(int id);

}
