package silva.giudicelli.rest_api_se_organize.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import silva.giudicelli.rest_api_se_organize.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	 Optional<User> findByEmail(String email);
	 Optional<User> findById(Long id);
	 @Query("SELECT u FROM User u JOIN u.roles r " +
		       "WHERE r.name = 'SUBSCRIBER' " +
		       "AND NOT EXISTS (SELECT s FROM Subscription s WHERE s.user = u)")
	 List<User> findSubscribersWithoutContract();
}
