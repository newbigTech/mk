const path = require('path'),
  webpack = require( 'webpack' ),
  config = require( './webpack.base.config' ),
  HtmlWebpackPlugin = require('html-webpack-plugin'),
  apis = require('./api-config'),
  src_dir =  'src',
  webapp_dir = path.join(src_dir,'main','webapp'),
  desktop_dir = path.join(src_dir,'desktop'),
  cms=`<script src="${apis.cmsConfig}"></script>`;
var api=`<script>${Object.keys(apis).map(key => `${key}='${apis[key]}'`).join(';')}</script>`;

config.devtool = '#source-map';
config.watch = true;
config.output.filename='build/[name].[hash].bundle.js';
config.plugins.push(
  new webpack.DefinePlugin({
    'process.env': {
      'NODE_ENV': '"development"'
    }
  }),
  new webpack.HotModuleReplacementPlugin(),
  new webpack.NoEmitOnErrorsPlugin(),
  new HtmlWebpackPlugin({
    favicon: path.resolve(webapp_dir,'favicon.ico'),
    template: path.resolve(desktop_dir,'index.tplt.html'),
    title: 'Hmall产品',
    injection: {
      api,
      cms
    }
  })
);
config.devServer={
  port: '9090',
  contentBase: webapp_dir, 
  historyApiFallback: {
    rewrites: [
      { from: /^.*\.html$/, to: '/' }
    ]
  },
  inline: true
}  
module.exports = config;