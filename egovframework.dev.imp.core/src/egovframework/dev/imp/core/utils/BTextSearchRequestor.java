
package egovframework.dev.imp.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.search.core.text.TextSearchMatchAccess;
import org.eclipse.search.core.text.TextSearchRequestor;

public class BTextSearchRequestor extends TextSearchRequestor {

    private final boolean fIsFileSearchOnly;
    private final String fFileName;
    private final ArrayList<HashMap<String, Object>> result;
    private HashMap<String, Object> detected;


    public BTextSearchRequestor(boolean isFileSearchOnly, String fileName) {
        this.fFileName = fileName;
        fIsFileSearchOnly= isFileSearchOnly;
        result = new ArrayList<HashMap<String, Object>>();
    }

    public String getfFileName() {
		return fFileName;
	}


	/**
     * Notification sent before search starts in the given file. This method is called for all files that are contained
     * in the search scope.
     * Implementors can decide if the file content should be searched for search matches or not.
     * <p>
     * The default behaviour is to search the file for matches.
     * </p>
     * @param file the file resource to be searched.
     * @return If false, no pattern matches will be reported for the content of this file.
     * @throws CoreException implementors can throw a {@link CoreException} if accessing the resource fails or another
     * problem prevented the processing of the search match.
     */
    public boolean acceptFile(IFile file) throws CoreException {
        if (fIsFileSearchOnly) {
//			if ( "*".equals(fFileName) ||  file.getName().equals(fFileName)) {
                if ( detected == null|| !file.equals(detected.get(BTextSearchUtil.K_FILE)))
                    detected = new HashMap<String, Object>();

                detected.put(BTextSearchUtil.K_FILE, (IFile)file);
                result.add(detected);
//			}else{
//				return false;
//			}
        }

        return true;
    }

    /**
     * Accepts the given search match and decides if the search should continue for this file.
     *
     * @param matchAccess gives access to information of the match found. The matchAccess is not a value
     * object. Its value might change after this method is finished, and the element might be reused.
     * @return If false is returned no further matches will be reported for this file.
     * @throws CoreException implementors can throw a {@link CoreException} if accessing the resource fails or another
     * problem prevented the processing of the search match.
     */
    @SuppressWarnings("unchecked")
	public boolean acceptPatternMatch(TextSearchMatchAccess matchRequestor) throws CoreException {

        int start= matchRequestor.getMatchOffset();
        int length= matchRequestor.getMatchLength();
        String matchStr = matchRequestor.getFileContent(start, length);
        IFile file = (IFile)(matchRequestor.getFile());


        if ( detected ==null || !file.equals(detected.get(BTextSearchUtil.K_FILE))){
            detected= new HashMap<String, Object>();
            detected.put(BTextSearchUtil.K_FILE, file);
            result.add(detected);
        }

        List<BMatchAccessor> matched = null;
        if ( detected.get(BTextSearchUtil.K_MATCH) == null){
            matched = new ArrayList<BMatchAccessor>();
            detected.put(BTextSearchUtil.K_MATCH, matched);
        }else matched = (List<BMatchAccessor>) detected.get(BTextSearchUtil.K_MATCH);
        matched.add( new BMatchAccessor(matchStr, start, length));
        return true;
    }

    /**
     *
     *
     * @return
     */
    public List<HashMap<String, Object>> getSearchResult(){
        return result;
    }
}
