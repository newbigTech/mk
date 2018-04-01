package com.hand.hmall.initdata.sc;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

/**
 * @author XinyangMei
 * @Title CouponTest
 * @Description desp
 * @date 2017/7/19 17:41
 */
public class CouponTest {

    @Test
    public void spitNum() {
    String str = "20mm*3000mm*30mm";
    String st[] = str.split("\\D{2,3}");
       double packingVolume  = Arrays.stream(st).
                mapToDouble(s->{return Integer.parseInt(s);}).reduce((total,p)->{return total*p/1000d;}).getAsDouble();
        System.out.println(packingVolume);
    }

    @Test
    public void getTime(){
        Date date = new Date("2017/07/23 17:11:24");
        System.out.println(date.getTime());
    }
}
