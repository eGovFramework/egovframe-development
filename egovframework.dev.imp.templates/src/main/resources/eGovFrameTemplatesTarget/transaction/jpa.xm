<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xmlns:jpa="http://www.springframework.org/schema/data/jpa"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
						http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
						http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
						http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
						http://www.springframework.org/schema/data/jpa http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">

	<bean id="tran1" class="org.springframework.orm.jpa.JpaTransactionManager">
		<property name="entityManagerFactory" ref="factory1" />
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
		<aop:pointcut id="requiredTx" expression="execution(* egovframework.sample..*Impl.*(..))" />
		<aop:advisor advice-ref="txAdvice" pointcut-ref="requiredTx" />
	</aop:config>

	<!-- @Transactional 스캔 -->
	<tx:annotation-driven transaction-manager="tran1" proxy-target-class="true" />

	<!-- 컨테이너가 관리하는 EntityManager 생성, @PersistenceContext와 함께 사용 -->
	<bean id="factory1" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
		<property name="dataSource" ref="ds1" />
		<!-- 어노테이션 매핑정보 스캔 -->
		<property name="packagesToScan">
			<list>
				<value>egovframework.sample</value>
				<!-- 다른 패키지를 더 스캔하려면 <value> 태그를 추가 -->
			</list>
		</property>
		<!-- 구현체별 자체 기능을 표준화 -->
		<property name="jpaVendorAdapter">
			<bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
				<property name="showSql" value="true" />
				<property name="generateDdl" value="true" />
			</bean>
		</property>
		<!-- persistence.xml 설정정보와 함께 사용가능 -->
		<property name="jpaProperties">
			<props>
				<prop key="hibernate.dialect">org.hibernate.dialect.H2Dialect</prop>
				<prop key="hibernate.format_sql">true</prop>
				<prop key="hibernate.use_sql_comments">true</prop>
				<prop key="hibernate.jdbc.batch_size">5</prop>
			</props>
		</property>
	</bean>

	<!-- Spring Data JPA 리포지토리 설정 -->
	<jpa:repositories base-package="egovframework.sample.repository" />

	<!-- @PersistenceContext 스캔 및 예외변환 후처리기 -->
	<context:annotation-config />
	<bean id="persistenceExceptionTranslationPostProcessor"
		class="org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor" />

</beans>