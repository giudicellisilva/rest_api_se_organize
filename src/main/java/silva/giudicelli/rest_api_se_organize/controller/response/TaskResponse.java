package silva.giudicelli.rest_api_se_organize.controller.response;

import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private Boolean completed;
    private LocalTime time;

    // Campos específicos (agregados no Response)
    private String instrument;
    private String sheetMusicLink;
    private Integer repetitions;
    private String muscleGroup;
    private String subject;
    private String resourceLink;
    private Integer durationMinutes;
    private String project;
    private String priority;
    private java.time.LocalDateTime deadline;

    public TaskResponse(Task model) {
        this.id = model.getId();
        this.title = model.getTitle();
        this.description = model.getDescription();
        this.completed = model.getCompleted();
        this.time = model.getTime();

        // Casting e preenchimento de campos específicos
        if (model instanceof MusicTask music) {
            this.type = "MUSIC";
            this.instrument = music.getInstrument();
            this.sheetMusicLink = music.getSheetMusicLink();
        } else if (model instanceof StudyTask study) {
            this.type = "STUDY";
            this.subject = study.getSubject();
            this.resourceLink = study.getResourceLink();
            this.durationMinutes = study.getDurationMinutes();
        } else if (model instanceof WorkTask work) {
            this.type = "WORK";
            this.project = work.getProject();
            this.priority = work.getPriority();
            this.deadline = work.getDeadline();
        } else if (model instanceof FitnessTask fitness) {
            this.type = "FITNESS";
            this.repetitions = fitness.getRepetitions();
            this.muscleGroup = fitness.getMuscleGroup();
        } else {
            this.type = "SIMPLE";
        }
    }

    // Getters para todos os campos (necessário para o Jackson serializar em JSON)
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public Boolean getCompleted() { return completed; }
    public LocalTime getTime() { return time; }
    public String getInstrument() { return instrument; }
    public String getSheetMusicLink() { return sheetMusicLink; }
    public Integer getRepetitions() { return repetitions; }
    public String getMuscleGroup() { return muscleGroup; }
    public String getSubject() { return subject; }
    public String getResourceLink() { return resourceLink; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public String getProject() { return project; }
    public String getPriority() { return priority; }
    public LocalDateTime getDeadline() { return deadline; }
}