package egovframework.bdev.imp.batch.wizards.com;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import egovframework.bdev.imp.batch.common.BatchLog;



/**
 * 파일을 생성 및 삭제 클래스
 * 
 * @author 배치개발환경 개발팀 최서윤
 * @since 2012.07.18
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2012.07.18  최서윤          최초 생성
 * 
 * 
 * </pre>
 */

public class HandlingFileOperation {
	/** 생성할 XML 파일*/
	public static IFile newXMLFile = null;
	
	/**
	 * 지정한 경로와 파일명으로 파일 생성
	 * 
	 * @param context
	 * @return IFile
	 * */
	@SuppressWarnings("restriction")
	public static IFile createFile(BatchJobContext context) {
		
		if(context.getFilePath().contains("/")){
			Path path = new Path(context.getFilePath()+"/"+context.getFileName());
			newXMLFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		}else{
			Path path = new Path(context.getProject().getFullPath()+"/"+context.getFileName());
			newXMLFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		}
		String contents = " ";
		InputStream inputStream = new ByteArrayInputStream(contents.getBytes());

		if (!newXMLFile.exists()) {
			try {
				newXMLFile.create(inputStream, false, null);
			} catch (CoreException e) {
				BatchLog.logError(e);
			}
		}else{
			String[] value = context.getFileName().split(".xml");
			if(context.getFilePath().contains("/")){
				Path path = new Path(context.getFilePath()+"/"+value[0]+"_"+"eGov"+".xml");
				newXMLFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			}else{
				Path path = new Path(context.getProject().getFullPath()+"/"+value[0]+"_"+"eGov"+".xml");
				newXMLFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			}
			try {
				newXMLFile.setContents(inputStream, true, false, null);
			} catch (CoreException e) {
				BatchLog.logError(e);
			}
		}
		return newXMLFile;
	}
	
	/**
	 * 구성한 일반 파일 XML파일로 변형
	 * 
	 * @param doc
	 * */
	public static void transformFiletoXMLFile(Document doc) {
		
		XMLOutputter xmlOut = new XMLOutputter();
		xmlOut.setXMLOutputProcessor(new CustomXMLOutputProcessor());
		Format format = xmlOut.getFormat();
		format.setIndent("    ");
		format.setLineSeparator("\r\n");
		format.setTextMode(Format.TextMode.TRIM); 
		xmlOut.setFormat(format);


		//출력을 위한 객체를 만듬, indent=” “, newline=ok로 설정
		try {   
			OutputStream outputStream = new ByteArrayOutputStream();
	        xmlOut.output(doc, outputStream);
			InputStream is = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());
			
			if (!newXMLFile.exists()) {
				newXMLFile.create(is, true, null);
			} else {
				newXMLFile.setContents(is, 0, null);
			}
			
		} catch (java.io.IOException e) {   
			BatchLog.logError(e);
		} catch (CoreException e) {
			BatchLog.logError(e);
		} 
	}
	
	/**
	 * 비정상 종료시 구성한 임시 파일 삭제 
	 * 
	 * @param newXMLFile
	 * */
	public static void deleteFile(IFile newXMLFile) {
		
		if(newXMLFile != null && newXMLFile.exists() && newXMLFile.isAccessible()){
			try {
				newXMLFile.delete(true, null);
			} catch (CoreException e) {
				BatchLog.logError(e);
			}
		}else{
		}
	}
	
}