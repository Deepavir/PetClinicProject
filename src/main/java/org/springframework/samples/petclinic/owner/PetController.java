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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.ConstantProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/owners/{ownerId}")
class PetController {

	@Autowired
	private PetRepository petrepo;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private OwnerRepository ownerrepo;
	//private final String PETFOLDERPATH = Paths.get("src/main/resources/static/petimages").toAbsolutePath().toString();
	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final OwnerRepository owners;

	public PetController(OwnerRepository owners) {
		this.owners = owners;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.owners.findPetTypes();
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable("ownerId") int ownerId) {
		return this.owners.findById(ownerId);
	}

	@ModelAttribute("pet")
	public Pet findPet(@PathVariable("ownerId") int ownerId,
			@PathVariable(name = "petId", required = false) Integer petId) {
		return petId == null ? new Pet() : this.owners.findById(ownerId).getPet(petId);
	}

	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	@GetMapping("/pets/new")
	public String initCreationForm(Owner owner, ModelMap model) {
		Pet pet = new Pet();
		owner.addPet(pet);
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

/*	@PostMapping("/pets/new")
	public String processCreationForm(Owner owner, @Valid Pet pet, BindingResult result, ModelMap model) {
		if (StringUtils.hasLength(pet.getName()) && pet.isNew() && owner.getPet(pet.getName(), true) != null) {
			result.rejectValue("name", "duplicate", "already exists");
		}
             
		owner.addPet(pet);
		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		this.owners.save(owner);
		return "redirect:/owners/{ownerId}";
	} */
	@PostMapping("/pets/new")
	public String processCreationForm(@Valid  @RequestBody @RequestParam("name") String name,
			@RequestParam("birthDate") LocalDate birthDate,
			@RequestParam("type") PetType type,
			@RequestParam("images") MultipartFile images , @Valid Pet pet1,BindingResult result, @PathVariable int ownerId
	) throws IOException {
		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		String filepath = ConstantProperty.PETFOLDERPATH+ File.separator+images.getOriginalFilename();
		System.out.println("in method");
		images.transferTo(new File(filepath));
          Owner owner = this.ownerrepo.findById(ownerId);
          Pet pet = new Pet();
          pet.setName(name);
          System.out.println(pet.getName());
          pet.setBirthDate(birthDate);
         pet.setType(type);
         pet.setPetimage(images.getOriginalFilename());
         owner.addPet(pet);
          // owner.setImages(images.getOriginalFilename());
		this.owners.save(owner);
		return "redirect:/owners/" + owner.getId();
	} 
	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm( Owner owner, @PathVariable("petId") int petId, ModelMap model) {
		Pet pet = owner.getPet(petId);
		model.put("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(@Valid Pet pet, BindingResult result, Owner owner, ModelMap model) {
		if (result.hasErrors()) {
			model.put("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		owner.addPet(pet);
		this.owners.save(owner);
		return "redirect:/owners/{ownerId}";
	}
	
	@PostMapping("/addpet")
	 public String savePet(@RequestParam("file") MultipartFile	 file,
	    		@RequestParam("petData") String petData,@PathVariable int ownerId
	    		){
	    	System.out.println("file infor "+file.getOriginalFilename());
	    	System.out.println("petsdata"+petData);
	    	Owner owner1 = this.owners.findById(ownerId);
	    	Owner owner = null;
	    		Pet pet=null;
	    		try {
	    			System.out.println("parsing the json data");
	    	 owner= mapper.readValue(petData,Owner.class);
	    	 pet = owner1.addPet(owner.getPet(owner1.getId()));
	    	 System.out.println("adding pet in owner"+pet);
	    	 this.owners.save(owner);
	    	 System.out.println("saving pet in owner"+owner);
				} catch (JsonProcessingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    		if(fileName.contains(".."))
			{
				System.out.println("not a a valid file");
			}
			try {
				//Pet pet = owner.getPet(owner.getId());
				 System.out.println("getting pet from owner"+pet);
				 pet.setPetimage(Base64.getEncoder().encodeToString(file.getBytes()));
				System.out.println("saving image");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		this.owners.save(owner1);
		 System.out.println("saving  owner"+owner1);
			System.out.println("pet data ");
	    		  return "Pet added successfully";
	}
}
