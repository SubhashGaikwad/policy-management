package com.etho.pm.service.mapper;

import com.etho.pm.domain.Nominee;
import com.etho.pm.service.dto.NomineeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Nominee} and its DTO {@link NomineeDTO}.
 */
@Mapper(componentModel = "spring", uses = { PolicyMapper.class })
public interface NomineeMapper extends EntityMapper<NomineeDTO, Nominee> {
    @Mapping(target = "policy", source = "policy", qualifiedByName = "id")
    NomineeDTO toDto(Nominee s);
}
