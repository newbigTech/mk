import React from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import ProductDetail from '../containers/ProductDetail';

const ProductDetailTemplate = (props) => {
  return <div>
    <Header></Header>
    <ProductDetail {...props}></ProductDetail>
    <Footer></Footer>
  </div>;
};

export default ProductDetailTemplate;