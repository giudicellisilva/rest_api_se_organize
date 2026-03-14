package silva.giudicelli.rest_api_se_organize.controller.request;

import jakarta.validation.constraints.NotBlank;
import silva.giudicelli.rest_api_se_organize.model.Phrase;

public record PhraseRequest(
    @NotBlank(message = "Name phase is require")
    String phrase,
    String author
) {
    public Phrase toModel() {
    		Phrase phrase = new Phrase();
    		phrase.setPhrase(this.phrase);
    		phrase.setAuthor(this.author);
        return phrase;
    }
}