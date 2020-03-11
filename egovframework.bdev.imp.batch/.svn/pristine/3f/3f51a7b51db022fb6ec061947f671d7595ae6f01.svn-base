package egovframework.bdev.imp.batch.wizards.jobcreation.views;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import egovframework.bdev.imp.batch.EgovBatchPlugin;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.DecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepAndDecisionVo;
import egovframework.bdev.imp.batch.wizards.jobcreation.model.StepVo;
/**
 * Job List, Step List에 Vo의 이름을 제공하는 LabelProvider
 * 
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 * </pre>
 */
public class ListProvider implements ITableLabelProvider {

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

	/**
	 * <pre>
	 * Step / Decision 이름 옆에 Step과 Decision을 구분할 수 있도록
	 * 그림을 붙여준다.
	 * </pre>
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		if(element instanceof JobVo){
			return EgovBatchPlugin.getDefault().getImage(EgovBatchPlugin.IMG_BATCH_JOB_LIST_JOB);
		}else if(element instanceof StepVo){
			return EgovBatchPlugin.getDefault().getImage(EgovBatchPlugin.IMG_BATCH_JOB_LIST_STEP);
		}else if(element instanceof DecisionVo){
			return EgovBatchPlugin.getDefault().getImage(EgovBatchPlugin.IMG_BATCH_JOB_LIST_DECISION);
		}
		return null;
	}

	/** Wizard의 JobList, Step List에서 JobVO, StepVO, DecisionVO의 name을 return한다. */
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof JobVo){
			return ((JobVo)element).getJobName();
		}else if(element instanceof StepAndDecisionVo){
			return ((StepAndDecisionVo)element).getName();
		}
		return "Unknown Value";
	}

}
