package com.etho.pm.service.mapper;

import com.etho.pm.domain.PremiunDetails;
import com.etho.pm.service.dto.PremiunDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PremiunDetails} and its DTO {@link PremiunDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PremiunDetailsMapper extends EntityMapper<PremiunDetailsDTO, PremiunDetails> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PremiunDetailsDTO toDtoId(PremiunDetails premiunDetails);
}
