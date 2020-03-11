package egovframework.dev.imp.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import egovframework.dev.imp.core.common.CoreLog;

public class PrefrencePropertyUtil {

	@SuppressWarnings("rawtypes")
	public static Object loadPreferences(AbstractUIPlugin plugin, Object vo) {
		IPreferenceStore store = plugin.getPreferenceStore();
		Class c = vo.getClass();
		Field[] fields = c.getDeclaredFields();

		for (int idx = 0; idx < fields.length; idx++) {
			try {
				if (Modifier.isPrivate(fields[idx].getModifiers())
						|| Modifier.isPublic(fields[idx].getModifiers())) {
					Class cls = fields[idx].getType();
					Method method = getMethod(c, new String[] { "set" },
							fields[idx], new Class[] { cls });
					if (method != null && method.getName() != "setId") {
						String fname = cls.getName();
						String path = getId(vo) + fields[idx].getName();
						if ("java.lang.String".equals(fname)) {
							Object o = store.getString(path);
							method.invoke(vo, new Object[] { o == null ? "" : o });
						} else if ("boolean".equals(fname)) {
							method.invoke(vo, new Object[] { Boolean.valueOf(store.getBoolean(path)) });
						} else if ("double".equals(fname)) {
							method.invoke(
									vo,
									new Object[] { new Double(store
											.getDouble(path)) });
						} else if ("float".equals(fname)) {
							method.invoke(
									vo,
									new Object[] { new Float(store
											.getFloat(path)) });
						} else if ("int".equals(fname)) {
							method.invoke(vo, new Object[] { Integer
									.valueOf(store.getInt(path)) });
						} else if ("long".equals(fname)) {
							method.invoke(vo, new Object[] { Long.valueOf(store
									.getLong(path)) });
						}
					}
				}

			} catch (Exception e) {
				CoreLog.logError(e);
			}
		}
		return vo;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static void savePreferences(AbstractUIPlugin plugin, Object vo) {
		IPreferenceStore store = plugin.getPreferenceStore();

		Class c = vo.getClass();
		Field[] fields = c.getDeclaredFields();

		for (int idx = 0; idx < fields.length; idx++) {
			try {
				if (Modifier.isPrivate(fields[idx].getModifiers())
						|| Modifier.isPublic(fields[idx].getModifiers())) {
					Method method = getMethod(c, new String[] { "get", "is" },
							fields[idx], new Class[0]);
					if (method != null) {
						String fname = fields[idx].getType().getName();
						String path = getId(vo) + fields[idx].getName();
						if ("java.lang.String".equals(fname)) {
							if ((String) method.invoke(vo, new Object[0]) != null)
								store.setValue(path, (String) method.invoke(vo,
										new Object[0]));
						} else if ("boolean".equals(fname)) {
							if ((Boolean) method.invoke(vo, new Object[0]) != null)
								store.setValue(path, ((Boolean) method.invoke(
										vo, new Object[0])).booleanValue());
							store.getBoolean(path);
						} else if ("double".equals(fname)) {
							if ((Double) method.invoke(vo, new Object[0]) != null)
								store.setValue(path, ((Double) method.invoke(
										vo, new Object[0])).doubleValue());
						} else if ("float".equals(fname)) {
							if ((Float) method.invoke(vo, new Object[0]) != null)
								store.setValue(path, ((Float) method.invoke(vo,
										new Object[0])).floatValue());
						} else if ("int".equals(fname)) {
							if ((Integer) method.invoke(vo, new Object[0]) != null)
								store.setValue(path, ((Integer) method.invoke(
										vo, new Object[0])).intValue());
						} else if ("long".equals(fname)) {
							if ((Long) method.invoke(vo, new Object[0]) != null)
								store.setValue(path, ((Long) method.invoke(vo,
										new Object[0])).longValue());
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		plugin.savePluginPreferences();

	}

	private static String getId(Object vo) {
		String id = "*.";
		try {
			id = (String) vo.getClass().getMethod("getId").invoke(vo, new Object[0])+ ".";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Method getMethod(Class clazz, String[] prefixes, Field field, Class[] classes) {
		Method method = null;
		String fname = field.getName().substring(0, 1).toUpperCase()+ field.getName().substring(1);
		for (int idx = 0; idx < prefixes.length; idx++) {
			try {
				method = clazz.getMethod(prefixes[idx] + fname, classes);

//				method = clazz.getMethod("setNexusId", new Class() { java.lang.String });
				break;
			} catch (NoSuchMethodException e) {
				CoreLog.logError(e);
			}
		}

		return method;
	}
}
