import React, { Component, PropTypes } from 'react'
import Icon from '../../components/Icon';

export default class Counter extends Component{
  constructor(props) {
    super(props);
  }


  render() {
    let { value,add,minus,changeVal,onBlur } = this.props;
    return (
      <div className="counter">
        <input type="text" value={value} onChange={(event)=>{changeVal(event)}}  onBlur={(event)=>{onBlur(event)}}/>
        <ul>
          <li><Icon onClick={()=>{add()}} name="number-add"></Icon></li>
          <li><Icon onClick={()=>{minus()}} name="number-minus"></Icon></li>
        </ul>
      </div>
    )
  }
}