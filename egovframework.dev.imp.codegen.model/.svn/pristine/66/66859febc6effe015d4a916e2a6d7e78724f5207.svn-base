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
package egovframework.dev.imp.codegen.model.generator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Type;

import egovframework.dev.imp.codegen.model.util.GenUtil;
import egovframework.dev.imp.codegen.model.util.LogUtil;
import egovframework.dev.imp.codegen.model.util.ProgressMonitorUtil;

/**
 * 스테레오 타입별로 코드를 생성하는 클래스
 * <p><b>NOTE:</b> 코드 생성 처리. 
 * @author 개발환경 개발팀 김형조 
 * @since 2009.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  김형조          최초 생성
 *
 * </pre>
 */
public class StereoTypeWriter {

    /** 로거 */
    protected final Logger log = Logger.getLogger(StereoTypeWriter.class);

    /** 일반 클래스의 스테레오 타입 */
    private static final String STEREO_CLASS = "Class";
    /** 일반 인터페이스의 스테레오 타입 */
	private static final String STEREO_INTERFACE = "Interface";
    /** 자바 코드의 확장자 */
	private static final String EXT_JAVA = ".java";
    /** Path 구분자 */
	private static final char PATH_SEPARATOR = '/';
    /** Package 구분자 */
	private static final char PACKAGE_SEPARATOR = '.';
    /** Work Unit 상수 */
	private static final int WORK_UNIT = 1;
    /** Container Work Unit 상수 */
	private static final int FIND_OR_CREATE_CONTAINER_WORK_UNIT = 4;
    /** 스테레오타입별 템플릿 저장 폴더 */
	private static final String TemplatePackageName = "egovframework.dev.imp.codegen.model.templates.Template";
    /** 템플릿 패키지 맵 */
	private final Map<String, Object> genTmpls = new HashMap<String, Object>();
    /** 코드 모델 */
	private final Model codeModel;
	/** 선택 모델 */
	private String[] seldObjs = null;
	/** 타겟 프로젝트 */
	private IJavaProject project;
	/** 타겟 폴더 */
	private String outputFolder = null;
	/** 코드 생성 전체 카운트 */
	private int totalCodeGenCount = 0;
	/** 코드 생성 성공 카운트 */
	private int successCodeGenCount = 0;
	
	/**
	 * 코드 생성 전체 카운트 가져오기
	 * @return
	 * 
	 */
	public int getTotalCodeGenCount(){
		return totalCodeGenCount;
	}
	/**
	 * 
	 * 코드 생성 성공 카운트 가져오기
	 * 
	 * @return
	 * 
	 */
	public int getSuccessCodeGenCount(){
		return successCodeGenCount;
	}

	/**
	 * 
	 * 생성자
	 * 
	 * @param model
	 * @param stereoTypeList
	 * 
	 */
	public StereoTypeWriter(Model model, Map<?, ?> stereoTypeList) {
		codeModel = model;
		GenUtil.setStereooTypeList(stereoTypeList);
		LogUtil.Console();
	}
	
	/**
	 * 
	 * 선택된 모델 세팅하기
	 * 
	 * @param seldObjs
	 * 
	 */
	public void setSelectedObjects(String[] seldObjs) {
		this.seldObjs = seldObjs;
	}
	
	/**
	 * 타겟 프로젝트 세팅하기
	 * 
	 * @param prj
	 * 
	 */
	public void setJavaProject(IJavaProject prj) {
		this.project = prj;
	}
	
	/**
	 * 타겟 폴더 세팅하기
	 * 
	 * @param folder
	 * 
	 */
	public void setOutputFolder(String folder) {
		outputFolder = folder;
	}

	/**
	 * 코드 생성 시작하기
	 * 
	 * @throws Exception
	 * 
	 */
	public void start() throws Exception {
		generate(codeModel.getOwnedElements(), "");
	}
	
	/**
	 * XMI 모델의 객체를 소스로 생성한다. 
	 * 
	 * @throws Exception
	 * 
	 */
	private void generate(EList<Element> elements, String pkg) throws CodeGenException {
		final IProgressMonitor monitor = ProgressMonitorUtil.initMonitor(null);
		//pkg 는 아래의 currentPkg 와 동일할 수 있다.
				
		Iterator<Element> ite = elements.iterator();
		Object obj = null;
		String src = null;
		IGenerator generator = null;

		
		try {
			while(ite.hasNext()) {
				obj = ite.next();
				
				if (obj instanceof Package) {
					Package pkgObj = (Package) obj;
					EList<Element> eles = pkgObj.getOwnedElements();
					
					if (eles.size() > 0 ) {
						generate(eles, ("".equals(pkg) ? pkg : pkg + ".") + pkgObj.getName());
					}
					
				} else if (obj instanceof Interface) {
					Interface itfc = (Interface) obj;
					String stereoType = null;
					
					String currentPkg = GenUtil.getPackage((Type)itfc);
					
					//위저드에서 선택된 객체 판별
					if (! GenUtil.checkExistence(seldObjs, "".equals(currentPkg) ? itfc.getName() : currentPkg + PACKAGE_SEPARATOR + itfc.getName())) continue;
					totalCodeGenCount++;
//					EList<Stereotype> stros = itfc.getAppliedStereotypes();
//					if (stros.size() > 0) stereoType = stros.get(0).getName();
					stereoType = GenUtil.getStereoType(itfc);
					
					try {
						if (stereoType == null)	generator = getGenerator(STEREO_INTERFACE);
						else generator = getGenerator(stereoType);
					} catch (Exception e) {
						generator = getGenerator(STEREO_INTERFACE);
					}
					
					src = generator.generate(itfc); 
					if (save(monitor, src.getBytes(), currentPkg, itfc.getName() + EXT_JAVA))
						successCodeGenCount++;
					
				} else if (obj instanceof Class) {
					Class cls = (Class) obj;
					String stereoType = null;

					String currentPkg = GenUtil.getPackage((Type)cls);
					
					//위저드에서 선택된 객체 판별
					if (! GenUtil.checkExistence(seldObjs, "".equals(currentPkg) ? cls.getName() : currentPkg + PACKAGE_SEPARATOR + cls.getName())) continue;
					totalCodeGenCount++;
//					EList<Stereotype> stros = cls.getAppliedStereotypes();
//					if (stros.size() > 0) stereoType = stros.get(0).getName();
					stereoType = GenUtil.getStereoType(cls);
					
					try {
						if (stereoType == null)	generator = getGenerator(STEREO_CLASS);
						else generator = getGenerator(stereoType);
					} catch (Exception e) {
						generator = getGenerator(STEREO_CLASS);
					}
					
					src = generator.generate(cls); 
					if (save(monitor, src.getBytes(), currentPkg, cls.getName() + EXT_JAVA))
						successCodeGenCount++;					
					
				} else if (obj instanceof DataType && !(obj instanceof PrimitiveType)) {
					DataType dataType = (DataType) obj;
					String currentPkg = GenUtil.getPackage((Type)dataType);
					
					IContainer container = findOrCreateContainer(monitor, currentPkg );
					IFile file = container.getFile( new Path(dataType.getName() + EXT_JAVA));
					
					if (!file.exists()) {
						//위저드에서 선택된 객체 판별
						if (! GenUtil.checkExistence(seldObjs, "".equals(currentPkg) ? dataType.getName() : currentPkg + PACKAGE_SEPARATOR + dataType.getName())) continue;
						
						generator = getGenerator("DataType");
						
						src = generator.generate(dataType); 
						save(monitor, src.getBytes(), currentPkg, dataType.getName() + EXT_JAVA);
					}					
					
				} 	
			}
			
 		} catch (Exception e) {
			throw new CodeGenException("StereoTypeWriter.getGenerator(EList, pkg) [" + e.getMessage() + "]");
		}
	}
	

	/**
	 * 생성된 코드를 파일로 저장하기
	 * 
	 * @param progressMonitor
	 * @param contents
	 * @param pkgName
	 * @param fileName
	 * @return
	 * @throws CodeGenException
	 * 
	 */
	public boolean save( final IProgressMonitor progressMonitor, 
			final byte[] contents, 
			String pkgName, 
			String fileName) throws CodeGenException
	{
		//ResourceUtil.  URI.createFileURI(file.getLocation().toOSString());
		IProgressMonitor monitor = ProgressMonitorUtil.initMonitor(progressMonitor);

		IContainer container = findOrCreateContainer(monitor, pkgName );
		IFile targetFile = container.getFile( new Path(fileName) );
		IFile result = getWritableTargetFile(targetFile);
		InputStream newContents = new ByteArrayInputStream( contents );
		
		try{
			if ( result.exists() ){
				if (MessageDialog.openConfirm(null, "eGovFrame Code Generator", fileName + " already exists.\nDo you want to overwrite?")){
					log.info("====================================================================================");
					log.info("Code Generation : Overwriting file " + fileName +".");
					log.info("( path : " + container.getFullPath() + " ) ");
					log.info("====================================================================================");
					result.setContents(
						newContents, true, true, new SubProgressMonitor( monitor,
								WORK_UNIT ) );
					return true;
				}else{
					log.info("====================================================================================");
			        log.info("Code Generation :" + fileName +" file Generation job has Canceled. File already exists. ");
			        log.info("( path : " + container.getFullPath() + " ) ");
					log.info("====================================================================================");
					return false;
				}
			} else {
				result.create(newContents, true, new SubProgressMonitor( monitor, WORK_UNIT ) );
				log.info("====================================================================================");
		        log.info("Code Generation : Creating file " + fileName +".");
		        log.info("( path : " + container.getFullPath() + " ) ");
				log.info("====================================================================================");
				return true;
			}
		} catch ( CoreException e ) {
			LogUtil.Log(e.getMessage());
			//throw new CodeGenException("'" + container.getFullPath() + "/" + fileName + "' CODEGEN ERROR FOR SAVING FILE ["+ e.getMessage() +"]");
		}
		return false;
	}

	
	/**
	 * 코드생성에 필요한 코드생성기 가져오기 
	 * 
	 * @param genName
	 * @return
	 * @throws Exception
	 * 
	 */
	private IGenerator getGenerator(String genName) throws CodeGenException {
		Object gen = genTmpls.get(genName);

		if (gen == null) {
			try {
				gen = loadGenerator(genName);
			} catch (Exception e) {
				throw new CodeGenException("StereoTypeWriter.getGenerator(" + genName + ") [" + e.getMessage() + "]");
			}
		}
		return (IGenerator) gen;
	}
	
	/**
	 * 생성기(템플릿)을 로딩한다.
	 * 
	 * @param genName
	 * @return 생성기
	 * @throws Exception
	 * 
	 */
	private Object loadGenerator (String genName) throws CodeGenException {
		try {
			java.lang.Class<?> genClass = java.lang.Class.forName(TemplatePackageName + genName);
			Object genObj = genClass.newInstance();
			genTmpls.put(genName, genObj);
			
			return genObj;
			
		}catch(ClassNotFoundException e){ 
			LogUtil.Log("\"Template"+ genName + "\" Template Class Error![" + e.getMessage()+"]");
			throw new CodeGenException("StereoTypeWriter.loadGenerator(" + genName + ") [Template" + genName + " Class Not Found!]");
		}catch(InstantiationException e){    
			LogUtil.Log("\"Template"+ genName + "\" Template Class Error![" + e.getMessage()+"]");
			throw new CodeGenException("StereoTypeWriter.loadGenerator(" + genName + ") [Template" + genName + " Instantiation Error!");
		}catch(IllegalAccessException e){ 
			LogUtil.Log("\"Template"+ genName + "\" Template Class Error![" + e.getMessage()+"]");
			throw new CodeGenException("StereoTypeWriter.loadGenerator(" + genName + ") [Template" + genName + " Class Access Error!");
		}catch(Error e) {//NoClassDefFoundError 처리
			LogUtil.Log("\"Template"+ genName + "\" Template Class Error![" + e.getMessage()+"]");
			throw new CodeGenException(genName + " Template File Not Found! StereoTypeWriter.loadGenerator(" + genName + ") [" + e.getMessage() + "]");
		}
	}
	
	/**
	 * 패키지 찾기 또는 생성하기  
	 * 
	 * @param progressMonitor
	 * @param pkgName
	 * @return
	 * @throws CodeGenException
	 * 
	 */
	private IContainer findOrCreateContainer(final IProgressMonitor progressMonitor,
			String pkgName ) 
			throws CodeGenException {
		
		//기본 프로젝트를 기반으로 Path 를 생성한다.
		StringBuffer path = new StringBuffer(project.getPath().toOSString());
		path.append( PATH_SEPARATOR );
		path.append( outputFolder);
		path.append( PATH_SEPARATOR );
		path.append( pkgName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR));

		IPath outputPath = new Path( path.toString() );
		
		progressMonitor.beginTask("", FIND_OR_CREATE_CONTAINER_WORK_UNIT ); //$NON-NLS-1$
		IProgressMonitor sub = new SubProgressMonitor( progressMonitor, WORK_UNIT );
	    IPath localLocation = null;
		IContainer container = null;

		try {
			container = CodeGenUtil.EclipseUtil.findOrCreateContainer(outputPath, true, localLocation, sub );
		} catch ( CoreException e )      {
			throw new CodeGenException("CODEGEN ERROR! CONTAINER NOT FOUND [" + outputFolder + pkgName + "["+ e.getMessage() + "]");
		}

		if ( container == null ) {
			throw new CodeGenException("CODEGEN ERROR! CONTAINER NOT FOUND [" + outputFolder + pkgName); 
		}

		return container;
	}

	/**
	 * 타겟 파일 가져오기 
	 * 
	 * @param targetFile
	 * @return
	 * @throws CodeGenException
	 * 
	 */
	private IFile getWritableTargetFile(
			final IFile targetFile) throws CodeGenException
			{
		IFile file = targetFile;

		if ( file.isReadOnly() ) {
			final ResourceAttributes attributes = new ResourceAttributes();
			attributes.setReadOnly( false );

			try {
				file.setResourceAttributes( attributes );
			} catch ( CoreException e ) {
				throw new CodeGenException("CODEGEN ERROR FOR GETTING WRITABLE FILE [" + e.getMessage() + "]");
			}
		}
		return file;
	}

}
