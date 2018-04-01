import React from 'react';
import Header from '../components/Header';
import Footer from '../components/Footer';

const NormalTemplate = (props) => {
  const renderSection = () => {
    return Object.keys(props).map((name, index) => {
      if(name.match(/^section\d+/)){
        return React.cloneElement(props[name],{key: index})
      }
    });
  }
  
  return <div>
    {
      renderSection()
    }
  </div>;
};

export default NormalTemplate;