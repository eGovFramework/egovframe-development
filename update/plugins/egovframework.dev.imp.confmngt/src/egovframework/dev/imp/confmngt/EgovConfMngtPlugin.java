/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package egovframework.dev.imp.confmngt;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * eGovFrame Configuration Managenent PlugIn의 Life Cycle을 관리하는 Activator 클래스
 * 
 * @author 개발환경 개발팀 조윤정
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 *      개정이력(Modification Information)
 *   
 *   수정일      수정자           수정내용
 *  ------- -------- ---------------------------
 *  2011.06.13  조윤정	    최초 생성
 * 
 * 
 * </pre>
 */

public class EgovConfMngtPlugin extends AbstractUIPlugin {

    /**
     * 플러그인 아이디
     */
    public static final String PLUGIN_ID = "egovframework.dev.imp.confmngt"; //$NON-NLS-1$

    /**
     *플러그인 공유 인스턴스 
     */
    private static EgovConfMngtPlugin plugin;

    /**
     * 생성자
     */
    public EgovConfMngtPlugin() {
    }

    /**
     * 플러그인 시작
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /**
     * 플러그인 종료
     * 
     * @param context
     * @throws Exception
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }


    /**
     * 공유 플러그인 인스턴스를 반환
     * 
     * @return 공유 플러그인 인스턴스
     */
    public static EgovConfMngtPlugin getDefault() {
        return plugin;
    }


}
