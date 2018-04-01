import React, { Component, PropTypes } from 'react';

export default class ComboBox extends Component{
  static propTypes = {
    width: PropTypes.number,
    height: PropTypes.number,
    name: PropTypes.string,
    disabled: PropTypes.bool,
    className: PropTypes.string
  }
  
  constructor(props) {
    super(props);
    this.state = {
      value: props.value
    };
  }
  
  componentWillReceiveProps(nextProps) {
    let { value } = nextProps;
    if(value != this.props.value)
      this.setState({
        value
      });
  }
  
  handleChange(e) {
    let { disabled, onChange } = this.props; 
    if(disabled) return;
    this.setState({value: e.target.value});
    onChange && onChange(e);
  }
  
  renderOptions() {
    let { options = {} } = this.props,
      items = [];
    return Object.keys(options).map((key,index) => {
      return <option key={index} value={key}>{options[key]}</option>;
    });
  }
  
  render() {
    let { name, placeholder, style={}, className='', disabled, width, height, onBlur, onClick, emptyOption } = this.props,
      { value='' } = this.state;
    style.width = width;
    style.height = height;
    return (
      <select name={name} disabled={disabled} className={`h-combo-box ${className}`} style={style} onChange={e => this.handleChange(e)} onBlur={onBlur} value={value} onClick={onClick}>
      { emptyOption && <option value="">{emptyOption}</option> }  
      {this.renderOptions()}
      </select>
    );
  }
}