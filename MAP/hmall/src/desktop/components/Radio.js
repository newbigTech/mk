import React, { PropTypes } from 'react';

const Radio = (props) => {
  const { name, onChange, value, choose, id, disabled, className, style } = props;

  return <i className={`h-radio ${className}${choose == value ? ' checked' : ''}`} style={style} id={id}>
    <input disabled={disabled} type="radio" name={name} value={value} onChange={onChange}></input>
  </i>;
};

Radio.propTypes = {
  id: PropTypes.string,
  name: PropTypes.string,
  disabled: PropTypes.bool,
  className: PropTypes.string,
  //value: PropTypes.string,
  //choose: PropTypes.string,
  onChange: PropTypes.func
}

Radio.defaultProps = {
  className: ''
}

export default Radio;