import React, { Component, PropTypes } from 'react'
import Button from "../Button";
import Icon from '../Icon';
import CheckBox from  '../CheckBox';

export default class ChooseList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      flag: true,
      flag_checkbox: false,
      chooseList: [],
    }
  }

  setFlag() {
    if (this.refs.choose.offsetHeight > 35) {
      this.setState({flag_checkbox: true});
    } else {
      this.setState({flag_checkbox: false});
    }
  }

  componentDidMount() {
    this.setFlag();
  }

  componentDidUpdate(prevProps) {
    if (prevProps.items.toString() != this.props.items.toString()) {
      this.setFlag();
    }
  }

  addChoose(value) {
    let { chooseList } = this.state, index = chooseList.indexOf(value);
    if (index != -1) {
      chooseList.splice(index, 1);
    } else {
      chooseList.push(value);
    }
    this.setState({chooseList: chooseList});
  }

  multipleChoose(callback) {
    if (this.state.flag_checkbox) {
      this.setState({flag: false});
      setTimeout(()=>{
        this.refs.choose.style.overflow="auto";
      },300,this);
    }
    callback && callback();
  }

  cancel(callback){
    if (this.state.flag_checkbox) {
      setTimeout(()=>{
        this.refs.choose.style.overflow="hidden";
      },300,this);
    };
    this.setState({chooseList:[]});
    callback && callback();
  }


  render() {

    var items = this.props.items.map((item, i)=> {
      if (this.props.loadingFlag) {
        return <li key={i}>{item}</li>
      } else {
        if (this.props.flag) {
          return <li key={i} onClick={()=>{this.addChoose(item)}}>
            <CheckBox name="item" className="check_style"
                      value={this.state.chooseList.indexOf(item)!=-1?"Y":"N"}/>
            <span>{item}</span>
          </li>
        } else {
          return <li key={i} onClick={()=>{this.props.singleClick(item,this.props.name)}}>{item}</li>
        }
      }
    });

    var disable_flag = this.state.chooseList.length == 0, { flag, name, choose, cancel } = this.props,
    { flag_checkbox, chooseList } = this.state;

    return (
        <div className="choose-list"
             style={{outline:flag?"solid 2px #27788B":"",borderBottom: flag?"":"solid 1px #DADADA"}}>
          <div className="c-second" style={{backgroundColor:flag?"#e9f3f5":""}}>
            <span className="item_title">{name}</span>
          </div>
          <div className={"all_choose"+(flag_checkbox?" less":"")+(this.state.flag?" ":" more")}>
            <ul ref="choose">
              {items}
            </ul>
            <div className="div_button">
              <button style={{display: flag_checkbox?flag?"none":"":"none"}}
                      onClick={()=> this.setState({flag: !this.state.flag})} className="button_c">
                {this.state.flag ? "更多" : "收起"}
                <Icon name="show-more" style={{ transform:this.state.flag?"rotate(0deg)":"rotate(180deg)" }}/>
              </button>
              <button className="button_c" style={{display: flag?"none":""}} onClick={()=> {this.multipleChoose(choose(name))}}>
                <Icon name="addButton"></Icon>多选
              </button>
            </div>
          </div>
          <div className="div_button_1" style={{height: flag?"50px":"0px"}}>
            <button className={"button_submit "+(disable_flag?"button_disable":"")}
                    onClick={()=>{this.props.submit(chooseList, name);}} disabled={disable_flag}>确定
            </button>
            <button className="button_cancel" onClick={()=>{this.cancel(cancel())}}>取消
            </button>
          </div>
        </div>
    )
  }
}