import React from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import ProductList from '../containers/ProductList';

const SearchTemplate= (props) => {
  const {section01, route } = props;
  return <div>
    <Header></Header>
    {
      section01 
    }
    <ProductList {...props} displayMode="C4" searchFlag={true}></ProductList>
    <Footer></Footer>
  </div>;
};

export default SearchTemplate;