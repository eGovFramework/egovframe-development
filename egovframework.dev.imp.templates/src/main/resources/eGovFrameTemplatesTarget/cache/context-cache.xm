<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:cache="http://www.springframework.org/schema/cache"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
		http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache-4.0.xsd">
  
  	<cache:annotation-driven />

	<bean id="cacheManager" class="org.springframework.cache.ehcache.EhCacheCacheManager">
	    <property name="cacheManager">
	        <bean class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
	            <property name="configLocation" value="classpath:${templateUtil.removeSourcePath(${txtConfigLocation})}"/>
	        </bean>
	    </property>
	</bean>
	
	<!-- pom.xml에 다음 라이브러리 추가 필요
	<dependency>
	    <groupId>net.sf.ehcache</groupId>
	    <artifactId>ehcache</artifactId>
	    <version>2.10.9.2</version>
	</dependency>
	-->

</beans>