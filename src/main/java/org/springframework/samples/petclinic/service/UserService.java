package org.springframework.samples.petclinic.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.Repository.RoleRepository;
import org.springframework.samples.petclinic.Repository.UserRepository;
import org.springframework.samples.petclinic.entity.Erole;
import org.springframework.samples.petclinic.entity.Role;
import org.springframework.samples.petclinic.entity.User;
import org.springframework.samples.petclinic.entity.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userrepo;
	@Autowired
	private RoleRepository rolerepo;

	public User saveUser(User user) {
		Set<Role> roles = new HashSet<>();
		roles.add(rolerepo.findByName(Erole.ROLE_USER));
		user.setRole(roles);
		User saveduser = this.userrepo.save(user);
		return saveduser;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userrepo.findByUsername(username);
			//	.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return UserPrincipal.build(user);
	}

}
