package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.entities.Faculty;
import org.uteq.resoluciones.resoluciones.repository.FacultyRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository repo;

    @Override
    @Transactional(readOnly = true)
    public List<Faculty> findAll() { return repo.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public Faculty findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Facultad no encontrada"));
    }

    @Override
    public Faculty create(Faculty f) {
        f.setCreationDate(LocalDateTime.now());
        f.setActive(true);
        return repo.save(f);
    }

    @Override
    public Faculty update(Long id, Faculty f) {
        Faculty existing = findById(id);
        existing.setName(f.getName());
        existing.setDescription(f.getDescription());
        existing.setActive(f.getActive());
        existing.setUpdateDate(LocalDateTime.now());
        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        Faculty f = findById(id);
        f.setActive(false);
        f.setUpdateDate(LocalDateTime.now());
        repo.save(f);
    }
}
