(() => {
  /**
   * 注册RMMV 函数钩子，当key函数被调用后，自动执行钩子函数。
   */
  PluginManager.regHook = function(key, dFunc, isCallOriginal = true) {
    const keys = key.split('.');
    if (keys.length !== 2 && keys.length !== 3) {
      return undefined;
    }
    const sFuncName = keys.pop();
    const sObj = keys.reduce((pv, nv) => pv[nv] || {}, window);
    const jobRegHook = setInterval(() => {
      if ($gameSystem) {
        if (sObj[sFuncName].__isHooked__) {
          sObj[sFuncName].__HookedFuncs__.push(dFunc);
          sObj[sFuncName].__isCallOriginal__ = isCallOriginal;
        } else {
          const sFunc = sObj[sFuncName];
          const sFuncNew = function() {
            const {
              __isCallOriginal__,
              __OriginalFunc__,
              __HookedFuncs__
            } = sFuncNew;
            __isCallOriginal__ && __OriginalFunc__.call(this);
            __HookedFuncs__.forEach(func => func.call(this));
          };
          sFuncNew.__isHooked__ = true;
          sFuncNew.__isCallOriginal__ = isCallOriginal;
          sFuncNew.__OriginalFunc__ = sFunc;
          sFuncNew.__HookedFuncs__ = [dFunc];
          sObj[sFuncName] = sFuncNew;
        }
        clearInterval(jobRegHook);
      }
    }, 100);
    return dFunc;
  };
  /**
   * 解除钩子函数
   */
  PluginManager.delHook = function(key, dFunc) {
    const keys = key.split('.');
    if (keys.length !== 2 && keys.length !== 3) {
      return false;
    }
    const sFuncName = keys.pop();
    const sObj = keys.reduce((pv, nv) => pv[nv] || {}, window);
    if (
      sObj[sFuncName] === undefined ||
      !sObj[sFuncName].__isHooked__ ||
      !sObj[sFuncName].__HookedFuncs__.includes(dFunc)
    ) {
      return false;
    } else {
      const { __HookedFuncs__ = [] } = sObj[sFuncName];
      sObj[sFuncName].__HookedFuncs__ = __HookedFuncs__.filter(
        func => func !== dFunc
      );
      return true;
    }
  };
  /**
   * 与PluginManager.regHook类似，但是这个是暂时注册替代函数，而且只能注册一次。
   * regDelegate后目标函数被替换成自定义函数，restoreDelegate后恢复原样。
   */
  PluginManager.regDelegate = function(key, dFunc) {
    const keys = key.split('.');
    if (keys.length !== 2 && keys.length !== 3) {
      return undefined;
    }
    const sFuncName = keys.pop();
    const sObj = keys.reduce((pv, nv) => pv[nv] || {}, window);
    if(sObj[sFuncName].__isDelegated__) {
      throw new Error("仅能托管一次，请先delDelegate");
    } else {
      const jobRegDelegate = setInterval(() => {
        if ($gameSystem) {
          const sFunc = sObj[sFuncName];
          const sFuncNew = function() {
            sFuncNew.__DelegatedFunc__.call(this);
          };
          sFuncNew.__isDelegated__ = true;
          sFuncNew.__OriginalFunc__ = sFunc;
          sFuncNew.__DelegatedFunc__ = dFunc;
          sObj[sFuncName] = sFuncNew;
          clearInterval(jobRegDelegate);
        }
      }, 100);
    }
  };
  PluginManager.restoreDelegate = function(key) {
    const keys = key.split('.');
    if (keys.length !== 2 && keys.length !== 3) {
      return false;
    }
    const sFuncName = keys.pop();
    const sObj = keys.reduce((pv, nv) => pv[nv] || {}, window);
    if (
      sObj[sFuncName] === undefined ||
      !sObj[sFuncName].__isDelegated__
    ) {
      return false;
    } else {
      sObj[sFuncName] = sObj[sFuncName].__OriginalFunc__;
      return true;
    }
  };
})();
