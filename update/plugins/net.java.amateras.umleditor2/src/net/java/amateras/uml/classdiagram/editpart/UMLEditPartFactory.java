package net.java.amateras.uml.classdiagram.editpart;

import net.java.amateras.uml.classdiagram.model.AggregationModel;
import net.java.amateras.uml.classdiagram.model.AssociationModel;
import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.ClassModel;
import net.java.amateras.uml.classdiagram.model.CompositeModel;
import net.java.amateras.uml.classdiagram.model.DependencyModel;
import net.java.amateras.uml.classdiagram.model.GeneralizationModel;
import net.java.amateras.uml.classdiagram.model.InterfaceModel;
import net.java.amateras.uml.classdiagram.model.OperationModel;
import net.java.amateras.uml.classdiagram.model.RealizationModel;
import net.java.amateras.uml.editpart.BaseUMLEditPartFactory;
import net.java.amateras.uml.editpart.RootEditPart;
import net.java.amateras.uml.model.RootModel;

import org.eclipse.gef.EditPart;

/**
 * EditPart궻긲?긏긣깏긏깋긚갃
 * 긾긢깑궻?궸돒궣궲댥돷궻EditPart궻귽깛긚?깛긚귩맯맟궢궲뺅땛궢귏궥갃
 * <ul>
 *   <li>RootModel -&gt; RootEditPart</li>
 *   <li>ClassModel -&gt; ClassEditPart</li>
 *   <li>InterfaceModel -&gt; InterfaceEditPart</li>
 *   <li>DependencyModel -&gt; DependencyEditPart</li>
 *   <li>AggregationModel -&gt; AggregationEditPart</li>
 *   <li>CompositeModel -&gt; CompositeEditPart</li>
 *   <li>GeneralizationModel -&gt; GeneralizationEditPart</li>
 *   <li>RealizationModel -&gt; RealizationEditPart</li>
 * </ul>
 * 
 * @author Naoki Takezoe
 */
public class UMLEditPartFactory extends BaseUMLEditPartFactory {

	protected EditPart createUMLEditPart(EditPart context, Object model) {
		EditPart part = null;
		if(model instanceof RootModel){
			return new RootEditPart();
		} else if(model instanceof ClassModel){
			return new ClassEditPart();
		} else if(model instanceof InterfaceModel){
			return new InterfaceEditPart();
		} else if(model instanceof DependencyModel){
			return new DependencyEditPart();
		} else if(model instanceof AggregationModel){
			return new AggregationEditPart();
		} else if(model instanceof CompositeModel){
			return new CompositeEditPart();
		} else if(model instanceof AssociationModel) {
			return new AssociationEditPart();
		} else if(model instanceof GeneralizationModel){
			return new GeneralizationEditPart();
		} else if(model instanceof RealizationModel){
			return new RealizationEditPart();
		} else if(model instanceof AttributeModel){
			return new AttributeEditPart();
		} else if(model instanceof OperationModel){
			return new OperationEditPart();
		} 
		return part;
	}
	
}
