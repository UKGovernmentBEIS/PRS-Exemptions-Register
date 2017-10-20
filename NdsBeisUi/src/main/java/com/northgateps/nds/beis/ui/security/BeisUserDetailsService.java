package com.northgateps.nds.beis.ui.security;

import com.northgateps.nds.platform.loggingsystem.aspect.DoNotWeaveLoggingSystem;
import com.northgateps.nds.platform.ui.security.cas.CasUserDetailsService;

/**
 * User details service that will assign roles.
 * 
 * If we wanted to set specific Spring Security roles/authorities, this is the place to set them. 
 */
@DoNotWeaveLoggingSystem
public class BeisUserDetailsService extends CasUserDetailsService {

}
