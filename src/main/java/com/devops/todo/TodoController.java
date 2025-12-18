package com.devops.todo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final Map<Long, Todo> storage = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo create(@Valid @RequestBody Todo todo) {
        long id = idGenerator.incrementAndGet();
        todo.setId(id);
        storage.put(id, todo);
        return todo;
    }

    // READ ALL
    @GetMapping
    public Collection<Todo> getAll() {
        return storage.values();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Todo getById(@PathVariable Long id) {
        Todo todo = storage.get(id);
        if (todo == null) {
            throw new NoSuchElementException("Todo not found with id " + id);
        }
        return todo;
    }

    // UPDATE
    @PutMapping("/{id}")
    public Todo update(@PathVariable Long id, @Valid @RequestBody Todo updated) {
        if (!storage.containsKey(id)) {
            throw new NoSuchElementException("Todo not found with id " + id);
        }
        updated.setId(id);
        storage.put(id, updated);
        return updated;
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!storage.containsKey(id)) {
            throw new NoSuchElementException("Todo not found with id " + id);
        }
        storage.remove(id);
    }
}

