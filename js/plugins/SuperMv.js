(function () {
  // 加载
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

  ImageManager.loadNormalBitmap = function(path, hue) {
    var key = this._generateCacheKey(path, hue);
    var bitmap = this._imageCache.get(key);
    if (!bitmap) {
      bitmap = Bitmap.load(decodeURIComponent(path));
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