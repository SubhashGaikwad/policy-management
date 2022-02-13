package com.etho.pm.service;

import com.etho.pm.domain.*; // for static metamodels
import com.etho.pm.domain.Users;
import com.etho.pm.repository.UsersRepository;
import com.etho.pm.service.criteria.UsersCriteria;
import com.etho.pm.service.dto.UsersDTO;
import com.etho.pm.service.mapper.UsersMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Users} entities in the database.
 * The main input is a {@link UsersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsersDTO} or a {@link Page} of {@link UsersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsersQueryService extends QueryService<Users> {

    private final Logger log = LoggerFactory.getLogger(UsersQueryService.class);

    private final UsersRepository usersRepository;

    private final UsersMapper usersMapper;

    public UsersQueryService(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    /**
     * Return a {@link List} of {@link UsersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsersDTO> findByCriteria(UsersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Users> specification = createSpecification(criteria);
        return usersMapper.toDto(usersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UsersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsersDTO> findByCriteria(UsersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Users> specification = createSpecification(criteria);
        return usersRepository.findAll(specification, page).map(usersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Users> specification = createSpecification(criteria);
        return usersRepository.count(specification);
    }

    /**
     * Function to convert {@link UsersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Users> createSpecification(UsersCriteria criteria) {
        Specification<Users> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Users_.id));
            }
            if (criteria.getGroupCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupCode(), Users_.groupCode));
            }
            if (criteria.getGroupHeadName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupHeadName(), Users_.groupHeadName));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Users_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Users_.lastName));
            }
            if (criteria.getBirthDate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBirthDate(), Users_.birthDate));
            }
            if (criteria.getMarriageDate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMarriageDate(), Users_.marriageDate));
            }
            if (criteria.getUserTypeId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserTypeId(), Users_.userTypeId));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsername(), Users_.username));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Users_.password));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Users_.email));
            }
            if (criteria.getImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImageUrl(), Users_.imageUrl));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Users_.status));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), Users_.activated));
            }
            if (criteria.getLicenceExpiryDate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicenceExpiryDate(), Users_.licenceExpiryDate));
            }
            if (criteria.getMobileNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobileNo(), Users_.mobileNo));
            }
            if (criteria.getAadharCardNuber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAadharCardNuber(), Users_.aadharCardNuber));
            }
            if (criteria.getPancardNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPancardNumber(), Users_.pancardNumber));
            }
            if (criteria.getOneTimePassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOneTimePassword(), Users_.oneTimePassword));
            }
            if (criteria.getOtpExpiryTime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOtpExpiryTime(), Users_.otpExpiryTime));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModified(), Users_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Users_.lastModifiedBy));
            }
            if (criteria.getUsersTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsersTypeId(), root -> root.join(Users_.usersType, JoinType.LEFT).get(UsersType_.id))
                    );
            }
            if (criteria.getPolicyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPolicyId(), root -> root.join(Users_.policies, JoinType.LEFT).get(Policy_.id))
                    );
            }
            if (criteria.getAddressId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAddressId(), root -> root.join(Users_.addresses, JoinType.LEFT).get(Address_.id))
                    );
            }
        }
        return specification;
    }
}
