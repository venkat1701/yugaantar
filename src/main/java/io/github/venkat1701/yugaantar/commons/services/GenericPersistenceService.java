package io.github.venkat1701.yugaantar.commons.services;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.commons.security.SecuredResource;
import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;
import io.github.venkat1701.yugaantar.utilities.annotations.DefaultSecurityPermissionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class GenericPersistenceService<T, ID, SECURITY extends Enum<SECURITY> & SecurityPermission> {
    private final JpaRepository<T, ID> repository;
    private final GenericMapper<T, ?> mapper;
    private final Class<SECURITY> securityClass;

    @Autowired
    private final GenericSecurityEvaluator securityEvaluator;

    @Autowired
    private DefaultSecurityPermissionResolver permissionResolver;

    public GenericPersistenceService(
            JpaRepository<T, ID> repository,
            GenericMapper<T, ?> mapper,
            Class<SECURITY> securityClass,
            GenericSecurityEvaluator securityEvaluator
    ) {
        this.securityClass = securityClass;
        this.repository = repository;
        this.mapper = mapper;
        this.securityEvaluator = securityEvaluator;
    }

    /**
     * Retrieves current authentication from SecurityContextHolder
     * @return Current Authentication object
     * @throws AccessDeniedException if no authentication is available
     */
    private Authentication getCurrentAuthentication() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("No valid authentication found");
        }
        return authentication;
    }

    /**
     * Retrieves all entities with VIEW_ALL permission
     * @return List of all entities
     * @throws AccessDeniedException If user lacks VIEW_ALL permission
     */
    @Transactional(readOnly = true)
    @PreAuthorize("@genericSecurityEvaluator.hasPermission(authentication, 'getAll')")
    public List<T> getAll() throws AccessDeniedException {
        return repository.findAll();
    }

    /**
     * Retrieves all DTOs with VIEW_ALL permission
     * @return List of all DTOs
     * @throws AccessDeniedException If user lacks VIEW_ALL permission
     */
    @Transactional(readOnly = true)
    @PreAuthorize("@genericSecurityEvaluator.hasPermission(authentication, 'getAll')")
    public List<?> getAllDto() throws AccessDeniedException {
        return mapper.toDtoList(repository.findAll());
    }

    /**
     * Retrieves an entity by ID with VIEW permission
     * @param id Entity ID
     * @return Optional of entity
     * @throws AccessDeniedException If user lacks VIEW permission
     */
    @Transactional(readOnly = true)
    @PreAuthorize("@genericSecurityEvaluator.hasPermission(authentication, #root, @defaultSecurityPermissionResolver.resolve('VIEW'))")
    public Optional<T> getById(ID id) throws AccessDeniedException {
        return repository.findById(id);
    }

    /**
     * Retrieves a DTO by ID with VIEW permission
     * @param id Entity ID
     * @return Optional of DTO
     * @throws AccessDeniedException If user lacks VIEW permission
     */
    @Transactional(readOnly = true)
    @PreAuthorize("@genericSecurityEvaluator.hasPermission(authentication, #root, @defaultSecurityPermissionResolver.resolve('VIEW'))")
    public Optional<?> getByIdDto(ID id) throws AccessDeniedException {
        return repository.findById(id).map(mapper::toDto);
    }

    /**
     * Saves a new entity with CREATE permission
     * @param entity Entity to save
     * @return Saved entity
     * @throws AccessDeniedException If user lacks CREATE permission
     */
    @Transactional
    @PreAuthorize("@genericSecurityEvaluator.hasPermission(authentication, #root, @defaultSecurityPermissionResolver.resolve('CREATE'))")
    public T save(T entity) throws AccessDeniedException {
        return repository.save(entity);
    }

    /**
     * Updates an existing entity with UPDATE permission
     * @param id Entity ID
     * @param entity Updated entity
     * @return Optional of updated entity
     * @throws AccessDeniedException If user lacks UPDATE permission
     */
    @Transactional
    @PreAuthorize("@genericSecurityEvaluator.hasPermission(authentication, #root, @defaultSecurityPermissionResolver.resolve('UPDATE'))")
    public Optional<T> update(ID id, T entity) throws AccessDeniedException {
        return repository.findById(id)
                .map(existing -> {
                    T updated = mapper.updateEntity(existing, entity);
                    return repository.save(updated);
                });
    }

    /**
     * Deletes an entity with DELETE permission
     * @param id Entity ID
     * @return Boolean indicating successful deletion
     * @throws AccessDeniedException If user lacks DELETE permission
     */
    @Transactional
    @PreAuthorize("@genericSecurityEvaluator.hasPermission(authentication, #root, @defaultSecurityPermissionResolver.resolve('DELETE'))")
    public boolean delete(ID id) throws AccessDeniedException {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}