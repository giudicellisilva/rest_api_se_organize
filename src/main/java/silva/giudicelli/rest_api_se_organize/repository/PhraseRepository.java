package silva.giudicelli.rest_api_se_organize.repository;

import java.util.List;

import silva.giudicelli.rest_api_se_organize.model.Phrase;
import silva.giudicelli.rest_api_se_organize.model.User;

public interface PhraseRepository {
	
	List<Phrase> findByUser(User user);
}
