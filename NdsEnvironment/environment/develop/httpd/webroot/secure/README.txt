This directory is here so we can test Shibboleth-SP.
ie pages in this directory <b>might</b> be secured by Shibboleth-SP <b>IF</b> the appropriate Apache HTTPD directives are set.
<br/>
To apply Shibboleth directives, see httpd-shibboleth.conf.
<p>
	Example directive :
	<br />
		<pre>
			&lt;Location /NdsBbisUi&gt;
			  AuthType shibboleth
			  ShibRequestSetting requireSession 1
			  require shib-session
			&lt;/Location&gt;
		</pre>
</p>

<a href="https://docs.google.com/document/d/19G80KtFDXo2ag4ilexrVtfyflgyWqWjWksY6jY4CxpE/edit#heading=h.ibv02dwluaf9">
	See also our Installing Shibboleth guide.
</a>