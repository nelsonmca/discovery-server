# discovery-server

This example uses Eureka Netflix server through spring cloud library.


<code>@EnableEurekaServer</code>
Does the magic of initializing and starting the discovery server


<code>application.properties</code>
set the properties 
<code>eureka.client.register-with-eureka=false</code> (so it do not try to register it. This is required if the cluster of discovery server to be created.)
