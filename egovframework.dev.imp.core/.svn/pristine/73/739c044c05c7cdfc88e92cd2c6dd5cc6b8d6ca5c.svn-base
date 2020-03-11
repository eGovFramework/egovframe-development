
package egovframework.dev.imp.core.utils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.search.core.text.TextSearchEngine;
import org.eclipse.search.core.text.TextSearchScope;
import org.eclipse.search.ui.text.FileTextSearchScope;

public class BTextSearchUtil {
	public final static String K_FILE = "file";
	public final static String K_MATCH = "match_data";

	/**
	 * Eclipse workspace 상의 resource 집합에서 검색을 수행한다. 이때, resource의 하위까지도 검색 대상으로 한다.<BR> 
	 *
	 * @param roots              workspace 상의 resource 집합의 최상의 resource 들의 배열
	 * @param includedText       찾고자 하는 텍스트
	 * @param qualifiedFileName  파일 이름에 대해서도 패턴을 주고자 한다면 이 값을 세팅한다. 
	 * @param isRegexSearch      includexText가 정규식 형태인지 아닌지 여부
	 * @return                   검색결과로 LFileSearchResult 타입의 객체를 멤버로 하는 ArrayList를 리턴한다.
	 */
	public static List<?> findFiles(IResource [] roots, String includedText, String qualifiedFileName, boolean isRegexSearch, IProgressMonitor monitor){
		
		List<?> result = null;
		String[] fileNamePattern =  new String[] {qualifiedFileName};
		
		String includedText2 = includedText;

		if ( includedText == null) includedText2 = "";

		FileTextSearchScope scope = null;
		if ( roots == null ) 
			scope = FileTextSearchScope.newWorkspaceScope(fileNamePattern, true);
		else 
			scope = FileTextSearchScope.newSearchScope(roots,fileNamePattern, true);

		Pattern pattern = createPattern( includedText2, false, isRegexSearch);
		boolean isFileSearchOnly = (pattern.pattern().length() ==0);

		BTextSearchRequestor collector= new BTextSearchRequestor( isFileSearchOnly, qualifiedFileName);

		result = findFiles( scope, pattern, collector, monitor);
		return result;
	}
	
	/**
	 * Eclipse workspace 상의 resource 집합에서 검색을 수행한다. 이때, resource의 하위까지도 검색 대상으로 한다.<BR> 
	 *
	 * @param roots          workspace 상의 resource 집합의 최상의 resource 들의 배열
	 * @param includedText   찾고자 하는 텍스트
	 * @param fileName       파일 이름에 대해서도 패턴을 주고자 한다면 이 값을 세팅한다. 
	 * @param extension      검색 대상 파일의 확장
	 * @param isRegexSearch  includexText가 정규식 형태인지 아닌지 여부
	 * @return               검색결과로 LFileSearchResult 타입의 객체를 멤버로 하는 ArrayList를 리턴한다.
	 */
	public static List<?> findFiles(IResource [] roots, String includedText, String fileName, String extension, boolean isRegexSearch, IProgressMonitor monitor){
		
		List<?> result = null;
		String[] fileNamePattern = null;
		
		String fileName2 = fileName;
		String extension2 = extension;
		String includedText2 = includedText;
		
		if ( fileName2 == null || "".equals(fileName2)) fileName2 = "*";
		else fileName2 = "*" + fileName2 +"*";
		
		if ( extension2 ==null || "".equals(extension2)) extension2 = "*";

		String[] extensions = extension2.split(",");
		fileNamePattern = new String[extensions.length];
		for ( int idx =0; idx < extensions.length;idx++){
			fileNamePattern[idx] = fileName2 + "." + extensions[idx];
		}

		if ( includedText2 == null) includedText2 = "";
		/************************************************************************
		 * second parameter : 하위까지 조사할 것인지 여부
		 ************************************************************************/
		FileTextSearchScope scope = null;
		if ( roots == null ) 
			scope = FileTextSearchScope.newWorkspaceScope(fileNamePattern, true);
		else 
			scope = FileTextSearchScope.newSearchScope(roots,fileNamePattern, true);
		
		/************************************************************************
		 * second parameter : true to create a case insensitive pattern
		 * thrid parameter : true if the passed string already is a reg-ex pattern
		 ************************************************************************/ 
		Pattern pattern = createPattern( includedText2, false, isRegexSearch);
		boolean isFileSearchOnly = (pattern.pattern().length() ==0);

		BTextSearchRequestor collector= new BTextSearchRequestor( isFileSearchOnly, fileName2);

		result = findFiles( scope, pattern, collector, monitor);
		return result;
	}
	
	/**
	 * 특정 검색 대상 범위(TextSearchScope)에서 원하는 패턴을 검색하여 결과를 리턴한다.<BR> 
	 *
	 * @param scope      검색 대상 범위
	 * @param pattern    검색하고자 하는 패턴
	 * @param collector  검색결과를 수집하는 TextSearchRequestor
	 * @param monitor
	 * @return           검색결과로 LFileSearchResult 타입의 객체를 멤버로 하는 ArrayList를 리턴한다.
	 */
	public static List<?> findFiles(TextSearchScope scope, Pattern pattern, BTextSearchRequestor collector, IProgressMonitor monitor){
		List<?> result = null;
		/************************************************************************
		 * monitor the progress monitor to use
		 ************************************************************************/
		IStatus status = TextSearchEngine.create().search(scope, collector, pattern, null);
		
		if (status !=null && status.getCode() == IStatus.OK){
			result = collector.getSearchResult();
		}
		
		return result;		
	}
	
	/**
	 * 검색하고자 하는 text 를 정규식 형태로 변환한다.<BR> 
	 *
	 * @param pattern           검색하고자 하는 text
	 * @param isCaseSensitive   대소문자구분
	 * @param isRegexSearch     검색하고자 하는 Text 가 정규식인지 여부
	 * @return
	 * @throws PatternSyntaxException
	 */
	public static Pattern createPattern(String pattern, boolean isCaseSensitive, boolean isRegexSearch) throws PatternSyntaxException {
		String pattern2 = pattern;
		if (!isRegexSearch) {
			pattern2=  asRegEx(pattern2, new StringBuffer()).toString();			
		}
		
		if (!isCaseSensitive)
			return Pattern.compile(pattern2, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.MULTILINE);
		
		return Pattern.compile(pattern2, Pattern.MULTILINE);
	}
	
	/**
	 * 검색하고자 하는 text 를 정규식 형태로 변환한다.<BR> 
	 *
	 * @param stringMatcherPattern
	 * @param out
	 * @return
	 */
	private static StringBuffer asRegEx(String stringMatcherPattern, StringBuffer out) {	
		boolean escaped= false;
		boolean quoting= false;
		
		int i= 0;
		while (i < stringMatcherPattern.length()) {
			char ch= stringMatcherPattern.charAt(i++);
			
			if (ch == '*' && !escaped) {
				if (quoting) {
					out.append("\\E"); //$NON-NLS-1$
					quoting= false;
				}
				out.append(".*"); //$NON-NLS-1$
				escaped= false;
				continue;
			} else if (ch == '?' && !escaped) {
				if (quoting) {
					out.append("\\E"); //$NON-NLS-1$
					quoting= false;
				}
				out.append("."); //$NON-NLS-1$
				escaped= false;
				continue;
			} else if (ch == '\\' && !escaped) {
				escaped= true;
				continue;								
				
			} else if (ch == '\\' && escaped) {
				escaped= false;
				if (quoting) {
					out.append("\\E"); //$NON-NLS-1$
					quoting= false;
				}
				out.append("\\\\"); //$NON-NLS-1$
				continue;								
			}
			
			if (!quoting) {
				out.append("\\Q"); //$NON-NLS-1$
				quoting= true;
			}
			if (escaped && ch != '*' && ch != '?' && ch != '\\')
				out.append('\\');
			out.append(ch);
			escaped= ch == '\\';
			
		}
		if (quoting)
			out.append("\\E"); //$NON-NLS-1$
		
		return out;
	}
}
