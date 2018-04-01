import React, { PropTypes } from 'react';

const Icon = (props) => {
  const { name, onClick, title, className, id, style={} } = props;
  if(onClick) {
    style.cursor = 'pointer';
  }
  return <i id={id} title={title} className={`icon ${className} icon-${name}`} style={style} onClick={onClick}></i>;
}

Icon.propTypes = {
  id: PropTypes.string,
  title: PropTypes.string,
  name: PropTypes.string,
  className: PropTypes.string,
  style: PropTypes.object,
  onClick: PropTypes.func
}

Icon.defaultProps = {
  className: ''
}
export default Icon;