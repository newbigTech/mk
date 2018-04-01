import React, { Component } from 'react';
import { Link, browserHistory } from 'react-router';
import Button from './Button';
import TextBox from './TextBox';

export default class Pagenation extends Component{
  
  constructor(props) {
    super(props);
    this.state = {
      forwardPage: ''
    }
  }
  
  items() {
    let { page, pagesize, total, location } = this.props,
      { pathname, query } = location,
      totalPage = Math.ceil(total/pagesize),
      items = [],
      n = 0;
    page = Number(page);
    items.push(this.createAnchor('<', page-1, page > 1, false, n++));
    items.push(this.createAnchor('上一页', page-1, page > 1, false, n++));
    for(var i = 1 ; i < 4 && i <= totalPage ; i++){
      items.push(this.createAnchor(i, i, i != page, true, n++));
    }
    if(totalPage > 10){
      if(page > 5)this.createSplit(items, n++);
      for(var i = page - 1;i < page + 2 ;i++){
        if(i > 3 && i < totalPage - 2){
          items.push(this.createAnchor(i, i, i != page, true, n++));
        }
      }
      if(page < totalPage - 4)this.createSplit(items, n++);
    }else if(totalPage > 6){
      for(var i = 4; i < totalPage - 2 ;i++){
        items.push(this.createAnchor(i, i, i != page, true, n++));
      }
    }
    for(var i = totalPage - 2 ; i < totalPage + 1; i++){
      if(i > 3){
        items.push(this.createAnchor(i, i, i != page, true, n++));
      }
    }
    items.push(this.createAnchor('下一页', page+1, page < totalPage, false, n++));
    items.push(this.createAnchor('>', page+1, page < totalPage, false, n++));
    items.push(<span key={n++}>共{totalPage}页</span>);
    items.push(<span key={n++}>到<TextBox onChange={e=>this.setForwardPage(e)}></TextBox>页</span>);
    items.push(<Button key={n++} disabled={!this.state.forwardPage} width={36} height={18} className="black" text="确定" onClick={() => this.goPage()}></Button>);
    
    return items;
  }
  
  getNewPath(page) {
    let { pathname, query } = this.props.location,
      old = query;
    query = {};
    for(var key in old){
      query[key] = old[key];
    }
    query.page = page;
    return { pathname, query };
  }
  
  goPage() {
    browserHistory.push(this.getNewPath(this.state.forwardPage));
  }
  
  setForwardPage(e) {
    let input = e.target,
      { pagesize, total } = this.props,
      totalPage = Math.ceil(total/pagesize),
      page = parseInt(input.value);
    if(isNaN(page)) page = '';
    else if(page < 1) page = 1;
    else if(page > totalPage) page = totalPage;
    this.setState({forwardPage: page});
    input.value = page;
  }
  
  createSplit(items,n) {
    return items.push(<span key={n}>···</span>);
  }
  
  createAnchor(text, page, link, current, n) {
    return link ? <Link key={n} to={this.getNewPath(page)}>{text}</Link> : current ? <b key={n}>{text}</b> : <i key={n}>{text}</i>
  }
  
  render() {
    let { total } = this.props;
    return total ? (
      <nav className="h-pagenation">
        {this.items()}
      </nav>
    ): null;
  }
}