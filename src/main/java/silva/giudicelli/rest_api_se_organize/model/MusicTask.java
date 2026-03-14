package silva.giudicelli.rest_api_se_organize.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_tasks_music")
@DiscriminatorValue("MUSIC")
@Data
@EqualsAndHashCode(callSuper = true)
public class MusicTask extends Task {
    private String instrument;
    private String sheetMusicLink; // Link para partitura
    
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public String getSheetMusicLink() {
		return sheetMusicLink;
	}
	public void setSheetMusicLink(String sheetMusicLink) {
		this.sheetMusicLink = sheetMusicLink;
	}
    
    
}