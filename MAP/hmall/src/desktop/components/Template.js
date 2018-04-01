import React, { Component, isValidElement } from 'react';
import { browserHistory } from 'react-router';
import ModalContainer from './ModalContainer'

export default class Template extends Component{
  
  constructor(props) {
    super(props);
    this.state = {};
    this.accessCheck(props);
  }
  
  componentDidMount() {
    this.lazyLoad(this.props.route);
    Hmall.Storage.set('modal-container',this.refs.modal);
  }
  
  lazyLoad(route) {
    const { tplt, components={} } = route,
      { lazyLoadItem } = this,
      componentsKeys = Object.keys(components);
    Promise.all([import(`../templates/${tplt}`)].concat(componentsKeys.map(key => {
      let { component, container } = components[key];
      if(component){
        return import(`./${component}`);
      }else if(container){
        return import(`../containers/${container}`);
      }
    }))).then(modules => {
      if(!this.isUnMounted){
        this.setState({Template: modules.shift().default, components: modules.reduce((obj, module, i) => {
          const { props={} } = components[componentsKeys[i]]
          obj[componentsKeys[i]] = React.createElement(module.default, props);
          return obj;
        }, {})},() => {
          this.setTitle();
        });
      }
    });
  }
  
  componentWillUpdate(nextProps) {
    let { route } = nextProps;
    if( route.path != this.props.route.path && this.accessCheck(nextProps)){
      this.state.Template = null;
      this.lazyLoad(route);
    };
  }
  
  componentWillUnmount() {
    this.isUnMounted = true;
  }

  setTitle(){
    let { routes } = this.props;
    document.title = routes[routes.length - 1].title;
  }
  
  accessCheck(props) {
    let { route, location} = props,
      { accessCheck } = route,
      { pathname, search } = location;
    if(accessCheck && !Hmall.getCookie('access_token')){
      this.state.stop = true;
      browserHistory.push({
        pathname: '/login.html',
        query: {
          redirect_url: pathname + search
        }
      });
      return false;
    }
    return true;
  }
  
  getBackgroundFit(fit){
    switch(fit || 'MIDDLE'){
      case 'FILL':
        return 'background-repeat: no-repeat;background-position: center; background-size: 100% 100%';
      case 'MIDDLE':
        return 'background-repeat: no-repeat;background-position: center; background-size: auto auto';
      case 'STRETCHING':
        return 'background-repeat: no-repeat;background-position: center; background-size: auto 100%';
      case 'RESPONSIVE':
        return 'background-repeat: no-repeat;background-position: top; background-size: 100% auto';
      case 'TILE':
        return 'background-repeat: repeat;background-position: left top; background-size: auto auto';
    }
  }
  
  render() {
    let { stop, Template, components } = this.state, 
      { location, route } = this.props,
      { tplt, backgroundColor=CMSConfig.backgroundColor, backgroundImage=CMSConfig.backgroundImage, backgroundFit=CMSConfig.backgroundFit, backgroundFixed=CMSConfig.backgroundFixed } = route;
    if(stop){
      return null;
    }else {
      for(var id in components) {
        components[id] = React.cloneElement(components[id], this.props);
      }
      if(backgroundImage){
        backgroundImage = `url(${Hmall.cdnPath(backgroundImage)})`;
      }else{
        backgroundImage = 'none';
      }
      return (
        <div className={tplt}>
          <style>{
            `body {background-color: ${backgroundColor};background-image: ${backgroundImage};background-attachment: ${backgroundFixed == 'Y'? 'fixed' : 'scroll'};${this.getBackgroundFit(backgroundFit)}}`
          }</style>
          {
            Template && <Template {...components} {...this.props}></Template>
          }
          <ModalContainer ref="modal"></ModalContainer>
        </div>
      );
    }
  }
}