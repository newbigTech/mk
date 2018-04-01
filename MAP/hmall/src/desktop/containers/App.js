import React, { Component } from 'react';
import ShortCut from '../components/ShortCut';
import Top from '../components/Top';

export default class App extends Component{

  //设置页面的标题
  setTitle(){
    let { routes } = this.props;
    document.title = routes[routes.length - 1].title;
  }

  componentDidMount() {
    this.setTitle();
    let { query } =  this.props.location ,
        { app_id , auth_code } = query ,    url = localStorage.getItem("url");
    if(app_id&&auth_code&&url){
      url = JSON.parse(localStorage.getItem("url"));
      if(url=="bind"){
        localStorage.removeItem("url");
        location.href =`/thirdParty.html?type=4&auth_code=${auth_code}&state=1212&bind=true`
      }else{
        location.href =`/thirdParty.html?type=4&auth_code=${auth_code}&state=1212`
      }
    }
  }
  componentDidUpdate(){
    this.setTitle();
  }

  render() {
    let { location, children } = this.props;
    return (
      <div className="page-wrapper">
        <div className="content">
          <ShortCut location={location}></ShortCut>
          {children}
          <Top></Top>
        </div>
      </div>
    );
  }
}
