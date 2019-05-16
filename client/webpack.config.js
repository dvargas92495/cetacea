var webpack = require('webpack');
var path = require('path');

var BUILD_DIR = path.resolve(__dirname, 'public');
var APP_DIR = path.resolve(__dirname, 'src');

var config = {
  entry: APP_DIR + '/index.jsx',
  output: {
    path: BUILD_DIR,
    filename: 'bundle.js'
  },
  module : {
    rules : [
      {
        test : /\.jsx?/,
        include : APP_DIR,
        loader : 'babel-loader',
        query : {
          presets: ['@babel/react']
        }
      },
      {
        test: /\.(png|jpg|gif)$/,
        use: [
          {
            loader: 'file-loader',
            options: {}
          }
        ]
      }
    ]
  },
  devtool: '#eval-source-map',
  plugins: [
    // ...
    function()
    {
      this.plugin("done", function(stats)
      {
        if (stats.compilation.errors && stats.compilation.errors.length && process.argv.indexOf('--watch') == -1)
        {
          console.log(stats.compilation.errors);
          throw new Error('webpack build failed.');
        }
        // ...
      });
    }
    // ...
  ]
};

module.exports = config;
