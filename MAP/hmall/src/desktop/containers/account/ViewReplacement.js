import React, { Component } from 'react';
import Form, { FormGroup } from '../../components/Form';
import TextBox from '../../components/TextBox';
import Button from '../../components/Button';
import ProductAndOrderViewer from '../../components/ProductAndOrderViewer';
import { Panel,Row,Col } from '../../components/Layout';
import Moment from 'moment';
import { browserHistory } from 'react-router';
import { TabPanel, Tab } from '../../components/Tab';

export default class ViewReplacement extends Component {

  fetchInfo() {
    let replaceId = this.props.location.query.replaceId;
    fetch(`${odService}/replaceSingleDetail/${replaceId}`, {
      headers: Hmall.getHeader({})
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          if (json.success) {
            this.setState({
              fetch_status: 'init',
              resp: json.resp,
              b_logisticsNumber: json.resp[0].back.b_logisticsNumber,
              b_logisticsCompanies: json.resp[0].back.b_logisticsCompanies
            });

          } else {
            this.setState({fetch_status: 'init'});
            if (json.msgCode == "OD_INFO_001") {
              alert("必输字段为空");
            } else if (json.msgCode == "OD_REFUND_006") {
              alert("replaceId对应的换货表信息为空");
            } else if (json.msgCode == "OD_INFO_006") {
              alert("获取订单收货地址出错");
            }
          }
        })
        .catch(Hmall.catchHttpError(()=> {
          this.setState({fetch_status: 'error'});
        }))
  }

  componentDidMount() {
    this.fetchInfo();
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }

  constructor(props) {
    super(props);
    this.state = {
      fetch_status: 'uninit',
      resp: [],
      other: false,
      b_logisticsCompanies: '',
      b_logisticsNumber: '',
      otherCompanies: ''
    }
  }


  //换退状态
  getReplaceStatus(replaceStatus) {
    switch (replaceStatus) {
      case 'WAIT_BUYER_RETURN_GOODS':
        return '待买家上传物流信息';
      case 'WAIT_SELLER_CONFIRM_GOODS' :
        return '待卖家收货';
      case 'CANCELLED':
        return '换退取消';
      case 'CLOSED' :
        return '超时关闭';
      case 'SELLER_HAS_CONFIRMED_GOODS':
        return '换退已入库';
      default:
        return '无状态';
    }
  }


  //换发状态
  getRefundStatus(refundStatus) {
    switch (refundStatus) {
      case 'WAIT_BUYER_RETURN_GOODS':
        return '待买家寄回商品';
      case 'WAIT_SELLER_CONFIRM_GOODS':
        return '待卖家收货';
      case 'WAIT_SELLER_SEND_GOODS':
        return '待发货';
      case 'WAIT_BUYER_CONFIRM_GOODS':
        return '已发货';
      case 'WAIT_SELLER_CONFIRM_GOODS':
        return '待卖家收货';
      case 'WAIT_BUYER_CONFIRM':
        return '待买家确认';
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
      case 'TRADE_FINISHED':
        return '交易成功';
      case 'TRADE_CLOSED_BY_UNIQLO':
        return '交易关闭';
      case 'TRADE_CLOSED':
        return '交易关闭';
      default :
        return '无状态';
    }
  }


  handleExpressInfo(e) {
    e.preventDefault();
    let data = {};
    let { logisticsCompanies,logisticsNumber,codeId,codeType,logistic } = e.target;
    if (logisticsNumber.value == "") {
      alert("请输入快递单号");
    } else if (logisticsCompanies.value == "") {
      alert("请输入物流公司")
    } else {
      if (logisticsCompanies.value == "其他") {
        data = {
          logisticsNumber: logisticsNumber.value,
          code: codeId.value,
          codeType: codeType.value,
          logisticsCompanies: logistic.value
        }
      } else {
        data = {
          logisticsNumber: logisticsNumber.value,
          code: codeId.value,
          codeType: codeType.value,
          logisticsCompanies: logisticsCompanies.value
        }
      }
      fetch(`${odService}/uploadLogisticsInfo`, {
        method: 'post',
        headers: Hmall.getHeader({"Content-Type": "application/json"}),
        body: JSON.stringify(data)
      })
          .then(Hmall.convertResponse('json', this))
          .then(json => {
            if (json.success) {
              alert("提交成功");
              this.fetchInfo();
            }
          })
          .catch(Hmall.catchHttpError(()=> {
            alert("服务器繁忙");
          }))
    }

  }


  cancleApply(replaceId) {
    if (confirm("是否取消换货?")) {
      fetch(`${odService}/cancelRequestReplace/${replaceId}`, {
        method: 'post',
        headers: Hmall.getHeader({})
      })
          .then(Hmall.convertResponse('json', this))
          .then(json => {
            if (json.success) {
              browserHistory.push('/account/order-center/replace-list.html');
            } else {
              if (json.msgCode == "OD_INFO_001") {
                alert("必输字段为空");
              } else if (json.msgCode == "OD_REFUND_006") {
                alert("replaceId对应的换货表信息为空");
              } else if (json.msgCode == "OD_INFO_004") {
                alert("修改订单商品标志出错");
              }
            }
          })
          .catch(Hmall.catchHttpError(()=> {
            alert("服务器繁忙");
          }))
    }
  }

  changeLogistic(e) {
    let { value } = e.target;
    if (value == '其他') {
      this.setState({
        other: true,
        b_logisticsCompanies: value
      })
    } else {
      this.setState({
        other: false,
        b_logisticsCompanies: value
      });
    }
  }

  returnGood() {
    let replaceId = this.props.location.query.replaceId;
    fetch(`${odService}/refund/replaceToRefund/` + replaceId, {
      method: 'post',
      headers: Hmall.getHeader({})
    })
        .then(Hmall.convertResponse('json', this))
        .then((json)=> {
          if (json.success) {
            alert("转退成功");
            this.fetchInfo();
          } else {
            alert(json.msg);
          }
        })
  }

  getReturnButton() {
    let resp = this.state.resp[0],
        { renewal,back} = resp || [], { r_status  } = renewal, { b_status } = back;
    if ((r_status == "WAIT_SELLER_SEND_GOODS" || r_status == "WAIT_SELLER_CONFIRM_GOODS") && (b_status == "WAIT_SELLER_CONFIRM_GOODS" || b_status == "SELLER_HAS_CONFIRMED_GOODS")) {
      return <Button className="red return-goods" text="转为退货" onClick={()=>{this.returnGood()}} width={150} height={40}/>
    }
  }

  showLogistic(b_status, b_replaceId) {
    let {  b_logisticsNumber , b_logisticsCompanies } = this.state
    switch (b_status) {
      case 'CLOSED':
        return <div style={{height:"50px"}}></div>;
      case 'CANCELLED':
        return <div style={{height:"50px"}}></div>;
      case "WAIT_BUYER_RETURN_GOODS":
        return <Form onSubmit={(e) => this.handleExpressInfo(e)}>
          <FormGroup>
            <span>物流公司：<span className="notice"></span></span>
            <select value={b_logisticsCompanies} onChange={(e)=>this.changeLogistic(e)} name="logisticsCompanies"
                    width={250} height={30}>
              <option>请选择物流公司</option>
              <option value="申通快递">申通快递</option>
              <option value="圆通快递">圆通快递</option>
              <option value="顺丰速运">顺丰速运</option>
              <option value="中国邮政">中国邮政</option>
              <option value="百世汇通">百世汇通</option>
              <option value="天天快递">天天快递</option>
              <option vlaue="德邦物流">德邦物流</option>
              <option value="其他">其他</option>
            </select>
            <TextBox value={this.state.otherCompanies} placeholder="请输入物流公司" className="logistic"
                     style={{display:this.state.other?'':'none'}} name="logistic" width={250}
                     height={30}></TextBox>
          </FormGroup>
          <input type="hidden" name="codeId" value={b_replaceId}></input>
          <input type="hidden" name="codeType" value="RP"></input>
          <FormGroup>
            <span>物流单号：<span className="notice"></span></span>
            <TextBox value={b_logisticsNumber} name="logisticsNumber" width={250} height={30}></TextBox>
          </FormGroup>
          <FormGroup>
            <Button onClick={() =>this.cancleApply(b_replaceId)} className="button-top cancle_button" text="取消申请"
                    width={150} height={40}></Button>
            <Button type="submit" className="red button-top" text="提交物流信息" width={150} height={40}></Button>
          </FormGroup>
        </Form>;
      default:
        return <ul className="exchange-info">
          <li>物流公司：{ b_logisticsCompanies }</li>
          <li>物流单号：{ b_logisticsNumber }</li>
          <li>
            {this.getReturnButton()}
          </li>
        </ul>
    }
  }

  render() {
    let resp = this.state.resp[0],
        { renewal,back} = resp || [],
        { r_size,r_style,r_orderId,r_status ,r_name, r_address } = renewal || [],
        { b_name,b_price,b_quantity,b_orderId,b_size,b_creationTime,b_replaceAddr,b_replaceId,b_mainPic,b_style,b_status,b_productCode } = back || [];
    let refundInfo1 = this.state.resp[1] || [],
        paymentTime = refundInfo1.paymentTime || [],
        renewalAddress = r_address ? r_address.consignee + '  ' + r_address.provinceName + " " + r_address.cityName + " " + r_address.districtName + " " + r_address.address + '  ' + r_address.mobilenumber : "";
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
                      <ProductAndOrderViewer shopName={"换货商品"} productCode={b_productCode} price={b_price} name={b_name}
                                             orderId={b_orderId} quantity={parseInt(b_quantity)} mainPic={b_mainPic}
                                             style={b_style} size={b_size}
                                             paymentTime={paymentTime}></ProductAndOrderViewer>
                    </Col>
                    <Col>
                      <Panel>
                        <TabPanel id="view_replacement">
                          <Tab title="换货">
                            <div className="order_change_detail">
                              <div className="span_position">
                                <span>换发单详情</span>
                              </div>
                              <div className="order_change_table">
                                <table>
                                  <tbody>
                                  <tr>
                                    <td className="td_size">换发状态：</td>
                                    <td>{this.getRefundStatus(r_status)}</td>
                                  </tr>
                                  <tr>
                                    <td className="td_size">换发编号：</td>
                                    <td>{r_orderId}</td>
                                  </tr>
                                  <tr>
                                    <td className="td_size">申请时间：</td>
                                    <td>{Moment(parseInt(b_creationTime)).format(HmallConfig.date_format)}</td>
                                  </tr>
                                  <tr>
                                    <td className="td_size">更换商品：</td>
                                    <td>{r_name} {r_size} {r_style}</td>
                                  </tr>
                                  <tr>
                                    <td className="td_size">收货地址：</td>
                                    <td>{renewalAddress}</td>
                                  </tr>
                                  </tbody>
                                </table>
                              </div>
                              <div className="change_return_detail">
                                <span>换退单详情</span>
                              </div>
                              <div className="order_change_table">
                                <table>
                                  <tbody>
                                  <tr>
                                    <td className="td_size">换退状态：</td>
                                    <td>{this.getReplaceStatus(b_status)}</td>
                                  </tr>
                                  <tr>
                                    <td className="td_size">换退编号：</td>
                                    <td>{b_replaceId}</td>
                                  </tr>
                                  <tr>
                                    <td className="td_size">申请时间：</td>
                                    <td>{Moment(parseInt(b_creationTime)).format(HmallConfig.date_format)}</td>
                                  </tr>
                                  <tr>
                                    <td className="td_size">寄回地址：</td>
                                    <td>{HmallConfig.contact + "  " + b_replaceAddr.address + " " + b_replaceAddr.contactNumber}</td>
                                  </tr>
                                  </tbody>
                                </table>
                                {
                                  this.showLogistic(b_status, b_replaceId)
                                }
                              </div>
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