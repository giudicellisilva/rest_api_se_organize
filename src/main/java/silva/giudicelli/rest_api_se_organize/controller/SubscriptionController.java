package silva.giudicelli.rest_api_se_organize.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import silva.giudicelli.rest_api_se_organize.service.SubscriptionService;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {
	SubscriptionService subscriptionService;
	
	@PostMapping("/cancel")
	@PreAuthorize("hasAuthority('SCOPE_SUBSCRIBER')")
	public ResponseEntity<Void> cancel(@AuthenticationPrincipal Jwt jwt) {
	    Long userId = Long.parseLong(jwt.getSubject());
	    subscriptionService.cancelSubscription(userId);
	    return ResponseEntity.noContent().build();
	}
}
