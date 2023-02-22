package org.springframework.samples.petclinic.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
	  Optional<User> findByUsername(String username);

	  Boolean existsByUsername(String username);

}
