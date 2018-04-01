var config = {};
console.log(process.env.NODE_ENV);
config.cdnFilePath = '//hmall.files.saas.hand-china.com';
if(process.env.NODE_ENV == 'production'){
  /*正式环境配置*/
  config.apiGateway = '//hmall.api.saas.hand-china.com';
  config.authServer = '//hmall.api.saas.hand-china.com';
  config.cmsConfig = `${config.cdnFilePath}/config/cms-config.js`;
  config.piwikServer = 'https://172.20.1.33/piwik/';
}else if(process.env.NODE_ENV == 'test'){
  /*测试环境配置*/
  config.apiGateway = '//hmall.ut.saas.hand-china.com';
  config.authServer = '//hmall.ut.saas.hand-china.com';
  config.cmsConfig = `${config.cdnFilePath}/config/cms-config.js`;
  config.piwikServer = 'https://172.20.1.33/piwik/';
}else if(process.env.NODE_ENV == 'preview'){
  /*CMS预览环境配置*/
  config.apiGateway = '//172.30.2.129:8080/api';
  config.authServer = '//172.30.2.129:8080';
  config.cmsConfig = `${config.cdnFilePath}/config/cms-preview.js`;
}else if(process.env.NODE_ENV == 'idc'){
  /*IDC环境配置*/
  config.apiGateway = '//116.214.35.153/api';
  config.authServer = '//116.214.35.153';
  config.cdnFilePath = '//116.214.35.153/cdn';
  config.cmsConfig = `${config.cdnFilePath}/config/cms-config.js`;
}else if(process.env.NODE_ENV == 'cloud'){
  /*Azure环境配置*/
  config.apiGateway = '//42.159.246.236/api';
  config.authServer = '//42.159.246.236';
  config.cdnFilePath = '//116.214.35.153/cdn';
  config.cmsConfig = `${config.cdnFilePath}/config/cms-config.js`;
}else if(process.env.NODE_ENV == 'prod'){
  /*PROD环境配置*/
  config.apiGateway = '//dynamictest.uniqlo.cn';
  config.authServer = '//dynamictest.uniqlo.cn';
  config.cdnFilePath = '';
  config.cmsConfig = `${config.cdnFilePath}/config/cms-config.js`;
}else if(process.env.NODE_ENV == 'prod_idc'){
  /*IDC环境配置*/
  config.apiGateway = '//dynamictest.uniqlo.cn/api';
  config.authServer = '//dynamictest.uniqlo.cn';
  config.cdnFilePath = '//statictest.uniqlo.cn';
  config.cmsConfig = `${config.cdnFilePath}/config/cms-config.js`;
}else if(process.env.NODE_ENV == 'prod_azure'){
  /*Azure环境配置*/
  config.apiGateway = '//42.159.232.122/api';
  config.authServer = '//42.159.232.122';
  config.cdnFilePath = '//statictest.uniqlo.cn';
  config.cmsConfig = `${config.cdnFilePath}/config/cms-config.js`;
}else if(process.env.NODE_ENV == 'prod_preview'){
  /*CMS预览环境配置*/
  config.apiGateway = '//116.214.35.153/api';
  config.authServer = '//116.214.35.153';
  config.cdnFilePath = '//116.214.35.157';
  config.cmsConfig = `${config.cdnFilePath}/config/cms-preview.js`;
}else{
  /*开发环境配置*/
  config.apiGateway = '//hmall.api.saas.hand-china.com';
  config.authServer = '//hmall.api.saas.hand-china.com';
  config.cmsConfig = '/config/cms-config.js';
}
config.promoteService = `${config.apiGateway}/hmall-promote-server`;
config.bdService = `${config.apiGateway}/hmall-bd-service`;
config.odService = `${config.apiGateway}/hmall-od-service`;
config.urService = `${config.apiGateway}/hmall-ur-service`;
config.pdService = `${config.apiGateway}/hmall-pd-service`;
config.ctService = `${config.apiGateway}/hmall-ct-service`;
config.smsService = `${config.apiGateway}/hmall-sms-service`;
config.scService = `${config.apiGateway}/hmall-sc-service`;
config.stService = `${config.apiGateway}/hmall-st-service`;
config.droolsService = `${config.apiGateway}/hmall-drools-service`;
config.tpService = `${config.apiGateway}/hmall-thirdparty-service`;
config.commentService = `${config.apiGateway}/hmall-comment-service`;
config.sitemsgService = `${config.apiGateway}/hmall-sitemsg-service`;
config.fileService = `${config.apiGateway}/hmall-file-service`;
config.verificationService = `${config.apiGateway}/hmall-verification-service`;
module.exports = config;