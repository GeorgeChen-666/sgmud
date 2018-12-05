(function () {
  // 加载JS模块
  PluginManager.loadModuleScript = function(name) {
    var url = this._path + name;
    var script = document.createElement('script');
    script.type = 'module';
    script.src = url;
    script.async = false;
    script.onerror = this.onError.bind(this);
    script._url = url;
    document.body.appendChild(script);
  };
  /**
   * 扩展解释器，在解析器当前执行的命令后面插入命令
   */
  Game_Interpreter.prototype.insertCommands = function(jsonCmds) {
    var listCmds = this._list;// 命令列表
    var listCount = listCmds.length;// 这个事件队列中一共多少条命令
    var cuCmdIndex = this._index;// 当前执行的是第几条命令
    var cuCmdIndent = this._indent;// 当前代码的缩进级别
    jsonCmds.forEach(function(e) {// 将参数命令的indent拼接到当前的indent级别下，参数的indent一定要从0开始。
      e.indent += cuCmdIndent;
    });
    this._list = listCmds.slice(0,cuCmdIndex + 1)// 在当前执行的事件后面插入
      .concat(jsonCmds)
      .concat(listCmds.slice(cuCmdIndex + 1,listCount));
  }

  ImageManager.loadBase64Bitmap = function(path, data, hue) {
    var key = this._generateCacheKey(path, hue);
    var bitmap = this._imageCache.get(key);
    bitmap = undefined;
    if (!bitmap) {
      bitmap = Object.create(Bitmap.prototype);
      bitmap._defer = true;
      bitmap.initialize();
      bitmap._decodeAfterRequest = true;
      bitmap._image = new Image();
      bitmap._image.src = data;
      bitmap.addLoadListener(function() {
        bitmap.rotateHue(hue);
      });
      this._imageCache.add(key, bitmap);
    }else if(!bitmap.isReady()){
      bitmap.decode();
    }
    return bitmap;
  };
})();