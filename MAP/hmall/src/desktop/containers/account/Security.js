import React, { Component } from 'react'
import Button from '../../components/Button'
import TextBox from '../../components/TextBox'
import Password from '../../components/Password'
import { Link, browserHistory } from 'react-router'
import Form, { FormGroup } from '../../components/Form'

export default class Security extends Component{
  
  /*componentWillUnmount() {
    this.isUnMounted = true;
  }*/

  
  constructor(props){
    super(props);
    this.state = {
      init: false,
      resp:[],
      appear:false
    }
  }
  
  
  alterPassword() {
    browserHistory.push('/account/security/change-password.html');
  }
  
  alterPhoneNum() {
    browserHistory.push({
      pathname: '/account/security/change-phone.html',
      state: true
    });
  }
  
  preventHandle(e){
    e.preventDefault();
  }
  
  alterMail() {
    browserHistory.push('/account/security/change-email.html');
  }
  
  /*showRemind(email) {
    debugger
    if(email==""||email==null){
      this.setState({appear:true});
    }else{
      return '';
    }
  }*/
  
  render() {
      let mobileNumber = Hmall.getCookie('mobile'),
        email = Hmall.getCookie('email');
      return (
        <div id="security_div">
          <Form className="label-left">
            <FormGroup>
              <label>密码</label>
              <Password readOnly={true} width={369} value="********"></Password>
              <Button onClick={()=> this.alterPassword()} width={80} text="修改密码"></Button>
            </FormGroup>
            <FormGroup>
              <label>手机</label>
              <TextBox readOnly={true} width={369} value={mobileNumber}></TextBox>
              <Button onClick={() =>this.alterPhoneNum()} width={80} text="修改手机号"></Button>
            </FormGroup>
            <FormGroup>
              <label>邮箱</label>
              {
                email ? 
                    [<TextBox key="0" readOnly={true} width={369} value={email}></TextBox>,
                    <Button key="1" onClick={this.alterMail} width={80} text="修改邮箱"></Button>] :
                    [<div key="0" style={{width: 369}}>未绑定 绑定邮箱后可用于找回密码</div>,<div key="1" style={{width: 80, textAlign: 'center'}}><Link to="/account/security/change-email.html">立即绑定</Link></div>]
              }
            </FormGroup>
          </Form>
        </div>
      );
  }
}