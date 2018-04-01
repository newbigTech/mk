import React, { Component, PropTypes } from 'react';
import Datetime from 'react-datetime';
import Moment from 'moment';

export default class DatePicker extends Component{
  
  static propTypes = {
    placeholder: PropTypes.string,
    width: PropTypes.number,
    height: PropTypes.number,
    name: PropTypes.string,
    readOnly: PropTypes.bool,
    disabled: PropTypes.bool,
    className: PropTypes.string
  }
  
  static defaultProps = {
    className: ''
  }
  
  constructor(props) {
    super(props);
    this.state = {
      value: props.value || ''
    };
  }
  
  componentWillReceiveProps(nextProps) {
    let { value } = nextProps;
    if(value != this.props.value && value != this.state.value)
      this.setState({value});
  }
  
  handleChange(v) {
    let { readOnly, onChange } = this.props; 
    if(readOnly) return;
    if(v instanceof Moment){
      this.setState({value: v.toDate().getTime()});
    }else if(!v){
      this.setState({value: v});
    }
    onChange && onChange(v);
  }
  
  get hName() {
    return 'h-date-picker';
  }
  
  get format() {
    return HmallConfig.date_format;
  }
  
  get timeFormat() {
    return false;
  }
  
  render() {
    let { name, placeholder, style={}, className, readOnly, disabled, width, height } = this.props,
      { value } = this.state,
      readonlyClass = disabled ? ' readonly':'',
      { format, viewMode, hName, timeFormat } = this;
    style.width = width;
    style.height = height;
    return (
      <div className={`${this.hName} ${className}`} style={style}>
        <Datetime onChange={v => this.handleChange(v)} value={value && Moment(Number(value)).format(format)} locale="zh-cn" dateFormat={format} timeFormat={timeFormat} closeOnSelect={true} inputProps={{ placeholder, readOnly, disabled, className: readonlyClass }}></Datetime>
        <input type="hidden" name={name} value={value}/>
      </div>
    );
  }
}