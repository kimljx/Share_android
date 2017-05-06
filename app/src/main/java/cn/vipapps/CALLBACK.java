package cn.vipapps;

public interface CALLBACK<T> {
	/**
	 * run 回调方法
	 * 
	 * @param isError
	 * @param result
	 *            回调类型
	 * */
	void run(boolean isError, T result);
}
