#!/usr/bin/env groovy

/*
 * Jenkins pipeline file for BEIS builds
 *
 */

    def MAJOR_VERSION_NUMBER="0.0"
    def TEST_DELAY=180

    properties ([
        buildDiscarder(logRotator(daysToKeepStr: '5', numToKeepStr: '5'))
    ])
 
    @Library('nds-jenkins-shared-library') import com.northgateps.Utilities

    timestamps {
    
        if (Utilities.isReleaseBuildFromBranchName(env.BRANCH_NAME)) {
            def branchParts = env.BRANCH_NAME.tokenize("/")
            RELEASE_VERSION=branchParts[2]
            FULL_BUILD_VERSION="${RELEASE_VERSION}.${BUILD_NUMBER}"
            RELEASE_TEXT="Release "
            ADDITIONAL_MVN_SETTINGS="-s NdsBeis/release-settings.xml"
        } else {
            RELEASE_VERSION=""
            FULL_BUILD_VERSION="${MAJOR_VERSION_NUMBER}.${BUILD_NUMBER}"
            RELEASE_TEXT=""
            ADDITIONAL_MVN_SETTINGS=""
        }

		def utils = new Utilities(steps, env, currentBuild, "beis", "NdsBeis", "BEIS", "${FULL_BUILD_VERSION}", "${TEST_DELAY}", "${RELEASE_VERSION}")
		utils.setEmailAddresses "${BEIS_BAS}", "${BEIS_DEVOPS}", "${BEIS_DEVELOPERS}", "${BEIS_TESTERS}"
		utils.setBuildRepos "${BEIS_BUILD_REPO_URL}", "${BEIS_RELEASE_REPO_URL}"
                utils.setAdditionalMavenSettings "${ADDITIONAL_MVN_SETTINGS}"
                utils.setJdkLabel "JDK_11"

		try {

			stage ('Build') {
				node ('beis-build') {
					utils.codeGitCheckout(scm)
					utils.emailChangeLog()
					utils.javaCodeBuild()
					utils.javaCodeBuildWithGradle("NdsBeisCas")
				}
			}

			stage ('Post Build') {
				parallel documentation: {
					node ('beis-build') {
					        //utils.javaDocumentationBuild()
					}
                    node ('scancode-toolkit-build') {
						//utils.codeGitCheckout(scm)
						//utils.scanCodeTooklitBuild()
                    }				
                }
				dockerImageBuild: {
					node ('beis-docker-build') {
						utils.dockerBaseImageBuild("beis-NdsBeisCas-${FULL_BUILD_VERSION}")
					}
					node ('beis-docker-auto-run') {
						utils.deployDockeredEnvironment("autotest")
					}
				}
			}

			lock (resource: "beis-${utils.getBranch()}-autotest", inversePrecedence: true) {
				stage ('Auto Testing') {
					node ('beis-build') {
						utils.codeGitCheckout(scm)
						utils.runAutotest("-Dnds.integrationTest.uploadFilePath=/var/tmp/files/")
					}
				}
			}

			def manualTestDeployed, manualTestLabel, manualTestNode, manualTestType, manualTestConnectedTo
			stage ('Manual Testing') {
                if ( utils.isReleaseBuild() ) {
                    manualTestLabel = "Release Smoke Test"
                    manualTestNode = "beis-docker-run"
                    manualTestType = "release"
                    manualTestConnectedTo = "Connected to BEISTST2"
                } else if (utils.isDevelopBranch() ) {
                    manualTestLabel = "Manual Test"
                    manualTestNode = "beis-docker-run"
                    manualTestType = "manualtest"
                    manualTestConnectedTo = "Connected to BEISTST2"
                } else  {
                    manualTestLabel = ""
                    manualTestNode = ""
                    manualTestType = ""
                    manualTestConnectedTo = ""
                 }

				node(manualTestNode) {
                    if (manualTestType?.trim()) {
					    manualTestDeployed = utils.deployDockeredEnvironment(manualTestType, manualTestLabel, manualTestConnectedTo)
                    } else {
                        manualTestDeployed = false
                    }
				}
			}

			if (manualTestDeployed && !utils.isReleaseBuild()) {
				stage ('Gather Manual Testing Results') {
					node('beis-build') {
						utils.codeGitCheckout(scm)
						if (utils.getWaitForManualTestResults()) {
							utils.waitForResults(manualTestType, manualTestLabel)
						} else {
							utils.tagReleaseCandiate()
						}
					}
				}
			}

	    	currentBuild.result = "SUCCESS"
            } catch (err) {
                currentBuild.result = "FAILURE"
                throw err
            } finally {
		        if (currentBuild.result != "ABORTED" && currentBuild.result != "NOT_BUILT") {
                    node ('beis-build') {
                        step([$class: 'Mailer', notifyEveryUnstableBuild: false, recipients: "${BEIS_DEVELOPERS}", sendToIndividuals: false])
                    }
                }
            }
        }
