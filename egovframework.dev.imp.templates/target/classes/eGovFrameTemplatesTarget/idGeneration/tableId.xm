<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd">

   <bean name="a" class="egovframework.rte.fdl.idgnr.impl.EgovTableIdGnrService" 
                           destroy-method="destroy">
      <property name="dataSource" ref="b"/>
      <property name="table"	  value="c"/>
      <property name="tableName"  value="d"/>	
      <property name="blockSize" value="e"/>
   </bean>	

</beans>   