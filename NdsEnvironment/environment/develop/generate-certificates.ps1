#REQUIRES -Version 3.0

<#
.SYNOPSIS
	Generates a set of self-signed certificates for the NDS project.
.DESCRIPTION
	Generates a set of self-signed certificates for the NDS project, allowing
	developers to easily call both the UI and ESB tiers. The following
	are generated:
	
		ca-key.pem                       The CA key
		ca-cert.pem                      The CA certificate
		
		server-key.pem			 The server key
		server.csr			 The server certificate request
		server-cert.pem			 The server certificate
		
		client-key.pem			 The client key
		client.csr			 The client certificate request
		client-cert.pem			 The client certificate
		
		cas-client-key.pem		 The client key for CAS
		cas-client.csr			 The client certificate request for CAS
		cas-client-cert.pem		 The client certificate for CAS

		bad-client-key.pem		 The client key for an invalid client certificate
		bad-client.csr			 The client certificate request for invalid cert.
		bad-client-cert.pem		 The "invalid" client certificate
		
	The bad-client-cert.pem is a valid certificate (i.e. it is from the same CA as
	the good client certificate), but it contains the role: wibble - which is assumed
	to be invalid for the application
	
.PARAMETER app
	The name of the application to generate the certificates for, defaults to NdsRefApp
	if none supplied. Script then assumes the ESB is called <app>Esb and the UI is called
	<app>Ui.

.PARAMETER password
	The password to use for the keys and PKCS12 keystores. Default is password.
	Btw quotes are optional for this value.  It works with/without them as expected.
	
.PARAMETER keyLength
	The key length to use. Default is 4096.
	
.PARAMETER validFor
	The number of days the certificates are valid for. Default is 3650

.PARAMETER roles
	The roles to add to the good client certificate. Default is for no roles
	Quote and comma (", ") separate if there are multiple roles.
	Roles have to be prefixed with the rolePrefix ie "ROLE_".

.PARAMETER casRoles	
	The roles to add to the good client certificate for CAS. Default is for no roles
	Quote and comma (", ") separate if there are multiple roles.
	Roles have to be prefixed with the rolePrefix ie "ROLE_".
	
.PARAMETER clientName
	The value to use for the common name for the client certificate. Default is client. 
	Bad client certificate will be prefixed with bad (i.e. badclient). 

.PARAMETER casClientName
	The value to use for the common name for the client certificate. Default is client,
	with a Cas suffic

.NOTES
    File Name      : generate-certificates.ps1
    Author         : Richard Tearle
    Prerequisite   : PowerShell V3 over Windows 7 or later
    Copyright 2013 - Northgate Public Services

.COMPONENT
	Requires OpenSSL - assumes it's located in the standard NDS place, which is
	..\..\tools\windows\openssl\ relative to this script.

.INPUTS
	None.

.OUTPUTS
	None.
	
.EXAMPLE
	C:\PS> generate-certificates -app "NdsBbis" -clientName NdsBbisUi -roles "ROLE_BBIS_UI, SomeOtherRole"

.EXAMPLE
	C:\PS> generate-certificates -app "NdsBbis" -password "wibble"

.EXAMPLE
	C:\PS> generate-certificates -app "NdsBbis" -keyLength=2048

.EXAMPLE
	C:\PS> generate-certificates -app "NdsBbis" -password "wibble" -keyLength=2048

.EXAMPLE
	C:\PS> generate-certificates -app "NdsBbis" -clientName NdsBbisUi -casClientName NdsBbisCas -roles "ROLE_BBIS_UI, SomeOtherRole" -casRoles "ROLE_BBIS_CAS, SomeOtherCasRole"
	
#>

## Parameters to this script
param([string]$app="NdsRefApp", [string]$password="password", [Int32]$keyLength=4096, [Int32]$validFor=3650, [string]$roles="", [string]$clientName="client", [string]$casRoles="", [string]$casClientName="clientCas")

## Path to OPENSSL, it's configuration file and the exe
$openssl_dir=(Get-Item -Path "..\..\tools\windows\openssl\" -Verbose).FullName
$openssl_cnf=(Get-Item -Path ".\openssl.cnf" -Verbose).FullName 
$openssl_exe=$openssl_dir + "openssl.exe"

## Root working folder and other folders
$working_folder=(Get-Item -Path ".\" -Verbose).FullName
$root_scratch="$($working_folder)\scratch"
$project_folder="$($root_scratch)\$($app)"
#$openssl_log="$($working_folder)\openssl.log"
$extfile="$($project_folder)\extfile.cnf"

## Project folders
$esb_folder=(Get-Item -Path "..\..\..\$($app)Esb" -Verbose).FullName
$ui_folder=(Get-Item -Path "..\..\..\$($app)Ui" -Verbose).FullName
$cas_folder=(Get-Item -Path "..\..\..\$($app)Cas" -Verbose).FullName

if ((Test-Path "$($esb_folder)\src\main\resources\config") -eq 0)
{
	"Creating ESB config folder $($esb_folder)\src\main\resources\config"
	New-Item -ItemType Directory -Path "$($esb_folder)\src\main\resources\config" | Out-null
}
if ((Test-Path "$($ui_folder)\src\main\resources\config") -eq 0)
{
	"Creating UI config folder $($ui_folder)\src\main\resources\config"
	New-Item -ItemType Directory -Path "$($ui_folder)\src\main\resources\config" | Out-null
}
if ((Test-Path "$($cas_folder)\src\main\resources\config") -eq 0)
{
	"Creating CAS config folder $($cas_folder)\src\main\resources\config"
	New-Item -ItemType Directory -Path "$($cas_folder)\src\main\resources\config" | Out-null
}

$esb_config_folder=(Get-Item -Path "$($esb_folder)\src\main\resources\config" -Verbose).FullName
$ui_config_folder=(Get-Item -Path "$($ui_folder)\src\main\resources\config" -Verbose).FullName
$cas_config_folder=(Get-Item -Path "$($cas_folder)\src\main\resources\config" -Verbose).FullName
$esb_cert_folder="$($esb_config_folder)\certs"
$ui_cert_folder="$($ui_config_folder)\certs"
$cas_cert_folder="$($cas_config_folder)\certs"


## Other, non folder based, stuff
$hostname=$env:COMPUTERNAME
$ip=get-WmiObject Win32_NetworkAdapterConfiguration | Where {$_.Ipaddress.length -gt 1} 
$ipv4list=$ip.ipaddress | where {$_ -notmatch ":"}
$subjectAltName="IP:127.0.0.1"
foreach($ipv4 in $ipv4list) { $subjectAltName+=",IP:$($ipv4)" }
$badClientName="bad$($clientName)"

## set the OPENSSL_CONF environment variable
$env:OPENSSL_CONF=$openssl_cnf

function Import-PfxCertificate {
	param([String]$certPath, [String]$certRootStore = "CurrentUser", [String]$certStore = "My", $pfxPass = $null)
	$pfx = new-object System.Security.Cryptography.X509Certificates.X509Certificate2
 
	if ($pfxPass -eq $null) {$pfxPass = read-host "Enter the pfx password" -assecurestring}
 
	$pfx.import($certPath,$pfxPass,"Exportable,PersistKeySet")
 
	$store = new-object System.Security.Cryptography.X509Certificates.X509Store($certStore,$certRootStore)
	$store.open("MaxAllowed")
	$store.add($pfx)
	$store.close()
}

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

## Make project folders
if ((Test-Path $esb_cert_folder) -eq 0)
{
	"Creating ESB certificate folder $esb_cert_folder"
	New-Item -ItemType Directory -Path $esb_cert_folder | Out-null
}
else
{
	"Cleaning ESB certificate folder $esb_cert_folder"
	Remove-Item "$($esb_cert_folder)\*" -recurse | Out-null
}
if ((Test-Path $ui_cert_folder) -eq 0)
{
	"Creating UI certificate folder $ui_cert_folder"
	New-Item -ItemType Directory -Path $ui_cert_folder | Out-null
}
else
{
	"Cleaning UI certificate folder $ui_cert_folder"
	Remove-Item "$($ui_cert_folder)\*" -recurse | Out-null
}
if ((Test-Path $cas_cert_folder) -eq 0)
{
	"Creating CAS certificate folder $cas_cert_folder"
	New-Item -ItemType Directory -Path $cas_cert_folder | Out-null
}
else
{
	"Cleaning CAS certificate folder $cas_cert_folder"
	Remove-Item "$($cas_cert_folder)\*" -recurse | Out-null
}

## Make the CA key and certificate
"Generating CA key and certificate"
&$openssl_exe genrsa -aes256 -out "$($project_folder)\ca-key.pem" -passout pass:$password $keyLength 2>&1 | Out-null
&$openssl_exe req -subj "/CN=$($hostname)/OU=NDS/O=Northgate Public Services/L=Milton Keynes/ST=Buckinghamshire/C=UK" -new -x509 -days $validFor -batch -key "$($project_folder)\ca-key.pem" -passin pass:$password -sha256 -out "$($project_folder)\ca-cert.pem" -extensions v3_ca 2>&1 | Out-null

## Make the Server key and certificate
"Generating server key and certificate"
&$openssl_exe genrsa -aes256 -out "$($project_folder)\server-key.pem" -passout pass:$password $keyLength 2>&1 | Out-null
&$openssl_exe req -subj "/CN=$($hostname)" -sha256 -new -key "$($project_folder)\server-key.pem" -passin pass:$password -out "$($project_folder)\server.csr" 2>&1 | Out-null
"subjectAltName = $($subjectAltName)" | Out-file -filepath $extfile -encoding ASCII
"basicConstraints = CA:FALSE" | Out-file -filepath $extfile -encoding ASCII -append
"nsCertType = server" | Out-file -filepath $extfile -encoding ASCII -append
"nsComment = `"OpenSSL Generated Server Certificate`"" | Out-file -filepath $extfile -encoding ASCII -append
"subjectKeyIdentifier = hash" | Out-file -filepath $extfile -encoding ASCII -append
"authorityKeyIdentifier = keyid,issuer:always" | Out-file -filepath $extfile -encoding ASCII -append
"keyUsage = critical, digitalSignature, keyEncipherment" | Out-file -filepath $extfile -encoding ASCII -append
"extendedKeyUsage = serverAuth" | Out-file -filepath $extfile -encoding ASCII -append
&$openssl_exe x509 -req -days $validFor -sha256 -in "$($project_folder)\server.csr" -CA "$($project_folder)\ca-cert.pem" -passin pass:$password -CAkey "$($project_folder)\ca-key.pem" -CAcreateserial -out "$($project_folder)\server-cert.pem" -extfile $extfile 2>&1 | Out-null

## Make the Client key and certificate (note that changes to the OIDs may need to be made in PreAuthEsbUserDetailsService.java or similar classes)
"Generating client key and certificate"
&$openssl_exe genrsa -aes256 -out "$($project_folder)\client-key.pem" -passout pass:$password $keyLength 2>&1 | Out-null
&$openssl_exe req -subj "/CN=$($clientName)" -new -key "$($project_folder)\client-key.pem" -passin pass:$password -out "$($project_folder)\client.csr" 2>&1 | Out-null
"basicConstraints = CA:FALSE" | Out-file -filepath $extfile -encoding ASCII
"nsCertType = client" | Out-file -filepath $extfile -encoding ASCII -append
"nsComment = `"OpenSSL Generated Client Certificate`"" | Out-file -filepath $extfile -encoding ASCII -append
"subjectKeyIdentifier = hash" | Out-file -filepath $extfile -encoding ASCII -append
"authorityKeyIdentifier = keyid,issuer" | Out-file -filepath $extfile -encoding ASCII -append
"keyUsage = critical, nonRepudiation, digitalSignature, keyEncipherment" | Out-file -filepath $extfile -encoding ASCII -append
"extendedKeyUsage = clientAuth" | Out-file -filepath $extfile -encoding ASCII -append
"1.2.826.0.1.968498.7.5.2.5.1 = ASN1:EXPLICIT:0P,FORMAT:UTF8,UTF8:`"$($roles)`"" | Out-file -filepath $extfile -encoding ASCII -append
&$openssl_exe x509 -req -days $validFor -sha256 -in "$($project_folder)\client.csr" -CA "$($project_folder)\ca-cert.pem" -passin pass:$password -CAkey "$($project_folder)\ca-key.pem" -CAcreateserial -out "$($project_folder)\client-cert.pem" -extfile $extfile 2>&1 | Out-null

## Make the Bad Client key and certificate
"Generating bad client key and certificate"
&$openssl_exe genrsa -aes256 -out "$($project_folder)\bad-client-key.pem" -passout pass:$password $keyLength 2>&1 | Out-null
&$openssl_exe req -subj "/CN=$($badClientName)" -new -key "$($project_folder)\bad-client-key.pem" -passin pass:$password -out "$($project_folder)\bad-client.csr" 2>&1 | Out-null
"basicConstraints = CA:FALSE" | Out-file -filepath $extfile -encoding ASCII
"nsCertType = client" | Out-file -filepath $extfile -encoding ASCII -append
"nsComment = `"OpenSSL Generated Client Certificate`"" | Out-file -filepath $extfile -encoding ASCII -append
"subjectKeyIdentifier = hash" | Out-file -filepath $extfile -encoding ASCII -append
"authorityKeyIdentifier = keyid,issuer" | Out-file -filepath $extfile -encoding ASCII -append
"keyUsage = critical, nonRepudiation, digitalSignature, keyEncipherment" | Out-file -filepath $extfile -encoding ASCII -append
"extendedKeyUsage = clientAuth" | Out-file -filepath $extfile -encoding ASCII -append
"1.2.826.0.1.968498.7.5.2.5.1 = ASN1:EXPLICIT:0P,FORMAT:UTF8,UTF8:`"wibble`"" | Out-file -filepath $extfile -encoding ASCII -append
&$openssl_exe x509 -req -days $validFor -sha256 -in "$($project_folder)\bad-client.csr" -CA "$($project_folder)\ca-cert.pem" -passin pass:$password -CAkey "$($project_folder)\ca-key.pem" -CAcreateserial -out "$($project_folder)\bad-client-cert.pem" -extfile $extfile 2>&1 | Out-null

## Make the Client key and certificate for CAS (note that changes to the OIDs may need to be made in PreAuthEsbUserDetailsService.java or similar classes)
"Generating client key and certificate for CAS"
&$openssl_exe genrsa -aes256 -out "$($project_folder)\cas-client-key.pem" -passout pass:$password $keyLength 2>&1 | Out-null
&$openssl_exe req -subj "/CN=$($casClientName)" -new -key "$($project_folder)\cas-client-key.pem" -passin pass:$password -out "$($project_folder)\cas-client.csr" 2>&1 | Out-null
"basicConstraints = CA:FALSE" | Out-file -filepath $extfile -encoding ASCII
"nsCertType = client" | Out-file -filepath $extfile -encoding ASCII -append
"nsComment = `"OpenSSL Generated Client Certificate`"" | Out-file -filepath $extfile -encoding ASCII -append
"subjectKeyIdentifier = hash" | Out-file -filepath $extfile -encoding ASCII -append
"authorityKeyIdentifier = keyid,issuer" | Out-file -filepath $extfile -encoding ASCII -append
"keyUsage = critical, nonRepudiation, digitalSignature, keyEncipherment" | Out-file -filepath $extfile -encoding ASCII -append
"extendedKeyUsage = clientAuth" | Out-file -filepath $extfile -encoding ASCII -append
"1.2.826.0.1.968498.7.5.2.5.1 = ASN1:EXPLICIT:0P,FORMAT:UTF8,UTF8:`"$($casRoles)`"" | Out-file -filepath $extfile -encoding ASCII -append
&$openssl_exe x509 -req -days $validFor -sha256 -in "$($project_folder)\cas-client.csr" -CA "$($project_folder)\ca-cert.pem" -passin pass:$password -CAkey "$($project_folder)\ca-key.pem" -CAcreateserial -out "$($project_folder)\cas-client-cert.pem" -extfile $extfile 2>&1 | Out-null

## Generate Trust & Key Stores
"Generating trust & key stores"
Get-Content "$($project_folder)\ca-key.pem", "$($project_folder)\ca-cert.pem" | Set-Content "$($project_folder)\ca-key-cert.pem"
&$openssl_exe pkcs12 -export -passin pass:$password -in "$($project_folder)\ca-key-cert.pem" -passout pass:$password -out "$($project_folder)\ca-truststore.p12" -name $app -CAfile "$($project_folder)\ca-cert.pem" -caname root -chain 2>&1 | Out-null
Get-Content "$($project_folder)\server-key.pem", "$($project_folder)\server-cert.pem" | Set-Content "$($project_folder)\server-key-cert.pem"
&$openssl_exe pkcs12 -export -passin pass:$password -in "$($project_folder)\server-key-cert.pem" -passout pass:$password -out "$($project_folder)\server-keystore.p12" -name $app -CAfile "$($project_folder)\ca-cert.pem" -caname root -chain 2>&1 | Out-null
Get-Content "$($project_folder)\client-key.pem", "$($project_folder)\client-cert.pem" | Set-Content "$($project_folder)\client-key-cert.pem"
&$openssl_exe pkcs12 -export -passin pass:$password -in "$($project_folder)\client-key-cert.pem" -passout pass:$password -out "$($project_folder)\client-keystore.p12" -name $app -CAfile "$($project_folder)\ca-cert.pem" -caname root -chain 2>&1 | Out-null
Get-Content "$($project_folder)\bad-client-key.pem", "$($project_folder)\bad-client-cert.pem" | Set-Content "$($project_folder)\bad-client-key-cert.pem"
&$openssl_exe pkcs12 -export -passin pass:$password -in "$($project_folder)\bad-client-key-cert.pem" -passout pass:$password -out "$($project_folder)\bad-client-keystore.p12" -name $app -CAfile "$($project_folder)\ca-cert.pem" -caname root -chain 2>&1 | Out-null
Get-Content "$($project_folder)\cas-client-key.pem", "$($project_folder)\cas-client-cert.pem" | Set-Content "$($project_folder)\cas-client-key-cert.pem"
&$openssl_exe pkcs12 -export -passin pass:$password -in "$($project_folder)\cas-client-key-cert.pem" -passout pass:$password -out "$($project_folder)\cas-client-keystore.p12" -name $app -CAfile "$($project_folder)\ca-cert.pem" -caname root -chain 2>&1 | Out-null

## Copy to Projects
"Copy certificates to project folders"
Copy-Item "$($project_folder)\ca-truststore.p12" "$($esb_cert_folder)\ca-truststore.p12"
Copy-Item "$($project_folder)\server-keystore.p12" "$($esb_cert_folder)\server-keystore.p12"

Copy-Item "$($project_folder)\server-cert.pem" "$($esb_cert_folder)\server-cert.pem"
Copy-Item "$($project_folder)\server-key.pem" "$($esb_cert_folder)\server-key.pem"
Copy-Item "$($project_folder)\ca-cert.pem" "$($esb_cert_folder)\ca-cert.pem"

Copy-Item "$($project_folder)\ca-truststore.p12" "$($ui_cert_folder)\ca-truststore.p12"
Copy-Item "$($project_folder)\client-keystore.p12" "$($ui_cert_folder)\client-keystore.p12"
Copy-Item "$($project_folder)\bad-client-keystore.p12" "$($ui_cert_folder)\bad-client-keystore.p12"

Copy-Item "$($project_folder)\ca-truststore.p12" "$($cas_cert_folder)\ca-truststore.p12"
Copy-Item "$($project_folder)\cas-client-keystore.p12" "$($cas_cert_folder)\client-keystore.p12"

## Install CA and client certificate into the local windows certificate store
## Should allow IE and Chrome to access any secured resources
"Installing CA and client certificates"
Import-PfxCertificate "$($ui_cert_folder)\ca-truststore.p12" "CurrentUser" "CA" $password
Import-PfxCertificate "$($ui_cert_folder)\client-keystore.p12" "CurrentUser" "My" $password
Import-PfxCertificate "$($cas_cert_folder)\client-keystore.p12" "CurrentUser" "My" $password

## Post Generation Notes
"TODO:"
"====="
"1. Add the client certificates, via their trust and key store $($ui_cert_folder)\client-truststore.p12 into the browser of your choice"
"2. Modify you tomcat connector to use the trust and keys stores within $($esb_cert_folder); similar to"
""
" Tomcat 8 : "
"	<Connector port=`"8443`" protocol=`"org.apache.coyote.http11.Http11Protocol`""
"		maxThreads=`"150`" SSLEnabled=`"true`" scheme=`"https`" secure=`"true`""
"		keystoreFile=`"$($esb_cert_folder)\server-keystore.p12`""
"		keystoreType=`"PKCS12`" keystorePass=`"$($password)`" clientAuth=`"true`""
"		truststoreFile=`"$($esb_cert_folder)\ca-truststore.p12`""
"		truststoreType=`"PKCS12`" truststorePass=`"$($password)`" sslProtocol=`"TLS`" />"
""
" Tomcat 8.5 : "
"	<Connector port=`"8443`" protocol=`"org.apache.coyote.http11.Http11NioProtocol`""
"            maxThreads=`"150`" SSLEnabled=`"true`" scheme=`"https`" secure=`"true`" server=`"Apache`" maxPostSize=`"100000`">"
"		<SSLHostConfig certificateVerification=`"required`" protocols=`"TLSv1.2`""
"               caCertificateFile=`"$($esb_cert_folder)\ca-cert.pem`" honorCipherOrder=`"true`""
"               ciphers=`"TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,"
"                        TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384,"
"                        TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,"
"                        TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256,TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256,"
"                        TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384,"
"                        TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,"
"                        TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384,"
"                        TLS_ECDH_RSA_WITH_AES_256_CBC_SHA,TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA,"
"                        TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256,"
"                        TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,"
"                        TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256,"
"                        TLS_ECDH_RSA_WITH_AES_128_CBC_SHA,TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA`">"
"                <Certificate certificateFile=`"$($esb_cert_folder)\server-cert.pem`""
"                        certificateKeyFile=`"$($esb_cert_folder)\server-key.pem`" certificateKeyPassword=`"$($password)`">"
"                </Certificate>"
"        </SSLHostConfig>"
"    </Connector>"
""
"Certificate generation complete."



