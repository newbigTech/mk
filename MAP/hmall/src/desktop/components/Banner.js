import React, { PropTypes } from 'react';
import { Link } from 'react-router';
import Picture from './Picture';

const Banner = (props) => {
  const { link, urlPath } = props;
  return <div className="h-component h-banner"><Link to={link}><Picture src={Hmall.cdnPath(urlPath)}></Picture></Link></div>;
};

Banner.propTypes = {
  link: PropTypes.string,
  urlPath: PropTypes.string
};

export default Banner;