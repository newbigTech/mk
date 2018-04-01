import React, { Component } from 'react';
import { Link, browserHistory } from 'react-router';
import { Panel, Row, Col } from '../components/Layout';
import { TabPanel, Tab } from '../components/Tab';
import Form, { FormGroup } from '../components/Form';
import TextBox from '../components/TextBox';
import Button from '../components/Button';
import Icon from '../components/Icon';
import Logo from '../components/Logo';
import NormalLogin from './NormalLogin';
import QuickLogin from './QuickLogin';

export default class Login extends Component{
  componentWillUnmount(){
    this.isUnMounted = true;
  }

  constructor(props) {
    super(props);
    this.state = {
      type: 'normal',
      modalIsOpen: false,
      third_party_url: '',
      taobo_token: ''
    }
  }

  handleRegister() {
    browserHistory.push('/register.html');
  }
  
  switchLogin() {
    this.setState({
      type: this.state.type == 'normal' ? 'quick' : 'normal'
    });
  }
  closeModal() {
    this.setState({modalIsOpen: false});
  }
  /*第三方账号绑定*/
  handleClickLogin(type){
    //${this.props.location.search}
    let { protocol, host, hostname } = location,
      { search } = this.props.location ,
      t =  search==""?"?type":"&type",
      params = {
        redirect_uri: encodeURIComponent(`${protocol}//${hostname == 'localhost' ? 'hhmall.saas.hand-china.com' : host}/thirdParty.html${search}${t}=${type}`)
      };
    if(type == 5){
      location.href = Hmall.urlAppend(HmallConfig.login_taobao, params);
    }else if(type == 4){
      fetch(`${tpService}/thirdParty/getAliPayAuthCode`,{
        method:"get",
        headers: {
          'Content-Type': 'application/json'
        },
      })
      .then(Hmall.convertResponse('json',this))
      .then(json=>{
        let { resp , success } = json;
        if(success)
          localStorage.setItem("url", JSON.stringify(search));
          location.href = resp
      })
      .catch(Hmall.catchHttpError());
      //location.href = Hmall.urlAppend(HmallConfig.login_alipay, params);
    }else if(type == 3){
      location.href = Hmall.urlAppend(HmallConfig.login_weibo, params);
    }else if(type == 2){
      location.href = Hmall.urlAppend(HmallConfig.login_wechat, params);
    }else if(type == 1){
      location.href = Hmall.urlAppend(HmallConfig.login_qq, params);
    }
  }
  render() {
    let { location } = this.props,
      { type, modalIsOpen, third_party_url } = this.state;
    return (
      <div className="form-wrap">
        <Panel>
          <div className={`login-switch ${type}`} onClick={() => this.switchLogin()}></div>
          <Row className="title">
            <Col width={50}><Logo href="/"></Logo></Col>
            <Col><h2>会员登录</h2></Col>
          </Row>
          {
            type == 'normal' ?
              <NormalLogin location={location}></NormalLogin> :
              <QuickLogin location={location}></QuickLogin>
          }
          <Form>
            <fieldset>
              <legend>第三方账号登录</legend>
            </fieldset>
            <FormGroup align="center">

              {/*<Icon name="taobao" onClick={()=>{this.handleClickLogin(5)}}></Icon>*/}
              <Icon name="wechat" onClick={()=>{this.handleClickLogin(2)}}></Icon>
              <Icon name="alipay" onClick={()=>{this.handleClickLogin(4)}}></Icon>
              <Icon name="qq" onClick={()=>{this.handleClickLogin(1)}}></Icon>
              <Icon name="weibo" onClick={()=>{this.handleClickLogin(3)}}></Icon>
            </FormGroup>
            <FormGroup>
              <Button className="black" onClick={this.handleRegister} text="新用户注册" height={44}></Button>
            </FormGroup>
          </Form>
        </Panel>
      </div>
    );
  }
}
