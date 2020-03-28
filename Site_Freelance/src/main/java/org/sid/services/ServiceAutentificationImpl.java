package org.sid.services;

import java.util.Optional;


import org.sid.dao.FreelancerRepository;
import org.sid.dao.ParticulierRepository;
import org.sid.entities.Freelancer;
import org.sid.entities.Particulier;
import org.springframework.beans.factory.annotation.Autowired;

public class ServiceAutentificationImpl  implements ServiceAutentification{
	@Autowired
	FreelancerRepository freelancerRepository;
	@Autowired
	ParticulierRepository particulierRepository;
	@Override
	public Particulier AuthentificationParticulier(String email, String password) {
		Optional<Particulier> particulierOptional = particulierRepository.findByEmail(email);
		Particulier particulier = null;
		if(particulierOptional.isPresent()) {
			particulier = particulierOptional.get();
			if(particulier.getPassword().equals(password)) {
				//this is Good
				particulier = particulierOptional.get();
			}else {
				// mot de pass incorrect
				particulier = null;
			}
		}else {
			// email incorrect or inexistant
		}
		return particulier;
	}
	@Override
	public Freelancer AuthentificationFreelancer(String email, String password) {
		Optional<Freelancer> freelancerOptional = freelancerRepository.findByEmail(email);
		Freelancer freelancer = null;
		if(freelancerOptional.isPresent()) {
			freelancer = freelancerOptional.get();
			if(freelancer.getPassword().equals(password)) {
				//this is Good
				freelancer = freelancerOptional.get();
			}else {
				// mot de pass incorrect
				freelancer = null;
			}
		}else {
			// email incorrect or inexistant
		}
		return freelancer;
	}
	
	
}
