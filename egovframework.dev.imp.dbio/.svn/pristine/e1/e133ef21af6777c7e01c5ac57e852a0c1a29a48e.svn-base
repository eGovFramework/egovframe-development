/*******************************************************************************
  * Copyright (c) 2000-2007 IBM Corporation and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  * IBM Corporation - initial API and implementation
  *******************************************************************************/
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
package egovframework.dev.imp.dbio.editor;

import org.eclipse.datatools.sqltools.common.ui.util.HTMLTextPresenter;
import org.eclipse.datatools.sqltools.core.DatabaseVendorDefinitionId;
import org.eclipse.datatools.sqltools.core.SQLDevToolsConfiguration;
import org.eclipse.datatools.sqltools.core.SQLToolsFacade;
import org.eclipse.datatools.sqltools.core.services.SQLEditorService;
import org.eclipse.datatools.sqltools.editor.contentassist.ISQLDBProposalsService;
import org.eclipse.datatools.sqltools.sql.ISQLSyntax;
import org.eclipse.datatools.sqltools.sqleditor.internal.PreferenceConstants;
import org.eclipse.datatools.sqltools.sqleditor.internal.SQLEditorPlugin;
import org.eclipse.datatools.sqltools.sqleditor.internal.indent.SQLAutoIndentStrategy;
import org.eclipse.datatools.sqltools.sqleditor.internal.indent.SQLCommentAutoIndentStrategy;
import org.eclipse.datatools.sqltools.sqleditor.internal.indent.SQLStringAutoIndentStrategy;
import org.eclipse.datatools.sqltools.sqleditor.internal.sql.SQLCodeScanner;
import org.eclipse.datatools.sqltools.sqleditor.internal.sql.SQLCompletionProcessor;
import org.eclipse.datatools.sqltools.sqleditor.internal.sql.SQLDBProposalsService;
import org.eclipse.datatools.sqltools.sqleditor.internal.sql.SQLDoubleClickStrategy;
import org.eclipse.datatools.sqltools.sqleditor.internal.sql.SQLPartitionScanner;
import org.eclipse.datatools.sqltools.sqleditor.internal.sql.SQLWordStrategy;
import org.eclipse.datatools.sqltools.sqleditor.internal.utils.SQLColorProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;

/**
 * SQLSourceViewerConfiguration
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
@SuppressWarnings("restriction")
public class SQLSourceViewerConfiguration extends SourceViewerConfiguration {
	    /** The completion processor for completing the user's typing. */
	    private SQLCompletionProcessor completionProcessor;
	    /** The content assist proposal service for database objects. */
	    private ISQLDBProposalsService fDBProposalsService;
		private IConnectionInfoEditor editor;
	    
	    static class SingleTokenScanner extends BufferedRuleBasedScanner {
	        public SingleTokenScanner( TextAttribute attribute ) {
	            setDefaultReturnToken( new Token( attribute ));
	        }
	    }

	    /**
	     * 생성자
	     * 
	     * @param editor
	     */
	    public SQLSourceViewerConfiguration(IConnectionInfoEditor editor) {
			this.setEditor(editor);
			completionProcessor = new SQLCompletionProcessor(); 
	    }
	    
	    private void setEditor(IConnectionInfoEditor editor) {
	    	this.editor = editor;
	    }
	    	    
	    /**
	     * IAutoEditStrategy 반환
	     * 
	     * @return IAutoEditStrategy
	     */
	    public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType)
	    {
	    	String sqlCode = SQLPartitionScanner.SQL_CODE;
	    	String defaultContentType = IDocument.DEFAULT_CONTENT_TYPE;
	    	String sqlCommnet = SQLPartitionScanner.SQL_COMMENT; 
	    	String sqlMultilineComment = SQLPartitionScanner.SQL_MULTILINE_COMMENT;
	    	String sqlString = SQLPartitionScanner.SQL_STRING;
	    	String sqlDoubleQuotesIdentifier = SQLPartitionScanner.SQL_DOUBLE_QUOTES_IDENTIFIER;
	    	
	    	IAutoEditStrategy[] result = new IAutoEditStrategy[1];
	    	
	        if (sqlCode.equals(contentType) || defaultContentType.equals(contentType))
	        {
	            result[0] = new SQLAutoIndentStrategy(SQLPartitionScanner.SQL_PARTITIONING);
	            return result;
	        }
	        else if (sqlCommnet.equals(contentType)
	        || sqlMultilineComment.equals(contentType))
	        {
	            result[0] = new SQLCommentAutoIndentStrategy(SQLPartitionScanner.SQL_PARTITIONING);
	            return result;
	        }
	        else if (sqlString.equals(contentType) || sqlDoubleQuotesIdentifier.equals(contentType))
	        {
	            result[0] = new SQLStringAutoIndentStrategy(SQLPartitionScanner.SQL_STRING);
	            return result;
	        }
	        return null;
	    }

	    /**
	     * ConfiguredDocumentPartitioning 정보 반환
	     * 
	     * @return ConfiguredDocumentPartitioning 정보
	     */
	    public String getConfiguredDocumentPartitioning( ISourceViewer sourceViewer ) {
	    	String sqlPartitioning = SQLPartitionScanner.SQL_PARTITIONING;
	        return sqlPartitioning;
	    }

	    /**
	     * ContentAssistant 객체 반환
	     * 
	     * return IContentAssistant
	     */
	    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
	    	SQLEditorPlugin defaultEditorPlugin = SQLEditorPlugin.getDefault();
	    	IPreferenceStore preferenceStore = defaultEditorPlugin.getPreferenceStore();
	    	
	        ContentAssistant contentAssistant = new ContentAssistant();
	    
	        String partitionInfo = getConfiguredDocumentPartitioning( sourceViewer );
	        contentAssistant.setDocumentPartitioning(partitionInfo);
	        
	        // Set content assist processors for various content types.
	        if (completionProcessor == null)
	        {
	        	completionProcessor = new SQLCompletionProcessor();
	        }
	        
	        contentAssistant.setContentAssistProcessor( completionProcessor, IDocument.DEFAULT_CONTENT_TYPE);
	        ISQLDBProposalsService dbProSvc = getDBProposalsService();
	        
	        if (dbProSvc != null) {
	        	completionProcessor.setDBProposalsService( dbProSvc );
	        }
	    
	        // Configure how content assist information will appear.
	        boolean enavleAutoActivation = preferenceStore.getBoolean(PreferenceConstants.ENABLE_AUTO_ACTIVATION);
	        boolean insertSingleProposalsAuto = preferenceStore.getBoolean(PreferenceConstants.INSERT_SINGLE_PROPOSALS_AUTO);
	        int preferenceInt = preferenceStore.getInt(PreferenceConstants.AUTO_ACTIVATION_DELAY);
	        int proposalOverlay = IContentAssistant.PROPOSAL_OVERLAY ;
	        int contextInfoAbove = IContentAssistant.CONTEXT_INFO_ABOVE;
	        IInformationControlCreator creator = getInformationControlCreator(sourceViewer);
	        Color frontColor = new Color(SQLEditorPlugin.getDisplay(), 0,0,0);
	        Color backColor = new Color(SQLEditorPlugin.getDisplay(), 255,255,255);
	        
	        contentAssistant.enableAutoActivation(enavleAutoActivation);
	        contentAssistant.setAutoActivationDelay(preferenceInt);
	        contentAssistant.setProposalPopupOrientation(proposalOverlay);
	        contentAssistant.setInformationControlCreator(creator);
	        contentAssistant.setContextInformationPopupOrientation(contextInfoAbove);
	        contentAssistant.setContextInformationPopupForeground(frontColor);
	        contentAssistant.setContextInformationPopupBackground(backColor);
	        //Set auto insert mode.
	        contentAssistant.enableAutoInsert(insertSingleProposalsAuto);
	        
	        return contentAssistant;
	        
	    }

		/**
		 * IInformationControlCreator 반환
		 * 
		 * return IInformationControlCreator
		 */
	    public IInformationControlCreator getInformationControlCreator(ISourceViewer sourceViewer) { //getInformationControlCreator
	        return new IInformationControlCreator() {
	            public IInformationControl createInformationControl(Shell parent) {
	                return new DefaultInformationControl(parent, new HTMLTextPresenter(true));
	            }
	        };
	    }
	    
	    /**
	     * ContentFormatter 반환
	     * 
	     * return ContentFormatter
	     */
	    public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
	    	String sqlPartitioning = SQLPartitionScanner.SQL_PARTITIONING;
	    	DatabaseVendorDefinitionId vendor = editor.getConnectionInfo().getDatabaseVendorDefinitionId();
	        ContentFormatter formatter = new ContentFormatter();
	        SQLDevToolsConfiguration factory =	SQLToolsFacade.getConfigurationByVendorIdentifier(vendor);
	    	ISQLSyntax sqlSyntax = factory.getSQLService().getSQLSyntax();
	        
	        formatter.setDocumentPartitioning(sqlPartitioning);
	        
	        IFormattingStrategy strategy = new SQLWordStrategy(sqlSyntax);
	        formatter.setFormattingStrategy( strategy, IDocument.DEFAULT_CONTENT_TYPE );
	        
	        return formatter;
	    }

	    /**
	     * ISQLDBProposalsService 반환
	     * 
	     * @return DBProposalsService 객체 
	     */
	    public ISQLDBProposalsService getDBProposalsService() {
	    	if (fDBProposalsService == null) {
	    		SQLDevToolsConfiguration toolsConfig =
	    			SQLToolsFacade.getConfigurationByVendorIdentifier(editor.getConnectionInfo().getDatabaseVendorDefinitionId());
	    		SQLEditorService editorService = toolsConfig.getSQLEditorService();
	    		ISQLDBProposalsService dbProposalsService = editorService.getSQLDBProposalsService(editor.getConnectionInfo());
	    		if (dbProposalsService == null) {
	    			dbProposalsService = new SQLDBProposalsService(editor.getConnectionInfo());
	    		}
	    		setDBProposalsService(dbProposalsService);
	    	}
	        return fDBProposalsService;
	    }
	    
	    /**
	     * TextDoubleClickStrategy 반환
	     * 
	     * @return TextDoubleClickStrategy 객체
	     */
	    public ITextDoubleClickStrategy getDoubleClickStrategy( ISourceViewer sourceViewer, String contentType) {
	        return new SQLDoubleClickStrategy();
	    }

	    /**
	     * PresentationReconciler 반환
	     * 
	     * return PresentationReconciler 객체
	     */
	    public IPresentationReconciler getPresentationReconciler( ISourceViewer sourceViewer ) {

	        SQLColorProvider colorProv = SQLEditorPlugin.getDefault().getSQLColorProvider();
	        
	        PresentationReconciler pr = new PresentationReconciler();
	        String docPartition = getConfiguredDocumentPartitioning( sourceViewer );
	        pr.setDocumentPartitioning( docPartition );

	        SQLEditorPlugin defaultPlugin = SQLEditorPlugin.getDefault();
	        DatabaseVendorDefinitionId vendor = editor.getConnectionInfo().getDatabaseVendorDefinitionId();
	        
	        SQLCodeScanner codeScan = new SQLCodeScanner( defaultPlugin.getSQLColorProvider() );
	        SQLDevToolsConfiguration factory = SQLToolsFacade.getConfigurationByVendorIdentifier(vendor);
	        
	        if (factory != null){
	        	codeScan.setSQLSyntax(factory.getSQLService().getSQLSyntax());
	        }
	        DefaultDamagerRepairer defaultDr = new DefaultDamagerRepairer(codeScan );
	        
	        pr.setDamager( defaultDr, IDocument.DEFAULT_CONTENT_TYPE );
	        pr.setRepairer( defaultDr, IDocument.DEFAULT_CONTENT_TYPE );
	        
	        TextAttribute attr = colorProv.createTextAttribute( SQLColorProvider.SQL_MULTILINE_COMMENT);
	        defaultDr = new DefaultDamagerRepairer( new SingleTokenScanner( attr ));
	        pr.setDamager(defaultDr, SQLPartitionScanner.SQL_MULTILINE_COMMENT);
	        pr.setRepairer(defaultDr, SQLPartitionScanner.SQL_MULTILINE_COMMENT);

	        attr = colorProv.createTextAttribute( SQLColorProvider.SQL_COMMENT );
	        defaultDr = new DefaultDamagerRepairer( new SingleTokenScanner( attr));
	        pr.setDamager( defaultDr, SQLPartitionScanner.SQL_COMMENT );
	        pr.setRepairer( defaultDr, SQLPartitionScanner.SQL_COMMENT );

	        attr = colorProv.createTextAttribute( SQLColorProvider.SQL_QUOTED_LITERAL );
	        defaultDr = new DefaultDamagerRepairer( new SingleTokenScanner( attr));
	        pr.setDamager( defaultDr, SQLPartitionScanner.SQL_STRING );
	        pr.setRepairer( defaultDr, SQLPartitionScanner.SQL_STRING );

	        attr = colorProv.createTextAttribute( SQLColorProvider.SQL_DELIMITED_IDENTIFIER );
	        defaultDr = new DefaultDamagerRepairer( new SingleTokenScanner( attr));
	        pr.setDamager( defaultDr, SQLPartitionScanner.SQL_DOUBLE_QUOTES_IDENTIFIER );
	        pr.setRepairer( defaultDr, SQLPartitionScanner.SQL_DOUBLE_QUOTES_IDENTIFIER );

	        return pr;
	    }

	    /**
	     * DBProposalsService 정보 설정
	     * 
	     */
	    public void setDBProposalsService( ISQLDBProposalsService dbProposalsService ) {
	        fDBProposalsService = dbProposalsService;
	        if (completionProcessor != null) {
	        	completionProcessor.setDBProposalsService( dbProposalsService );
	        }
	    }
	    
	    /**
	     * ConfiguredContentTypes 정보
	     * 
	     * @param sourceViewer
	     * @return ConfiguredContentTypes 정보
	     */
	    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer)
	    {
	        return SQLPartitionScanner.SQL_PARTITION_TYPES;
	    }

	    /**
	     * DefaultPrefixes 정보 반환
	     * 
	     * @param sourceViewer
	     * @param contentType
	     * @return DefaultPrefixes 정보
	     */
	    public String[] getDefaultPrefixes(ISourceViewer sourceViewer, String contentType) 
	    {
	        return new String[] 
	        {
	            "--", "" 
	        }
	        ;
	    }

}
