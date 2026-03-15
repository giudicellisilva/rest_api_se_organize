package silva.giudicelli.rest_api_se_organize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.service.SubscriptionService;
import silva.giudicelli.rest_api_se_organize.service.UserService;

@RestController @RequestMapping("/payment")
public class PaymentController {
    @Autowired private SubscriptionService subscriptionService;
    @Autowired private UserService userService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> checkout(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        User user = userService.findById(userId).orElseThrow();
        
        subscriptionService.processNewSubscription(user, 29.90);
        
        return ResponseEntity.ok("Pagamento aprovado e assinatura ativa!");
    }
}
