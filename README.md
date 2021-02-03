# discovery-server

This example uses Eureka Netflix server through spring cloud library. Register services in eurekha so others can find the services to collaborate


<code>@EnableEurekaServer</code>
 Does the magic of initializing and starting the discovery server

<br/>
<code>application.properties</code>
 set the properties 
<br/>
<code>eureka.client.register-with-eureka=false</code>  (so it do not try to register it. This is   required if the cluster of discovery server to be created.)


URL : http://localhost:8761

