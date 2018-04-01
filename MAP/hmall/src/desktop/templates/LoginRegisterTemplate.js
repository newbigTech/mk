import React from 'react';
import { Row, Col } from '../components/Layout';

const LoginRegisterTemplate = (props) => {
  return <Row className="content">
    <Col width={904}>
      <img src={Hmall.cdnPath(CMSConfig.loginRegisterBanner.urlPath)}></img>
    </Col>
    <Col>
      {props.main}
    </Col>
  </Row>;
};

export default LoginRegisterTemplate;