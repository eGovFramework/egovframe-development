package egovframework.bdev.imp.confmngt;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * eGovFramework Batch Preferences 플러그인의 라이프 사이클을 관리하는 Activator 클래스
 * @author 배치개발환경 개발팀 최서윤
 * @since 2012.06.28
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2012.06.28  최서윤          최초 생성
 * 
 * 
 * </pre>
 */
public class EgovBatchConfMngtPlugin extends AbstractUIPlugin {

	/** 플러그인 아이디 */
	public static final String PLUGIN_ID = "egovframework.bdev.imp.confmngt"; //$NON-NLS-1$

	/** 플러그인 공유 인스턴스 */
	private static EgovBatchConfMngtPlugin plugin;
	
	/**
     * 생성자
     */
	public EgovBatchConfMngtPlugin() {
	}

	/**
     * 플로그인 시작
     * @param context
     */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
     * 플러그인 종료
     * @param context
     */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
     * 공유 플러그인 인스턴스를 반환
     * @return 공유 플러그인 인스턴스
     */
	public static EgovBatchConfMngtPlugin getDefault() {
		return plugin;
	}

	/**
     * ImageDescriptor 가져오기
     * @param key
     * @return ImageDescriptor
     */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
