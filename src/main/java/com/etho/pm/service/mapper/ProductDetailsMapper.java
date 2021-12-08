package com.etho.pm.service.mapper;

import com.etho.pm.domain.ProductDetails;
import com.etho.pm.service.dto.ProductDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductDetails} and its DTO {@link ProductDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProductTypeMapper.class })
public interface ProductDetailsMapper extends EntityMapper<ProductDetailsDTO, ProductDetails> {
    @Mapping(target = "productType", source = "productType", qualifiedByName = "id")
    ProductDetailsDTO toDto(ProductDetails s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDetailsDTO toDtoId(ProductDetails productDetails);
}
