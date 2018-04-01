import React, { Component, PropTypes } from 'react';
import Upload from 'rc-upload';

export default class Demo extends Component{
  //React.PropTypes主要用来验证组件接收到的 props 是否为正确的数据类型，如果不正确， console 中就会出现对应的 warning 。出于性能方面的考虑，这个 API 只在生产环境下使用。
  static propTypes = {
    name: PropTypes.string.isRequired,
    color: PropTypes.string.isRequired,

    //othertypes
    myArray: PropTypes.array,
    myBool: PropTypes.bool,
    myFunc: PropTypes.func,
    myNumber: PropTypes.number,
    myString: PropTypes.string,

    myObject: PropTypes.shape({
      text: PropTypes.string,
      numbers: PropTypes.arrayOf(PropTypes.number)
    })
  }    
  constructor (props) {
    super(props);

    this.uploaderProps = {
      action: '/upload.do',//接口地址
//      data: { a: 1, b: 2 },
      headers: Hmall.getHeader(),
      multiple: false, //是否多文件
      beforeUpload(file) {
        console.log('beforeUpload', file.name);
      },
      onStart: (file) => {
        console.log('onStart', file.name);
        // this.refs.inner.abort(file);
      },
      onSuccess(file) {
        console.log('onSuccess', file);
      },
      onProgress(step, file) {
        console.log('onProgress', Math.round(step.percent), file.name);
      },
      onError(err) {
        console.log('onError', err);
      }
    };
    
    this.state = {
      email:'',
      lastName:'',
      firstName:''
    };
  }
  componentWillMount(){
    //组件将要渲染时调用
    fetch('data/hmget?type=PPO&code=002')
      .then(response => response.json())
      .then(data => this.setState(data))
      .catch(err => console.error(err));
  }
  componentDidMount(){
    //组件渲染时调用
  }
  componentWillUpdate(nextProps,nextState){
    //组件将要更新时调用
  }
  componentDidUpdate(preProps,preState){
    //组件更新时调用
  }
  componentWillUnmount(){
    //组件销毁时调用    
  }
  componentWillReceiveProps(nextProps){
    //已加载组件收到新的参数时调用
  }
  shouldComponentUpdate(nextProps,nextState){
    //组件判断是否重新渲染时调用
    return true;
  }
  handleInputChange(e){
    alert(e.target.value)
  }
  render() {
    return (
      <div>
        <Upload {...this.uploaderProps}><a>开始上传</a></Upload>
        <div dangerouslySetInnerHTML={{__html:'<div>动态内容</div>'}}></div>
        <div className={this.props.name} style={{color:this.props.color}}>Hello {this.state.lastName + " " + this.state.firstName}!!!!!!!Email:{this.state.email}</div>
        <input value={this.state.value} onChange={this.handleInputChange}/>
      </div>
    );
  }
}