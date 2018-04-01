import cookie from 'react-cookie';
import { browserHistory } from 'react-router';

const KEY_BROWSER_HISTORY = 'browserHistory',
  KEY_ACCESS_TOKEN = 'access_token',
  KEY_COOKIE_OPTIONS = 'cookieOptions',
  KEY_LOGIN_DETAIL = 'loginDetail',
  KEY_USER_NAME = 'username',
  KEY_USER_ID = 'userid',
  KEY_USER_GROUP = 'usergroup',
  KEY_MOBILE = 'mobile',
  KEY_EMAIL = 'email';

let cachedScript = [],
  storage = {},
  timeoutIds = [],
  intervalIds = [],
  _setInterval = setInterval,
  _clearInterval = clearInterval,
  _setTimeout = setTimeout,
  _clearTimeout = clearTimeout,
  isUnMounted = (component) => {
    return component && component.isUnMounted;
  };
window.setInterval = function(callback, defer, component,...args){
  const id = _setInterval(()=> {
    if(isUnMounted(component)){
      clearInterval(id);
    }else if(typeof callback == 'string'){
      eval(callback);
    }else if(callback){
      callback();
    }
  }, defer,...args);
  intervalIds.push(id);
  return id;
};
window.clearInterval = function(id){
  _clearInterval(id);
  intervalIds.splice(intervalIds.indexOf(id),1);
};
window.setTimeout = function(callback, defer, component,...args){
  const id = _setTimeout(() => {
    if(!isUnMounted(component)){
      if(typeof callback == 'string'){
        eval(callback);
      }else if(callback){
        callback();
      }
    }
  }, defer, ...args);
  timeoutIds.push(id);
  return id;
};
window.clearTimeout = function(id){
  _clearTimeout(id);
  timeoutIds.splice(timeoutIds.indexOf(id),1);
};
export function clearIntervalAndTimeout(){
  intervalIds.forEach(id => _clearInterval(id));
  timeoutIds.forEach(id => _clearTimeout(id));
  intervalIds = [];
  timeoutIds = [];
}

export function getHeader(headers){
  headers = headers || {};
  const access_token = getCookie(KEY_ACCESS_TOKEN);
  if(access_token){
    headers.Authorization = 'bearer ' + access_token;
  }
  return headers;
}

export function setCookie(name,value,options) {
  if(name == KEY_ACCESS_TOKEN){
    localStorage.setItem(KEY_COOKIE_OPTIONS,JSON.stringify(options));
  }else if(!options){
    options = JSON.parse(localStorage.getItem(KEY_COOKIE_OPTIONS))||{
      path: '/'
    };
  }
  if(options.expires){
    options.expires = new Date(options.expires);
  }
  cookie.save(name,value,options);
}

export const getCookie = cookie.load;

export const removeCookie = cookie.remove;

export function login(props) {
  let { access_token, expires_in, groups, success, error } = props,
    expirationDate = Date.now() + expires_in * 1000 , 
    url = localStorage.getItem("url");
  setCookie(KEY_ACCESS_TOKEN, access_token, {
    path: '/',
    expires: expirationDate
  });
  fetch(`${authServer}/auth/user`,{
    headers: getHeader()
  })
  .then(convertResponse('json'))
  .then(res => {
    let { name, username, userId, groups, mobileNumber, email = '' } = res,
      loginDetail= localStorage.getItem(KEY_LOGIN_DETAIL);
    setCookie(KEY_USER_NAME, name || username);
    setCookie(KEY_USER_ID, userId);
    setCookie(KEY_USER_GROUP, groups);
    setCookie(KEY_MOBILE, mobileNumber);
    setCookie(KEY_EMAIL, email || '');
    if(loginDetail){
      loginDetail = JSON.parse(loginDetail);
      if(loginDetail.clickType == "buyNow"){
        localStorage.removeItem(KEY_LOGIN_DETAIL);
        let settlementArr = {
          "userId": userId,
          "creationTime": Date.now(),
          "products": [loginDetail],
        };
        fetch(`${odService}/order/createTempOrders`, {
          method: "post",
          headers: getHeader({
            "Content-Type": "application/json"
          }),
          body: JSON.stringify({
            map: settlementArr
          })
        })
        .then(convertResponse('json'))
        .then(json => {
          let {success , resp ,msgCode} = json
          if (success) {
            if (json.success) {
              browserHistory.push({pathname: '/payment/settlement.html', state: {tempIds: resp, flashCart: false}});
            } else {
              success && success();
            }
          }
        })
        .catch(catchHttpError(()=>{success && success();}));
      }else{
        browserHistory.push(loginDetail.redirect);
      }
    }else if(url){
      url = JSON.parse(localStorage.getItem("url"));
      if(url!="bind"){
        localStorage.removeItem("url");
        browserHistory.push(url);
      }else{
        success && success();
      }
    }else{
      success && success();
    }
  })
  .catch(catchHttpError(error));
}

export function logout() {
  removeCookie(KEY_ACCESS_TOKEN,{
    path: '/'
  });
  removeCookie(KEY_USER_NAME,{
    path: '/'
  });
  removeCookie(KEY_USER_ID,{
    path: '/'
  });
  removeCookie(KEY_USER_GROUP,{
    path: '/'
  });
  removeCookie(KEY_EMAIL,{
    path: '/'
  });
  removeCookie(KEY_MOBILE,{
    path: '/'
  });
  localStorage.removeItem(KEY_COOKIE_OPTIONS);
}

export function convertResponse(type,component,callback){
  return function(res){
    if(isUnMounted(component)){
      return new Promise((resolve, reject) => {
        res.abort = true;
        reject(res);
      });
    }
    callback && callback();
    if(res.ok){
      return res[type]();
    }else{
      return new Promise((resolve, reject) => {
        reject(res);
      });
    }
  }
}

export function catchHttpError(callback){
  return function(res){
    if(res.abort){
      return;
    }
    callback && callback(res);
    let { console } = window;
    if(res.status && res.status != 400){
      let reader = res.body.getReader(),
          received = [];
      reader.read()
          .then(function processResult(result) {
            if (result.done) {
              console && console.error(received.join(''));
              return;
            }
            received.push(String.fromCharCode.apply(null, result.value));
            return reader.read().then(processResult);
          });
      switch(res.status){
        case 401:
          const access_token = getCookie(KEY_ACCESS_TOKEN)
          if(!access_token){
            logout();
            confirm('登录已失效，是否重新登录？') && browserHistory.push('/login.html');
          }else{
            console && console.error('未授权请求');
          }
          break;
        case 403: console && console.error('服务器拒绝请求');break;
        case 404: console && console.error('服务器找不到请求的资源');break;
        case 415: console && console.error('不支持的媒体类型');break;
        case 500: console && console.error('服务器内部错误');break;
      }
    }else if(res.message){
      if(res.stack){
        console.error(res.stack);
      }
      console.error(res.message);
    }
  };
};
    
export function addBrowserHistory(history){
  let historys = getBrowserHistories(), index = -1;
  historys.some((item,i) => {
    if(item.productCode == history.productCode){
      index = i;
      return true;
    }
  });
  if(index != -1){
    historys.splice(index,1);
  }
  historys.unshift(history);
  if(historys.length > 30) {
    historys.pop();
  }
  localStorage.setItem(KEY_BROWSER_HISTORY,JSON.stringify(historys));
}

export function getBrowserHistories(){
  return JSON.parse(localStorage.getItem(KEY_BROWSER_HISTORY)) || [];
}

export function loadScript(src, callback, component) {
  if(cachedScript.indexOf(src) != -1){
    callback && callback();
  }else{
    let doc = document,
      script = doc.createElement('script');
    script.src = src;
    script.addEventListener('load', function(){
      cachedScript.push(src);
      if(!isUnMounted(component)){
        callback && callback();
      }
    }, false);
    doc.head.appendChild(script);
  }
}

export function loadBaiduMap(callback){
  if(!window.BMap) {
    const t = window.BMap_loadScriptTime = Date.now();
    loadScript(`//api.map.baidu.com/getscript?v=2.0&ak=${HmallConfig.baidu_map_ak}&services=&t=${t}`,...arguments);
  }else{
    callback && callback();
  }
}

export function loadIpAddress(){
  loadScript('//pv.sohu.com/cityjson?ie=utf-8',...arguments);
}


export function loadXiaoNeng(content){
  let { pathname} = location ,
      c = pathname.substring(0,3),
      { title } = document
  window.NTKF_PARAM = {
    siteid:"kf_9552",		            //企业ID，为固定值，必填
    settingid:"kf_9552_1492150519088",	//接待组ID，为固定值，必填
    uid:getCookie('userid')||"",		                //用户ID，未登录可以为空，但不能给null，uid赋予的值显示到小能客户端上
    uname:getCookie('username')||"",		    //用户名，未登录可以为空，但不能给null，uname赋予的值显示到小能客户端上
    isvip:"0",                          //是否为vip用户，0代表非会员，1代表会员，取值显示到小能客户端上
    userlevel:"1",		                //网站自定义会员级别，0-N，可根据选择判断，取值显示到小能客户端上
    erpparam:"abc",                      //erpparam为erp功能的扩展字段，可选，购买erp功能后用于erp功能集成
  }
  if(c=="/c/"){
    window.NTKF_PARAM.ntalkerparam = {}
    window.NTKF_PARAM.ntalkerparam.category = title
    window.NTKF_PARAM.ntalkerparam.brand = "优衣库"
  }
  switch (title){
    case "产品详情" : window.NTKF_PARAM.itemid = content , window.NTKF_PARAM.itemparam = "pc_Product" ;break;
    case "购物车" : window.NTKF_PARAM.ntalkerparam = content;break;
    case "结算页" : window.NTKF_PARAM.ntalkerparam = content;break;
    case "订单支付页" : window.NTKF_PARAM.orderid = content.orderid ,  window.NTKF_PARAM.orderprice = content.orderprice;break;
    case "支付成功页" : window.NTKF_PARAM.orderid = content.orderid ,  window.NTKF_PARAM.orderprice = content.orderprice;break;
  }
  loadScript('http://dl.ntalker.com/js/xn6/ntkfstat.js?siteid=kf_9552')
}
export function showXiaoNeng(){
  NTKF.im_openInPageChat('kf_9552_1492150519088');
}
export const Storage ={
  set: function(id,value) {
    storage[id] = value;
  },
  get: function(id) {
    return storage[id];
  }
};

export function inViewport(element) {
  if (element.offsetParent === null) {
    return false;
  }

  let top = window.pageYOffset,
    bottom = top + window.innerHeight,
    rect = element.getBoundingClientRect(),
    elTop = rect.top + top;

  return (
    top <= elTop + element.offsetHeight &&
    bottom >= elTop
  );
}

export function urlAppend(url, params) {
  return `${url}${url.indexOf('?') === -1?'?':'&'}${Object.keys(params).map( key => key + '=' + params[key]).join('&')}`;
}

export function cdnPath(path) {
  if(path) {
    return cdnFilePath + path;
  }
}

export function dialog(options) {
  const modal = Storage.get('modal-container');
  modal && modal.dialog(options);
}

export function clearModals() {
  const modal = Storage.get('modal-container');
  modal && modal.clear();
}