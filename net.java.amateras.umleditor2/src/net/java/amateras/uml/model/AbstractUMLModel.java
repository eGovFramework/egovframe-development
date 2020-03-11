package net.java.amateras.uml.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

import net.java.amateras.uml.UMLColorRegistry;
import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.properties.BooleanPropertyDescriptor;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 */
public abstract class AbstractUMLModel implements Serializable, IPropertySource {

	private static final long serialVersionUID = 79681161290534534L;

	public static final String P_BACKGROUND_COLOR = "_background";
	
	public static final String P_FOREGROUND_COLOR = "_foreground";
	
	public static final String P_SHOW_ICON = "_showicon";

	private RGB backgroundColor;
	
	private RGB foregroundColor;
	
	private boolean showIcon = true;
	
	private AbstractUMLEntityModel parent;
	
	/** 깏긚긥궻깏긚긣 */
	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	/** 깏긚긥궻믁돿 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	/** 긾긢깑궻빾뛛귩믅뭢 */
	public void firePropertyChange(String propName, Object oldValue,Object newValue) {
		listeners.firePropertyChange(propName, oldValue, newValue);
	}

	/** 깏긚긥궻랁룣 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	public Object getEditableValue() {
		return this;
	}

	public void setParent(AbstractUMLEntityModel parent) {
		this.parent = parent;
	}
	
	public AbstractUMLEntityModel getParent() {
		return parent;
	}
	
	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[] {
				new ColorPropertyDescriptor(P_BACKGROUND_COLOR, UMLPlugin
						.getDefault().getResourceString("property.background")),
				new ColorPropertyDescriptor(P_FOREGROUND_COLOR, UMLPlugin
						.getDefault().getResourceString("property.foreground")),
				new BooleanPropertyDescriptor(P_SHOW_ICON, UMLPlugin
						.getDefault().getResourceString("property.showicon")) };
	}

	public Object getPropertyValue(Object id) {
		if (id.equals(P_BACKGROUND_COLOR)) {
			return backgroundColor;
		} else if (P_FOREGROUND_COLOR.equals(id)) {
			return foregroundColor;
		} else if (P_SHOW_ICON.equals(id)) {
			return new Boolean(isShowIcon());
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		return P_BACKGROUND_COLOR.equals(id) || P_FOREGROUND_COLOR.equals(id)
				|| P_SHOW_ICON.equals(id);
	}

	public void setPropertyValue(Object id, Object value) {
		if (P_BACKGROUND_COLOR.equals(id)) {
			setBackgroundColor((RGB) value);
		} else if (P_FOREGROUND_COLOR.equals(id)) {
			setForegroundColor((RGB) value);
		} else if (P_SHOW_ICON.equals(id)) {
			setShowIcon(((Boolean) value).booleanValue());
		}
	}

	public void resetPropertyValue(Object id) {
	}

	public Color getBackgroundColor() {
		return UMLColorRegistry.getColor(backgroundColor);
	}

	public void setBackgroundColor(RGB backgroundColor) {
		this.backgroundColor = backgroundColor;
		firePropertyChange(P_BACKGROUND_COLOR, null, backgroundColor);
	}

	public Color getForegroundColor() {
		return UMLColorRegistry.getColor(foregroundColor);
	}

	public void setForegroundColor(RGB foregroundColor) {
		this.foregroundColor = foregroundColor;
		firePropertyChange(P_FOREGROUND_COLOR, null, foregroundColor);
	}

	public boolean isShowIcon() {
		return showIcon;
	}

	public void setShowIcon(boolean showIcon) {
		this.showIcon = showIcon;
		firePropertyChange(P_SHOW_ICON, null, new Boolean(showIcon));
	}
	
	public void copyPresentation(AbstractUMLModel model) {
		if (backgroundColor != null) {
			model.setBackgroundColor(backgroundColor);
		}
		if (foregroundColor != null) {
			model.setForegroundColor(foregroundColor);
		}
		model.setShowIcon(showIcon);
	}
//	/**
//	 * 덙릶궳뱊궠귢궫긆긳긙긃긏긣궕궞궻긆긳긙긃긏긣궴뱳궢궋궔궵궎궔귩뵽믦궢귏궥갃
//	 * 긢긲긅깑긣궳궼RuntimeException궕throw궠귢귡귝궎궸궶궯궲궓귟갂
//	 * 긮긙깄귺깑긾긢깑궼궞궻긽?긞긤귩밙먛궸렳몧궥귡뷠뾴궕궇귟귏궥갃
//	 */
//	public boolean equals(Object obj){
//		throw new RuntimeException("equals is not implemented!");
//	}
}
