package com.etho.pm.service.criteria;

import com.etho.pm.domain.enumeration.StatusInd;
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
 * Criteria class for the {@link com.etho.pm.domain.Users} entity. This class is used
 * in {@link com.etho.pm.web.rest.UsersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UsersCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatusInd
     */
    public static class StatusIndFilter extends Filter<StatusInd> {

        public StatusIndFilter() {}

        public StatusIndFilter(StatusIndFilter filter) {
            super(filter);
        }

        @Override
        public StatusIndFilter copy() {
            return new StatusIndFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private InstantFilter birthDate;

    private LongFilter userTypeId;

    private StringFilter username;

    private StringFilter password;

    private StringFilter email;

    private StringFilter imageUrl;

    private StatusIndFilter status;

    private BooleanFilter activated;

    private StringFilter mobileNo;

    private StringFilter oneTimePassword;

    private InstantFilter otpExpiryTime;

    private InstantFilter lastModified;

    private StringFilter lastModifiedBy;

    private LongFilter usersTypeId;

    private LongFilter policyId;

    private LongFilter addressId;

    private Boolean distinct;

    public UsersCriteria() {}

    public UsersCriteria(UsersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.birthDate = other.birthDate == null ? null : other.birthDate.copy();
        this.userTypeId = other.userTypeId == null ? null : other.userTypeId.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.activated = other.activated == null ? null : other.activated.copy();
        this.mobileNo = other.mobileNo == null ? null : other.mobileNo.copy();
        this.oneTimePassword = other.oneTimePassword == null ? null : other.oneTimePassword.copy();
        this.otpExpiryTime = other.otpExpiryTime == null ? null : other.otpExpiryTime.copy();
        this.lastModified = other.lastModified == null ? null : other.lastModified.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.usersTypeId = other.usersTypeId == null ? null : other.usersTypeId.copy();
        this.policyId = other.policyId == null ? null : other.policyId.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UsersCriteria copy() {
        return new UsersCriteria(this);
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

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public InstantFilter getBirthDate() {
        return birthDate;
    }

    public InstantFilter birthDate() {
        if (birthDate == null) {
            birthDate = new InstantFilter();
        }
        return birthDate;
    }

    public void setBirthDate(InstantFilter birthDate) {
        this.birthDate = birthDate;
    }

    public LongFilter getUserTypeId() {
        return userTypeId;
    }

    public LongFilter userTypeId() {
        if (userTypeId == null) {
            userTypeId = new LongFilter();
        }
        return userTypeId;
    }

    public void setUserTypeId(LongFilter userTypeId) {
        this.userTypeId = userTypeId;
    }

    public StringFilter getUsername() {
        return username;
    }

    public StringFilter username() {
        if (username == null) {
            username = new StringFilter();
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getPassword() {
        return password;
    }

    public StringFilter password() {
        if (password == null) {
            password = new StringFilter();
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            imageUrl = new StringFilter();
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StatusIndFilter getStatus() {
        return status;
    }

    public StatusIndFilter status() {
        if (status == null) {
            status = new StatusIndFilter();
        }
        return status;
    }

    public void setStatus(StatusIndFilter status) {
        this.status = status;
    }

    public BooleanFilter getActivated() {
        return activated;
    }

    public BooleanFilter activated() {
        if (activated == null) {
            activated = new BooleanFilter();
        }
        return activated;
    }

    public void setActivated(BooleanFilter activated) {
        this.activated = activated;
    }

    public StringFilter getMobileNo() {
        return mobileNo;
    }

    public StringFilter mobileNo() {
        if (mobileNo == null) {
            mobileNo = new StringFilter();
        }
        return mobileNo;
    }

    public void setMobileNo(StringFilter mobileNo) {
        this.mobileNo = mobileNo;
    }

    public StringFilter getOneTimePassword() {
        return oneTimePassword;
    }

    public StringFilter oneTimePassword() {
        if (oneTimePassword == null) {
            oneTimePassword = new StringFilter();
        }
        return oneTimePassword;
    }

    public void setOneTimePassword(StringFilter oneTimePassword) {
        this.oneTimePassword = oneTimePassword;
    }

    public InstantFilter getOtpExpiryTime() {
        return otpExpiryTime;
    }

    public InstantFilter otpExpiryTime() {
        if (otpExpiryTime == null) {
            otpExpiryTime = new InstantFilter();
        }
        return otpExpiryTime;
    }

    public void setOtpExpiryTime(InstantFilter otpExpiryTime) {
        this.otpExpiryTime = otpExpiryTime;
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

    public LongFilter getUsersTypeId() {
        return usersTypeId;
    }

    public LongFilter usersTypeId() {
        if (usersTypeId == null) {
            usersTypeId = new LongFilter();
        }
        return usersTypeId;
    }

    public void setUsersTypeId(LongFilter usersTypeId) {
        this.usersTypeId = usersTypeId;
    }

    public LongFilter getPolicyId() {
        return policyId;
    }

    public LongFilter policyId() {
        if (policyId == null) {
            policyId = new LongFilter();
        }
        return policyId;
    }

    public void setPolicyId(LongFilter policyId) {
        this.policyId = policyId;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public LongFilter addressId() {
        if (addressId == null) {
            addressId = new LongFilter();
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
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
        final UsersCriteria that = (UsersCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(userTypeId, that.userTypeId) &&
            Objects.equals(username, that.username) &&
            Objects.equals(password, that.password) &&
            Objects.equals(email, that.email) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(status, that.status) &&
            Objects.equals(activated, that.activated) &&
            Objects.equals(mobileNo, that.mobileNo) &&
            Objects.equals(oneTimePassword, that.oneTimePassword) &&
            Objects.equals(otpExpiryTime, that.otpExpiryTime) &&
            Objects.equals(lastModified, that.lastModified) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(usersTypeId, that.usersTypeId) &&
            Objects.equals(policyId, that.policyId) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            firstName,
            lastName,
            birthDate,
            userTypeId,
            username,
            password,
            email,
            imageUrl,
            status,
            activated,
            mobileNo,
            oneTimePassword,
            otpExpiryTime,
            lastModified,
            lastModifiedBy,
            usersTypeId,
            policyId,
            addressId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
            (userTypeId != null ? "userTypeId=" + userTypeId + ", " : "") +
            (username != null ? "username=" + username + ", " : "") +
            (password != null ? "password=" + password + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (activated != null ? "activated=" + activated + ", " : "") +
            (mobileNo != null ? "mobileNo=" + mobileNo + ", " : "") +
            (oneTimePassword != null ? "oneTimePassword=" + oneTimePassword + ", " : "") +
            (otpExpiryTime != null ? "otpExpiryTime=" + otpExpiryTime + ", " : "") +
            (lastModified != null ? "lastModified=" + lastModified + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (usersTypeId != null ? "usersTypeId=" + usersTypeId + ", " : "") +
            (policyId != null ? "policyId=" + policyId + ", " : "") +
            (addressId != null ? "addressId=" + addressId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
