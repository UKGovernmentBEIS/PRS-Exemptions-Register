package com.northgateps.nds.beis.api;

import com.northgateps.nds.platform.api.model.NdsEsbModel;

/**
 * Interface used for validation for Beis RegistrationDetails
 *
 */
public interface BeisRegistrationEsbModel extends NdsEsbModel {

    public BeisRegistrationDetails getBeisRegistrationDetails();
}
