/**
 *
 */
package net.java.amateras.uml.sequencediagram.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.java.amateras.uml.model.AbstractUMLConnectionModel;
import net.java.amateras.uml.model.AbstractUMLModel;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Takahiro Shida.
 *
 */
public class ActivationModel extends MessageAcceptableModel {


	private static final long serialVersionUID = -2621679091279222643L;

	public static final String P_SOURCE_CONNECTION = "_source_connection";

	public static final String P_TARGET_CONNECTION = "_target_connection";

	public static final String P_CHILDREN = "_children";

	public static final int DEFAULT_WIDTH = 10;

	public static final int DEFAULT_HEIGHT = 20;

	private ActivationModel owner;

	private LifeLineModel ownerLine;

	private boolean movable = true;


	public boolean isMovable() {
		return movable;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public void addActivation(ActivationModel model) {
		addChild(model);
		this.ownerLine.getOwner().getRoot().addActivation(model);
		model.setOwner(this);
		model.setOwnerLine(this.ownerLine);
		firePropertyChange(P_CHILDREN, null, model);
	}

	public void removeActivation(ActivationModel model) {
		removeChild(model);
		this.ownerLine.getOwner().getRoot().removeActivation(model);
	}

	public void setOwner(ActivationModel owner) {
		this.owner = owner;
	}

	public void setOwnerLine(LifeLineModel lineModel) {
		this.ownerLine = lineModel;
	}

	public LifeLineModel getOwnerLine() {
		return ownerLine;
	}

	public ActivationModel getOwner() {
		return owner;
	}

	public InteractionModel getRoot() {
		return ownerLine.getOwner().getRoot();
	}

	/**
	 *
	 */
	public void setConstraint(Rectangle constraint) {
		Rectangle old = getConstraint();
		if (old != null) {
			//댷벍궻맕뙽.뚁귂뢯궢뙰귝귟뤵궸댷벍궳궖궶궋.
			List<SyncMessageModel> targetConnection = getSyncTargetConnection();
			for (Iterator<SyncMessageModel> iter = targetConnection.iterator(); iter.hasNext();) {
				SyncMessageModel element = (SyncMessageModel) iter.next();
				ActivationModel source = (ActivationModel) element.getSource();
				if (constraint.y < source.getConstraint().y) {
					constraint.y = source.getConstraint().y;
				}
				//렔뚅뚁귂뢯궢궻뤾뜃궼궠귞궸뚁귂뢯궢뙰 + 뜼빁귝귟귖뤵궸댷벍궳궖궶궋.
				if (element.isRecursive()) {
					if (constraint.y < source.getConstraint().y + SyncMessageModel.DELTA_Y) {
						constraint.y = source.getConstraint().y + SyncMessageModel.DELTA_Y;
					}
				}
			}
			Rectangle delta = new Rectangle();
			delta.x = constraint.x - old.x;
			delta.y = constraint.y - old.y;
			delta.width = constraint.width - old.width;
			delta.height = constraint.height - old.height;
			List<SyncMessageModel> list = getSyncSourceConnection();
			for (Iterator<SyncMessageModel> iter = list.iterator(); iter.hasNext();) {
				SyncMessageModel element = (SyncMessageModel) iter.next();
				element.updateCallee(delta);
			}
		}
		super.setConstraint(constraint);
		computeCaller();
	}

	/**
	 * 댷벍귩럔뒋맜뗦듩궸뵿뎕궠궧귡.
	 * @param delta
	 */
	public void adjustLocation(Rectangle delta) {
		Rectangle rectangle = getConstraint();
		if (rectangle != null) {
			rectangle.translate(delta.getLocation());
			rectangle.translate(delta.width / 2, 0);
			setConstraint(rectangle);
		}
		for (Iterator<AbstractUMLModel> iter = getChildren().iterator(); iter.hasNext();) {
			ActivationModel element = (ActivationModel) iter.next();
			element.adjustLocation(delta);
		}
	}

	/**
	 * 릂궴럔궔귞뒋맜뗦듩궻긖귽긛귩뮧맢궥귡.
	 *
	 */
	public void computeSize() {
		Rectangle rectangle = getConstraint();
		if (rectangle != null) {
			rectangle.height = getChildrenSize() - rectangle.y;
			super.setConstraint(rectangle);
			computeOwner();
		}
	}

	/**
	 * 럔뒋맜뗦듩궻긖귽긛귩롦벦궥귡.
	 * @return
	 */
	private int getChildrenSize() {
		int max = getConstraint().y + DEFAULT_HEIGHT;
		for (Iterator<AbstractUMLModel> iter = getChildren().iterator(); iter.hasNext();) {
			ActivationModel element = (ActivationModel) iter.next();
			int cmax = element.getConstraint().y
					+ element.getConstraint().height;
			if (max < cmax) {
				max = cmax;
			}
		}
		int callee = getCalleeSize();
		if (max > callee) {
			return max + 10;
		} else {
			return callee;
		}
	}

	/**
	 * 릂뒋맜뗦듩/깋귽긲깋귽깛궻긖귽긛귩뮧맢궥귡.
	 *
	 */
	public void computeOwner() {
		if (owner != null) {
			owner.computeSize();
		} else {
			if (ownerLine != null) {
				ownerLine.computeSize();
			}
		}
	}

//	/**
//	 * 럔뒋맜뗦듩궻긖귽긛귩똶럁궥귡.
//	 * @return
//	 */
//	public int computeChild() {
//		Rectangle rectangle = getConstraint();
//
//		List list = getSyncSourceConnection();
//		int max = rectangle.y + ActivationModel.DEFAULT_HEIGHT;
//		for (Iterator iter = list.iterator(); iter.hasNext();) {
//			SyncMessageModel element = (SyncMessageModel) iter.next();
//			ActivationModel target = (ActivationModel) element.getTarget();
//			ActivationModel model = (ActivationModel) target;
//			int child = model.computeChild();
//			if (child > max) {
//				max = child + 10;
//			}
//		}
//		if (rectangle != null) {
//			rectangle.height = max - rectangle.y;
//			super.setConstraint(rectangle);
//			computeOwner();
//		}
//		return max;
//	}

	/**
	 * 뚁귂뢯궢뙰궻긖귽긛귩뮧맢궥귡.
	 *
	 */
	public void computeCaller() {
		//럔떉궻띍묈긖귽긛귩롦벦.
		int size = getCalleeSize();
		Rectangle rectangle = getConstraint().getCopy();
		rectangle.height = size - rectangle.y;
		super.setConstraint(rectangle);
		//뚁귂뢯궢뙰궸믅뭢.
		List<SyncMessageModel> list = getSyncTargetConnection();
		for (Iterator<SyncMessageModel> iter = list.iterator(); iter.hasNext();) {
			SyncMessageModel element = (SyncMessageModel) iter.next();
			element.updateCaller(size);
		}
		if (!movable) {
			List<AbstractUMLConnectionModel> connections = getOwnerLine().getOwner().getModelTargetConnections();
			if (!connections.isEmpty()) {
				SyncMessageModel model = (SyncMessageModel) connections.get(0);
				model.updateCaller(size);
			}
		}
	}

	/**
	 * 뚁귂뢯궢뙰궻긖귽긛귩뮧맢궥귡.
	 * @param size
	 */
	public void computeCaller(int size) {
		Rectangle rectangle = getConstraint();
		if (rectangle != null) {
			int max = size;
			int calleeSize = getCalleeSize();
			if (size < calleeSize) {
				max = calleeSize;
			}
			rectangle.height = max - rectangle.y;
			super.setConstraint(rectangle);
			//뚁귂뢯궢뙰궸믅뭢.
			List<SyncMessageModel> list = getSyncTargetConnection();
			for (Iterator<SyncMessageModel> iter = list.iterator(); iter.hasNext();) {
				SyncMessageModel element = (SyncMessageModel) iter.next();
				element.updateCaller(max);
			}
			if (!movable) {
				List<AbstractUMLConnectionModel> connections = getOwnerLine().getOwner().getModelTargetConnections();
				if (!connections.isEmpty()) {
					SyncMessageModel model = (SyncMessageModel) connections.get(0);
					model.updateCaller(size);
				}
			}
		}
	}

	/**
	 * 긽?긞긤뼹밙뱰.뚁귂뢯궢듫똚궔귞긖귽긛귩뮧맢.
	 * @param delta
	 */
	public void updateLocation(Rectangle delta) {
		Rectangle rectangle = getConstraint();
		if (rectangle != null) {
			rectangle.translate(0, delta.getLocation().y);
			super.setConstraint(rectangle);
		}
		List<SyncMessageModel> list = getSyncSourceConnection();
		for (Iterator<SyncMessageModel> iter = list.iterator(); iter.hasNext();) {
			SyncMessageModel element = iter.next();
			element.updateCallee(delta);
		}
	}

	/**
	 * 벏딖긽긞긜?긙궻귒뺅땛.
	 * @return
	 */
	public List<SyncMessageModel> getSyncSourceConnection() {
		List<AbstractUMLConnectionModel> modelSourceConnections = getModelSourceConnections();
		List<SyncMessageModel> rv = new ArrayList<SyncMessageModel>();
		for (Iterator<AbstractUMLConnectionModel> iter = modelSourceConnections.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			if (element instanceof SyncMessageModel) {
				rv.add((SyncMessageModel) element);
			}
		}
		return rv;
	}

	/**
	 * 벏딖긽긞긜?긙궻귒뺅땛.
	 * @return
	 */
	public List<SyncMessageModel> getSyncTargetConnection() {
		List<AbstractUMLConnectionModel> modelTargetConnections = getModelTargetConnections();
		List<SyncMessageModel> rv = new ArrayList<SyncMessageModel>();
		for (Iterator<AbstractUMLConnectionModel> iter = modelTargetConnections.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			if (element instanceof SyncMessageModel) {
				rv.add((SyncMessageModel) element);
			}
		}
		return rv;
	}

	public int getNestLevel() {
		if (owner == null) {
			return 1;
		} else {
			return owner.getNestLevel() + 1;
		}
	}

	public int getCalleeSize() {
		int max = getConstraint().y + ActivationModel.DEFAULT_HEIGHT;
		List<SyncMessageModel> connection = getSyncSourceConnection();
		for (Iterator<SyncMessageModel> iter = connection.iterator(); iter.hasNext();) {
			SyncMessageModel element = iter.next();
			int size = element.getCalleeSize();
			if (max < size) {
				max = size;
			}
		}
		return max + 10;
	}

}
