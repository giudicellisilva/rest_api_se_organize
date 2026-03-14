package silva.giudicelli.rest_api_se_organize.controller.request;

import java.util.Date;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import silva.giudicelli.rest_api_se_organize.model.Role;
import silva.giudicelli.rest_api_se_organize.model.User;

public record UserRequest(
    @NotBlank(message = "Fill in the name")
    String name,
    
    String surname,
    
    @NotBlank
    String email,
    
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date birth,
    
    @NotBlank
    String password
) {
    public User toModel(PasswordEncoder passwordEncoder) {
    		System.out.println("name");
        User user = new User();
        user.setName(this.name);
        user.setSurname(this.surname);
        user.setEmail(this.email);
        user.setBirth(this.birth);
        user.setPassword(passwordEncoder.encode(this.password));
        return user;
    }
}