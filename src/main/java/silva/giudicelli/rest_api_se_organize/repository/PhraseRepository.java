package silva.giudicelli.rest_api_se_organize.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import silva.giudicelli.rest_api_se_organize.model.Phrase;
import silva.giudicelli.rest_api_se_organize.model.User;

public interface PhraseRepository extends JpaRepository<Phrase, Long>{
	
	List<Phrase> findByUser(User user);
	Phrase save(Phrase phrase);
	List<Phrase> findAllByUserId(Long id);
	void delete(Phrase phrase);
	Optional<Phrase> findById(Long id);
}
