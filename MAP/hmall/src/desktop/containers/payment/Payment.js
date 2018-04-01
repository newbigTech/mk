/**
 * Created by ZWL on 2016/10/22.
 */
import React, { Component } from 'react';

export default class Payment extends Component{
  render() {
    return (
      <section>
        {this.props.children}
      </section>
    );
  }
}
