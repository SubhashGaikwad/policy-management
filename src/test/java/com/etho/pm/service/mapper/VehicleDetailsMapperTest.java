package com.etho.pm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleDetailsMapperTest {

    private VehicleDetailsMapper vehicleDetailsMapper;

    @BeforeEach
    public void setUp() {
        vehicleDetailsMapper = new VehicleDetailsMapperImpl();
    }
}
