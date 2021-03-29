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
package egovframework.bdev.imp.ide.com.wizards.pages;

import java.util.regex.Pattern;

import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import egovframework.bdev.imp.ide.common.BatchIdeMessages;
import egovframework.bdev.imp.ide.job.wizards.model.NewBatchProjectContext;
import egovframework.dev.imp.core.utils.IdeUIUtil;

/**
 * Batch eGovFramework 프로젝트 생성 마법사 페이지 클래스
 * @author 배치개발환경 개발팀
 * @since 2012.07.02
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 * 수정일      	수정자       	    수정내용
 * ----------	---------------    ---------------------------
 * 2012.07.02	배치개발환경 개발팀	최초 생성
 * 2012.08.18	조용현		validation 수정
 * 
 * </pre>
 */
public class BatchProjectCreationPage extends BatchBaseProjectCreationPage {

	/** 위젯 */
	protected Text groupIdText;
	protected Text artifactIdText;
	protected Combo versionCombo;
	protected Text packageText;
	protected NewBatchProjectContext context;

	/**
	 * 생성자
	 * @param pageName
	 * @param context
	 */
	protected BatchProjectCreationPage(String pageName, NewBatchProjectContext context) {
		super(pageName);

		this.context = context;
	}

	/**
	 * 프로젝트명 변경 이벤트 리스너
	 */
	private final Listener projectNameModifyListener = new Listener() {
		public void handleEvent(Event e) {
			updateArtifactId();
			boolean valid = validatePage();
			setPageComplete(valid);
		}
	};

	/**
	 * 메이블 설정을 위한 위젯
	 * @param container
	 */
	private void createMavenSettingGroup(Composite container) {

		// 그룹
		Group mavenGroup = new Group(container, SWT.NONE);
		mavenGroup.setText(BatchIdeMessages.wizardsPagesBProjectCreationPage14);
		GridLayout dirLayout = new GridLayout(3, false);
		mavenGroup.setLayout(dirLayout);
		mavenGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// 그룹 아이디
		groupIdText = IdeUIUtil.createTextField(mavenGroup, BatchIdeMessages.wizardsPagesBProjectCreationPage15);
		groupIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPageComplete(validatePage());
			}
		});

		// 아티팩트 아이디
		artifactIdText = IdeUIUtil.createTextField(mavenGroup, BatchIdeMessages.wizardsPagesBProjectCreationPage16);
		artifactIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPageComplete(validatePage());
			}
		});

		// 버젼
		versionCombo = IdeUIUtil.createComboField(mavenGroup, BatchIdeMessages.wizardsPagesBProjectCreationPage17);
		versionCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPageComplete(validatePage());
			}
		});
	}

	/**
	 * 프로젝트 명에 따라 그룹아이디,아티팩아이디 자동 설정
	 */
	private void updateArtifactId() {
		String projectName = getProjectName();
		String groupId = ""; //$NON-NLS-1$
		String artifactId = ""; //$NON-NLS-1$

		if ((projectName.length() > 0) && (".".equals(projectName.substring(projectName.length() - 1)))) { //$NON-NLS-1$
			projectName = projectName.substring(0, projectName.length() - 1);
		}

		artifactId = projectName;

		if (projectName.lastIndexOf(".") > 0) { //$NON-NLS-1$
			groupId = projectName.substring(0, projectName.lastIndexOf(".")); //$NON-NLS-1$
			groupIdText.setText(TextProcessor.process(groupId));
			artifactId = projectName.substring(projectName.lastIndexOf(".") + 1); //$NON-NLS-1$
		}

		artifactIdText.setText(TextProcessor.process(artifactId));
	}

	/**
	 * 위젯 생성
	 */
	@Override
	protected void createControls(Composite parent) {

		createMavenSettingGroup(parent);

		addProjectNameListener(projectNameModifyListener);
	}

	/**
	 * 검증 페이지
	 */
	@Override
	/*
	protected boolean validatePage() {

		if (getProjectName().length() < 1) { //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.wizardsPagesCoreBProjectCreationPage1);
			return false;
		}

		if (!super.validatePage()) {
			return false;
		}

		if (getGroupId().length() < 1) { //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.wizardsPagesBProjectCreationPage11);
			return false;
		}

		if (!validateProjectID(getGroupId())) {
			setErrorMessage(BatchIdeMessages.batchProjectCreationPage9);
			return false;
		}

		if (getArtifactId().length() < 1) { //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.wizardsPagesBProjectCreationPage13);
			return false;
		}

		if (!validateProjectID(getArtifactId())) {
			setErrorMessage(BatchIdeMessages.batchProjectCreationPage12);
			return false;
		}

		if (getVersion().length() < 1) { //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.batchProjectCreationPage1);
			return false;
		}

		if (!validateProjectID(getVersion())) {
			setErrorMessage(BatchIdeMessages.batchProjectCreationPage2);
			return false;
		}

		updateContext();
		return true;
	}
    */
	protected boolean validatePage() {

		if ("".equals(getProjectName())) {  //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.wizardsPagesCoreBProjectCreationPage1);
			return false;
		}

		if (!super.validatePage()) {
			return false;
		}

		if ("".equals(getGroupId())) {  //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.wizardsPagesBProjectCreationPage11);
			return false;
		}

        if (!Pattern.matches("^[a-zA-Z0-9-._]*$", getGroupId()) || getGroupId().indexOf("`")>-1 || getGroupId().indexOf("^")>-1|| getGroupId().indexOf("\\")>-1) { //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.batchProjectCreationPage9);
			return false;
		}

        if ("".equals(getArtifactId())) {  //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.wizardsPagesBProjectCreationPage13);
			return false;
		}

        if (!Pattern.matches("^[a-zA-Z0-9-._]*$", getArtifactId()) || getArtifactId().indexOf("`")>-1 || getArtifactId().indexOf("^")>-1|| getArtifactId().indexOf("\\")>-1) { //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.batchProjectCreationPage12);
			return false;
		}

		if ("".equals(getVersion())) { //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.batchProjectCreationPage1);
			return false;
		}

		if (!Pattern.matches("^[a-zA-Z0-9-._]*$", getVersion()) || getVersion().indexOf("`")>-1 || getVersion().indexOf("^")>-1|| getVersion().indexOf("\\")>-1) { //$NON-NLS-1$
			setErrorMessage(BatchIdeMessages.batchProjectCreationPage2);
			return false;
		}

		updateContext();
		return true;
	}
	
	// 컨텍스트 정보 변경
	protected void updateContext() {
		context.setProject(getProjectHandle());
		context.setProjectName(getProjectName());
		context.setGroupId(getGroupId());
		context.setArtifactId(getArtifactId());
		context.setVersion(getVersion());
		context.setPackageName(getPackage());
		context.setLocationPath(getLocationPath());

	}

	//getter
	public String getGroupId() {
		if (groupIdText == null) {
			return ""; //$NON-NLS-1$
		}

		return groupIdText.getText();
	}

	//getter
	public String getArtifactId() {
		if (artifactIdText == null) {
			return ""; //$NON-NLS-1$
		}

		return artifactIdText.getText();
	}

	//getter
	public String getVersion() {
		if (versionCombo == null) {
			return ""; //$NON-NLS-1$
		}

		return versionCombo.getText();
	}

	//getter
	public String getPackage() {
		if (packageText == null) {
			return ""; //$NON-NLS-1$
		}
		return packageText.getText().trim();
	}

	// 화면 생성
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
	}

}
