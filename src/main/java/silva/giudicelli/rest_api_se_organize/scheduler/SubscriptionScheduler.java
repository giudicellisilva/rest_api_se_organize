package silva.giudicelli.rest_api_se_organize.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import silva.giudicelli.rest_api_se_organize.model.Role;
import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.repository.RoleRepository;
import silva.giudicelli.rest_api_se_organize.repository.UserRepository;

@Component
public class SubscriptionScheduler {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkExpiredSubscriptions() {
        LocalDate today = LocalDate.now();
        Role basicRole = roleRepository.findByName("BASIC");
        
        // Busca usuários SUBSCRIBER com data de expiração anterior a hoje
        List<User> expiredUsers = userRepository.findExpiredSubscribers(today);

        for (User user : expiredUsers) {
            user.setRoles(new HashSet<>(Set.of(basicRole)));
            userRepository.save(user);
        }
    }
}