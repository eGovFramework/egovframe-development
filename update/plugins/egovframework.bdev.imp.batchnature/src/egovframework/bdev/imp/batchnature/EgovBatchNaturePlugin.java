package egovframework.bdev.imp.batchnature;

import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import egovframework.bdev.imp.batchnature.common.BatchNatureLog;

public class EgovBatchNaturePlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "egovframework.bdev.imp.ide.natures.egovnature"; //$NON-NLS-1$

	/** 배치 작업 생성 마법사 배너 이미지 */
    public static final String IMG_BATCH_NATURE = "batch_nature";
    /** 하이콘 최상위 경로 */
    private static final String ICONS_PATH = "icons/"; 
    
	private static EgovBatchNaturePlugin plugin;

	public EgovBatchNaturePlugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static EgovBatchNaturePlugin getDefault() {
		return plugin;
	}

	/**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path){
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
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
        	BatchNatureLog.logError(e);
        }
        return;
    }
    
    /**
     * 이미지 레스트스리 최기화
     * @param registry
     */
    protected void initializeImageRegistry(ImageRegistry registry) {
        super.initializeImageRegistry(registry);

        registerImage(registry, IMG_BATCH_NATURE, "eGovframeBatchNature.png"); 
        
        return;
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
    
}
