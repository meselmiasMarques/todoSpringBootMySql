package com.tutorial.todoapi.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.tutorial.todoapi.entities.Todo;

public interface TodoRepository extends JpaRepository<Todo,Long> {
    
}
