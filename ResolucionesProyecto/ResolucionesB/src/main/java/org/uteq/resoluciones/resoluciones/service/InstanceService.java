package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.entities.Instance;

import java.util.List;

public interface InstanceService {

    // CREATE / UPDATE
    Instance save(Instance instance);

    // READ
    List<Instance> findAll();

    Instance findById(Long id);

    // DELETE
    void deleteById(Long id);

    // 🔥 FILTROS
    List<Instance> findByLevelId(Long idLevel);

    List<Instance> findByInstanceFatherId(Long idInstanceFather);
}
