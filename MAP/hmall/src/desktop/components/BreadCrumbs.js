import React, { Component } from 'react'
import { Link, IndexLink } from 'react-router'

export default class BreadCrumbs extends Component{
  
  items() {
    return this.props.routers.map((router,index) => {
      return <li key={index}>{index == 0 ? null:'>'}{router.title}</li>;
    });
  }
  
  render() {
    return (
      <ul className="h-breadcrumbs clearfix">
        {this.items()}
      </ul>
    );
  }
}