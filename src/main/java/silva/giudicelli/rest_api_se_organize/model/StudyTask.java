package silva.giudicelli.rest_api_se_organize.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_tasks_study")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudyTask extends Task {
    private String subject;
    private String resourceLink;
    private Integer durationMinutes;
    
    
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getResourceLink() {
		return resourceLink;
	}
	public void setResourceLink(String resourceLink) {
		this.resourceLink = resourceLink;
	}
	public Integer getDurationMinutes() {
		return durationMinutes;
	}
	public void setDurationMinutes(Integer durationMinutes) {
		this.durationMinutes = durationMinutes;
	}
    
    
}
