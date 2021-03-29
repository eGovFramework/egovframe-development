<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j='http://jakarta.apache.org/log4j/'>

	<appender name="appender" class="org.apache.log4j.FileAppender">
		<param name="File" value="./logs/file/sample.log" />
		<param name="Append" value="true" />
		<layout class="org.apache.log4j.PatternLayout">
			<param name="ConversionPattern" value="%d %5p [%c] %m%n" />
		</layout>
	</appender> 

	<logger name="org.springframework" additivity="false">
		<level value="DEBUG" />
		<appender-ref ref="appender" />
	</logger>
	
	<root>
		<level value="OFF" />
		<appender-ref ref="appender" />
	</root>
		
</log4j:configuration>	