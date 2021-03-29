package egovframework.dev.imp.commngt;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import egovframework.dev.imp.commngt.common.ComMngtMessages;
import egovframework.dev.imp.commngt.common.CommngtLog;

/**
 * 공통컴포넌트 생성 조립 도구 플러그인의 라이프 사이클을 관리하는 Activator 클래스
 * @author 개발환경 개발팀 박수림
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  박수림          최초 생성
 * 
 * 
 * </pre>
 */
public class EgovCommngtPlugin extends AbstractUIPlugin {

	/** 플러그인 아이디 */
	public static final String PLUGIN_ID = "egovframework.dev.imp.commngt"; 
	/** 플러그인 공유 인스턴스 */
	private static EgovCommngtPlugin plugin;
	/** 아이콘 경로 */
	private static final String ICONS_PATH = "icons/"; //$NON-NLS-1$
	/** 공통컴포넌트 위저드 이미지 */
	public static final String IMG_COMMNGT_WIZ_BANNER = "commngt_wiz"; //$NON-NLS-1$
	/** 공통컴포넌트 범례 */
	public static final String IMG_COMMNGT_REMARKS = "remarks"; //$NON-NLS-1$
	
	/**
     * 생성자
     */
	public EgovCommngtPlugin() {
	}

    /**
     * 플러그인 시작
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
	public static EgovCommngtPlugin getDefault() {
		return plugin;
	}

    /**
     * 이미지 가져오기
     * @param key
     * @return Image
     */
    public Image getImage(String key) {
        Image image = getImageRegistry().get(key);
        return image;
    }

    /**
     * ImageDescriptor 가져오기
     * @param key
     * @return ImageDescriptor
     */
    public ImageDescriptor getImageDescriptor(String key) {
        ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
        return imageDescriptor;
    }

	/**
	 * 이미지 등록
	 * @param registry
	 * @param key
	 * @param fileName
	 */
	@SuppressWarnings("deprecation")
	private void registerImage(ImageRegistry registry, String key,
			String fileName) {
		try {
			IPath path = new Path(ICONS_PATH + fileName);
			URL url = find(path);
			if (url != null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception e) {
			CommngtLog.logError(e);
		}
		return;
	}
	
	/**
	 * ImageRegistry 초기화
	 * 
	 * @param registry
	 */
	protected void initializeImageRegistry(ImageRegistry registry) {
		super.initializeImageRegistry(registry);

		registerImage(registry, IMG_COMMNGT_WIZ_BANNER,	"commngt_wiz.png"); //$NON-NLS-1$	
		registerImage(registry, IMG_COMMNGT_REMARKS,	ComMngtMessages.imgCommngtRemarks); //$NON-NLS-1$	
		return;
	}
	
    /**
     * 활성화된 워크벤치 윈도우 가져오기
     * @return WorkbenchWindow
     */
    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        IWorkbenchWindow workbenchWindow =
            getDefault().getWorkbench().getActiveWorkbenchWindow();
        return workbenchWindow;
    }
    
    
}
