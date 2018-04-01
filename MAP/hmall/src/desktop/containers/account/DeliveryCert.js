import React, { Component } from 'react'
import Form ,{ FormGroup } from '../../components/Form'
import TextBox from '../../components/TextBox'
import Button from '../../components/Button'
import { Link } from 'react-router'
import ComboBox from '../../components/ComboBox';
import Moment from 'moment';

export default class DeliveryCert extends Component{
  
  componentDidMount(){
    fetch(`${odService}/order/queryForUserCodes`,{
       headers:Hmall.getHeader()
    })
    .then(Hmall.convertResponse('json',this))
    .then(json =>{
      this.setState({
        fetch_status: 'init',
        resp: json.resp,
        respSource:json.resp
      });
    })
    .catch(Hmall.catchHttpError(()=>{
    	this.setState({
    		fetch_status: 'error'
    	});
    }))
  }
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  constructor(props) {
    super(props);
    this.state = {
        fetch_status: 'uninit',
        resp: [],
        respSource:[],
        flag: false
    }
  }
  
  getStatus(status) {
    switch(status) {
      case 'WAIT': return '待核销';
      case 'PICKED': return '已核销';
      case 'FAILED': return '已过期';
    }
  }
  
  filter(e) {
	  e.preventDefault();
	  let { pickupCode, orderId, status,pickupshop } =  e.target,
    	  _pickupCode = pickupCode.value,
       _orderId = orderId.value,
       _status = status.value,
       _pickupshop = pickupshop.value,
	      { respSource } = this.state;
   let _respSource = respSource.filter((detail,i) =>{
     let { receivingInfo, status, code, orderId } = detail,
         { address } = receivingInfo,
         { msgCode } = code;
     if((_pickupCode && _pickupCode != msgCode) ||
         (_orderId && _orderId != orderId) ||
         (_status && _status != status) ||
         (_pickupshop && address.indexOf(_pickupshop)==-1))return;
       return detail;
   })
   this.setState({resp: _respSource});
  }
  
  showMsgCode(msgCode) {
    if(msgCode==null){
      return '';
    }else{
      return msgCode;
    }
  }
  
  delliveryCertBody() {
    let { resp } = this.state;
      return resp.map((array,index) => {
        return (
            <tbody className="delivery_table_body">
              <tr>
                <td className="td_center">{this.showMsgCode(array.code.msgCode)}</td>
                <td className="td_center">{array.orderId}</td>
                <td className="td_center">{this.getStatus(array.status)}</td>
                <td className="td_center">
                  <ul>
                    <li>从<label>{Moment(array.startDate).format(HmallConfig.date_format)}</label></li>
                    <li>至<label>{Moment(array.expiredDate).format(HmallConfig.date_format)}</label></li>
                  </ul>
                </td>
                <td>
                  <p>
                    {array.receivingInfo.address}
                  </p>
                  <ul>
                    <li>营业时间：<label>{array.receivingInfo.businessTime}</label></li>
                    <li>联系电话：<label>{array.receivingInfo.storePhone}</label></li>
                  </ul>
                </td>
                <td className="td_center">
                  <Link to={`/account/order-center/take-proof.html?oid=${array.orderId}&pickupId=${array.pickupId}`}>查看详情</Link>
                </td>
              </tr>
            </tbody>
        );
      })
   
  }
  
  render() {
	  let { resp, fetch_status } = this.state;
      return (
        <div id="delivercert_div">
          <div>
            <Form onSubmit = {(e) => this.filter(e)}>
              <FormGroup>
                <TextBox name="pickupCode" placeholder="输入提货码" width={140} height={30}></TextBox>
                <TextBox name="orderId" placeholder="输入订单编号" width={140} height={30}></TextBox>
                <select name="status" style={{width:140,height:30}}>
                  <option value="">按状态筛选</option>
                  <option value="WAIT">待核销</option>
                  <option value="PICKED">已核销</option>
                  <option value="FAILED">已过期</option>
                </select>
                <TextBox name="pickupshop" placeholder="输入提货门店" width={140} height={30}></TextBox>
                <Button type="submit" text="搜索" width={80} height={30} className="black"></Button>
              </FormGroup>
            </Form>
          </div>
          <div className="delivery_inform">
            {
            	(()=>{
            		switch(fetch_status){
            		  case 'uninit': return <div className="loading"></div>;
            		  case 'init': return resp.length? 
            				<table className="delivery_table">
			                    <thead className="delivery_table_head">
			                      <tr>
			                        <td className="delivery_code">提货码</td>
			                        <td className="order_number">订单编号</td>
			                        <td className="delivery_state">状态</td>
			                        <td className="vaildity_period">有效期</td>
			                        <td className="delivery_store">提货门店</td>
			                        <td className="delivery_operate">操作</td>
			                      </tr>
			                    </thead>
			                    {this.delliveryCertBody()}
			                </table>:<h1 className="info">暂无凭证</h1>;
            		  case 'error': return <h1 className="error">网页出错</h1>;
            		}
            	})()
            }
            
          </div>
        </div>
      );
  }
}