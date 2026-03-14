package silva.giudicelli.rest_api_se_organize.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import silva.giudicelli.rest_api_se_organize.model.Role;
import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.repository.RoleRepository;
import silva.giudicelli.rest_api_se_organize.repository.UserRepository;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final SecurityConfig securityConfig;

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder, SecurityConfig securityConfig) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.securityConfig = securityConfig;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());

        var userAdmin = userRepository.findByEmail("admin@admin.com");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin ja existe");
                },
                () -> {
                    var user = new User();
                    user.setName("admin");
                    user.setEmail("admin@admin.com");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );
    }
}
