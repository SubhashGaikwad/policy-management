package com.etho.pm.repository;

import com.etho.pm.domain.Nominee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Nominee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NomineeRepository extends JpaRepository<Nominee, Long>, JpaSpecificationExecutor<Nominee> {}
