package com.isateca.packages.roles;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public List<RoleEntity> findAll() {
        return roleRepository.findAll();
    }

    @Transactional
    public RoleEntity save(RoleEntity role) {
        if (role.getName() == null || role.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío.");
        }
        return roleRepository.save(role);
    }

    @Transactional
    public void delete(Integer roleId) {
        roleRepository.deleteById(roleId);
    }
}