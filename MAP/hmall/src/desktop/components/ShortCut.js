import React, { Component } from 'react';
import { Link, IndexLink, browserHistory } from 'react-router';
import SearchBox from './SearchBox';
import MiniCart from './MiniCart';
import Icon from './Icon';
import Logo from './Logo';


export default class ShortCut extends Component{
  
  constructor(props) {
    super(props);
    this.state = {
      user: Hmall.getCookie('username')
    };
    Hmall.Storage.set('shortcut',this);
  }
  
  handleScroll(e) {
    let body = document.body;
    if(body.scrollTop > 88){
      body.classList.add('head-fixed');
    }else{
      body.classList.remove('head-fixed');
    }
  }

  componentDidMount(){
    window.addEventListener("scroll", this.handleScroll, false);
  }
  
  componentWillUnmount(){
    window.removeEventListener('scroll', this.handleScroll);
  }
  
  getRedirectPath(path) {
    let { pathname, search } = this.props.location,
      url = encodeURIComponent(pathname + search);
    if(pathname == '/'){
      return path;
    }
    return `${path}?redirect_url=${url}`
  }
  
  getUserInfo() {
    let { user } = this.state;
    return Hmall.getCookie('access_token') ? 
      [<li key="1"><b>Hi,<Link to="/account/base-info.html">{user}</Link></b></li>,
       <li key="2" className="spliter"></li>,
       <li key="3"><Link onClick={() => Hmall.logout()} to={this.getRedirectPath('/login.html')}>退出</Link></li>]
      :
      [<li key="1"><Link to={this.getRedirectPath('/register.html')}>注册</Link></li>,
       <li key="2" className="spliter"></li>,
       <li key="3"><Link to={this.getRedirectPath('/login.html')}>登录</Link></li>];
  }
  
  handleSearch(value) {
    if(value){
      browserHistory.push('/search.html?description='+value);
    }
  }
  render() {
    let { description } = this.props.location.query;
    return (
      <div className="shortcut">
        <Logo href="/"></Logo>
        <ul>
          {
            this.getUserInfo()
          }
          <li className="spliter"></li>
          <li><Link to="/account/favorites.html"><Icon className="head-icon" name="favorite"></Icon>收藏夹</Link></li>
          <li className="spliter"></li>
          <MiniCart></MiniCart>
          <li className="spliter"></li>
          <li><Link to="/message.html"><Icon className="head-icon" name="message"></Icon>站内信</Link></li>
          <li className="spliter"></li>
          <li><Icon className="head-icon" name="service"></Icon>
            <span onClick={()=>{Hmall.showXiaoNeng()}}>联系客服</span>
          </li>
          <li className="spliter"></li>
          <li><SearchBox value={description} onSearch={this.handleSearch} placeholder="站内搜索" width={194} autoCompleteService={`${scService}/search/selectAutomatically`}></SearchBox></li>
        </ul>
      </div>
    );
  }
}