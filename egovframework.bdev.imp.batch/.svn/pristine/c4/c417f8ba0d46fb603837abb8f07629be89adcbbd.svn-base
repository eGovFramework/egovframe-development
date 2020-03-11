package egovframework.bdev.imp.batch;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import egovframework.bdev.imp.batch.common.BatchLog;

/**
 * eGovFramework Batch 플러그인의 라이프 사이클을 관리하는 Activator 클래스
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
public class EgovBatchPlugin extends AbstractUIPlugin {

	/** 플러그인 아이디 */
	public static final String PLUGIN_ID = "egovframework.bdev.imp.batch"; //$NON-NLS-1$

	/** 배치 작업 생성 마법사 배너 이미지 */
	public static final String IMG_BATCH_JOB_WIZ_BANNER = "batch_job_wiz_banner";
	/** 배치 실행모듈 생성 마법사 작업 이미지 */
	public static final String IMG_BATCH_JOB_LIST_JOB = "batch_job_list_job";
	/** 배치 실행모듈 생성 마법사 스텝 이미지 */
	public static final String IMG_BATCH_JOB_LIST_STEP = "batch_job_list_step";
	/** 배치 실행모듈 생성 마법사 디시젼 이미지 */
	public static final String IMG_BATCH_JOB_LIST_DECISION = "batch_job_list_decision";
	/** 배치 실행모듈 생성 마법사 작업추가 이미지 */
	public static final String IMG_BATCH_JOB_ADD = "batch_job_add";
	/** 배치 실행모듈 생성 마법사 스텝디시젼추가 이미지 */
	public static final String IMG_BATCH_STEPDECISION_ADD = "batch_stepdecision_add";
	/** 배치 실행모듈 생성 마법사 배너 이미지 */
	public static final String IMG_BATCH_JOB_LAUNCHER_WIZ_BANNER = "batch_job_launcher_wiz_banner";

	/** 항목 이미지 */
	public static final String IMG_ITEM = "item";
	/** 하이콘 최상위 경로 */
	private static final String ICONS_PATH = "icons/";

	/** 플러그인 공유 인스턴스 */
	private static EgovBatchPlugin plugin;

	/**
	 * 생성자
	 */
	public EgovBatchPlugin() {
	}

	///
	/**
	* 이미지를 레지스트리에 등록
	* @param registry
	* @param key
	* @param fileName
	*/
	@SuppressWarnings("deprecation")
	private void registerImage(ImageRegistry registry, String key, String fileName) {
		try {
			IPath path = new Path(ICONS_PATH + fileName);
			URL url = find(path);
			if (url != null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception e) {
			BatchLog.logError(e);
		}
		return;
	}

	/**
	 * 이미지 레스트스리 최기화
	 * @param registry
	 */
	protected void initializeImageRegistry(ImageRegistry registry) {
		super.initializeImageRegistry(registry);

		registerImage(registry, IMG_BATCH_JOB_WIZ_BANNER, "big_Job.png");
		registerImage(registry, IMG_BATCH_JOB_LAUNCHER_WIZ_BANNER, "big_JobLauncher.png");

		registerImage(registry, IMG_BATCH_JOB_LIST_JOB, "sm_JobList.png");
		registerImage(registry, IMG_BATCH_JOB_LIST_STEP, "sm_StepList.png");
		registerImage(registry, IMG_BATCH_JOB_LIST_DECISION, "sm_DecisionList.png");

		registerImage(registry, IMG_BATCH_JOB_ADD, "job_Add.png");
		registerImage(registry, IMG_BATCH_STEPDECISION_ADD, "stepDecision_Add.png");

		return;
	}

	///

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
	public static EgovBatchPlugin getDefault() {
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
}
