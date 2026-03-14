package silva.giudicelli.rest_api_se_organize.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tb_tasks_simple")
@DiscriminatorValue("SIMPLE")
@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleTask extends Task {
}