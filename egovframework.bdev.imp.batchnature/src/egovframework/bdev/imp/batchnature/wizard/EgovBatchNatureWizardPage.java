package egovframework.bdev.imp.batchnature.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import egovframework.bdev.imp.batchnature.EgovBatchNaturePlugin;

public class EgovBatchNatureWizardPage extends WizardPage {

	/** Page Size */
	Point size = new Point(470,580);
	
    private final IProject project;

    protected EgovBatchNatureWizardPage(IProject project, String pageName) throws Exception {
        super(pageName);
        setTitle("eGovFrame Batch Nature");
        setDescription("선택한 프로젝트에 eGovFrame Batch Nature를 추가합니다.");
        this.project = project;
    }

    public void createControl(Composite parent){
        
        Composite control = new Composite(parent, SWT.None);
		control.setLayout(new GridLayout(1, false));
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label prjlabel = new Label(control, SWT.NONE);
		prjlabel.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
		prjlabel.setText("선택한 프로젝트 : "+ project.getName());
		
        Label toplabel = new Label(control, SWT.NONE);
        toplabel.setText("- eGovFrame Batch Nature를 추가할 경우 eGovFrame Batch에서 제공하는\n 다음의 기능들을 사용하실 수 있습니다.\n ");
        
        Label infoImageLabel = new Label(control, SWT.ICON|SWT.CENTER);
        infoImageLabel.setAlignment(SWT.CENTER);
		infoImageLabel.setImage(EgovBatchNaturePlugin.getDefault().getImage(EgovBatchNaturePlugin.IMG_BATCH_NATURE));
        
        setControl(parent);
   
    }

    public Control getControl(){
        return super.getControl();
    }
    
    @Override
	public void setVisible(boolean visible) {
		if(visible){
			getShell().setSize(size);
			
			setPageComplete(true);
			getControl().getParent().layout(true, true);
		}
		super.setVisible(visible);
    }
}
