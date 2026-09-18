<?xml version="1.0" encoding="UTF-8"?>
<config xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.ehcache.org/v3"
    xsi:schemaLocation="http://www.ehcache.org/v3 http://www.ehcache.org/schema/ehcache-core-3.0.xsd">

    <!--
        EhCache 3.x 설정 파일
        Spring 6.2.11 + Jakarta EE 9 환경
    -->

    <!-- 디스크 저장소 설정 -->
    <persistence directory="user.dir/second" />

    <!-- 기본 캐시 템플릿 -->
    <cache-template name="defaultCache">
        <!-- 기본 캐시 만료 정책 -->
        <expiry>
            <!-- TTL (Time To Live) - 생성 후 만료 시간 -->
            <ttl unit="minutes">120</ttl>
        </expiry>

        <!-- 저장소 설정 : Heap -> OffHeap -> Disk 순으로 자동으로 overflow 처리됨-->
        <resources>
            <!-- 힙 메모리 설정 -->
            <heap unit="entries">100</heap>
            <!-- 오프힙 메모리 설정-->
            <offheap unit="MB">10</offheap>
            <!-- 디스크 메모리 설정 -->
            <disk unit="MB" persistent="true">100</disk>
        </resources>
    </cache-template>

    <!-- 사용자 캐시 설정 -->
    <cache alias="cache" uses-template="defaultCache">

        <!-- 캐시 키와 값 타입 설정 -->
        <key-type>java.lang.Object</key-type>
        <value-type>java.lang.Object</value-type>

        <expiry>
            <!-- TTI (Time To Idle) - 사용 후 만료 시간 -->
            <tti unit="seconds">360</tti>
        </expiry>
        <resources>
            <heap unit="entries">10</heap>
        </resources>
    </cache>

</config>
