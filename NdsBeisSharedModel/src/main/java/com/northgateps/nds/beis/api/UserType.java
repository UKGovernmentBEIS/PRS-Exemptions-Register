package com.northgateps.nds.beis.api;

/* 
 * NB this is awkward because whilst we might think this is great - the back office returns "Landlord" and "Agent"
 * not LANDLORD and AGENT, so be careful when using these and always compare upper case when comparing strings.
 */
public enum UserType {

	LANDLORD,AGENT
}
