package egovframework.bdev.tst.wizards.views;

import org.eclipse.jface.viewers.LabelProvider;

import egovframework.bdev.imp.batch.wizards.jobcreation.model.JobVo;
/**
 * Job Info의 Label Provider를 관리하는 클래스
 * @author 조용현
 * @since 2012.07.24
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
public class JobListLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if(!(element instanceof JobVo)){
			return "NOT JobVo Type";
		}
		
		JobVo jobVo = (JobVo)element;
		
		return jobVo.getJobName();
	}
}
