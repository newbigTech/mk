import React, { Component } from 'react';
import Form, { FormGroup } from './Form';
import TextBox from './TextBox';
import { Panel } from './Layout';
import Button from './Button';
import ComboBox from '../components/ComboBox';
import { getProvinces, getCitys, getDistricts } from './Address';

export default class AddressManager extends Component{
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  componentWillMount() {
    getProvinces(provinces => {
      this.setState({provinces});
    });
  }
  
  constructor(props){
    super(props);
    this.state = {
      appear: false,
      phone_error: null,
      name_error: null,
      address_error: null,
      buttonClick: false,
      error_msg: null,
      isNew: false,
      addAddress: true
    };
  }
  
  canAddAddress(){
    let { phone_error, areas_error, name_error, address_error, buttonClick } = this.state;
    return !phone_error && !areas_error && !name_error && !address_error && !buttonClick;
  }
  
  handleSubmit(e) {
    e.preventDefault();
    let { rearfixednumber,headfixednumber,address,consignee,mobilenumber,zipcode,area } = e.target,
      { addressMsg, state, city, district, isDefault, addressList, provinces, citys, districts } = this.state;
     let  addressId = addressMsg && addressMsg.addressId, districtName = districts&&district!=""?districts[district]:"",
        areaCode = "",provinceName = provinces[state],cityName = citys?citys[city]:"",
        fixednumber = "";
    if(headfixednumber.value!=""&&rearfixednumber.value!=""){
      areaCode = headfixednumber.value,
      fixednumber = rearfixednumber.value;
    }
    if(this.canAddAddress()){
      let callback = () => this.setState({buttonClick: false});
      this.setState({buttonClick: true});
      fetch(addressId ? `${urService}/customer/address/update/${addressId}` : `${urService}/customer/address/insert`,{
        method:'post',
        headers: Hmall.getHeader({'Content-Type': 'application/json'}),
        body: JSON.stringify({
          address: address.value,
          consignee: consignee.value,
          mobilenumber: mobilenumber.value,
          zipcode: zipcode.value,
          state,
          city,
          district,
          fixednumber,
          provinceName,
          cityName,
          districtName,
          areaCode,
        })
      })
      .then(Hmall.convertResponse('json',this, callback))
      .then(json =>{
        let { success, msgCode, resp } = json || {},
            userId = addressMsg? addressMsg.userId : Hmall.getCookie('userid'),
            { onAddressSaved } = this.props;
        if(success){
          let addressObj = {
            zipcode: zipcode.value,
            isDefault,
            consignee: consignee.value,
            address: address.value,
            mobilenumber: mobilenumber.value,
            fixednumber,
            areaCode,
            state,
            city,
            provinceName,
            cityName,
            districtName,
            district,
            userId,
            addressId: addressId || resp[0].addressId,
          };
          if(addressMsg)
            Object.assign(addressMsg, addressObj);
          else
            addressList.push(addressObj);
          onAddressSaved(addressObj);
          this.close();
        }else{
          this.setState({error_msg: '信息未填写完整或格式不正确'});
        }
      })
      .catch(Hmall.catchHttpError(callback));
    }
  }
  
//省切换
  changeProvince(e) {
    let {value} = e.target;
    getCitys(value, citys => {
      let city = Object.keys(citys)[0]||'';
      Object.assign(this.state,{
        citys,
        state: value
      });
      this.changeCity({
        target: {
          value: city
        }
      });
    });
  }
  
  
  //城市切换
  changeCity(e){
    let {value} = e.target;
    getDistricts(value, districts => {
      let district = Object.keys(districts)[0]||'';
      Object.assign(this.state,{
        districts,
        city: value
      });
      this.changeDistrict({
        target: {
          value: district
        }
      });
    });
  }
  
  //区县切换
  changeDistrict(e) {
    let {value} = e.target;
    Object.assign(this.state,{
      district: value
    });

    this.handleExamAddress();
  }
  
 //关闭浮层
  close(){
    this.setState({ appear: false,
      phone_error: null,
      name_error: null,
      address_error: null,
      buttonClick: false,
      error_msg: null,
      isNew: false});
  }
  
 //打开浮层
  open(addressList, addressMsg){
    
    let obj = {
      appear: true,
      addressMsg,
      addressList,
      state: '',
      city: '',
      district: '',
      citys: undefined,
      districts: undefined,
      key: Date.now()
    };
    if(addressMsg) {
      let { state, city, district } = addressMsg;
      Object.assign(obj, {
        state,
        city,
        district,
        isDefault: addressMsg.isDefault,
        isNew: false
      });
      getCitys(state, citys => {
        obj.citys = citys;
        if(obj.citys && obj.districts) {
          this.setState(obj);
        }
      });
      getDistricts(city, districts => {
        obj.districts = districts;
        if(obj.citys && obj.districts) {
          this.setState(obj);
        }
      });
      this.setState({addAddress:false})
    }else{
      obj.isDefault = !addressList.length;
      obj.isNew = true;
      this.setState(obj);
      this.setState({addAddress:true})
    }
  }
  
  //姓名非空验证
  validateName(e){
    let { value } = e.target;
    this.setState({name_error: value ? null : '收货人姓名不能为空'});
  }
  
//详细地址非空验证
  validateAddress(e){
    let { value } = e.target;
    this.setState({address_error: value ? null : '详细地址不能为空'});
  }
  
//手机号验证
  validatePhone(e){
    let { mobilenumber, headfixednumber, rearfixednumber } = e.target.form,
        v1 = mobilenumber.value,
        v2 = headfixednumber.value + '-' + rearfixednumber.value,
        mobileReg = HmallConfig.mobilephone_regx,
        phoneReg = HmallConfig.phone_regx;
   if(!v1 && v2 == '-') {
     this.setState({phone_error: '手机号和固定电话至少一个不能为空',addAddress:true});
   }else if(v1 && !mobileReg.test(v1)) {
     this.setState({phone_error: '手机格式不正确',addAddress:true});
   }else if(v2 != '-' && !phoneReg.test(v2)) {
     this.setState({phone_error: '固定电话格式不正确',addAddress:true});
   }else{
     this.setState({phone_error: null,addAddress:false});
   }
  }

  hasDistricts(){

    return this.state.city==""?true:!!Object.keys(this.state.districts||{}).length;
  }

//验证地址不为空
  handleExamAddress(e){
    let { state, city , district } = this.state;
    this.hasDistricts()?this.setState({areas_error: state && city && district  ? null : '地址信息不完整'}):this.setState({areas_error: state && city   ? null : '地址信息不完整'});
  }

  showErrorMsg(error_msg) {
    return error_msg ? <p className="error-msg" >{error_msg}</p> : null
  }
  
  render(){
    let {addAddress , appear, state, city, district, provinces, citys, districts, name_error, address_error, error_msg, phone_error, buttonClick, addressMsg = {}, isNew, key } = this.state,
      { consignee, address, mobilenumber, fixednumber, region, zipcode , areaCode } = addressMsg;
/*    if(fixednumber){
      var fixednumberArr = fixednumber.split("-"),
        headfixednumber = fixednumberArr[0],
        rearfixednumber = fixednumberArr[1];
    }*/
    return (
      <div id="new_add_address_one" key={key} style={{display: appear ? 'block' : 'none'}}>
        <div className="address_position">
          <div className="address_title_one">
            <span>{isNew ? '新增收货地址':'编辑收货地址'}</span>
            <span onClick = {()=>this.close()}></span>
          </div>
          <div className="address_main">
            <Form onSubmit={e => this.handleSubmit(e)}>
              <FormGroup>
                <span className="notice">收货人：</span>
              </FormGroup>
              <FormGroup>
              <TextBox name="consignee" width={215} height={40} onBlur={e => this.validateName(e)} value={consignee}></TextBox>
              </FormGroup>
              {
                this.showErrorMsg(name_error)
              }
              <FormGroup>
                <label className="notice">所在地区:</label>
              </FormGroup>
              <FormGroup>
                <ComboBox value={state} name="state" options={provinces} emptyOption="请选择" onChange = {e=>this.changeProvince(e)}>
                </ComboBox>
                <ComboBox value={city} name="city" options={citys} emptyOption="请选择" onChange = {e=>this.changeCity(e)}>
                </ComboBox>
                {this.hasDistricts()?
                    <ComboBox value={district} name="district" options={districts} emptyOption="请选择" onChange = {e=>this.changeDistrict(e)}>
                    </ComboBox>:""}
              </FormGroup>
              <FormGroup>
                <label className="notice">详细地址:</label>
              </FormGroup>
              <FormGroup>
              <TextBox name="address" width={470} height={40} onBlur={e => this.validateAddress(e)} value={address}></TextBox>
              </FormGroup>
              {
                this.showErrorMsg(address_error)
              }
              <FormGroup>
                <label>邮编:</label>
              </FormGroup>
              <FormGroup>
              <TextBox name="zipcode" width={165} height={40} value={zipcode}></TextBox>
              </FormGroup>
              <FormGroup>
                <label className="notice">手机号:</label>
                <label className="mob_phone">固定电话:</label>
              </FormGroup>
              <FormGroup>
                <TextBox name="mobilenumber" width={215} height={40} onChange={e => this.validatePhone(e)} value={mobilenumber}></TextBox>
                <span className="phoneline">或</span>
                <TextBox name="headfixednumber" width={70} height={40}  onChange={e => this.validatePhone(e)} value={areaCode}></TextBox>
                <p className="phoneline">-</p>
                <TextBox name="rearfixednumber" className="textbox_location" width={190} height={40}
                         onChange={e => this.validatePhone(e)} value={fixednumber}></TextBox>
              </FormGroup>
              {
                this.showErrorMsg(phone_error)
              }
              {
                this.showErrorMsg(error_msg)
              }
              <FormGroup>
                {this.props.flag?(
                  <p>
                    <Button type="reset" onClick = {()=>this.close()} text="取消" className="white" width={85} heigth={35}></Button>
                    <Button type="submit" disabled={!this.canAddAddress()} text="确定" className="red" width={85} heigth={35}></Button>
                  </p>
                ):
                  (<Button type="submit" disabled={!this.canAddAddress()||addAddress} text="保存收货地址" className="red" width={130} heigth={40}></Button>)}
              </FormGroup>
            </Form>
          </div>
        </div>
      </div>
    )
  }
}
