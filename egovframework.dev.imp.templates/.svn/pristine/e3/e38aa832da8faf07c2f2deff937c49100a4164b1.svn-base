<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:jee="http://www.springframework.org/schema/jee"
	xmlns:util="http://www.springframework.org/schema/util"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd
	http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-2.5.xsd
	http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-2.5.xsd">
	
   <tx:jta-transaction-manager/>
   <jee:jndi-lookup id="ds1" jndi-name="jndi1" resource-ref="true">
      <jee:environment>
         java.naming.factory.initial=factory
         java.naming.provider.url=http://localhost/
      </jee:environment>
   </jee:jndi-lookup>

</beans>
