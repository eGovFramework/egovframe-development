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
package egovframework.hdev.imp.ide.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;

/**  
 * IDE용 리소스 유틸리티 클래스
 * @Class Name : ResourceUtils
 * @Description : ResourceUtils Class
 * @Modification Information  
 * @
 * @  수정일			수정자		수정내용
 * @ ---------		---------	-------------------------------
 * @ 2012. 7. 12.		이율경		최초생성
 * 
 * @author 디바이스 API 개발환경 팀
 * @since 2012. 7. 12.
 * @version 1.0
 * @see
 * 
 */
public class ResourceUtils {
    /**
     * 폴더 존재 여부 확인하여 없으면 생성
     * @param folder
     * @param monitor
     * @throws CoreException
     */
    public static void ensureFolderExists(IFolder folder,
            IProgressMonitor monitor) throws CoreException {
        int ticks = folder.getProjectRelativePath().segmentCount();
        IProgressMonitor subMonitor = Policy.monitorFor(monitor);
        subMonitor.beginTask(null, ticks);
        IContainer parent;
        String[] segs;
        int i;
        IFolder tmp;
        try {
            if (folder.exists())
                return;
        } finally {
            subMonitor.done();
        }
        parent = folder.getProject();
        segs = folder.getProjectRelativePath().segments();
        for (i = 0; i < segs.length; i++) {
            tmp = parent.getFolder(new Path(segs[i]));
            if ((tmp != null) && (!tmp.exists()))
                tmp.create(true, true, Policy.subMonitorFor(monitor, 1));
            // else
            // monitor.worked(1);
            parent = tmp;
        }

        return;
    }

    /**
     * 파일명을 기반으로 폴더 존재 여부 확인하여 없으면 생성
     * @param project
     * @param fileName
     * @throws CoreException
     */
    public static void ensureFolderExists(IProject project, String fileName)
            throws CoreException {
        String path = ""; 

        if (fileName.lastIndexOf("/") < 0) 
            return;

        path = fileName.substring(0, fileName.lastIndexOf("/")); 

        IFolder folder = project.getFolder(path);
        ResourceUtils.ensureFolderExists(folder, null);
    }
    
    /**
     * 파일을 생성한다.
     * @param f        생성할 파일
     * @param content  파일의 내용
     * @param charset  characterset
     */
    public static void toTextFile(IFile f, String content, String charset)
        throws UnsupportedEncodingException, CoreException  {
        InputStream source = new ByteArrayInputStream( content.getBytes(charset));
        if (!f.isAccessible())
            f.create(source, true, null);
        else
            f.setContents( source , true, false, null);
    }
    
    /**
     * 파일을 복사한다.
     * @param input  복사 대상
     * @param output 복사 결과
     * @throws CoreException
     */
    public static void copy(IResource input, IResource output) throws CoreException {

        if ( input.getType() == output.getType()){
            input.copy(output.getFullPath(), IResource.FORCE, null);
        }
    }
    
    /**
     * Return the java.io.File object of IFile.
     * IFile 객체를 File 객체로 변환한다.
     *
     * @param iFile
     * @return
     */
    public static File getFile(IFile iFile) {
        return new File(iFile.getLocationURI());
    }
    
    /**
     * 특정 위치의 파일에 내용을 저장한다.
     * @param path
     * @param content
     */
    public static void writeToFile(String path, String content) throws CoreException {

        IFile f = getFileInWorkspace(path);
        if ( f.exists())
            f.setContents( new ByteArrayInputStream(content.getBytes()), IResource.FORCE, null);
    }
    
    /**
     * workspace내에서 파일을 찾아 리턴한다.
     * @param path
     * @return
     */
    public static IFile getFileInWorkspace(String path){
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IFile f = root.getFile(new Path(asWorkspaceRelativePath(path)));
        return f;
    }
    
    /**
     * Returns a relative path of this resource with respect to the current workspace.
     * workspace 에 대한 상대 경로를 반환한다.
     *
     * @param path
     * @return
     */
    public static String asWorkspaceRelativePath(String path) {
        if (path == null || path.length() == 0) { return path; }
        String path2 = path.replace('\\', '/');

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IPath location = root.getLocation();
        String ws = location.toPortableString();

        if (path2.startsWith(ws)) {
        	path2 = path2.substring(ws.length());
        }
        return path2;
    }
    
    /**
     * 특정 폴더 하위에 파일을 생성한다.
     * @param container   파일이 생성될 폴더
     * @param fname       파일명
     * @param content     파일내용
     * @param monitor
     * @throws CoreException
     */
    public static IFile createFile(IContainer container, String fname, InputStream content, IProgressMonitor monitor) throws CoreException {
        IPath p = new Path(fname.replace('\\', '/'));
        IContainer fo = null;

        if ( p.segmentCount() > 1) { // include path
            fo = container.getFolder(p.removeLastSegments(1));
            if ( !fo.isAccessible()) ((IFolder)fo).create(true, true, Policy.subMonitorFor(monitor, 1));
        }else {
            fo = container;
        }

        IFile ff = fo.getFile(new Path(p.lastSegment()));


        if (ff.isAccessible()) {

            ff.setContents(content, true, false, monitor);

        } else {
            ff.create(content, true, monitor);
        }
        return ff;
    }
    
    /**
     * 특정 폴더 하위에 파일을 생성한다.
     * 하위 폴더를 모두 생성한다.
     * @param container   파일이 생성될 폴더
     * @param fname       파일명
     * @param content     파일내용
     * @param monitor
     * @throws CoreException
     */
    public static IFile createFolderFile(IContainer container, String fname, InputStream content, IProgressMonitor monitor) throws CoreException {
    	IPath p = new Path(fname.replace('\\', '/'));
    	
    	IProgressMonitor pMonitor = monitor;
    	IContainer fo = container;
    	if(pMonitor == null) pMonitor = new NullProgressMonitor();
    	if ( p.segmentCount() > 1) { // include path
    		IPath pt;
    		for(int i=p.segmentCount()-1 ; i >  0 ; --i) {
    			pt = p.removeLastSegments(i);
    			fo = container.getFolder(pt);
    			if ( !fo.isAccessible()) ((IFolder)fo).create(true, true, new SubProgressMonitor(pMonitor, 1));
    		}
    	} 
    	
    	IFile ff = fo.getFile(new Path(p.lastSegment()));
    	
    	if (ff.isAccessible()) {
    		
    		ff.setContents(content, true, false, new SubProgressMonitor(pMonitor, 1));
    		
    	} else {
    		ff.create(content, true, new SubProgressMonitor(pMonitor, 1));
    	}
    	return ff;
    }
    
    /**
     * 경로의 앞부분을 떼어내고 상대경로 표기로 바꾼다.
     * @param prefix  잘라내고자 하는 상위 경로
     * @param path    잘릴 대상 경로
     */
    public static String asRelativePath(String prefix, String path) {

        String result = path;
        if (path == null) {
            return result;
        }

        IPath p = new Path(path);
        IPath pp = new Path(prefix);

        if ( pp.isPrefixOf(p) ){
            result = p.removeFirstSegments(pp.segmentCount()).toPortableString();
        }

        result = asRelativePath(pp, p);
        return result;
    }

    /**
     * pre path, file path를 입력받아 portableString을 return한다.
     * @param prefix
     * @param source
     * @return
     */
    public static String asRelativePath(IPath prefix, IPath source){
        String result = source.toPortableString();
        if ( prefix.isPrefixOf(source)){
            result = source.removeFirstSegments(prefix.segmentCount()).toPortableString();
        }
        return result;
    }
    
}
