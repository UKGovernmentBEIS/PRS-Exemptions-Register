package com.northgateps.nds.beis.ui.model;

/**
 * Enumeration detailing registration type.
 * This is used by the Dashboard event handler 
 * as UI state to determine the type of user 
 * logged on and too minimise esb hits.
 */
public enum RegistrationStatusType {
    FOUND_REGISTERED,
    FOUND_PARTIALLY_REGISTERED,
    USE_REGISTERED
}