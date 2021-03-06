package com.etho.pm.service;

import com.etho.pm.domain.Policy;
import com.etho.pm.repository.PolicyRepository;
import com.etho.pm.service.dto.PolicyDTO;
import com.etho.pm.service.mapper.PolicyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Policy}.
 */
@Service
@Transactional
public class PolicyService {

    private final Logger log = LoggerFactory.getLogger(PolicyService.class);

    private final PolicyRepository policyRepository;

    private final PolicyMapper policyMapper;

    public PolicyService(PolicyRepository policyRepository, PolicyMapper policyMapper) {
        this.policyRepository = policyRepository;
        this.policyMapper = policyMapper;
    }

    /**
     * Save a policy.
     *
     * @param policyDTO the entity to save.
     * @return the persisted entity.
     */
    public PolicyDTO save(PolicyDTO policyDTO) {
        log.debug("Request to save Policy : {}", policyDTO);
        Policy policy = policyMapper.toEntity(policyDTO);
        policy = policyRepository.save(policy);
        return policyMapper.toDto(policy);
    }

    /**
     * Partially update a policy.
     *
     * @param policyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PolicyDTO> partialUpdate(PolicyDTO policyDTO) {
        log.debug("Request to partially update Policy : {}", policyDTO);

        return policyRepository
            .findById(policyDTO.getId())
            .map(existingPolicy -> {
                policyMapper.partialUpdate(existingPolicy, policyDTO);

                return existingPolicy;
            })
            .map(policyRepository::save)
            .map(policyMapper::toDto);
    }

    /**
     * Get all the policies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PolicyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Policies");
        return policyRepository.findAll(pageable).map(policyMapper::toDto);
    }

    /**
     * Get one policy by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PolicyDTO> findOne(Long id) {
        log.debug("Request to get Policy : {}", id);
        return policyRepository.findById(id).map(policyMapper::toDto);
    }

    /**
     * Delete the policy by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Policy : {}", id);
        policyRepository.deleteById(id);
    }
}
