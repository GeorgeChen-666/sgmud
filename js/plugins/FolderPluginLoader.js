/*:
 * @plugindesc 加载文件夹内的插件,入口文件js/main.js.
 * @author GeorgeChen
 *
 * @param plugin1
 * @desc forder name
 * @param plugin2
 * @param plugin3
 * @param plugin4
 * @param plugin5
 * @param plugin6
 * @param plugin7
 * @param plugin8
 * @param plugin9
 * @param plugin10
 * @param plugin11
 * @param plugin12
 * @param plugin13
 * @param plugin14
 * @param plugin15
 * @param plugin16
 * @param plugin17
 * @param plugin18
 * @param plugin19
 * @param plugin20
 * @param plugin21
 * @param plugin22
 * @param plugin23
 * @param plugin24
 * @param plugin25
 * @param plugin26
 * @param plugin27
 * @param plugin28
 * @param plugin29
 * @param plugin30
 * @param plugin31
 * @param plugin32
 * @param plugin33
 * @param plugin34
 * @param plugin35
 *
 */
/**
 * 支持加载任意文件夹的js
 */
PluginManager.loadScript = function(name, path = "", basepath = this._path) {
  const url = basepath + path + name;
  const script = document.createElement("script");
  script.type = "text/javascript";
  script.src = url;
  script.async = false;
  script.onerror = this.onError.bind(this);
  script._url = url;
  document.body.appendChild(script);
};

(() => {
  let parameters = PluginManager.parameters("FolderPluginLoader");

  for (let i = 1; i < 100; i++) {
    const folderName = parameters[`plugin${i}`];
    if (!!folderName) {
      PluginManager.loadScript("main.js", `${folderName}/js/`);
    }
  }
})();
