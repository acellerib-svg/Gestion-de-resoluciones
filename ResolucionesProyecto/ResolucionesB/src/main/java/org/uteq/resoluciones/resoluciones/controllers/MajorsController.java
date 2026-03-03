package org.uteq.resoluciones.resoluciones.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.entities.Majors;
import org.uteq.resoluciones.resoluciones.service.MajorsService;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MajorsController {

    private final MajorsService service;

    @GetMapping
    public List<Majors> list(@RequestParam(required = false) Long facultyId) {
        if (facultyId != null) {
            return service.findByFacultyId(facultyId);
        }
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Majors getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Majors create(@RequestBody Majors major) { return service.create(major); }

    @PutMapping("/{id}")
    public Majors update(@PathVariable Long id, @RequestBody Majors major) { return service.update(id, major); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
