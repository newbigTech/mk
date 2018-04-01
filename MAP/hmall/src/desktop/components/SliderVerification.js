import React, { Component, PropTypes } from 'react';
import binstring from 'binstring';

let points = [],
  beginDate,
  endDate,
  _handleMove,
  _handleUp;

const expire = 5 * 60 * 1000;

export default class SliderVerification extends Component{
  
  constructor(props) {
    super(props)
    this.state = this.getInitState();
  }
  
  getInitState() {
    return {
      status: 'init',
      text: '请按住滑块，拖到最右边',
      left: 0
    };
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  componentWillReceiveProps(nextProps) {
    if(nextProps.secretKey != this.props.secretKey){
      this.reset();
    }
  }
  
  reset() {
    this.setState(this.getInitState());
  }
  
  validate() {
    let { wrap } = this.refs,
      { phoneNumber, secretKey, onValid, onInValid } = this.props,
      ehs = Crypto.util.bytesToBase64(
        Crypto.AES.encrypt(
          JSON.stringify({
            'b': beginDate.getTime(),
            'e': endDate.getTime(),
            'p': points,
            'l': wrap.getBoundingClientRect().left,
            'w': wrap.offsetWidth
          }),
          binstring(secretKey,{in:'hex',out:'bytes'}),
          {asBytes: true, mode: new Crypto.mode.ECB(Crypto.pad.pkcs7)}
        )
      );
    fetch(`${verificationService}/v/check/${phoneNumber}`,{
      method: 'POST',
      headers : Hmall.getHeader(),
      body: ehs
    })
    .then(Hmall.convertResponse('json',this))
    .then(valid => {
      if(valid){
        onValid && onValid();
//        this.setState({
//          status: valid? 'valid':'invalid',
//              text: `验证${valid?'通过':'失败'}`
//              
//        });
        this.setState({
          status: 'valid',
          text: '验证通过'
        });
      }else{
        this.reset();
        onInValid && onInValid();
      }
    })
    .catch(Hmall.catchHttpError());
  }
  
  handleMouseDown(e) {
    if(this.state.status != 'init')return;
    let x = e.pageX || e.x,
      y = e.pageY || e.y;
    points = [[x, y]];
    beginDate = new Date();
    document.addEventListener('mousemove',_handleMove = (e => this.handleMouseMove(e)),false);
    document.addEventListener('mouseup',_handleUp = (e => this.handleMouseUp(e)),false);
  }
  
  handleMouseMove(e) {
    let x = e.pageX || e.x,
      y = e.pageY || e.y,
      { wrap, block } = this.refs,
      w = wrap.offsetWidth - block.offsetWidth,
      _x = x - points[0][0],
      notPush = false;
    if(_x < 0){
      _x = 0;
      notPush = true;
    }
    
    if(_x > w){
      _x = w;
      notPush = true;
    }

    this.setState({ left: _x });
    
    if(!notPush)
      points.push([x, y]);
  }
  
  handleMouseUp(e) {
    let x = e.pageX || e.x,
      y = e.pageY || e.y,
      { wrap, block } = this.refs,
      w = wrap.offsetWidth - block.offsetWidth,
      _x = x - points[0][0];
    if(_x < w) {
      this.setState({ left: 0 });
    }else{
      this.setState({
        status: 'validing',
        text: '正在验证...'
      });
      endDate = new Date();
      this.validate();
    }
    document.removeEventListener('mousemove',_handleMove,false);
    document.removeEventListener('mouseup',_handleUp,false);
    _handleMove = _handleUp = null;
  }
  
  render() {
    let { status, left, text } = this.state,
      { width } = this.props;
    return (
      <div ref="wrap" className={`h-slider-verification ${status}`} style={{width}}>
        <span>{text}</span>
        <div className="h-slider-bar">
          <div ref="block" style={{marginLeft: left}} className="h-slider-block" onMouseDown={e => this.handleMouseDown(e)}></div>
        </div>
      </div>
    );
  }
}