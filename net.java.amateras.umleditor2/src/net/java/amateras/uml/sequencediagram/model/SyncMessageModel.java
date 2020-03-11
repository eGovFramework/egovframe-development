/**
 * 
 */
package net.java.amateras.uml.sequencediagram.model;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Takahiro Shida.
 *
 */
public class SyncMessageModel extends MessageModel {

	private static final long serialVersionUID = 8393983245605292948L;

	public static final String P_ORDER = "_order";
	
	public static final int DELTA_Y = 20;

	private int order, oldOrder;
	
	@SuppressWarnings("unused")
	private static int number = 1;
	
	public SyncMessageModel() {
		setName("");
	}
	
	public void attachSource() {
		super.attachSource();
		number++;
	}
	
	public void detachSource() {
		super.detachSource();
		number--;
	}
	
	public void setOrder(int order) {
		this.oldOrder = this.order;
		this.order = order;
		firePropertyChange(P_ORDER, new Integer(oldOrder), new Integer(this.order));
	}
	
	public int getOrder() {
		return order;
	}
	
	public void updateCallee(Rectangle delta) {
		MessageAcceptableModel target = (MessageAcceptableModel) getTarget();
		target.updateLocation(delta);
	}
	
	public void updateCaller(int size) {
		MessageAcceptableModel source = (MessageAcceptableModel) getSource();
		source.computeCaller(size);
	}
	
	public int getCalleeSize() {
		MessageAcceptableModel target = (MessageAcceptableModel) getTarget();
		return target.getCalleeSize();
	}
	public boolean isRecursive() {
		MessageAcceptableModel source = (MessageAcceptableModel) getSource();
		MessageAcceptableModel target = (MessageAcceptableModel) getTarget();
		return source.equals(target.getOwner());
	}
}
