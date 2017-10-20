<?xml version="1.0" encoding="UTF-8"?>
<structure version="16" xsltversion="1" html-doctype="HTML4 Transitional" compatibility-view="IE9" relativeto="*SPS" encodinghtml="UTF-8" encodingrtf="ISO-8859-1" encodingpdf="UTF-8" useimportschema="1" embed-images="1" enable-authentic-scripts="1" authentic-scripts-in-debug-mode-external="0" generated-file-location="DEFAULT">
	<parameters/>
	<schemasources>
		<namespaces>
			<nspair prefix="beped" uri="http://northgatepublicservices.com/nds/beis/printexemptiondetails/2016/11/16"/>
			<nspair prefix="ns1" uri="http://northgatepublicservices.com/nds/platform/model/2015/11/24"/>
			<nspair prefix="ns11" uri="http://northgatepublicservices.com/nds/platform/validation/2015/05/28"/>
			<nspair prefix="ns2" uri="http://northgatepublicservices.com/nds/beis/model/2016/11/16"/>
		</namespaces>
		<schemasources>
			<xsdschemasource name="XML" main="1" schemafile="beis_schema\printExemptionDetails.xsd" workingxmlfile="exemption-details.xml"/>
		</schemasources>
	</schemasources>
	<modules/>
	<flags>
		<scripts/>
		<mainparts/>
		<globalparts/>
		<designfragments/>
		<pagelayouts/>
		<xpath-functions/>
	</flags>
	<scripts>
		<script language="javascript"/>
	</scripts>
	<script-project>
		<Project version="3" app="AuthenticView"/>
	</script-project>
	<importedxslt/>
	<globalstyles/>
	<mainparts>
		<children>
			<globaltemplate subtype="main" match="/">
				<document-properties/>
				<styles font-family="Helvetica"/>
				<children>
					<documentsection>
						<properties columncount="1" columngap="0.50in" headerfooterheight="fixed" pagemultiplepages="0" pagenumberingformat="1" pagenumberingstartat="auto" pagestart="next" paperheight="11.69in" papermarginbottom="0.79in" papermarginfooter="0.30in" papermarginheader="0.30in" papermarginleft="0.60in" papermarginright="0.60in" papermargintop="0.79in" paperwidth="8.27in"/>
						<watermark>
							<image transparency="50" fill-page="1" center-if-not-fill="1"/>
							<text transparency="50"/>
						</watermark>
					</documentsection>
					<template subtype="source" match="XML">
						<children>
							<template subtype="element" match="beped:PrintExemptionDetailsNdsRequest">
								<children>
									<newline/>
									<text fixtext="Exemption summary">
										<styles font-family="Helvetica" font-size="18pt"/>
									</text>
									<newline/>
									<newline/>
									<line/>
									<tgrid>
										<properties cellpadding="0" cellspacing="0" width="100%"/>
										<children>
											<tgridbody-cols>
												<children>
													<tgridcol>
														<properties width="30%"/>
													</tgridcol>
													<tgridcol>
														<properties width="70%"/>
													</tgridcol>
												</children>
											</tgridbody-cols>
											<tgridbody-rows>
												<children>
													<tgridrow>
														<children>
															<tgridcell>
																<properties valign="top"/>
																<children>
																	<text fixtext="EPC Reference ID"/>
																</children>
															</tgridcell>
															<tgridcell>
																<properties valign="top"/>
																<children>
																	<template subtype="element" match="beped:exemptionDetail">
																		<children>
																			<template subtype="element" match="ns2:referenceId">
																				<children>
																					<content subtype="regular"/>
																				</children>
																				<variables/>
																			</template>
																		</children>
																		<variables/>
																	</template>
																</children>
															</tgridcell>
														</children>
													</tgridrow>
												</children>
											</tgridbody-rows>
										</children>
									</tgrid>
									<line>
										<styles height="1px"/>
									</line>
									<tgrid>
										<properties cellpadding="0" cellspacing="0" width="100%"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties valign="top" width="30%"/>
																	</tgridcol>
																	<tgridcol>
																		<properties width="70%"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridbody-rows>
																<children>
																	<tgridrow>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="Landlord Name">
																						<styles font-size="12pt"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<tgrid>
																						<properties cellpadding="0" cellspacing="0" width="100%"/>
																						<children>
																							<tgridbody-cols>
																								<children>
																									<tgridcol/>
																								</children>
																							</tgridbody-cols>
																							<tgridbody-rows>
																								<children>
																									<tgridrow>
																										<children>
																											<tgridcell>
																												<children>
																													<template subtype="element" match="beped:exemptionDetail">
																														<children>
																															<template subtype="element" match="ns2:landlordDetails">
																																<children>
																																	<template subtype="element" match="ns2:personNameDetail">
																																		<children>
																																			<template subtype="element" match="ns2:firstname">
																																				<children>
																																					<content subtype="regular"/>
																																				</children>
																																				<variables/>
																																			</template>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																											</tgridcell>																											
																										</children>
																									</tgridrow>
																									<tgridrow>
																										<children>
																											<tgridcell>
																												<children>
																													<template subtype="element" match="beped:exemptionDetail">
																														<children>
																															<template subtype="element" match="ns2:landlordDetails">
																																<children>
																																	<template subtype="element" match="ns2:personNameDetail">
																																		<children>
																																			<template subtype="element" match="ns2:surname">
																																				<children>
																																					<content subtype="regular"/>
																																				</children>
																																				<variables/>
																																			</template>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																											</tgridcell>																											
																										</children>
																									</tgridrow>
																								</children>
																							</tgridbody-rows>
																						</children>
																					</tgrid>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>	
													<line>
														<styles height="1px"/>
													</line>
													<tgrid>
														<properties cellpadding="0" cellspacing="0" width="100%"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties valign="top" width="30%"/>
																	</tgridcol>
																	<tgridcol>
																		<properties width="70%"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridbody-rows>
																<children>
																	<tgridrow>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="Landlord Name">
																						<styles font-size="12pt"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<template subtype="element" match="beped:exemptionDetail">
																						<children>
																							<template subtype="element" match="ns2:landlordDetails">
																								<children>
																									<template subtype="element" match="ns2:organisationNameDetail">
																										<children>
																											<template subtype="element" match="ns2:orgName">
																												<children>
																													<content subtype="regular"/>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																						<variables/>
																					</template>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>									
													<line>
														<styles height="1px"/>
													</line>									
													<tgrid>
														<properties cellpadding="0" cellspacing="0" width="100%"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties valign="top" width="30%"/>
																	</tgridcol>
																	<tgridcol>
																		<properties width="70%"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridbody-rows>
																<children>
																	<tgridrow>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="Landlord Email Address">
																						<styles font-size="12pt"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<template subtype="element" match="beped:exemptionDetail">
																						<children>
																							<template subtype="element" match="ns2:landlordDetails">																				
																										<children>
																											<template subtype="element" match="ns2:emailAddress">
																												<children>
																													<content subtype="regular"/>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																								</template>																				
																						</children>
																						<variables/>
																					</template>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
													<line>
														<styles height="1px"/>
													</line>
													<tgrid>
														<properties cellpadding="0" cellspacing="0" width="100%"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties valign="top" width="30%"/>
																	</tgridcol>
																	<tgridcol>
																		<properties width="70%"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridbody-rows>
																<children>
																	<tgridrow>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="Landlord Phone Number">
																						<styles font-size="12pt"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<template subtype="element" match="beped:exemptionDetail">
																						<children>
																							<template subtype="element" match="ns2:landlordDetails">																				
																										<children>
																											<template subtype="element" match="ns2:phoneNumber">
																												<children>
																													<content subtype="regular"/>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																								</template>																				
																						</children>
																						<variables/>
																					</template>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
													<line>
														<styles height="1px"/>
													</line>
													<tgrid>
														<properties cellpadding="0" cellspacing="0" width="100%"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties valign="top" width="30%"/>
																	</tgridcol>
																	<tgridcol>
																		<properties width="70%"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridbody-rows>
																<children>
																	<tgridrow>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="Address of exempt property">
																						<styles font-size="12pt"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<template subtype="element" match="beped:exemptionDetail">
																						<children>
																							<template subtype="element" match="ns2:epc">
																								<children>
																									<template subtype="element" match="ns2:propertyAddress">
																										<children>
																											<template subtype="element" match="ns2:singleLineAddressPostcode">
																												<children>
																													<content subtype="regular"/>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																						<variables/>
																					</template>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
													<line>
														<styles height="1px"/>
													</line>
													<tgrid>
														<properties cellpadding="0" cellspacing="0" width="100%"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties valign="top" width="30%"/>
																	</tgridcol>
																	<tgridcol>
																		<properties width="70%"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridbody-rows>
																<children>
																	<tgridrow>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="EPC number">
																						<styles font-size="12pt"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<template subtype="element" match="beped:exemptionDetail">
																						<children>
																							<template subtype="element" match="ns2:epc">
																								<children>
																									<template subtype="element" match="ns2:referenceNumber">
																										<children>
																											<content subtype="regular"/>
																										</children>
																										<variables/>
																									</template>
																								</children>
																								<variables/>
																							</template>
																						</children>
																						<variables/>
																					</template>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
													<line>
														<styles height="1px"/>
													</line>
													<tgrid>
														<properties cellpadding="0" cellspacing="0" width="100%"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties valign="top" width="30%"/>
																	</tgridcol>
																	<tgridcol>
																		<properties width="70%"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridbody-rows>
																<children>
																	<tgridrow>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="Exemption type">
																						<styles font-size="12pt"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<template subtype="element" match="beped:exemptionType">
																						<children>
																							<template subtype="element" match="ns2:pwsDescription">
																								<children>
																									<content subtype="regular"/>
																								</children>
																								<variables/>
																							</template>
																						</children>
																						<variables/>
																					</template>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
													<line>
														<styles height="1px"/>
													</line>
													<tgrid>
														<properties cellpadding="0" cellspacing="0" width="100%"/>
														<children>
															<tgridbody-cols>
																<children>
																	<tgridcol>
																		<properties valign="top" width="30%"/>
																	</tgridcol>
																	<tgridcol>
																		<properties width="70%"/>
																	</tgridcol>
																</children>
															</tgridbody-cols>
															<tgridbody-rows>
																<children>
																	<tgridrow>
																		<children>
																			<tgridcell>
																				<children>
																					<text fixtext="Evidence required">
																						<styles font-size="12pt"/>
																					</text>
																				</children>
																			</tgridcell>
																			<tgridcell>
																				<children>
																					<template subtype="element" match="beped:exemptionType">
																						<children>
																							<template subtype="element" match="ns2:pwsText">
																								<children>
																									<content subtype="regular">
																										<properties id="pwsText"/>
																									</content>
																								</children>
																								<variables/>
																							</template>
																						</children>
																						<variables/>
																					</template>
																				</children>
																			</tgridcell>
																		</children>
																	</tgridrow>
																</children>
															</tgridbody-rows>
														</children>
													</tgrid>
													<line/>
													<newline/>
													<text fixtext="Evidence of exemption">
														<styles font-family="Helvetica" font-size="18pt"/>
													</text>
													<newline/>
													<line/>
													<condition>
														<children>
															<conditionbranch xpath="beped:exemptionDetail/ns2:epcEvidenceFiles/ns1:resources !=&apos;&apos;">
																<children>
																	<tgrid>
																		<properties border="0" width="100%"/>
																		<children>
																			<tgridbody-cols>
																				<children>
																					<tgridcol>
																						<properties width="30%"/>
																					</tgridcol>
																					<tgridcol>
																						<properties width="70%"/>
																					</tgridcol>
																				</children>
																			</tgridbody-cols>
																			<tgridbody-rows>
																				<children>
																					<tgridrow>
																						<children>
																							<tgridcell>
																								<properties valign="top"/>
																								<children>
																									<tgrid>
																										<properties border="0" width="100%"/>
																										<children>
																											<tgridbody-cols>
																												<children>
																													<tgridcol/>
																												</children>
																											</tgridbody-cols>
																											<tgridbody-rows>
																												<properties valign="top"/>
																												<children>
																													<template subtype="element" match="beped:exemptionType">
																														<children>
																															<template subtype="element" match="ns2:documentsPwsLabel">
																																<children>
																																	<tgridrow>
																																		<children>
																																			<tgridcell>
																																				<children>
																																					<content subtype="regular"/>
																																				</children>
																																			</tgridcell>
																																		</children>
																																	</tgridrow>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																											</tgridbody-rows>
																										</children>
																									</tgrid>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<properties valign="top"/>
																								<children>
																									<tgrid>
																										<properties border="0" width="100%"/>
																										<children>
																											<tgridbody-cols>
																												<children>
																													<tgridcol>
																														<properties width="100%"/>
																													</tgridcol>
																												</children>
																											</tgridbody-cols>
																											<tgridbody-rows>
																												<children>
																													<template subtype="element" match="beped:exemptionDetail">
																														<children>
																															<template subtype="element" match="ns2:epcEvidenceFiles">
																																<children>
																																	<template subtype="element" match="ns1:resources">
																																		<children>
																																			<tgridrow>
																																				<children>
																																					<tgridcell>
																																						<children>
																																							<text fixtext="File uploaded: "/>
																																							<template subtype="element" match="ns1:fileName">
																																								<children>
																																									<content subtype="regular"/>
																																								</children>
																																								<variables/>
																																							</template>
																																							<newline/>
																																							<paragraph/>
																																							<newline/>
																																							<template subtype="element" match="ns1:description">
																																								<children>
																																									<content subtype="regular"/>
																																								</children>
																																								<variables/>
																																							</template>
																																							<newline/>
																																							<condition>
																																								<children>
																																									<conditionbranch xpath="position() != last()">
																																										<children>
																																											<line>
																																												<styles height="1px"/>
																																											</line>
																																										</children>
																																									</conditionbranch>
																																								</children>
																																							</condition>
																																						</children>
																																					</tgridcell>
																																				</children>
																																			</tgridrow>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																											</tgridbody-rows>
																										</children>
																									</tgrid>
																								</children>
																							</tgridcell>
																						</children>
																					</tgridrow>
																				</children>
																			</tgridbody-rows>
																		</children>
																	</tgrid>
																</children>
															</conditionbranch>
														</children>
													</condition>
													<condition>
														<children>
															<conditionbranch xpath="beped:exemptionDetail/ns2:exemptionTextFile/ns1:resources !=&apos;&apos;">
																<children>
																	<line>
																		<styles height="1px"/>
																	</line>
																	<tgrid>
																		<properties border="0" width="100%"/>
																		<children>
																			<tgridbody-cols>
																				<children>
																					<tgridcol>
																						<properties width="30%"/>
																					</tgridcol>
																					<tgridcol>
																						<properties width="70%"/>
																					</tgridcol>
																				</children>
																			</tgridbody-cols>
																			<tgridbody-rows>
																				<children>
																					<tgridrow>
																						<children>
																							<tgridcell>
																								<properties valign="top"/>
																								<children>
																									<tgrid>
																										<properties border="0" width="100%"/>
																										<children>
																											<tgridbody-cols>
																												<children>
																													<tgridcol/>
																												</children>
																											</tgridbody-cols>
																											<tgridbody-rows>
																												<properties valign="top"/>
																												<children>
																													<template subtype="element" match="beped:exemptionType">
																														<children>
																															<template subtype="element" match="ns2:textPwsLabel">
																																<children>
																																	<tgridrow>
																																		<children>
																																			<tgridcell>
																																				<children>
																																					<content subtype="regular"/>
																																				</children>
																																			</tgridcell>
																																		</children>
																																	</tgridrow>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																											</tgridbody-rows>
																										</children>
																									</tgrid>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<properties valign="top"/>
																								<children>
																									<tgrid>
																										<properties border="0" width="100%"/>
																										<children>
																											<tgridbody-cols>
																												<children>
																													<tgridcol>
																														<properties width="100%"/>
																													</tgridcol>
																												</children>
																											</tgridbody-cols>
																											<tgridbody-rows>
																												<children>
																													<template subtype="element" match="beped:exemptionDetail">
																														<children>
																															<template subtype="element" match="ns2:exemptionTextFile">
																																<children>
																																	<template subtype="element" match="ns1:resources">
																																		<children>
																																			<tgridrow>
																																				<children>
																																					<tgridcell>
																																						<children>
																																							<text fixtext="File uploaded: "/>
																																							<template subtype="element" match="ns1:fileName">
																																								<children>
																																									<content subtype="regular"/>
																																								</children>
																																								<variables/>
																																							</template>
																																							<newline/>
																																							<paragraph/>
																																							<newline/>
																																							<template subtype="element" match="ns1:description">
																																								<children>
																																									<content subtype="regular"/>
																																								</children>
																																								<variables/>
																																							</template>
																																							<newline/>
																																							<condition>
																																								<children>
																																									<conditionbranch xpath="position() != last()">
																																										<children>
																																											<line>
																																												<styles height="1px"/>
																																											</line>
																																										</children>
																																									</conditionbranch>
																																								</children>
																																							</condition>
																																						</children>
																																					</tgridcell>
																																				</children>
																																			</tgridrow>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																																<variables/>
																															</template>
																														</children>
																														<variables/>
																													</template>
																												</children>
																											</tgridbody-rows>
																										</children>
																									</tgrid>
																								</children>
																							</tgridcell>
																						</children>
																					</tgridrow>
																				</children>
																			</tgridbody-rows>
																		</children>
																	</tgrid>
																</children>
															</conditionbranch>
														</children>
													</condition>
													<condition>
														<children>
															<conditionbranch xpath="beped:exemptionDetail/ns2:exemptionStartDate !=&apos;&apos;">
																<children>
																	<line>
																		<styles height="1px"/>
																	</line>
																	<tgrid>
																		<properties cellpadding="0" cellspacing="0" width="100%"/>
																		<children>
																			<tgridbody-cols>
																				<children>
																					<tgridcol>
																						<properties valign="top" width="30%"/>
																					</tgridcol>
																					<tgridcol>
																						<properties width="70%"/>
																					</tgridcol>
																				</children>
																			</tgridbody-cols>
																			<tgridbody-rows>
																				<children>
																					<tgridrow>
																						<children>
																							<tgridcell>
																								<children>
																									<template subtype="element" match="beped:exemptionType">
																										<children>
																											<template subtype="element" match="ns2:startDatePwsLabel">
																												<children>
																													<content subtype="regular"/>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																									</template>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<properties valign="top"/>
																								<children>
																									<template subtype="element" match="beped:exemptionDetail">
																										<children>
																											<template subtype="element" match="ns2:exemptionStartDate">
																												<children>
																													<content subtype="regular">
																														<format basic-type="xsd" string="DD Month YYYY" datatype="date"/>
																													</content>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																									</template>
																								</children>
																							</tgridcell>
																						</children>
																					</tgridrow>
																				</children>
																			</tgridbody-rows>
																		</children>
																	</tgrid>
																</children>
															</conditionbranch>
														</children>
													</condition>
													<condition>
														<children>
															<conditionbranch xpath="beped:exemptionDetail/ns2:exemptionReason !=&apos;&apos;">
																<children>
																	<line>
																		<styles height="1px"/>
																	</line>
																	<tgrid>
																		<properties cellpadding="0" cellspacing="0" width="100%"/>
																		<children>
																			<tgridbody-cols>
																				<children>
																					<tgridcol>
																						<properties valign="top" width="30%"/>
																					</tgridcol>
																					<tgridcol>
																						<properties width="70%"/>
																					</tgridcol>
																				</children>
																			</tgridbody-cols>
																			<tgridbody-rows>
																				<children>
																					<tgridrow>
																						<children>
																							<tgridcell>
																								<children>
																									<template subtype="element" match="beped:exemptionType">
																										<children>
																											<template subtype="element" match="ns2:frvPwsLabel">
																												<children>
																													<content subtype="regular"/>
																												</children>
																												<variables/>
																											</template>
																										</children>
																										<variables/>
																									</template>
																								</children>
																							</tgridcell>
																							<tgridcell>
																								<children>
																									<tgrid>
																										<properties cellpadding="0" cellspacing="0" width="100%"/>
																										<children>
																											<tgridbody-cols>
																												<children>
																													<tgridcol/>
																												</children>
																											</tgridbody-cols>
																											<tgridbody-rows>
																												<children>
																													<tgridrow>
																														<children>
																															<tgridcell>
																																<properties valign="top"/>
																																<children>
																																	<template subtype="element" match="beped:exemptionDetail">
																																		<children>
																																			<template subtype="element" match="ns2:exemptionReason">
																																				<children>
																																					<content subtype="regular"/>
																																				</children>
																																				<variables/>
																																			</template>
																																		</children>
																																		<variables/>
																																	</template>
																																	<paragraph/>
																																	<newline/>
																																</children>
																															</tgridcell>
																														</children>
																													</tgridrow>
																													<tgridrow>
																														<children>
																															<tgridcell>
																																<children>
																																	<template subtype="element" match="beped:exemptionDetail">
																																		<children>
																																			<template subtype="element" match="ns2:exemptionReasonAdditionalText">
																																				<children>
																																					<content subtype="regular"/>
																																				</children>
																																				<variables/>
																																			</template>
																																		</children>
																																		<variables/>
																																	</template>
																																</children>
																															</tgridcell>
																														</children>
																													</tgridrow>
																												</children>
																											</tgridbody-rows>
																										</children>
																									</tgrid>
																								</children>
																							</tgridcell>
																						</children>
																					</tgridrow>
																				</children>
																			</tgridbody-rows>
																		</children>
																	</tgrid>
																</children>
															</conditionbranch>
														</children>
													</condition>
													<line/>
													<newline/>
													<newline/>
												</children>
												<variables/>
											</template>
										</children>
										<variables/>
									</template>
								</children>
							</globaltemplate>
						</children>
					</mainparts>
					<globalparts/>
					<designfragments>
						<children>
							<globaltemplate subtype="named" match="LargeTextSpacer">
								<parameters/>
								<children>
									<paragraph>
										<children>
											<text fixtext=" "/>
										</children>
									</paragraph>
									<paragraph>
										<children>
											<text fixtext=" "/>
										</children>
									</paragraph>
									<paragraph>
										<children>
											<text fixtext=" "/>
										</children>
									</paragraph>
								</children>
							</globaltemplate>
						</children>
					</designfragments>
					<xmltables/>
					<authentic-custom-toolbar-buttons/>
				</structure>
