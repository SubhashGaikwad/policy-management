package com.etho.pm.service.mapper;

import com.etho.pm.domain.ProductType;
import com.etho.pm.service.dto.ProductTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductType} and its DTO {@link ProductTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductTypeMapper extends EntityMapper<ProductTypeDTO, ProductType> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductTypeDTO toDtoId(ProductType productType);
}
