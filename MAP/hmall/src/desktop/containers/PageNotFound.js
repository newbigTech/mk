import React from 'react';
import { Link } from 'react-router';
import { Panel, Row, Col } from '../components/Layout';
import Icon from '../components/Icon';
import Banner from '../components/Banner';

const PageNotFound = () => {
  const { pageErrorBanner={
    urlPath: '/images/error_message_banner.png',
    link: '/'
  }} = CMSConfig;
  return <Panel id="page-404">
    <Row>
      <Col width={626}>
        <Banner urlPath={pageErrorBanner.urlPath} link={pageErrorBanner.link}></Banner>
      </Col>
      <Col className="page-right">
        <div className="page-404-right">
          <div>
            <Icon name="page-not-found"></Icon>
          </div>
          <div>
            <h1>抱歉！您访问的页面出错啦...</h1>
          </div>
          <div>
            <ul>
              <li onClick={() => { location.reload()}}><Icon name="refresh-page" title="刷新页面"></Icon><span>刷新页面</span></li>
              <li><Link to="/"><Icon name="return-homePage" title="返回首页"></Icon><span>返回首页</span></Link></li>
            </ul>
          </div>
        </div>
      </Col>
    </Row>
  </Panel>;
};

export default PageNotFound;