const webpack = require('webpack');
const path = require('path');

const fs = require('fs')

const HtmlWebpackPlugin = require('html-webpack-plugin')
const { VueLoaderPlugin } = require('vue-loader')
const UglifyJSPlugin = require('uglifyjs-webpack-plugin')

const vueLoaderConfig = {
}

let localConfig = {}

if (fs.existsSync(path.join(__dirname, 'webpack.local.js'))) {
  localConfig = require('./webpack.local.js')
}

module.exports = {

  devServer: {
    proxy: {
      '/tour': {
        target: 'http://localhost:8822/'
      },
      ...(localConfig.devServer || {})
    }
  },

  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'vue-loader',
        options: vueLoaderConfig
      },
      {
        include: [path.resolve(__dirname, 'src')],
        loader: 'babel-loader',

        options: {
          plugins: ['syntax-dynamic-import'],

          presets: [
            [
              '@babel/preset-env',
              {
                modules: false
              }
            ]
          ]
        },

        test: /\.js$/
      },
      {
        test: /\.(scss|css)$/,

        use: [
          {
            loader: 'style-loader'
          },
          {
            loader: 'css-loader'
          },
          {
            loader: 'sass-loader'
          }
        ]
      },
      {
        test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: 'img/[name].[ext]'
        }
      },
      {
        test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 10000,
          name: 'media/[name].[ext]'
        }
      },
      {
        test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
        loader: 'url-loader',
        options: {
          limit: 80000,
          name: 'fonts/[name].[ext]'
        }
      },
    ]
  },

  entry: './src',

  output: {
    library: 'Tour',
    libraryExport: 'default',
    filename: 'tour.js'
  },

  mode: 'development',

  resolve: {
    extensions: ['.js', '.vue', '.json'],
    alias: {
      'vue$': 'vue/dist/vue.esm.js'
    }
  },

  plugins: [
    new HtmlWebpackPlugin({
      title: 'tour-ui',
      filename: 'index.html',
      template: path.resolve(__dirname, './index.html')
    }),
    new VueLoaderPlugin(),
    new webpack.ProvidePlugin({
      '$': 'jquery'
    })
  ],

  optimization: {
    splitChunks: {
      cacheGroups: {
        vendors: {
          priority: -10,
          test: /[\\/]node_modules[\\/]/
        }
      },

      chunks: 'async',
      minChunks: 1,
      minSize: 30000,
      name: true
    }
  }
};
