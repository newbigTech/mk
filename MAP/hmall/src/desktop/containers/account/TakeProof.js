/**
 * Created by ZWL on 2016/10/28.
 */
import React, { Component } from 'react'
import { Panel,Row,Col } from '../../components/Layout'
import { TabPanel, Tab } from '../../components/Tab';
import { Link, IndexLink } from 'react-router'
import Moment from 'moment';
import Form, { FormGroup } from '../../components/Form';
import Button from '../../components/Button';
import TextBox from '../../components/TextBox';

export default class TakeProof extends Component{
  componentWillUnmount(){
    this.isUnMounted = true;
  }
  componentWillMount(){
    {/*组件将要渲染时调用*/}
    //${odService}/order/queryForPickupCode订单id/自提单id
    let { oid , pickupId } = this.props.location.query
    fetch(`${odService}/order/queryForPickupCode/${oid}/${pickupId}`,{
      headers: Hmall.getHeader({
      }),

    })
      .then(Hmall.convertResponse('json',this))
      .then(json=>{
        let { resp , success} = json
        if(success){
          let arr = {orderId:"",phone:"",pickupId:"",startDate:"",expiredDate:"",address:"",businessTime:"",storePhone:""}
          arr.orderId = resp[0].orderId
          arr.phone = resp[0].receivingInfo.userPhone
          arr.pickupId = resp[0].pickupId
          arr.startDate = resp[0].startDate
          arr.expiredDate = resp[0].expiredDate
          arr.address = resp[0].receivingInfo.address
          arr.businessTime = resp[0].receivingInfo.businessTime
          arr.storePhone = resp[0].receivingInfo.storePhone
          this.setState({goods:resp[0].codes,order:arr,fetch_status:"init"})
        }
      })
      .catch(Hmall.catchHttpError(()=>{
        this.setState({
          fetch_status: 'error'
        });
      }))
  }
  constructor(props){
    super(props);
    this.state = {
      fetch_status: "uninit",
      goods : [],
      img_style_smart :{display:"none"},
      img_style_big :{},
      a:[] ,
      order:{orderId:"",phone:"",pickupId:"",startDate:"",expiredDate:"",address:"",businessTime:"",storePhone:""},
      phone_error: false,
      phone_change:false
    }
  }
  /*修改手机号码*/
  handleChangePhone(order){
    //${odService}/order/updatePickupPhone
    if(!this.state.phone_error){
      fetch(`${odService}/order/updatePickupPhone`,{
      method:"post",
      headers:Hmall.getHeader({
        "Content-Type":"application/json"
      }),

      body:JSON.stringify({
        pickupId : order.pickupId ,
        userPhone : order.phone
      })
    })
      .then(Hmall.convertResponse('json',this))
      .then(json=>{
        if(json.success){
          let arr = this.state.goods
          arr.forEach((a,i)=>{
            if(a.status == "WAIT"){
              a.opration =json.resp[0].activeCode
            }
          })
          this.setState({goods:arr,phone_change:false});
        }else{
          this.setState({phone_error:true})
        }
      })
      .catch(Hmall.catchHttpError())
    }else{
      this.setState({phone_error:true})
    }

  }
  /*重新发送验证码*/
  handleSendCode(){
    let { orderId , pickupId , phone} = this.state.order
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
    }
    this.timeoutId = setTimeout(()=> {
      fetch(`${odService}/order/createNewCode`,{
        method:"post",
        headers:Hmall.getHeader({
          "Content-Type":"application/json"
        }),
        body:JSON.stringify({
          orderId: orderId,
          pickupId: pickupId,
          userPhone: phone
        })
      })
          .then(Hmall.convertResponse('json',this))
          .then(json=>{
            if(json.success){
              this.setState({goods:json.resp[0].codes})
              alert("验证码发送成功，请注意查收")
            }else{
              alert("每天只有一次发送机会")
            }
          })
          .catch(Hmall.catchHttpError(
              ()=>{
                alert("发码失败")
              }
          ))}, 10000, this)
  }
  /*验证手机号码格式*/
  validatePhone(e){
    let { value } = e.target,
      flag = (HmallConfig.mobilephone_regx.test(value))
      if(flag){
        let object = this.state.order
        object.phone = value
        this.setState({
          order: object,
          phone_error:false
        });
      }else{
        this.setState({phone_error:true})
      }
  }
  //状态统计
  renderType(type){
    let waitNumber = 0,pickupNumber = 0, failedNumber = 0
    this.state.goods.map((g, i)=> {
      if (g.status == "WAIT") {
        waitNumber = waitNumber + 1
      } else if (g.status == "PICKED") {
        pickupNumber = pickupNumber + 1
      } else {
        failedNumber = failedNumber + 1
      }})
    switch (type){
      case 1 : return pickupNumber;
      case 2 : return waitNumber;
      case 3 : return failedNumber;
    }
  }
  //提货吗详情
  renderPickUp(){
    let { goods , img_style_big , img_style_smart} = this.state
   return goods.map((g, i)=> {
      let state = ""
      let failure = {}
      if (g.status == "WAIT") {
        state = "待核销"
      } else if (g.status == "PICKED") {
        state = "已核销"
      } else {
        state = "失效"
      }
      if (state == "失效") {
        failure = {textDecoration: "line-through"}
      }
      return (
        <tr key={i}>
          <td style={failure}>{g.msgCode}</td>
          <td>
            {g.status == "WAIT" ? (
              <div className="div-in">
                <img onClick={()=>{this.setState({img_style_smart:{display:"block"},img_style_big:{display:"none"}})}}
                     style={img_style_big}
                     src={"data:image/png;base64,"+g.qrcodeStr}/>
                <img onClick={()=>{  this.setState({img_style_smart:{display:"none"},img_style_big:{display:"block"}})}}
                     style={img_style_smart} src={"data:image/png;base64,"+g.qrcodeStr}/>
              </div>
            ) : (
              <div className="div-out">
                <img src={"data:image/png;base64,"+g.qrcodeStr}/>
              </div>)}
          </td>
          <td>{state}</td>
          <td>{g.opration}</td>
        </tr>
      )
    })
  }
  render() {
    let { order ,fetch_status  ,phone_change , phone_error} = this.state,
      {orderId ,phone ,pickupId ,startDate ,expiredDate ,address ,businessTime ,storePhone } = order

    return (
      <div className="takeProof">
        {fetch_status=="init"?<Form className="label-left2">
          <FormGroup>
            <label>订单编号：</label><span>{orderId}</span>
          </FormGroup>
          <FormGroup>
            <label>接收手机：</label>{phone_change?"":<span>{phone}</span>}
            {phone_change?
                <TextBox name="name" width={180} value={name} placeholder="请填写接收手机号码" onChange={(e)=>{this.validatePhone(e)}}></TextBox>:""
            }
            {phone_change?"":<span className="span-click" onClick={()=>{this.setState({phone_change:true})}}>修改</span>}
            {phone_change?<span className="span-click" onClick={()=>{this.handleChangePhone(order)}}>确定</span>:""}
            {phone_error?<span className="notice notice-word" >联系方式为空或者格式不对</span>:""}
          </FormGroup>
          <FormGroup>
            <label>已核销：</label><span>{this.renderType(1)} 份</span>
          </FormGroup>
          <FormGroup>
            <label>未核销：</label><span>{this.renderType(2)} 份</span>
          </FormGroup>
          <FormGroup>
            <label>已过期：</label><span>{this.renderType(3)} 份</span>
          </FormGroup>
          <FormGroup>
            <label>有效期：</label><span>{Moment(startDate).format(HmallConfig.date_format)}
            至 {Moment(expiredDate).format(HmallConfig.date_format)}</span>
          </FormGroup>
          <FormGroup>
            <label>提货门店：</label>
            <span>{address}</span>
            <ul>
              <li>营业时间：{businessTime}</li>
              <li>联系电话：{storePhone}</li>
            </ul>
          </FormGroup>
          <FormGroup>
            <label>操作：</label>
            <span>你每天有1次重新发送的机会，请保管好短信</span>
            <Button width={80} height={30} text="重新发码" onClick={()=>{this.handleSendCode()}}></Button>
          </FormGroup>
        </Form>:""}
        {
          (() => {
            switch(fetch_status){
              case 'uninit': return <div className="loading"></div>;
              case 'init': return <dic className="proof-table">
                <p>提货码详情</p>
                <table>
                  <thead>
                  <tr>
                    <td>凭证码</td>
                    <td>二维码</td>
                    <td>状态</td>
                    <td>操作</td>
                  </tr>
                  </thead>
                  <tbody>
                  {this.renderPickUp()}
                  </tbody>
                </table>
              </dic>
              case 'error':return <h1 className="error">网页出错</h1>
            }
          })()
        }

      </div>
    );

  }
}