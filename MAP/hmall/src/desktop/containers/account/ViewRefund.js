import React, { Component } from 'react';
import Button from '../../components/Button';
import { Link, browserHistory } from 'react-router';
import ProductAndOrderViewer from '../../components/ProductAndOrderViewer';
import { Panel,Row,Col } from '../../components/Layout';
import { TabPanel, Tab } from '../../components/Tab';
import Moment  from 'moment';
import TextArea from '../../components/TextArea';

export default class ViewRefund extends Component {

  getInfo() {
    let refundId = this.props.location.query.refundId;
    fetch(`${odService}/refundSingleDetail/${refundId}`, {
      headers: Hmall.getHeader({})
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          if (json.success) {
            let { resp = [{}] } = json ,{ compensation, totalFee, maxCompensateRation, compensateRation, detailId} = resp[0],
                summary = (totalFee*compensateRation/100).toFixed(2),
                state = {
                  fetch_status: 'init',
                  resp: resp,
                  chooseReason: resp[0].refundReason,
                  showFlag: compensation!="0",
                  paymentAmount: summary>maxCompensateRation?maxCompensateRation.toString():summary,
                };
            if (resp[0].refundStatus == "WAIT_BUYER_CONFIRM") {
              fetch(`${odService}/notdeliverReason/`+detailId,
                  {
                    method: 'GET',
                    headers: Hmall.getHeader({"Content-Type": "Application/json"})
                  })
                  .then(Hmall.convertResponse('json', this))
                  .then(json=> {
                    if (json.success) {
                      this.setState({notdeliverReason: json.resp});
                    }
                  })
            }
            this.setState(state);
          } else {
            this.setState({fetch_status: 'init'});
          }
        })
        .catch(Hmall.catchHttpError(()=> {
          this.setState({fetch_status: 'error'});
        }))
  }

  componentDidMount() {
    this.getInfo();
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }

  constructor(props) {
    super(props);
    this.state = {
      paymentAmount: "",
      showFlag: true,
      fetch_status: 'uninit',
      resp: [],
      respTwo: [],
      notdeliverReason: [],
      chooseReason: "",
    }
  }

  returnOrderList() {
    browserHistory.push('/account/order-center/order-list.html');
  }

  getRefundStatus(refundStatus) {
    switch (refundStatus) {
      case 'WAIT_SELLER_AGREE_TO_RETURN':
        return '待退货审核';
      case 'WAIT_BUYER_RETURN_TO_STORE':
        return '待买家送回门店';
      case 'WAIT_BUYER_RETURN_GOODS_SROTE':
        return '待买家寄回门店';
      case 'WAIT_BUYER_RETURN_GOODS_WH':
        return '待买家寄回总仓';
      case 'WAIT_SELLER_CONFIRM_GOODS':
        return '待卖家收货';
      case 'WAIT_BUYER_CONFIRM':
        return '协商中待买家确认';
      case 'WAIT_SELLER_AGREE':
        return '待退款审核';
      case 'WAIT_REFUND_TO_BUYER':
        return '待退款';
      case 'SELLER_REFUND_AUDIT':
        return '已退款审核';
      case 'SELLER_REFUSE_BUYER':
        return '已拒绝';
      case 'SUCCESS':
        return '退款成功';
      case 'CLOSED':
        return '退款关闭';
      case 'FAIL':
        return '退款失败';
      default :
        return '无状态';
    }
  }

  getOptions(notdeliverReason) {
    return notdeliverReason.map((item, i)=> {
      return <option key={i} value={item.value}>{item.meaning}</option>
    })
  }

  changeReason(event) {
    let val  = event.target.value, { notdeliverReason } = this.state;
    notdeliverReason.map( (item, i)=>{
      if(item.value == val ){
        this.setState({showFlag: item.relatedcomp=="Y"});
      }
    } )
    this.setState({chooseReason: val});
  }

  confimInfo() {
    let { chooseReason,resp,paymentAmount,showFlag }=this.state,
        refundInfo = this.refs.refundInfo.value, { oid,productId } = this.props.location.query;
    fetch(`${odService}/refund/updateRefundStatusOfConfirm`, {
    //fetch(`http://10.211.52.221:5555/hmall-od-service/refund/updateRefundStatusOfConfirm`, {
      method: "post",
      headers: Hmall.getHeader({"Content-Type": "application/json"}),
      body: JSON.stringify({
        refundType: "NOTDELIVERREFUND",
        refundReason: chooseReason,
        refundDesc: refundInfo,
        refundId: resp[0].refundId,
        compensateMoney: showFlag?paymentAmount:"",
      })
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          if (json.success) {
            this.getInfo();
            alert("修改成功");
          } else {
            alert(json.msg);
          }
        })
  }


  render() {
    let resp = this.state.resp[0] || [], { notdeliverReason, chooseReason,showFlag, paymentAmount } = this.state;
    let {compensation,creationTime,name,paymentTime,quantity, reason, refundAmount, refundDesc, refundReason, refundId, refundStatus,size, stylePic, styleText, unitPrice, productCode,orderId } = resp || [],
        flag = refundStatus == "WAIT_BUYER_CONFIRM";
    return (
        <Row>
          {
            (()=> {
              switch (this.state.fetch_status) {
                case 'uninit':
                  return <div className="loading"></div>;
                case 'init':
                  return this.state.resp.length ? <Row>
                    <Col className="orderViewer">
                      <ProductAndOrderViewer shopName={"申请退款商品"} productCode={productCode} price={unitPrice}
                                             orderId={orderId} quantity={quantity} name={name} mainPic={stylePic}
                                             style={styleText} size={size}
                                             paymentTime={paymentTime}></ProductAndOrderViewer>
                    </Col>
                    <Col>
                      <Panel>
                        <TabPanel id="viewrefund_div">
                          <Tab title="退款">
                            <div className="refund_details">
                              <ul>
                                <li>退款状态：<b>{this.getRefundStatus(refundStatus)}</b></li>
                                <li>退款编号：{refundId}</li>
                                <li>申请时间：{Moment(Number(creationTime)).format(HmallConfig.datetime_format)}</li>
                                <li>退款原因：{flag ?
                                    <select value={chooseReason} onChange={(event)=>{this.changeReason(event)}}>
                                      {this.getOptions(notdeliverReason)}
                                    </select> : reason}</li>
                                <li>退款金额：{"￥" + refundAmount}</li>
                                <li style={{display: showFlag?"":"none" }}>赔付金额：{"￥" + paymentAmount}</li>
                                <li>退款说明：{flag ? <textArea ref="refundInfo" className="text_about"
                                                           defaultValue={refundDesc}/> : refundDesc}</li>
                                <li className="notice-word"><span>温馨提示：</span>退款将按照原支付方式退回</li>
                              </ul>
                            </div>
                            <div className="return_my_order">
                              <Button text="确定" width={150} height={40} onClick={()=>{this.confimInfo()}}
                                      className="confim_button" style={{display:flag?"":"none"}}></Button>
                              <Button onClick={() => this.returnOrderList()} text="返回我的订单" width={150} height={40}
                                      className="red"></Button>
                            </div>
                          </Tab>
                        </TabPanel>
                      </Panel>
                    </Col>
                  </Row> : <h1 className="info">暂无详情</h1>;
                case 'error':
                  return <h1 className="error">网页出错</h1>;
              }
            })()
          }
        </Row>
    );
  }
}