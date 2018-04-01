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

const message = {
  'UR_STAFF_01': '员工ID不存在',
  'UR_STAFF_02': <span>当前员工ID已注册，请直接 <Link to="/login.html">登录</Link></span>,
  'UR_STAFF_04': '身份信息格式不正确或与员工ID不匹配',
  'UR_MOBILE_02': '手机格式不正确，请重新输入',
  'UR_MOBILE_03': '当前手机号已注册',
  'UR_PASSWORD_02': '两次密码输入不一致，请重新输入',
  'UR_PASSWORD_07': '密码不能为空',
  'UR_PASSWORD_08': '确认密码不能为空',
  'UR_VERIF_01': '滑块验证不通过',
  'MSG_01': '验证码码输入错误次数大于3次',
  'MSG_02': '验证码不能为空',
  'MSG_03': '验证码错误，请重新输入'
}

export default class Register extends Component{
  
  constructor(props) {
    super(props);
    this.registeredPhone = {};
    this.validEmpolyeeId = {};
    this.registeredEmpolyeeId = {};
    this.state = {
      phone_number: '',
      empolyee_id: '',
      accept: true,
      retry: 0,
      verification_key: '',
      registering: false,
      phone_has_registered_validing: false,
      empolyee_validing: false,
      phone_error: '',
      identity_error: '',
      empolyee_error: '',
      password_error: '',
      confirm_error: '',
      code_error: '',
      slider_error: '',
      verf: true
    }
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  canRegister() {
    let { verf, accept, registering} = this.state;
    return verf && !registering && accept;
  }
  
  clearError() {
    this.setState({
      phone_error: '',
      identity_error: '',
      empolyee_error: '',
      password_error: '',
      confirm_error: '',
      code_error: '',
      slider_error: ''
    });
  }
  
  handleSubmit(e) {
    e.preventDefault();
    let { mobilephone, code, password, password2, employeeid, identity } = e.target,
      { location } = this.props,
      { query } = location,  
      v1 = password.value,
      v2 = password2.value
      callback = () => this.setState({registering: false});
    if(this.canRegister()){
      this.clearError();
      this.setState({registering: true});
      fetch(`${urService}/registerStaff`,{
        method: 'post',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          employeeId: employeeid.value,
          identity: identity.value,
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
          if(msgCode == 'UR_STAFF_01'){
            this.setState({
              empolyee_error: message[msgCode]
            })
          }else if(msgCode == 'UR_STAFF_02'){
            this.setState({
              empolyee_error: message[msgCode]
            })
          }else if(msgCode == 'UR_STAFF_04'){
            this.setState({
              identity_error: message[msgCode]
            })
          }else if(msgCode == 'UR_MOBILE_03'){
            this.setState({
              phone_error: message[msgCode]
            })
          }else if(msgCode == 'UR_MOBILE_02'){
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
          }else if(msgCode == 'MSG_02'){
            this.setState({
              code_error: message[msgCode]
            })
          }else if(msgCode == 'MSG_01' || msgCode == 'MSG_03'){
            let { time, key } = resp[0];
            this.setState({
              code_error: message[msgCode],
              retry: time,
              verification_key: key,
              verf: !key
            })
          }else if(msgCode == 'UR_VERIF_01'){
            this.setState({
              slider_error: message[msgCode]
            })
          }
          callback();
        }
      })
      .catch(Hmall.catchHttpError(callback));
    }
  }

  getCode(e) {
    let { value } = e.target.form.mobilephone;
    fetch(`${smsService}/mobile/send/${value}`)
    .catch(Hmall.catchHttpError());
  }
  
  validRegister(value){
    if(this.registeredPhone[value]){
      this.setState({
        phone_error: message['UR_MOBILE_03']
      });
    }else{
      this.setState({phone_has_registered_validing: true});
      let callback = () => this.setState({phone_has_registered_validing: false});
      fetch(`${urService}/mobile/staff/${value}?t=${Date.now()}`)
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
  validateEmployeeId(e) {
    let { value } = e.target;
    this.setState({
      empolyee_id: value,
      empolyee_error: '',
    });
    if(value) {
      if(this.registeredEmpolyeeId[value]){
        this.setState({
          empolyee_error: message['UR_STAFF_02']
        });
      }else if(!this.validEmpolyeeId[value]){
        this.setState({empolyee_validing: true});
        let callback = () => this.setState({empolyee_validing: false});
        fetch(`${urService}/checkStaff/${value}`)
        .then(Hmall.convertResponse('json', this, callback))
        .then(json => {
          let { success, msgCode, resp } = json;
          if(success){
            this.validEmpolyeeId[value] = true;
            this.setState({
              empolyee_error: ''
            });
          }else{
            if(msgCode == 'UR_STAFF_01'){
              this.setState({
                empolyee_error: message[msgCode]
              });
            }else if(msgCode == 'UR_STAFF_02'){
              this.setState({
                empolyee_error: message[msgCode]
              });
              this.registeredEmpolyeeId[value] = true;
            }
          }
        })
        .catch(Hmall.catchHttpError(callback))
      }
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
    if(valid){
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
    this.setState({code_error: e.target.form.code.value ? '' : message['MSG_02']});
  }
  
  validateIdentity(e) {
    this.setState({identity_error: e.target.form.identity.value ? '' : message['UR_STAFF_04']});
  }
  
  showSliderVerification() {
    let { phone_number,phone_error, retry, verification_key, slider_error } = this.state;
    return !phone_error && retry > 2 && verification_key &&
      <FormGroup>
        <SliderVerification onValid={() => this.setState({ slider_error: '', verf: true })} onInValid={() => this.setState({ slider_error: message['UR_VERIF_01'] })} phoneNumber={phone_number} secretKey={verification_key}></SliderVerification> 
        {this.showErrorMsg(slider_error)}
      </FormGroup>;
  }
  
  showErrorMsg(error_msg) {
    return error_msg ? <p className="error-msg" >{error_msg}</p> : null
  }
  
  render() {
    let { phone_number, phone_error, phone_has_registered_validing, accept, empolyee_error, identity_error, registering} = this.state;
    return (
      <div className="form-wrap">
        <Panel>
          <Row className="title">
            <Col width={50}><Logo href="/"></Logo></Col>
            <Col><h2>员工注册</h2></Col>
          </Row>
          <Form onSubmit={(e) => this.handleSubmit(e)}>
            <FormGroup>
              <TextBox name="employeeid" placeholder="员工ID" onBlur={e => this.validateEmployeeId(e)}></TextBox>
            </FormGroup>
            {this.showErrorMsg(empolyee_error)}
            <FormGroup>
              <TextBox name="identity" onBlur={e => this.validateIdentity(e)} placeholder="身份ID"></TextBox>
            </FormGroup>
            {this.showErrorMsg(identity_error)}
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
              <input checked={accept} name="accept" onChange={e => this.setState({accept: e.target.checked})} type="checkbox"/><p>我已阅读并同意<a href="">《注册用户协议》</a></p>
            </FormGroup>
            <FormGroup align="center">
              <Button type="submit" text="注册" disabled={!this.canRegister()} className="red" height={44}></Button>
            </FormGroup>
          </Form>
        </Panel>
      </div>
    );
  }
}