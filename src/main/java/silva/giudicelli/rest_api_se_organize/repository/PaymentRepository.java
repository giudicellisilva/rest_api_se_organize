package silva.giudicelli.rest_api_se_organize.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import silva.giudicelli.rest_api_se_organize.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
}
