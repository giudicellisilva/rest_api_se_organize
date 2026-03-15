package silva.giudicelli.rest_api_se_organize.controller;

import jakarta.validation.Valid;
import silva.giudicelli.rest_api_se_organize.controller.request.PhraseRequest;
import silva.giudicelli.rest_api_se_organize.controller.response.PhraseResponse;
import silva.giudicelli.rest_api_se_organize.exception.BadCredentialsException;
import silva.giudicelli.rest_api_se_organize.model.Phrase;
import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.service.PhraseService;
import silva.giudicelli.rest_api_se_organize.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phrase")
public class PhraseController {

    @Autowired
    private PhraseService phaseService;
    @Autowired
    private UserService userService;
    
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_SUBSCRIBER', 'SCOPE_BASIC')")
    public PhraseResponse createPhase(
        @RequestBody @Valid PhraseRequest request,
        @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = Long.parseLong(jwt.getSubject());

        User user = userService.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("Usuário do token não encontrado."));

        Phrase phase = request.toModel();
        phase.setUser(user); 

        phase = phaseService.save(phase);
        return new PhraseResponse(phase);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_SUBSCRIBER', 'SCOPE_BASIC')")
    public List<PhraseResponse> listAll(@AuthenticationPrincipal Jwt jwt) {
        // 1. Pega o ID do usuário que está fazendo a requisição
        Long userId = Long.parseLong(jwt.getSubject());
        
        // 2. Busca apenas as frases desse ID específico
        return phaseService.findAllByUserId(userId).stream()
                .map(PhraseResponse::new)
                .toList();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_SUBSCRIBER', 'SCOPE_BASIC')")
    public PhraseResponse updatePhase(@PathVariable Long id, @RequestBody PhraseRequest request) {
        Phrase existingPhase = phaseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Fase não encontrada."));

        if (request.phrase() != null) existingPhase.setPhrase(request.phrase());
        if (request.author() != null) existingPhase.setAuthor(request.author());

        existingPhase = phaseService.save(existingPhase);
        return new PhraseResponse(existingPhase);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_SUBSCRIBER', 'SCOPE_BASIC')")
    public void deletePhase(@PathVariable Long id) {
        Phrase phase = phaseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Fase não encontrada."));
        phaseService.delete(phase);
    }
}