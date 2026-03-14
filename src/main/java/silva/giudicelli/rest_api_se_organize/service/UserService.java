package silva.giudicelli.rest_api_se_organize.service;

import java.util.Optional;

import silva.giudicelli.rest_api_se_organize.model.User;

public interface UserService {
	User register(User user);
	Optional<User> getUser(String email);
	Optional<User> findById(Long id);
	void delete(User user);
}
