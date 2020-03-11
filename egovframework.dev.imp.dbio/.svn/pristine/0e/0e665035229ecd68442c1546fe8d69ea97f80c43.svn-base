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


import java.util.HashMap;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.ide.ResourceUtil;

import egovframework.dev.imp.dbio.DBIOPlugin;
import egovframework.dev.imp.dbio.common.DbioLog;

/**
 * Java Development Tool Utility
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20    김형조      최초 생성
 *
 * 
 * </pre>
 */
public class JdtUtil {
	
	private JdtUtil() {}
	
	/**
	 * OutputFolder 존재 확인
	 * @param folder
	 * @return
	 */
	public static boolean isOutputFolder(IFolder folder) {
		IJavaProject project = (IJavaProject) folder.getProject().getAdapter(IJavaElement.class);
		if (project != null) {
			try {
				IFolder temp = folder.getWorkspace().getRoot().getFolder(project.getOutputLocation());
				if (temp != null && temp.equals(folder)) {
					return true;
				}
			} catch (JavaModelException e) {
				DbioLog.logError(e);
			}
		}
		return false;
	}
	
	/**
	 * OutputFolder 존재 확인
	 * @param outputLocations
	 * @param folder
	 * @return
	 */
	public static boolean isOutputFolder(HashMap<?, ?> outputLocations, IFolder folder) {
		IJavaProject project = (IJavaProject) folder.getProject().getAdapter(IJavaElement.class);
		if (project != null) {
			try {

				if (outputLocations.get(folder.getFullPath()) != null) {
					return true;
				}
				
				IFolder temp = folder.getWorkspace().getRoot().getFolder(project.getOutputLocation());				
				if (temp != null && temp.equals(folder)) {
					return true;
				}
			} catch (JavaModelException e) {
				DbioLog.logError(e);
			}
		}
		return false;
	}
	
	public static String selectClass(IProject project, String filter) throws CoreException {
		IType element = (IType)
		selectType(project, filter, IJavaElementSearchConstants.CONSIDER_CLASSES, "Select class");
		return element == null ? null : element.getFullyQualifiedName('$');
	}
	
	private static IJavaElement selectType(IProject project, String filter,
			int style, String title) throws CoreException {
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(new IJavaElement[] {JavaCore.create(project)});
		SelectionDialog dialog = JavaUI.createTypeDialog(getShell(),
				PlatformUI.getWorkbench().getProgressService(), scope,
				style, false, filter);
		if (title != null) {
			dialog.setTitle(title);
		}
		if (dialog.open() == Window.OK) {
			Object[] elements = dialog.getResult();
			if (elements == null | elements.length == 0) {
				return null;
			} else {
				return (IJavaElement) ResourceUtil.getAdapter(elements[0], IJavaElement.class, false);
			}
		} else {
			return null;
		}
	}
	
	private static Shell getShell() {
		return DBIOPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell();
	}
	 
	 /**
	 * 추가 : javaElement에 대한 field 목록을 가져옴. 
	 * @param javaElement
	 * @return
	 * @throws JavaModelException
	 */
	public static String[] getProperty(IJavaElement javaElement) throws JavaModelException{
		IType element = (IType) javaElement;
		String[] result = null;
		if (element.getFields()!=null){
			result = new String[element.getFields().length];
			for (int i=0;i<element.getFields().length;i++)
				result[i] = element.getFields()[i].getElementName();
		}
		return result;
	 }

}
