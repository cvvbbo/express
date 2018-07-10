package com.emiaoqian.express.utils;





import com.emiaoqian.express.application.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xiong on 2017/8/21.
 */

public class EncodeBuilder {

    public static String Mylord(String unsign){
        //其实不叫封装的封装。。
        EncodeUtils t3=new EncodeUtils();
        HashMap<String, String> tomap = t3.tomap(unsign);
        String sortmap = t3.sortmap(tomap);
        String encode = EncodeUtils.encode(sortmap);
        String token = t3.addString(encode, "MIAOQIAN_API_TOKEN");
        String upwrite = t3.upwrite(token);
        String encode1 = EncodeUtils.encode(upwrite);
        String upwrite1 = t3.upwrite(encode1);
        String s=(unsign.substring(0,unsign.length()-1));
        String newsign=s+",\"sign\":\""+upwrite1+"\"}";
        return newsign;

    }

    public static String Yougrace(String unsign){
        EncodeUtils t3=new EncodeUtils();
        HashMap<String, String> tomap = t3.tomap(unsign);
        String sortmap = t3.sortmap(tomap);
        String encode = EncodeUtils.encode(sortmap);
        String token = t3.addString(encode, "MIAOQIAN_API_TOKEN");
        String upwrite = t3.upwrite(token);
        String encode1 = EncodeUtils.encode(upwrite);
        String upwrite1 = t3.upwrite(encode1);
        return  upwrite1;
    }

    //这个是网页的。。。
    public static  String newString(String unsign){
        unsign=(unsign.substring(0,(unsign.length()-1))+",\"mid\":\"abcdefg\",\"appv\":\"android\"}");
        LogUtil.e("拼接字符串的时候--"+unsign);
        EncodeUtils t3=new EncodeUtils();
        HashMap<String, String> tomap = t3.tomap(unsign);
        String sortmap = t3.sortmap(tomap);
        LogUtil.e("---字典排序之后--"+sortmap);
        //md5加密
        String encode = EncodeUtils.encode(sortmap);
        LogUtil.e("--没加token之前"+encode);
        String upwrite = t3.upwrite(encode);
        String s = t3.addString(upwrite, "MIAOQIAN_API_TOKEN");
        String encode1 = EncodeUtils.encode(s);
        String upwrite1 = t3.upwrite(encode1);
        return upwrite1;
    }

    //这个是获取登录信息的
    public static String javaToJSON(String userid,String Umtoken,String time) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userid);
            jsonObj.put("deviceNo", Umtoken);
            jsonObj.put("timestamp",time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }


    //测试新页面，获取log的，我也不知道什么是log。。1.17
    public static String javaToJSON2(String time) {
        JSONObject jsonObj = new JSONObject();
        try {

            //这里是初次登录和非初次登录的判断
            String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", " ");
            if (user_id.matches("[0-9]+")){
                jsonObj.put("userId", user_id);
                jsonObj.put("timestamp",time);

            }else {

                //下面这个是初次登录，没有获取userid
                //jsonObj.put("userId", userid);
                jsonObj.put("timestamp", time);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    public static String javaToJSON3(String userid,String time,String pageNum,String fileid,String companyName,String type) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("userId", userid);
            jsonObj.put("timestamp",time);
            jsonObj.put("pageNum",pageNum);
            jsonObj.put("id",fileid);
            jsonObj.put("companyName",companyName);
            jsonObj.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }


    /***
     *
     * 巨神坑，这里是，有为空的值（key-value），就是value为空，就不加进签名验证里面，但是这个为空的字段还是要加到最后网络请求的表单里面
     * 没办法，毕竟是写后台的 2018.4.27！！！！！！！！！！！！！！！！！！！！！！！
     *
     *
     */
    public static String javaToJSONsign(String userid,String time,String logistic_code,
                                        String receiver_mobile,String duration,String service_time,String receiver,
                                        String receiver_name,String longitude,String latitude,String current_addr) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("user_id", userid);
            jsonObj.put("timestamp",time);
            jsonObj.put("logistic_code",logistic_code);
            jsonObj.put("receiver_mobile",receiver_mobile);
            if (!duration.equals("")){
                jsonObj.put("duration",duration);
            }
            jsonObj.put("service_time",service_time);

            jsonObj.put("receiver", receiver);

            //jsonObj.put("receiver_name","1");
            if (!longitude.equals("")) {
                jsonObj.put("longitude", longitude);
            }

            if (!latitude.equals("")) {
                jsonObj.put("latitude", latitude);
            }
            if (!current_addr.equals("")) {
                jsonObj.put("current_addr", current_addr);
            }

            //因为这个能为空
            if (!receiver_name.equals("")) {
                jsonObj.put("receiver_name", receiver_name);
            }
//            jsonObj.put("longitude",longitude);
//            jsonObj.put("latitude",latitude);
//            jsonObj.put("current_addr",current_addr);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }


    //搜索的拼接字符串
    public static String javaToJSON4(String login_type,String time,String password,String mobile) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("login_type", login_type);
            jsonObj.put("timestamp",time);
            jsonObj.put("password",password);
            jsonObj.put("mobile",mobile);
            jsonObj.put("appv","v1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    public static String javaToJSONNewPager1(Map<String,String> datas) {
        JSONObject jsonObj = new JSONObject();

        Iterator iter = datas.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            try {
                jsonObj.put(key,val);
            }catch (JSONException e){

            }

        }

        return jsonObj.toString();
    }


    //搜索的拼接字符串
    public static String javaTouserinfor(String user_id,String time) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("user_id", user_id);
            jsonObj.put("timestamp",time);
            jsonObj.put("appv","v1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    public static String javaToAddTip(String user_id,String time,String tipname) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name", tipname);
            jsonObj.put("user_id", user_id);
            jsonObj.put("timestamp",time);
            jsonObj.put("appv","v1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }


    //搜索的拼接字符串
    public static String javaToexpressinfor(String logistic_code,String time) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("logistic_code", logistic_code);
            jsonObj.put("timestamp",time);
            jsonObj.put("appv","v1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }


    public static String javaToexpressimageinfor(String type,String time) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("type", type);
            jsonObj.put("timestamp",time);
            jsonObj.put("appv","android");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }



    //搜索的拼接字符串
    public static String javaToJSON66(String time) {
        JSONObject jsonObj = new JSONObject();
        try {

            jsonObj.put("timestamp",time);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }


    //搜索的拼接字符串
    public static String javaToSendCode(String time,String mobile) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("timestamp",time);
            jsonObj.put("mobile",mobile);
            jsonObj.put("appv","v1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }



    //新页面1.17
    public static  String newString2(String unsign){
        //unsign=(unsign.substring(0,(unsign.length()-1))+",\"mid\":\"abcdefg\",\"appv\":\"android\"}");
        LogUtil.e("拼接字符串的时候--"+unsign);
        EncodeUtils t3=new EncodeUtils();
        HashMap<String, String> tomap = t3.tomap(unsign);
        String sortmap = t3.sortmap(tomap);
        LogUtil.e("---字典排序之后--"+sortmap);
        //md5加密
        String encode = EncodeUtils.encode(sortmap);
        LogUtil.e("--没加token之前"+encode);
        String upwrite = t3.upwrite(encode);
        String s = t3.addString(upwrite, "MIAOQIAN_API_TOKEN");
        //String s = t3.addString(upwrite, "MIAOQIAN_API_TEST");
        String encode1 = EncodeUtils.encode(s);
        String upwrite1 = t3.upwrite(encode1);
        LogUtil.e("--全部加密之后--"+upwrite1);
        return upwrite1;
    }

    //上传文件的加密，去掉的那行是下面mid，和appv:android 这两个字段 4.25！！！
    public static  String newStringUpfile(String unsign){
        unsign=(unsign.substring(0,(unsign.length()-1))+",\"mid\":\"abcdefg\",\"appv\":\"android\"}");
        LogUtil.e("拼接字符串的时候--"+unsign);
        EncodeUtils t3=new EncodeUtils();
        HashMap<String, String> tomap = t3.tomap(unsign);
        String sortmap = t3.sortmap(tomap);
        LogUtil.e("---字典排序之后--"+sortmap);
        //md5加密
        String encode = EncodeUtils.encode(sortmap);
        LogUtil.e("--没加token之前"+encode);
        String upwrite = t3.upwrite(encode);
        String s = t3.addString(upwrite, "MIAOQIAN_API_TOKEN");
        //String s = t3.addString(upwrite, "MIAOQIAN_API_TEST");
        String encode1 = EncodeUtils.encode(s);
        String upwrite1 = t3.upwrite(encode1);
        LogUtil.e("--全部加密之后--"+upwrite1);
        return upwrite1;
    }



}
