package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Classroom;
import com.mycompany.myapp.repository.ClassroomRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Classroom}.
 */
@Service
@Transactional
public class ClassroomService {

    private final Logger log = LoggerFactory.getLogger(ClassroomService.class);

    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    /**
     * Save a classroom.
     *
     * @param classroom the entity to save.
     * @return the persisted entity.
     */
    public Classroom save(Classroom classroom) {
        log.debug("Request to save Classroom : {}", classroom);
        return classroomRepository.save(classroom);
    }

    /**
     * Update a classroom.
     *
     * @param classroom the entity to save.
     * @return the persisted entity.
     */
    public Classroom update(Classroom classroom) {
        log.debug("Request to save Classroom : {}", classroom);
        return classroomRepository.save(classroom);
    }

    /**
     * Partially update a classroom.
     *
     * @param classroom the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Classroom> partialUpdate(Classroom classroom) {
        log.debug("Request to partially update Classroom : {}", classroom);

        return classroomRepository
            .findById(classroom.getId())
            .map(existingClassroom -> {
                if (classroom.getName() != null) {
                    existingClassroom.setName(classroom.getName());
                }
                if (classroom.getAddress() != null) {
                    existingClassroom.setAddress(classroom.getAddress());
                }

                return existingClassroom;
            })
            .map(classroomRepository::save);
    }

    /**
     * Get all the classrooms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Classroom> findAll() {
        log.debug("Request to get all Classrooms");
        return classroomRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the classrooms with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Classroom> findAllWithEagerRelationships(Pageable pageable) {
        return classroomRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one classroom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Classroom> findOne(Long id) {
        log.debug("Request to get Classroom : {}", id);
        return classroomRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the classroom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Classroom : {}", id);
        classroomRepository.deleteById(id);
    }
}
