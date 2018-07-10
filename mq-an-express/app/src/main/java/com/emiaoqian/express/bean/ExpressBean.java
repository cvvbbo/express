package com.emiaoqian.express.bean;

import java.util.List;

/**
 * Created by xiong on 2018/4/24.
 */

public class ExpressBean {


    /**
     * day : 04-01
     * data : [{"AcceptTime":"07:21","AcceptStation":"【北京市】  【北京通州三部】 的丛小亮13691300643（13691300643） 正在第1次派件, 请保持电话畅通,并耐心等待"},{"AcceptTime":"06:40","AcceptStation":"【北京市】  快件到达 【北京通州三部】"}]
     */

    public String day;
    public List<DataBean> data;



    public static class DataBean {
        /**
         * AcceptTime : 07:21
         * AcceptStation : 【北京市】  【北京通州三部】 的丛小亮13691300643（13691300643） 正在第1次派件, 请保持电话畅通,并耐心等待
         */

        public String AcceptTime;
        public String AcceptStation;


    }
}
