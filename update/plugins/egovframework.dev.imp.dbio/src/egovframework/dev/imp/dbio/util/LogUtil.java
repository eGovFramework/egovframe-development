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
package egovframework.dev.imp.dbio.util;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;


public class LogUtil {
	private static IConsoleManager manager;
	private static MessageConsole console;
	public static MessageConsoleStream stdOutStream;
	static Logger logger = Logger.getLogger(LogUtil.class);	
	public static void Console(){
		manager = ConsolePlugin.getDefault().getConsoleManager();
		IConsole[] consoles = manager.getConsoles();
		if (consoles.length>0){
			console = (MessageConsole) consoles[0];
			stdOutStream = console.newMessageStream();
		}else{
			console = new MessageConsole("Query Test Log",null);
			stdOutStream = console.newMessageStream();
			manager.addConsoles(new IConsole[]{console});
		}
		stdOutStream.setColor(new Color(PlatformUI.getWorkbench().getDisplay(),0,0,128));
//		manager.showConsoleView(console);
	}
	public static void Log(String str){
		
		if (console==null)
			Console();
		if (stdOutStream==null)
			stdOutStream = console.newMessageStream();
		stdOutStream.println(str);
	}
	
	public static void debug(String str){
		logger.debug(str);		
	}

}
