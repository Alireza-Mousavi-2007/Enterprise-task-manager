package com.taskmanager.enterprizetaskmanager.dto;

import com.taskmanager.enterprizetaskmanager.enums.Priority;
import com.taskmanager.enterprizetaskmanager.enums.Status;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class TaskDTO {

    @NotBlank(message = "Task title can't be empty")
    private String title;

    private Status status;

    private Priority priority;

    private LocalDateTime dueDate;

    public TaskDTO() {
    }

    public TaskDTO(String title, Priority priority) {
        this.title = title;
        this.priority = priority;
    }

    public TaskDTO(String title, Priority priority, LocalDateTime dueDate) {
        this.title = title;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public TaskDTO(String title, Status status, Priority priority, LocalDateTime dueDate) {
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
