package net.java.amateras.uml.sequencediagram;

import net.java.amateras.uml.DiagramEditor;
import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.model.NoteModel;
import net.java.amateras.uml.model.RootModel;
import net.java.amateras.uml.sequencediagram.action.AddReturnMessageAction;
import net.java.amateras.uml.sequencediagram.action.ImportClassModelAction;
import net.java.amateras.uml.sequencediagram.editpart.SequenceEditPartFactory;
import net.java.amateras.uml.sequencediagram.figure.InstanceFigure;
import net.java.amateras.uml.sequencediagram.model.ActorModel;
import net.java.amateras.uml.sequencediagram.model.InstanceModel;
import net.java.amateras.uml.sequencediagram.model.InteractionModel;
import net.java.amateras.uml.sequencediagram.model.SyncMessageModel;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * GEF귩럊뾭궢궫UML걁긘?긑깛긚?귽귺긐깋?걂긄긢귻?갃
 * 
 * @author Takahiro Shida.
 */
public class SequenceDiagramEditor extends DiagramEditor {
	
	private AddReturnMessageAction returnMessageAction;
	private ImportClassModelAction importClassModelAction;
	
	protected PaletteRoot getPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		UMLPlugin plugin = UMLPlugin.getDefault();
		
		// 긾긢깑띿맟댥둖궻긟?깑귩둰?궥귡긐깑?긵
		PaletteGroup tools = new PaletteGroup(plugin.getResourceString("palette.tool"));
		// '멗묖' 긟?깑궻띿맟궴믁돿
		ToolEntry tool = new SelectionToolEntry();
		tools.add(tool);
		root.setDefaultEntry(tool);
		// '댪귒쁤' 긟?깑궻띿맟궴믁돿
		tool = new MarqueeToolEntry();
		tools.add(tool);
		
		PaletteDrawer common = new PaletteDrawer(plugin.getResourceString("palette.common"));
		common.add(createEntityEntry(plugin.getResourceString("palette.common.note"),
				NoteModel.class,"icons/note.gif"));
//		common.add(createConnectionEntry(plugin.getResourceString("palette.common.anchor"),
//				AnchorModel.class,"icons/dependency.gif"));
		
		// 긾긢깑궻띿맟귩뛱궎긟?깑귩둰?궥귡긐깑?긵
		PaletteDrawer entities = new PaletteDrawer(plugin.getResourceString("palette.entity"));
		entities.add(createEntityEntry(plugin.getResourceString("palette.entity.instance"),
				InstanceModel.class,"icons/class.gif"));
		entities.add(createEntityEntry(plugin.getResourceString("palette.entity.actor"),
				ActorModel.class,"icons/actor16.gif"));

		PaletteDrawer relations = new PaletteDrawer(plugin.getResourceString("palette.message"));
		relations.add(createConnectionEntry(plugin.getResourceString("palette.message"),
				SyncMessageModel.class,"icons/dependency.gif"));
		
		// 띿맟궢궫2궰궻긐깑?긵귩깑?긣궸믁돿
		root.add(tools);
		root.add(common);
		root.add(entities);
		root.add(relations);
		return root;
	}


	protected RootModel createInitializeModel() {
		InteractionModel model = new InteractionModel();
		model.setBackgroundColor(InstanceFigure.INSTANCE_COLOR.getRGB());
		model.setForegroundColor(ColorConstants.black.getRGB());
		model.setShowIcon(true);
		return model;
	}

	protected String getDiagramType() {
		return "sequence";
	}

	protected void createDiagramAction(GraphicalViewer viewer) {
		returnMessageAction = new AddReturnMessageAction(getCommandStack(), viewer);
		importClassModelAction = new ImportClassModelAction(getCommandStack(), viewer);
	}

	protected void fillDiagramPopupMenu(MenuManager manager) {
		manager.add(new Separator("generate"));
		manager.add(returnMessageAction);
		manager.add(importClassModelAction);
	}

	protected void updateDiagramAction(ISelection selection) {
		returnMessageAction.update((IStructuredSelection)selection);
		importClassModelAction.update((IStructuredSelection)selection);
	}

	protected EditPartFactory createEditPartFactory() {
		return new SequenceEditPartFactory();
	}
	

}
