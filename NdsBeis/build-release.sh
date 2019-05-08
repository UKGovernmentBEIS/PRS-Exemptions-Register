#!/bin/bash

# Build release of BEIS

set -e

maven=$1
release_version=$2
release_repo=$3

# this is probably a hack, but version numbers of plugins aren't updating as for dependencies, so we append the following to any builds that use any
# NDS plugins. (It should be possible to update versions:update-property, but that just fails with an NPE)
platform_version=-Dnds.platform.version=${release_version}
plugin_version=-Dnds.checkweaving.version=${release_version}" "-Dnds.generatepageobjects.version=${release_version}" "${platform_version}
databases=-Dndsdb.server=vm-nds-tst02.global.internal" "-Dndsdb.port=27018" "-Dldap.url=ldap://vm-nds-tst02.global.internal:1390
version_plugin=org.codehaus.mojo:versions-maven-plugin:2.5

# NDS Logging System
${maven} -f platform/NdsAspects/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.platform.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f platform/NdsAspects/pom.xml -s beis/NdsBeis/release-settings.xml versions:set -DgroupId=* -DartifactId=* -DoldVersion=* -DnewVersion=${release_version} -DgenerateBackupPoms=false
${maven} -f platform/NdsAspects/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=release::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${platform_version}

# NDS Shared POMs
# note the grep/sed lines later, as maven will not update-parent with the release-version as it's already valid for the range specified in the poms, even though there
# might be a later version somewhere else
${maven} -f platform/NdsSharedPoms/build/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.platform.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f platform/NdsSharedPoms/build/pom.xml -s beis/NdsBeis/release-settings.xml versions:set -DgroupId=* -DartifactId=* -DoldVersion=* -DnewVersion=${release_version} -DgenerateBackupPoms=false
${maven} -f platform/NdsSharedPoms/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.checkweaving.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f platform/NdsSharedPoms/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.platform.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f platform/NdsSharedPoms/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${platform_version}
${maven} -f platform/NdsSharedPoms/NdsSharedPomTest/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.platform.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f platform/NdsSharedPoms/NdsSharedPomTest/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${platform_version}
${maven} -f platform/NdsSharedPoms/NdsSharedPomUi/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.platform.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f platform/NdsSharedPoms/NdsSharedPomUi/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version}
${maven} -f platform/NdsSharedPoms/NdsSharedPomEsb/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.platform.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f platform/NdsSharedPoms/NdsSharedPomEsb/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version}
${maven} -f platform/NdsSharedPoms/NdsSharedPomCas/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.platform.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f platform/NdsSharedPoms/NdsSharedPomCas/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version}

# Nds Platform Build
grep -l -r --include "pom.xml" "<version>0.0.0-SNAPSHOT</version>" platform/NdsPlatform* | xargs -i -n 1 sed 's~<version>0.0.0-SNAPSHOT</version>~<version>'${release_version}'</version>~g' -i {}
grep -l -r --include "pom.xml" "<version>0.0.0-SNAPSHOT</version>" platform/NdsMavenPlugins* | xargs -i -n 1 sed 's~<version>0.0.0-SNAPSHOT</version>~<version>'${release_version}'</version>~g' -i {}
${maven} -f platform/NdsPlatform/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.platform.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f platform/NdsPlatform/pom.xml -s beis/NdsBeis/release-settings.xml -DskipTests -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version} 
${maven} -f platform/NdsMavenPlugins/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.platform.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f platform/NdsMavenPlugins/pom.xml -s beis/NdsBeis/release-settings.xml versions:update-property versions:update-property -DnewVersion="[${release_version}]" -Dproperty=nds.platform.version -DgenerateBackupPoms=false
${maven} -f platform/NdsMavenPlugins/pom.xml -s beis/NdsBeis/release-settings.xml -DskipTests -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version} 

# BEIS Build
grep -l -r --include "pom.xml" "<version>0.0.0-SNAPSHOT</version>" beis/NdsBeis* | xargs -i -n 1 sed 's~<version>0.0.0-SNAPSHOT</version>~<version>'${release_version}'</version>~g' -i {}
${maven} -f beis/NdsBeis/pom.xml -s beis/NdsBeis/release-settings.xml ${version_plugin}:set-property -Dproperty=nds.platform.version -DnewVersion=${release_version} -DallowSnapshots=true -DforceNewVersion=true -DgenerateBackupPoms=false
${maven} -f beis/NdsBeis/pom.xml -s beis/NdsBeis/release-settings.xml versions:set -DgroupId=* -DartifactId=* -DoldVersion=* -DnewVersion=${release_version} -DgenerateBackupPoms=false
${maven} -f beis/NdsBeis/pom.xml -s beis/NdsBeis/release-settings.xml versions:update-property versions:update-property -DnewVersion="[${release_version}]" -Dproperty=nds.platform.version -DgenerateBackupPoms=false
${maven} -f beis/NdsBeis/pom.xml -s beis/NdsBeis/release-settings.xml versions:update-property versions:update-property -DnewVersion="[${release_version}]" -Dproperty=nds.beis.version -DgenerateBackupPoms=false
${maven} -f beis/NdsBeis/pom.xml ${databases} -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version} ${platform_version}
