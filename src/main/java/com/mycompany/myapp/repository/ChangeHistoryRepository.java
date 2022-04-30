package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ChangeHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ChangeHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChangeHistoryRepository extends JpaRepository<ChangeHistory, Long>, JpaSpecificationExecutor<ChangeHistory> {}
