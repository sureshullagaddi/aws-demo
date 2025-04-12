package com.aws.rest.api.service;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskExecutor taskExecutor;

    public TaskService(TaskExecutor taskExecutor){
        this.taskExecutor = taskExecutor;
    }
    public void processTask(String taskName) {
        taskExecutor.execute(() -> {
            System.out.println("Processing: " + taskName + " - " + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Completed: " + taskName);
        });
    }
}
