/**
 * Created by ZWL on 2016/10/24.
 */
import React, { Component } from 'react'
import Flow from '../../components/Cart/Flow'
import { Link, IndexLink, browserHistory } from 'react-router'
import Button from '../../components/Button';

export default class PayFailed extends Component{
  render() {
      let { state } = this.props.location
      if(state){
         let { orderIds } = state
          return (
              <div className="payFailed">

                  <div className="div_flow">
                      <Flow/>
                  </div>
                  <div className="payBody">
                      <h1>X 支付未成功！</h1>
                      <span>提醒： 请在24小时内完成支付，超过24小时未付款的订单系统将会自动取消。</span>
                  </div>
                  <div className="payButton">
                      <Button className="white" width={140} height={50} text="稍后支付"
                              onClick={()=>{browserHistory.push({pathname:'/'})}}/>
                      <Button className="red" width={140} height={50} text="立即支付"
                              onClick={()=>{browserHistory.push({pathname:'/payment/order-payment.html',state:{order_list:orderIds,success:true}})}}/>
                  </div>
              </div>
          );
      }
  }
}
