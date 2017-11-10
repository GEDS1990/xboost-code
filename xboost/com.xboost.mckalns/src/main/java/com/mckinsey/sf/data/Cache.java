package com.mckinsey.sf.data;

import java.util.HashMap;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 28, 2017
 * 
 * @version
 */
public class Cache {

	public HashMap<String, HashMap<String, IContext>> cache = new HashMap<String, HashMap<String, IContext>>();

	public void put(Route route, Job job, IContext ctx) {
		if (!cache.containsKey(route.getId())) {
			HashMap<String, IContext> insertCtx = new HashMap<String, IContext>();
			cache.put(route.getId(), insertCtx);
		}

		cache.get(route.getId()).put(job.getId(), ctx);
	}

	public void del(Route route) {
		cache.remove(route.getId());
	}

	public IContext get(Route route, Job job) {
		if (cache.containsKey(route.getId())) {
			if (cache.get(route.getId()).containsKey(job.getId())) {
				return cache.get(route.getId()).get(job.getId());
			}
		}
		return null;
	}

}
