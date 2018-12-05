(async function() {
  const { Scene_Basketball } = await import('./rpg_scenes/Scene_Basketball.js');
  window.Scene_Basketball = Scene_Basketball;
})();