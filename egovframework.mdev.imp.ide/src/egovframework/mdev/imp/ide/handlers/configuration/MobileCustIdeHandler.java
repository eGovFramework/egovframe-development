package egovframework.mdev.imp.ide.handlers.configuration;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import egovframework.mdev.imp.ide.EgovMobileIdePlugin;
import egovframework.mdev.imp.ide.common.MoblieIdeLog;
import egovframework.mdev.imp.ide.handlers.EgovMobileMenu;

public class MobileCustIdeHandler extends AbstractHandler implements EgovMobileMenu {

    /**
     * 맞춤형개발환경 Wizard 실행
     */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		try {
			EgovMobileIdePlugin.getActiveWorkbenchWindow().run(true, true, new MobileInstallJob());
		} catch (InvocationTargetException e) {
			MoblieIdeLog.logError(e);
			MoblieIdeLog.setDialogMessage(e.getCause());
		} catch (InterruptedException e) {
			MoblieIdeLog.logError(e);
			MoblieIdeLog.setDialogMessage(e.getCause());
		}
		return null;
	}
}
