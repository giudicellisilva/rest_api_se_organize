package silva.giudicelli.rest_api_se_organize.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TaskRequest(
    @NotBlank(message = "Título é obrigatório")
    String title,
    
    String description,
    
    @NotNull(message = "Data é obrigatória")
    LocalDate date,
    
    String type, // "GENERIC" ou "MUSIC"
    
    // Campos específicos de Música
    String instrument,
    String sheetMusicLink,
    
    // Campos específicos do Fitness
    Integer repetitions,
    String muscleGroup

) {}