
import {Scene_MapMenu,Window_MapMenuCommand} from '../view/MapMenu'

/**
 * 地图展现菜单的场景替换
 */
Scene_Map.prototype.callMenu = function(){
    SoundManager.playOk();
    SceneManager.push(Scene_MapMenu);
    //Window_MapMenuCommand.initCommandPosition();
    $gameTemp.clearDestination();
    this._mapNameWindow.hide();
    this._waitCount = 2;
};
/**
 * 进度条场景
 * 后续想把$ProgessHelper数据换到SceneManager的属性中
 */
function Scene_Progess() {
    this.initialize.apply(this, arguments);
}

Scene_Progess.prototype = Object.create(Scene_Base.prototype);
Scene_Progess.prototype.constructor = Scene_Progess;
Scene_Progess.prototype.initialize = function() {
    Scene_Base.prototype.initialize.call(this);
};
Scene_Progess.prototype.create = function() {
    Scene_Base.prototype.create.call(this);
    this.createBackground();
    this.createWindowLayer();
    this.createSceneWindow();
};
Scene_Progess.prototype.createSceneWindow = function() {
    this._win = new Window_Progess();
    this.addWindow(this._win);
};
Scene_Progess.prototype.update = function() {
    if(Input.isTriggered('escape') || TouchInput.isCancelled()) {
        this._win.close();
        SoundManager.playCancel()
        //SceneManager.goto(Scene_Map);
        SceneManager.pop();//SceneManager.clearStack
    }
    //每秒一次
    if(this.seconds!==$gameSystem.playtime()) {
        this.seconds=$gameSystem.playtime();
        var Progess = SceneManager.Progess;
        (Progess.onEachStep||function(){})();
        if(Progess.val >= Progess.max)
        {
            this._win.close();
            SceneManager.goto(Scene_Map);
            (Progess.onFinish||function(){})();
        }
        this._win.refresh();
    }
    Scene_Base.prototype.update.call(this);
};

/**
 * 请教
 */
function Scene_Learn() {
    this.initialize.apply(this, arguments);
}
Scene_Learn.prototype = Object.create(Scene_Base.prototype);
Scene_Learn.prototype.constructor = Scene_Learn;
Scene_Learn.prototype.initialize = function() {
    Scene_Base.prototype.initialize.call(this);
};
Scene_Learn.prototype.create = function() {
    Scene_Base.prototype.create.call(this);
    this.createBackground();
    this.createWindowLayer();
    this.createSceneWindow();
};
Scene_Learn.prototype.createSceneWindow = function() {
    //先获取请教的actor技能列表还有等级列表
    this._win_cmd = new Window_Command_Easy(0,0,240,3,function(){
        //然后在这列举出技能和等级
        this.addCommand("武功1",   'kunfu1');
        this.addCommand("武功2",   'kunfu1');
        this.addCommand("武功3",   'kunfu1');
        this.addCommand("武功4",   'kunfu1');
        this.addCommand("武功5",   'kunfu1');
    });
    this._win_cmd.setHandler('kunfu1',  function(){
        //this._win_cmd.index是选择的序号
        //然后根据选择的序号获取技能
        //下面就复写vx里的请教代码就好了。
        //请教的进度条就调用Scene_Progess
        SceneManager.goto(Scene_Map);
    }.bind(this));
    this._win_cmd.x = Graphics.boxWidth - this._win_cmd.width;
    this._win_cmd.y = (Graphics.boxHeight - this._win_cmd.height) / 2;
    this.addWindow(this._win_cmd);
};
Scene_Learn.prototype.update = function() {
    if(Input.isTriggered('escape') || TouchInput.isCancelled()) {
        this._win_cmd.close();
        SoundManager.playCancel()
        SceneManager.goto(Scene_Map);
    }
    Scene_Base.prototype.update.call(this);
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
// /**
//  * 从NPC处学习技能
//  */
// function Scene_LearnNpc() {
//     this.initialize.apply(this, arguments);
// }
// Scene_LearnNpc.prototype = Object.create(Scene_Base.prototype);
// Scene_LearnNpc.prototype.constructor = Scene_LearnNpc;
// Scene_LearnNpc.prototype.initialize = function() {
//     Scene_Base.prototype.initialize.call(this);
// };
// Scene_LearnNpc.prototype.create = function() {
//     Scene_Base.prototype.create.call(this);
//     this.createBackground();
//     this.createWindowLayer();
//     this.createSceneWindow();
// };

(function(){
    /**
     * 默认场景的背景是模糊
     */
    Scene_Base.prototype.createBackground = function() {
        this._backgroundSprite = new Sprite();
        this._backgroundSprite.bitmap = SceneManager.backgroundBitmap();
        this.addChild(this._backgroundSprite);
    };
    /**
     * 如果场景没换就不刷新地图数据，
     * 否则脚本中修改的地图数据在弹窗口会被刷没。
     */
    Scene_Map.prototype.create = function() {
        Scene_Base.prototype.create.call(this);
        this._transfer = $gamePlayer.isTransferring();
        var mapId = this._transfer ? $gamePlayer.newMapId() : $gameMap.mapId();
        if(mapId!=$gameMap.mapId()) {
            DataManager.loadMapData(mapId);
        }
    };
    Scene_Map.prototype.refreshDisplayObjects = function() {
        this.removeChild(this._spriteset);
        this._spriteset = new Spriteset_Map();
        this.addChild(this._spriteset);
        var width = Graphics.boxWidth;
        var height = Graphics.boxHeight;
        var x = (Graphics.width - width) / 2;
        var y = (Graphics.height - height) / 2;
        this.removeChild(this._windowLayer);
        this._windowLayer = new WindowLayer();
        this._windowLayer.move(x, y, width, height);
        this.addChild(this._windowLayer);
        this.createAllWindows();
    };

    var winMenuCommand;
    //一级场景
    Scene_Menu.prototype.createCommandWindow = function() {

        //this._optionMenuWindow
        this._commandWindow = new Window_MenuCommand(0, 0);
        winMenuCommand = this._commandWindow;
        this._commandWindow.setHandler('item',      function() {
            SceneManager.push(Scene_Item);
        }.bind(this));
        this._commandWindow.setHandler('skill',     function(){
            SceneManager.push(Scene_Skill);
        }.bind(this));
        // this._commandWindow.setHandler('equip',     this.commandPersonal.bind(this));
        // this._commandWindow.setHandler('status',    this.commandPersonal.bind(this));
        // this._commandWindow.setHandler('formation', this.commandFormation.bind(this));
        this._commandWindow.setHandler('options',   function(){
            this._commandWindow.deactivate();
            this._optionMenuWindow.show();
            this._optionMenuWindow.activate();

        }.bind(this));
        // this._commandWindow.setHandler('save',      this.commandSave.bind(this));
        // this._commandWindow.setHandler('gameEnd',   this.commandGameEnd.bind(this));
        this._commandWindow.setHandler('cancel',    this.popScene.bind(this));
        this.addWindow(this._commandWindow);

    };

    Scene_Menu.prototype.create = function() {
        Scene_MenuBase.prototype.create.call(this);
        this.createCommandWindow();
        this.createGoldWindow();
        this.createStatusWindow();
        this._optionMenuWindow = new Window_Command_Easy(100,75,240,5,function(){
            this.addCommand("内力..",   '');
            //this.addCommand("法力..",   '');
            this.addCommand("练功",   '');
            this.addCommand(TextManager.save,   'save');
            this.addCommand(TextManager.gameEnd,   'gameEnd');
        });
        this._optionMenuWindow.setHandler('save',      this.commandSave.bind(this));
        this._optionMenuWindow.setHandler('gameEnd',   this.commandGameEnd.bind(this));
        this._optionMenuWindow.setHandler('cancel',    function(){
            this._commandWindow.activate();
            this._optionMenuWindow.deactivate();
            this._optionMenuWindow.hide();
        }.bind(this));
        this._optionMenuWindow.deactivate();
        this._optionMenuWindow.hide();
        this.addWindow(this._optionMenuWindow);
        this._statusWindow.x = this._commandWindow.width;
        this._statusWindow.y = 0;
        this._goldWindow.x = 0;//Graphics.boxWidth - this._goldWindow.width;
        this._goldWindow.y = Graphics.boxHeight - this._goldWindow.height;
    };

    Scene_Skill.prototype.create = function() {
        Scene_ItemBase.prototype.create.call(this);

        this._levelWindow = new Window_Help(1);
        //this._levelWindow.hide();
        this.addWindow(this._levelWindow);
        this.createSkillTypeWindow();
        //this.createStatusWindow();
        this.createItemWindow();
        this.createHelpWindow();

        //this.createActorWindow();
        this.refreshActor();
    };
    Scene_Skill.prototype.createHelpWindow = function() {
        this._helpWindow = new Window_Help(6);
        this._helpWindow.y=this._skillTypeWindow.y+this._skillTypeWindow.height;
        this.addWindow(this._helpWindow);
        this._skillTypeWindow.setHelpWindow(this._helpWindow);
        this._itemWindow.setHelpWindow(this._helpWindow);
    };
    Scene_Skill.prototype.createSkillTypeWindow = function() {
        var wy = this._levelWindow.height;
        this._skillTypeWindow = new Window_SkillType(0, wy);

        this._skillTypeWindow.setHandler('skill',    this.commandSkill.bind(this));
        this._skillTypeWindow.setHandler('cancel',   this.popScene.bind(this));
        this._skillTypeWindow.setHandler('pagedown', this.nextActor.bind(this));
        this._skillTypeWindow.setHandler('pageup',   this.previousActor.bind(this));
        this.addWindow(this._skillTypeWindow);
    };
    Scene_Skill.prototype.createItemWindow = function() {
        var wx = this._skillTypeWindow.width;
        var wy = this._levelWindow.height;
        var ww = Graphics.boxWidth - wx;
        var wh = this._skillTypeWindow.height;
        this._itemWindow = new Window_SkillList(wx, wy, ww, wh);

        this._itemWindow.setHandler('ok',     this.onItemOk.bind(this));
        this._itemWindow.setHandler('cancel', this.onItemCancel.bind(this));
        this._skillTypeWindow.setSkillWindow(this._itemWindow);
        this.addWindow(this._itemWindow);
    };
    Scene_Skill.prototype.refreshActor = function() {
        var actor = this.actor();
        this._skillTypeWindow.setActor(actor);
        //this._statusWindow.setActor(actor);
        this._itemWindow.setActor(actor);
    };
    Scene_Skill.prototype.determineItem = function() {
        var me = this.actor();
        var skill = this.item();
        if(me.getequipskill(skill.stypeId)==skill) {
            me.setequipskill(skill.stypeId,null);
        }
        else {
            me.setequipskill(skill.stypeId,skill);
        }
        Input.clear();
        this.activateItemWindow();
    };
    Scene_Battle.prototype.updateWindowPositions = function() {
    };
    Scene_Battle.prototype.startPartyCommandSelection = function() {
        this.refreshStatus();
        this._actorCommandWindow.activate();
        //this._statusWindow.deselect();
        //this._statusWindow.open();
        //this._actorCommandWindow.close();
        //this._partyCommandWindow.setup();
    };
    Scene_Battle.prototype.commandAttack = function() {
        $gameParty.battleMembers()[0].makeAttackMove();
        BattleManager.inputtingAction().setAttack();
        if($gameTroop.members().length==1) {
            $gameTroop.select($gameTroop.members()[0]);
            this.onEnemyOk();
        }
        else {
            this.selectEnemySelection();
        }

    };
})();