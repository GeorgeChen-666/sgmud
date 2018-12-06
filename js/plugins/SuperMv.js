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

  SceneManager.backgroundBitmapNoBlur = function() {
    return this._backgroundBitmapNoBlur;
  };
  SceneManager.snapForBackground = function() {
    this._backgroundBitmap = this.snap();
    this._backgroundBitmapNoBlur = this.snap();
    this._backgroundBitmap.blur();
  };

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

  DataManager.setMapCache = function (mapId, data) {
    DataManager._mapCache[mapId] = data;
  };
  /**
   * 更强大的地图加载器，支持缓存。
   * @param mapId 地图ID
   * @param isRefresh 是否刷新缓存
   * @param nameTempl 地图文件名模版（支持通过../读取其它文件夹下的地图数据，base：data/）
   */
  DataManager.loadMapData = function(mapId, isRefresh=false, nameTempl = 'Map%1.json') {
    DataManager._mapCache = (DataManager._mapCache || {});
    if (mapId > 0) {
      var cachedMap = DataManager._mapCache[mapId];
      if(cachedMap && !isRefresh) {
        $dataMap = cachedMap;
      } else {
        var filename = nameTempl.format(mapId.padZero(3));
        this._mapLoader = ResourceHandler.createLoader('data/' + filename, this.loadDataFile.bind(this, '$dataMap', filename));
        this.loadDataFile('$dataMap', filename);
        setTimeout(function () {
          DataManager.setMapCache(mapId, $dataMap);
        },500);
      }
    } else {
      this.makeEmptyMap();
    }
  };
})();