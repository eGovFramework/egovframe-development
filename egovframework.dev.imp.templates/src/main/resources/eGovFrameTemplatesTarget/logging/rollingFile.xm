<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xml>
<Configuration>
    <Appenders>
        <RollingFile name="appender" fileName="${txtLogFileName}" filePattern="./logs/rolling/rollingSample.%i.log">
            <PatternLayout pattern="%d %5p [%c] %m%n"/>
            <Policies>
                <SizeBasedTriggeringPolicy size="1000"/>
            </Policies>
            <DefaultRolloverStrategy max="3"/>
        </RollingFile>
    </Appenders>
    <Loggers>
        <Logger name="egovframework" level="DEBUG" additivity="false">
            <AppenderRef ref="appender" />
        </Logger>
        <Logger name="org.egovframe" level="DEBUG" additivity="false">
            <AppenderRef ref="appender" />
        </Logger>
        <Logger name="org.springframework" level="DEBUG" additivity="false">
            <AppenderRef ref="appender" />
        </Logger>
        <Root level="INFO">
            <AppenderRef ref="appender" />
        </Root>
    </Loggers>
</Configuration>
