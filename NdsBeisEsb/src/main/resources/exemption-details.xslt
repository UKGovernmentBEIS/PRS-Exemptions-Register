<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:beped="http://northgatepublicservices.com/nds/beis/printexemptiondetails/2016/11/16"
	xmlns:beis="http://northgatepublicservices.com/nds/beis/model/2016/11/16"
	xmlns:pmod="http://northgatepublicservices.com/nds/platform/model/2015/11/24">
	<!--Page layout Setting -->
	<xsl:variable name="fo:layout-master-set">
		<fo:layout-master-set>
			<fo:simple-page-master master-name="page-master"
				margin-left="0.60in" margin-right="0.60in" page-height="11.69in"
				page-width="8.27in" margin-top="0.30in" margin-bottom="0.30in">
				<fo:region-body margin-top="0.49in" margin-bottom="0.49in"
					column-count="1" column-gap="0.50in" />
			</fo:simple-page-master>
		</fo:layout-master-set>
	</xsl:variable>

	<!--Main template -->
	<xsl:template match="/">
		<fo:root>
			<xsl:copy-of select="$fo:layout-master-set" />
			<fo:page-sequence master-reference="page-master">
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-family="Helvetica">
						<xsl:for-each select="beped:PrintExemptionDetailsNdsRequest">
							<fo:block>
								<fo:leader leader-pattern="space" />
							</fo:block>
							<xsl:call-template name="Title">
								<xsl:with-param name="title" select="'Exemption summary'" />
							</xsl:call-template>
							<fo:block />
							<xsl:call-template name="ThickLineDivider" />
							<fo:table table-layout="fixed" width="100%"
								border-spacing="0">
								<fo:table-column column-width="30%" />
								<fo:table-column column-width="70%" />
								<fo:table-body>
									<xsl:call-template name="TableRowValue">
										<xsl:with-param name="label" select="'Exemption reference'" />
										<xsl:with-param name="node"
											select="beped:exemptionDetail/beis:referenceId" />
									</xsl:call-template>
									<xsl:if
										test="string-length(beped:exemptionDetail/beis:landlordDetails/beis:personNameDetail) > 0">										
												<xsl:call-template name="TableRowValue">
													<xsl:with-param name="label" select="'Landlord Name'" />
													<xsl:with-param name="node"
														select="beped:exemptionDetail/beis:landlordDetails/beis:personNameDetail/beis:firstname" />
													<xsl:with-param name="node2"
														select="beped:exemptionDetail/beis:landlordDetails/beis:personNameDetail/beis:surname" />
												</xsl:call-template>											
									</xsl:if>
									<xsl:if
										test="string-length(beped:exemptionDetail/beis:landlordDetails/beis:organisationNameDetail/beis:orgName) > 0">										
												<xsl:call-template name="TableRowValue">
													<xsl:with-param name="label" select="'Landlord Name'" />
													<xsl:with-param name="node"
														select="beped:exemptionDetail/beis:landlordDetails/beis:organisationNameDetail/beis:orgName" />
												</xsl:call-template>											
									</xsl:if>											
									<xsl:if
										test="string-length(beped:exemptionDetail/beis:landlordDetails/beis:emailAddress) > 0">										
												<xsl:call-template name="TableRowValue">
													<xsl:with-param name="label" select="'Landlord Email Address'" />
													<xsl:with-param name="node"
														select="beped:exemptionDetail/beis:landlordDetails/beis:emailAddress" />
												</xsl:call-template>											
									</xsl:if>
									<xsl:if
										test="string-length(beped:exemptionDetail/beis:landlordDetails/beis:phoneNumber) > 0">										>
												<xsl:call-template name="TableRowValue">
													<xsl:with-param name="label" select="'Landlord Phone Number'" />
													<xsl:with-param name="node"
														select="beped:exemptionDetail/beis:landlordDetails/beis:phoneNumber" />
												</xsl:call-template>											
									</xsl:if>									
									<xsl:call-template name="TableRowValue">
										<xsl:with-param name="label" select="'Address of exempt property'" />
										<xsl:with-param name="node"
											select="beped:exemptionDetail/beis:epc/beis:propertyAddress/beis:singleLineAddressPostcode" />
									</xsl:call-template>
								
									<xsl:call-template name="TableRowValue">
										<xsl:with-param name="label" select="'Exemption type'" />
										<xsl:with-param name="node"
											select="beped:exemptionType/beis:pwsDescription" />
									</xsl:call-template>
								
									<xsl:call-template name="TableRowValue">
										<xsl:with-param name="label" select="'Evidence required'" />
										<xsl:with-param name="node" select="beped:exemptionType/beis:pwsText" />
									</xsl:call-template>

									<xsl:call-template name="FileUploadTableRow">
										<xsl:with-param name="label" select="'EPC provided'" />
										<xsl:with-param name="node"
											select="beped:exemptionDetail/beis:epc/beis:files/pmod:resources" />
									</xsl:call-template>

								</fo:table-body>
							</fo:table>
							<xsl:call-template name="ThickLineDivider" />
							<xsl:call-template name="Title">
								<xsl:with-param name="title" select="'Supporting information'" />
							</xsl:call-template>
							<fo:block />
							<xsl:call-template name="ThickLineDivider" />
							<xsl:if
								test="string-length(beped:exemptionDetail/beis:epcEvidenceFiles/pmod:resources) > 0">
								<fo:table table-layout="fixed" width="100%"
									border-spacing="0">
									<fo:table-column column-width="30%" />
									<fo:table-column column-width="70%" />
									<fo:table-body>
										<xsl:call-template name="FileUploadTableRow">
											<xsl:with-param name="label"
												select="beped:exemptionType/beis:documentsPwsLabel" />
											<xsl:with-param name="node"
												select="beped:exemptionDetail/beis:epcEvidenceFiles/pmod:resources" />
										</xsl:call-template>
									</fo:table-body>
								</fo:table>
								<xsl:call-template name="ThinLineDivider" />
							</xsl:if>
							<xsl:if
								test="string-length(beped:exemptionDetail/beis:exemptionTextFile/pmod:resources) >0  or string-length(beped:exemptionDetail/beis:exemptionText) >0">
								<fo:table table-layout="fixed" width="100%"
									border-spacing="0">
									<fo:table-column column-width="30%" />
									<fo:table-column column-width="70%" />
									<fo:table-body>
										<xsl:call-template name="FileUploadTableRow">
											<xsl:with-param name="label"
												select="beped:exemptionType/beis:textPwsLabel" />
											<xsl:with-param name="node"
												select="beped:exemptionDetail/beis:exemptionTextFile/pmod:resources" />
											<xsl:with-param name="node2"
												select="beped:exemptionDetail/beis:exemptionText" />
										</xsl:call-template>
									</fo:table-body>
								</fo:table>
								<xsl:call-template name="ThinLineDivider" />
							</xsl:if>
							<xsl:if
								test="string-length(beped:exemptionDetail/beis:exemptionStartDate) > 0">
								<fo:table table-layout="fixed" width="100%"
									border-spacing="0">
									<fo:table-column column-width="30%" />
									<fo:table-column column-width="70%" />
									<fo:table-body>
										<xsl:variable name="startDateLabel"
											select="beped:exemptionType/beis:startDatePwsLabel" />
										<xsl:for-each select="beped:exemptionDetail/beis:exemptionStartDate">
											<xsl:variable name="startDate">
												<xsl:call-template name="NdsDateFormat">
													<xsl:with-param name="date" select="string(string(.))" />
												</xsl:call-template>
											</xsl:variable>
											<xsl:call-template name="TableRowValue">
												<xsl:with-param name="label" select="$startDateLabel" />
												<xsl:with-param name="node" select="$startDate" />
											</xsl:call-template>
										</xsl:for-each>
									</fo:table-body>
								</fo:table>
							</xsl:if>
							<xsl:if
								test="string-length(beped:exemptionDetail/beis:exemptionReason) >0">
								<fo:table table-layout="fixed" width="100%"
									border-spacing="0">
									<fo:table-column column-width="30%" />
									<fo:table-column column-width="70%" />
									<fo:table-body>
										<xsl:call-template name="TableRowValue">
											<xsl:with-param name="label"
												select="beped:exemptionType/beis:frvPwsLabel" />
											<xsl:with-param name="node"
												select="beped:exemptionDetail/beis:exemptionReason" />
											<xsl:with-param name="node2"
												select="beped:exemptionDetail/beis:exemptionReasonAdditionalText" />
										</xsl:call-template>
									</fo:table-body>
								</fo:table>
							</xsl:if>
						</xsl:for-each>
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
	<!-- Template For Uploaded File Display -->
	<xsl:template name="FileUploadTableRow">
		<xsl:param name="label" />
		<xsl:param name="node" />
		<xsl:param name="node2" />
		<xsl:variable name="filesLabel" select="$label" />
		<fo:table-row>
			<fo:table-cell padding="0" display-align="before">
				<fo:block-container overflow="hidden">
					<fo:block text-align="left">
						<fo:inline>
							<fo:inline font-size="12pt">
								<xsl:value-of select="$filesLabel" />
							</fo:inline>
						</fo:inline>
					</fo:block>
				</fo:block-container>
			</fo:table-cell>
			<fo:table-cell padding="0" display-align="before">
				<fo:block-container overflow="hidden">
					<fo:block text-align="left">
						<fo:inline>
							<fo:table table-layout="fixed" width="100%"
								border-spacing="2pt">
								<fo:table-column column-width="100%" />
								<fo:table-body start-indent="0pt">
									<xsl:for-each select="$node">
										<fo:table-row>
											<fo:table-cell padding="2pt" display-align="center">
													<fo:block-container overflow="hidden">
														<fo:block text-align="left">
															<fo:inline>
																<xsl:text>File uploaded: </xsl:text>
															</fo:inline>
															<fo:inline>
																<xsl:value-of select="pmod:fileName" />
															</fo:inline>
															<fo:inline-container>
																<fo:block>
																	<xsl:text>&#x200B;</xsl:text>
																</fo:block>
															</fo:inline-container>
															<fo:block margin-right="100% - 100%"
																space-before="0" space-after="0" margin="0pt" />
															<fo:block>
																<fo:leader leader-pattern="space" />
															</fo:block>
															<fo:inline>
																<xsl:value-of select="pmod:description" />
															</fo:inline>
														</fo:block>
														<xsl:if test="position() != last()">
															<xsl:call-template name="ThinLineDivider" />
														</xsl:if>
													</fo:block-container>
											</fo:table-cell>
										</fo:table-row>
									</xsl:for-each>
									<xsl:if test="$node2 !=&apos;&apos;">
										<fo:table-row>
											<fo:table-cell padding="0">
												<xsl:call-template name="ThinLineDivider" />
											</fo:table-cell>
										</fo:table-row>
										<fo:table-row>
											<xsl:call-template name="TableCell">
												<xsl:with-param name="label" select="$node2" />
												<xsl:with-param name="displayalign" select="'center'" />
												<xsl:with-param name="padding" select="2" />
												<xsl:with-param name="linefeedtreatment"
													select="'preserve'" />
											</xsl:call-template>
										</fo:table-row>
									</xsl:if>
								</fo:table-body>
							</fo:table>
						</fo:inline>
					</fo:block>
				</fo:block-container>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Template For Each row in table except for File Upload Display -->
	<xsl:template name="TableRowValue">
		<xsl:param name="label" />
		<xsl:param name="node" />
		<xsl:param name="node2" />
		<fo:table-row>
			<xsl:call-template name="TableCell">
				<xsl:with-param name="label" select="$label" />
			</xsl:call-template>

			<fo:table-cell padding="0" display-align="before">
				<fo:table table-layout="fixed" width="100%" border-spacing="0">
					<fo:table-column column-width="proportional-column-width(1)" />
					<fo:table-body>
						<fo:table-row>
							<xsl:call-template name="TableCell">
								<xsl:with-param name="label" select="$node" />
							</xsl:call-template>
						</fo:table-row>
						<xsl:if test="$node2 !=&apos;&apos;">
							<fo:table-row>
								<xsl:call-template name="TableCell">
									<xsl:with-param name="label" select="$node2" />
									<xsl:with-param name="padding" select="2" />
									<xsl:with-param name="displayalign" select="'center'" />
									<xsl:with-param name="linefeedtreatment" select="'preserve'" />
								</xsl:call-template>
							</fo:table-row>
						</xsl:if>
					</fo:table-body>
				</fo:table>
			</fo:table-cell>
		</fo:table-row>
		<xsl:call-template name="RowDivider" />
	</xsl:template>

	<xsl:template name="ThickLineDivider">
		<fo:block text-align="center">
			<fo:leader leader-pattern="rule" rule-thickness="2"
				leader-length="100%" />
		</fo:block>
		<fo:block>
			<fo:leader leader-pattern="space" />
		</fo:block>
	</xsl:template>

	<xsl:template name="ThinLineDivider">
		<fo:block text-align="center">
			<fo:leader rule-thickness="0.01in" leader-pattern="rule"
				leader-length="100%" />
		</fo:block>
		<fo:block>
			<fo:leader leader-pattern="space" />
		</fo:block>
	</xsl:template>


	<xsl:template name="RowDivider">
		<fo:table-row>
			<fo:table-cell padding="0" number-columns-spanned="2">
				<fo:block text-align="center">
					<fo:leader rule-thickness="0.01in" leader-pattern="rule"
						leader-length="100%" />
				</fo:block>
				<fo:block>
					<xsl:text>&#x200B;</xsl:text>
				</fo:block>
			</fo:table-cell>
		</fo:table-row>
	</xsl:template>

	<!-- Template to display date in date- month- year format e.g. 1 June 2007 -->
	<xsl:template name="NdsDateFormat">
		<xsl:param name="date" />
		<xsl:value-of select="format-number(number(substring($date, 9, 2)), '00')" />
		<xsl:text> </xsl:text>
		<xsl:variable name="nMonth">
			<xsl:call-template name="DateToMonthNum">
				<xsl:with-param name="date" select="$date" />
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$nMonth = 1">
				January
			</xsl:when>
			<xsl:when test="$nMonth = 2">
				February
			</xsl:when>
			<xsl:when test="$nMonth = 3">
				March
			</xsl:when>
			<xsl:when test="$nMonth = 4">
				April
			</xsl:when>
			<xsl:when test="$nMonth = 5">
				May
			</xsl:when>
			<xsl:when test="$nMonth = 6">
				June
			</xsl:when>
			<xsl:when test="$nMonth = 7">
				July
			</xsl:when>
			<xsl:when test="$nMonth = 8">
				August
			</xsl:when>
			<xsl:when test="$nMonth = 9">
				September
			</xsl:when>
			<xsl:when test="$nMonth = 10">
				October
			</xsl:when>
			<xsl:when test="$nMonth = 11">
				November
			</xsl:when>
			<xsl:otherwise>
				December
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text> </xsl:text>
		<xsl:value-of select="format-number(number(substring($date, 1, 4)), '0000')" />
	</xsl:template>

	<xsl:template name="DateToMonthNum">
		<xsl:param name="date" />
		<xsl:choose>
			<xsl:when test="starts-with($date, '--')">
				<xsl:value-of select="number(substring($date, 3, 2))" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="number(substring($date, 6, 2))" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="Title">
		<xsl:param name="title" />
		<fo:inline font-family="Helvetica" font-size="18pt">
			<xsl:value-of select="$title" />
		</fo:inline>
	</xsl:template>

	<xsl:template name="TableCell">
		<xsl:param name="displayalign" select="'before'" />
		<xsl:param name="linefeedtreatment" select="'ignore'" />
		<xsl:param name="label" />
		<xsl:param name="padding" select="0" />
		<fo:table-cell>
			<xsl:attribute name="display-align">
				<xsl:value-of select="$displayalign" />
			</xsl:attribute>
			<xsl:attribute name="padding">
				<xsl:value-of select="$padding" />
			</xsl:attribute>
			<fo:block-container overflow="hidden">
				<fo:block text-align="left">
					<xsl:attribute name="linefeed-treatment">
					<xsl:value-of select="$linefeedtreatment" />
				</xsl:attribute>
					<fo:inline>
						<xsl:value-of select="$label" />
					</fo:inline>
				</fo:block>
			</fo:block-container>
		</fo:table-cell>
	</xsl:template>

</xsl:stylesheet>