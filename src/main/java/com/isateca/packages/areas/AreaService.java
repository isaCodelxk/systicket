package com.isateca.packages.areas;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AreaService {

    private final AreaRepository areaRepository;
    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Transactional(readOnly = true)
    public List<AreaEntity> findAll() {
        return areaRepository.findAll();
    }

    @Transactional
    public AreaEntity save(AreaEntity area) {
        if (area.getName() == null || area.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del rol no puede estar vacío.");
        }
        return areaRepository.save(area);
    }

    @Transactional
    public void delete(Integer roleId) {
        areaRepository.deleteById(roleId);
    }
}
