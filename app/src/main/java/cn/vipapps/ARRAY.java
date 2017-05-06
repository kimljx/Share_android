package cn.vipapps;

public class ARRAY {
	public static int indexOf(Object[] array, Object item) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(item)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean contains(Object[] array, Object item) {
		return indexOf(array, item) >= 0;
	}
}
