package com.etho.pm.domain;

import com.etho.pm.domain.enumeration.PolicyStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Policy.
 */
@Entity
@Table(name = "policy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Policy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "policy_amount")
    private Long policyAmount;

    @Column(name = "instalment_amount")
    private Long instalmentAmount;

    @Column(name = "term")
    private Long term;

    @Column(name = "instalment_period")
    private Long instalmentPeriod;

    @Column(name = "instalment_date")
    private Long instalmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PolicyStatus status;

    @NotNull
    @Column(name = "date_start", nullable = false)
    private Instant dateStart;

    @NotNull
    @Column(name = "date_end", nullable = false)
    private Instant dateEnd;

    @NotNull
    @Column(name = "maturity_date", nullable = false)
    private Instant maturityDate;

    @Column(name = "uin_no")
    private String uinNo;

    @NotNull
    @Column(name = "last_modified", nullable = false)
    private Instant lastModified;

    @NotNull
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @OneToMany(mappedBy = "policy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "policy" }, allowSetters = true)
    private Set<Nominee> nominees = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "usersType", "policies", "addresses" }, allowSetters = true)
    private Users users;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Policy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPolicyAmount() {
        return this.policyAmount;
    }

    public Policy policyAmount(Long policyAmount) {
        this.setPolicyAmount(policyAmount);
        return this;
    }

    public void setPolicyAmount(Long policyAmount) {
        this.policyAmount = policyAmount;
    }

    public Long getInstalmentAmount() {
        return this.instalmentAmount;
    }

    public Policy instalmentAmount(Long instalmentAmount) {
        this.setInstalmentAmount(instalmentAmount);
        return this;
    }

    public void setInstalmentAmount(Long instalmentAmount) {
        this.instalmentAmount = instalmentAmount;
    }

    public Long getTerm() {
        return this.term;
    }

    public Policy term(Long term) {
        this.setTerm(term);
        return this;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public Long getInstalmentPeriod() {
        return this.instalmentPeriod;
    }

    public Policy instalmentPeriod(Long instalmentPeriod) {
        this.setInstalmentPeriod(instalmentPeriod);
        return this;
    }

    public void setInstalmentPeriod(Long instalmentPeriod) {
        this.instalmentPeriod = instalmentPeriod;
    }

    public Long getInstalmentDate() {
        return this.instalmentDate;
    }

    public Policy instalmentDate(Long instalmentDate) {
        this.setInstalmentDate(instalmentDate);
        return this;
    }

    public void setInstalmentDate(Long instalmentDate) {
        this.instalmentDate = instalmentDate;
    }

    public PolicyStatus getStatus() {
        return this.status;
    }

    public Policy status(PolicyStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public Instant getDateStart() {
        return this.dateStart;
    }

    public Policy dateStart(Instant dateStart) {
        this.setDateStart(dateStart);
        return this;
    }

    public void setDateStart(Instant dateStart) {
        this.dateStart = dateStart;
    }

    public Instant getDateEnd() {
        return this.dateEnd;
    }

    public Policy dateEnd(Instant dateEnd) {
        this.setDateEnd(dateEnd);
        return this;
    }

    public void setDateEnd(Instant dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Instant getMaturityDate() {
        return this.maturityDate;
    }

    public Policy maturityDate(Instant maturityDate) {
        this.setMaturityDate(maturityDate);
        return this;
    }

    public void setMaturityDate(Instant maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getUinNo() {
        return this.uinNo;
    }

    public Policy uinNo(String uinNo) {
        this.setUinNo(uinNo);
        return this;
    }

    public void setUinNo(String uinNo) {
        this.uinNo = uinNo;
    }

    public Instant getLastModified() {
        return this.lastModified;
    }

    public Policy lastModified(Instant lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Policy lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Set<Nominee> getNominees() {
        return this.nominees;
    }

    public void setNominees(Set<Nominee> nominees) {
        if (this.nominees != null) {
            this.nominees.forEach(i -> i.setPolicy(null));
        }
        if (nominees != null) {
            nominees.forEach(i -> i.setPolicy(this));
        }
        this.nominees = nominees;
    }

    public Policy nominees(Set<Nominee> nominees) {
        this.setNominees(nominees);
        return this;
    }

    public Policy addNominee(Nominee nominee) {
        this.nominees.add(nominee);
        nominee.setPolicy(this);
        return this;
    }

    public Policy removeNominee(Nominee nominee) {
        this.nominees.remove(nominee);
        nominee.setPolicy(null);
        return this;
    }

    public Users getUsers() {
        return this.users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Policy users(Users users) {
        this.setUsers(users);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Policy)) {
            return false;
        }
        return id != null && id.equals(((Policy) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Policy{" +
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
            "}";
    }
}
