/**
 * @requires Override.js
 * @requires Hooks.js
 * @requires ScriptCommands.js
 * @requires FolderPluginLoader.js
 */
(() => {
  const basePath = "Basketball/";
  window._gbb = {
    basePath
  };
  DataManager.setMapCache = function(mapId, data) {
    DataManager._mapCache[mapId] = data;
  };
  if (typeof StateMachine === "undefined") {
    PluginManager.loadScript("js/libs/state-machine.js", basePath);
  }
  PluginManager.loadScript("js/rpg_windows/Window_PowerBar.js", basePath);
  PluginManager.loadScript("js/rpg_windows/Window_Text.js", basePath);
  PluginManager.loadScript("js/rpg_managers/BasketballManager.js", basePath);
})();
