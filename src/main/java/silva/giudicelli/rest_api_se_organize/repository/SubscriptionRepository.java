package silva.giudicelli.rest_api_se_organize.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import silva.giudicelli.rest_api_se_organize.model.Subscription;
import silva.giudicelli.rest_api_se_organize.model.User;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
	Optional<Subscription> findByUser(User user);
	void deleteByUserId(Long userId);
}
