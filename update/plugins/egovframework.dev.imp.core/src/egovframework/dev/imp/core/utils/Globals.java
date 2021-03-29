package egovframework.dev.imp.core.utils;

public class Globals {
	//OS 유형
    public static String osType = EgovProperties.getProperty("Globals.OsType");
    //DB 유형
    public static String dbType = EgovProperties.getProperty("Globals.DbType");
    //메인 페이지
    public static String mainPage = EgovProperties.getProperty("Globals.MainPage");
    //게시판 2단계 기능 사용여부
    public static String addOptions = EgovProperties.getProperty("Globals.AddOptions");
    
    
//    //ShellFile 경로
//    public static final String ShellFilePath = EgovProperties.getProperty("Globals.ShellFilePath");
//    //퍼로퍼티 파일 위치
//    public static final String ConfPath = EgovProperties.getProperty("Globals.ConfPath");
//    //Server정보 프로퍼티 위치
//    public static final String ServerConfPath = EgovProperties.getProperty("Globals.ServerConfPath");
//    //Client정보 프로퍼티 위치
//    public static final String ClientConfPath = EgovProperties.getProperty("Globals.ClientConfPath");
//    //파일포맷 정보 프로퍼티 위치
//    public static final String FileFormatPath = EgovProperties.getProperty("Globals.FileFormatPath");
//    
//    //파일 업로드 원 파일명
//	public static final String ORIGIN_FILE_NM = "originalFileName";
//	//파일 확장자
//	public static final String FILE_EXT = "fileExtension";
//	//파일크기
//	public static final String FILE_SIZE = "fileSize";
//	//업로드된 파일명
//	public static final String UPLOAD_FILE_NM = "uploadFileName";
//	//파일경로
//	public static final String FILE_PATH = "filePath";
//	
//	//메일발송요청 XML파일경로
//	public static final String MAIL_REQUEST_PATH = EgovProperties.getProperty("Globals.MailRequestPath");
//	//메일발송응답 XML파일경로
//	public static final String MAIL_RESPONSE_PATH = EgovProperties.getProperty("Globals.MailRResponsePath");
//	
	// G4C 연결용 IP (localhost)
	public static String localIp = EgovProperties.getProperty("Globals.LocalIp");
	
	//UserName
	public static String userName = EgovProperties.getProperty("Globals.UserName");
	
	//PassWord
	public static String password = EgovProperties.getProperty("Globals.Password");
	
	//Url
	public static String url = EgovProperties.getProperty("Globals.Url");
    
}
