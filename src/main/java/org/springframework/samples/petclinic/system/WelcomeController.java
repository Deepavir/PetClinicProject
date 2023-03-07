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

package org.springframework.samples.petclinic.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Repository.UserRepository;
import org.springframework.samples.petclinic.entity.User;
import org.springframework.samples.petclinic.model.VisitorCount;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.VisitorCountRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
class WelcomeController {
	@Autowired
	private VisitorCountRepo visitorcountrepo;

	@Autowired
	private UserRepository userrepo;
	
	@Autowired
	private OwnerRepository ownerrepo;

	@GetMapping("/welcome")
	public String welcome(HttpServletRequest request, HttpServletResponse response, Model model) {
		response.setContentType("welcome.html");
		HttpSession session = request.getSession();

		if (session.isNew()) {
			System.out.println("in new session");
			VisitorCount visitorcount = visitorcountrepo.findById(1).orElse(new VisitorCount(0));
			System.out.println("visitor count" + visitorcount.getCount());
			visitorcount.setCount(visitorcount.getCount() + 1);
			visitorcountrepo.save(visitorcount);
			System.out.println("visitorcount=" + visitorcount);
			model.addAttribute("visitorCount", visitorcount.getCount());
		} else {
			System.out.println("not a new session");
			VisitorCount visitorcount = visitorcountrepo.findById(1).orElse(new VisitorCount(0));
			System.out.println("visitor count" + visitorcount.getCount());
			visitorcount.setCount(visitorcount.getCount());
			visitorcountrepo.save(visitorcount);

			model.addAttribute("visitorCount", visitorcount.getCount());
		}

		return "welcome";
	}

	@ModelAttribute("user")
	public User userRegistration() {
		return new User();
	}

	/*
	 * @GetMapping("/registration") public String
	 * registrationFormView(@ModelAttribute("user") User user) { return
	 * "registration"; } public String
	 * registrationProcessForm(@ModelAttribute("user") User user) {
	 * this.userrepo.save(user); return"redirect:/registration?success"; }
	 */

	@GetMapping("/reg")
	public String showRegistrationForm(@ModelAttribute("user")  User user) {
		return "registration";
	}

	@PostMapping("/reg")
	public String registerUserAccount( @ModelAttribute("user")  User user) {
		Owner owner = new Owner();
		owner.setFirstName(user.getUsername());
		owner.setPassword(user.getPassword());
		owner.setAddress("dummy");
		owner.setCity("dummy");
		owner.setLastName("dummy");
		owner.setTelephone("1234");
		owner.setEmail("dummy@gmail.com");
		this.ownerrepo.save(owner);
	user.setOwner(owner);
	
         this.userrepo.save(user);
		return "redirect:/reg?success";
	}

	@GetMapping("/")
	public String logininfoData(@ModelAttribute("user") User user) {
		System.out.println("into get login method");
		return "login";
	}

	/**
	 * This method authenticate data from user model and allow user to login
	 * 
	 * @param user -accepts username and password in model.
	 * @return welcome page if user data is valid ,else redirect to error message
	 *         -invalid username and password.
	 */
	@PostMapping("/")
	public String loginIntoData(@ModelAttribute("user") User user) {
		System.out.println("into post login method");
		User user1 = this.userrepo.findByUsername(user.getUsername());
		if (user1 == null) {
			return "redirect:/?error";
		}
		else {
		System.out.println("into login method");
		if (user1.getUsername().equals(user.getUsername())) {
			if(user1.getPassword().equals(user.getPassword())) {
		          Owner owner = user1.getOwner();
		          
		          if(owner.getAddress().equals("dummy")) {
		        	  return"redirect:/owners/new/"+owner.getId();
		          }
		          else {
			return "redirect:/welcome";
		          }
			}
		}
		}
		return "redirect:/?error";
	}

}
