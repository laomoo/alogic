package com.alogic.vfs.xscript;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.alogic.vfs.client.Directory;
import com.alogic.vfs.client.Tool;
import com.alogic.vfs.client.Tool.FileInfo;
import com.alogic.vfs.client.Tool.Result;
import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.doc.XsObject;
import com.anysoft.util.JsonTools;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 报告实现
 * @author yyduan
 * @version 1.6.7.11 [20170203 duanyy] <br>
 * - 支持按结果过滤输出 <br>
 * 
 * @version 1.6.9.1 [20170516 duanyy] <br>
 * - 修复部分插件由于使用新的文档模型产生的兼容性问题 <br>
 * 
 * @version 1.6.9.1 [20170516 duanyy] <br>
 * - 修复部分插件由于使用新的文档模型产生的兼容性问题 <br>
 */
public class Report extends VFS implements Tool.Watcher{
	/**
	 * 对象id
	 */
	protected String cid = "$vfs-report";
	protected static final String pattern = "%3.1f%%\t|%4s\t|%32s|%32s|%16s|%16s|%s";
	protected Map<String,Integer> totalStat = new HashMap<String,Integer>();
	protected Map<String,Integer> currentStat = new HashMap<String,Integer>();
	
	/**
	 * 是否输出详细信息
	 */
	protected boolean detail = true;
	
	/**
	 * 结果输出的集合
	 */
	protected Set<String> logResult = new HashSet<String>();
	
	public Report(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	public void configure(Properties p){
		super.configure(p);
		
		cid = PropertiesConstants.getString(p,"cid",cid,true);
		detail = PropertiesConstants.getBoolean(p, "detail", detail);
		
		String result = PropertiesConstants.getString(p,"results","Differ,More,Less,New,Overwrite,Del,Failed",true);
		String[] results = result.split(",");
		for (String s:results){
			logResult.add(s);
		}
	}
	
	@Override
	protected void onExecute(XsObject root,XsObject current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		if (StringUtils.isNotEmpty(cid)){
			try{
				begin(current,ctx);
				ctx.setObject(cid, this);
				super.onExecute(root, current, ctx, watcher);
			}finally{
				end(current,ctx);
				ctx.removeObject(cid);
			}
		}else{
			super.onExecute(root, current, ctx, watcher);
		}
	}

	protected void begin(XsObject current, LogicletContext ctx){
		
	}

	@Override
	public void begin(Directory src, Directory dest) {
		currentStat.clear();
		log(String.format("SOURCE = %s",src));
		log(String.format("DESTINATION = %s",dest));
		
		if (detail){
			log(String.format("%6s\t|%4s\t|%-32s|%-32s|%-16s|%-16s|%s","Prog","Result","MD5(SRC)","MD5(DEST)","LEN(SRC)","LEN(DEST","FileName"));
		}
	}

	@Override
	public void progress(Directory src, Directory dest, FileInfo fileInfo,
			Result result, float progress) {
		if (detail && logResult.contains(result.name())){
			String srcMd5 = fileInfo.srcAttrs() == null ? "":JsonTools.getString(fileInfo.srcAttrs(), "md5", "");
			String srcLen = fileInfo.srcAttrs() == null ? "":JsonTools.getString(fileInfo.srcAttrs(), "length", "");
			String destMd5 = fileInfo.destAttrs() == null ? "":JsonTools.getString(fileInfo.destAttrs(), "md5", "");
			String destLen = fileInfo.destAttrs() == null ? "":JsonTools.getString(fileInfo.destAttrs(), "length", "");
			log(String.format(pattern, progress*100,result.sign(),srcMd5,destMd5,srcLen,destLen,fileInfo.uPath()));
		}
		
		Integer found = currentStat.get(result.sign());
		if (found == null){
			currentStat.put(result.sign(), 1);
		}else{
			currentStat.put(result.sign(),found + 1);
		}
		
		found = totalStat.get(result.sign());
		if (found == null){
			totalStat.put(result.sign(), 1);
		}else{
			totalStat.put(result.sign(),found + 1);
		}	
	}

	@Override
	public void progress(Directory src, Directory dest, String message,
			String level, float progress) {
		log(message,level);
	}

	@Override
	public void end(Directory src, Directory dest) {
		log("Current Result Statistics:");
		Iterator<Entry<String,Integer>> iter = currentStat.entrySet().iterator();
		while (iter.hasNext()){
			Entry<String,Integer> entry = iter.next();
			log(String.format("\t%s\t%d",entry.getKey(),entry.getValue()));
		}
	}

	protected void end(XsObject current, LogicletContext ctx) {
		log("Total Result Statistics:");
		Iterator<Entry<String,Integer>> iter = totalStat.entrySet().iterator();
		while (iter.hasNext()){
			Entry<String,Integer> entry = iter.next();
			log(String.format("\t%s\t%d",entry.getKey(),entry.getValue()));
		}		
	}
}
