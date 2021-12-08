package com.etho.pm.service.mapper;

import com.etho.pm.domain.Users;
import com.etho.pm.service.dto.UsersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Users} and its DTO {@link UsersDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsersTypeMapper.class })
public interface UsersMapper extends EntityMapper<UsersDTO, Users> {
    @Mapping(target = "usersType", source = "usersType", qualifiedByName = "id")
    UsersDTO toDto(Users s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UsersDTO toDtoId(Users users);
}
