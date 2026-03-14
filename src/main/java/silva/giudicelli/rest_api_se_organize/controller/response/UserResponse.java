package silva.giudicelli.rest_api_se_organize.controller.response;

import java.util.Date;
import java.util.Set;

import org.modelmapper.ModelMapper;

import lombok.Data;
import silva.giudicelli.rest_api_se_organize.model.Role;
import silva.giudicelli.rest_api_se_organize.model.User;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private Date birth;
    private Set<Role> roles;
    
    public UserResponse(User user) {
        if (user != null) {
            this.id = user.getId();
            this.name = user.getName();
            this.surname = user.getSurname();
            this.email = user.getEmail();
            this.birth = user.getBirth();
            this.roles = user.getRoles();
        }
    }
}
