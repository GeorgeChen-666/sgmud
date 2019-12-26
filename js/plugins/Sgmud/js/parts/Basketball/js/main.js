(() => {
  const basePath = 'Sgmud/js/parts/Basketball/';
  window._gbb = {
    basePath
  };
  if (typeof StateMachine === 'undefined') {
    PluginManager.loadScript('js/libs/state-machine.js', basePath);
  }
  PluginManager.loadScript('js/rpg_windows/Window_PowerBar.js', basePath);
  PluginManager.loadScript('js/rpg_windows/Window_Text.js', basePath);
  PluginManager.loadScript('js/rpg_managers/BasketballManager.js', basePath);
})();
