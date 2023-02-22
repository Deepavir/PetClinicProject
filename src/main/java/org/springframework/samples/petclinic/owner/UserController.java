package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.entity.User;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

	@Autowired
	private UserService userservice;
	@GetMapping("/registration")
	public String getRegistrationForm(@ModelAttribute("user") User user) {
		
		return "registration";
	}
	
	@PostMapping("/registration")
	public String saveRegistrationForm(@ModelAttribute("user") User user) {
		userservice.saveUser(user);
		return "redirect:/registration?success";
	}
}
