import React, { Component } from 'react';

export default class Top extends Component{
  
  handleScroll(e) {
    let body = document.body;
    if(body.scrollTop > 0){
      body.classList.add('top-show');
    }else{
      body.classList.remove('top-show');
    }
  }

  componentDidMount(){
    window.addEventListener("scroll", this.handleScroll, false);
  }
  
  componentWillUnmount(){
    window.removeEventListener('scroll', this.handleScroll);
  }
  
  render() {
    return (
      <div className="h-top" onClick={() => {document.body.scrollTop = 0}}></div>
    );
  }
}