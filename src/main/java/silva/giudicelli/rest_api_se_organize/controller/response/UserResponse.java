package silva.giudicelli.rest_api_se_organize.controller.response;


import java.util.Set;

import org.modelmapper.ModelMapper;

import lombok.Data;
import silva.giudicelli.rest_api_se_organize.model.Role;
import silva.giudicelli.rest_api_se_organize.model.User;

public record UserResponse(User user) {
	
}
