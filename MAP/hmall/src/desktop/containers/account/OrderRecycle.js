import React, { Component } from 'react';
import { Link, browserHistory } from 'react-router';
import { Panel, Row, Col } from '../../components/Layout';
import Button from '../../components/Button';
import TextBox from '../../components/TextBox';
import ComboBox from '../../components/ComboBox';
import Pagenation from '../../components/Pagenation';
import Icon from '../../components/Icon';
import Moment from 'moment';

const pageSize = 5,
  ORDER_STATUS = {
    0: 'WAIT_BUYER_PAY',
    1: 'WAIT_SELLER_SEND_GOODS',
    2: 'SELLER_CONSIGNED_PART',
    3: 'WAIT_BUYER_CONFIRM_GOODS',
    4: 'WAIT_BUYER_TAKE_GOODS',
    5: 'TRADE_FINISHED',
    6: 'TRADE_CLOSED_BY_UNIQLO',
    7: 'TRADE_CLOSED'
  };
export default class OrderRecycle extends Component{
  
  componentWillMount(){
    this.query(this.props)
  }
  
  query(props) {
    let { page=1 } = props.location.query;
    fetch(`${odService}/order/queryForRecycleOrders/${page}/${pageSize}`,{
    headers:Hmall.getHeader({})
    })
    .then(Hmall.convertResponse('json',this))
    .then(json =>{
      this.setState({
        resp:json.resp,
        total: json.total,
        fetch_status: 'init' ,
      });
    })
    .catch(Hmall.catchHttpError(()=>{
      this.setState({
        fetch_status: 'error'
      });
    }))
  }
  
  componentWillReceiveProps(nextProps){
    let { page } = nextProps.location.query,
        { query } = this.props.location;
    if(query.page!= page){
      this.state.fetch_status = 'uninit';
      this.state.nowPage = page ;
      this.query(nextProps);
    }
  }
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  constructor(props){
    super(props);
    this.state = {
        fetch_status: 'uninit',
        resp: [],
        total: 0,
        nowPage: 1,
    }
  }
  
  buyAgain(orderId) {
    browserHistory.push('/cart.html?oid='+orderId);
  }
  
  //商品再次购买
  buyAgainOrder(order){
    let { details, distribution, address, orderId } = order,
        { city, district, state } = address,
        storeName = address.storeName||'',
        distributionId = address.code||'',
        orderBuyAgainCode = [],
        orderBuyFalseName = [],
        orderBuySuccess = [],
        newAddress = [city, district, state];
    details.map( (detail, index) => {
      let { productDetailInfo={}, summaryInfo={}, quantity, productCode, productId } = detail,
          { name } = summaryInfo,
          temp = {
            quantity,
            productCode,
            productId,
            distribution,
            storeName,
            distributionId,
            address:newAddress
          };
      orderBuySuccess.push(temp);
      orderBuyAgainCode.push(productCode)
    });
    fetch(`${pdService}/product/validateProductByCodes`,{
      method: 'POST',
      headers: Hmall.getHeader({"Content-Type":"application/json",}),
      body: JSON.stringify(orderBuyAgainCode)
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          let { success, resp } = json;
          if(success) {
            fetch(`${ctService}/cart/insert`,{
              method: 'POST',
              headers: Hmall.getHeader({"Content-Type":"application/json",}),
              body: JSON.stringify(orderBuySuccess)
            })
                .then(Hmall.convertResponse('json',this))
                .then(json => {
                  if(json.success) {
                    Hmall.Storage.get('miniCart').show(true);
                    this.buyAgain(orderId)
                  }
                })
          }else{
            resp.map((code, index) => {
              details.map( (detail, index) => {
                let { productCode, summaryInfo={} } = detail,
                    { name } = summaryInfo;
                if(productCode==code){
                  orderBuyFalseName.push(name);
                }
              })
            })
            alert(orderBuyFalseName.join('、')+"商品已不能购买")
          }
        })
        .catch(Hmall.catchHttpError())
  }
  
  
  //获取操作
  getOperation(order) {
    let { status, orderId } = order;
    switch(status){
      case 'TRADE_CLOSED': return 
        <p key={1}><Link  className="buyAgain" onClick={()=>this.buyAgainOrder(order)}>再次购买</Link></p>;
      case 'TRADE_FINISHED': return [
        <p key={2} className="notice-word"><Link to={`/account/order-center/order-list/evaluate.html?orderId=${orderId}`}>评价</Link></p>,
        <p key={3} ><Link className="buyAgain" onClick={()=>this.buyAgainOrder(order)}>再次购买</Link></p>
        ];
    }
  }
  
  //获取交易状态
  getStatusButton(status,uid,orderId,productId) {
    switch(status) {
      case 'TRADE_CLOSED': return <Link to={`/account/order-center/order-list/view-refund.html?oid=${orderId}&productId=${productId}`}>查看退款</Link>;
      case 'TRADE_FINISHED': return <Link to={`/account/order-center/order-list/apply-aftermarket.html?uid=${uid}`}>申请售后</Link>
    }
  }
  
  getProductDetail(order) {
    let { node,creationTime,orderId, price, details, distribution } = order,
      size = details.length;
    return details.map( (detail,index) => {
      let { detailId, quantity,approvalInfo, summaryInfo, orderId,productDetailInfo,status } = detail;
      return (
          <tr  key={detailId} className="shop-info">
            <td className="top show-border-bottom image-shoping">
              <Link to={`/product-detail.html?productCode=${productDetailInfo.productCode}`}><img src={Hmall.cdnPath(productDetailInfo.stylePic)}></img></Link>
            </td>
            <td className="top show-border-bottom left-position">
              <p><Link to={`/product-detail.html?productCode=${productDetailInfo.productCode}`}>{summaryInfo.name}</Link></p>
              <p className="size-color">颜色：{productDetailInfo.styleText}&nbsp;&nbsp;尺码：{productDetailInfo.size}</p>
              {
                summaryInfo.isNoReasonToReturn == 'Y'&&<Icon name="7days" title="7天无理由退换"></Icon>
              }
            </td>
            <td className="center show-border-bottom">
              ￥{productDetailInfo.price}
            </td>
            <td className="center show-border-bottom">
              {quantity}
            </td>
            <td className="center show-border-bottom">
              {this.getStatusButton(status,detailId,orderId,productDetailInfo.productId)}
            </td>
            {
              index == 0 ?
              <td rowSpan={size} className="center the-second-position top left-border show-border-bottom">
                <p><b>￥{(price.total).toFixed(2)}</b></p>
                <p>（含运费：￥{(price.freight).toFixed(2)}）</p>
              </td>:null
            }
            {
              index == 0 ?
              <td rowSpan={size} className="center the-second-position top left-border show-border-bottomr">
                <p>{this.getStatusDescription(status,distribution)}</p>
                <p><Link to={`/account/order-center/order-detail.html?oid=${orderId}`}>订单详情</Link></p>
              </td>:null
            }
            {
              index == 0 ?
              <td rowSpan={size} className="center top the-second-position left-border show-border-bottom">
                {
                  this.getOperation(order)
                }
                <p className="mousepoint" onClick={() => this.restoreOrder(orderId)}>还原订单</p>
              </td>:null
            }
          </tr>
      );
    })
  }
  
  getStatusDescription(code,distribution){
    switch(code){
      case ORDER_STATUS[0]: return <span className="notice-word">待付款</span>;
      case ORDER_STATUS[1]: return distribution=='EXPRESS'?'待发货':'已付款';
      case ORDER_STATUS[2]: return '部分发货';
      case ORDER_STATUS[3]: return '已发货';
      case ORDER_STATUS[4]: return '待自提';
      case ORDER_STATUS[5]: return '交易成功';
      case ORDER_STATUS[6]: return '交易取消';
      case ORDER_STATUS[7]: return '交易关闭';
    }
  }
  
  restoreOrder(orderId) {
   	if(confirm("确定还原该订单？")){
   		let { resp, total, nowPage } = this.state;
   		if(nowPage!=(Math.ceil((total)/pageSize))){
       nowPage = nowPage;
     }else{
       nowPage = Math.ceil((total-1)/pageSize)
     }
     nowPage==0 ? nowPage=1:nowPage;
     this.forceUpdate();
   		fetch(`${odService}/orderReturn/reduction/${orderId}`,{
   		  method:'post',
   		  headers:Hmall.getHeader({
   		    'Content-Type': 'application/json'
   		  }),
   		  body: JSON.stringify({
   		    pageNum: nowPage,
   		    pageSize: pageSize
   		  })
   		})
   		.then(Hmall.convertResponse('json',this))
   		.then(json =>{
   		  let { success, total, resp } = json,
   		      _resp = this.state.resp; 
   		  if(success){
   		    _resp.forEach((array,i)=>{
   		      if(array.orderId == orderId){
   		        _resp.splice(i,1);
   		      }
   		  })
   			   this.setState({
   			     resp: resp,
   			     total: total,
   			     nowPage: nowPage
   			   });
   		  }
   		  else{
   		    alert(json.msgCode);
   		  }
   		})
   		.catch(Hmall.catchHttpError())
   	}
  }
  
  getRecycleList() {
    let { resp } = this.state;
      return resp.map( (order,i) => {
      	  let { node, creationTime, orderId, price, details, distribution } = order;
            return (
                <tbody key={orderId}>
                  <tr className="order-recycle-title">
                    <td className="date-position">
                      <span>{Moment(creationTime).format(HmallConfig.date_format)}</span>
                    </td>
                    <td colSpan="6">
                      <span>订单编号:{orderId}</span>
                    </td>
                    <td className="icon-position">
                      <Icon name="recycle" onClick={() => this.deleteRecycle(orderId)}></Icon>
                    </td>
                  </tr>
                  {
                    this.getProductDetail(order)
                  }
                  <tr className="top-border"><td colSpan="8"></td></tr>
                </tbody>
            );
      })
  }
  
  deleteRecycle(orderId) {
   	if(confirm("是否删除该订单？")){
   		let { resp, total, nowPage } = this.state;
   		if(nowPage!=(Math.ceil((total)/pageSize))){
   		  nowPage = nowPage;
     }else{
       nowPage = Math.ceil((total-1)/pageSize)
     }
   		nowPage==0 ? nowPage=1:nowPage;
   		this.props.location.query.page = nowPage;
     this.forceUpdate();
   		fetch(`${odService}/orderReturn/delete/${orderId}`,{
   		  method: 'POST',
   		  headers: Hmall.getHeader({
   		    'Content-Type': 'application/json'
   		  }),
   		  body: JSON.stringify({
   		    pageNum: nowPage,
         pageSize: pageSize
   		  })
   		})
   		.then(Hmall.convertResponse('json',this))
   		.then(json =>{
   		  let { success, total, resp } = json,
   		      _resp = this.state.resp;
   		  if(success){
   		    _resp.forEach((array,i)=>{
           if(array.orderId == orderId){
             _resp.splice(i,1);
           }
         })
         this.setState({ resp: resp, total: total, nowPage: nowPage });
   		  }else{
   		    alert(json.msgCode)
   		  }
   		})
   		.catch(Hmall.catchHttpError())
	}
    
  }
  
  render() {
    	let { resp, fetch_status, nowPage = 1 } = this.state;
      return (
        <div id="orderrecycle_div">
          {
        	  (()=>{
        		  switch(fetch_status){
        		  case 'uninit': return <div className="loading"></div>;
        		  case 'init': return resp!=null? 
        				  <table className="title_inform_orderrecycle">
			                  <thead className="orderrecycle_thead">
			                    <tr>
			                      <td colSpan="2" className="shop_inform_one">商品信息</td>
			                      <td className="shop_inform_two">单价</td>
			                      <td className="shop_inform_three">数量</td>
			                      <td className="shop_inform_four">商品操作</td>
			                      <td className="shop_inform_five">实付款</td>
			                      <td className="shop_inform_six">交易状态</td>
			                      <td className="shop_inform_seven">操作</td>
			                    </tr>
			                  </thead>
			                  {this.getRecycleList()}
			                  <tfoot className="foot_div">
			                    <tr>
			                      <td colSpan="8">
      			      		          <Pagenation page={nowPage}
      			      				              pagesize={pageSize}
      			      				              total={parseInt(this.state.total)} location={this.props.location}>
      			      		          </Pagenation>
			      		              </td>
			                    </tr>
			                  </tfoot>
			              </table>:<h1 className="info">暂无回收订单</h1>;
        		    case 'error': return <h1 className="error">网页出错</h1>;
        		  }
        	  })()
          }
         </div>
      );
  }
}