package io.github.venkat1701.yugaantar.commons.controllers;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.commons.security.SecuredResource;
import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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

public abstract class GenericCrudController<T, D, ID, S extends Enum<S> & SecurityPermission> {
    private static final Logger logger = LoggerFactory.getLogger(GenericCrudController.class);

    private final GenericPersistenceService<T, ID, S> service;
    private final GenericMapper<T, D> dtoConverter;

    public GenericCrudController(GenericPersistenceService<T, ID, S> service, GenericMapper<T, D> dtoConverter) {
        this.service = service;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping
    public ResponseEntity<List<D>> getAll(Authentication authentication) {
        logger.debug("Fetching all entities");
        try {
            List<T> entities = service.getAll();
            List<D> dtos = entities.stream()
                    .map(dtoConverter::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (AccessDeniedException e) {
            logger.warn("Access denied while fetching all entities: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> getById(@PathVariable ID id, Authentication authentication) {
        logger.debug("Fetching entity by ID: {}", id);
        try {
            Optional<T> entity = service.getById(id);
            return entity.map(dtoConverter::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (AccessDeniedException e) {
            logger.warn("Access denied while fetching entity {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping
    public ResponseEntity<D> create(@Valid @RequestBody D dto, Authentication authentication) {
        logger.debug("Creating new entity from DTO: {}", dto);
        try {
            T entity = dtoConverter.fromDto(dto);
            T savedEntity = service.save(entity);
            D savedDto = dtoConverter.toDto(savedEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
        } catch (AccessDeniedException e) {
            logger.warn("Access denied while creating entity: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<D> update(@PathVariable ID id, @Valid @RequestBody D dto, Authentication authentication) {
        logger.debug("Updating entity with ID: {} using DTO: {}", id, dto);
        try {
            T entity = dtoConverter.fromDto(dto);
            Optional<T> updatedEntity = service.update(id, entity);
            return updatedEntity.map(dtoConverter::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (AccessDeniedException e) {
            logger.warn("Access denied while updating entity {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id, Authentication authentication) {
        logger.debug("Deleting entity with ID: {}", id);
        try {
            if (service.delete(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (AccessDeniedException e) {
            logger.warn("Access denied while deleting entity {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access denied: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        logger.error("Unexpected error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }
}