package io.github.venkat1701.yugaantar.commons.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class GenericCrudService<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(GenericCrudService.class);

    private final JpaRepository<T, ID> repository;

    public GenericCrudService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        logger.debug("Fetching all entities");
        return repository.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        logger.debug("Fetching paged entities");
        return repository.findAll(pageable);
    }

    public Optional<T> findById(ID id) {
        logger.debug("Fetching entity by ID: {}", id);
        return repository.findById(id);
    }

    @Transactional
    public T save(T entity) {
        logger.debug("Saving new entity: {}", entity);
        return repository.save(entity);
    }

    @Transactional
    public Optional<T> update(ID id, T entity) {
        logger.debug("Updating entity with ID: {} using entity: {}", id, entity);
        return repository.findById(id)
                .map(existing -> {
                    T updated = updateEntity(existing, entity);
                    return repository.save(updated);
                });
    }

    @Transactional
    public void deleteById(ID id) {
        logger.debug("Deleting entity with ID: {}", id);
        repository.deleteById(id);
    }

    protected T updateEntity(T existing, T updated) {
        return updated;
    }
}
