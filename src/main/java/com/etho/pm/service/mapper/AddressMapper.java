package com.etho.pm.service.mapper;

import com.etho.pm.domain.Address;
import com.etho.pm.service.dto.AddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsersMapper.class, CompanyMapper.class })
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "users", source = "users", qualifiedByName = "id")
    @Mapping(target = "company", source = "company", qualifiedByName = "id")
    AddressDTO toDto(Address s);
}
