import React, { Component } from 'react';
import Store from "../components/ProductDetail/Store";
import Icon from '../components/Icon';
import { initMap,initializeMarkers, moveMap } from  "../components/ProductDetail/Map";
import Button from "../../desktop/components/Button";
import ComboBox from "./ComboBox";
import { getProvinces, getCitys, getDistricts } from './Address';

export default class AlertMap extends Component {

  componentWillUnmount() {
    document.body.style.overflowY = 'auto';
    document.body.style.marginRight = "0px";
    this.isUnMounted = true;
  }
  
  queryStore(obj,text) {
    let  callback = () => this.setState({loading_flag: false});
    fetch(`${stService}/stock/queryOthers`, {
      method: 'post',
      headers: Hmall.getHeader({"Content-Type": "application/json"}),
      body: JSON.stringify(obj)
    })
        .then(Hmall.convertResponse('json', this, callback))
        .then(json=> {
              if (json.resp.length > 0) {
                initializeMarkers(json.resp, (resp)=> {
                  this.handleChangeChecked(resp);
                }, (arr)=> {
                  let flag = true;
                  arr.forEach((item)=> {
                    if (item.code == this.state.onCheck) {
                      flag = false;
                    }
                  })
                  if (flag) {
                    this.setState({onCheck: ""});
                  }
                  this.setState({store_info: arr});
                })
              }
              this.setState({
                store_info: json.resp,
                onCheck: "",
                storeName: "",
                text,
              });
            }
        ).catch(Hmall.catchHttpError(callback))
  }

  getInfo(code, level, text, productCode = this.state.productCode, productId = this.state.productId,flag = false) {
    let obj = {
      "areaId": code,
      "type": level,
      "productCode": productCode
    };
    if (productId) {
      obj.productId = productId;
      this.getStock(productCode, productId);
    }
    if(flag) {
      if (text != this.state.text) {
        setTimeout(()=> {
          Hmall.loadBaiduMap(() => {
            initMap(text, ( level * 2 + 5));
            this.queryStore(obj,text);
          },this)
        }, 500, this);
      }else {
        setTimeout(()=> {
          this.queryStore(obj, text);
        }, 500, this);
      }
    }else {
      initMap(text, ( level * 2 + 5));
      this.queryStore(obj,text);
    }
    this.setState({loading_flag: true});
  }


  getStock(productCode, productId) {
    fetch(`${stService}/stock/query`, {
      method: 'post',
      headers: Hmall.getHeader({"Content-Type": "application/json"}),
      body: JSON.stringify({
        productId: productId,
        distribution: "EXPRESS",
        productCode: productCode
      })
    }).then(Hmall.convertResponse('json', this))
        .then(json=> {
          if (json.success) {
            this.setState({deliveryStock: json.resp[0].stock});
          }
        }).catch(Hmall.catchHttpError());
  }

  componentWillMount() {
    getProvinces(provinces => {
      this.setState({provinces});
    });
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.store_class == "store_fade_out") {
      let { defaultAddress, productId, productCode } = nextProps,
          { state, city, district } = this.state,
          _productId = this.state.productId,
          _state = defaultAddress[0],
          _city = defaultAddress[1],
          _district = defaultAddress[2];
      document.body.style.overflowY = 'hidden';
      document.body.style.marginRight = "17px";
      if (_state != state || _city != city || _district != district || productId != _productId) {
        let address = this.getAddress(_state, _city, _district),
            callback = (citys, districts) => {
              Object.assign(this.state, {
                state: _state,
                citys,
                districts,
                city: _city,
                district: _district,
                productId,
                productCode
              });
              if (_district) {
                this.getInfo(_district, "4", citys[_city] + districts[_district], productCode, productId,true);
              } else {
                this.getInfo(_city, "3", citys[_city], productCode, productId, true);
              }
            };
        if (_city) {
          getCitys(_state, citys => {
            getDistricts(_city, districts => {
              callback(citys, districts);
            });
          });
        } else {
          callback({}, {});
        }
      }
    }
  }

  //省切换
  changeProvince(e) {
    let {value} = e.target;
    getCitys(value, citys => {
      let city = Object.keys(citys)[0],
          cityName = citys[city];
      getDistricts(city, districts => {
        Object.assign(this.state, {
          state: value,
          citys,
          districts,
          city,
          district: ''
        });
        this.getInfo(city, "3", this.state.provinces[value]);
      });
    });
  }

  //城市切换
  changeCity(e) {
    let {value} = e.target;
    getDistricts(value, districts => {
      Object.assign(this.state, {
        city: value,
        district: '',
        districts
      });
      this.getInfo(value, "3", this.state.citys[value]);
    });
  }

  //区县切换
  changeDistrict(e) {
    let {value} = e.target,
        { city, citys, districts } = this.state,
        code, level, text;
    this.state.district = value;
    if (value) {
      code = value;
      level = "4";
      text = citys[city] + districts[value];
    } else {
      code = city;
      level = "3";
      text = citys[city];
    }
    this.getInfo(code, level, text);
  }

  getAddress(state = this.state.state, city = this.state.city, district = this.state.district) {
    let { provinces, citys, districts } = this.state;
    return (provinces[state] || '') + (citys[city] || '') + (districts[district] || '');
  }

  constructor(props) {
    super(props);
    this.state = {
      text: "",
      state: "",
      city: "",
      district: "",
      provinces: {},
      citys: {},
      districts: {},
      loading_flag: true,
      deliveryStock: 0,
      productId: "",
      productCode: "",
      chooseFlag: false,
      store_info: [],
      onCheck: "",
      selectStock: 0,
      storeName: ""
    };
  }


  handleChangeChecked(info) {
    if (info.code != "delivery") {
      if (window.BMap) {
        moveMap(info.longitude, info.latitude, info.code);
      }
      if (info.state != this.state.state || info.city != this.state.city) {
        getCitys(info.state, citys => {
          var city = info.city;
          getDistricts(city, districts => {
            Object.assign(this.state, {
              state: info.state,
              citys,
              districts,
              city,
              district: '',
              text: citys[city]
            });
          });
        });
      }
      this.setState({onCheck: info.code, state: info.state, storeName: info.displayName, selectStock: info.stock});
    } else {
      this.setState({onCheck: "delivery", storeName: "", selectStock: info.stock});
    }
  }

  setStores() {
    var items = [];
    if (this.props.show_flag) {
      items.push(<Store info={ {code:"delivery",name:"快递配送",stock:this.state.deliveryStock}} flag={true}
                        choose={this.state.onCheck} key={""} code={""}
                        changeChecked={this.handleChangeChecked.bind(this)} showFlag={true} />)
    }
    if (this.state.store_info) {
      this.state.store_info.forEach((item, i)=> {
        items.push(<Store info={item} flag={this.props.show_stock} choose={this.state.onCheck} key={item.code}
                          code={(i+1)} changeChecked={this.handleChangeChecked.bind(this)} showFlag={false} />)
      })
    }
    return items;
  }

  searchStore() {
    let value = this.refs.search.value;
    this.setState({loading_flag: true});
    fetch(`${scService}/i/storeSearch/searchStoreNameList`, {
      method: 'post',
      headers: Hmall.getHeader({"Content-Type": "application/json"}),
      body: JSON.stringify({
        "displayName": value
      })
    }).then(Hmall.convertResponse('json', this))
        .then(json=> {
          initMap("中国", 5);
          this.setState({loading_flag: false,store_info:json.resp});
          initializeMarkers(json.resp, (resp)=> {
            this.handleChangeChecked(resp);
          }, (arr)=> {
            let flag = true;
            arr.forEach((item)=> {
              if (item.code == this.state.onCheck) {
                flag = false;
              }
            })
            if (flag) {
              this.setState({onCheck: ""});
            }
            this.setState({store_info: arr});
          })
          this.refs.search.value = "";
        })
  }

  confirm() {
    let obj, { productId,productCode,onCheck,state, city, district,selectStock,storeName } = this.state;
    if (!this.state.selectStock) {
      if (this.state.productId == "") {
        obj = {
          "distribution": "PICKUP",
          "productCode": productCode,
          "storeId": onCheck
        }
      } else {
        obj = {
          productId: productId,
          "distribution": "PICKUP",
          "productCode": productCode,
          "storeId": onCheck
        }
      }
      fetch(`${stService}/stock/query`, {
        method: 'post',
        headers: Hmall.getHeader({"Content-Type": "application/json"}),
        body: JSON.stringify(obj)
      }).then(Hmall.convertResponse('json', this))
          .then(json=> {
            if (json.success) {
              let stock = json.resp[0].stock;
              this.setState({selectStock: stock});
              this.props.confirm([state, city, district], this.getAddress(), storeName, onCheck, stock);
            }
          }).catch(Hmall.catchHttpError());
    } else {
      this.props.confirm([state, city, district], this.getAddress(), storeName, onCheck, selectStock);
    }

  }

  hideMap() {
    setTimeout(() => {
      document.body.style.marginRight = "0px";
      document.body.style.overflowY = 'auto';
    }, 500, this);
  }

  render() {
    let { state, city, district, provinces, citys, districts } = this.state;
    return (
        <div className={"div_store "+(this.props.store_class)}>
          <div className="map" id="map"></div>
          <div className="search_store">
            <input type="text" ref="search" className="key_find" placeholder="输入门店关键字" id="suggestId"
                   onKeyDown={(e)=>{if(e.keyCode==13){this.searchStore()}}}/>
            <Icon name="search-store" onClick={()=>{this.searchStore()}}></Icon>
          </div>
          <div className="select_stores">
            <div className="div_select">
              <ComboBox value={state}
                        onChange={(event)=>{this.changeProvince(event)}} options={provinces}>
              </ComboBox>
              <ComboBox value={city}
                        onChange={(event)=>{this.changeCity(event)}} options={citys}>
              </ComboBox>
              <ComboBox value={district}
                        onChange={(event)=>{this.changeDistrict(event)}}
                        options={districts} emptyOption=" ">
              </ComboBox>
            </div>
            <div className="title_span">门店库存</div>
            {this.state.loading_flag ? <div className="loading"></div> :
                <ul>
                  {this.setStores()}
                </ul>
            }
            <div className="button_li">
              <Button text="取消" className="button_cancel_store" onClick={()=>{this.props.cancel();this.hideMap()}}/>
              <Button text="确定" className={"button_sure"+(this.state.onCheck==""?" button_disable":"")}
                      disabled={this.state.onCheck==""} onClick={()=>{this.confirm(),this.hideMap()}}/>
            </div>
          </div>
        </div>
    )
  }
}