package io.github.venkat1701.yugaantar.commons.services;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.commons.security.SecuredResource;
import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;
import io.github.venkat1701.yugaantar.utilities.annotations.UserSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public class GenericPersistenceService<T, ID, SECURITY extends Enum<SECURITY> & SecurityPermission> {
    private final JpaRepository<T, ID> repository;
    private final GenericMapper<T, ?> mapper;
    private final Class<SECURITY> securityClass;
    private final GenericSecurityEvaluator evaluator;
    public GenericPersistenceService(JpaRepository<T, ID> repository, GenericMapper<T, ?> mapper, Class<SECURITY> securityClass, GenericSecurityEvaluator evaluator) {
        this.securityClass = securityClass;
        this.repository = repository;
        this.mapper = mapper;
        this.evaluator = evaluator;
    }

    public boolean checkPermission(Authentication authentication, String permissionName) throws AccessDeniedException {
        try{
            SECURITY permission = Enum.valueOf(securityClass, permissionName);
            boolean hasPermission = this.evaluator.hasPermission(authentication, this, permission);

            if(!hasPermission) {
                throw new AccessDeniedException("Access is Denied");
            }
            return true;
        } catch(IllegalArgumentException e) {
            throw new AccessDeniedException("Invalid permission: "+permissionName);
        }
    }

    @SecuredResource(permission = "VIEW_ALL", securityClass = SecurityPermission.class)
    public List<T> getAll() {
        return repository.findAll();
    }

    @SecuredResource(permission = "VIEW_ALL", securityClass = SecurityPermission.class)
    public List<? extends Object> getAllDto() {
        return mapper.toDtoList(repository.findAll());
    }

    @SecuredResource(permission = "VIEW", securityClass = SecurityPermission.class)
    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    @SecuredResource(permission="VIEW", securityClass = SecurityPermission.class)
    public Optional<? extends Object> getByIdDto(ID id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @SecuredResource(permission="CREATE", securityClass = SecurityPermission.class)
    public T save(T entity) {
        return repository.save(entity);
    }

    @SecuredResource(permission="UPDATE", securityClass = SecurityPermission.class)
    public Optional<T> update(ID id, T entity) {
        return repository.findById(id)
                .map(existing -> {
                    T updated = mapper.updateEntity(existing, entity);
                    return repository.save(updated);
                });
    }

    @SecuredResource(permission="DELETE", securityClass = SecurityPermission.class)
    public boolean delete(ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
