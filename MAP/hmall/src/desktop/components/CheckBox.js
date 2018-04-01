import React, { Component, PropTypes } from 'react';

export default class CheckBox extends Component {
  static defaultProps = {
    checkedValue: 'Y',
    unCheckedValue: 'N',
    className: ''
  }
  
  static propTypes = {
    width: PropTypes.number,
    height: PropTypes.number,
    name: PropTypes.string,
    className: PropTypes.string,
    onChange: PropTypes.func
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
  
  handleChange(e){
    let { onChange, checkedValue, unCheckedValue } = this.props,
        { value } = this.state;
    if( value == checkedValue ){
      value = unCheckedValue
    }else{
      value = checkedValue
    }
    this.setState({value});
    e.target.value = value;
    onChange && onChange(e) 
  }
  
  render() {
    let { width, height, className, name, checkedValue } = this.props,
        { value } = this.state;
    if(value==checkedValue){
      className += ' checked'
    }
    return <span className={`h-checkbox ${className}`} style={{width, height}}><input name={name} type="checkbox" value= {value} onChange={(e) => this.handleChange(e)}/></span>
  }
}