package silva.giudicelli.rest_api_se_organize.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import jakarta.transaction.Transactional;
import silva.giudicelli.rest_api_se_organize.model.Payment;
import silva.giudicelli.rest_api_se_organize.model.Role;
import silva.giudicelli.rest_api_se_organize.model.Subscription;
import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.repository.PaymentRepository;
import silva.giudicelli.rest_api_se_organize.repository.RoleRepository;
import silva.giudicelli.rest_api_se_organize.repository.SubscriptionRepository;
import silva.giudicelli.rest_api_se_organize.repository.UserRepository;

@Service
public class SubscriptionServiceImplement implements SubscriptionService {
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private SubscriptionRepository subscriptionRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;

    @Override @Transactional
    public void processNewSubscription(User user, Double amount) {
        // 1. Simula pagamento
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        // 2. Atualiza ou Cria a Assinatura (Contrato)
        Subscription sub = subscriptionRepository.findByUser(user).orElse(new Subscription());
        sub.setUser(user);
        sub.setStartDate(LocalDate.now());
        sub.setEndDate(LocalDate.now().plusMonths(1)); // 1 mês de acesso
        sub.setActive(true);
        subscriptionRepository.save(sub);

        Role subscriberRole = roleRepository.findByName("SUBSCRIBER");

	     // Use HashSet para permitir que o Hibernate gerencie a coleção
	     Set<Role> roles = new HashSet<>();
	     roles.add(subscriberRole);
	
	     user.setRoles(roles);
	     userRepository.save(user);
    }
    
    @Transactional
    public void demoteToBasic(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Role basicRole = roleRepository.findByName("BASIC");
       
        Set<Role> roles = new HashSet<>();
        roles.add(basicRole);
        
        user.setRoles(roles);
        userRepository.save(user);
    }
    
    @Transactional
    public void cancelSubscription(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        subscriptionRepository.deleteByUserId(userId);

        Role basicRole = roleRepository.findByName("BASIC");
        user.setRoles(new HashSet<>(Set.of(basicRole)));
        

        userRepository.save(user);
    }
}
