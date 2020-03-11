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
package egovframework.dev.imp.codegen.template.util;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;


/**
 * 코드 생성 로그를 콘솔로 출력하도록 하는 유틸 클래스
 * <p><b>NOTE:</b> 코드 생성 로그 처리. 
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
public class LogUtil {
	/** 콘솔 관리자 */
	private static IConsoleManager manager;
	
	/** 콘솔 */
	private static MessageConsole console;
	/** 콘솔 메시지 스트림*/
	public static MessageConsoleStream stdOutStream;
	/** 로거 */
	static Logger logger = Logger.getLogger(LogUtil.class);	
	/**
	 * 콘솔 세팅하기
	 */
	public static void console(){
		manager = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] consoles = manager.getConsoles();
		if (consoles.length>0){
			for (int i=0;i<consoles.length;i++){
				if (consoles[i] instanceof MessageConsole){
					if (((MessageConsole) consoles[i]).getName().equals("Template Based Code Generation Log"))
						console = (MessageConsole) consoles[i];
				}
			}
		}
		if (console==null){
			console = new MessageConsole("Template Based Code Generation Log",null);
			stdOutStream = console.newMessageStream();
			manager.addConsoles(new IConsole[]{console});
		}
		stdOutStream.setColor(new Color(PlatformUI.getWorkbench().getDisplay(),0,0,128));
//		manager.showConsoleView(console);
	}
	/**
	 * 로그 출력하기
	 * @param str
	 */
	public static void Log(String str){
		
		if (console==null)
			console();
		if (stdOutStream==null)
			stdOutStream = console.newMessageStream();
		stdOutStream.println(str);
	}
	
	/**
	 * 디버그 출력하기
	 * @param str
	 */
	public static void debug(String str){
		logger.debug(str);		
	}
	
	/**
	 *  콘솔 초기화 
	 */
	public static void consoleClear(){
		if (console==null)
			console();
		console.clearConsole();
	}

}
