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
package egovframework.dev.imp.core.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class EmptyConfPreperencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	public EmptyConfPreperencePage() {
	}

	public EmptyConfPreperencePage(String title) {
		super(title);
	}

	public EmptyConfPreperencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	public void init(IWorkbench workbench) {
		//init
		
	}

	@Override
	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();

		String description = "Expand the tree to edit preferences for a specific eGovFrame feature.";  

		Text text = new Text(parent, SWT.READ_ONLY); 
		text.setBackground(parent.getBackground()); 
		text.setText(description); 
		
		return parent;
	}


}
