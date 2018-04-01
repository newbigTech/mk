package com.hand.hmall.om.controllers;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.impl.RequestHelper;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hmall.dto.ResponseData;
import com.hand.hmall.om.dto.NoticeConfig;
import com.hand.hmall.om.dto.Notification;
import com.hand.hmall.om.service.INoticeConfigService;
import com.hand.hmall.om.service.INotificationService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author 马君
 * @version 0.1
 * @name NotificationController
 * @description 通知Controller
 * @date 2017/10/19 14:51
 */
@Controller
public class NotificationController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private INoticeConfigService iNoticeConfigService;

    @Autowired
    private INotificationService iNotificationService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 为岗位或员工进行权限赋值
     * @param request request
     * @param noticeConfigs 权限记录
     * @return ResponseData
     */
    @RequestMapping(value = "/notice/addNoticeConfig", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData addNoticeConfig(HttpServletRequest request, @RequestBody List<NoticeConfig> noticeConfigs) {
        try {
            noticeConfigs.stream().forEach(config -> iNoticeConfigService.addConfig(config));
            return new ResponseData(true);
        } catch (Exception e) {
            logger.error("权限赋值失败", e);
            return new ResponseData(false);
        }
    }

    /**
     * 根据权限支配id删除
     * @param request request
     * @param noticeConfigs 待删除权限配置
     * @return ResponseData
     */
    @RequestMapping(value = "/notice/removeNoticeConfig", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData removeNoticeConfig(HttpServletRequest request, @RequestBody List<NoticeConfig> noticeConfigs) {
        try {
            iNoticeConfigService.removeConfigs(noticeConfigs);
            return new ResponseData(true);
        } catch (Exception e) {
            logger.error("权限删除失败", e);
            return new ResponseData(false);
        }
    }

    /**
     * 通知查询
     * @param request request
     * @param notification 查询参数
     * @return ResponseData
     */
    @RequestMapping(value = "/hmall/om/to/do/notice/listNotification")
    @ResponseBody
    public com.hand.hap.system.dto.ResponseData listNotification(Notification notification,@RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                         @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize,HttpServletRequest request) {
        IRequest iRequest = RequestHelper.createServiceRequest(request);
        try {
            List<Notification> list = iNotificationService.listNotification(iRequest, notification,page,pageSize);
            return new com.hand.hap.system.dto.ResponseData(list);
        } catch (Exception e) {
            logger.error("查询失败", e);
            com.hand.hap.system.dto.ResponseData responseData = new com.hand.hap.system.dto.ResponseData();
            responseData.setSuccess(false);
            responseData.setMessage(e.getMessage());
            return responseData;
        }
    }

    /**
     * 确认通知
     * @param request request
     * @param notification 待确认通知
     * @return ResponseData
     */
    @RequestMapping(value = "/hmall/om/to/do/notice/confirm", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData confirm(HttpServletRequest request,Notification notification) {
        IRequest iRequest = RequestHelper.createServiceRequest(request);
        ResponseData responseData = new ResponseData();
        try {
            iNotificationService.confirm(iRequest, notification);
            responseData.setSuccess(true);
            return responseData;
        } catch (Exception e) {
            logger.error("通知确认失败", e);
            responseData.setSuccess(false);
            responseData.setMsg(e.getMessage());
            return responseData;
        }
    }
}
