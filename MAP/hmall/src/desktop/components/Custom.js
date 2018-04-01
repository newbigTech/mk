import React, { Component } from 'react';
import { browserHistory } from 'react-router';

const jsre = /(?:<script([^>]*)?>)((\n|\r|.)*?)(?:<\/script>)/ig,
  jsSrcRe = /\ssrc=([\'\"])(.*?)\1/i,
  imgSrcRe = /(<img[^>]*)(src)/ig;

function findParent(el, selector = '*') {
  const matchesSelector = el.matches || el.webkitMatchesSelector || el.mozMatchesSelector || el.msMatchesSelector
  do{
    if (matchesSelector.call(el, selector)) {
      return el;
    }
  } while ((el = el.parentElement) !== null);
}

export default class Custom extends Component{

  constructor(props) {
    super(props);
    this.images = [];
    this.loadedImages = [];
    this.handleScroll = () => {
      this.doScroll();
    }
  }
  
  handleClick(e) {
    let { target } = e, href,
      parent = findParent(target, 'a[href]');
    if( parent ){
      href = parent.href.replace(location.origin,'');
      if(href.indexOf('http') == -1) {
        e.preventDefault();
        browserHistory.push(href);
      }
    }
  }
  
  lazyLoadImgs() {
    let { wrapper } = this.refs,
      images = this.images = Array.prototype.slice.call(wrapper.querySelectorAll('img[data-src]'));
    if(images.length) {
      window.addEventListener('scroll', this.handleScroll, false);
      this.doScroll();
    }
  }
  
  doScroll() {
    this.images.forEach(img => {
      if(Hmall.inViewport(img)) {
        this.loadPicture(img);
      }
    });
  }
  
  loadPicture(picture) {
    let src = picture.getAttributeNS('','data-src');
    if(!picture.src && !picture.loading){
      picture.loading = true;
      let img = new Image();
      img.src = src;
      img.onload = () => {
        if(!this.isUnMounted){
          picture.src = src;
          picture.removeAttributeNS('','data-src');
          delete picture.loading;
          this.loadedImages.push(picture);
          if(this.loadedImages.length == this.images.length) {
            this.detachScroll();
          }
        }
      }
      img.onerror = () => {
        if(!this.isUnMounted){
          delete picture.loading;
        }
      }
    }
  }
  
  detachScroll() {
    window.removeEventListener('scroll', this.handleScroll);
  }
  
  componentDidMount() {
    let { html } = this.props,
      jsscript = [],
      jslink = [],
      match;
    while(match = jsre.exec(html)){
      let attrs = match[1],
        srcMatch = attrs ? attrs.match(jsSrcRe) : false;
      if(srcMatch && srcMatch[2]){
        jslink.push(new Promise(resolve => {
          Hmall.loadScript(srcMatch[2],() => {
            resolve();
          },this);
        }));
      }else if(match[2] && match[2].length > 0){
        jsscript.push(match[2]);
      }
    }
    Promise.all(jslink).then(() => {
      jsscript.forEach(js => {
        eval(js);
      });
    });
    this.lazyLoadImgs();
  }
  
  componentWillUnMount() {
    this.isUnMounted = true;
    if(this.images.length) {
      this.detachScroll();
    }
    Hmall.clearIntervalAndTimeout();
  }
  
  render() {
    let { html } = this.props;
    html = html.replace(imgSrcRe,'$1data-$2');
    return <div ref="wrapper" onClick={this.handleClick} className={`h-component h-custom`} dangerouslySetInnerHTML={{__html:html}}></div>;
  }
}
