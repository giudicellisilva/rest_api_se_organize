package silva.giudicelli.rest_api_se_organize.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import silva.giudicelli.rest_api_se_organize.controller.request.UserRequest;
import silva.giudicelli.rest_api_se_organize.controller.response.UserResponse;
import silva.giudicelli.rest_api_se_organize.model.Role;
import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.repository.RoleRepository;
import silva.giudicelli.rest_api_se_organize.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public UserResponse createUser(@RequestBody @Valid UserRequest newUser) {
		User user = newUser.toModel(passwordEncoder);
		Role basicRole = roleRepository.findByName("BASIC");
		user.setRoles(Set.of(basicRole));
		user = userService.register(user);
		return new UserResponse(user);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_BASIC')")
    public UserResponse getUser(@PathVariable String email) {
        var user = userService.getUser(email)
        		.orElseThrow(() -> new BadCredentialsException("Usuário não encontrado com o e-mail informado."));;
        System.out.println("User Response: " + user);
        	return new UserResponse(user);
    }
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_BASIC')")
	public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest updateRequest) { 
	    
	    User existingUser = userService.findById(id)
	            .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado."));
	    
	    if (updateRequest.name() != null) existingUser.setName(updateRequest.name());
	    if (updateRequest.surname() != null) existingUser.setSurname(updateRequest.surname());
	    if (updateRequest.email() != null) existingUser.setEmail(updateRequest.email());
	    if (updateRequest.birth() != null) existingUser.setBirth(updateRequest.birth());
	    
	    if (updateRequest.password() != null && !updateRequest.password().isBlank()) {
	        existingUser.setPassword(passwordEncoder.encode(updateRequest.password()));
	    }

	    User updatedUser = userService.register(existingUser);
	    return new UserResponse(updatedUser);
	}

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_BASIC')")
    public void deleteUser(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado."));
        userService.delete(user); 
    }
}
