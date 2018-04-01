const path = require('path'),
  webpack = require( 'webpack' ),
  HtmlWebpackPlugin = require('html-webpack-plugin'),
  CleanWebpackPlugin = require('clean-webpack-plugin'),
  config = require( './webpack.base.config' ),
  apis = require('./api-config'),
  src_dir =  'src',
  webapp_dir = path.join(src_dir,'main','webapp'),
  desktop_dir = path.join(src_dir,'desktop'),
  cms = process.env.NODE_ENV == 'preview' ? `<script>document.write('<s'+'cript src="${apis.cmsConfig}?t='+Date.now()+'"></s'+'cript>')</script>` : `<script src="${apis.cmsConfig}"></script>`,
  piwik = apis.piwikServer ? `<!-- Piwik -->
    <script type="text/javascript">
      var _paq = _paq || [];
      /* tracker methods like "setCustomDimension" should be called before "trackPageView" */
      _paq.push(['trackPageView']);
      _paq.push(['enableLinkTracking']);
      (function() {
        var u="${apis.piwikServer}";
        _paq.push(['setTrackerUrl', u+'piwik.php']);
        _paq.push(['setSiteId', '1']);
        var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
        g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
      })();
    </script>
    <!-- End Piwik Code -->` : '';
delete apis.cmsConfig;
delete apis.piwikServer;
var api=`<script>${Object.keys(apis).map(key => `${key}='${apis[key]}'`).join(';')}</script>`;

config.plugins.push(
  new webpack.BannerPlugin({banner: '/*! Copyright Hand China Co.,Ltd. */', raw: true, entryOnly: true}),
  new CleanWebpackPlugin(['build'], {
    root: path.resolve(webapp_dir),
    verbose: true,
    dry: false
    //exclude: ["dist/1.chunk.js"]
  }),
  new webpack.optimize.UglifyJsPlugin({
    compress: {
      warnings: false
    }
  }),
  new webpack.LoaderOptionsPlugin({
    minimize: true
  }),
  new webpack.DefinePlugin({
    'process.env': {
      'NODE_ENV': '"production"'
    }
  }),
  new HtmlWebpackPlugin({
    favicon: path.resolve(webapp_dir,'favicon.ico'),
    template: path.resolve(desktop_dir,'index.tplt.html'),
    title: '优衣库官方旗舰店-优衣库官方网络旗舰店',
    minify: {
      removeComments: true,
      collapseWhitespace: true
    },
    injection: {
      api,
      cms,
      piwik
    }
  })
);

module.exports = config;