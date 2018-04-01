import React, { Component, PropTypes } from 'react';
import Datetime from 'react-datetime';
import Moment from 'moment';
import DatePicker from './DatePicker';

export default class DatetimePicker extends DatePicker{
  
  static propTypes = DatePicker.propTypes;

  static defaultProps = DatePicker.defaultProps;
  
  get hName() {
    return 'h-datetime-picker';
  }

  get dateTimeFormat() {
    return HmallConfig.datetime_format;
  }
  
  get timeFormat() {
    return HmallConfig.time_format;
  }

  render() {
    let { name, placeholder, style={}, className='', readOnly, disabled, width, height,timeConstraints ,defaultValue } = this.props,
        { value } = this.state,
        readonlyClass = disabled ? ' readonly':'',
        { format, viewMode, hName, timeFormat,dateTimeFormat } = this;
    style.width = width;
    style.height = height;
    return (
        <div className={`${this.hName} ${className}`} style={style}>
          <Datetime onChange={v => this.handleChange(v)} value={value && Moment(Number(value)).format(dateTimeFormat)} locale="zh-cn" dateFormat={format} timeFormat={timeFormat} closeOnSelect={true} timeConstraints={timeConstraints} defaultValue={defaultValue} inputProps={{ placeholder, readOnly, disabled, className: readonlyClass }}></Datetime>
          <input type="hidden" name={name} value={value}/>
        </div>
    );
  }
}