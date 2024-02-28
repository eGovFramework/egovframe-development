package egovframework.bdev.imp.batchnature.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.bdev.imp.batchnature.EgovBatchNaturePlugin;
import egovframework.bdev.imp.batchnature.common.BatchNatureLog;
import egovframework.dev.imp.core.utils.BTextSearchUtil;
import egovframework.dev.imp.core.utils.EgovJavaElementUtil;
import egovframework.dev.imp.core.utils.XmlUtil;

public class HandlePomXMLFileUtil {

	/**
	 * is pom.xml exist
	 * 
	 * @param project
	 * @return isPomFileExist
	 */
	public static boolean isPomFileExist(IProject project) {

		boolean isPomFileExist = false;
		List<?> foundList = null;
		IFile actualFile = null;

		try {
			if (EgovJavaElementUtil.isJavaProject(project)) {

				foundList = BTextSearchUtil.findFiles(project.members(), null, "pom.xml", false, null);
			}
		} catch (CoreException e) {
			BatchNatureLog.logError(e);
		}
		if (foundList != null) {
			for (int j = 0; j < foundList.size(); j++) {

				Map<?, ?> map = (Map<?, ?>) foundList.get(j);
				if (map != null) {
					Object f = map.get(BTextSearchUtil.K_FILE);

					if (f instanceof IFile) {
						actualFile = (IFile) f;
						break;
					}
				}
			}
		}

		if (actualFile != null) {
			if (actualFile.isAccessible() && actualFile.exists()) {
				isPomFileExist = true;
				modifyPom(actualFile, project);
			}
		} else {
			isPomFileExist = createPomFile(actualFile, project);
		}

		return isPomFileExist;

	}

	/**
	 * if not exist create
	 * 
	 * @param actualFile
	 * @param project
	 * @return boolean pom파일생성
	 */
	private static boolean createPomFile(IFile actualFile, IProject project) {

		Path path = new Path(project.getFullPath() + "/" + "pom.xml");
		actualFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

		InputStream inputStream = createPropertiesFileinZip(null, "examples/pomFile.zip");

		try {
			actualFile.create(inputStream, false, null);

			inputStream.close();
		} catch (CoreException e) {
			BatchNatureLog.logError(e);
		} catch (IOException e) {
			BatchNatureLog.logError(e);
		}

		return true;
	}

	/**
	 * pom 있을 경우 batch 있는지 확인 후 수정
	 * 
	 * @param actualFile
	 * @param project
	 */
	private static void modifyPom(IFile actualFile, IProject project) {

		try {
			// pom.xml 파일을 읽어 노드 리스트를 구성한다.
			Node rootNode = XmlUtil
					.getRootNode(new File(project.getProject().getFile("pom.xml").getLocation().toOSString()));
			NodeList oriNodes = XmlUtil.getNodeList(rootNode, "/project/dependencies/dependency");

			if (oriNodes != null) {

				boolean eGovBatchNoExist = true; // eGovFrame Batch 디펜던시 존재여부
				boolean springBatchInfraNoExist = true; // spring Batch infrastructure 디펜더시 존재여부
				boolean springBatchCoreNoExist = true; // spring Batch core 디펜더시 존재여부
				Node oriDependency = null;

				// 디펜던시 존재여부 및 버전 확인
				for (int j = 0; oriNodes.getLength() > j; j++) {
					oriDependency = oriNodes.item(j);
					Node oriArtifactId = XmlUtil.getNode(oriDependency, "./artifactId");

					if (oriArtifactId != null
							&& oriArtifactId.getFirstChild().getNodeValue().equals("egovframework.rte.bat.core")) {
						eGovBatchNoExist = false;
						Node oriVersion = XmlUtil.getNode(oriDependency, "./version");

						// 현재 3.1.0 버전의 eGovBatch 적용 (2017.07.05 sh.jang)
						if (oriVersion != null && !oriVersion.getFirstChild().getNodeValue().equals("4.2.0")) {
							oriVersion.getFirstChild().setNodeValue("4.2.0");
						}
					}

					if (oriArtifactId != null
							&& oriArtifactId.getFirstChild().getNodeValue().equals("spring-batch-infrastructure")) {
						springBatchInfraNoExist = false;
					}

					if (oriArtifactId != null
							&& oriArtifactId.getFirstChild().getNodeValue().equals("spring-batch-core")) {
						springBatchCoreNoExist = false;
					}
				}

				// 4.2 batch 적용 (2017.07.04 sh.jang)
				if (eGovBatchNoExist) {
					String xmlStr = "<dependency>" + "\n\t\t\t<groupId>" + "org.egovframe.rte" + "</groupId>"
							+ "\n\t\t\t<artifactId>" + "org.egovframe.rte.bat.core" + "</artifactId>"
							+ "\n\t\t\t<version>" + "4.2.0" + "</version>\n\t\t" + "</dependency>" + "\n ";

					XmlUtil.addFirstNode(rootNode, "/project/dependencies", xmlStr, "\n\t\t", "\t");
				}

				if (springBatchInfraNoExist) {
					String xmlStr = "<dependency>" + "\n\t\t\t<groupId>" + "org.springframework.batch" + "</groupId>"
							+ "\n\t\t\t<artifactId>" + "spring-batch-infrastructure" + "</artifactId>"
							+ "\n\t\t\t<version>" + "4.3.8.RELEASE" + "</version>\n\t\t" + "</dependency>" + "\n ";

					XmlUtil.addFirstNode(rootNode, "/project/dependencies", xmlStr, "\n\t\t", "\t");
				}

				if (springBatchCoreNoExist) {
					String xmlStr = "<dependency>" + "\n\t\t\t<groupId>" + "org.springframework.batch" + "</groupId>"
							+ "\n\t\t\t<artifactId>" + "spring-batch-core" + "</artifactId>" + "\n\t\t\t<version>"
							+ "4.3.8.RELEASE" + "</version>\n\t\t" + "</dependency>" + "\n ";

					XmlUtil.addFirstNode(rootNode, "/project/dependencies", xmlStr, "\n\t\t", "\t");
				}

				// 파일을 생성한다.
				InputStream inStream = new ByteArrayInputStream(XmlUtil.getXmlString(rootNode, "/").getBytes("UTF-8"));
				actualFile.setContents(inStream, true, false, null);
				inStream.close();
			}

		} catch (Exception e) {
			BatchNatureLog.logError(e);
		}
	}

	/**
	 * Zip 파일 내에 pom.xml 파일을 내용 복사
	 * 
	 * @param monitor
	 * @param fileName
	 * @return table 정보가 담긴 script
	 */
	@SuppressWarnings("unchecked")
	public static InputStream createPropertiesFileinZip(IProgressMonitor monitor, String fileName) {

		InputStream inputStream = null;

		URL insetUrl = EgovBatchNaturePlugin.getDefault().getBundle().getEntry(fileName);

		try {
			// URL insetUrl =
			// Platform.asLocalURL(EgovBatchNaturePlugin.getDefault().getBundle().getEntry(fileName));

			URL url = FileLocator.toFileURL(insetUrl);
			URL resolvedUrl = FileLocator.resolve(url);

			File inputZipFile = new File(resolvedUrl.getFile());
			ZipFile zipFile = new ZipFile(inputZipFile, "UTF-8");

			Enumeration<? extends ZipEntry> enumeration = zipFile.getEntries();

			while (enumeration.hasMoreElements()) {

				ZipEntry entry = enumeration.nextElement();

				String name = entry.getName();
				if (!entry.isDirectory()) {
					if (name.indexOf("pom.xml") > -1) {

						inputStream = zipFile.getInputStream(entry);
					}
				}
			}

			// stream이 close되어 전달되는 문제 발생하여 null인경우만 close함 (2017-07-05 sh.jang)
			if (inputStream == null || inputStream.equals(null))
				inputStream.close();

		} catch (IOException e) {
			BatchNatureLog.logError(e);
		}
		return inputStream;

	}

}
