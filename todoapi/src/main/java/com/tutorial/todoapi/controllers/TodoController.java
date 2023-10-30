package com.tutorial.todoapi.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tutorial.todoapi.entities.Todo;
import com.tutorial.todoapi.exceptions.ErrorResponse;
import com.tutorial.todoapi.exceptions.TodoNotFound;
import com.tutorial.todoapi.repositories.TodoRepository;

@RestController
@RequestMapping(value = "/v1/todos")
public class TodoController {
    
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping
    public List<Todo> getAll(){
        List<Todo> list = todoRepository.findAll();
        return list;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id){
       Todo todo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFound(id));
       return ResponseEntity.ok(todo);
    }

    @PostMapping
    public ResponseEntity<Todo> insert (@RequestBody Todo todo){
       Todo result = todoRepository.save(todo);
       return ResponseEntity.ok().body(result);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Todo> update (@RequestBody Todo todo, @PathVariable Long id){
        return todoRepository.findById(id)
        .map(model -> {
            model.setName(todo.getName());
            model.setDone(todo.isDone());
            Todo todoSave = todoRepository.save(model);
            return ResponseEntity.ok(todoSave);
        
        }).orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(TodoNotFound.class)
    public ResponseEntity<ErrorResponse> handleTodoNaoEncontradoException(TodoNotFound ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }
 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Ocorreu um erro interno."));
    }
}
