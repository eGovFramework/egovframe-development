package egovframework.bdev.imp.batch.wizards.jobcreation.pages;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import egovframework.bdev.imp.batch.wizards.jobcreation.model.BatchXMLFileBeanIDList;


/**
 * JobInfoControl, StepInfoControl, DecisionInfoControl의 부모 class
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.09.18	조용현	최초생성
 * 
 * 
 * </pre>
 */
abstract public class InfoContentsConstructor {
	
	/** BatchJobCreationCustomizePage의 Apply Button */
	protected Button applyButton = null;

	/** Info가 그려지는 WizardPage( BatchJobCreationCustomizePage ) */
	protected WizardPage currentPage = null;
	
	/**
	 * <pre>
	 * Info가 그려지는 Control
	 * BatchJobCreationCustomizePage의 infoContents
	 * </pre>
	 */
	protected Composite infoControl = null;
	
	/** Apply한 JobVo 및 Step, Decision Vo들의 Bean ID list */
	protected BatchXMLFileBeanIDList batchXMLFileBeanIDList = null;
	
	/** Info 페이지에서 입력된 Bean ID를 저장하는 Bean ID list */
	protected BatchXMLFileBeanIDList invalidBatchXMLFileBeanIDList = null;
	
	/** 선택한 Project */
	protected IProject selectedProject = null;
	
	/** Project Bean ID List */
	protected List<String> projectBeanIDList = null;
	
	/**
	 * <pre>
	 * 해당 bean ID가 중복되는지 검사후 
	 * 중복시 ErrorMessage 출력
	 * </pre>
	 * @param beanID
	 * @return
	 */
	protected boolean isBeanIDDuplicate(BatchXMLFileBeanIDList invalidBeanIDList, String beanID){	
		
		if(invalidBeanIDList.isBeanIDExistIncludeJobRWBeanList(beanID)){
			return true;
		}
		
		if(projectBeanIDList.contains(beanID)){
			return true;
		}
		
		return false;
	}
	
	/**
	 * 해당 Message로 InfoValidationException을 throw
	 * 
	 * @param message
	 * @throws InfoValidationException
	 */
	protected void throwInfoValidationException(String message) throws InfoValidationException{
		throw new InfoValidationException(message);
	}
	
	/**
	 * InfoValidationException
	 * 
	 * @author SDS
	 *
	 */
	@SuppressWarnings("serial")
	protected class InfoValidationException extends Exception{
		String message = null;

		public InfoValidationException(String message) {
			this.message = message;
		}
	}
	
	/**
	 * Job, Step, Decision Info의 Validation 설정
	 * 
	 * @return
	 */
	protected Listener validation(){
		
		return new Listener() {
			
			public void handleEvent(Event event){
				String message = null;
				boolean isApplyButtonEnable = false;
				
				try{
					validateItems();
					
					isApplyButtonEnable = true;
				}catch(InfoValidationException e){
					message = e.message;
					
				}finally{
					currentPage.setErrorMessage(message);
					applyButton.setEnabled(isApplyButtonEnable);
				}
			}
		};
	}
	
	/**
	 * Info의 Item들을 validation
	 * 
	 * @throws InfoValidationException
	 */
	abstract protected void validateItems() throws InfoValidationException;
	
}
