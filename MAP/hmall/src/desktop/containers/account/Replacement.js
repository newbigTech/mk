import React, { Component } from 'react'
import { Panel,Col,Row } from '../../components/Layout'
import { Link, IndexLink, browserHistory } from 'react-router'
import ComboBox from '../../components/ComboBox';
import Button from '../../components/Button';
import Form ,{ FormGroup } from '../../components/Form';
import Moment from 'moment';
import Icon from '../../components/Icon';
import Pagenation from '../../components/Pagenation';

const PAGE_SIZE = 3;
const statusOptions = {
  'WAIT_BUYER_RETURN_GOODS': '待买家上传物流信息',
  'WAIT_SELLER_CONFIRM_GOODS': '待卖家收货',
  'CANCELLED' : '换退取消',
  'CLOSED' : '超时关闭',
  'SELLER_HAS_CONFIRMED_GOODS' : '换退已入库',
  'WAIT_SELLER_SEND_GOODS' : '待发货',
  'WAIT_BUYER_CONFIRM_GOODS': '已发货',
  'TRADE_FINISHED' : '交易成功',
  'TRADE_CLOSED_ALL' : '交易关闭',
}

export default class Replacement extends Component{
  
  componentWillUnmount(){
    this.isUnMounted = true;
  }
  componentWillMount(){
      {/*组件将要渲染时调用*/}
    this.query();
  }

  query() {
    let { page = 1, replaceStatus } = this.state.query;
    fetch(`${odService}/replaceList/${page}/${PAGE_SIZE}`,{
      method: 'POST',
      headers: Hmall.getHeader({
        'Content-Type' : 'application/json'
      }),
      body: JSON.stringify({
        replaceStatus
      })
    })
    .then(Hmall.convertResponse('json',this))
    .then(json=>{
      let { success , resp ,total} = json
        if(success){
          this.setState({resp:resp,fetch_status:"init",total:total});
        }else{
          this.setState({
            fetch_status: 'error'
          });
        }
    })
    .catch(Hmall.catchHttpError(()=>{
      this.setState({
        fetch_status: 'error'
      });
    }))
  }
  
  componentWillReceiveProps(nextProps){
    let { page, replaceStatus } = nextProps.location.query,
        { query } = this.props.location;
    if(query.page != page || query.replaceStatus != replaceStatus){
      this.state.fetch_status = 'uninit';
      this.state.query = { page, replaceStatus };
      this.query()
    }
  }

  constructor(props) {
    super(props);
    let { page = 1, replaceStatus } = props.location.query;
    this.state = {
      fetch_status: 'uninit',
      resp: [],
      total: 0,
      query: {
        page,
        replaceStatus
      }
    }
  }
  /*搜索方法*/
  handleSearch(e){
    e.preventDefault();
    let { pathname } = this.props.location,
        { replaceStatus } = e.target;
    browserHistory.push({
      pathname,
      query: {
        replaceStatus: replaceStatus.value || undefined
      }
    })
  }
  
  /*取消方法*/
  handleCancel(id){
    if(confirm("是否确认取消")){
      fetch(`${odService}/cancelRequestReplace/${id}`,{
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

  getTypesBack(code){
    switch(code){
      case 'WAIT_BUYER_RETURN_GOODS': return '待买家上传物流信息';
      case 'WAIT_SELLER_CONFIRM_GOODS': return '待卖家收货';
      case 'CLOSED': return '换退关闭';
      case 'CANCELLED': return '换退取消';
      case 'SELLER_HAS_CONFIRMED_GOODS': return '换退已入库';
      default: return '无状态';
    }
  }
  
  getTypes(code){
    switch(code){
      case 'WAIT_BUYER_RETURN_GOODS': return '待买家寄回商品';
      case 'WAIT_SELLER_CONFIRM_GOODS': return '待卖家收货';
      case 'WAIT_SELLER_SEND_GOODS': return '待发货';
      case 'WAIT_BUYER_CONFIRM_GOODS': return '已发货';
      case 'TRADE_FINISHED': return '交易成功';
      case 'TRADE_CLOSED_BY_UNIQLO': return '交易关闭';
      case 'TRADE_CLOSED': return '交易关闭';
      default: return '无状态';
    }
  }
  finishOrder(orderId) {
    if(confirm("确认收货？")){
      fetch(`${odService}/order/updateOrderConfirmed/${orderId}`,{
        method: 'POST',
        headers: Hmall.getHeader({"Content-Type":"application/json",})
      })
          .then(Hmall.convertResponse('json', this))
          .then(json => {
            let { success  , msgCode } = json ,{ resp } = this.state , r = resp;
            if(success) {
              r.back.b_status = "TRADE_FINISHED";
              this.setState({resp:r})
            }else{
              alert(msgCode);
            }
          })
          .catch(Hmall.catchHttpError())
    }
  }
  replacementList(){
    let { resp } = this.state
    return resp.map((detail,i)=>{
      let { back, renewal } = detail,
          { b_creationTime, b_replaceId , b_orderId, b_productCode, b_status, b_mainPic, b_name, b_size, b_style } = back,
          { r_mainPic, r_name, r_size, r_style, r_status  ,r_orderId , r_isClosedAfterSale  } = renewal
      return(
            <tbody key={i}>
            <tr>
              <td  colSpan="3">
                <span>{Moment(Number(b_creationTime)).format(HmallConfig.date_format)}</span>
                <span>换退编号：</span><span>{b_replaceId}</span>
                <span>换发编号：</span><span>{r_orderId}</span>
                <span>订单编号：</span><span>{b_orderId}</span>
              </td>
            </tr>
            <tr>
              <td>
                <span className="span-back">换发商品</span>
                <Link to={"/product-detail.html?productCode="+b_productCode}>
                  <img src={Hmall.cdnPath(r_mainPic)}/>
                </Link>
                <ul>
                  <li> <Link to={"/product-detail.html?productCode="+b_productCode}>{r_name}</Link></li>
                  <li>尺码：{r_size} 颜色：{r_style}</li>
                  <li><Icon name="7days" title="7天无理由退换"/></li>
                </ul>
              </td>
              <td>{this.getTypes(r_status)}</td>
              <td rowSpan="2">
                <ul>
                  <li><Link to={"/account/order-center/order-list/view-aftermarket-replacement.html?replaceId="+b_replaceId}>查看详情</Link></li>
                  <li>{r_isClosedAfterSale?<Link to={`/account/order-center/order-list/apply-aftermarket.html?uid=${0}`}>申请售后</Link>:[]}</li>
                  <li>{b_status=="WAIT_BUYER_RETURN_GOODS"?<Button className="red" text="取消换货" width={75} height={35}
                                                                   disabled={b_status=="CLOSED"}  onClick={()=>{this.handleCancel(b_replaceId)}}/>:""}
                    {b_status=="WAIT_BUYER_CONFIRM_GOODS"?<Button className="red" text="确认收货" width={75} height={35}
                                                                  onClick={() => this.finishOrder(b_orderId)}/>:""}</li>
                </ul>
              </td>
            </tr>
            <tr>
              <td>
                <span className="span-back">换退商品</span>
                <Link to={"/product-detail.html?productCode="+b_productCode}>
                  <img src={Hmall.cdnPath(b_mainPic)}/>
                </Link>
                <ul>
                  <li><Link to={"/product-detail.html?productCode="+b_productCode}>{b_name}</Link></li>
                  <li>尺码：{b_size} 颜色：{b_style}</li>
                  <li><Icon name="7days" title="7天无理由退换"/></li>
                </ul>
              </td>
              <td>{this.getTypesBack(b_status)}</td>
            </tr>
            </tbody>
        )
      })
    }
    
  render() {
    let { resp , fetch_status , total, query } = this.state,
        { page = 1, replaceStatus } = query;
      return (
        <div className="replacement">
          <nav>
            <Form onSubmit = { e => this.handleSearch(e)}>
              <ComboBox  name="replaceStatus" value={replaceStatus} options={statusOptions} width={150} emptyOption="按状态筛选"/>
              <Button type="submit" className="black" width={76} height={35} text="搜索"/>
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
                      <th width={470}>商品信息</th>
                      <th width={266}>申请状态</th>
                      <th width={270}>操作</th>
                    </tr>
                    </thead>
                    {this.replacementList()}
                    <tfoot>
                    <tr>
                      <td colSpan="3">
                        <Pagenation page={page} pagesize={PAGE_SIZE} total={(parseInt(total))} location={this.props.location}></Pagenation>
                      </td>
                    </tr>
                    </tfoot>
                  </table> :
                  <h1 className="info">无订单</h1>;
                case 'error':return <h1 className="error">网页出错</h1>
              }
            })()
          }

        </div>
      );
  }
}