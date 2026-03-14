package silva.giudicelli.rest_api_se_organize.service;

import java.util.List;
import java.util.Optional;
import silva.giudicelli.rest_api_se_organize.model.Phrase;

public interface PhraseService {
    Phrase save(Phrase phase);
    void delete(Phrase phase);
    Optional<Phrase> findById(Long id);
    List<Phrase> findAllByUserId(Long userId);
}