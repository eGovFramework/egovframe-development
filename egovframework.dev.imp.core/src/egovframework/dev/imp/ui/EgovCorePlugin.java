package egovframework.dev.imp.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EgovCorePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "egovframework.dev.imp.ui";
	
	public static final String IDE_PLUGIN_ID = "egovframework.dev.imp.ide";
	
	public static final String MOBLIE_IDE_PLUGIN_ID = "egovframework.mdev.imp.ide"; 
	
	public static final String BATCH_IDE_PLUGIN_ID = "egovframework.bdev.imp.ide"; 

	// The shared instance
	private static EgovCorePlugin plugin;
	
	public static final String ID_ACTION_SET = IDE_PLUGIN_ID + ".actionSet";
	
	/** 부트 웹 프로젝트 마법사 플러그인 */
    public static final String ID_BOOTWEB_PROJECT_WIZARD = IDE_PLUGIN_ID + ".wizards.egovbootwebprojectwizard"; 
    /** 웹 프로젝트 마법사 플러그인 */
    public static final String ID_WEB_PROJECT_WIZARD = IDE_PLUGIN_ID + ".wizards.egovwebprojectwizard"; 
    /** 템플릿 프로젝트 마법사 플러그인 */
    public static final String ID_TEMPLATE_PROJECT_WIZARD = IDE_PLUGIN_ID + ".wizards.egovtemplateprojectwizard";
    /** 부트 템플릿 프로젝트 마법사 플러그인 */
    public static final String ID_BOOT_TEMPLATE_PROJECT_WIZARD = IDE_PLUGIN_ID + ".wizards.egovboottemplateprojectwizard";
    /** 모바일 웹 프로젝트 마법사 플러그인 */
    public static final String ID_WEB_MOBILE_PROJECT_WIZARD = MOBLIE_IDE_PLUGIN_ID + ".wizards.egovmobliewebprojectwizard";
    /** 모바일 템플릿 프로젝트 마법사 플러그인 */
    public static final String ID_MOBILE_TEMPLATE_PROJECT_WIZARD = MOBLIE_IDE_PLUGIN_ID + ".wizards.egovmobiletemplateprojectwizard";
    /** 배치 템플릿 프로젝트 마법사 플러그인 */
    public static final String ID_BATCH_TEMPLATE_PROJECT_WIZARD = BATCH_IDE_PLUGIN_ID + ".wizards.egovbatchtemplateprojectwizard";
    
    /**
	 * The constructor
	 */
	public EgovCorePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static EgovCorePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
