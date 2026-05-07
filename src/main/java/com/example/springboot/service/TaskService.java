package com.example.springboot.service;

import com.example.springboot.model.Task;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

    private final Map<Long, Task> store = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<Task> getAllTasks() {
        return new ArrayList<>(store.values());
    }

    @Async("taskExecutor")
    public CompletableFuture<Task> processTaskAsync(Task task) {
        long id = idCounter.getAndIncrement();
        task.setId(id);
        task.setStatus("PROCESSING");
        store.put(id, task);

        System.out.printf("[%s] Starting task %d%n", Thread.currentThread().getName(), id);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            task.setStatus("FAILED");
            return CompletableFuture.completedFuture(task);
        }

        task.setStatus("DONE");
        store.put(id, task);
        System.out.printf("[%s] Finished task %d%n", Thread.currentThread().getName(), id);

        return CompletableFuture.completedFuture(task);
    }
}
