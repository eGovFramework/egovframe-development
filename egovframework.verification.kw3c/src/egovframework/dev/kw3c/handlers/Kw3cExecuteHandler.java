package egovframework.dev.kw3c.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;

import egovframework.dev.kw3c.Kw3cPlugin;

public class Kw3cExecuteHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			Path path = new Path(Kw3cPlugin.getDefault().getInstalledPath());
			String exeFileName = path.append("KW3CValidator/" + "KW3C.exe").toOSString();
			
			
			//권한 문제로 인한 KW3C 실행 오류 (2017.6.15) 
			//Runtime runtime = Runtime.getRuntime();			
			//@SuppressWarnings("unused")
			//Process prc = runtime.exec(exeFileName);
			
			//실행 오류 수정 (2017.6.15)
			ProcessBuilder exec = new ProcessBuilder(new String[] {"cmd.exe", "/C", exeFileName});
			exec.start();
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
