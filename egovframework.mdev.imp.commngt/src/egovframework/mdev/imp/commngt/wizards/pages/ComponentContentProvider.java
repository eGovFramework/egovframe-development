package egovframework.mdev.imp.commngt.wizards.pages;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import egovframework.mdev.imp.commngt.wizards.model.Category;
import egovframework.mdev.imp.commngt.wizards.model.Component;
import egovframework.mdev.imp.commngt.wizards.model.IComponentElement;

/**
 * 공통컴포넌트 정보 생성 시 ContentProvider 클래스
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
public class ComponentContentProvider implements ITreeContentProvider {
	/** viewerInput 데이터 */
	private List<IComponentElement> viewerInput = null;
	/** Provider dispose */
	public void dispose() {
		//dispose
	}
	/** 입력값변경 */
	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof List<?>)
			viewerInput = (List<IComponentElement>)newInput;
	}

	/**
	 * 정보를 구성하는 엘리먼트를 가져옴
	 * @param inputElment viewer의 input
	 */
	@SuppressWarnings("rawtypes")
	public Object[] getElements(Object inputElement) {
		// TreeViewer가 최초로 화면에 보여질 때 이클립스가 호출

		if ( inputElement instanceof List){
			return ((List)inputElement).toArray();
		}
		return null;
	}
	
	/** 자식노드 */
	public Object[] getChildren(Object parentElement) {
		if ( parentElement instanceof IComponentElement){
			Object children = ((IComponentElement)parentElement).getChildren();
			if (children !=null)
				return ((List<?>)children).toArray();
		}
		return null;
	}
	
	/** 부모노드 */
	public Object getParent(Object element) {
		
		if(element instanceof Component){
			for(IComponentElement inputElement: viewerInput){
				if(inputElement instanceof Category && inputElement.getChildren().contains(element)){
					return (Category)inputElement;
				}
			}
			
		}
		return null;
	}

	/** 트리의 +/- 이미지를 구성하기 위해 호출됨 */
	public boolean hasChildren(Object element) {
		if ( element instanceof IComponentElement){
			Object children = ((IComponentElement)element).getChildren();
			
			return children ==null? false : true;
		}
		return false;
	}

}
