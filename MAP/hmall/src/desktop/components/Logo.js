import React, { PropTypes } from 'react';
import { Link } from 'react-router';

const Logo = (props) => {
  const { href, width, height } = props;
  return <Link to={href} style={{width,height}} className="logo"></Link>;
};

Logo.propTypes = {
  href: PropTypes.string,
  width: PropTypes.number,
  height: PropTypes.number
};

export default Logo;