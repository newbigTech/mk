import React, { Component } from 'react';
import { Link, browserHistory } from 'react-router';
import Form, { FormGroup } from '../../../components/Form';
import TextBox from '../../../components/TextBox';
import Button from '../../../components/Button';

export default class ChangeEmail extends Component{
  
  constructor(props){
    super(props);
    this.state = {
      other_is_empty: true,
      changing: false,
      error_msg: null
    };
  }
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  canSubmit() {
    let {other_is_empty, error_msg, changing } = this.state;
    return !other_is_empty && !error_msg && !changing;
  }
  
  backSecurity() {
    browserHistory.push('/account/security.html');
  }
  
  changeEmail(e) {
    e.preventDefault();
    if(this.canSubmit()){
      let { email } = e.target,
        callback = () => this.setState({changing: false});
      this.setState({changing: true});
      fetch(`${urService}/customer/security/update/email`,{
        method:'post',
        headers: Hmall.getHeader({'Content-Type': 'application/json'}),
        body:JSON.stringify({
          email:email.value
        })
      })
      .then(Hmall.convertResponse('json', this, callback))
      .then(json => {
        if(json.success){
          Hmall.setCookie('email',email.value);
          this.backSecurity();
        }
      })
      .catch(Hmall.catchHttpError(callback));
    }
  }
  
  validateEmpty(e) {
    let { value } = e.target.form.email;
    this.setState({ other_is_empty: !value });
  }
  
  validEmail(e) {
    let { value } = e.target,
      reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
      valid = reg.test(value);
    this.setState({error_msg: valid? null: '请输入正确的邮箱'});
  }

  showErrorMsg(error_msg) {
    return error_msg ? <p className="error-msg" >{error_msg}</p> : null
  }
  
  render(){
    return(
        <div id="change-email">
          <Form className="label-left" onSubmit={(e) => this.changeEmail(e)}>
            <FormGroup>
              <label>邮箱</label>
              <TextBox name="email" width={300} height={40} onChange={e => {this.validateEmpty(e);this.validEmail(e)}}></TextBox>
            </FormGroup>
            {
              this.showErrorMsg(this.state.error_msg)
            }
            <FormGroup>
              <Button  onClick={() => this.backSecurity()} width={101} height={35} text="取消"></Button>
              <Button type="submit" disabled={!this.canSubmit()} className="red" text="确定" width={101} height={35}></Button>
            </FormGroup>
          </Form>
        </div>
    );
  }
}