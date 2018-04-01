import React, { Component, PropTypes } from 'react';
import { Link } from 'react-router';
import { Panel } from './Layout';
import Product from './Product';

const { recommends } = CMSConfig;

export default class VRecommend extends Component{

  static propTypes = {
    pagesize: PropTypes.number
  }
  
  static defaultProps = {
    pagesize: 3
  }
  
  constructor(props) {
    super(props);
    this.state={
      page: 1
    };
  }

  slideDown(){
    let { page } = this.state,
      totalPage = Math.ceil(recommends.length / this.props.pagesize);
    this.setState({page: page < totalPage ? page + 1 : 1});
  }

  slideUp(){
    let { page } = this.state,
      totalPage = Math.ceil(recommends.length / this.props.pagesize);
    this.setState({page: page > 1 ? page - 1 : totalPage});
  }

  renderRecommends() {
    const { pagesize } = this.props,
      { page } = this.state;
    return recommends.map((item,i)=>{
      return <li key={i}><Product info={item} width={240} loadPicture={i < pagesize * page}></Product></li>;
    });
  }
  
  render(){
    const { page } = this.state,
      { pagesize } = this.props,
      height = 316 * pagesize;
    return <Panel className="h-component h-v-recommend">
        <div className="panel-header line">
        <span>店铺推荐</span>
      </div>
      <div className="scroll-wrapper" style={{height}}>
        <ul style={{marginTop: height * (1 - page)}} >
          {this.renderRecommends()}
        </ul>
      </div>
      <div className="recommend-footer">
        <i className="slide up" onClick={()=>{this.slideUp()}}></i>
        <i className="slide down" onClick={()=>{this.slideDown()}}></i>
      </div>
    </Panel>;
  }
}