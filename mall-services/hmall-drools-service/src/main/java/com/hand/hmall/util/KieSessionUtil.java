package com.hand.hmall.util;

import com.hand.hmall.model.ResourceWrapper;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class KieSessionUtil {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // artifactId <--> Container映射表
    private static ConcurrentHashMap<String, KieContainer> activityContainerMapping = new ConcurrentHashMap<>();

//    // couponId + releaseId <--> Container映射表
//    private static ConcurrentHashMap<String, KieContainer> couponContainerMapping = new ConcurrentHashMap<>();


    public static KieSession getKieSession(KieServices kieServices, File file) throws Exception {
        KieFileSystem kfs = kieServices.newKieFileSystem();
        Resource resource = kieServices.getResources().newFileSystemResource(
                file);
        resource.setResourceType(ResourceType.DRL);
        kfs.write(resource);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
        if (kieBuilder.getResults().getMessages(Message.Level.ERROR).size() > 0) {
            throw new Exception();
        }
        KieContainer kieContainer = kieServices.newKieContainer(kieServices
                .getRepository().getDefaultReleaseId());
        KieBase kBase = kieContainer.getKieBase();
        return kBase.newKieSession();
    }

    public static KieSession kieSessionByStream(KieServices kieServices, ResourceWrapper r, String artifactId, String version) {
        KieContainer container = getKieContainerByCouponId(kieServices, r, artifactId, version);
        return container.newKieSession();

    }

    public static KieContainer getKieContainerByCouponId(KieServices kieServices, ResourceWrapper r, String couponId, String releaseId) {
        String lookupKey = couponId + releaseId;
        if (activityContainerMapping.get(lookupKey) == null) {
            ReleaseId rid = kieServices.newReleaseId("com.hand.hmall", couponId, releaseId);
            //先去KieRepository中查找KieJar，如果没有先编译KieJar
            if (null == kieServices.getRepository().getKieModule(rid)) {
                // 创建初始化的KieJar
                DroolsUtils droolsUtils = new DroolsUtils();
                InternalKieModule kJar = droolsUtils.createKieJarByStream(kieServices, rid, r);
                KieRepository repository = kieServices.getRepository();
                repository.addKieModule(kJar);
            }
            KieContainer kieContainer = kieServices.newKieContainer(rid);
            kieContainer.updateToVersion(rid);
            activityContainerMapping.put(lookupKey, kieContainer);
        }
        return activityContainerMapping.get(lookupKey);
    }

    public static KieSession getKieSession(KieServices kieServices, List<ResourceWrapper> r, String artifactId, String version) {
        ReleaseId releaseId = kieServices.newReleaseId("com.hand.hmall", artifactId, version);
        //先去KieRepository中查找KieJar，如果没有先编译KieJar
        if (null == kieServices.getRepository().getKieModule(releaseId)) {
            // 创建初始化的KieJar
            DroolsUtils droolsUtils = new DroolsUtils();
            InternalKieModule kJar = droolsUtils.createKieJar(kieServices, releaseId, r);
            KieRepository repository = kieServices.getRepository();
            repository.addKieModule(kJar);
        }
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        kieContainer.updateToVersion(releaseId);
        return kieContainer.newKieSession();
    }


    /**
     * 开启对应促销Id的会话
     *
     * @param kieServices
     * @param artifactId
     * @param version
     * @return
     */
    public static KieSession getKieSessionByActivityId(KieServices kieServices, String artifactId, String version) {
        KieContainer kieContainer = getKieContainerByActivityId(kieServices, artifactId, version);
        if (kieContainer == null)
            return null;
        return kieContainer.newKieSession();
    }

    /**
     * 根据促销活动Id获取对应的规则容器
     *
     * @param kieServices
     * @param artifactId
     * @param version
     * @return
     */
    public static KieContainer getKieContainerByActivityId(KieServices kieServices, String artifactId, String version) {
        String lookupKey = artifactId + version;
        if (activityContainerMapping.get(lookupKey) == null) {
            ReleaseId releaseId = kieServices.newReleaseId("com.hand.hmall", artifactId, version);
            if (null != kieServices.getRepository().getKieModule(releaseId)) {
                KieContainer kieContainer = kieServices.newKieContainer(releaseId);
                kieContainer.updateToVersion(releaseId);
                activityContainerMapping.put(lookupKey, kieContainer);
            } else {
                return null;
            }
        }
        return activityContainerMapping.get(lookupKey);
    }

    public static void addActivityContainerMapping(String key, KieContainer container) {
        activityContainerMapping.put(key, container);
    }


    public static void removeActivityContainerMapping(String key) {
        activityContainerMapping.remove(key);
    }

}
