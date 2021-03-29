package egovframework.mdev.imp.commngt.util;

import java.util.List;

import egovframework.mdev.imp.commngt.common.MobileComMngtMessages;
import egovframework.mdev.imp.commngt.wizards.model.Component;
import egovframework.mdev.imp.commngt.wizards.model.IComponentElement;
import egovframework.mdev.imp.commngt.wizards.model.SqlStatementModel;

/**
 * TableViewer를 위해 SqlStatementModel을 setting하는 클래스
 * 
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

public class SettingSqlStatementUtil {

	/**
	 * SqlStatementModel에 테이블명과 컴포넌트명을 set
	 * @param checkedComponent
	 * @param ssm
	 * 
	 * */
	public static void setSqlStatementModel(List<IComponentElement> checkedComponent, List<SqlStatementModel> ssm) {

		Component component;
		if (checkedComponent != null && ssm.isEmpty()) {
			SqlStatementModel model = null;
			String[] useTableList = null;

			for (int j = 0; j < checkedComponent.size(); j++) {
				component = (Component) checkedComponent.get(j);
				useTableList = component.getUseTable().split(","); //$NON-NLS-1$
				
				
				for (int k = 0; k < useTableList.length; k++) {
					if(useTableList[k].indexOf("N/A") < 0){ //$NON-NLS-1$
				
						if (useTableList[k].trim() != null) {
							model = new SqlStatementModel();
							model.setStmtName(useTableList[k].trim()
										.toUpperCase());
							model.setResultMessage(MobileComMngtMessages.settingSqlStatementUtilresult);
							model.setComponent(component.getName());
	
							boolean isIncluded = false;
							if (ssm.isEmpty()) {
								ssm.add(model);
							} else {
								for (int o = 0; o < ssm.size(); o++) {
									if (ssm.get(o).getStmtName().equalsIgnoreCase(useTableList[k].trim())) {
										isIncluded = true;
										if(ssm.get(o).getComponent().indexOf(model.getComponent()) < 0){
											// 만약 이미 저장한 컴포넌트 정보들 중 동일한 테이블이 존재한다면
											
											ssm.get(o).setComponent(
													ssm.get(o).getComponent() + ", " //$NON-NLS-1$
															+ model.getComponent());
										}
									}
	
	
								}
								if (!isIncluded) { // 존재하지 않는다면
									ssm.add(model);
								} else {
									isIncluded = false;
								}
							}
	
						}
						
						
					}//if
					
				}
			
				
			}
		}
	}
}
