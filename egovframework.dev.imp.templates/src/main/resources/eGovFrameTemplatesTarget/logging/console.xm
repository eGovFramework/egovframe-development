<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xml>
<Configuration>
    <Appenders>
        <Console name="console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d %5p [%c] %m%n" />
        </Console>
    </Appenders>
    <Loggers>
        <Logger name="egovframework" level="DEBUG" additivity="false">
            <AppenderRef ref="console" />
        </Logger>
        <Logger name="org.egovframe" level="DEBUG" additivity="false">
            <AppenderRef ref="console" />
        </Logger>
        <Logger name="org.springframework" level="DEBUG" additivity="false">
            <AppenderRef ref="console" />
        </Logger>
        <Root level="INFO">
            <AppenderRef ref="console" />
        </Root>
    </Loggers>
</Configuration>
