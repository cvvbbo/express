package com.emiaoqian.express.interfaces;

import com.emiaoqian.express.views.XzhengRelativelayout;

/**
 * Created by xiong on 2018/4/3.
 */

public class Myinterface2 {


    public interface  showredcircle{
        void showredcirclecallback();

        int isshowcallback();

        void hidecallback();
    }


    public interface deleteimage{
        void  delectimagecallback(XzhengRelativelayout xz);
    }

    public interface  getuserinfor{
        void getuserinforcallback(String wait,String signed,String haspush,String url);
    }

    public interface  GetDrawlayoutstate{
        void GetDrawlayoutstatecallback();
    }

    public interface Getheadimg{
        void Getheadimgcallback(String headimg,String leve_im);
    }

    public interface WebviewChangeHeadIm{
        void WebviewChangeHeadImcallback();
    }
}


