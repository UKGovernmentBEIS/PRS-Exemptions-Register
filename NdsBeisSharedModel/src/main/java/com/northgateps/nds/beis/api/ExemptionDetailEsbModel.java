package com.northgateps.nds.beis.api;

import com.northgateps.nds.platform.api.model.NdsEsbModel;

/**
 * Interface for accessing the ExemptionDetails from a model.
 */
public interface ExemptionDetailEsbModel extends NdsEsbModel{
    ExemptionDetail getExemptionDetail();
}
