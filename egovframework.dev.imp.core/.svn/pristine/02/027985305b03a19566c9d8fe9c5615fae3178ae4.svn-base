/*
 * @(#) LNullUtils.java
 * 
 * Copyright �� LG CNS, Inc. All rights reserved.
 *
 * Do Not Erase This Comment!!! (�� �ּ����� ������ ����)
 *
 * DevOn Core�� Ŭ������ ���� ������Ʈ�� ����ϴ� ��� DevOn Core ���ߴ���ڿ���
 * ������Ʈ ���ĸ�Ī, ����� ����ó(Email)���� mail�� �˷��� �Ѵ�.
 *
 * �ҽ��� �����Ͽ� ����ϴ� ��� DevOn Core ���ߴ���ڿ���
 * ����� �ҽ� ��ü�� ����� ������ �˷��� �Ѵ�.
 * �����ڴ� ����� �ҽ��� �����ϴٰ� �ǴܵǴ� ��� �ش� ������ �ݿ��� �� �ִ�.
 * �߿��� Idea�� �����Ͽ��ٰ� �ǴܵǴ� ��� �����Ͽ� ���� List�� �ݿ��� �� �ִ�.
 *
 * (����!) �������� ������� ����� �� �� ������
 * LG CNS �ܺη��� ������ �Ͽ����� �� �ȴ�.
 */
package egovframework.dev.imp.core.utils;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;


public class NullUtil {

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
    	if(isNull(text)){
    		return true;
    	}
    	
		if(text.getText() != null && text.getText().length() > 0){
			return false;
		}else{
			return true;
		}
    }
    
    public static boolean isEmpty(String string){
    	if(isNull(string)){
    		return true;
    	}
    	
    	if(string.length() > 0){
			return false;
		}else{
			return true;
		}
    }
    
    public static boolean isEmpty(Object[] objects){
    	if(isNull(objects)){
    		return true;
    	}
    	
    	if(objects.length > 0){
			return false;
		}else{
			return true;
		}
    }
    
    public static boolean isEmpty(Combo combo){
    	if(isNull(combo)){
    		return true;
    	}
    	
    	if(combo.getText() != null && combo.getText().length() > 0){
			return false;
		}else{
			return true;
		}
    }
    
    public static boolean isEmpty(List<?> list){
    	if(isNull(list)){
    		return true;
    	}
    	
    	if(list.size() > 0){
			return false;
		}else{
			return true;
		}
    }
    
    public static boolean isEmpty(Map<?, ?> map){
    	if(isNull(map)){
    		return true;
    	}
    	
    	if(map.size() > 0){
    		return false;
    	}else{
    		return true;
    	}
    }
    
}
