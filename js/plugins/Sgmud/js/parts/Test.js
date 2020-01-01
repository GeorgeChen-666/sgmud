
PluginManager.regHook('Game_Event.prototype.list', oFunc => function() {
  const dataEvent = this.event();
  if(!!dataEvent.meta.actorId) {
    return [
      {"code":355,"indent":0,"parameters":[function() {
        this.showTalkWindow();
      }]},
      ...oFunc()
    ];
  } else {
    return oFunc();
  }
});
Game_Map.prototype.getCurrentGameEvent = function(){
  //$gameMap._interpreter._eventId
  return this.event(this._interpreter._eventId);
}
Game_Interpreter.prototype.showTalkWindow = function () {
  const indent = this._indent;
  var dEvent = $gameMap.getCurrentGameEvent().event();
  var dActor = $dataActors[dEvent.meta.actorId];
  var options = ['交谈','查看','战斗','切磋'];
  const eops = dEvent.meta.eOptions || '';
  const nhops = dEvent.meta.nOptions || '';
  eops.split(',').forEach(function(op) {
      if(op!=''){
          options.push(op);
      }
  }, this);

  var tvs = [];//rt._list
  tvs = tvs.concat([
      {"code":102,"indent":indent,"parameters":[options,-2,0,2,0]}
  ])
  for(var i=0; i<options.length; i++) {
      var txtOpt = options[i];
      if(nhops.split(',').contains(txtOpt)) {
          continue;
      }
      var cmds = [
          {"code":402,"indent":indent,"parameters":[i, txtOpt]}
      ]
      if(txtOpt == '交谈') {
          cmds = cmds.concat(this.getActorTalkRandomCommand());
      }
      else if(txtOpt == '查看') {
          cmds = cmds.concat([
            {"code": 230,"indent": indent+1,"parameters": [8]},
            {"code":355,"indent":indent+1,"parameters":[function(){
                SceneManager.goto(Scene_NpcInfo);
            }]}
          ]);
      }
      else if(txtOpt == '战斗'||txtOpt == '切磋') {

      }
      else if(txtOpt == '请教') {
          var tempskills=[];
          var optskills = [];
          for(var j=1;j<=14;j++) {
              var skcode = dActor.meta['武功'+j];
              var sklevel = dActor.meta['等级'+j];
              if(skcode) {
                  tempskills.push({
                      data:$dataSkills[skcode],
                      level:sklevel
                  });
                  optskills.push($dataSkills[skcode].name+' x '+sklevel);
              }
          }
          cmds = cmds.concat([
              {"code":101,"indent":indent+1,"parameters":[dActor.faceName,dActor.faceIndex,0,0]},
              {"code":401,"indent":indent+1,"parameters":['【'+dEvent.name+'】']},//空一行
              {"code":401,"indent":indent+1,"parameters":["你想学什么就说吧！"]},
              {"code":102,"indent":indent+1,"parameters":[optskills,-2,0,2,0]},
          ]);
          for(var j=0;j<tempskills.length;j++) {
              var tmps = tempskills[j];
              cmds = cmds.concat([
                  {"code":402,"indent":indent+1,"parameters":[j,tmps.data.name]},
                  {"code":355,"indent":indent+2,"parameters":[function(){
                      var tmps = this._params[1];
                      showLearnProgess(this,tmps.data,tmps.level,indent+2); 
                  },tmps]},//tmps要按参数传，不能直接调用，要么回调函数调用时值全都是循环结束以后的tmps值。
                  {"code":0,"indent":indent+2,"parameters":[]},
              ]);
          }
          cmds = cmds.concat([
              {"code":404,"indent":indent+1,"parameters":[]},
              {"code":0,"indent":indent+1,"parameters":[]}
          ]);
      }
      tvs = tvs.concat(cmds);
  }
  tvs = tvs.concat([
      {"code":403,"indent":indent,"parameters":[6,null]},
      {"code":0,"indent":indent+1,"parameters":[]},
      {"code":404,"indent":indent,"parameters":[]},
      {"code":355,"indent":indent,"parameters":[function(){
          //将选择结果序号赋值给变量1
          $gameVariables.setValue(1,this._branch[this._indent]);
      }]},
      {"code":0,"indent":indent,"parameters":[]}
  ])
  this.insertCommands(tvs)
}
/**
 * 处理随机对话
 */
Game_Interpreter.prototype.getActorTalkRandomCommand = function () {
  var dEvent = $gameMap.getCurrentGameEvent().event();
  var dActor = $dataActors[dEvent.meta.actorId];
  var strTalkTxt = [
      '我什么也不知道，就算知道也不说，\n打死你我也不说。',
      '你没看到我正在忙吗，你还是找别人CHAT去吧。',
      dEvent.name + '看了你一眼，转身又忙自己的事情去了。',
      '今天的天气还真是。。哈哈哈哈哈。',
      dEvent.name + '睁大眼睛看着你，\n显然不知道你正在说什么。'
  ][Math.randomInt(5)];
  return this.getActorTalkCommand(strTalkTxt);
}
Game_Interpreter.prototype.getActorTalkCommand = function(txt) {
  var dEvent = $gameMap.getCurrentGameEvent().event();
  var dActor = $dataActors[dEvent.meta.actorId];
  var cmds=[];
  cmds = cmds.concat([
      {"code":101,"indent":this._indent,"parameters":[dActor.faceName,dActor.faceIndex,0,0]},
      {"code":401,"indent":this._indent,"parameters":['【'+dEvent.name+'】']},//空一行
      {"code":401,"indent":this._indent,"parameters":[txt]},
      {"code":0,"indent":this._indent,"parameters":[]},
  ]);
  return cmds;
}