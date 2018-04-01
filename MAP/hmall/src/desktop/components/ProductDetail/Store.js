import React, { Component, PropTypes } from 'react';
import { Link } from "react-router";
import Radio from "../Radio";
import Icon from "../Icon";

export default class Store extends Component {
  constructor(props) {
    super(props);
    this.state = {
      hide_flag: true,
    }
  }

  render() {
    let { showFlag,choose,info,flag,code } = this.props;
    if (showFlag) {
      return <li className="store_li">
        <div className="radio_choose">
          <Radio name="stores" value={info.code} choose={choose} onChange={()=>{this.props.changeChecked(info)}}/>
          <span className="displayName_span">{info.name}</span>
          <span className="express_div" style={{display:flag?"":"none"}}>{info.stock}</span>
        </div>
      </li>
    }
    return (
        <li className="store_li">
          <div className="radio_choose">
            <Radio name="stores" value={info.code} choose={choose}
                   onChange={()=>{this.props.changeChecked(info)}}/>
            <span className="displayName_span">{code}.{info.displayName}</span>
            <Icon name="open-store" style={{transform:this.state.hide_flag?"rotate(0deg)":"rotate(180deg)"}}
                  onClick={()=>{this.setState({hide_flag:!this.state.hide_flag})}}></Icon>
            <Link to={"/search.html?storeCode="+info.code}><span className="check_input">查看该门店商品</span></Link>
            <span className="stock_div" style={{display:flag?"":"none"}}>{info.stock}</span>
          </div>
          <div className="detail_store" style={{height:this.state.hide_flag?"0px":"80px"}}>
            <div className="address_div">
              地址：{info.address}
            </div>
            <div>
              营业时间：{info.openingTime}-{info.closingTime}
            </div>
            <div>
              联系电话：{info.contactNumber}
            </div>
          </div>
        </li>
    )
  }
}