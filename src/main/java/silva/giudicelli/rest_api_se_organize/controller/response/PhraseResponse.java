package silva.giudicelli.rest_api_se_organize.controller.response;

import silva.giudicelli.rest_api_se_organize.model.Phrase;

public class PhraseResponse {
	private Long id;
    private String phrase;
    private String author;

    public PhraseResponse(Phrase model) {
    		this.id = model.getId();
        this.phrase = model.getPhrase();
        this.author = model.getAuthor();
    }
    
    public Long getId() { return id; }
    public String getPhrase() { return phrase; }
    public String getAuthor() { return author; }
}