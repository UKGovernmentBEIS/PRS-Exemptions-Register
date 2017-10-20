#REQUIRES -Version 3.0

<#
.SYNOPSIS
	Generates a keystore to encrypt the view state for the NDS project.
.DESCRIPTION
	Generates a keystore to encrypt the UI view state for the NDS project. The
	following is generated
	
	app-keystore.jceks		the keystore used to encrypt the UI view state
	
.PARAMETER  app
	The name of the application to generate the certificates for, defaults to NdsRefApp
	if none supplied. Script then assumes the the UI is called appUi, and will append this
	to the application name

.PARAMETER  password
	The password to use for the keystore. Default is password
	
.PARAMETER keyLength
	The key length to use. Default is 256.
	
.PARAMETER alias
	The generated key's alias. Default is encryption.1

.NOTES
    File Name      : generate-uistate-keystore
    Author         : Richard Tearle
    Prerequisite   : PowerShell V3 over Windows 7 or later
    Copyright 2015 - Northgate Public Services

.COMPONENT
	Requires Java - assumes it's located in %JAVA_HOME%

.INPUTS
	None.

.OUTPUTS
	None.
	
.EXAMPLE
	C:\PS> generate-uistate-keystore -app "NdsBbis" -password "password"

.EXAMPLE
	C:\PS> generate-certificates -app "NdsBbis" -keyLength 2048 -password "wibble"

.EXAMPLE
	C:\PS> generate-certificates -app "NdsBbis" -alias "MyPassword" -password "wibble"

.EXAMPLE
	C:\PS> generate-certificates -app "NdsBbis" -keyLength 2048 -password "wibble" -alias "MyPassword"

#>

## Parameters to this script
param([string]$app="NdsRefApp", [string]$password="password", [Int32]$keyLength=256, [string]$alias="encryption.1")

## Path to OPENSSL, it's configuration file and the exe
$jre_dir=(Get-Item -Path $env:JRE_HOME"\bin\" -Verbose).FullName
$keytool_exe=$jre_dir + "keytool.exe"

## Root working folder and other folders
$working_folder=(Get-Item -Path ".\" -Verbose).FullName
$root_scratch="$($working_folder)\scratch"
$project_folder="$($root_scratch)\$($app)"

## Project folders
$ui_folder=(Get-Item -Path "..\..\..\$($app)Ui" -Verbose).FullName

if ((Test-Path "$($ui_folder)\src\main\resources\config\security") -eq 0)
{
	"Creating UI config security folder $($ui_folder)\src\main\resources\config\security"
	New-Item -ItemType Directory -Path "$($ui_folder)\src\main\resources\config\security" | Out-null
}

$ui_resources_folder=(Get-Item -Path "$($ui_folder)\src\main\resources" -Verbose).FullName
$ui_config_folder=(Get-Item -Path "$($ui_resources_folder)\config" -Verbose).FullName
$ui_security_folder="$($ui_config_folder)\security"

## Make scratch folder if not already present
if ((Test-Path $root_scratch) -eq 0)
{
	"Creating scratch folder $root_scratch"
	New-Item -ItemType Directory -Path $root_scratch | Out-null
}

## Make project folder under the scratch folder, or clean if existing folder
if ((Test-Path $project_folder) -eq 0)
{
	"Creating project folder $project_folder"
	New-Item -ItemType Directory -Path $project_folder | Out-null
}
else
{
	"Cleaning project folder $project_folder"
	Remove-Item "$($project_folder)\*" -recurse | Out-null
}

if ((Test-Path $ui_security_folder) -eq 0)
{
	"Creating UI certificate folder $ui_security_folder"
	New-Item -ItemType Directory -Path $ui_security_folder | Out-null
}
else
{
	"Cleaning UI certificate folder $ui_security_folder"
	Remove-Item "$($ui_security_folder)\*" -recurse | Out-null
}

## Generate KeyStore
&$keytool_exe -genseckey -alias $alias -keyalg AES -keysize $keyLength -storetype jceks -storepass $password -keypass $password -keystore "$($project_folder)\app-keystore.jceks"

## Copy to Projects
"Copy certificates to project folders"
Copy-Item "$($project_folder)\app-keystore.jceks" "$($ui_security_folder)\app-keystore.jceks"

## Post Generation Notes
"Manual Steps:"
""
"To complete the generation and configuration of the KeyStore, perform the following manual steps"
""
"1. Modify $($ui_config_folder)\*Ui.dev.properties with the following:"
""
"  app.keyStore.file=config/security/app-keystore.jceks"
"  app.keyStore.password=$($password)"
"  app.keyStore.type=JCEKS"
""
"2. Modify the servlet configuration for your application to include the following:"
""
"  <property name='securityKeySourceFactory'>"
"    <bean class='com.northgateps.nds.platform.ui.security.CyclingSecurityKeySourceFactory'>"
"      <property name='sourceList'>"
"        <list>"
"          <bean class='com.northgateps.nds.platform.ui.security.KeystoreSecurityKeySource'>"
"            <property name='name' value='$($alias)' />"
"          </bean>"
"        </list>"
"      </property>"
"    </bean>"
"  </property>"
""
"KeyStore generation complete."
""
