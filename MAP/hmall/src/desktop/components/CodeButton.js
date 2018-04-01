import React, { Component, PropTypes } from 'react';
import Button from './Button';

const TIME = HmallConfig.verification_code_retry_interval;

export default class CodeButton extends Component{
  static propTypes = {
    width: PropTypes.number,
    height: PropTypes.number,
    className: PropTypes.string,
    disabled: PropTypes.bool
  }
  
  static defaultProps = {
    text: '获取验证码'
  }
  
  getClockText(time) {
    return `请等待${time}秒`;
  }
  
  constructor(props) {
    super(props);
    let text,
      last,
      time = Hmall.getCookie('code-time')||0;
    last = time - Date.now();
    if(last>0) {
      last = Math.floor(last/1000);
      text = this.getClockText(last);
    }
    this.state = {
      clock: false,
      text: text || this.props.text
    }
    if(last>0) {
      this.clock(last);
    }
  }
  
  componentWillReceiveProps(nextProps) {
    
    if(this.state.clock && 'text' in nextProps){
      let temp = {};
      for(var key in nextProps){
        if( key != 'text'){
          temp[key] = nextProps[key]
        }
      }
      nextProps = temp;
    }
    this.setState(nextProps);
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  clock(time) {
    if(time > 0){
      this.setState({ text: this.getClockText(time), clock: true });
      setTimeout(() => this.clock(time - 1), 1000, this);
    }else{
      this.setState({ text: this.props.text, clock: false });
    }
  }

  getCode() {
    fetch(`${smsService}/mobile/send/${this.props.phone}`)
    .catch(Hmall.catchHttpError());
  }
  
  handleClick(e) {
    let time = new Date(Date.now() + TIME * 1000);
    Hmall.setCookie('code-time',time.getTime(),{
      expires: new Date(Date.now() + TIME * 1000)
    });
    this.clock(TIME);
    this.getCode();
  }
  
  render() {
    let { style, className, width, height, phone , disabled} = this.props,
      { clock, text } = this.state;
    disabled = disabled || clock || !phone;
    return (
      <Button disabled={ disabled } text={text} height={height} width={width} className={className} style={style} onClick={e => this.handleClick(e) }>
      </Button>
    );
  }
}