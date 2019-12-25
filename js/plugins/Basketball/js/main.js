(() => {
  window._gbb = {};
  if (typeof StateMachine === 'undefined') {
    PluginManager.loadScript('state-machine.js', 'Basketball/js/libs/');
  }
  PluginManager.loadScript('Window_PowerBar.js', 'Basketball/js/rpg_windows/');
  PluginManager.loadScript('Window_Text.js', 'Basketball/js/rpg_windows/');
  PluginManager.loadScript(
    'BasketballManager.js',
    'Basketball/js/rpg_managers/'
  );
})();
