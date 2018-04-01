import React, { Component } from 'react';
import Form, { FormGroup } from '../../components/Form';
import TextBox from  '../../components/TextBox';
import Button from '../../components/Button';
import { Panel,Row,Col } from '../../components/Layout';
import ProductAndOrderViewer from '../../components/ProductAndOrderViewer';
import Moment from 'moment';
import { browserHistory } from 'react-router';
import { TabPanel, Tab } from '../../components/Tab';
import ComboBox from '../../components/ComboBox';
import PicUpload from '../../components/PicUpload';
import Radio from '../../components/Radio';

export default class ViewRefundReturn extends Component {

  fetchRefundInfo() {
    let refundId = this.props.location.query.refundId;
    fetch(`${odService}/refundSingleDetail/${refundId}`, {
      headers: Hmall.getHeader({})
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          if (json.success) {
            let { resp = [{}] } = json, { compensation, totalFee, maxCompensateRation, compensateRation, detailId} = resp[0],
                summary = (totalFee * compensateRation / 100).toFixed(2);
            if (resp[0].refundStatus == "WAIT_BUYER_CONFIRM") {
              fetch(`${odService}/shippingRefundReason/` + detailId,
                  {
                    method: 'GET',
                    headers: Hmall.getHeader({"Content-Type": "Application/json"})
                  })
                  .then(Hmall.convertResponse('json', this))
                  .then(json=> {
                    if (json.success) {
                      this.setState({shippingRefundReason: json.resp});
                    }
                  })
            }
            this.setState({
              fetch_status: 'init',
              resp: json.resp,
              logisticsCompanies: json.resp[0].logisticsCompanies,
              logisticsNumber: json.resp[0].logisticsNumber,
              showFlag: compensation != "0",
              summary: Number(resp[0].refundAmount),
              paymentAmount: summary > maxCompensateRation ? maxCompensateRation.toString() : summary,
              oldPaths: resp[0].paths.slice(),
              chooseReason: resp[0].refundReason,
              chooseQuantity: resp[0].returnQuantity,
              distribution: resp[0].returnWay,
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
          this.setState({
            fetch_status: 'error'
          });
        }))
  }

  componentDidMount() {
    this.fetchRefundInfo();
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }

  constructor(props) {
    super(props);
    this.state = {
      fetch_status: 'uninit',
      shippingRefundReason: [],
      resp: [],
      other: false,
      logisticsCompanies: '',
      logisticsNumber: '',
      canChangeLogistic: false,
      otherCompanies: '',
      newPaths: [],
      oldPaths: [],
    }
  }

  getImage(img) {
    this.setState({newPaths: img})
  }

  deletePic(path) {
    let { oldPaths } = this.state;

    oldPaths.map((item, i)=> {
      if (item == path) {
        oldPaths.splice(i, 1);
      }
    })
    this.setState({oldPaths: oldPaths});
  }

  getOptions(notdeliverReason) {
    return notdeliverReason.map((item, i)=> {
      return <option key={i} value={item.value}>{item.meaning}</option>
    })
  }

  changeSummary(e) {
    let val = e.target.value, { resp, chooseQuantity } = this.state, { returnMoney, unitPrice } = resp[0],
        money = returnMoney > unitPrice * chooseQuantity ? unitPrice * chooseQuantity : returnMoney;
    if (!isNaN(val) && val <= money) {
      this.setState({summary: val});
    } else if (val == "") {
      this.setState({summary: ""});
    } else if (val > money) {
      this.setState({summary: money});
    }
  }

  blurSummary(e) {
    let val = e.target.value, { resp, chooseQuantity } = this.state, { returnMoney, unitPrice } = resp[0],
        money = returnMoney > unitPrice * chooseQuantity ? unitPrice * chooseQuantity : returnMoney;
    if (val == "") {
      this.setState({summary: money});
    }
  }

  changeReason(event) {
    let val = event.target.value, { shippingRefundReason } = this.state;
    shippingRefundReason.map((item, i)=> {
      if (item.value == val) {
        this.setState({showFlag: item.relatedcomp == "Y"});
      }
    })
    this.setState({chooseReason: val});
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

  getRefundType(returnWay) {
    switch (returnWay) {
      case 'EXPR_WAREHOUSE':
        return '快递退回 ';
      case 'EXPR_STORE':
        return '门店退回';
      case 'SELF_STORE':
        return '门店寄回';
      default :
        return '无';
    }
  }

  showImg(path) {
    if (path && path.length > 0) {
      return path.map((item, i)=> {
        return (<img className="img-size-show" src={item}></img>);
      })
    } else {
      return (<span>未上传凭证</span>);
    }
  }

  changeQuantity(e) {
    let val = e.target.value, { resp } = this.state, {returnNum} = resp[0];
    if (!isNaN(val) && val > 0 && val < returnNum) {
      this.setState({chooseQuantity: parseInt(val)})
    } else if (val == "") {
      this.setState({chooseQuantity: ""})
    } else if (val >= returnNum) {
      this.setState({chooseQuantity: returnNum});
    }
  }

  blurQuantity(e) {
    let val = e.target.value, { resp, summary } = this.state, {returnNum, returnMoney, unitPrice } = resp[0],
        money = returnMoney > unitPrice * val ? unitPrice * val : returnMoney;
    if (val == "") {
      this.setState({chooseQuantity: returnNum});
    } else if (money < summary) {
      this.setState({summary: money});
    }
  }

  getReturnType(e) {
    this.setState({distribution: e.target.value});
  }

  getRadioType() {
    let { resp = [] } = this.state, {distribution, store} = resp[0], items = [];
    items.push(<li>
      <Radio choose={this.state.distribution} onChange={(e) => this.getReturnType(e)} name="return_method"
             value="EXPR_WAREHOUSE"></Radio><span>快递退回</span>
    </li>);
    if (distribution == "EXPRESS") {
      if (store) {
        if (store.type == "P") {
          items.push(<li>
            <Radio choose={this.state.distribution} onChange={(e) => this.getReturnType(e)} name="return_method"
                   value="EXPR_STORE"></Radio><span>门店寄回</span>
          </li>)
        }
      }
    } else {
      items.push(<li>
        <Radio choose={this.state.distribution} onChange={(e) => this.getReturnType(e)} name="return_method"
               value="EXPR_STORE"></Radio><span>门店寄回</span>
      </li>);
      items.push(
          <li>
            <Radio choose={this.state.distribution} onChange={(e) => this.getReturnType(e)} name="return_method"
                   value="SELF_STORE"></Radio><span>门店退回</span>
          </li>)
    }
    return items;
  }

  getReturnInfo() {
    let { resp = [] } = this.state, {warehouseList} = resp[0];
    return warehouseList.map((item, i)=> {
      return <span>{HmallConfig.contact} {item.address} {item.contactNumber}</span>;
    })
  }

  getAddressInfo() {
    let { distribution,resp } = this.state, { store } = resp;

    switch (distribution) {
      case "EXPR_WAREHOUSE":
        return <span>退回地址信息：{this.getReturnInfo()}</span>
      case "EXPR_STORE":
        return <span>寄回门店地址：{store.provinces + store.displayName} {store.contacts} {store.contactNumber}</span>
      case "SELF_STORE":
        <span>门店退回地址：{store.provinces + store.displayName} {store.openingTime}-{store.closingTime} {store.contactNumber}</span>
    }
  }

  refundInfo() {
    let { resp, showFlag, paymentAmount,chooseReason, shippingRefundReason,summary,chooseQuantity  } = this.state, refundInfo = resp[0] || [],
        { address,creationTime, paths, name, orderId, payedTime, productCode, productId, quantity, reason,refundAmount, refundDesc, refundId,
            refundReason, refundStatus, returnQuantity, returnNum,returnMoney,
            size, stylePic, styleText, unitPrice, returnWay} = refundInfo,
        { contactNumber } = address, flag = refundStatus == "WAIT_BUYER_CONFIRM";

    return (
        <ul>
          <li>
            <span>退款状态：</span>
            <span>{this.getRefundStatus(refundStatus)}</span>
          </li>
          <li>
            <span>退款编号：</span>
            <span>{refundId}</span>
          </li>
          <li>
            <span>申请时间：</span>
            <span>{Moment(Number(creationTime)).format(HmallConfig.date_format)}</span>
          </li>
          <li>
            <span>退款原因：</span>
            {flag ? <select value={chooseReason} onChange={(event)=>{this.changeReason(event)}}>
              {this.getOptions(shippingRefundReason)}
            </select> : reason}
          </li>
          <li>
            <span>退货数量：</span>
            {flag ? <div className="return-money"><input type="text" className="number-input" value={chooseQuantity}
                                                         onChange={(e)=>{this.changeQuantity(e)}}
                                                         onBlur={(e)=>{this.blurQuantity(e)}}/>
              <span className="remark">(最多{returnNum}件)</span>
            </div>
                : returnQuantity}
          </li>
          <li>
            <span>退款金额：</span>
            {flag ? <div className="return-money">
              <input type="text" className="number-input" value={summary}
                     onChange={(e)=>{this.changeSummary(e)}} onBlur={(e)=>{this.blurSummary(e)}}/>
              <span
                  className="remark">(最多￥{returnMoney > chooseQuantity * unitPrice ? chooseQuantity * unitPrice : returnMoney})</span>
            </div> : "￥" + refundAmount}
          </li>
          <li style={{ display: showFlag?"":"none" }}>
            <span>赔付金额：</span>
            <span>{"￥" + paymentAmount}</span>
          </li>
          <li>
            <span>退款说明：</span>
            {flag ? <textarea ref="refundInfo" className="text-about"
                              defaultValue={refundDesc}/> : refundDesc}
          </li>
          <li>
            <span>已传凭证：</span>
            {flag ? <div className="picupload-position">
              <PicUpload getImage={(img) =>{ this.getImage(img)}} defaultPath={paths}
                         deletePic={(path)=>{this.deletePic(path)}}></PicUpload>
              <p>每张图片大小不超过10M，最多3张，支持GIF、JPG、PNG、BMP格式</p>
            </div> : this.showImg(paths)}
          </li>
          <li>
            <span>退回方式：</span>
            <span>{flag ? <ul className="radio-ul">{this.getRadioType()}</ul> : this.getRefundType(returnWay)}</span>
          </li>
          <li>
            {flag ? this.getAddressInfo() : "退回地址信息：" + HmallConfig.contact + " " + address.address + " " + contactNumber}
          </li>
        </ul>
    );
  }

  cancleApply(refundId) {
    if (confirm("是否取消退货退款？")) {
      fetch(`${odService}/cancelRequestRefund/${refundId}`, {
        method: 'post',
        headers: Hmall.getHeader({})
      })
          .then(Hmall.convertResponse('json', this))
          .then(json => {
            if (json.success) {
              browserHistory.push('account/order-center/refund-return-list.html');
            } else {
              if (json.msgCode == "OD_INFO_001") {
                alert("必输字段为空");
              } else if (json.msgCode == "OD_REFUND_005") {
                alert("refundId对应的退款表信息为空");
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

  handleLogistic(e) {
    e.preventDefault();
    let { codeId, codeType, logisticsCompanies,logisticsNumber,logistic } = e.target,
        data = {};
    if (logisticsCompanies.value == "其他") {
      data = {
        code: codeId.value,
        codeType: codeType.value,
        logisticsNumber: logisticsNumber.value,
        logisticsCompanies: logistic.value
      }
    } else {
      data = {
        code: codeId.value,
        codeType: codeType.value,
        logisticsNumber: logisticsNumber.value,
        logisticsCompanies: logisticsCompanies.value
      }
    }
    if (confirm("是否上传物流信息？")) {
      fetch(`${odService}/uploadLogisticsInfo`,
          {
            method: 'post',
            headers: Hmall.getHeader({'Content-Type': 'application/json'}),
            body: JSON.stringify(data)
          })
          .then(Hmall.convertResponse('json', this))
          .then(json=> {
            if (json.success) {
              alert("上传物流信息成功");
              this.setState({canChangeLogistic: true});
              this.fetchRefundInfo();
            } else {
              alert("上传物流信息失败");
            }
          })
          .catch(Hmall.catchHttpError(()=> {
            alert("服务器繁忙");
          }))
    }
  }

  changeLogistic(e) {
    let { value } = e.target;
    if (value == "其他") {
      this.setState({
        other: true,
        logisticsCompanies: value
      });
    } else {
      this.setState({
        other: false,
        logisticsCompanies: value
      });
    }
  }

  confimInfo() {
    let { chooseReason,summary,oldPaths, newPaths,showFlag,paymentAmount,distribution,resp,chooseQuantity }=this.state,
        refundInfo = this.refs.refundInfo.value, { warehouseList,store } = resp[0],
        { refundId } = this.props.location.query;
    let refundAddr = {};
    if (distribution == "EXPR_WAREHOUSE") {
      refundAddr = warehouseList[0];
    } else {
      refundAddr = store;
    }
    fetch(`${odService}/refund/updateRefundStatusOfConfirm`, {
      //fetch(`http://10.211.52.221:5555/hmall-od-service/refund/updateRefundStatusOfConfirm`, {
      method: "post",
      headers: Hmall.getHeader({"Content-Type": "application/json"}),
      body: JSON.stringify({
        refundType: "REFUNDS",
        refundReason: chooseReason,
        refundDesc: refundInfo,
        refundAmount: summary,
        refundId: refundId,
        oldPaths,
        newPaths,
        compensateMoney: showFlag ? paymentAmount : "",
        returnWay: distribution,
        refundAddr,
        returnQuantity: chooseQuantity,
      })
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          if (json.success) {
            this.fetchRefundInfo();
            alert("修改成功");
          } else {
            alert(json.msg);
          }
        })
  }

  backOrderList() {
    browserHistory.push('/account/order-center/order-list.html');
  }

  showLogistic(refundStatus, refundId) {
    let { canChangeLogistic , logisticsNumber , otherCompanies , logisticsCompanies, other} = this.state,
        flag = refundStatus == "WAIT_BUYER_CONFIRM";
    ;
    switch (refundStatus) {
      case 'WAIT_BUYER_RETURN_TO_STORE' || "WAIT_BUYER_RETURN_GOODS_SROTE" || "WAIT_BUYER_RETURN_GOODS_WH":
        return <Form onSubmit={(e)=>this.handleLogistic(e)}>
          <FormGroup>
            <span>物流公司：<span className="notice"></span></span>
            <select value={logisticsCompanies} onChange={(e)=>this.changeLogistic(e)} name="logisticsCompanies">
              <option value="">请选择物流公司</option>
              <option value="申通快递">申通快递</option>
              <option value="圆通快递">圆通快递</option>
              <option value="顺丰速运">顺丰速运</option>
              <option value="中国邮政">中国邮政</option>
              <option value="百世汇通">百世汇通</option>
              <option value="天天快递">天天快递</option>
              <option vlaue="德邦物流">德邦物流</option>
              <option value="其他">其他</option>
            </select>
            {other ? <TextBox value={otherCompanies} className="logistic" placeholder="请输入物流公司" name="logistic"
                              onChange={(e)=>{this.setState({otherCompanies:e.target.value})}} width={223}
                              height={30}></TextBox> : ""}
          </FormGroup>
          <input name="codeId" type="hidden" value={refundId}></input>
          <input name="codeType" type="hidden" value="RF"></input>
          <FormGroup>
            <span>物流单号： <span className="notice"></span></span>
            <TextBox value={logisticsNumber} name="logisticsNumber" width={223} height={30}
                     onChange={(e)=>{this.setState({logisticsNumber:e.target.value})}}></TextBox>
          </FormGroup>
          <FormGroup>
            <Button onClick={() => this.cancleApply(refundId)} className="red cancle_button button-top"
                    text="取消申请" width={150} height={40}></Button>
            <Button
                disabled={logisticsNumber==""||logisticsCompanies==""||logisticsCompanies=="其他"&&otherCompanies==""}
                type="submit" className="red button-top"
                text="提交物流信息" width={150} height={40}></Button>
          </FormGroup>
        </Form>;
      case 'WAIT_SELLER_AGREE_TO_RETURN':
        return <div className="refund_button">
          <Button text="返回我的订单" width={150} height={40} className="red"
                  onClick={() => this.backOrderList()}></Button>
        </div>;
      default:
        return <Form>
          <FormGroup>
            <span>物流公司：</span>{logisticsCompanies == "其他" ? otherCompanies : logisticsCompanies}
          </FormGroup>
          <FormGroup>
            <span>物流单号：</span>{logisticsNumber}
          </FormGroup>
          <FormGroup>
            <div className="refund_button">
              <Button text="确定" width={150} height={40} onClick={()=>{this.confimInfo()}}
                      className="confim_button" style={{display:flag?"":"none"}}></Button>
              <Button text="返回我的订单" width={150} height={40} className="red"
                      onClick={() => this.backOrderList()}></Button>
            </div>
          </FormGroup>
        </Form>;
    }
  }

  render() {
    let refundInfo = this.state.resp[0] || [],
        { address,creationTime, paths, name, orderId, payedTime, productCode, productId, quantity, reason,refundAmount, refundDesc, refundId, refundReason, refundStatus, returnQuantity, size, stylePic, styleText, unitPrice, returnWay} = refundInfo;
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
                      <ProductAndOrderViewer shopName={"退货退款商品"} productCode={productCode} price={unitPrice}
                                             orderId={orderId} quantity={quantity} name={name} mainPic={stylePic}
                                             style={styleText} size={size}
                                             paymentTime={payedTime}></ProductAndOrderViewer>
                    </Col>
                    <Col>
                      <Panel>
                        <TabPanel id="view_refund_return">
                          <Tab title="退货退款">
                            <div className="refund_return_table">
                              <ul>
                                {this.refundInfo(refundStatus)}
                              </ul>
                              {
                                this.showLogistic(refundStatus, refundId)
                              }
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