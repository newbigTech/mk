import React, { Component } from 'react';
import { TabPanel, Tab } from '../../components/Tab';
import Form,{ FormGroup } from '../../components/Form';
import TextBox from '../../components/TextBox';
import Button from '../../components/Button';
import ProductAndOrderViewer from '../../components/ProductAndOrderViewer';
import { Panel,Row,Col } from '../../components/Layout';
import {browserHistory} from 'react-router';
import TextArea from '../../components/TextArea';

export default class ApplyRefund extends Component{
  
  componentWillMount(){
    let detailId = this.props.location.query.uid;
    fetch(`${odService}/refundInfo/${detailId}`,{
      method: 'GET',
      headers:Hmall.getHeader({})
    })
    .then(Hmall.convertResponse('json',this))
    .then(json =>{
      if(json.success){
        let { compensateRation,orderInfo,maxCompensateRation } = json.resp[0],
            { unitPrice ,quantity, } = orderInfo, money =(unitPrice*quantity*compensateRation/100).toFixed(2),
            reparationsMoney = money<maxCompensateRation?money:maxCompensateRation.toString();
        this.setState({
          fetch_status: 'init',
          resp: json.resp ,
          reparationsMoney
        });
        fetch(`${odService}/notdeliverReason/`+detailId,
        {
          method: 'GET',
          headers: Hmall.getHeader({"Content-Type":"Application/json"})
        })
        .then(Hmall.convertResponse('json',this))
        .then(json=>{
          if(json.success){
            if(json.resp[0].relatedcomp=="Y") {
                this.setState({ showFlag : true });
            }
            this.setState({notdeliverReason: json.resp});
          }
        })
      }else{
      this.setState({fetch_status: 'init'});
        if(json.msgCode=="OD_INFO_001"){
          alert("必输字段为空");
        }else if(json.msgCode=="OD_REFUND_004"){
          alert("没有查到匹配的值");
        }else if(json.msgCode=="OD_INFO_005"){
          alert("获取商品库存出错");
        }else if(json.msgCode=="OD_INFO_012") {
          alert("获取订单支付时间出错");
        }
      }
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
  
  constructor(props){
    super(props);
    this.state ={
      reparationsMoney : 0,
      showFlag: false,
      fetch_status: 'uninit',
      resp:[],
      charNum: 0,
      notdeliverReason:[]
    }
  }
  
  showOption(option){
    return option.map(reason => {
      const { value, meaning } = reason;
      return <option value={value} key={value}>{meaning}</option>;
    });
  }
  
  submitApply(e) {
    let { refundReason, refundDesc, orderId, productId, detailId } = e.target,{ showFlag, reparationsMoney } = this.state;
    e.preventDefault();
    if(!refundReason.value) {
      alert("请选择退货原因");
    }else{
        if(confirm("是否确认提交申请")){
          fetch(`${odService}/refundNotDeliver`,
          //fetch(`http://10.211.52.221:5555/hmall-od-service/refundNotDeliver`,
          {
            method:'post',
            headers:Hmall.getHeader({'Content-Type': 'application/json'}),
            body:JSON.stringify({
              orderId: orderId.value,
              productId: productId.value,
              refundReason: refundReason.value,
              refundDesc: refundDesc.value,
              detailId: detailId.value,
              compensateMoney:  showFlag?reparationsMoney:"",
            })
          })
          .then(Hmall.convertResponse('json',this))
          .then(json =>{
            if(json.success){
              browserHistory.push('/account/order-center/refund-return-list.html');
            }else{
              if(json.msgCode=="OD_INFO_001"){
                alert("必输字段为空");
              }else if(json.msgCode=="OD_INFO_002"){
                alert("orderId对应的订单商品详情信息为空");
              }else if(json.msgCode=="OD_INFO_004"){
                alert("修改订单商品标志出错");
              }else if(json.msgCode=="OD_INFO_007"){
                alert("退款表存在改退款信息，不能重复退款");
              }else if(json.msgCode=="OD_INFO_008"){
                alert("申请退款数据插入退款表出错")
              }
            }
          })
          .catch(Hmall.catchHttpError(()=>{
            alert("服务器繁忙");
          }))
        }
    }
  }
  
  limitChar(e) {
    let value = e.target.value;
    if(value.length>200){
      value = value.substr(0,199);
    }
    return value;
  }
  
  countChar(e) {
    let value = e.target.value;
    this.setState({charNum: value.length})
  }

  changeReason(e) {
    let val = e.target.value, { notdeliverReason } = this.state;

    notdeliverReason.map( (item, i)=>{
      if(item.value == val ){
        this.setState({showFlag: item.relatedcomp=="Y"});
      }
    } )

  }
  
  render(){
      let { resp = [], showFlag, reparationsMoney } = this.state,
      { orderInfo,invenList, count, paytime,shippMoney,refundMoney,deliveryNum, freight, remaining, compensateRation, maxCompensateRation } = resp[0]||[],
      { unitPrice ,quantity, productCode,size, productId,orderId,name,mainPic,styleText,detailId} = orderInfo||[];

       let { paymentTime }= paytime||[];
      return(
          <Row>
          {
            (()=>{
              switch(this.state.fetch_status){
              case 'uninit': return <div className="loading"></div>;
              case 'init': return resp.length?<Row>
                        <Col className="orderViewer">
                        <ProductAndOrderViewer shopName={"申请退款商品"} productCode={productCode} price={unitPrice} orderId={orderId} quantity={quantity}  name={name} mainPic={mainPic} style={styleText} size={size} paymentTime={paymentTime}></ProductAndOrderViewer>
                      </Col>
                      <Col>
                        <Panel>
                          <TabPanel id="apply-refund">
                            <Tab title="退款">
                              <Form onSubmit={(e) =>this.submitApply(e)}>
                                <FormGroup>
                                  <span>退款原因：<span className="notice"></span></span>
                                  <select name="refundReason" onChange={(e)=>{this.changeReason(e)}} style={{ width: 240, height: 40}}>
                                    {
                                      this.showOption(this.state.notdeliverReason)
                                    }
                                  </select>
                                </FormGroup>
                                <FormGroup>
                                  <span>退款金额：<span ></span></span>
                                  <TextBox className="notice-distance" readOnly={true} name="refundSum" width={80} height={40} value={"￥"+(unitPrice*quantity+(remaining==1?freight:0))}></TextBox>
                                </FormGroup>
                                <FormGroup style={{ display: showFlag?"":"none" }}>
                                  <span>赔付金额：<span ></span></span>
                                  <TextBox className="notice-distance" readOnly={true} name="refundReparations" width={80} height={40} value={"￥"+(reparationsMoney)}></TextBox>
                                  <span className="remark-span">(最大赔付金额:￥{maxCompensateRation})</span>
                                </FormGroup>
                                <FormGroup>
                                  <div className="returnExplain">
                                    <span className="return-explain">退货说明：</span>
                                    <span className="char-limit">({this.state.charNum}/200字)</span>
                                  </div>
                                  <div>
                                    <TextArea className="notice-distance" onChange={e => this.countChar(e)} maxLength={200} width={570} height={65} name="refundDesc"></TextArea>
                                  </div>
                                </FormGroup>
                                <input name="productId" type="hidden" value={productId}></input>
                                <input name="orderId" type="hidden" value={orderId}></input>
                                <input name="detailId" type="hidden" value={detailId}></input>
                                <FormGroup>
                                  <Button type="submit" text="提交申请" className="red button-left" width={150}></Button>
                                </FormGroup>
                              </Form>
                            </Tab>
                          </TabPanel>
                        </Panel>
                      </Col>
                     </Row>:<h1 className="info">读取信息失败</h1>;
          case 'error': return <h1 className="error">网页出错</h1>;
              }
            })()
          }
          </Row>
      );
  }
}