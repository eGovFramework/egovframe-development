<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
						http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

	<bean id="userTransaction" class="com.atomikos.icatch.jta.UserTransactionImp">
		<property name="transactionTimeout" value="20"></property>
	</bean>

	<bean id="atomikosTransactionManager" class="com.atomikos.icatch.jta.UserTransactionManager"
		init-method="init" destroy-method="close">
		<property name="forceShutdown" value="false"></property>
	</bean>

	<bean id="tran1" class="org.springframework.transaction.jta.JtaTransactionManager">
		<property name="userTransaction" ref="userTransaction"></property>
		<property name="transactionManager" ref="atomikosTransactionManager"></property>
	</bean>

	<!-- AOP 설정 - 트랜잭션 관리 및 포인트컷 설정 -->
	<tx:advice id="txAdvice" transaction-manager="tran1">
		<tx:attributes>
			<tx:method name="*"
				rollback-for="Exception"
				propagation="REQUIRED"
				isolation="DEFAULT" />
		</tx:attributes>
	</tx:advice>

	<aop:config proxy-target-class="true">
		<aop:pointcut id="requiredTx" expression="execution(* egovframework.com..*Impl.*(..)) or execution(* org.egovframe.rte.fdl.excel.impl.*Impl.*(..))" />
		<aop:advisor advice-ref="txAdvice" pointcut-ref="requiredTx" />
	</aop:config>

	<tx:annotation-driven transaction-manager="tran1" proxy-target-class="true" />

</beans>
