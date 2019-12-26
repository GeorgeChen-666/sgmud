/**
 * 支持加载任意文件夹的js
 */
PluginManager.loadScript = function(name, path = '', basepath = this._path) {
  const url = basepath + path + name;
  const script = document.createElement('script');
  script.type = 'text/javascript';
  script.src = url;
  script.async = false;
  script.onerror = this.onError.bind(this);
  script._url = url;
  document.body.appendChild(script);
};
PluginManager.loadScript('main.js', 'Sgmud/js/');
