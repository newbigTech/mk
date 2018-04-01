import React, { Component } from 'react';
import { Panel } from '../../components/Layout';
import TextBox from '../../components/TextBox';
import Form ,{ SubForm, FormGroup } from '../../components/Form';
import Button from '../../components/Button';
import PicUpload from '../../components/PicUpload';
import { browserHistory } from 'react-router';
import TextArea from '../../components/TextArea';
import NumberBox from '../../components/NumberBox';
import { Link } from 'react-router';

export default class Evaluate extends Component {

  componentWillMount() {
    let oid = this.props.location.query.orderId;
    if (oid) {
      fetch(`${odService}/order/queryForOrderDetails/${oid}`, {
        headers: Hmall.getHeader({})
      })
          .then(Hmall.convertResponse('json', this))
          .then(json => {
            let totalPaths = [], { details } = json.resp[0];
            for (let i = 0; i < details.length; i++) {
              totalPaths.push({productId: details[i].productDetailInfo.productId, paths: []});
            }
            this.setState({
              fetch_status: 'init',
              resp: json.resp,
              totalPaths
            });
          })
          .catch(Hmall.catchHttpError(()=> {
            this.setState({
              fetch_status: 'error'
            });
          }))
    }
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }

  constructor(props) {
    super(props);
    this.state = {
      paths: [],
      data: [],
      resp: [],
      fetch_status: 'uninit',
      allPaths: [],
      heightValid: [],
      weightValid: []
    }
  }

  handleSubmit(e) {
    e.preventDefault();
    if (confirm("是否提交评论？")) {
      let { totalPaths } = this.state,
          { height, weight, orderId, productCode, evaluation, size, styleText,detailId,productId } = e.target,
          list = [];
      e.preventDefault();
      for (let i = 0; i < totalPaths.length; i++) {
        list.push({
          height: height.value||height[i].value,
          weight: weight.value||weight[i].value,
          orderId: orderId.value||orderId[i].value,
          productCode: productCode.value||productCode[i].value,
          evaluation: evaluation.value||evaluation[i].value,
          paths: totalPaths[i].paths,
          size: size.value||size[i].value,
          styleText: styleText.value||styleText[i].value,
          detailId: detailId.value||detailId[i].value,
          productId: productId.value||productId[i].value
        });
      }
      fetch(`${commentService}/evaluate`, {
        method: 'post',
        headers: Hmall.getHeader({'Content-Type': 'application/json'}),
        body: JSON.stringify(list)
      })
          .then(Hmall.convertResponse('json', this))
          .then(json => {
            if (json.success) {
              browserHistory.push('/account/order-center/order-list.html');
            }
          })
          .catch(Hmall.catchHttpError(()=> {
            alert("服务器繁忙");
          }))
    }
  }

  componentWillUnmount() {
    this.isUnMounted = true;
  }

  getImage(img, productId) {
    this.setState({paths: img});
    let { totalPaths }=this.state;
    totalPaths.map((item, i)=> {
      if (item.productId == productId) {
        item.paths = img;
      }
    })
    this.setState({totalPaths});
  }

  numberValid(e) {
    let { value } = e.target,
        reg = /^[1-9]\d{0,2}$/;
    if (value != "") {
      let validNumber = reg.test(value);
      if (!validNumber) {
        alert("输入数字过大");
      }
    }
  }

  showEvaluate(detials, orderId) {
    let { paths } = this.state;
    return detials.map((detail, index) => {
      let { summaryInfo, productDetailInfo,detailId,productId,productCode } = detail;
      return (

          <table className="evaluate_table_one">
            <tbody className="evaluate_tbody">
            <tr>
              <td className="evaluate_tbody_td">
                <div className="evaluate_tbody_td_div">
                  <Link to={`/product-detail.html?productCode=${productCode}`}><img
                      src={Hmall.cdnPath(productDetailInfo.stylePic)}></img></Link>
                </div>
                <div className="evaluate_ul">
                  <ul>
                    <li><Link to={`/product-detail.html?productCode=${productCode}`}>{summaryInfo.name}</Link></li>
                    <li>{ productDetailInfo.productId }</li>
                    <li>{productDetailInfo.size}{productDetailInfo.styleText}</li>
                  </ul>
                </div>
              </td>
              <td className="evaluate_form">
                <ul>
                  <li className="form-li">
                    <FormGroup>
                      <span>身高</span>
                      <NumberBox onBlur={(e)=>this.numberValid(e)} allowNegative={false} allowDecimal={false}
                                 name="height" width={60} height={25}></NumberBox>
                      <span>cm</span>
                      &nbsp;&nbsp;&nbsp;&nbsp;
                      <span>体重</span>
                      <NumberBox onBlur={(e)=>this.numberValid(e)} allowNegative={false} allowDecimal={false}
                                 name="weight" width={60} height={25}></NumberBox>
                      <span>kg</span>
                      <input name="size" type="hidden" value={productDetailInfo.size}></input>
                      <input name="orderId" type="hidden" value={orderId}></input>
                      <input name="styleText" type="hidden" value={productDetailInfo.styleText}></input>
                      <input name="productCode" type="hidden" value={productDetailInfo.productCode}></input>
                      <input name="detailId" value={detailId} type="hidden"></input>
                      <input name="productId" value={productId} type="hidden"></input>
                    </FormGroup>
                  </li>
                  <li>
                    <TextArea width={440} height={150} name="evaluation"></TextArea>
                  </li>
                  <li className="evaluate_button">
                    <PicUpload getImage={(img) =>this.getImage(img,productDetailInfo.productId)}></PicUpload>
                  </li>
                </ul>
              </td>
            </tr>
            </tbody>
          </table>
      );
    })
  }

  render() {
    let { details=[], orderId } = this.state.resp[0] || [];

    return (
        <Panel id="evaluate_div">
          {
            (()=> {
              switch (this.state.fetch_status) {
                case 'uninit':
                  return <div className="loading"></div>;
                case 'init':
                  return this.state.resp.length ? <Form onSubmit={(e) => this.handleSubmit(e)}>
                    <FormGroup>
                      {this.showEvaluate(details, orderId)}
                    </FormGroup>
                    <FormGroup>
                      <Button type="submit" text="发表评价" className="red submitevaluate" width={130} height={35}></Button>
                    </FormGroup>
                  </Form> : <h1 className="info">暂无商品信息</h1>;
                case 'error':
                  return <h1 className="error">网页出错</h1>;
              }
            })()
          }

        </Panel>
    );
  }
}