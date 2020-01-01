/**
 * 查看NPC的窗口
 */
function Window_NpcInfo() {
  this.initialize.apply(this, arguments);
}
Window_NpcInfo.prototype = Object.create(Window_Base.prototype);
Window_NpcInfo.prototype.constructor = Window_NpcInfo;
Window_NpcInfo.prototype.initialize = function() {
  var width = Graphics.boxWidth;
  var height = Graphics.boxHeight;
  Window_Base.prototype.initialize.call(this, 0, 0, width, height);
  var dEvent = $gameMap.getCurrentGameEvent().event();
  var dActor = $dataActors[dEvent.meta.actorid];
  this.drawText(dEvent.name, 0, 0, this.contentsWidth(),'center');
  this.drawHorzLine(this.lineHeight() * 1);
  var hiw = this.lineHeight()*5
  this.drawText(dEvent.name+'看起来约'+parseInt(dActor.meta.年龄/10)+'0多岁', hiw, this.lineHeight()*2, this.contentsWidth());
  this.drawText('生得一脸稚气', hiw, this.lineHeight()*3, this.contentsWidth());
  this.drawText('武艺看起来不堪一击', hiw, this.lineHeight()*4, this.contentsWidth());
  this.drawText('出手似乎很轻', hiw, this.lineHeight()*5, this.contentsWidth());
  this.drawFace(dActor.faceName,dActor.faceIndex,0,this.lineHeight() * 2.5,Window_Base._faceWidth, Window_Base._faceHeight);
  this.drawHorzLine(this.lineHeight() * 7);
  this.drawText('带着：', this.lineHeight() * 1, this.lineHeight()*8, this.contentsWidth());
  this.drawHorzLine(this.lineHeight() * 9);
  this.drawTextEx(dActor.profile,this.lineHeight() * 1,this.lineHeight() * 10);
  
};

/**
 * 查看npc
 */
function Scene_NpcInfo() {
  this.initialize.apply(this, arguments);
}
Scene_NpcInfo.prototype = Object.create(Scene_Base.prototype);
Scene_NpcInfo.prototype.constructor = Scene_NpcInfo;
Scene_NpcInfo.prototype.initialize = function() {
  Scene_Base.prototype.initialize.call(this);
};
Scene_NpcInfo.prototype.create = function() {
  Scene_Base.prototype.create.call(this);
  this.createBackground();
  this.createWindowLayer();
  this.createSceneWindow();
};
Scene_NpcInfo.prototype.createSceneWindow = function() {
  //先获取请教的actor技能列表还有等级列表
  this._win = new Window_NpcInfo();
  // this._win.setHandler('kunfu1',  function(){
  //     //this._win_cmd.index是选择的序号
  //     //然后根据选择的序号获取技能
  //     //下面就复写vx里的请教代码就好了。
  //     //请教的进度条就调用Scene_Progess
  //     SceneManager.goto(Scene_Map);
  // }.bind(this));
  this._win.x = Graphics.boxWidth - this._win.width;
  this._win.y = (Graphics.boxHeight - this._win.height) / 2;
  this.addWindow(this._win);
};
Scene_NpcInfo.prototype.update = function() {
  if(Input.isTriggered('escape') || TouchInput.isCancelled()) {
      this._win.close();
      SoundManager.playCancel()
      SceneManager.goto(Scene_Map);
  }
  Scene_Base.prototype.update.call(this);
};