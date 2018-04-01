import React, { Component } from 'react';
import { Link, IndexLink } from 'react-router';
import { browserHistory } from 'react-router';
import { Panel, Row, Col} from '../components/Layout';
import Moment from 'moment';

export default class Message extends Component{
  
  componentWillUnmount(){
    this.isUnMounted = true;
  }
  
  constructor(props){
    super(props)
    this.state={
      title: "站内信",
      init1: false,
      init2: false,
      filters: [],
      detailMessage: [],
      unListRead:[],
      unListNum:0,
      msgNum:0
    }
    let access_token = Hmall.getCookie('access_token');
    if(!access_token){
      let { pathname, search } = props.location,
        url = encodeURIComponent(pathname+search);
      browserHistory.push(`/login.html?redirect_url=${url}`);
    }
  }
  
  componentDidMount(){
    let thisMsgCode = this.props.location.query.msgCode,
        thisPageNum = this.props.location.query.pageNum;
    if(thisMsgCode==null||thisMsgCode==undefined){
      thisMsgCode = 1;
    }
    if(thisPageNum==null||thisPageNum==undefined){
      thisPageNum = 1;
    }
    var codeNumber = parseInt(thisMsgCode);
    var pageNumber = parseInt(thisPageNum);
    let userid=Hmall.getCookie('userid');
    if(userid){
      //初始化左侧列表信息
      fetch(`${sitemsgService}/siteMsg/list`,{
        headers:Hmall.getHeader({})
      })
      .then(Hmall.convertResponse('json',this))
      .then(json => {
        if(json.success){
          this.setState({filters:json.resp,init1:true})
          let { filters } = this.state;
          var number = Number(codeNumber)
          if(!filters||filters.length != 0){
            this.state.filters.map((unread, i) => {
              this.state.unListRead.push(unread.unReadCount)
            })
            filters[number - 1].unReadCount = 0;
            this.setState({
              title:filters[number - 1].typeName,unListNum:codeNumber
            })
          }
           //初始化右侧列表信息
          fetch(`${sitemsgService}/siteMsg/info/${codeNumber}`,{
            headers:Hmall.getHeader({}),
          })
          .then(Hmall.convertResponse('json',this))
          .then(json=>{
            if(json.success){
              let count = 0;
              json.resp.map((index, i) => {
                return count++;
              })
              this.setState({detailMessage:json.resp,init2:true,msgNum:count})
            }
            else{
                this.setState({init2:false})
                }
          })
          .catch(Hmall.catchHttpError());
        }
      })
    }
  }
  
  componentWillReceiveProps(nextProps){
    if(this.props.location.query.msgCode != nextProps.location.query.msgCode){
      let nextMsgCode = nextProps.location.query.msgCode,
          nextPageNum = nextProps.location.query.pageNum;
      if(nextMsgCode==null||nextMsgCode==undefined){
         nextPageNum = 1;
      }
      if(nextMsgCode==null||nextPageNum==undefined){
         nextPageNum = 1;
      }
      var codeNumber = parseInt(nextMsgCode);
      var pageNumber = parseInt(nextPageNum);
      let userid=Hmall.getCookie('userid');
      if(userid){
        //更新左侧信息列表
            var number = Number(codeNumber)
            let{filters} = this.state
            filters[number - 1].unReadCount = 0;
            this.setState({
              title:filters[number - 1].typeName,unListNum:codeNumber
            }) 
            //加载右侧信息列表
            fetch(`${sitemsgService}/siteMsg/info/${number}`,{
              headers:Hmall.getHeader({}),
             
            })
            .then(Hmall.convertResponse('json',this))
            .then(json=>{
              if(json.success){
                let count = 0;
                json.resp.map((index, i) => {
                  return count++;
                })
                this.setState({detailMessage:json.resp,init2:true,msgNum:count})
              }
              else{
                this.setState({init2:false,msgNum:0})
              }
            })
            .catch(Hmall.catchHttpError());
            //加载右侧信息列表结束
      }
    }
  }
  
  //点击切换事件
  handleInfo(title,number){
    let { filters } = this.state;
    filters[number - 1].unReadCount = 0;
    this.setState({
      title:title,
      filters:filters
    })
    fetch(`${sitemsgService}/siteMsg/info/${number}`,{
      headers:Hmall.getHeader({})
    })
    .then(Hmall.convertResponse('json',this))
    .then(json=>{
      if(json.success){
        this.setState({detailMessage:json.resp,init2:true})
      }
      else{
        this.setState({init2:false})
      }
    })
    .catch(Hmall.catchHttpError())
  }
  
  handleDelete(msgcode,i){
    fetch(`${sitemsgService}/siteMsg/delete/`+msgcode,{
      method: "POST",  
      headers: Hmall.getHeader({"Content-Type":"application/json"}), 
    })
      .then(Hmall.convertResponse('json',this))
      .then(json=>{
        if(json.success){
          var detailMessageArr = this.state.detailMessage
          detailMessageArr.splice(i,1)
          this.setState({detailMessage:detailMessageArr})
      }})
      .catch(Hmall.catchHttpError())
  }
  
  render() {
    let { title, msgNum } = this.state;
    if(this.state.init1){
      let {typeNameList} = this.props;
      typeNameList = {typeNameList};
      var messageList=""
        if(this.state.filters.length!=0){
          messageList = this.state.filters.map((typeNameList, i) => {
            return (
                <li key={i}>
                  <Link to={"/Message.html?msgCode="+typeNameList.msgCode+"&pageNum=1"}>
                    <span>
                        {typeNameList.typeName}
                        <span className={typeNameList.unReadCount>0?('show'):('dis')}>
                            <span className="unRead">{typeNameList.unReadCount}</span>
                        </span>
                    </span>
                  </Link>
                </li>
            )}
        )} else{
             messageList =  <div className="loading"></div>
           }
      } else{
         messageList = <div className="loading"></div>
        }
    
    if(this.state.init2){
      let {detailMessage} = this.props;
      detailMessage={detailMessage};
      var detailMessageList=""
      if(!this.state.detailMessage ||!this.state.detailMessage.length){
         detailMessageList = <div className="noMessage">您当前还没有{this.state.title}</div>
      }else{
        detailMessageList = this.state.detailMessage.map((detailMessage, i) => {
          let {uid,title,context,createTime,siteMsgId} = detailMessage;
          return (
              <div className="i7" key={uid}>
                <div className="i8">{Moment(Number(createTime)).format(HmallConfig.date_format)}</div>
                <div className="i9">
                  <div className="i10">
                    {context}
                  </div>
                </div>
                <div className={ i< this.state.unListRead[this.state.unListNum - 1]?('noread'):('')}>
                </div>
              </div>
              
          )
         })
        }
    }else{
      detailMessageList = <div className="loading"></div>
    }
 
    
    return (
      <div id="message-menu">
        <Row>
          {/*左边的导航栏*/}
          <Col width={220}>
            <Panel>
              <ul>
                <li className="list-head"><p></p><p>站内信</p></li>
                {messageList}
              </ul>
            </Panel>
          </Col>
          {/*右边的详细介绍栏*/}

          <Col>
            <Panel id="accountInformation">
              {/*右边的详细介绍栏*/}
              <div className="i4 clearfix">
                <div className="i5"><span className="i6">{title}</span><span className = "msgNum">共{msgNum}条</span></div>
                  {detailMessageList}
              </div>
            </Panel>
          </Col>
        </Row>
      </div>
    );
  }
}