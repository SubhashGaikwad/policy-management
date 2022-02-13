package com.etho.pm.repository;

import com.etho.pm.domain.PremiunDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PremiunDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PremiunDetailsRepository extends JpaRepository<PremiunDetails, Long>, JpaSpecificationExecutor<PremiunDetails> {}
