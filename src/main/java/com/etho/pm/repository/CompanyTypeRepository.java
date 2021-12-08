package com.etho.pm.repository;

import com.etho.pm.domain.CompanyType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CompanyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Long>, JpaSpecificationExecutor<CompanyType> {}
