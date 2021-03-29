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

import org.eclipse.datatools.sqltools.core.DatabaseVendorDefinitionId;
import org.eclipse.datatools.sqltools.core.SQLDevToolsConfiguration;
import org.eclipse.datatools.sqltools.core.SQLToolsFacade;
import org.eclipse.datatools.sqltools.sqleditor.internal.sql.SQLPartitionScanner;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * SQL Source Viewer
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
public class SQLSourceViewer extends SourceViewer {

	private IConnectionInfoEditor editor;
	
	/**
	 * 생성자
	 * @param parent
	 * @param editor
	 */
	public SQLSourceViewer(Composite parent, IConnectionInfoEditor editor) {
		super(parent, new VerticalRuler(2), SWT.FULL_SELECTION | SWT.WRAP | SWT.V_SCROLL);
		this.setEditor(editor);
		//접속 정보는 나중에 설정하도록 한다.- joe
		configure(new SQLSourceViewerConfiguration(editor));
		setDocument(createDocument());
	}
	
	private void setEditor(IConnectionInfoEditor editor) {
		this.editor = editor;
	}
	/**
	 * 쿼리 설정
	 * @param query
	 */
	public void setQuery(String query) {
		getDocument().set(query);
	}
	
	/**
	 * 쿼리 반환
	 * @return 쿼리
	 */
	public String getQuery() {
		return getDocument().get();
	}

	/**
	 * 문서 생성
	 * 
	 * @return 문서 정보
	 */
	private IDocument createDocument() {
		DatabaseVendorDefinitionId vendorId = editor.getConnectionInfo().getDatabaseVendorDefinitionId(); 

    	SQLDevToolsConfiguration factory = SQLToolsFacade.getConfigurationByVendorIdentifier(vendorId);
        SQLPartitionScanner _sqlPartitionSanner = new SQLPartitionScanner(factory.getSQLService().getSQLSyntax());

        IDocument document = new Document();
        IDocumentPartitioner partitioner = new FastPartitioner(
        		_sqlPartitionSanner, new String[] {
        				SQLPartitionScanner.SQL_COMMENT,
        				SQLPartitionScanner.SQL_MULTILINE_COMMENT,
        				SQLPartitionScanner.SQL_STRING,
        				SQLPartitionScanner.SQL_DOUBLE_QUOTES_IDENTIFIER });
        partitioner.connect(document);
        if (document instanceof IDocumentExtension3) {
            IDocumentExtension3 extension3 = (IDocumentExtension3) document;
            extension3.setDocumentPartitioner(SQLPartitionScanner.SQL_PARTITIONING, partitioner);
        } else {
        	document.setDocumentPartitioner(partitioner);
        }
		return document;
	}
}
