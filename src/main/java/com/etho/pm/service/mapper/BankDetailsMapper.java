package com.etho.pm.service.mapper;

import com.etho.pm.domain.BankDetails;
import com.etho.pm.service.dto.BankDetailsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankDetails} and its DTO {@link BankDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BankDetailsMapper extends EntityMapper<BankDetailsDTO, BankDetails> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankDetailsDTO toDtoId(BankDetails bankDetails);
}
