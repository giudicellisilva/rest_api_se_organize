package silva.giudicelli.rest_api_se_organize.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import silva.giudicelli.rest_api_se_organize.model.Task;
import silva.giudicelli.rest_api_se_organize.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findAllByUser(User user);
    List<Task> findAllByUserAndDateOrderByOrderAsc(User user, LocalDate date);
    Task findFirstByUserAndDateOrderByOrderDesc(User user, LocalDate date);
    
}