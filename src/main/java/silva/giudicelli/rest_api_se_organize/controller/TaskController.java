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
    @Transactional // Importante para garantir que o delete e o create ocorram juntos
    public TaskResponse update(@PathVariable Long id, @RequestBody @Valid TaskRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        Task taskExistente = taskService.findById(id);

        // Validação de Segurança
        if (!taskExistente.getUser().getId().equals(userId)) {
            throw new RuntimeException("Acesso negado: você não é o dono desta tarefa");
        }

        // 1. Identificar o tipo atual da tarefa no banco
        String tipoNoBanco = "GENERIC";
        if (taskExistente instanceof MusicTask) tipoNoBanco = "MUSIC";
        else if (taskExistente instanceof FitnessTask) tipoNoBanco = "FITNESS";

        // 2. Verificar se o tipo mudou comparando com o request
        if (!tipoNoBanco.equalsIgnoreCase(request.type())) {
            // Se mudou, precisamos trocar a classe. O jeito mais seguro é deletar e criar.
            User user = taskExistente.getUser();
            Integer currentOrder = taskExistente.getOrder(); // Preservamos a ordem original
            
            taskService.delete(id, user); // Removemos a tarefa antiga (e seus campos específicos)

            // Criamos a nova tarefa com a classe correta
            Task novaTask;
            if ("MUSIC".equalsIgnoreCase(request.type())) {
                MusicTask music = new MusicTask();
                music.setInstrument(request.instrument());
                music.setSheetMusicLink(request.sheetMusicLink());
                novaTask = music;
            } else if ("FITNESS".equalsIgnoreCase(request.type())) {
                FitnessTask fitness = new FitnessTask();
                fitness.setRepetitions(request.repetitions());
                fitness.setMuscleGroup(request.muscleGroup());
                novaTask = fitness;
            } else {
                novaTask = new SimpleTask();
            }

            novaTask.setTitle(request.title());
            novaTask.setDescription(request.description());
            novaTask.setDate(request.date());
            novaTask.setUser(user);
            novaTask.setOrder(currentOrder);

            return new TaskResponse(taskService.save(novaTask));
        }

        taskExistente.setTitle(request.title());
        taskExistente.setDescription(request.description());
        taskExistente.setDate(request.date());

        if (taskExistente instanceof MusicTask music) {
            music.setInstrument(request.instrument());
            music.setSheetMusicLink(request.sheetMusicLink());
        } else if (taskExistente instanceof FitnessTask fitness) {
            fitness.setRepetitions(request.repetitions());
            fitness.setMuscleGroup(request.muscleGroup());
        }

        return new TaskResponse(taskService.save(taskExistente));
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