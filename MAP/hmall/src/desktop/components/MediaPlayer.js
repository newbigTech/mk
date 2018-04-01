import React, { PropTypes } from 'react';

const MediaPlayer = (props) => {
  const { urlPath, width, height, autoplay } = this.props;
  return <div className="h-component h-video" style={{ width, height }}>
    <video width="100%" height="100%" preload="none" autoPlay={autoplay == 'Y'} controls="controls" poster="">
      <source src={urlPath} type="video/mp4"></source>
    </video>
  </div>;
};

MediaPlayer.propTypes = {
  urlPath: PropTypes.string,
  autoplay: PropTypes.string,
  width: PropTypes.number,
  height: PropTypes.number
};

export default MediaPlayer;