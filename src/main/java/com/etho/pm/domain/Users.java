package com.etho.pm.domain;

import com.etho.pm.domain.enumeration.StatusInd;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Users.
 */
@Entity
@Table(name = "users")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "group_head_name")
    private String groupHeadName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull
    @Column(name = "marriage_date", nullable = false)
    private LocalDate marriageDate;

    @Column(name = "user_type_id")
    private Long userTypeId;

    @NotNull
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusInd status;

    @NotNull
    @Column(name = "activated", nullable = false)
    private Boolean activated;

    @Column(name = "licence_expiry_date")
    private LocalDate licenceExpiryDate;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "aadhar_card_nuber")
    private String aadharCardNuber;

    @Column(name = "pancard_number")
    private String pancardNumber;

    @Column(name = "one_time_password")
    private String oneTimePassword;

    @Column(name = "otp_expiry_time")
    private LocalDate otpExpiryTime;

    @NotNull
    @Column(name = "last_modified", nullable = false)
    private LocalDate lastModified;

    @NotNull
    @Column(name = "last_modified_by", nullable = false)
    private String lastModifiedBy;

    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private UsersType usersType;

    @OneToMany(mappedBy = "users")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "agency", "company", "product", "premiunDetails", "vehicleClass", "bankDetails", "nominees", "members", "users" },
        allowSetters = true
    )
    private Set<Policy> policies = new HashSet<>();

    @OneToMany(mappedBy = "users")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "users", "company" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Users id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupCode() {
        return this.groupCode;
    }

    public Users groupCode(String groupCode) {
        this.setGroupCode(groupCode);
        return this;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupHeadName() {
        return this.groupHeadName;
    }

    public Users groupHeadName(String groupHeadName) {
        this.setGroupHeadName(groupHeadName);
        return this;
    }

    public void setGroupHeadName(String groupHeadName) {
        this.groupHeadName = groupHeadName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Users firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Users lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Users birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getMarriageDate() {
        return this.marriageDate;
    }

    public Users marriageDate(LocalDate marriageDate) {
        this.setMarriageDate(marriageDate);
        return this;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        this.marriageDate = marriageDate;
    }

    public Long getUserTypeId() {
        return this.userTypeId;
    }

    public Users userTypeId(Long userTypeId) {
        this.setUserTypeId(userTypeId);
        return this;
    }

    public void setUserTypeId(Long userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getUsername() {
        return this.username;
    }

    public Users username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public Users password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public Users email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Users imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StatusInd getStatus() {
        return this.status;
    }

    public Users status(StatusInd status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusInd status) {
        this.status = status;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public Users activated(Boolean activated) {
        this.setActivated(activated);
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public LocalDate getLicenceExpiryDate() {
        return this.licenceExpiryDate;
    }

    public Users licenceExpiryDate(LocalDate licenceExpiryDate) {
        this.setLicenceExpiryDate(licenceExpiryDate);
        return this;
    }

    public void setLicenceExpiryDate(LocalDate licenceExpiryDate) {
        this.licenceExpiryDate = licenceExpiryDate;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public Users mobileNo(String mobileNo) {
        this.setMobileNo(mobileNo);
        return this;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAadharCardNuber() {
        return this.aadharCardNuber;
    }

    public Users aadharCardNuber(String aadharCardNuber) {
        this.setAadharCardNuber(aadharCardNuber);
        return this;
    }

    public void setAadharCardNuber(String aadharCardNuber) {
        this.aadharCardNuber = aadharCardNuber;
    }

    public String getPancardNumber() {
        return this.pancardNumber;
    }

    public Users pancardNumber(String pancardNumber) {
        this.setPancardNumber(pancardNumber);
        return this;
    }

    public void setPancardNumber(String pancardNumber) {
        this.pancardNumber = pancardNumber;
    }

    public String getOneTimePassword() {
        return this.oneTimePassword;
    }

    public Users oneTimePassword(String oneTimePassword) {
        this.setOneTimePassword(oneTimePassword);
        return this;
    }

    public void setOneTimePassword(String oneTimePassword) {
        this.oneTimePassword = oneTimePassword;
    }

    public LocalDate getOtpExpiryTime() {
        return this.otpExpiryTime;
    }

    public Users otpExpiryTime(LocalDate otpExpiryTime) {
        this.setOtpExpiryTime(otpExpiryTime);
        return this;
    }

    public void setOtpExpiryTime(LocalDate otpExpiryTime) {
        this.otpExpiryTime = otpExpiryTime;
    }

    public LocalDate getLastModified() {
        return this.lastModified;
    }

    public Users lastModified(LocalDate lastModified) {
        this.setLastModified(lastModified);
        return this;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Users lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public UsersType getUsersType() {
        return this.usersType;
    }

    public void setUsersType(UsersType usersType) {
        this.usersType = usersType;
    }

    public Users usersType(UsersType usersType) {
        this.setUsersType(usersType);
        return this;
    }

    public Set<Policy> getPolicies() {
        return this.policies;
    }

    public void setPolicies(Set<Policy> policies) {
        if (this.policies != null) {
            this.policies.forEach(i -> i.setUsers(null));
        }
        if (policies != null) {
            policies.forEach(i -> i.setUsers(this));
        }
        this.policies = policies;
    }

    public Users policies(Set<Policy> policies) {
        this.setPolicies(policies);
        return this;
    }

    public Users addPolicy(Policy policy) {
        this.policies.add(policy);
        policy.setUsers(this);
        return this;
    }

    public Users removePolicy(Policy policy) {
        this.policies.remove(policy);
        policy.setUsers(null);
        return this;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setUsers(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setUsers(this));
        }
        this.addresses = addresses;
    }

    public Users addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Users addAddress(Address address) {
        this.addresses.add(address);
        address.setUsers(this);
        return this;
    }

    public Users removeAddress(Address address) {
        this.addresses.remove(address);
        address.setUsers(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Users)) {
            return false;
        }
        return id != null && id.equals(((Users) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Users{" +
            "id=" + getId() +
            ", groupCode='" + getGroupCode() + "'" +
            ", groupHeadName='" + getGroupHeadName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", marriageDate='" + getMarriageDate() + "'" +
            ", userTypeId=" + getUserTypeId() +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", email='" + getEmail() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", status='" + getStatus() + "'" +
            ", activated='" + getActivated() + "'" +
            ", licenceExpiryDate='" + getLicenceExpiryDate() + "'" +
            ", mobileNo='" + getMobileNo() + "'" +
            ", aadharCardNuber='" + getAadharCardNuber() + "'" +
            ", pancardNumber='" + getPancardNumber() + "'" +
            ", oneTimePassword='" + getOneTimePassword() + "'" +
            ", otpExpiryTime='" + getOtpExpiryTime() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
