<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

	<!-- Job Detail Bean Configuration (Spring Framework 6.x compatible) -->
	<bean id="job1" class="org.springframework.scheduling.quartz.JobDetailFactoryBean">
		<property name="jobClass" value="javax.swing.text.DefaultHighlighter$SafeDamager" /> <!-- org.springframework.scheduling.quartz.QuartzJobBean 구현 클래스 -->
		<property name="jobDataAsMap">
			<map>
				<entry key="property1" value="12000" />
			</map>
		</property>
	</bean>

</beans>	