package com.northgateps.nds.beis.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.northgateps.nds.platform.application.api.metadata.EmailFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.UserNameFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/**
 * 
 * This is a class to represent details required or that can change on an update of user details.
 * This needs to be separate from BeisUserDetails because passwords are not required and confirmation email not required.
 *
 */

public class BeisUpdateUserDetails extends AbstractValidatableModel {

    @EmailFieldMetadata
    @RequiredFieldMetadata
    @StringLengthFieldMetadata(maxLength = 100)
    private String email;

    @UserNameFieldMetadata
    @RequiredFieldMetadata
    @StringLengthFieldMetadata(maxLength = 100)
    private String username;
    
    @JsonIgnore
    private transient String password;

    private Boolean updatingEmail = Boolean.FALSE;

    private UserType userType; 
        
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getUpdatingEmail() {
        return updatingEmail;
    }

    public void setUpdatingEmail(Boolean updatingEmail) {
        this.updatingEmail = updatingEmail;
    }
}
