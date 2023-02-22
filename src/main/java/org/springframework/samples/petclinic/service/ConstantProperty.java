package org.springframework.samples.petclinic.service;

import java.nio.file.Paths;

public class ConstantProperty {

	public final static String FOLDERPATH = Paths.get("src/main/resources/static/picture").toAbsolutePath().toString();

	public final static String PETFOLDERPATH = Paths.get("src/main/resources/static/petimages").toAbsolutePath()
			.toString();
}
