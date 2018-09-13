# RetrofitMoudel
使用Rxjava retrofit 多线程下载一个文件，可带进度回调

之前看到很多demo都是使用Okhttp的拦截器addInterceptor(new JsDownloadInterceptor(listener))，在其中做回调，
但是返回的还是 .map(new Function<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody apply(ResponseBody body) throws Exception {
                        return body;
                    }
                })
                通过map 返回的还是body或者是InputStream；inputStream也就是在body之中的，所以我觉得没有必要加入一个拦截器，
                可以直接在我们写文件的时候进行回调
               while ((len = inputString.read(b)) != -1) {
                raf.write(b, 0, len);
                total += len;
//                mListener.onProgress((int) (total * 100 / length));//回调监听
                Log.w("download--->", "total-->" + total + "--progress-->" + (int) (total * 100 / end) + "--length-->" + end);
            }  。
            
            
上述内容比较适合在单线程中的回调；而在多线程中，我们只需要加入一个数据库，随时保存下载内容的长度，并且定时获取下载的长度占总长度的百分比就行。就可以实现
界面的UI显示了。
