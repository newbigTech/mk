/**
 * Created by ZWL on 2017/2/22.
 */
import React, { Component } from 'react';
import { Link, IndexLink, browserHistory } from 'react-router';
import { Panel } from './../Layout';
import Product from './../Product';
import Button from '../../components/Button';
import Icon from '../../components/Icon';

export default class SimilarityProduct extends Component{
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  componentDidMount(){
    fetch(`${pdService}/product/similar`,{
      method:"POST",
      headers: {
        'Content-Type': 'application/json'
      },
      body:JSON.stringify(this.props.obj)
    })
    .then(Hmall.convertResponse('json',this))
    .then(json=>{
      let { resp , success } = json
      if(success){
        this.setState({
          products: resp,
          totalPage: Math.ceil(resp.length / 6)
        });
      }
    })
    .catch(Hmall.catchHttpError())
  }

  constructor(props) {
    super(props);
    this.state = {
      products: [],
      left: 0,
      totalPage: 0,
      changePage: 0 ,
      icon: true
    }
  }
  //推荐商品

  renderProducts(products) {
    return products.map((item,i)=>{
      return <li key={i}><Product info={item} width={188}></Product></li>;
    });
  }

  renderRound(){
    let { totalPage , changePage} = this.state , arr = []

    for (let i = 0;  i < totalPage; i++)
      arr.push(<li key={i} style={{backgroundColor:changePage==i?"#000":"#dadada"}}></li>)

    return arr
  }

  toLeft(){
    let left = this.state.left
    left = left - 1190
    this.setState({left:left,changePage:this.state.changePage+1})
  }

  toRight(){
    let left = this.state.left
    left = left + 1190
    this.setState({left:left,changePage:this.state.changePage-1})
  }

  mouse(){
    this.setState({icon:!this.state.icon})
  }

  render(){
    let { margin_left , right } = this.props,
      { products , left , totalPage , changePage , icon} = this.state;
    return <span className="h-similar" onMouseEnter={()=>{this.mouse()}} onMouseLeave={()=>{this.mouse()}}>相似宝贝
      {icon?<Icon name="open-address" style={{transform:"rotate(90deg)"}}/>:
                    <Icon name="cart-similar"/>}
        <div className="overflow" style={{marginLeft:products.length!=0?-margin_left:""}}>
          <div className="triangle" style={{right:right}}></div>
          {
            products.length ?
            <div className="similarity">
              {changePage ? <Icon name="cart_red_left" onClick={()=>{this.toRight()}}></Icon> : <Icon name="cart_gray_left"></Icon>}
              <div className="over_div">
                <ul style={{left:left}}>
                  {this.renderProducts(products)}
                </ul>
              </div>
              {changePage!=totalPage-1?<Icon name="cart_red_right" onClick={()=>{this.toLeft()}}></Icon>:<Icon name="cart_gray_right"></Icon>}
              <ul className="circle" style={{marginLeft:625-totalPage*15/2}}>
                {this.renderRound()}
              </ul>
            </div>:
            <div>没有相似宝贝</div>
          }
      </div>
    </span>;
  }
}