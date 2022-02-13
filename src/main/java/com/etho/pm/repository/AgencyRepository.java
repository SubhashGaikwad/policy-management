package com.etho.pm.repository;

import com.etho.pm.domain.Agency;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Agency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long>, JpaSpecificationExecutor<Agency> {}
