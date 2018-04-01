package com.hand.hmall.util;
/*
 * create by Xinyang.mei.
 * All Rights Reserved
 */

import com.markor.map.external.setupservice.dto.SetupSequenceConditionDto;
import com.markor.map.external.setupservice.service.CreateSetupSequenceExternalService;
import com.markor.map.external.setupservice.service.ISetupSequenceHeaderExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * @author XinyangMei
 * @Title GenerateRedeemCodeUtil
 * @Description desp
 * @date 2017/9/24 11:31
 */
@Component
public class GenerateRedeemCodeUtil {
    private static Random strGen = new Random();
    private static char[] numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
    private static GenerateRedeemCodeUtil instance;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CreateSetupSequenceExternalService createSetupSequenceExternalService;

    @Autowired
    private ISetupSequenceHeaderExternalService iSetupSequenceHeaderExternalService;

    public GenerateRedeemCodeUtil() {
    }


    /**
     * 产生随机字符串 *
     */
    public String randomString(int length) {
        if (length < 1) {
            return null;
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[strGen.nextInt(35)];
        }
        return new String(randBuffer);
    }

    //生成兑换码
    public synchronized String getRedeemCode() {

        String timeNow = new SimpleDateFormat("yy-MM-dd").format(Calendar.getInstance().getTime());
        //获取当日流水号

        String[] time = timeNow.split("-");
        String randomPre = randomString(4);
        String randomEnd = randomString(4);
        String serialNum = getRedeemSerialNum();
        StringBuffer sb = new StringBuffer().append(time[2]).append(randomEnd).append(time[0]).append(serialNum).append(time[1]).append(randomPre);
        System.out.println(sb);
        return sb.toString().toUpperCase();
    }

    private String getRedeemSerialNum() {
        String timeNow = new SimpleDateFormat("yyMMdd").format(Calendar.getInstance().getTime());
        String serialNum;
        StringBuffer serialType = new StringBuffer("COU-").append(timeNow);
        SetupSequenceConditionDto setupSequenceConditionDto = createSetupSequenceExternalService.createSetupSequence(36L, 4L, serialType.toString(), "", "", "CP"
                , "", "", "", "", "", "", "", "", "",
                "");
        return iSetupSequenceHeaderExternalService.encode(setupSequenceConditionDto).getCode();
    }
}
