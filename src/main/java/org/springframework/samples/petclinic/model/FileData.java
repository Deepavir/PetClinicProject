package org.springframework.samples.petclinic.model;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "Filedata")
@Builder
public class FileData {
	@Override
	public String toString() {
		return "FileData [name=" + name + ", type=" + type + ", filePath=" + filePath + "]";
	}
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String name;
    private String type;
    private String filePath;
    private Integer ownersId;

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Integer getOwnersId() {
		return ownersId;
	}
	public void setOwnersId(Integer ownersId) {
		this.ownersId = ownersId;
	}
	
	
	

}
