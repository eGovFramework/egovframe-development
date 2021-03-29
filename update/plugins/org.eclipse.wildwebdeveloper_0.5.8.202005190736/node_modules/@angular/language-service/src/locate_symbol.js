/**
 * @license
 * Copyright Google Inc. All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
(function (factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        var v = factory(require, exports);
        if (v !== undefined) module.exports = v;
    }
    else if (typeof define === "function" && define.amd) {
        define("@angular/language-service/src/locate_symbol", ["require", "exports", "tslib", "@angular/compiler", "@angular/language-service/src/expression_diagnostics", "@angular/language-service/src/expressions", "@angular/language-service/src/types", "@angular/language-service/src/utils"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var tslib_1 = require("tslib");
    var compiler_1 = require("@angular/compiler");
    var expression_diagnostics_1 = require("@angular/language-service/src/expression_diagnostics");
    var expressions_1 = require("@angular/language-service/src/expressions");
    var types_1 = require("@angular/language-service/src/types");
    var utils_1 = require("@angular/language-service/src/utils");
    /**
     * Traverse the template AST and locate the Symbol at the specified `position`.
     * @param info Ast and Template Source
     * @param position location to look for
     */
    function locateSymbol(info, position) {
        var templatePosition = position - info.template.span.start;
        var path = utils_1.findTemplateAstAt(info.templateAst, templatePosition);
        var compileTypeSummary = undefined;
        if (path.tail) {
            var symbol_1 = undefined;
            var span_1 = undefined;
            var attributeValueSymbol_1 = function (ast, inEvent) {
                if (inEvent === void 0) { inEvent = false; }
                var attribute = findAttribute(info, position);
                if (attribute) {
                    if (utils_1.inSpan(templatePosition, utils_1.spanOf(attribute.valueSpan))) {
                        var dinfo = utils_1.diagnosticInfoFromTemplateInfo(info);
                        var scope = expression_diagnostics_1.getExpressionScope(dinfo, path, inEvent);
                        if (attribute.valueSpan) {
                            var result = expressions_1.getExpressionSymbol(scope, ast, templatePosition, info.template.query);
                            if (result) {
                                symbol_1 = result.symbol;
                                var expressionOffset = attribute.valueSpan.start.offset;
                                span_1 = utils_1.offsetSpan(result.span, expressionOffset);
                            }
                        }
                        return true;
                    }
                }
                return false;
            };
            path.tail.visit({
                visitNgContent: function (ast) { },
                visitEmbeddedTemplate: function (ast) { },
                visitElement: function (ast) {
                    var component = ast.directives.find(function (d) { return d.directive.isComponent; });
                    if (component) {
                        compileTypeSummary = component.directive;
                        symbol_1 = info.template.query.getTypeSymbol(compileTypeSummary.type.reference);
                        symbol_1 = symbol_1 && new OverrideKindSymbol(symbol_1, types_1.DirectiveKind.COMPONENT);
                        span_1 = utils_1.spanOf(ast);
                    }
                    else {
                        // Find a directive that matches the element name
                        var directive = ast.directives.find(function (d) { return d.directive.selector != null && d.directive.selector.indexOf(ast.name) >= 0; });
                        if (directive) {
                            compileTypeSummary = directive.directive;
                            symbol_1 = info.template.query.getTypeSymbol(compileTypeSummary.type.reference);
                            symbol_1 = symbol_1 && new OverrideKindSymbol(symbol_1, types_1.DirectiveKind.DIRECTIVE);
                            span_1 = utils_1.spanOf(ast);
                        }
                    }
                },
                visitReference: function (ast) {
                    symbol_1 = ast.value && info.template.query.getTypeSymbol(compiler_1.tokenReference(ast.value));
                    span_1 = utils_1.spanOf(ast);
                },
                visitVariable: function (ast) { },
                visitEvent: function (ast) {
                    if (!attributeValueSymbol_1(ast.handler, /* inEvent */ true)) {
                        symbol_1 = findOutputBinding(info, path, ast);
                        symbol_1 = symbol_1 && new OverrideKindSymbol(symbol_1, types_1.DirectiveKind.EVENT);
                        span_1 = utils_1.spanOf(ast);
                    }
                },
                visitElementProperty: function (ast) { attributeValueSymbol_1(ast.value); },
                visitAttr: function (ast) {
                    var e_1, _a;
                    var element = path.head;
                    if (!element || !(element instanceof compiler_1.ElementAst))
                        return;
                    // Create a mapping of all directives applied to the element from their selectors.
                    var matcher = new compiler_1.SelectorMatcher();
                    try {
                        for (var _b = tslib_1.__values(element.directives), _c = _b.next(); !_c.done; _c = _b.next()) {
                            var dir = _c.value;
                            if (!dir.directive.selector)
                                continue;
                            matcher.addSelectables(compiler_1.CssSelector.parse(dir.directive.selector), dir);
                        }
                    }
                    catch (e_1_1) { e_1 = { error: e_1_1 }; }
                    finally {
                        try {
                            if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
                        }
                        finally { if (e_1) throw e_1.error; }
                    }
                    // See if this attribute matches the selector of any directive on the element.
                    // TODO(ayazhafiz): Consider caching selector matches (at the expense of potentially
                    // very high memory usage).
                    var attributeSelector = "[" + ast.name + "=" + ast.value + "]";
                    var parsedAttribute = compiler_1.CssSelector.parse(attributeSelector);
                    if (!parsedAttribute.length)
                        return;
                    matcher.match(parsedAttribute[0], function (_, directive) {
                        symbol_1 = info.template.query.getTypeSymbol(directive.directive.type.reference);
                        symbol_1 = symbol_1 && new OverrideKindSymbol(symbol_1, types_1.DirectiveKind.DIRECTIVE);
                        span_1 = utils_1.spanOf(ast);
                    });
                },
                visitBoundText: function (ast) {
                    var expressionPosition = templatePosition - ast.sourceSpan.start.offset;
                    if (utils_1.inSpan(expressionPosition, ast.value.span)) {
                        var dinfo = utils_1.diagnosticInfoFromTemplateInfo(info);
                        var scope = expression_diagnostics_1.getExpressionScope(dinfo, path, /* includeEvent */ false);
                        var result = expressions_1.getExpressionSymbol(scope, ast.value, templatePosition, info.template.query);
                        if (result) {
                            symbol_1 = result.symbol;
                            span_1 = utils_1.offsetSpan(result.span, ast.sourceSpan.start.offset);
                        }
                    }
                },
                visitText: function (ast) { },
                visitDirective: function (ast) {
                    compileTypeSummary = ast.directive;
                    symbol_1 = info.template.query.getTypeSymbol(compileTypeSummary.type.reference);
                    span_1 = utils_1.spanOf(ast);
                },
                visitDirectiveProperty: function (ast) {
                    if (!attributeValueSymbol_1(ast.value)) {
                        symbol_1 = findInputBinding(info, path, ast);
                        span_1 = utils_1.spanOf(ast);
                    }
                }
            }, null);
            if (symbol_1 && span_1) {
                return { symbol: symbol_1, span: utils_1.offsetSpan(span_1, info.template.span.start), compileTypeSummary: compileTypeSummary };
            }
        }
    }
    exports.locateSymbol = locateSymbol;
    function findAttribute(info, position) {
        var templatePosition = position - info.template.span.start;
        var path = compiler_1.findNode(info.htmlAst, templatePosition);
        return path.first(compiler_1.Attribute);
    }
    function findInputBinding(info, path, binding) {
        var e_2, _a;
        var element = path.first(compiler_1.ElementAst);
        if (element) {
            try {
                for (var _b = tslib_1.__values(element.directives), _c = _b.next(); !_c.done; _c = _b.next()) {
                    var directive = _c.value;
                    var invertedInput = invertMap(directive.directive.inputs);
                    var fieldName = invertedInput[binding.templateName];
                    if (fieldName) {
                        var classSymbol = info.template.query.getTypeSymbol(directive.directive.type.reference);
                        if (classSymbol) {
                            return classSymbol.members().get(fieldName);
                        }
                    }
                }
            }
            catch (e_2_1) { e_2 = { error: e_2_1 }; }
            finally {
                try {
                    if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
                }
                finally { if (e_2) throw e_2.error; }
            }
        }
    }
    function findOutputBinding(info, path, binding) {
        var e_3, _a;
        var element = path.first(compiler_1.ElementAst);
        if (element) {
            try {
                for (var _b = tslib_1.__values(element.directives), _c = _b.next(); !_c.done; _c = _b.next()) {
                    var directive = _c.value;
                    var invertedOutputs = invertMap(directive.directive.outputs);
                    var fieldName = invertedOutputs[binding.name];
                    if (fieldName) {
                        var classSymbol = info.template.query.getTypeSymbol(directive.directive.type.reference);
                        if (classSymbol) {
                            return classSymbol.members().get(fieldName);
                        }
                    }
                }
            }
            catch (e_3_1) { e_3 = { error: e_3_1 }; }
            finally {
                try {
                    if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
                }
                finally { if (e_3) throw e_3.error; }
            }
        }
    }
    function invertMap(obj) {
        var e_4, _a;
        var result = {};
        try {
            for (var _b = tslib_1.__values(Object.keys(obj)), _c = _b.next(); !_c.done; _c = _b.next()) {
                var name_1 = _c.value;
                var v = obj[name_1];
                result[v] = name_1;
            }
        }
        catch (e_4_1) { e_4 = { error: e_4_1 }; }
        finally {
            try {
                if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
            }
            finally { if (e_4) throw e_4.error; }
        }
        return result;
    }
    /**
     * Wrap a symbol and change its kind to component.
     */
    var OverrideKindSymbol = /** @class */ (function () {
        function OverrideKindSymbol(sym, kindOverride) {
            this.sym = sym;
            this.kind = kindOverride;
        }
        Object.defineProperty(OverrideKindSymbol.prototype, "name", {
            get: function () { return this.sym.name; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverrideKindSymbol.prototype, "language", {
            get: function () { return this.sym.language; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverrideKindSymbol.prototype, "type", {
            get: function () { return this.sym.type; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverrideKindSymbol.prototype, "container", {
            get: function () { return this.sym.container; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverrideKindSymbol.prototype, "public", {
            get: function () { return this.sym.public; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverrideKindSymbol.prototype, "callable", {
            get: function () { return this.sym.callable; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverrideKindSymbol.prototype, "nullable", {
            get: function () { return this.sym.nullable; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(OverrideKindSymbol.prototype, "definition", {
            get: function () { return this.sym.definition; },
            enumerable: true,
            configurable: true
        });
        OverrideKindSymbol.prototype.members = function () { return this.sym.members(); };
        OverrideKindSymbol.prototype.signatures = function () { return this.sym.signatures(); };
        OverrideKindSymbol.prototype.selectSignature = function (types) { return this.sym.selectSignature(types); };
        OverrideKindSymbol.prototype.indexed = function (argument) { return this.sym.indexed(argument); };
        return OverrideKindSymbol;
    }());
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoibG9jYXRlX3N5bWJvbC5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uL3BhY2thZ2VzL2xhbmd1YWdlLXNlcnZpY2Uvc3JjL2xvY2F0ZV9zeW1ib2wudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7Ozs7OztHQU1HOzs7Ozs7Ozs7Ozs7O0lBRUgsOENBQWtOO0lBR2xOLCtGQUE0RDtJQUM1RCx5RUFBa0Q7SUFDbEQsNkRBQWdFO0lBQ2hFLDZEQUFzRztJQVF0Rzs7OztPQUlHO0lBQ0gsU0FBZ0IsWUFBWSxDQUFDLElBQWUsRUFBRSxRQUFnQjtRQUM1RCxJQUFNLGdCQUFnQixHQUFHLFFBQVEsR0FBRyxJQUFJLENBQUMsUUFBUSxDQUFDLElBQUksQ0FBQyxLQUFLLENBQUM7UUFDN0QsSUFBTSxJQUFJLEdBQUcseUJBQWlCLENBQUMsSUFBSSxDQUFDLFdBQVcsRUFBRSxnQkFBZ0IsQ0FBQyxDQUFDO1FBQ25FLElBQUksa0JBQWtCLEdBQWlDLFNBQVMsQ0FBQztRQUNqRSxJQUFJLElBQUksQ0FBQyxJQUFJLEVBQUU7WUFDYixJQUFJLFFBQU0sR0FBcUIsU0FBUyxDQUFDO1lBQ3pDLElBQUksTUFBSSxHQUFtQixTQUFTLENBQUM7WUFDckMsSUFBTSxzQkFBb0IsR0FBRyxVQUFDLEdBQVEsRUFBRSxPQUF3QjtnQkFBeEIsd0JBQUEsRUFBQSxlQUF3QjtnQkFDOUQsSUFBTSxTQUFTLEdBQUcsYUFBYSxDQUFDLElBQUksRUFBRSxRQUFRLENBQUMsQ0FBQztnQkFDaEQsSUFBSSxTQUFTLEVBQUU7b0JBQ2IsSUFBSSxjQUFNLENBQUMsZ0JBQWdCLEVBQUUsY0FBTSxDQUFDLFNBQVMsQ0FBQyxTQUFTLENBQUMsQ0FBQyxFQUFFO3dCQUN6RCxJQUFNLEtBQUssR0FBRyxzQ0FBOEIsQ0FBQyxJQUFJLENBQUMsQ0FBQzt3QkFDbkQsSUFBTSxLQUFLLEdBQUcsMkNBQWtCLENBQUMsS0FBSyxFQUFFLElBQUksRUFBRSxPQUFPLENB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K0IsT0FBTyxJQUFJLENBQUMsR0FBRyxDQUFDLElBQUksQ0FBQyxDQUFDLENBQUM7OztXQUFBO1FBRXRELHNCQUFJLHlDQUFTO2lCQUFiLGNBQW9DLE9BQU8sSUFBSSxDQUFDLEdBQUcsQ0FBQyxTQUFTLENBQUMsQ0FBQyxDQUFDOzs7V0FBQTtRQUVoRSxzQkFBSSxzQ0FBTTtpQkFBVixjQUF3QixPQUFPLElBQUksQ0FBQyxHQUFHLENBQUMsTUFBTSxDQUFDLENBQUMsQ0FBQzs7O1dBQUE7UUFFakQsc0JBQUksd0NBQVE7aUJBQVosY0FBMEIsT0FBTyxJQUFJLENBQUMsR0FBRyxDQUFDLFFBQVEsQ0FBQyxDQUFDLENBQUM7OztXQUFBO1FBRXJELHNCQUFJLHdDQUFRO2lCQUFaLGNBQTBCLE9BQU8sSUFBSSxDQUFDLEdBQUcsQ0FBQyxRQUFRLENBQUMsQ0FBQyxDQUFDOzs7V0FBQTtRQUVyRCxzQkFBSSwwQ0FBVTtpQkFBZCxjQUErQixPQUFPLElBQUksQ0FBQyxHQUFHLENBQUMsVUFBVSxDQUFDLENBQUMsQ0FBQzs7O1dBQUE7UUFFNUQsb0NBQU8sR0FBUCxjQUFZLE9BQU8sSUFBSSxDQUFDLEdBQUcsQ0FBQyxPQUFPLEVBQUUsQ0FBQyxDQUFDLENBQUM7UUFFeEMsdUNBQVUsR0FBVixjQUFlLE9BQU8sSUFBSSxDQUFDLEdBQUcsQ0FBQyxVQUFVLEVBQUUsQ0FBQyxDQUFDLENBQUM7UUFFOUMsNENBQWUsR0FBZixVQUFnQixLQUFlLElBQUksT0FBTyxJQUFJLENBQUMsR0FBRyxDQUFDLGVBQWUsQ0FBQyxLQUFLLENBQUMsQ0FBQyxDQUFDLENBQUM7UUFFNUUsb0NBQU8sR0FBUCxVQUFRLFFBQWdCLElBQUksT0FBTyxJQUFJLENBQUMsR0FBRyxDQUFDLE9BQU8sQ0FBQyxRQUFRLENBQUMsQ0FBQyxDQUFDLENBQUM7UUFDbEUseUJBQUM7SUFBRCxDQUFDLEFBM0JELElBMkJDIiwic291cmNlc0NvbnRlbnQiOlsiLyoqXG4gKiBAbGljZW5zZVxuICogQ29weXJpZ2h0IEdvb2dsZSBJbmMuIEFsbCBSaWdodHMgUmVzZXJ2ZWQuXG4gKlxuICogVXNlIG9mIHRoaXMgc291cmNlIGNvZGUgaXMgZ292ZXJuZWQgYnkgYW4gTUlULXN0eWxlIGxpY2Vuc2UgdGhhdCBjYW4gYmVcbiAqIGZvdW5kIGluIHRoZSBMSUNFTlNFIGZpbGUgYXQgaHR0cHM6Ly9hbmd1bGFyLmlvL2xpY2Vuc2VcbiAqL1xuXG5pbXBvcnQge0FTVCwgQXR0cmlidXRlLCBCb3VuZERpcmVjdGl2ZVByb3BlcnR5QXN0LCBCb3VuZEV2ZW50QXN0LCBDb21waWxlVHlwZVN1bW1hcnksIENzc1NlbGVjdG9yLCBEaXJlY3RpdmVBc3QsIEVsZW1lbnRBc3QsIFNlbGVjdG9yTWF0Y2hlciwgVGVtcGxhdGVBc3RQYXRoLCBmaW5kTm9kZSwgdG9rZW5SZWZlcmVuY2V9IGZyb20gJ0Bhbmd1bGFyL2NvbXBpbGVyJztcblxuaW1wb3J0IHtBc3RSZXN1bHR9IGZyb20gJy4vY29tbW9uJztcbmltcG9ydCB7Z2V0RXhwcmVzc2lvblNjb3BlfSBmcm9tICcuL2V4cHJlc3Npb25fZGlhZ25vc3RpY3MnO1xuaW1wb3J0IHtnZXRFeHByZXNzaW9uU3ltYm9sfSBmcm9tICcuL2V4cHJlc3Npb25zJztcbmltcG9ydCB7RGVmaW5pdGlvbiwgRGlyZWN0aXZlS2luZCwgU3BhbiwgU3ltYm9sfSBmcm9tICcuL3R5cGVzJztcbmltcG9ydCB7ZGlhZ25vc3RpY0luZm9Gcm9tVGVtcGxhdGVJbmZvLCBmaW5kVGVtcGxhdGVBc3RBdCwgaW5TcGFuLCBvZmZzZXRTcGFuLCBzcGFuT2Z9IGZyb20gJy4vdXRpbHMnO1xuXG5leHBvcnQgaW50ZXJmYWNlIFN5bWJvbEluZm8ge1xuICBzeW1ib2w6IFN5bWJvbDtcbiAgc3BhbjogU3BhbjtcbiAgY29tcGlsZVR5cGVTdW1tYXJ5OiBDb21waWxlVHlwZVN1bW1hcnl8dW5kZWZpbmVkO1xufVxuXG4vKipcbiAqIFRyYXZlcnNlIHRoZSB0ZW1wbGF0ZSBBU1QgYW5kIGxvY2F0ZSB0aGUgU3ltYm9sIGF0IHRoZSBzcGVjaWZpZWQgYHBvc2l0aW9uYC5cbiAqIEBwYXJhbSBpbmZvIEFzdCBhbmQgVGVtcGxhdGUgU291cmNlXG4gKiBAcGFyYW0gcG9zaXRpb24gbG9jYXRpb24gdG8gbG9vayBmb3JcbiAqL1xuZXhwb3J0IGZ1bmN0aW9uIGxvY2F0ZVN5bWJvbChpbmZvOiBBc3RSZXN1bHQsIHBvc2l0aW9uOiBudW1iZXIpOiBTeW1ib2xJbmZvfHVuZGVmaW5lZCB7XG4gIGNvbnN0IHRlbXBsYXRlUG9zaXRpb24gPSBwb3NpdGlvbiAtIGluZm8udGVtcGxhdGUuc3Bhbi5zdGFydDtcbiAgY29uc3QgcGF0aCA9IGZpbmRUZW1wbGF0ZUFzdEF0KGluZm8udGVtcGxhdGVBc3QsIHRlbXBsYXRlUG9zaXRpb24pO1xuICBsZXQgY29tcGlsZVR5cGVTdW1tYXJ5OiBDb21waWxlVHlwZVN1bW1hcnl8dW5kZWZpbmVkID0gdW5kZWZpbmVkO1xuICBpZiAocGF0aC50YWlsKSB7XG4gICAgbGV0IHN5bWJvbDogU3ltYm9sfHVuZGVmaW5lZCA9IHVuZGVmaW5lZDtcbiAgICBsZXQgc3BhbjogU3Bhbnx1bmRlZmluZWQgPSB1bmRlZmluZWQ7XG4gICAgY29uc3QgYXR0cmlidXRlVmFsdWVTeW1ib2wgPSAoYXN0OiBBU1QsIGluRXZlbnQ6IGJvb2xlYW4gPSBmYWxzZSk6IGJvb2xlYW4gPT4ge1xuICAgICAgY29uc3QgYXR0cmlidXRlID0gZmluZEF0dHJpYnV0ZShpbmZvLCBwb3NpdGlvbik7XG4gICAgICBpZiAoYXR0cmlidXRlKSB7XG4gICAgICAgIGlmIChpblNwYW4odGVtcGxhdGVQb3NpdGlvbiwgc3Bhbk9mKGF0dHJpYnV0ZS52YWx1ZVNwYW4pKSkge1xuICAgICAgICAgIGNvbnN0IGRpbmZvID0gZGlhZ25vc3RpY0luZm9Gcm9tVGVtcGxhdGVJbmZvKGluZm8pO1xuICAgICAgICAgIGNvbnN0IHNjb3BlID0gZ2V0RXhwcmVzc2lvblNjb3BlKGRpbmZvLCBwYXRoLCBpbkV2ZW50KTtcbiAgICAgICAgICBpZiAoYXR0cmlidXRlLnZhbHVlU3Bhbikge1xuICAgICAgICAgICAgY29uc3QgcmVzdWx0ID0gZ2V0RXhwcmVzc2lvblN5bWJvbChzY29wZSwgYXN0LCB0ZW1wbGF0ZVBvc2l0aW9uLCBpbmZvLnRlbXBsYXRlLnF1ZXJ5KTtcbiAgICAgICAgICAgIGlmIChyZXN1bHQpIHtcbiAgICAgICAgICAgICAgc3ltYm9sID0gcmVzdWx0LnN5bWJvbDtcbiAgICAgICAgICAgICAgY29uc3QgZXhwcmVzc2lvbk9mZnNldCA9IGF0dHJpYnV0ZS52YWx1ZVNwYW4uc3RhcnQub2Zmc2V0O1xuICAgICAgICAgICAgICBzcGFuID0gb2Zmc2V0U3BhbihyZXN1bHQuc3BhbiwgZXhwcmVzc2lvbk9mZnNldCk7XG4gICAgICAgICAgICB9XG4gICAgICAgICAgfVxuICAgICAgICAgIHJldHVybiB0cnVlO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgICByZXR1cm4gZmFsc2U7XG4gICAgfTtcbiAgICBwYXRoLnRhaWwudmlzaXQoXG4gICAgICAgIHtcbiAgICAgICAgICB2aXNpdE5nQ29udGVudChhc3QpIHt9LFxuICAgICAgICAgIHZpc2l0RW1iZWRkZWRUZW1wbGF0ZShhc3QpIHt9LFxuICAgICAgICAgIHZpc2l0RWxlbWVudChhc3QpIHtcbiAgICAgICAgICAgIGNvbnN0IGNvbXBvbmVudCA9IGFzdC5kaXJlY3RpdmVzLmZpbmQoZCA9PiBkLmRpcmVjdGl2ZS5pc0NvbXBvbmVudCk7XG4gICAgICAgICAgICBpZiAoY29tcG9uZW50KSB7XG4gICAgICAgICAgICAgIGNvbXBpbGVUeXBlU3VtbWFyeSA9IGNvbXBvbmVudC5kaXJlY3RpdmU7XG4gICAgICAgICAgICAgIHN5bWJvbCA9IGluZm8udGVtcGxhdGUucXVlcnkuZ2V0VHlwZVN5bWJvbChjb21waWxlVHlwZVN1bW1hcnkudHlwZS5yZWZlcmVuY2UpO1xuICAgICAgICAgICAgICBzeW1ib2wgPSBzeW1ib2wgJiYgbmV3IE92ZXJyaWRlS2luZFN5bWJvbChzeW1ib2wsIERpcmVjdGl2ZUtpbmQuQ09NUE9ORU5UKTtcbiAgICAgICAgICAgICAgc3BhbiA9IHNwYW5PZihhc3QpO1xuICAgICAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgICAgLy8gRmluZCBhIGRpcmVjdGl2ZSB0aGF0IG1hdGNoZXMgdGhlIGVsZW1lbnQgbmFtZVxuICAgICAgICAgICAgICBjb25zdCBkaXJlY3RpdmUgPSBhc3QuZGlyZWN0aXZlcy5maW5kKFxuICAgICAgICAgICAgICAgICAgZCA9PiBkLmRpcmVjdGl2ZS5zZWxlY3RvciAhPSBudWxsICYmIGQuZGlyZWN0aXZlLnNlbGVjdG9yLmluZGV4T2YoYXN0Lm5hbWUpID49IDApO1xuICAgICAgICAgICAgICBpZiAoZGlyZWN0aXZlKSB7XG4gICAgICAgICAgICAgICAgY29tcGlsZVR5cGVTdW1tYXJ5ID0gZGlyZWN0aXZlLmRpcmVjdGl2ZTtcbiAgICAgICAgICAgICAgICBzeW1ib2wgPSBpbmZvLnRlbXBsYXRlLnF1ZXJ5LmdldFR5cGVTeW1ib2woY29tcGlsZVR5cGVTdW1tYXJ5LnR5cGUucmVmZXJlbmNlKTtcbiAgICAgICAgICAgICAgICBzeW1ib2wgPSBzeW1ib2wgJiYgbmV3IE92ZXJyaWRlS2luZFN5bWJvbChzeW1ib2wsIERpcmVjdGl2ZUtpbmQuRElSRUNUSVZFKTtcbiAgICAgICAgICAgICAgICBzcGFuID0gc3Bhbk9mKGFzdCk7XG4gICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIH1cbiAgICAgICAgICB9LFxuICAgICAgICAgIHZpc2l0UmVmZXJlbmNlKGFzdCkge1xuICAgICAgICAgICAgc3ltYm9sID0gYXN0LnZhbHVlICYmIGluZm8udGVtcGxhdGUucXVlcnkuZ2V0VHlwZVN5bWJvbCh0b2tlblJlZmVyZW5jZShhc3QudmFsdWUpKTtcbiAgICAgICAgICAgIHNwYW4gPSBzcGFuT2YoYXN0KTtcbiAgICAgICAgICB9LFxuICAgICAgICAgIHZpc2l0VmFyaWFibGUoYXN0KSB7fSxcbiAgICAgICAgICB2aXNpdEV2ZW50KGFzdCkge1xuICAgICAgICAgICAgaWYgKCFhdHRyaWJ1dGVWYWx1ZVN5bWJvbChhc3QuaGFuZGxlciwgLyogaW5FdmVudCAqLyB0cnVlKSkge1xuICAgICAgICAgICAgICBzeW1ib2wgPSBmaW5kT3V0cHV0QmluZGluZyhpbmZvLCBwYXRoLCBhc3QpO1xuICAgICAgICAgICAgICBzeW1ib2wgPSBzeW1ib2wgJiYgbmV3IE92ZXJyaWRlS2luZFN5bWJvbChzeW1ib2wsIERpcmVjdGl2ZUtpbmQuRVZFTlQpO1xuICAgICAgICAgICAgICBzcGFuID0gc3Bhbk9mKGFzdCk7XG4gICAgICAgICAgICB9XG4gICAgICAgICAgfSxcbiAgICAgICAgICB2aXNpdEVsZW1lbnRQcm9wZXJ0eShhc3QpIHsgYXR0cmlidXRlVmFsdWVTeW1ib2woYXN0LnZhbHVlKTsgfSxcbiAgICAgICAgICB2aXNpdEF0dHIoYXN0KSB7XG4gICAgICAgICAgICBjb25zdCBlbGVtZW50ID0gcGF0aC5oZWFkO1xuICAgICAgICAgICAgaWYgKCFlbGVtZW50IHx8ICEoZWxlbWVudCBpbnN0YW5jZW9mIEVsZW1lbnRBc3QpKSByZXR1cm47XG4gICAgICAgICAgICAvLyBDcmVhdGUgYSBtYXBwaW5nIG9mIGFsbCBkaXJlY3RpdmVzIGFwcGxpZWQgdG8gdGhlIGVsZW1lbnQgZnJvbSB0aGVpciBzZWxlY3RvcnMuXG4gICAgICAgICAgICBjb25zdCBtYXRjaGVyID0gbmV3IFNlbGVjdG9yTWF0Y2hlcjxEaXJlY3RpdmVBc3Q+KCk7XG4gICAgICAgICAgICBmb3IgKGNvbnN0IGRpciBvZiBlbGVtZW50LmRpcmVjdGl2ZXMpIHtcbiAgICAgICAgICAgICAgaWYgKCFkaXIuZGlyZWN0aXZlLnNlbGVjdG9yKSBjb250aW51ZTtcbiAgICAgICAgICAgICAgbWF0Y2hlci5hZGRTZWxlY3RhYmxlcyhDc3NTZWxlY3Rvci5wYXJzZShkaXIuZGlyZWN0aXZlLnNlbGVjdG9yKSwgZGlyKTtcbiAgICAgICAgICAgIH1cblxuICAgICAgICAgICAgLy8gU2VlIGlmIHRoaXMgYXR0cmlidXRlIG1hdGNoZXMgdGhlIHNlbGVjdG9yIG9mIGFueSBkaXJlY3RpdmUgb24gdGhlIGVsZW1lbnQuXG4gICAgICAgICAgICAvLyBUT0RPKGF5YXpoYWZpeik6IENvbnNpZGVyIGNhY2hpbmcgc2VsZWN0b3IgbWF0Y2hlcyAoYXQgdGhlIGV4cGVuc2Ugb2YgcG90ZW50aWFsbHlcbiAgICAgICAgICAgIC8vIHZlcnkgaGlnaCBtZW1vcnkgdXNhZ2UpLlxuICAgICAgICAgICAgY29uc3QgYXR0cmlidXRlU2VsZWN0b3IgPSBgWyR7YXN0Lm5hbWV9PSR7YXN0LnZhbHVlfV1gO1xuICAgICAgICAgICAgY29uc3QgcGFyc2VkQXR0cmlidXRlID0gQ3NzU2VsZWN0b3IucGFyc2UoYXR0cmlidXRlU2VsZWN0b3IpO1xuICAgICAgICAgICAgaWYgKCFwYXJzZWRBdHRyaWJ1dGUubGVuZ3RoKSByZXR1cm47XG4gICAgICAgICAgICBtYXRjaGVyLm1hdGNoKHBhcnNlZEF0dHJpYnV0ZVswXSwgKF8sIGRpcmVjdGl2ZSkgPT4ge1xuICAgICAgICAgICAgICBzeW1ib2wgPSBpbmZvLnRlbXBsYXRlLnF1ZXJ5LmdldFR5cGVTeW1ib2woZGlyZWN0aXZlLmRpcmVjdGl2ZS50eXBlLnJlZmVyZW5jZSk7XG4gICAgICAgICAgICAgIHN5bWJvbCA9IHN5bWJvbCAmJiBuZXcgT3ZlcnJpZGVLaW5kU3ltYm9sKHN5bWJvbCwgRGlyZWN0aXZlS2luZC5ESVJFQ1RJVkUpO1xuICAgICAgICAgICAgICBzcGFuID0gc3Bhbk9mKGFzdCk7XG4gICAgICAgICAgICB9KTtcbiAgICAgICAgICB9LFxuICAgICAgICAgIHZpc2l0Qm91bmRUZXh0KGFzdCkge1xuICAgICAgICAgICAgY29uc3QgZXhwcmVzc2lvblBvc2l0aW9uID0gdGVtcGxhdGVQb3NpdGlvbiAtIGFzdC5zb3VyY2VTcGFuLnN0YXJ0Lm9mZnNldDtcbiAgICAgICAgICAgIGlmIChpblNwYW4oZXhwcmVzc2lvblBvc2l0aW9uLCBhc3QudmFsdWUuc3BhbikpIHtcbiAgICAgICAgICAgICAgY29uc3QgZGluZm8gPSBkaWFnbm9zdGljSW5mb0Zyb21UZW1wbGF0ZUluZm8oaW5mbyk7XG4gICAgICAgICAgICAgIGNvbnN0IHNjb3BlID0gZ2V0RXhwcmVzc2lvblNjb3BlKGRpbmZvLCBwYXRoLCAvKiBpbmNsdWRlRXZlbnQgKi8gZmFsc2UpO1xuICAgICAgICAgICAgICBjb25zdCByZXN1bHQgPVxuICAgICAgICAgICAgICAgICAgZ2V0RXhwcmVzc2lvblN5bWJvbChzY29wZSwgYXN0LnZhbHVlLCB0ZW1wbGF0ZVBvc2l0aW9uLCBpbmZvLnRlbXBsYXRlLnF1ZXJ5KTtcbiAgICAgICAgICAgICAgaWYgKHJlc3VsdCkge1xuICAgICAgICAgICAgICAgIHN5bWJvbCA9IHJlc3VsdC5zeW1ib2w7XG4gICAgICAgICAgICAgICAgc3BhbiA9IG9mZnNldFNwYW4ocmVzdWx0LnNwYW4sIGFzdC5zb3VyY2VTcGFuLnN0YXJ0Lm9mZnNldCk7XG4gICAgICAgICAgICAgIH1cbiAgICAgICAgICAgIH1cbiAgICAgICAgICB9LFxuICAgICAgICAgIHZpc2l0VGV4dChhc3QpIHt9LFxuICAgICAgICAgIHZpc2l0RGlyZWN0aXZlKGFzdCkge1xuICAgICAgICAgICAgY29tcGlsZVR5cGVTdW1tYXJ5ID0gYXN0LmRpcmVjdGl2ZTtcbiAgICAgICAgICAgIHN5bWJvbCA9IGluZm8udGVtcGxhdGUucXVlcnkuZ2V0VHlwZVN5bWJvbChjb21waWxlVHlwZVN1bW1hcnkudHlwZS5yZWZlcmVuY2UpO1xuICAgICAgICAgICAgc3BhbiA9IHNwYW5PZihhc3QpO1xuICAgICAgICAgIH0sXG4gICAgICAgICAgdmlzaXREaXJlY3RpdmVQcm9wZXJ0eShhc3QpIHtcbiAgICAgICAgICAgIGlmICghYXR0cmlidXRlVmFsdWVTeW1ib2woYXN0LnZhbHVlKSkge1xuICAgICAgICAgICAgICBzeW1ib2wgPSBmaW5kSW5wdXRCaW5kaW5nKGluZm8sIHBhdGgsIGFzdCk7XG4gICAgICAgICAgICAgIHNwYW4gPSBzcGFuT2YoYXN0KTtcbiAgICAgICAgICAgIH1cbiAgICAgICAgICB9XG4gICAgICAgIH0sXG4gICAgICAgIG51bGwpO1xuICAgIGlmIChzeW1ib2wgJiYgc3Bhbikge1xuICAgICAgcmV0dXJuIHtzeW1ib2wsIHNwYW46IG9mZnNldFNwYW4oc3BhbiwgaW5mby50ZW1wbGF0ZS5zcGFuLnN0YXJ0KSwgY29tcGlsZVR5cGVTdW1tYXJ5fTtcbiAgICB9XG4gIH1cbn1cblxuZnVuY3Rpb24gZmluZEF0dHJpYnV0ZShpbmZvOiBBc3RSZXN1bHQsIHBvc2l0aW9uOiBudW1iZXIpOiBBdHRyaWJ1dGV8dW5kZWZpbmVkIHtcbiAgY29uc3QgdGVtcGxhdGVQb3NpdGlvbiA9IHBvc2l0aW9uIC0gaW5mby50ZW1wbGF0ZS5zcGFuLnN0YXJ0O1xuICBjb25zdCBwYXRoID0gZmluZE5vZGUoaW5mby5odG1sQXN0LCB0ZW1wbGF0ZVBvc2l0aW9uKTtcbiAgcmV0dXJuIHBhdGguZmlyc3QoQXR0cmlidXRlKTtcbn1cblxuZnVuY3Rpb24gZmluZElucHV0QmluZGluZyhcbiAgICBpbmZvOiBBc3RSZXN1bHQsIHBhdGg6IFRlbXBsYXRlQXN0UGF0aCwgYmluZGluZzogQm91bmREaXJlY3RpdmVQcm9wZXJ0eUFzdCk6IFN5bWJvbHx1bmRlZmluZWQge1xuICBjb25zdCBlbGVtZW50ID0gcGF0aC5maXJzdChFbGVtZW50QXN0KTtcbiAgaWYgKGVsZW1lbnQpIHtcbiAgICBmb3IgKGNvbnN0IGRpcmVjdGl2ZSBvZiBlbGVtZW50LmRpcmVjdGl2ZXMpIHtcbiAgICAgIGNvbnN0IGludmVydGVkSW5wdXQgPSBpbnZlcnRNYXAoZGlyZWN0aXZlLmRpcmVjdGl2ZS5pbnB1dHMpO1xuICAgICAgY29uc3QgZmllbGROYW1lID0gaW52ZXJ0ZWRJbnB1dFtiaW5kaW5nLnRlbXBsYXRlTmFtZV07XG4gICAgICBpZiAoZmllbGROYW1lKSB7XG4gICAgICAgIGNvbnN0IGNsYXNzU3ltYm9sID0gaW5mby50ZW1wbGF0ZS5xdWVyeS5nZXRUeXBlU3ltYm9sKGRpcmVjdGl2ZS5kaXJlY3RpdmUudHlwZS5yZWZlcmVuY2UpO1xuICAgICAgICBpZiAoY2xhc3NTeW1ib2wpIHtcbiAgICAgICAgICByZXR1cm4gY2xhc3NTeW1ib2wubWVtYmVycygpLmdldChmaWVsZE5hbWUpO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuICB9XG59XG5cbmZ1bmN0aW9uIGZpbmRPdXRwdXRCaW5kaW5nKGluZm86IEFzdFJlc3VsdCwgcGF0aDogVGVtcGxhdGVBc3RQYXRoLCBiaW5kaW5nOiBCb3VuZEV2ZW50QXN0KTogU3ltYm9sfFxuICAgIHVuZGVmaW5lZCB7XG4gIGNvbnN0IGVsZW1lbnQgPSBwYXRoLmZpcnN0KEVsZW1lbnRBc3QpO1xuICBpZiAoZWxlbWVudCkge1xuICAgIGZvciAoY29uc3QgZGlyZWN0aXZlIG9mIGVsZW1lbnQuZGlyZWN0aXZlcykge1xuICAgICAgY29uc3QgaW52ZXJ0ZWRPdXRwdXRzID0gaW52ZXJ0TWFwKGRpcmVjdGl2ZS5kaXJlY3RpdmUub3V0cHV0cyk7XG4gICAgICBjb25zdCBmaWVsZE5hbWUgPSBpbnZlcnRlZE91dHB1dHNbYmluZGluZy5uYW1lXTtcbiAgICAgIGlmIChmaWVsZE5hbWUpIHtcbiAgICAgICAgY29uc3QgY2xhc3NTeW1ib2wgPSBpbmZvLnRlbXBsYXRlLnF1ZXJ5LmdldFR5cGVTeW1ib2woZGlyZWN0aXZlLmRpcmVjdGl2ZS50eXBlLnJlZmVyZW5jZSk7XG4gICAgICAgIGlmIChjbGFzc1N5bWJvbCkge1xuICAgICAgICAgIHJldHVybiBjbGFzc1N5bWJvbC5tZW1iZXJzKCkuZ2V0KGZpZWxkTmFtZSk7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICB9XG4gIH1cbn1cblxuZnVuY3Rpb24gaW52ZXJ0TWFwKG9iajoge1tuYW1lOiBzdHJpbmddOiBzdHJpbmd9KToge1tuYW1lOiBzdHJpbmddOiBzdHJpbmd9IHtcbiAgY29uc3QgcmVzdWx0OiB7W25hbWU6IHN0cmluZ106IHN0cmluZ30gPSB7fTtcbiAgZm9yIChjb25zdCBuYW1lIG9mIE9iamVjdC5rZXlzKG9iaikpIHtcbiAgICBjb25zdCB2ID0gb2JqW25hbWVdO1xuICAgIHJlc3VsdFt2XSA9IG5hbWU7XG4gIH1cbiAgcmV0dXJuIHJlc3VsdDtcbn1cblxuLyoqXG4gKiBXcmFwIGEgc3ltYm9sIGFuZCBjaGFuZ2UgaXRzIGtpbmQgdG8gY29tcG9uZW50LlxuICovXG5jbGFzcyBPdmVycmlkZUtpbmRTeW1ib2wgaW1wbGVtZW50cyBTeW1ib2wge1xuICBwdWJsaWMgcmVhZG9ubHkga2luZDogRGlyZWN0aXZlS2luZDtcbiAgY29uc3RydWN0b3IocHJpdmF0ZSBzeW06IFN5bWJvbCwga2luZE92ZXJyaWRlOiBEaXJlY3RpdmVLaW5kKSB7IHRoaXMua2luZCA9IGtpbmRPdmVycmlkZTsgfVxuXG4gIGdldCBuYW1lKCk6IHN0cmluZyB7IHJldHVybiB0aGlzLnN5bS5uYW1lOyB9XG5cbiAgZ2V0IGxhbmd1YWdlKCk6IHN0cmluZyB7IHJldHVybiB0aGlzLnN5bS5sYW5ndWFnZTsgfVxuXG4gIGdldCB0eXBlKCk6IFN5bWJvbHx1bmRlZmluZWQgeyByZXR1cm4gdGhpcy5zeW0udHlwZTsgfVxuXG4gIGdldCBjb250YWluZXIoKTogU3ltYm9sfHVuZGVmaW5lZCB7IHJldHVybiB0aGlzLnN5bS5jb250YWluZXI7IH1cblxuICBnZXQgcHVibGljKCk6IGJvb2xlYW4geyByZXR1cm4gdGhpcy5zeW0ucHVibGljOyB9XG5cbiAgZ2V0IGNhbGxhYmxlKCk6IGJvb2xlYW4geyByZXR1cm4gdGhpcy5zeW0uY2FsbGFibGU7IH1cblxuICBnZXQgbnVsbGFibGUoKTogYm9vbGVhbiB7IHJldHVybiB0aGlzLnN5bS5udWxsYWJsZTsgfVxuXG4gIGdldCBkZWZpbml0aW9uKCk6IERlZmluaXRpb24geyByZXR1cm4gdGhpcy5zeW0uZGVmaW5pdGlvbjsgfVxuXG4gIG1lbWJlcnMoKSB7IHJldHVybiB0aGlzLnN5bS5tZW1iZXJzKCk7IH1cblxuICBzaWduYXR1cmVzKCkgeyByZXR1cm4gdGhpcy5zeW0uc2lnbmF0dXJlcygpOyB9XG5cbiAgc2VsZWN0U2lnbmF0dXJlKHR5cGVzOiBTeW1ib2xbXSkgeyByZXR1cm4gdGhpcy5zeW0uc2VsZWN0U2lnbmF0dXJlKHR5cGVzKTsgfVxuXG4gIGluZGV4ZWQoYXJndW1lbnQ6IFN5bWJvbCkgeyByZXR1cm4gdGhpcy5zeW0uaW5kZXhlZChhcmd1bWVudCk7IH1cbn1cbiJdfQ==