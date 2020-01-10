(() => {
  function* getKeyObjects(key) {
    const keys = key.split('.');
    const sFuncName = keys.pop();
    const sObj = keys.reduce((pv, nv) => pv[nv] || {}, window);
    const sFunc = sObj[sFuncName];
    yield sObj;
    yield sFunc;
    yield sFuncName;
  }
  /**
   * 注册RMMV 函数钩子，当key函数被调用后，自动执行钩子函数。
   */
  PluginManager.regHook = function(key, dFunc) {
    const [sObj, sFunc, sFuncName] = [...getKeyObjects(key)];
    const doHook = () => {
      const sFuncNew = function(...args) {
        const { __SourceFunc__, __HookedFunc__ } = sFuncNew;
        const doCallSource = () => __SourceFunc__.apply(this, args);
        return __HookedFunc__(doCallSource).apply(this, args);
      };
      sFuncNew.__SourceFunc__ = sFunc;
      sFuncNew.__HookedFunc__ = dFunc;
      sObj[sFuncName] = sFuncNew;
    };
    if ($gameSystem) {
      doHook();
    } else {
      Scene_Boot.Totos.push(doHook);
    }
    return dFunc;
  };
  PluginManager.regBatchHooks = function(hookMap={}) {
    return Object.keys(hookMap).map(key => PluginManager.regHook(key, hookMap[key]));
  };
  /**
   * 解除钩子函数
   */
  PluginManager.delHook = function(key, dFunc) {
    const [sObj, sFunc, sFuncName] = [...getKeyObjects(key)];
    const doUnHook = () => {
      let lastFunc = null;
      let currentFunc = sFunc;
      while (currentFunc !== null && currentFunc !== undefined) {
        if (currentFunc.__HookedFunc__ === dFunc) {
          if (lastFunc === null) {
            sObj[sFuncName] = currentFunc.__SourceFunc__;
          } else {
            lastFunc.__SourceFunc__ = currentFunc.__SourceFunc__;
            currentFunc.__SourceFunc__ = null;
          }
          break;
        }
        lastFunc = currentFunc;
        currentFunc = currentFunc.__SourceFunc__;
      }
    };
    if ($gameSystem) {
      doUnHook();
    } else {
      Scene_Boot.Totos.push(doUnHook);
    }
  };
  /**
   * 当Hook以后我们无法直接覆盖原始的函数，可以通过下面两个函数覆盖和恢复原始函数。
   */
  PluginManager.setHookOriginal = function(key, dFunc) {
    const [, sFunc] = [...getKeyObjects(key)];
    let lastFunc = null;
    let currentFunc = sFunc;
    while (currentFunc !== null && currentFunc !== undefined) {
      if (currentFunc.__SourceFunc__ === undefined && lastFunc !== undefined) {
        lastFunc.__SourceFuncBak__ = lastFunc.__SourceFunc__;
        lastFunc.__SourceFunc__ = dFunc;
        return;
      }
      lastFunc = currentFunc;
      currentFunc = currentFunc.__SourceFunc__;
    }
  };
  PluginManager.restoreHookOriginal = function(key) {
    const [, sFunc] = [...getKeyObjects(key)];
    let lastFunc = null;
    let currentFunc = sFunc;
    while (currentFunc !== null && currentFunc !== undefined) {
      if (currentFunc.__SourceFunc__ === undefined && lastFunc !== undefined) {
        lastFunc.__SourceFunc__ = lastFunc.__SourceFuncBak__;
        return;
      }
      lastFunc = currentFunc;
      currentFunc = currentFunc.__SourceFunc__;
    }
  };
})();
