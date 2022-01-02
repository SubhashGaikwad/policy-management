package com.etho.pm.service.mapper;

import com.etho.pm.domain.Agency;
import com.etho.pm.service.dto.AgencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agency} and its DTO {@link AgencyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AgencyMapper extends EntityMapper<AgencyDTO, Agency> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgencyDTO toDtoId(Agency agency);
}
