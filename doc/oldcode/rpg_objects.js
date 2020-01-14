(function(){
    Game_Map.prototype.getCurrentGameEvent = function(){
        //$gameMap._interpreter._eventId
        return this.event(this._interpreter._eventId);
    }

    //增加根据actor生成对战敌人的功能
    Game_Troop.prototype.setup = function(troopId,actorIds=[]) {
        this.clear();
        this._troopId = troopId;
        this._enemies = [];
        //根据参数传来的actor数组添加敌人。
        actorIds.forEach(function(actorId){
            var dEnemy = $dataEnemies[1];
            var dActor = $dataActors[actorId];
            dEnemy.battlerName = dActor.battlerName;
            //处理攻击方式和掉落物品还有特性
            //actions,dropItems,traits
            //dEnemy.gold
            dEnemy.name = dActor.name;
            //处理三围
            var enemy = new Game_Enemy(1, 129, 325,actorId);
            this._enemies.push(enemy);
        }, this);
        //根据敌群添加敌人
        this.troop().members.forEach(function(member) {
            if ($dataEnemies[member.enemyId]) {
                var enemyId = member.enemyId;
                var x = member.x;
                var y = member.y;
                var enemy = new Game_Enemy(enemyId, x, y);
                if (member.hidden) {
                    enemy.hide();
                }
                this._enemies.push(enemy);
            }
        }, this);

        this.makeUniqueNames();
    };
    //判断是否是普通进攻技能
    Game_Action.prototype.isAttack = function() {
        if(this.item().stypeId==10||this.item().stypeId==7) {//绝招或者法术
            return false;
        }
        else {
            return true;
        }
        //return this.item() === $dataSkills[this.subject().attackSkillId()];
    };
    //处理事件的结果，暂时没修改
    Game_Action.prototype.apply = function(target) {
        var result = target.result();
        this.subject().clearResult();
        result.clear();
        result.used = this.testApply(target);
        result.missed = (result.used && Math.random() >= this.itemHit(target));
        result.evaded = (!result.missed && Math.random() < this.itemEva(target));
        result.physical = this.isPhysical();
        result.drain = this.isDrain();
        if (result.isHit()) {
            // if (this.isAttack()) {//如果是攻击技能就设定攻击哪里，先瞎写几个，我都忘了有哪些了
            //     result.hitPart = ["头部","腹部","双腿"][Math.randomInt(3)];
            // }
            if (this.item().damage.type > 0) {
                result.critical = (Math.random() < this.itemCri(target));
                var value = this.makeDamageValue(target, result.critical);
                this.executeDamage(target, value);
            }
            this.item().effects.forEach(function(effect) {
                this.applyItemEffect(target, effect);
            }, this);
            this.applyItemUserEffect(target);
        }
    };
})();
(function(){
    //增加触摸移动后的回调函数
    Game_Temp.prototype.setEndTouchMovingCallback=function(callback){
        this.onEndTouchMoving=callback;
    };
    Game_Player.prototype.updateNonmoving = function(wasMoving) {
        if (!$gameMap.isEventRunning()) {
            if (wasMoving) {
                $gameParty.onPlayerWalk();
                this.checkEventTriggerHere([1,2]);
                if ($gameMap.setupStartingEvent()) {
                    return;
                }
            }
            if (this.triggerAction()) {
                return;
            }
            if (wasMoving) {
                this.updateEncounterCount();
            } else {
                $gameTemp.clearDestination();
                if($gameTemp.onEndTouchMoving){
                    $gameTemp.onEndTouchMoving();
                    delete $gameTemp.onEndTouchMoving;
                }
            }
        }
    };
    /**
     * 在解析器中创建一个执行js回调函数的指令
     * 多用于在js文件中解析器指令和JS命令结合使用的情况，如果在js中使用解析器355指令，还需要用字符串形式编写js脚本，太麻烦了
     * 指令名为‘CallBack’
     * 
     * 用法如下(rt为Game_Interpreter实例)
     *   rt._list = rt._list.concat([
     *      {"code":'CallBack',"indent":1,"parameters":function(){alert('这是一个回调函数')}}
     *   ])
     */
    Game_Interpreter.prototype.commandCallBack = function() {
        if(!this._params||this._params.length==0) {return true};
        if(typeof this._params[0] === 'function')
        {
            this._params[0](this);
        }
        return true;
    };
    /**
     * 在解析器当前执行的命令后面插入命令
     */
    Game_Interpreter.prototype.insertCommand = function(cmds) {
        var rtListTotal = this._list.length;
        this._list = this._list.slice(0,this._index+1)
            .concat(cmds)
            .concat(this._list.slice(this._index+1,rtListTotal));
    }
})();

(function(){

})();

