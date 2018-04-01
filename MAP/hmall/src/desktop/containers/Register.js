import React, { Component } from 'react';
import { Link, browserHistory } from 'react-router';
import { Panel, Row, Col } from '../components/Layout';
import { TabPanel, Tab } from '../components/Tab';
import Form, { FormGroup } from '../components/Form';
import TextBox from '../components/TextBox';
import Password from '../components/Password';
import Button from '../components/Button';
import CodeButton from '../components/CodeButton';
import Logo from '../components/Logo';
import SliderVerification from '../components/SliderVerification';
import CheckBox from '../components/CheckBox';

const message = {
  'UR_MOBILE_02': '手机格式不正确，请重新输入',
  'UR_MOBILE_03': <span>当前手机号已注册，请 <Link to="/login.html">登录</Link></span>,
  'UR_PASSWORD_02': '两次密码输入不一致，请重新输入',
  'UR_PASSWORD_07': '密码不能为空',
  'UR_PASSWORD_08': '确认密码不能为空',
  'UR_VERIF_01': '滑块验证不通过',
  'MSG_01': '验证码码输入错误次数大于3次',
  'MSG_02': '验证码不能为空',
  'MSG_03': '验证码错误，请重新输入',
  'THIRD_F_06': '手机格式不正确，请重新输入',
  'THIRD_F_08': '验证码输入错误次数大于3次',
  'THIRD_F_09': '验证码错误,请重新输入',
  'THIRD_F_10': '两次输入密码不一致,请重新输入',
  'THIRD_F_13': <span>当前手机号已注册，请 <Link to="/login.html">登录</Link></span>,
}

export default class Register extends Component{
  
  constructor(props) {
    super(props);
    this.registeredPhone = {};
    this.state = {
      phone_number: '',
      accept: true,
      retry: 0,
      verification_key: '',
      registering: false,
      phone_error: '',
      phone_has_registered_validing: false,
      code_error: '',
      slider_error: '',
      password_error: '',
      confirm_error: '',
      verf: true
    }
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  canRegister() {
    let { accept, verf, registering} = this.state;
    return verf && !registering && accept;
  }
  
  clearError() {
    this.setState({
      phone_error: '',
      code_error: '',
      password_error: '',
      slider_error: '',
      confirm_error: ''
    });
  }
  
  handleSubmit(e) {
    e.preventDefault();
    let { mobilephone, code, password, password2 } = e.target,
      { location } = this.props,
      { state, query, search } = location, 
      v1 = password.value,
      v2 = password2.value,
      callback = () => this.setState({registering: false});
    if(this.canRegister()){
      this.clearError();
      this.setState({registering: true});
      if(state){
        let { openId , accessToken , type } =  state;
        fetch(`${tpService}/thirdParty/bandMobile`,{
          method: 'post',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            password: v1 && Crypto.MD5(v1),
            passwordConfirm: v2 && Crypto.MD5(v2),
            accessToken:accessToken,
            openId: openId,
            phone: mobilephone.value,
            t: String(Date.now()),
            msg: code.value,
            type: type
          })
        })
        .then(response => response.json())
        .then(json=>{
          let { success, msgCode, resp ,msg} = json;
          if(success){
            let { expires_in, access_token } = resp[0],
              { redirect_url = '/' } = state;
            Hmall.login({
              access_token,
              expires_in,
              success: () => {
                browserHistory.push(redirect_url);
              },
              error: callback
            });
          }else{
            if(msgCode == 'THIRD_F_12'){
              let { protocol, host, hostname } = location,
                params = {
                  redirect_uri: encodeURIComponent(`${protocol}//${hostname == 'localhost' ? 'hhmall.saas.hand-china.com' : host}/thirdParty.html?type=${type}&redirect_url=${search}`)
                };
              if(type == 5){
                location.href = Hmall.urlAppend(HmallConfig.login_taobao, params);
              }else if(type == 4){
                location.href = Hmall.urlAppend(HmallConfig.login_alipay, params);
              }else if(type == 3){
                location.href = Hmall.urlAppend(HmallConfig.login_weibo, params);
              }else if(type == 2){
                location.href = Hmall.urlAppend(HmallConfig.login_wechat, params);
              }else if(type == 1){
                location.href = Hmall.urlAppend(HmallConfig.login_qq, params);
              }
            }else if(msgCode == 'THIRD_F_06'){
              this.setState({
                phone_error: message[msgCode]
              });
            }else if(msgCode == 'THIRD_F_08'){
              let { time, key } = resp[0];
              this.setState({
                code_error: message[msgCode],
                retry: time,
                verification_key: key,
                verf: !key
              });
            }else if(msgCode == 'THIRD_F_09'){
              this.setState({
                code_error: message[msgCode],
                retry: resp[0].time
              });
            }else if(msgCode == 'THIRD_F_10'){
              this.setState({
                password_error: message[msgCode]
              });
            }else if(msgCode == 'THIRD_F_13'){
              this.setState({
                phone_error: message[msgCode]
              });
            }
            callback();
          }
        })
        .catch(Hmall.catchHttpError(callback));
      }else{
        fetch(`${urService}/register`,{
          method: 'post',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            mobileNumber: mobilephone.value,
            msg: code.value,
            pwd: v1 && Crypto.MD5(v1),
            vldPsd: v2 && Crypto.MD5(v2),
            t: String(Date.now())
          })
        })
        .then(Hmall.convertResponse('json',this))
        .then(json => {
          let { success, msgCode, resp } = json;
          if(success){
            let { expires_in, access_token } = resp[0],
              { redirect_url = '/' } = query;
            Hmall.login({
              access_token,
              expires_in,
              success: () => {
                browserHistory.push(redirect_url);
              },
              error: callback
            });
          }else{
            if(msgCode == 'UR_MOBILE_02' || msgCode == 'UR_MOBILE_03'){
              this.setState({
                phone_error: message[msgCode]
              });
            }else if( msgCode == 'UR_PASSWORD_07'){
              this.setState({
                password_error: message[msgCode]
              });
            }else if(msgCode == 'UR_PASSWORD_02' || msgCode == 'UR_PASSWORD_08'){
              this.setState({
                confirm_error: message[msgCode]
              });
            }else if(msgCode == 'MSG_01' || msgCode == 'MSG_03'){
              let { time, key } = resp[0];
              this.setState({
                code_error: message[msgCode],
                retry: time,
                verification_key: key,
                verf: !key
              });
            }else if(msgCode == 'MSG_02'){
              this.setState({
                code_error: message[msgCode]
              });
            }else if(msgCode == 'UR_VERIF_01'){
              this.setState({
                slider_error: message[msgCode]
              });
            }
            callback();
          }
        })
        .catch(Hmall.catchHttpError(callback));
      }
    }
  }
  
  validRegister(value){
    if(this.registeredPhone[value]){
      this.setState({
        phone_error: message['UR_MOBILE_03']
      });
    }else{
      this.setState({phone_has_registered_validing: true});
      let callback = () => this.setState({phone_has_registered_validing: false});
      fetch(`${urService}/mobile/${value}?t=${Date.now()}`)
      .then(Hmall.convertResponse('json', this, callback))
      .then(json => {
        let { success, msgCode, resp } = json;
        if(success){
          this.setState({
            phone_error: '',
            retry: resp[0].time,
            verification_key: resp[0].key,
            verf: !resp[0].key
          });
        }else{
          if(msgCode == 'UR_MOBILE_02'){
            this.setState({
              phone_error: message[msgCode]
            });
          }else if(msgCode == 'UR_MOBILE_03'){
            this.setState({
              phone_error: message[msgCode]
            });
            this.registeredPhone[value] = true;
          }
        }
      })
      .catch(Hmall.catchHttpError(callback))
    }
  }
  
  validatePhone(e){
    let { value } = e.target,
      reg = HmallConfig.mobilephone_regx,
      valid = reg.test(value);
    this.setState({
      phone_number: value,
      phone_error: valid ? '' : message['UR_MOBILE_02']
    });
    if(valid && !this.props.location.state){
      this.validRegister(value);
    }
  }
  
  validatePassword(e) {
    let { target } = e,
      { form} = target,
      { password, password2 } = form,
      v1 = password.value,
      v2 = password2.value;
      if(!v1){
        this.setState({password_error: message['UR_PASSWORD_07']});
      }else if(v2 && v1 != v2 ){
        this.setState({
          confirm_error: message['UR_PASSWORD_02'],
          password_error: ''
        });
      }else{
        this.setState({
          password_error: '',
          confirm_error: ''
        });
      }
  }
  validatePassword2(e) {
    let { target } = e,
      { form} = target,
      { password, password2 } = form,
      v1 = password.value,
      v2 = password2.value;
    if(!v2){
      this.setState({confirm_error: message['UR_PASSWORD_08']});
    }else if(v1 && v1 != v2 ){
      this.setState({confirm_error: message['UR_PASSWORD_02']});
    }else{
      this.setState({confirm_error: ''});
    }
  }
  
  validateCode(e) {
    let { value } = e.target.form.code;
    if(!value) {
      this.setState({code_error: message['MSG_02']});
    }else {
      this.setState({code_error: ''});
    }
  }
  
  showSliderVerification() {
    let { phone_number, phone_error, slider_error, retry, verification_key } = this.state;
    return !phone_error && retry > 2 && verification_key &&
      <FormGroup>
        <SliderVerification onValid={() => this.setState({ slider_error: '',verf: true })} onInValid={() => this.setState({ slider_error: message['UR_VERIF_01'] })} phoneNumber={phone_number} secretKey={verification_key}></SliderVerification> 
        {this.showErrorMsg(slider_error)}
      </FormGroup>;
  }
  
  showErrorMsg(password_error) {
    return password_error ? <p className="error-msg" >{password_error}</p> : null
  }
  
  render() {
    let { phone_number, phone_error, code_error, phone_has_registered_validing, accept, password_error, confirm_error, registering} = this.state;
    let { state} =  this.props.location
    return (
      <div className="form-wrap">
        <Panel>
          <Row className="title">
            <Col width={50}><Logo href="/"></Logo></Col>
            <Col>{state!=null?(<h2>手机绑定</h2>):<h2>会员注册</h2>}</Col>
          </Row>
          <Form onSubmit={(e) => this.handleSubmit(e)}>
            <FormGroup>
              <TextBox name="mobilephone" placeholder="手机号" onBlur={e => this.validatePhone(e)}></TextBox>
            </FormGroup>
            {this.showErrorMsg(phone_error)}
            <FormGroup>
              <TextBox onBlur={e => this.validateCode(e)} name="code" placeholder="验证码" width={138}></TextBox>
              <CodeButton disabled={!!phone_error || phone_has_registered_validing} phone={phone_number} width={98} height={36}></CodeButton>
            </FormGroup>
            {this.showErrorMsg(code_error)}
            {this.showSliderVerification()}
            <FormGroup>
              <Password name="password" placeholder="设置密码" onBlur={e => this.validatePassword(e)}></Password>
            </FormGroup>
            {this.showErrorMsg(password_error)}
            <FormGroup>
              <Password name="password2" placeholder="确认密码" onBlur={e => this.validatePassword2(e)}></Password>
            </FormGroup>
            {this.showErrorMsg(confirm_error)}
            <FormGroup>
              <CheckBox value={accept?'Y':'N'} name="accept" onChange={e => this.setState({accept: e.target.value == 'Y'})}></CheckBox><p>我已阅读并同意<a href="">《注册用户协议》</a></p>
            </FormGroup>
            <FormGroup align="center">
              <Button type="submit" text={state ? "手机绑定" : "注册"} disabled={!this.canRegister()} className="red" height={44}></Button>
            </FormGroup>
          </Form>
        </Panel>
      </div>
    );
  }
}