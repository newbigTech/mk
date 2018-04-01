import React, { Component } from 'react';
import { Link, browserHistory } from 'react-router';
import { Panel, Row, Col } from '../components/Layout';
import Form, { FormGroup } from '../components/Form';
import { TabPanel, Tab } from '../components/Tab';
import TextBox from '../components/TextBox';
import Button from '../components/Button';
import CodeButton from '../components/CodeButton';
import SliderVerification from '../components/SliderVerification';

const message = {
  'UR_MOBILE_01': '未找到该手机号关联的账户',
  'UR_MOBILE_02': '手机格式不正确，请重新输入',
  'UR_VERIF_01': '滑块验证不通过',
  'MSG_01': '验证码码输入错误次数大于3次',
  'MSG_02': '验证码不能为空',
  'MSG_03': '验证码错误，请重新输入',
  'UR_EMAIL_01': '该邮箱未绑定用户账户',
  'UR_EMAIL_06': '邮箱格式不正确，请重新输入'
}

export default class PasswordFind extends Component{
  
  constructor(props) {
    super(props);
    this.registeredPhone = {};
    this.state = {
      phone_number: '',
      email: '',
      retry: 0,
      verification_key: '',
      other_is_empty: true,
      email_error: '',
      phone_error: '',
      phone_has_registered_validing: false,
      code_error: '',
      slider_error: '',
      email_send_success: false,
      verf: true
    }
  }
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  validRegister(value){
    let secretKey = this.registeredPhone[value];
    if(!secretKey){
      this.setState({phone_has_registered_validing: true});
      let callback = () => this.setState({phone_has_registered_validing: false});
      fetch(`${urService}/mobile/${value}?t=${Date.now()}`)
      .then(Hmall.convertResponse('json', this, callback))
      .then(json => {
        let { msgCode, resp } = json;
        if(msgCode == 'UR_MOBILE_01'){
          this.setState({
            phone_error: message[msgCode]
          });
        }else if(msgCode == 'UR_MOBILE_02'){
          this.setState({
            phone_error: message[msgCode]
          });
        }else if(msgCode == 'UR_MOBILE_03'){
          let { time, key } = resp[0];
          this.setState({
            code_error:  time > 2 ? message['MSG_01'] : '',
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
  
  validatePhone(e){
    let { value } = e.target,
      reg = HmallConfig.mobilephone_regx,
      valid = reg.test(value);
    this.setState({
      phone_number: value,
      phone_error: valid ? '' : '手机格式不正确，请重新输入'
    });
    if(valid){
      this.validRegister(value);
    }
  }
  
  validateEmail(e) {
    let { value } = e.target,
      reg = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,
      valid = reg.test(value);
    this.setState({
      email: value,
      email_error: valid ? '' : '邮箱格式不正确，请重新输入'
    });
  }
  
  validateCode(e) {
    this.setState({ 
      code_error: e.target.form.code.value ? '' : message['MSG_02']
    });
  }

  getCode(e) {
    let { value } = e.target.form.mobilephone;
    fetch(`${smsService}/mobile/send/${value}`)
    .catch(Hmall.catchHttpError());
  }

  showSliderVerification() {
    let { retry, phone_number, phone_error, slider_error, verification_key } = this.state;
    return !phone_error && retry > 2 && verification_key &&
      <FormGroup>
        <SliderVerification width={339} onValid={() => this.setState({ slider_error: '',verf: true })} onInValid={() => this.setState({ slider_error: message['UR_VERIF_01'] })} phoneNumber={phone_number} secretKey={verification_key}></SliderVerification> 
        {this.showErrorMsg(slider_error)}
      </FormGroup>;
  }
  
  getEmaillSendSuccessMessage() {
    if(this.state.email_send_success) {
      return (
          <FormGroup>
            <span className="notice notice-word" >邮件发送成功，去邮箱查收并继续。</span>
          </FormGroup>)
    }
  }

  canMobileSubmit() {
    let { verf, changing } = this.state;
    return verf && !changing;
  }
  
  clearError() {
    this.setState({
      phone_error: '',
      code_error: '',
      email_error: '',
      slider_error: ''
    });
  }
  
  handleSubmit(e) {
    e.preventDefault();
    let { mobilephone, code } = e.target,
      callback = () => this.setState({changing: false});
    if(this.canMobileSubmit()){
      this.clearError();
      this.setState({
        changing: true
      });
      fetch(`${urService}/forgotPwdByMobile`,{
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          'mobileNumber': mobilephone.value,
          'msg': code.value,
          't': String(Date.now())
        })
      })
      .then(Hmall.convertResponse('json',this,callback))
      .then(json => {
        let { success, msgCode, resp } = json;
        if(success){
          browserHistory.push({pathname:'/change-password.html',state:{
            userId: resp[0].userId
          }});
        }else if(msgCode == 'UR_MOBILE_01'){
          this.setState({
            phone_error: message[msgCode]
          });
        }else if(msgCode == 'UR_MOBILE_02'){
          this.setState({
            phone_error: message[msgCode]
          });
        }else if(msgCode == 'MSG_03'){
          this.setState({
            code_error: message[msgCode],
            retry: resp[0].time
          })
        }else if(msgCode == 'MSG_02'){
          this.setState({
            code_error: message[msgCode]
          })
        }else if(msgCode == 'MSG_01'){
          let { time, key } = resp[0];
          this.setState({
            code_error: message[msgCode],
            retry: time,
            verification_key: key,
            verf: false
          })
          this.registeredPhone[mobilephone.value] = key;
        }else if(msgCode == 'UR_VERIF_01'){
          this.setState({
            slider_error: message[msgCode]
          })
        }
      })
      .catch(Hmall.catchHttpError(callback))
    }
  }
  
  handleEmailSubmit(e) {
    e.preventDefault();
    let { email } = e.target,
      { email_error } = this.state;
    if(!email || email_error) return;
    fetch(`${urService}/forgotPwdByEmail`,{
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        'email': email.value
      })
    })
    .then(Hmall.convertResponse('json',this))
    .then(json => {
      let { success, msgCode} = json;
      if(success){
        this.setState({
          email_send_success: true
        });
      }else{
        if(msgCode == 'UR_EMAIL_06'){
          this.setState({
            email_error: message[msgCode]
          });
        }else if(msgCode == 'UR_EMAIL_01'){
          this.setState({
            email_error: message[msgCode]
          });
        }
      }
    })
    .catch(Hmall.catchHttpError())
  }
    
  showErrorMsg(error_msg) {
    return error_msg ? <p className="error-msg" >{error_msg}</p> : null
  }
  
  render() {
    let { phone_number, email, phone_error, email_error, phone_has_registered_validing, code_error } = this.state;
    return (
      <Panel id="password-find">
        <Row>
          <Col width={626}>
            <img src={Hmall.cdnPath(CMSConfig.passwordFindBanner.urlPath)}></img>
          </Col>
          <Col>
            <TabPanel width={470}>
              <Tab title="手机找回" width={120}>
                <Form className="label-left" onSubmit={ e => this.handleSubmit(e) }>
                  <FormGroup>
                    <label>手机号</label>
                    <TextBox name="mobilephone" width={339} onBlur={ e => this.validatePhone(e) }></TextBox>
                  </FormGroup>
                  {this.showErrorMsg(phone_error)}
                  <FormGroup>
                    <label>验证码</label>
                    <TextBox name="code" onBlur={e => this.validateCode(e)} width={222}></TextBox>
                    <CodeButton disabled={!!phone_error || phone_has_registered_validing} phone={phone_number} width={107} height={36}></CodeButton>
                  </FormGroup>
                  {this.showErrorMsg(code_error)}
                  {this.showSliderVerification()}
                  <FormGroup>
                    <p className="notice">如您无法通过手机和邮箱找回密码，请致电 <span className="notice-word">400 888 0296</span> 联系客服团队</p>
                  </FormGroup>
                  <FormGroup>
                    <Button disabled={!this.canMobileSubmit()} type="submit" className="red" text="立即找回" width={150}></Button>
                  </FormGroup>
                </Form>
              </Tab>
              <Tab title="邮箱找回" width={120}>
                <Form className="label-left" onSubmit={ e => this.handleEmailSubmit(e) }>
                  <FormGroup>
                    <label>邮箱地址</label>
                    <TextBox name="email" width={300} onBlur={ e => this.validateEmail(e) }></TextBox>
                  </FormGroup>
                  {this.showErrorMsg(email_error)}
                  <FormGroup>
                    <p className="notice">如您无法通过手机和邮箱找回密码，请致电 <span className="notice-word">400 888 0296</span> 联系客服团队</p>
                  </FormGroup>
                  {this.getEmaillSendSuccessMessage()}
                  <FormGroup>
                    <Button type="submit" disabled={email_error} className="red" text="立即找回" width={150}></Button>
                  </FormGroup>
                </Form>
              </Tab>
            </TabPanel>
          </Col>
        </Row>
      </Panel>
    );
  }
}
