package com.etho.pm.repository;

import com.etho.pm.domain.ParameterLookup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ParameterLookup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParameterLookupRepository extends JpaRepository<ParameterLookup, Long>, JpaSpecificationExecutor<ParameterLookup> {}
