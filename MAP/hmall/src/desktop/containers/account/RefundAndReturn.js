import React, { Component } from 'react'
import { Panel,Col,Row } from '../../components/Layout'
import { Link, IndexLink, browserHistory} from 'react-router'
import Moment from 'moment';
import ComboBox from '../../components/ComboBox';
import Button from '../../components/Button';
import Icon from '../../components/Icon';
import Form ,{ FormGroup } from '../../components/Form';
import Pagenation from '../../components/Pagenation';

const PAGE_SIZE = 3;

const statusOptions = {

   'WAIT_SELLER_AGREE_TO_RETURN':  '待退货审核',
   'WAIT_BUYER_RETURN_GOODS':  '待买家送回门店/寄回门店/寄回总仓',
   'WAIT_SELLER_CONFIRM_GOODS':  '待卖家收货',
   'WAIT_BUYER_CONFIRM':  '协商中待买家确认',
   'WAIT_SELLER_AGREE':  '待退款审核',
   'SELLER_REFUSE_BUYER':  '已拒绝',
   'SUCCESS':  '退款成功',
   'CLOSED':  '退款关闭',
   'FAIL':  '退款失败',
   'OFFLINE_REFUND_SUCCESS':  '已线下退款',
}
const typeOptions = {
  'REFUND': '仅退款',
  'RETURN': '退货退款',
}
export default class RefundAndReturn extends Component{

  componentWillUnmount(){
    this.isUnMounted = true;
  }
  componentWillMount(){
    {/*组件将要渲染时调用*/}
    //http://10.211.110.117:5555/hmall-od-service
    //${odService}/refundList
    this.query()
  }

  query() {
    let { page = 1, refundType, refundStatus } = this.state.query;
    fetch(`${odService}/refundList/${page}/${PAGE_SIZE}`,{
      method: 'POST',
      headers:Hmall.getHeader({
        "Content-Type":"application/json"
      }),
      body: JSON.stringify({
        refundType,
        refundStatus
      })
    })
    .then(Hmall.convertResponse('json',this))
    .then(json=>{
      let { success, resp, total }= json;
      if(success){
        this.setState({resp:resp,fetch_status:"init",total:total});
      }
    })
    .catch(Hmall.catchHttpError(
        ()=>{
          this.setState({fetch_status : "error"})
        }
    ))
  }
   
  componentWillReceiveProps(nextProps){
    let { page, refundType, refundStatus } = nextProps.location.query,
        { query } = this.props.location;
    if(page != query.page || refundType != query.refundType || refundStatus!= query.refundStatus){
      this.state.fetch_status = 'uninit';
      this.state.query = { page, refundType, refundStatus };
      this.query()
    }
  }

  constructor(props) {
    super(props);
    let { page, refundType, refundStatus } = props.location.query;
    this.state = {
      success: '',
      resp: [],
      fetch_status: 'uninit',
      total : 0,
      query: {
        page,
        refundType,
        refundStatus
      }
    }
  }
  /*搜索方法*/
  handleSearch(e) {
    e.preventDefault();
    let { pathname } = this.props.location,
        { refundType, refundStatus } = e.target;
    browserHistory.push({
      pathname,
      query: {
        refundType: refundType.value || undefined,
        refundStatus: refundStatus.value || undefined
      }
    })
  }
    
  /*取消方法*/
  handleCancel(id){
    if(confirm("是否确认取消")){
      fetch(`${odService}/cancelRequestRefund/${id}`,{
        method:"POST",
        headers:Hmall.getHeader({
          "Content-Type":"application/json"
        }),
      })
      .then(Hmall.convertResponse('json',this))
      .then(json=>{
        if(json.success){
          this.query()
        }else{
          alert("取消失败")
        }
      })
       .catch(Hmall.catchHttpError())
    }
  }
    
  getRefundType(goods) {
    let { refundType ,refundId , orderId , productId }  = goods;
    switch(refundType){
      case 'SHIPPINGREFUNDS':return <Link to={`/account/order-center/order-list/view-aftermarket-refund-return.html?refundId=${refundId}`}>查看详情</Link>;
      case 'REFUNDS': return <Link to={`/account/order-center/order-list/view-aftermarket-only-refund.html?refundId=${refundId}`}>查看详情</Link>;
      case 'NOTDELIVERREFUND': return <Link to={`/account/order-center/order-list/view-refund.html?refundId=${refundId}`}>查看详情</Link>;
    }
  }

  getStatus(code){
    switch(code){
      case 'WAIT_SELLER_AGREE_TO_RETURN': return '待退货审核';
      case 'WAIT_BUYER_RETURN_TO_STORE': return '待买家送回门店';
      case 'WAIT_BUYER_RETURN_GOODS_STORE': return '待买家寄回门店';
      case 'WAIT_BUYER_RETURN_GOODS_WH': return '待买家寄回总仓';
      case 'WAIT_SELLER_CONFIRM_GOODS': return '待卖家收货';
      case 'WAIT_BUYER_CONFIRM': return '协商中待买家确认';
      case 'WAIT_SELLER_AGREE': return '待退款审核';
      case 'SELLER_REFUSE_BUYER': return '已拒绝';
      case 'SUCCESS': return '退款成功';
      case 'CLOSED': return '退款关闭';
      case 'FAIL': return '退款失败';
      case 'OFFLINE_REFUND_SUCCESS': return '已线下退款';
      default: return '无状态';
    }
  }
  
  getTypes(code){
    switch(code){
      case 'SHIPPINGREFUNDS': return '退货退款';
      case 'REFUNDS': return '仅退款';
      case 'NOTDELIVERREFUND': return '仅退款';
      default: return '无状态';
    }
  }

  refundAndReturnList() {
    let { resp } = this.state;
    return resp.map((goods,i)=>{
        return(
            <tbody key={i}>
            <tr id="odd">
              <td  colSpan="5">
                <span>{Moment(goods.creationTime).format(HmallConfig.date_format)}</span>
                <span>退款编号：</span>{goods.refundId}
                <span>订单编号：</span>
                <Link to={"/account/order-center/order-detail.html?oid="+goods.orderId}>{goods.orderId}</Link>
              </td>
            </tr>
            <tr>
              <td className="top">
                <Link to={"/product-detail.html?productCode="+goods.productCode}>
                  <img src={Hmall.cdnPath(goods.mainPic)}/>
                </Link>
                <ul>
                  <li>
                    <Link to={"/product-detail.html?productCode="+goods.productCode}>
                      <span className="span-hover">{goods.name}</span><br/>
                    </Link>
                  </li>
                  <li>
                    <span className="color-size">颜色：{goods.styleText}&nbsp;&nbsp;尺码：{goods.size}</span>
                  </li>
                  <li><Icon name="7days" title="7天无理由退换"></Icon></li>
                </ul>
              </td>
              <td className="top">{this.getTypes(goods.refundType)}</td>
              <td className="top">￥{goods.refundAmount}</td>
              <td className="top">
                <span>{this.getStatus(goods.refundStatus)}</span><br/><br/>
                {this.getRefundType(goods)}
              </td>
              <td className="top">
                {goods.refundStatus=="WAIT_SELLER_AGREE_TO_RETURN"||goods.refundStatus=="WAIT_BUYER_RETURN_TO_STORE"||goods.refundStatus=="WAIT_BUYER_RETURN_GOODS_STORE"||goods.refundStatus=="WAIT_BUYER_RETURN__WH"
                ||goods.refundStatus=="WAIT_BUYER_CONFIRM"&&goods.refundType=="SHIPPINGREFUNDS"
                    ?<Button className="red" disabled={goods.refundStatus=="CLOSED"} onClick={()=>{this.handleCancel(goods.refundId)}} text="取消" width={75} height={35}/>:""}
              </td>
            </tr>
            <tr><td colSpan="5"></td></tr>
            </tbody>
        )
     });
  }
    
  render() {
    let { fetch_status, total, query, resp } = this.state,
        { page = 1, refundType, refundStatus } = query,
      BoxStyle = {marginLeft:"20px"};
    return (
      <div className="refundAndReturn">
        <nav>
          <Form onSubmit={e => this.handleSearch(e)}>
            <ComboBox  name="refundType" value={refundType} options={typeOptions} width={150} emptyOption="按类型筛选"></ComboBox>
            <ComboBox  name="refundStatus" value={refundStatus} options={statusOptions} width={150} emptyOption="按状态筛选"style={BoxStyle}></ComboBox>
            <Button type="submit" className="black" width={76} height={35} text="搜索"></Button>
          </Form>
        </nav>
        {
          (() => {
            switch(fetch_status){
              case 'uninit': return <div className="loading"></div>;
              case 'init': return resp.length ?
                <table>
                  <thead>
                  <tr>
                    <th width={400}>商品信息</th>
                    <th width={150}>退单类型</th>
                    <th width={150}>退款金额</th>
                    <th width={160}>状态</th>
                    <th width={147}>操作</th>
                  </tr>
                  </thead>
                  {this.refundAndReturnList()}
                  <tfoot>
                  <tr>
                    <td colSpan="5">
                      <Pagenation page={page} pagesize={PAGE_SIZE} total={(parseInt(total))} location={this.props.location}></Pagenation>
                    </td>
                  </tr>
                  </tfoot>
                </table>:
                <h1 className="info">无订单</h1>;
              case 'error':return <h1 className="error">网页出错</h1>
            }
          })()
        }
      </div>
    );
  }
}
