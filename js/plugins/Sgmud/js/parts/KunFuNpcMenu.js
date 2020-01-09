PluginManager.regHook(
  'Game_Event.prototype.list',
  oFunc =>
    function() {
      const dataEvent = this.event();
      return [
        !!dataEvent.meta.actorId && 
        { code: 355, indent: 0, parameters: [
            function() {
              this.showTalkWindow();
            }
          ]
        },
        ...oFunc()
      ].filter(e=>!!e);
    }
);
Game_Map.prototype.getCurrentGameEvent = function() {
  //$gameMap._interpreter._eventId
  return this.event(this._interpreter._eventId);
};
Game_Interpreter.prototype.showTalkWindow = function() {
  const indent = this._indent;
  var dEvent = $gameMap.getCurrentGameEvent().event();
  var dActor = $dataActors[dEvent.meta.actorId];
  let options = ['交谈', '查看', '战斗', '切磋'];
  const optionsMapping = new Map([
    ['交谈', getActorRandomTalkCommand(indent + 1)],
    ['查看', getNpcInfoCommand(indent + 1)],
    ['战斗', []],
    ['切磋', []],
    ['请教', getLearningCommand(indent + 1)]
  ]);
  const eops = dEvent.meta.eOptions || '';
  const nhops = dEvent.meta.nOptions || '';
  options = options.concat(eops.split(',')).filter(e => !!e);
  var tvs = [
    { code: 102, indent, parameters: [options, -2, 0, 2, 0] },
    ...options
      .map((txtOpt, i) => {
        if (nhops.split(',').contains(txtOpt)) {
          return null;
        }
        return [
          { code: 402, indent, parameters: [i, txtOpt] },
          ...optionsMapping.get(txtOpt)
        ];
      })
      .filter(e => !!e)
      .flat(),
    { code: 403, indent, parameters: [6, null] },
    { code: 0, indent: indent + 1},
    { code: 404, indent},
    {
      code: 355,
      indent,
      parameters: [
        function() {
          //将选择结果序号赋值给变量1
          $gameVariables.setValue(1, this._branch[indent]);
        }
      ]
    },
    { code: 0, indent}
  ];
  console.log(JSON.stringify(tvs));
  this.insertCommands(tvs);
};
/**
 * 处理随机对话
 */
getActorRandomTalkCommand = function(indent) {
  var dEvent = $gameMap.getCurrentGameEvent().event();
  var dActor = $dataActors[dEvent.meta.actorId];
  var strTalkTxt = [
    '我什么也不知道，就算知道也不说，\n打死你我也不说。',
    '你没看到我正在忙吗，你还是找别人CHAT去吧。',
    dEvent.name + '看了你一眼，转身又忙自己的事情去了。',
    '今天的天气还真是。。哈哈哈哈哈。',
    dEvent.name + '睁大眼睛看着你，\n显然不知道你正在说什么。'
  ][Math.randomInt(5)];
  return [
    {
      code: 101,
      indent,
      parameters: [dActor.faceName, dActor.faceIndex, 0, 0]
    },
    { code: 401, indent, parameters: ['【' + dEvent.name + '】'] },
    { code: 401, indent, parameters: [strTalkTxt] },
    { code: 0, indent }
  ];
};

getNpcInfoCommand = function(indent) {
  return [
    { code: 230, indent, parameters: [8] },
    {
      code: 355,
      indent,
      parameters: [
        function() {
          SceneManager.goto(Scene_NpcInfo);
        }
      ]
    }
  ];
};

getLearningCommand = function(indent) {
  var dEvent = $gameMap.getCurrentGameEvent().event();
  var dActor = $dataActors[dEvent.meta.actorId];
  let cmds = [];
  var tempskills = [];
  var optskills = [];
  for (var j = 1; j <= 14; j++) {
    var skcode = dActor.meta['武功' + j];
    var sklevel = dActor.meta['等级' + j];
    if (skcode) {
      tempskills.push({
        data: $dataSkills[skcode],
        level: sklevel
      });
      optskills.push($dataSkills[skcode].name + ' x ' + sklevel);
    }
  }
  cmds = cmds.concat([
    {
      code: 101,
      indent,
      parameters: [dActor.faceName, dActor.faceIndex, 0, 0]
    },
    { code: 401, indent, parameters: ['【' + dEvent.name + '】'] }, //空一行
    { code: 401, indent, parameters: ['你想学什么就说吧！'] },
    { code: 102, indent, parameters: [optskills, -2, 0, 2, 0] }
  ]);
  for (var j = 0; j < tempskills.length; j++) {
    var tmps = tempskills[j];
    cmds = cmds.concat([
      { code: 402, indent, parameters: [j, tmps.data.name] },
      {
        code: 355,
        indent: indent + 1,
        parameters: [
          function() {
            const tmps = this._params[1];
            showLearnProgess(this, tmps.data, tmps.level, indent + 1);
          },
          tmps
        ]
      }, //tmps要按参数传，不能直接调用，要么回调函数调用时值全都是循环结束以后的tmps值。
      { code: 0, indent: indent + 1 }
    ]);
  }
  cmds = cmds.concat([
    { code: 404, indent },
    { code: 0, indent }
  ]);
  return cmds;
};
