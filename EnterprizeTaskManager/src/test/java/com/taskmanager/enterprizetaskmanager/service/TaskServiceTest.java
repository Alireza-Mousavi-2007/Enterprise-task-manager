package com.taskmanager.enterprizetaskmanager.service;

import com.taskmanager.enterprizetaskmanager.dto.TaskDTO;
import com.taskmanager.enterprizetaskmanager.entity.Task;
import com.taskmanager.enterprizetaskmanager.repository.TaskRepository;
import com.taskmanager.enterprizetaskmanager.service.impl.TaskServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.taskmanager.enterprizetaskmanager.enums.Priority.LOW;
import static com.taskmanager.enterprizetaskmanager.enums.Status.PENDING;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;

    @BeforeEach
    public void init() {
        task = Task.builder().id(1).title("title").status(PENDING).priority(LOW)
                .createdAt(LocalDateTime.now()).dueDate(LocalDateTime.now()).build();
    }

    @Test
    public void addTask_add_task() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        TaskDTO dto = TaskDTO.builder().title("title").status(PENDING).priority(LOW)
                .dueDate(LocalDateTime.now()).build();

        var tested = taskService.addTAsk(dto);

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getTitle()).isNotEmpty();

        Mockito.verify(taskRepository).save(any(Task.class));

    }

    @Test
    public void getTaskById_get_task() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        var tested = taskService.getTaskById(task.getId());

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getTitle()).isNotEmpty();

        Mockito.verify(taskRepository).findById(anyInt());

    }

    @Test
    public void getAllTasks_getAll_ListOfTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        var tested = taskService.getAllTAsks();

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.size()).isGreaterThan(0);
        Assertions.assertThat(tested).contains(task);

        Mockito.verify(taskRepository).findAll();

    }

    @Test
    public void updateTask_update_task() {
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        TaskDTO dto = TaskDTO.builder().title("title").status(PENDING).priority(LOW)
                .dueDate(LocalDateTime.now()).build();

        var tested = taskService.updateTask(task.getId(), dto);

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getId()).isGreaterThan(0);
        Assertions.assertThat(tested.getTitle()).isNotEmpty();

        Mockito.verify(taskRepository).findById(anyInt());
        Mockito.verify(taskRepository).save(any(Task.class));

    }

    @Test
    public void updateTaskStatus_updateStatus_task(){
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        var tested = taskService.updateTAskStatus(task.getId(),task.getStatus());

        Assertions.assertThat(tested).isNotNull();
        Assertions.assertThat(tested.getStatus()).isEqualTo(task.getStatus());

        Mockito.verify(taskRepository).findById(anyInt());
        Mockito.verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void deleteTask_delete_void(){
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));

        taskService.deleteTask(task.getId());

        Mockito.verify(taskRepository).findById(anyInt());
        Mockito.verify(taskRepository).deleteById(anyInt());
    }
}
