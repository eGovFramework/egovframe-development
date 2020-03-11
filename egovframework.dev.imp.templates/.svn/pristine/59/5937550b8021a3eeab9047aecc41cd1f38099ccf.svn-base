<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

	<bean id="service1" class="org.apache.commons.dbcp.AbandonedConfig">
		<property name="property1" value="7200"/>
	</bean>
	 
	<bean id="job1"
		class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
		<property name="targetObject" ref="service1" />
		<property name="targetMethod" value="start" />
		<property name="concurrent" value="false" />
	</bean>

</beans>