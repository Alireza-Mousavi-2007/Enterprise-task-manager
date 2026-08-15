package com.taskmanager.enterprizetaskmanager.service.impl;

import com.taskmanager.enterprizetaskmanager.dto.TaskDTO;
import com.taskmanager.enterprizetaskmanager.entity.Task;
import com.taskmanager.enterprizetaskmanager.enums.status;
import com.taskmanager.enterprizetaskmanager.exceptions.TaskNotFoundException;
import com.taskmanager.enterprizetaskmanager.repository.TaskRepository;
import com.taskmanager.enterprizetaskmanager.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository repo;

    public TaskServiceImpl(TaskRepository repo) {
        this.repo = repo;
    }

    @Override
    public Task addTAsk(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(taskDTO.getDueDate());


        return repo.save(task);
    }

    @Override
    public Task getTaskById(Integer id) {
        var task = repo.findById(id);
        if (task.isPresent()) return task.get();
        else
            throw new TaskNotFoundException("there's no task with id = " + id);

    }

    @Override
    public List<Task> getAllTAsks() {
        return repo.findAll();
    }

    @Override
    public Task updateTask(Integer taskId, TaskDTO taskDTO) {

        var task = repo.findById(taskId);
        if (task.isPresent()) {
            task.get().setTitle(taskDTO.getTitle());
            task.get().setStatus(taskDTO.getStatus());
            task.get().setPriority(taskDTO.getPriority());
            task.get().setDueDate(taskDTO.getDueDate());
            return task.get();
        } else
            throw new TaskNotFoundException("there's no task with id = " + taskId);
    }

    @Override
    public Task updateTAskStatus(Integer taskId, status status) {
        var task = repo.findById(taskId);
        if(task.isPresent()){
            task.get().setStatus(status);
            return task.get();
        }
        throw new TaskNotFoundException("there's no task with id = " + taskId);
    }

    @Override
    public void deleteTask(Integer id) {
        var task=repo.findById(id);

        if(task.isPresent()) repo.deleteById(id);
        else throw new TaskNotFoundException("there's no task with id = " + id);
    }
}
