(() => {
  Scene_Boot.Totos = [];
  const Scene_Boot_start = Scene_Boot.prototype.start;
  Scene_Boot.prototype.start = function() {
    Scene_Boot.Totos.forEach(todo => {
      todo.call(this);
    });
    Scene_Boot_start.call(this);
  };
})();
