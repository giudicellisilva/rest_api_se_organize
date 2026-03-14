package silva.giudicelli.rest_api_se_organize.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_phrases")
public class Phrase {
	@Id
	@GeneratedValue
	private Long id;
	private String phrase;
	private String author;
	
	@ManyToOne
	private User user;
}
