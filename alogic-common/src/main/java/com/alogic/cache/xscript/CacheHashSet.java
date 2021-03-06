package com.alogic.cache.xscript;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alogic.cache.CacheObject;
import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 设置Hash值
 * @author yyduan
 * @since 1.6.11.29
 * 
 * @version 1.6.11.43 [20180708 duanyy]  <br>
 * - 支持raw模式 <br>
 */
public class CacheHashSet extends CacheObjectOperation{
	
	/**
	 * 分组
	 */
	protected String $group = CacheObject.DEFAULT_GROUP;
	
	/**
	 * hash key
	 */
	protected String $key;
	
	/**
	 * 值
	 */
	protected String $value = "";
	
	protected boolean overwrite = true;
	
	/**
	 * 是否以原始值写入
	 */
	protected boolean raw = false;
	
	public CacheHashSet(String tag, Logiclet p) {
		super(tag, p);
	}
	
	@Override
	public void configure(Properties p){
		super.configure(p);
		
		$group = PropertiesConstants.getRaw(p, "group", $group);
		$key =  PropertiesConstants.getRaw(p, "key", "");
		$value =  PropertiesConstants.getRaw(p, "value", $value);
		overwrite = PropertiesConstants.getBoolean(p,"overwrite",overwrite);
		raw = PropertiesConstants.getBoolean(p,"raw",raw);
	}

	@Override
	protected void onExecute(CacheObject cache, Map<String, Object> root,
			Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {

		String key = PropertiesConstants.transform(ctx, $key, "");
		
		if (StringUtils.isNotEmpty(key)){
			cache.hSet(
				PropertiesConstants.transform(ctx, $group, CacheObject.DEFAULT_GROUP), 
				key, 
				raw?PropertiesConstants.getRaw(ctx,$value,""):PropertiesConstants.transform(ctx, $value, ""), 
				overwrite);
		}
	}

}
