import React, { Component } from 'react';
import { Panel,Row,Col } from '../../components/Layout';
import Button from '../../components/Button';
import Icon from '../../components/Icon';

export default class Binding extends Component{
  componentWillUnmount(){
    this.isUnMounted = true;
  }

  componentWillMount(){
    let{ state } = this.props.location,
      access_token = Hmall.getCookie('access_token'),
      userId = Hmall.getCookie('userid');
    if(state){
      let { binds } = this.state , { resp }  = state;
      resp.map(r => {
        Object.assign(binds.find(b => b.type == r.type), {
          state: r.figureurl,
          ID: r.nickname,
          openId: r.openId
        });
      });
      this.setState({ fetch_status: "init", binds })
    }else{
      this.getBinding(userId, access_token);
    }
  }
  
  getBinding(userId, access_token) {
    fetch(`${tpService}/thirdParty/getBanding?userId=${userId}&access_token=${access_token}`,{
      headers:Hmall.getHeader({})
    })
    .then(Hmall.convertResponse('json',this))
    .then(json=>{
      let { success ,resp ,msg } = json;
      if(success){
        let { binds } = this.state;
        resp.map(r => {
          Object.assign(binds.find(b => b.type == r.type), {
            state: r.figureurl,
            ID: r.nickname,
            openId: r.openId
          });
        });
        this.setState({ fetch_status: "init", binds })
      }else{
        alert(msg || "连接超时")
      }
    })
    .catch(Hmall.catchHttpError(()=>{
      this.setState({
        fetch_status: 'error'
      });
    }));
  }
  
  constructor(props){
    super(props);
    this.state = {
      success: false,
      binds :[
        {type:2, icon: "wechat", name: "微信", state: "未绑定", ID: "", openId: ""},
        {type:4, icon: "alipay", name: "支付宝", state: "未绑定", ID: "", openId: ""},
        {type:1, icon: "qq", name: "QQ", state: "未绑定", ID: "", openId: ""},
        {type:3, icon: "weibo", name: "微博", state: "未绑定", ID: "", openId: ""}
      ],
      fetch_status: 'uninit',
    }
  }
  /*第三方账号绑定*/
  handleClickBind(type){
    let { protocol, host, hostname } = location,
      params = {
        redirect_uri: encodeURIComponent(`${protocol}//${hostname == 'localhost' ? 'hhmall.saas.hand-china.com' : host}/thirdParty.html?type=${type}&bind=true`)
      };
    if(type == 5){
      location.href = Hmall.urlAppend(HmallConfig.login_taobao, params);
    }else if(type == 4){
      fetch(`${tpService}/thirdParty/getAliPayAuthCode`,{
        method:"get",
        headers: {
          'Content-Type': 'application/json'
        }
      })
      .then(Hmall.convertResponse('json',this))
      .then(json=>{
        let { resp , success } = json
        if(success){
          let url = "bind"
          localStorage.setItem("url", JSON.stringify(url));
          location.href = resp
        }
      })
      .catch(Hmall.catchHttpError())
      //location.href = Hmall.urlAppend(HmallConfig.login_alipay, params);
    }else if(type == 3){
      location.href = Hmall.urlAppend(HmallConfig.login_weibo, params);
    }else if(type == 2){
      location.href = Hmall.urlAppend(HmallConfig.login_wechat, params);
    }else if(type == 1){
      location.href = Hmall.urlAppend(HmallConfig.login_qq, params);
    }
  }
  /*解除绑定*/
  handleOutBind(openId,type){
    let userId = Hmall.getCookie('userid'),access_token = Hmall.getCookie('access_token');
    fetch(`${tpService}/thirdParty/cancelBand?access_token=${access_token}`,{
      method:"POST",
      headers: {
        'Content-Type': 'application/json'
      },
      body:JSON.stringify({
        openId,
        userId,
        type
      })
    })
    .then(Hmall.convertResponse('json',this))
    .then(json=>{
      if(json.success){
        let { binds } = this.state;
        Object.assign(binds.find(b => b.type == type), {
          state: "未绑定",
          ID: "",
          openId: ""
        });
        this.setState({ binds });
      }else{
        alert("解绑失败");
      }
    })
    .catch(Hmall.catchHttpError(e=>{
      alert("解绑失败");
    }));
  }
  //账号信息
  renderBinds(){
    return this.state.binds.map(b => {
      let { type } = b;
      return <tr key={type}>
        <td><Icon name={b.icon} className={b.state=="未绑定" ? 'disabled' : null}></Icon>{b.name}</td>
        {
          b.state=="未绑定" ? <td>未绑定</td> : 
            <td><img src={b.state}></img>{b.ID}</td>
        }
        <td>{
          b.state=="未绑定" ? <Button text="绑定" width={70} height={30} onClick={()=>{this.handleClickBind(type)}}></Button> :
            <span className="cancel" onClick={()=>{this.handleOutBind(b.openId,type)}}>解绑</span>
        }</td>
        <td></td>
      </tr>;
    });
  }
  render() {
    let { fetch_status } = this.state
    return (
      <div className="binding">
        {
          (() => {
            switch(fetch_status){
              case 'uninit': return <div className="loading"></div>;
              case 'init': return <table cellSpacing={0}>
                <tbody>
                {this.renderBinds()}
                </tbody>
              </table>;
              case 'error':return <h1 className="error">网页出错</h1>
            }
          })()
        }
      </div>
    );
  }
}