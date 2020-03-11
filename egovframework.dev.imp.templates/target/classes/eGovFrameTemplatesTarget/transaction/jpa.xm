<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

	<bean id="tran1" class="org.springframework.orm.jpa.JpaTransactionManager">
	   <property name="entityManagerFactory" ref="factory1"/>
	</bean>
	 
	<bean id="factory1" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
	   <property name="persistenceUnitName" value="unit1"/>
	   <property name="persistenceXmlLocation" value="classpath:/xml/"/>
	   <property name="dataSource" ref="ds1"/>
	</bean>	

</beans>