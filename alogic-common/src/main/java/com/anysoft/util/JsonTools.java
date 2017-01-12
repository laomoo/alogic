package com.anysoft.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jayway.jsonpath.spi.JsonProvider;
import com.jayway.jsonpath.spi.JsonProviderFactory;


/**
 * Json的一些工具
 * @author duanyy
 * @since 1.0.6
 * 
 * @version 1.0.8 [20140410 duanyy] <br>
 * + Add {@link com.anysoft.util.JsonTools#setString(Map, String, String) setString(Map, String, String)} <br>
 * + Add {@link com.anysoft.util.JsonTools#setInt(Map, String, int) setInt(Map,String,int)} <br>
 * + Add {@link com.anysoft.util.JsonTools#setBoolean(Map, String, boolean) setBoolean(Map,String,boolean)} <br>
 * + Add {@link com.anysoft.util.JsonTools#getBoolean(Map, String, boolean) getBoolean(Map,String,boolean)} <br>
 * 
 * @version 1.0.14 [20140615 duanyy] <br>
 * + 增加一下Json工具
 * 
 * @version 1.4.1 [20140901 duanyy] <br>
 * + 增加getDouble和setDouble方法
 * 
 * @version 1.4.2 [20140903 duanyy] <br>
 * + 修正getDouble和getLong的类型转换bug
 * 
 * @version 1.4.4 [20140912 duanyy] <br>
 * + 将Map参数进行参数化 <br>
 * 
 * @version 1.6.4.27 [20160125 duanyy] <br>
 * - 根据sonar建议优化代码 <br>
 * 
 * @version 1.6.4.41 [20160401 duanyy] <br>
 * - 增加float操作方法 <br>
 * 
 * @version 1.6.6.13 [20170106 duanyy] <br>
 * - 增加json2txt和txt2json功能  <br>
 */
public class JsonTools {
	
	protected static JsonProvider provider = null;	
	static {
		provider = JsonProviderFactory.createProvider();
	}	
	
	private JsonTools(){
		
	}
		
	
	/**
	 * 从Json对象中获取指定的属性值
	 * 
	 * @param json Json对象
	 * @param name 属性名
	 * @param defaultValue 缺省值
	 * @return 属性值
	 */
	public static String getString(Map<String,Object> json,String name,String defaultValue){
		Object found = json.get(name);
		if (found == null){
			return defaultValue;
		}
		
		if (found instanceof String){
			return (String)found;
		}
		return found.toString();
	}
	
	/**
	 * 向Json对象中设置指定的属性值
	 * @param json Json对象
	 * @param name 属性名
	 * @param value 属性值(String)
	 * 
	 * @since 1.0.8
	 */
	public static void setString(Map<String,Object> json,String name,String value){
		if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(value)){
			json.put(name, value);
		}
	}
	
	/**
	 * 从Json对象中获取指定的属性值
	 * @param json Json对象
	 * @param name 属性名
	 * @param defaultValue 缺省值
	 * @return 属性值
	 */
	public static int getInt(Map<String,Object> json,String name,int defaultValue){
		Object found = json.get(name);
		if (found == null){
			return defaultValue;
		}
		
		if (found instanceof Integer){
			return (Integer)found;
		}
		
		try {
			return Integer.parseInt(found.toString());
		}catch (NumberFormatException ex){
			return defaultValue;
		}
	}
	
	
	
	/**
	 * 向Json对象中设置指定属性值
	 * @param json
	 * @param name
	 * @param value
	 * 
	 * @since 1.4.1
	 */
	public static void setDouble(Map<String,Object> json,String name,double value){
		if (StringUtils.isNotEmpty(name)){
			json.put(name, value);
		}
	}
	
	/**
	 * 从Json对象中获取属性值
	 * @param json
	 * @param name
	 * @param dftValue
	 * @return double value
	 * 
	 * @since 1.4.1
	 */
	public static double getDouble(Map<String,Object> json,String name,double dftValue){
		Object found = json.get(name);
		if (found == null){
			return dftValue;
		}
		
		if (found instanceof Double){
			return (Double)found;
		}
		
		try {
			return Double.parseDouble(found.toString());
		}catch (NumberFormatException ex){
			return dftValue;
		}
	}
	
	/**
	 * 向Json对象中设置指定值
	 * @param json Json对象
	 * @param name 属性名
	 * @param value float值
	 */
	public static void setFloat(Map<String,Object> json,String name,float value){
		if (StringUtils.isNotEmpty(name)){
			json.put(name, value);
		}
	}
	
	/**
	 * 从Json对象中获取属性值
	 * @param json Json对象
	 * @param name 属性名
	 * @param dftValue 缺省值
	 * @return float value
	 * 
	 * @since 1.6.4.41
	 */
	public static float getFloat(Map<String,Object> json,String name,float dftValue){
		Object found = json.get(name);
		if (found == null){
			return dftValue;
		}
		
		if (found instanceof Float){
			return (Float)found;
		}
		
		try {
			return Float.parseFloat(found.toString());
		}catch (NumberFormatException ex){
			return dftValue;
		}
	}
	
	/**
	 * 向Json对象中设置指定属性值
	 * @param json Json对象
	 * @param name 属性名
	 * @param value 属性值
	 */
	public static void setInt(Map<String,Object> json,String name,int value){
		if (StringUtils.isNotEmpty(name)){
			json.put(name,value);
		}
	}

	/**
	 * 从Json对象中获取指定的属性值
	 * @param json Json对象
	 * @param name 属性名
	 * @param defaultValue 缺省值
	 * @return 属性值
	 * 
	 * @since 1.0.14
	 */
	public static long getLong(Map<String,Object> json,String name,long defaultValue){
		Object found = json.get(name);
		if (found == null){
			return defaultValue;
		}
		
		if (found instanceof Long){
			return (Long)found;
		}
		
		try {
			return Long.parseLong(found.toString());
		}catch (NumberFormatException ex){
			return defaultValue;
		}
	}
	
	/**
	 * 向Json对象中设置指定属性值
	 * @param json Json对象
	 * @param name 属性名
	 * @param value 属性值
	 * 
	 * @since 1.0.14
	 */
	public static void setLong(Map<String,Object> json,String name,long value){
		if (StringUtils.isNotEmpty(name)){
			json.put(name,value);
		}
	}
	
	
	/**
	 * 从Json对象中获取指定的属性值
	 * @param json Json对象
	 * @param name 属性名
	 * @param defaultValue 缺省值
	 * @return 属性值
	 * 
	 * @since 1.0.8
	 */
	public static boolean getBoolean(Map<String,Object> json,String name,boolean defaultValue){
		Object found = json.get(name);
		if (found == null){
			return defaultValue;
		}
		
		if (found instanceof Boolean){
			return (Boolean)found;
		}
		
		return BooleanUtils.toBoolean(found.toString());
	}
	
	/**
	 * 向Json对象中设置指定的属性值
	 * @param json Json对象
	 * @param name 属性名
	 * @param value 属性值
	 */
	public static void setBoolean(Map<String,Object> json,String name,boolean value){
		if (StringUtils.isNotEmpty(name)){
			json.put(name,value);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void xml2Json(Element root, Map<String,Object> json) {
		NodeList children = root.getChildNodes();

		for (int i = 0, length = children.getLength(); i < length; i++) {
			Node n = children.item(i);
			int nodeType = n.getNodeType();
			if (nodeType == Node.ELEMENT_NODE){
				Element e = (Element) n;
				String key = e.getNodeName();
				List<Object> array = null;
				Object found = json.get(key);
				if (found != null){
					if (found instanceof List){
						array = (List<Object>)found;
					}else{
						array = new ArrayList<Object>();
						Object removed = json.remove(key);
						if (removed != null)
						array.add(removed);
						json.put(key, array);
					}
				}
				
				Map<String,Object> map = new HashMap<String,Object>();
				//clone attribute
				{
					NamedNodeMap attrs = e.getAttributes();
					for (int j = 0; j < attrs.getLength(); j++) {
						Node attr = attrs.item(j);
						map.put(attr.getNodeName(), attr.getNodeValue());
					}
				}
				
				xml2Json(e,map);
				
				if (array != null){
					array.add(map);		
				}else{
					json.put(key, map);		
				}				
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void json2Xml(List<Object> json,Element e,String key){
		Document doc = e.getOwnerDocument();
		for (Object item:json){
			if (item instanceof Map){
				Element newElem = doc.createElement(key);
				json2Xml((Map<String,Object>)item,newElem);
				e.appendChild(newElem);
			}else{
				if (item instanceof List){
					json2Xml((List<Object>)item,e,key);
				}else{
					Element newElem = doc.createElement(key);
					newElem.setAttribute(key, item.toString());
					e.appendChild(newElem);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void json2Xml(Map<String,Object> json,Element e){
		Set<String> keys = json.keySet();
		Iterator<String> iter = keys.iterator();
		Document doc = e.getOwnerDocument();
		
		while (iter.hasNext()) {
			String key = iter.next();
			Object data = json.get(key);
			if (data instanceof List) {
				List<Object> list = (List<Object>) data;
				json2Xml(list, e, key);
			} else {
				if (data instanceof Map) {
					Element newElem = doc.createElement(key);
					json2Xml((Map<String, Object>) data, newElem);
					e.appendChild(newElem);
					continue;
				} else {
					e.setAttribute(key, data.toString());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void clone(List<Object> src,List<Object> dest){
		for (Object item:src){
			if (item instanceof Map){
				Map<String,Object> newData = new HashMap<String,Object>(); // NOSONAR
				clone((Map<String,Object>)item,newData);
				dest.add(newData);
			}else{
				if (item instanceof List){
					List<Object> newList = new ArrayList<Object>();  // NOSONAR
					clone((List<Object>)item,newList);
				}else{
					dest.add(item.toString());
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void clone(Map<String,Object> src,Map<String,Object> dest){
		Set<String> keys = src.keySet();
		Iterator<String> iter = keys.iterator();
		
		while (iter.hasNext()) {
			String key = iter.next();
			Object data = src.get(key);
			if (data instanceof List) {
				List<Object> list = (List<Object>) data;
				List<Object> newList = new ArrayList<Object>(); // NOSONAR
				clone(list, newList);
				dest.put(key, newList);
			} else {
				if (data instanceof Map) {
					Map<String, Object> newData = new HashMap<String, Object>(); // NOSONAR
					clone((Map<String, Object>) data, newData);
					dest.put(key, newData);
					continue;
				} else {
					dest.put(key, data.toString());
				}
			}
		}
	}	
	
	/**
	 * 将Map对象转换为Json文本
	 * @param json map对象
	 * @return Json文本
	 */
	public static String map2text(Map<String,Object> json){
		return provider.toJson(json);
	}
	
	/**
	 * 将json文本转换为Map对象
	 * @param text json文本
	 * @return Map对象
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> text2map(String text){
		Object json = provider.parse(text);
		if (json instanceof Map){
			return (Map<String,Object>)json;
		}
		return null;
	}
	
	/**
	 * 将List对象转换为Json文本
	 * @param json List对象
	 * @return Json文本
	 */
	public static String list2text(List<Object> json){
		return provider.toJson(json);
	}
	
	/**
	 * 将Json文本转换为List对象
	 * @param text Json文本
	 * @return List对象
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> text2list(String text){
		Object json = provider.parse(text);
		if (json instanceof List){
			return (List<Object>)json;
		}
		return null;		
	}
}
