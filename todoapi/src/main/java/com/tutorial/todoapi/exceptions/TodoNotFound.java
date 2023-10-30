package com.tutorial.todoapi.exceptions;

public class TodoNotFound extends RuntimeException {
    
    public TodoNotFound(Long id){
        super("Tarefa não encontrada: " + id);
    }
}
