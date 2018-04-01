/**
 * Created by ZWL on 2016/10/28.
 */
import React, { Component } from 'react'
import { Panel,Row,Col } from '../../components/Layout'
import { TabPanel, Tab } from '../../components/Tab';
import { Link, browserHistory } from 'react-router';
import Moment from 'moment';
import Button from '../../components/Button';
import Icon from '../../components/Icon'

export default class OrderDetail extends Component{
  
  componentWillMount(){
    let oid = this.props.location.query.oid;
    if(oid){
      fetch(`${odService}/order/queryForOrderDetails/${oid}`,{
        headers: Hmall.getHeader({})
      })
      .then(Hmall.convertResponse('json',this))
      .then(json =>{
        if(json.success){
          this.setState({
            fetch_status: 'init',
            resp: json.resp
          });
        }
      })
      .catch(Hmall.catchHttpError(()=>{
    	  this.setState({fetch_status: 'error'});
      }))
    }
  }
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  constructor(props){
    super(props);
    this.state={
        fetch_status: 'uninit',
        resp: []
    }
  }
  
  stateJudge(status,express) {
    switch(status) {
      case 'WAIT_BUYER_PAY' : return '待付款';
      case 'WAIT_SELLER_SEND_GOODS'  : return express=="EXPRESS"?'待发货':'已付款';
      case 'SELLER_CONSIGNED_PART' : return '部分发货';
      case 'WAIT_BUYER_CONFIRM_GOODS': return '已发货';
      case 'TRADE_FINISHED': return '交易成功';
      case 'TRADE_CLOSED_BY_UNIQLO': return '	交易取消';
      case 'TRADE_CLOSED': return '交易关闭';
      case 'WAIT_BUYER_TAKE_GOODS': return '待自提';
      default : return '无状态';
    }
  }
  
  invoiceJudge(type,needInvoice) {
	if(needInvoice){
		switch(type){
	      case 'E': return '电子发票：';
	    }
	}else{
		return '不需要发票';
	}
  }
  
  expressJudge(express) {
    switch(express) {
      case 'EXPRESS': return '快递配送';
      case 'PICKUP': return '门店自提';
    }
  }
  
  showProductDetail() {
    let { resp } = this.state;
    if(resp.length){
      return resp.map(order => {
        let details = order.details,
          status = order.status;
        return details.map(details => {
          let { detailId, quantity, summaryInfo, productDetailInfo } = details;
          return (
              <tr key={detailId}>
                <td className="img-width-position">
                  <div className="img-position">
                  <Link to={`/product-detail.html?productCode=${productDetailInfo.productCode}`}><img src={Hmall.cdnPath(productDetailInfo.stylePic)}></img></Link>
                  </div>
                  <ul>
                    <li><Link to={`/product-detail.html?productCode=${productDetailInfo.productCode}`}>{summaryInfo.name}</Link></li>
                    <li className="color-size">颜色：{productDetailInfo.styleText}&nbsp;&nbsp;尺码：{productDetailInfo.size}</li>
                    <li>
                    {
                      summaryInfo.isNoReasonToReturn == "Y" ?
                        <i className="icon icon-7days"></i> : null
                    }
                    </li>
                  </ul>
                 </td>
               <td className="price-width top">￥{productDetailInfo.price}</td>
               <td className="quantity-width top">{quantity}</td>
               <td className="little-total-price top">￥{(quantity*productDetailInfo.price).toFixed(2)}</td>
             </tr>
          );
        })
      });
    }
  }
  
  getStepName(i){
    switch(i){
      case 0: return '订单创建';
      case 1: return '订单支付';
      case 2: return '订单发货';
      case 3: return '确认收货';
      case 4: return '评价商品';
    }
  }
  
  getNodesHtml(nodes) {
    let html = [] , { resp } = this.state ,
        {isCommented , commentTime } = resp[0];
    for(var i = 0;i < 4;i++){
      let node = nodes[i],
        nnode = nodes[i + 1];
      html.push(
          <div className="flow-col">
            {
              this.getNodeHtml(node,nnode,i)
            }
            <span style={{color:node?"#1b1b1b":"#dadada"}}>{this.getStepName(i)}</span>
            {
              this.getNodeHtml2(node)
            }
          </div>)
      
    }
    isCommented?html.push(
        <div className="flow-col">
          <h1>{5}</h1>
          <span style={{color:"#1b1b1b"}}>{this.getStepName(4)}</span>
          <span className="s1">{Moment(commentTime).format(HmallConfig.date_format)}</span>
        </div>):
        html.push(
            <div className="flow-col">
              <h1 className="color">{5}</h1>
              <span style={{color:"#dadada"}}>{this.getStepName(4)}</span>
            </div>
        )

    return html;
  }
  
  getNodeHtml(node,nnode,i) {
    let html = [];
    if(node){
      html.push(<h1>{i + 1}</h1>);
    }else{
      html.push(<h1 className="color">{i + 1}</h1>);
    }
    return html;
  }
  getNodeHtml2(node) {
    if(node){
      return <span className="s1">{Moment(node.date).format(HmallConfig.date_format)}</span>;
    }
    return null;
  }
  
  operateProcedure() {
    let resp = this.state.resp[0],
        nodes = resp.node,
        html = (
          <div className="flow">
            {this.getNodesHtml(nodes)}
          </div>);
    return html;
  }
  
  showExpressDetail() {
    let { distribution } = this.state.resp[0],
      express = this.state.resp[0].express;
    if(distribution == 'EXPRESS'){
        return (
            <TabPanel>
            {
              express.map( (expressIndex,index) => {
                return (
                    <Tab title={"包裹"+(index+1)} width={120} key={index}>
                      <div id="d1">
                        <span>物流信息</span><br/><br/>
                        {expressIndex.info}
                      </div>
                    </Tab>
                )
              })
            }
            </TabPanel>
        );
    }else if(distribution== 'PICKUP'){
      return (
          <div></div>
      );
    }
  }
  
  showDownLoadTicket(type,needInvoice) {
	if(needInvoice){
		switch(type){
	      case 'E': return <Button text="下载电子发票" width={100} height={30}></Button>;
	    }
	}else{
		return <div></div>;
	}
    
  }
  
  showContent(needInvoice,content){
	if(needInvoice){
		return <span>{content}</span>;
	}else{
		return <span></span>;
	}
  }
  
  expressOrTake() {
    let { distribution } = this.state.resp[0];
    if(distribution=='EXPRESS'){
      let resp = this.state.resp[0],
      price = resp.price,
      address = resp.address,
      orderId = resp.orderId,
      receiveInfo = address.name+"  "+address.userPhone+"  "+address.address,
      status = resp.status,
      express = resp.distribution,
      freight = price.freight,
      invoice = resp.invoice.type,
      content = resp.invoice.content,
      needInvoice = resp.invoice.needInvoice;
      return (
          <Row className="message">
            <div className="space-distance">
            </div>
            <Col id="massage-all">
              <Row id="message-title">
                <span>订单信息</span>
              </Row>
              <Row id="message-body">
                <ul>
                  <li>收货信息：<br/>{receiveInfo}</li><br/>
                  <li>订单编号：<br/>{orderId}</li><br/>
                  <li>发票信息：{this.showContent(needInvoice,content)}<br/>{this.invoiceJudge(invoice,needInvoice)}{this.showDownLoadTicket(invoice,needInvoice)}</li>
                </ul>
              </Row>
            </Col>
            <Col id="state">
              <ul>
                <li><Icon name="order-warning"></Icon><span className="order-state">订单状态：{this.stateJudge(status,express)}</span></li>
                <li className="s1 ">配送方式：{this.expressJudge(express)}</li>
              </ul>
            </Col>
          </Row>
      );
    }else if(distribution == 'PICKUP'){
      let resp = this.state.resp[0],
        address_info = resp.address,
        price = resp.price,
        orderId = resp.orderId,
        express = resp.distribution,
        totalPrice = price.total,
        discount = price.discount,
        freight = price.freight,
        account = price.account,
        invoice = resp.invoice.type,
        content = resp.invoice.content,
        needInvoice = resp.invoice.needInvoice,
        address = address_info.address,
        status = resp.status,
        businessTime = address_info.businessTime,
        storePhone = address_info.storePhone,
        distribution = resp.distribution,
        autoCloseTime = resp.autoCloseTime,
        pickupId = resp.pickup.map(function(array,index){
         return array.pickupId;
        }),
        expiredDate = resp.pickup.map(function(array,index){
        	return array.expiredDate;
        });
      return (
          <div className="border-pickup clearfix">
            <div className="space-distance">
            </div>
            <div className="order-info-mation">
              <div className="message-title">
                <span>订单信息</span>
              </div>
              <div className="message-body">
                <ul>
                  <li>订单编号：<br/>{orderId}</li>
                  <li>取货地址：<br/>{address}</li>
                  <li>营业时间：<br/>{businessTime}</li>
                  <li>联系电话：<br/>{storePhone}</li>
                  <li>发票信息：{this.showContent(needInvoice,content)}<br/>{this.invoiceJudge(invoice,needInvoice)}</li>
                </ul>
                {this.showDownLoadTicket(invoice,needInvoice)}
              </div>
            </div>
            <div className="state">
              <br/><br/>
              <span>订单状态：</span>
              <a>{this.stateJudge(status,express)}</a><br/><br/>
              <span className="s1">配送方式：{this.expressJudge(express)}</span><br/><br/>
              { status!= 'WAIT_BUYER_PAY' && <span className="s2" ><Link to={`/account/order-center/take-proof.html?oid=${orderId}&pickupId=${pickupId}`}>提货凭证</Link></span> }
              <br/><br/>
              <span className="s1">提货时限：请于{Moment(autoCloseTime).format(HmallConfig.date_format)}日前至门店自提</span><br/><br/>
            </div>
          </div>
      );
    }
  }
  
  render() {
    let oid = this.props.location.query.oid;
        let resp = this.state.resp[0]||[],
          { price, address, orderId, status, express } = resp||[],
          { total, discount, freight, account } = price||[],
          invoice1 = resp.invoice||[],
          invlice = invoice1.type||[];
        return (
          <Panel id="orderDetail">
          {
        	  (()=>{
        		  switch(this.state.fetch_status){
        		  case 'uninit': return <div className="loading"></div>;
        		  case 'init': return this.state.resp.length?<div>
					  	            <div className="procedure-position">
						              {this.operateProcedure()}
						            </div>
						            <div>
						              {this.expressOrTake()}
						            </div>
						            <div>
						              {this.showExpressDetail()}
						            </div>
						            <div className="space-distance"></div>
						            <table>
						              <thead>
						                <tr className="tr-background-color">
						                  <td className="">商品信息</td>
						                  <td>单价</td>
						                  <td>数量</td>
						                  <td>小计</td>
						                </tr>
						              </thead>
						              <tbody>
						                {this.showProductDetail()}
						              </tbody>
						            </table>
						            <div className="account-div clearfix">
						              <ul className="account-data">
						                <li className="char-color">￥{total.toFixed(2)}</li>
						                <li>-￥{discount.toFixed(2)}</li>
						                <li>￥{freight.toFixed(2)}</li>
						                <li className="char-color big-size">￥{account.toFixed(2)}</li>
						              </ul>
						              <ul className="account-name">
                            <li>商品总额：</li>
                            <li>优惠：</li>
                            <li>运费：</li>
                            <li>订单总额：</li>
                          </ul>
						            </div>
						          </div>:<h1 className="info">暂无详情</h1>;
        		  case 'error': return <h1 className="error">网页出错</h1>;
        		  }
        	  })()
          } 
          </Panel>
        );
  }
}