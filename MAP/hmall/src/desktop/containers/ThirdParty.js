import React, { Component } from 'react';
import { browserHistory } from 'react-router';
/**
 * Created by Wuhuazhen on 2016/12/25.
 */
export default class ThirdParty extends Component {

  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  componentDidMount() {
    let { query } =  this.props.location,code_party = "",
      { code , auth_code ,redirect_url='/' ,type ,bind ,payForm} = query,
        access_token = Hmall.getCookie('access_token'),
        userId = Hmall.getCookie('userid');
    code_party = code?code:auth_code

    if(bind){
      fetch(`${tpService}/thirdParty/bandMobile2?access_token=${access_token}`,{
        method:"POST",
        headers: {
          'Content-Type': 'application/json'
        },
        body:JSON.stringify({
          code: code_party,
          userId: userId,
          type: type
        })
      })
          .then(Hmall.convertResponse('json',this))
          .then(json=>{
            if(json.success){
              this.getBinding(userId, access_token);
            }else{
              alert(json.msg || "连接超时")
            }
          })
          .catch(Hmall.catchHttpError())
    }else if(payForm){
      this.refs.pay_form.submit()
    }else{
      fetch(`${tpService}/thirdParty/login?code=${code_party}&type=${type}`)
          .then(response => response.json())
          .then(json=>{
            let { success, msgCode, resp }  = json;
            if (success) {
              localStorage.removeItem("url");
              let { expires_in, access_token } = resp[0];
              Hmall.login({
                access_token,
                expires_in,
                success: ()=>{
                  browserHistory.push(redirect_url);
                }
              });
            } else if (msgCode == 'THIRD_03') {
              let { openId , accessToken ,type} = resp[0]
              browserHistory.push({
                pathname: `/register.html`,
                state:{
                  redirect_url: redirect_url,
                  openId: openId,
                  accessToken: accessToken,
                  type: type
                }
              });
            }else if(msgCode == 'Time_Out'){
              alert("访问服务端接口超时。")
            }
          })
    }}

  getBinding(userId, access_token) {
    fetch(`${tpService}/thirdParty/getBanding?userId=${userId}&access_token=${access_token}`,{
      headers:Hmall.getHeader({})
    })
        .then(Hmall.convertResponse('json',this))
        .then(json=>{
          let { success ,resp ,msg } = json;
          if(success){
            browserHistory.push({
              pathname:`/account/binding.html`,
              state:{resp:resp}});
          }else{
            alert(msg || "连接超时")
          }
        })
        .catch(Hmall.catchHttpError());
  }

  render() {

    let access_token = Hmall.getCookie('access_token'),
        thirdPartyPay = "",
        orderInfo = localStorage.getItem("orderInfo"),
        success = false;

    if(orderInfo) {
      orderInfo = JSON.parse(orderInfo);
      var { orderId  , acoumt ,type} = orderInfo
      localStorage.removeItem("orderInfo");
      if(type == 2){
        thirdPartyPay = "unionpay";
      }else if(type == 3){
        thirdPartyPay = "alipay";
      }else if(type == 5){
        thirdPartyPay = "zhpay";
      }
      success = true
    }else{
      success = false
    }
    return (
        <div>
          {success?<form ref="pay_form" action={tpService+"/thirdParty/"+thirdPartyPay+"?access_token="+access_token} method="post">
            <input type="hidden" name="orderId" id="orderId" value={orderId}/>
            <input type="hidden" name="acoumt" id="acoumt" value={acoumt}/>
          </form>:""}
        </div>
    );
  }
}