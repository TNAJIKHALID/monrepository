package org.sid.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.sid.dao.ParticulierRepository;
import org.sid.entities.Particulier;
import org.sid.forms.Login;
import org.sid.services.EmailService;
import org.sid.services.ServiceAutentification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.bytebuddy.utility.RandomString;
@Controller
public class SignInControllerParticulier implements WebMvcConfigurer {
	@Autowired
	private EmailService emailService;
	@Autowired
	private ParticulierRepository particulierRepository;
	@Autowired
	private ServiceAutentification serviceAutentification;

	@RequestMapping(value = "/")
	public String toHome(Model model) {
		return "home";
	}
   /*********************** HOME PAGE ************************/
	
	@RequestMapping(value = "/loginParticulier")
	public String loginParticulier(Model model) {
		model.addAttribute("message", false);
		model.addAttribute("login", new Login());
		return "loginParticulier";
	}
	
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/results").setViewName("results");
	}

	@RequestMapping(value = "/connexionParticulier")
	public String connexion(@Valid Login login, BindingResult bindingResult, Model model) {
		String pageAfter = "profilParticulier";
		boolean message = false;
		if (bindingResult.hasErrors()) {
			pageAfter = "loginParticulier";
			message = true;
			model.addAttribute("messageValid", message);
		} else {
			Particulier particulier = serviceAutentification.AuthentificationParticulier(login.getMail(),
					login.getPassword());
			if (particulier == null) {
				message = true;
				model.addAttribute("messageForm", message);
				pageAfter = "loginParticulier";
			}
		}
		return pageAfter;
	}

	@RequestMapping(value = "/forgotPasswordParticulierPage")
	public String forgot() {
		return "forgotPasswordParticulier";
	}

	@RequestMapping(value = "/resetPasswordParticulier")
	public String forgotPasswordParticulier(Model model, @RequestParam("email") String email, HttpSession session) {
		String pageAfter, validationCode = RandomString.make(5);
		Particulier particulier;
		Optional<Particulier> optionnelParticulier = particulierRepository.findByEmail(email);
		if (optionnelParticulier.isPresent()) {
			particulier = optionnelParticulier.get();
			emailService.resetPasswordMail(particulier, validationCode);
			session.setAttribute("validationCode", validationCode);
			session.setAttribute("particulier", particulier);
			pageAfter = "resetPasswordParticulier";
		} else {
			pageAfter = "forgotPasswordParticulier";
		}

		return pageAfter;
	}

	@RequestMapping(value = "/resetPasswordParticulierToPrfile")
	public String restPassword(Model model, @RequestParam String password, @RequestParam String password2,
			@RequestParam String validationCode, HttpSession session) {
		String pageAfter = "profilParticulier", currentPage = "resetPasswordParticulier";
		String validationInput = (String) session.getAttribute("validationCode");
		Particulier particulier = (Particulier) session.getAttribute("particulier");
		if (!password.equals(password2)) {
			pageAfter = currentPage;
		} else if (!validationInput.equals(validationCode)) {
			pageAfter = currentPage;
		} else
			particulier.setPassword(password);
			particulierRepository.save(particulier);
		return pageAfter;
	}
}
