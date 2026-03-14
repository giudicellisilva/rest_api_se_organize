package silva.giudicelli.rest_api_se_organize.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import silva.giudicelli.rest_api_se_organize.model.Phrase;
import silva.giudicelli.rest_api_se_organize.repository.PhraseRepository;

@Service
public class PhraseServiceImplement implements PhraseService {

    @Autowired
    private PhraseRepository phaseRepository;

    @Override
    @Transactional
    public Phrase save(Phrase phase) {
        // Aqui você pode adicionar regras de negócio antes de salvar
        return phaseRepository.save(phase);
    }

    @Override
    public List<Phrase> findAllByUserId(Long id) {
        return phaseRepository.findAllByUserId(id);
    }

    @Override
    @Transactional
    public void delete(Phrase phrase) {
        phaseRepository.delete(phrase);
    }
    
    @Override
    public Optional<Phrase> findById(Long id) {
        return phaseRepository.findById(id);
    }
}
