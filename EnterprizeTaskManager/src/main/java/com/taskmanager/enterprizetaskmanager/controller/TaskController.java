package com.taskmanager.enterprizetaskmanager.controller;

import com.taskmanager.enterprizetaskmanager.dto.TaskDTO;
import com.taskmanager.enterprizetaskmanager.entity.Task;
import com.taskmanager.enterprizetaskmanager.enums.status;
import com.taskmanager.enterprizetaskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<String> addTask(@Valid @RequestBody TaskDTO taskDTO) {
        var task = taskService.addTAsk(taskDTO);
        return ResponseEntity.ok("task " + task.getTitle() + " added.");
    }

    @GetMapping("/id")
    public Task getTaskById(@PathVariable Integer id) {
        var task = taskService.getTaskById(id);
        return task;
    }

    @GetMapping
    public List<Task> getAllTAsks() {
        return taskService.getAllTAsks();
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Integer id, @RequestBody TaskDTO taskDTO) {

        var task = taskService.updateTask(id, taskDTO);
        return task;
    }

    @PutMapping("/{id}/status")
    public Task updateTAskStatus(@PathVariable Integer id, @RequestBody status status) {
        var task = taskService.updateTAskStatus(id, status);
        return task;
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Integer id) {
            taskService.deleteTask(id);
    }


}
