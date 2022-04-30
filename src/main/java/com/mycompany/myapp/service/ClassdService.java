package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Classd;
import com.mycompany.myapp.repository.ClassdRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Classd}.
 */
@Service
@Transactional
public class ClassdService {

    private final Logger log = LoggerFactory.getLogger(ClassdService.class);

    private final ClassdRepository classdRepository;

    public ClassdService(ClassdRepository classdRepository) {
        this.classdRepository = classdRepository;
    }

    /**
     * Save a classd.
     *
     * @param classd the entity to save.
     * @return the persisted entity.
     */
    public Classd save(Classd classd) {
        log.debug("Request to save Classd : {}", classd);
        return classdRepository.save(classd);
    }

    /**
     * Update a classd.
     *
     * @param classd the entity to save.
     * @return the persisted entity.
     */
    public Classd update(Classd classd) {
        log.debug("Request to save Classd : {}", classd);
        return classdRepository.save(classd);
    }

    /**
     * Partially update a classd.
     *
     * @param classd the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Classd> partialUpdate(Classd classd) {
        log.debug("Request to partially update Classd : {}", classd);

        return classdRepository
            .findById(classd.getId())
            .map(existingClassd -> {
                if (classd.getName() != null) {
                    existingClassd.setName(classd.getName());
                }

                return existingClassd;
            })
            .map(classdRepository::save);
    }

    /**
     * Get all the classds.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Classd> findAll() {
        log.debug("Request to get all Classds");
        return classdRepository.findAll();
    }

    /**
     * Get one classd by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Classd> findOne(Long id) {
        log.debug("Request to get Classd : {}", id);
        return classdRepository.findById(id);
    }

    /**
     * Delete the classd by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Classd : {}", id);
        classdRepository.deleteById(id);
    }
}
