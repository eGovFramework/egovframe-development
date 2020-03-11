package egovframework.hdev.imp.ide.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

/**  
 * Null 관련 유틸리티 클래스
 * @Class Name : NullUtil
 * @Description : NullUtil Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 9. 17.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 9. 17.
 * @version 1.0
 * @see
 * 
 */
public class NullUtil {

    /**
     * Constructor for LNullUtils.
     */
    private NullUtil() {
        super();
    }


    /**
     * null 체크
     * @param value
     * @return
     */
    public static boolean isNull(Object value) {
        return value == null;
    }

    /**
     * null 체크
     * @param value
     * @return
     */
    public static boolean isNone(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * null 체크
     * @param value
     * @return
     */
    public static boolean notNone(String value) {
        return value != null && value.length() > 0;
    }

    /**
     * list null 체크
     * @param value
     * @return
     */
    public static boolean isNone(List<?> value) {
        return (value == null || value.isEmpty());
    }

    /**
     * object null 체크
     * @param value
     * @return
     */
    public static boolean isNone(Object[] value) {
        return (value == null || value.length == 0);
    }


    /**
     * map null 체크
     * @param value
     * @return
     */
    public static boolean isNone(Map<?, ?> value) {
        return (value == null || value.isEmpty());
    }


    /**
     * 기본값 세팅 
     * @param originalStr
     * @param defaultStr
     * @return
     */
    public static String NVL(String originalStr, String defaultStr) {
        if (originalStr == null || originalStr.length() < 1) return defaultStr;
        return originalStr;
    }

    /**
     * 기본값 세팅 
     * @param originalStr
     * @param defaultStr
     * @return
     */
    public static String NVL(Object object, String defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        return NVL(object.toString(), defaultValue);
    }

    /**
     * string 객체로 생성
     * @param o
     * @return
     */
    public static String print(Object o) {
        if (o == null) {
            return "";
        }
        return o.toString();
    }
	
	public static boolean isEmpty(Text text){
		if(NullUtil.isNull(text)){
			return true;
		}
		
		if(text.getText() != null && text.getText().length() > 0){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean isEmpty(Combo combo){
		if(NullUtil.isNull(combo)){
			return true;
		}
		
		if(combo.getText() != null && combo.getText().length() > 0){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean isEmpty(String string){
		if(NullUtil.isNull(string)){
			return true;
		}
		
		if(string.length() > 0){
			return false;
		}else{
			return true;
		}
	}
	
	public static boolean isEmpty(Object[] objects){
		if(NullUtil.isNull(objects)){
			return true;
		}
		
		if(objects.length > 0){
			return false;
		}else{
			return true;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(ArrayList arrayList){
		if(NullUtil.isNull(arrayList)){
			return true;
		}
		
		if(arrayList.size() > 0){
			return false;
		}else{
			return true;
		}
	}
}
