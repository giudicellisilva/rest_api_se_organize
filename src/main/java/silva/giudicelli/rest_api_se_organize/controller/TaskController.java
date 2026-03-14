package silva.giudicelli.rest_api_se_organize.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import silva.giudicelli.rest_api_se_organize.controller.request.TaskRequest;
import silva.giudicelli.rest_api_se_organize.controller.response.TaskResponse;
import silva.giudicelli.rest_api_se_organize.model.FitnessTask;
import silva.giudicelli.rest_api_se_organize.model.MusicTask;
import silva.giudicelli.rest_api_se_organize.model.SimpleTask;
import silva.giudicelli.rest_api_se_organize.model.Task;
import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.service.TaskService;
import silva.giudicelli.rest_api_se_organize.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_BASIC')")
    public TaskResponse create(@RequestBody @Valid TaskRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        User user = userService.findById(userId).orElseThrow();

        Task task;
        if ("MUSIC".equalsIgnoreCase(request.type())) {
            MusicTask music = new MusicTask();
            music.setInstrument(request.instrument());
            music.setSheetMusicLink(request.sheetMusicLink());
            task = music;
        } else if ("FITNESS".equalsIgnoreCase(request.type())) {
            FitnessTask fitness = new FitnessTask();
            fitness.setRepetitions(request.repetitions());
            fitness.setMuscleGroup(request.muscleGroup());
            task = fitness;
        } else {
            task = new SimpleTask(); 
        }

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDate(request.date());
        task.setUser(user);

        return new TaskResponse(taskService.save(task));
    }

    @GetMapping("/{date}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_BASIC')")
    public List<TaskResponse> listByDate(@PathVariable LocalDate date, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        User user = userService.findById(userId).orElseThrow();

        return taskService.findByDay(user, date).stream()
                .map(TaskResponse::new)
                .toList();
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_BASIC')")
    @Transactional
    public TaskResponse update(@PathVariable Long id, @RequestBody @Valid TaskRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        
        Task taskExistente = taskService.findById(id);
        if (!taskExistente.getUser().getId().equals(userId)) {
            throw new RuntimeException("Acesso negado: você não é o dono desta tarefa");
        }

        String tipoNoBanco = getTaskType(taskExistente);

        // Se o tipo mudou, entramos no fluxo de substituição
        if (!tipoNoBanco.equalsIgnoreCase(request.type())) {
            User user = taskExistente.getUser();
            Integer currentOrder = taskExistente.getOrder();

            taskService.delete(id, user); 

            Task novaTask = createSpecificTask(request);
            
            novaTask.setTitle(request.title());
            novaTask.setDescription(request.description());
            novaTask.setDate(request.date());
            novaTask.setUser(user);
            novaTask.setOrder(currentOrder);

            return new TaskResponse(taskService.save(novaTask));
        }

        updateTaskFields(taskExistente, request);
        return new TaskResponse(taskService.save(taskExistente));
    }


    private String getTaskType(Task task) {
        if (task instanceof MusicTask) return "MUSIC";
        if (task instanceof FitnessTask) return "FITNESS";
        return "SIMPLE";
    }

    private Task createSpecificTask(TaskRequest request) {
        if ("MUSIC".equalsIgnoreCase(request.type())) {
            MusicTask music = new MusicTask();
            music.setInstrument(request.instrument());
            music.setSheetMusicLink(request.sheetMusicLink());
            return music;
        } 
        if ("FITNESS".equalsIgnoreCase(request.type())) {
            FitnessTask fitness = new FitnessTask();
            fitness.setRepetitions(request.repetitions());
            fitness.setMuscleGroup(request.muscleGroup());
            return fitness;
        }
        return new SimpleTask();
    }

    private void updateTaskFields(Task task, TaskRequest request) {
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setDate(request.date());

        if (task instanceof MusicTask music) {
            music.setInstrument(request.instrument());
            music.setSheetMusicLink(request.sheetMusicLink());
        } else if (task instanceof FitnessTask fitness) {
            fitness.setRepetitions(request.repetitions());
            fitness.setMuscleGroup(request.muscleGroup());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_BASIC')")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        User user = userService.findById(userId).orElseThrow();
        
        // O service já possui a lógica de validar o dono que criamos antes
        taskService.delete(id, user);
    }
}