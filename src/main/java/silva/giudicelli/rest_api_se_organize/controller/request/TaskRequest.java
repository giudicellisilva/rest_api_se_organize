package silva.giudicelli.rest_api_se_organize.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskRequest(
    @NotBlank(message = "Título é obrigatório")
    String title,
    
    String description,
    
    @NotNull(message = "Data é obrigatória")
    LocalDate date,
    
    String type, // SIMPLE, MUSIC, FITNESS, STUDY, WORK
    
    // Campos específicos de Música
    String instrument,
    String sheetMusicLink,
    
    // Campos específicos do Fitness
    Integer repetitions,
    String muscleGroup,

    // Campos específicos de Estudo (Novos)
    String subject,
    String resourceLink,
    Integer durationMinutes,

    // Campos específicos de Trabalho (Novos)
    String project,
    String priority,
    LocalDateTime deadline

) {}