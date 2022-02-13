package com.etho.pm.domain;

import com.etho.pm.domain.enumeration.ParameterType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ParameterLookup.
 */
@Entity
@Table(name = "parameter_lookup")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ParameterLookup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private Long name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ParameterType type;

    @NotNull
    @Column(name = "last_modified", nullable = false)
    private LocalDate lastModified;

    @NotNull
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parameterLookups" }, allowSetters = true)
    private VehicleDetails vehicleDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ParameterLookup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getName() {
        return this.name;
    }

    public ParameterLookup name(Long name) {
        this.setName(name);
        return this;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public ParameterType getType() {
        return this.type;
    }

    public ParameterLookup type(ParameterType type) {
        this.setType(type);
        return this;
    }

    public void setType(ParameterType type) {
        this.type = type;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public ParameterLookup lastModified(LocalDate lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public ParameterLookup lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public VehicleDetails getVehicleDetails() {
        return this.vehicleDetails;
    }

    public void setVehicleDetails(VehicleDetails vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public ParameterLookup vehicleDetails(VehicleDetails vehicleDetails) {
        this.setVehicleDetails(vehicleDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterLookup)) {
            return false;
        }
        return id != null && id.equals(((ParameterLookup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParameterLookup{" +
            "id=" + getId() +
            ", name=" + getName() +
            ", type='" + getType() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
