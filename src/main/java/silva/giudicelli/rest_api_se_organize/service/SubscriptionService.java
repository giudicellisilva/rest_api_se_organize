package silva.giudicelli.rest_api_se_organize.service;

import silva.giudicelli.rest_api_se_organize.model.User;

public interface SubscriptionService {
	void processNewSubscription(User user, Double amount);
	void demoteToBasic(Long userId);
	void cancelSubscription(Long userId);
}
