package silva.giudicelli.rest_api_se_organize.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_tasks_fitness")
@DiscriminatorValue("FITESS")
@Data
@EqualsAndHashCode(callSuper = true)
public class FitnessTask extends Task {
    private Integer repetitions;
    private String muscleGroup;
    
	public Integer getRepetitions() {
		return repetitions;
	}
	public void setRepetitions(Integer repetitions) {
		this.repetitions = repetitions;
	}
	public String getMuscleGroup() {
		return muscleGroup;
	}
	public void setMuscleGroup(String muscleGroup) {
		this.muscleGroup = muscleGroup;
	}
    
    
}