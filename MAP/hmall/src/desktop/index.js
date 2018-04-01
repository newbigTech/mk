import './styles/default/style.less';
import 'react-datetime/css/react-datetime.css';
import React from 'react';
import { render } from 'react-dom'; 
import { Router, Route, browserHistory } from 'react-router';
import App from './containers/App';
import Template from './components/Template';

function lazyLoadContainer(module) {  
  return (location, cb) => {
    import(`./containers/${module}`).then(module => {
      cb(null, module.default);
    })
  }
}

let _routes = [],
    { pages } = CMSConfig,
    others = [],
    i = 0;
  pages.forEach( page => {
    let { components, homepage } = page;
    if(page.tplt) {
      page.component = Template;
    }
    others.push(<Route key={++i} {...page}></Route>);
    if(homepage) {
      page.path = '/';
      others.unshift(<Route key={0} {...page}></Route>);
    }
  });
  others = others.concat([
    <Route key={++i} path="/search.html" title="搜索列表" component={Template} tplt="SearchTemplate"></Route>,
    <Route key={++i} path="/product-detail.html" title="产品详情" component={Template} tplt="ProductDetailTemplate"></Route>,
    <Route key={++i} path="/cart.html" accessCheck={true} title="购物车" component={Template} components={{'section1': {container: 'Cart'}}} tplt="HeaderFooterTemplate"></Route>,
    <Route key={++i} path="/password-find.html" title="找回密码" component={Template} tplt="PasswordFindTemplate"></Route>,
    <Route key={++i} path="/change-password.html" title="修改密码" component={Template} components={{'section1': {container: 'ChangePassword'}}} tplt="HeaderFooterTemplate"></Route>,
    <Route key={++i} path="/account" accessCheck={true} title="个人中心" component={Template} components={{'section1': {container: 'account/Account'}}} tplt="HeaderFooterTemplate" >
      <Route path="base-info.html" title="个人资料" getComponent={lazyLoadContainer('account/BaseInfo')}></Route>
      <Route path="security.html" title="安全中心" getComponent={lazyLoadContainer('account/Security')}></Route>
      <Route path="security" title="安全中心">
        <Route path="change-password.html" title="修改密码" getComponent={lazyLoadContainer('account/security/ChangePassword')}></Route>
        <Route path="change-phone.html" title="修改手机号" getComponent={lazyLoadContainer('account/security/ChangePhone')}></Route>
        <Route path="change-email.html" title="修改邮箱" getComponent={lazyLoadContainer('account/security/ChangeEmail')}></Route>
      </Route>
      <Route path="address.html" title="地址簿" getComponent={lazyLoadContainer('account/Address')}></Route>
      <Route path="binding.html" title="账号绑定" getComponent={lazyLoadContainer('account/Binding')}></Route>
      <Route path="order-center" title="订单中心">
        <Route path="order-list.html" title="我的订单" getComponent={lazyLoadContainer('account/OrderList')}></Route>
        <Route path="order-list" title="我的订单">
          <Route path="apply-refund.html" showTitle={false} title="申请退款" getComponent={lazyLoadContainer('account/ApplyRefund')} ></Route>
          <Route path="apply-aftermarket.html" showTitle={false} title="申请售后" getComponent={lazyLoadContainer('account/ApplyAfterMarket')} ></Route>
          <Route path="evaluate.html" title="评价" getComponent={lazyLoadContainer('account/Evaluate')}></Route>
          <Route path="view-refund.html" title="查看退款" showTitle={false} getComponent={lazyLoadContainer('account/ViewRefund')}></Route>
          <Route path="view-aftermarket-only-refund.html" showTitle={false} title="查看售后-仅退款" getComponent={lazyLoadContainer('account/ViewOnlyRefund')}></Route>
          <Route path="view-aftermarket-refund-return.html" showTitle={false} title="查看售后-退款退货" getComponent={lazyLoadContainer('account/ViewRefundReturn')}></Route>
          <Route path="view-aftermarket-replacement.html" showTitle={false} title="查看售后-换货" getComponent={lazyLoadContainer('account/ViewReplacement')}></Route>
        </Route>
        <Route path="order-detail.html" title="订单详情" getComponent={lazyLoadContainer('account/OrderDetail')}></Route>
        <Route path="take-proof.html" title="订单详情-自提" getComponent={lazyLoadContainer('account/TakeProof')}></Route>
        <Route path="refund-return-list.html" title="退款退货" getComponent={lazyLoadContainer('account/RefundAndReturn')}></Route>
        <Route path="replace-list.html" title="我的换货" getComponent={lazyLoadContainer('account/Replacement')}></Route>
        <Route path="delivery-cert.html" title="提货凭证" getComponent={lazyLoadContainer('account/DeliveryCert')}></Route>
        <Route path="order-recycle.html" title="订单回收" getComponent={lazyLoadContainer('account/OrderRecycle')}></Route>
      </Route>
      <Route path="member-center" title="会员中心">
        <Route path="coupon.html" title="我的优惠券" getComponent={lazyLoadContainer('account/Coupon')}></Route>
      </Route>
      <Route path="favorites.html" title="收藏夹" getComponent={lazyLoadContainer('account/Favorites')}></Route>
    </Route>,
    <Route key={++i} path="/payment" accessCheck={true} component={Template} components={{'section1': {container: 'payment/Payment'}}} tplt="HeaderFooterTemplate">
      <Route path="settlement.html" title="结算页" getComponent={lazyLoadContainer('payment/Settlement')}></Route>
      <Route path="alipay_return.html" title="订单支付页" getComponent={lazyLoadContainer('payment/AlipayReturn')}></Route>
      <Route path="unionpay_return.html" title="订单支付页" getComponent={lazyLoadContainer('payment/UnionpayReturn')}></Route>
      <Route path="pickup_guide.html" title="自提需知页" getComponent={lazyLoadContainer('payment/PickUpGuide')}></Route>
      <Route path="order-payment.html" title="订单支付页" getComponent={lazyLoadContainer('payment/OrderPayment')}></Route>
      <Route path="pay-success.html" title="支付成功页" getComponent={lazyLoadContainer('payment/PaySuccess')}></Route>
      <Route path="lottery.html" title="抽奖页" getComponent={lazyLoadContainer('payment/Lottery')}></Route>
      <Route path="pay-failed.html" title="支付失败页" getComponent={lazyLoadContainer('payment/PayFailed')}></Route>
    </Route>,
    <Route key={++i} path="/message.html" accessCheck={true} title="站内信" component={Template} components={{'section1': {container: 'Message'}}} tplt="HeaderFooterTemplate"></Route>,
    <Route key={++i} path="*" title="404" component={Template} components={{'section1': {container: 'PageNotFound'}}} tplt="HeaderFooterTemplate"></Route>,   
  ]);

_routes.push(<Route key={++i} path="/thirdParty.html" getComponent={lazyLoadContainer('ThirdParty')}></Route>);
_routes.push(<Route key={++i} path="/register.html" title="会员注册" component={Template} components={{'main': {container: 'Register'}}} tplt="LoginRegisterTemplate"></Route>);
//_routes.push(<Route key={++i} path="/staff-register.html" title="员工注册" component={Template} components={{'main': {container: 'StaffRegister'}}} tplt="LoginRegisterTemplate"></Route>);
_routes.push(<Route key={++i} path="/login.html" title="会员登录" component={Template} components={{'main': {container: 'Login'}}} tplt="LoginRegisterTemplate"></Route>);
_routes.push(<Route key={++i} component={App}>{others}</Route>);

render(
  <Router routes={_routes} history={browserHistory} onUpdate={() => Hmall.clearModals()}></Router>,
  document.getElementById('hmall-container')
);