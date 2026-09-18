<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!-- Simple Trigger Configuration (Spring Framework 6.x compatible) -->
	<bean id="trigger" class="org.springframework.scheduling.quartz.SimpleTriggerFactoryBean">
		<property name="jobDetail" ref="job1" />
		<property name="startDelay" value="0" /> <!-- 0ms 후에 시작 -->
		<property name="repeatInterval" value="10000" /> <!-- 매 10000ms마다 실행 -->
	</bean>

</beans>