package org.uteq.resoluciones.resoluciones.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uteq.resoluciones.resoluciones.entities.Instance;
import org.uteq.resoluciones.resoluciones.repository.InstanceRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InstanceServiceImpl implements InstanceService {

    private final InstanceRepository instanceRepository;

    public InstanceServiceImpl(InstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    @Override
    public Instance save(Instance instance) {
        if (instance.getId() == null) {
            instance.setCreationDate(LocalDateTime.now());
        }
        instance.setUpdateDate(LocalDateTime.now());
        return instanceRepository.save(instance);
    }

    @Override
    public List<Instance> findAll() {
        return instanceRepository.findAll();
    }

    @Override
    public Instance findById(Long id) {
        return instanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Instancia no encontrada con id: " + id));
    }

    @Override
    public List<Instance> findByLevelId(Long idLevel) {
        return instanceRepository.findByLevel_Id(idLevel);
    }

    @Override
    public List<Instance> findByInstanceFatherId(Long idInstanceFather) {
        return instanceRepository.findByInstanceFather_Id(idInstanceFather);
    }

    @Override
    public void deleteById(Long id) {
        if (!instanceRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: la instancia no existe");
        }
        instanceRepository.deleteById(id);
    }
}
