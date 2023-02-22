package org.springframework.samples.petclinic.owner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;

/**
 * @author deepa
 *
 */
/**
 * @author deepa
 *
 */
/**
 * @author deepa
 *
 */
@RestController
public class FileController {

	// private FileService service;
	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private OwnerRepository owners;
	@Autowired
	private OwnerRepositoryy ownerrepo;

	@Autowired
	private OwnerService ownerservice;

	@Autowired
	private PetRepository petrepo;

	// to upload file in file system
	/*
	 * @PostMapping("/{ownerId}/file") public ResponseEntity<?>
	 * uploadImageToFIleSystem(@RequestParam("image")MultipartFile
	 * file,@PathVariable int id) throws IOException { String uploadImage =
	 * service.uploadImageToFileSystem(file,id); return
	 * ResponseEntity.status(HttpStatus.OK) .body(uploadImage); }
	 */
	// method to add owner object and image together .
	/*
	 * @PostMapping("/addowner") public String saveOwner(@RequestParam("file")
	 * MultipartFile file,
	 * 
	 * @RequestParam("ownerData") String ownerData ){
	 * System.out.println("file infor"+file.getOriginalFilename());
	 * System.out.println("userdata"+ownerData); Owner owner = null;
	 * 
	 * try { owner = mapper.readValue(ownerData,Owner.class);
	 * 
	 * } catch (JsonProcessingException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); } String fileName =
	 * StringUtils.cleanPath(file.getOriginalFilename());
	 * if(fileName.contains("..")) { System.out.println("not a a valid file"); } try
	 * { //owner.setImages(Base64.getEncoder().encodeToString(file.getBytes())); }
	 * catch (IOException e) { e.printStackTrace(); }
	 * 
	 * 
	 * this.owners.save(owner); return "Owner added successfully";
	 * 
	 * }
	 */
	// method to read aboutus page
	/**
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/owners/abouts")
	public String getAboutInfo() throws IOException {
		String filepath = "/home/deepa/Documents/workspace-spring-tool-suite-4-4.17.1.RELEASE/spring-petclinic/src/main/resources/static/aboutus.text";
		InputStream is = new FileInputStream(filepath);
		int content = is.read();
		String finalvalue = "";
		while (content != -1) {
			char ch = (char) content;
			finalvalue += ch;
			content = is.read();
		}

		return finalvalue;
	}

	/**
	 * This method provide paginated and sorted owner data order by firstname and
	 * createddate .
	 * 
	 * @param page -This parameter provides page number of data to be displayed.
	 * @return paginated data
	 */
	@Operation(summary="Get owner data",description="Get  owner data sorted by firstname",tags="Get")
	@GetMapping("/owner/name/{page}/{pagesize}")
	private Page<Owner> getAllOwnerSort(@PathVariable int page, @PathVariable int pagesize) {

		Page<Owner> list = this.ownerservice.getDataByFirstNameAndCreatedDated(page - 1, pagesize);

		return list;
	}

	/**
	 * This method provide paginated and sorted owner data orderby createddate.
	 * 
	 * @param page-This parameter provides page number
	 * @param pagesize  -This parameter gets number of records to be displayed.
	 * @return paginated data as per page and pagesize ,sorted by createddate.
	 */
	@Operation(summary="Get owner data",description="Get  owner data sorted by createddate",tags="Get")
	@GetMapping("/owner/{page}/{pagesize}")
	private Page<Owner> getAllOwnerByCreatedDate(@PathVariable int page, @PathVariable int pagesize) {
		Pageable paging = PageRequest.of(page, pagesize);

		Page<Owner> list = this.ownerservice.getDataByCreatedDate(page, pagesize);

		return list;
	}
}
