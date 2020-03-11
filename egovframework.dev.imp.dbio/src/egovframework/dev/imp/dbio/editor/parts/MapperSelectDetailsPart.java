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
package egovframework.dev.imp.dbio.editor.parts;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import egovframework.dev.imp.dbio.common.DbioMessages;
import egovframework.dev.imp.dbio.components.fields.ComboEditField;
import egovframework.dev.imp.dbio.components.fields.ComboField;
import egovframework.dev.imp.dbio.components.fields.FieldEvent;
import egovframework.dev.imp.dbio.components.fields.HyperLinkComboButtonField;
import egovframework.dev.imp.dbio.components.fields.HyperLinkComboEditButtonField;
import egovframework.dev.imp.dbio.components.fields.HyperLinkTextButtonField;
import egovframework.dev.imp.dbio.components.fields.IFieldListener;
import egovframework.dev.imp.dbio.components.fields.TextField;
import egovframework.dev.imp.dbio.editor.model.MapperSelectElement;
import egovframework.dev.imp.dbio.util.StringUtil;

/**
 * Mapper 에디터의 QueryDetails 화면
 * @author 개발환경 개발팀 김효수
 * @since 2019.02.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2019.02.25  김효수          MyBatis DBIO  
 *
 * 
 * </pre>
 */
public class MapperSelectDetailsPart extends MapperQueryDetailsPart {

	private ComboEditField resultClassField; //ComboField -> ComboEditField
	private ComboEditField resultMapField; //ComboField -> ComboEditField
	private TextField resultXmlField;
	private ComboField resultCacheModelField;
	private Button resultRadioMap;
	private Button resultRadioClass;
	private Button resultRadioXml;
	private Composite resultClassCps;
	private Composite resultMapCps;
	private Composite resultXmlCps;
	private Composite resultCacheModelCps;

	private boolean refreshing = false;

	private final IFieldListener listener = new IFieldListener() {
		public void eventOccured(FieldEvent event) {
			switch (event.getType()) {
			case ButtonSelected:
				buttonSelected(event.getSource());
				break;
			case HyperLinkActivated:
				linkActivated(event.getSource());
				break;
			case TextChanged:
				textChanged(event.getSource());
				break;
			default :
				break;
			}
		}
	};

	/**
	 * 생성자
	 * 
	 * @param masterPart
	 */
	public MapperSelectDetailsPart(MapperMasterPart masterPart) {
		super(masterPart);
		this.setResultclassField();
		this.setResultMapField();
		this.setResultXmlField();
		this.setResultCacheModelField();
	}

	private void setResultclassField() {
		//this.resultClassField = new HyperLinkComboButtonField("Type:", "Browse");
		this.resultClassField = new HyperLinkComboEditButtonField("Type:", "Browse"); //ComboField -> ComboEditField : new : HyperLinkComboButtonField -> HyperLinkComboEditButtonField
		//this.resultClassField;
	}

	private void setResultMapField() {
		this.resultMapField = new ComboEditField("Map:"); //ComboField -> ComboEditField
	}
	
	private void setResultXmlField() {
		this.resultXmlField = new TextField("Name:");
	}
	
	private void setResultCacheModelField() {
		this.resultCacheModelField = new ComboField("CacheModel:");
	}

	/**
	 * 화면 구성
	 */
	@Override
	protected void createMapContents(FormToolkit toolkit, Composite parent, int cols) {
		super.createMapContents(toolkit, parent, cols);

		MouseAdapter resultRadioListener = new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				String resultType = (String) e.widget.getData();

				if (CLASS.equals(resultType)) {
					//ComboField -> ComboEditField
					resultClassField.setEnabled(true);
					resultMapField.setFocus();
					resultMapField.setText("");
					resultMapField.setEnabled(false);
					/*
					resultXmlField.setFocus();
					resultXmlField.setText("");
					resultXmlField.setEditable(false);
					*/					
					resultClassCps.setVisible(true);
					resultMapCps.setVisible(false);
					/*
					resultXmlCps.setVisible(false);
					*/
				} else if (MAP.equals(resultType)){
					resultMapField.setEnabled(true);
					
					resultClassField.setFocus();
					resultClassField.setText("");
					//ComboField -> ComboEditField
					resultClassField.setEnabled(false);
					/*
					resultXmlField.setFocus();
					resultXmlField.setText("");
					resultXmlField.setEditable(false);
					*/
					resultClassCps.setVisible(false);
					resultMapCps.setVisible(true);
					/*
					resultXmlCps.setVisible(false);
					*/
				}/* 
				  else if (XML.equals(resultType)){
					resultXmlField.setEditable(true);
					
					resultClassField.setFocus();
					resultClassField.setText("");
					resultClassField.setEnabled(false);
					resultMapField.setFocus();
					resultMapField.setText("");
					resultMapField.setEnabled(false);
					
					resultClassCps.setVisible(false);
					resultMapCps.setVisible(false);
					resultXmlCps.setVisible(true);
				}*/
			}
		};

		//1행
		Label label = toolkit.createLabel(parent, "Result");
		GridData gd = new GridData();
		//gd.horizontalSpan = 1;
		label.setLayoutData(gd);

		Composite rsltBtnCpst = toolkit.createComposite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(3, false);
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.marginTop = 0;		
		rsltBtnCpst.setLayout(gl);
		gd = new GridData();
		gd.horizontalSpan = cols;
		rsltBtnCpst.setLayoutData(gd);

		resultRadioClass = toolkit.createButton(rsltBtnCpst, CLASS, SWT.RADIO);
		resultRadioClass.setData(CLASS);
		resultRadioClass.addMouseListener(resultRadioListener);

		resultRadioMap = toolkit.createButton(rsltBtnCpst, MAP, SWT.RADIO);
		resultRadioMap.setData(MAP);
		resultRadioMap.addMouseListener(resultRadioListener);
		/*
		resultRadioXml = toolkit.createButton(rsltBtnCpst, XML, SWT.RADIO);
		resultRadioXml.setData(XML);
		resultRadioXml.addMouseListener(resultRadioListener);
		*/
		
		//2행
		label = toolkit.createLabel(parent, "");

		Composite resultClsMapXmlCps = toolkit.createComposite(parent, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = cols -1;
		resultClsMapXmlCps.setLayoutData(gd);
		
		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 0;
		formLayout.marginHeight = 0;
		formLayout.marginTop = 0;
		resultClsMapXmlCps.setLayout(formLayout);
		
		FormData fd = new FormData();		
	    fd.top = new FormAttachment(0, 0);
	    fd.left = new FormAttachment(0, 0);
	    fd.bottom = new FormAttachment(100, 0);
	    fd.right = new FormAttachment(100, 0);		
		resultClassCps = toolkit.createComposite(resultClsMapXmlCps, SWT.NONE);
		resultClassCps.setLayoutData(fd);		
		toolkit.paintBordersFor(resultClassCps);
		
		fd = new FormData();		
	    fd.top = new FormAttachment(0, 3);
	    fd.left = new FormAttachment(0, 0);
	    fd.bottom = new FormAttachment(100, 0);
	    fd.right = new FormAttachment(100, 0);			
		resultMapCps = toolkit.createComposite(resultClsMapXmlCps, SWT.NONE);
		resultMapCps.setLayoutData(fd);
		toolkit.paintBordersFor(resultMapCps);
		/*
		fd = new FormData();
		fd.top = new FormAttachment(0, 3);
		fd.left = new FormAttachment(0, 0);
		fd.bottom = new FormAttachment(100, 0);
		fd.right = new FormAttachment(100, 0);
		resultXmlCps = toolkit.createComposite(resultClsMapXmlCps, SWT.NONE);
		resultXmlCps.setLayoutData(fd);
		toolkit.paintBordersFor(resultXmlCps);
		*/
		gl = new GridLayout(3, false);
		gl.marginBottom = 1;
		gl.marginHeight = 0;
		gl.marginTop = 1;
		
		resultClassCps.setLayout(gl);
		resultMapCps.setLayout(gl);
		/*
		resultXmlCps.setLayout(gl);
		*/
		resultClassField.create(toolkit, resultClassCps, 3);
		resultMapField.create(toolkit, resultMapCps, 3);
		/*
		resultXmlField.create(toolkit, resultXmlCps, 3);
        */
		//3행
		/*
		resultCacheModelCps = toolkit.createComposite(parent, SWT.NONE);
		toolkit.paintBordersFor(resultCacheModelCps);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = cols;
		resultCacheModelCps.setLayoutData(gd);
		gl = new GridLayout(3, false);
		gl.marginBottom = 1;
		gl.marginHeight = 1;
		gl.marginTop = 0;		
		resultCacheModelCps.setLayout(gl);		
		
		label = toolkit.createLabel(resultCacheModelCps, "");
		gd = new GridData();
		gd.widthHint = 60;
		label.setLayoutData(gd);
		
		resultCacheModelField.create(toolkit, resultCacheModelCps, 2);
        */
		resultClassField.addFieldListener(listener);
		resultMapField.addFieldListener(listener);
		/*
		resultXmlField.addFieldListener(listener);
		resultCacheModelField.addFieldListener(listener);
		*/
	}

	@Override
	public void refresh() {
		try {
			refreshing = true;
			super.refresh();
			loadResultMaps();
			/*
			loadResultCacheModel();
			*/
			MapperSelectElement element = (MapperSelectElement) currentElement;
			//ComboField -> ComboEditField
			resultClassField.setItems(masterPart.getInitialTypes());
			String resultClassText = StringUtil.nvl(element.getResultClass());
			if("".equals(resultClassText)) {
				resultClassText = DbioMessages.mapper_display_Type_guide;
			}
			resultClassField.setText(resultClassText);
			String resultMapText = StringUtil.nvl(element.getResultMap());
			if("".equals(resultMapText)) {
				resultMapText = DbioMessages.mapper_display_Type_guide;
			}
			resultMapField.setText(resultMapText);
			/*
			resultXmlField.setText(StringUtil.nvl(element.getResultXml()));
			*/
			//2017-06-17 In/Out에서  Result 설정에 따른 라디오 버튼(class, map, xml) 설정 수정 (sh.jang)
			if ((element.getResultClass() == null 
					&& element.getResultMap() == null
					&& element.getResultXml() == null) || 
					("".equals(element.getResultClass()) 
							&& "".equals(element.getResultMap()) 
							&& "".equals(element.getResultXml()))) {
				
				resultRadioClass.setSelection(true);
				//ComboField -> ComboEditField
				resultClassField.setEnabled(true);
				resultClassCps.setVisible(true);
				
				resultRadioMap.setSelection(false);
				resultMapField.setEnabled(false);
				resultMapCps.setVisible(false);
				/*
				resultRadioXml.setSelection(false);
				resultXmlField.setEditable(false);				
				resultXmlCps.setVisible(false);
				*/
			} else if (element.getResultMap() != null && !"".equals(element.getResultMap())) {

				resultRadioClass.setSelection(false);
				//ComboField -> ComboEditField
				resultClassField.setEnabled(false);
				resultClassCps.setVisible(false);
				
				resultRadioMap.setSelection(true);
				resultMapField.setEnabled(true);
				resultMapCps.setVisible(true);
				/*
				resultRadioXml.setSelection(false);
				resultXmlField.setEditable(false);
				resultXmlCps.setVisible(false);
				*/
			} else if (element.getResultClass() != null && !"".equals(element.getResultClass())){
			
				if (element.getResultXml() == null || "".equals(element.getResultXml())){
					
					resultRadioClass.setSelection(true);
					//ComboField -> ComboEditField
					resultClassField.setEnabled(true);
					resultClassCps.setVisible(true);
					
					resultRadioMap.setSelection(false);
					resultMapField.setEnabled(false);
					resultMapCps.setVisible(false);
					/*
					resultRadioXml.setSelection(false);
					resultXmlField.setEditable(false);
					resultXmlCps.setVisible(false);
					*/
				} else {					
					
					resultRadioClass.setSelection(false);
					//ComboField -> ComboEditField
					resultClassField.setEnabled(false);
					resultClassCps.setVisible(false);
					
					resultRadioMap.setSelection(false);
					resultMapField.setEnabled(false);
					resultMapCps.setVisible(false);
					/*					
					resultRadioXml.setSelection(true);
					resultXmlField.setEditable(true);				
					resultXmlCps.setVisible(true);
					*/
				}
			}
            /*
			resultCacheModelField.setText(StringUtil.nvl(element.getResultCacheModel()));
			*/
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			refreshing = false;
		}
	}

	/**
	 * ResultMap 정보 표시
	 */
	private void loadResultMaps() {
		List<String> resultMaps = new LinkedList<String>();
		Element root = (Element) currentElement.getDOMElement().getParentNode();
		NodeList children = root.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element
					&& "resultMap".equals(((Element) child).getTagName())) {
				String id = ((Element) child).getAttribute("id"); //$NON-NLS-1$
				if (id != null && !"".equals(id.trim())) {
					resultMaps.add(id);
				}
			}
		}
		String[] items = resultMaps.toArray(new String[resultMaps.size()]);
		resultMapField.setItems(items);
	}

	/**
	 * Result CacheModel 정보 표시
	 */
	private void loadResultCacheModel() {
		List<String> resultCacheModels = new LinkedList<String>();
		Element root = (Element) currentElement.getDOMElement().getParentNode();
		NodeList children = root.getChildNodes();
		resultCacheModels.add("");
		
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element
					&& "cacheModel".equals(((Element) child).getTagName())) {
				String id = ((Element) child).getAttribute("id"); //$NON-NLS-1$
				if (id != null && !"".equals(id.trim())) {
					resultCacheModels.add(id);
				}
			}
		}
		String[] items = resultCacheModels.toArray(new String[resultCacheModels.size()]);
		resultCacheModelField.setItems(items);
	}

	@Override
	protected void buttonSelected(Object source) {
		if (source == resultClassField) {
			String newClass = masterPart.getPage().selectClass();
			if (newClass != null && !newClass.equals(resultClassField.getText())) {
				resultClassField.setText(newClass);
				MapperSelectElement element = (MapperSelectElement) currentElement;
				element.setResultClass(newClass);
			}
		} else {
			super.buttonSelected(source);
		}
	}

	@Override
	protected void linkActivated(Object source) {
		if (source == resultClassField) {
			String newClass = masterPart.getPage().openOrCreateNewJavaClass(resultClassField.getText());
			if (newClass != null && !newClass.equals(resultClassField.getText())) {
				resultClassField.setText(newClass);
				MapperSelectElement element = (MapperSelectElement) currentElement;
				element.setResultClass(newClass);
			}
		} else {
			super.linkActivated(source);
		}
	}

	@Override
	protected void textChanged(Object source) {
		super.textChanged(source);
		if (!refreshing) {
			MapperSelectElement element = (MapperSelectElement) currentElement;
			if (source == resultClassField) {
				element.setResultClass(resultClassField.getText());
			} else if (source == resultMapField) {
				element.setResultMap(resultMapField.getText());
			}/* else if (source == resultXmlField) {
				element.setResultXml(resultXmlField.getText());
			} else if (source == resultCacheModelField) {
				element.setResultCacheModel(resultCacheModelField.getText());
			}
			*/
		}
	}
}
