import React, { Component, PropTypes } from 'react';

export default class Picture extends Component{
  
  static propTypes = {
    width: PropTypes.number,
    height: PropTypes.number,
    className: PropTypes.string,
    style: PropTypes.string,
    lazy: PropTypes.bool,
    load: PropTypes.bool,
    onLoad: PropTypes.func,
    src: PropTypes.string
  }
  
  static defaultProps = {
    lazy: true,
    load: true
  }
  
  constructor(props) {
    super(props);
    this.state = {
      status: 'unload'
    }
    this.handleScroll = () => {
      this.doScroll();
    }
  }
  
  doScroll() {
    let { img } = this.refs,
      { status } = this.state;
    if(Hmall.inViewport(img)) {
      this.detachScroll();
      this.loadPicture(this.props);
    }
  }
  
  componentDidMount() {
    if(this.props.lazy) {
      window.addEventListener('scroll', this.handleScroll, false);
      this.doScroll();
    }else{
      this.loadPicture(this.props);
    }
  }

  componentWillUnmount() {
    this.isUnMounted = true;
    if(this.state.status == 'unload') {
      this.detachScroll();
    }
  }
  
  componentWillReceiveProps(nextProps) {
    if(!this.props.load && nextProps.load) {
      this.loadPicture(nextProps);
    }
  }
  
  loadPicture(props) {
    let { load, src } = props;
    if(load){
      this.refs.img.classList.add('pic-loading');
      this.setState({status: 'loading'});
      let img = new Image();
      img.src = src;
      img.onload = () => {
        if(!this.isUnMounted){
          this.onLoad();
        }
      }
      img.onerror = () => {
        if(!this.isUnMounted){
          this.onError();
        }
      }
    }
  }
  
  onLoad() {
    let { onLoad } = this.props;
    onLoad && onLoad();
    this.setState({status: 'loaded'},() => {
      setTimeout(() => {
        this.refs.img.classList.remove('pic-loading')
      },5,this);
    });
  }
  
  onError() {
    this.setState({status: 'error'},() => {
      let cl = this.refs.img.classList;
      cl.remove('pic-loading');
      cl.add('error');
    });
  }
  
  detachScroll() {
    window.removeEventListener('scroll', this.handleScroll);
  }
  
  render() {
    let { src, lazy, className='', width, height, style={}, onClick, load } = this.props;
    style.width = width;
    style.height = height;
    return <div className={`h-picture ${className}`} style={style} ref="img" >{this.state.status == 'loaded' && <img onClick={onClick} src={src}></img>}</div>;
  }
}
