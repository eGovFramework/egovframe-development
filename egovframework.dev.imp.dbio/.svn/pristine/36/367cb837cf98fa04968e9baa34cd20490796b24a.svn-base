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
package egovframework.dev.imp.dbio.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.ide.StringMatcher;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import egovframework.dev.imp.dbio.editor.model.DOMElementProxy;
import egovframework.dev.imp.dbio.editor.model.MapperCRUDElement;
import egovframework.dev.imp.dbio.editor.model.MapperQueryGroupElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapCRUDElement;
import egovframework.dev.imp.dbio.editor.model.SqlMapQueryGroupElement;
import egovframework.dev.imp.dbio.util.FileUtil;
import egovframework.dev.imp.dbio.util.JdtUtil;
import egovframework.dev.imp.dbio.util.StructuredModelUtil;

/**
 * QueryId Search
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
@SuppressWarnings("restriction")
public class QueryIdSearchJob extends Job {

	private IProject project;
	private StringMatcher matcher;
	private QueryIdSearchView searchView;
	private final Map<IFile, List<DOMElementProxy>> result = new HashMap<IFile, List<DOMElementProxy>>(); 
	//private final Map<IFile, List<SqlMapCRUDElement>> result = new HashMap<IFile, List<SqlMapCRUDElement>>();

	public QueryIdSearchJob(QueryIdSearchView searchView) {
		super("Search Query ID");
		setUser(true);
		this.setSearchView(searchView); 
	}

	private void setSearchView(QueryIdSearchView searchView) {
		this.searchView = searchView;
	}
	public void setTaragetProject(IProject project) {
		this.project = project;
	}

	public void setQuery(String text) {
		this.matcher = new 	StringMatcher("*" + text + "*", true, false); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@SuppressWarnings("rawtypes")
	public Map getResultMap() {
		return this.result;
	}

	/**
	 * Query ID 검색
	 */
	@Override
	public IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Search Query ID", 5);
			result.clear();
			if (project == null) return Status.OK_STATUS;

			final HashMap<IPath, IPath> outputLocations = new HashMap<IPath, IPath>();
			IJavaProject javaProject = JavaCore.create(project);
			try {
				outputLocations.put(javaProject.getOutputLocation(), javaProject.getOutputLocation());

				IClasspathEntry[] classpathEntrys = javaProject.getRawClasspath();
				for(int i = 0; i < classpathEntrys.length; i ++){				
					IPath output = classpathEntrys[i].getOutputLocation();
					if (outputLocations.get(output) == null) {
						outputLocations.put(output, output);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	

			project.accept(new IResourceVisitor() {
				public boolean visit(IResource resource) throws CoreException {
					switch (resource.getType()) {
					case IResource.FOLDER:
						return !JdtUtil.isOutputFolder(outputLocations, (IFolder) resource);
					case IResource.FILE:
						
						/*if (FileUtil.isSqlMapFile((IFile) resource)) {
							searchInFile((IFile) resource);
						}else if (FileUtil.isMapperFile((IFile) resource)) {
							searchInFile((IFile) resource);
						}
						*/
						if (FileUtil.isEGovSqlMapperFile((IFile) resource)) {
							searchInFile((IFile) resource);
						}
						break;
					default :
						break;
					}
					return true;
				}
			});
			monitor.worked(4);

			final TreeNode[] nodes = makeResultTree();
			monitor.worked(1);

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					searchView.setResult(nodes);
				}
			});
			return Status.OK_STATUS;
		} catch (CoreException e) {
			return e.getStatus();
		} finally {
			monitor.done();
		}
	}
/*
	private TreeNode[] makeResultTree() {
		List<FileTreeNode> children = new LinkedList<FileTreeNode>();
		for(IFile file : result.keySet()) {
			FileTreeNode fileNode = new FileTreeNode(file);
			children.add(fileNode);

			List<SqlMapElementNode> elements = new LinkedList<SqlMapElementNode>();
			for (SqlMapCRUDElement element : result.get(file)) {
				SqlMapElementNode elementNode = new SqlMapElementNode(fileNode, element);
				elements.add(elementNode);
			}
			fileNode.setChildren(elements.toArray(new TreeNode[elements.size()]));
		}
		return children.toArray(new TreeNode[children.size()]);
	}
*/
	private TreeNode[] makeResultTree() {
		List<FileTreeNode> children = new LinkedList<FileTreeNode>();
		for(IFile file : result.keySet()) {
			FileTreeNode fileNode = new FileTreeNode(file);
			children.add(fileNode);

			List<TreeNode> elements = new LinkedList<TreeNode>();
			for (DOMElementProxy element : result.get(file)) {
				if (element instanceof SqlMapCRUDElement) {
					SqlMapElementNode elementNode = new SqlMapElementNode(fileNode, (SqlMapCRUDElement)element);
					elements.add(elementNode);				
				}else if(element instanceof MapperCRUDElement) {
					MapperElementNode elementNode = new MapperElementNode(fileNode, (MapperCRUDElement)element);
					elements.add(elementNode);				
				}
			}
			fileNode.setChildren(elements.toArray(new TreeNode[elements.size()]));
		}
		return children.toArray(new TreeNode[children.size()]);
	}
	
	private void searchInFile(IFile file) {
		IStructuredModel model = null;
		try {
			model = StructuredModelUtil.getModelForRead(file);
			if (model != null && (model instanceof IDOMModel)) {
				IDOMDocument domDoc = ((IDOMModel) model).getDocument();

				Element element = domDoc.getDocumentElement();
				if ("sqlMap".equals(element.getTagName())) {
					SqlMapQueryGroupElement queries = new SqlMapQueryGroupElement(element);
					for (Object child : queries.getChildren()) {
						SqlMapCRUDElement mapElement = (SqlMapCRUDElement) child;
						if (matcher.match(mapElement.getId())) {
							found(file, mapElement);
						}
					}
				}
				
				if ("mapper".equals(element.getTagName())) {
					MapperQueryGroupElement queries = new MapperQueryGroupElement(element);
					for (Object child : queries.getChildren()) {
						MapperCRUDElement mapElement = (MapperCRUDElement) child;
						if (matcher.match(mapElement.getId())) {
							found(file, mapElement);
						}
					}
				}
			}
			else
			{
				if("xml".equals(file.getFileExtension())){
					try {
						InputStream stream = file.getContents();						
						DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
						fact.setValidating(false);
						DocumentBuilder builder = fact.newDocumentBuilder();
						builder.setEntityResolver(new EntityResolver() {
							  @Override
							  public InputSource resolveEntity(String arg0, String arg1)
							        throws SAXException, IOException {
							    if(arg0.contains("iBATIS.com")) {
							        return new InputSource(new StringReader(""));
							    } else {
							        return null;
							    }
							  }
							});
						
						Document domDoc = builder.parse(stream);
						
						Element element = domDoc.getDocumentElement();
						if ("sqlMap".equals(element.getTagName())) {
							SqlMapQueryGroupElement queries = new SqlMapQueryGroupElement(element);
							for (Object child : queries.getChildren()) {
								SqlMapCRUDElement mapElement = (SqlMapCRUDElement) child;
								if (matcher.match(mapElement.getId())) {
									found(file, mapElement);
								}
							}
						}
						
						if ("mapper".equals(element.getTagName())) {
							MapperQueryGroupElement queries = new MapperQueryGroupElement(element);
							for (Object child : queries.getChildren()) {
								MapperCRUDElement mapElement = (MapperCRUDElement) child;
								if (matcher.match(mapElement.getId())) {
									found(file, mapElement);
								}
							}
						}
						if (stream != null) stream.close();
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}

		} finally {
			if (model != null) {
				model.releaseFromRead();
			}
		}
	}
/*
	private void found(IFile file, SqlMapCRUDElement element) {
		List<SqlMapCRUDElement> list = result.get(file);
		if (list == null) {
			list = new LinkedList<SqlMapCRUDElement>();
			result.put(file, list);
		}
		list.add(element);
	}
*/
	private void found(IFile file, DOMElementProxy element) {
		List<DOMElementProxy> list = result.get(file);
		if (list == null) {
			list = new LinkedList<DOMElementProxy>();
			result.put(file, list);
		}
		list.add(element);
	}
}
