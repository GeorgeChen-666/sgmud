(() => {
      /**
       * 注册RMMV 函数钩子，当key函数被调用后，自动执行钩子函数。
       */
      PluginManager.regHook = function(key, dFunc) {
        const keys = key.split(".");
        const sFuncName = keys.pop();
        const sObj = keys.reduce((pv, nv) => pv[nv] || {}, window);
        const sFunc = sObj[sFuncName];
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
        return new Promise(rs => {
          if ($gameSystem) {
            doHook();
            rs(sObj[sFuncName]);
          } else {
            const jobRegHook = setInterval(() => {
              if ($gameSystem) {
                doHook();
                clearInterval(jobRegHook);
                rs(sObj[sFuncName]);
              }
            }, 100);
          }
        });
      };
      /**
       * 解除钩子函数
       */
      PluginManager.delHook = function(key, dFunc) {
        const keys = key.split(".");
        const sFuncName = keys.pop();
        const sObj = keys.reduce((pv, nv) => pv[nv] || {}, window);
        const sFunc = sObj[sFuncName];
        let lastFunc = null;
        let currentFunc = sFunc;
        while (currentFunc !== null && currentFunc !== undefined) {
          if (currentFunc === dFunc) {
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
})();
