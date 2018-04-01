import React, { Component, PropTypes } from 'react';
import { Link, IndexLink ,browserHistory} from 'react-router';
import onClickOutside from 'react-onclickoutside';
import Button from './Button';
import Icon from './Icon';
let enterId,leaveId;
class MiniCart extends Component{
  
  constructor(props){
    super(props);
    this.state = {
      total: 0.00,
      count: 0,
      status: 'uninit',
      show: false,
      cartList: []
    };
    Hmall.Storage.set('miniCart', this);
  }
  
  componentWillUnmount(){
    this.isUnMounted = true;
  }
  
  componentWillMount(){
    let token = Hmall.getCookie('access_token');
    if(token){
      fetch(`${ctService}/cart/mini`,{
        headers:Hmall.getHeader()
      })
          .then(Hmall.convertResponse('json',this))
          .then(json => {
            let { success, resp } = json;
            if(success){
              let { count } = resp[0];
              this.setState({
                count :count,
              });
            }else{
            }
          })
          .catch (Hmall.catchHttpError());
    }
  }
  
   //点击添加购物车后执行
  query(){
    this.setState({status: 'uninit'});
    fetch(`${ctService}/cart/mini`,{
      headers:Hmall.getHeader()
    })
    .then(Hmall.convertResponse('json',this))
    .then(json => {
      let { success, resp } = json;
      if(success){
        let { cartList , count , total} = resp[0]
        this.setState({
          status: 'init',
          count :count,
          total : total,
          cartList
        });
      }else{
        this.setState({status: 'error'});
      }
    })
    .catch (Hmall.catchHttpError(() => {
      this.setState({status: 'error'});
    }));
  }
  
  handleMouseEnter() {
    if(leaveId){
      clearTimeout(leaveId);
      leaveId = null;
    }
    enterId = setTimeout(() => {
      this.show();
    },500,this);
  }
  
  handleMouseLeave() {
    if(enterId){
      clearTimeout(enterId);
      enterId = null;
    }
    leaveId = setTimeout(() => {
      this.hide();
    },4000,this);
  }
  
  handleClick() {
    if(enterId){
      clearTimeout(enterId);
      enterId = null;
    }
    this.show();
  }
  
  handleClickOutside() {
    this.hide();
  }
  
  show(reload) {
    let { show, status } = this.state;
    if(!show){
      this.setState({show: true});
      if(reload || this.state.status != 'init'){
        this.query();
      }
    }
  }
  
  hide() {
    if(this.state.show){
      this.setState({show: false});
    }
  }
  
  deleteCart(count) {
    this.setState({count: this.state.count - count});
    this.query();
  }
  
  gotoCart() {
    this.setState({show: false});
    browserHistory.push('/cart.html')
  }
  
  renderPopup() {
    let { status, cartList, count, total } = this.state,
      showGoods;
    if(status == 'init'){
      if(cartList && cartList.length){
        showGoods = cartList.map((goods, i)=> {
          let { productDetails, productCode, quantity } = goods;
          if(i < 3 && productDetails){
            let { summaryInfo, productDetailInfo } = productDetails,
              { name , approval } = summaryInfo,
              { styleText, size, price ,stylePic} = productDetailInfo;
            return(
              <div className="h-mini-hotcart-container clearfix" key={i}>
                <div className="h-mini-hotcart-img">
                  <img src={Hmall.cdnPath(stylePic)}/>
                </div>
                <div className="h-mini-hotcart-des">
                  <p title={name}>
                    <Link to={`/product-detail.html?productCode=${productCode}`}>{name}</Link>
                  </p>
                  <p>颜色：{styleText}</p>
                  <p>尺寸：{size}</p>
                  <p>数量：{quantity}</p>
                  {approval=="list"?<p>￥{price}</p>:<Icon name="cart_out"/>}
                </div>
              </div>
            )
          }
        });
      }else{
        showGoods = <div className="h-mini-hotcart-nogoods">您的购物车空空如也</div>;
      }
    }else if(status == 'error'){
       showGoods = <div className="h-mini-hotcart-nogoods">加载失败，请<a href="javascript:void(0)" onClick={() => this.query()}>刷新</a>重试</div> 
    }else{
       showGoods = <div className="h-mini-hotcart-nogoods loading"></div> 
    }  
    return <div className="h-mini-hotcart-box">
      <div className="h-mini-hotcart-title">共有商品（{count}件）</div>
      {showGoods}
      <div className="h-mini-hotcart-total">
        <span className="h-mini-hotcart-total-left">商品总额</span>
        <span className="h-mini-hotcart-total-right">￥{total}</span>
      </div>
      <div className="h-mini-hotcart-view">
        <Button className="red" onClick={() => this.gotoCart()} text='查看购物车' height={44}>
          <Icon name="cart"></Icon>
        </Button>
      </div>
    </div>
  }
  
  render() {
    let { count, show } = this.state,
      token = Hmall.getCookie('access_token');
    return <li className={`h-mini-hotcart${show ? ' show' : ''}`} onMouseEnter={token && (() => this.handleMouseEnter())} onMouseLeave={token && (() => this.handleMouseLeave())}>
      <Link to="/cart.html"><Icon className="head-icon" name="cart"></Icon>购物车(<span className="notice-word">{count}</span>)</Link><Icon onClick={token && (() => this.handleClick())} className="head-icon" name="cart-arrow"></Icon>
      {!!token && this.renderPopup()}
    </li>
  }
  
}

export default onClickOutside(MiniCart)