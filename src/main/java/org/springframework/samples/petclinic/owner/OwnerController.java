/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.InputStreamReader;

import org.apache.el.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.samples.petclinic.exception.ImageSizeException;
import org.springframework.samples.petclinic.exception.InvalidFileException;
import org.springframework.samples.petclinic.model.OwnerDto;
import org.springframework.samples.petclinic.service.ConstantProperty;
import jakarta.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
class OwnerController {
	private static final Logger log = LoggerFactory.getLogger(OwnerController.class);
	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	private final OwnerRepository owners;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private ContactRepository contactrepo;

	public OwnerController(OwnerRepository clinicService) {
		this.owners = clinicService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable(name = "ownerId", required = false) Integer ownerId) {
		return ownerId == null ? new Owner() : this.owners.findById(ownerId);
	}

	@ModelAttribute("contactdetail")
	public Optional<ContactDetail> findContact(@PathVariable(name = "contactId", required = false) Integer contactId) {
		return contactId == null ? Optional.of(new ContactDetail()) : this.contactrepo.findById(contactId);
	}
/*
 * This method used to get page with owners detail field view.
 * @return creation form 
 * 
 * 
 */
	@GetMapping("/owners/new")
	public String initCreationForm(Map<String, Object> model) {
		log.info("initCreationForm method========owner creation form initialized");
		Owner owner = new Owner();
		model.put("owner", owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	/*
	 * @PostMapping("/owners/new") public String processCreationForm(@Valid Owner
	 * owner , BindingResult result) throws IOException {
	 * System.out.println("owner " + owner); System.out.println("result " + result);
	 * 
	 * if (result.hasErrors()) { return VIEWS_OWNER_CREATE_OR_UPDATE_FORM; }
	 * 
	 * 
	 * this.owners.save(owner); return "redirect:/owners/" + owner.getId(); }
	 */
//endpoint to add new owner details
	
	
	
	@PostMapping("/owners/new")
	public String processCreationForm(@Valid @RequestParam("firstName") String firstname,
			@RequestParam("lastName") String lastname, @RequestParam("address") String address,
			@RequestParam("city") String city, @RequestParam("telephone") String telephone,
			@RequestParam("email") String email, @RequestParam("imagess") MultipartFile images, @Valid Owner owner,
			BindingResult result) {

		log.info("processCreationForm=====process creation form initiatied");
		String filepath = ConstantProperty.FOLDERPATH + File.separator + images.getOriginalFilename();
		if (result.hasErrors()) {
			log.info("Following error occurred ========{}", result.toString());
			System.out.println("changes");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		log.info("owner details recieved with owner name:{}", firstname);
		Owner owner1 = new Owner();
		owner1.setFirstName(firstname);
		owner1.setLastName(lastname);
		owner1.setAddress(address);
		owner1.setCity(city);
		owner1.setTelephone(telephone);
		owner1.setEmail(email);

		System.out.println("get file size" + images.getSize());

		System.out.println("image content type" + images.getContentType());
		try {
			if (images.getContentType().endsWith("png")) {
				if (images.getSize() < 1000000) {
					owner1.setImages(images.getOriginalFilename());

					images.transferTo(new File(filepath));
					log.info("image stored successfully");
				} else {

					throw new ImageSizeException("Image size exceeded the maximum limit");

				}
			} else {

				throw new InvalidFileException("This System accepts only .png Files");
			}

			owner1.setImages(images.getOriginalFilename());

			this.owners.save(owner1);
			log.info("Owner details added successfully with id{}", owner1.getId());

			log.info("owner data saved successfully");
		} catch (IllegalStateException | IOException e) {

			e.printStackTrace();
		}

		return "redirect:/owners/" + owner1.getId();
	}

	@GetMapping("/owners/find")
	public String initFindForm(Map<String, Object> model) {
		log.info("initFindForm=======Find Form initialized");
		model.put("owner", new Owner());
		return "owners/findOwners";
	}

	@GetMapping("/owners")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
			Model model, @RequestParam(name = "sortBy", defaultValue = "default") String sortBy) {
		log.info("processFindForm======processFindForm initiated");
		log.info(sortBy);
		
		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) {
			log.info("lastname=====null");
			owner.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		Page<Owner> ownersResults = findPaginatedForOwnersLastName(page, owner.getLastName());
		if (ownersResults.isEmpty()) {
			// no owners found
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		}

		if (ownersResults.getTotalElements() == 1) {
			// 1 owner found
			System.out.println("lastname found");
			owner = ownersResults.iterator().next();
			return "redirect:/owners/" + owner.getId();
		}
		 if
			 (sortBy.equalsIgnoreCase("name")) {
				log.info("sortby;{}", sortBy);
				Page<Owner> pagedowner = getAllOwner(page);
	              System.out.println("pagedowner"+pagedowner);
	              System.out.println(page);
	              model.addAttribute("sort",sortBy);
				return addPaginationModel(page, model, pagedowner,sortBy);
			}
		 if
		 (sortBy.equalsIgnoreCase("addedTime")) {
			log.info("sortby;{}", sortBy);
			Page<Owner> pagedowner = getAllOwnerByTime(page);
              System.out.println("pagedowner"+pagedowner);
			return addPaginationModel(page, model, pagedowner,sortBy);
		}
		 else {
			// multiple owners found
			
			
			return addPaginationModel(page, model, ownersResults);
		}
		
	}
	private String addPaginationModel(int page, Model model, Page<Owner> paginated) {
		model.addAttribute("listOwners", paginated);
		List<Owner> listOwners = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", listOwners);
		return "owners/ownersList";
	}
	private String addPaginationModel(int page, Model model, Page<Owner> paginated ,String sortBy) {
		model.addAttribute("listOwners", paginated);
		List<Owner> listOwners = paginated.getContent();
		System.out.println(sortBy);
		String s = sortBy;
		model.addAttribute("sort",s);
		System.out.println(sortBy);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", listOwners);
		return "owners/ownersList";
	}
	private Page<Owner> findPaginatedForOwnersLastName(int page, String lastname) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return owners.findByLastName(lastname, pageable);
	}
	
	/*
	 * This method is used to sort owners data by firstname and createddate.
	 * @param page this parameter provides page number 
	 * @return list of owner data sorted by firstname and createddate.
	 * 
	 */
	private Page<Owner> getAllOwner(int page){
		int pageSize= 10;
		//geable pageable = PageRequest.of(page - 1, pageSize);
		Pageable paging = PageRequest.of(page-1 , pageSize, Sort.by("firstName").ascending().and(Sort.by("createdDate")));
		Page<Owner> list = this.owners.findAll(paging);
		
		return list;
	}
	/*
	 * This method is used to sort owners data bycreateddate.
	 * @param page this parameter provides page number 
	 * @return list of owner data sorted by createddate.
	 * 
	 */
	private Page<Owner> getAllOwnerByTime(int page){
		int pageSize= 10;
		//geable pageable = PageRequest.of(page - 1, pageSize);
		Pageable paging = PageRequest.of(page-1,pageSize,Sort.by("createdDate").ascending());
		Page<Owner> list = this.owners.findAll(paging);
		
		return list;
	}
	
	/*
	 * This method is used to get owners data by ownersid.
	 * @param ownerId This parameter provides id of owner
	 * @return owner details with ownerId.
	 * 
	 */
	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
		Owner owner = this.owners.findById(ownerId);
		model.addAttribute(owner);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}
	/*
	 * This method is used to update/edit owners data with ownersid
	 * @param ownerId This parameter provides id of owner
	 * @return updated owners detail.
	 * 
	 */
	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
			@PathVariable("ownerId") int ownerId) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		owner.setId(ownerId);
		this.owners.save(owner);
		return "redirect:/owners/{ownerId}";
	}

	/**
	 * Custom handler for displaying an owner.
	 * 
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		Owner owner = this.owners.findById(ownerId);
		mav.addObject(owner);
		return mav;
	}
	/*
	 * @PostMapping("/addowner") public String saveOwner(@RequestParam("file")
	 * MultipartFile file,
	 * 
	 * @RequestParam("ownerData") String ownerData ){
	 * System.out.println("file infor"+file.getOriginalFilename());
	 * System.out.println("userdata"+ownerData); Owner owner = null; try { owner =
	 * mapper.readValue(ownerData,Owner.class); } catch (JsonProcessingException e1)
	 * { // TODO Auto-generated catch block e1.printStackTrace(); } String fileName
	 * = StringUtils.cleanPath(file.getOriginalFilename());
	 * if(fileName.contains("..")) { System.out.println("not a a valid file"); } try
	 * { owner.setImages(Base64.getEncoder().encodeToString(file.getBytes())); }
	 * catch (IOException e) { e.printStackTrace(); }
	 * 
	 * System.out.println("pet" +owner.getPets()); this.owners.save(owner); return
	 * "Owner added successfully"; }
	 */

	/*
	 * @PostMapping("/{ownerId}/file") public ResponseEntity<?>
	 * uploadImageToFIleSystem(@RequestParam("image")MultipartFile
	 * file,@PathVariable int id) throws IOException { String uploadImage =
	 * service.uploadImageToFileSystem(file,id); return
	 * ResponseEntity.status(HttpStatus.OK) .body(uploadImage); }
	 */
	// endpoint to get aboutus page
	/*
	 * This method used to get aboutus detail.
	 * @return aboutus page.
	 * 
	 * 
	 * 
	 */
	@GetMapping("/owners/about")
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
		log.info("getAboutInfo======get about info visited");

		return "about";
	}

//endpoint to get contact us pages
	/*
	 * This method used to get contactus detail.
	 * @return contactus page.
	 * 
	 * 
	 * 
	 */
	@GetMapping("/owners/contact")
	public String getContactInfo() {
		log.info("getContactInfo=======get contact info initiated");
		return "contact";
	}
 /*
  * This method used to add contact detail from contactus page
  * @param contactdetail as model
  * 
  * 
  */
	@PostMapping("/owners/contact")
	public String addContactInfo(@Valid ContactDetail contact, BindingResult result) {
		log.info("addContactInfo=============add contact info initiated");
		if (result.hasErrors()) {
			log.info("Following error occurred====={}", result.toString());
			return "contact";
		}
		log.info("contact details to be added:{}", contact.getName());
		this.contactrepo.save(contact);
		return "contact";
	}
	
	

}
