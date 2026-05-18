# 표준프레임워크 개발환경 5.0.1 (egovframe-development)

![Eclipse](https://img.shields.io/badge/Eclipse-FE7A16.svg?style=for-the-badge&logo=Eclipse&logoColor=white)
![java](https://img.shields.io/badge/java-007396?style=for-the-badge&logo=JAVA&logoColor=white)
![spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)



## 개발환경 소개

### 주요 특징
#### 1. 개발환경 구축의 자동화 및 최적화
- 라이트버전의 개발환경 제공으로 개발환경을 경량화
- 필요 기능만을 선택적으로 설치함으로써 최적화된 개발환경을 구성하는 기능을 제공
- 한번의 설치로 쉽고 빠르게 OS별 서버 환경을 구성하는 기능을 제공   

<img width="710" alt="image" src="https://user-images.githubusercontent.com/68622744/230293274-5457abc5-e67f-44a0-a7c8-b718fc1ec20b.png">   

#### 2. 편리한 조립식 구현환경
- 위저드 방식을 이용하여 한번의 클릭으로 공통컴포넌트를 선택 및 설치하는 기능을 제공
- 공통컴포넌트 설정부터 테이블 생성까지 바로 실행 가능한 코드를 생성하는 기능을 제공
- 홈페이지, 포털사이트, 내부 업무 시스템 등 유형별 사이트 템플릿 프로젝트를 제공

<img width="714" alt="image" src="https://user-images.githubusercontent.com/68622744/230293423-48f3e5e4-fe23-45a3-b89b-cd41f55a6e2c.png">   


#### 3. 모바일웹 구현 기능 제공
- 설치와 동시에 바로 적용 가능한 모바일에 최적화된 표준 소스코드를 제공
- 모바일 템플릿 및 공통컴포넌트 적용을 위한 위저드 방식의 조립식 구현환경을 제공
- 웹검증도구를 개발환경에 내장하여 모바일웹을 포함한 웹 호환성을 개발과 동시에 검증하는 기능을 제공

<img width="705" alt="image" src="https://user-images.githubusercontent.com/68622744/230293549-0e93c427-98cb-4f5f-9412-68fe89fa585e.png">   


#### 4. 배치작업 구현기능 제공
- 배치템플릿 적용을 위한 위저드 방식의 조립식 구현환경을 제공
- 배치작업에 대한 설정만으로 파일을생성할 수 있는 배치작업 및 배치 실행파일 생성도구를 제공
- 배치작업 테스트 도구를 개발환경에 포함하여 기 개발된 배치작업을 검증하는 기능을 제공

<img width="706" alt="image" src="https://user-images.githubusercontent.com/68622744/230293694-e1f97342-f8a4-4db0-b722-050e3cdcfec4.png">   


### 구성 요소

- `구현도구`	 개발자의 코드 작성을 지원하는 도구로 IDE, Mobile IDE, Common Component, Mobile Common Component, Editor, Debug, Batch IDE로 구성  
- `테스트도구` 개발자가 작성된 코드를 테스트 하기 위한 도구로 Unit Test, Test Automation, Test Coverage, Test Reporting, Batch Job Test 으로 구성  
- `빌드도구`	개발자가 작성된 코드를 컴파일, 빌드, 배포하기 위한 도구로 Build와 Deploy  
- `형상관리도구`	형상요소에 대한 식별 및 등록, 이력관리를 지원하는 Configuration Management와 이슈를 등록하고 검색할 수 있는 ChangeManagement로 구성   


<img width="709" alt="image" src="https://user-images.githubusercontent.com/68622744/230294638-029c75d1-afb9-420f-8716-54ce9713fb9d.png">

## 개발환경 RCP 구성 (5.0.1)
### RCP 및 JEE 구성, 기타 준비물
- Windows, Linux
     - JDK 21
     - Eclipse IDE for RCP and RAP Developers (2025-03)
          - Windows : x86_64
          - Linux : x86_64
     - Eclipse IDE for Enterprise Java and Web Developers (2025-03)
          - Windows : x86_64
          - Linux : x86_64
     - Tomcat 10.0

- macOS AArch64, macOS x86_64
     - JDK 21
     - Eclipse IDE for RCP and RAP Developers (2025-12)
          - macOS AArch64 : AArch64
          - macOS x86_64 : x86_64
     - Eclipse IDE for Enterprise Java and Web Developers (2025-12)
          - macOS AArch64 : AArch64
          - macOS x86_64 : x86_64
     - Tomcat 10.0

### (테스트용) 이클립스 JEE 개발환경 구성

표준프레임워크 플러그인 (eGovFrame Plug-ins)의 테스트 환경 구성하는 방법을 소개한다.

#### 1. 이클립스 JEE 다운로드 
- Windows, Linux : https://www.eclipse.org/downloads/packages/release/2025-03/r
- macOS : https://www.eclipse.org/downloads/packages/release/2025-12/r

#### 2. JEE 추가 Plug-ins 프로그램 설치
Eclipse를 키고 메뉴에 Help > Install New Software... 클릭하여 설치한다.

##### Windows, Linux

| 소프트웨어 | 버전 | 자동설치/업데이트주소 | 필수여부 |
| ------- | --- | --------------- | ------ |
| Eclipse | 4.35 | https://www.eclipse.org/downloads/packages/release/2025-03/r <br> → Eclipse IDE for Enterprise Java and Web Developers 다운로드 | 필수 |
| Spring Tools | 4.31.0 | https://cdn.spring.io/spring-tools/release/TOOLS/sts4/update/e4.35/ <br> → **Spring Tools** : Spring Boot Language Server Feature, Spring IDE Boot Microservices Dash, Spring Tool Suite 4 main Feature <br> → **Spring Tools - Developer Resources** : Spring Boot Language Server Feature Developer Resources, Spring IDE Boot Microservices Dash Developer Resources, Spring Tool Suite 4 main Feature Developer Resources | 필수 |
| UML2 Extension | 5.5.3 | https://download.eclipse.org/releases/2025-03/ <br> → **Modeling** : UML2 Extender SDK | 필수 |
| Subversive SVN Team Provider | 5.1.0 | https://download.eclipse.org/technology/subversive/updates/release/5.1.0/ <br> → **Eclipse Subversive SVN Team Provider** : Eclipse Subversive SVN Team Provider | 필수 |
| Subversive SVN Connectors | 1.0.1 | https://arsysop.github.io/svn/release/ <br> → **Subversive SVN Connectors** : ArSysOp SVN Connectors with SVNKit 1.10 | 필수 |
| MyBatipse | 1.3.0 | https://harawata.github.io/eclipse-site/ <br> → **MyBatipse** : MyBatipse | 선택 |
| PMD | 7.15.0 | https://pmd.github.io/pmd-eclipse-plugin-p2-site/ <br> → **PMD for Eclipse** : PMD Plug-in | 선택 |
| Properties Editor | 6.0.5 | http://propedit.sourceforge.jp/eclipse/updates/ 또는 http://osdn.net/projects/propedit/storage/eclipse/updates <br> → **PropertiesEditor** : Properties Editor v6.0.5, PropertiesEditor_Asian_NLS v6.0.0 <br> * 위 url로 연결이 안 되는 경우가 잦음. 이 경우 다음 url로 설치 : https://maven.egovframe.go.kr/eclipse-plugin/properties-editor/6.0.5/ | 선택 |
| SpotBugs | 4.9.3 | https://spotbugs.github.io/eclipse/ <br> → **SpotBugs** : SpotBugs | 선택 |
| Lombok | 1.18.38 | https://projectlombok.org/p2 <br>  → **Lombok** : Lombok | 선택 |
| Grep Console | 3.7.0 | http://eclipse.schedenig.name <br> → **Grep Console** : Grep Console <br> * 위 url로 연결이 안 되는 경우가 잦음. 이 경우 다음 url로 설치 : https://maven.egovframe.go.kr/eclipse-plugin/grep-console/3.7.0/ | 선택 |
| Eclipse Docker Tooling | 5.19.0 | https://download.eclipse.org/linuxtools/update-docker-5.19.0/ <br> → **Linux Tools** : Docker Tooling | 선택 |
| Amateras Modeler | 2.2.0 | https://takezoe.github.io/amateras-update-site/ <br> → **Amateras Modeler** : Amateras Modeler <br> * 설치 후 eclipse.ini 에 –add-opens=java.desktop/java.beans=ALL-UNNAMED 추가 | 필수 |

##### macOS

| 소프트웨어 | 버전 | 자동설치/업데이트주소 | 필수여부 |
| ------- | --- | --------------- | ------ |
| Eclipse | 4.38 | https://www.eclipse.org/downloads/packages/release/2025-12/r <br> → Eclipse IDE for Enterprise Java and Web Developers 다운로드 | 필수 |
| Spring Tools | 5.0.1 | https://cdn.spring.io/spring-tools/release/update/e4.38/ <br> → **Spring Tools** : Spring Boot Language Server Feature, Spring IDE Boot Microservices Dash, Spring Tool Suite 4 main Feature <br> → **Spring Tools - Developer Resources** : Spring Boot Language Server Feature Developer Resources, Spring IDE Boot Microservices Dash Developer Resources, Spring Tool Suite 4 main Feature Developer Resources | 필수 |
| UML2 Extension | 5.5.3 | https://download.eclipse.org/releases/2025-12/ <br> → **Modeling** : UML2 Extender SDK | 필수 |
| Subversive SVN Team Provider | 5.1.0 | https://download.eclipse.org/technology/subversive/updates/release/5.1.0/ <br> → **Eclipse Subversive SVN Team Provider** : Eclipse Subversive SVN Team Provider | 필수 |
| Subversive SVN Connectors | 1.0.1 | https://arsysop.github.io/svn/release/ <br> → **Subversive SVN Connectors** : ArSysOp SVN Connectors with SVNKit 1.10 | 필수 |
| MyBatipse | 1.3.0 | https://harawata.github.io/eclipse-site/ <br> → **MyBatipse** : MyBatipse | 선택 |
| PMD | 7.15.0 | https://pmd.github.io/pmd-eclipse-plugin-p2-site/ <br> → **PMD for Eclipse** : PMD Plug-in | 선택 |
| Properties Editor | 6.0.5 | http://propedit.sourceforge.jp/eclipse/updates/ 또는 http://osdn.net/projects/propedit/storage/eclipse/updates <br> → **PropertiesEditor** : Properties Editor v6.0.5, PropertiesEditor_Asian_NLS v6.0.0 <br> * 위 url로 연결이 안 되는 경우가 잦음. 이 경우 다음 url로 설치 : https://maven.egovframe.go.kr/eclipse-plugin/properties-editor/6.0.5/ | 선택 |
| SpotBugs | 4.9.3 | https://spotbugs.github.io/eclipse/ <br> → **SpotBugs** : SpotBugs | 선택 |
| Lombok | 1.18.38 | https://projectlombok.org/p2 <br>  → **Lombok** : Lombok | 선택 |
| Grep Console | 3.7.0 | http://eclipse.schedenig.name <br> → **Grep Console** : Grep Console <br> * 위 url로 연결이 안 되는 경우가 잦음. 이 경우 다음 url로 설치 : https://maven.egovframe.go.kr/eclipse-plugin/grep-console/3.7.0/ | 선택 |
| Eclipse Docker Tooling | 5.21.0 | https://download.eclipse.org/linuxtools/update-docker-5.21.0/ <br> → **Linux Tools** : Docker Tooling | 선택 |
| Amateras Modeler | 2.2.0 | https://takezoe.github.io/amateras-update-site/ <br> → **Amateras Modeler** : Amateras Modeler <br> * 설치 후 eclipse.ini 에 –add-opens=java.desktop/java.beans=ALL-UNNAMED 추가 | 필수 |

※ 개발환경의 플러그인 설치 항목 (필수/옵션) 을 모두 설치한다. 단, **eGovFrame Plug-ins만 설치하지 않는다.**  
※ 설치가이드 플러그인 [자동설치/업데이트주소]는 해당 플러그인을 공식적으로 받을 수 있는 저작자의 배포 주소이다. 주소가 변경되거나 상황에 따라서 다운로드가 안 될 수 있다.



### (플러그인 개발용) 이클립스 RCP 환경 구축

#### 1. 이클립스 RCP 다운로드 
- Windows, Linux : https://www.eclipse.org/downloads/packages/release/2025-03/r
- macOS : https://www.eclipse.org/downloads/packages/release/2025-12/r

#### 2. RCP 추가 Plug-ins 프로그램 설치
Properties Editor 설치 : 위의 [ JEE 추가 Plug-ins 프로그램 설치 표 ] 를 참고하여 Properties Editor 를 설치한다.

### 개발환경 Plug-ins 프로젝트 설정

#### 1. egovframe-development import
다운로드 받은 개발환경 소스를 Eclipse 메뉴에서 File>Import… 를 클릭하여 프로젝트를 가져온다.

#### 2. Target 설정

##### 2-1. ‘egovframework.target’ 프로젝트에서 egovframework.target.target 파일 열기  

##### 2-2. 이전 이클립스 JEE 삭제    
(예 : C:\EGOV_DEV\eclipse-ide-2024-03-x64-RCP-TEST\eclipse ← 삭제)    
![이전 이클립스 JEE 삭제](https://user-images.githubusercontent.com/68622744/230328765-15fa9e76-e3f9-4c9d-9f91-30858dafab95.png)  

##### 2-3. ‘Add…’ 버튼을 클릭 후 > ‘Directory’ 선택 후 > Location에 위에서 설치한 이클립스 JEE 설정    
(예 :  C:\EGOV_DEV\eeclipse-jee-2024-03-R-win32-x86_64\eclipse)    

##### 2-4. Content탭에서 모든 Plug-ins 선택  
Content탭에서 Plug-ins 선택 후 ‘Select All’로 모든 플러그인을 선택    
![Target Platorm 설정](https://user-images.githubusercontent.com/68622744/230327199-12988bda-4be7-4fdb-b91a-8483d652ca36.png)  

##### 2-5. Target Platorm 설정  
Export 버튼을 클릭 후 RCP 내 workspace의 update 프로젝트 선택
![Target Platorm 설정](https://user-images.githubusercontent.com/68622744/230329271-f8119b4b-9226-43fc-abd1-1b59f96924b5.png)  

##### 2-6. Target Platform에 적용  
Target Definition에서 ‘Reload Target Platform’ 클릭
![Target Platform에 적용](https://user-images.githubusercontent.com/68622744/230327601-0f8ae1b3-0808-436c-9122-6c55ea0ea7fc.png)  
※ Reload Target Platform을 클릭하면 전체 프로젝트에 대한 빌드가 진행되며 오류사항들이 없어진다. 만약 오류 사항이 있으면 직접 오류사항을 체크하여 해결하여야 한다. 보통 라이브러리 존재 유무에 대한 오류로 필요하거나 변경해야 하는 라이브러리는 수정한다.


### 개발환경 Plug-ins 테스트
RCP및 Plug-ins소스 설정과 빌드가 완료되면 테스트를 위하여 이클립스 JEE와 연결하도록 설정한다.

#### 1. RCP 이클립스 상단 메뉴 Run > Run Configurations  

#### 2. Eclipse Application > New Configuration 생성  

#### 3. Main 탭에서 Workspace Data의 Location을 이클립스 JEE의 workspace로 지정  
![Main 탭에서 Workspace Data의 Location을 이클립스 JEE의 workspace로 지정](https://user-images.githubusercontent.com/68622744/230327833-5152ac6e-74d6-4fc3-81e7-9c10ac2c6fb7.png)  

#### 4. Arguments 탭에서 VM argument에 인코딩 ‘UTF-8’ 설정 추가  
    -Dfile.encoding=UTF-8  
![Arguments 탭에서 VM argument에 인코딩 ‘UTF-8’ 설정 추가](https://user-images.githubusercontent.com/68622744/230328038-b39e687a-999f-4c5e-941e-7ba7f25cab0f.png)  

##### 5. Plug-ins 탭에서 ‘Validatoe Plug-ins’을 실행하여 Plug-ins 검사 진행  
‘Validate Plug-ins’을 통하여 Target에 적용된 플러그인에 대한 유효성 검사를 진행하며 검사 시 문제가 발생하는 플러그인을 제외하거나 추가하여 진행한다.


## 참조
1. [개발환경 가이드](https://www.egovframe.go.kr/docs/5.0/egovframe-development/)
2. [개발환경 다운로드](https://www.egovframe.go.kr/home/sub.do?menuNo=107)


