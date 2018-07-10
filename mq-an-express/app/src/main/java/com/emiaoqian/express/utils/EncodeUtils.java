package com.emiaoqian.express.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class EncodeUtils {


	/***
	 * ①json数组转化为key，value的形式
	 *
	 * @param str
	 * @return
	 */
	public HashMap<String,String> tomap(String str){
		  HashMap<String,String> m=new HashMap<String, String>();
		     JSONObject a;
			try {
				a = new JSONObject(str);
				Iterator it = a.keys(); 
				while(it.hasNext()){
					String key=(String) it.next();
					String string = a.getString(key);
					m.put(key, string);
					
				}
				//System.out.println(m);
				return m;
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
	}


	/**
	 *
	 * 【排序
	 * @param a
	 * @return
	 */
	public String sortmap(Map<String,String> a){
		String f="";
		
		Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {

                return o1.compareTo(o2);
			}
			
		});
		map.putAll(a);
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            for(int i=0;i<map.size();i++){
            	String key = iter.next();
            	String c=key+map.get(key);
				f+=c;
            }
            return f;
        }
        return null;
	}

	//md5加密
	public static String encode(String text) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] digest = md.digest(text.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				int a = b & 0xff;//
				String hexString = Integer.toHexString(a);
				if (hexString.length() == 1) {
					hexString = "0" + hexString;
				}
				sb.append(hexString);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//大写
	public String upwrite(String s){
		String string = s.toUpperCase();
		return string;
	}

	//添加字符串
	public String addString(String a,String b){
		String c=a+b;
		return c;
		
	}
	


}
