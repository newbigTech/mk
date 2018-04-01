import React, { Component, PropTypes } from 'react';
import { SimpleTextBox } from './TextBox';

export default class Password extends SimpleTextBox{
  get type() {
    return 'password';
  }
  
  fixPasswordBug() {
    return <input style={{visibility: "hidden", position: 'absolute', left: -10000}}/>
  }
}