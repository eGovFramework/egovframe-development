/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.dev.imp.dbio.components.fields;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * FormField
 * @author 개발환경 개발팀 김형조
 * @since 2009.02.20
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.02.20  김형조          최초 생성
 *
 * 
 * </pre>
 */
public class FormField {
		
	private final List<IFieldListener> listeners = new LinkedList<IFieldListener>();
	
	/**
	 * 레이블 생성
	 * 
	 * @param toolkit
	 * @param parent
	 * @param text
	 */
	protected Label createLabel(FormToolkit toolkit, Composite parent, String text) {
		return toolkit.createLabel(parent, text);
	}
	
	/**
	 * 필드 리스너 추가
	 * 
	 * @param listener
	 */
	public void addFieldListener(IFieldListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * 필드 리스너 삭제
	 * 
	 * @param listener
	 */
	public void removeFieldListener(IFieldListener listener) {
		listeners.remove(listener);
	}
	
	/**
	 * 이벤트 처리
	 * @param event
	 */
	protected void fireFieldEvent(final FieldEvent event) {
		for (final IFieldListener listener : listeners) {
			SafeRunner.run(new ISafeRunnable() {
				public void handleException(Throwable exception) {
					// DbioLog.logInfo("sham");
				}

				public void run() throws Exception {
					listener.eventOccured(event);
				}
			});
		}
	}
}
