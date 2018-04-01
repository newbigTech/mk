import React, { PropTypes } from 'react';

const getTitle = (title, subtitle) => {
  return title && <div className="form-head"><h1>{title}</h1><h2>{subtitle}</h2></div>;
},
Form = (props) => {
  const { id, width, onSubmit, children, className, title, subtitle } = props;
  return <form id={id} className={`h-form ${className}`} style={{width}} onSubmit={onSubmit}>
    {getTitle(title, subtitle)}
    {children}
  </form>;
},
SubForm = (props) => {
  const { children, title, subtitle } = props;
  return <div className="h-sub-form">
    {getTitle(title, subtitle)}
    {children}
  </div>;
},
FormGroup = (props) => {
  const { align, className, children, style } = props;
  return <div className={`h-form-group ${className} clearfix ${align}`} style={style}>
    {children}
  </div>;
};

Form.propTypes = {
  id: PropTypes.string,
  title: PropTypes.string,
  subtitle: PropTypes.string,
  width: PropTypes.number,
  onSubmit: PropTypes.func
}

SubForm.propTypes = {
  title: PropTypes.string,
  subtitle: PropTypes.string
}

FormGroup.propTypes = {
  align: PropTypes.string,
  className: PropTypes.string,
  style: PropTypes.object
}

Form.defaultProps = {
  className: 'label-top'
}

FormGroup.defaultProps = {
  className: '',
  align: ''
}

export { Form as default, SubForm, FormGroup };