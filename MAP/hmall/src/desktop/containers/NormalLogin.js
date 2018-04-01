import React, { Component } from 'react';
import { Link, browserHistory } from 'react-router';
import Form, { FormGroup } from '../components/Form';
import TextBox from '../components/TextBox';
import Password from '../components/Password';
import Button from '../components/Button';
import Icon from '../components/Icon';
import SliderVerification from '../components/SliderVerification';

const message = {
  'UR_MOBILE_01': <span>尚未注册账号，请先 <Link to="/register.html">注册</Link></span>,
  'UR_MOBILE_02': '手机格式不正确，请重新输入',
  'UR_LOGIN_01': <span>尚未注册账号，请先 <Link to="/register.html">注册</Link></span>,
  'UR_PASSWORD_03': '密码错误，请重新输入',
  'UR_PASSWORD_04': '密码输入错误次数大于3次',
  'UR_PASSWORD_07': '密码不能为空',
  'UR_VERIF_01': '滑块验证不通过',
  'MSG_01': '验证码码输入错误次数大于3次',
  'MSG_02': '验证码不能为空',
  'MSG_03': '验证码错误，请重新输入'
}

export default class NormalLogin extends Component{
  
  constructor(props) {
    super(props);
    this.registeredPhone = {};
    this.state = {
      phone_error: '',
      slider_error: '',
      password_error: '',
      code_error: '',
      logining: false,
      retry: 0,
      verification_key: '',
      verf: true
    }
  }
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  getSubmitBody(v1,v2) {
    return{
      'customerId': v1,
      'pwd': v2 && Crypto.MD5(v2),
      't': String(Date.now())
    }
  }
  
  getLoginUrl() {
    return `${urService}/login/normal`;
  }
  
  getMobileVarifyUrl(value){
    return `${urService}/login/check/${value}?t=${Date.now()}`;
  }
  
  canLogin() {
    let { verf, logining } = this.state;
    return  verf && !logining;
  }
  
  clearError() {
    this.setState({
      phone_error: '',
      code_error: '',
      password_error: '',
      slider_error: ''
    });
  }
  
  handleSubmit(e){
    e.preventDefault();
    let { user_name, password } = e.target,
      callback = () => this.setState({logining: false});
    if(this.canLogin()){
      this.clearError();
      this.setState({
        logining: true
      });
      fetch(this.getLoginUrl(),{
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.getSubmitBody(user_name.value, password.value))
      })
      .then(Hmall.convertResponse('json',this))
      .then(json => {
        let { success, msgCode, resp }  = json
        if(success){
          let { expires_in, access_token } = resp[0],
            { redirect_url = '/' } = this.props.location.query;
          Hmall.login({
            access_token,
            expires_in,
            success: () => {
              browserHistory.push(redirect_url);
            },
            error: callback
          });
        }else {
          if(msgCode == 'UR_MOBILE_01'){
            this.setState({
              phone_error: message[msgCode]
            });
          }else if(msgCode == 'UR_MOBILE_02'){
            this.setState({
              phone_error: message[msgCode]
            });
          }else if(msgCode == 'UR_PASSWORD_03'){
            this.setState({
              password_error: message[msgCode],
              retry: resp[0].time
            });
          }else if(msgCode == 'MSG_03'){
            this.setState({
              code_error: message[msgCode],
              retry: resp[0].time
            });
          }else if(msgCode == 'MSG_02'){
            this.setState({
              code_error: message[msgCode]
            });
          }else if(msgCode == 'UR_PASSWORD_04'){
            let { time, key } = resp[0];
            this.setState({
              password_error: message[msgCode],
              retry: time,
              verification_key: key,
              verf: !key
            });
            this.registeredPhone[user_name.value] = key;
          }else if(msgCode == 'MSG_01'){
            let { time, key } = resp[0];
            this.setState({
              code_error: message[msgCode],
              retry: time,
              verification_key: key,
              verf: !key
            });
            this.registeredPhone[user_name.value] = key;
          }else if(msgCode == 'UR_VERIF_01'){
            this.setState({
              slider_error: message[msgCode]
            });
          }
          callback();
        }
      })
      .catch(Hmall.catchHttpError(callback))
    }
  }
  
  validRegister(value){
    let secretKey = this.registeredPhone[value];
    if(!secretKey){
      this.setState({phone_has_registered_validing: true});
      let callback = () => this.setState({phone_has_registered_validing: false});
      fetch(this.getMobileVarifyUrl(value))
      .then(Hmall.convertResponse('json', this, callback))
      .then(json => {
          let { msgCode, resp } = json;
          if(msgCode == 'UR_MOBILE_01' || msgCode == 'UR_LOGIN_01'){
            this.setState({
              phone_error: message[msgCode]
            });
          }else if(msgCode == 'UR_MOBILE_02'){
            this.setState({
              phone_error: message[msgCode]
            });
          }else if(msgCode == 'UR_LOGIN_02' || msgCode == 'UR_MOBILE_03'){
            let { time, key } = resp[0];
            this.setState({
              password_error: time > 2 && msgCode == 'UR_LOGIN_02' ? message['UR_PASSWORD_04'] : '',
              code_error:  time > 2 && msgCode == 'UR_MOBILE_03' ? message['MSG_01'] : '',
              retry: time,
              verification_key: key,
              verf: !key
            });
            this.registeredPhone[value] = key;
          }
      })
      .catch(Hmall.catchHttpError(callback))
    }else if(secretKey != true){
      this.setState({
        retry: 3,
        verification_key: secretKey,
        verf: false
      })
    }else{
      this.setState({
        retry: 0,
        verification_key: '',
        verf: true
      })
    }
  }
  
  validUserName(e) {
    let { value } = e.target;
    if(value){
      this.setState({
        phone_number: value,
        phone_error: ''
      });
      this.validRegister(value);
    }else{
      this.setState({
        phone_number: '',
        phone_error: '手机号 / ID不能为空'
      });
    }
  }
  
  validatePassword(e) {
    this.setState({ 
      password_error: e.target.form.password.value ? '' : message['UR_PASSWORD_07']
    });
  }
  
  showSliderVerification() {
    let { retry, phone_number,phone_error, slider_error, verification_key } = this.state;
    return !phone_error && retry > 2 && verification_key &&
      <FormGroup>
        <SliderVerification onValid={() => this.setState({ slider_error: '',verf: true })} onInValid={() => this.setState({ slider_error: message['UR_VERIF_01'] })} phoneNumber={phone_number} secretKey={verification_key}></SliderVerification> 
        {this.showErrorMsg(slider_error)}
      </FormGroup>;
  }
  
  showErrorMsg(error_msg) {
    return error_msg ? <p className="error-msg" >{error_msg}</p> : null
  }
  
  render() {
    let { phone_error, password_error } = this.state;
    return (
      <Form onSubmit={e => this.handleSubmit(e)}>
        <FormGroup>
          <TextBox name="user_name" placeholder="手机号 / ID" onBlur={e => this.validUserName(e)}>
            <Icon name="username"></Icon>
          </TextBox>
          { this.showErrorMsg(phone_error) }
        </FormGroup>
        <FormGroup>
          <Password name="password" onBlur={e => this.validatePassword(e)} placeholder="密码">
            <Icon name="password"></Icon>
          </Password>
        </FormGroup>
        {this.showErrorMsg(password_error)}
        {this.showSliderVerification()}
        <FormGroup align="right">
          <Link to="/password-find.html">忘记密码</Link>
        </FormGroup>
        <FormGroup>
          <Button disabled={!this.canLogin()} type="submit" text="登录" className="red" height={44}></Button>
        </FormGroup>
      </Form>
    );
  }
}