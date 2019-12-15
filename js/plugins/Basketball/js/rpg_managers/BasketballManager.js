(()=>{
  const { 
    DataManager, 
    Scene_Map,
    _gbb:{ Window_PowerBar, Window_Text } 
  } = window;
  DataManager.loadMapData(1001,false,'Map.json','js/plugins/Basketball/data/');
  let onMapLoaded,onSceneMapUpdate,isMenuEnabled;
  const BasketballManager = new StateMachine({
    init: 'load',
    transitions: [
      { name: 'showMenu', from: '*',  to: 'menu' },
      { name: 'startGame', from: '*', to: 'power'},
      { name: 'throwBall', from: '*', to: 'ball'},
      { name: 'showScore', from: '*', to: 'score' },
      { name: 'showConfirm', from: '*', to: 'confirm'},
      { name: 'endGame', from: '*', to: 'unload'},
    ],
    data: {
    },
    methods: {
      enter: function() {
        const {
          $gamePlayer: {_x: x, _y: y, _direction: direction},
          $gameMap:{
            _mapId: mapId,
            _interpreter:itpr,
          }
        } = window;
    
        this._CustomWindows = (this._CustomWindows||[]);
        this._actorIndex = 0;
        this._touchCount = 0;
        this._score = 0;
        this._totalScore = 0;
        //记录之前位置
        this._locHistory = { x, y, direction, mapId,};
        //插入自定元素
        this._powerWindow = new Window_PowerBar();
        this._CustomWindows.push(this._powerWindow);
        this._textWindow = new Window_Text();
        this._CustomWindows.push(this._textWindow);
        this._hackSceneMap();
    
        itpr.insertCommands([
          {"code":201,"indent":0,"parameters":[0,1001,-1,-1,0,0]},
          {"code":355,"indent":0,"parameters":[()=>{
            this.op('showMenu');
          }]},
          {"code":0,"indent":0,"parameters":[]}
        ]);
      },
      onMenu: function() { 
        const {
          $gameMap:{
            _interpreter:itpr,
          }
        } = window;
        const evchar = $gameMap.event(3);
        const char = $gameParty.allMembers()[this._actorIndex];
        evchar._characterName = char._characterName;
        evchar._characterIndex = char._characterIndex;
        evchar._direction = 6;
    
        this._powerWindow.close();
        this._textWindow.close();
        itpr.insertCommands([
          {"code":101,"indent":0,"parameters":["",0,1,0]},
          {"code":401,"indent":0,"parameters":[""]},
          {"code":401,"indent":0,"parameters":[""]},
          {"code":401,"indent":0,"parameters":["                      ==投篮游戏=="]},
          {"code":102,"indent":0,"parameters":[["开始游戏","游戏说明","退出游戏"],2,0,1,0]},
          {"code":402,"indent":0,"parameters":[0,"开始游戏"]},
          {"code":355,"indent":1,"parameters":[()=>{
            this.op('startGame');
          }]},
          {"code":402,"indent":0,"parameters":[1,"游戏说明"]},
          {"code":105,"indent":1,"parameters":[2,false]},
          {"code":405,"indent":1,"parameters":["这是一段很长很长的说明啊啊啊啊啊啊啊啊啊"]},
          {"code":355,"indent":1,"parameters":[()=>{
            this.op('onMenu');
          }]},
          {"code":0,"indent":1,"parameters":[]},
          {"code":402,"indent":0,"parameters":[2,"退出游戏"]},
          {"code":355,"indent":1,"parameters":[()=>{
            this.op('endGame');
          }]},
          {"code":0,"indent":1,"parameters":[]},
          {"code":404,"indent":0,"parameters":[]},
        ]);
      },
      onPower: function() {
        this._powerWindow.open();
      },
      onBall: function() {
        const {
          $gameMap:{
            _interpreter:itpr,
          }
        } = window;
        let [x, y] = [12, 0];
        this._powerWindow.refresh();
        this._score = this._powerWindow.getScore();
        this._totalScore += this._score;
        if(this._score === 0) {
          y = 1;
          x = x - parseInt(Math.random() * 3) - 1;
        }
        itpr.insertCommands([
          {"code":205,"indent":0,"parameters":[2,{"list":[{"code":14,"parameters":[0,0],"indent":null},{"code":0}],"repeat":false,"skippable":false,"wait":false}]},
          {"code":505,"indent":0,"parameters":[{"code":14,"parameters":[0,0],"indent":null}]},
          {"code":205,"indent":0,"parameters":[3,{"list":[{"code":14,"parameters":[0,0],"indent":null},{"code":0}],"repeat":false,"skippable":false,"wait":true}]},
          {"code":505,"indent":0,"parameters":[{"code":14,"parameters":[0,0],"indent":null}]},
          {"code":111,"indent":0,"parameters":[()=>{
            return (this._score == 2);
          }]},
          {"code":212,"indent":1,"parameters":[2,2,false]},
          {"code":0,"indent":1,"parameters":[]},
          {"code":412,"indent":0,"parameters":[]},
          {"code":205,"indent":0,"parameters":[2,{"list":[{"code":14,"parameters":[x,y],"indent":null},{"code":0}],"repeat":false,"skippable":false,"wait":true}]},
          {"code":505,"indent":0,"parameters":[{"code":14,"parameters":[x,y],"indent":null}]},
          {"code":111,"indent":0,"parameters":[()=>{
            return (this._score > 0);
          }]},
          {"code":250,"indent":1,"parameters":[{"name":"Wind7","volume":90,"pitch":130,"pan":0}]},
          {"code":412,"indent":0,"parameters":[]},
          {"code":205,"indent":0,"parameters":[3,{"list":[{"code":0}],"repeat":false,"skippable":false,"wait":true}]},
          {"code":203,"indent":0,"parameters":[2,0,2,10,0]},
          {"code":355,"indent":0,"parameters":[()=>{
            this.op('showScore');
          }]},
          {"code":230,"indent":0,"parameters":[60]},
          {"code":0,"indent":0,"parameters":[]}
        ]);
      },
      onScore: function() {
        if(this._score > 0) {
          this._textWindow.open();
          this._textWindow.setText(`得分：${this._score}`)
        }
        this.op('startGame');
      },
      onConfirm: function() {
        const {
          $gameMap:{
            _interpreter:itpr,
          }
        } = window;
        itpr.insertCommands([
          {"code":101,"indent":0,"parameters":["",0,0,2]},
          {"code":401,"indent":0,"parameters":[`共得分${this._totalScore}，是否结束？`]},
          {"code":102,"indent":0,"parameters":[["是","否"],1,0,2,0]},
          {"code":402,"indent":0,"parameters":[0,"是"]},
          {"code":355,"indent":1,"parameters":[()=>{
            this.op('showMenu');
          }]},
          {"code":0,"indent":1,"parameters":[]},
          {"code":402,"indent":0,"parameters":[1,"否"]},
          {"code":355,"indent":1,"parameters":[()=>{
            this.op('startGame');
          }]},
          {"code":0,"indent":1,"parameters":[]},
          {"code":404,"indent":0,"parameters":[]},
          {"code":0,"indent":0,"parameters":[]}
        ]);
      },
      update: function(map) {
        if(this.is('power')) {
          this.throwBall._touchCount = this.throwBall._touchCount || 0 ;
          if (TouchInput.isTriggered() || this.throwBall._touchCount > 0) {
            if (TouchInput.isPressed()) {
              if (this.throwBall._touchCount === 0 || this.throwBall._touchCount >= 15) {
                this.op('throwBall',true);
              }
              this.throwBall._touchCount++;
            } else {
              this.throwBall._touchCount = 0;
            }
          }
          if(map.isMenuCalled()) {
            this.op('showConfirm',true);
          }
          this._powerWindow.nextValue();
        }
      },
      onUnload: function() {
        const {
          $gameMap:{
            _interpreter:itpr,
          }
        } = window;
        const { x, y, direction, mapId,} = this._locHistory;
        itpr.insertCommands([{"code":201,"indent":0,"parameters":[0,mapId,x,y,direction,0]}]);
        this._restoreSceneMap();
      },
      _hackSceneMap: function() {
        const me = this;
        isMenuEnabled = Scene_Map.prototype.isMenuEnabled;
        Scene_Map.prototype.isMenuEnabled = function () {
          return false;
        }
        //劫持onMapLoaded显示自定义窗口
        onMapLoaded = Scene_Map.prototype.onMapLoaded;
        Scene_Map.prototype.onMapLoaded = function() {
          onMapLoaded.call(this);
          me._CustomWindows.forEach(child => {
            child.close();
            this.addWindow(child);
          });
        };
        //劫持update
        onSceneMapUpdate = Scene_Map.prototype.update;
        Scene_Map.prototype.update = function () {
          onSceneMapUpdate.call(this);
          me.update(this);
        };
      },
      _restoreSceneMap: function() {
        // 恢复之前的劫持
        Scene_Map.prototype.onMapLoaded = onMapLoaded;
        Scene_Map.prototype.update = onSceneMapUpdate;
        Scene_Map.prototype.isMenuEnabled = isMenuEnabled;
      },
      op: function(fn,rightNow = false) {
        if(rightNow) {
          this[fn]()
        } else {
          setTimeout(()=>{
            this[fn]()
          },0);
        }
      },
    }
  });
  window.BasketballManager = BasketballManager;
})()
