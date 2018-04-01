import React from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import ProductList from '../containers/ProductList';

const ProductListTemplate2 = (props) => {
  const {section01, route } = props,
    code = route.path.match(/\/c\/([^.]*).html/)[1];
  return <div>
    <Header></Header>
    {
      section01 
    }
    <ProductList {...props} displayMode="C5" searchFlag={false} params={{categoryCode: code}}></ProductList>
    <Footer></Footer>
  </div>;
};

export default ProductListTemplate2;