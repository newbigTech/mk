import React, { PropTypes } from 'react';

const Button = (props) => {
  
  const { style, className, width, height, disabled, text, children, type, onClick } = props;
  style.width = width;
  style.height = height;
  
  const handleClick = (e) => {
    if(type != 'submit' || disabled){
      e.preventDefault();
    }
    if(disabled)return;
    onClick && onClick(e);
  },
  firstChild = () => {
    return React.Children.toArray(children).filter((child, index) => {
      if(index == 0 && (children.length > 1 || className.indexOf('icon-right') == -1)) {
        return true;
      }
    });
  },
  secondChild = () => {
    return React.Children.toArray(children).filter((child, index) => {
      if(children.length > 1 ? index == 1 : className.indexOf('icon-right') != -1) {
        return true;
      }
    });
  };
  
  return (
    <button disabled={disabled} className={`h-btn ${className}`} style={style} onClick={handleClick}>
      {firstChild()}
      {text}
      {secondChild()}
    </button>
  );
};

Button.propTypes = {
  type: PropTypes.string,
  text: PropTypes.string,
  width: PropTypes.number,
  height: PropTypes.number,
  className: PropTypes.string,
  style: PropTypes.object,
  disabled: PropTypes.bool,
  onClick: PropTypes.func
};

Button.defaultProps = {
  className: 'default',
  style: {}
};

export default Button;