package com.taskmanager.enterprizetaskmanager.service;

import com.taskmanager.enterprizetaskmanager.dto.TaskDTO;
import com.taskmanager.enterprizetaskmanager.entity.Task;
import com.taskmanager.enterprizetaskmanager.enums.Status;

import java.util.List;

public interface TaskService {

    public Task addTAsk(TaskDTO  taskDTO);

    public Task getTaskById(Integer id);

    public List<Task> getAllTAsks();

    public Task updateTask(Integer taskId , TaskDTO taskDTO);

    public Task updateTAskStatus(Integer taskId, Status status);

    public void deleteTask(Integer id);

}
