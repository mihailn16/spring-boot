package com.example.springboot.controller;

import com.example.springboot.model.Task;
import com.example.springboot.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET http://localhost:8080/api/tasks
    @GetMapping
    public ResponseEntity<List<Task>> getTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // POST http://localhost:8080/api/tasks
    // Body: { "name": "my task" }
    @PostMapping
    public ResponseEntity<String> createTask(@Valid @RequestBody Task task) {
        taskService.processTaskAsync(task);
        return ResponseEntity.accepted().body("Task accepted and processing in background.");
    }
}
