import React, { Component, PropTypes } from 'react';
import TextBox from './TextBox';

export default class NumberBox extends TextBox{
  static propTypes = {
    placeholder: PropTypes.string,
    width: PropTypes.number,
    height: PropTypes.number,
    maxLength: PropTypes.number,
    name: PropTypes.string,
    readOnly: PropTypes.bool,
    className: PropTypes.string,
    allowNegative: PropTypes.bool,
    allowDecimal: PropTypes.bool
  }
  
  static defaultProps = {
    allowNegative: true,
    allowDecimal: true
  }
  
  constructor(props) {
    super(props);
    
    this.state = {
      value: props.value
    }
  }
  
  handleChange(e) {
    let { readOnly, onChange, allowNegative, allowDecimal } = this.props; 
    if(readOnly) return;
    let { value } = e.target,
      v = Number(value);
    if(!isNaN(v) || value == '-'){
      if(!allowNegative){
        value = value.replace('-','');
      }
      if(!allowDecimal && value){
        value = parseInt(value);
      }
      e.target.value = value;
      this.state.value = value;
      onChange && onChange(e);
    }
  }
  
  handleBlur(e) {
    let { onBlur } = this.props;
    if(isNaN(e.target.value)){
      this.setState({value: ''});
    }
    onBlur && onBlur(e);
  }
}