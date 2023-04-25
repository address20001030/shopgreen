package vn.fs.controller.admin;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import vn.fs.entities.Role;
import vn.fs.entities.User;
import vn.fs.repository.UserRepository;
import vn.fs.service.RoleService;

/**
 * @author DongTHD
 *
 */
@Controller
@RequestMapping("/admin")
public class UserController{

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleService roleService;


	@ModelAttribute(value = "rolesList")
	public List<Role> getAllRoles(){
		return roleService.getAllRoles();
	}


	@ModelAttribute(value = "user")
	public User user(Model model, Principal principal, User user) {

		if (principal != null) {
			model.addAttribute("user", new User());
			user = userRepository.findByEmail(principal.getName());

			model.addAttribute("user", user);
			StringBuilder names = new StringBuilder();
			user.getRoles().stream().forEach(t->names.append(t.getName()));
			model.addAttribute("roles",names);
		}

		return user;
	}




	@GetMapping(value = "/users")
	public String customer(Model model, Principal principal) {
		
		User user = userRepository.findByEmail(principal.getName());
		model.addAttribute("user", user);

		StringBuilder names = new StringBuilder();
		user.getRoles().stream().forEach(t->names.append(t.getName()));
		if (names.toString().equals("ROLE_ADMIN")){
			List<User> users = userRepository.findAll();
			model.addAttribute("users", users);
			return "/admin/users";
		}
		return "redirect:/admin/products";


	}

	@GetMapping(value = "/editUser/{id}")
	public String edit(@PathVariable("id") Long id, ModelMap model){

		Optional<User> findUser = userRepository.findById(id);
		if (!findUser.isEmpty()) {
			StringBuilder roleInfo = new StringBuilder();
			findUser.get().getRoles().stream().forEach(t->roleInfo.append(t.getName()));
			model.addAttribute("roleInfo",roleInfo);
			model.addAttribute("userInfo", findUser.get());
			return "admin/editUser";
		}
		return "redirect:/admin/users";
	}

	@PostMapping(value = "/editUser")
	public String updateRoleUser(@ModelAttribute("userInfo") User user, ModelMap model){

		Optional<User> findUser = userRepository.findById(user.getUserId());

		if (!findUser.isEmpty()){
			User userUpdate = findUser.get();
			userUpdate.setRoles(user.getRoles());
			userRepository.save(userUpdate);
		}

		return "redirect:/admin/users";
	}
}
