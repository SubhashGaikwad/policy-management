package com.etho.pm.repository;

import com.etho.pm.domain.VehicleDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the VehicleDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleDetailsRepository extends JpaRepository<VehicleDetails, Long>, JpaSpecificationExecutor<VehicleDetails> {}
