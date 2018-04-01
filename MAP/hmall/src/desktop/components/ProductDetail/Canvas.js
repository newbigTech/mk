import React, { Component, PropTypes } from 'react'
import { Initialize, LoadPhotos, MouseDown, MouseMove, MouseUp, MouseWheel } from './Core'

export default class Canvas extends Component {

  componentDidMount() {
    {
      window.bgR = 255;
      window.bgG = 255;
      window.bgB = 255;
      window.fps = 24;
      window.co = 0;
      window.dh = 0;
      window.bk = 0;
      window.aA = 0;
      window.aw = 0;
      Initialize();
      LoadPhotos(this.props.url, 1, 72, 1903, 925);
    }
  }

  handleMouseDown(e) {
    MouseDown(e);
    document.addEventListener('mousemove', MouseMove, false);
    document.addEventListener('mouseup', this.handleMouseUp, false);
  }
  
  handleMouseUp(e) {
    MouseUp(e);
    document.removeEventListener('mousemove', MouseMove, false);
    document.removeEventListener('mouseup', this.handleMouseUp, false);
  }
  
  render() {
    return (
    <div className={this.props.className}>
      <canvas id="CanvasBG">非常抱歉！您的浏览器不支持Html5体,请使用其它浏览器尝试。</canvas>
      <canvas id="CanvasUI" onMouseDown={e => this.handleMouseDown(e)} onWheel={e => MouseWheel(e)}> </canvas>
    </div>
  )
  }

}