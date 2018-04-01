import React, { PropTypes } from 'react';
import Button from './Button';
import Icon from './Icon';

const Modal = (props) => {
  const { width, title, text, zIndex, onClose, buttons } = props,
    handleClose = () => {
      onClose && onClose();
    },
    handleClick = button => {
      const { onClick } = button;
      if(!onClick || onClick() !== false) {
        onClose && onClose();
      }
    },
    renderButtons = () => {
      return buttons.map(button => {
        return <Button className={button.className} text={button.text} onClick={() => handleClick(button)}></Button>
      });
    };
  return <div className="h-modal" style={{zIndex}} >
    <div className="h-modal-container" style={{width}}>
      <div className="h-modal-header">
        <Icon name="alert-close" onClick={ handleClose }></Icon>
      </div>
      <div className="h-modal-body">
        <h3>{title}</h3>
        <p>{text}</p>
      </div>
      <div className="h-modal-footer">
        {
          renderButtons()
        }
      </div>
    </div>
  </div>;
};

Modal.propTypes = {
  text: PropTypes.string,
  title: PropTypes.string,
  width: PropTypes.number,
  zIndex: PropTypes.number,
  onClose: PropTypes.func,
  buttons: PropTypes.arrayOf(PropTypes.shape({
    text: PropTypes.string,
    title: PropTypes.string,
    className: PropTypes.string,
    onClick: PropTypes.func
  }))
};

Modal.defaultProps = {
  title: '',
  text: ''
};

export default Modal;
