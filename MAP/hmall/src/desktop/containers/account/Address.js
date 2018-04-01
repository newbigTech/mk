import React, { Component } from 'react';
import { Link, browserHistory } from 'react-router';
import TextBox from '../../components/TextBox';
import Button from '../../components/Button';
import { Panel, Row , Col} from '../../components/Layout';
import Form, { FormGroup } from '../../components/Form';
import AddressManager from '../../components/AddressManager';
import { getProvinces, getCitys, getDistricts } from '../../components/Address';

export default class Address extends Component {

  componentWillMount() {
    fetch(`${urService}/customer/address/list`, {
      headers: Hmall.getHeader({})
    })
        .then(Hmall.convertResponse('json', this))
        .then(json => {
          let { success, resp } = json;
          if (success) {
            let num = 0, arr = resp, address = {}
            arr.forEach((r, i)=> {
              if (r.isDefault) {
                num = i
              }
            })
            if (arr.length != 0) {
              address = arr[0]
              arr[0] = arr[num]
              arr[num] = address
            }

            this.setState({
              resp: arr,
              fetch_status: 'init'
            });
          }
        })
        .catch(Hmall.catchHttpError(()=> {
          this.setState({
            fetch_status: 'error'
          });
        }));
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }


  constructor(props) {
    super(props);
    this.state = {
      fetch_status: 'uninit',
      resp: [],
      items: [],
    };
  }

  openAddressManager(addressMsg) {
    this.refs.addressManager.open(this.state.resp, addressMsg);
  }

  handleAddressSaved() {
    this.forceUpdate();
  }

  deleteAddress(addressId, i) {
    if (confirm("是否删除地址？")) {
      fetch(`${urService}/customer/address/delete/${addressId}`, {
        method: 'post',
        headers: Hmall.getHeader({'Content-Type': 'application/json'})
      })
          .then(Hmall.convertResponse('json', this))
          .then(json => {
            if (json.success) {
              let { resp } = this.state,
                  { isDefault } = resp[i];
              resp.splice(i, 1);
              if (isDefault && resp.length) {
                resp[0].isDefault = true;
              }
              this.setState({resp});
            }
          })
          .catch(Hmall.catchHttpError(()=> {
            alert("服务器繁忙");
          }));
    }
  }

  setComfirmAddress(addressId, i) {
    if (confirm("是否设置为默认地址？")) {
      fetch(`${urService}/customer/address/default/${addressId}`, {
        method: 'post',
        headers: Hmall.getHeader({'Content-Type': 'application/json'})
      })
          .then(Hmall.convertResponse('json', this))
          .then(json => {
            let { resp } = this.state;
            if (json.success) {
              resp.forEach((item, index)=> {
                if (index == i) {
                  item.isDefault = true;
                  let {provinceName, cityName, districtName}=item;
                  Hmall.setCookie("Address", [provinceName, cityName, districtName], {
                    path: '/'
                  });
                  Hmall.setCookie("AddressId", [item.state, item.city, item.district], {
                    path: '/'
                  });
                  resp.unshift.apply(resp, resp.splice(i, 1));
                } else {
                  item.isDefault = false;
                }
              })
              this.setState({resp});
            }
          })
          .catch(Hmall.catchHttpError(()=> {
            alert("服务器繁忙!");
          }));
    }
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

  showComfirmAddress(isDefault, addressId, index) {
    if (!isDefault) {
      return (
          <Button className="setAddress" onClick={(e,i) =>this.setComfirmAddress(addressId,index)} width={80}
                  height={20} text="设为默认"></Button>
      );
    }
  }

  renderAddress(resp = this.state.resp) {
    var items = [];
    resp.map((item, index)=> {
      var { fixednumber, mobilenumber, isDefault, addressId, consignee, address,state,city,district, provinceName, cityName, districtName } = item;

      items.push((
          <div className={isDefault?"div_border clearfix addDefault":"div_border clearfix addUnDefault"} key={index}>
            <div className="button_div">
              <div className="button_position"><span onClick={() => this.openAddressManager(item)}>编辑</span></div>
              <div className="button_line"><span>|</span></div>
              <div className="button_position"><span onClick={() => this.deleteAddress(addressId,index)}>删除</span></div>
              {this.comfirmAddress(isDefault, addressId)}
            </div>
            <div className="div_position">
              <div className="label_name">
                <label className="lable_head"><span>联系人：</span></label>
                <label className="lable_rear">{consignee}</label>
              </div>
              <div className="label_name">
                <label className="lable_head"><span>联系方式：</span></label>
                <label className="lable_rear">{mobilenumber || fixednumber}</label>
              </div>
              <div className="label_name">
                <label className="lable_head"><span>收货地址：</span></label>
                <label className="lable_rear">{provinceName + cityName + districtName + address}</label>
              </div>
            </div>
            <div className="label_setAdd">{this.showComfirmAddress(isDefault, addressId, index)}</div>
          </div>
      ))
    })
    return items;
  }

  render() {
    return (
        <div id="address_div" className="clearfix">
          {
            (()=> {
              switch (this.state.fetch_status) {
                case 'uninit':
                  return <div className="loading"></div>;
                case 'init':
                  return this.state.resp.length ? <div>{this.renderAddress()}</div> : <h1 className="info">暂无地址信息</h1>;
                case 'error':
                  return <h1 className="error">网页出错</h1>;
              }
            })()
          }
          <div className="new_button clearfix">
            <div className="new_button_one"><Button onClick={()=>this.openAddressManager()} className="add_adress"
                                                    text="新增收货地址" width={120} height={35}></Button></div>
          </div>
          <AddressManager ref="addressManager" onAddressSaved={()=>this.handleAddressSaved()}></AddressManager>
        </div>
    );
  }
}