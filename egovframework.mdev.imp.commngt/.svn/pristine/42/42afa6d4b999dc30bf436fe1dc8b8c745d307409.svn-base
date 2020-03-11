package egovframework.mdev.imp.commngt.wizards.pages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PatternFilter;

import egovframework.mdev.imp.commngt.MobileComMngtPlugin;
import egovframework.mdev.imp.commngt.common.MobileComMngtMessages;
import egovframework.mdev.imp.commngt.util.FilteredCheckboxTree;
import egovframework.mdev.imp.commngt.util.FilteredCheckboxTree.FilterableCheckboxTreeViewer;
import egovframework.mdev.imp.commngt.util.FormBrowser;
import egovframework.mdev.imp.commngt.wizards.model.Category;
import egovframework.mdev.imp.commngt.wizards.model.Component;
import egovframework.mdev.imp.commngt.wizards.model.ComponentElementFactory;
import egovframework.mdev.imp.commngt.wizards.model.IComponentElement;
import egovframework.mdev.imp.commngt.wizards.model.MobileCommngtContext;

/**
 * 공통컴포넌트 선택 마법사페이지 클래스
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
public class SelectCommonComponentPage extends WizardPage implements ModifyListener {

	/** 컴포넌트 체크박스트리  뷰어*/
	private FilterableCheckboxTreeViewer treev = null;
	/** 컴포넌트 체크박스트리 */
	private FilteredCheckboxTree tree = null;

	/** 폼브라우저 */
	protected FormBrowser descriptionBrowser;
	/** 공통컴포넌트 컨텍스트 */
	private final MobileCommngtContext context;
	/** 공통컴포넌트 팩토리 */
	private final ComponentElementFactory factory;
	/** 컴포넌트 contentProvider */
	private final ComponentContentProvider contentProvider = new ComponentContentProvider();
	/** dependency 정보 */
	private Text dependencyInfo = null;
	/** Dialog에 띄울 메시지(Depend된 컴포넌트) */
	private String descDialog = ""; //$NON-NLS-1$
	/** 프로젝트 명 (선택한 프로젝트가 변경되었는지 비교시 사용) */
	private String compareProjectName = ""; //$NON-NLS-1$
	/** 선택 프로젝트 명 */
	private Label projectName = null;

	/**
	 * 생성자
	 */
	public SelectCommonComponentPage(String pageName, MobileCommngtContext context) {
		super(pageName);
		this.context = context;
		setTitle(MobileComMngtMessages.selectComponentPagepageTitle);
		setDescription(MobileComMngtMessages.selectComponentPagepageDescription);
		descriptionBrowser = new FormBrowser(SWT.BORDER | SWT.V_SCROLL);
		factory = ComponentElementFactory.getInstance();
	}

	/**
	 * 초기화
	 */
	@SuppressWarnings("unchecked")
	public void init() {

		List<IComponentElement> elements = (List<IComponentElement>) treev.getInput();
		if (elements == null)
			return;
		//treev.collapseAll();

		for (IComponentElement catElement : elements) {
			if (catElement instanceof Category) {
				List<IComponentElement> components = ((Category) catElement).getChildren();

				for (IComponentElement comElement : components) {
					if (comElement instanceof Component && comElement.isCreatedComponent()) {
						//treev.setGrayChecked(comElement, true);
						treev.setGrayed(comElement, true);
						treev.setChecked(comElement, true);
						//이미 설치된 컴포넌트가 포함된 카테고리만 펼치기
						treev.expandToLevel(catElement, 1);
						//기존에 설치한 컴포넌트 중 dependency 컴포넌트를 임의로 지웠을때
						List<Component> dependencylist = comElement.getDependencyPackage();
						for (Component dependency : dependencylist) {
							//dependency 중 - 이미 설치된 컴포넌트가 아니고 & 체크되어 있는지 않으면 - 자동체크
							if (!dependency.isCreatedComponent() && !treev.getChecked(dependency)) {
								treev.setChecked(dependency, true);
								//직접 체크 여부 - setSelection
								dependency.setSelection(false);
							}
						}

					}
				}
			}
		}

		parentNodeCheck();
		getCheckedComponents();
		projectName.forceFocus();

	}

	/** 페이지 완료 여부 */
	public boolean isPageComplete() {
		//		if(context.getComponent()!= null && context.getComponent().size() > 0){}
		//		else	setErrorMessage(ComMngtMessages.selectComponentPagepageDescription);
		return (context.getComponent() != null && context.getComponent().size() > 0);
	}

	/** 텍스트 편집 */
	public void modifyText(ModifyEvent e) {
		getWizard().getContainer().updateButtons();
	}

	/**
	 * 화면단 컨트롤 생성
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.verticalSpacing = 10;
		container.setLayout(layout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		createSelectComponentControls(container);

		setControl(container);
		updatePageComplete();

	}

	/**
	 * 컴포넌트 선택 컨트롤 생성
	 */
	public void createSelectComponentControls(Composite container) {

		Label label = new Label(container, SWT.NONE);
		label.setText(MobileComMngtMessages.selectComponentPage3);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);
		projectName = new Label(container, SWT.WRAP | SWT.BOLD);
		projectName.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projectName.setLayoutData(gd);

		/* 컴포넌트 트리 + 설명*/
		SashForm sashForm = new SashForm(container, SWT.HORIZONTAL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		//gd.widthHint = 300;
		gd.verticalSpan = 2;
		sashForm.setLayoutData(gd);

		Group treeGroup = new Group(sashForm, SWT.NONE);
		treeGroup.setLayout(new GridLayout());

		PatternFilter filter = new PatternFilter();
		filter.setIncludeLeadingWildcard(true);

		tree = new FilteredCheckboxTree(treeGroup, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL, filter);
		/* 컴포넌트 트리*/
		treev = (FilterableCheckboxTreeViewer) tree.getViewer();
		treev.setContentProvider(contentProvider);
		treev.setLabelProvider(new ComponentLabelProvider());
		treev.getTree().setLinesVisible(true);
		treev.getTree().setHeaderVisible(false);

		/* 우측 설명 부분*/
		SashForm sashFormSub = new SashForm(sashForm, SWT.VERTICAL);
		gd = new GridData(GridData.FILL_BOTH);
		sashFormSub.setLayoutData(gd);

		/* Description*/
		Group descriptionGroup = new Group(sashFormSub, SWT.NONE);
		descriptionGroup.setLayout(new GridLayout());
		descriptionGroup.setText(MobileComMngtMessages.selectComponentPage0);
		createDescriptionIn(descriptionGroup);

		/* Dependency*/
		Group dependencyGroup = new Group(sashFormSub, SWT.NONE);
		dependencyGroup.setLayout(new GridLayout());
		dependencyGroup.setText(MobileComMngtMessages.selectComponentPage1);

		dependencyInfo = new Text(dependencyGroup, SWT.BOLD | SWT.READ_ONLY | SWT.MULTI | SWT.FILL);
		dependencyInfo.setLayoutData(gd);
		dependencyInfo.setText("- N/A"); //$NON-NLS-1$

		treev.addSelectionChangedListener(new componentSelectionChangedListener());
		treev.addCheckStateListener(new componentCheckStateListener());

		Dialog.applyDialogFont(container);

		Label note = new Label(container, SWT.BOLD | SWT.TOP);
		note.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		note.setText("Note:\n "); //$NON-NLS-1$
		GridData gd2 = new GridData();
		gd2.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
		note.setLayoutData(gd2);
		GridData gd4 = new GridData();
		gd4.verticalAlignment = GridData.CENTER;
		gd4.horizontalSpan = 2;
		gd4.heightHint = -1;
		gd4.horizontalIndent = 0;
		gd4.horizontalAlignment = GridData.FILL;
		Label label1 = new Label(container, SWT.NONE);
		label1.setText(MobileComMngtMessages.selectComponentPage2);
		label1.setLayoutData(gd4);

		Label tmptmp3 = new Label(container, SWT.NONE);
		tmptmp3.setText("");
		GridData tmp_gd13 = new GridData();
		tmptmp3.setLayoutData(tmp_gd13);

		Label tmptmp4 = new Label(container, SWT.NONE);
		tmptmp4.setText(MobileComMngtMessages.selectComponentPage7);
		GridData tmp_gd14 = new GridData();
		tmp_gd14.horizontalSpan = 2;
		tmp_gd14.horizontalAlignment = SWT.LEFT;
		tmp_gd14.verticalAlignment = SWT.BOTTOM;
		tmptmp4.setLayoutData(tmp_gd14);

		/** 범례 
		Button tmp1 = new Button(container, SWT.CHECK);
		tmp1.setText(""); //$NON-NLS-1$
		tmp1.setEnabled(false);
		GridData tmp_gd11 = new GridData();
		tmp_gd11.horizontalAlignment = SWT.RIGHT;
		tmp_gd11.verticalAlignment = SWT.BOTTOM;
		tmp_gd11.heightHint = 12;
		tmp1.setLayoutData(tmp_gd11);
		
		Label tmp_desc1 = new Label(container, SWT.NONE);
		tmp_desc1.setText(ComMngtMessages.selectComponentPage4);
		GridData tmp_gd12 = new GridData();
		tmp_gd12.horizontalSpan = 2;
		tmp_gd12.verticalAlignment = SWT.BOTTOM;
		tmp_gd12.heightHint = 12;
		tmp_desc1.setLayoutData(tmp_gd12);
		
		Button tmp2 = new Button(container, SWT.CHECK);
		tmp2.setSelection(true);
		tmp2.setEnabled(false);
		GridData tmp_gd21 = new GridData();
		tmp_gd21.horizontalAlignment = SWT.RIGHT;
		tmp_gd21.heightHint = 12;
		tmp2.setLayoutData(tmp_gd21);
		
		Label tmp_desc2 = new Label(container, SWT.NONE);
		tmp_desc2.setText(ComMngtMessages.selectComponentPage5);
		GridData tmp_gd22 = new GridData();
		tmp_gd22.horizontalSpan = 2;
		tmp_gd22.heightHint = 12;
		tmp_desc2.setLayoutData(tmp_gd22);	

		Button tmp3 = new Button(container, SWT.CHECK);
		tmp3.setGrayed(true);
		tmp3.setSelection(true);
		tmp3.setEnabled(false);
		GridData tmp_gd31 = new GridData();
		tmp_gd31.horizontalAlignment = SWT.RIGHT;
		tmp_gd31.verticalAlignment = SWT.TOP;
		tmp_gd31.heightHint = 12;
		tmp3.setLayoutData(tmp_gd31);
		
		Label tmp_desc3 = new Label(container, SWT.NONE);
		tmp_desc3.setText(ComMngtMessages.selectComponentPage6);
		GridData tmp_gd32 = new GridData();
		tmp_gd32.horizontalSpan = 2;
		tmp_gd32.verticalAlignment = SWT.TOP;
		tmp_gd32.heightHint = 12;
		tmp_desc3.setLayoutData(tmp_gd32);	
		*/
		Label tmptmp = new Label(container, SWT.NONE);
		tmptmp.setText("");
		GridData tmp_gd11 = new GridData();
		tmptmp.setLayoutData(tmp_gd11);

		Label tmptmp2 = new Label(container, SWT.NONE);
		tmptmp2.setImage(MobileComMngtPlugin.getDefault().getImage(MobileComMngtPlugin.imgCommngtRemarks));
		GridData tmp_gd12 = new GridData();
		tmp_gd12.horizontalSpan = 2;
		tmp_gd12.horizontalAlignment = SWT.LEFT;
		tmp_gd12.verticalAlignment = SWT.BOTTOM;
		tmptmp2.setLayoutData(tmp_gd12);

	}

	/** Description 폼브라우저 생성 */
	public void createDescriptionIn(Composite composite) {
		descriptionBrowser.createControl(composite);
		Control c = descriptionBrowser.getControl();
		GridData gd = new GridData(GridData.FILL_BOTH);
		c.setLayoutData(gd);
		descriptionBrowser.setText("- N/A"); //$NON-NLS-1$
	}

	/** 페이지 완결성 체크 */
	private void updatePageComplete() {
		setPageComplete(false);
		// 페이지의 완결성 체크
		if (isPageComplete())
			return;
		// 페이지의 완결성 체크를 건너 뛰었다면 페이지를 완료상태로 변경
		setPageComplete(true);
		setMessage(null);
		setErrorMessage(null);
	}

	/** 마법사페이지 종료 */
	public void dispose() {
		super.dispose();
	}

	/** TreeViewer에서 사용할 LabelProvider*/
	private class ComponentLabelProvider extends LabelProvider implements IColorProvider {
		final private Color gray = new Color(Display.getCurrent(), 110, 110, 110);

		@Override
		public Image getImage(Object element) {
			return super.getImage(element);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof IComponentElement) {
				if (((IComponentElement) element).isCreatedComponent()) {
					return ((IComponentElement) element).getName() + "[Already Exist]"; //$NON-NLS-1$
				} else {
					return ((IComponentElement) element).getName();
				}
			}
			return super.getText(element);
		}

		public Color getForeground(Object element) {
			if (element == null)
				return null;
			if (((IComponentElement) element).isCreatedComponent()) {
				return gray;
			}
			return null;
		}

		public Color getBackground(Object element) {
			return null;
		}

		public void dispose() {
			gray.dispose();
		}
	}

	/** 공통컴포넌트 트리 Selection Changed Listener*/
	private class componentSelectionChangedListener implements ISelectionChangedListener {
		@SuppressWarnings("unchecked")
		public void selectionChanged(SelectionChangedEvent event) {
			if (event.getSelection() == null)
				return;
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			Object selected = selection.getFirstElement();
			if (selected == null)
				return;
			if (((IComponentElement) selected).getDesc() == null)
				descriptionBrowser.setText(""); //$NON-NLS-1$
			else
				descriptionBrowser.setText(((IComponentElement) selected).getDesc() == null ? "" : ((IComponentElement) selected).getDesc()); //$NON-NLS-1$

			String dString = ""; //$NON-NLS-1$
			if (selected instanceof Component) {
				List<Component> dependencylist = ((Component) selected).getDependencyPackage();
				if (dependencylist != null) {
					for (int i = 0; i < dependencylist.size(); i++) {
						if (i == 0) {
							dString = dependencylist.get(i).getName() == "" ? "- N/A" : "- " + dependencylist.get(i).getParentName() + ">" + dependencylist.get(i).getName(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						} else {
							dString = dString + "\n- " + dependencylist.get(i).getParentName() + ">" + dependencylist.get(i).getName(); //$NON-NLS-1$
						}
					}
				} else {
					dString = "- N/A"; // N/A //$NON-NLS-1$
				}
			} else {
				dString = "- N/A"; // N/A //$NON-NLS-1$
			}
			dependencyInfo.setText(dString);
		}
	}

	/** 공통컴포넌트 트리 Check State Listener*/
	private class componentCheckStateListener implements ICheckStateListener {
		public void checkStateChanged(CheckStateChangedEvent event) {
			//----------------성능향상 0929--------------------
			if (event == null)
				return;

			final Object eventObj = event.getElement();

			if (event.getChecked()) {
				//체크 event - 부모, 자식 둘다
				//---------------------------------------------
				Runnable runnable = new Runnable() {
					public void run() {
						//---------------------------------------------	
						if (eventObj instanceof Category) {
							//자식노드 체크
							treev.setSubtreeChecked(eventObj, true);
							int exist_cnt = 0;
							for (IComponentElement children : ((Category) eventObj).getChildren()) {
								//if(children instanceof Component && !treev.getGrayed(children)){
								if (children instanceof Component) {
									treev.setGrayed(children, false);
									//직접 체크 여부 - setSelection
									((Component) children).setSelection(true);
									//dependency 컴포넌트 체크
									checkDependency((Component) children);
									if (children.isCreatedComponent()) {
										exist_cnt++;
									}
								}
							}

							if (exist_cnt > 0) {
								MessageDialog.openInformation(getShell(), MobileComMngtMessages.selectComponentPagedialogTitle, MobileComMngtMessages.selectComponentPagedialog1);
							}

						} else {
							//직접 체크 여부 - setSelection
							((Component) eventObj).setSelection(true);

							//Dependency 컴포넌트 체크
							checkDependency((Component) eventObj);

						}

						//---------------------------------------------
					}
				};
				BusyIndicator.showWhile(tree.getDisplay(), runnable);
				//---------------------------------------------		

			} else {
				//카테고리 노드
				if (event.getElement() instanceof Category) {

					//부모 노드 체크해제 event
					//---------------------------------------------
					Runnable runnable = new Runnable() {
						public void run() {
							//---------------------------------------------		
							//부모노드가 gray check 상태이면 그냥 check로 변경
							if (treev.getGrayed((Category) eventObj)) {
								treev.setSubtreeChecked(eventObj, true);
								for (IComponentElement children : ((Category) eventObj).getChildren()) {
									//if(children instanceof Component && !treev.getGrayed(children)){
									if (children instanceof Component) {
										treev.setGrayed(children, false);
										//직접 체크 여부 - setSelection
										((Component) children).setSelection(true);
										//dependency 컴포넌트 체크
										checkDependency((Component) children);
									}
								}
								MessageDialog.openInformation(getShell(), MobileComMngtMessages.selectComponentPagedialogTitle, MobileComMngtMessages.selectComponentPagedialog1);

							} else {

								//자식노드 체크해제
								//treev.setSubtreeChecked(event.getElement(), false);	
								for (IComponentElement comp : ((Category) eventObj).getChildren()) {
									if (comp instanceof Component) {
										if (comp.isCreatedComponent()) {
											if (!treev.getGrayed(comp)) {
												//treev.setGrayChecked(comp, true);
												treev.setGrayed(comp, true);
												treev.setChecked(comp, true);
											}
										} else {
											treev.setGrayed(comp, false);
											treev.setChecked(comp, false);

										}
									}
								}

								//카테고리 체크해제 시 dependency 자동해제는 자식노드의 체크해제를 다 한 후 실행해야함
								boolean msg = false;
								int cnt = 0;
								for (IComponentElement comp2 : ((Category) eventObj).getChildren()) {
									if (comp2 instanceof Component && !comp2.isCreatedComponent()) {
										//직접 체크 여부 - setSelection
										//((Component)event.getElement()).setSelection(false);
										((Component) comp2).setSelection(false);
										//dependency 컴포넌트 체크 해제
										uncheckDependency((Component) comp2);
										//Dependency 로 참조되는  컴포넌트 체크해제 불가 (알리지 않고 체크 남겨둠)
										boolean tmp = ((Component) comp2).isSelection();
										if (checkDependComponent2((Component) comp2, null)) {
											msg = true;
											cnt++;
											treev.setChecked((Component) comp2, true);
											//직접 체크 여부 - setSelection
											((Component) comp2).setSelection(tmp);
										}
									}
								}
								if (msg) {
									//메시지는 자식노드일때와 다름
									MessageDialog.openInformation(getShell(), MobileComMngtMessages.selectComponentPagedialogTitle,
											MobileComMngtMessages.selectComponentPagedialog3 + " " + cnt + MobileComMngtMessages.selectComponentPagedialog4); //$NON-NLS-1$
								}
							}
							//---------------------------------------------
						}
					};
					BusyIndicator.showWhile(tree.getDisplay(), runnable);
					//---------------------------------------------

				} else {
					//자식 노드 체크해제 event
					//---------------------------------------------
					Runnable runnable = new Runnable() {
						@SuppressWarnings("unchecked")
						public void run() {
							//---------------------------------------------	

							//이미 설치된 컴포넌트 해제할때
							if (((Component) eventObj).isCreatedComponent()) {
								if (treev.getGrayed((Component) eventObj)) { //graycheck 상태일때
									//treev.setGrayChecked((Component)eventObj, false);
									treev.setGrayed((Component) eventObj, false);
									treev.setChecked((Component) eventObj, true);
									//직접 체크 여부 - setSelection
									((Component) eventObj).setSelection(true);
									MessageDialog.openInformation(getShell(), MobileComMngtMessages.selectComponentPagedialogTitle,
											MobileComMngtMessages.selectComponentPagedialog1);

								} else {//그냥 check 상태일때
										//treev.setGrayChecked((Component)eventObj, true);
									treev.setGrayed((Component) eventObj, true);
									treev.setChecked((Component) eventObj, true);
									//직접 체크 여부 - setSelection
									((Component) eventObj).setSelection(false);
								}
							} else {

								//Dependency 로 참조되는  컴포넌트 체크해제 불가 알림
								boolean tmp = ((Component) eventObj).isSelection();
								if (checkDependComponent((Component) eventObj, ((Component) eventObj).getDependencyPackage()) > 0) {
									MessageDialog.openInformation(getShell(), MobileComMngtMessages.selectComponentPagedialogTitle,
											MobileComMngtMessages.selectComponentPagedialog5 + " " + descDialog + MobileComMngtMessages.selectComponentPagedialog2); //$NON-NLS-1$
									treev.setChecked((Component) eventObj, true);
									//직접 체크 여부 - setSelection
									((Component) eventObj).setSelection(tmp);
								} else {
									//Dependency 컴포넌트 체크 해제
									uncheckDependency((Component) eventObj);
								}

							}

							//---------------------------------------------
						}
					};
					BusyIndicator.showWhile(tree.getDisplay(), runnable);
					//---------------------------------------------		

				}

			}

			parentNodeCheck();
			//선택한 컴포넌트 컨텍스트에 담기
			getCheckedComponents();
		}
	}

	/** 부모노드 자동 체크 - 전체 확인 
	 *  부모노드는 하위노드의 모든 노드가 같은 상태일때 그 상태를 따라감
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public void parentNodeCheck() {
		List<IComponentElement> elements = (List<IComponentElement>) treev.getInput();
		//boolean isNonExist = false;
		String catStat = "";
		String nowStat = "";
		String beforeStat = "";
		IComponentElement children_now = null;
		IComponentElement children_before = null;
		if (elements == null)
			return;
		for (IComponentElement catElement : elements) {
			if (catElement instanceof Category) {
				List<IComponentElement> components = ((Category) catElement).getChildren();
				//카테고리 하위의 첫번째 노드 보내서 카테고리 노드 체크
				Category cat = (Category) contentProvider.getParent((Component) components.get(0));
				int cnt = 0;
				//----------------성능향상 0929--------------------
				catStat = "";
				nowStat = "";
				beforeStat = "";
				children_now = null;
				children_before = null;

				if (cat != null) {
					//자식노드 for문
					for (int i = 0; i < cat.getChildren().size(); i++) {
						children_now = cat.getChildren().get(i);
						if (i > 0)
							children_before = cat.getChildren().get(i - 1);

						//현재 자식노드 상태
						//if(treev.getGrayChecked(children_now)){
						if (treev.getGrayed(children_now) && treev.getChecked(children_now)) {
							nowStat = "graycheck";
						} else if (treev.getChecked(children_now)) {
							nowStat = "check";
						} else {
							nowStat = "none";
						}
						//이전 자식노드 상태 (첫번째 노드 아닐때)
						if (children_before != null) {
							//if(treev.getGrayChecked(children_before)){
							if (treev.getGrayed(children_before) && treev.getChecked(children_before)) {
								beforeStat = "graycheck";
							} else if (treev.getChecked(children_before)) {
								beforeStat = "check";
							} else {
								beforeStat = "none";
							}
						}

						if (i == 0) {
							//첫번째 노드일때 현재 자식노드 상태를 부모 노드 상태로 가짐
							catStat = nowStat;
						} else {
							if (nowStat.equals("none")) {
								//자식중 none이 하나라도 있으면 부모는 none, break 
								catStat = "none";
								break;
							} else if (!nowStat.equals(beforeStat)) {
								//지금 자식노드와 이전자식노드의 상태가 다르면 부모는 none, break 
								catStat = "none";
								break;
							} else {
								catStat = nowStat;
							}
						}

					}

					//부모노드 상태 셋팅
					if (catStat.equals("graycheck")) {
						treev.setGrayed(cat, true);
						treev.setChecked(cat, true);
					} else if (catStat.equals("none")) {
						treev.setGrayed(cat, false);
						treev.setChecked(cat, false);
					} else {
						treev.setGrayed(cat, false);
						treev.setChecked(cat, true);
					}
				}

			}
		}

	}

	/** dependency 컴포넌트 체크 */
	@SuppressWarnings("unchecked")
	public void checkDependency(Component checkedComponent) {
		List<Component> dependencylist = checkedComponent.getDependencyPackage();
		for (Component dependency : dependencylist) {
			//dependency 중 - 이미 설치된 컴포넌트가 아니고 & 체크되어 있는지 않으면 - 자동체크
			if (!dependency.isCreatedComponent() && !treev.getChecked(dependency)) {
				treev.setChecked(dependency, true);
				//직접 체크 여부 - setSelection
				dependency.setSelection(false);
			}
		}
	}

	/** dependency 컴포넌트 체크 해제 */
	@SuppressWarnings("unchecked")
	public void uncheckDependency(Component checkedComponent) {

		List<Component> dependencylist = checkedComponent.getDependencyPackage();
		for (Component dependency : dependencylist) {
			//dependency 중 - 이미 설치된 컴포넌트가 아니고 & 체크되어 있는 상태이고 & 직접선택한 경우가 아니면 - 자동해제
			if (!dependency.isCreatedComponent() && treev.getChecked(dependency) && !dependency.isSelection()) {
				//----------------성능향상 0929--------------------	
				//if(checkDependComponent(dependency, dependencylist) == 0){
				if (!checkDependComponent2(dependency, dependencylist)) {
					treev.setChecked(dependency, false);
					//직접 체크 여부 - setSelection
					dependency.setSelection(false);
				}
			}
		}
	}

	/** 
	 * 체크한 컴포넌트를 의존하는 컴포넌트 존재 시 갯수 알림 
	 * param : checkedComponent - 대상 컴포넌트
	 * param : exceptDependency - 대상 컴포넌트가 포함되어 있는 "어떤 컴포넌트"의 dependencyList 
	 *                          - 대상 컴포넌트가 "어떤 컴포넌트"의 dependency 라서 확인하는거라면 "어떤 컴포넌트"의 dependency 목록에 있는건 의존 컴포넌트 갯수에서 제외함(체크해제할꺼니까-자동해제안할건 제외해야함=isSelection이 false인것)  
	 * */
	public int checkDependComponent(Component checkedComponent, List<Component> exceptDependency) {

		//selection이 false인 것만 남김!
		List<IComponentElement> exceptDependency_false = new ArrayList<IComponentElement>();
		if (exceptDependency != null) {
			for (Component exCom : exceptDependency) {
				if (!exCom.isSelection())
					exceptDependency_false.add(exCom);
			}
		}

		int cnt = 0;
		descDialog = ""; //$NON-NLS-1$

		//		String curPackageName = checkedComponent.getPackageName();
		//		List<IComponentElement> tmp = (List<IComponentElement>) treev.getInput();
		//		
		//		for(IComponentElement catElement : tmp){						
		//			if ( catElement instanceof Category){
		//				List<IComponentElement> components = ((Category) catElement).getChildren();
		//				
		//				for( IComponentElement comElement : components){	
		//					if ( comElement instanceof Component && treev.getChecked(comElement)) {
		//
		//						if(comElement.getDependencyPackage() != null && !checkedComponent.isCreatedComponent()){
		//							
		//							if(comElement.getDependencyPackage().contains(checkedComponent)){
		//								
		//								if(exceptDependency != null ){
		//									if( !exceptDependency_false.contains(comElement) ){
		//										cnt++;
		//										if(cnt==1) descDialog = comElement.getName();
		//										else descDialog = descDialog + ", " + comElement.getName(); //$NON-NLS-1$									
		//									}
		//									
		//
		//								}else{
		//									cnt++;
		//									if(cnt==1) descDialog = comElement.getName();
		//									else descDialog = descDialog + ", " + comElement.getName(); //$NON-NLS-1$
		//								}
		//							}
		//							
		//						}
		//						
		//					}
		//				}
		//
		//				
		//			}
		//		}

		//----------------성능향상 0929--------------------		

		for (Object o : getCheckedElements_all()) {
			if (o instanceof Component) {

				if (((Component) o).getDependencyPackage() != null && !checkedComponent.isCreatedComponent()) {

					if (((Component) o).getDependencyPackage().contains(checkedComponent)) {

						if (exceptDependency != null) {
							if (!exceptDependency_false.contains(((Component) o))) {
								cnt++;
								if (cnt == 1)
									descDialog = ((Component) o).getName();
								else
									descDialog = descDialog + ", " + ((Component) o).getName(); //$NON-NLS-1$	
							}

						} else {
							cnt++;
							if (cnt == 1)
								descDialog = ((Component) o).getName();
							else
								descDialog = descDialog + ", " + ((Component) o).getName(); //$NON-NLS-1$
						}
					}

				}

			}
		}
		return cnt;

	}

	/** 
	 * 체크한 컴포넌트를 의존하는 컴포넌트 존재 시 true 
	 * param : checkedComponent - 대상 컴포넌트
	 * param : exceptDependency - 대상 컴포넌트가 포함되어 있는 "어떤 컴포넌트"의 dependencyList 
	 *                          - 대상 컴포넌트가 "어떤 컴포넌트"의 dependency 라서 확인하는거라면 "어떤 컴포넌트"의 dependency 목록에 있는건 의존 컴포넌트 갯수에서 제외함(체크해제할꺼니까-자동해제안할건 제외해야함=isSelection이 false인것)  
	 * */
	public boolean checkDependComponent2(Component checkedComponent, List<Component> exceptDependency) {
		//----------------성능향상 0929--------------------

		//selection이 false인 것만 남김!
		List<IComponentElement> exceptDependency_false = new ArrayList<IComponentElement>();
		if (exceptDependency != null) {
			for (Component exCom : exceptDependency) {
				if (!exCom.isSelection())
					exceptDependency_false.add(exCom);
			}
		}

		//----------------성능향상 0929--------------------		
		Object[] oz = getCheckedElements_all();

		for (Object o : oz) {
			if (o instanceof Component) {

				if (((Component) o).getDependencyPackage() != null && !checkedComponent.isCreatedComponent()) {

					if (((Component) o).getDependencyPackage().contains(checkedComponent)) {

						if (exceptDependency != null) {
							if (!exceptDependency_false.contains(((Component) o))) {
								return true;
							}

						} else {
							return true;
						}
					}

				}

			}
		}

		return false;

	}

	/** 선택된 컴포넌트 목록_grayed 포함 */
	@SuppressWarnings("unchecked")
	public Object[] getCheckedElements_all() {
		List<IComponentElement> checkedElements = new LinkedList<IComponentElement>();
		List<IComponentElement> items = (List<IComponentElement>) treev.getInput();
		for (IComponentElement item : items) {
			List<IComponentElement> components = ((Category) item).getChildren();
			for (IComponentElement comp : components) {
				if (comp instanceof Component) {
					if (treev.getChecked(comp))
						checkedElements.add(comp);
				}
			}
		}
		return checkedElements.toArray();
	}

	/** 선택된 컴포넌트 목록 */
	@SuppressWarnings("unchecked")
	public void getCheckedComponents() {

		List<IComponentElement> tmp = (List<IComponentElement>) treev.getInput();
		List<IComponentElement> checkedComponents = new ArrayList<IComponentElement>();
		//		int cnt = 0; //게시판 옵션이 true인 컴포넌트 cnt
		for (IComponentElement catElement : tmp) {
			if (catElement instanceof Category) {
				List<IComponentElement> components = ((Category) catElement).getChildren();

				for (IComponentElement comElement : components) {

					if (comElement instanceof Component && treev.getChecked(comElement)) {
						if (!treev.getGrayChecked(comElement))
							checkedComponents.add(comElement);

						//						if(comElement.isAddedOptions()) cnt++;
					}
				}

			}
		}

		context.setComponent(checkedComponents);
		//		if(cnt > 0) context.setAddedOptions(true);
		//		else context.setAddedOptions(false);

		//페이지 완결성 체크
		isPageComplete();
		updatePageComplete();

		if (context.getComponent() == null || context.getComponent().size() == 0) {
			setErrorMessage(MobileComMngtMessages.selectComponentPagepageDescription);
		} else {
			setPageComplete(true);
			setErrorMessage(null);
			setMessage(null);
		}
	}

	/** 마법사 페이지 visible 상태일 때 컴포넌트 목록 가져오기 */
	public void setVisible(boolean visible) {

		/* 이 페이지로 넘어왔을 때 */
		if (visible && context.getJavaProject() != null && !compareProjectName.equals(context.getJavaProject().getProject().getName())) {
			projectName.setText(context.getJavaProject().getProject().getName());
			factory.containsComponent(context.getJavaProject());
			treev.setInput(factory.getComponentElements());
			compareProjectName = context.getJavaProject().getProject().getName();

			init();
			setPageComplete(true);
			setErrorMessage(null);
			setMessage(null);
		}
		/* 페이지를 떠날때 filter text clear  --> 최초 검색 후 컴포넌트 선택시 dependency 선택 안되어 if(!visible) 조건 뺌 20111024*/
		//if(!visible) tree.clearText();
		tree.clearText();

		super.setVisible(visible);
	}

}
