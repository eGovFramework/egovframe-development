package egovframework.dev.imp.codegen.template.templates;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * 템플릿 기반 코드젠에 사용할 템플릿 플러그인 
 * <p><b>NOTE:</b> 플러그인 로딩, 종료 이벤트 처리. 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  이흥주          최초 생성
 *
 * </pre>
 */
public class EgovCodeGenTemplatesPlugin extends AbstractUIPlugin {

	// 플러그인 ID
	public static final String PLUGIN_ID = "egovframework.dev.imp.codegen.template.templates";

	// 공유 인스턴스 
	private static EgovCodeGenTemplatesPlugin plugin;
	
	/**
	 * 생성자
	 * 
	 */
	public EgovCodeGenTemplatesPlugin() {
	}

	/*
	 * 플러그인 시작
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * 플러그인 중지
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * 인스턴스를 반환함. 
	 * 
	 * @return the shared instance
	 */
	public static EgovCodeGenTemplatesPlugin getDefault() {
		return plugin;
	}

}
