/**
 * Created by ZWL on 2017/2/27.
 */
import React, { Component } from 'react'
import { Link, IndexLink, browserHistory } from 'react-router'

export default class AlipayReturn extends Component {
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  componentDidMount() {
    let { out_trade_no , trade_no , trade_status , total_fee } =  this.props.location.query , order = {"orderId":out_trade_no}

    if (trade_status == "TRADE_FINISHED" || trade_status == "TRADE_SUCCESS") {
      fetch(`${tpService}/thirdParty/findOrderBatchStatusAndConfirmStatus?orderId=${out_trade_no}&type=${3}`, {
        method: "get",
        headers: Hmall.getHeader({
          "Content-Type": "application/json"
        }),
      })
          .then(Hmall.convertResponse('json', this))
          .then(json=> {
            let {resp ,success } = json
            if (success) {
              if (resp[0] == 1) {
                fetch(`${tpService}/thirdParty/updOrderBatchStatus?orderId=${out_trade_no}&type=${3}&trade_no=${trade_no}`, {
                  method: "get",
                  headers: Hmall.getHeader({
                    "Content-Type": "application/json"
                  }),
                })
                    .then(Hmall.convertResponse('json', this))
                    .then(json=> {
                      if (json.success) {
                        fetch(`${odService}/order/updateOrderPayed/${out_trade_no}/3`, {
                          method: "POST",
                          headers: Hmall.getHeader({
                            "Content-Type": "application/json"
                          }),
                        })
                            .then(Hmall.convertResponse('json', this))
                            .then(json=> {
                              if (json.success) {
                                browserHistory.push({
                                  pathname: '/payment/pay-success.html',
                                  state: {account: total_fee, orderIds: out_trade_no}
                                });
                              }else{
                                browserHistory.push({pathname:'/payment/pay-failed.html',state:{orderIds:out_trade_no}})
                              }})
                            .catch(Hmall.catchHttpError(e=> {
                              ()=>{
                                browserHistory.push({pathname:'/payment/pay-failed.html',state:{orderIds:out_trade_no}})
                              }
                            }))
                      }else {
                        browserHistory.push({pathname:'/payment/pay-failed.html',state:{orderIds:out_trade_no}})
                      }
                    })
                    .catch(Hmall.catchHttpError(e=> {()=>{
                      browserHistory.push({pathname:'/payment/pay-failed.html',state:{orderIds:out_trade_no}})
                    }
                    }))
              }else if(resp[0] == 2){
                browserHistory.push({
                  pathname: '/payment/pay-success.html',
                  state: {account: total_fee, orderIds: out_trade_no}
                });
              }else if(resp[0] == 3){
                browserHistory.push({pathname:'/payment/pay-failed.html',state:{orderIds:out_trade_no}})
              }
            }else{
              browserHistory.push({pathname:'/payment/pay-failed.html',state:{orderIds:out_trade_no}})
            }
          })
          .catch(Hmall.catchHttpError(
              ()=> {
                browserHistory.push({pathname:'/payment/pay-failed.html',state:{orderIds:out_trade_no}})
              }
          ))
    }
  }

  constructor(props) {
    super(props);
    this.state = {}
  }

  render() {
    return(<div></div>)
  }
}
