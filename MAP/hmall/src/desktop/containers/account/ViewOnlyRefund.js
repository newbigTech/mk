import React, { Component } from 'react';
import Button from '../../components/Button';
import { Panel,Row,Col } from '../../components/Layout';
import ProductAndOrderViewer from '../../components/ProductAndOrderViewer';
import Moment from 'moment';
import { browserHistory,Link } from 'react-router';
import { TabPanel, Tab } from '../../components/Tab';
import PicUpload from '../../components/PicUpload';

export default class ViewOnlyRefund extends Component {

  fetchRefundInfo() {
    let refundId = this.props.location.query.refundId;
    fetch(`${odService}/refundSingleDetail/${refundId}`, {
      headers: Hmall.getHeader({})
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          if (json.success) {
            let { resp = [{}] } = json, { compensation, totalFee, maxCompensateRation, compensateRation,detailId} = resp[0],
                summary = (totalFee * compensateRation / 100).toFixed(2);

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
            this.setState({
              chooseReason: resp[0].refundReason,
              fetch_status: 'init',
              resp: resp,
              summary: Number(resp[0].refundAmount),
              oldPaths: resp[0].paths.slice(),
              showFlag: compensation != "0",
              paymentAmount: summary > maxCompensateRation ? maxCompensateRation.toString() : summary,
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


  componentWillMount() {
    this.fetchRefundInfo();
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }

  constructor(props) {
    super(props);
    this.state = {
      fetch_status: 'uninit',
      resp: [],
      chooseReason: "",
      notdeliverReason: [],
      summary: 0,
      oldPaths: [],
      newPaths: [],
      paymentAmount: "",
      showFlag: true,
    }
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

  showImg(path) {
    if (path.length == 0) {
      return (<span>未上传凭证</span>);
    } else {
      return path.map(img => {
        return (<img className="img-size-show" src={img}></img>);
      })
    }
  }

  getOptions(notdeliverReason) {
    return notdeliverReason.map((item, i)=> {
      return <option key={i} value={item.value}>{item.meaning}</option>
    })
  }

  changeSummary(e) {
    let val = e.target.value, {returnMoney} = this.state.resp[0];
    if (!isNaN(val) && val <= returnMoney) {
      this.setState({summary: val});
    } else if (val == "") {
      this.setState({summary: ""});
    } else if (val > returnMoney) {
      this.setState({summary: returnMoney});
    }
  }

  blurSummary(e) {
    let val = e.target.value, {returnMoney} = this.state.resp[0];
    if (val == "") {
      this.setState({summary: returnMoney});
    }
  }

  changeReason(event) {
    let val = event.target.value, { notdeliverReason } = this.state;
    notdeliverReason.map((item, i)=> {
      if (item.value == val) {
        this.setState({showFlag: item.relatedcomp == "Y"});
      }
    })
    this.setState({chooseReason: val});
  }

  refundInfo() {
    let refundInfo = this.state.resp[0] || [], { chooseReason, notdeliverReason,summary,showFlag, paymentAmount } =this.state,
        {returnMoney,address,creationTime, name, paths, orderId, payedTime, productCode, productId, quantity, reason,refundAmount, refundDesc, refundId, refundReason, refundStatus, } = refundInfo,
        flag = refundStatus == "WAIT_BUYER_CONFIRM";
    return (
        <ul>
          <li>
            <span>退款状态：</span>
            <b><span >{this.getRefundStatus(refundStatus)}</span></b>
          </li>
          <li>
            <span>退款编号：</span>
            <span>{refundId}</span>
          </li>
          <li>
            <span>申请时间：</span>
            <span>{Moment(Number(creationTime)).format(HmallConfig.datetime_format)}</span>
          </li>
          <li>
            <span>退款原因：</span>
            {flag ? <select value={chooseReason} onChange={(event)=>{this.changeReason(event)}}>
              {this.getOptions(notdeliverReason)}
            </select> : reason}
          </li>
          <li>
            <span style={{ float:flag?"left":"" }}>退款金额：</span>
            {flag ? <div className="return-money">
              <input type="text" className="number-input" value={summary} onChange={(e)=>{this.changeSummary(e)}}
                     onBlur={(e)=>{this.blurSummary(e)}}/>
              <span className="remark">(最多￥{returnMoney})</span>
            </div> : "￥" + refundAmount}
          </li>
          <li style={{ display: showFlag?"":"none" }}>
            <span>赔付金额：</span>
            {"￥" + paymentAmount}
          </li>
          <li>
            <span>退款说明：</span>
            {flag ? <textarea ref="refundInfo" className="text-about"
                              defaultValue={refundDesc}/> : refundDesc}
          </li>
          <li>
            <span style={{float:flag?"left":""}}>已传凭证：</span>
            {flag ? <div className="picupload-position">
              <PicUpload getImage={(img) =>{ this.getImage(img)}} defaultPath={paths}
                         deletePic={(path)=>{this.deletePic(path)}}></PicUpload>
              <p>每张图片大小不超过10M，最多3张，支持GIF、JPG、PNG、BMP格式</p>
            </div> : this.showImg(paths)}
          </li>
        </ul>
    );
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

  confimInfo() {
    let { chooseReason,summary,oldPaths, newPaths,showFlag,paymentAmount }=this.state, refundInfo = this.refs.refundInfo.value,
        { refundId } = this.props.location.query;
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
        returnQuantity: "",
        refundAddr: {},
        returnWay: "",
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

  render() {
    let refundInfo = this.state.resp[0] || [],
        { address,creationTime, paths, name, orderId, payedTime, productCode, productId, quantity, reason,refundAmount, refundDesc, refundId, refundReason, refundStatus, returnQuantity, size, stylePic, styleText, unitPrice } = refundInfo,
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
                      <ProductAndOrderViewer shopName={"仅退款商品"} productCode={productCode} price={unitPrice}
                                             orderId={orderId} quantity={quantity} name={name} mainPic={stylePic}
                                             style={styleText} size={size}
                                             paymentTime={payedTime}></ProductAndOrderViewer>
                    </Col>
                    <Col>
                      <Panel>
                        <TabPanel id="only_refund">
                          <Tab title="仅退款">
                            <div className="only_refund_table">
                              {this.refundInfo()}
                              <div className="only_refund_button">
                                <Button text="确定" width={150} height={40} onClick={()=>{this.confimInfo()}}
                                        className="confim_button" style={{display:flag?"":"none"}}></Button>
                                <Button text="返回我的订单" width={150} height={40} className="red"
                                        onClick={() => this.backOrderList()}></Button>
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