package com.etho.pm.service.dto;

import com.etho.pm.domain.enumeration.PolicyStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.etho.pm.domain.Policy} entity.
 */
public class PolicyDTO implements Serializable {

    private Long id;

    private Long policyAmount;

    private Long instalmentAmount;

    private Long term;

    private Long instalmentPeriod;

    private Long instalmentDate;

    private PolicyStatus status;

    @NotNull
    private Instant dateStart;

    @NotNull
    private Instant dateEnd;

    @NotNull
    private Instant maturityDate;

    private String uinNo;

    @NotNull
    private Instant lastModified;

    @NotNull
    private String lastModifiedBy;

    private UsersDTO users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPolicyAmount() {
        return policyAmount;
    }

    public void setPolicyAmount(Long policyAmount) {
        this.policyAmount = policyAmount;
    }

    public Long getInstalmentAmount() {
        return instalmentAmount;
    }

    public void setInstalmentAmount(Long instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Long getInstalmentPeriod() {
        return instalmentPeriod;
    }

    public void setInstalmentPeriod(Long instalmentPeriod) {
        this.instalmentPeriod = instalmentPeriod;
    }

    public Long getInstalmentDate() {
        return instalmentDate;
    }

    public void setInstalmentDate(Long instalmentDate) {
        this.instalmentDate = instalmentDate;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public Instant getDateStart() {
        return dateStart;
    }

    public void setDateStart(Instant dateStart) {
        this.dateStart = dateStart;
    }

    public Instant getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Instant dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Instant getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Instant maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getUinNo() {
        return uinNo;
    }

    public void setUinNo(String uinNo) {
        this.uinNo = uinNo;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public UsersDTO getUsers() {
        return users;
    }

    public void setUsers(UsersDTO users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PolicyDTO)) {
            return false;
        }

        PolicyDTO policyDTO = (PolicyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, policyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PolicyDTO{" +
            "id=" + getId() +
            ", policyAmount=" + getPolicyAmount() +
            ", instalmentAmount=" + getInstalmentAmount() +
            ", term=" + getTerm() +
            ", instalmentPeriod=" + getInstalmentPeriod() +
            ", instalmentDate=" + getInstalmentDate() +
            ", status='" + getStatus() + "'" +
            ", dateStart='" + getDateStart() + "'" +
            ", dateEnd='" + getDateEnd() + "'" +
            ", maturityDate='" + getMaturityDate() + "'" +
            ", uinNo='" + getUinNo() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", users=" + getUsers() +
            "}";
    }
}
