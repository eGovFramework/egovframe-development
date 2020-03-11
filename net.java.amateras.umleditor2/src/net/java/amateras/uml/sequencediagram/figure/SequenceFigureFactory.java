/**
 * 
 */
package net.java.amateras.uml.sequencediagram.figure;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.sequencediagram.figure.ext.ActivationFigureEx;
import net.java.amateras.uml.sequencediagram.figure.ext.InstanceFigureEx;

/**
 * @author shidat
 *
 */
public class SequenceFigureFactory {

	/**
	 * 귺긏긡귻긹귽긘깈깛궻?귩롦벦궥귡.
	 * @return
	 */
	public static ActivationFigure getActivationFigure() { 
		if (UMLPlugin.getDefault().getPreferenceStore().getBoolean(UMLPlugin.PREF_NEWSTYLE)) {
			return new ActivationFigureEx();
		}
		return new ActivationFigure();
	}
	
	/**
	 * 귽깛긚?깛긚궻?귩롦벦궥귡.
	 * @return
	 */
	public static InstanceFigure getInstanceFigure() {
		if (UMLPlugin.getDefault().getPreferenceStore().getBoolean(UMLPlugin.PREF_NEWSTYLE)) {
			return new InstanceFigureEx();
		}
		return new InstanceFigure();
	}
}
