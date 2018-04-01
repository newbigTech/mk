/**
 * Created by ZWL on 2016/10/22.
 */
import React, { Component } from 'react';
import { Link, IndexLink ,browserHistory} from 'react-router';
import { Panel, Row, Col} from '../../components/Layout';
import Flow from '../../components/Cart/Flow';
import DatePicker from '../../components/DatePicker';
import DatetimePicker from '../../components/DatetimePicker';
import TextBox from '../../components/TextBox';
import Button from '../../components/Button';
import AddressManager from '../../components/AddressManager';
import Form, { FormGroup } from '../../components/Form';
import Moment from 'moment';
import ComboBox from '../../components/ComboBox';
import { getProvinces, getCitys, getDistricts } from '../../components/Address';
import Radio from "../../components/Radio";
import Icon from '../../components/Icon';
import SimilarityProduct from '../../components/Cart/SimilarityProduct'

export default class Settlement extends Component {
  componentWillUnmount() {
    this.isUnMounted = true;
  }

  componentDidMount() {
    let { state }  = this.props.location;
    if (state) {
      let { tempIds }  = state;
      {/*获得订单优惠卷的接口*/
      }
      fetch(`${odService}/order/queryForTempOrders`, {
        method: "POST",
        headers: Hmall.getHeader({
          "Content-Type": "application/json"
        }),
        body: JSON.stringify(
            tempIds
        )
      })
          .then(Hmall.convertResponse('json',this))
          .then(json=> {
                let { resp , success ,msgCode} = json, arr = resp, sum = 1 ,coupon = {"couponId": "", "couponName": "不使用优惠卷"},content = {"cartprice":0,"items":[]};
                if (success) {
                  let timeArr = this.state.deliveryTimes , hour , cartprice = 0;
                  arr.forEach((a, i)=> {
                    cartprice = cartprice + a.price.account
                    a.products.forEach((p,i)=>{
                      content.items.push({"id":p.productDetailInfo.productCode,"count":p.quantity})
                    })
                    content.cartprice = cartprice
                    Hmall.loadXiaoNeng(content)
                    if (a.distributionId == "") {
                      timeArr[i] = "ALL"
                      this.setState({express_flag: true});
                    } else {
                      hour = Moment(Date.now()-Date.now()%3600000+7200000).format('HH')
                      timeArr[i] = hour>20||hour<8?Date.now()-Date.now()%(3600000*24)+86400000:Date.now()-Date.now()%3600000+7200000
                      this.setState({pick_deal: true});
                    }
                    if(a.address.userPhone==null){
                      a.address.userPhone = ""
                    }
                  })
                  content.cartprice = cartprice
                  sum = Math.ceil(arr.length/3)

                  fetch(`${droolsService}/sale/calculation/precomputed`,{
                    method:"POST",
                    headers: Hmall.getHeader({
                      "Content-Type":"application/json"
                    }),
                    body:JSON.stringify(
                        resp
                    )
                  })
                      .then(Hmall.convertResponse('json',this))
                      .then(json=>{
                        let { success ,resp } = json
                        if(success){
                          arr.forEach((a,ii)=>{
                            resp.forEach((r,i)=>{
                              if(a.tempId == r.tempId){
                                a.activity = r
                                a.activity.coupons.push(coupon)
                                a.price = r.price
                              }
                            })
                          })
                          this.setState({order: arr, deliveryTimes: timeArr, fetch_status: "init" , order_height:this.state.order_height * sum});
                        }else{
                          let activity = {"coupons":[{"couponId": "", "couponName": "不使用优惠卷"}],"optimumCoupon":null}
                          arr.forEach((a,ii)=>{
                            a.activity = activity
                          })
                          this.setState({order: arr, deliveryTimes: timeArr, fetch_status: "init" , order_height:this.state.order_height * sum});
                        }
                      })
                      .catch(Hmall.catchHttpError(() => {
                        let activity = {"coupons":[{"couponId": "", "couponName": "不使用优惠卷"}],"optimumCoupon":null}
                        arr.forEach((a,ii)=>{
                          a.activity = activity
                        })
                        this.setState({order: arr, deliveryTimes: timeArr, fetch_status: "init" , order_height:this.state.order_height * sum});
                      }));
                } else {
                }
              }
          )
          .catch(Hmall.catchHttpError(() => this.setState({fetch_status: "error"})));

      {/*获得用户快递地址的接口*/
      }
      fetch(`${urService}/customer/address/list`, {
        method: "get",
        headers: Hmall.getHeader({
          "Content-Type": "application/json"
        }),

      })
          .then(Hmall.convertResponse('json',this))
          .then(json=> {
                let { resp , success ,msgCode} = json;
                if (success) {
                  if (resp.length == 0) {

                  } else {
                  let num = 0, arr = resp, address = {}
                  arr.forEach((r, i)=> {
                    if (r.isDefault) {
                      num = i
                    }
                  })
                  address = arr[0]
                  arr[0] = arr[num]
                  arr[num] = address
                  this.setState({change_expressNum: 0, express: arr})
                }
                } else {

                }
              }
          )
          .catch(Hmall.catchHttpError(() => this.setState({fetch_status: "error"})));


      this.state.express.some((g, i)=> {
        if (g.isDefault) {
          this.setState({get_addressId: g.addressId});
          return true;
        }
      });

    }
  }

  constructor(props) {
    super(props);
    {/*设初始状态属性*/
    }
    this.state = {
      buttonClick: false,
      order_height: 500,
      time_expressValue: "ALL",
      optionNumFlag: true,
      couponId: "",
      couponsOptions: {},
      agreement_flag: true,
      coupons: [],
      coupons_tempId: [],
      filters_select: [],
      invoice_content: "个人",
      invoice_title: "",
      invoice_needInvoice: false,
      addressStyle: {display: "none"},
      addressOut: true,
      outStyle: "",
      pick_deal: false,
      settlement_goods_All: true,
      settlement_goods_details: {display: "none"},
      show_flag: false,
      goods_details: [],
      goods_details_place: "",
      goods_details_customer: "",
      goods_details_phone: "",
      goods_details_price: 0,
      goods_details_preferential: 0,
      goods_details_freight: 0,
      goods_details_address_phone: "",
      goods_details_businessTime: "",
      order: [],
      express: [],
      submit: {
        "orderInfos": [],
        "invoice": {
          "type": "", //电子发票默认传E
          "title": "",  //需要发票个人/公司，不选择发票""
          "content": "",  //选择个人默认"个人",公司为用户所填内容，不选择发票""
          "needInvoice": true,
          "isCreate": false, //默认false
          "invoiceUrl": ""//默认""
        },
        "creationTime": 1484210050574,
        "flashCart": false
      },
      get_addressId: "",
      order_tempId: "",
      order_distributionId: "",
      order_id: 0,
      flag: true,
      check_name: false,
      check_phone: false,
      express_flag: false,
      fetch_status: "uninit",
      deliveryTimes: [],
      change_expressNum: 0,
      unit_placeholder : "" ,
      datetime : 0 ,
      OrderListReturn: {} ,
      change_address:false,
      change_coupon:false
    };
  }

  /*提交订单*/
  handleSubmit() {
    let callback = () => this.setState({buttonClick: false});
    this.setState({buttonClick: true});
    let { order , submit , invoice_title , unitValue ,invoice_needInvoice ,deliveryTimes ,coupons ,invoice_content} = this.state,
        { flashCart , delete_count } = this.props.location.state,
        orders = order,
        settlementArr = submit
    settlementArr.creationTime = new Date().getTime()
    settlementArr.invoice.type = invoice_needInvoice?"E":"N"
    settlementArr.invoice.title = invoice_title
    settlementArr.invoice.content = invoice_content
    settlementArr.invoice.needInvoice = invoice_needInvoice
    settlementArr.invoice.isCreate = false
    settlementArr.flashCart = flashCart
    settlementArr.invoiceUrl = ""
    orders.map((o, ii)=> {
      let orderInfo = {
        "tempId": "",
        "couponsId": "",
        "deliveryTime": "",
        "currency": "",
        "saleChannel": "",
        "buyerMessage": "",
        "buyerMemo": "",
        "markDesc": ""
      }
      orderInfo.tempId = o.tempId
      orderInfo.couponsId = ""
      orderInfo.deliveryTime = deliveryTimes[ii]
      orderInfo.currency = "CNY"
      orderInfo.saleChannel = "PC"
      orderInfo.buyerMessage = "无买家留言"
      orderInfo.buyerMemo = "无买家备注"
      orderInfo.markDesc = "无订单备注"
      settlementArr.orderInfos.push(orderInfo)
      orderInfo = {}
    })
    fetch(`${odService}/order/createOrders`, {
      method: "POST",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
      body: JSON.stringify(
          settlementArr
      )
    })
        .then(Hmall.convertResponse('json', this , callback))
        .then(json=> {

          let { success , resp ,msgCode} = json
          if (success) {
            Hmall.Storage.get('miniCart').deleteCart(delete_count)
            browserHistory.push({
              pathname: '/payment/order-payment.html',
              state: {order_list: resp[0]}
            });
          } else if (msgCode == "OD_CREATE_03") {
            alert("订单重复提交")
          } else if (msgCode == "OD_INVENTORY_EMPTY") {
            alert("有商品库存不足")
            browserHistory.push({pathname: '/cart.html'})
          }
        })
        .catch(Hmall.catchHttpError(callback));
  }


  /*更改默认快递地址*/
  handleChangeDefaultAddress(id) {
    fetch(`${urService}/customer/address/default/${id}`, {
      method: "post",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
    })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          if (json.success) {
            let arr = this.state.express ,state, city, district , num = 0 , address = {}
            arr.forEach((g, i)=> {
              if (id == g.addressId) {
                num = i
                g.isDefault = true
                getProvinces(provinces=> {
                  state = g.state==""?"":provinces[g.state];
                  getCitys(g.state, citys => {
                    city = g.city==""?"":citys[g.city];
                    getDistricts(g.city, districts=> {
                      district = g.districts==""?"":districts[g.district];
                      Hmall.setCookie("Address", [state, city, district]);
                      Hmall.setCookie("AddressId", [g.state, g.city, g.district]);
                    })
                  });
                })
              } else {
                g.isDefault = false
              }
            })
            this.setState({express: arr})

          }
        })
        .catch(Hmall.catchHttpError())

  }

  /*更改订单地址*/
  handleChangeOrderAddress() {
    let { order_tempId , order_distributionId ,order , change_expressNum , express} = this.state
    let order_arr = order
    order_arr.forEach((o, i)=> {
      if (o.tempId == order_tempId) {
        o.address.address = express[change_expressNum].address
        o.address.name = express[change_expressNum].consignee
        o.address.userPhone = express[change_expressNum].mobilenumber
        o.address.state = express[change_expressNum].state
        o.address.city = express[change_expressNum].city
        o.address.district = express[change_expressNum].district
        o.address.fixPhone = express[change_expressNum].fixednumber
        o.address.provinceName = express[change_expressNum].provinceName
        o.address.cityName   = express[change_expressNum].cityName
        o.address.districtName = express[change_expressNum].districtName
        //o.price.freight = resp[0].freight
      }
    })
    this.setState({order: order_arr})
    fetch(`${odService}/order/updateTempOrderAddress`, {
      method: "POST",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
      body: JSON.stringify({
        "distribution": "EXPRESS",
        "tempId": order_tempId,
        "contact": {
          "userPhone": "",
          "name": ""
        },
        "distributionId": order_distributionId  // 当快递时候的数据，

      })
    })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          let { success , resp , msgCode } = json
          if (success) {
            order_arr.forEach((o, i)=> {
              if (o.tempId == order_tempId) {
                o.price.freight = resp[0].freight
              }
            })
            this.setState({order: order_arr})
          } else {
            alert("更改地址失败")
          }
        })
        .catch(Hmall.catchHttpError(()=>{
          alert("更改地址失败")
        }))
  }

  /*更改订单联系人与电话*/
  handleChangeOrderContact() {
    let { order_tempId , goods_details_phone , goods_details_customer , order_distributionId ,
        order , order_id} =  this.state
    let order_arr = order
    order_arr.forEach((o, i)=> {
      if (o.tempId == order_tempId) {
        o.address.name = goods_details_customer
        o.address.userPhone = goods_details_phone
      }
    })
    this.setState({order: order_arr})
    {/*组件将要渲染时调用*/
    }
    fetch(`${odService}/order/updateTempOrderAddress`, {
      method: "POST",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
      body: JSON.stringify({
        "distribution": "PICKUP",
        "tempId": order_tempId,
        "contact": {
          "userPhone": goods_details_phone,
          "name": goods_details_customer
        },
        "distributionId": order_distributionId   // 当快递时候的数据，
      })
    })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          let { success , resp ,msgCode } = json
          if (success) {

          } else {
            alert("更改收货人与联系电话失败")
          }
        })
        .catch(Hmall.catchHttpError(()=>{
          alert("更改收货人与联系电话失败")
        }))
  }

  /*查看订单按钮*/
   handleOrderConfirm(order, i) {
    let { express } = this.state ,options = {} ,
        state = order.address.provinceName?order.address.provinceName:"" ,
        city = order.address.cityName?order.address.cityName:"" ,
        district = order.address.districtName?order.address.districtName:"" ,
        storeName = order.address.storeName?order.address.storeName:"" ,
        address = order.address.address?order.address.address:"",
        addressAll;
    document.body.scrollTop = 0
     addressAll =  state+" "+city+" "+district+" "+address+" , "+storeName
    order.activity.coupons.map((c, i)=> {
      let flag = true
      this.state.order.forEach((o, i)=> {
        if (o != order && o.activity.optimumCoupon != null && o.activity.optimumCoupon.couponId == c.couponId) {
          flag = false
        }
      })
      options[c.couponId]=flag?c.couponName:""
    })
    this.setState({
      check_phone:false,
      check_name:false,
      settlement_goods_All: false,
      settlement_goods_details: {display: "block"},
      goods_details: order.products,
      goods_details_place: addressAll,
      goods_details_customer: order.address.name,
      goods_details_phone: order.address.userPhone,
      goods_details_price: order.price.total,
      goods_details_preferential: order.price.discount,
      goods_details_freight: order.price.freight,
      goods_details_address_phone: order.address.storePhone,
      goods_details_businessTime: order.address.businessTime,
      outStyle: "size_fade_out",
      order_tempId: order.tempId,
      order_distributionId: order.distributionId,
      order_id: i,
      couponsOptions: options,
      optionNumFlag: order.activity.optimumCoupon == null ? true : false,
      couponId: order.activity.optimumCoupon == null ? "" : order.activity.optimumCoupon.couponId,
      change_address: false,
      change_coupon: false
    })
  }
  handleOrderListReturn(){
    document.body.scrollTop = 0
    this.setState({settlement_goods_All:true,addressOut:true,
      settlement_goods_details:{display:"block"},outStyle:"size_fade_in"})
  }
  changeCoupon(){
    let { order_id , order , couponId ,order_distributionId} = this.state, arr = order, count = 0;
    arr[order_id].activity.coupons.forEach((c, i)=> {
      if (c.couponId == couponId) {
        count = i
      }
    })
//${droolsService}

    fetch(`${odService}/order/queryForTempOrders`, {
      method: "POST",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
      body: JSON.stringify(
          [order[order_id].tempId]
      )
    })
        .then(Hmall.convertResponse('json',this))
        .then(json=> {
          let resps = {}
          if (json.success) {
            resps = json.resp[0]
            resps.couponsId = couponId
            fetch(`${droolsService}/sale/calculation/optionCoupon`, {
              method: "POST",
              headers: Hmall.getHeader({
                "Content-Type": "application/json"
              }),
              body: JSON.stringify(resps)
            })
                .then(Hmall.convertResponse('json',this))
                .then(json=> {
                      let { resp , success ,msgCode} = json, arr = order
                      if (success) {
                        arr[order_id].activity.optimumCoupon = arr[order_id].activity.coupons.length==1?null:arr[order_id].activity.coupons[count]
                        arr[order_id].price = resp[0].price
                        this.setState({order: arr})
                      } else {
                      }
                    }
                )
                .catch(Hmall.catchHttpError());
          }
        })
        .catch(Hmall.catchHttpError(
            ()=> {
              this.setState({fetch_status: "error"})
            }
        ))
  }
  /*订单详情确认*/
  handleOrderListConfirm() {
    let { order_distributionId ,change_address , change_coupon} = this.state
    document.body.scrollTop = 0
    this.setState({
      settlement_goods_All: true,addressOut:true,
      settlement_goods_details: {display: "block"}, outStyle: "size_fade_in"
    })
    if (order_distributionId == ""&&change_address) {
      this.handleChangeOrderAddress()
    } else if(change_address){
      this.handleChangeOrderContact()
    }else if(change_coupon){
      this.changeCoupon()
    }
  }

  /*返回购物车*/
  returnCart() {
    browserHistory.push({pathname: '/cart.html'})
  }

  /*新增地址*/
  handleAddressSaved() {
    this.forceUpdate();
  }

  //编辑地址薄

  openAddressManager(addressMsg) {
    this.refs.addressManager.open(this.state.express, addressMsg);
  }

  //姓名非空验证
  validateName(e) {
    let { value } = e.target;
    if (value=="")
      this.setState({check_name: true})
    else
      this.setState({ check_name: false, goods_details_customer: value,change_address:true})

  }

  //手机号验证
  validatePhone(e) {
    let { value } = e.target,
        reg = HmallConfig.mobilephone_regx;
    if (reg.test(value)) {
      this.setState({
        check_phone: false,
        goods_details_phone: value,
        change_address:true
      })
    } else
      this.setState({check_phone: true})
  }

  //错误信息显示
  displayErrorMessage(v) {
    return {display: v ? '' : 'none'};
  }

  /*快递默认地址集合*/
  renderExpress() {
    let {change_expressNum  ,express , addressOut} = this.state , address , fixednumber = ""
    return express.map((express, i)=> {
      fixednumber = express.areaCode +"-"+ express.fixednumber
        address = <li key={i}>
          <Radio name="address" value={change_expressNum} choose={i}
                 onChange={(e)=>{if(e.target.checked){this.setState({change_expressNum:i,change_address:true})}}}/>

          {express.consignee}{express.provinceName}{express.cityName}{express.districtName}{express.address}{express.mobilenumber==""?fixednumber:express.mobilenumber}
          <span onClick={() =>this.openAddressManager(express)}>修改</span>
          {express.isDefault == true ? (
              <span style={express.isDefault?({marginRight:"62px",color:"#d7d7d7"}):("")}>默认地址</span>
          ) : (
              <span className="span-click" style={!express.isDefault?({color:"#ff0000"}):("")}
                    onClick={()=>{this.handleChangeDefaultAddress(express.addressId)}}>设为默认地址</span>
          )}
        </li>
        return(
            change_expressNum==i?address:addressOut?<li key={i} style={{display:"none"}}/>:address
        )
      })
  }

  /*遍历商品集合，按送货方式和送货地址区分为不同订单*/
  renderOrder() {
    let goodsStyleCount = 0 ,{order ,time_expressValue , deliveryTimes } = this.state  ,goodListThree = {marginRight: "0px"};

    return order.map((order, i)=> {
      let count = [], number = 0  ,productsImage, address = order.address.address?order.address.address:"" ,
          provinceName = order.address.provinceName?order.address.provinceName:"" ,
          cityName = order.address.cityName?order.address.cityName:"",
          districtName = order.address.districtName?order.address.districtName:"",
          storeName = order.address.storeName?order.address.storeName:"",
          addressAll = provinceName + " " + cityName+ " " + districtName+ " " + address+ " " + storeName;

      count = order.products
      count.map((count, i)=> {
        number = number + Number(count.quantity)
      })
      productsImage = count.map((order, i)=> {
        if (i <= 3) {
          return (
              <img key={i} src={Hmall.cdnPath(order.productDetailInfo.stylePic)}/>
          )
        }
      });
      goodsStyleCount = goodsStyleCount + 1

      return (
          <div key={i} className="goodsList" style={goodsStyleCount%3==0?goodListThree:null}>
            <ul className="title">
              <li>订单{i + 1}</li>
              <li><span>{order.distributionId == "" ? "快递配送" : "门店自提"}</span></li>
            </ul>
            <div id="goods">
              <div className="ul-info">
                <ul >
                  <li><p title={addressAll}>{addressAll}</p></li>
                  <li>
                    {order.address.name?<span>{order.address.name}</span>:order.distributionId == "" ?
                    <span className="notice notice-word">请点击查看订单，填写收货地址</span> :
                    <span className="notice notice-word">请点击查看订单，填写收货人，联系方式</span>}
                    <span>{order.address.userPhone?order.address.userPhone:order.address.fixPhone}</span>
                  </li>
                </ul>
              </div>
              <div id="div-images">
                {productsImage}
                {count.length>3?<span>...</span>:""}
                <ul id="ul-number">
                  <li>共</li>
                  <li>{number}</li>
                  <li>件</li>
                </ul>
              </div>
              <ul className="ul-coupons">
                <li>优惠券</li>
                <li>{order.activity.optimumCoupon == null ? "无可用优惠券" : order.activity.optimumCoupon.couponName}</li>
              </ul>
              <ul className="ul-price">
                <li>商品金额</li>
                <li>优惠</li>
                <li>运费</li>
                <li>订单总额</li>
              </ul>
              <ul className="ul-price2">
                <li>￥ {order.price.total.toFixed(2)}</li>
                <li>- ￥ {order.price.discount.toFixed(2)}</li>
                <li>￥ {order.price.freight.toFixed(2)}</li>
                <li>￥ {order.price.account.toFixed(2)}</li>
              </ul>
              <Button width={105} height={30} text="查看订单"
                      onClick={()=>{this.handleOrderConfirm(order,i)}}/>
              {order.distributionId == "" ? (
                  <div className="time-express">
                    送货时间：
                    <Radio name="time" value="ALL" choose={time_expressValue} ref="time_all"
                           onChange={(e)=>{this.handleChangeTime(e,i,"ALL",true)}}/>
                    <span onClick={()=>{this.handleChangeTime("",i,"ALL",true)}}>任意时间</span>
                    <Radio name="time" value="WEEKEND" choose={time_expressValue} ref="time_weekend"
                           onChange={(e)=>{this.handleChangeTime(e,i,"WEEKEND",true)}}/>
                    <span onClick={()=>{this.handleChangeTime("",i,"WEEKEND",true)}}>仅周末</span>
                    <Radio name="time" value="WEEKDAY" choose={time_expressValue} ref="time_weekday"
                           onChange={(e)=>{this.handleChangeTime(e,i,"WEEKDAY",true)}}/>
                    <span onClick={()=>{this.handleChangeTime("",i,"WEEKDAY",true)}}>仅工作日</span>
                  </div>

              ) : (
                  <nav className="time-pickup">
                    <span className={this.renderDeliveryTimes(i)?"notice notice-word":""}>自提时间：</span>
                    <nav className="time-pickup-body">
                      <DatetimePicker name="start" value={deliveryTimes[i]} timeConstraints={
      {
        hours: {
          min: 8,
              max: 20
        }
      }
    } onChange={(e)=>{this.handleChangeTime(e,i,"",false)}} defaultValue={Date.now()}
                                      placeholder="请选择门店自提时间"></DatetimePicker>
                    </nav>

                  </nav>
              )}
            </div>
          </div>

      )
    })
  }

  /*不同订单的内的商品详情*/
  renderOrderDetail() {
    return this.state.goods_details.map((goods, i)=> {
      let obj = {"gDept":goods.summaryInfo.gDept,
        "intenCode":goods.summaryInfo.intenCode,
        "code":goods.summaryInfo.code}
      return (
          <tr key={i}>
            <td>
              <img src={Hmall.cdnPath(goods.productDetailInfo.stylePic)}/>
            </td>
            <td>
              <ul className="td2-ul">
                {/**/}
                <li>{goods.summaryInfo.name}</li>
                <li><Icon name="7days" title="7天无理由退换"/></li>
                <li><SimilarityProduct obj={obj} margin_left={211} right={1000}/></li>
              </ul>
            </td>
            <td>
              <ul>
                <li>颜色：{goods.productDetailInfo.styleText}</li>
                <li>尺码：{goods.productDetailInfo.size}</li>
              </ul>
            </td>
            <td>
              <ul>
                <li>{goods.productDetailInfo.price}</li>
                <li><span>初上市价：</span>{goods.summaryInfo.originPrice}</li>
              </ul>
            </td>
            <td>
              {/*计数的组件*/}
              {goods.quantity}
            </td>
            <td>
              <span>￥ </span>{(Number(goods.productDetailInfo.price) * Number(goods.quantity)).toFixed(2)  }
            </td>
          </tr>)
    })
  }

  //更改每个商品的配送时间
  handleChangeTime(e, i, time, type) {
    let arr = this.state.deliveryTimes , datetime = 0
    if (type) {
      arr[i] = time
      this.setState({time_expressValue:time})
    } else {
      if(e.toDate().getHours()<8){
        datetime = e.toDate().setHours(8)
      }else if(e.toDate().getHours()>20){
        datetime = e.toDate().setHours(20)
      }else{
        datetime = e.toDate().getTime()
      }
      arr[i] = datetime
    }
    this.setState({deliveryTimes: arr})
  }

  //判断是否每个订单都有配送时间
  renderDeliveryTimes(ii) {
    let flag = false , count = 0 ,{ deliveryTimes ,order } = this.state
    if(ii=="disable"){
      order.forEach((o, i)=> {
        if (deliveryTimes[i] < Date.now()) {
          flag = true
        }
      })
    }else{
      if(deliveryTimes[ii] < Date.now()){
        flag = true
      }
    }
    return flag
  }

  //判断是否每个订单都有联系人
  renderContact(){
    let { order } = this.state ,sum = 0
    order.forEach((o,i)=>{
      if(o.address.userPhone!=""||o.address.fixPhone!=""&&o.address.name!=""){
        sum = sum +　1
      }
    })
    return sum==order.length?false:true
  }
  //总计
  renderAccount(type) {
    let priceTotal = 0, freightTotal = 0, discountTotal = 0, accountTotal = 0, total = 0, freight = 0, discount = 0, account = 0 ,count = 0
    this.state.order.map((order, i)=> {

      priceTotal = priceTotal + order.price.total
      freightTotal = freightTotal + order.price.freight
      discountTotal = discountTotal + order.price.discount
      accountTotal = accountTotal + order.price.account
      order.products.forEach((p,i)=>{
        count = p.quantity + count
      })

    })
    switch (type) {
      case 1 :
        return priceTotal.toFixed(2);
      case 2 :
        return freightTotal.toFixed(2);
      case 3 :
        return discountTotal.toFixed(2);
      case 4 :
        return accountTotal.toFixed(2);
      case 5 :
        return count;
    }
  }

  render() {
    let { fetch_status ,settlement_goods_All , show_flag , pick_deal , agreement_flag ,express_flag , express ,addressOut ,check_name,
            couponsOptions  ,invoice_needInvoice ,invoice_title , invoice_content ,optionNumFlag , goods_details_place  ,order_distributionId
            , goods_details_address_phone , goods_details_businessTime  ,check_phone , goods_details_customer , order_height , buttonClick
            ,goods_details_phone , goods_details_price,goods_details_preferential,goods_details_freight ,couponId , order_id , unit_placeholder} = this.state,
        button_returnCart_style = {display: this.props.location.state.flashCart?null : "none", marginLeft: "884px" , borderColor:"#1b1b1b"},
        button_account_style = {marginLeft: this.props.location.state.flashCart? "20px" : "1047px"},
        times =  this.renderDeliveryTimes("disable") ,
        submit_disable = buttonClick||times||!agreement_flag||express_flag&&express.length==0||this.renderContact()||invoice_title=="unit"&&invoice_content=="";
    return (
        <div className="settlement">
          <AddressManager ref="addressManager" onAddressSaved={()=>this.handleAddressSaved()}
                          flag={false}></AddressManager>
          <Flow count={2}/>
          {
            (() => {
              switch (fetch_status) {
                case 'uninit':
                  return <div className="loading"></div>;
                case 'init':
                  return  settlement_goods_All?<div className="settlement-goods">
                    <h1>订单信息</h1>
                    <div className="order-list" style={{height:order_height}}>{this.renderOrder()}</div>
                    <div className="row-invoice">
                      <span className="span-info">发票信息</span>
                      {/*发票信息修改的3个状态*/}
                      <div className="div-info">
                        {!invoice_needInvoice?<ul>
                          <li>不需要发票</li>
                          <li onClick={()=>{ this.setState({invoice_needInvoice:true,show_flag:false,invoice_title:"personal",invoice_content:"个人"})
            }}>修改
                          </li>
                        </ul>:
                        <Form className="label-left">
                          {!show_flag ? <FormGroup>
                            <label>发票类型</label>
                            <Radio name="invoiceType" value={true} choose={invoice_needInvoice}
                                   onChange={(e)=>{if(e.target.checked){
              this.setState({invoice_needInvoice:true})}}}/>
                            <span>电子发票</span>
                            <Radio name="invoiceType" value={false} choose={invoice_needInvoice}
                                   onChange={(e)=>{if(e.target.checked){
              this.setState({invoice_needInvoice:false,invoice_title:"",invoice_content:""})}}}/>
                            <span>不需要发票</span>
                          </FormGroup>:
                          <FormGroup>
                            <label>发票类型</label>
                            <span className="span-invoice">电子发票</span>
                <span className="span-click" onClick={()=>{ this.setState({show_flag:false})
            }}>修改</span>
                          </FormGroup>}
                          {show_flag ?<FormGroup>
                            <label>发票抬头</label>
                            <span className="span-invoice">{invoice_content}</span>
                          </FormGroup>:
                          <FormGroup>
                            <label>发票抬头</label>
                            <Radio name="invoiceUp" value="personal" choose={invoice_title}
                                   onChange={(e)=>{if(e.target.checked&&invoice_needInvoice){
              this.setState({invoice_title:"personal",invoice_content:"个人",unit_placeholder:""})}}}/>
                            <span>个人</span>
                            <Radio name="invoiceUp" value="unit" choose={invoice_title}
                                   onChange={(e)=>{if(e.target.checked&&invoice_needInvoice){
              this.setState({invoice_title:"unit",invoice_content:"",unit_placeholder:"请填写单位名称"})}}}/><span>单位</span>
                            {invoice_title=="unit"?<TextBox value={invoice_content=="个人"?"":invoice_content} width={187}  placeholder={unit_placeholder}
                                                            onBlur={(event)=>{ if(invoice_title=="unit"){ this.setState({invoice_content:event.target.value}) } }}/>:""}
                            <Button width={92}
                                    onClick={()=>{this.setState({show_flag:true})}}
                                    disabled={invoice_title=="unit"&&invoice_content==""} text="确定 "/>
                          </FormGroup>}
                          <FormGroup>
                            <label className="span-red">温馨提示</label>
                            <span className="span-red">1. 发票内容均为服装；2. 默认为电子发票，若需要纸质发票请联系客服</span>
                          </FormGroup>
                        </Form>}
                      </div>
                    </div>
                    <div className="div-account">
                      <div className="clearfix">
                        <ul className="ul-price">
                          <li>  ￥ {this.renderAccount(1)}</li>
                          <li>  - ￥ {this.renderAccount(3)}</li>
                          <li>  ￥ {this.renderAccount(2)}</li>
                          <li>  ￥ {this.renderAccount(4)}</li>

                        </ul>
                        <ul className="ul-text">
                          <li><span className="spa-red">{this.renderAccount(5)} </span> 件商品 , 总商品金额： </li>
                          <li>优惠： </li>
                          <li>运费： </li>
                          <li>应付总额： </li>
                        </ul>
                      </div>

                        {pick_deal ? (<div className="div-notice">

                          <Radio value={true} choose={agreement_flag}
                                  onChange={()=>{this.setState({agreement_flag:!agreement_flag})}}/>
                          <span style={{color:!agreement_flag?"#ff0000":""}}>我已阅读并同意</span>
                          <Link to="/payment/pickup-guide.html"><span className="span-notice">《门店自提须知》</span></Link>
                        </div>) : (<span></span>)}

                      <Button style={button_returnCart_style} id="returnCart" className="white"
                              onClick={()=>{this.returnCart()}} text="返回购物车" height={49} width={143}/>
                      <Button style={button_account_style} className="red"
                              disabled={submit_disable}
                              onClick={()=>{this.handleSubmit()}} text="提交订单" height={49} width={143}/>
                    </div>
                  </div>: <div className="details-out">
                    <div className="goods-details ">
                      <h1 className="span-title">订单确认-订单{order_id + 1}</h1>
                      <div className="goods-details-body">


                        {order_distributionId == "" ?
                            (
                                <div className="goods-details-body-place">
                                  <span className="span-place">快递配送</span>

                                  <ul className="EXPRESS">
                                    <li><Button text="新增收货地址" className="add-address" width={95} height={30}
                                                onClick={()=>this.openAddressManager()}></Button></li>
                                    <li/>
                                    {this.renderExpress()}
                                    {this.state.express.length <= 1 ? <span></span> :<li>
                                      <span className="addAddress"
                                            onClick={()=>{this.setState({addressOut:!addressOut})}}>{addressOut?"更多地址":"收起地址"}</span>
                                      <Icon name="open-address"  onClick={() => this.setState({addressOut:!addressOut})}
                                            style={{transform:addressOut? "rotate(180deg)":"rotate(0deg)",marginTop:"1px",marginLeft:"-8px"}}/>
                                    </li>}
                                  </ul>
                                </div>

                            ) : (
                            <div className="goods-details-body-place">
                              <span className="span-place">门店自提</span>
                              <ul className="PICKUP">
                                <li>取货地址：{goods_details_place}</li>
                                <li>
                                  <span>营业时间：{goods_details_businessTime}</span>
                                  <span>联系电话：{goods_details_address_phone}</span>
                                </li>
                                <li>
                                  <FormGroup>
                                    <label className="notice">取货人：</label>
                                    <TextBox name="consignee" value={goods_details_customer} placeholder="请填写取货人"
                                             width={150} height={33} onChange={e => this.validateName(e)}></TextBox>
                                    <label className="notice">手机号：</label>
                                    <TextBox name="mobilenumber" value={goods_details_phone} placeholder="请填写联系方式"
                                             width={150} height={33} onChange={e => this.validatePhone(e)}></TextBox>

                                  </FormGroup>
                                </li>
                                <li>
                                  <span className="notice notice-word" id="span-phone" style={this.displayErrorMessage(check_phone)}>手机号为空或格式不对</span>
                                  <span className="notice notice-word"  style={this.displayErrorMessage(check_name)}>取货人姓名为空</span>
                                </li>
                              </ul>
                            </div>
                        )}

                        <div className="goods-details-body-table">
                          <span className="span-table">商品信息</span>
                          <table>
                            <thead>
                            <tr>
                              <td>商品图片</td>
                              <td>商品名称</td>
                              <td>尺码/颜色</td>
                              <td>价格</td>
                              <td>数量</td>
                              <td>小计</td>
                            </tr>
                            </thead>
                            <tbody>
                            {this.renderOrderDetail()}
                            </tbody>
                          </table>
                        </div>

                      </div>
                      <div className="goods-details-body-coupons">
                        <span>优惠券</span>
                        {optionNumFlag ? "无可用优惠券" :
                            <ComboBox name="status" options={couponsOptions} value={couponId}
                                      width={200} onBlur={(e)=>{this.setState({couponId:e.target.value,change_coupon:true}) }}/>}

                      </div>
                      <div className="goods-details-body-account">
                        <ul>
                          <li>  ￥ {goods_details_price.toFixed(2)}</li>
                          <li>  - ￥ {goods_details_preferential.toFixed(2)}</li>
                          <li>  ￥ {goods_details_freight.toFixed(2)}</li>
                          <li>  ￥ {(goods_details_price - goods_details_preferential + goods_details_freight).toFixed(2)}</li>
                        </ul>
                        <ul>
                          <li>商品金额： </li>
                          <li>优惠： </li>
                          <li>运费： </li>
                          <li>订单合计： </li>
                        </ul>
                      </div>
                      <Button className="white" text="返回" style={{marginLeft:"940px",marginRight:"20px"}} width={130} height={40}
                              onClick={()=>{this.handleOrderListReturn()}}/>
                      <Button className="red" text="确定" width={130} height={40} disabled={check_phone||check_name}
                              onClick={()=>{this.handleOrderListConfirm()}}
                      />

                    </div>
                  </div>
                      ;
                case 'error':
                  return <h1 className="error">网页出错</h1>
              }
            })()
          }

        </div>
    );
  }
}