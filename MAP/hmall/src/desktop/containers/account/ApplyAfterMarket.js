import React, { Component } from 'react';
import { TabPanel, Tab } from '../../components/Tab';
import Form, { SubForm, FormGroup } from '../../components/Form';
import TextBox from '../../components/TextBox';
import Button from '../../components/Button';
import { Panel, Row, Col } from '../../components/Layout';
import ProductAndOrderViewer from '../../components/ProductAndOrderViewer';
import { browserHistory } from 'react-router';
import AddressManager from '../../components/AddressManager';
import PicUpload from '../../components/PicUpload';
import TextArea from '../../components/TextArea';
import Radio from '../../components/Radio';
import Icon from '../../components/Icon';
import NumberBox from '../../components/NumberBox';

export default class ApplyAfterMarket extends Component {

  componentWillMount() {
    let detailId = this.props.location.query.uid;
    if (detailId) {
      //fetch(`http://10.211.52.221:5555/hmall-od-service/refundInfo/${detailId}`, {
      fetch(`${odService}/refundInfo/${detailId}`, {
        headers: Hmall.getHeader({})
      })
          .then(Hmall.convertResponse('json', this))
          .then(json => {
            if (json.success) {
              if ((json.resp[0].orderInfo.quantity - json.resp[0].count) <= 0) {
                this.setState({
                  fetch_status: 'init',
                  resp: json.resp,
                  canNotRefundAndReturn: 'YES',
                  canNotReplacement: 'YES'
                });
              } else {
                let { shippMoney,refundMoney, orderInfo,deliveryNum, count, compensateRation ,maxCompensateRation,replaceAmount} = json.resp[0],
                    { quantity, unitPrice } = orderInfo, summary = (deliveryNum * unitPrice) - shippMoney - refundMoney - replaceAmount,
                    money = (unitPrice * quantity * compensateRation / 100).toFixed(2),
                    reparationsMoney = money < maxCompensateRation ? money : maxCompensateRation.toString();
                this.setState({
                  fetch_status: 'init',
                  resp: json.resp,
                  returnPrice: summary.toFixed(2),
                  onlyReturnPrice: summary.toFixed(2),
                  returnNum: deliveryNum - count,
                  reparationsMoney,
                  maxMoney: summary,
                });
              }
              fetch(`${odService}/shippingRefundReason/` + detailId,
                  {
                    method: 'GET',
                    headers: Hmall.getHeader({"Content-Type": "Application/json"})
                  })
                  .then(Hmall.convertResponse('json', this))
                  .then(json=> {
                    if (json.success) {
                      if (json.resp[0].relatedcomp == "Y") {
                        this.setState({return_refund_flag: true});
                      }
                      this.setState({shippingRefundReason: json.resp, returnChooseReason: json.resp[0].value});
                    }
                  })
            } else {
              this.setState({fetch_status: 'init'});
              if (json.msgCode == "OD_INFO_001") {
                alert("必输字段为空");
              } else if (json.msgCode == "OD_REFUND_004") {
                alert("没有查到匹配的值");
              } else if (json.msgCode == "OD_INFO_005") {
                alert("获取商品库存出错");
              } else if (json.msgCode == "OD_INFO_012") {
                alert("获取订单支付时间出错");
              }
            }
          })
          .catch(Hmall.catchHttpError(()=> {
            this.setState({fetch_status: 'error'});
          }));
    }
  }

  componentDidMount() {
    fetch(`${urService}/customer/address/list`, {
      headers: Hmall.getHeader({})
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          if (json.success) {

            json.resp.map((respAdr, index) => {
              if (respAdr.isDefault) {
                this.setState({addressId: respAdr.addressId});
                if (index != 0) {
                  respAdr.index = 0;
                  json.resp[0].index = index;
                }
              } else {
                respAdr.index = index;
              }
            })
            this.setState({respAddr: json.resp});
          }
        })
        .catch(Hmall.catchHttpError());
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }

  constructor(props) {
    super(props);
    this.state = {
      replaceValue: "",
      onlyRefundValue: "",
      refundValue: "",
      reparationsMoney: 0,
      returnFlag: false,
      return_refund_flag: false,
      fetch_status: 'uninit',
      expand: false,
      respAddr: [],
      resp: [],
      canNotRefundAndReturn: 'NO',
      canNotReplacement: 'NO',
      returnNum: 0,
      returnPrice: 0,
      onlyReturnPrice: 0,
      appear: false,
      size: '',
      styleText: '',
      paths: [],
      addressId: '',
      size_choose: 0,
      color_choose: 0,
      sizesColor: [],
      styleTextSizes: [],
      indexChange: 1,
      charNum: 0,
      sizes: [],
      distribution: 'EXPR_WAREHOUSE',
      choose_address: '',
      shippingRefundReason: [],
      returnChooseReason: "",
      refundReason: [],
      refundChooseReason: "",
      replaceReason: [],
      maxMoney: 0,
    };
  }

  submitReturnAndRefund(e) {
    e.preventDefault();
    let { orderId, productId, returnQuantity, refundAmount, refundReason, refundDesc, detailId,return_method } = e.target;
    let { resp,distribution,reparationsMoney,return_refund_flag  } = this.state,
        { shippMoney,refundMoney, orderInfo, count, store, warehouseList } = resp[0],
        { quantity, unitPrice } = orderInfo,
        reg = /^[0-9]*[1-9][0-9]*$/;
    if (this.state.paths) {
      this.state.paths.splice(1, this.state.paths[this.state.paths.length - 1]);
    }
    if (!refundReason.value) {
      alert("请选择退货原因");
      e.preventDefault();
    } else if (!refundDesc.value) {
      alert("请填写退款说明");
      e.preventDefault();
    } else {
      if (confirm("是否确认提交申请")) {
        let refundAddr = {};
        if (distribution == "EXPR_WAREHOUSE") {
          refundAddr = warehouseList[0];
        } else {
          refundAddr = store;
        }
        fetch(`${odService}/shippingRefunds`, {
          //fetch(`http://10.211.52.221:5555/hmall-od-service/shippingRefunds`, {
          method: 'post',
          headers: Hmall.getHeader({'Content-Type': 'application/json'}),
          body: JSON.stringify({
            orderId: orderId.value,
            productId: productId.value,
            returnQuantity: returnQuantity.value,
            refundAmount: refundAmount.value,
            refundReason: refundReason.value,
            refundDesc: refundDesc.value,
            paths: this.state.paths,
            code: refundAddr.code,
            refundAddr,
            returnWay: return_method.value || 'EXPR_WAREHOUSE',
            detailId: detailId.value,
            compensateMoney: return_refund_flag ? reparationsMoney : "",
          })
        })
            .then(Hmall.convertResponse('json', this))
            .then(json => {
              if (json.success) {
                browserHistory.push('/account/order-center/refund-return-list.html');
              } else {
                if (json.msgCode == "OD_INFO_001") {
                  alert("必输字段为空");
                } else if (json.msgCode == "OD_REFUND_001") {
                  alert("退款金额小于0");
                } else if (json.msgCode == "OD_INFO_002") {
                  alert("orderId对应的订单商品详情信息为空");
                } else if (json.msgCode == "OD_REFUND_002") {
                  alert("退货换货的数量超过该商品的总数量");
                } else if (json.msgCode == "OD_REFUND_003") {
                  alert("退款金额大于该商品的总金额");
                } else if (json.msgCode == "OD_INFO_004") {
                  alert("修改订单商品标志出错");
                } else if (json.msgCode == "OD_REFUND_008") {
                  alert("退货件数不符合要求");
                } else if (json.msgCode == "OD_INFO_009") {
                  alert("申请售后数据插入退款表出错");
                }
              }
            })
            .catch(Hmall.catchHttpError(()=> {
              alert("服务器繁忙");
            }));
      }
    }
  }


  submitOnlyReturn(e) {
    let { refundReason,refundAmount,refundDesc,orderId,productId,detailId } = e.target,
        { returnFlag,reparationsMoney } = this.state;
    e.preventDefault();
    let { resp } = this.state,
        { orderInfo, count,shippMoney,refundMoney } = resp[0],
        { quantity, unitPrice } = orderInfo;
    if (this.state.paths) {
      this.state.paths.splice(1, this.state.paths[this.state.paths.length - 1]);
    }
    if (!refundReason.value) {
      alert("请选择退款原因");
    } else {
      if (!refundDesc.value) {
        alert("请填写退款说明");
        e.preventDefault();
      } else {
        if (confirm("是否确认提交申请")) {
          fetch(`${odService}/refunds`, {
            //fetch(`http://10.211.52.221:5555/hmall-od-service/refunds`, {
            method: 'post',
            headers: Hmall.getHeader({
              'Content-Type': 'application/json'
            }),
            body: JSON.stringify({
              refundReason: refundReason.value,
              refundAmount: refundAmount.value,
              refundDesc: refundDesc.value,
              orderId: orderId.value,
              productId: productId.value,
              paths: this.state.paths,
              detailId: detailId.value,
              compensateMoney: returnFlag ? reparationsMoney : "",
            })
          })
              .then(Hmall.convertResponse('json', this))
              .then(json => {
                if (json.success) {
                  browserHistory.push('/account/order-center/refund-return-list.html');
                } else {
                  if (json.msgCode == "OD_INFO_001") {
                    alert("必输字段为空");
                  } else if (json.msgCode == "OD_INFO_002") {
                    alert("orderId对应的订单商品详情信息为空");
                  } else if (json.msgCode == "OD_REFUND_003") {
                    alert("退款金额大于该商品的总金额");
                  } else if (json.msgCode == "OD_REFUND_001") {
                    alert("退款金额小于0");
                  } else if (json.msgCode == "OD_INFO_004") {
                    alert("修改订单商品标志出错");
                  } else if (json.msgCode == "OD_INFO_009") {
                    alert("申请售后数据插入退款表出错");
                  }
                }
              })
              .catch(Hmall.catchHttpError(()=> {
                alert("服务器繁忙");
              }));
        }
      }
    }
  }


  submitReplacement(e) {
    e.preventDefault();
    let { replaceReason,replaceDesc,orderId,productId,productCode,detailId } = e.target,
        { resp,respAddr,addressId }= this.state, replaceAddr = resp[0].warehouseList[0], newProductId = this.getProductId();
    respAddr.map((item, i)=> {
      if (item.addressId == addressId) {
        addressId = item;
      }
    })
    if (!newProductId) {
      alert("请选择颜色和尺码");
    } else {
      if (!replaceReason.value) {
        alert("请选择换货原因");
      } else {
        if (!replaceDesc.value) {
          alert("请填写换货说明");
        } else {

          if (confirm("是否确认提交申请")) {
            //fetch(`http://10.211.52.221:5555/hmall-od-service/shippingReplacement`, {
            fetch(`${odService}/shippingReplacement`, {
              method: 'post',
              headers: Hmall.getHeader({
                'Content-Type': 'application/json'
              }),
              body: JSON.stringify({
                productCode: productCode.value,
                replaceReason: replaceReason.value,
                replaceDesc: replaceDesc.value,
                orderId: orderId.value,
                newProductId: newProductId,
                productId: productId.value,
                code: replaceAddr.code,
                replaceAddr,
                addressId,
                detailId: detailId.value
              })
            })
                .then(Hmall.convertResponse('json', this))
                .then(json => {
                  if (json.success) {
                    browserHistory.push('/account/order-center/replace-list.html');
                  } else {
                    if (json.msgCode == "OD_INFO_001") {
                      alert("必输字段为空");
                    } else if (json.msgCode == "OD_INFO_002") {
                      alert("orderId对应的订单商品详情信息为空");
                    } else if (json.msgCode == "OD_REFUND_002") {
                      alert("退货换货的数量超过该商品的总数量");
                    } else if (json.msgCode == "OD_INFO_004") {
                      alert("修改订单商品标志出错");
                    } else if (json.msgCode == "OD_INFO_010") {
                      alert("申请售后数据插入换货表出错");
                    }
                  }
                })
                .catch(Hmall.catchHttpError(()=> {
                  alert("服务器繁忙");
                }));
          }
        }
      }
    }
  }

  changeQuantity(event) {
    let { resp, returnNum, returnPrice,maxMoney }= this.state,
        { count,deliveryNum, shippMoney,refundMoney,orderInfo,replaceAmount  } = resp[0],
        { unitPrice }= orderInfo,
        value = event.target.value, maxValue = deliveryNum - count,
        maxPrice = maxMoney > value * unitPrice ? value * unitPrice : maxMoney;
    if (value == "") {
      this.setState({returnNum: maxValue});
    } else if (returnPrice > maxPrice) {
      this.setState({returnPrice: maxPrice.toFixed(2)});
    }
  }

  getReturnType(e) {
    let { value } = e.target;
    this.setState({distribution: value});
  }


  getRadio(e, index) {
    let { respAddr } = this.state;
    respAddr.map((item, i)=> {
      if (item.index < index) {
        item.index += 1;
      } else if (item.index == index) {
        item.index = 0;
      }
    })
    let value = e.target.value;
    this.setState({addressId: value, respAddr: respAddr});
  }

  openAddressManager(addressMsg) {
    this.refs.addressManager.open(this.state.respAddr, addressMsg);
  }

  showAddressMore() {
    let { respAddr, expand } = this.state;
    if (respAddr.length > 1) {
      return (
          <div className="moreAddress">
            <span className="address_span"
                  onClick={() => this.setState({ expand: !expand})}>{expand ? '收起地址' : '更多地址'}</span>
            <Icon name="open-address" onClick={() => this.setState({ expand: !expand})}
                  style={{transform:expand? "rotate(180deg)":"rotate(0deg)"}}/>
          </div>
      );
    }
  }

  setComfirmAddress(addressId, i) {
    fetch(`${urService}/customer/address/default/${addressId}`, {
      method: 'post',
      headers: Hmall.getHeader({'Content-Type': 'application/json'})
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          let { respAddr } = this.state;
          if (json.success) {
            respAddr.forEach((item, index)=> {
              item.isDefault = index == i;
            });
            this.setState({respAddr});
          }
        })
        .catch(Hmall.catchHttpError());
  }

  comfirmAddress(isDefault, addressId) {
    if (isDefault) {
      return (
          <div className="label_position">
            <label>默认地址</label>
          </div>
      )
    }
  }

  showComfirmAddress(addressId, index) {
    return (
        <a className="address-btn" onClick={(e,i) =>this.setComfirmAddress(addressId,index)}>设为默认地址</a>
    );
  }


  showAddress() {
    let { respAddr,addressId,expand } = this.state;

    return respAddr.map((respAdr, i) => {
      let { consignee,isDefault, address, mobilenumber, provinceName, cityName, districtName,index } = respAdr;
      return (
          <ul style={{ top:(15+index*35)+"px"}} ref="address_ul">
            <li className="consignee_li"><Radio choose={addressId} onChange={(e) => this.getRadio(e,index)}
                                                name="address_name"
                                                value={respAdr.addressId} defaultChecked={isDefault}></Radio>
              {consignee} {provinceName} {cityName} {districtName} {address} {mobilenumber}
            </li>
            <li className="isDefault_li">{isDefault ? "默认地址" : this.showComfirmAddress(respAdr.addressId, i)}</li>
            <li><a className="address-btn" onClick={() =>this.openAddressManager(respAdr,index)}>修改</a></li>
          </ul>
      );
    })
  }

  handleAddressSaved(addressObj) {
    let { respAddr } = this.state;
    respAddr.map((item, i)=> {
      if (item.addressId == addressObj.addressId) {
        item.index = 0;
      } else {
        item.index += 1;
      }
    })
    this.forceUpdate();
    this.setState({addressId: addressObj.addressId});
  }

  getImage(img) {
    this.setState({paths: img});
  }

  getDefaultAddressId() {
    fetch(`${odService}/replaceReason`,
        {
          method: 'get',
          headers: Hmall.getHeader({"Content-Type": "Application/json"})
        })
        .then(Hmall.convertResponse('json', this))
        .then(json=> {
          if (json.success) {
            this.setState({replaceReason: json.resp});
          }
        })
        .catch(Hmall.catchHttpError(()=> {
          alert("服务器繁忙");
        }))
  }

  showOption(option) {
    return option.map(reason=> {
      return <option value={reason.value}>{reason.meaning}</option>;
    })
  }

  getProductId() {
    let invenList = this.state.resp[0].invenList,
        productId = '';
    invenList.map(sizeAndStyle => {
      if (this.state.size == sizeAndStyle.size) {
        if (this.state.styleText == sizeAndStyle.styleText) {
          productId = sizeAndStyle.productId;
        }
      }
    })
    return productId;
  }

  getSize(value) {

    let sizeColor = [],
        { invenList = [] } = this.state.resp[0];

    invenList.forEach((invenlist, index)=> {
      if (value == invenlist.size) {
        sizeColor.push(invenlist.styleText);
      }
    })
    if (value == this.state.size_choose) {
      this.setState({
        size_choose: 0,
        sizesColor: []
      });
    } else {
      this.setState({
        size_choose: value,
        sizesColor: sizeColor
      });
    }
  }

  showSize() {
    let { invenList = [] } = this.state.resp[0];
    invenList.forEach(invenlist => {
      if (this.state.sizes.indexOf(invenlist.size) == -1) {
        this.state.sizes.push(invenlist.size);
      }
    })
    if (this.state.styleTextSizes.length == 0) {
      return this.state.sizes.map((size, index) => {
        return <div className="singleSize" onClick={() => this.getSize(size)} key={index} value={size}
                    style={{border:this.state.size_choose==size?"1px solid red":""}}>
          {size}
          <div className="singleSizeTwo" style={{display: this.state.size_choose==size?"":"none"}}></div>
        </div>;
      })
    } else if (this.state.styleTextSizes.length != 0) {
      return this.state.sizes.map((size, index) => {
        if (this.state.styleTextSizes.indexOf(size) != -1) {
          return <div className="singleSize" onClick={() => this.getSize(size)} key={index} value={size}
                      style={{border:this.state.size_choose==size?"1px solid red":""}}>
            {size}
            <div className="singleSizeTwo" style={{display: this.state.size_choose==size?"":"none"}}></div>
          </div>;
        } else {
          return <div className="singleSizeThree">{size}</div>;
        }
      })
    }
  }

  getStyleText(value) {
    let colorSize = [],
        { invenList = [] } = this.state.resp[0];
    invenList.forEach((invenlist, index)=> {
      if (value == invenlist.styleText) {
        colorSize.push(invenlist.size);
      }
    })
    if (value == this.state.color_choose) {
      this.setState({
        color_choose: 0,
        styleTextSizes: []
      });
    } else {
      this.setState({
        color_choose: value,
        styleTextSizes: colorSize
      });
    }
  }

  showStyleText() {
    let styleTexts = [],
        { invenList = [] } = this.state.resp[0];
    invenList.forEach(invenlist => {
      if (styleTexts.indexOf(invenlist.styleText) == -1) {
        styleTexts.push(invenlist.styleText);
      }
    })
    if (this.state.sizesColor.length == 0) {
      return styleTexts.map((styleText, index) => {
        return <div className="singleSize" onClick={() => this.getStyleText(styleText)} value={styleText}
                    style={{border:this.state.color_choose==styleText?"1px solid red":""}}>
          {styleText}
          <div className="singleSizeTwo" style={{display:this.state.color_choose==styleText?"":"none"}}></div>
        </div>;
      })
    } else if (this.state.sizesColor.length != 0) {
      return styleTexts.map((styleText, index)=> {
        if (this.state.sizesColor.indexOf(styleText) != -1) {
          return <div className="singleSize" onClick={() => this.getStyleText(styleText)} value={styleText}
                      style={{border:this.state.color_choose==styleText?"1px solid red":""}}>
            {styleText}
            <div className="singleSizeTwo" style={{display:this.state.color_choose==styleText?"":"none"}}></div>
          </div>;
        } else {
          return <div className="singleSizeThree">{styleText}</div>;
        }
      })
    }
  }

  getProductId() {
    let invenList = this.state.resp[0].invenList,
        productId = '';
    invenList.map(sizeAndStyle => {
      if (this.state.size_choose == sizeAndStyle.size) {
        if (this.state.color_choose == sizeAndStyle.styleText) {
          productId = sizeAndStyle.productId;
        }
      }
    })
    return productId;
  }

  countChar(e, flag = true) {
    let value = e.target.value;
    if (flag) {
      this.setState({refundValue: value});
    } else {
      this.setState({onlyRefundValue: value});
    }
  }


  getDistributionRadio() {
    let { resp = [] } = this.state, {distribution, store} = resp[0], items = [];
    if (distribution == "EXPRESS") {
      if (store) {
        if (store.type == "P") {
          items.push(
              <li>
                <Radio choose={this.state.distribution} onChange={(e) => this.getReturnType(e)} name="return_method"
                       value="EXPR_STORE"></Radio><span>门店寄回</span>
              </li>
          )
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
                   value="SELF_STORE"/><span>门店退回</span>
          </li>
      );

    }
    return items;
  }

  changeCharNum(flag) {
    let { refundReason } = this.state, detailId = this.props.location.query.uid;
    this.setState({
      paths: [],
    });
    if (flag && refundReason.length == 0) {
      fetch(`${odService}/refundReason/` + detailId,
          {
            method: 'get',
            headers: Hmall.getHeader({"Content-Type": "Application/json"})
          })
          .then(Hmall.convertResponse('json', this))
          .then(json=> {
            if (json.success) {
              if (json.resp[0].relatedcomp == "Y") {
                this.setState({returnFlag: true});
              }
              this.setState({refundReason: json.resp, refundChooseReason: json.resp[0].value});
            }
          })
          .catch(Hmall.catchHttpError(()=> {
            alert("服务器繁忙");
          }))
    }
  }

  getReturnInfo() {
    let { resp = [] } = this.state, {warehouseList} = resp[0];
    return warehouseList.map((item, i)=> {
      return <p>{HmallConfig.contact} {item.address} {item.contactNumber}</p>;
    })
  }

  changeReturnNum(event) {
    let { resp,returnNum }= this.state,
        { count,deliveryNum } = resp[0],
        value = event.target.value, maxValue = deliveryNum - count,
        reg = /^[0-9]*[1-9][0-9]*$/;
    if (!isNaN(value) && value > 0 && reg.test(value) && maxValue >= value) {
      this.setState({returnNum: value});
    } else if (maxValue < value) {
      this.setState({returnNum: maxValue});
    } else if (value == "") {
      this.setState({returnNum: ""});
    }
  }

  judgeNumber(e, returnNum = this.state.returnNum, flag = true) {
    let { resp,maxMoney }= this.state, { shippMoney,refundMoney,orderInfo,replaceAmount }=resp[0],
        {unitPrice} =orderInfo, value = e.target.value,
        maxPrice = maxMoney > returnNum * unitPrice ? returnNum * unitPrice : maxMoney;
    if (value == "") {
      if (flag) {
        this.setState({returnPrice: maxPrice.toFixed(2)});
      } else {
        this.setState({onlyReturnPrice: maxPrice.toFixed(2)});
      }
    }
  }

  getPrice(value, maxPrice) {
    let str;
    if (!isNaN(value) && value >= 0 && maxPrice >= value) {
      str = value;
    } else if (value > maxPrice) {
      str = maxPrice.toFixed(2);
    } else if (value == "") {
      str = "";
    }
    return str;
  }

  changeReturnPrice(e, returnNum = this.state.returnNum, flag = true) {
    let { resp,maxMoney }= this.state, { shippMoney,refundMoney,orderInfo,replaceAmount }=resp[0],
        {unitPrice} =orderInfo, value = e.target.value,
        maxPrice = maxMoney > returnNum * unitPrice ? returnNum * unitPrice : maxMoney;
    if (flag) {
      this.setState({returnPrice: this.getPrice(value, maxPrice)})
    } else {
      this.setState({onlyReturnPrice: this.getPrice(value, maxPrice)})
    }
  }

  changReturnReason(e) {
    let val = e.target.value, { shippingRefundReason } = this.state;
    shippingRefundReason.map((item, i)=> {
      if (item.value == val) {
        this.setState({return_refund_flag: item.relatedcomp == "Y"});
      }
    })
    this.setState({returnChooseReason: val})
  }

  getAddressInfo() {
    let { distribution,resp = [],  } = this.state,{ store } = resp[0] ;
    if (distribution == "EXPR_WAREHOUSE") {
      return <FormGroup>
        <p>退回地址信息：{this.getReturnInfo()}</p>
      </FormGroup>
    } else if (distribution == "EXPR_STORE") {
      return <FormGroup>
        <p>寄回门店地址：{store.provinces + store.displayName} {store.contacts} {store.contactNumber}</p>
      </FormGroup>
    } else if (distribution == "SELF_STORE") {
      return (<FormGroup>
        <p>
          门店退回地址：{store.provinces + store.displayName} {store.openingTime}-{store.closingTime} {store.contactNumber}</p>
      </FormGroup>)
    }

  }

  showReturnAndRefund(code) {
    let { resp = [], returnPrice, returnNum, shippingRefundReason, refundValue, reparationsMoney, return_refund_flag,maxMoney,returnChooseReason } = this.state,
        { orderInfo,invenList, count, paytime,shippMoney,refundMoney,deliveryNum,notEndNum,replaceAmount,maxCompensateRation,
            distribution, } = resp[0],
        { unitPrice ,quantity, productCode,size, productId,orderId,name,mainPic,style,styleText,detailId } = orderInfo,
        paymentTime = paytime.paymentTime, length = refundValue.length;
    if (code == "NO") {
      if (notEndNum < 1) {
        return <Form onSubmit={(e) => this.submitReturnAndRefund(e)}>
          <FormGroup>
            <span>退货原因：<span className="notice"></span></span>
            <select name="refundReason" style={{ width: 240, height: 40}} value={returnChooseReason}
                    onChange={(e)=>{this.changReturnReason(e)}}>
              {
                this.showOption(shippingRefundReason)
              }
            </select>
          </FormGroup>
          <input name="orderId" type="hidden" value={orderId}></input>
          <input name="productId" type="hidden" value={productId}></input>
          <input name="detailId" type="hidden" value={detailId}></input>
          <FormGroup>
            <span>退款数量：<span className="notice"></span></span>
            <input className="number-input" onBlur={(event) => this.changeQuantity(event)} name="returnQuantity"
                   onChange={(e)=>{this.changeReturnNum(e)}} value={returnNum}/>
            <span className="remark">(最多{deliveryNum - count}件)</span>
          </FormGroup>
          <FormGroup>
            <span>退款金额：<span className="notice"/></span>
            <input className="number-input" onBlur={(e) => this.judgeNumber(e)} name="refundAmount"
                   onChange={(e)=>{this.changeReturnPrice(e)}} value={returnPrice}/>
          <span
              className="remark">(最多￥ { maxMoney > (returnNum * unitPrice) ? (returnNum * unitPrice).toFixed(2) : maxMoney.toFixed(2)})</span>
          </FormGroup>
          <FormGroup style={{ display:return_refund_flag?"":"none" }}>
            <span className="compensate-money">赔付金额：</span>
            <TextBox className="max-compensate" value={reparationsMoney}
                     readOnly={true}></TextBox>
            <span style={{ color: "#D0D0D0"}}>（最大赔付金额：￥{ maxCompensateRation }）</span>
          </FormGroup>
          <FormGroup>
            <div>
              <span className="return-explain">退货说明：<span className="notice"></span></span>
              <span className="char-limit">({length}/200字)</span>
            </div>
            <div>
            <TextArea onChange={e => this.countChar(e)} maxLength={200} width={570} height={65}
                      name="refundDesc" value={refundValue}></TextArea>
            </div>
          </FormGroup>
          <FormGroup>
            <span>上传凭证：</span>
            <div className="picupload-position">
              <div className="evaluate_button">
                <PicUpload getImage={(img) => this.getImage(img)} uploadState="return"></PicUpload>
              </div>
              <p>每张图片大小不超过10M，最多3张，支持GIF、JPG、PNG、BMP格式</p>
            </div>
          </FormGroup>
          <FormGroup>
            <label>退回方式：</label>
          </FormGroup>
          <FormGroup>
            <ul className="radio-ul">
              <li><Radio choose={this.state.distribution} onChange={(e) => this.getReturnType(e)} name="return_method"
                         value="EXPR_WAREHOUSE"></Radio><span>快递退回</span></li>
              {this.getDistributionRadio()}
            </ul>
          </FormGroup>
          {this.getAddressInfo()}
          <FormGroup>
            <p className="notice-word">温馨提示：请您尽快将商品寄回，并更新物流信息，超过7天未填写则系统自动取消退货单</p>
          </FormGroup>
          <FormGroup>
            <Button type="submit" text="提交申请" className="red btn-distance" width={150}></Button>
          </FormGroup>
        </Form>
      } else {
        return <div className="applygoods">有未完成的申请，请稍后再试</div>;
      }
    } else {
      return <div className="applygoods">申请正在处理，暂无商品可申请</div>;
    }
  }

  showReplacement(code) {
    let { resp = [],expand, respAddr, replaceValue } = this.state,
        { orderInfo,invenList, count, paytime,shippMoney,refundMoney,deliveryNum,notEndNum } = resp[0],
        { unitPrice ,quantity, productCode,size, productId,orderId,name,mainPic,style,detailId } = orderInfo,
        paymentTime = paytime.paymentTime;
    if (code == "NO") {
      return <Form onSubmit={(e) => this.submitReplacement(e)}>
        <div className="border-bottom">
          <span className="char-size">选择更换的商品</span>
          <span className="left-distance notice-word">温馨提示，仅可更换同款商品</span>
        </div>
        <SubForm>
          <FormGroup>
            <span>尺码：</span>
            <div clssName="showSize">
              {this.showSize()}
            </div>
          </FormGroup>
          <FormGroup>
            <span>颜色：</span>
            <div className="showStyle">
              {this.showStyleText()}
            </div>
          </FormGroup>
        </SubForm>
        <div className="border-bottom-two">
          <span className="char-size">填写换货原因</span>
        </div>
        <SubForm>
          <FormGroup>
            <span>换货原因：<span className="notice"></span></span>
            <select name="replaceReason" style={{ width: 240, height: 40}}>
              {
                this.showOption(this.state.replaceReason)
              }
            </select>
          </FormGroup>
          <input name="orderId" type="hidden" value={orderId}></input>
          <input name="productId" type="hidden" value={productId}></input>
          <input name="productCode" type="hidden" value={productCode}></input>
          <input name="detailId" type="hidden" value={detailId}></input>
          <FormGroup>
            <div>
              <span className="return-explain">换货说明：<span className="notice"></span></span>
              <span className="char-limit">({replaceValue.length}/200字)</span>
            </div>
            <div>
              <TextArea onChange={(e) => {this.setState({ replaceValue: e.target.value })}} maxLength={200} width={570}
                        height={65}
                        name="replaceDesc" value={replaceValue}></TextArea>
            </div>
          </FormGroup>
        </SubForm>
        <div className="border-bottom-two">
          <span className="char-size">选择收货地址</span>
        </div>
        <SubForm>
          <FormGroup>
            <div className="addressadd">
              <Button className="newAdd" onClick={()=>this.openAddressManager()} text="新增收货地址"></Button>
              <div style={{ height: expand? (respAddr.length*35)+"px":"35px" }} className="address_div">
                {this.showAddress()}
              </div>
              {this.showAddressMore()}
            </div>
          </FormGroup>
        </SubForm>
        <div className="border-bottom-two">
          <span className="char-size">退回商品</span>
        </div>
        <SubForm>
          <FormGroup>
            <p>退回地址信息：{this.getReturnInfo()}</p>
          </FormGroup>
          <FormGroup>
            <p className="notice-word">温馨提示：请您尽快将商品寄回，并更新物流信息，超过7天未填写则系统自动取消换货单</p>
          </FormGroup>
          <FormGroup>
            <Button type="submit" text="提交申请" className="red btn-distance" width={150}></Button>
          </FormGroup>
        </SubForm>
      </Form>;
    } else {
      return <div className="applygoods">申请正在处理，暂无商品可申请</div>;
    }
  }

  changeOnlyRefundReason(e) {
    let val = e.target.value, { refundReason,returnFlag } = this.state;
    refundReason.map((item, i)=> {
      if (item.value == val) {
        this.setState({returnFlag: item.relatedcomp == "Y"});
      }
    })
    this.setState({refundChooseReason: val});
  }

  render() {
    let { resp = [],onlyReturnPrice, reparationsMoney,returnFlag, onlyRefundValue,refundChooseReason } = this.state,
        { notEndNum,orderInfo,invenList, count, paytime,shippMoney,refundMoney,deliveryNum,replaceAmount,maxCompensateRation,compensateRation } = resp[0] || [],
        {  unitPrice ,quantity, productCode,size, productId,orderId,name,mainPic,style,styleText,detailId,} = orderInfo || [],
        { paymentTime } = paytime || [];

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
                      <ProductAndOrderViewer shopName={"申请售后商品"} productCode={productCode} price={unitPrice}
                                             orderId={orderId} quantity={quantity} name={name} mainPic={mainPic}
                                             style={styleText} size={size}
                                             paymentTime={paymentTime}></ProductAndOrderViewer>
                    </Col>
                    <Col>
                      <Panel>
                        <TabPanel id="apply-aftermarket">
                          <Tab onClick={()=> this.changeCharNum()} title="退货退款">
                            {
                              this.showReturnAndRefund(this.state.canNotRefundAndReturn)
                            }
                          </Tab>
                          <Tab onClick={() => this.changeCharNum(true)} title="仅退款">
                            {
                              notEndNum > 0 ? <div className="applygoods">有未完成的申请，请稍后再试</div> :
                                  <Form onSubmit={(e) => this.submitOnlyReturn(e)}>
                                    <FormGroup>
                                      <span>退款原因：<span className="notice"></span></span>
                                      <select name="refundReason" style={{ width: 254, height: 40}}
                                              value={ refundChooseReason }
                                              onChange={(e)=>{this.changeOnlyRefundReason(e)}}>
                                        {
                                          this.showOption(this.state.refundReason)
                                        }
                                      </select>
                                    </FormGroup>
                                    <FormGroup>
                                      <span>退款金额：<span className="notice"></span></span>
                                      <input className="number-input" onBlur={(e) => this.judgeNumber(e,quantity,false)}
                                             name="refundAmount"
                                             onChange={(e)=>{this.changeReturnPrice(e,quantity,false)}}
                                             value={onlyReturnPrice}/>
                                      <span className="remark">
                                        (最多 ￥{((quantity * unitPrice) - shippMoney - refundMoney - replaceAmount).toFixed(2)})
                                      </span>
                                    </FormGroup>
                                    <FormGroup style={{ display: returnFlag?"":"none" }}>
                                      <span className="compensate-money">赔付金额：</span>
                                      <TextBox className="max-compensate" value={reparationsMoney}
                                               readOnly={true}></TextBox>
                                      <span style={{ color: "#D0D0D0"}}>（最大赔付金额：￥{ maxCompensateRation }）</span>
                                    </FormGroup>
                                    <input name="orderId" type="hidden" value={orderId}></input>
                                    <input name="productId" type="hidden" value={productId}></input>
                                    <input name="detailId" type="hidden" value={detailId}></input>
                                    <FormGroup>
                                      <div>
                                        <span className="return-explain">退款说明：<span className="notice"></span></span>
                                        <span className="char-limit">({onlyRefundValue.length}/200字)</span>
                                      </div>
                                      <div>
                                  <TextArea onChange={e => this.countChar(e,false)} maxLength={200} width={570}
                                            height={65}
                                            name="refundDesc" value={ onlyRefundValue }></TextArea>
                                      </div>
                                    </FormGroup>
                                    <FormGroup>
                                      <span>上传凭证：</span>
                                      <div className="picupload-position">
                                        <div className="evaluate_button">
                                          <PicUpload getImage={(img) => this.getImage(img)}
                                                     uploadState="onlyReturn"></PicUpload>
                                        </div>
                                        <p>每张图片大小不超过10M，最多3张，支持GIF、JPG、PNG、BMP格式</p>
                                      </div>
                                    </FormGroup>
                                    <FormGroup>
                                      <Button type="submit" text="提交申请" className="red btn-distance"
                                              width={150}></Button>
                                    </FormGroup>
                                  </Form>
                            }

                          </Tab>
                          <Tab title="换货" onClick={() => this.getDefaultAddressId()}>
                            <AddressManager ref="addressManager"
                                            onAddressSaved={(addressObj)=>this.handleAddressSaved(addressObj)}></AddressManager>
                            {
                              this.showReplacement(this.state.canNotReplacement)
                            }
                          </Tab>
                        </TabPanel>
                      </Panel>
                    </Col>
                  </Row> : <h1 className="info">读取信息失败</h1>;
                case 'error':
                  return <h1 className="error">网页出错</h1>;
              }
            })()
          }
        </Row>
    );
  }
}