<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">
    
	<bean id="C3P0_Datasource" class="com.mchange.v2.c3p0.ComboPooledDataSource"
		destroy-method="close">
		<property name="driverClass" value="org.postgresql.Driver" />
		<property name="jdbcUrl" value="jdbc:postgresql://localhost/testdb" />
		<property name="user" value="swaldman" />
		<property name="password" value="test-password" />
		<property name="initialPoolSize" value="3" />
		<property name="minPoolSize" value="3" />
		<property name="maxPoolSize" value="50" />
		<!-- <property name="timeout" value="0" /> -->   <!-- 0 means: no timeout -->
		<property name="idleConnectionTestPeriod" value="200" />
		<property name="acquireIncrement" value="1" />
		<property name="maxStatements" value="0" />  <!-- 0 means: statement caching is turned off.  -->
		<!-- c3p0 is very asynchronous. Slow JDBC operations are generally performed 
	                by helper threads that don't hold contended locks. 
			Spreading these operations over multiple threads can significantly improve performance 
			by allowing multiple operations to be performed simultaneously -->
		<property name="numHelperThreads" value="3" />  <!-- 3 is default -->
	</bean>
</beans>