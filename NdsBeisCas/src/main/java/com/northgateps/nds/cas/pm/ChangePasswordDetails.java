package com.northgateps.nds.cas.pm;

import java.io.Serializable;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Change password details of an existing user.
 * Based on CAS' PasswordChangeRequest.
 */
public class ChangePasswordDetails implements Serializable {
    
    private static final long serialVersionUID = 8885460875620586503L;
    
    @JsonIgnore
    @Size(min = 1, max=100, message = "required.oldPassword")
    private transient String oldPassword;

    @JsonIgnore
    @Size(min = 1, max=100,  message = "required.newPassword")
    private transient String newPassword;
    
    @JsonIgnore
    @Size(min = 1, max=100, message = "required.confirmPassword")
    private transient String confirmPassword;

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        final ChangePasswordDetails rhs = (ChangePasswordDetails) obj;
        return new EqualsBuilder()           
                .append(this.oldPassword, rhs.oldPassword)
                .append(this.newPassword, rhs.newPassword)
                .append(this.confirmPassword, rhs.confirmPassword)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(oldPassword)
                .append(newPassword)
                .append(confirmPassword)
                .toHashCode();
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}