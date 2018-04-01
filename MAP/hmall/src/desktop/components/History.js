import React, { Component, PropTypes } from 'react';
import Button from './Button';
import { Panel } from './Layout';
import Product from './Product';

export default class History extends Component{

  constructor(props) {
    super(props);
    this.state = {
      overFlag: false
    };
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
      products = Hmall.getBrowserHistories(),
      flag = products.length > 6;
    return <Panel className="h-component h-history">
      <div className="panel-header"><span>最近浏览</span></div>
      <div className="panel-body">
        <ul>
          {this.renderProducts(products, overFlag)}
        </ul>
      </div>
      {flag &&
        <Button text={overFlag?"收起":"更多"}
                onClick={()=>{this.setState({overFlag:!overFlag})}}></Button>}
    </Panel>;
  }
}