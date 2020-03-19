(() => {
  const {
    DataManager,
    _gbb: { Window_PowerBar, Window_Text, basePath }
  } = window;
  DataManager.loadMapData(
    1001,
    false,
    "Map.json",
    `js/plugins/${basePath}data/`
  );
  let onMapLoadedRestore, onSceneMapUpdateRestore, isMenuEnabledRestore;
  const BasketballManager = new StateMachine({
    init: "load",
    transitions: [
      { name: "showMenu", from: "*", to: "menu" },
      { name: "startGame", from: "*", to: "power" },
      { name: "throwBall", from: "*", to: "ball" },
      { name: "showScore", from: "*", to: "score" },
      { name: "showConfirm", from: "*", to: "confirm" },
      { name: "endGame", from: "*", to: "unload" }
    ],
    data: {},
    methods: {
      enter: function() {
        const {
          $gamePlayer: { _x: x, _y: y, _direction: direction },
          $gameMap: { _mapId: mapId, _interpreter: itpr }
        } = window;
        this._CustomWindows = this._CustomWindows || [];
        this._actorIndex = 0;
        this._touchCount = 0;
        this._score = 0;
        this._totalScore = 0;
        //记录之前位置
        this._locHistory = { x, y, direction, mapId };
        //插入自定元素
        this._powerWindow = new Window_PowerBar();
        this._CustomWindows.push(this._powerWindow);
        this._textWindow = new Window_Text();
        this._CustomWindows.push(this._textWindow);
        this._hackSceneMap();
        new $mvs(itpr)
          .transferPlayerDirect(1001, -1, -1, 0, 0)
          .callJsFunction(() => this.op("showMenu"))
          .done();
      },
      onMenu: function() {
        const {
          $gameMap: { _interpreter: itpr }
        } = window;
        const indent = itpr._indent;
        const evchar = $gameMap.event(3);
        const char = $gameParty.allMembers()[this._actorIndex];
        evchar._characterName = char._characterName;
        evchar._characterIndex = char._characterIndex;
        evchar._direction = 6;

        this._powerWindow.close();
        this._textWindow.close();
        new $mvs(itpr)
          .showText(["", "", "                      ==投篮游戏=="], "", 0, 1, 0)
          .showChoices(["开始游戏", "游戏说明", "退出游戏"], 2, 0, 1, 0)
          .whenChoicesIndex(0, subCommand => {
            subCommand.callJsFunction(() => this.op("startGame"));
          })
          .whenChoicesIndex(1, subCommand => {
            subCommand
              .showScrollingText(["这是一段很长很长的说明啊啊啊啊啊啊啊啊啊"])
              .callJsFunction(() => this.op("onMenu"));
          })
          .whenChoicesIndex(2, subCommand => {
            subCommand.callJsFunction(() => this.op("endGame"));
          })
          .endWhen()
          .done();
      },
      onPower: function() {
        this._powerWindow.open();
      },
      onBall: function() {
        const {
          $gameMap: { _interpreter: itpr }
        } = window;
        this._powerWindow.refresh();
        this._score = this._powerWindow.getScore();
        this._totalScore += this._score;

        new $mvs(itpr)
          .setMovementRoute(2, routeCommand =>
            routeCommand.jump(0, 0).getCommands(false, false, false)
          )
          .setMovementRoute(3, routeCommand =>
            routeCommand.jump(0, 0).getCommands(false, false, true)
          )
          .whenFunctionIs(
            () => this._score === 2,
            subCommand => {
              subCommand.showAnimation(2, 2, false);
            }
          )
          .endWhen()
          .setMovementRoute(2, routeCommand => {
            let [x, y] = [12, 0];
            if (this._score === 0) {
              y = 1;
              x = x - parseInt(Math.random() * 3) - 1;
            }
            return routeCommand.jump(x, y).getCommands(false, false, true);
          })
          .whenFunctionIs(
            () => this._score > 0,
            subCommand => subCommand.playSE("Wind7", 90, 130)
          )
          .endWhen()
          .setMovementRoute(3, routeCommand =>
            routeCommand.getCommands(false, false, true)
          )
          .setEventLocationDirect(2, 2, 10, 0)
          .callJsFunction(() => this.op("showScore"))
          .wait(60)
          .done();
      },
      onScore: function() {
        if (this._score > 0) {
          this._textWindow.open();
          this._textWindow.setText(`得分：${this._score}`);
        }
        this.op("startGame");
      },
      onConfirm: function() {
        const {
          $gameMap: { _interpreter: itpr }
        } = window;
        const indent = itpr._indent;
        new $mvs(itpr)
          .showText([`共得分${this._totalScore}，是否结束？`])
          .showChoices(["是", "否"], 1, 0, 2, 0)
          .whenChoicesIndex(0, subCommand => {
            subCommand.callJsFunction(() => this.op("showMenu"));
          })
          .whenChoicesIndex(1, subCommand => {
            subCommand.callJsFunction(() => this.op("startGame"));
          })
          .endWhen()
          .done();
      },
      update: function(map) {
        if (this.is("power")) {
          this.throwBall._touchCount = this.throwBall._touchCount || 0;
          if (TouchInput.isTriggered() || this.throwBall._touchCount > 0) {
            if (TouchInput.isPressed()) {
              if (
                this.throwBall._touchCount === 0 ||
                this.throwBall._touchCount >= 15
              ) {
                this.op("throwBall", true);
              }
              this.throwBall._touchCount++;
            } else {
              this.throwBall._touchCount = 0;
            }
          }
          if (map.isMenuCalled()) {
            this.op("showConfirm", true);
          }
          this._powerWindow.nextValue();
        }
      },
      onUnload: function() {
        const {
          $gameMap: { _interpreter: itpr }
        } = window;
        const { x, y, direction, mapId } = this._locHistory;
        new $mvs(itpr).transferPlayerDirect(mapId, x, y, direction, 0).done();
        this._restoreSceneMap();
      },
      _hackSceneMap: async function() {
        const me = this;
        isMenuEnabledRestore = PluginManager.regHook(
          "Scene_Map.prototype.isMenuEnabled",
          () => () => false
        );
        //劫持onMapLoaded显示自定义窗口
        onMapLoadedRestore = PluginManager.regHook(
          "Scene_Map.prototype.onMapLoaded",
          oFunc =>
            function() {
              oFunc();
              me._CustomWindows.forEach(child => {
                child.close();
                this.addWindow(child);
              });
            }
        );
        //劫持update
        onSceneMapUpdateRestore = PluginManager.regHook(
          "Scene_Map.prototype.update",
          oFunc =>
            function() {
              oFunc();
              me.update(this);
            }
        );
      },
      _restoreSceneMap: function() {
        // 恢复之前的劫持
        isMenuEnabledRestore();
        onSceneMapUpdateRestore();
        onMapLoadedRestore();
      },
      op: function(fn, rightNow = false) {
        if (rightNow) {
          this[fn]();
        } else {
          setTimeout(() => {
            this[fn]();
          }, 0);
        }
      }
    }
  });
  window.BasketballManager = BasketballManager;
})();
