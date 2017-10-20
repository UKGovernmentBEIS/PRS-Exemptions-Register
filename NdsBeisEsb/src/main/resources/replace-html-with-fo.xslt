<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes=" xlink xs xsi">
	<xsl:output version="1.0" method="xml" encoding="UTF-8" indent="no" omit-xml-declaration="yes"/>
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="ul">
		<fo:list-block provisional-distance-between-starts="0.3cm" provisional-label-separation="0.15cm">
			<xsl:attribute name="space-after"><xsl:choose><xsl:when test="ancestor::ul or ancestor::ol"><xsl:text>0pt</xsl:text></xsl:when><xsl:otherwise><xsl:text>12pt</xsl:text></xsl:otherwise></xsl:choose></xsl:attribute>
			<xsl:attribute name="start-indent"><xsl:variable name="ancestors"><xsl:text>0.15</xsl:text></xsl:variable><xsl:value-of select="concat($ancestors, 'cm')"/></xsl:attribute>
			<xsl:apply-templates select="*"/>
		</fo:list-block>
	</xsl:template>

	<xsl:template match="ul/li">
		<fo:list-item>
			<fo:list-item-label end-indent="label-end()">
				<fo:block>&#x2022;</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()">
				<fo:block>
					<xsl:apply-templates select="*|text()"/>
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>

	<xsl:template match="root">
		<fo:block>
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
	</xsl:template>

	<xsl:template match="p">
		<fo:block font-size="12pt" line-height="15pt">
			<xsl:apply-templates select="*|text()"/>
		</fo:block>
	</xsl:template>

	<xsl:template match="br">
		<fo:block> </fo:block>
	</xsl:template>
</xsl:stylesheet>
