package net.java.amateras.uml.classdiagram.property;

import java.util.ArrayList;
import java.util.List;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.classdiagram.model.Argument;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * 긽?긞긤궻덙릶귩뺂뢜궥귡궫귕궻?귽귺깓긐갃
 *
 * @author Naoki Takezoe
 */
public class ArgumentsEditDialog extends Dialog {

	private Table table;
	private MenuItem add;
	private MenuItem edit;
	private MenuItem remove;
	private List<Argument> arguments;

	public ArgumentsEditDialog(Shell parent, List<Argument> arguments){
		super(parent);
		setShellStyle(getShellStyle()|SWT.RESIZE);
		this.arguments = arguments;
	}

	protected void constrainShellSize() {
		getShell().setSize(300,200);
	}

	protected Control createDialogArea(Composite parent) {
		getShell().setText(UMLPlugin.getDefault().getResourceString("argumentsDialog.title"));

		Composite container = new Composite(parent,SWT.NULL);
		container.setLayout(new FillLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		// 긟깏?귩룊딖돸
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn column1 = new TableColumn(table, SWT.LEFT);
		column1.setText(UMLPlugin.getDefault().getResourceString("argumentsDialog.label.argumentName"));
		column1.setWidth(120);

		TableColumn column2 = new TableColumn(table, SWT.LEFT);
		column2.setText(UMLPlugin.getDefault().getResourceString("argumentsDialog.label.argumentType"));
		column2.setWidth(120);

		// ?긞긵귺긞긵긽긦깄?귩먠믦
		Menu menu = new Menu(table);
		add = new MenuItem(menu, SWT.PUSH);
		add.setText(UMLPlugin.getDefault().getResourceString("argumentsDialog.menu.add"));
		add.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent evt){
				addArgument();
			}
		});

		edit = new MenuItem(menu, SWT.PUSH);
		edit.setText(UMLPlugin.getDefault().getResourceString("argumentsDialog.menu.edit"));
		edit.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent evt){
				editArgument();
			}
		});

		remove = new MenuItem(menu, SWT.PUSH);
		remove.setText(UMLPlugin.getDefault().getResourceString("argumentsDialog.menu.remove"));
		remove.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent evt){
				removeArgument();
			}
		});

		table.setMenu(menu);
		table.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent evt){
				updateMenus();
			}
		});
		table.addMouseListener(new MouseAdapter(){
			public void mouseDoubleClick(MouseEvent e){
				editArgument();
			}
		});

		// 룊딖뭠귩긜긞긣
		for(int i=0;i<arguments.size();i++){
			Argument arg = (Argument)arguments.get(i);
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(0, arg.getName());
			item.setText(1, arg.getType());
		}

		return container;
	}

	/**
	 * 긽긦깄?궻륉뫴귩뛛륷궢귏궥갃
	 */
	private void updateMenus(){
		if(table.getSelectionCount()==0){
			edit.setEnabled(false);
			remove.setEnabled(false);
		} else {
			edit.setEnabled(true);
			remove.setEnabled(true);
		}
	}

	/**
	 * 뼎뷉궸덙릶귩믁돿궢귏궥갃
	 */
	private void addArgument(){
		int count = table.getItems().length + 1;

		SingleArgumentDialog dialog = new SingleArgumentDialog(
				getShell(),"arg" + count, "int");

		if(dialog.open() == Dialog.OK){
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(new String[]{dialog.getName(),dialog.getType()});
		}
	}

	/**
	 * 멗묖궠귢궫덙릶걁븸릶멗묖궠귢궲궋귡뤾뜃궼띍룊궻귖궻걂귩뺂뢜궢귏궥갃
	 */
	private void editArgument(){
		TableItem[] items = table.getSelection();
		if(items.length > 0){
			items[0].getText(0);
			SingleArgumentDialog dialog = new SingleArgumentDialog(
					getShell(),items[0].getText(0),items[0].getText(1));
			if(dialog.open()==Dialog.OK){
				items[0].setText(0, dialog.getName());
				items[0].setText(1, dialog.getType());
			}
		}
	}

	/**
	 * 멗묖궠귢궫덙릶귩랁룣궢귏궥갃
	 */
	private void removeArgument(){
		TableItem[] items = table.getSelection();
		for(int i=0;i<items.length;i++){
			items[i].dispose();
		}
	}

	protected void okPressed() {
		this.arguments = new ArrayList<Argument>();
		TableItem[] items = table.getItems();
		for(int i=0;i<items.length;i++){
			Argument arg = new Argument();
			arg.setName(items[i].getText(0));
			arg.setType(items[i].getText(1));
			this.arguments.add(arg);
		}
		super.okPressed();
	}

	/**
	 * 뺂뢜뙅됈걁Argument궻깏긚긣걂귩롦벦궢귏궥갃
	 *
	 * @return Argument궻깏긚긣
	 */
	public List<Argument> getArguments(){
		return this.arguments;
	}

	/**
	 * 덙릶궻빾릶뼹궴?귩볺쀍궥귡궫귕궻?귽귺깓긐갃
	 */
	private class SingleArgumentDialog extends Dialog {

		private Text txtName;
		private Text txtType;
		private String name = "";
		private String type = "";

		public SingleArgumentDialog(Shell shell,String name,String type){
			super(shell);
			this.name = name;
			this.type = type;
		}

		protected void constrainShellSize() {
			Shell shell = getShell();
			shell.pack();
			shell.setSize(300,shell.getSize().y);
		}

		protected Control createDialogArea(Composite parent) {

			getShell().setText(UMLPlugin.getDefault().getResourceString("argumentDialog.title"));

			Composite container = new Composite(parent,SWT.NULL);
			GridData gd = new GridData(GridData.FILL_BOTH);
			container.setLayoutData(gd);

			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			container.setLayout(layout);

			Label label = new Label(container,SWT.NULL);
			label.setText(UMLPlugin.getDefault().getResourceString("argumentDialog.label.argumentName"));

			txtName = new Text(container,SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			txtName.setLayoutData(gd);
			txtName.setText(name);

			label = new Label(container,SWT.NULL);
			label.setText(UMLPlugin.getDefault().getResourceString("argumentDialog.label.argumentType"));

			txtType = new Text(container,SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			txtType.setLayoutData(gd);
			txtType.setText(type);

			return container;
		}

		protected void okPressed() {
			name = txtName.getText();
			type = txtType.getText();
			super.okPressed();
		}

		public String getName(){
			return this.name;
		}

		public String getType(){
			return this.type;
		}
	}
}
