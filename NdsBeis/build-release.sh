#!/bin/bash

# Build release of BEIS

set -e

maven=$1
release_version=$2
release_repo=$3

# this is probably a hack, but version numbers of plugins aren't updating as for dependencies, so we append the following to any builds that use any
# NDS plugins. (It should be possible to update versions:update-property, but that just fails with an NPE)
plugin_version=-Dnds.checkweaving.version=${release_version}" "-Dnds.generatepageobjects.version=${release_version}
databases=-Dndsdb.server=vm-nds-tst02.global.internal" "-Dndsdb.port=27018" "-Dldap.url=ldap://vm-nds-tst02.global.internal:1390

# NDS Logging System

${maven} -f nds/NdsAspects/pom.xml -s beis/NdsBeis/release-settings.xml versions:set -DgroupId=* -DartifactId=* -DoldVersion=* -DnewVersion=${release_version} -DgenerateBackupPoms=false
${maven} -f nds/NdsAspects/pom.xml -s beis/NdsBeis/release-settings.xml versions:use-latest-versions -Dincludes=com.northgateps.nds.*:* -DgenerateBackupPoms=false
${maven} -f nds/NdsAspects/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=release::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U

# NDS Shared POMs

${maven} -f nds/NdsSharedPoms/build/pom.xml -s beis/NdsBeis/release-settings.xml versions:set -DgroupId=* -DartifactId=* -DoldVersion=* -DnewVersion=${release_version} -DgenerateBackupPoms=false 
${maven} -f nds/NdsSharedPoms/build/pom.xml -s beis/NdsBeis/release-settings.xml versions:use-latest-versions -Dincludes=com.northgateps.nds.*:* -DgenerateBackupPoms=false 
${maven} -f nds/NdsSharedPoms/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U
${maven} -f nds/NdsSharedPoms/NdsSharedPomTest/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U
${maven} -f nds/NdsSharedPoms/NdsSharedPomUi/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version}
${maven} -f nds/NdsSharedPoms/NdsSharedPomEsb/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version} 
${maven} -f nds/NdsSharedPoms/NdsSharedPomCas/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version} 

# Nds Platform Build

${maven} -f nds/NdsPlatform/pom.xml -s beis/NdsBeis/release-settings.xml versions:set -DgroupId=* -DartifactId=* -DoldVersion=* -DnewVersion=${release_version} -DgenerateBackupPoms=false
${maven} -f nds/NdsPlatform/pom.xml -s beis/NdsBeis/release-settings.xml versions:use-latest-versions -Dincludes=com.northgateps.nds.*:* -DgenerateBackupPoms=false
${maven} -f nds/NdsPlatform/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version}
${maven} -f nds/NdsMavenPlugins/pom.xml -s beis/NdsBeis/release-settings.xml versions:set -DgroupId=* -DartifactId=* -DoldVersion=* -DnewVersion=${release_version} -DgenerateBackupPoms=false
${maven} -f nds/NdsMavenPlugins/pom.xml -s beis/NdsBeis/release-settings.xml versions:use-latest-versions -Dincludes=com.northgateps.nds.*:* -DgenerateBackupPoms=false
${maven} -f nds/NdsMavenPlugins/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version}

# BEIS Build

${maven} -f beis/NdsSharedPoms/build/pom.xml -s beis/NdsBeis/release-settings.xml versions:set -DgroupId=* -DartifactId=* -DoldVersion=* -DnewVersion=${release_version} -DgenerateBackupPoms=false
${maven} -f beis/NdsSharedPoms/build/pom.xml -s beis/NdsBeis/release-settings.xml versions:use-latest-versions -Dincludes=com.northgateps.beis.*:*,com.northgateps.nds.*:* -DgenerateBackupPoms=false
${maven} -f beis/NdsSharedPoms/build/pom.xml -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version}
${maven} -f beis/NdsBeis/pom.xml -s beis/NdsBeis/release-settings.xml versions:set -DgroupId=* -DartifactId=* -DoldVersion=* -DnewVersion=${release_version} -DgenerateBackupPoms=false
${maven} -f beis/NdsBeis/pom.xml -s beis/NdsBeis/release-settings.xml versions:use-latest-versions -Dincludes=com.northgateps.beis.*:*,com.northgateps.nds.*:* -DgenerateBackupPoms=false
${maven} -f beis/NdsBeis/pom.xml ${databases} -s beis/NdsBeis/release-settings.xml -DaltDeploymentRepository=build::default::${release_repo} -DdeployAtEnd=true clean install deploy:deploy -U ${plugin_version}
