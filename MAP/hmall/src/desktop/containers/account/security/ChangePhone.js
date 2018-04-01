import React, { Component } from 'react';
import { Link,browserHistory } from 'react-router';
import Form, { FormGroup } from '../../../components/Form';
import TextBox from '../../../components/TextBox';
import Button from '../../../components/Button';
import CodeButton from '../../../components/CodeButton';
import SliderVerification from '../../../components/SliderVerification';

const message = {
  'UR_MOBILE_02': '手机格式不正确，请重新输入',
  'UR_MOBILE_03': '当前手机号已注册',
  'UR_VERIF_01': '滑块验证不通过',
  'MSG_01': '验证码码输入错误次数大于3次',
  'MSG_02': '验证码不能为空',
  'MSG_03': '验证码错误，请重新输入'
}

export default class ChangePhone extends Component{
  
  constructor(props) {
    super(props);
    this.registeredPhone = {};
    this.state = {
      phone_number: '',
      retry: 0,
      verification_key: '',
      changing: false,
      phone_error: '',
      phone_has_registered_validing: false,
      code_error: '',
      slider_error: '',
      verf: true
    };
  }
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  canSubmit() {
    let { verf, changing} = this.state;
    return verf && !changing;
  }
  
  backSecurity() {
    let { state } = this.props.location , url = ""
    url = state?"account/security.html":"/account/base-info.html"
    browserHistory.push(url);
  }
  
  clearError() {
    this.setState({
      phone_error: '',
      code_error: '',
      slider_error: ''
    });
  }
  
  validRegister(value) {
    if(this.registeredPhone[value]){
      this.setState({
        phone_error: message['UR_MOBILE_03']
      });
    }else{
      let callback = () => this.setState({phone_has_registered_validing: false});
      this.setState({phone_has_registered_validing: true});
      fetch(`${urService}/mobile/${value}?t=${Date.now()}`)
      .then(Hmall.convertResponse('json', this, callback))
      .then(json => {
        let { msgCode, msg, success, resp } = json;
        if(success){
          let { time } = resp[0];
          if(time<3){
            this.setState({
              phone_error: '',
              phone_number:value,
              retry: time
            });
          }else{
            let { key } = resp[0];
            this.setState({
              phone_error: '',
              phone_number:value,
              retry: time,
              verification_key: key,
              verf: !key
            });
          }
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
      .catch(Hmall.catchHttpError(callback));
    }
  }
  
  validatePhone(e) {
    let { value } = e.target.form.mobileNumber,
      reg = HmallConfig.mobilephone_regx,
      valid = reg.test(value);
    this.setState({
      phone_number: '',
      phone_error: valid ? '' : message['UR_MOBILE_02']
    });
    if( valid ){
      this.validRegister(value);
    }
  }
  
  handleSubmit(e) {
    e.preventDefault();
    if(this.canSubmit()){
      this.clearError();
      let { mobileNumber,code } = e.target,
        mobile = mobileNumber.value,
        callback = () => this.setState({changing: false});
      this.setState({changing: true});
      fetch(`${urService}/customer/security/update/mobilePhone`,{
        method:'post',
        headers: Hmall.getHeader({'Content-Type': 'application/json'}),
        body:JSON.stringify({
          mobileNumber: mobile,
          code: code.value,
          t: String(Date.now())
        })
      })
      .then(Hmall.convertResponse('json', this, callback))
      .then(json => {
        let { success, msgCode, resp } = json;
        if(success){
          Hmall.logout();
          alert("手机号修改成功，请重新登录");
          browserHistory.push('/login.html');
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
      })
      .catch(Hmall.catchHttpError(callback));
    }
  }
  
  validateCode(e) {
    this.setState({code_error: e.target.form.code.value ? '' : message['MSG_02']});
  }
  
  showSliderVerification() {
    let { phone_number, phone_error, slider_error, retry, verification_key } = this.state;
    return  !phone_error && retry > 2 && verification_key &&
      <FormGroup>
        <SliderVerification width={339} onValid={() => this.setState({ slider_error: '', verf: true })} onInValid={() => this.setState({ slider_error: message['UR_VERIF_01'] })} phoneNumber={phone_number} secretKey={verification_key}></SliderVerification> 
        {this.showErrorMsg(slider_error)}
      </FormGroup>;
  }

  showErrorMsg(error_msg) {
    return error_msg ? <p className="error-msg" >{error_msg}</p> : null
  }
  
  render(){
    let { phone_number, phone_error, phone_has_registered_validing, changing, code_error } = this.state;
    return(
        <div id="change-phone">
          <Form className="label-left" onSubmit={(e) => this.handleSubmit(e)}>
            <FormGroup>
              <label>手机号</label>
              <TextBox onBlur={(e) => this.validatePhone(e)} name="mobileNumber" width={339}></TextBox>
            </FormGroup>
            {this.showErrorMsg(phone_error)}
            <FormGroup>
              <label>验证码</label>
              <TextBox onBlur={(e) => this.validateCode(e)} name="code" width={221}></TextBox>
              <CodeButton disabled={!!phone_error || phone_has_registered_validing} phone={phone_number} width={108}></CodeButton>
            </FormGroup>
            {this.showErrorMsg(code_error)}
            {this.showSliderVerification()}
            <FormGroup>
              <Button  onClick={() => this.backSecurity()} width={101} height={35} text="取消"></Button>
              <Button disabled={!this.canSubmit()}  type="submit" className="red" text="确定" width={101} height={35}></Button>
            </FormGroup>
          </Form>
        </div>
    );
  }
}