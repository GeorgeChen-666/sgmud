(async function() {
  const { Scene_Basketball } = await import('./rpg_scenes/Scene_Basketball.js');
  window.Scene_Basketball = Scene_Basketball;

  class Basketball {
    static start() {
      const {
        $gamePlayer: {_x: x, _y: y, _direction: direction},
        $gameMap:{
          _mapId: mapId,
          _interpreter:itpr,
        }
      } = window;
      this._locHistory = { x, y, direction, mapId,};
      itpr.insertCommands([
        {"code":201,"indent":0,"parameters":[0,2,-1,-1,0,0]},
        {"code":101,"indent":0,"parameters":["",0,1,0]},
        {"code":401,"indent":0,"parameters":[""]},
        {"code":401,"indent":0,"parameters":[""]},
        {"code":401,"indent":0,"parameters":["                      ==投篮游戏=="]},
        {"code":102,"indent":0,"parameters":[["开始游戏","游戏说明","退出游戏"],2,0,1,0]},
        {"code":402,"indent":0,"parameters":[0,"开始游戏"]},
        {"code":0,"indent":1,"parameters":[]},
        {"code":402,"indent":0,"parameters":[1,"游戏说明"]},
        {"code":105,"indent":1,"parameters":[2,false]},
        {"code":405,"indent":1,"parameters":["这是一段很长很长的说明啊啊啊啊啊啊啊啊啊"]},
        {"code":0,"indent":1,"parameters":[]},
        {"code":402,"indent":0,"parameters":[2,"退出游戏"]},
        {"code":355,"indent":1,"parameters":["this.Basketball.exit()"]},
        {"code":0,"indent":1,"parameters":[]},
        {"code":404,"indent":0,"parameters":[]},
        {"code":0,"indent":0,"parameters":[]}
        ]);
    }
    static exit() {
      const {
        $gameMap:{
          _interpreter:itpr,
        }
      } = window;
      const { x, y, direction, mapId,} = this._locHistory;
      itpr.insertCommands([{"code":201,"indent":0,"parameters":[0,mapId,x,y,direction,0]}]);
    }
  }
  window.Game_Interpreter.prototype.Basketball = Basketball;
})();