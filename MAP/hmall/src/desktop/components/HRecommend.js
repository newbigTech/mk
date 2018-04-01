/**
 * Created by ZWL on 2017/2/22.
 */
import React, { Component } from 'react';
import { Panel } from './Layout';
import Product from './Product';
import Button from './Button';

export default class HRecommend extends Component{

  constructor(props) {
    super(props);
    this.state = {
      overFlag: false
    }
  }

  renderProducts(products, overFlag) {
    return products.map((item,i)=>{
      if(overFlag || i < 6){
        return <li key={i}><Product info={item} width={188}></Product></li>;
      }
    });
  }

  render(){
    let { overFlag } = this.state,
      products = CMSConfig.recommends,
      flag = products.length > 6;
    return <Panel className="h-history">
      <div className="panel-header"><span>店铺推荐</span></div>
      <div className="panel-body">
        <ul>
          {this.renderProducts(products, overFlag)}
        </ul>
      </div>
      {flag &&
          <Button text={overFlag ? "收起" : "更多"}
                  onClick={()=>{this.setState({overFlag: !overFlag})}}></Button>}
    </Panel>;
  }
}