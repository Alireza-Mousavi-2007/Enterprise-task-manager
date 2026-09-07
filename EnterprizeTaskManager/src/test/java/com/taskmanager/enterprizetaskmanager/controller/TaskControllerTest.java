package com.taskmanager.enterprizetaskmanager.controller;


import com.taskmanager.enterprizetaskmanager.dto.TaskDTO;
import com.taskmanager.enterprizetaskmanager.entity.Task;
import com.taskmanager.enterprizetaskmanager.enums.Priority;
import com.taskmanager.enterprizetaskmanager.enums.Status;
import com.taskmanager.enterprizetaskmanager.security.jwt.JwtToken;
import com.taskmanager.enterprizetaskmanager.service.TaskService;
import com.taskmanager.enterprizetaskmanager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private TaskService taskService;
    @MockitoBean
    private JwtToken jwtToken;
    @MockitoBean
    private UserDetailsService userDetailsService;

    private Task task;

    @BeforeEach
    public void init() {
        task = Task.builder().id(1).title("title").status(Status.PENDING).priority(Priority.LOW).createdAt(LocalDateTime.now()).dueDate(LocalDateTime.now()).build();
    }

    @Test
    @WithMockUser(username = "alireza", authorities = {"read"})
    public void addTask_post_responseEntity() throws Exception {
        TaskDTO dto = TaskDTO.builder().title("title")
                .status(Status.PENDING)
                .priority(Priority.LOW)
                .dueDate(LocalDateTime.now()).build();

        when(taskService.addTAsk(any(TaskDTO.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("title"));
    }

    @Test
    @WithMockUser(username = "alireza", authorities = {"read"})
    public void getTaskById_get_responseEntity() throws Exception {
        when(taskService.getTaskById(anyInt())).thenReturn(task);

        mockMvc.perform(get("/api/tasks/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()));
    }

    @Test
    @WithMockUser(username = "alireza", authorities = {"read"})
    public void getAllTasks_get_responseEntity() throws Exception {
        when(taskService.getAllTAsks()).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value(task.getTitle()));
    }

    @Test
    @WithMockUser(username = "alireza", authorities = {"update"})
    public void updateTask_put_responseEntity() throws Exception {
        TaskDTO dto = TaskDTO.builder().title("title")
                .status(Status.PENDING)
                .priority(Priority.LOW)
                .dueDate(LocalDateTime.now()).build();
        when(taskService.updateTask(anyInt(), any(TaskDTO.class))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/{id}", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.title").value(dto.getTitle()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "alireza", authorities = {"update"})
    public void updateTaskStatus_put_responseEntity() throws Exception {
        when(taskService.updateTAskStatus(anyInt(), any(Status.class))).thenReturn(task);


        mockMvc.perform(put("/api/tasks/{id}/status", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"" + Status.PENDING.name() + "\""))
                .andExpect(jsonPath("$.status").value(String.valueOf(task.getStatus())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "alireza", authorities = {"update"})
    public void deleteTask_delete_void() throws Exception {

        mockMvc.perform(delete("/api/tasks/{id}", 1).with(csrf())).andExpect(status().isOk());

        verify(taskService).deleteTask(1);
    }
}
