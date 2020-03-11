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
package egovframework.hdev.imp.ide.pages;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebFacetProjectCreationDataModelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.DataModelEvent;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.DataModelPropertyDescriptor;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelListener;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelSynchHelper;
import org.eclipse.wst.common.frameworks.internal.operations.IProjectCreationPropertiesNew;
import org.eclipse.wst.common.frameworks.internal.ui.NewProjectGroup;
import org.eclipse.wst.common.frameworks.internal.ui.ValidationStatus;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectWorkingCopy;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectEvent;
import org.eclipse.wst.common.project.facet.core.events.IFacetedProjectListener;
import org.eclipse.wst.common.project.facet.core.events.IProjectFacetsChangedEvent;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntimeComponent;
import org.eclipse.wst.common.project.facet.core.runtime.RuntimeManager;
import org.eclipse.wst.server.ui.ServerUIUtil;
import org.eclipse.wst.web.internal.facet.RuntimePresetMappingRegistry;

import egovframework.hdev.imp.ide.common.DeviceAPIMessages;
import egovframework.hdev.imp.ide.common.ProjectFacetConstants;
import egovframework.hdev.imp.ide.model.DeviceAPIContext;
import egovframework.hdev.imp.ide.wizards.examples.DeviceAPITemplateInfo;

/**
 * evice API Web Project 생성 마법사 페이지 클래스
 * 
 * @since 2012.07.24
 * @author 디바이스 API 개발환경 팀 조용현
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.07.24	조용현	최초생성
 * 
 * 
 * </pre>
 */
@SuppressWarnings({ "restriction", "unchecked" })
public class DeviceAPIWebProjectCreationPage extends
		DeviceAPIProjectCreationPage implements
		IFacetProjectCreationDataModelProperties, IDataModelListener {

	/** 위젯 */
	private Combo serverTargetCombo;
	private Combo moduleVersionCombo = null;

	private DataModelSynchHelper synchHelper;
	private IDataModel model;
	private IProjectFacet temprojectFacet = null;
	private NewProjectGroup projectNameGroup;
	private final IFacetedProjectWorkingCopy fpjwc;
	private final IFacetedProjectListener fpjwcListener;
	private String[] validationPropertyNames = new String[] {
			IProjectCreationPropertiesNew.PROJECT_NAME,
			IProjectCreationPropertiesNew.PROJECT_LOCATION, FACET_RUNTIME,
			FACETED_PROJECT_WORKING_COPY };

	private Map<String, Integer> validationMap;
	private final ValidationStatus status = new ValidationStatus();
	private boolean isEnableFinishButton = false;

	private Button defaultButton = null;
	private Button selectDBButton = null;
	
	private Text descriptionText = null;

	/**
	 * 생성자
	 * 
	 * @param pageName
	 * @param context
	 */
	public DeviceAPIWebProjectCreationPage(String pageName,
			DeviceAPIContext context) {
		super(pageName, context);
		setTitle(DeviceAPIMessages.WEB_PROJECT_TITLE);
		setDescription(DeviceAPIMessages.WEB_PROJECT_DESCRIPTION);

		createDataModel();

		this.fpjwc = (IFacetedProjectWorkingCopy) this.model
				.getProperty(FACETED_PROJECT_WORKING_COPY);

		this.fpjwcListener = new IFacetedProjectListener() {
			public void handleEvent(final IFacetedProjectEvent event) {
				// do nothing
			}
		};
		this.fpjwc.addListener(this.fpjwcListener,
				IFacetedProjectEvent.Type.VALIDATION_PROBLEMS_CHANGED);

		synchRuntimes();
	}

	/**
	 * 데이터 모델 생성
	 */
	private void createDataModel() {
		IDataModel dataModel = DataModelFactory
				.createDataModel(new WebFacetProjectCreationDataModelProvider());
		this.model = dataModel;
		this.model.addListener(this);
		synchHelper = initializeSynchHelper(this.model);
	}

	/**
	 * 런타임 마법사 띄우기
	 * 
	 * @param shell
	 * @param model
	 * @param serverTypeID
	 * @return
	 */
	private boolean launchNewTargetRuntimeWizard(Shell shell,
			final IDataModel model, String serverTypeID) {
		if (model == null) {
			return false;
		}

		final DataModelPropertyDescriptor[] preDescriptors = model
				.getValidPropertyDescriptors(FACET_RUNTIME);

		final boolean[] waiting = { true };

		final IDataModelListener dataModelListener = new IDataModelListener() {
			public void propertyChanged(final DataModelEvent event) {
				if (event.getPropertyName().equals(FACET_RUNTIME)
						&& event.getFlag() == DataModelEvent.VALID_VALUES_CHG) {
					synchronized (waiting) {
						waiting[0] = false;
						waiting.notify();
					}
					model.removeListener(this);
				}
			}
		};

		model.addListener(dataModelListener);

		boolean isOK = ServerUIUtil.showNewRuntimeWizard(shell, serverTypeID,
				null);

		if (isOK) {

			final Thread newRuntimeSelectionThread = new Thread() {
				public void run() {
					RuntimeManager.getRuntimes();
					synchronized (waiting) {
						while (waiting[0]) {
							try {
								waiting.wait();
							} catch (InterruptedException e) {
							}
						}
					}

					DataModelPropertyDescriptor[] postDescriptors = model
							.getValidPropertyDescriptors(FACET_RUNTIME);
					Object[] preProperty = new Object[preDescriptors.length];
					for (int i = 0; i < preProperty.length; i++) {
						preProperty[i] = preDescriptors[i].getPropertyValue();
					}
					Object[] postProperty = new Object[postDescriptors.length];
					for (int i = 0; i < postProperty.length; i++) {
						postProperty[i] = postDescriptors[i].getPropertyValue();
					}
					Object newProperty = getNewObject(preProperty, postProperty);

					if (newProperty != null)
						model.setProperty(FACET_RUNTIME, newProperty);
				}
			};

			newRuntimeSelectionThread.start();
			return true;
		} else {
			model.removeListener(dataModelListener);
			return false;
		}
	}

	private Object getNewObject(Object[] oldObjects, Object[] newObjects) {
		if (oldObjects != null && newObjects != null
				&& oldObjects.length < newObjects.length) {
			for (int i = 0; i < newObjects.length; i++) {
				boolean found = false;
				Object object = newObjects[i];
				for (int j = 0; j < oldObjects.length; j++) {
					if (oldObjects[j] == object) {
						found = true;
						break;
					}
				}
				if (!found)
					return object;
			}
		}
		if (oldObjects == null && newObjects != null && newObjects.length == 1)
			return newObjects[0];
		return null;
	}

	private boolean internalLaunchNewRuntimeWizard(Shell shell, IDataModel model) {
		return launchNewTargetRuntimeWizard(shell, model, getModuleTypeID());
	}

	private String getModuleTypeID() {
		return ProjectFacetConstants.WEB_FACET_ID; // J2EEProjectUtilities.DYNAMIC_WEB;
	}

	private void createServerTargetComposite(Composite parent) {

		// 그룹
		Group group = new Group(parent, SWT.NONE);
		group.setText(DeviceAPIMessages.WEB_PROJECT_SERVER_TARGET_GROUP_TEXT);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(new GridLayout(2, false));

		// 타겟 서버 콤보
		serverTargetCombo = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		serverTargetCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button newServerTargetButton = new Button(group, SWT.NONE);
		newServerTargetButton.setText(DeviceAPIMessages.WEB_PROJECT_NEW_SERVER_TARGET_BUTTON_TEXT);
		newServerTargetButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unused")
			public void widgetSelected(SelectionEvent e) {
				if (!internalLaunchNewRuntimeWizard(getShell(), model)) {
					String temp = ""; //$NON-NLS-1$
				}
			}
		});
		
		Control[] deps = new Control[] { newServerTargetButton };
		synchHelper.synchCombo(serverTargetCombo, FACET_RUNTIME, deps);
		if (serverTargetCombo.getSelectionIndex() == -1
				&& serverTargetCombo.getVisibleItemCount() != 0)
			serverTargetCombo.select(0);
	}

	private void createModuleVersionComposite(Composite top) {

		temprojectFacet = ProjectFacetsManager
				.getProjectFacet(getModuleTypeID());
		if (temprojectFacet.getVersions().size() <= 1) {
			return;
		}

		final Group group = new Group(top, SWT.NONE);
		GridData groupGridData = new GridData(GridData.FILL_HORIZONTAL);
		GridLayout gridLayout = new GridLayout(1, false);
		group.setLayoutData(groupGridData);
		group.setLayout(gridLayout);
		group.setText(DeviceAPIMessages.WEB_PROJECT_WEB_MODULE_VERSION_GROUP_LABEL);

		moduleVersionCombo = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		GridData comboGridData = new GridData(GridData.FILL_HORIZONTAL);
		moduleVersionCombo.setLayoutData(comboGridData);
		updateModuleVersions();

		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				projectFacetVersionSelectedEvent();
			}
		};

		moduleVersionCombo.addSelectionListener(selectionAdapter);

		fpjwc.addListener(
				new IFacetedProjectListener() {
					public void handleEvent(IFacetedProjectEvent event) {
						if (event.getType() == IFacetedProjectEvent.Type.PROJECT_FACETS_CHANGED) {
							IProjectFacetsChangedEvent actionEvent = (IProjectFacetsChangedEvent) event;
							Set<IProjectFacetVersion> versions = actionEvent
									.getFacetsWithChangedVersions();

							for (IProjectFacetVersion facetVersion : versions) {
								if (facetVersion.getProjectFacet().equals(
										temprojectFacet)) {
									String selectedFacetVersionString = facetVersion
											.getVersionString();
									String selectedText = moduleVersionCombo
											.getItem(moduleVersionCombo
													.getSelectionIndex());
									if (!selectedText
											.equals(selectedFacetVersionString)) {
										String[] items = moduleVersionCombo
												.getItems();
										int selectedVersionIndex = -1;
										for (int i = 0; i < items.length
												&& selectedVersionIndex == -1; i++) {
											if (items[i]
													.equals(selectedFacetVersionString)) {
												selectedVersionIndex = i;
												moduleVersionCombo
														.select(selectedVersionIndex);
											}
										}
									}
									continue;
								} // if
							}// for

							validatePage();

						} else if (event.getType() == IFacetedProjectEvent.Type.PRIMARY_RUNTIME_CHANGED) {
							// 런타입 설정 변경시 버젼 콤보 아이템 변경
							updateModuleVersions();
						}
					}

				}, IFacetedProjectEvent.Type.PROJECT_FACETS_CHANGED,
				IFacetedProjectEvent.Type.PRIMARY_RUNTIME_CHANGED);
	}

	private void createButtonDescriptionControl(Composite control) {
		Composite buttonDescriptionControl = new Composite(control, SWT.None);
		buttonDescriptionControl.setLayout(new GridLayout(2, false));
		buttonDescriptionControl
				.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group tableCreationTypeGroup = new Group(buttonDescriptionControl,
				SWT.None);
		tableCreationTypeGroup.setLayout(new GridLayout());
		tableCreationTypeGroup.setLayoutData(new GridData(
				GridData.FILL_VERTICAL));
		tableCreationTypeGroup.setText(DeviceAPIMessages.WEB_PROJECT_TABLE_CREATE_GROUP_TEXT);

		defaultButton = new Button(tableCreationTypeGroup, SWT.RADIO);
		defaultButton.setText(DeviceAPIMessages.WEB_PROJECT_TABLE_CREATE_DEFAULT_BUTTON_TEXT);
		defaultButton.setSelection(true);
		defaultButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				descriptionText.setText(DeviceAPIMessages.WEB_PROJECT_DESCRIPTION_GROUP_TEXT1);
				validatePage();
			}
		});

		selectDBButton = new Button(tableCreationTypeGroup, SWT.RADIO);
		selectDBButton.setText(DeviceAPIMessages.WEB_PROJECT_TABLE_CREATE_SELECT_BUTTON_TEXT);
		selectDBButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				descriptionText.setText(DeviceAPIMessages.WEB_PROJECT_DESCRIPTION_GROUP_TEXT2);
				validatePage();
			}
		});

		Group descriptionGroup = new Group(buttonDescriptionControl, SWT.None);
		descriptionGroup.setLayout(new GridLayout());
		descriptionGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		descriptionGroup.setText("설명");

		descriptionText = new Text(descriptionGroup, SWT.V_SCROLL
				| SWT.READ_ONLY | SWT.MULTI);
		descriptionText.setLayoutData(new GridData(GridData.FILL_BOTH));
		descriptionText.setText(DeviceAPIMessages.WEB_PROJECT_DESCRIPTION_GROUP_TEXT1);
		descriptionText.setSize(270, 1000);
	}

	private void setEnableFinishButton() {
		isEnableFinishButton = true;
		setPageComplete(false);
		getContainer().updateButtons();
	}

	private void setDisableFinishButton(boolean isPageComplete) {
		isEnableFinishButton = false;
		setPageComplete(isPageComplete);
		getContainer().updateButtons();
	}

	/**
	 * 버젼 변경
	 */
	private void updateModuleVersions() {

		SortedSet<IProjectFacetVersion> facetVersions = fpjwc
				.getAvailableVersions(temprojectFacet);
		String[] items = new String[facetVersions.size()];
		int i = 0;
		int selectedVersionIndex = -1;
		for (IProjectFacetVersion facetVersion : facetVersions) {
			items[i] = facetVersion.getVersionString();
			if (selectedVersionIndex == -1
					&& items[i]
							.equals(ProjectFacetConstants.DEFAULT_SERVLET_VERSION)) {
				selectedVersionIndex = i;
			}
			i++;
		}
		moduleVersionCombo.clearSelection();
		moduleVersionCombo.setItems(items);
		if (selectedVersionIndex == -1) {
			selectedVersionIndex = items.length - 1;
		}
		moduleVersionCombo.select(selectedVersionIndex);

	}

	private IProjectFacetVersion getPrimaryFacetVersion() {
		IProjectFacetVersion facetVersion = null;

		if (this.temprojectFacet.getVersions().size() > 1) {
			final int selectedIndex = this.moduleVersionCombo
					.getSelectionIndex();

			if (selectedIndex != -1) {
				final String fvstr = this.moduleVersionCombo
						.getItem(selectedIndex);
				facetVersion = this.temprojectFacet.getVersion(fvstr);
			}
		} else {
			facetVersion = this.temprojectFacet.getDefaultVersion();
		}

		return facetVersion;
	}

	private Set<IProjectFacetVersion> getFacetConfiguration(
			final IProjectFacetVersion primaryFacetVersion) {
		final Set<IProjectFacetVersion> config = new HashSet<IProjectFacetVersion>();

		for (IProjectFacet fixedFacet : this.fpjwc.getFixedProjectFacets()) {
			if (fixedFacet == primaryFacetVersion.getProjectFacet()) {
				config.add(primaryFacetVersion);
			} else {
				config.add(this.fpjwc.getHighestAvailableVersion(fixedFacet));
			}
		}

		return config;
	}

	private void projectFacetVersionSelectedEvent() {
		final IProjectFacetVersion facetVersion = getPrimaryFacetVersion();
		if (facetVersion != null) {
			String presetID = null;
			IRuntime runtime = (IRuntime) model.getProperty(FACET_RUNTIME);
			if (runtime != null) {
				if (runtime.getRuntimeComponents().size() > 0) {
					IRuntimeComponent runtimeComponent = runtime
							.getRuntimeComponents().get(0);
					presetID = RuntimePresetMappingRegistry.INSTANCE
							.getPresetID(runtimeComponent
									.getRuntimeComponentType().getId(),
									runtimeComponent
											.getRuntimeComponentVersion()
											.getVersionString(), facetVersion
											.getProjectFacet().getId(),
									facetVersion.getVersionString());
				}
			}

			if (presetID == null) {
				final Set<IProjectFacetVersion> facets = getFacetConfiguration(facetVersion);
				this.fpjwc.setProjectFacets(facets);
			} else {
				final Set<IProjectFacetVersion> facets = getFacetConfiguration(facetVersion);
				this.fpjwc.setProjectFacets(facets);
				this.fpjwc.setSelectedPreset(presetID);

			}
		}
	}

	/**
	 * 속성 값 체크
	 * 
	 * @param propertyName
	 * @param validationKey
	 */
	private void validateProperty(String propertyName, Integer validationKey) {
		setOKStatus(validationKey);
		IStatus status1 = model.validateProperty(propertyName);
		if (!status1.isOK()) {
			String message = status1.isMultiStatus() ? status1.getChildren()[0]
					.getMessage() : status1.getMessage();
			switch (status1.getSeverity()) {
			case IStatus.ERROR:
				setErrorStatus(validationKey, message);
				break;
			case IStatus.WARNING:
				setWarningStatus(validationKey, message);
				break;
			case IStatus.INFO:
				setInfoStatus(validationKey, message);
				break;
			default:
				break;
			}
		}
	}

	private void initializeValidationProperties() {
		validationPropertyNames = getValidationPropertyNames();
		if (validationPropertyNames == null
				|| validationPropertyNames.length == 0)
			validationMap = Collections.EMPTY_MAP;
		else {
			validationMap = new HashMap<String, Integer>(validationPropertyNames.length);
			for (int i = 0; i < validationPropertyNames.length; i++)
				validationMap.put(validationPropertyNames[i], new Integer(i));
		}
	}

	private IFacetedProjectWorkingCopy getFacetedProjectWorkingCopy() {
		return this.fpjwc;
	}

	private boolean getStatus(Integer key) {
		return status.hasError(key);
	}

	private void setErrorStatus(Integer key, String errorMessage) {
		status.setErrorStatus(key, errorMessage);
	}

	private void setWarningStatus(Integer key, String warningMessage) {
		status.setWarningStatus(key, warningMessage);
	}

	private void setInfoStatus(Integer key, String infoMessage) {
		status.setInfoStatus(key, infoMessage);
	}

	private void setOKStatus(Integer key) {
		status.setOKStatus(key);
	}

	private String[] getValidationPropertyNames() {
		return this.validationPropertyNames;
	}

	/**
	 * 런타임 동기화
	 */
	private void synchRuntimes() {
		final Boolean[] suppressBackEvents = { Boolean.FALSE };

		model.addListener(new IDataModelListener() {
			public void propertyChanged(DataModelEvent event) {
				if (IDataModel.VALUE_CHG == event.getFlag()
						|| IDataModel.DEFAULT_CHG == event.getFlag()) {
					if (IFacetProjectCreationDataModelProperties.FACET_RUNTIME
							.equals(event.getPropertyName())) {
						if (!suppressBackEvents[0].booleanValue()) {
							IRuntime runtime = (IRuntime) event.getProperty();
							try {
								setRuntimeAndDefaultFacets(runtime);
							} catch (Exception e) {

							}
						}
					}
				}
			}
		});

		getFacetedProjectWorkingCopy().addListener(
				new IFacetedProjectListener() {
					public void handleEvent(final IFacetedProjectEvent event) {
						suppressBackEvents[0] = Boolean.TRUE;
						model.setProperty(
								IFacetProjectCreationDataModelProperties.FACET_RUNTIME,
								getFacetedProjectWorkingCopy()
										.getPrimaryRuntime());
						suppressBackEvents[0] = Boolean.FALSE;
					}
				}, IFacetedProjectEvent.Type.PRIMARY_RUNTIME_CHANGED);
	}

	/**
	 * 런타임, 기본 패싯 설정
	 * 
	 * @param runtime
	 * @throws CoreException
	 */
	private void setRuntimeAndDefaultFacets(final IRuntime runtime)
			throws CoreException {
		final IFacetedProjectWorkingCopy dm = getFacetedProjectWorkingCopy();

		dm.setTargetedRuntimes(Collections.<IRuntime> emptySet());

		if (runtime != null) {
			final Set<IProjectFacetVersion> minFacets = new HashSet<IProjectFacetVersion>();

			for (IProjectFacet f : dm.getFixedProjectFacets()) {
				minFacets.add(f.getLatestSupportedVersion(runtime));
			}

			dm.setProjectFacets(minFacets);

			dm.setTargetedRuntimes(Collections.singleton(runtime));
		}

		dm.setSelectedPreset(FacetedProjectFramework.DEFAULT_CONFIGURATION_PRESET_ID);
	}

	protected void updateControls() {
		updateModuleVersions();
	}

	protected IProjectFacet getPrimaryFacet() {
		return this.temprojectFacet;
	}

	/**
	 * UI 컨트롤 생성
	 */
	@Override
	protected void createControls(Composite parent) {
		initializeValidationProperties();

		createServerTargetComposite(parent);
		createModuleVersionComposite(parent);

		super.createControls(parent);

		createButtonDescriptionControl(parent);
	}

	@Override
	protected boolean validatePage() {
		setDisableFinishButton(false);

		String deviceAPIProjectName = context.getDeviceapiProjectName();
		
		if(deviceAPIProjectName.length() > 0)
		if (getWebProjectName().equals(deviceAPIProjectName)) {
			setErrorMessage(DeviceAPIMessages.WEB_PROJECT_ERROR_EQUAL_WITH_DEVICE_API_PJT);
			setPageComplete(false);
			return false;
		}

		if (!super.validatePage()) {
			return false;
		}

		String javaVersion = System.getProperty("java.version"); //$NON-NLS-1$
		if ("3.0".equals(moduleVersionCombo.getText()) && javaVersion != null && (javaVersion.indexOf("1.5") > -1 || javaVersion.indexOf("1.4") > -1)) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			setErrorMessage(DeviceAPIMessages.WEB_PROJECT_ERROR_REQUIRE_JAVA_VERSION);
			setPageComplete(false);
			return false;
		}

		validateControlsBase();

		updateContext();

		if (defaultButton.getSelection()) {
			
			setEnableFinishButton();
			return false;
		} else if (selectDBButton.getSelection()) {
			
			setDisableFinishButton(true);
			return true;
		} else {
			return true;
		}
	}

	// 컨텍스트 정보 변경
	protected void updateContext() {

		context.setWebProjectName(getWebProjectName());
		context.setWebProject(getWebProjectHandle());
		context.setWebPomFileName(DeviceAPITemplateInfo.webPomFile);
		context.setWebLocationPath(getWebLocationPath());
		context.setWebPackageName(getWebPackage());
		context.setVersion(getVersion());

		context.setServletVersion(moduleVersionCombo.getItem(moduleVersionCombo
				.getSelectionIndex()));
		context.setRuntimeName(serverTargetCombo.getItem(serverTargetCombo
				.getSelectionIndex()));

		context.setGroupId(getGroupId());
		context.setArtifactId(getArtifactId());

	}

	protected final String validateControlsBase() {
		if (!validationMap.isEmpty()) {
			String propName;
			for (int i = 0; i < validationPropertyNames.length; i++) {
				propName = validationPropertyNames[i];
				Integer valKey = (Integer) validationMap.get(propName);
				if (valKey != null)
					validateProperty(propName, valKey);
				if (!getStatus(valKey))
					return propName;
			}
		}
		return null;
	}

	public void propertyChanged(final DataModelEvent event) {
		final Runnable uiChanges = new Runnable() {
			public void run() {
				String propertyName = event.getPropertyName();
				if (validationPropertyNames != null
						&& (event.getFlag() == DataModelEvent.VALUE_CHG || (!isPageComplete() && event
								.getFlag() == DataModelEvent.VALID_VALUES_CHG))) {
					for (int i = 0; i < validationPropertyNames.length; i++) {
						if (validationPropertyNames[i].equals(propertyName)) {
							validatePage();
							updateModuleVersions();
							break;
						}
					}
				}
			}
		};
		if (Thread.currentThread() == Display.getDefault().getThread()) {
			uiChanges.run();
		} else {
			Display.getDefault().asyncExec(uiChanges);
		}

	}

	public void dispose() {
		super.dispose();
		if (projectNameGroup != null)
			projectNameGroup.dispose();

		this.fpjwc.removeListener(this.fpjwcListener);
	}

	public DataModelSynchHelper initializeSynchHelper(IDataModel dm) {
		return new DataModelSynchHelper(dm);
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			getShell().setSize(533, 659);
		}
		super.setVisible(visible);
	}

	/**
	 * @return the isEnableFinishButton
	 */
	public boolean isEnableFinishButton() {
		return isEnableFinishButton;
	}

}
