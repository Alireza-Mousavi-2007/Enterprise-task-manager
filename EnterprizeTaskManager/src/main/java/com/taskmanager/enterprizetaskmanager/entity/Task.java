package com.taskmanager.enterprizetaskmanager.entity;

import com.taskmanager.enterprizetaskmanager.enums.Priority;
import com.taskmanager.enterprizetaskmanager.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Builder
public class Task {

    @Id
    @Column(name = "task_id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "task_title")
    @NotBlank(message = "task title can't be empty")
    private String title;

    @Column(name = "task_status")
    private Status status;

    @Column(name = "task_priority")
    private Priority priority;

    @Column(name = "task_created_time")
    private LocalDateTime createdAt;

    @Column(name = "task_dueDate")
    private LocalDateTime dueDate;

    public Task() {
    }

    public Task(String title) {
        this.title = title;
    }

    public Task(Integer id, String title, Status status, Priority priority, LocalDateTime createdAt, LocalDateTime dueDate) {
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
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
