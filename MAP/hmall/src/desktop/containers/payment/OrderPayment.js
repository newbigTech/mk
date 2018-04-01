/**
 * Created by ZWL on 2016/10/22.
 */
import React, { Component } from 'react';
import { Link, IndexLink, browserHistory } from 'react-router'
import Flow from '../../components/Cart/Flow'
import Button from '../../components/Button';
import Moment from 'moment';
import Icon from '../../components/Icon';
import Radio from "../../components/Radio";

const localPays = {
  '1':  'order-wpay',
  '2':  'order-ypay',
  '3':  'order-alipay',
  '4':  'order-gpay',
  '5':  'order-zpay',
  '6':  'hpay',
}
export default class OrderPayment extends Component {
  componentWillUnmount() {
    this.isUnMounted = true;
    if (this.timeoutId){
      clearInterval(this.timeoutId);
    }
  }

  //this.props.location.state.order_list
  componentWillMount() {
    let {query,state } = this.props.location,
        {oid} = query,
        orders = [],parentId ,
        access_token = Hmall.getCookie('access_token'),
        content = { "orderid":"","orderprice":"" };
    if (oid) {
      orders = [oid ]
      parentId = oid
      this.setState({pay_flag:false})
    } else {
      orders = state.order_list.orderId
      parentId = state.order_list.parentId
    }
    {/*组件将要渲染时调用*/
    }
    fetch(`${odService}/order/queryForOrdersPrice`, {
      method: "POST",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
      body: JSON.stringify(
          orders
      )
    })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          let {success ,  resp} = json
          if (success) {
            this.setState({price: resp, orderIds: orders , parentId:parentId})
            content.orderid = parentId
            content.orderprice = resp[0].account
            Hmall.loadXiaoNeng(content)
          }
        })
        .catch(Hmall.catchHttpError(
            ()=> {
              this.setState({fetch_status: "error"})
            }
        ))
    fetch(`${tpService}/thirdParty/queryPaymentChannel/1?access_token=${access_token}`, {
      method: "get",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
    })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          let { success , resp , msg } = json
          if (success) {
            this.setState({pays:resp,fetch_status: "init"})
          }else
            alert(msg || "连接超时")
        })
        .catch(Hmall.catchHttpError(
            ()=> {
              this.setState({fetch_status: "error"})
            }
        ))
  }
  constructor(props) {
    super(props);
    this.state = {
      buttonClick:false,
      wechatpayReturn_flag: true,
      wechatpayReturn: "",
      pay_flag: true,
      success: "",
      pay_out_style: false,
      price: [{
        "total": 0,
        "freight": 0,
        "discount": 0,
        "account": 0
      }],
      fetch_status: "uninit",
      orderIds: [],
      pay_num:"7",
      pays:[],
      parentId: ""
    }
  }

  handlePayState() {
    let { price , parentId  , pay_num } = this.state
    fetch(`${tpService}/thirdParty/findOrderBatchStatusAndConfirmStatus?orderId=${parentId}&type=${pay_num}`, {
      method: "get",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
    })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          let {resp ,success } = json
          if (success) {
            if (resp[0] == 2) {
              browserHistory.push({
                pathname: '/payment/pay-success.html',
                state: {account: price[0].account, orderIds: parentId}
              });
            }else{
              browserHistory.push({pathname:'/payment/pay-failed.html',
                state: {orderIds: parentId}})
            }
          }else{
            browserHistory.push({pathname:'/payment/pay-failed.html',
              state: {orderIds: parentId}})
          }
        })
        .catch(Hmall.catchHttpError(
            ()=> {
              browserHistory.push({pathname:'/payment/pay-failed.html',
                state: {parentId}})
            }
        ))

  }

  /*定时查看订单状态接口*/
  checkedOrderStatus() {
    //${tpService}
    let { price , parentId } = this.state
    this.timeoutId = setInterval(()=> {
      fetch(`${tpService}/thirdParty/findOrderBatchStatusAndConfirmStatus?orderId=${parentId}&type=${1}`, {
        method: "get",
        headers: Hmall.getHeader({
          "Content-Type": "application/json"
        }),
      })
          .then(Hmall.convertResponse('json', this))
          .then(json=> {
            let {resp ,success } = json
            if (success) {
              if (resp[0] == 2) {
                if (this.timeoutId){
                  clearInterval(this.timeoutId);
                }
                browserHistory.push({
                  pathname: '/payment/pay-success.html',
                  state: {account: price[0].account, orderIds: parentId}
                });
              }
            }else{
              if (this.timeoutId){
                clearInterval(this.timeoutId);
              }
              browserHistory.push({pathname:'/payment/pay-failed.html',
                state: {orderIds: parentId}})
            }
          })
          .catch(Hmall.catchHttpError(
              ()=> {
                if (this.timeoutId){
                  clearInterval(this.timeoutId);
                }
                browserHistory.push({pathname:'/payment/pay-failed.html',
                  state: {orderIds:parentId}})
              }
          ))
    }, 5000)
  }

  /*立即支付按钮*/
  handlePayNow() {
    let {query,state } = this.props.location,
        {oid } = query,parentId ,access_token = Hmall.getCookie('access_token');
    if (oid) {
      parentId = oid
    } else {
      parentId = state.order_list.parentId
    }
    fetch(`${odService}/order/updateOrdersPayed/1`, {
      method: "POST",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
      body: JSON.stringify([{"orderId":parentId}])
    })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          let { success , msgCode } = json
          if (success) {
            this.setState({pay_out_style: true})
          } else {
            if (msgCode == "OD_UPDATE_08") {
              alert("重复支付！")
            }
            alert("支付失败")
          }
        });
  }

  handPay(id,acoumt){
    let access_token = Hmall.getCookie('access_token');
    fetch(`${tpService}/thirdParty/handPay?access_token=${access_token}`, {
      method: "POST",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
      body: JSON.stringify({
        "orderId":id,
        "acoumt":acoumt
      })
    })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          let { success , msgCode ,msg} = json
          if (success) {
            this.setState({pay_out_style: true})
          } else {
            alert( msg || "服务器异常")
          }
        });
  }

  thirtyPay() {
    let callback = () => this.setState({buttonClick: false});
    this.setState({buttonClick: true,pay_out_style: true});
    let { pay_num  ,price , parentId } = this.state ,access_token = Hmall.getCookie('access_token'), account = price[0].account ,
        orderInfo = {
          orderId : parentId,
          acoumt : account,
          type : pay_num
        };
    switch (pay_num) {
      case "1" : return Hmall.loadIpAddress(()=> {
        fetch(`${tpService}/thirdParty/wechatpay?access_token=${access_token}`, {
          method: "POST",
          headers: Hmall.getHeader({
            "Content-Type": "application/json"
          }),
          body: JSON.stringify({
                'orderId': parentId,
                'acoumt': account,
                'ip':returnCitySN["cip"]
              }
          )
        })
            .then(Hmall.convertResponse('json', this , callback))
            .then(json=> {
              let { resp ,success } = json
              if (success) {
                this.setState({
                  wechatpayReturn: resp[0],
                  wechatpayReturn_flag: false
                })
                this.checkedOrderStatus()
              }
            })
            .catch(Hmall.catchHttpError(e=> {
              alert("微信支付失败")
              callback()
            }));
      })
      case "6" : return this.handPay(parentId,account);
      case "7" : return this.handlePayNow();
      default :return localStorage.setItem("orderInfo", JSON.stringify(orderInfo)), window.open("/thirdParty.html?payForm=true");
      //browserHistory.push({
      //pathname: '/payment/pay_form.html',
      //state: {orderId: orderIds[0].orderId, acoumt: account, type: pay_num}
      //});
    }
  }
  close() {
    this.setState({pay_out_style:false})
 }
  renderPays(){
    let { pays , pay_num } = this.state ,
    paysLi=  pays.map((p,i)=>{
      let { paymentType , identification , visible , color} = p
      return (visible=="Y"?<li key={i}>
        <Radio name="pay" value={paymentType} choose={pay_num} id="radio-margin"
               onChange={(e)=>{if(e.target.checked){this.setState({pay_num:paymentType})}}}/>
        <Icon name={localPays[paymentType]} onClick={()=>{this.setState({pay_num:paymentType})}}/>
        <span style={{color:color}}>{identification}</span>
      </li>:"")
    })
    return (
        <div className="thirtyPay">
          <span>选择支付方式</span>
          <ul>
            {paysLi}
          </ul>
        </div>
    )
  }
  render() {
    let  button_style = {marginLeft: "910px", marginRight: "20px"},

        { fetch_status ,wechatpayReturn , wechatpayReturn_flag , pay_flag  , price ,pay_out_style  , buttonClick ,parentId} = this.state
    return (
        <div>
          <div className="orderPayment">
            <div className="div-flow">
              <Flow/>
            </div>
            <div id="pay-out" style={{display  : pay_out_style?"block":"none"}}>
              <div className="pay-out-position">
                <div className="pay-out-title-one">
                  <span>提示</span>
                  <span onClick = {()=>this.close()}></span>
                </div>
                <div className="pay-out-main">
                  <h1>请在新打开的支付页面或者扫描二维码完成支付</h1>
                  {wechatpayReturn_flag ? "" : <img src={"data:image/png;base64,"+wechatpayReturn}/>}
                  <Button className="white" width={100} height={30} text="支付遇到问题"
                          onClick={()=>{this.handlePayState()}}/>
                  <Button className="red" width={100} height={30} text="支付成功"
                          onClick={()=>{this.handlePayState()}}/>
                </div>
              </div>
            </div>

            {/*支付布局*/}
            {
              (() => {
                switch (fetch_status) {
                  case 'uninit':
                    return <div className="pay">
                      <div className="loading"></div>
                    </div>;
                  case 'init':
                    return <div className="pay">
                      <div className="pay-body">
                        <Icon name="order"/>
                        <ul>
                          <li>{pay_flag?"您的订单已提交成功  !":"请立即支付  !"}</li>
                          <li>提醒：请在24小时内完成付款，超出24小时未付款的订单系统将自动取消 。</li>
                          <li>
                            <span>应付金额：</span>
                            <span>￥{price[0].account.toFixed(2)}</span>
                           <span> ( 为保证您能及时提货，请尽快付款 )</span>
                          </li>
                        </ul>
                        {this.renderPays()}
                        <Button width={143} height={49} text="稍后支付" className="white" style={button_style}
                                onClick={()=>{browserHistory.push({pathname:'/account/order-center/order-list.html'})}}/>
                        <Button width={143} height={49} text="立即支付" className="red" disabled={buttonClick}
                                onClick={()=>{this.thirtyPay()}}/>
                      </div>
                    </div>;
                  case 'error':
                    return <h1 className="error">网页出错</h1>
                }
              })()
            }

          </div>
        </div>
    );

  }
}