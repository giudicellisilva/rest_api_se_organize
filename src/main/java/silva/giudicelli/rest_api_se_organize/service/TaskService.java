package silva.giudicelli.rest_api_se_organize.service;

import silva.giudicelli.rest_api_se_organize.model.Task;
import silva.giudicelli.rest_api_se_organize.model.User;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    Task save(Task task);
    List<Task> findByDay(User user, LocalDate date);
    void delete(Long id, User user);
    void reorderTasks(Long userId, LocalDate date, List<Long> taskIds);
    Task findById(Long id);
    List<Task> listAll(User user);
    List<Task> findByPeriod(User user, LocalDate startDate, LocalDate endDate);
}