import React, { Component, PropTypes } from 'react';

export default class Search extends Component{
  static propTypes = {
        placeholder: PropTypes.string,
        width: PropTypes.number
  }

  render() {
    return (
      <div className="search_div">
        <i className="icon_search" onClick={()=>{this.props.search()}}></i>
        <input className="search" type="text"
               value={this.props.text}
               placeholder="在当前结果中搜索" onChange ={(event)=>this.props.onChange(event)}
               onKeyDown ={(event)=>this.props.onKeyDown(event)}
        />
      </div>
    )
  }
}