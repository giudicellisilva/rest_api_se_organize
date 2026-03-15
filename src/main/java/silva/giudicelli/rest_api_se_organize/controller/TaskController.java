package silva.giudicelli.rest_api_se_organize.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import silva.giudicelli.rest_api_se_organize.controller.request.TaskRequest;
import silva.giudicelli.rest_api_se_organize.controller.response.TaskResponse;
import silva.giudicelli.rest_api_se_organize.model.FitnessTask;
import silva.giudicelli.rest_api_se_organize.model.MusicTask;
import silva.giudicelli.rest_api_se_organize.model.SimpleTask;
import silva.giudicelli.rest_api_se_organize.model.StudyTask;
import silva.giudicelli.rest_api_se_organize.model.Task;
import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.model.WorkTask;
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
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_SUBSCRIBER', 'SCOPE_BASIC')")
	public TaskResponse create(@RequestBody @Valid TaskRequest request, @AuthenticationPrincipal Jwt jwt) {
	    Long userId = Long.parseLong(jwt.getSubject());
	    User user = userService.findById(userId).orElseThrow();
	
	    String type = request.type().toUpperCase();
	    boolean isPremiumType = type.equals("FITNESS") || type.equals("STUDY");
	
	    String scopes = jwt.getClaimAsString("scope");
	    boolean isSubscriber = scopes != null && (scopes.contains("SUBSCRIBER") || scopes.contains("ADMIN"));
	
	    if (isPremiumType && !isSubscriber) {
	        throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, 
	            "O tipo " + type + " é exclusivo para assinantes Premium!");
	    }
	
	    Task task = switch (type) {
	        case "MUSIC" -> {
	            MusicTask music = new MusicTask();
	            music.setInstrument(request.instrument());
	            music.setSheetMusicLink(request.sheetMusicLink());
	            yield music;
	        }
	        case "FITNESS" -> {
	            FitnessTask fitness = new FitnessTask();
	            fitness.setRepetitions(request.repetitions());
	            fitness.setMuscleGroup(request.muscleGroup());
	            yield fitness;
	        }
	        case "STUDY" -> {
	            StudyTask study = new StudyTask();
	            study.setSubject(request.subject());
	            study.setResourceLink(request.resourceLink());
	            study.setDurationMinutes(request.durationMinutes());
	            yield study;
	        }
	        case "WORK" -> {
	            WorkTask work = new WorkTask();
	            work.setProject(request.project());
	            work.setPriority(request.priority());
	            work.setDeadline(request.deadline());
	            yield work;
	        }
	        default -> new SimpleTask();
	    };
	
	    // 4. Dados comuns
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
    @Transactional
	@PreAuthorize("hasAnyAuthority('SCOPE_ADMIN', 'SCOPE_SUBSCRIBER', 'SCOPE_BASIC')")
	public TaskResponse update(@PathVariable Long id, @RequestBody @Valid TaskRequest request, @AuthenticationPrincipal Jwt jwt) {
	    Long userId = Long.parseLong(jwt.getSubject());
	    
	    // 1. Busca a tarefa e valida a posse
	    Task taskExistente = taskService.findById(id);
	    if (!taskExistente.getUser().getId().equals(userId)) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: você não é o dono desta tarefa");
	    }
	
	    // 2. Validação Premium para o NOVO tipo solicitado
	    String novoTipo = request.type().toUpperCase();
	    boolean isPremiumType = novoTipo.equals("FITNESS") || novoTipo.equals("STUDY");
	    
	    String scopes = jwt.getClaimAsString("scope");
	    boolean isSubscriber = scopes != null && (scopes.contains("SUBSCRIBER") || scopes.contains("ADMIN"));
	
	    if (isPremiumType && !isSubscriber) {
	        throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, 
	            "Upgrade negado: o tipo " + novoTipo + " é exclusivo para assinantes Premium!");
	    }
	
	    String tipoNoBanco = getTaskType(taskExistente);
	
	    // 3. Se o tipo mudou (ex: de WORK para STUDY)
	    if (!tipoNoBanco.equalsIgnoreCase(novoTipo)) {
	        User user = taskExistente.getUser();
	        Integer currentOrder = taskExistente.getOrder();
	
	        // Remove a antiga para criar a nova instância da subclasse correta
	        taskService.delete(id, user); 
	
	        Task novaTask = createSpecificTask(request);
	        
	        novaTask.setTitle(request.title());
	        novaTask.setDescription(request.description());
	        novaTask.setDate(request.date());
	        novaTask.setUser(user);
	        novaTask.setOrder(currentOrder);
	
	        return new TaskResponse(taskService.save(novaTask));
	    }
	
	    // 4. Se o tipo é o mesmo, apenas atualiza os campos
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