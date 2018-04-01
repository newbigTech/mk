import React, { Component,PropTypes } from 'react'
import Button from './Button'

export default class AlertWindow extends Component{
  
  constructor(props){
    super(props);
    this.state={
        appear:this.props.appear,
        text:this.props.text
    }
  }
  
  static propTypes={
    text:PropTypes.string
  }
  
  componentWillReceiveProps(nextProps){
    this.setState(nextProps);
  }
  
  handleClick() {
    let { appear } = this.state,
      { onClose } = this.props;
    if(appear && onClose) {
      onClose();
    }
    this.setState({appear:!appear })
  }
  
  render(){
    return(
       <div id="confirm-wins" style={{display:this.state.appear?'block':'none'}}>
         <div className="wins">
           <div className="text-center">{this.props.text}</div>
           <div className="btn-position"><Button onClick={() => this.handleClick()} className="black" text="确定" width={100} height={30}></Button></div>
         </div>
       </div>
    );
  }
}