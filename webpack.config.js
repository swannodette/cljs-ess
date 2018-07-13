var HtmlWebpackPlugin = require('html-webpack-plugin')
module.exports = {
    entry: './src/js/index.js',
    output: {
      filename: 'index_bundle.js'
    },
    module: {
      rules:[
          {
              test:/\.css$/,
              use:['style-loader', 'css-loader']
          }
     ]
  }
}