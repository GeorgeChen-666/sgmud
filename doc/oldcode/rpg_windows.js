/**
 * 公用选项窗口
 */
function Window_Command_Easy() {
    this.initialize.apply(this, arguments);
}
Window_Command_Easy.prototype = Object.create(Window_Command.prototype);
Window_Command_Easy.prototype.constructor = Window_Command_Easy;
Window_Command_Easy.prototype.initialize = function(x, y,width = 240,vrows = 3,cb) {
    this._parmCB = cb;
    this._parmWidth = width;
    this._parmVisibleRows = vrows;
    Window_Command.prototype.initialize.call(this, x, y);
};
Window_Command.prototype.makeCommandList = function() {
    if(this._parmCB) {
        this._parmCB();
    }
};
Window_Command_Easy.prototype.windowWidth = function() {
    return this._parmWidth;
};
Window_Command_Easy.prototype.numVisibleRows = function() {
    //return Math.min(Math.ceil(this.maxItems() / this.maxCols()),this._parmVisibleRows);
    return this._parmVisibleRows;
};

function Window_Text() {
    this.initialize.apply(this, arguments);
}
Window_Text.prototype = Object.create(Window_Base.prototype);
Window_Text.prototype.constructor = Window_Text;
Window_Text.prototype.initialize = function(text='',x=0,y=0,width=null,rows=1) {
    this._text = text;
    width = width || Graphics.boxWidth;
    var height = this.lineHeight()*rows;
    Window_Base.prototype.initialize.call(this, x, y, width, height);
    this.refresh();
};
Window_Text.prototype.setText = function(text) {
    this._text = text;
};
Window_Text.prototype.refresh = function() {
    this.contents.clear();
    this.drawTextEx(this._text,0,0);
};

/**
 * 进度条窗口
 */
function Window_Progess() {
    this.initialize.apply(this, arguments);
}
Window_Progess.prototype = Object.create(Window_Base.prototype);
Window_Progess.prototype.constructor = Window_Progess;
Window_Progess.prototype.initialize = function() {
    var width = this.windowWidth();
    var height = this.windowHeight();
    var x = (Graphics.boxWidth - width) / 2;
    Window_Base.prototype.initialize.call(this, x, 0, width, height);
    this.refresh();
};
Window_Progess.prototype.windowWidth = function() {
    return 400;
};
Window_Progess.prototype.windowHeight = function() {
    return this.fittingHeight(1);
};
Window_Progess.prototype.refresh = function() {
    var x = this.textPadding();
    var width = this.contents.width;
    this.contents.clear();
    var Progess = SceneManager.Progess;
    var max = Progess.max;
    var val = Progess.val;
    this.drawGaugeBar('', val , max,0,0,width,true,true, this.tpGaugeColor1(),this.tpGaugeColor2());
    //this.drawCurrencyValue(this.value(), this.currencyUnit(), x, 0, width);
};
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

(function() {
    Window_Base.prototype.drawHorzLine = function(y) {
        //this.contents.drawText(text, x, y, maxWidth, this.lineHeight(), align);
        line_y = y + this.lineHeight() / 2 - 1
        this.contents.fillRect(0,line_y,this.contentsWidth(),2,this.normalColor());
    };
    Window_Base.prototype.drawGaugeBar = function(title,min,max, x, y, width,hasGauge=false,hasText=true,color1,color2) {
        width = width || 186;
        if(hasGauge) {
            this.drawGauge(x, y, width, (max==0?0:min / max), color1, color2);
        }
        this.changeTextColor(this.systemColor());
        this.drawText(title, x, y, 56);
        if(hasText) {
            this.drawCurrentAndMax(min, max, x, y, width,
                                this.normalColor(), this.normalColor());
        }
    };
    Window_Base.prototype.drawActorHp = function(actor, x, y, width,withnum=true) {
        this.drawGaugeBar(TextManager.hpA,actor.hp, actor.fhp,x, y, width,
            true,withnum,this.hpGaugeColor1(),this.hpGaugeColor1());
    };
    Window_Base.prototype.drawActorMp = function(actor, x, y, width,withnum=true) {
        this.drawGaugeBar(TextManager.mpA,actor.mp, actor.fmp,x, y, width,
            true,withnum,this.mpGaugeColor1(),this.mpGaugeColor1());
    };

    Window_Gold.prototype.windowHeight = function() {
        return this.fittingHeight(3);
    };
    Window_Gold.prototype.refresh = function() {
        var x = this.textPadding();
        var width = this.contents.width - this.textPadding() * 2;
        var actor = $gameParty.members()[0];
        this.contents.clear();
        this.drawCurrencyValue(actor.currentExp(), '经验', x, 0, width);
        this.drawCurrencyValue($gameParty.gold(), this.currencyUnit(), x, this.lineHeight() * 2, width);
        this.drawCurrencyValue(actor.point(), '潜能', x, this.lineHeight() * 1, width);
    };
    //一级菜单重写
    Window_MenuCommand.prototype.windowWidth = function() {
        return 240;
    };
    Window_MenuCommand.prototype.maxCols = function() {
        return 1;
    };
    Window_MenuCommand.prototype.numVisibleRows = function() {
        return 3;
    };
    Window_MenuCommand.prototype.initialize = function(x, y) {
        var width = this.windowWidth();
        var height = this.windowHeight();
        Window_Command.prototype.initialize.call(this, x, y, width, height);
        this.selectLast();
    };
    Window_MenuCommand.prototype.makeCommandList = function() {
        this.addCommand(TextManager.item  , 'item', true);
        this.addCommand(TextManager.skill , 'skill', true);
        this.addCommand(TextManager.options, 'options', true);
    };
    Window_MenuStatus.prototype.maxItems = function() {
        return 1;
    };
    Window_MenuStatus.prototype.windowHeight = function() {
        return Graphics.boxHeight;
    };

    Window_MenuStatus.prototype.maxCols = function() { return 1;};

    Window_MenuStatus.prototype.numVisibleRows = function() { return 1;};

    Window_MenuStatus.prototype.drawItemImage = function(index) {
        var actor = $gameParty.members()[index];
        var rect = this.itemRectForText(index);
        var w = Math.min(rect.width, 144);
        var h = Math.min(rect.height, 144);
        var lineHeight = this.lineHeight();
        this.changePaintOpacity(actor.isBattleMember());
        this.drawActorFace(actor, rect.x, rect.y + lineHeight * 2, w, h);
        this.changePaintOpacity(true);
    };

    Window_MenuStatus.prototype.drawItemStatus = function(index) {
        var me = $gameParty.members()[index];
        var rect = this.itemRectForText(index);
        var x = rect.x;
        var y = rect.y;
        var width = rect.width;
        var bottom = y + rect.height;
        var lineHeight = this.lineHeight();
        //门派
        this.drawActorClass(me, x, y + lineHeight * 0, width);
        //角色名
        this.drawActorName(me, lineHeight * 6, y + lineHeight * 0, width);
        //角色图标
        this.drawActorIcons(me, lineHeight * 13, y + lineHeight * 0, width);
        this.drawHorzLine(lineHeight * 1);
        this.drawGaugeBar('食物',me.food,me.ffood,x + 160, y + lineHeight * 2, width - 240,
            true,true,this.hpGaugeColor1(),this.hpGaugeColor2());
        this.drawGaugeBar('饮水',me.water,me.fwater,x + 160, y + lineHeight * 3, width - 240,
            true,true,this.mpGaugeColor1(),this.mpGaugeColor1());
        this.drawActorHp(me,x + 160, y + lineHeight * 4,width - 240);
        this.drawText(parseInt((me.fhp/me.mfhp)*100)+'%', x + width - 63 , y + lineHeight * 4, 70);
        this.drawActorMp(me,x + 160, y + lineHeight * 5,width - 240);
        this.drawText('+255', x + width - 63 , y + lineHeight * 5, 70);
        this.drawHorzLine(lineHeight * 6);
        this.drawGaugeBar('膂力',me.str_now,me.str,x , y + lineHeight * 7, 200);
        this.drawGaugeBar('敏捷',me.dex_now,me.dex,x + 287 , y + lineHeight * 7, 200);
        this.drawGaugeBar('悟性',me.int_now,me.int,x , y + lineHeight * 8, 200);
        this.drawGaugeBar('根骨',me.vit_now,me.vit,x + 287 , y + lineHeight * 8, 200);
        this.drawHorzLine(lineHeight * 9);
        this.resetTextColor();
        this.drawText('你是一名' + me.age + '岁的' + me.sex + '生', x, bottom - lineHeight * 6, width);
        this.drawText('你一脸稚气', x, bottom - lineHeight * 5, width);
        this.drawText('武艺看起来不堪一击', x, bottom - lineHeight * 4, width);
        this.drawText('出手似乎很轻', x, bottom - lineHeight * 3, width);
        this.drawText('你还是光棍一条', x, bottom - lineHeight * 2, width);
    };

    Window_SkillType.prototype.numVisibleRows = function() {
        return 7;
    };
    /**
     * 所有可配置的类别都展示
     */
    Window_SkillType.prototype.makeCommandList = function() {
        for(var i=1;i<8;i++) {
            var name = $dataSystem.skillTypes[i];
            this.addCommand(name, 'skill', true, i);
        }
    };
    Window_SkillList.prototype.drawItemName = function(skill, x, y, width) {
        width = width || 312;
        if (skill) {
            var skill_level = this._actor.skilllevel(skill.id);
            var iconBoxWidth = Window_Base._iconWidth + 4;
            this.resetTextColor();
            this.drawIcon(skill.iconIndex, x + 2, y + 2);
            this.drawText(skill.name + ' = ' + skill_level, x + iconBoxWidth, y, width - iconBoxWidth);
        }
    };
    Window_SkillList.prototype.isCurrentItemEnabled = function() {
        //判断是不是可以装备的
        return this._data[this.index()].occasion==2;//使用场合是选单画面
    };

    Window_SkillList.prototype.isEnabled = function(item) {
        return this._actor && this._actor.getequipskill(item.stypeId) == item;
    };

    Window_ActorCommand.prototype.windowWidth = function() {
        return Graphics.boxWidth;
    };
    Window_ActorCommand.prototype.maxCols = function() {
        return 6;
    };
    Window_ActorCommand.prototype.numVisibleRows = function() {
        return 1;
    };
    Window_ActorCommand.prototype.makeCommandList = function() {
        if (this._actor) {
            //this.addAttackCommand();
            //this.addSkillCommands();
            //this.addGuardCommand();
            //this.addItemCommand();
            this.addCommand('普通攻击',  'attack');
            this.addCommand('绝招攻击',  'fight');
            this.addCommand('使用内力',  'fight');
            this.addCommand('使用物品',  'fight');
            this.addCommand('调整招式',  'fight');
            this.addCommand('逃　　跑',  'fight');
        }
    };
    Window_BattleStatus.prototype.maxCols = function() {
        return 2;
    };
    Window_BattleStatus.prototype.updateCursor = function() {
    };
    Window_BattleStatus.prototype.maxItems = function() {
        return 2;
    };
    Window_BattleStatus.prototype.drawItem = function(index) {
        if(index == 0) {
            var actor = $gameParty.battleMembers()[index];
            this.drawBasicArea(this.basicAreaRect(index), actor);
            this.drawGaugeArea(this.gaugeAreaRect(index), actor);
        }
        else if(index == 1) {
            var enemy = $gameTroop.members()[0];
            this.drawBasicArea(this.basicAreaRect(index), enemy);
            this.drawGaugeAreaWithoutTp(this.gaugeAreaRect(index), enemy,false);
        }
    };
    Window_BattleStatus.prototype.drawGaugeAreaWithoutTp = function(rect, actor,hasText=true) {
        this.drawActorHp(actor, rect.x + 0, rect.y, rect.width,hasText);
        this.drawActorMp(actor, rect.x + 0,  rect.y+this.fittingHeight(0), rect.width,hasText);
    };
    Window_BattleStatus.prototype.gaugeAreaRect = function(index) {
        var rect = this.itemRectForText(index);
        //rect.x = 0;
        rect.y = this.fittingHeight(0);
        rect.width = this.itemRect().width;
        return rect;
    };
    Window_BattleStatus.prototype.itemRect = function(index) {
        var rect = new Rectangle();
        var maxCols = this.maxCols();
        rect.width = this.itemWidth() - 5;
        rect.height = this.height;//this.itemHeight();
        rect.x = index % maxCols * (rect.width + this.spacing()) - this._scrollX;
        rect.y = 0;//Math.floor(index / maxCols) * rect.height - this._scrollY;
        return rect;
    };
    Window_BattleStatus.prototype.initialize = function() {
        var width = Graphics.boxWidth;
        var height = this.windowHeight();
        var x = 0;
        var y = Graphics.boxHeight - height - this.fittingHeight(1);
        Window_Selectable.prototype.initialize.call(this, x, y, width, height);
        this.deactivate();
        this.refresh();
        this.openness = 0;
    };
    Window_BattleStatus.prototype.numVisibleRows = function() {
        return 3;
    };
    Window_BattleLog.prototype.messageSpeed = function() {
        return 24;
    };
    Window_BattleLog.prototype.startAction = function(subject, action, targets) {
        var item = action.item();
        this.push('performActionStart', subject, action);
        this.push('waitForMovement');
        this.push('performAction', subject, action);
        this.push('showAnimation', subject, targets.clone(), item.animationId);
        this.displayAction(subject, item,targets);
    };
    Window_BattleLog.prototype.displayAction = function(subject, item,targets) {
        var numMethods = this._methods.length;
        if (DataManager.isSkill(item)) {
            if (item.message1) {
                var msg = item.message1.format(item.name);
                msg = msg.replace(/N/g,subject.name())
                .replace(/SW/g,subject.weapons().length>0?subject.weapons()[0].name:'')
                .replace(/SB/g,targets[0].name())
                .replace(/SP/g,['头部','颈部','胸口','后心','左肩','右肩','左臂','右臂','左手','右手','腰间','小腹','左腿','右腿','左脚','右脚'][Math.randomInt(16)])
                targets[0].result().skill = item;
                this.push('addText', msg);
            }
            if (item.message2) {
                this.push('addText', item.message2.format(item.name));
            }
        } else {
            this.push('addText', TextManager.useItem.format(subject.name(), item.name));
        }
        if (this._methods.length === numMethods) {
            this.push('wait');
        }
    };
    //此处处理受伤和躲避的文字显示
    Window_BattleLog.prototype.makeHpDamageText = function(target) {
        var result = target.result();
        var damage = result.hpDamage;
        var isActor = target.isActor();
        var fmt;//result.hitPart决定哪里受伤害
        if (damage > 0 && result.drain) {//吸血
            fmt = isActor ? TextManager.actorDrain : TextManager.enemyDrain;
            return fmt.format(target.name(), TextManager.hp, damage);
        } else if (damage > 0) {//伤害
            var damType = result.skill.damage.elementId;
            var mhp = target.mfhp;
            var hpnow = result.hpDamage;
            //var rate = Math.min(hpnow,mhp) / mhp;
            var rate = (mhp - Math.max(0,target.hp)) / mhp;
            if(damType==1) {//拳
                fmt=[
                    '结果只是轻轻地碰到SB',
                    '结果在SB的伤处造成一处瘀青',
                    '结果一击命中，SB被打肿了一块老高',
                    '结果一击命中，SB痛苦地闷哼了一声',
                    '结果『砰』的一声，SB退了两步',
                    '结果SB连退了好几步，差一点摔倒',
                    '结果重重的击中，SB吐出一口鲜血',
                    '结果一声巨响，SB像捆稻草般飞了出去'
                ][parseInt(rate * 7)];
            }
            else if(damType==2){//掌
                fmt=[
                    '结果SB退了半步，毫发无损',
                    '结果给SB造成一处瘀伤',
                    '结果一击命中，SB痛得弯下腰',
                    '结果SB痛苦地闷哼了一声，显然受了点内伤',
                    '结果SB摇摇晃晃，一跤摔倒在地',
                    '结果SB脸色一下变得惨白，连退了好几步',
                    '结果『轰』的一声，SB口中鲜血狂喷而出',
                    '结果SB一声惨叫，像滩软泥般塌了下去'
                ][parseInt(rate * 7)];
            }
            else if(damType==3){//爪
                fmt=[
                    '只是抓破SB的一点皮',
                    '结果SB被抓出了五条淡淡的血痕',
                    '结果抓下了SB一小块皮肉',
                    '结果一爪命中，SB被抓出了五条血沟',
                    '结果SB皮开肉破，鲜血流了下来',
                    '结果SB被抓出了五个血洞，鲜血急喷',
                    '结果SB被连皮带肉抓下了一大块，露出了骨头',
                    '结果『喀嚓』一声，SB的骨头被抓得粉碎'
                ][parseInt(rate * 7)];   
            }
            else if(damType==4){//割
                fmt=[
                    '只轻轻地划破SB的皮肉',
                    '结果划出一道细长的血痕',
                    '结果SB被割出了一道伤口',
                    '结果SB被划出了一道血淋淋的伤口',
                    '结果SB被划出了一道又长又深的伤口',
                    '结果SB被砍出了一道深及见骨的可怕伤口'
                ][parseInt(rate * 5)];
            }
            else if(damType==5){//刺
                fmt=[
                    '结果只轻轻地刺破SB皮肉',
                    '结果在SB身上刺出一个创口',
                    '结果SB被刺入了寸许',
                    '结果刺得SB退了几步',
                    '结果刺出一个血肉模糊的伤口',
                    '结果SB被刺了个透明窟窿，鲜血飞溅'
                ][parseInt(rate * 5)];
            }
            else if(damType==6){//雷
                fmt=[
                    '结果“嗤！”地一声，SB闻到了一股焦味，还好只烧掉了几根头发。',
                    '结果SB被震得全身一抖。',
                    '结果SB被震得全身一颤。',
                    '结果SB被雷电炸了个满脸花。',
                    '结果SB被雷电震得脚步踉跄，连退几步。',
                    '结果雷鸣电闪中SB眉发全焦，被炸得浑身发黑。',
                    '结果雷电交加中SB双眼一翻白，哼也没哼瘫软在地。'
                ][parseInt(rate * 6)];
            }
            else if(damType==7){//火
                fmt=[
                    '结果SB只是被火星烫了一下。',
                    '结果SB被火烧起了一个小水泡。',
                    '结果SB被火烧掉一大片皮，连声呼痛。',
                    '结果SB被火烧得浑身冒烟，狼狈不堪。',
                    '结果SB全身着火，不住在地上翻滚号呼。',
                    '结果SB身陷火窟，长声惨呼中传来阵阵刺鼻焦臭。'
                ][parseInt(rate * 5)];
            }
            else if(damType==8){//冰
                fmt=[
                    '结果SB只是冻得打了个喷嚏。',
                    '结果SB被冻得连打冷颤。',
                    '结果SB被冻得脸色发青，嘴唇发紫。',
                    '结果SB被冻气所伤，周身结了一层白霜。',
                    '结果寒气弥漫中，SB全身冻出了一层薄冰。',
                    '结果SB竟被冻成一尊晶莹剔透的冰雕。'
                ][parseInt(rate * 5)];
            }
            else
            {
                fmt=[
                    '没有造成任何伤害',
                    '结果对SB造成了轻微的伤害',
                    '结果对SB造成了一处伤害',
                    '结果对SB造成了颇为严重的伤害',
                    '结果对SB造成了相当严重的伤害',
                    '结果对SB造成了十分严重的伤害',
                    '结果对SB造成了极其严重的伤害',
                    '结果对SB造成了非常可怕的严重伤害'
                ][parseInt(rate * 7)];
            }
            fmt = fmt.replace(/SB/g,target.name())
// 万鸦咒
// 火焰弹——你用手一指，唰的一声，一个火球向SB飞去。
// 烈焰球——你身形一转，周围一片火海，烈火熊熊，半空中一个巨大的火球砸向SB。
// 三昧火——你双掌磨擦念动真言：喝声“疾！”。双掌一分，一股丈余长烈焰直向SB扑面而来！
// 成功——SB被烈焰包围，随着“嗤”！一阵焦臭，SB的物品也被烧了个干净！
// 失败——SB识得厉害，大叫一声“不好”，一个懒驴打滚滚出数丈，狼狈万状地避开！
// 火风暴——烈焰球×3。
// 成功——Ⅰ结果SB身陷火窟，长声惨呼中传来阵阵刺鼻焦臭。
// Ⅱ结果SB全身着火，不住在地上翻滚号呼。
// Ⅲ结果SB被火烧得浑身冒烟，狼狈不堪。
// Ⅳ结果SB被火烧掉一大片皮，连声呼痛。
// Ⅴ结果SB被火烧起了一个小水泡。
// Ⅵ结果SB只是被火星烫了一下。
// 失败——但是火球被SB法力一引，反而砸到了你的顶门。
// 平手——但SB在千钧一发之际躲了开来。
// 附加描述——“啪”的一团火焰在SB脚下燃起，将SB拢在中央。
// 玄冰咒
// 寒冰弹——你口中念咒，祭起一个冰球打向SB。
// 雾棱镖——你口中念咒，周围忽然雾茫茫一片，一支冰镖悄无声息地飞向SB。
// 暴风雪——你在手中划了个咒，望天一指，只见顿时天上雪舞风刮，大大小小的雪片冰雹向SB飘了（过去）。
// 成功——结果SB被冻得手足僵硬，动弹不得！
// 失败——SB回手一挥，漫天飞雪顿时淡了下去！
// 冰凌剑——雾棱镖×3。
// 成功——Ⅰ结果SB竟被冻成一尊晶莹剔透的冰雕。
// Ⅱ结果寒气弥漫中，SB全身冻出了一层薄冰。
// Ⅲ结果SB被冻气所伤，周身结了一层白霜。
// Ⅳ结果SB被冻得脸色发青，嘴唇发紫。
// Ⅴ结果SB被冻得连打冷颤。
// Ⅵ结果SB只是冻得打了个喷嚏。
// 失败——但是冰球/镖被SB以法力一引，反而砸到了你的顶门。
// 平手——可是SB经验老到，连消带打化解了这凌厉的一击。
// 附加描述——SB现在呆若木鸡。
// 五雷咒
// 闪光弹——你念动真言，掌中祭起一团烁烁金光，向SB劈面掷去。
// 雷火弹——你掐指念咒，电光缭绕雷声大作，一团雷火直袭SB。
// 掌心雷——你披发念咒，周身电光闪烁，手掌猛向SB张开，一个霹雳直劈过去。
// 连珠雷——雷火弹×2＋闪光弹×1
// 成功——Ⅰ结果雷电交加中SB双眼一翻白，哼也没哼瘫软在地。
// Ⅱ结果雷鸣电闪中SB眉发全焦，被炸得浑身发黑。
// Ⅲ结果SB被雷电震得脚步踉跄，连退几步。
// Ⅳ结果SB被雷电炸了个满脸花。
// Ⅴ结果SB被震得全身一颤。
// Ⅵ结果SB被震得全身一抖。
// Ⅶ结果“嗤！”地一声，SB闻到了一股焦味，还好只烧掉了几根头发。
// 失败——但金光/雷火/霹雳被SB以法力反激，反向你射去。
// 附加描述——SB头晕眼花，攻势不由一缓。
            //fmt = isActor ? TextManager.actorDamage : TextManager.enemyDamage;
            //return fmt.format(target.name(), damage);
            return fmt;
        } else if (damage < 0) {//恢复
            fmt = isActor ? TextManager.actorRecovery : TextManager.enemyRecovery;
            return fmt.format(target.name(), TextManager.hp, -damage);
        } else {//没伤害
            fmt = isActor ? TextManager.actorNoDamage : TextManager.enemyNoDamage;
            return fmt.format(target.name());
        }//还得判断是否招架绝招什么的老了去了
    };
})();
