package com.etho.pm.service;

import com.etho.pm.domain.*; // for static metamodels
import com.etho.pm.domain.Policy;
import com.etho.pm.repository.PolicyRepository;
import com.etho.pm.service.criteria.PolicyCriteria;
import com.etho.pm.service.dto.PolicyDTO;
import com.etho.pm.service.mapper.PolicyMapper;
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
 * Service for executing complex queries for {@link Policy} entities in the database.
 * The main input is a {@link PolicyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PolicyDTO} or a {@link Page} of {@link PolicyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PolicyQueryService extends QueryService<Policy> {

    private final Logger log = LoggerFactory.getLogger(PolicyQueryService.class);

    private final PolicyRepository policyRepository;

    private final PolicyMapper policyMapper;

    public PolicyQueryService(PolicyRepository policyRepository, PolicyMapper policyMapper) {
        this.policyRepository = policyRepository;
        this.policyMapper = policyMapper;
    }

    /**
     * Return a {@link List} of {@link PolicyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PolicyDTO> findByCriteria(PolicyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Policy> specification = createSpecification(criteria);
        return policyMapper.toDto(policyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PolicyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PolicyDTO> findByCriteria(PolicyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Policy> specification = createSpecification(criteria);
        return policyRepository.findAll(specification, page).map(policyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PolicyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Policy> specification = createSpecification(criteria);
        return policyRepository.count(specification);
    }

    /**
     * Function to convert {@link PolicyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Policy> createSpecification(PolicyCriteria criteria) {
        Specification<Policy> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Policy_.id));
            }
            if (criteria.getPolicyAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPolicyAmount(), Policy_.policyAmount));
            }
            if (criteria.getInstalmentAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInstalmentAmount(), Policy_.instalmentAmount));
            }
            if (criteria.getTerm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTerm(), Policy_.term));
            }
            if (criteria.getInstalmentPeriod() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInstalmentPeriod(), Policy_.instalmentPeriod));
            }
            if (criteria.getInstalmentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInstalmentDate(), Policy_.instalmentDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Policy_.status));
            }
            if (criteria.getDateStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateStart(), Policy_.dateStart));
            }
            if (criteria.getDateEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEnd(), Policy_.dateEnd));
            }
            if (criteria.getMaturityDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMaturityDate(), Policy_.maturityDate));
            }
            if (criteria.getUinNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUinNo(), Policy_.uinNo));
            }
            if (criteria.getLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModified(), Policy_.lastModified));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Policy_.lastModifiedBy));
            }
            if (criteria.getNomineeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNomineeId(), root -> root.join(Policy_.nominees, JoinType.LEFT).get(Nominee_.id))
                    );
            }
            if (criteria.getUsersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsersId(), root -> root.join(Policy_.users, JoinType.LEFT).get(Users_.id))
                    );
            }
        }
        return specification;
    }
}
