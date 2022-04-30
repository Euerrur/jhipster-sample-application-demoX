package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Classroom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Classroom entity.
 */
@Repository
public interface ClassroomRepository
    extends ClassroomRepositoryWithBagRelationships, JpaRepository<Classroom, Long>, JpaSpecificationExecutor<Classroom> {
    default Optional<Classroom> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Classroom> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Classroom> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
