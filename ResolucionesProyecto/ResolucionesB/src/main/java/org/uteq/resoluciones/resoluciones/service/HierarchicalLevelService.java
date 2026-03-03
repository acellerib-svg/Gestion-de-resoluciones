package org.uteq.resoluciones.resoluciones.service;

import org.uteq.resoluciones.resoluciones.dto.HierarchicalLevelRequest;
import org.uteq.resoluciones.resoluciones.entities.Hierarchicallevel;

import java.util.List;

public interface HierarchicalLevelService {

    List<Hierarchicallevel> findAll();

    Hierarchicallevel findById(Long id);

    Hierarchicallevel create(HierarchicalLevelRequest request);

    Hierarchicallevel update(Long id, HierarchicalLevelRequest request);

    void delete(Long id);
}
