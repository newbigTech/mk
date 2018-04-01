package com.hand.hap.activiti.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.hand.hap.activiti.components.ApprovalRule;
import com.hand.hap.activiti.core.IActivitiConstants;
import com.hand.hap.activiti.custom.ForecastActivityCmd;
import com.hand.hap.activiti.custom.ICustomTaskProcessor;
import com.hand.hap.activiti.custom.JumpActivityCmd;
import com.hand.hap.activiti.custom.process.CustomHistoricProcessInstanceQueryResource;
import com.hand.hap.activiti.custom.task.CustomTaskQueryResource;
import com.hand.hap.activiti.dto.*;
import com.hand.hap.activiti.exception.TaskActionException;
import com.hand.hap.activiti.exception.WflSecurityException;
import com.hand.hap.activiti.exception.dto.ActiviException;
import com.hand.hap.activiti.exception.mapper.ActiviExceptionMapper;
import com.hand.hap.activiti.listeners.TaskCreateNotificationListener;
import com.hand.hap.activiti.service.IActivitiService;
import com.hand.hap.activiti.util.ActivitiUtils;
import com.hand.hap.core.IRequest;
import com.hand.hap.hr.dto.Employee;
import com.hand.hap.mybatis.util.StringUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.form.FormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.persistence.entity.data.GroupDataManager;
import org.activiti.engine.impl.persistence.entity.data.UserDataManager;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.rest.common.api.DataResponse;
import org.activiti.rest.service.api.RestResponseFactory;
import org.activiti.rest.service.api.engine.variable.RestVariable;
import org.activiti.rest.service.api.history.HistoricProcessInstanceCollectionResource;
import org.activiti.rest.service.api.history.HistoricProcessInstanceQueryRequest;
import org.activiti.rest.service.api.history.HistoricTaskInstanceQueryRequest;
import org.activiti.rest.service.api.history.HistoricTaskInstanceQueryResource;
import org.activiti.rest.service.api.management.DeadLetterJobCollectionResource;
import org.activiti.rest.service.api.runtime.process.ExecutionVariableCollectionResource;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCollectionResource;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.activiti.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.activiti.rest.service.api.runtime.task.TaskActionRequest;
import org.activiti.rest.service.api.runtime.task.TaskQueryRequest;
import org.activiti.rest.service.api.runtime.task.TaskResource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author shengyang.zhou@hand-china.com
 * @author njq.niu@hand-china.com
 */
@Service
public class ActivitiServiceImpl implements IActivitiService, IActivitiConstants, InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private GroupDataManager groupDataManager;

    @Autowired
    private UserDataManager userDataManager;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RestResponseFactory restResponseFactory;

    @Autowired
    private ProcessEngineConfigurationImpl processEngineConfiguration;

    @Autowired
    private ActiviExceptionMapper exceptionMapper;

    @Autowired
    private ApprovalRule approvalRule;

    @Autowired
    private ForecastActivityCmd forecastActivityCmd;



   /* @Autowired
    private TaskCreateNotificationListener taskCreateNotificationListener;*/

    /* 以下 为 手动注入的 bean */
    private TaskResource taskResource = new TaskResource();

    private ExecutionVariableCollectionResource executionVariableCollectionResource = new ExecutionVariableCollectionResource();

    private CustomTaskQueryResource taskQueryResource = new CustomTaskQueryResource();

    private CustomHistoricProcessInstanceQueryResource historicProcessInstanceQueryResource = new CustomHistoricProcessInstanceQueryResource();

    private ProcessInstanceCollectionResource processInstanceCollectionResource = new ProcessInstanceCollectionResource();

    private HistoricTaskInstanceQueryResource historicTaskInstanceQueryResource = new HistoricTaskInstanceQueryResource();

    private HistoricProcessInstanceCollectionResource historicProcessInstanceCollectionResource = new HistoricProcessInstanceCollectionResource();

    private DeadLetterJobCollectionResource deadLetterJobCollectionResource = new DeadLetterJobCollectionResource();

    /* Fake request,response,used to call rest api */
    private HttpServletRequest fakeRequest = new MockHttpServletRequest();
    private HttpServletResponse fakeResponse = new MockHttpServletResponse();

    private List<ICustomTaskProcessor> taskProcessors ;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ProcessInstanceResponse startProcess(IRequest iRequest, ProcessInstanceCreateRequest createRequest) {
        try {
            String employeeCode = iRequest.getEmployeeCode();
            Authentication.setAuthenticatedUserId(employeeCode);
            return processInstanceCollectionResource.createProcessInstance(createRequest, fakeRequest, fakeResponse);
        } finally {
            Authentication.setAuthenticatedUserId(null);
        }
    }

    public DataResponse getInvolvedProcess(IRequest request, Map<String, String> allParameters) {
        return processInstanceCollectionResource.getProcessInstances(allParameters, fakeRequest);
    }

    @Override
    public Model deployModel(String modelId) throws Exception {
        Model model = repositoryService.getModel(modelId);

        byte[] modelData = repositoryService.getModelEditorSource(modelId);
        JsonNode jsonNode = objectMapper.readTree(modelData);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);

        // byte[] xmlBytes = new BpmnXMLConverter().convertToXML(bpmnModel,
        // "UTF-8");

        Deployment deploy = repositoryService.createDeployment().category(model.getCategory()).name(model.getName())
                .key(model.getKey()).addBpmnModel(model.getKey() + ".bpmn20.xml", bpmnModel).deploy();

        model.setDeploymentId(deploy.getId());
        repositoryService.saveModel(model);
        return model;

    }

    @Override
    public void completeTask(IRequest request, Task taskEntity, TaskActionRequestExt actionRequest)
            throws TaskActionException {
        if (!TaskActionRequest.ACTION_COMPLETE.equalsIgnoreCase(actionRequest.getAction())) {
            return;
        }
        Authentication.setAuthenticatedUserId(request.getEmployeeCode());
        String action = null;// 本次操作执行的动作
        String taskId = taskEntity.getId();
        List<RestVariable> vars = actionRequest.getVariables();
        if (vars != null) {
            for (RestVariable rv : vars) {
                if (PROP_APPROVE_RESULT.equalsIgnoreCase(rv.getName())) {
                    action = String.valueOf(rv.getValue());
                    break;
                }
            }
        }

        if (StringUtils.isEmpty(taskEntity.getAssignee())) {
            actionRequest.setAssignee(request.getEmployeeCode());
            // 自动 claim
            taskService.claim(taskId, request.getEmployeeCode());
            taskService.addComment(taskId, taskEntity.getProcessInstanceId(), COMMENT_ACTION, action);
            taskService.addComment(taskId, taskEntity.getProcessInstanceId(), PROP_COMMENT, actionRequest.getComment());
            taskResource.executeTaskAction(taskId, actionRequest);
        } else {
            actionRequest.setAssignee(request.getEmployeeCode());
            taskService.addComment(taskId, taskEntity.getProcessInstanceId(), COMMENT_ACTION, action);
            taskService.addComment(taskId, taskEntity.getProcessInstanceId(), PROP_COMMENT, actionRequest.getComment());
            taskResource.executeTaskAction(taskId, actionRequest);
        }
    }

    @Override
    public void delegateTask(IRequest request,  Task taskEntity, TaskActionRequestExt actionRequest)
            throws TaskActionException {
        if (!TaskActionRequest.ACTION_DELEGATE.equalsIgnoreCase(actionRequest.getAction())) {
            return;
        }
        Authentication.setAuthenticatedUserId(request.getEmployeeCode());
        String assignee = actionRequest.getAssignee();
        if (StringUtils.isEmpty(assignee)) {
            throw new TaskActionException(TaskActionException.DELEGATE_NO_ASSIGNEE);
        }

        String taskId = taskEntity.getId();
        taskService.addComment(taskId, taskEntity.getProcessInstanceId(), COMMENT_ACTION, DELEGATE);
        taskService.addComment(taskId, taskEntity.getProcessInstanceId(), PROP_COMMENT, actionRequest.getComment());
       /* taskService.addComment(taskId, taskEntity.getProcessInstanceId(), COMMENT_DELEGATE_BY, actionRequest.getComment());*/
        taskService.setAssignee(taskId, assignee);

        //taskCreateNotificationListener.sendMessage(taskEntity.getAssignee());
        //taskCreateNotificationListener.sendMessage(assignee);

      /*  DelegationState state = taskEntity.getDelegationState();
        if (state != null && state == DelegationState.PENDING) {
            // 正在转交中
            throw new TaskActionException(TaskActionException.DELEGATE_IN_PENDING);
        }

        if (taskEntity.getOwner() != null) {

            if (eq(taskEntity.getOwner(), assignee)) {
                throw new TaskActionException(TaskActionException.DELEGATE_TO_OWNER);
            }

            if (!hasRight(request.getEmployeeCode(), taskEntity.getOwner())) {
                throw new TaskActionException(TaskActionException.DELEGATE_NEED_OWNER_OR_ADMIN);
            }
        }

        taskEntity.setOwner(assignee);// change owner when delegate
        taskService.saveTask(taskEntity);

        if (StringUtils.isEmpty(taskEntity.getAssignee())) {
            // actionRequest.setAssignee(request.getEmployeeCode());
            taskService.addComment(taskId, taskEntity.getProcessInstanceId(), COMMENT_DELEGATE_BY, actionRequest.getComment());
            taskResource.executeTaskAction(taskId, actionRequest);
        } else if (hasRight(request.getEmployeeCode(), taskEntity.getAssignee())) {
            // actionRequest.setAssignee(request.getEmployeeCode());
            taskService.addComment(taskId, taskEntity.getProcessInstanceId(), COMMENT_DELEGATE_BY, actionRequest.getComment());
            taskResource.executeTaskAction(taskId, actionRequest);
        } else {
            throw new TaskActionException(TaskActionException.COMPLETE_TASK_NEED_ASSIGNEE_OR_ADMIN);
        }*/
    }

    @Override
    public void resolveTask(IRequest request, Task taskEntity, TaskActionRequestExt actionRequest)
            throws TaskActionException {
        if (!TaskActionRequest.ACTION_RESOLVE.equalsIgnoreCase(actionRequest.getAction())) {
            return;
        }
        Authentication.setAuthenticatedUserId(request.getEmployeeCode());

        String taskId = taskEntity.getId();

       /* if (!hasRight(request.getEmployeeCode(), taskEntity.getOwner())) {
            throw new TaskActionException(TaskActionException.RESOLVE_NEED_OWNER_OR_ADMIN);
        }
        taskResource.executeTaskAction(taskId, actionRequest);*/
        String action = null;// 本次操作执行的动作

        List<RestVariable> vars = actionRequest.getVariables();
        if (vars != null) {
            for (RestVariable rv : vars) {
                if (PROP_APPROVE_RESULT.equalsIgnoreCase(rv.getName())) {
                    action = String.valueOf(rv.getValue());
                    break;
                }
            }
        }
        taskService.addComment(taskId, taskEntity.getProcessInstanceId(), COMMENT_ACTION, action);
        taskService.addComment(taskId, taskEntity.getProcessInstanceId(), PROP_COMMENT, actionRequest.getComment());
        taskService.resolveTask(taskId);
        //taskCreateNotificationListener.sendMessage(taskEntity.getAssignee());
    }

    public void addSignTask(IRequest request, Task taskEntity, TaskActionRequestExt actionRequest) {
        if (!ACTION_ADD_SIGN.equalsIgnoreCase(actionRequest.getAction())) {
            return;
        }
        Authentication.setAuthenticatedUserId(request.getEmployeeCode());
        String assignee = actionRequest.getAssignee();

        String taskId = taskEntity.getId();
        taskService.addComment(taskId, taskEntity.getProcessInstanceId(), COMMENT_ACTION, ADD_SIGN);
        taskService.addComment(taskId, taskEntity.getProcessInstanceId(), PROP_COMMENT, actionRequest.getComment());
        taskService.delegateTask(taskId, assignee);
        //taskCreateNotificationListener.sendMessage(taskEntity.getAssignee());
    }
    public  void carbonCopy(IRequest request, Task taskEntity, TaskActionRequestExt actionRequest) throws TaskActionException{

        Authentication.setAuthenticatedUserId(request.getEmployeeCode());
        String assignee = actionRequest.getCarbonCopyUsers();
        Set<String> users = org.springframework.util.StringUtils.commaDelimitedListToSet(assignee);
        String taskId = taskEntity.getId();
        for(String user : users) {
            taskService.addUserIdentityLink(taskId, user, ACTION_CARBON_COPY);
        }
    }

    @Override
    public void jumpTo(IRequest request, Task taskEntity, TaskActionRequestExt actionRequest) {
        if (!ACTION_JUMP.equalsIgnoreCase(actionRequest.getAction())) {
            return;
        }
        Authentication.setAuthenticatedUserId(request.getEmployeeCode());
        String taskId = taskEntity.getId();
        taskService.addComment(taskId, taskEntity.getProcessInstanceId(), COMMENT_ACTION, JUMP);
        taskService.addComment(taskId, taskEntity.getProcessInstanceId(), PROP_COMMENT, actionRequest.getComment());
        JumpActivityCmd cmd = new JumpActivityCmd(taskId, actionRequest.getJumpTarget());
        processEngineConfiguration.getCommandExecutor().execute(cmd);
    }

    protected boolean hasRight(String a, String b,boolean isAdmin) {
        return isAdmin || eq(a, b);
    }

    protected boolean eq(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    @Override
    public void executeTaskAction(IRequest request, String taskId, TaskActionRequestExt actionRequest,boolean isAdmin)
            throws TaskActionException {
        if (StringUtils.isEmpty(actionRequest.getAction())) {
            throw new IllegalArgumentException("Action is required.");
        }
        Task taskEntity = getTaskById(taskId);
        if (!hasRight(request.getEmployeeCode(), taskEntity.getAssignee(),isAdmin)) {
            throw new TaskActionException(TaskActionException.COMPLETE_TASK_NEED_ASSIGNEE_OR_ADMIN);
        }
        Authentication.setAuthenticatedUserId(request.getEmployeeCode());

        try {
            //处理抄送
            carbonCopy(request,taskEntity,actionRequest);
            if (TaskActionRequest.ACTION_COMPLETE.equalsIgnoreCase(actionRequest.getAction())) {
                completeTask(request, taskEntity, actionRequest);
                return;
            }
            if (TaskActionRequest.ACTION_DELEGATE.equalsIgnoreCase(actionRequest.getAction())) {
                delegateTask(request, taskEntity, actionRequest);
                return;
            }

            if (TaskActionRequest.ACTION_RESOLVE.equalsIgnoreCase(actionRequest.getAction())) {
                resolveTask(request, taskEntity, actionRequest);
                return;
            }

            if (ACTION_JUMP.equalsIgnoreCase(actionRequest.getAction())) {
                jumpTo(request, taskEntity, actionRequest);
                return;
            }
            if (ACTION_ADD_SIGN.equals(actionRequest.getAction())) {
                addSignTask(request, taskEntity, actionRequest);
                return;
            }

        } catch (ActivitiException e) {
            self().saveException(taskId, e);
            throw e;
        }
    }

    public void saveException(String taskId, ActivitiException e) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        StackTraceElement[] elements = e.getStackTrace();
        String mess = null;
        for (StackTraceElement element : elements) {
            mess += element.toString() + "\n\r";
        }
        exceptionMapper.insert(new ActiviException(task.getProcessInstanceId(), mess, new Date()));
    }

    @Override
    public void deleteDeployment(String deploymentId,Boolean cascade) {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        String processDefinitionId = processDefinition.getId();
        List<Task> taskList = taskService.createTaskQuery().processDefinitionId(processDefinitionId).list();
        Set<String> users = new HashSet<>();
        taskList.forEach(t -> {
            users.add(t.getAssignee());
        });
        if (cascade) {
            repositoryService.deleteDeployment(deploymentId, true);
        } else {
            repositoryService.deleteDeployment(deploymentId);
        }
        for(String user : users){
            //taskCreateNotificationListener.sendMessage(user);
        }
    }

    @Override
    public List<ActivitiNode> getProcessNodes(IRequest request, String processDefinitionId) {
        Process process = ProcessDefinitionUtil.getProcess(processDefinitionId);
        List<ActivitiNode> list = new ArrayList<>();
        Collection<FlowElement> eles = process.getFlowElements();
        for (FlowElement fe : eles) {
            if (fe instanceof UserTask) {
                ActivitiNode node = new ActivitiNode();
                node.setName(fe.getName());
                node.setNodeId(fe.getId());
                node.setType("UserTask");
                list.add(node);
            }
        }
        return list;
    }

    @Override
    public List<ActivitiNode> getUserTaskFromModelSource(IRequest request, String modelId) {
        List<ActivitiNode> list = new ArrayList<>();
        byte[] data = repositoryService.getModelEditorSource(modelId);
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(data);
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
            Process process = bpmnModel.getMainProcess();
            Collection<FlowElement> elements = process.getFlowElements();
            for (FlowElement flowElement : elements) {
                if (flowElement instanceof UserTask) {
                    ActivitiNode node = new ActivitiNode();
                    node.setNodeId(flowElement.getId());
                    node.setName(flowElement.getName());
                    node.setType("UserTask");
                    list.add(node);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    @Override
    public String getEmployeeName(String userId) {
        UserEntity userEntity = userDataManager.findById(userId);
        if (userEntity != null) {
            return userEntity.getFirstName();
        }
        return userId;
    }

    @Override
    public String getGroupName(String groupId) {
        Group group = groupDataManager.findById(groupId);
        if (group != null) {
            return group.getName();
        }
        return groupId;
    }

    @Override
    public HistoricProcessInstanceResponseExt getInstanceDetail(IRequest request, String processInstanceId) {
        HistoricProcessInstanceResponseExt historicProcessInstanceResponseExt = new HistoricProcessInstanceResponseExt();
        //查询流程实例历史
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //设置申请人，流程名称
        historicProcessInstanceResponseExt.setStartUserId(historicProcessInstance.getStartUserId());
        historicProcessInstanceResponseExt.setStartUserName(getEmployeeName(historicProcessInstance.getStartUserId()));
        historicProcessInstanceResponseExt.setProcessName(historicProcessInstance.getProcessDefinitionName());
        historicProcessInstanceResponseExt.setStartTime(historicProcessInstance.getStartTime());

        //获取流程活动历史
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
        List<HistoricTaskInstanceResponseExt> list = new ArrayList<>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            setHistoricActivityInstanceResponseExt(historicActivityInstance, list, processInstanceId);
        }
        historicProcessInstanceResponseExt.getHistoricTaskList().addAll(list);

        //设置全局表单url
        String key = formService.getStartFormKey(historicProcessInstance.getProcessDefinitionId());
        historicProcessInstanceResponseExt.setBusinessKey(historicProcessInstance.getBusinessKey());
        historicProcessInstanceResponseExt.setFormKey(key);


        return historicProcessInstanceResponseExt;
    }

    @Override
    public TaskResponseExt getTaskDetails(IRequest request, String taskId,boolean isAdmin) throws WflSecurityException{
        Task task = getTaskById(taskId);

        TaskResponseExt taskExt = new TaskResponseExt(task);

        List<Group> userGroup = null;

        // display name of assignee or group
        if (StringUtils.isNotEmpty(taskExt.getAssignee())) {
            // privilege check
            if (!hasRight(request.getEmployeeCode(), taskExt.getAssignee(),isAdmin)) {
                throw new WflSecurityException(WflSecurityException.NEED_ASSIGNEE_OR_ADMIN);
            }
            taskExt.setAssigneeName(getEmployeeName(taskExt.getAssignee()));
        } else {
            //TODO : 检查逻辑
            List<IdentityLink> idList = taskService.getIdentityLinksForTask(task.getId());
            List<String> nameList = new ArrayList<>();
            boolean isCandi = isAdmin;
            for (IdentityLink il : idList) {
                if (il.getGroupId() != null) {
                    // privilege check
                    if (!isCandi) {
                        if (userGroup == null) {
                            userGroup = processEngineConfiguration.getUserDataManager()
                                    .findGroupsByUser(request.getEmployeeCode());
                        }
                        for (Group g : userGroup) {
                            if (eq(g.getId(), il.getGroupId())) {
                                isCandi = true;
                                break;
                            }
                        }
                    }
                    // privilege check end

                    nameList.add(getGroupName(il.getGroupId()));
                } else if (il.getUserId() != null) {
                    if (!isCandi && eq(request.getEmployeeCode(), il.getUserId())) {
                        // privilege check
                        isCandi = true;
                    }
                    nameList.add(getEmployeeName(il.getUserId()));
                }
            }
            if (!isCandi) {
                if (!hasRight(request.getEmployeeCode(), taskExt.getAssignee(),isAdmin)) {
                    throw new WflSecurityException(WflSecurityException.NEED_ASSIGNEE_OR_ADMIN);
                }
            }
            taskExt.setAssigneeName(StringUtils.join(nameList.toArray(), ";"));
        }

        // attachment
        List<org.activiti.engine.task.Attachment> attaList = taskService.getTaskAttachments(taskId);
        taskExt.setAttachments(attaList);

        // form data:formVariables
        FormData formData = formService.getTaskFormData(taskId);
        taskExt.setFormData(restResponseFactory.createFormDataResponse(formData));

        // approve history
       /* List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).finished().list();
        for (HistoricTaskInstance aHistoricTaskInstanceList : historicTaskInstanceList) {
            HistoricTaskInstanceResponseExt taskHistory = new HistoricTaskInstanceResponseExt(
                    aHistoricTaskInstanceList);
            if (StringUtil.isNotEmpty(taskHistory.getAssignee())) {
                taskHistory.setAssigneeName(getEmployeeName(taskHistory.getAssignee()));
            }

            taskHistory.setComment(getCommentOfType(taskHistory.getId(), PROP_COMMENT));
            taskHistory.setAction(getCommentOfType(taskHistory.getId(), COMMENT_ACTION));

            taskExt.getHistoricTaskList().add(taskHistory);
        }*/
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<HistoricTaskInstanceResponseExt> list = new ArrayList<>();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            setHistoricActivityInstanceResponseExt(historicActivityInstance, list, task.getProcessInstanceId());
        }
        taskExt.getHistoricTaskList().addAll(list);


        // delegate
        List<Comment> comments = taskService.getTaskComments(task.getId(), COMMENT_DELEGATE_BY);
        if (!comments.isEmpty()) {
            Comment comment = comments.get(comments.size() - 1);
            TaskDelegate taskDelegate = new TaskDelegate();

            taskDelegate.setFromUserId(comment.getUserId());
            taskDelegate.setFromUserName(getEmployeeName(comment.getUserId()));
            taskDelegate.setTime(comment.getTime());
            taskDelegate.setReason(comment.getFullMessage());
            taskExt.setTaskDelegate(taskDelegate);
        }

        // processInstance
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId()).singleResult();
        ProcessInstanceResponseExt processInstanceResp = (ProcessInstanceResponseExt) restResponseFactory
                .createProcessInstanceResponse(processInstance);
        processInstanceResp.setStartUserName(getEmployeeName(processInstanceResp.getStartUserId()));
        taskExt.setProcessInstance(processInstanceResp);

        // execution variable
        List<RestVariable> vars = executionVariableCollectionResource.getVariables(task.getExecutionId(), null,
                fakeRequest);

        taskExt.setExecutionVariables(vars);
        return taskExt;
    }

    @Override
    public TaskResponseExt getTaskDetails(IRequest request, String taskId) throws WflSecurityException {
        return  getTaskDetails(request,taskId,false);
    }

    @Override
    public DataResponse queryTaskList(IRequest iRequest, TaskQueryRequest taskQueryRequest,
                                      Map<String, String> requestParams) {

        DataResponse dataResponse = taskQueryResource.getQueryResult(taskQueryRequest, requestParams, fakeRequest);
        List<TaskResponseExt> list = (List<TaskResponseExt>) dataResponse.getData();
        for (TaskResponseExt taskResponse : list) {
            if (StringUtils.isNotEmpty(taskResponse.getOwner())) {
                taskResponse.setOwner(getEmployeeName(taskResponse.getOwner()));
            }
            if (StringUtils.isNotEmpty(taskResponse.getAssignee())) {
                taskResponse.setAssigneeName(getEmployeeName(taskResponse.getAssignee()));
            } else {
                List<IdentityLink> idList = taskService.getIdentityLinksForTask(taskResponse.getId());
                List<String> nameList = new ArrayList<>();
                for (IdentityLink il : idList) {
                    if (il.getGroupId() != null) {
                        nameList.add(getGroupName(il.getGroupId()));
                    } else if (il.getUserId() != null) {
                        nameList.add(processApproveName(il.getUserId()));
                    }
                }
                taskResponse.setAssigneeName(StringUtils.join(nameList.toArray(), ";"));
            }
            ProcessInstance procInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(taskResponse.getProcessInstanceId()).list().iterator().next();
            taskResponse.setProcessName(procInstance.getProcessDefinitionName());

            taskResponse.setStartUserId(procInstance.getStartUserId());
            taskResponse.setStartUserName(getEmployeeName(procInstance.getStartUserId()));
            Date dueDate = taskResponse.getDueDate();
            if(dueDate != null) {
                Long dueTime = ActivitiUtils.secondsBetweenDate(taskResponse.getCreateTime(),dueDate);
                for (ICustomTaskProcessor processor : taskProcessors) {
                    taskResponse.setDueTime(processor.getDueTime(taskResponse.getCreateTime(),dueTime));
                    if (!processor.processorContinue()) {
                        break;
                    }
                }
            }
        }
        return dataResponse;
    }

    @Override
    public DataResponse queryProcessInstances(IRequest iRequest, HistoricProcessInstanceQueryRequest historicProcessInstanceQueryRequest, Map<String, String> requestParams, boolean showRetract) {
        DataResponse dataResponse = historicProcessInstanceQueryResource.queryProcessInstances(historicProcessInstanceQueryRequest, requestParams, fakeRequest);
        for (HistoricProcessInstanceResponseExt his : (List<HistoricProcessInstanceResponseExt>) dataResponse.getData()) {
            if (StringUtils.isNotEmpty(his.getStartUserId())) {
                his.setStartUserName(getEmployeeName(his.getStartUserId()));
            }
            if (StringUtils.isNotEmpty(his.getTaskDefKey())) {
                BpmnModel bpmnModel = repositoryService.getBpmnModel(his.getProcessDefinitionId());
                Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
                for (FlowElement flowElement : flowElements) {
                    if(his.getTaskDefKey().equals(flowElement.getId())){
                        his.setTaskName(flowElement.getName());
                        break;
                    }
                }
            }
            // TODO: 优化！
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(his.getId()).list();
            if (!tasks.isEmpty()) {
                String[] currentApprovers = new String[tasks.size()];
                for (int i = 0; i < tasks.size(); i++) {
                    currentApprovers[i] = getTaskApprove(tasks.get(i));
                }
                //设置当前审批人
                his.setCurrentApprover(StringUtils.join(currentApprovers, ","));
            }
            if(showRetract && his.getEndTime() == null){
                his.setRecall(isStartRecall(his.getId(), iRequest.getEmployeeCode()) || isTaskRecall(his.getId(), iRequest.getEmployeeCode()));
            }

        }
        return dataResponse;
    }

    /*@Override
    public DataResponse queryHistoricProcessInstance(IRequest iRequest, Map<String, String> params) {

        if ("involve".equalsIgnoreCase(params.get("queryType"))) {
            params.put("involvedUser", iRequest.getEmployeeCode());
            params.remove("startedBy");
        } else if ("create".equalsIgnoreCase(params.get("queryType"))) {
            params.put("startedBy", iRequest.getEmployeeCode());
            params.remove("involvedUser");
        } else if ("any".equalsIgnoreCase(params.get("queryType"))) {
            if (!isAdmin(iRequest.getEmployeeCode())) {
                throw new RuntimeException(new WflSecurityException(WflSecurityException.NEED_ASSIGNEE_OR_ADMIN));
            }
        }

        DataResponse dataResponse;
        List<HistoricProcessInstanceResponseExt> list;
        if ("true".equalsIgnoreCase(params.get("suspend"))) {
            Map<String, String> param = new HashMap<>();
            param.put("suspended", "true");
            dataResponse = processInstanceCollectionResource.getProcessInstances(param, fakeRequest);
            // List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().suspended().list();
            list = new ArrayList<>();
            for (ProcessInstanceResponseExt processInstance : (List<ProcessInstanceResponseExt>) dataResponse.getData()) {
                HistoricProcessInstanceResponseExt historicTaskInstanceExt = new HistoricProcessInstanceResponseExt();
                historicTaskInstanceExt.setId(processInstance.getId());
                historicTaskInstanceExt.setStartTime(processInstance.getStartTime());
                historicTaskInstanceExt.setStartUserName(processInstance.getStartUserId());
                historicTaskInstanceExt.setSuspended(true);
                historicTaskInstanceExt.setProcessName(processInstance.getProcessDefinitionName());
                historicTaskInstanceExt.setProcessDefinitionId(processInstance.getProcessDefinitionId());
                list.add(historicTaskInstanceExt);
            }
            dataResponse.setData(list);
        } else {
            dataResponse = historicProcessInstanceCollectionResource.getHistoricProcessInstances(params,
                    fakeRequest);
        }


        for (HistoricProcessInstanceResponseExt his : (List<HistoricProcessInstanceResponseExt>) dataResponse.getData()) {
            if (StringUtils.isNotEmpty(his.getStartUserId())) {
                his.setStartUserName(getEmployeeName(his.getStartUserId()));
            }
            //设置全局表单url
            String key = formService.getStartFormKey(his.getProcessDefinitionId());
            his.setFormKey(key);

            List<Task> tasks = taskService.createTaskQuery().processInstanceId(his.getId()).list();
            if (!tasks.isEmpty()) {
                if( tasks.get(0).getName() != null) {
                    StringBuilder taskName = new StringBuilder(tasks.get(0).getName());
                    his.setTaskName(taskName.toString());
                }
                StringBuilder currentApprover = new StringBuilder(getTaskApprove(tasks.get(0)));
                for (int i = 1; i < tasks.size(); i++) {
                    currentApprover = currentApprover.append(",").append(getTaskApprove(tasks.get(i)));
                }
                //设置当前审批人
                his.setCurrentApprover(currentApprover.toString());
                his.setRecall(isStartRecall(his.getId(), iRequest.getEmployeeCode()) || isTaskRecall(his.getId(), iRequest.getEmployeeCode()));
            }
            List<Execution> list1 = runtimeService.createExecutionQuery().processInstanceId(his.getId()).list();
            for (Execution ls : list1) {
                if (ls.isSuspended()) {
                    his.setSuspended(true);
                    break;
                }
            }
            // 最后审批人
           *//* List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(his.getId()).orderByHistoricTaskInstanceEndTime().desc().list();
            if (historicTaskInstanceList != null && !historicTaskInstanceList.isEmpty()) {
                for (int i = 0; i < historicTaskInstanceList.size(); i++) {
                    HistoricTaskInstanceResponseExt taskLastResponse = new HistoricTaskInstanceResponseExt(historicTaskInstanceList.get(i));
                    List<Comment> comments = getCommentOfType(taskLastResponse.getId(), COMMENT_ACTION);
                    if (comments == null) {
                        continue;
                    }
                    if (StringUtil.isNotEmpty(taskLastResponse.getAssignee())
                            && StringUtil.isNotEmpty(comments.get(comments.size() - 1).getFullMessage())) {
                        if (StringUtil.isNotEmpty(comments.get(comments.size() - 1).getUserId())) {
                            his.setLastApprover(getEmployeeName(comments.get(comments.size() - 1).getUserId()));
                            his.setLastApproverCode(comments.get(comments.size() - 1).getUserId());
                            his.setLastApproveAction(comments.get(comments.size() - 1).getFullMessage());
                        }
                        break;
                    }

                }
            }*//*
        }

        return dataResponse;
    }
*/
    @Override
    public DataResponse queryHistoricTaskInstances(IRequest iRequest, HistoricTaskInstanceQueryRequest queryRequest,
                                                   Map<String, String> allRequestParams) {
        if (allRequestParams == null) {
            allRequestParams = Collections.emptyMap();
        }

        List<HistoricActivityInstance> datas = historyService.createHistoricActivityInstanceQuery().processInstanceId(queryRequest.getProcessInstanceId()).orderByHistoricActivityInstanceStartTime().asc().list();

        DataResponse dataResponse = new DataResponse();
        List<HistoricTaskInstanceResponseExt> list = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : datas) {
            setHistoricActivityInstanceResponseExt(historicActivityInstance, list, queryRequest.getProcessInstanceId());
        }

        Iterator<HistoricTaskInstanceResponseExt> it = list.iterator();
        while (it.hasNext()) {
            HistoricTaskInstanceResponseExt i = it.next();
            if (null == i.getEndTime()) {
                it.remove();
            }
        }
        boolean key = false;
        HistoricActivityInstance endDom = null;
        for (HistoricActivityInstance ext : datas) {
            if (ext.getDeleteReason() != null) {
                key = true;
                endDom = ext;
                break;
            }
        }
        if (key) {
            HistoricTaskInstanceResponseExt ext = new HistoricTaskInstanceResponseExt();
            if (IActivitiConstants.ACT_RETRACT.equals(endDom.getDeleteReason())) {
                ext.setName("用户撤销");
            } else {
                ext.setName("管理员关闭");
            }
            ext.setAction("终止");
            ext.setEndTime(datas.get(datas.size() - 1).getEndTime());
            list.add(ext);
        }

        dataResponse.setData(list);
        return dataResponse;
    }

   /* protected boolean isAdmin(String userId) {
        return "ADMIN".equalsIgnoreCase(userId);
    }*/

    protected Task getTaskById(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new ActivitiObjectNotFoundException("Could not find a task with id '" + taskId + "'.", Task.class);
        }
        return task;
    }

    private void setHistoricActivityInstanceResponseExt(HistoricActivityInstance historicActivityInstance, List<HistoricTaskInstanceResponseExt> list, String processInstanceId) {
        if ("userTask".equals(historicActivityInstance.getActivityType())) {
            List<Comment> comments = getCommentOfType(historicActivityInstance.getTaskId(), PROP_COMMENT);
            List<Comment> actions = getCommentOfType(historicActivityInstance.getTaskId(), COMMENT_ACTION);
            if (comments != null && comments.size() != 0) {
                for (int index = comments.size() - 1; index >= 0; index--) {
                    HistoricTaskInstanceResponseExt historicTaskInstanceResponseExt = new HistoricTaskInstanceResponseExt(historicActivityInstance);
                    historicTaskInstanceResponseExt.setComment(comments.get(index).getFullMessage());
                    historicTaskInstanceResponseExt.setAction(actions.get(index).getFullMessage());
                    historicTaskInstanceResponseExt.setAssignee(actions.get(index).getUserId());
                    historicTaskInstanceResponseExt.setAssigneeName(actions.get(index).getUserId());
                    historicTaskInstanceResponseExt.setEndTime(actions.get(index).getTime());
                    list.add(historicTaskInstanceResponseExt);
                }
            }
            return;
        }
        HistoricTaskInstanceResponseExt historicTaskInstanceResponseExt = new HistoricTaskInstanceResponseExt(historicActivityInstance);
        if ("startEvent".equalsIgnoreCase(historicActivityInstance.getActivityType()) && StringUtil.isEmpty(historicActivityInstance.getActivityName())) {
            String startUser = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).list().get(0).getStartUserId();
            historicTaskInstanceResponseExt.setAssignee(startUser);
            historicTaskInstanceResponseExt.setAssigneeName(startUser);
            historicTaskInstanceResponseExt.setName("开始");
        }
        if ("endEvent".equalsIgnoreCase(historicActivityInstance.getActivityType()) && StringUtil.isEmpty(historicActivityInstance.getActivityName())) {
            historicTaskInstanceResponseExt.setName("结束");
        }

        if (!"exclusiveGateway".equals(historicActivityInstance.getActivityType()) && !"parallelGateway".equals(historicActivityInstance.getActivityType()) && null == historicActivityInstance.getDeleteReason()) {
            list.add(historicTaskInstanceResponseExt);
        }
    }


    protected List<Comment> getCommentOfType(String taskId, String type) {
        List<Comment> list = taskService.getTaskComments(taskId, type);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        autowireCapableBeanFactory.autowireBean(taskResource);
        autowireCapableBeanFactory.autowireBean(executionVariableCollectionResource);
        autowireCapableBeanFactory.autowireBean(taskQueryResource);
        autowireCapableBeanFactory.autowireBean(historicProcessInstanceQueryResource);
        autowireCapableBeanFactory.autowireBean(processInstanceCollectionResource);
        autowireCapableBeanFactory.autowireBean(historicTaskInstanceQueryResource);
        autowireCapableBeanFactory.autowireBean(historicProcessInstanceCollectionResource);
        autowireCapableBeanFactory.autowireBean(deadLetterJobCollectionResource);

        Map<String, ICustomTaskProcessor> listeners = applicationContext
                .getBeansOfType(ICustomTaskProcessor.class);
        taskProcessors = new ArrayList<>();
        taskProcessors.addAll(listeners.values());
        Collections.sort(taskProcessors);
    }

    @Override
    public Boolean isStartRecall(String procId,String employeeCode) {
        List<Comment> list = taskService.getProcessInstanceComments(procId);
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(procId).suspended().singleResult();
        String userId = historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult().getStartUserId();
        if (list.isEmpty() && null == processInstance && employeeCode.equalsIgnoreCase(userId)) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean isTaskRecall(String procId, String employeeCode) {
        //处于挂起或者结束状态的流程 不能撤回
        ProcessInstance suspendedProc = runtimeService.createProcessInstanceQuery().processInstanceId(procId).suspended().singleResult();
        if(suspendedProc!=null){
            return  false;
        }

        HistoricProcessInstance finishedProc = historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).finished().singleResult();
        if( finishedProc!=null){
            return false;
        }
        //加签的任务不能撤回
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(procId).list();
        if(tasks!=null){
            for(Task task:tasks){
                if(task.getOwner()!=null){
                    return false;
                }
            }
        }

        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(procId)
                .orderByHistoricActivityInstanceStartTime().asc().list();
        HistoricActivityInstance lastHistoricActivityInstance = historicActivityInstances.get(historicActivityInstances.size() - 1);

        //获取活动历史表最后一条记录的executionId   根据executionId找到execution的父execution 找出父execution的孩子execution
        String executionId = taskService.createTaskQuery().processInstanceId(procId).list().iterator().next().getExecutionId();
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(procId).executionId(executionId).singleResult();
        List<Execution> childExecutionList = runtimeService.createExecutionQuery().processInstanceId(procId).parentId(execution.getParentId()).list();
        //获取当前处于激活状态的执行器
        String activityId = lastHistoricActivityInstance.getActivityId();
        List<Execution> activeExecutionList = runtimeService.createExecutionQuery().processInstanceId(procId).activityId(activityId).list();
        //孩子execution>1 表示当前是多实例 比较孩子execution和激动状态的execution数量 如果数量不一致 表示多实例情况下 有人审批了 不允许撤回
        if (childExecutionList.size() > 1 && activeExecutionList.size() != childExecutionList.size()) {
            return false;
        }

        if ("userTask".equalsIgnoreCase(lastHistoricActivityInstance.getActivityType()) && lastHistoricActivityInstance.getEndTime() == null) {
            String multiActivityId="";
            boolean isLastApprove=true;
            for (int i = historicActivityInstances.size() - 1; i >= 0; i--) {
                HistoricActivityInstance historicActivityInstance = historicActivityInstances.get(i);
                String assignee = historicActivityInstance.getAssignee();
                String activityType = historicActivityInstance.getActivityType();
                if ("userTask".equalsIgnoreCase(activityType) &&historicActivityInstance.getEndTime() == null) {
                    continue;
                }
                if ("parallelGateway".equalsIgnoreCase(activityType) || "exclusiveGateway".equalsIgnoreCase(activityType)
                        || "eventBasedGateway".equalsIgnoreCase(activityType) || "inclusiveGateway".equalsIgnoreCase(activityType)) {
                    continue;
                }
                //最近审批的那个人
                if(isLastApprove){
                    //如果当前任务审批人与最近的一次任务审批人为同一人 获取最近审批的act_id
                    if ("userTask".equalsIgnoreCase(activityType) && assignee != null && assignee.equalsIgnoreCase(employeeCode)) {
                        multiActivityId = historicActivityInstance.getActivityId();
                        isLastApprove = false;
                        //连续两个任务都是同一人审批，且撤回了，最近一次审批可能是撤回操作
                        String deleteReson = historicActivityInstance.getDeleteReason();
                        if("jump".equalsIgnoreCase(deleteReson)){
                            return false;
                        }
                        continue;
                    }else{
                        return false;
                    }
                }
                //如果相同act_id 连续数量超过1 表示前面是个多实例任务 不允许撤回
                if(multiActivityId.equalsIgnoreCase(historicActivityInstance.getActivityId())){
                    return false;
                }else{
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void taskRecall(String procId, String employeeCode) {
        List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(procId)
                .orderByHistoricActivityInstanceStartTime().asc().list();
        String taskId = historicActivityInstances.get(historicActivityInstances.size() - 1).getTaskId();
        String activityId = "";
        for (int i = historicActivityInstances.size() - 1; i >= 0; i--) {
            HistoricActivityInstance historicActivityInstance = historicActivityInstances.get(i);
            String assignee = historicActivityInstance.getAssignee();
            String activityType = historicActivityInstance.getActivityType();
            if ("userTask".equalsIgnoreCase(activityType) && historicActivityInstance.getEndTime() == null) {
                continue;
            }
            if ("parallelGateway".equalsIgnoreCase(activityType) || "exclusiveGateway".equalsIgnoreCase(activityType)
                    || "eventBasedGateway".equalsIgnoreCase(activityType) || "inclusiveGateway".equalsIgnoreCase(activityType)) {
                continue;
            }
            if ("userTask".equalsIgnoreCase(activityType) && assignee.equalsIgnoreCase(employeeCode)) {
                activityId = historicActivityInstance.getActivityId();
            }
            break;
        }
        Authentication.setAuthenticatedUserId(employeeCode);
        taskService.addComment(taskId, procId, COMMENT_ACTION, RECALL);
        taskService.addComment(taskId, procId, PROP_COMMENT, employeeCode + "撤回审批");
        JumpActivityCmd cmd = new JumpActivityCmd(taskId, activityId);
        processEngineConfiguration.getCommandExecutor().execute(cmd);
    }

   /* private boolean hasRetractConfig(String processDefinitionId) {
        StartFormData startFormData = formService.getStartFormData(processDefinitionId);
        if (startFormData != null) {
            List<FormProperty> properties = startFormData.getFormProperties();
            for (FormProperty property : properties) {
                if (APPROVAL_RETRACT.equalsIgnoreCase(property.getId())) {
                    if ("Y".equalsIgnoreCase(property.getName())) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }*/


    public List<ProcessInstanceForecast> processInstanceForecast(String processInstanceId){
       /* ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();*/

        Map<String,List> history = new HashMap<>();
        //获取流程活动历史
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).
                activityType("userTask").orderByHistoricActivityInstanceEndTime().asc().list();
        String processDefinitionId = historicActivityInstanceList.get(0).getProcessDefinitionId();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            List<HistoricTaskInstanceResponseExt> list = new ArrayList<>();
            setHistoricActivityInstanceResponseExt(historicActivityInstance, list, processInstanceId);
            List value = history.get(historicActivityInstance.getActivityId());
            if(value != null && !value.isEmpty()){
                value.addAll(list);
            }else{
                value = list;
            }
            if(!value.isEmpty()) {
                history.put(historicActivityInstance.getActivityId(), value);
            }
        }
        List<ProcessInstanceForecast> processInstanceForecastList = new ArrayList<>();
        //获取当前活动节点execution
        List<Execution>executions =  runtimeService.createExecutionQuery().processInstanceId(processInstanceId).list();
        if(executions != null && ! executions.isEmpty()) {
            Execution execution = executions.get(executions.size() - 1);
            ForecastActivityCmd.executionId.set(execution.getId());
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        //从model中获取用户任务信息
        for (UserTask flowNode : bpmnModel.getProcesses().get(0).findFlowElementsOfType(UserTask.class)) {
            ProcessInstanceForecast forecast = new ProcessInstanceForecast();
            String key = flowNode.getId();
            forecast.setGraphicInfo(bpmnModel.getGraphicInfo(key));
            forecast.setTaskId(key);
            forecast.setTaskName(flowNode.getName());
            //判断当前节点是否审批过
            List historyInfo = history.get(key);
            if(historyInfo != null ){
                HistoricTaskInstanceResponseExt info = (HistoricTaskInstanceResponseExt) historyInfo.get(0);
                //如果是撤回操作，当前节点其实还未审批
                if(historyInfo.size() >1 || !"RECALL".equalsIgnoreCase(info.getAction())){
                    forecast.setExecuted(true);
                    forecast.setHistory(historyInfo);
                }
            }
            if(!forecast.isExecuted()) {
                ForecastActivityCmd.userTask.set(flowNode);
                Set<String> approve = (Set) processEngineConfiguration.getCommandExecutor().execute(forecastActivityCmd);
                if (approve == null || approve.isEmpty()) {
                    approve.add("不确定");
                }
                forecast.setForecast(approve);
            }
            processInstanceForecastList.add(forecast);
        }
        return processInstanceForecastList;
    }

    @Override
    public List<ActiviException> queryException(ActiviException exception, int page, int pagesize) {
        PageHelper.offsetPage(page, pagesize);
        List<ActiviException> list = exceptionMapper.selectAllException(exception);
        return list;
    }

    @Override
    public void executeTaskByAdmin(IRequest request, String procId, TaskActionRequestExt taskActionRequest) throws TaskActionException {
        Task task = taskService.createTaskQuery().processInstanceId(procId).singleResult();
        this.executeTaskAction(request, task.getId(), taskActionRequest,true);
    }

    private String processApproveName(String userId){
        return getEmployeeName(userId)+"("+userId+")";
    }

    private String getTaskApprove(Task task){
        if(StringUtils.isNotEmpty(task.getAssignee())) {
            return processApproveName(task.getAssignee());
        }else {
            List<IdentityLink> idList = taskService.getIdentityLinksForTask(task.getId());
            List<String> nameList = new ArrayList<>();
            for (IdentityLink il : idList) {
                if (il.getGroupId() != null) {
                    nameList.add(getGroupName(il.getGroupId()));
                } else if (il.getUserId() != null) {
                    nameList.add(processApproveName(il.getUserId()));
                }
            }
            return StringUtils.join(nameList.toArray(), ";");
        }
    }

}
