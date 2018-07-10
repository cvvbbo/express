package com.emiaoqian.express.utils;

import android.os.Handler;
import android.util.Log;


import com.emiaoqian.express.application.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by xiong on 2017/8/15.
 */

public class httphelper {

    //以后凡是自己写的每一个类都要仔细检查！！！。。。

    //校验json数据的
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    private static httphelper helpter=new httphelper();
    private final OkHttpClient okHttpClient;
    //尝试在联网的工具类里面就试着让更新界面执行在主线程
    Handler handler=new Handler();
    private FileOutputStream fos;


    public  static httphelper create(){

        return helpter;

    }


    private httphelper(){
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120,TimeUnit.SECONDS)
                .addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new ReceivedCookiesInterceptor()).build();

    }


    public void doget(String url, final httpcallback h){
        Request request=new Request.Builder().get()
                .url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final ResponseBody body = response.body();
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });

            }
        });

    }


    //验证签名。
    public void dopost(String url, String newsign, final httpcallback h){
        final Request request=new Request.Builder().url(url).post(RequestBody.create(JSON,newsign)).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });


            }
        });

    }



    /***记住签名在另一个类里面，自己挖的坑。。。***/
    //公司名companyName（普通网页的）这里有个坑啊，后台貌似吧app改成appv了，在别的请求里面，不知道这个不能能用2.6
    public void saveDeviceNo2(String url, String umtoken, String user_id, String sign,String time,final httpcallback h){

        //往请求里面添加参数
//        RequestBody formBody = new FormBody.Builder()
//                .add("user_id",user_id)
//                .add("device_no", umtoken)
//                .add("mid","abcdefg")
//                .add("app","android")
//                .add("timestamp",time)
//                .add("sign", sign)
//                .add("app","android")
//                .build();
        RequestBody formBody = new FormBody.Builder()
                .add("userId",user_id)
                .add("deviceNo", umtoken)
                .add("mid","abcdefg")
                .add("appv","android")
                .add("timestamp",time)
                .add("sign", sign)
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }


    //获取首页信息
    public void saveDeviceNo1(String url,String sign,String time,final httpcallback h){

        LogUtil.e("----地址是-"+url);

        String user_id = sharepreferenceUtils.getStringdata(MyApplication.mcontext, "user_id", " ");

        if (user_id.matches("[0-9]+")){
            RequestBody formBody = new FormBody.Builder()
                    .add("userId",user_id)
                    //.add("device_no", umtoken)
                    .add("mid","abcdefg")
                    .add("appv","android")
                    .add("timestamp",time)
                    .add("sign", sign)
                    .build();

            Request request=new Request.Builder().url(url).post(formBody).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            h.fail(e);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    ResponseBody body = response.body();
                    //待会测试下这个响应码2.5
                    int code = response.code();
                    LogUtil.e("---响应码是多少---"+code);
                    final String string = body.string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            h.success(string);
                        }
                    });
                }
            });


        }else {
            RequestBody formBody = new FormBody.Builder()
                    //.add("userId",user_id)
                    //.add("device_no", umtoken)
                    .add("mid","abcdefg")
                    .add("appv","android")
                    .add("timestamp",time)
                    .add("sign", sign)
                    .build();

            Request request=new Request.Builder().url(url).post(formBody).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            h.fail(e);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    ResponseBody body = response.body();
                    //待会测试下这个响应码2.5
                    int code = response.code();
                    LogUtil.e("---响应码是多少---"+code);
                    final String string = body.string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            h.success(string);
                        }
                    });
                }
            });

        }


//        RequestBody formBody = new FormBody.Builder()
//                    .add("userId","90")
//                    //.add("device_no", umtoken)
//                    .add("mid","abcdefg")
//                    .add("appv","android")
//                    .add("timestamp",time)
//                    .add("sign", sign)
//                    .build();
//
//            Request request=new Request.Builder().url(url).post(formBody).build();
//
//            okHttpClient.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, final IOException e) {
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            h.fail(e);
//                        }
//                    });
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//                    ResponseBody body = response.body();
//                    //待会测试下这个响应码2.5
//                    int code = response.code();
//                    LogUtil.e("---响应码是多少---"+code);
//                    final String string = body.string();
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            h.success(string);
//                        }
//                    });
//                }
//            });



    }



    //上传包裹正反面，上传语音文件，上传相关数据
    public void upImage(String url, ArrayList<String> s,String user_id,String logistic_code,String receiver_mobile,String service_time,
            String receiver, String receiver_name,String duration,String longitude,String latitude,String time,String current_addr,
                        String sign, final httpcallback h){

        MultipartBody.Builder builder=new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        ArrayList<String> imageurl=new ArrayList<>();

        //把数组加进集合
//        for (int j=0;j<s.length;j++){
//            imageurl.add(s[j]);
//            Log.e("--",imageurl.get(j));
//        }
        imageurl.addAll(s);

        Log.e("---",imageurl.size()+" ");


        for (int i=0;i<imageurl.size();i++){
            File f=new File(imageurl.get(i));
            //第一个参数是文件名，就是纯文件名，不要jpg的。（这个第一个参数可能是上传图片的key值，然后第二个参数是文件名，带后缀的！切记）
            //image/jpg

            if (f.getName().equals("2.png")){
                builder.addFormDataPart("position_img", f.getName(),RequestBody.create(MediaType.parse(getMimeType(f.getName())),f));
            }

            else  if (f.getName().equals("1.png")){
                builder.addFormDataPart("package_img", f.getName(),RequestBody.create(MediaType.parse(getMimeType(f.getName())),f));

            }else {

                //服务端获取的文件名字就是靠第二个参数，如果第二个参数没有后缀名，那么这个参数服务器是不知道他是个什么类型的文件
                builder.addFormDataPart(f.getName().substring(0, f.getName().length() - 4), f.getName(), RequestBody.create(MediaType.parse(getMimeType(f.getName())), f));
                //builder.addFormDataPart(f.getName(), f.getName().substring(0,f.getName().length()-4),RequestBody.create(MediaType.parse(getMimeType(f.getName())),f));
            }
                LogUtil.e("图片是"+f.getName().substring(0,f.getName().length()-4));
        }

        builder.addFormDataPart("current_addr",current_addr);
        //builder.addFormDataPart("receiver_name","1");
        builder.addFormDataPart("longitude",longitude);
        builder.addFormDataPart("latitude",latitude);

        //签收前姓名，非必要参数。验证签名不加，但是上传必加，其他为空的值同理 4.27
        builder.addFormDataPart("receiver_name", receiver_name);


//        builder.addFormDataPart("current_addr",current_addr);
//        builder.addFormDataPart("longitude",longitude);
//        builder.addFormDataPart("latitude",latitude);


        /***必填参数**/
        builder.addFormDataPart("user_id",user_id);
        //物流单号
        builder.addFormDataPart("logistic_code",logistic_code);
        //收件人电话
        builder.addFormDataPart("receiver_mobile",receiver_mobile);
        //收件时间
        builder.addFormDataPart("service_time",service_time);

        //签收人

        builder.addFormDataPart("receiver", receiver);

        builder.addFormDataPart("duration",duration);


        builder.addFormDataPart("appv","android");
        builder.addFormDataPart("timestamp",time);
        builder.addFormDataPart("sign", sign);
        builder.addFormDataPart("mid","abcdefg");



        RequestBody requestBody = builder.build();
        Request request=new Request.Builder()
                .post(requestBody).url(url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });



            }


            @Override
            public void onResponse (Call call, Response response){
                try {


                    ResponseBody body = response.body();
                    final String string = body.string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            h.success(string);
                        }
                    });

                }catch (Exception e){
                    LogUtil.e("???"+e);

                }
            }

        });



    }


    public void upImage2(String url,String time,String sign,final httpcallback h){

        MultipartBody.Builder builder=new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        ArrayList<String> imageurl=new ArrayList<>();



        builder.addFormDataPart("appv","android");
        builder.addFormDataPart("timestamp",time);
        builder.addFormDataPart("sign", sign);
        builder.addFormDataPart("mid","abcdefg");



        RequestBody requestBody = builder.build();
        Request request=new Request.Builder()
                .post(requestBody).url(url).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });



            }


            @Override
            public void onResponse (Call call, Response response){
                try {


                    ResponseBody body = response.body();
                    final String string = body.string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            h.success(string);
                        }
                    });

                }catch (Exception e){
                    LogUtil.e("???"+e);

                }
            }

        });

    }


    private static String getMimeType(String filename) {
        FileNameMap filenameMap = URLConnection.getFileNameMap();
        String contentType = filenameMap.getContentTypeFor(filename);
        if (contentType == null) {
            contentType = "application/octet-stream"; //* exe,所有的可执行程序
        }
        return contentType;
    }


    /**
     *
     * {"login_type":"password" , "mobile":"17600710151" , "password":"123456" , "appv":"v1",
     * "timestamp":"1111111111111", "sign":"11","test_key":"MIAOQIAN_API_TEST"}
     * @param url
     * @param
     * @param sign
     * @param time
     * @param h
     */


    //这个是快递家的 4.23
    public void saveLogin(String url, String login_type,String mobile,String password,String sign,String time,final httpcallback h){

        //往请求里面添加参数
        RequestBody formBody = new FormBody.Builder()
                .add("login_type",login_type)
                .add("password", password)
                .add("mobile",mobile)
                .add("appv","v1")
                .add("timestamp",time)
                .add("sign", sign)
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                //待会测试下这个响应码2.5
                int code = response.code();
                LogUtil.e("---响应码是多少---"+code);
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }


    //快递家发送短信验证码
    public void sendcode(String url, String mobile,String sign,String time,final httpcallback h){

        //往请求里面添加参数
        RequestBody formBody = new FormBody.Builder()
                .add("mobile",mobile)
                .add("appv","v1")
                .add("timestamp",time)
                .add("sign", sign)
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                //待会测试下这个响应码2.5
                int code = response.code();
                LogUtil.e("---响应码是多少---"+code);
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }


    //快递家获取用户标签的
    public void AddUserTip(String url, String tipname,String user_id,String sign,String time,final httpcallback h){

        //往请求里面添加参数
        RequestBody formBody = new FormBody.Builder()
                .add("name",tipname)
                .add("user_id",user_id)
                .add("appv","v1")
                .add("timestamp",time)
                .add("sign", sign)
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                //待会测试下这个响应码2.5
                int code = response.code();
                LogUtil.e("---响应码是多少---"+code);
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }

    //这个接口被很多类调用，点进去看看就知道了
    public void GetPersonData(String url, String user_id,String sign,String time,final httpcallback h){

        //往请求里面添加参数
        RequestBody formBody = new FormBody.Builder()
                .add("user_id",user_id)
                .add("appv","v1")
                .add("timestamp",time)
                .add("sign", sign)
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                //待会测试下这个响应码2.5
                int code = response.code();
                LogUtil.e("---响应码是多少---"+code);
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }

    //获取快递信息(就是这个快递到哪了)
    public void GetExpressData(String url, String logistic_code,String sign,String time,final httpcallback h){

        //往请求里面添加参数
        RequestBody formBody = new FormBody.Builder()
                .add("logistic_code",logistic_code)
                .add("appv","v1")
                .add("timestamp",time)
                .add("sign", sign)
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                //待会测试下这个响应码2.5
                int code = response.code();
                LogUtil.e("---响应码是多少---"+code);
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }


    //新版首页 5.13 再次改版(这里的user_id也是以前的userId,因为写接口的人变了  5.13)
    public void NewdataHomePage2(String url, Map<String ,String> datas, final httpcallback h){

        RequestBody formBody=null;
        FormBody.Builder builder = new FormBody.Builder();
        // FormBody.Builder add=null;
        Iterator iter = datas.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();

            //这里不能直接就new FormBody.Builder().add("xxx","xxx").build();
            //这样的话会每次都new一个，只能先把new FormBody.Builder()这个提取出来
            formBody=builder.add(key,val).build();

        }

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                //待会测试下这个响应码2.5
                int code = response.code();
                LogUtil.e("---响应码是多少---"+code);
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }

    public void GetExpressImageinfo(String url, String type,String sign,String time,final httpcallback h){

        //往请求里面添加参数
        RequestBody formBody = new FormBody.Builder()
                .add("type",type)
                .add("appv","android")
                .add("timestamp",time)
                .add("sign", sign)
                .build();

        Request request=new Request.Builder().url(url).post(formBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.fail(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                ResponseBody body = response.body();
                //待会测试下这个响应码2.5
                int code = response.code();
                LogUtil.e("---响应码是多少---"+code);
                final String string = body.string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        h.success(string);
                    }
                });
            }
        });

    }



   public  interface  httpcallback{
       void success(String s);
       void fail(Exception e);
   }


   //请求里面添加sessionid然后保存下来，方便下次请求验证！
    public class AddCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences= sharepreferenceUtils.getHashsetdata(MyApplication.mcontext, "cookie", new HashSet<String>());
            for (String cookie : preferences) {
                //后台cookie的开头就是叫cookie。。。
                builder.addHeader("Cookie", cookie);
                Log.e("OkHttp", "Adding Header: " + cookie);
            }
            return chain.proceed(builder.build());
        }
    }

    //这里是接收cookie
    public class ReceivedCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();
                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                    Log.e("---Received",cookies+"");
                }
                sharepreferenceUtils.saveHashsetdata(MyApplication.mcontext,"cookie",cookies);
            }

            return originalResponse;
        }
    }

}
