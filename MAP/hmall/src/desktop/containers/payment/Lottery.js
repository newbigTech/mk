
/**
 * Created by ZWL on 2017/3/8.
 */
import React, { Component } from 'react';
import Button from '../../components/Button';

export default class Lottery extends Component{
  componentWillUnmount() {
    this.isUnMounted = true;
  }
  
  componentDidMount() {
    let { prize  } = this.state,count = 0 , arr = prize , noprize ={}
    fetch(`${droolsService}/h/sale/draw/getAwardpro/8fa1812f-f46d-41a3-a9e8-d8ed40817bbf`, {
      method: "get",
      headers: Hmall.getHeader({
        "Content-Type": "application/json"
      }),
    })
        .then(Hmall.convertResponse('json',this))
        .then(json=> {
          let { success , resp } = json , arr = resp
          if(success){
            arr.map((p,i)=> {
              p.awardProbility = Number(p.awardProbility)
              count = count + p.awardProbility
            })
            noprize.couponName = "无",
                noprize.awardProbility = 100-count
            noprize.couponId = ""
            noprize.drawId = ""
            arr.push(noprize)
            this.setState({prize:arr})
          }
        })
        .catch(Hmall.catchHttpError(() =>{alert(你好)}));
  }
  constructor(props) {
    super(props);
    this.state = {
      prize: [],
      probability:0,
      flag: true,
    }
  }

  beginPrize(){
    let r  = Math.floor(Math.random()*100) , { prize } = this.state ,count = 0 , probability = 3600 , prize_num = -1
    prize.forEach((p,i)=>{
      if(count<=r&&r<count+p.awardProbility){
        probability = probability - 90*i

        setTimeout(()=>{
          alert("恭喜你获得"+p.couponName)
        }, 500, this);
        prize_num = i
      }
      count = count + p.awardProbility
    })
    this.setState({probability:probability,flag:false})
    if(prize_num>=0){
      fetch(`${droolsService}/h/sale/draw/addAwardRecord`, {
        method: "POST",
        headers: Hmall.getHeader({
          "Content-Type": "application/json"
        }),
        body: JSON.stringify({
          "convertData":[{"userId":Hmall.getCookie('userid'),"name":Hmall.getCookie('username'),"mobileNumber":Hmall.getCookie('mobile')}],
          "couponId": prize[prize_num].couponId,
          "couponName": prize[prize_num].couponName,
          "type":"COUPON_TYPE_03",
          "drawId": prize[0].drawId
        })
      })
          .then(Hmall.convertResponse('json',this))
          .then(json=> {
            let { success , resp } = json
            if(success){
            }
          })
          .catch(Hmall.catchHttpError(() => this.setState({})));
    }




  }

  render(){
    let { prize , probability , flag } = this.state, count = 0 ,
        prizes =  prize.map((p,i)=>{
          return <li key={i}>{p.couponName}</li>
        }),
        circle_style={transform:`rotate(${probability}deg)`}


    return (
        <div className="lottery">
          <div className="square">
            <div className="circle" style={circle_style}>
              <ul>
                {prizes}
              </ul>
            </div>
          </div>
          <Button width={80} height={40} className="red" text="开始抽奖" disabled={!flag} style={{marginRight:"120px"}} onClick={()=>{this.beginPrize()}}/>
          <Button width={80} height={40} className="red" text="重置抽奖"  onClick={()=>{ this.setState({probability:0,flag:true})}}/>
        </div>
    )
  }
}