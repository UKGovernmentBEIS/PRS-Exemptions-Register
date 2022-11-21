# PRS-Exemptions-Register

## Introduction

The application provides a system for Landlords and agents to register a property for an exemption under the 
_The Energy Efficiency (Private Rented Property)(England and Wales) Regulations 2015_.

[See the guidance] (https://www.gov.uk/government/publications/the-private-rented-property-minimum-standard-landlord-guidance-documents)

The exemptions are recorded in a central database.

There are also public enquiries to enable a member of the public to find if a property has an exemption or if a penalty has been issued for a property where a property does not meet the standard and a valid exemption has not been claimed

## BHT Rough dev steps to get running
0) Connect to the VPN to get access to the dev servers
1) httpd to proxy requests
2) CAS started by gradle/spring boot
3) In beis-<ui|esb>/pom.xml, comment out the <packagingExcludes> line and mark for dev only do not commit etc.  
This puts the properties and certs etc into the war file (not wanted on releases but useful for dev).
4) Build at nds/beis with mvn package (add -DskipTests=true if need to skip them).  This makes the war files in NdsBeis<Ui|Esb>/target/.
5) Start tomcat pointing at the 2 war files
6) DashboardDetailsRouteTest update back office?
7) Build automatically is tricky... junit changes need building but will STS do it right?  Can't run it from there for sure
8) Update dates in DashboardDetailsRouteTest if failing (seriously you're still doing upgrades 5 years later?) -
	add 5 years to the non-expired dates in #checkValues() and #createSingleExemptionNdsResponse() and #createGetPrsAccountExemptionsResponse()