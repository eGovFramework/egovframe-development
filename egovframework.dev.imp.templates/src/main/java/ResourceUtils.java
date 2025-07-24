//
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import org.eclipse.core.resources.IProject;
//import org.eclipse.jdt.core.IClasspathEntry;
//import org.eclipse.jdt.core.IJavaProject;
//import org.eclipse.jdt.core.JavaCore;
//
///**
// * 
// * 리소스 관리 유틸리티 클래스
// * <p><b>NOTE:</b> 리소스 생성시 폴더 존재 여부 확인과 폴더 생성을 위한 유틸리티
// * 
// * @author 개발환경 개발팀 이흥주
// * @since 2009.08.03
// * @version 1.0
// * @see
// *
// * <pre>
// *  == 개정이력(Modification Information) ==
// *   
// *   수정일      수정자           수정내용
// *  -------    --------    ---------------------------
// *   2009.08.03  이흥주          최초 생성
// *
// * </pre>
// */
//public class ResourceUtils {
//
//    
//    /**
//     * 
//     * 자바 프로젝트 로부터 소스 경로 가져오기
//     *
//     * @param project
//     * @return
//     */
//    @SuppressWarnings("unused")
//	public static String[] getSourcePath(IProject project) {
//        ArrayList<String> path = new ArrayList<String>();
//        
//        IJavaProject javaProject = JavaCore.create(project);
//        if ((javaProject == null) || (!javaProject.exists()))
//            return null;
//        try {
//            IClasspathEntry[] classpath;
//            ArrayList<IClasspathEntry> entries;
//            classpath = javaProject.getRawClasspath();
//            entries =
//                new ArrayList<IClasspathEntry>(Arrays.asList(classpath));
//            for(int i=0; i< classpath.length; i++){
//                if (classpath[i].getEntryKind() == IClasspathEntry.CPE_SOURCE){
//                    path.add(classpath[i].getPath().toString());
//                }
//                
//            }
//        }
//        catch(Exception ex)        {
//            ex.printStackTrace();
//        }
//        
//        return (String[])path.toArray(new String[path.size()]);
//    }
//}
