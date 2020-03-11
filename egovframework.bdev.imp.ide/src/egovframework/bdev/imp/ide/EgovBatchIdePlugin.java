package egovframework.bdev.imp.ide;

import java.io.File;
import java.net.URL;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import egovframework.bdev.imp.ide.common.BatchIdeLog;

/**
 * eGovFramework Batch IDE 플러그인의 라이프 사이클을 관리하는 Activator 클래스
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
public class EgovBatchIdePlugin extends AbstractUIPlugin {

	/** 플러그인 아이디 */
	public static final String PLUGIN_ID = "egovframework.bdev.imp.ide";

	/** 퍼스펙트 아이디 */
    public static final String ID_PERSPECTIVE = PLUGIN_ID + ".perspectives.egovperspective"; 
    /** 네이처 아이디 */
    public static final String ID_NATURE = PLUGIN_ID + ".natures.egovnature"; 

    /** 액션 셋 */
    public static final String ID_ACTION_SET = PLUGIN_ID + ".actionSet";

    /** 배치 템플릿 프로젝트 마법사 배너 이미지 */
    public static final String IMG_BATCH_TMP_PROJ_WIZ_BANNER = "batch_tmp_proj_wiz_banner"; 
    /** 배치 템플릿 프로젝트 파일 이미지 */
    public static final String IMG_BATCH_TMP_PROJ_WIZ_SAM = "batch_tmp_proj_wiz_sam"; 
    /** 배치 템플릿 프로젝트 디비 이미지 */
    public static final String IMG_BATCH_TMP_PROJ_WIZ_DB = "batch_tmp_proj_wiz_db"; 
    /** 배치 템플릿 프로젝트 스케줄러 이미지 */
    public static final String IMG_BATCH_TMP_PROJ_WIZ_SCHEDULER = "batch_tmp_proj_wiz_scheduler"; 
    /** 배치 템플릿 프로젝트 커맨드라인 이미지 */
    public static final String IMG_BATCH_TMP_PROJ_WIZ_COMMANDLINE = "batch_tmp_proj_wiz_commandline"; 
    /** 배치 템플릿 프로젝트 웹 이미지 */
    public static final String IMG_BATCH_TMP_PROJ_WIZ_WEB = "batch_tmp_proj_wiz_web"; 
    
    /** 항목 이미지 */
    public static final String IMG_ITEM = "item"; 
    /** 하이콘 최상위 경로 */
    private static final String ICONS_PATH = "icons/"; 

    /** 플러그인 공유 인스턴스 */
    private static EgovBatchIdePlugin plugin;
	
    /**
     * 생성자
     */
	public EgovBatchIdePlugin() {
	}

    /**
     * 이미지를 레지스트리에 등록
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
            BatchIdeLog.logError(e);
        }
        return;
    }

    /**
     * 이미지 레스트스리 최기화
     * @param registry
     */
    protected void initializeImageRegistry(ImageRegistry registry) {
        super.initializeImageRegistry(registry);

        registerImage(registry, IMG_BATCH_TMP_PROJ_WIZ_BANNER, "big_BatchTemplate.png"); 
        registerImage(registry, IMG_BATCH_TMP_PROJ_WIZ_SAM, "big_SAM.png"); 
        registerImage(registry, IMG_BATCH_TMP_PROJ_WIZ_DB, "big_DB.png");
        registerImage(registry, IMG_BATCH_TMP_PROJ_WIZ_SCHEDULER, "big_Scheduler_2.png");
        registerImage(registry, IMG_BATCH_TMP_PROJ_WIZ_COMMANDLINE, "big_CommandLine.png");
        registerImage(registry, IMG_BATCH_TMP_PROJ_WIZ_WEB, "big_Web.png");
        return;
    }

    /**
     * 플로그인 시작
     * @param context
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        return;
    }

    /**
     * 플러그인 종료
     * @param context
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
        return;
    }

    /**
     * 공유 플러그인 인스턴스를 반환
     * @return 공유 플러그인 인스턴스
     */
    public static EgovBatchIdePlugin getDefault() {
        return plugin;
    }

    /**
     * 워크스페이스 가져오기
     * @return Workspace
     */
    public static IWorkspace getWorkspace() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        return workspace;
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

    /**
     * 활성화된 워크벤치 쉘 가져오기
     * @return Shell
     */
    public static Shell getActiveWorkbenchShell() {
        Shell shell = getActiveWorkbenchWindow().getShell();
        return shell;
    }

    /**
     * 활성화된 워크벤치 페이지 가져오기
     * @return WorkbenchPage
     */
    public static IWorkbenchPage getActiveWorkbenchPage() {
        IWorkbenchPage workbenchPage =
            getActiveWorkbenchWindow().getActivePage();
        return workbenchPage;
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
     * 플러그인 설치 경로 확인
     * @return 플러그인 설치 경로
     */
    @SuppressWarnings("deprecation")
    public String getInstalledPath() {
        String path = ""; 
        try {
        	
        	//기존 플러그인 설치 경로 확인 오류
            //URL url = Platform.resolve(getDefault().getDescriptor().getInstallURL());        	
        	  
            //대체 기능 (경로 취득 로직변경)2017.7.04
        	URL url = Platform.asLocalURL(plugin.getBundle().getEntry("/"));
        	
            path = (new File(url.getPath())).getAbsolutePath();
        } catch (Exception e) {
            BatchIdeLog.logError(e);
        }
        return path;
    }
}
