<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xml>
<Configuration>
    <Appenders>
		<JDBC name="appender1" tableName="${txtTableName}">
			<!-- DriverManager 설정 -->
            <DriverManager
                driverClassName="${txtDriverClass}"
                connectionString="hsql://localhost"
                userName="sa"
                password="1">
                <!-- 필요 시 추가 연결 속성 설정 -->
                <!-- 예: <Property name="useSSL" value="false" /> -->
            </DriverManager>
			<Column name="reg_dt" isEventTimestamp="true" />
			<Column name="level" pattern="%p" />
			<Column name="logger" pattern="%c" />
			<Column name="message" pattern="%m" />
			<Column name="exception" pattern="%ex{full}" />
			<!--
	    	<Filters>
				<ThresholdFilter level="INFO" onMatch="DENY" onMismatch="NEUTRAL"/>
				<RegexFilter regex=".*Exception.*" onMatch="NEUTRAL" onMismatch="DENY"/>
			</Filters>
			-->
		</JDBC>
        <File name="appender1" fileName="${txtLogFileName}" append="${cboAppend}">
            <PatternLayout pattern="${txtConversionPattern}"/>
        </File>
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
		