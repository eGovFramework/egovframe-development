<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xml>
<Configuration>
    <Appenders>
		<RollingFile name="appender1" fileName="./logs/time/timeBasedRollingSample.log" filePattern="./logs/time/timeBasedRollingSample.%d{yyyy-MM-dd_HH-mm}.log">
            <PatternLayout pattern="%d %5p [%c] %m%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy interval="1" modulate="true"/>
            </Policies>
        </RollingFile>
    </Appenders>
    <Loggers>
        <Logger name="egovframework" level="DEBUG" additivity="false">
            <AppenderRef ref="appender1" />
        </Logger>
        <Logger name="org.egovframe" level="DEBUG" additivity="false">
            <AppenderRef ref="appender1" />
        </Logger>
        <Logger name="org.springframework" level="DEBUG" additivity="false">
            <AppenderRef ref="appender1" />
        </Logger>
        <Root level="INFO">
            <AppenderRef ref="appender1" />
        </Root>
    </Loggers>
</Configuration>
