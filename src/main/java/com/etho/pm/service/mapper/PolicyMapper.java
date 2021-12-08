package com.etho.pm.service.mapper;

import com.etho.pm.domain.Policy;
import com.etho.pm.service.dto.PolicyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Policy} and its DTO {@link PolicyDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsersMapper.class })
public interface PolicyMapper extends EntityMapper<PolicyDTO, Policy> {
    @Mapping(target = "users", source = "users", qualifiedByName = "id")
    PolicyDTO toDto(Policy s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PolicyDTO toDtoId(Policy policy);
}
