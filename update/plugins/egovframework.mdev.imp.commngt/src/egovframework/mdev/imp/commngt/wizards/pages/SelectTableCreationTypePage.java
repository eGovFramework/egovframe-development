package egovframework.mdev.imp.commngt.wizards.pages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import egovframework.mdev.imp.commngt.common.MobileComMngtMessages;
import egovframework.mdev.imp.commngt.util.HandlingPropertiesUtil;
import egovframework.mdev.imp.commngt.wizards.model.IComponentElement;
import egovframework.mdev.imp.commngt.wizards.model.MobileCommngtContext;

/**
 * 테이블 생성여부와 install type에 관한 마법사페이지 클래스
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

public class SelectTableCreationTypePage extends WizardPage {

	/** Table 정보 그룹핑 */
	private Group relTable = null; 
	/** Table 생성여부 선택사항 1 */
	private Button radioButton = null; 
	/** Table 생성여부 선택사항 2 */
	private Button radioButton2 = null; 
	/** 페이지 완성 여부 체크 */
	private boolean flag = false; 
	/** 공통컴포넌트 컨텍스트 */
	private final MobileCommngtContext context;
	/** 선택 컴포넌트 목록 */
	private static List<IComponentElement> checkedComponent;
	/** Table 형태 화면에 출력 */
	private TableViewer viewer = null;
	/** SelectInstallTypePage 인지 판단 */
	public String isSelectInstallTypePage=null;
	/** type 정보 */
	private Text descript = null;

	/**
     * 생성자
     * @param pageName
     * @param context
     */
	public SelectTableCreationTypePage(String pageName, MobileCommngtContext context) {
		super(pageName);
		this.context = context;
		setTitle(MobileComMngtMessages.selectInstallTypePagepageTitle);
		setDescription(MobileComMngtMessages.selectInstallTypePagepageDescription);
	}

	/**
	 * page 완성 여부 check
	 * @return 완성 여부 
	 */
	public boolean isPageComplete() {
		return flag;
	}

	/**
	 * 텍스트 편집
	 * */
	public void modifyText(ModifyEvent e) {
		getWizard().getContainer().updateButtons();
	}

	
	public void getNextPage(IWizardPage page) {
		 if (page instanceof CustomizeTableCreationPage) {
			 CustomizeTableCreationPage custPage = (CustomizeTableCreationPage) page;
			 custPage.checkLastPage = null;
		 }
	}
	
	/**
	 * 화면단 컨트롤 생성
	 * @param parent
	 */
	public void createControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);

		tableInfo(container);
		relTable.pack();
		radioInfo(container);

		layout.numColumns = 2;

		setControl(container);
		updatePageComplete(); 

	}

	/**
     * 컴포넌트 정보와 필요한 테이블에 대한 정보
     * @param parent
     */
	public void tableInfo(Composite parent) {
		
		SashForm sashFormSub = new SashForm(parent, SWT.VERTICAL);
		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.horizontalSpan = 2;
		gData.heightHint = 180;
		gData.widthHint = 100;
		sashFormSub.setLayoutData(gData);
		
		relTable = new Group(sashFormSub, SWT.NONE);
		relTable.setLayout(new GridLayout());
		relTable.setText(MobileComMngtMessages.selectInstallTypePage0);

		viewer = new TableViewer(relTable, SWT.V_SCROLL | SWT.READ_ONLY
				| SWT.CENTER |SWT.BORDER);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLayoutData(gData);

		TableColumn col2 = new TableColumn(viewer.getTable(), SWT.NONE);
		col2.setText(MobileComMngtMessages.selectInstallTypePage1);
		col2.setWidth(140);
		TableColumn col3 = new TableColumn(viewer.getTable(), SWT.NONE);
		col3.setText(MobileComMngtMessages.selectInstallTypePage2);
		col3.setWidth(300);

		viewer.setContentProvider(new IStructuredContentProvider() {

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				//inputChanged
			}

			public void dispose() {
				//dispose
			}

			@SuppressWarnings("unchecked")
			public Object[] getElements(Object inputElement) {

				ArrayList<IComponentElement> list = (ArrayList<IComponentElement>) inputElement;
				return list.toArray();
			}

		});

		viewer.setLabelProvider(new CheckedComponentTableLableProvider());

		relTable.setLayoutData(gData);

	}

	/**
     * 테이블 생성여부 선택
     * @param parent
     */
	public void radioInfo(Composite parent) {

		GridLayout lay1 = new GridLayout();
		lay1.numColumns = 1;
		lay1.makeColumnsEqualWidth = true;

		GridData groupData = new GridData(GridData.FILL_BOTH);
		GridData rad = new GridData(GridData.FILL_BOTH);

		Group button = new Group(parent, SWT.NONE);
		button.setText(MobileComMngtMessages.selectInstallTypePage3);
		button.setLayoutData(groupData);
		button.setLayout(lay1);

		radioButton = new Button(button, SWT.RADIO);
		radioButton.setText(MobileComMngtMessages.selectInstallTypePagetypeTitle1);
		radioButton.addSelectionListener(selLis);
		radioButton.setLayoutData(rad);
		radioButton.setSelection(true);

		radioButton2 = new Button(button, SWT.RADIO);
		radioButton2.setText(MobileComMngtMessages.selectInstallTypePagetypeTitle3);
		radioButton2.addSelectionListener(selLis);
		radioButton2.setLayoutData(rad);

		/* description */
		GridLayout lay2 = new GridLayout();
		lay2.numColumns = 1;
		lay2.makeColumnsEqualWidth = true;

		GridData horDescription = new GridData(GridData.FILL_BOTH);
		GridData label = new GridData(GridData.FILL_BOTH);

		Group description = new Group(parent, SWT.NONE);
		description.setSize(100, 100);
		description.setText(MobileComMngtMessages.selectInstallTypePage4);
		description.setLayoutData(horDescription);
		description.setLayout(lay2);

		descript = new Text(description, SWT.MULTI | SWT.WRAP
				| SWT.V_SCROLL | SWT.READ_ONLY | SWT.H_SCROLL);
		descript.setText(MobileComMngtMessages.selectInstallTypePagetypeDesc1);

		descript.setLayoutData(label);

	}

	/**
	 * Table 생성여부 선택
	 * */
	SelectionListener selLis = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent even) {
			updatePageComplete();

			Button button = (Button) even.widget;
			if(radioButton.equals(button)){
				context.setInstallType(MobileComMngtMessages.selectInstallTypePagetypeCode1);
				descript.setText(MobileComMngtMessages.selectInstallTypePagetypeDesc1);
				HandlingPropertiesUtil.setContext(context);
			}else if(radioButton2.equals(button)){
				context.setInstallType(MobileComMngtMessages.selectInstallTypePagetypeCode3);
				descript.setText(MobileComMngtMessages.selectInstallTypePagetypeDesc3);
				
				flag = false;
			}
		}
	};


	/**
	 * 이 페이지로 넘어왔을 때 호출 앞에서 선택한 값 셋팅
	 * @param visible
	 */
	public void setVisible(boolean visible) {

		if (visible) {
			isSelectInstallTypePage="isSelectInstallTypePage"; //$NON-NLS-1$
			
			flag = false;
			radioButton2.setEnabled(true);

			checkedComponent = context.getComponent();

			if (!checkedComponent.isEmpty()) {
				viewer.setInput(checkedComponent);
			}
			
			int size = 0;
			for(int i = 0; i < checkedComponent.size(); i++){
				if(checkedComponent.get(i).getUseTable().indexOf(MobileComMngtMessages.selectTableCreationTypePagetableYN) > -1){
					size++;
				}
			}
			if(checkedComponent.size() == size){
				flag = true;
				radioButton2.setEnabled(false);
				radioButton2.setSelection(false);
				radioButton.setSelection(true);
			}
//			if(context.getInstallType().length() < 0){
//				context.setInstallType(MobileComMngtMessages.selectInstallTypePagetypeCode1);
//			}
			isPageComplete();
			updatePageComplete();
			
			getNextPage(this.getNextPage());
		}

 		super.setVisible(visible);
	}
	
	/**
	 * 페이지 완성여부 update
	 * */
	private void updatePageComplete() {
		boolean completed = (radioButton.getSelection());

		flag = completed;
		setPageComplete(completed);
	}

	/**
	 * next버튼 활성화 여부
	 * */
	public boolean canFlipToNextPage() {

		if (getErrorMessage() != null)
			return false;

		if (radioButton2.getSelection())
			return true;
		return false;
	}


}

/**
 * 테이블에 값 셋팅
 */
class CheckedComponentTableLableProvider extends LabelProvider implements
		ITableLabelProvider {

	/**
	 * @param element
	 * @param columnIndex
	 * @return null
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	/**
	 * @param element
	 * @param columnIndex
	 * @return 각 행 삽입내용
	 */
	public String getColumnText(Object element, int columnIndex) {

		if (element instanceof IComponentElement) {
			IComponentElement item = (IComponentElement) element;

			if(columnIndex == 0){
				return item.getName();
				
			}else if(columnIndex == 1){
				return item.getUseTable();
				
			}
		}
		return null;
	}
}


