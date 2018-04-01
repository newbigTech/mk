import React, { Component } from 'react';
import { Link, browserHistory } from 'react-router';
import Form, { FormGroup } from '../components/Form';
import TextBox from '../components/TextBox';
import Button from '../components/Button';
import CodeButton from '../components/CodeButton';
import Icon from '../components/Icon';
import NormalLogin from './NormalLogin';

const message = {
  'MSG_02': '验证码不能为空',
  'UR_MOBILE_02': '手机格式不正确，请重新输入'
};

export default class QuickLogin extends NormalLogin{
  
  getSubmitBody(v1,v2) {
    return{
      'mobileNumber': v1,
      'msg': v2,
      't': String(Date.now())
    }
  }
  
  getLoginUrl() {
    return `${urService}/login/quick`;
  }
  
  getMobileVarifyUrl(value){
    return `${urService}/mobile/${value}?t=${Date.now()}`
  }

  validatePhone(e) {
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
  
  validateCode(e) {
    this.setState({ 
      code_error: e.target.form.password.value ? '' : message['MSG_02']
    });
  }
  
  render() {
    let { phone_number, phone_error, phone_has_registered_validing, code_error } = this.state;
    return (
      <Form onSubmit={e => this.handleSubmit(e)}>
        <FormGroup>
          <TextBox name="user_name" placeholder="手机号" onBlur={e => this.validatePhone(e)}>
            <Icon name="username"></Icon>
          </TextBox>
          { this.showErrorMsg(phone_error) }
        </FormGroup>
        <FormGroup>
          <TextBox name="password" onBlur={e => this.validateCode(e)} placeholder="验证码" width={138}></TextBox>
          <CodeButton disabled={!!phone_error || phone_has_registered_validing} phone={phone_number} width={98} height={36}></CodeButton>
        </FormGroup>
        {this.showErrorMsg(code_error)}
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