const path = require('path'),
  webpack = require('webpack'),
  ExtractTextPlugin = require('extract-text-webpack-plugin'),
  WebpackChunkHash = require("webpack-chunk-hash"),
  src_dir = 'src',
  webapp_dir = path.join(src_dir,'main','webapp'),
  desktop_dir = path.join(src_dir,'desktop'),
  extractLess = new ExtractTextPlugin({
    filename: 'build/[name].hmall.css'
  });

const config = {
    entry: {
      desktop: path.resolve(src_dir, 'desktop','index.js'),
      polyfill: ['babel-polyfill','classlist-polyfill','webstorage-polyfill','whatwg-fetch'],
      react: ['react','react-dom','react-router'],
      moment: ['moment'],
      vendor: ['react-cookie', 'react-datetime','react-dropzone','react-onclickoutside','cryptojs','binstring']
    },
    output: {
      publicPath: '/',
      path: path.resolve(__dirname, webapp_dir),
      filename: 'build/[name].[chunkhash].bundle.js',
      chunkFilename: 'build/[name].[chunkhash].chunk.js'
    },
    plugins:[
       new webpack.HashedModuleIdsPlugin(),
       new WebpackChunkHash(),
       extractLess,
       new webpack.ContextReplacementPlugin(/moment[\/\\]locale$/, /zh-cn/),
       new webpack.optimize.CommonsChunkPlugin({
         name: ["polyfill","vendor","react","moment"],
         minChunks: 4
       }),
       new webpack.ProvidePlugin({
         Hmall: path.resolve(desktop_dir, 'util.js'),
         HmallConfig: path.resolve(desktop_dir, 'config.js')
       })
    ],
    module: {
      rules: [{
        test: /\.js$/,
        loader: 'babel-loader',
        options: {
          presets: ['react',  ['es2015', {modules: false}] , 'stage-2'],
          plugins: ['syntax-dynamic-import']
        },
        include: path.resolve(desktop_dir)
      }, {
        test: /\.(less|css)$/,
        use: extractLess.extract({
         fallback: "style-loader",
         use: [{
           loader: "css-loader",
           options: {
             discardComments: { removeAll: true }
           }
         }, "less-loader"]
       })
      }, {
        test: /\.(png|jpg|gif)$/,
        loader: 'url-loader',
        options: {
          limit: 8192,
          name: 'build/images/[hash].[ext]'
        }
      }]
    }
  };

module.exports = config;
