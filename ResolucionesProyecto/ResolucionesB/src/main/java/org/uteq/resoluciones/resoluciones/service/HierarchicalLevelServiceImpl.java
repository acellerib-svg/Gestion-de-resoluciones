package org.uteq.resoluciones.resoluciones.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.dto.HierarchicalLevelRequest;
import org.uteq.resoluciones.resoluciones.entities.Hierarchicallevel;
import org.uteq.resoluciones.resoluciones.repository.HierarchicalLevelRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HierarchicalLevelServiceImpl implements HierarchicalLevelService {

    private final HierarchicalLevelRepository hierarchicalLevelRepository;

    // =========================
    // READ
    // =========================
    @Override
    @Transactional(readOnly = true)
    public List<Hierarchicallevel> findAll() {
        return hierarchicalLevelRepository.findAllByOrderByOrderAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Hierarchicallevel findById(Long id) {
        return hierarchicalLevelRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Nivel jerarquico no encontrado con id: " + id));
    }

    // =========================
    // CREATE
    // =========================
    @Override
    public Hierarchicallevel create(HierarchicalLevelRequest request) {
        Hierarchicallevel level = new Hierarchicallevel();
        level.setName(request.getName());
        level.setOrder(request.getLevelOrder());
        level.setDescription(request.getDescription());
        level.setActive(request.getActive());
        level.setCreationDate(LocalDateTime.now());

        return hierarchicalLevelRepository.save(level);
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    public Hierarchicallevel update(Long id, HierarchicalLevelRequest request) {
        Hierarchicallevel level = findById(id);

        level.setName(request.getName());
        level.setOrder(request.getLevelOrder());
        level.setDescription(request.getDescription());
        level.setActive(request.getActive());
        level.setUpdateDate(LocalDateTime.now());

        return hierarchicalLevelRepository.save(level);
    }

    // =========================
    // DELETE (borrado logico)
    // =========================
    @Override
    public void delete(Long id) {
        Hierarchicallevel level = findById(id);
        level.setActive(false);
        level.setUpdateDate(LocalDateTime.now());
        hierarchicalLevelRepository.save(level);
    }
}
