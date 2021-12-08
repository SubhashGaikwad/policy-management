package com.etho.pm.service.mapper;

import com.etho.pm.domain.CompanyType;
import com.etho.pm.service.dto.CompanyTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompanyType} and its DTO {@link CompanyTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyTypeMapper extends EntityMapper<CompanyTypeDTO, CompanyType> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompanyTypeDTO toDtoId(CompanyType companyType);
}
