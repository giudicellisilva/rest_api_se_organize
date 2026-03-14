package silva.giudicelli.rest_api_se_organize.controller.response;

import silva.giudicelli.rest_api_se_organize.model.MusicTask;
import silva.giudicelli.rest_api_se_organize.model.Task;

public class TaskResponse {
    private Long id;
    private String title;
    private String type;

    public TaskResponse(Task model) {
        this.id = model.getId();
        this.title = model.getTitle();
        this.type = (model instanceof MusicTask) ? "MUSIC" : "SIMPLE";
    }

    // O JACKSON SÓ VÊ O QUE TEM GETTER PÚBLICO
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
}