<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!-- Method Invoking Job Detail Configuration (Spring Framework 6.x compatible) -->
	<bean id="service1" class="org.apache.commons.dbcp.AbandonedConfig">
		<property name="property1" value="7200"/>
	</bean>

	<bean id="job1"
		class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
		<property name="targetObject" ref="service1" />
		<property name="targetMethod" value="start" />
		<property name="concurrent" value="false" />
	</bean>

	<!--
		스케줄로 실행될 실제 서비스 로직인 org.apache.commons.dbcp.AbandonedConfig 클래스를 생성한 후
		그 클래스 내에서 "@Service" 등의 어노테이션을 사용하여 "service1" Bean을 등록하거나,
		위와 같이 XML에서 직접 Bean을 등록한다.
	-->

</beans>