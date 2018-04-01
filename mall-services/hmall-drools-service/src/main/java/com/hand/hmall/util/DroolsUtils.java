package com.hand.hmall.util;

import com.hand.hmall.model.ResourceWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;

import java.util.List;

/**
 * 动态生成kjar工具类
 *
 * @author tengliquan
 */
public class DroolsUtils {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private KieServices kieServices = KieServices.Factory.get();

    /**
     * 单个文件打包
     *
     * @param ks
     * @param releaseId       jar包的版本信息
     * @param resourceWrapper 文件资源
     * @return
     */
    public InternalKieModule createKieJarByStream(KieServices ks, ReleaseId releaseId, ResourceWrapper resourceWrapper) {
        KieFileSystem kfs = createKieFileSystemWithKProject(ks, true);
        //设置日期格式
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
        kfs.writePomXML(getPom(releaseId));
        kfs.write("src/main/resources/" + resourceWrapper.getTargetResourceName(), resourceWrapper.getResource());
        KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();

        if (kieBuilder.getResults().getMessages(Message.Level.ERROR).size() > 0) {
            List<Message> messages = kieBuilder.getResults().getMessages();
            StringBuffer errorRule = new StringBuffer();
            for (Message message : messages) {
                errorRule.append(message.getPath().replace(".drl", "")).append(",");
                logger.debug("ID : " + message.getPath().replace(".drl", ""));
            }
            IllegalStateException exception = new IllegalStateException(errorRule.toString());
            exception.getMessage();
            throw exception;
        }
        //生成对应促销的container，保存
        InternalKieModule kieModule = (InternalKieModule) kieBuilder.getKieModule();
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        KieSessionUtil.addActivityContainerMapping(releaseId.getArtifactId() + releaseId.getVersion(), kieContainer);
        kieContainer.updateToVersion(releaseId);
        return kieModule;
    }

    /**
     * 多个文件打包
     *
     * @param ks
     * @param releaseId
     * @param resourceWrappers
     * @return
     */
    public InternalKieModule createKieJar(KieServices ks, ReleaseId releaseId, List<ResourceWrapper> resourceWrappers) {
        KieFileSystem kfs = createKieFileSystemWithKProject(ks, true);
        //设置日期格式
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
        kfs.writePomXML(getPom(releaseId));
        for (ResourceWrapper resourceWrapper : resourceWrappers) {
            kfs.write("src/main/resources/" + resourceWrapper.getTargetResourceName(), resourceWrapper.getResource());
        }
        KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll();
        if (!kieBuilder.getResults().getMessages().isEmpty()) {
            List<Message> messages = kieBuilder.getResults().getMessages();
            StringBuffer errorRule = new StringBuffer();
            for (Message message : messages) {
                errorRule.append(message.getPath().replace(".drl", "")).append(",");
                System.out.println(message);
            }
            IllegalStateException exception = new IllegalStateException(errorRule.toString());
            exception.getMessage();
            throw exception;
        }
        InternalKieModule kieModule = (InternalKieModule) kieBuilder.getKieModule();
        return kieModule;
    }

    /**
     * 从内存中移除jar
     *
     * @param kieServices
     * @param releaseId
     */
    public void removeKJar(KieServices kieServices, ReleaseId releaseId) {
        KieRepository repository = kieServices.getRepository();
        KieModule kieModule = repository.getKieModule(releaseId);
        repository.removeKieModule(releaseId);
    }


    /**
     * 创建默认的kBase和kieSession
     *
     * @param ks
     * @param isdefault
     * @return
     */
    private KieFileSystem createKieFileSystemWithKProject(KieServices ks, boolean isdefault) {
        KieModuleModel kproj = ks.newKieModuleModel();
        KieBaseModel kieBaseModel1 = kproj.newKieBaseModel("KBase").setDefault(isdefault)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY).setEventProcessingMode(EventProcessingOption.STREAM);
        // Configure the KieSession.  
        kieBaseModel1.newKieSessionModel("KSession").setDefault(isdefault)
                .setType(KieSessionModel.KieSessionType.STATEFUL);
        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.writeKModuleXML(kproj.toXML());
        return kfs;
    }

    /**
     * 创建kjar的pom
     *
     * @param releaseId
     * @param dependencies
     * @return pom
     */
    private String getPom(ReleaseId releaseId, ReleaseId... dependencies) {
        String pom = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                + "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n"
                + "  <modelVersion>4.0.0</modelVersion>\n" + "\n" + "  <groupId>" + releaseId.getGroupId()
                + "</groupId>\n" + "  <artifactId>" + releaseId.getArtifactId() + "</artifactId>\n" + "  <version>"
                + releaseId.getVersion() + "</version>\n" + "\n";
        if (dependencies != null && dependencies.length > 0) {
            pom += "<dependencies>\n";
            for (ReleaseId dep : dependencies) {
                pom += "<dependency>\n";
                pom += "  <groupId>" + dep.getGroupId() + "</groupId>\n";
                pom += "  <artifactId>" + dep.getArtifactId() + "</artifactId>\n";
                pom += "  <version>" + dep.getVersion() + "</version>\n";
                pom += "</dependency>\n";
            }
            pom += "</dependencies>\n";
        }
        pom += "</project>";
        return pom;
    }

}