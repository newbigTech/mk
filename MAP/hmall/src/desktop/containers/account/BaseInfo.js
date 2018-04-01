import React, { Component } from 'react'
import { Link, browserHistory } from 'react-router';
import Moment from 'moment';
import {Panel, Row, Col} from '../../components/Layout';
import Form, { FormGroup } from '../../components/Form';
import TextBox from '../../components/TextBox';
import Button from '../../components/Button';
import DatePicker from '../../components/DatePicker';
import ComboBox from '../../components/ComboBox';
import { getProvinces, getCitys, getDistricts } from '../../components/Address';
import RadioList from '../../components/RadioList';
import Radio from "../../components/Radio";

const sexOptions = {
  'MALE': '男',
  'FEMALE': '女',
  'NONE': '保密'
};

export default class BaseInfo extends Component{
 //初始化获取数据
  componentWillMount(){
    getProvinces(provinces => {
      this.setState({provinces});
    });
    fetch(`${urService}/customer/info/query`,{
      headers: Hmall.getHeader()
    })
    .then(Hmall.convertResponse('json',this))
    .then(json =>{
      let { success, resp } = json;
      if(success){
        let data = resp[0];
        this.receiveData(data);
        this.setState({
          resp: data || {},
          fetch_status: 'init'
        });
      }
    })
    .catch(Hmall.catchHttpError(()=>{
      this.setState({
        fetch_status: 'error'
      });
    }));
  }
  
  //刷新数据
  receiveData(obj){
    let { state, city, district, sex = 'MALE', birthday, name } = obj || {};
    getCitys(state, citys => {
      this.setState({citys});
    });
    getDistricts(city, districts => {
      this.setState({districts});
    });
    Object.assign(this.state,{
      state,
      city,
      sex,
      sexName: sexOptions[sex],
      district,
      birthday,
      name
    });
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  constructor(props){
    super(props);
    this.state={
      editable: false,
      name_error: null,
      address_error: null,
      birthday_error: null,
      mobileNumber:'',
      resp:{},
      fetch_status: 'uninit',
      editbirthday: true ,
    };
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
  
  toggleEditable(e){
    let { editable, resp } = this.state;
    if(editable) {
      this.receiveData(resp);
      this.setState({
        editable: false,
        name_error: null,
        address_error: null,
        birthday_error: null,
        error_msg: null,
        editbirthday: true
      });
    }else{
      this.setState({
        editable: true
      });
      if(!Object.keys(resp).length){
        this.setState({
          editbirthday: false
        });
      }
    }
  }
  
 //验证名字 
  handleChangeName(e){
    let { value } = e.target,
        reg = /^[ ]+$/;
    if(!reg.test(value)){
      this.setState({ name: value, name_error: value ? null : '姓名不能为空' });
    }else{
      this.setState({ name: value, name_error: '姓名不能为空' });
    }
    
  }
  
  
  //性别改变
  handleChangeSex(e){
    let { value } = e.target;
    this.setState({ sex: value, sexName: sexOptions[value]});
  }
  

  //验证地址不为空
  handleExamAddress(e){
    let { state, city, district,districts } = this.state;
    this.setState({address_error: state && city && (!this.hasDistricts() || district) ? null : '地址信息不完整'});
  }
  
  //生日改变
  handleExamBirthday(e){
    let value = e.toDate().getTime();
    this.setState({birthday: value,birthday_error: value ? null : '生日不能为空' });
  }
  
  //提交按钮可点击
  canEditeMessage(){
    let { name_error, birthday_error, state, city, district,districts } = this.state;
    return  state && city && (!this.hasDistricts() || district) && !name_error && !birthday_error;
  }
  
  
  handleSubmit(e){
    e.preventDefault();
    if(this.canEditeMessage()) {
      let { state, city, district, sex, birthday, name, resp } = this.state,
        data = { name, state, city, district, birthday: birthday && Number(birthday), sex };
      fetch(`${urService}/customer/info/update`,{
        method: 'post',
        headers: Hmall.getHeader({'Content-Type': 'application/json'}),
        body: JSON.stringify(data)
      })
      .then(Hmall.convertResponse('json',this))
      .then(json =>{
        if(json.success){
          alert("编辑成功");
          this.receiveData(data);
          Object.assign(resp, data);
          this.setState({
            editable:false,
            editbirthday: true,
            error_msg:''
          });
          Hmall.setCookie('username', name);
          let shortcut = Hmall.Storage.get('shortcut');
          shortcut.setState({user: name});
        }else{
          this.setState({
            error_msg: "请填写正确信息",
          });
        }
      })
      .catch(Hmall.catchHttpError());
    }
  }
 
  changePhoneNum() {
    browserHistory.push({pathname: '/account/security/change-phone.html',state:false});
  }

  showErrorMsg(error_msg) {
    return error_msg ? <p className="error-msg" >{error_msg}</p> : null
  }
  
  hasDistricts(){
    return !!Object.keys(this.state.districts||{}).length;
  }
  
  render() {
    let mobileNumber = Hmall.getCookie('mobile'),
      { editbirthday,init, editable, resp, birthday_error, name_error, address_error, state, city, district, sex, sexName, birthday, name, provinces, citys, districts,error_msg } = this.state,
      orginSex = resp.sex ;
      return (
        <Panel id="baseinfo_div">
          <Row>
            <Col>{
              (()=>{
                switch(this.state.fetch_status){
                case 'uninit': return <div className="loading"></div>;
                case 'init': return <Form className="label-left" onSubmit={e => this.handleSubmit(e)}>
                      <FormGroup>
                        <label className="notice">姓名</label>
                        <TextBox name="name" width={151} readOnly={!editable} value={name} onChange={e => this.handleChangeName(e)}></TextBox>
                      </FormGroup>
                      {this.showErrorMsg(name_error)}
                      <FormGroup>
                        <label className="notice">性别</label>
                        {
                          editable && !orginSex ? 
                          <RadioList name="sex" value={sex} width={69} options={sexOptions} onChange={e => this.handleChangeSex(e)}></RadioList> :
                          <span className="readonly">{orginSex?sexName:""}</span>
                        }
                      </FormGroup>
                      <FormGroup>
                        <label className="notice">生日</label>
                        <DatePicker name="birthday" value={birthday} width={308} disabled={editbirthday} onChange={e => this.handleExamBirthday(e)}></DatePicker>
                      </FormGroup>
                      {this.showErrorMsg(birthday_error)}
                      <FormGroup>
                        <label className="notice">城市</label>
                        <ComboBox disabled={!editable} width={150} value={state} name="provinceCode" options={provinces} emptyOption="请选择省" onChange = {e=>this.changeProvince(e)}></ComboBox>
                        <ComboBox disabled={!editable} width={150} value={city} name="cityCode" options={citys} emptyOption="请选择市" onChange = {e=>this.changeCity(e)}></ComboBox>
                        {orginSex?
                          (this.hasDistricts() || !editable) &&
                            <ComboBox disabled={!editable} width={150} value={district} name="districtCode" options={districts} emptyOption="请选择区" onChange = {e=>this.changeDistrict(e)}></ComboBox>:
                            <ComboBox disabled={!editable} width={150} value={district} name="districtCode" options={districts} emptyOption="请选择区" onChange = {e=>this.changeDistrict(e)}></ComboBox>
                        }
                      </FormGroup>
                      {this.showErrorMsg(address_error)}
                      <FormGroup>
                        <label className="notice">手机</label>
                        <TextBox width={187} readOnly={true} value={mobileNumber}></TextBox>
                        {
                          !editable && <Button onClick={this.changePhoneNum} text="修改手机号" width={92}></Button>
                        }
                      </FormGroup>
                      {this.showErrorMsg(error_msg)}
                      <FormGroup>
                        <Button onClick={(e) => this.toggleEditable(e)} text={editable?'取消':'编辑'} className={editable ? null : 'red'} width={100} height={35}></Button>
                        {
                          editable && <Button type="submit" disabled={!this.canEditeMessage()} text="确定" className="red" width={100} height={35}></Button>
                        }
                      </FormGroup>


                    </Form>;
                case 'error': return <h1 className="error">网页出错</h1>;
                }
              })()
            }
              
            </Col>
          </Row>
        </Panel>
      );
  }
}