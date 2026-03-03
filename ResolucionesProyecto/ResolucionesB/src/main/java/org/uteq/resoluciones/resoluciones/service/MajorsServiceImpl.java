package org.uteq.resoluciones.resoluciones.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.entities.Faculty;
import org.uteq.resoluciones.resoluciones.entities.Majors;
import org.uteq.resoluciones.resoluciones.repository.FacultyRepository;
import org.uteq.resoluciones.resoluciones.repository.MajorsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MajorsServiceImpl implements MajorsService {

    private final MajorsRepository repo;
    private final FacultyRepository facultyRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Majors> findAll() { return repo.findAll(); }

    @Override
    @Transactional(readOnly = true)
    public List<Majors> findByFacultyId(Long facultyId) { return repo.findByFacultyId(facultyId); }

    @Override
    @Transactional(readOnly = true)
    public Majors findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Carrera no encontrada"));
    }

    @Override
    public Majors create(Majors m) {
        if (m.getFaculty() != null && m.getFaculty().getId() != null) {
            Faculty faculty = facultyRepo.findById(m.getFaculty().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Facultad no encontrada"));
            m.setFaculty(faculty);
        }
        m.setCreationDate(LocalDateTime.now());
        m.setActive(true);
        return repo.save(m);
    }

    @Override
    public Majors update(Long id, Majors m) {
        Majors existing = findById(id);
        existing.setName(m.getName());
        existing.setDescription(m.getDescription());
        existing.setActive(m.getActive());
        if (m.getFaculty() != null && m.getFaculty().getId() != null) {
            Faculty faculty = facultyRepo.findById(m.getFaculty().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Facultad no encontrada"));
            existing.setFaculty(faculty);
        }
        existing.setUpdateDate(LocalDateTime.now());
        return repo.save(existing);
    }

    @Override
    public void delete(Long id) {
        Majors m = findById(id);
        m.setActive(false);
        m.setUpdateDate(LocalDateTime.now());
        repo.save(m);
    }
}
