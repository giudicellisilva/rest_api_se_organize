package silva.giudicelli.rest_api_se_organize.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import silva.giudicelli.rest_api_se_organize.model.Task;
import silva.giudicelli.rest_api_se_organize.model.User;
import silva.giudicelli.rest_api_se_organize.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskServiceImplement implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    @Transactional
    public Task save(Task task) {
        // Se for uma nova task e não tiver ordem definida
        if (task.getId() == null && task.getOrder() == null) {
            Task lastTask = taskRepository.findFirstByUserAndDateOrderByOrderDesc(
                task.getUser(), 
                task.getDate()
            );
            int nextOrder = (lastTask != null) ? lastTask.getOrder() + 1 : 0;
            task.setOrder(nextOrder);
        }
        return taskRepository.save(task);
    }

    @Override
    public List<Task> findByDay(User user, LocalDate date) {
        
    		List<Task> listTask = taskRepository.findAllByUserAndDateOrderByOrderAsc(user, date);
    		System.out.println("Lista: " + listTask);
    		return listTask;
    }

    @Override
    @Transactional
    public void delete(Long id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        
        // Verifica se a tarefa pertence ao usuário logado
        if (!task.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Acesso negado a esta tarefa");
        }
        
        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public void reorderTasks(Long userId, LocalDate date, List<Long> taskIds) {
        for (int i = 0; i < taskIds.size(); i++) {
            final int currentOrder = i; 
            Long taskId = taskIds.get(i);
            
            taskRepository.findById(taskId).ifPresent(t -> {
                if (t.getUser().getId().equals(userId)) {
                    t.setOrder(currentOrder); // Usamos a cópia aqui
                    taskRepository.save(t);
                }
            });
        }
    }
    
    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }

	@Override
	public List<Task> listAll(User user) {
		
		return taskRepository.findAllByUser(user);
	}
}