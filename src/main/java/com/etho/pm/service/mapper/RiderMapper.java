package com.etho.pm.service.mapper;

import com.etho.pm.domain.Rider;
import com.etho.pm.service.dto.RiderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rider} and its DTO {@link RiderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RiderMapper extends EntityMapper<RiderDTO, Rider> {}
