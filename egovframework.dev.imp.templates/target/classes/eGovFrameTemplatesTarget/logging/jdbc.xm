<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j='http://jakarta.apache.org/log4j/'>

	<appender name="appender1" class="org.apache.log4j.jdbc.JDBCAppender">
		<param name="Driver" value="hsql.driver"/>
		<param name="URL" value="hsql://localhost"/>
		<param name="User" value="sa"/>
		<param name="Password" value="1"/>
		<param name="Sql" value="insert into STMR_LOG (msg) values('%d %p [%c] - &lt;%m&gt;%n')"/>
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