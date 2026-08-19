package com.taskmanager.enterprizetaskmanager.controller;

import com.taskmanager.enterprizetaskmanager.dto.TaskDTO;
import com.taskmanager.enterprizetaskmanager.entity.Task;
import com.taskmanager.enterprizetaskmanager.enums.Status;
import com.taskmanager.enterprizetaskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Tasks",description = "for operating on tasks")
public class TaskController {

    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create')")
    @Operation(summary ="add task by TaskDTO",description = "private String title,private Status Status,private Priority Priority,private LocalDateTime dueDate")
    public ResponseEntity<String> addTask(@Valid @RequestBody TaskDTO taskDTO) {
        var task = taskService.addTAsk(taskDTO);
        return ResponseEntity.ok("task " + task.getTitle() + " added.");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read')")
    @Operation(summary ="get task by id")
        public Task getTaskById(@PathVariable Integer id) {
        var task = taskService.getTaskById(id);
        return task;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read')")
    @Operation(summary ="get all taks")
    public List<Task> getAllTAsks() {
        return taskService.getAllTAsks();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update')")
    @Operation(summary ="update task by id and taskDTO",description = "TaskDTO :private String title,private Status Status,private Priority Priority,private LocalDateTime dueDate")
    public Task updateTask(@PathVariable Integer id, @Valid @RequestBody TaskDTO taskDTO) {

        var task = taskService.updateTask(id, taskDTO);
        return task;
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('update')")
    @Operation(summary ="update task Status by id")
    public Task updateTAskStatus(@PathVariable Integer id, @Valid @RequestBody Status status) {
        var task = taskService.updateTAskStatus(id, status);
        return task;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete')")
    public void deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
    }


}
