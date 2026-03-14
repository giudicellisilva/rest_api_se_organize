package silva.giudicelli.rest_api_se_organize.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_phrases")
@Data
public class Phrase {
	@Id
	@GeneratedValue
	private Long id;
	private String phrase;
	private String author;
	
	@ManyToOne
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPhrase() {
		return phrase;
	}

	public String getAuthor() {
		return author;
	}

	public User getUser() {
		return user;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setUser(User user) {
		this.user = user;
	}


}

	