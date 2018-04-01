import React, { Component, PropTypes } from 'react'


export default class Choose extends Component{
  static propTypes = {
    text: PropTypes.string,
  }

  render() {
      return (
        <div className="choose">
          <span >{this.props.title} : </span>
          <span className="span_body">{this.props.text}</span>
          <span className="choose_span" onClick={()=>{this.props.closeItem()}}>×</span>
        </div>
      )
  }
}