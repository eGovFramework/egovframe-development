package egovframework.dev.imp.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;


/**
 * JavaProject의 패키지 정보를 찾는 유틸 클래스
 * @author 개발환경 개발팀 박수림
 * @since 2011.06.13
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.06.13  박수림          최초 생성
 * 
 * 
 * </pre>
 */
public class EgovJavaElementUtil {
	/** 패키지 찾기 */
	public static IPackageFragment findPackage(IJavaProject project,
			String srcfolder, String pName) throws CoreException {
		IPackageFragmentRoot root = getSourceFolder(project, srcfolder);

		IPackageFragment p = root.getPackageFragment(pName.replace('/', '.'));

		return p;
	}

	/** 소스폴더 구하기 (param: javaProject) */
    public static IPackageFragmentRoot getSourceFolder(IJavaProject project) throws JavaModelException{
        IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();

        for (int idx = 0; roots != null && idx < roots.length; idx++) {
            if (roots[idx].getKind() == IPackageFragmentRoot.K_SOURCE) {
                return roots[idx];
            }
        }
        return null;
    }
    
	/** 소스폴더 List 구하기 (param: javaProject) */
    public static List<IPackageFragmentRoot> getSourceFolders(IJavaProject project) throws JavaModelException{
        IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
        List<IPackageFragmentRoot> src = new ArrayList<IPackageFragmentRoot>();

        for (int idx = 0; roots != null && idx < roots.length; idx++) {
            if (roots[idx].getKind() == IPackageFragmentRoot.K_SOURCE) {
            	src.add(roots[idx]);
            }
        }
        return src;
    }

	/** 소스폴더 구하기(param: javaProject, name) */
    public static IPackageFragmentRoot getSourceFolder(IJavaProject project, String name) throws CoreException{
        if (name == null || "".equals(name)) return getSourceFolder(project);

        IFolder folder = project.getProject().getFolder(name);

        if (!folder.exists()) folder.create(true, true, null);
        IPackageFragmentRoot root = project.getPackageFragmentRoot(folder);

        return root;
    }

	/** 자바프로젝트 여부 리턴 */
    public static boolean isJavaProject(IProject project) throws CoreException{
        if (project != null && project.isNatureEnabled(JavaCore.NATURE_ID)) {
            return true;
        }
        return false;
    }

}
