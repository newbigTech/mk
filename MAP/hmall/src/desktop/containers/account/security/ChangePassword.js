import React, { Component,PropTypes } from 'react';
import { Link, browserHistory } from 'react-router';
import Form, { FormGroup } from '../../../components/Form';
import TextBox from '../../../components/TextBox';
import Button from '../../../components/Button';
import Password from '../../../components/Password';

const message = {
  'UR_PASSWORD_02': '两次密码输入不一致，请重新输入',
  'UR_PASSWORD_05': '密码不能为空'
};

export default class ChangePassword extends Component{
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  constructor(props){
    super(props);
    this.state = {
      other_is_empty: true,
      changing: false,
      error_msg: null
    };
  }
  
  canSubmit() {
    let {other_is_empty, error_msg, changing } = this.state;
    return !other_is_empty && !error_msg && !changing;
  }
  
  changePassword(e){
    e.preventDefault();
    if(this.canSubmit()){
      let { newPassword, confirm } = e.target,
        v1 = newPassword.value,
        v2 = confirm.value,
        callback = () => this.setState({changing: false});
      this.setState({changing: true});
      fetch(`${urService}/customer/security/update/password`,{
        method: 'post',
        headers: Hmall.getHeader({'Content-Type': 'application/json'}),
        body: JSON.stringify({
          newPassword: v1 && Crypto.MD5(v1),
          confirm: v2 && Crypto.MD5(v2)
        })
      })
      .then(Hmall.convertResponse('json',this, callback))
      .then(json => {
        if(json.success){
          alert("密码修改成功!");
          browserHistory.push('/login.html');
        }
      })
      .catch(Hmall.catchHttpError(callback));
    }
  }
  
  validateEmpty(e) {
    let { newPassword, confirm } = e.target.form,
      v1 = newPassword.value,
      v2 = confirm.value;
    this.setState({ other_is_empty: !v1 || !v2 });
  }
  
  validatePassword(e) {
    let { target } = e,
      { form, value } = target,
      { newPassword, confirm } = form,
      v2 = target == newPassword ? confirm.value : newPassword.value;
      if(!value){
        this.setState({error_msg: message['UR_PASSWORD_05']});
      }else if(v2 && value != v2 ){
        this.setState({error_msg: message['UR_PASSWORD_02']});
      }else{
        this.setState({error_msg: ''});
      }
  }

  showErrorMsg(error_msg) {
    return error_msg ? <p className="error-msg" >{error_msg}</p> : null
  }
  
  handleCancel() {
    browserHistory.push('/account/security.html');
  }
  
  render(){
    return(
      <div id="change-password">
        <Form className="label-left" onSubmit={(e) =>this.changePassword(e)}>
          <FormGroup>
            <label>新密码</label>
            <Password name="newPassword" width={339} onChange={ e => {this.validateEmpty(e);this.validatePassword(e)}}></Password>
          </FormGroup>
          <FormGroup>
            <label>确认新密码</label>
            <Password name="confirm" width={339} onChange={ e => {this.validateEmpty(e);this.validatePassword(e)}}></Password>
          </FormGroup>
          {
            this.showErrorMsg(this.state.error_msg)
          }
          <FormGroup>
            <Button text="取消" onClick={this.handleCancel} width={101} height={35}></Button>
            <Button type="submit" disabled={!this.canSubmit()} className="red" text="确定" width={101} height={35}></Button>
          </FormGroup>
        </Form>
      </div>
    );
    
  }
  
}