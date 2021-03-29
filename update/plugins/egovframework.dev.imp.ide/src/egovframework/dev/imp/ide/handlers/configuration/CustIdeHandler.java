package egovframework.dev.imp.ide.handlers.configuration;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import egovframework.dev.imp.ide.EgovIdePlugin;
import egovframework.dev.imp.ide.common.IdeLog;
import egovframework.dev.imp.ide.handlers.EgovMenu;

public class CustIdeHandler extends AbstractHandler implements EgovMenu {

    /**
     * 맞춤형개발환경 Wizard 실행
     */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {
			EgovIdePlugin.getActiveWorkbenchWindow().run(true, true, new InstallJob());
		} catch (InvocationTargetException e) { 
			IdeLog.logError(e);
			IdeLog.setDialogMessage(e.getCause());
		} catch (InterruptedException e) {
			IdeLog.logError(e);
			IdeLog.setDialogMessage(e.getCause());
		}
		return null;
	}
}
