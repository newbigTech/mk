import React, { Component } from 'react';
import TextBox from '../../components/TextBox';
import Button from '../../components/Button';
import { Link, IndexLink, browserHistory } from 'react-router';
import Pagenation from '../../components/Pagenation';
import CheckBox from '../../components/CheckBox';
import Icon from '../../components/Icon';

const pagesize = 10;

export default class Favorites extends Component{
  dload(props){
    let {page =1} = props.location.query;
    let userId=Hmall.getCookie('userid');
    if(userId){
      fetch(`${urService}/customer/favorite/list`,{
        method: "POST",
        headers:Hmall.getHeader({"Content-Type":"application/json",}),
        body: JSON.stringify(
          {
            page:page,
            pageSize:pagesize,
          }
        )})
    .then(Hmall.convertResponse('json',this))
    .then(json => {
        this.setState({resp:json.resp,total:json.total,statePage: page,fetch_status:'init'})
    })
    .catch(Hmall.catchHttpError(() => {
      this.setState({
        fetch_status: 'error'
      });
    }))}
  }
    
  componentWillMount(){
    /*组件将要渲染时调用*/
    this.dload(this.props);
  }
  
  componentWillReceiveProps(nextProps){
    let { page } = nextProps.location.query,
        { query } = this.props.location;
    if( query.page!= page) {
      this.state.fetch_status = 'uninit';
      this.state.statePage =  page ;
      this.dload(nextProps);
    }
  }
  
  componentWillUnmount(){
    this.isUnMounted = true;
  }
  
  constructor(props){
    super(props);
    this.state = {
        takeSelect: [],
        resp: [],
        statePage:'1',
        total:0,
        checkFlag:false,
        fetch_status:'uninit'
       }
    }
    
  /*单选功能*/
  handleSelectOne(Code){ 
    let {takeSelect ,resp} =this.state;
      if(takeSelect.indexOf(Code)!=-1){  
        takeSelect.splice(takeSelect.indexOf(Code),1);
       }else{
         takeSelect.push(Code);
         };
    this.setState({takeSelect:takeSelect});
    if(takeSelect.length==resp.length){
        this.setState({checkFlag:true})
    }else{
    this.setState({checkFlag:false})
     }
  }
  
  /*删除多条数据后重新获取数据*/ 
  reGet2(){
    debugger
    let { takeSelect ,resp, total ,checkFlag, statePage } = this.state;
    if(takeSelect.length==0){
      alert("请选择删除的商品")
    }else{
      if(statePage!=(Math.ceil(total/pagesize))){
        statePage = statePage;
      }else{
        statePage = Math.ceil((total-takeSelect.length)/pagesize);
      }
      statePage==0 ? statePage=1:statePage;
      this.forceUpdate();
      fetch(`${urService}/customer/favorite/multDelete`,{
        method: "POST",
        headers: Hmall.getHeader({"Content-Type":"application/json",}), 
        body: JSON.stringify({productList:takeSelect,page:statePage,pageSize:pagesize})})
        .then(Hmall.convertResponse('json',this))
        .then(json=>{if(json.success){
          this.removeAll(); 
          this.setState( {resp: json.resp , total: json.total, statePage: statePage});
      }})
        .catch(Hmall.catchHttpError())
    }
}

  /*全选功能*/
  handleSelectAll(e){
    let { takeSelect ,resp , checkFlag}=this.state;
    if(!checkFlag){
      resp.map((goods,i)=>{
      if(takeSelect.indexOf(goods.summaryInfo.productCode)==-1){
        takeSelect.push(goods.summaryInfo.productCode);
        }
      })   
    }else{
      takeSelect = [];
    }
     this.setState({takeSelect:takeSelect,checkFlag:!checkFlag})   
  }

  
 /*删除已选*/
  removeAll(){
    let { takeSelect ,resp ,checkFlag } = this.state,
        newResp = [];
    resp.forEach((goods,i)=>{
      if(takeSelect.indexOf(goods.summaryInfo.productCode)==-1){
          newResp.push(goods);
      }
    })
    this.setState({resp:newResp,takeSelect:[],checkFlag: false});
  }
  
  favoriteList(){
    let { takeSelect, resp } = this.state;
    return resp.map((goods,i)=>{
            let { summaryInfo, approvalInfo, currentPrice} = goods,
                { productCode, name, isNoReasonToReturn } = summaryInfo;
            return (
              <tr key={i}>
                <td>
                  <CheckBox width={16} height= {16} value={takeSelect.indexOf(productCode)==-1? 'N' : 'Y' } onChange={(value)=>{this.handleSelectOne(productCode)}}></CheckBox>
                </td>
                <td>
                  <Link to={"/product-detail.html?productCode="+productCode}><img src={Hmall.cdnPath(summaryInfo.mainPic)}/></Link>
                </td>
                <td className="top">
                  <Link to={"/product-detail.html?productCode="+productCode}><span>{name}</span></Link><br/>
                  {
                    isNoReasonToReturn=="Y"&& <Icon name="7days" title="7天无理由退货"></Icon>
                  }
                </td>
                <td>
                  ￥ {currentPrice}
                </td>
            </tr>
          )})
  }
  
  render() {
    let { resp, fetch_status } = this.state;
    return (
        <div id="favorites-div">
        {
          (() => {
            switch(fetch_status) {
              case 'uninit': return <div className="loading"></div>
              case 'init': return resp.length?
                    <table>
                      <thead>
                        <tr>
                          <td></td>
                          <td></td>
                          <td><strong>商品信息</strong></td>
                          <td><strong>单价</strong></td>
                        </tr>
                      </thead>
                      <tbody>
                        { this.favoriteList() }
                      </tbody>
                      <tfoot>
                        <tr>
                          <td colSpan="4">
                            <CheckBox width={16} height= {16} value={this.state.checkFlag?'Y' : 'N'}  onChange={e =>this.handleSelectAll(e)}></CheckBox>
                            <span onClick = {e =>this.handleSelectAll(e)}>全选</span>
                            <span onClick={()=>{this.reGet2()}}>删除已选</span>
                          </td>
                        </tr>
                        <tr>
                          <td colSpan="4"></td>
                        </tr>
                        <tr>
                          <td colSpan="8">
                            <Pagenation page={this.state.statePage} pagesize={pagesize} total={(parseInt(this.state.total))} location={this.props.location}></Pagenation>
                          </td>
                        </tr>
                      </tfoot>
                    </table>
                    :<h1 className="info">无收藏商品</h1>;
              case 'error': return <h1 className="error">网页出错</h1>
            }
          })()
        }
          </div>
        )
  }
}