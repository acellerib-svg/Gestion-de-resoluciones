package org.uteq.resoluciones.resoluciones.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.uteq.resoluciones.resoluciones.entities.Faculty;
import org.uteq.resoluciones.resoluciones.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/api/faculties")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FacultyController {

    private final FacultyService service;

    @GetMapping
    public List<Faculty> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public Faculty getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty create(@RequestBody Faculty faculty) { return service.create(faculty); }

    @PutMapping("/{id}")
    public Faculty update(@PathVariable Long id, @RequestBody Faculty faculty) { return service.update(id, faculty); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}
