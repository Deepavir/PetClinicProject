package org.springframework.samples.petclinic.owner;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.FileData;
import org.springframework.stereotype.Repository;

@Repository
public interface  FileDataRepository extends JpaRepository<FileData,Long>{

	Optional<FileData> findByName(String fileName);
}
