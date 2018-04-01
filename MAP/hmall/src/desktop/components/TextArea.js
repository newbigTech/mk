import React, { Component, PropTypes } from 'react'

export default class TextArea extends Component{
  static propTypes = {
    placeholder: PropTypes.string,
    width: PropTypes.number,
    height: PropTypes.number,
    maxLength: PropTypes.number,
    name: PropTypes.string,
    readOnly: PropTypes.bool,
    className: PropTypes.string,
    onBlur: PropTypes.func,
    onClick: PropTypes.func
  }
  
  static defaultProps = {
    className: 'default'
  }
  
  constructor(props) {
    super(props);
    
    this.state = {
      value: props.value
    }
  }
  
  componentWillReceiveProps(nextProps) {
    let { value } = nextProps;
    if(value != this.props.value)
      this.setState({value});
  }
  
  handleChange(e) {
    let { readOnly, onChange } = this.props; 
    if(readOnly) return;
    this.setState({value: e.target.value});
    onChange && onChange(e);
  }
  
  render() {
    let { name, placeholder, style={}, className, readOnly, width, height, onBlur, onClick, maxLength } = this.props,
      readonlyClass = readOnly ? ' readonly':'';
    style.width = width;
    style.height = height;
    return (
      <textarea maxLength={maxLength} placeholder={placeholder} name={name} readOnly={readOnly} className={`h-text-area ${className}${readonlyClass}`} onChange={e => this.handleChange(e)} onBlur={onBlur} value={this.state.value} onClick={onClick} style={style} autoComplete="off"/>
    );
  }
}