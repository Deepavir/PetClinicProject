package org.springframework.samples.petclinic.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.samples.petclinic.model.FileData;
import org.springframework.samples.petclinic.owner.FileDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

/*@Service
public class FileService {

	@Autowired
	private FileDataRepository filedatarepo;

	//private final String FOLDER_PATH = "/home/deepa/Documents/workspace-spring-tool-suite-4-4.17.1.RELEASE/spring-petclinic/src/main/resources/static/resources/pictures";
	
	private final String FOLDER_PATH = Paths.get("src/main/resources/static/picture").toAbsolutePath().toString();
	public FileService() throws IOException{
		super();
		// TODO Auto-generated constructor stub
	}
	public String uploadImageToFileSystem(MultipartFile file, int id) throws IOException {
		String filePath = FOLDER_PATH +  File.separator+file.getOriginalFilename();

		/*FileData filedata = new FileData();
		filedata.setName(file.getOriginalFilename());
		filedata.setType(file.getContentType());
		filedata.setFilePath(filePath);
		filedata.setOwnersId(id);
		this.filedatarepo.save(filedata);

		file.transferTo(new File(filePath));

		if (filedata != null) {
			return "file uploaded successfully : " + filePath;
		}
		return null;
	}
}*/
