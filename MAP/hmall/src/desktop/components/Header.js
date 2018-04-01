import React from 'react';
import Custom from './Custom';

const Header = () => {
  return <header>
    <Custom html={CMSConfig.header}></Custom>
  </header>;
};

export default Header;
