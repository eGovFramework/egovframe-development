<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j='http://jakarta.apache.org/log4j/'>
	
	<appender name="appender" class="org.apache.log4j.rolling.RollingFileAppender">
		<rollingPolicy class="org.apache.log4j.rolling.FixedWindowRollingPolicy">
			<param name="FileNamePattern" value="./logs/rolling/rollingSample.%i.log" />
			<param name="MaxIndex" value="3" />
		</rollingPolicy>
		<triggeringPolicy class="org.apache.log4j.rolling.SizeBasedTriggeringPolicy">
			<param name="MaxFileSize" value="1000" />
		</triggeringPolicy>
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