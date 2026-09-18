<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean name="service" class="org.egovframe.rte.fdl.property.impl.EgovPropertyServiceImpl" destroy-method="destroy">

        <property name="properties">
            <map>
                <entry key="key1" value="10000"/>
            </map>
        </property>


    </bean>

</beans>    