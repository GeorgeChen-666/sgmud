//=============================================================================
// 供Game_Interpreter实例调用的函数
//=============================================================================
Game_Interpreter.prototype.onMapEnter = function (rt) {
    var dMap = $dataMap;
    //乱入NPC配置文件js\plugins\KnightErrantBox\data\map_rand_npc.js
    var dRandNpcs = [];
    for(var i=0;i<dMap.events.length;i++) {
        //如果是乱入npc栏则加入到数组中
        if(dMap.events[i] && dMap.events[i].name == '$RandNpc') {
            dRandNpcs.push(dMap.events[i]);
        }
    }
    //找到本地图的npc
    var dMapName = dMap.displayName;
    var rNpcData = null;
    for(var i=0;i<MapRandNpc.length;i++) {
        if(MapRandNpc[i] && dMapName == MapRandNpc[i][0]) {
            rNpcData = MapRandNpc[i];
            break;
        }
    }
    if(!rNpcData||!rNpcData[2]){return;}
    
    for(var i=0;i<rNpcData[2].length;i++){
        var NpcActor = rNpcData[2][i];
        //NPC出现的概率满足
        if(Math.randomInt(100)<=NpcActor[2]&&dRandNpcs.length>0){
            //重新copy一份npc模版，然后随机位置添加到地图中。
            var dNpc = JsonEx.makeDeepCopy(dRandNpcs[0]);//Object.assign({}, dRandNpcs[0]);
            dNpc.id = dMap.events.length;
            dNpc.meta.actorid = NpcActor[1];
            dMap.events[dNpc.id]=dNpc;
            $gameMap._events[dNpc.id] = new Game_Event($gameMap._mapId, dNpc.id);
            var block = $gameMap.event(dNpc.id);
            for(var j=0;j<30;j++) {
                var bx = Math.randomInt(10) + 3;
                var by = 6;
                if($gameMap.eventIdXy(bx,by) == 0) {
                    block.locate(bx,by);
                    break;
                }
            }
            //$gameMap.refreshTileEvents();
            //界面的元素变了以后要刷新Sprite
            var dSceneMap = SceneManager._scene;
            if(dSceneMap instanceof Scene_Map) {
                dSceneMap.refreshDisplayObjects();
            }
            
        }
    }

    //根据对应actor修正行走图和名字，再增加默认事件
    for(var i=0;i<dMap.events.length;i++) {
        var dNpc = dMap.events[i];
        if(dNpc && dNpc.meta && dNpc.meta.actorid) {
            var dActor = $dataActors[dNpc.meta.actorid];
            dNpc.name = dActor.name
            var block = $gameMap.event(dNpc.id);
            block._characterName = dActor.characterName;
            block._characterIndex = dActor.characterIndex;
            if(!dNpc.pages[0].list || dNpc.pages[0].list.length == 0 
            || dNpc.pages[0].list.length == 1 && dNpc.pages[0].list[0].code == 0) {

                dNpc.pages[0].list = [
                    {"code":355,"indent":0,"parameters":["showTalkWindow(this)"]},
                    {"code":0,"indent":0,"parameters":[]}
                ];
                
            }
        }
    }

    //加载本地图NPC头像
    dMap.events.forEach(function(dNpc) {
        if(dNpc && dNpc.meta && dNpc.meta.actorid) {
            var actor= $dataActors[dNpc.meta.actorid];
            ImageManager.loadFace(actor.faceName);
        } 
    }, this);
}

//indent为脚本起始缩进
Game_Interpreter.prototype.showTalkWindow = function (rt,eops='',nhops='',indent=0) {
    var dEvent = $gameMap.getCurrentGameEvent().event();
    var dActor = $dataActors[dEvent.meta.actorid];
    var options = ['交谈','查看','战斗','切磋'];
    eops.split(',').forEach(function(op) {
        if(op!=''){
            options.push(op);
        }
    }, this);

    var tvs = [];//rt._list
    tvs = tvs.concat([
        {"code":102,"indent":indent,"parameters":[options,-2,0,2,0]}
    ])
    for(var i=0;i<options.length;i++) {
        var txtOpt = options[i];
        if(nhops.split(',').contains(txtOpt)) {
            continue;
        }
        var cmds = [
            {"code":402,"indent":indent,"parameters":[i,txtOpt]}
        ]
        if(txtOpt == '交谈') {
            cmds = cmds.concat(getActorTalkRandomCommand(rt,indent+1));
        }
        else if(txtOpt == '查看') {
            cmds = cmds.concat([
                {"code":'CallBack',"indent":indent+1,"parameters":[function(rt){
                    SceneManager._scene._messageWindow._choiceWindow._openness=0
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
                    {"code":'CallBack',"indent":indent+2,"parameters":[function(rt){
                        var tmps = rt._params[1];
                        showLearnProgess(rt,tmps.data,tmps.level,indent+2); 
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
        {"code":'CallBack',"indent":indent,"parameters":[function(rt){
            //将选择结果序号赋值给变量1
            $gameVariables.setValue(1,rt._branch[rt._indent]);
        }]},
        {"code":0,"indent":indent,"parameters":[]}
    ])
    rt.insertCommand(tvs)
}
/**
 * 处理随机对话
 */
Game_Interpreter.prototype.getActorTalkRandomCommand = function (rt,indent=0) {
    var dEvent = $gameMap.getCurrentGameEvent().event();
    var dActor = $dataActors[dEvent.meta.actorid];
    var strTalkTxt = [
        '我什么也不知道，就算知道也不说，\n打死你我也不说。',
        '你没看到我正在忙吗，你还是找别人CHAT去吧。',
        dEvent.name + '看了你一眼，转身又忙自己的事情去了。',
        '今天的天气还真是。。哈哈哈哈哈。',
        dEvent.name + '睁大眼睛看着你，\n显然不知道你正在说什么。'
    ][Math.randomInt(5)];
    return getActorTalkCommand(rt,strTalkTxt,indent);
}
Game_Interpreter.getActorTalkCommand = function (rt,txt,indent=0) {
    var dEvent = $gameMap.getCurrentGameEvent().event();
    var dActor = $dataActors[dEvent.meta.actorid];
    var cmds=[];
    cmds = cmds.concat([
        {"code":101,"indent":indent,"parameters":[dActor.faceName,dActor.faceIndex,0,0]},
        {"code":401,"indent":indent,"parameters":['【'+dEvent.name+'】']},//空一行
        {"code":401,"indent":indent,"parameters":[txt]},
        {"code":0,"indent":indent,"parameters":[]},
    ]);
    return cmds;
}
/**
 * 处理请教进度条
 */
Game_Interpreter.prototype.showLearnProgess = function (rt,skill,level,indent=0) {
    var dEvent = $gameMap.getCurrentGameEvent().event();
    var dActor = $dataActors[dEvent.meta.actorid];
    var me = $gameParty.members()[0];
    var skid = skill.id;
    SceneManager.Progess = {
        val:me.skillexp(skid),
        max:Math.pow(me.skilllevel(skid) + 1,2),
        onEachStep:function(){
            var needMoney = 0;
            if(skill.name == '读书识字')
            {
                if(me.skilllevel(skid)<20) {needMoney = 5;}
                else if(me.skilllevel(skid)<30) {needMoney = 10;}
                else if(me.skilllevel(skid)<60) {needMoney = 50;}
                else if(me.skilllevel(skid)<80) {needMoney = 150;}
                else if(me.skilllevel(skid)<100) {needMoney = 300;}
                else if(me.skilllevel(skid)<120) {needMoney = 500;}
                else { needMoney = 1000;}
            }
            if($gameParty.gold() < needMoney) {
                rt.insertCommand(showActorTalk(rt,'没钱读什么书啊，回去准备够学费再来吧！',indent));
                SceneManager.pop();
            }
            else if(me.pot <= 0) {
                me.pot=0;
                rt.insertCommand(showActorTalk(rt,'你的潜能已经发挥到极限了，没有办法再成长了。',indent));
                SceneManager.pop();
            }
            else {
                $gameParty.loseGold(needMoney);
                SceneManager.Progess.val += me.int_now / 2 + Math.randomInt(10) / 10.0;
                me.skillexps()[skid] = SceneManager.Progess.val;
                me.pot--;
            }
        },
        onFinish:function(){
            var cmds=[];
            //做一些校验
            if(me.skilllevel(skid) >= level) {
                cmds = cmds.concat(showActorTalk(rt,'你的功夫已经不输为师了，真是可喜可贺呀！',indent));
            }
            else if(me.exp < Math.pow(me.skilllevel(skid),3) / 10) {
                cmds = cmds.concat(showActorTalk(rt,'你的武学经验不足，无法领会更深的功夫！',indent));
            }
            else {
                me.learnSkill(skid);
                me.skillexps()[skid] = 0;
                me.skilllevels()[skid] = me.skilllevel(skid) + 1;
                cmds = cmds.concat([
                    {"code":101,"indent":indent,"parameters":[dActor.faceName,dActor.faceIndex,0,0]},
                    {"code":401,"indent":indent,"parameters":['【'+dEvent.name+'】']},
                    {"code":401,"indent":indent,"parameters":['你的功夫进步了！\n继续学习吗？']},
                    {"code":102,"indent":indent,"parameters":[['是','否'],-2,0,2,0]},
                    {"code":402,"indent":indent,"parameters":[0,"是"]},
                    {"code":'CallBack',"indent":indent+1,"parameters":[function(rt){
                        showLearnProgess(rt,skill,level,indent); 
                    }]},
                    {"code":0,"indent":indent+1,"parameters":[]},
                    {"code":402,"indent":indent,"parameters":[1,"否"]},
                    {"code":0,"indent":indent+1,"parameters":[]},
                    {"code":404,"indent":indent,"parameters":[]},
                    {"code":0,"indent":indent+1,"parameters":[]},
                ]);
            }
            rt.insertCommand(cmds);
            SceneManager.pop();
        }
    };
    var cmds=[];
    cmds = cmds.concat([
        {"code":'CallBack',"indent":indent,"parameters":[function(rt){
            SceneManager.push(Scene_Progess);
        }]},
        {"code":0,"indent":indent,"parameters":[]},
    ]);
    rt.insertCommand(cmds);
}