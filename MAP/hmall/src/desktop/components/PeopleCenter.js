import React from 'react';
import { Link } from 'react-router';

const menus = [{
  name: '个人资料',
  link: '/account/base-info.html'
},{
  name: '安全中心',
  link: '/account/security.html'
},{
  name: '地址簿',
  link: '/account/address.html'
},{
  name: '账号绑定',
  link: '/account/binding.html'
},{
  name: '订单中心',
  submenus: [{
      name: '我的订单',
      link: '/account/order-center/order-list.html'
    },{
      name: '退款退货',
      link: '/account/order-center/refund-return-list.html'
    },{
      name: '我的换货',
      link: '/account/order-center/replace-list.html'
    },{
      name: '提货凭证',
      link: '/account/order-center/delivery-cert.html'
    },{
      name: '订单回收',
      link: '/account/order-center/order-recycle.html'
  }]
},{
  name: '会员中心',
  submenus: [{
      name: '我的优惠券',
      link: '/account/member-center/coupon.html'
  }]
},{
  name: '收藏夹',
  link: '/account/favorites.html'
}];

const PeopleCenter = (props) => {
  
  const toggleExpand = e => {
    e.target.classList.toggle('collapse');
  };
  
  const renderMenus = menus => {
    return menus.map((menu, index) => {
      const { name, link, submenus} = menu;
      if(submenus) {
        return <li key={index}>
          <a className="submenu" onClick={toggleExpand}>{name}</a>
          <ul>
          {
            renderMenus(submenus)
          }
          </ul>
        </li>
      }else {
        return <li key={index}><Link to={link} activeClassName="active">{name}</Link></li>
      }
    });
  }
  
  return <div id="account-menu">
    <ul>
      <li>我的账户</li>
      {
        renderMenus(menus)
      }
    </ul>
  </div>;
}

export default PeopleCenter;