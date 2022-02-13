package com.etho.pm.service.mapper;

import com.etho.pm.domain.Member;
import com.etho.pm.service.dto.MemberDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(componentModel = "spring", uses = { PolicyMapper.class })
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {
    @Mapping(target = "policy", source = "policy", qualifiedByName = "id")
    MemberDTO toDto(Member s);
}
