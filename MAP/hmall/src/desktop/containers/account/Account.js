import React, { Component } from 'react';
import { browserHistory } from 'react-router';
//import BreadCrumbs from '../../components/BreadCrumbs';
import { Panel, Row, Col } from '../../components/Layout';
import PeopleCenter from '../../components/PeopleCenter';


export default class Account extends Component{
  
  render() {
    let { routes } = this.props,
      { title, showTitle=true } = routes[routes.length - 1];
    return (
      <Row id="account">
        <Col width={244}>
          <PeopleCenter></PeopleCenter>
        </Col>
        <Col>
          <Panel className="account-panel">
            {
              showTitle && <div className="title">{title}</div>
            }
            <div className="body">
              {this.props.children}
            </div>
          </Panel>
        </Col>
      </Row>
    );
  }
}
