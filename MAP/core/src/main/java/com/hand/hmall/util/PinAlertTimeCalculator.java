package com.hand.hmall.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * author: zhangzilong
 * name: PinAlertTimeCalculator
 * discription: pin码预警时间计算器
 * date: 2017/11/17
 * version: 0.1
 */
public class PinAlertTimeCalculator {

    /**
     * 预警时间计算器
     *
     * @param createTime pin码事件创建时间
     * @param breakTime  每天不计入预警时间的时间区间（eg：20:00-8:00）
     * @param alertAfter 多久没有进入下一节点后报警(minutes)
     * @return 下一节点报警时间
     */
    public static Date getAlertTime(Date createTime, String breakTime, Integer alertAfter) throws ParseException {
        // 休息开始时间与结束时间
        String breakTimeStart = breakTime.split("-")[0];
        String breakTimeEnd = breakTime.split("-")[1];
        // 时间转换器，用于计算休息时间
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        // 得出每天工作的小时数
        Long breakHours = Math.abs(sdf1.parse(breakTimeStart).getTime() - sdf1.parse(breakTimeEnd).getTime()) / 1000 / 60 / 60;
        // 预警小时数
        int alertAfterHoursTotal = alertAfter / 60;
        // 预警日与pin码事件创建时间的天数差
        int alertAfterDays = (int) (alertAfterHoursTotal / breakHours);

        // 将pin码创建时间加上小时差，判断时针是否落在休息时间段内，如果是，小时数再加一次预警小时数
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(createTime);
        gc.add(12, alertAfter % 720);
        if (sdf1.parse(sdf1.format(gc.getTime())).getTime() - sdf1.parse(breakTimeStart).getTime() > 0 || sdf1.parse(breakTimeEnd).getTime() - sdf1.parse(sdf1.format(gc.getTime())).getTime() >0) {
            gc.add(10, (int) (24 - breakHours));
        }
        // pin码创建时间加上天数差
        gc.add(Calendar.DAY_OF_YEAR, alertAfterDays);

        // 最终得出PIN码预警时间
        return gc.getTime();
    }
}
