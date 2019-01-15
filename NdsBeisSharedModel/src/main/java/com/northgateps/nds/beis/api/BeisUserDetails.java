package com.northgateps.nds.beis.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.northgateps.nds.platform.application.api.depend.FieldDependency;
import com.northgateps.nds.platform.application.api.metadata.EmailFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.RequiredFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.StringLengthFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.UserNameFieldMetadata;
import com.northgateps.nds.platform.application.api.metadata.ViolationFieldMetadata;
import com.northgateps.nds.platform.application.api.validation.AbstractValidatableModel;

/**
 * 
 * Stores the user details
 *
 */
public class BeisUserDetails extends AbstractValidatableModel {

	@EmailFieldMetadata
	@RequiredFieldMetadata
	@StringLengthFieldMetadata(maxLength = 100)
	private String email;

	@EmailFieldMetadata
	@RequiredFieldMetadata
	@StringLengthFieldMetadata(maxLength = 100)
	@ViolationFieldMetadata(invalidMessage = "ValidationField_RegistrationModel_confirmEmail", dependencies = {
			@FieldDependency(path = "~.email", comparator = "notSameAs") })
	private String confirmEmail;

	@UserNameFieldMetadata
	@RequiredFieldMetadata
	@StringLengthFieldMetadata(minLength = 3, maxLength = 100,invalidMessage="Validation_Username_must_not_outside_length_restrictions")
	private String username;

	@JsonIgnore
	@RequiredFieldMetadata
	@StringLengthFieldMetadata(maxLength = 100)
	@ViolationFieldMetadata(invalidMessage = "ValidationField_Username_Password_notsame", dependencies = {
            @FieldDependency(path = "~.username", comparator = "sameAs"),
            @FieldDependency(path = "~.username", comparator = "notEmpty")})
    private transient String password;
	
	@JsonIgnore
	@RequiredFieldMetadata
	@ViolationFieldMetadata(invalidMessage = "ValidationField_RegistrationModel_confirmPassword", dependencies = {
			@FieldDependency(path = "~.password", comparator = "notSameAs"),
            @FieldDependency(path = "~.confirmPassword", comparator = "notEmpty")})
	private transient String confirmPassword;

    @RequiredFieldMetadata(invalidMessage ="Validation_beisRegistrationDetails.userDetails.userType")
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}
	
	

}
