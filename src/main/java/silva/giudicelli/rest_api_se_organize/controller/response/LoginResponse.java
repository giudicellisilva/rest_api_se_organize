package silva.giudicelli.rest_api_se_organize.controller.response;

import silva.giudicelli.rest_api_se_organize.model.User;

public record LoginResponse(String accessToken, Long expiresIn, String refreshToken, User user) {
	
}
