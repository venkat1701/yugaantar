package io.github.venkat1701.yugaantar.commons.controllers;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class GenericController<T, D, ID> {
    private static final Logger logger = LoggerFactory.getLogger(GenericController.class);

    private final GenericPersistenceService<T, ID> service;
    private final GenericMapper<T, D> dtoConverter;

    public GenericController(GenericPersistenceService<T, ID> service, GenericMapper<T, D> dtoConverter) {
        this.service = service;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping
    public ResponseEntity<List<D>> getAll() {
        logger.debug("Fetching all entities");
        List<T> entities = service.getAll();
        List<D> dtos = entities.stream()
                .map(dtoConverter::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> getById(@PathVariable ID id) {
        logger.debug("Fetching entity by ID: {}", id);
        Optional<T> entity = service.getById(id);
        return entity.map(dtoConverter::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<D> create(@Valid @RequestBody D dto) {
        logger.debug("Creating new entity from DTO: {}", dto);
        T entity = dtoConverter.fromDto(dto);
        T savedEntity = service.save(entity);
        D savedDto = dtoConverter.toDto(savedEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<D> update(@PathVariable ID id, @Valid @RequestBody D dto) {
        logger.debug("Updating entity with ID: {} using DTO: {}", id, dto);
        T entity = dtoConverter.fromDto(dto);
        Optional<T> updatedEntity = service.update(id, entity);
        return updatedEntity.map(dtoConverter::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        logger.debug("Deleting entity with ID: {}", id);
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        logger.error("Validation error: {}", ex.getMessage());
        BindingResult result = ex.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }
}
