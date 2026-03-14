package silva.giudicelli.rest_api_se_organize.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
    private UserRepository userRepository;
	
    @Override
    public User register(User newUser) {

        return userRepository.save(newUser);
    }
    
    @Override
    public Optional<User> getUser(String email){
    		return userRepository.findByEmail(email);
    }
    
    @Override
    public Optional<User> findById(Long id){
    		return userRepository.findById(id);
    }
    
    @Override
    public void delete(User user){
    		userRepository.delete(user);
    }
}
