package egovframework.dev.imp.commngt.util;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.sqltools.core.profile.ProfileUtil;
import org.eclipse.datatools.sqltools.editor.core.connection.ISQLEditorConnectionInfo;
import org.eclipse.datatools.sqltools.internal.sqlscrapbook.util.SQLUtility;

/**
 * Datasource Explorer 정보 핸들링을 위한 클래스
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

public class UsingDatasourceUtil {

	/**
	 * Datasource Explorer에 등록해 놓은 DB 정보 가져오기
	 * @return 등록된 datasource
	 */
	public static String[] getProfileNames() {
		IConnectionProfile[] profiles = ProfileUtil.getProfiles();
		String[] profileNames = new String[profiles.length];

		for (int i = 0; i < profiles.length; i++) {
			profileNames[i] = profiles[i].getName();
		}
		return profileNames;
	}

	/**
	 * Datasource Explorer에서 Combo에 등록해놓은 DB Name을 이용해서 Datasource Explorer에
	 * 사용자가 등록해 놓은 정보 가져오기
	 * @return 선택한 datasource에 대한 나머지 정보
	 */
	public static ISQLEditorConnectionInfo getConnectionInfo(String profileName) {
		return SQLUtility.getConnectionInfo(profileName);
	}

}
