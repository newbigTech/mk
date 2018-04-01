import React, { Component, PropTypes } from 'react';
import Radio from './Radio';

export default class RadioList extends Component{
  static propTypes = {
    width: PropTypes.number,
    height: PropTypes.number,
    name: PropTypes.string,
    readOnly: PropTypes.bool,
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
    let { readOnly, onChange } = this.props; 
    if(readOnly) return;
    let { checked, value } = e.target;
    if(checked){
      this.setState({value});
    }
    onChange && onChange(e);
  }
  
  renderOptions() {
    let { options = {}, name, width, height } = this.props,
      { value } = this.state;
    return Object.keys(options).map((key,index) => {
      return <label key={index} style={{ width, height }}><Radio name={name} value={key} choose={value} onChange={e => this.handleChange(e)}></Radio>{options[key]}</label>
    });
  }
  
  render() {
    let { style, className='', readOnly } = this.props,
      readonlyClass = readOnly ?' readonly':'';
    
    return <div className={`h-radio-list ${className} ${readonlyClass}`} style={style}>
      {this.renderOptions()}
    </div>
  }
}