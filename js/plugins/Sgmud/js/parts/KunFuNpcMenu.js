//如果一个事件有meta.actorId，那么就会弹出菜单。
PluginManager.regHook(
  "Game_Event.prototype.list",
  (oFunc) =>
    function () {
      const dataEvent = this.event();
      return [
        !!dataEvent.meta.actorId && {
          code: 355,
          indent: 0,
          parameters: ["this.showTalkWindow();"],
        },
        ...oFunc(),
      ].filter((e) => !!e);
    }
);
Game_Interpreter.prototype.showTalkWindow = function () {
  const indent = this._indent;
  const dEvent = this.character().event();
  const dActor = $dataActors[dEvent.meta.actorId];
  let options = ["交谈", "查看", "战斗", "切磋"];
  const optionsMapping = new Map([
    ["交谈", getActorRandomTalkCommand],
    ["查看", getNpcInfoCommand],
    ["战斗", []],
    ["切磋", []],
    ["请教", getLearningCommand],
  ]);
  const eops = dEvent.meta.eOptions || "";
  const nhops = dEvent.meta.nOptions || "";
  options = options.concat(eops.split(",")).filter((e) => !!e);

  new $mvs(this)
    .showChoices(options)
    .calculateAndCall((cmds) => {
      options.forEach((txtOpt, i) => {
        if (!nhops.split(",").contains(txtOpt)) {
          cmds.whenChoicesIndex(i, optionsMapping.get(txtOpt));
        }
      });
    })
    .endWhen()
    .callJsFunction(function () {
      //将选择结果序号赋值给变量1
      $gameVariables.setValue(1, this._branch[indent]);
    })
    .done();
};
/**
 * 处理随机对话
 */
getActorRandomTalkCommand = (cmds) => {
  const dEvent = cmds.interpreter.character().event();
  const dActor = $dataActors[dEvent.meta.actorId];
  const strTalkTxt = [
    "我什么也不知道，就算知道也不说，\n打死你我也不说。",
    "你没看到我正在忙吗，你还是找别人CHAT去吧。",
    dEvent.name + "看了你一眼，转身又忙自己的事情去了。",
    "今天的天气还真是。。哈哈哈哈哈。",
    dEvent.name + "睁大眼睛看着你，\n显然不知道你正在说什么。",
  ][Math.randomInt(5)];
  cmds.showText(
    ["【" + dEvent.name + "】", strTalkTxt],
    dActor.faceName,
    dActor.faceIndex,
    $mvs.enumBackground.Window,
    $mvs.enumPositionV.Top
  );
};
getNpcInfoCommand = (cmds) => {
  cmds.wait(8).callJsFunction(function () {
    SceneManager.goto(Scene_NpcInfo);
  });
};

getLearningCommand = (cmds) => {
  const dEvent = cmds.interpreter.character().event();
  const dActor = $dataActors[dEvent.meta.actorId];
  const tempskills = [];
  const optskills = [];
  for (let j = 1; j <= 14; j++) {
    let skcode = dActor.meta["武功" + j];
    let sklevel = dActor.meta["等级" + j];
    if (skcode) {
      tempskills.push({
        data: $dataSkills[skcode],
        level: sklevel,
      });
      optskills.push($dataSkills[skcode].name + " x " + sklevel);
    }
  }
  cmds
    .showText(
      ["【" + dEvent.name + "】", "你想学什么就说吧！"],
      dActor.faceName,
      dActor.faceIndex,
      $mvs.enumBackground.Window,
      $mvs.enumPositionV.Top
    )
    .showChoices(optskills)
    .calculateAndCall((cmds) => {
      for (let j = 0; j < tempskills.length; j++) {
        cmds.whenChoicesIndex(j, (subCmds) => {
          subCmds.callJsFunction(function () {
            const tmps = tempskills[j];
            showLearnProgess(subCmds, tmps.data, tmps.level);
          });
        });
      }
    })
    .endWhen();
};
showLearnProgess = (cmds, skill, maxLevel) => {
  const dEvent = cmds.interpreter.character().event();
  const dActor = $dataActors[dEvent.meta.actorId];
  const me = $gameParty.members()[0];
  const skid = skill.id;
  const stopAndShowText = (text) => {
    $gameMessage.clear();
    return cmds.showText(
      ["【" + dEvent.name + "】", text],
      dActor.faceName,
      dActor.faceIndex,
      $mvs.enumBackground.Window,
      $mvs.enumPositionV.Top
    );
  };
  cmds.interpreter.setWaitMode("message");
  $gameMessage.setProgress(
    me.skillexp(skid),
    Math.pow(me.skilllevel(skid) + 1, 2),
    skill.name,
    () => me.int_now / 2 + Math.randomInt(10) / 10.0,
    () => {
      let needMoney = 0;
      if (skill.name === "读书识字") {
        if (me.skilllevel(skid) < 20) {
          needMoney = 5;
        } else if (me.skilllevel(skid) < 30) {
          needMoney = 10;
        } else if (me.skilllevel(skid) < 60) {
          needMoney = 50;
        } else if (me.skilllevel(skid) < 80) {
          needMoney = 150;
        } else if (me.skilllevel(skid) < 100) {
          needMoney = 300;
        } else if (me.skilllevel(skid) < 120) {
          needMoney = 500;
        } else {
          needMoney = 1000;
        }
      }
      if ($gameParty.gold() < needMoney) {
        stopAndShowText("没钱读什么书啊，回去准备够学费再来吧！").done();
      } else if (me.pot <= 0) {
        stopAndShowText("你的潜能已经发挥到极限了，没有办法再成长了。").done();
      } else {
        $gameParty.loseGold(needMoney);
        me.skillexps()[skid] = $gameMessage.progressVal;
        me.pot--;
      }
    },
    () => {
      if (me.skilllevel(skid) >= maxLevel) {
        stopAndShowText("你的功夫已经不输为师了，真是可喜可贺呀！").done();
      } else if (me.exp < Math.pow(me.skilllevel(skid), 3) / 10) {
        stopAndShowText("你的武学经验不足，无法领会更深的功夫！").done();
      } else {
        me.learnSkill(skid);
        me.skillexps()[skid] = 0;
        me.skilllevels()[skid] = me.skilllevel(skid) + 1;
        stopAndShowText("你的功夫进步了！\n继续学习吗？")
          .showChoices(["是", "否"])
          .whenChoicesIndex(0, (subCmds) => {
            subCmds.callJsFunction(() => {
              subCmds.interpreter.clear();
              showLearnProgess(subCmds, skill, maxLevel);
            });
          })
          .whenChoicesIndex(1, () => {})
          .done();
      }
    }
  );
};
