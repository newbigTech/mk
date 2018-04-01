package com.hand.hmall.util;

import com.hand.hmall.dto.PagedValues;
import com.hand.hmall.dto.Params;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.dto.ScoreRange;
import com.hand.hmall.util.redis.dao.BaseDao;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author tiantao 1.1
 *
 */
public class PageUtil {

    private static final String PAGE="page";      //当前页面编号
    private static final String PAGESIZE="pagesize";    //每页显示数据的个数


    /**
     * 获取当前要显示的页面编号,如果没有page属性，那么返回1
     * @param map
     * @return
     * @throws PageException
     */
    public static Integer getPage(Map<String,Object> map) throws PageException{
        Object parmeterValue=map.get(PAGE);
        Integer page=1;
        try{
            page=Integer.valueOf(parmeterValue.toString());
            map.remove(PAGE);
            return page;
        }catch(Exception ex){
            throw new PageException(PageException.GET_PAGE_ERROR);
        }
    }

    /**
     * 获取每页要显示数据的个数，如果没有默认为10
     * @param map
     * @return
     * @throws PageException
     */
    public static Integer getPageSize(Map<String,Object> map) throws PageException{
        Object parmeterValue=map.get(PAGESIZE);
        Integer pageSize=10;
        try{
            pageSize=Integer.valueOf(parmeterValue.toString());
            map.remove(PAGESIZE);
            return pageSize;
        }catch(Exception ex){
            throw new PageException(PageException.GET_PAGE_ERROR);
        }
    }

    /**
     * 通过key值，获取map中对应key的值，如果没有该值，返回null
     * @param map
     * @param name
     * @return
     */
    public static String getValue(Map<String, Object> map,String name){
        Object value=map.get(name);
        if(value==null){
            return null;
        }else{
            return value.toString().trim();
        }
    }

    /**
     * 通过name，获取map中对应的key值，如果没有该值，或者值的个数为0返回null，
     * 如果有该值并且不是List<Object>抛出异常
     * @param map
     * @param name
     * @return
     * @throws PageException
     */
    public static List<Object> getList(Map<String,Object> map,String name) throws PageException{
        Object value=map.get(name);
        if(value==null){
            return null;
        }else{
            try{
                List<Object> list=(List<Object>)map.get(name);
                if(list.size()<=0){
                    return null;
                }
                return list;
            }catch(Exception ex){
                throw new PageException(PageException.ILLEGAL_LISTOBJECT);
            }
        }
    }



    /**
     * 获取日期类型的字段
     * @param map  存放的查询条件
     * @param name   字段名
     * @param format 日期格式
     * @return
     * @throws ParseException
     */
    public static Long getDate(Map<String, Object> map,String name,String format) throws PageException{
        Object value=map.get(name);
        if(value==null){
            return null;
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                return sdf.parse(value.toString()).getTime();
            } catch (ParseException e) {
                throw new PageException(PageException.DATE_FORMAT_ERROR);
            }
        }
    }

    /**
     * 添加Equal条件<br/>
     * 如果value为null或“”，则不添加
     * @return
     */
    public static Params addEqual(String name,String value,Params params){
        if(params==null){      //如果params为null，创建params
            params=new Params();
        }
        if(params.getEquals()==null){       //如果Equal为null，创建Equal
            params.setEquals(new HashMap<String,List<Object>>());
        }

        Map<String,List<Object>> map=params.getEquals();
        if(value!=null&&!value.equals("")){
            List<Object> list=new ArrayList<Object>();
            list.add(value);
            map.put(name, list);
        }
        params.setEquals(map);
        return params;
    }

    /**
     * 添加Match条件
     * @param name
     * @param value
     * @param params
     * @return
     */
    public static Params addMatch(String name,String value,Params params){
        if(params==null){      //如果params为null，创建params
            params=new Params();
        }
        if(params.getMatches()==null){    //如果Match为null，创建Match
            params.setMatches(new HashMap<String,Object>());
        }

        Map<String,Object> map_match=params.getMatches();
        if(value!=null&&!value.equals("")){
            map_match.put(name, value);
        }
        params.setMatches(map_match);

        return params;
    }

    /**
     * 添加range条件  (未测试)
     * @param name
     * @param min
     * @param max
     * @param params
     * @return
     */
    public static Params addRange(String name,Object min,Object max,Params params){
        if(params==null){      //如果params为null，创建params
            params=new Params();
        }
        if(params.getRanges()==null){    //如果Range为null，创建Range
            params.setRanges(new HashMap<String,ScoreRange>());
        }

        Map<String,ScoreRange> map_range=params.getRanges();
        if(min==null||min.equals("")){min=null;}
        if(max==null||max.equals("")){max=null;}

        if(min==null&&max==null){
            return params;
        }else{
            ScoreRange scoreRange=new ScoreRange();
            if(min!=null){
                scoreRange.setMin(min.toString());
            }
            if(max!=null){
                scoreRange.setMax(max.toString());
            }
            map_range.put(name, scoreRange);
            return params;
        }

    }


    /**
     * 通过查询条件获取查询信息(并进行分页)
     * @param params
     * @return 如果查询到数据，返回pageValues对象，如果没有查询到数据，返回空的Pagevalues对象
     */
    public static PagedValues QueryInfo_byParams(BaseDao baseDao,Params params,Integer page,Integer pageSize){

        List<String> keys = new ArrayList<String>();

        //添加equal条件
        if (MapUtils.isNotEmpty(params.getEquals())) {
            String key1 = baseDao.selectIdsSetByEqual(params.getEquals());
            if (StringUtils.isNotEmpty(key1)) {
                keys.add(key1);
            }
        }

        //添加ranges条件
        if (MapUtils.isNotEmpty(params.getRanges())) {
            String key2 = baseDao.selectIdsSetByRange(params.getRanges());
            if (StringUtils.isNotEmpty(key2)) {
                keys.add(key2);
            }
        }

        //添加match条件
        Map<String, Object> matches = params.getMatches();
        if (MapUtils.isNotEmpty(matches)) {
            String key3 = baseDao.selectIdsSetByMatch(matches);
            if (StringUtils.isNotEmpty(key3)) {
                keys.add(key3);
            }
        }

        PagedValues values=null;
        if (keys.size() > 0) {
            values = baseDao.selectValuesByKeys(keys,
                    (page - 1) * pageSize, pageSize, true);
        }else{
            values=new PagedValues(0,Collections.EMPTY_LIST);
        }

        return values;
    }

    /**
     * 通过查询条件获取查询满足条件的所有信息
     * @param baseDao
     * @param params
     * @return
     */
    public static List<Map<String,?>> QueryInfo_byParams(BaseDao baseDao,Params params){

        List<String> keys = new ArrayList<String>();

        //添加equal条件
        if (MapUtils.isNotEmpty(params.getEquals())) {
            String key1 = baseDao.selectIdsSetByEqual(params.getEquals());
            if (StringUtils.isNotEmpty(key1)) {
                keys.add(key1);
            }
        }

        //添加ranges条件
        if (MapUtils.isNotEmpty(params.getRanges())) {
            String key2 = baseDao.selectIdsSetByRange(params.getRanges());
            if (StringUtils.isNotEmpty(key2)) {
                keys.add(key2);
            }
        }

        //添加match条件
        Map<String, Object> matches = params.getMatches();
        if (MapUtils.isNotEmpty(matches)) {
            String key3 = baseDao.selectIdsSetByMatch(matches);
            if (StringUtils.isNotEmpty(key3)) {
                keys.add(key3);
            }
        }

        List<Map<String,?>> values=null;
        if (keys.size() > 0) {
            values = baseDao.selectValuesByKeys(keys);
        }else{
            values=Collections.EMPTY_LIST;
        }

        return values;
    }


    /**
     * 获取用户是否输入条件
     * @param map
     * @return 如果输入返回true，没有输入返回false
     */
    public static boolean isHaveConditions(Map<String,Object> map){
        for (String key : map.keySet()) {
            Object value=map.get(key);
            if(value!=null&&!value.toString().trim().equals("")){
                return true;
            }
        }
        return false;
    }


    /**
     * 添加Equal条件，，其中的条件有可能是List<Object>,或者Object
     * @param name
     * @param value
     * @param params
     * @return
     */
    public static Params addEqualList(String name,Object value,Params params){
        if(params==null){      //如果params为null，创建params
            params=new Params();
        }
        if(params.getEquals()==null){       //如果Equal为null，创建Equal
            params.setEquals(new HashMap<String,List<Object>>());
        }

        Map<String,List<Object>> map=params.getEquals();
        if(value!=null&&!value.equals("")){
            List<Object> list_obj;
            try{
                list_obj=(List<Object>) value;     //如果强转换List《Object》成功！
                map.put(name, list_obj);
            }catch(Exception ex){             //如果强转换失败
                list_obj=new ArrayList<Object>();
                list_obj.add(value);
                map.put(name, list_obj);
            }
        }
        params.setEquals(map);

        return params;
    }


    /**
     * 通过多条件，查询结果，并提取key=name的数据，组装成一个集合 (配合多表查询)
     * @param baseDao
     * @param params
     * @param name
     */
    public static List<String> byEqualGetName(BaseDao baseDao,Params params,String name){
        List<String> keys = new ArrayList<String>();

        //添加equal条件
        if (MapUtils.isNotEmpty(params.getEquals())) {
            String key1 = baseDao.selectIdsSetByEqual(params.getEquals());
            if (StringUtils.isNotEmpty(key1)) {
                keys.add(key1);
            }
        }

        //添加ranges条件
        if (MapUtils.isNotEmpty(params.getRanges())) {
            String key2 = baseDao.selectIdsSetByRange(params.getRanges());
            if (StringUtils.isNotEmpty(key2)) {
                keys.add(key2);
            }
        }

        //添加match条件
        Map<String, Object> matches = params.getMatches();
        if (MapUtils.isNotEmpty(matches)) {
            String key3 = baseDao.selectIdsSetByMatch(matches);
            if (StringUtils.isNotEmpty(key3)) {
                keys.add(key3);
            }
        }

        List<String> list_Name=new ArrayList<String>();
        if (keys.size() > 0) {
            List<Map<String,?>> list=baseDao.selectValuesByKeys(keys);
            for (Map<String, ?> map : list) {
                list_Name.add(map.get(name).toString());
            }
        }

        return list_Name;

    }


    /**
     * 通过pageValues对象，获取ResponseData对象
     * @param pageValues
     * @return
     */
    public static ResponseData byPageValuesGetResponseData(PagedValues pageValues){
        if(pageValues!=null){
            ResponseData data=new ResponseData();
            data.setResp(pageValues.getValues());
            data.setTotal((int)pageValues.getTotal());
            return data;
        }else{
            return new ResponseData();
        }
    }

    /**
     * 通过异常对象，获取ResponseData对象
     * @param e
     * @return
     */
    public static ResponseData byExceptionGetResponseData(Exception e){
        ResponseData data=new ResponseData();
        data.setSuccess(false);
        data.setMsg(e.getMessage());
        return data;
    }



}
