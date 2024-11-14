package io.github.venkat1701.yugaantar.commons.services;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class GenericPersistenceService<T, ID> {
    private final JpaRepository<T, ID> repository;
    private final GenericMapper<T, ?> mapper;

    public GenericPersistenceService(JpaRepository<T, ID> repository, GenericMapper<T, ?> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public List<? extends Object> getAllDto() {
        return mapper.toDtoList(repository.findAll());
    }

    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    public Optional<? extends Object> getByIdDto(ID id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public Optional<T> update(ID id, T entity) {
        return repository.findById(id)
                .map(existing -> {
                    T updated = mapper.updateEntity(existing, entity);
                    return repository.save(updated);
                });
    }

    public boolean delete(ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
