import React, { PropTypes } from 'react';
import Product from './Product';

const ProductGroup = (props) => {

  const { displayMode, items } = props,
    renderProducts = items => {
      return items && items.map((item, i) => {
        return <li key={i}><Product info={item}></Product></li>;
      });
    }

  return <div className={`h-product-group ${displayMode}`}>
    <ul>
    {
      renderProducts(items)
    }
    </ul>
  </div>;
};

ProductGroup.propTypes = {
  displayMode: PropTypes.string,
  items: PropTypes.arrayOf(Product.propTypes.info)
};

ProductGroup.defaultProps = {
  displayMode: 'C5'
};

export default ProductGroup;