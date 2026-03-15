package silva.giudicelli.rest_api_se_organize.controller.response;

import silva.giudicelli.rest_api_se_organize.model.FitnessTask;
import silva.giudicelli.rest_api_se_organize.model.MusicTask;
import silva.giudicelli.rest_api_se_organize.model.StudyTask;
import silva.giudicelli.rest_api_se_organize.model.Task;
import silva.giudicelli.rest_api_se_organize.model.WorkTask;

public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String type;

    public TaskResponse(Task model) {
        this.id = model.getId();
        this.title = model.getTitle();
	    	if (model instanceof MusicTask) {
	    	    this.type = "MUSIC";
	    	} else if (model instanceof StudyTask) {
	    	    this.type = "STUDY";
	    	} else if (model instanceof WorkTask) {
	    	    this.type = "WORK";
	    	} else if (model instanceof FitnessTask) {
	    	    this.type = "FITNESS";
	    	} else {
	    	    this.type = "SIMPLE";
	    	}
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() {return description; }
    public String getType() { return type; }
}