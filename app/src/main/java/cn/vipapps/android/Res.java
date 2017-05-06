package cn.vipapps.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Field;

public class Res {
	private static int resourceID(String name, String type) {
		Context context = ACTIVITY.context;
		Resources resources = ACTIVITY.context.getResources();
		int getResourceID = resources.getIdentifier(name, type, context.getPackageName());
		if (getResourceID == 0) {
			Log.e("[getResourceID]", type + " " + name);
		}
		return getResourceID;
	}

	public static int animID(String name) {
		return resourceID(name, "animator");
	}

	public static int drawableID(String name) {
		return resourceID(name, "drawable");
	}

	public static int layoutID(String name) {
		return resourceID(name, "layout");
	}

	public static int arrayID(String name) {
		return resourceID(name, "array");
	}

	public static int stringID(String name) {
		return resourceID(name, "strings");
	}

	public static int styleableID(String name) {
		if (styleableClass == null) {
			String className = ACTIVITY.context.getPackageName() + ".R$styleable";
			try {
				styleableClass = Class.forName(className);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			Field field = styleableClass.getField(name);
			return (Integer) field.get(null);
		} catch (Exception e) {
			Log.e("[styleableClass]", name);
			e.printStackTrace();
			return 0;
		}
	}

	public static int id(String name) {
		return resourceID(name, "id");
	}

	public static int styleID(String name) {
		return resourceID(name, "style");
	}

	static Class<?> styleableClass;

	public static int[] styleableIDs(String name) {
		if (styleableClass == null) {
			String className = ACTIVITY.context.getPackageName() + ".R$styleable";
			try {
				styleableClass = Class.forName(className);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			Field field = styleableClass.getField(name);
			return (int[]) field.get(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("deprecation")
	public static Drawable drawable(String name) {
		int id = resourceID(name, "drawable");
		Resources resources = ACTIVITY.context.getResources();
		return resources.getDrawable(id);
	}
	@SuppressWarnings("deprecation")
	public static Drawable drawable(int id) {
		Resources resources = ACTIVITY.context.getResources();
		return resources.getDrawable(id);
	}
	public static String string(String name) {
		int id = resourceID(name, "strings");
		Resources resources = ACTIVITY.context.getResources();
		return resources.getString(id);
	}
}
