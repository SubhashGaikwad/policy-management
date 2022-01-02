package com.etho.pm.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.etho.pm.domain.Rider} entity.
 */
public class RiderDTO implements Serializable {

    private Long id;

    private String name;

    private Instant commDate;

    private String sum;

    private String term;

    private String ppt;

    private Long premium;

    @NotNull
    private Instant lastModified;

    @NotNull
    private String lastModifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCommDate() {
        return commDate;
    }

    public void setCommDate(Instant commDate) {
        this.commDate = commDate;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getPpt() {
        return ppt;
    }

    public void setPpt(String ppt) {
        this.ppt = ppt;
    }

    public Long getPremium() {
        return premium;
    }

    public void setPremium(Long premium) {
        this.premium = premium;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RiderDTO)) {
            return false;
        }

        RiderDTO riderDTO = (RiderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, riderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RiderDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", commDate='" + getCommDate() + "'" +
            ", sum='" + getSum() + "'" +
            ", term='" + getTerm() + "'" +
            ", ppt='" + getPpt() + "'" +
            ", premium=" + getPremium() +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
