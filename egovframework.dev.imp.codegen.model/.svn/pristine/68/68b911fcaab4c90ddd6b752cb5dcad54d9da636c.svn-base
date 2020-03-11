/*
 * Copyright 2008-2009 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.dev.imp.codegen.model.validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import egovframework.dev.imp.codegen.model.converter.XMIFileHandler;
import egovframework.dev.imp.codegen.model.converter.XMIHandler;

/**
 * XMI 문서 파일을 Validation 하는 클래스
 * <p><b>NOTE:</b> 
 * @author 개발환경 개발팀 윤수열
 * @since 2009.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 *
 * </pre>
 */
public class XMIDocumentValidator implements DataModelValidationCheck {

	/* 
	 * 객체 에 대한  Validation 을 수행함. 
	 * 
	 * @see egovframework.dev.imp.codegen.model.validator.DataModelValidationCheck#validate(java.lang.Object)
	 * 
	 */
	public boolean validate(Object element) throws Exception {
		if (element instanceof String)
			return validationCheck( (String)element );
		return false;
	}

	/**
	 *  지정된 경로에 있는 XMI 파일에 대하여 Validation을 수행함. 
	 * 
	 * @param strPath
	 * @return
	 * 
	 */
	private boolean validationCheck(String strPath){ 
		XMLReader xr = new SAXParser();
		XMIHandler handler = (XMIHandler) new XMIFileHandler();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);
		FileReader reader = null;
		try {
			reader = new FileReader(new File(strPath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			return false;
		}
		try {
			xr.parse(new InputSource(reader));
		} catch (IOException e) {
			e.printStackTrace();
			return false;			
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
