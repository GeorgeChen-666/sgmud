/**
 * @requires Override.js
 * @requires Hooks.js
 * @requires ScriptCommands.js
 * @requires FolderPluginLoader.js
 */
(() => {
  const basePath = "Sgmud/js/parts/";
  PluginManager.loadScript("KunFuActorAttr.js", basePath);
  PluginManager.loadScript("KunFuMapMenu.js", basePath);
  PluginManager.loadScript("KunFuNpcMenu.js", basePath);
  PluginManager.loadScript("KunFuNpcInfo.js", basePath);
})();
