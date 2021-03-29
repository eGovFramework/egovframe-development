"use strict";

var _postcss = require("postcss");

var _postcss2 = _interopRequireDefault(_postcss);

var _cssSelectorTokenizer = require("css-selector-tokenizer");

var _cssSelectorTokenizer2 = _interopRequireDefault(_cssSelectorTokenizer);

var _icssUtils = require("icss-utils");

var _genericNames = require("generic-names");

var _genericNames2 = _interopRequireDefault(_genericNames);

var _fromPairs = require("lodash/fromPairs");

var _fromPairs2 = _interopRequireDefault(_fromPairs);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _toConsumableArray(arr) { if (Array.isArray(arr)) { for (var i = 0, arr2 = Array(arr.length); i < arr.length; i++) { arr2[i] = arr[i]; } return arr2; } else { return Array.from(arr); } } /* eslint-env node */


var plugin = "postcss-icss-selectors";

var trimNodes = function trimNodes(nodes) {
  var firstIndex = nodes.findIndex(function (node) {
    return node.type !== "spacing";
  });
  var lastIndex = nodes.slice().reverse().findIndex(function (node) {
    return node.type !== "spacing";
  });
  return nodes.slice(firstIndex, nodes.length - lastIndex);
};

var isSpacing = function isSpacing(node) {
  return node.type === "spacing" || node.type === "operator";
};

var isModifier = function isModifier(node) {
  return node.type === "pseudo-class" && (node.name === "local" || node.name === "global");
};

function localizeNode(node, _ref) {
  var mode = _ref.mode,
      inside = _ref.inside,
      getAlias = _ref.getAlias;

  var newNodes = node.nodes.reduce(function (acc, n, index, nodes) {
    switch (n.type) {
      case "spacing":
        if (isModifier(nodes[index + 1])) {
          return [].concat(_toConsumableArray(acc), [Object.assign({}, n, { value: "" })]);
        }
        return [].concat(_toConsumableArray(acc), [n]);

      case "operator":
        if (isModifier(nodes[index + 1])) {
          return [].concat(_toConsumableArray(acc), [Object.assign({}, n, { after: "" })]);
        }
        return [].concat(_toConsumableArray(acc), [n]);

      case "pseudo-class":
        if (isModifier(n)) {
          if (inside) {
            throw Error(`A :${n.name} is not allowed inside of a :${inside}(...)`);
          }
          if (index !== 0 && !isSpacing(nodes[index - 1])) {
            throw Error(`Missing whitespace before :${n.name}`);
          }
          if (index !== nodes.length - 1 && !isSpacing(nodes[index + 1])) {
            throw Error(`Missing whitespace after :${n.name}`);
          }
          // set mode
          mode = n.name;
          return acc;
        }
        return [].concat(_toConsumableArray(acc), [n]);

      case "nested-pseudo-class":
        if (n.name === "local" || n.name === "global") {
          if (inside) {
            throw Error(`A :${n.name}(...) is not allowed inside of a :${inside}(...)`);
          }
          return [].concat(_toConsumableArray(acc), _toConsumableArray(localizeNode(n.nodes[0], {
            mode: n.name,
            inside: n.name,
            getAlias
          }).nodes));
        } else {
          return [].concat(_toConsumableArray(acc), [Object.assign({}, n, {
            nodes: localizeNode(n.nodes[0], { mode, inside, getAlias }).nodes
          })]);
        }

      case "id":
      case "class":
        if (mode === "local") {
          return [].concat(_toConsumableArray(acc), [Object.assign({}, n, { name: getAlias(n.name) })]);
        }
        return [].concat(_toConsumableArray(acc), [n]);

      default:
        return [].concat(_toConsumableArray(acc), [n]);
    }
  }, []);

  return Object.assign({}, node, { nodes: trimNodes(newNodes) });
}

var localizeSelectors = function localizeSelectors(selectors, mode, getAlias) {
  var node = _cssSelectorTokenizer2.default.parse(selectors);
  return _cssSelectorTokenizer2.default.stringify(Object.assign({}, node, {
    nodes: node.nodes.map(function (n) {
      return localizeNode(n, { mode, getAlias });
    })
  }));
};

var walkRules = function walkRules(css, callback) {
  css.walkRules(function (rule) {
    if (rule.parent.type !== "atrule" || !/keyframes$/.test(rule.parent.name)) {
      callback(rule);
    }
  });
};

var getMessages = function getMessages(aliases) {
  return Object.keys(aliases).map(function (name) {
    return {
      plugin,
      type: "icss-scoped",
      name,
      value: aliases[name]
    };
  });
};

var getValue = function getValue(messages, name) {
  return messages.find(function (msg) {
    return msg.type === "icss-value" && msg.value === name;
  });
};

var isRedeclared = function isRedeclared(messages, name) {
  return messages.find(function (msg) {
    return msg.type === "icss-scoped" && msg.name === name;
  });
};

var flatten = function flatten(array) {
  return array.reduce(function (acc, item) {
    return [].concat(_toConsumableArray(acc), _toConsumableArray(item));
  }, []);
};

var getComposed = function getComposed(name, messages, root) {
  return [name].concat(_toConsumableArray(flatten(messages.filter(function (msg) {
    return msg.name === name && msg.value !== root;
  }).map(function (msg) {
    return getComposed(msg.value, messages, root);
  }))));
};

var composeAliases = function composeAliases(aliases, messages) {
  return Object.keys(aliases).reduce(function (acc, name) {
    return Object.assign({}, acc, {
      [name]: getComposed(name, messages, name).map(function (value) {
        return aliases[value] || value;
      }).join(" ")
    });
  }, {});
};

var mapMessages = function mapMessages(messages, type) {
  return (0, _fromPairs2.default)(messages.filter(function (msg) {
    return msg.type === type;
  }).map(function (msg) {
    return [msg.name, msg.value];
  }));
};

var composeExports = function composeExports(messages) {
  var composed = messages.filter(function (msg) {
    return msg.type === "icss-composed";
  });
  var values = mapMessages(messages, "icss-value");
  var scoped = mapMessages(messages, "icss-scoped");
  var aliases = Object.assign({}, scoped, values);
  return composeAliases(aliases, composed);
};

module.exports = _postcss2.default.plugin(plugin, function () {
  var options = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  return function (css, result) {
    var _result$messages;

    var _extractICSS = (0, _icssUtils.extractICSS)(css),
        icssImports = _extractICSS.icssImports,
        icssExports = _extractICSS.icssExports;

    var generateScopedName = options.generateScopedName || (0, _genericNames2.default)("[name]__[local]---[hash:base64:5]");
    var input = css && css.source && css.source.input || {};
    var aliases = {};
    walkRules(css, function (rule) {
      var getAlias = function getAlias(name) {
        if (aliases[name]) {
          return aliases[name];
        }
        // icss-value contract
        var valueMsg = getValue(result.messages, name);
        if (valueMsg) {
          aliases[valueMsg.name] = name;
          return name;
        }
        var alias = generateScopedName(name, input.from, input.css);
        aliases[name] = alias;
        // icss-scoped contract
        if (isRedeclared(result.messages, name)) {
          result.warn(`'${name}' already declared`, { node: rule });
        }
        return alias;
      };
      try {
        rule.selector = localizeSelectors(rule.selector, options.mode === "global" ? "global" : "local", getAlias);
      } catch (e) {
        throw rule.error(e.message);
      }
    });
    (_result$messages = result.messages).push.apply(_result$messages, _toConsumableArray(getMessages(aliases)));
    // contracts
    var composedExports = composeExports(result.messages);
    var exports = Object.assign({}, icssExports, composedExports);
    css.prepend((0, _icssUtils.createICSSRules)(icssImports, exports));
  };
});