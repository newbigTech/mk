/**
 * Created by ZWL on 2016/10/24.
 */
import React, { Component } from 'react'
import Flow from '../../components/Cart/Flow'
import { Link, IndexLink, browserHistory } from 'react-router'
import Button from '../../components/Button';

export default class PaySuccess extends Component{
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  componentWillMount() {
    let { state } = this.props.location ,orderId = "";
    if (state) {
      let { account , orderIds } = state , content = { "orderid":"","orderprice":"" };
      content.orderid = orderIds
      content.orderprice = account
      Hmall.loadXiaoNeng(content);
      fetch(`${odService}/order/queryForChildOrders/${orderIds}`, {
        method: "get",
        headers: Hmall.getHeader({
          "Content-Type": "application/json"
        }),
      })
          .then(Hmall.convertResponse('json', this))
          .then(json=> {
            let { resp, success , msgCode } = json
            if (success) {
              resp.forEach((r, i)=> {
                orderId = orderId + r + "  "
              })
              this.setState({account:account,orderId:orderId,fetch_status :"init"})
            } else {
              if (msgCode == "OD_UPDATE_08") {
                alert("重复支付！")
              }
              alert("支付失败")
            }
          })
          .catch(Hmall.catchHttpError(
                ()=> {
                  this.setState({fetch_status: "error"})
                }
            ))
    }
  }
  constructor(props) {
    super(props);
    this.state = {
      account : 0,
      orderId : "",
      fetch_status :"uninit"
    }
  }
  render() {
    let { orderId , account , fetch_status} = this.state

    return (<div>{
          (() => {
            switch (fetch_status) {
              case 'uninit':
                return <div className="paySuccess">
                  <div className="loading"></div>
                </div>;
              case 'init':
                return <div className="paySuccess">
                  <div className="div_flow">
                    <Flow/>
                  </div>
                  <div className="payBody">
                    <h1>√  恭喜您，支付成功！</h1>
                    <span>您已经成功付款</span><a>￥{account}</a><span>订单编号：{orderId}</span>
                  </div>
                  <div className="payButton">
                    <Button className="white" width={140} height={50} text="查看订单"
                            onClick={()=>{browserHistory.push({pathname:'/account/order-center/order-list.html'})}}/>
                    <Button className="red" width={140} height={50} text="继续购物"
                            onClick={()=>{browserHistory.push({pathname:'/'})}}/>
                  </div>
                </div>
              case 'error':
                return <h1 className="error">网页出错</h1>
            }
          })()
        }</div>
    );
  }
}
