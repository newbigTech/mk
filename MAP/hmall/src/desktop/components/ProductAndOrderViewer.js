import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import { Panel } from './Layout';
import Moment from 'moment';

const ProductAndOrderViewer = (props) => {
  const { mainPic, name, size, price, quantity, orderId, paymentTime, style, productCode, shopName } = props;
  return <Panel>
    <div id="product-order-viewer">
      <div className="detail">
        <div className="head">
          {shopName}
        </div>
        <div className="p-body">
          <div className="row clearfix">
            <Link to={`/product-detail.html?productCode=${productCode}`}>
              <img width="100" height="100" src={Hmall.cdnPath(mainPic)}></img>
            </Link>
            <p className="right-distance"><Link
                to={`/product-detail.html?productCode=${productCode}`}>{name}</Link></p>
            <p><label className="size-color">颜色：</label><span className="size-color">{style}</span></p>
            <p><label className="size-color">尺码：</label><span className="size-color">{size}</span></p>
          </div>
          <div className="p-border">
            <p className="label-margin"><label>单价：</label><span>￥{Number(price).toFixed(2)}</span></p>
            <p className="label-margin"><label>数量：</label><span>{quantity}</span></p>
            <p className="label-margin"><label>小计：</label><span
                className="notice-word">￥{(price * quantity).toFixed(2)}</span></p>
            <hr />
            <p className="label-margin"><label>订单编号：</label><span>{orderId}</span></p>
            <p className="label-margin">
              <label>成交时间：</label><span>{Moment(Number(paymentTime)).format(HmallConfig.date_format)}</span></p>
          </div>
        </div>
      </div>
    </div>
  </Panel>;
};

ProductAndOrderViewer.propTypes = {
  mainPic: PropTypes.string,
  name: PropTypes.string,
  size: PropTypes.string,
  price: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  quantity: PropTypes.number,
  orderId: PropTypes.string,
  paymentTime: PropTypes.string,
  style: PropTypes.string,
  productCode: PropTypes.string,
  shopName: PropTypes.string
};

export default ProductAndOrderViewer;