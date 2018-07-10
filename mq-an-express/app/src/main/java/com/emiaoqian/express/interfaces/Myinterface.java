package com.emiaoqian.express.interfaces;

/**
 * Created by xiong on 2018/4/2.
 */

public class Myinterface  {


    public interface DismessSmallDialog{

        void DismessSmallDialogcallback();
    }


    public interface DismessForNetwork{

        void DismessForNetworkcallback();
    }


   public interface HiddenButtonSheet{
        void HiddenButtonSheetcallback(boolean ishide);
   }

   public interface getWebviewtitle{
       void getWebviewtitlecallback(String webviewtitle);
   }





}
