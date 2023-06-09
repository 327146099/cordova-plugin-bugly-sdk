!function (a, b) {

  function j (a, b) {
    try {
      if ('function' != typeof a) return a
      if (!a.Bugly) {
        var f = m()
        a.Bugly = function (h) {
          if (b && b.eventHandler && (d = h), e = f, !g) {
            var j = a.apply(this, arguments)
            return e = null, j
          }
          try {
            return a.apply(this, arguments)
          } catch (i) {
            throw c.reportException(i, null, null, 'error'), u(), i
          } finally {e = null}
        }, a.Bugly.Bugly = a.Bugly
      }
      return a.Bugly
    } catch (h) {return a}
  }

  function l () {k = !1}

  function m () {
    var b, a = document.currentScript || e
    return !a && k && (b = document.scripts ||
      document.getElementsByTagName('script'), a = b[b.length - 1]), a
  }

  function q (b) {
    var c = a.console
    void 0 !== c && void 0 !== c.log && c.log('[Bugly] ' + b)
  }

  function r (b) {
    var c, d
    if (i && (i -= 1, c = [b.name, b.message, b.stacktrace].join('|'), c !==
    f)) return f = c, d = {
      projectRoot: a.location.protocol + '//' + a.location.host,
      context: a.location.pathname,
      url: a.location.href,
      userAgent: navigator.userAgent,
      language: navigator.language || navigator.userLanguage,
      name: b.name,
      message: b.message,
      stacktrace: b.stacktrace,
      file: b.file,
      lineNumber: b.lineNumber,
      columnNumber: b.columnNumber,
    }, 0 === d.lineNumber && /Script error\.?/.test(d.message) ? q(
      'Ignoring cross-domain script error.') : ('undefined' !=
    typeof BuglySdk && null != BuglySdk
      ? BuglySdk.reportJSException(d)
      : 'undefined' != typeof exceptionUploader && null != exceptionUploader &&
      exceptionUploader.reportJSException(JSON.stringify(d)), void 0)
  }

  function s () {
    var a, b, f, g, h, c = 10, d = '[anonymous]'
    try {throw new Error('')} catch (e) {a = '<generated>\n', b = t(e)}
    if (!b) {
      a = '<generated-ie>\n', f = []
      try {
        for (g = arguments.callee.caller.caller; g && f.length < c;) h = n.test(
          g.toString()) ? RegExp.$1 || d : d, f.push(h), g = g.caller
      } catch (i) {q(i)}
      b = f.join('\n')
    }
    return b
  }

  function t (a) {return a.stacktrace || a.stack || a.backtrace}

  function u () {h += 1, a.setTimeout(function () {h -= 1})}

  function w (b, c, d) {
    var e = b[c], f = d(e)
    b[c] = f, 'undefined' != typeof Bugly_TESTING && a.undo &&
    a.undo.push(function () {b[c] = e})
  }

  var d, e, f, c, g, h, i, k, n, o, x
  if (void 0 === a.Bugly) {
    if (c = {}, g = !0, h = 0, i = 10, c.noConflict = function () {return a.Bugly = b, c}, c.refresh = function () {i = 10}, c.reportException = function (
      a, b) {
      a && r({
        name: b || a.name,
        message: a.message || a.description,
        stacktrace: t(a) || s(),
        file: a.fileName || a.sourceURL,
        lineNumber: a.lineNumber || a.line,
        columnNumber: a.columnNumber ? a.columnNumber + 1 : void 0,
      })
    }, k = 'complete' !== document.readyState, document.addEventListener
      ? (document.addEventListener('DOMContentLoaded', l,
        !0), a.addEventListener('load', l, !0))
      : a.attachEvent('onload',
        l), n = /function\s*([\w\-$]+)?\s*\(/i, o = document.getElementsByTagName(
      'script'), o[o.length - 1], a.atob) {
      if (a.ErrorEvent) try {
        0 === new a.ErrorEvent('test').colno && (g = !1)
      } catch (v) {}
    } else g = !1
    w(a, 'onerror', function (b) {
      return 'undefined' != typeof Bugly_TESTING &&
      (c._onerror = b), function (d, f, g, i, j) {
        !i && a.event && (i = a.event.errorCharacter), e = null, h || r({
          name: j && j.name,
          message: d,
          file: f,
          lineNumber: g,
          columnNumber: i,
          stacktrace: j && t(j) || s(),
        }), 'undefined' != typeof Bugly_TESTING && (b = c._onerror), b &&
        b(d, f, g, i, j)
      }
    }), x = function (a) {
      return function (b, c) {
        if ('function' == typeof b) {
          b = j(b)
          var d = Array.prototype.slice.call(arguments, 2)
          return a(function () {b.apply(this, d)}, c)
        }
        return a(b, c)
      }
    }, w(a, 'setTimeout', x), w(a, 'setInterval', x), a.requestAnimationFrame &&
    w(a, 'requestAnimationFrame',
      function (a) {return function (b) {return a(j(b))}}), a.setImmediate &&
    w(a, 'setImmediate', function (a) {
      return function () {
        var c = Array.prototype.slice.call(arguments)
        return c[0] = j(c[0]), a.apply(this, c)
      }
    }), 'EventTarget Window Node ApplicationCache AudioTrackList ChannelMergerNode CryptoOperation EventSource FileReader HTMLUnknownElement IDBDatabase IDBRequest IDBTransaction KeyOperation MediaController MessagePort ModalWindow Notification SVGElementInstance Screen TextTrack TextTrackCue TextTrackList WebSocket WebSocketWorker Worker XMLHttpRequest XMLHttpRequestEventTarget XMLHttpRequestUpload'.replace(
      /\w+/g, function (b) {
        var c = a[b] && a[b].prototype
        c && c.hasOwnProperty && c.hasOwnProperty('addEventListener') &&
        (w(c, 'addEventListener', function (a) {
          return function (b, c, d, e) {
            try {
              c && c.handleEvent &&
              (c.handleEvent = j(c.handleEvent, { eventHandler: !0 }))
            } catch (f) {q(f)}
            return a.call(this, b, j(c, { eventHandler: !0 }), d, e)
          }
        }), w(c, 'removeEventListener', function (a) {
          return function (b, c, d, e) {
            return a.call(this, b, c, d, e), a.call(this, b, j(c), d, e)
          }
        }))
      }), a.Bugly = c, 'function' == typeof define && define.amd ? define([],
      function () {return c}) : 'object' == typeof module && 'object' ==
      typeof module.exports && (module.exports = c)
  }
}(window, window.Bugly)
