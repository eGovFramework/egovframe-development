package egovframework.mdev.imp.commngt.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;

import egovframework.mdev.imp.commngt.MobileComMngtPlugin;
import egovframework.mdev.imp.commngt.common.MobileComMngtLog;

/**
 * 사용자가 선택한 Component의 Zip파일에서 Script파일을 찾는 클래스
 * 
 * @author 개발환경 개발팀 최서윤
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  최서윤          최초 생성
 * 
 * 
 * </pre>
 */

public class FindingScriptinZipUtil {

	/**
	 * Zip 파일에서 선택한 컴포넌트의 table script를 반환한다
	 * @param monitor
	 * @param fileName
	 * @param dbType
	 * @return table 정보가 담긴 script
	 * */
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> getScriptFileinZip(IProgressMonitor monitor, String fileName, String dbType) {

		HashMap<String, String> hashMap = new HashMap<String, String>();

		java.net.URL insetUrl = MobileComMngtPlugin.getDefault().getBundle()
				.getEntry(fileName);
			try {
				java.net.URL url = FileLocator.toFileURL(insetUrl);
				java.net.URL resolvedUrl = FileLocator.resolve(url);

				File inputZipFile = new File(resolvedUrl.getFile());
				ZipFile zipFile = new ZipFile(inputZipFile);

				Enumeration<? extends ZipEntry> enumeration = zipFile.getEntries();

				while (enumeration.hasMoreElements()) {

					ZipEntry entry = enumeration.nextElement();

					String name = entry.getName();
					if (!entry.isDirectory()) {
						if (name.indexOf("_insert_") > -1
								&& name.endsWith(".sql")
								&& name.indexOf(dbType) > -1) { 

							InputStream inputStream = zipFile
									.getInputStream(entry);
							String insertInputStream =  IOUtils.toString(inputStream, "UTF-8"); 
							hashMap.put("insert", insertInputStream);

						} else if (name.indexOf("_create_") > -1
								&& name.endsWith(".sql")
								&& name.indexOf(dbType) > -1) {

							InputStream inputStream = zipFile
									.getInputStream(entry);
							String createInputStream =  IOUtils.toString(inputStream, "UTF-8"); 
							hashMap.put("create", createInputStream);

						}

					}

				}

			} catch (IOException e) {
				MobileComMngtLog.logError(e);
			}
		return hashMap;

	}
	
	/**
	 * Zip 파일 내에 Globals.properties 파일을 다시 복사해 준다.
	 * @param monitor
	 * @param fileName
	 * @return table 정보가 담긴 script
	 * */
	@SuppressWarnings("unchecked")
	public static InputStream createPropertiesFileinZip(IProgressMonitor monitor, String fileName) {

		InputStream inputStream = null;

		java.net.URL insetUrl = MobileComMngtPlugin.getDefault().getBundle()
				.getEntry(fileName);
			try {
				java.net.URL url = FileLocator.toFileURL(insetUrl);
				java.net.URL resolvedUrl = FileLocator.resolve(url);

				File inputZipFile = new File(resolvedUrl.getFile());
				ZipFile zipFile = new ZipFile(inputZipFile,"UTF-8");

				Enumeration<? extends ZipEntry> enumeration = zipFile.getEntries();

				while (enumeration.hasMoreElements()) {

					ZipEntry entry = enumeration.nextElement();

					String name = entry.getName();
					if (!entry.isDirectory()) {
						if (name.indexOf("globals.properties") > -1) { 

							inputStream = zipFile.getInputStream(entry);
						}
					}


				}

			} catch (IOException e) {
				MobileComMngtLog.logError(e);
			}
		return inputStream;

	}
}
