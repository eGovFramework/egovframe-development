package net.java.amateras.uml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.RGB;

public abstract class AbstractUMLEntityModel extends AbstractUMLModel {
	
	private static final long serialVersionUID = -8875966173249745953L;
	private Rectangle constraint;
	private List<AbstractUMLConnectionModel> sourceConnections = new ArrayList<AbstractUMLConnectionModel>();
	private List<AbstractUMLConnectionModel> targetConnections = new ArrayList<AbstractUMLConnectionModel>();
	
	private List<AbstractUMLModel> children = new ArrayList<AbstractUMLModel>();
	//flags of show/hide property
	private Map<String, Boolean> filterProperty = new HashMap<String, Boolean>();
		
	public static final String P_CONSTRAINT = "_constraint";
	public static final String P_SOURCE_CONNECTION = "_source_connection";
	public static final String P_TARGET_CONNECTION = "_target_connection";
	public static final String P_CHILDREN = "_children";
	public static final String P_FILTER = "_filter";
	public static final String P_FORCE_UPDATE = "_force_update";
	
	public Map<String, Boolean> getFilterProperty() {
		return filterProperty;
	}
	
	public void setFilterProperty(Map<String, Boolean> filterProperty) {
		this.filterProperty = filterProperty;
		firePropertyChange(P_FILTER, null, filterProperty);
	}
	
	public Rectangle getConstraint() {
		return constraint;
	}
	
	public void addChild(AbstractUMLModel model) {
		children.add(model);
		model.setParent(this);
		firePropertyChange(P_CHILDREN,null,model);
	}
	
	public void removeChild(AbstractUMLModel model) {
		children.remove(model);
		model.setParent(this);
		firePropertyChange(P_CHILDREN,null,model);
	}
	
	public void forceUpdate() {
		firePropertyChange(P_FORCE_UPDATE, null ,null);
	}

	public List<AbstractUMLModel> getChildren(){
		return this.children;
	}
	
	public void setConstraint(Rectangle constraint) {
		if(constraint.x < 0){
			constraint.x = 0;
		}
		if(constraint.y < 0){
			constraint.y = 0;
		}
		this.constraint = constraint;
		firePropertyChange(P_CONSTRAINT, null, constraint);
	}
	
	/** 궞궻긾긢깑궔귞뢯귡긓긨긏긘깈깛 긾긢깑궻믁돿 */
	public void addSourceConnection(AbstractUMLConnectionModel connx) {
		sourceConnections.add(connx);
		firePropertyChange(P_SOURCE_CONNECTION, null, connx);
	}
	
	/** 궞궻긾긢깑궸먝뫏궠귢귡긓긨긏긘깈깛 긾긢깑궻믁돿 */
	public void addTargetConnection(AbstractUMLConnectionModel connx) {
		targetConnections.add(connx);
		firePropertyChange(P_TARGET_CONNECTION, null, connx);
	}
	
	/** 궞궻긾긢깑귩먝뫏뙰궴궥귡긓긨긏긘깈깛궻깏긚긣귩뺅궥 */
	public List<AbstractUMLConnectionModel> getModelSourceConnections() {
		return sourceConnections;
	}
	
	/** 궞궻긾긢깑귩먝뫏먩궴궥귡긓긨긏긘깈깛궻깏긚긣귩뺅궥 */
	public List<AbstractUMLConnectionModel> getModelTargetConnections() {
		return targetConnections;
	}
	
	/** 궞궻긾긢깑귩긓긨긏긘깈깛궻??긚궔귞먛귟뿣궥 */
	public void removeSourceConnection(AbstractUMLConnectionModel connx) {
		sourceConnections.remove(connx);
		firePropertyChange(P_SOURCE_CONNECTION, connx, null);
	}
	
	/** 궞궻긾긢깑귩긓긨긏긘깈깛궻??긒긞긣궔귞먛귟뿣궥 */
	public void removeTargetConnection(AbstractUMLConnectionModel connx) {
		targetConnections.remove(connx);
		firePropertyChange(P_TARGET_CONNECTION, connx, null);
	}
	
	public void setBackgroundColor(RGB backgroundColor) {
		for (Iterator<AbstractUMLModel> iter = children.iterator(); iter.hasNext();) {
			AbstractUMLModel element = (AbstractUMLModel) iter.next();
			element.setBackgroundColor(backgroundColor);
		}
		super.setBackgroundColor(backgroundColor);
	}
	
	public void setForegroundColor(RGB foregroundColor) {
		for (Iterator<AbstractUMLModel> iter = children.iterator(); iter.hasNext();) {
			AbstractUMLModel element = (AbstractUMLModel) iter.next();
			element.setForegroundColor(foregroundColor);
		}
		super.setForegroundColor(foregroundColor);
	}
	
	public void setShowIcon(boolean showIcon) {
		for (Iterator<AbstractUMLModel> iter = children.iterator(); iter.hasNext();) {
			AbstractUMLModel element = (AbstractUMLModel) iter.next();
			element.setShowIcon(showIcon);
		}
		super.setShowIcon(showIcon);
	}
}
