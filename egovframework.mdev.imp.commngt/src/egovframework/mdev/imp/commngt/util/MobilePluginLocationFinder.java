package egovframework.mdev.imp.commngt.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * 플러그인 내의 로케이션 정보를 찾는 유틸 클래스
 * @author 개발환경 개발팀 박수림
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  박수림          최초 생성
 * 
 * 
 * </pre>
 */
public class MobilePluginLocationFinder {


	/**
	 * Returns a URL for the given path in the given bundle(project).
	 * 특정 플러그인에 위치한 resource 의 URL을 리턴한다.
	 * 
	 * @param plugin
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static URL getURL(Plugin plugin, String path) throws FileNotFoundException {
		URL url = FileLocator.find(plugin.getBundle(), new Path(path), null);
		if (url == null)
			throw new FileNotFoundException(path + " Not found");
		return url;
	}

	/**
	 * Returns File Object for the given path in the given bundle(project).
	 * 특정 플러그인에 위치한 file 객체를 리턴한다.
	 * 
	 * @param plugin
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static File getAbsoluteFile(Plugin plugin, String path) throws IOException {
		URL url = FileLocator.resolve(getURL(plugin, path));
		return new File(url.getFile());
	}

	/**
	 * 특정 플러그인의 File handle을 리턴한다.
	 * 
	 * @param plugin
	 * @return
	 */
	public static File getBundleLocation(Plugin plugin) {
		return new File(plugin.getBundle().getLocation());
	}


	/**
	 * plugin state area에 대한 local file system의 handle 을 리턴한다.
	 * @param plugin
	 * @return
	 */
	public static File getStateLocation(AbstractUIPlugin plugin) {
		IPath path = Platform.getStateLocation(plugin.getBundle());
		return path.toFile();
	}
//
//	/**
//	 * 특정 플러그인의 temp 폴더를 시스템 기본 temp 폴더 하위에 생성한다.
//	 * @param plugin
//	 * @return
//	 */
//	public static File getTemporaryFolder(Plugin plugin) {
//		return BFileUtil.getTemporaryFolder(plugin.getBundle().getSymbolicName());
//	}
//
//	/**
//	 * 
//	 * @param plugin
//	 * @param path
//	 * @param overwrite
//	 * @return
//	 * @throws IOException
//	 */
//	public static File extractFile(Plugin plugin, String path, boolean overwrite) throws IOException {
//		URL inner = getURL(plugin, path);
//		File dir = getTemporaryFolder(plugin);
//		File file = new File(dir, path);
//		if (file.exists() && !overwrite)
//			return file;
//
//		LFileCopy.copy( new BufferedInputStream(inner.openStream())
//			, new BufferedOutputStream(new FileOutputStream(file)));
//
//		return file;
//	}
}
