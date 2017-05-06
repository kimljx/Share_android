package cn.vipapps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import cn.vipapps.android.ACTIVITY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MESSAGE {
	public static Map<Context, List<BroadcastReceiver>> allUnregisters = new HashMap<Context, List<BroadcastReceiver>>();
	public static Map<Context, List<IntentFilter>> allFilters = new HashMap<Context, List<IntentFilter>>();
	public static Map<Context, Map<String, List<CALLBACK<Bundle>>>> _globalCallbacks = new HashMap<Context, Map<String, List<CALLBACK<Bundle>>>>();
	public static Map<Context, Map<String, Map<String, List<CALLBACK<Bundle>>>>> _receiverCallbacks = new HashMap<Context, Map<String, Map<String, List<CALLBACK<Bundle>>>>>();

	/**
	 * sendMessage 发送消息
	 * 
	 * @param message
	 *            :发送字段
	 */
	public static void send(String message, Bundle params) {
		send(message, null, params);
	}

	public static void send(String message, Object receiver, Bundle params) {
		//
		Intent intent = new Intent();
		if (receiver != null) {
			intent.addCategory(receiver.toString());
		}
		intent.setAction(message);
		if (params != null) {
			intent.putExtras(params);
		}
		ACTIVITY.context.sendBroadcast(intent);
	}

	/**
	 * sendMessage 接收消息
	 * 
	 * @param message
	 *            :接收字段
	 * @param callback
	 *            :回调方法
	 */
	public static void receive(String message, CALLBACK<Bundle> callback) {
		receive(message, null, callback);
	}

	public static void receive(String message, Object receiver, CALLBACK<Bundle> callback) {
		receive(ACTIVITY.context, message, receiver, callback);
	}

	public static void receive(Context context, String message, Object receiver, CALLBACK<Bundle> callback) {
		boolean isExist = false;
		if (allFilters.containsKey(context)) {
			List<IntentFilter> filters = allFilters.get(context);
			for (IntentFilter filter : filters) {
				if (filter.countActions() == 1 && filter.getAction(0).equalsIgnoreCase(message)) {
					if (receiver == null) {
						if (filter.countCategories() == 0) {
							isExist = true;
						}
					} else {
						if (filter.getCategory(0).equalsIgnoreCase(receiver.toString())) {
							isExist = true;
						}
					}
				}
			}
		}
		if (!isExist) {
			IntentFilter filter = new IntentFilter();
			if (receiver != null) {
				filter.addCategory(receiver.toString());
			}
			filter.addAction(message);
			List<IntentFilter> filters;
			if (allFilters.containsKey(context)) {
				filters = allFilters.get(context);
			} else {
				filters = new ArrayList<IntentFilter>();
			}
			filters.add(filter);
			allFilters.put(context, filters);
			BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String message = intent.getAction();
					Set<String> categories = intent.getCategories();
					List<CALLBACK<Bundle>> callbacks;
					if (categories != null && categories.size() == 1) {
						String category = categories.toArray(new String[categories.size()])[0];
						callbacks = _receiverCallbacks.get(context).get(message).get(category);
					} else {
						callbacks = _globalCallbacks.get(context).get(message);
					}
					for (CALLBACK<Bundle> callback : callbacks) {
						callback.run(false, intent.getExtras());
					}
				}

			};
			context.registerReceiver(broadcastReceiver, filter);
			List<BroadcastReceiver> unregisters;
			if (allUnregisters.containsKey(context)) {
				unregisters = allUnregisters.get(context);
			} else {
				unregisters = new ArrayList<BroadcastReceiver>();
			}
			unregisters.add(broadcastReceiver);
			allUnregisters.put(context, unregisters);
		}
		List<CALLBACK<Bundle>> callbacks;
		if (receiver != null) {
			String category = receiver.toString();
			Map<String, Map<String, List<CALLBACK<Bundle>>>> receiverCallbacks;
			if (_receiverCallbacks.containsKey(message)) {
				receiverCallbacks = _receiverCallbacks.get(context);
			} else {
				receiverCallbacks = new HashMap<String, Map<String, List<CALLBACK<Bundle>>>>();
			}
			Map<String, List<CALLBACK<Bundle>>> allCallbacks;
			if (receiverCallbacks.containsKey(message)) {
				allCallbacks = receiverCallbacks.get(message);
			} else {
				allCallbacks = new HashMap<String, List<CALLBACK<Bundle>>>();
			}
			if (allCallbacks.containsKey(category)) {
				callbacks = allCallbacks.get(category);
			} else {
				callbacks = new ArrayList<CALLBACK<Bundle>>();
			}
			callbacks.add(callback);
			allCallbacks.put(category, callbacks);
			receiverCallbacks.put(message, allCallbacks);
			_receiverCallbacks.put(context, receiverCallbacks);
		} else {
			Map<String, List<CALLBACK<Bundle>>> globalCallbacks = new HashMap<String, List<CALLBACK<Bundle>>>();
			if (_globalCallbacks.containsKey(context)) {
				globalCallbacks = _globalCallbacks.get(context);
			} else {
				globalCallbacks = new HashMap<String, List<CALLBACK<Bundle>>>();
			}
			if (globalCallbacks.containsKey(message)) {
				callbacks = globalCallbacks.get(message);
			} else {
				callbacks = new ArrayList<CALLBACK<Bundle>>();
			}
			callbacks.add(callback);
			globalCallbacks.put(message, callbacks);
			_globalCallbacks.put(context, globalCallbacks);
		}
	}

	public static void clear(Context context) {
		if (allUnregisters.containsKey(context)) {
			List<BroadcastReceiver> unregisters = allUnregisters.get(context);
			for (BroadcastReceiver unregister : unregisters) {
				context.unregisterReceiver(unregister);
			}
			allUnregisters.remove(context);
		}
		if (allFilters.containsKey(context)) {
			allFilters.remove(context);
		}
	}
}
