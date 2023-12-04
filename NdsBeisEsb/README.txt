Test failure notes - 

Conversion problem - if you get this it won't tell you what went wrong.  Find out which route it's running and
debug the Java code it calls to work out what's returning the wrong thing (usually Null or an error response)
and why.

Where a route has 

	<to uri="cxf:bean:beisGetPrsAccountExemptionsService" />
	
it's likely mocked in the test, you can debug that too.