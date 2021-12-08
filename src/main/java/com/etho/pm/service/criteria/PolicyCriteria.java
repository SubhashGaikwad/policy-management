package com.etho.pm.service.criteria;

import com.etho.pm.domain.enumeration.PolicyStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.etho.pm.domain.Policy} entity. This class is used
 * in {@link com.etho.pm.web.rest.PolicyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /policies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PolicyCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PolicyStatus
     */
    public static class PolicyStatusFilter extends Filter<PolicyStatus> {

        public PolicyStatusFilter() {}

        public PolicyStatusFilter(PolicyStatusFilter filter) {
            super(filter);
        }

        @Override
        public PolicyStatusFilter copy() {
            return new PolicyStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter policyAmount;

    private LongFilter instalmentAmount;

    private LongFilter term;

    private LongFilter instalmentPeriod;

    private LongFilter instalmentDate;

    private PolicyStatusFilter status;

    private InstantFilter dateStart;

    private InstantFilter dateEnd;

    private InstantFilter maturityDate;

    private StringFilter uinNo;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private LongFilter nomineeId;

    private LongFilter usersId;

    private Boolean distinct;

    public PolicyCriteria() {}

    public PolicyCriteria(PolicyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.policyAmount = other.policyAmount == null ? null : other.policyAmount.copy();
        this.instalmentAmount = other.instalmentAmount == null ? null : other.instalmentAmount.copy();
        this.term = other.term == null ? null : other.term.copy();
        this.instalmentPeriod = other.instalmentPeriod == null ? null : other.instalmentPeriod.copy();
        this.instalmentDate = other.instalmentDate == null ? null : other.instalmentDate.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.dateStart = other.dateStart == null ? null : other.dateStart.copy();
        this.dateEnd = other.dateEnd == null ? null : other.dateEnd.copy();
        this.maturityDate = other.maturityDate == null ? null : other.maturityDate.copy();
        this.uinNo = other.uinNo == null ? null : other.uinNo.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.nomineeId = other.nomineeId == null ? null : other.nomineeId.copy();
        this.usersId = other.usersId == null ? null : other.usersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PolicyCriteria copy() {
        return new PolicyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getPolicyAmount() {
        return policyAmount;
    }

    public LongFilter policyAmount() {
        if (policyAmount == null) {
            policyAmount = new LongFilter();
        }
        return policyAmount;
    }

    public void setPolicyAmount(LongFilter policyAmount) {
        this.policyAmount = policyAmount;
    }

    public LongFilter getInstalmentAmount() {
        return instalmentAmount;
    }

    public LongFilter instalmentAmount() {
        if (instalmentAmount == null) {
            instalmentAmount = new LongFilter();
        }
        return instalmentAmount;
    }

    public void setInstalmentAmount(LongFilter instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public LongFilter getTerm() {
        return term;
    }

    public LongFilter term() {
        if (term == null) {
            term = new LongFilter();
        }
        return term;
    }

    public void setTerm(LongFilter term) {
        this.term = term;
    }

    public LongFilter getInstalmentPeriod() {
        return instalmentPeriod;
    }

    public LongFilter instalmentPeriod() {
        if (instalmentPeriod == null) {
            instalmentPeriod = new LongFilter();
        }
        return instalmentPeriod;
    }

    public void setInstalmentPeriod(LongFilter instalmentPeriod) {
        this.instalmentPeriod = instalmentPeriod;
    }

    public LongFilter getInstalmentDate() {
        return instalmentDate;
    }

    public LongFilter instalmentDate() {
        if (instalmentDate == null) {
            instalmentDate = new LongFilter();
        }
        return instalmentDate;
    }

    public void setInstalmentDate(LongFilter instalmentDate) {
        this.instalmentDate = instalmentDate;
    }

    public PolicyStatusFilter getStatus() {
        return status;
    }

    public PolicyStatusFilter status() {
        if (status == null) {
            status = new PolicyStatusFilter();
        }
        return status;
    }

    public void setStatus(PolicyStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getDateStart() {
        return dateStart;
    }

    public InstantFilter dateStart() {
        if (dateStart == null) {
            dateStart = new InstantFilter();
        }
        return dateStart;
    }

    public void setDateStart(InstantFilter dateStart) {
        this.dateStart = dateStart;
    }

    public InstantFilter getDateEnd() {
        return dateEnd;
    }

    public InstantFilter dateEnd() {
        if (dateEnd == null) {
            dateEnd = new InstantFilter();
        }
        return dateEnd;
    }

    public void setDateEnd(InstantFilter dateEnd) {
        this.dateEnd = dateEnd;
    }

    public InstantFilter getMaturityDate() {
        return maturityDate;
    }

    public InstantFilter maturityDate() {
        if (maturityDate == null) {
            maturityDate = new InstantFilter();
        }
        return maturityDate;
    }

    public void setMaturityDate(InstantFilter maturityDate) {
        this.maturityDate = maturityDate;
    }

    public StringFilter getUinNo() {
        return uinNo;
    }

    public StringFilter uinNo() {
        if (uinNo == null) {
            uinNo = new StringFilter();
        }
        return uinNo;
    }

    public void setUinNo(StringFilter uinNo) {
        this.uinNo = uinNo;
    }

    public InstantFilter getLastModified() {
        return lastModified;
    }

    public InstantFilter lastModified() {
        if (lastModified == null) {
            lastModified = new InstantFilter();
        }
        return lastModified;
    }

    public void setLastModified(InstantFilter lastModified) {
        this.lastModified = lastModified;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LongFilter getNomineeId() {
        return nomineeId;
    }

    public LongFilter nomineeId() {
        if (nomineeId == null) {
            nomineeId = new LongFilter();
        }
        return nomineeId;
    }

    public void setNomineeId(LongFilter nomineeId) {
        this.nomineeId = nomineeId;
    }

    public LongFilter getUsersId() {
        return usersId;
    }

    public LongFilter usersId() {
        if (usersId == null) {
            usersId = new LongFilter();
        }
        return usersId;
    }

    public void setUsersId(LongFilter usersId) {
        this.usersId = usersId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PolicyCriteria that = (PolicyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(policyAmount, that.policyAmount) &&
            Objects.equals(instalmentAmount, that.instalmentAmount) &&
            Objects.equals(term, that.term) &&
            Objects.equals(instalmentPeriod, that.instalmentPeriod) &&
            Objects.equals(instalmentDate, that.instalmentDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(dateStart, that.dateStart) &&
            Objects.equals(dateEnd, that.dateEnd) &&
            Objects.equals(maturityDate, that.maturityDate) &&
            Objects.equals(uinNo, that.uinNo) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(nomineeId, that.nomineeId) &&
            Objects.equals(usersId, that.usersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            policyAmount,
            instalmentAmount,
            term,
            instalmentPeriod,
            instalmentDate,
            status,
            dateStart,
            dateEnd,
            maturityDate,
            uinNo,
            lastModified,
            lastModifiedBy,
            nomineeId,
            usersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PolicyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (policyAmount != null ? "policyAmount=" + policyAmount + ", " : "") +
            (instalmentAmount != null ? "instalmentAmount=" + instalmentAmount + ", " : "") +
            (term != null ? "term=" + term + ", " : "") +
            (instalmentPeriod != null ? "instalmentPeriod=" + instalmentPeriod + ", " : "") +
            (instalmentDate != null ? "instalmentDate=" + instalmentDate + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (dateStart != null ? "dateStart=" + dateStart + ", " : "") +
            (dateEnd != null ? "dateEnd=" + dateEnd + ", " : "") +
            (maturityDate != null ? "maturityDate=" + maturityDate + ", " : "") +
            (uinNo != null ? "uinNo=" + uinNo + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (nomineeId != null ? "nomineeId=" + nomineeId + ", " : "") +
            (usersId != null ? "usersId=" + usersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
