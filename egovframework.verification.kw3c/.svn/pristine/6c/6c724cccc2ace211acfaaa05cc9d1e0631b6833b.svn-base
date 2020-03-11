package egovframework.dev.kw3c;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Kw3cPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "egovframework.dev.kw3c"; //$NON-NLS-1$

	// The shared instance
	private static Kw3cPlugin plugin;
	
	/**
	 * The constructor
	 */
	public Kw3cPlugin() {
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
	public static Kw3cPlugin getDefault() {
		return plugin;
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
            
            //대체 기능 (경로 취득 로직변경)2017.6.14
        	URL url = Platform.asLocalURL(plugin.getBundle().getEntry("/"));
        	
            path = (new File(url.getPath())).getAbsolutePath();
        } catch (Exception e) {
//            MoblieIdeLog.logError(e);
        	e.printStackTrace();
        }
        return path;
    }

}
