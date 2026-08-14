package com.taskmanager.enterprizetaskmanager.entity;

import com.taskmanager.enterprizetaskmanager.enums.priority;
import com.taskmanager.enterprizetaskmanager.enums.status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @Column(name = "task_id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "task_title")
    @NotBlank(message = "task title can't be empty")
    private String title;

    @Column(name = "task_status")
    private status status;

    @Column(name = "task_priority")
    private priority priority;

    @Column(name = "task_created_time")
    private LocalDateTime createdAt;

    @Column(name = "task_dueDate")
    private LocalDateTime dueDate;

    public Task() {
    }

    public Task(String title) {
        this.title = title;
    }

    public Task(Integer id, String title, com.taskmanager.enterprizetaskmanager.enums.status status, com.taskmanager.enterprizetaskmanager.enums.priority priority, LocalDateTime createdAt, LocalDateTime dueDate) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public com.taskmanager.enterprizetaskmanager.enums.status getStatus() {
        return status;
    }

    public void setStatus(com.taskmanager.enterprizetaskmanager.enums.status status) {
        this.status = status;
    }

    public com.taskmanager.enterprizetaskmanager.enums.priority getPriority() {
        return priority;
    }

    public void setPriority(com.taskmanager.enterprizetaskmanager.enums.priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
