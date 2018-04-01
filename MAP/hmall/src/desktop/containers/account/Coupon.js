import React, { Component } from 'react'
import Button from '../../components/Button'
import TextBox from '../../components/TextBox'
import { TabPanel, Tab } from '../../components/Tab'
import Form ,{ FormGroup } from '../../components/Form'
import AlertWindow from '../../components/AlertWindow'
import Icon from '../../components/Icon';
import Moment from 'moment';

export default class Coupon extends Component{

  constructor(props){
    super(props);
    this.state={
        appear: false,
        text: '',
        resp: [],
        value: '',
        init: true,
        flag_available:true,
        flag_used:true,
        flag_invalid:true,
        resp_available:[],
        resp_used:[],
        resp_invalid:[],
    }
  }
  
  receiveData(){
    fetch(`${promoteService}/promotion/coupon/list/STATUS_01`,{
      headers:Hmall.getHeader({}),
    })
    .then(Hmall.convertResponse('json',this))
    .then(json => {
      this.setState({init:true,resp_available:json.resp||[],flag_available:false})
    })
    .catch(Hmall.catchHttpError())
  }
  
  /*初始化获取数据 */
  componentWillMount(){   
  let userId=Hmall.getCookie('userid');
    if(userId){
      this.receiveData()
    }
  }
  
  componentWillUnmount(){
    this.isUnMounted = true;
  }
  
/*兑换优惠券*/
  checkCoupon(){
    if(this.state.value==''){
      this.setState({appear:!this.state.appear,text:'优惠码不能为空'})
    }else{
      fetch(`${promoteService}/promotion/coupon/convert`,{
      method:"post",
      body:JSON.stringify({"couponCode":this.state.value}),
      headers:Hmall.getHeader({
        "Content-Type":"application/json",
      }),
    })
      .then(Hmall.convertResponse('json',this))
      .then(json=>{if(json.success){
        let {STATUS} =json.resp||[]
        this.setState({
          appear:!this.state.appear,
          init:true,
          text:'兑换成功',
          });
      }
      else{
        if(json.msgCode=="PROMOTE_COUPON_001"){
          this.setState({appear:!this.state.appear,text:'该优惠码不存在'})
        }
        else if(json.msgCode="PROMOTE_COUPON_002"){
          this.setState({appear:!this.state.appear,text:'优惠码失效'})
        }
        }
      })
      .catch(Hmall.catchHttpError())
    }
  }
  
  handleWinClose(){
    this.state.appear = false;
    this.receiveData();
  }

  getCoupons(type){
    let temp = this.getType(type),
        flag = temp[1];
    if(flag){
      let  {flag_available,flag_used,flag_invalid,resp_available,resp_used,resp_invalid} = this.state;
    fetch(`${promoteService}/promotion/coupon/list/${type}`,{
      headers:Hmall.getHeader({}),
    })
      .then(Hmall.convertResponse('json',this))
      .then(json=>{if(json.success){
        if(type=="STATUS_01"){
          this.setState({resp_available:json.resp,flag_available:false})
        }
        if(type=="STATUS_02"){
          this.setState({resp_used:json.resp,flag_used:false})
        }
        if(type=="STATUS_03"){
          this.setState({resp_invalid:json.resp,flag_invalid:false})
        }
      }})
      .catch(Hmall.catchHttpError())
    }
  }
  
   /*获取优惠券背景色的类型*/
  getRange(range){
    switch( range){
      case'ALL': return "imgOne" ;
      case'PICKUP': return "imgTwo";
      case'EXPRESS': return "imgThree";
    }
  }
  /*页面中英文对应*/
  expressRange(range){
    switch(range){
      case'ALL': return "店铺通用" ;
      case'PICKUP': return "门店专用";
      case'EXPRESS': return "快递专用"; 
    }
  }

  getType(type){
    switch( type){
      case'STATUS_01':return [this.state.resp_available,this.state.flag_available]
      case'STATUS_02':return [this.state.resp_used,this.state.flag_used]
      case'STATUS_03':return [this.state.resp_invalid,this.state.flag_invalid]
    }
  }
  
  /*获取优惠券类型*/
  coupon(type){
    let className1="imgOne",
        status = type;
    var temp= this.getType(type);
    let resp_temp = temp[0],
          flag = temp[1];
      if(!resp_temp.length){
        return <div>
                 <div className="img-circle"><strong>!</strong></div>
                 <span className="coupon-information">暂无优惠券信息</span>  
               </div>
      }
      else{
      return resp_temp.map((detail,index)=>{
        let { range, status, startDate, endDate, couponName, benefit } = detail;
          var className1= this.getRange(range);
          if(status=="STATUS_03"){
            className1="imgFour";
          }
          return <div key={index} className= {className1} style={{opacity:status=='STATUS_02'?0.3:1}} >
          <div className="img-top"><p>{Moment(Number(startDate)).format('YYYY.MM.DD')}~{Moment(Number(endDate)).format('YYYY.MM.DD')}</p></div>
          <div className="split"></div>
          <div className="img-middle">
            <div className="middle-one">
              <p>{couponName}</p>
            </div>
            <div className="middle-two">
              <p>{benefit}</p>
            </div>
          </div>
          <div className="split"></div>
          <div className="img-bottom"><p>{this.expressRange(range)}</p></div>
          {
            status=="STATUS_02" && <Icon name="coupon-used"></Icon>
          }
        </div>
        })
      }
  }
  

  render() {
    if(this.state.init==false){
      return (<div className="loading"></div>)
    }else{
      return (
        <div id="coupon-div">
          <AlertWindow appear={this.state.appear} text={this.state.text}  onClose={() => this.handleWinClose()}/>
          <div className="coupon-button">
            <Form>
              <FormGroup>
                <span className="coupon-input">输入兑换码:</span>
                <TextBox className="coupon-text" onChange={(event)=>{this.setState({value:event.target.value})}} width={200} height={42}></TextBox>
                <Button onClick={()=>this.checkCoupon()} text="兑换" width={94} height={42} className="white"></Button>
              </FormGroup>
            </Form>
          </div>
          <div className="tab-div">
            <TabPanel width={977}>
              <Tab title="可使用的优惠券" width={173} onClick={() =>this.getCoupons("STATUS_01")}>
                <div className="img-div clearfix">
                  {this.coupon("STATUS_01")}
                </div>
              </Tab>
              <Tab title="已使用的优惠券" width={173} onClick={() =>this.getCoupons("STATUS_02")}>
                <div className="img-div clearfix">
                  {this.coupon("STATUS_02")}
                </div>
              </Tab>
              <Tab title="已失效的优惠券" width={173} onClick={() =>this.getCoupons("STATUS_03")}>
                <div className="inform-div clearfix">
                  {this.coupon("STATUS_03")}
                </div>
              </Tab>
            </TabPanel> 
          </div>
        </div>
      );
      }
    }
  }
