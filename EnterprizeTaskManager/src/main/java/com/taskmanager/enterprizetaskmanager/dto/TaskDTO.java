package com.taskmanager.enterprizetaskmanager.dto;

import com.taskmanager.enterprizetaskmanager.enums.priority;

import java.time.LocalDateTime;

public class TaskDTO {

    private String title;

    private priority priority;

    private LocalDateTime dueDate;

    public TaskDTO(String title, com.taskmanager.enterprizetaskmanager.enums.priority priority) {
        this.title = title;
        this.priority = priority;
    }

    public TaskDTO(String title, com.taskmanager.enterprizetaskmanager.enums.priority priority, LocalDateTime dueDate) {
        this.title = title;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public com.taskmanager.enterprizetaskmanager.enums.priority getPriority() {
        return priority;
    }

    public void setPriority(com.taskmanager.enterprizetaskmanager.enums.priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
