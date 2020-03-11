/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.hdev.imp.ide.wizards.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.osgi.util.NLS;

import egovframework.hdev.imp.ide.common.DeviceAPIIdeLog;

/**  
 * @Class Name : DeviceAPITemplateInfo
 * @Description : DeviceAPITemplateInfo Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 8. 22.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 8. 22.
 * @version 1.0
 * @see
 * 
 */
public class DeviceAPITemplateInfo {
	
	/** 번들 아이디 */
	public static final String BUNDLE_NAME_TEMPLATE = "egovframework.hdev.imp.ide.wizards.examples.examples";
	
    /** deviceapiExampleFile */
    public static String deviceapiExampleFile;
    
    /** Web POM File */
    public static String webPomFile;
    
    /** Web example File */
    public static String webexample;
    
    /** TABLE */
    public static String table1;
    public static String table2;
    public static String table3;
    public static String table4;
    public static String table5;
    public static String table6;
    public static String table7;
    public static String table8;
    public static String table9;
    public static String table10;
    public static String table11;
    public static String table12;
    public static String table13;
    public static String table14;
    public static String table15;
    public static String table16;
    public static String table17;
    public static String table18;
    public static String table19;
    public static String table20;
    public static String table21;
    public static String table22;
    public static String table23;
    public static String table24;
    public static String table25; 

    
    /** TABLE DESC */
    public static String table1desc;
    public static String table2desc;
    public static String table3desc;
    public static String table4desc;
    public static String table5desc;
    public static String table6desc;
    public static String table7desc;
    public static String table8desc;
    public static String table9desc;
    public static String table10desc;
    public static String table11desc;
    public static String table12desc;
    public static String table13desc;
    public static String table14desc;
    public static String table15desc;
    public static String table16desc;
    public static String table17desc;
    public static String table18desc;
    public static String table19desc;
    public static String table20desc;
    public static String table21desc;
    public static String table22desc;
    public static String table23desc;
    public static String table24desc;
    public static String table25desc;

    
    public static String template1name;
    public static String template1desc;
    public static String template1example;
    
    public static String template2name;
    public static String template2desc;
    public static String template2example;
    
    public static String template3name;
    public static String template3desc;
    public static String template3example;
    
    public static String template4name;
    public static String template4desc;
    public static String template4example;
    
    public static String template5name;
    public static String template5desc;
    public static String template5example;
    
    public static String template6name;
    public static String template6desc;
    public static String template6example;
    
    public static String template7name;
    public static String template7desc;
    public static String template7example;
    
    public static String template8name;
    public static String template8desc;
    public static String template8example;
    
    public static String template9name;
    public static String template9desc;
    public static String template9example;
    
    public static String template10name;
    public static String template10desc;
    public static String template10example;
    
    public static String template11name;
    public static String template11desc;
    public static String template11example;
    
    public static String template12name;
    public static String template12desc;
    public static String template12example;
    
    public static String template13name;
    public static String template13desc;
    public static String template13example;
    
    public static String template14name;
    public static String template14desc;
    public static String template14example;
    
    public static String template15name;
    public static String template15desc;
    public static String template15example;

    public static String template16name;
    public static String template16desc;
    public static String template16example;
    
    public static String template17name;
    public static String template17desc;
    public static String template17example;
    
    public static String template18name;
    public static String template18desc;
    public static String template18example;
    
    public static String template19name;
    public static String template19desc;
    public static String template19example;
    
    public static String template20name;
    public static String template20desc;
    public static String template20example;
    
    public static String template21name;
    public static String template21desc;
    public static String template21example;
    
    public static String template22name;
    public static String template22desc;
    public static String template22example;
    
    public static String template23name;
    public static String template23desc;
    public static String template23example;
    
    public static String template24name;
    public static String template24desc;
    public static String template24example;
    
    public static String template25name;
    public static String template25desc;
    public static String template25example;

    /**
     * 리소스 번들로 부터 속성 값 읽어오기
     */
    static {
        try {
        	NLS.initializeMessages(BUNDLE_NAME_TEMPLATE, DeviceAPITemplateInfo.class);

        } catch (Exception ex) {
        	
            DeviceAPIIdeLog.logError(ex);
        }
    }
    
    /**
     * 템플릿 정보 가져오기
     * @param tamplateName
     * @return HashMap<String,String>
     * @throws Exception 
     */
    public static HashMap<String, String> getTemplateInfo(String tamplateName) throws Exception {
    	
    	HashMap<String, String> map =  new HashMap<String, String>();
    	
    	String templateType = "";
    	
    	if("Sample_Template".equals(tamplateName)) {
    		
    		templateType = "template1";
    	} else if("Contacts".equals(tamplateName)) {
    		
    		templateType = "template2";
    	} else if("GPS".equals(tamplateName)) {
    		
    		templateType = "template3";
    	} else if("Accelerator".equals(tamplateName)) {
    		
    		templateType = "template4";
    	} else if("Compass".equals(tamplateName)) {
    		
    		templateType = "template5";
    	} else if("Camera".equals(tamplateName)) {
    		
    		templateType = "template6";
    	} else if("Vibrator".equals(tamplateName)) {
    		
    		templateType = "template7";
    	} else if("Media".equals(tamplateName)) {
    		
    		templateType = "template8";
    	} else if("FileReadWriter".equals(tamplateName)) {
    		
    		templateType = "template9";
    	} else if("Device".equals(tamplateName)) {
    		
    		templateType = "template10";
    	} else if("Network".equals(tamplateName)) {
    		
    		templateType = "template11";
    	} else if("PKIMagicXSign".equals(tamplateName)) {
    		
    		templateType = "template12";
    	} else if("PKIWizSign".equals(tamplateName)) {
    		
    		templateType = "template13";
    	} else if("PKIXecureSmart".equals(tamplateName)) {
    		
    		templateType = "template14";
    	} else if("Interface".equals(tamplateName)) {
    		
    		templateType = "template15";    		
    	} else if("BarcodeScanner".equals(tamplateName)) {
    		
    		templateType = "template16";
    	} else if("DeviceFilemgmt".equals(tamplateName)) {
    		
    		templateType = "template17";
    	} else if("FileOpener".equals(tamplateName)) {
    		
    		templateType = "template18";
    	} else if("JailBreakDetection".equals(tamplateName)) {
    		
    		templateType = "template19";
    	} else if("PushNotiflcations".equals(tamplateName)) {
    		
    		templateType = "template20";
    	} else if("SocketIO".equals(tamplateName)) {
    		
    		templateType = "template21";
    	} else if("Sqlite".equals(tamplateName)) {
    		
    		templateType = "template22";
    	} else if("StreamingMedia".equals(tamplateName)) {
    		
    		templateType = "template23";
    	} else if("UnZip".equals(tamplateName)) {
    		
    		templateType = "template24";
    	} else if("WebResourceUpdate".equals(tamplateName)) {
    		
    		templateType = "template25";
    	} 
    	
    	Class<DeviceAPITemplateInfo> c = DeviceAPITemplateInfo.class;
    	
    	map.put("name", c.getField(templateType + "name").get(c).toString());
    	map.put("desc", c.getField(templateType + "desc").get(c).toString());
    	map.put("example", c.getField(templateType + "example").get(c).toString());
    	
    	return map;
    }
    
    public static List<String> getTableList() throws Exception {
    	
    	Class<DeviceAPITemplateInfo> c = DeviceAPITemplateInfo.class;
    	List<String> tableList = new ArrayList<String>();
    	
    	for(int i=1; i<26; i++) {
    		
    		tableList.add(c.getField("table" + i).get(c).toString());
    	}
    	
    	return tableList;
    }
    
    /**
     * 테이블 설명 가져오기
     * @return HashMap<String,String>
     * @throws Exception 
     */
    public static HashMap<String, String> getTableDesc() throws Exception {
    	
    	Class<DeviceAPITemplateInfo> c = DeviceAPITemplateInfo.class;
    	HashMap<String, String> map = new HashMap<String, String>();
    	
    	for(int i=1; i<26; i++) {
    		
    		String tableName = c.getField("table" + i).get(c).toString();
    		String tableDesc = c.getField("table" + i + "desc").get(c).toString();
    		
    		map.put(tableName, tableDesc);
    	}
    	
    	return map;
    }
}
