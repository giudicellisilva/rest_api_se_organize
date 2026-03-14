package silva.giudicelli.rest_api_se_organize.controller.response;

public record LoginResponse(String accessToken, Long expiresIn, String refreshToken) {
	
}
