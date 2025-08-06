<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xml>
<Configuration>
    <Appenders>
        <File name="appender" fileName="./logs/file/sample.log" append="true">
            <PatternLayout pattern="%d %5p [%c] %m%n"/>
        </File>
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
