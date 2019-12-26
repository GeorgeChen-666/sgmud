(function() {
  /**
   * 直接加载base64资源
   */
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
    } else if (!bitmap.isReady()) {
      bitmap.decode();
    }
    return bitmap;
  };

  /**
   * 更强大的地图加载器，支持缓存。
   * @param mapId 地图ID
   * @param isRefresh 是否刷新缓存
   * @param nameTempl 地图文件名模版（支持通过../读取其它文件夹下的地图数据，base：data/）
   * @param basepath 路径
   */
  DataManager.loadMapData = function(
    mapId,
    isRefresh = false,
    nameTempl = 'Map%1.json',
    basepath = 'data/'
  ) {
    DataManager._mapCache = DataManager._mapCache || {};
    if (mapId > 0) {
      var cachedMap = DataManager._mapCache[mapId];
      if (cachedMap && !isRefresh) {
        $dataMap = cachedMap;
      } else {
        var filename = nameTempl.format(mapId.padZero(3));
        this._mapLoader = ResourceHandler.createLoader(
          basepath + filename,
          this.loadDataFile.bind(this, '$dataMap', filename)
        );
        this.loadDataFile('$dataMap', filename, basepath).then(() => {
          DataManager.setMapCache(mapId, $dataMap);
        });
      }
    } else {
      this.makeEmptyMap();
    }
  };
  DataManager.loadDataFile = function(name, src, basepath = 'data/') {
    return new Promise((resolve, reject) => {
      var xhr = new XMLHttpRequest();
      var url = basepath + src;
      xhr.open('GET', url);
      xhr.overrideMimeType('application/json');
      xhr.onload = function() {
        if (xhr.status < 400) {
          window[name] = JSON.parse(xhr.responseText);
          DataManager.onLoad(window[name]);
          resolve(window[name]);
        }
      };
      xhr.onerror =
        this._mapLoader ||
        function() {
          DataManager._errorUrl = DataManager._errorUrl || url;
          reject(DataManager._errorUrl);
        };
      window[name] = null;
      xhr.send();
    });
  };
  DataManager.setMapCache = function(mapId, data) {
    DataManager._mapCache[mapId] = data;
  };
})();
