<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
						http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

	<bean id="tran1" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="ds1"/>
	</bean>

	<bean id="transactionTemplate" class="org.springframework.transaction.support.TransactionTemplate">
		<property name="transactionManager" ref="tran1"/>
	</bean>

	<aop:config proxy-target-class="true">
		<aop:pointcut id="requiredTx" expression="execution(* egovframework.sample..*Impl.*(..))"/>
		<aop:advisor advice-ref="txAdvice" pointcut-ref="requiredTx" />
	</aop:config>

	<!-- 또는 pointcut을 bean으로 등록하지 않는다면
	<aop:config proxy-target-class="true">
		<aop:advisor advice-ref="txAdvice" pointcut="execution(* egovframework.sample..*Impl.*(..))" />
	</aop:config>
	-->

	<tx:advice id="txAdvice" transaction-manager="tran1">
		<tx:attributes>
			<tx:method name="*"
				rollback-for="Exception"
				timeout="20"
				propagation="REQUIRED"
				isolation="DEFAULT" />
		</tx:attributes>
	</tx:advice>

	<!-- 어노테이션(@Transactional) 기반 트랜잭션 관리 -->
	<tx:annotation-driven transaction-manager="tran1" proxy-target-class="true" />

	<!--
		transaction-manager는 다음 가이드를 참조한다.
		https://egovframework.github.io/egovframe-docs/egovframe-runtime/persistence-layer/transaction/
	-->

</beans>