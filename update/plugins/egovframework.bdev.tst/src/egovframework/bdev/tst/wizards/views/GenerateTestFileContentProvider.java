package egovframework.bdev.tst.wizards.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
/**
 * <pre>
 * Project TreeViewer의 ContentsProvider
 * eGoveBatch Nature를 가지는
 *  project만을 선택해서 보여준다.
 * </pre>
 * @author 배치개발환경 개발팀 조용현
 * @since 2012.06.28
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 *수정일	  	수정자	  수정내용
 *-----------	------	----------------
 *2012.07.24	조용현	최초생성
 *
 * 
 * </pre>
 */
public class GenerateTestFileContentProvider implements ITreeContentProvider {
	
	public Object[] getChildren(Object element) {
    	if (element instanceof IContainer) {
            IContainer container = (IContainer) element;
            if (container.isAccessible()) {
                try {
                    List<IResource> children = new ArrayList<IResource>();
                    IResource[] members = container.members();
                    for (int i = 0; i < members.length; i++) {
                        if (members[i].getType() != IResource.FILE) {
                            children.add(members[i]);
                        }
                    }
                    return children.toArray();
                } catch (CoreException e) {
                }
            }
        }
        return new Object[0];
    }

    public Object[] getElements(Object element) {
    	if (element instanceof IProject[]) {
    		return (IProject[])element;
    	}else{
    		return getChildren(element);
    	}
    }


    public Object getParent(Object element) {
        if (element instanceof IResource) {
			return ((IResource) element).getParent();
		}
        return null;
    }

    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
   
    public void dispose() {
    }

}
