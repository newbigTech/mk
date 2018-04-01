import React, { Component } from 'react';
import { Link, browserHistory } from 'react-router';
import { Panel } from '../components/Layout';
import Form, { FormGroup } from '../components/Form';
import Button from '../components/Button';
import Password from '../components/Password';

export default class ChangePassword extends Component{
  
  constructor(props) {
    super(props);
    
    this.state = {
      init: true,
      changing: false,
      other_is_empty: true,
      bad_credentials: false,
      error_msg: ''
    };
  }
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  componentWillMount() {
    let { uid } = this.props.location.query;
    if(uid){
      this.setState({init: false});
      fetch(`${urService}/validateEmail/${uid}`)
      .then(Hmall.convertResponse('json',this))
      .then(json => {
        if(json.success){
          this.setState({
            init: true
          });
        }else{
          this.setState({
            init: true,
            bad_credentials: true
          })
        }
      })
      .catch(Hmall.catchHttpError())
    }
  }
  
  validatePassword(e) {
    let { target } = e,
      { form, value } = target,
      { password, confirm } = form,
      v2 = target == password ? confirm.value : password.value;
      if(!value){
        this.setState({error_msg: '密码不能为空'});
      }else if(v2 && value != v2 ){
        this.setState({error_msg: '两次密码输入不一致，请重新输入'});
      }else{
        this.setState({error_msg: ''});
      }
  }
  
  validateEmpty(e) {
    let { password, confirm } = e.target.form,
      v1 = password.value,
      v2 = confirm.value;
    this.setState({ other_is_empty: !v1 || !v2 });
  }

  displayErrorMessage(v){
    return {display: v ? '' : 'none'};
  }
  
  showErrorMsg(error_msg) {
    return error_msg? 
        <FormGroup>
          <span className="notice notice-word" style={this.displayErrorMessage(error_msg)}>{error_msg}</span>
        </FormGroup> : null;
  }
  
  canSubmit() {
    let {other_is_empty, error_msg, changing } = this.state;
    return !other_is_empty && !error_msg && !changing;
  }
  
  getChangePage(customerId) {
    let { state, query = {} } = this.props.location,
      { uid } = query,
      { userId } = state || {},
      { bad_credentials, error_msg } = this.state,
      type;
    if(uid && !bad_credentials){
      type = 'Email';
    }else if(userId){
      type = 'Mobile';
    }
    return type ? 
        <Form className="label-left" onSubmit={(e) =>this.changePassword(e, uid || userId, type)}>
          <FormGroup>
            <label>新密码</label>
            <Password name="password" width={339} onChange={ e => {this.validateEmpty(e);this.validatePassword(e)}}></Password>
          </FormGroup>
          <FormGroup>
            <label>确认新密码</label>
            <Password name="confirm" width={339} onChange={ e => {this.validateEmpty(e);this.validatePassword(e)}}></Password>
          </FormGroup>
          {
            this.showErrorMsg(error_msg)
          }
          <FormGroup>
            <Button type="submit" disabled={!this.canSubmit()} className="black" text="确定" width={100} height={35}></Button>
          </FormGroup>
        </Form> 
        : <p>该链接已失效</p>
  }
  
  changePassword(e,userId,type){
    e.preventDefault();
    let { password, confirm } = e.target,
      v1 = password.value,
      v2 = confirm.value,
      callback = () => this.setState({changing: false});
    if(this.canSubmit()){
      this.setState({changing: true});
      fetch(`${urService}/changePasswordBy${type}`,{
        method:'post',
        headers: {'Content-Type': 'application/json'},
        body:JSON.stringify({
          userId,
          password: v1 && Crypto.MD5(v1),
          confirm: v2 && Crypto.MD5(v2)
        })
      })
      .then(Hmall.convertResponse('json', this, callback))
      .then(json => {
        let { success, msgCode} = json;
        if(success){
          browserHistory.push('/login.html');
        }else if(msgCode == 'UR_PASSWORD_02'){
          this.setState({
            error_msg: '两次密码输入不一致，请重新输入'
          })
        }else if(msgCode == 'UR_PASSWORD_07' || msgCode == 'UR_PASSWORD_08'){
          this.setState({
            error_msg: '密码不能为空'
          })
        }
      })
      .catch(Hmall.catchHttpError(callback));
    }
  }
  
  render() {
    let { init } = this.state;
    return init ? 
      <Panel id="change-password">
      {
        this.getChangePage()
      }
      </Panel>
     : <div className="loading"></div>;
  }
}
