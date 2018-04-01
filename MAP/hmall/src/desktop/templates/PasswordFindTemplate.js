import React from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';
import PasswordFind from '../containers/PasswordFind';

const PasswordFindTemplate = (props) => {
  return <div>
    <Header></Header>
    <PasswordFind {...props}></PasswordFind>
    <Footer></Footer>
  </div>;
};

export default PasswordFindTemplate;