<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j='http://jakarta.apache.org/log4j/'>

	<appender name="appender1" class="org.apache.log4j.db.EgovDBAppender">
		<!-- caller_filename, caller_class, caller_method, caller_line -->		
		<param name="locationInfo" value="true" />
		<!-- Oracle 인 경우 아래를 false 로 설정 또는 아래 옵션 라인 삭제(기본 false) -->
		<param name="useSupportsGetGeneratedKeys" value="false" />
		<connectionSource class="org.apache.log4j.db.DriverManagerConnectionSource">
			<param name="driverClass" value="hsql.driver" />
			<param name="url" value="hsql://localhost/sampledb" />
			<param name="user" value="sa" />
			<param name="password" value="1" />
		</connectionSource>
	</appender>
	
	<logger name="org.springframework" additivity="false">
		<level value="DEBUG" />
		<appender-ref ref="appender1" />
	</logger>
	
	<root>
		<level value="OFF" />
		<appender-ref ref="appender1" />
	</root>
		
</log4j:configuration>		