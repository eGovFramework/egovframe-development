/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package egovframework.dev.imp.confmngt.properties;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.dialogs.PropertyPage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import egovframework.dev.imp.confmngt.EgovConfMngtPlugin;
import egovframework.dev.imp.confmngt.common.ConfMngtLog;
import egovframework.dev.imp.confmngt.common.ConfMngtMessages;
import egovframework.dev.imp.confmngt.preferences.model.NexusInfo;
import egovframework.dev.imp.confmngt.preferences.model.NexusTableLabelProvider;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.PrefrencePropertyUtil;
import egovframework.dev.imp.core.utils.XmlUtil;

/**
 * nexus repository 정보를 pom.xml에 저장하는 클래스
 * 
 * @author 개발환경 개발팀 조윤정
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 *      개정이력(Modification Information)
 *   
 *   수정일      수정자           수정내용
 *  ------- -------- ---------------------------
 *  2011.06.13  조윤정     최초 생성
 * 
 * 
 * </pre>
 */
public class NexusPropertyPage extends PropertyPage implements
		IWorkbenchPropertyPage {

	private CheckboxTableViewer tableViewer;
	private Node rootNode;
	private NodeList nodeList;

	private List<NexusInfo> nexusInfoList;
	
	public NexusPropertyPage() {
		setDescription(ConfMngtMessages.nexusPropertyPageDESC);
	}

	/**
	 * 해당 Property Page 에 입력한 값을 저장할 프로젝트를 리턴함
	 */
	private IProject getSelectedProject() {
		IAdaptable element = getElement();
		if (element instanceof IProject) {
			return (IProject) element;
		}
		Object resource = element.getAdapter(IResource.class);
		if (resource instanceof IProject) {
			return (IProject) resource;
		}
		return null;
	}
	
	private void loadNexusPreference() {
		nexusInfoList = new ArrayList<NexusInfo>();

		int nexusCnt = EgovConfMngtPlugin.getDefault().getPreferenceStore()
				.getInt("nexusCount"); //$NON-NLS-1$

		if (nexusCnt > 0)
			for (int i = 0; i < nexusCnt; i++) {
				NexusInfo nexusInfo = new NexusInfo();
				nexusInfo.setId(String.valueOf(i));

				NexusInfo nexusInfoForEach = (NexusInfo) PrefrencePropertyUtil
						.loadPreferences(EgovConfMngtPlugin.getDefault(),
								nexusInfo);
				nexusInfoList.add(nexusInfoForEach);
			}
	}
	
	private void createLink(Composite parent, String message,
			final String pageId) {
		Link preferencesLink = new Link(parent, SWT.RIGHT);
		preferencesLink.setLayoutData(new GridData(SWT.END, SWT.CENTER, false,
				false));
		preferencesLink.setText(message);
		preferencesLink.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				PreferencesUtil.createPreferenceDialogOn(
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow().getShell(), pageId,
								new String[] { "egovframework.dev.imp.confmngt.preferences.nexuspreferencepage" }, //$NON-NLS-1$
								null).open();
				
				List<String> checkedList = new ArrayList<String>();

				for (int jnx = 0; jnx < nexusInfoList.size(); jnx++) {
					if(tableViewer.getChecked(nexusInfoList.get(jnx)))
						checkedList.add(nexusInfoList.get(jnx).getNexusId());
				
				}
				loadNexusPreference();
				tableViewer.setInput(nexusInfoList);
				
				//저장된 checked 정보를 tableviewer에 반영
				for (int jnx = 0; jnx < nexusInfoList.size(); jnx++) {
					if(checkedList.contains(nexusInfoList.get(jnx).getNexusId())){
						tableViewer.setChecked((NexusInfo) nexusInfoList.get(jnx), true);
					}
				}
			}
		});
	}
	
	/**
	 * Nexus Property Page의 Table Viewer를 구성하는 기본 메소드
	 * 
	 * @param parent
	 * @return
	 * @throws Exception
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	public Control createContents(Composite parent) {
		noDefaultAndApplyButton();

		Composite container = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		GridData gridData = new GridData(GridData.FILL_BOTH);

		container.setLayout(layout);
		container.setLayoutData(gridData);

		createLink(
				container,
				ConfMngtMessages.nexusPropertyPageLINK,
				"egovframework.dev.imp.confmngt.preferences.nexuspreferencepage" //$NON-NLS-1$
				);

		tableViewer = CheckboxTableViewer.newCheckList(container, SWT.TOP
				| SWT.BORDER);

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		String[] columnNames = new String[] { ConfMngtMessages.nexusPropertyPageID, ConfMngtMessages.nexusPropertyPageURL, ConfMngtMessages.nexusPropertyPageRELEASE,
				ConfMngtMessages.nexusPropertyPageSNAPSHOTS };
		int[] columnWidth = new int[] { 80, 360, 75, 75 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT, SWT.CENTER,
				SWT.CENTER };

		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(table,
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidth[i]);
		}

		loadNexusPreference();

		tableViewer.setLabelProvider(new NexusTableLabelProvider());
		tableViewer.setContentProvider(new ArrayContentProvider());
		// TableViewer 로드 시 Preference Store에 있는 값들을 Set
		tableViewer.setInput(nexusInfoList);

		try {

			if (!getSelectedProject().getFile("pom.xml").exists()) { //$NON-NLS-1$
				setMessage(ConfMngtMessages.nexusPropertyPageNOTEXIST, WARNING);

			} else {
				rootNode = XmlUtil.getRootNode(new File(getSelectedProject()
						.getFile("pom.xml").getLocation().toOSString())); //$NON-NLS-1$

				nodeList = XmlUtil.getNodeList(rootNode,
						"/project/repositories/repository"); //$NON-NLS-1$
			}

		} catch (Exception e) {
			ConfMngtLog.logError(e);
		}

		// TableViewer 로드 시 선택한 프로젝트에 적용되어있는 Nexus 정보를 pom.xml에서 읽어와서 Check 표시
		if (NullUtil.isNull(nodeList)) {
			tableViewer.setInput(nexusInfoList);

		} else {
			for (int inx = 0; inx < nodeList.getLength(); inx++) {
				try {
					Node targetRepository = nodeList.item(inx);
					Node nexusIdInXml = XmlUtil.getNode(targetRepository,
							"./id"); //$NON-NLS-1$

					for (int jnx = 0; jnx < nexusInfoList.size(); jnx++) {
						if (nexusInfoList.get(jnx).getNexusId()
								.equals(nexusIdInXml.getTextContent()))

							tableViewer.setChecked(
									(NexusInfo) nexusInfoList.get(jnx), true);
					}

				} catch (Exception e) {
					ConfMngtLog.logError(e);
				}
			}

		}
		return container;
	}



	/**
	 * @param node
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 * @throws CoreException
	 */
	private void createXmlFile(Node node) {
		try {
			IFile originf = getSelectedProject().getFile(new Path("pom.xml")); //$NON-NLS-1$
			InputStream inStream = new ByteArrayInputStream(XmlUtil
					.getXmlString(node, "/").getBytes("UTF-8")); //$NON-NLS-1$ //$NON-NLS-2$
			originf.setContents(inStream, true, false, null);

		} catch (CoreException e) {
			ConfMngtLog.logCoreError(e);
		} catch (UnsupportedEncodingException e) {
			ConfMngtLog.logError(e);
		} catch (Exception e) {
			ConfMngtLog.logError(e);
		}
	}

	/**
	 * 사용자가 선택한 Nexus 정보가 해당 프로젝트의 pom.xml에 존재하지 않을 경우, pom.xml에 사용자가 선택한 Nexus
	 * 정보를 추가한다.
	 * 
	 * @param rootNode
	 * @param nodeList
	 * @throws Exception
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	private void addNexusToXml(Node rootNode, NodeList nodeList) {
		Object[] checkedItems = tableViewer.getCheckedElements();

		try {
			if (!NullUtil.isNull(checkedItems)) {
				for (Object item : checkedItems) {
					String nexusIdInTable = ((NexusInfo) item).getNexusId();
					String nexusUrlInTable = ((NexusInfo) item).getNexusUrl();
					boolean releaseInTable = ((NexusInfo) item).getIsRealeaseSelected();
					boolean snapshotsInTable = ((NexusInfo) item).getIsSnapshotsSelected();
					
					boolean isEqual = false;
					
					for (int inx = 0; inx < (NullUtil.isNull(nodeList) ? 0 : nodeList.getLength()); inx++) {
						Node nexusIdInXml = XmlUtil.getNode(nodeList.item(inx), "./id"); //$NON-NLS-1$
						
						if (nexusIdInTable.equals(nexusIdInXml.getTextContent())) {
							isEqual = true;
							XmlUtil.modifyNodeValue(rootNode, "/project/repositories/repository[id = '" + nexusIdInXml.getTextContent() + "']/url", nexusUrlInTable); //$NON-NLS-1$ //$NON-NLS-2$
							XmlUtil.modifyNodeValue(rootNode, "/project/repositories/repository[id = '" + nexusIdInXml.getTextContent() + "']/releases/enabled", releaseInTable+""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							XmlUtil.modifyNodeValue(rootNode, "/project/repositories/repository[id = '" + nexusIdInXml.getTextContent() + "']/snapshots/enabled", snapshotsInTable+""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							break;
						}
					}
					
					if (!isEqual) {
						String xmlStr = "<repository>" + "\n\t\t\t<id>" //$NON-NLS-1$ //$NON-NLS-2$
								+ nexusIdInTable + "</id>" + "\n\t\t\t<url>" //$NON-NLS-1$ //$NON-NLS-2$
								+ nexusUrlInTable + "</url>" //$NON-NLS-1$
								+ "\n\t\t\t<releases>\n\t\t\t\t<enabled>" //$NON-NLS-1$
								+ releaseInTable
								+ "</enabled>\n\t\t\t</releases>" //$NON-NLS-1$
								+ "\n\t\t\t<snapshots>\n\t\t\t\t<enabled>" //$NON-NLS-1$
								+ snapshotsInTable
								+ "</enabled>\n\t\t\t</snapshots>" //$NON-NLS-1$
								+ "\n\t\t</repository>" //$NON-NLS-1$
								+ "\n ";
						
		

						XmlUtil.addFirstNode(rootNode, "/project/repositories", //$NON-NLS-1$
								xmlStr, "\n\t\t", "\t"); //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				createXmlFile(rootNode);
			}
		} catch (TransformerConfigurationException e) {
			ConfMngtLog.logError(e);
		} catch (TransformerFactoryConfigurationError e) {
			ConfMngtLog.logError(e);
		} catch (TransformerException e) {
			ConfMngtLog.logError(e);
		} catch (Exception e) {
			ConfMngtLog.logError(e);
		}
	}
	

	/**
	 * 사용자가  선택 해제한 Nexus정보를 pom.xml에서 삭제한다.
	 * 
	 * @param rootNode
	 * @param nodeList
	 * @throws Exception
	 */
	private void removeNexusFromXml(Node rootNode, NodeList nodeList) {
		try {
			int itemCnt = tableViewer.getTable().getItemCount();

			for (int inx = 0; inx < itemCnt; inx++) {
				boolean isEqual = false;
				Node nexusIdInXml = null;
				
				if (!tableViewer.getChecked(tableViewer.getElementAt(inx))) {
					String nexusIdInTable = ((NexusInfo) tableViewer.getElementAt(inx)).getNexusId();
					
					for (int jnx = 0; jnx < (NullUtil.isNull(nodeList) ? 0 : nodeList.getLength()); jnx++) {
						nexusIdInXml = XmlUtil.getNode(nodeList.item(jnx), "./id");
						if (nexusIdInTable.equals(nexusIdInXml.getTextContent())) {
							isEqual = true;
							break;
						}					
					}
				}

				if (isEqual)
					XmlUtil.removeNode(rootNode,
							"/project/repositories/repository[id = '" + nexusIdInXml.getTextContent() + "']"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			createXmlFile(rootNode);
		} catch (Exception e) {
			ConfMngtLog.logError(e);
		}
	}


	/**
	 * Nexus Property Page에서 OK 버튼 클릭시 수행되는 메소드
	 * 
	 * @return
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk() {
		try {
			rootNode = XmlUtil.getRootNode(new File(getSelectedProject()
					.getFile("pom.xml").getLocation().toOSString())); //$NON-NLS-1$

			if (!XmlUtil.existNode(rootNode, "/project/repositories")) {//$NON-NLS-1$
				String xmlStr = "<repositories>\n\t</repositories>"; //$NON-NLS-1$

				Node findNode = XmlUtil.getNode(rootNode, "/project/dependencies");
				
				if (findNode == null) {
					//repositories 바로 앞에 repositories 위치
					XmlUtil.addNode(rootNode, "/project", xmlStr, "\t", "\n");
				}else{
					//dependencies 바로 앞에 repositories 위치
				    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				    DocumentBuilder builder = factory.newDocumentBuilder();
				    Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
				    Node node = doc.getDocumentElement();
				    
				    Document ownerDoc = rootNode.getOwnerDocument();
				    Node importedNode = ownerDoc.importNode(node, true);
//				    Text preValueText = ownerDoc.createTextNode("");
				    Text postValueText = ownerDoc.createTextNode("\n\t");					

				    
//				    rootNode.insertBefore(preValueText, findNode);
					rootNode.insertBefore(importedNode, findNode);
					rootNode.insertBefore(postValueText, findNode);
				}
				createXmlFile(rootNode);
			}

			nodeList = XmlUtil.getNodeList(rootNode,
					"/project/repositories/repository"); //$NON-NLS-1$

			addNexusToXml(rootNode, nodeList);
			removeNexusFromXml(rootNode, nodeList);

		} catch (Exception e) {
			ConfMngtLog.logError(e);
		}
		return true;
	}
}
