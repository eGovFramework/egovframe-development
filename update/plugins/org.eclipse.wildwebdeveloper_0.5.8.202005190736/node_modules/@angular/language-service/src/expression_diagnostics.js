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
        define("@angular/language-service/src/expression_diagnostics", ["require", "exports", "tslib", "@angular/compiler", "@angular/language-service/src/expression_type", "@angular/language-service/src/symbols"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var tslib_1 = require("tslib");
    var compiler_1 = require("@angular/compiler");
    var expression_type_1 = require("@angular/language-service/src/expression_type");
    var symbols_1 = require("@angular/language-service/src/symbols");
    function getTemplateExpressionDiagnostics(info) {
        var visitor = new ExpressionDiagnosticsVisitor(info, function (path, includeEvent) {
            return getExpressionScope(info, path, includeEvent);
        });
        compiler_1.templateVisitAll(visitor, info.templateAst);
        return visitor.diagnostics;
    }
    exports.getTemplateExpressionDiagnostics = getTemplateExpressionDiagnostics;
    function getExpressionDiagnostics(scope, ast, query, context) {
        if (context === void 0) { context = {}; }
        var analyzer = new expression_type_1.AstType(scope, query, context);
        analyzer.getDiagnostics(ast);
        return analyzer.diagnostics;
    }
    exports.getExpressionDiagnostics = getExpressionDiagnostics;
    function getReferences(info) {
        var result = [];
        function processReferences(references) {
            var e_1, _a;
            var _loop_1 = function (reference) {
                var type = undefined;
                if (reference.value) {
                    type = info.query.getTypeSymbol(compiler_1.tokenReference(reference.value));
                }
                result.push({
                    name: reference.name,
                    kind: 'reference',
                    type: type || info.query.getBuiltinType(symbols_1.BuiltinType.Any),
                    get definition() { return getDefinitionOf(info, reference); }
                });
            };
            try {
                for (var references_1 = tslib_1.__values(references), references_1_1 = references_1.next(); !references_1_1.done; references_1_1 = references_1.next()) {
                    var reference = references_1_1.value;
                    _loop_1(reference);
                }
            }
            catch (e_1_1) { e_1 = { error: e_1_1 }; }
            finally {
                try {
                    if (references_1_1 && !references_1_1.done && (_a = references_1.return)) _a.call(references_1);
                }
                finally { if (e_1) throw e_1.error; }
            }
        }
        var visitor = new /** @class */ (function (_super) {
            tslib_1.__extends(class_1, _super);
            function class_1() {
                return _super !== null && _super.apply(this, arguments) || this;
            }
            class_1.prototype.visitEmbeddedTemplate = function (ast, context) {
                _super.prototype.visitEmbeddedTemplate.call(this, ast, context);
                processReferences(ast.references);
            };
            class_1.prototype.visitElement = function (ast, context) {
                _super.prototype.visitElement.call(this, ast, context);
                processReferences(ast.references);
            };
            return class_1;
        }(compiler_1.RecursiveTemplateAstVisitor));
        compiler_1.templateVisitAll(visitor, info.templateAst);
        return result;
    }
    function getDefinitionOf(info, ast) {
        if (info.fileName) {
            var templateOffset = info.offset;
            return [{
                    fileName: info.fileName,
                    span: {
                        start: ast.sourceSpan.start.offset + templateOffset,
                        end: ast.sourceSpan.end.offset + templateOffset
                    }
                }];
        }
    }
    /**
     * Resolve all variable declarations in a template by traversing the specified
     * `path`.
     * @param info
     * @param path template AST path
     */
    function getVarDeclarations(info, path) {
        var e_2, _a;
        var results = [];
        for (var current = path.head; current; current = path.childOf(current)) {
            if (!(current instanceof compiler_1.EmbeddedTemplateAst)) {
                continue;
            }
            var _loop_2 = function (variable) {
                var symbol = info.members.get(variable.value) || info.query.getBuiltinType(symbols_1.BuiltinType.Any);
                var kind = info.query.getTypeKind(symbol);
                if (kind === symbols_1.BuiltinType.Any || kind === symbols_1.BuiltinType.Unbound) {
                    // For special cases such as ngFor and ngIf, the any type is not very useful.
                    // We can do better by resolving the binding value.
                    var symbolsInScope = info.query.mergeSymbolTable([
                        info.members,
                        // Since we are traversing the AST path from head to tail, any variables
                        // that have been declared so far are also in scope.
                        info.query.createSymbolTable(results),
                    ]);
                    symbol = refinedVariableType(symbolsInScope, info.query, current);
                }
                results.push({
                    name: variable.name,
                    kind: 'variable',
                    type: symbol, get definition() { return getDefinitionOf(info, variable); },
                });
            };
            try {
                for (var _b = (e_2 = void 0, tslib_1.__values(current.variables)), _c = _b.next(); !_c.done; _c = _b.next()) {
                    var variable = _c.value;
                    _loop_2(variable);
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
        return results;
    }
    /**
     * Resolve a more specific type for the variable in `templateElement` by inspecting
     * all variables that are in scope in the `mergedTable`. This function is a special
     * case for `ngFor` and `ngIf`. If resolution fails, return the `any` type.
     * @param mergedTable symbol table for all variables in scope
     * @param query
     * @param templateElement
     */
    function refinedVariableType(mergedTable, query, templateElement) {
        // Special case the ngFor directive
        var ngForDirective = templateElement.directives.find(function (d) {
            var name = compiler_1.identifierName(d.directive.type);
            return name == 'NgFor' || name == 'NgForOf';
        });
        if (ngForDirective) {
            var ngForOfBinding = ngForDirective.inputs.find(function (i) { return i.directiveName == 'ngForOf'; });
            if (ngForOfBinding) {
                var bindingType = new expression_type_1.AstType(mergedTable, query, {}).getType(ngForOfBinding.value);
                if (bindingType) {
                    var result = query.getElementType(bindingType);
                    if (result) {
                        return result;
                    }
                }
            }
        }
        // Special case the ngIf directive ( *ngIf="data$ | async as variable" )
        var ngIfDirective = templateElement.directives.find(function (d) { return compiler_1.identifierName(d.directive.type) === 'NgIf'; });
        if (ngIfDirective) {
            var ngIfBinding = ngIfDirective.inputs.find(function (i) { return i.directiveName === 'ngIf'; });
            if (ngIfBinding) {
                var bindingType = new expression_type_1.AstType(mergedTable, query, {}).getType(ngIfBinding.value);
                if (bindingType) {
                    return bindingType;
                }
            }
        }
        // We can't do better, return any
        return query.getBuiltinType(symbols_1.BuiltinType.Any);
    }
    function getEventDeclaration(info, includeEvent) {
        var result = [];
        if (includeEvent) {
            // TODO: Determine the type of the event parameter based on the Observable<T> or EventEmitter<T>
            // of the event.
            result = [{ name: '$event', kind: 'variable', type: info.query.getBuiltinType(symbols_1.BuiltinType.Any) }];
        }
        return result;
    }
    function getExpressionScope(info, path, includeEvent) {
        var result = info.members;
        var references = getReferences(info);
        var variables = getVarDeclarations(info, path);
        var events = getEventDeclaration(info, includeEvent);
        if (references.length || variables.length || events.length) {
            var referenceTable = info.query.createSymbolTable(references);
            var variableTable = info.query.createSymbolTable(variables);
            var eventsTable = info.query.createSymbolTable(events);
            result = info.query.mergeSymbolTable([result, referenceTable, variableTable, eventsTable]);
        }
        return result;
    }
    exports.getExpressionScope = getExpressionScope;
    var ExpressionDiagnosticsVisitor = /** @class */ (function (_super) {
        tslib_1.__extends(ExpressionDiagnosticsVisitor, _super);
        function ExpressionDiagnosticsVisitor(info, getExpressionScope) {
            var _this = _super.call(this) || this;
            _this.info = info;
            _this.getExpressionScope = getExpressionScope;
            _this.diagnostics = [];
            _this.path = new compiler_1.AstPath([]);
            return _this;
        }
        ExpressionDiagnosticsVisitor.prototype.visitDirective = function (ast, context) {
            // Override the default child visitor to ignore the host properties of a directive.
            if (ast.inputs && ast.inputs.length) {
                compiler_1.templateVisitAll(this, ast.inputs, context);
            }
        };
        ExpressionDiagnosticsVisitor.prototype.visitBoundText = function (ast) {
            this.push(ast);
            this.diagnoseExpression(ast.value, ast.sourceSpan.start.offset, false);
            this.pop();
        };
        ExpressionDiagnosticsVisitor.prototype.visitDirectiveProperty = function (ast) {
            this.push(ast);
            this.diagnoseExpression(ast.value, this.attributeValueLocation(ast), false);
            this.pop();
        };
        ExpressionDiagnosticsVisitor.prototype.visitElementProperty = function (ast) {
            this.push(ast);
            this.diagnoseExpression(ast.value, this.attributeValueLocation(ast), false);
            this.pop();
        };
        ExpressionDiagnosticsVisitor.prototype.visitEvent = function (ast) {
            this.push(ast);
            this.diagnoseExpression(ast.handler, this.attributeValueLocation(ast), true);
            this.pop();
        };
        ExpressionDiagnosticsVisitor.prototype.visitVariable = function (ast) {
            var directive = this.directiveSummary;
            if (directive && ast.value) {
                var context = this.info.query.getTemplateContext(directive.type.reference);
                if (context && !context.has(ast.value)) {
                    if (ast.value === '$implicit') {
                        this.reportError('The template context does not have an implicit value', spanOf(ast.sourceSpan));
                    }
                    else {
                        this.reportError("The template context does not define a member called '" + ast.value + "'", spanOf(ast.sourceSpan));
                    }
                }
            }
        };
        ExpressionDiagnosticsVisitor.prototype.visitElement = function (ast, context) {
            this.push(ast);
            _super.prototype.visitElement.call(this, ast, context);
            this.pop();
        };
        ExpressionDiagnosticsVisitor.prototype.visitEmbeddedTemplate = function (ast, context) {
            var previousDirectiveSummary = this.directiveSummary;
            this.push(ast);
            // Find directive that references this template
            this.directiveSummary =
                ast.directives.map(function (d) { return d.directive; }).find(function (d) { return hasTemplateReference(d.type); });
            // Process children
            _super.prototype.visitEmbeddedTemplate.call(this, ast, context);
            this.pop();
            this.directiveSummary = previousDirectiveSummary;
        };
        ExpressionDiagnosticsVisitor.prototype.attributeValueLocation = function (ast) {
            var path = compiler_1.findNode(this.info.htmlAst, ast.sourceSpan.start.offset);
            var last = path.tail;
            if (last instanceof compiler_1.Attribute && last.valueSpan) {
                return last.valueSpan.start.offset;
            }
            return ast.sourceSpan.start.offset;
        };
        ExpressionDiagnosticsVisitor.prototype.diagnoseExpression = function (ast, offset, includeEvent) {
            var _a;
            var _this = this;
            var scope = this.getExpressionScope(this.path, includeEvent);
            (_a = this.diagnostics).push.apply(_a, tslib_1.__spread(getExpressionDiagnostics(scope, ast, this.info.query, {
                event: includeEvent
            }).map(function (d) { return ({
                span: offsetSpan(d.ast.span, offset + _this.info.offset),
                kind: d.kind,
                message: d.message
            }); })));
        };
        ExpressionDiagnosticsVisitor.prototype.push = function (ast) { this.path.push(ast); };
        ExpressionDiagnosticsVisitor.prototype.pop = function () { this.path.pop(); };
        ExpressionDiagnosticsVisitor.prototype.reportError = function (message, span) {
            if (span) {
                this.diagnostics.push({ span: offsetSpan(span, this.info.offset), kind: expression_type_1.DiagnosticKind.Error, message: message });
            }
        };
        ExpressionDiagnosticsVisitor.prototype.reportWarning = function (message, span) {
            this.diagnostics.push({ span: offsetSpan(span, this.info.offset), kind: expression_type_1.DiagnosticKind.Warning, message: message });
        };
        return ExpressionDiagnosticsVisitor;
    }(compiler_1.RecursiveTemplateAstVisitor));
    function hasTemplateReference(type) {
        var e_3, _a;
        if (type.diDeps) {
            try {
                for (var _b = tslib_1.__values(type.diDeps), _c = _b.next(); !_c.done; _c = _b.next()) {
                    var diDep = _c.value;
                    if (diDep.token && diDep.token.identifier &&
                        compiler_1.identifierName(diDep.token.identifier) == 'TemplateRef')
                        return true;
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
        return false;
    }
    function offsetSpan(span, amount) {
        return { start: span.start + amount, end: span.end + amount };
    }
    function spanOf(sourceSpan) {
        return { start: sourceSpan.start.offset, end: sourceSpan.end.offset };
    }
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZXhwcmVzc2lvbl9kaWFnbm9zdGljcy5qcyIsInNvdXJjZVJvb3QiOiIiLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uL3BhY2thZ2VzL2xhbmd1YWdlLXNlcnZpY2Uvc3JjL2V4cHJlc3Npb25fZGlhZ25vc3RpY3MudHMiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7Ozs7OztHQU1HOzs7Ozs7Ozs7Ozs7O0lBRUgsOENBQWlaO0lBRWpaLGlGQUF3RztJQUN4RyxpRUFBNkc7SUFpQjdHLFNBQWdCLGdDQUFnQyxDQUFDLElBQTRCO1FBRTNFLElBQU0sT0FBTyxHQUFHLElBQUksNEJBQTRCLENBQzVDLElBQUksRUFBRSxVQUFDLElBQXFCLEVBQUUsWUFBcUI7WUFDekMsT0FBQSxrQkFBa0IsQ0FBQyxJQUFJLEVBQUUsSUFBSSxFQUFFLFlBQVksQ0FBQztRQUE1QyxDQUE0QyxDQUFDLENBQUM7UUFDNUQsMkJBQWdCLENBQUMsT0FBTyxFQUFFLElBQUksQ0FBQyxXQUFXLENBQUMsQ0FBQztRQUM1QyxPQUFPLE9BQU8sQ0FBQyxXQUFXLENBQUM7SUFDN0IsQ0FBQztJQVBELDRFQU9DO0lBRUQsU0FBZ0Isd0JBQXdCLENBQ3BDLEtBQWtCLEVBQUUsR0FBUSxFQUFFLEtBQWtCLEVBQ2hELE9BQTBDO1FBQTFDLHdCQUFBLEVBQUEsWUFBMEM7UUFDNUMsSUFBTSxRQUFRLEdBQUcsSUFBSSx5QkFBTyxDQUFDLEtBQUssRUFBRSxLQUFLLEVBQUUsT0FBTyxDQUFDLENBQUM7UUFDcEQsUUFBUSxDQUFDLGNBQWMsQ0FBQyxHQUFHLENBQUMsQ0FBQztRQUM3QixPQUFPLFFBQVEsQ0FBQyxXQUFXLENBQUM7SUFDOUIsQ0FBQztJQU5ELDREQU1DO0lBRUQsU0FBUyxhQUFhLENBQUMsSUFBNEI7UUFDakQsSUFBTSxNQUFNLEdBQXdCLEVBQUUsQ0FBQztRQUV2QyxTQUFTLGlCQUFpQixDQUFDLFVBQTBCOztvQ0FDeEMsU0FBUztnQkFDbEIsSUFBSSxJQUFJLEdBQXFCLFNB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QUFFLE1BQWM7UUFDNUMsT0FBTyxFQUFDLEtBQUssRUFBRSxJQUFJLENBQUMsS0FBSyxHQUFHLE1BQU0sRUFBRSxHQUFHLEVBQUUsSUFBSSxDQUFDLEdBQUcsR0FBRyxNQUFNLEVBQUMsQ0FBQztJQUM5RCxDQUFDO0lBRUQsU0FBUyxNQUFNLENBQUMsVUFBMkI7UUFDekMsT0FBTyxFQUFDLEtBQUssRUFBRSxVQUFVLENBQUMsS0FBSyxDQUFDLE1BQU0sRUFBRSxHQUFHLEVBQUUsVUFBVSxDQUFDLEdBQUcsQ0FBQyxNQUFNLEVBQUMsQ0FBQztJQUN0RSxDQUFDIiwic291cmNlc0NvbnRlbnQiOlsiLyoqXG4gKiBAbGljZW5zZVxuICogQ29weXJpZ2h0IEdvb2dsZSBJbmMuIEFsbCBSaWdodHMgUmVzZXJ2ZWQuXG4gKlxuICogVXNlIG9mIHRoaXMgc291cmNlIGNvZGUgaXMgZ292ZXJuZWQgYnkgYW4gTUlULXN0eWxlIGxpY2Vuc2UgdGhhdCBjYW4gYmVcbiAqIGZvdW5kIGluIHRoZSBMSUNFTlNFIGZpbGUgYXQgaHR0cHM6Ly9hbmd1bGFyLmlvL2xpY2Vuc2VcbiAqL1xuXG5pbXBvcnQge0FTVCwgQXN0UGF0aCwgQXR0cmlidXRlLCBCb3VuZERpcmVjdGl2ZVByb3BlcnR5QXN0LCBCb3VuZEVsZW1lbnRQcm9wZXJ0eUFzdCwgQm91bmRFdmVudEFzdCwgQm91bmRUZXh0QXN0LCBDb21waWxlRGlyZWN0aXZlU3VtbWFyeSwgQ29tcGlsZVR5cGVNZXRhZGF0YSwgRGlyZWN0aXZlQXN0LCBFbGVtZW50QXN0LCBFbWJlZGRlZFRlbXBsYXRlQXN0LCBOb2RlLCBQYXJzZVNvdXJjZVNwYW4sIFJlY3Vyc2l2ZVRlbXBsYXRlQXN0VmlzaXRvciwgUmVmZXJlbmNlQXN0LCBUZW1wbGF0ZUFzdCwgVGVtcGxhdGVBc3RQYXRoLCBWYXJpYWJsZUFzdCwgZmluZE5vZGUsIGlkZW50aWZpZXJOYW1lLCB0ZW1wbGF0ZVZpc2l0QWxsLCB0b2tlblJlZmVyZW5jZX0gZnJvbSAnQGFuZ3VsYXIvY29tcGlsZXInO1xuXG5pbXBvcnQge0FzdFR5cGUsIERpYWdub3N0aWNLaW5kLCBFeHByZXNzaW9uRGlhZ25vc3RpY3NDb250ZXh0LCBUeXBlRGlhZ25vc3RpY30gZnJvbSAnLi9leHByZXNzaW9uX3R5cGUnO1xuaW1wb3J0IHtCdWlsdGluVHlwZSwgRGVmaW5pdGlvbiwgU3BhbiwgU3ltYm9sLCBTeW1ib2xEZWNsYXJhdGlvbiwgU3ltYm9sUXVlcnksIFN5bWJvbFRhYmxlfSBmcm9tICcuL3N5bWJvbHMnO1xuXG5leHBvcnQgaW50ZXJmYWNlIERpYWdub3N0aWNUZW1wbGF0ZUluZm8ge1xuICBmaWxlTmFtZT86IHN0cmluZztcbiAgb2Zmc2V0OiBudW1iZXI7XG4gIHF1ZXJ5OiBTeW1ib2xRdWVyeTtcbiAgbWVtYmVyczogU3ltYm9sVGFibGU7XG4gIGh0bWxBc3Q6IE5vZGVbXTtcbiAgdGVtcGxhdGVBc3Q6IFRlbXBsYXRlQXN0W107XG59XG5cbmV4cG9ydCBpbnRlcmZhY2UgRXhwcmVzc2lvbkRpYWdub3N0aWMge1xuICBtZXNzYWdlOiBzdHJpbmc7XG4gIHNwYW46IFNwYW47XG4gIGtpbmQ6IERpYWdub3N0aWNLaW5kO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gZ2V0VGVtcGxhdGVFeHByZXNzaW9uRGlhZ25vc3RpY3MoaW5mbzogRGlhZ25vc3RpY1RlbXBsYXRlSW5mbyk6XG4gICAgRXhwcmVzc2lvbkRpYWdub3N0aWNbXSB7XG4gIGNvbnN0IHZpc2l0b3IgPSBuZXcgRXhwcmVzc2lvbkRpYWdub3N0aWNzVmlzaXRvcihcbiAgICAgIGluZm8sIChwYXRoOiBUZW1wbGF0ZUFzdFBhdGgsIGluY2x1ZGVFdmVudDogYm9vbGVhbikgPT5cbiAgICAgICAgICAgICAgICBnZXRFeHByZXNzaW9uU2NvcGUoaW5mbywgcGF0aCwgaW5jbHVkZUV2ZW50KSk7XG4gIHRlbXBsYXRlVmlzaXRBbGwodmlzaXRvciwgaW5mby50ZW1wbGF0ZUFzdCk7XG4gIHJldHVybiB2aXNpdG9yLmRpYWdub3N0aWNzO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gZ2V0RXhwcmVzc2lvbkRpYWdub3N0aWNzKFxuICAgIHNjb3BlOiBTeW1ib2xUYWJsZSwgYXN0OiBBU1QsIHF1ZXJ5OiBTeW1ib2xRdWVyeSxcbiAgICBjb250ZXh0OiBFeHByZXNzaW9uRGlhZ25vc3RpY3NDb250ZXh0ID0ge30pOiBUeXBlRGlhZ25vc3RpY1tdIHtcbiAgY29uc3QgYW5hbHl6ZXIgPSBuZXcgQXN0VHlwZShzY29wZSwgcXVlcnksIGNvbnRleHQpO1xuICBhbmFseXplci5nZXREaWFnbm9zdGljcyhhc3QpO1xuICByZXR1cm4gYW5hbHl6ZXIuZGlhZ25vc3RpY3M7XG59XG5cbmZ1bmN0aW9uIGdldFJlZmVyZW5jZXMoaW5mbzogRGlhZ25vc3RpY1RlbXBsYXRlSW5mbyk6IFN5bWJvbERlY2xhcmF0aW9uW10ge1xuICBjb25zdCByZXN1bHQ6IFN5bWJvbERlY2xhcmF0aW9uW10gPSBbXTtcblxuICBmdW5jdGlvbiBwcm9jZXNzUmVmZXJlbmNlcyhyZWZlcmVuY2VzOiBSZWZlcmVuY2VBc3RbXSkge1xuICAgIGZvciAoY29uc3QgcmVmZXJlbmNlIG9mIHJlZmVyZW5jZXMpIHtcbiAgICAgIGxldCB0eXBlOiBTeW1ib2x8dW5kZWZpbmVkID0gdW5kZWZpbmVkO1xuICAgICAgaWYgKHJlZmVyZW5jZS52YWx1ZSkge1xuICAgICAgICB0eXBlID0gaW5mby5xdWVyeS5nZXRUeXBlU3ltYm9sKHRva2VuUmVmZXJlbmNlKHJlZmVyZW5jZS52YWx1ZSkpO1xuICAgICAgfVxuICAgICAgcmVzdWx0LnB1c2goe1xuICAgICAgICBuYW1lOiByZWZlcmVuY2UubmFtZSxcbiAgICAgICAga2luZDogJ3JlZmVyZW5jZScsXG4gICAgICAgIHR5cGU6IHR5cGUgfHwgaW5mby5xdWVyeS5nZXRCdWlsdGluVHlwZShCdWlsdGluVHlwZS5BbnkpLFxuICAgICAgICBnZXQgZGVmaW5pdGlvbigpIHsgcmV0dXJuIGdldERlZmluaXRpb25PZihpbmZvLCByZWZlcmVuY2UpOyB9XG4gICAgICB9KTtcbiAgICB9XG4gIH1cblxuICBjb25zdCB2aXNpdG9yID0gbmV3IGNsYXNzIGV4dGVuZHMgUmVjdXJzaXZlVGVtcGxhdGVBc3RWaXNpdG9yIHtcbiAgICB2aXNpdEVtYmVkZGVkVGVtcGxhdGUoYXN0OiBFbWJlZGRlZFRlbXBsYXRlQXN0LCBjb250ZXh0OiBhbnkpOiBhbnkge1xuICAgICAgc3VwZXIudmlzaXRFbWJlZGRlZFRlbXBsYXRlKGFzdCwgY29udGV4dCk7XG4gICAgICBwcm9jZXNzUmVmZXJlbmNlcyhhc3QucmVmZXJlbmNlcyk7XG4gICAgfVxuICAgIHZpc2l0RWxlbWVudChhc3Q6IEVsZW1lbnRBc3QsIGNvbnRleHQ6IGFueSk6IGFueSB7XG4gICAgICBzdXBlci52aXNpdEVsZW1lbnQoYXN0LCBjb250ZXh0KTtcbiAgICAgIHByb2Nlc3NSZWZlcmVuY2VzKGFzdC5yZWZlcmVuY2VzKTtcbiAgICB9XG4gIH07XG5cbiAgdGVtcGxhdGVWaXNpdEFsbCh2aXNpdG9yLCBpbmZvLnRlbXBsYXRlQXN0KTtcblxuICByZXR1cm4gcmVzdWx0O1xufVxuXG5mdW5jdGlvbiBnZXREZWZpbml0aW9uT2YoaW5mbzogRGlhZ25vc3RpY1RlbXBsYXRlSW5mbywgYXN0OiBUZW1wbGF0ZUFzdCk6IERlZmluaXRpb258dW5kZWZpbmVkIHtcbiAgaWYgKGluZm8uZmlsZU5hbWUpIHtcbiAgICBjb25zdCB0ZW1wbGF0ZU9mZnNldCA9IGluZm8ub2Zmc2V0O1xuICAgIHJldHVybiBbe1xuICAgICAgZmlsZU5hbWU6IGluZm8uZmlsZU5hbWUsXG4gICAgICBzcGFuOiB7XG4gICAgICAgIHN0YXJ0OiBhc3Quc291cmNlU3Bhbi5zdGFydC5vZmZzZXQgKyB0ZW1wbGF0ZU9mZnNldCxcbiAgICAgICAgZW5kOiBhc3Quc291cmNlU3Bhbi5lbmQub2Zmc2V0ICsgdGVtcGxhdGVPZmZzZXRcbiAgICAgIH1cbiAgICB9XTtcbiAgfVxufVxuXG4vKipcbiAqIFJlc29sdmUgYWxsIHZhcmlhYmxlIGRlY2xhcmF0aW9ucyBpbiBhIHRlbXBsYXRlIGJ5IHRyYXZlcnNpbmcgdGhlIHNwZWNpZmllZFxuICogYHBhdGhgLlxuICogQHBhcmFtIGluZm9cbiAqIEBwYXJhbSBwYXRoIHRlbXBsYXRlIEFTVCBwYXRoXG4gKi9cbmZ1bmN0aW9uIGdldFZhckRlY2xhcmF0aW9ucyhcbiAgICBpbmZvOiBEaWFnbm9zdGljVGVtcGxhdGVJbmZvLCBwYXRoOiBUZW1wbGF0ZUFzdFBhdGgpOiBTeW1ib2xEZWNsYXJhdGlvbltdIHtcbiAgY29uc3QgcmVzdWx0czogU3ltYm9sRGVjbGFyYXRpb25bXSA9IFtdO1xuICBmb3IgKGxldCBjdXJyZW50ID0gcGF0aC5oZWFkOyBjdXJyZW50OyBjdXJyZW50ID0gcGF0aC5jaGlsZE9mKGN1cnJlbnQpKSB7XG4gICAgaWYgKCEoY3VycmVudCBpbnN0YW5jZW9mIEVtYmVkZGVkVGVtcGxhdGVBc3QpKSB7XG4gICAgICBjb250aW51ZTtcbiAgICB9XG4gICAgZm9yIChjb25zdCB2YXJpYWJsZSBvZiBjdXJyZW50LnZhcmlhYmxlcykge1xuICAgICAgbGV0IHN5bWJvbCA9IGluZm8ubWVtYmVycy5nZXQodmFyaWFibGUudmFsdWUpIHx8IGluZm8ucXVlcnkuZ2V0QnVpbHRpblR5cGUoQnVpbHRpblR5cGUuQW55KTtcbiAgICAgIGNvbnN0IGtpbmQgPSBpbmZvLnF1ZXJ5LmdldFR5cGVLaW5kKHN5bWJvbCk7XG4gICAgICBpZiAoa2luZCA9PT0gQnVpbHRpblR5cGUuQW55IHx8IGtpbmQgPT09IEJ1aWx0aW5UeXBlLlVuYm91bmQpIHtcbiAgICAgICAgLy8gRm9yIHNwZWNpYWwgY2FzZXMgc3VjaCBhcyBuZ0ZvciBhbmQgbmdJZiwgdGhlIGFueSB0eXBlIGlzIG5vdCB2ZXJ5IHVzZWZ1bC5cbiAgICAgICAgLy8gV2UgY2FuIGRvIGJldHRlciBieSByZXNvbHZpbmcgdGhlIGJpbmRpbmcgdmFsdWUuXG4gICAgICAgIGNvbnN0IHN5bWJvbHNJblNjb3BlID0gaW5mby5xdWVyeS5tZXJnZVN5bWJvbFRhYmxlKFtcbiAgICAgICAgICBpbmZvLm1lbWJlcnMsXG4gICAgICAgICAgLy8gU2luY2Ugd2UgYXJlIHRyYXZlcnNpbmcgdGhlIEFTVCBwYXRoIGZyb20gaGVhZCB0byB0YWlsLCBhbnkgdmFyaWFibGVzXG4gICAgICAgICAgLy8gdGhhdCBoYXZlIGJlZW4gZGVjbGFyZWQgc28gZmFyIGFyZSBhbHNvIGluIHNjb3BlLlxuICAgICAgICAgIGluZm8ucXVlcnkuY3JlYXRlU3ltYm9sVGFibGUocmVzdWx0cyksXG4gICAgICAgIF0pO1xuICAgICAgICBzeW1ib2wgPSByZWZpbmVkVmFyaWFibGVUeXBlKHN5bWJvbHNJblNjb3BlLCBpbmZvLnF1ZXJ5LCBjdXJyZW50KTtcbiAgICAgIH1cbiAgICAgIHJlc3VsdHMucHVzaCh7XG4gICAgICAgIG5hbWU6IHZhcmlhYmxlLm5hbWUsXG4gICAgICAgIGtpbmQ6ICd2YXJpYWJsZScsXG4gICAgICAgIHR5cGU6IHN5bWJvbCwgZ2V0IGRlZmluaXRpb24oKSB7IHJldHVybiBnZXREZWZpbml0aW9uT2YoaW5mbywgdmFyaWFibGUpOyB9LFxuICAgICAgfSk7XG4gICAgfVxuICB9XG4gIHJldHVybiByZXN1bHRzO1xufVxuXG4vKipcbiAqIFJlc29sdmUgYSBtb3JlIHNwZWNpZmljIHR5cGUgZm9yIHRoZSB2YXJpYWJsZSBpbiBgdGVtcGxhdGVFbGVtZW50YCBieSBpbnNwZWN0aW5nXG4gKiBhbGwgdmFyaWFibGVzIHRoYXQgYXJlIGluIHNjb3BlIGluIHRoZSBgbWVyZ2VkVGFibGVgLiBUaGlzIGZ1bmN0aW9uIGlzIGEgc3BlY2lhbFxuICogY2FzZSBmb3IgYG5nRm9yYCBhbmQgYG5nSWZgLiBJZiByZXNvbHV0aW9uIGZhaWxzLCByZXR1cm4gdGhlIGBhbnlgIHR5cGUuXG4gKiBAcGFyYW0gbWVyZ2VkVGFibGUgc3ltYm9sIHRhYmxlIGZvciBhbGwgdmFyaWFibGVzIGluIHNjb3BlXG4gKiBAcGFyYW0gcXVlcnlcbiAqIEBwYXJhbSB0ZW1wbGF0ZUVsZW1lbnRcbiAqL1xuZnVuY3Rpb24gcmVmaW5lZFZhcmlhYmxlVHlwZShcbiAgICBtZXJnZWRUYWJsZTogU3ltYm9sVGFibGUsIHF1ZXJ5OiBTeW1ib2xRdWVyeSwgdGVtcGxhdGVFbGVtZW50OiBFbWJlZGRlZFRlbXBsYXRlQXN0KTogU3ltYm9sIHtcbiAgLy8gU3BlY2lhbCBjYXNlIHRoZSBuZ0ZvciBkaXJlY3RpdmVcbiAgY29uc3QgbmdGb3JEaXJlY3RpdmUgPSB0ZW1wbGF0ZUVsZW1lbnQuZGlyZWN0aXZlcy5maW5kKGQgPT4ge1xuICAgIGNvbnN0IG5hbWUgPSBpZGVudGlmaWVyTmFtZShkLmRpcmVjdGl2ZS50eXBlKTtcbiAgICByZXR1cm4gbmFtZSA9PSAnTmdGb3InIHx8IG5hbWUgPT0gJ05nRm9yT2YnO1xuICB9KTtcbiAgaWYgKG5nRm9yRGlyZWN0aXZlKSB7XG4gICAgY29uc3QgbmdGb3JPZkJpbmRpbmcgPSBuZ0ZvckRpcmVjdGl2ZS5pbnB1dHMuZmluZChpID0+IGkuZGlyZWN0aXZlTmFtZSA9PSAnbmdGb3JPZicpO1xuICAgIGlmIChuZ0Zvck9mQmluZGluZykge1xuICAgICAgY29uc3QgYmluZGluZ1R5cGUgPSBuZXcgQXN0VHlwZShtZXJnZWRUYWJsZSwgcXVlcnksIHt9KS5nZXRUeXBlKG5nRm9yT2ZCaW5kaW5nLnZhbHVlKTtcbiAgICAgIGlmIChiaW5kaW5nVHlwZSkge1xuICAgICAgICBjb25zdCByZXN1bHQgPSBxdWVyeS5nZXRFbGVtZW50VHlwZShiaW5kaW5nVHlwZSk7XG4gICAgICAgIGlmIChyZXN1bHQpIHtcbiAgICAgICAgICByZXR1cm4gcmVzdWx0O1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuICB9XG5cbiAgLy8gU3BlY2lhbCBjYXNlIHRoZSBuZ0lmIGRpcmVjdGl2ZSAoICpuZ0lmPVwiZGF0YSQgfCBhc3luYyBhcyB2YXJpYWJsZVwiIClcbiAgY29uc3QgbmdJZkRpcmVjdGl2ZSA9XG4gICAgICB0ZW1wbGF0ZUVsZW1lbnQuZGlyZWN0aXZlcy5maW5kKGQgPT4gaWRlbnRpZmllck5hbWUoZC5kaXJlY3RpdmUudHlwZSkgPT09ICdOZ0lmJyk7XG4gIGlmIChuZ0lmRGlyZWN0aXZlKSB7XG4gICAgY29uc3QgbmdJZkJpbmRpbmcgPSBuZ0lmRGlyZWN0aXZlLmlucHV0cy5maW5kKGkgPT4gaS5kaXJlY3RpdmVOYW1lID09PSAnbmdJZicpO1xuICAgIGlmIChuZ0lmQmluZGluZykge1xuICAgICAgY29uc3QgYmluZGluZ1R5cGUgPSBuZXcgQXN0VHlwZShtZXJnZWRUYWJsZSwgcXVlcnksIHt9KS5nZXRUeXBlKG5nSWZCaW5kaW5nLnZhbHVlKTtcbiAgICAgIGlmIChiaW5kaW5nVHlwZSkge1xuICAgICAgICByZXR1cm4gYmluZGluZ1R5cGU7XG4gICAgICB9XG4gICAgfVxuICB9XG5cbiAgLy8gV2UgY2FuJ3QgZG8gYmV0dGVyLCByZXR1cm4gYW55XG4gIHJldHVybiBxdWVyeS5nZXRCdWlsdGluVHlwZShCdWlsdGluVHlwZS5BbnkpO1xufVxuXG5mdW5jdGlvbiBnZXRFdmVudERlY2xhcmF0aW9uKGluZm86IERpYWdub3N0aWNUZW1wbGF0ZUluZm8sIGluY2x1ZGVFdmVudD86IGJvb2xlYW4pIHtcbiAgbGV0IHJlc3VsdDogU3ltYm9sRGVjbGFyYXRpb25bXSA9IFtdO1xuICBpZiAoaW5jbHVkZUV2ZW50KSB7XG4gICAgLy8gVE9ETzogRGV0ZXJtaW5lIHRoZSB0eXBlIG9mIHRoZSBldmVudCBwYXJhbWV0ZXIgYmFzZWQgb24gdGhlIE9ic2VydmFibGU8VD4gb3IgRXZlbnRFbWl0dGVyPFQ+XG4gICAgLy8gb2YgdGhlIGV2ZW50LlxuICAgIHJlc3VsdCA9IFt7bmFtZTogJyRldmVudCcsIGtpbmQ6ICd2YXJpYWJsZScsIHR5cGU6IGluZm8ucXVlcnkuZ2V0QnVpbHRpblR5cGUoQnVpbHRpblR5cGUuQW55KX1dO1xuICB9XG4gIHJldHVybiByZXN1bHQ7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBnZXRFeHByZXNzaW9uU2NvcGUoXG4gICAgaW5mbzogRGlhZ25vc3RpY1RlbXBsYXRlSW5mbywgcGF0aDogVGVtcGxhdGVBc3RQYXRoLCBpbmNsdWRlRXZlbnQ6IGJvb2xlYW4pOiBTeW1ib2xUYWJsZSB7XG4gIGxldCByZXN1bHQgPSBpbmZvLm1lbWJlcnM7XG4gIGNvbnN0IHJlZmVyZW5jZXMgPSBnZXRSZWZlcmVuY2VzKGluZm8pO1xuICBjb25zdCB2YXJpYWJsZXMgPSBnZXRWYXJEZWNsYXJhdGlvbnMoaW5mbywgcGF0aCk7XG4gIGNvbnN0IGV2ZW50cyA9IGdldEV2ZW50RGVjbGFyYXRpb24oaW5mbywgaW5jbHVkZUV2ZW50KTtcbiAgaWYgKHJlZmVyZW5jZXMubGVuZ3RoIHx8IHZhcmlhYmxlcy5sZW5ndGggfHwgZXZlbnRzLmxlbmd0aCkge1xuICAgIGNvbnN0IHJlZmVyZW5jZVRhYmxlID0gaW5mby5xdWVyeS5jcmVhdGVTeW1ib2xUYWJsZShyZWZlcmVuY2VzKTtcbiAgICBjb25zdCB2YXJpYWJsZVRhYmxlID0gaW5mby5xdWVyeS5jcmVhdGVTeW1ib2xUYWJsZSh2YXJpYWJsZXMpO1xuICAgIGNvbnN0IGV2ZW50c1RhYmxlID0gaW5mby5xdWVyeS5jcmVhdGVTeW1ib2xUYWJsZShldmVudHMpO1xuICAgIHJlc3VsdCA9IGluZm8ucXVlcnkubWVyZ2VTeW1ib2xUYWJsZShbcmVzdWx0LCByZWZlcmVuY2VUYWJsZSwgdmFyaWFibGVUYWJsZSwgZXZlbnRzVGFibGVdKTtcbiAgfVxuICByZXR1cm4gcmVzdWx0O1xufVxuXG5jbGFzcyBFeHByZXNzaW9uRGlhZ25vc3RpY3NWaXNpdG9yIGV4dGVuZHMgUmVjdXJzaXZlVGVtcGxhdGVBc3RWaXNpdG9yIHtcbiAgcHJpdmF0ZSBwYXRoOiBUZW1wbGF0ZUFzdFBhdGg7XG4gIC8vIFRPRE8oaXNzdWUvMjQ1NzEpOiByZW1vdmUgJyEnLlxuICBwcml2YXRlIGRpcmVjdGl2ZVN1bW1hcnkgITogQ29tcGlsZURpcmVjdGl2ZVN1bW1hcnk7XG5cbiAgZGlhZ25vc3RpY3M6IEV4cHJlc3Npb25EaWFnbm9zdGljW10gPSBbXTtcblxuICBjb25zdHJ1Y3RvcihcbiAgICAgIHByaXZhdGUgaW5mbzogRGlhZ25vc3RpY1RlbXBsYXRlSW5mbyxcbiAgICAgIHByaXZhdGUgZ2V0RXhwcmVzc2lvblNjb3BlOiAocGF0aDogVGVtcGxhdGVBc3RQYXRoLCBpbmNsdWRlRXZlbnQ6IGJvb2xlYW4pID0+IFN5bWJvbFRhYmxlKSB7XG4gICAgc3VwZXIoKTtcbiAgICB0aGlzLnBhdGggPSBuZXcgQXN0UGF0aDxUZW1wbGF0ZUFzdD4oW10pO1xuICB9XG5cbiAgdmlzaXREaXJlY3RpdmUoYXN0OiBEaXJlY3RpdmVBc3QsIGNvbnRleHQ6IGFueSk6IGFueSB7XG4gICAgLy8gT3ZlcnJpZGUgdGhlIGRlZmF1bHQgY2hpbGQgdmlzaXRvciB0byBpZ25vcmUgdGhlIGhvc3QgcHJvcGVydGllcyBvZiBhIGRpcmVjdGl2ZS5cbiAgICBpZiAoYXN0LmlucHV0cyAmJiBhc3QuaW5wdXRzLmxlbmd0aCkge1xuICAgICAgdGVtcGxhdGVWaXNpdEFsbCh0aGlzLCBhc3QuaW5wdXRzLCBjb250ZXh0KTtcbiAgICB9XG4gIH1cblxuICB2aXNpdEJvdW5kVGV4dChhc3Q6IEJvdW5kVGV4dEFzdCk6IHZvaWQge1xuICAgIHRoaXMucHVzaChhc3QpO1xuICAgIHRoaXMuZGlhZ25vc2VFeHByZXNzaW9uKGFzdC52YWx1ZSwgYXN0LnNvdXJjZVNwYW4uc3RhcnQub2Zmc2V0LCBmYWxzZSk7XG4gICAgdGhpcy5wb3AoKTtcbiAgfVxuXG4gIHZpc2l0RGlyZWN0aXZlUHJvcGVydHkoYXN0OiBCb3VuZERpcmVjdGl2ZVByb3BlcnR5QXN0KTogdm9pZCB7XG4gICAgdGhpcy5wdXNoKGFzdCk7XG4gICAgdGhpcy5kaWFnbm9zZUV4cHJlc3Npb24oYXN0LnZhbHVlLCB0aGlzLmF0dHJpYnV0ZVZhbHVlTG9jYXRpb24oYXN0KSwgZmFsc2UpO1xuICAgIHRoaXMucG9wKCk7XG4gIH1cblxuICB2aXNpdEVsZW1lbnRQcm9wZXJ0eShhc3Q6IEJvdW5kRWxlbWVudFByb3BlcnR5QXN0KTogdm9pZCB7XG4gICAgdGhpcy5wdXNoKGFzdCk7XG4gICAgdGhpcy5kaWFnbm9zZUV4cHJlc3Npb24oYXN0LnZhbHVlLCB0aGlzLmF0dHJpYnV0ZVZhbHVlTG9jYXRpb24oYXN0KSwgZmFsc2UpO1xuICAgIHRoaXMucG9wKCk7XG4gIH1cblxuICB2aXNpdEV2ZW50KGFzdDogQm91bmRFdmVudEFzdCk6IHZvaWQge1xuICAgIHRoaXMucHVzaChhc3QpO1xuICAgIHRoaXMuZGlhZ25vc2VFeHByZXNzaW9uKGFzdC5oYW5kbGVyLCB0aGlzLmF0dHJpYnV0ZVZhbHVlTG9jYXRpb24oYXN0KSwgdHJ1ZSk7XG4gICAgdGhpcy5wb3AoKTtcbiAgfVxuXG4gIHZpc2l0VmFyaWFibGUoYXN0OiBWYXJpYWJsZUFzdCk6IHZvaWQge1xuICAgIGNvbnN0IGRpcmVjdGl2ZSA9IHRoaXMuZGlyZWN0aXZlU3VtbWFyeTtcbiAgICBpZiAoZGlyZWN0aXZlICYmIGFzdC52YWx1ZSkge1xuICAgICAgY29uc3QgY29udGV4dCA9IHRoaXMuaW5mby5xdWVyeS5nZXRUZW1wbGF0ZUNvbnRleHQoZGlyZWN0aXZlLnR5cGUucmVmZXJlbmNlKSAhO1xuICAgICAgaWYgKGNvbnRleHQgJiYgIWNvbnRleHQuaGFzKGFzdC52YWx1ZSkpIHtcbiAgICAgICAgaWYgKGFzdC52YWx1ZSA9PT0gJyRpbXBsaWNpdCcpIHtcbiAgICAgICAgICB0aGlzLnJlcG9ydEVycm9yKFxuICAgICAgICAgICAgICAnVGhlIHRlbXBsYXRlIGNvbnRleHQgZG9lcyBub3QgaGF2ZSBhbiBpbXBsaWNpdCB2YWx1ZScsIHNwYW5PZihhc3Quc291cmNlU3BhbikpO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIHRoaXMucmVwb3J0RXJyb3IoXG4gICAgICAgICAgICAgIGBUaGUgdGVtcGxhdGUgY29udGV4dCBkb2VzIG5vdCBkZWZpbmUgYSBtZW1iZXIgY2FsbGVkICcke2FzdC52YWx1ZX0nYCxcbiAgICAgICAgICAgICAgc3Bhbk9mKGFzdC5zb3VyY2VTcGFuKSk7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICB9XG4gIH1cblxuICB2aXNpdEVsZW1lbnQoYXN0OiBFbGVtZW50QXN0LCBjb250ZXh0OiBhbnkpOiB2b2lkIHtcbiAgICB0aGlzLnB1c2goYXN0KTtcbiAgICBzdXBlci52aXNpdEVsZW1lbnQoYXN0LCBjb250ZXh0KTtcbiAgICB0aGlzLnBvcCgpO1xuICB9XG5cbiAgdmlzaXRFbWJlZGRlZFRlbXBsYXRlKGFzdDogRW1iZWRkZWRUZW1wbGF0ZUFzdCwgY29udGV4dDogYW55KTogYW55IHtcbiAgICBjb25zdCBwcmV2aW91c0RpcmVjdGl2ZVN1bW1hcnkgPSB0aGlzLmRpcmVjdGl2ZVN1bW1hcnk7XG5cbiAgICB0aGlzLnB1c2goYXN0KTtcblxuICAgIC8vIEZpbmQgZGlyZWN0aXZlIHRoYXQgcmVmZXJlbmNlcyB0aGlzIHRlbXBsYXRlXG4gICAgdGhpcy5kaXJlY3RpdmVTdW1tYXJ5ID1cbiAgICAgICAgYXN0LmRpcmVjdGl2ZXMubWFwKGQgPT4gZC5kaXJlY3RpdmUpLmZpbmQoZCA9PiBoYXNUZW1wbGF0ZVJlZmVyZW5jZShkLnR5cGUpKSAhO1xuXG4gICAgLy8gUHJvY2VzcyBjaGlsZHJlblxuICAgIHN1cGVyLnZpc2l0RW1iZWRkZWRUZW1wbGF0ZShhc3QsIGNvbnRleHQpO1xuXG4gICAgdGhpcy5wb3AoKTtcblxuICAgIHRoaXMuZGlyZWN0aXZlU3VtbWFyeSA9IHByZXZpb3VzRGlyZWN0aXZlU3VtbWFyeTtcbiAgfVxuXG4gIHByaXZhdGUgYXR0cmlidXRlVmFsdWVMb2NhdGlvbihhc3Q6IFRlbXBsYXRlQXN0KSB7XG4gICAgY29uc3QgcGF0aCA9IGZpbmROb2RlKHRoaXMuaW5mby5odG1sQXN0LCBhc3Quc291cmNlU3Bhbi5zdGFydC5vZmZzZXQpO1xuICAgIGNvbnN0IGxhc3QgPSBwYXRoLnRhaWw7XG4gICAgaWYgKGxhc3QgaW5zdGFuY2VvZiBBdHRyaWJ1dGUgJiYgbGFzdC52YWx1ZVNwYW4pIHtcbiAgICAgIHJldHVybiBsYXN0LnZhbHVlU3Bhbi5zdGFydC5vZmZzZXQ7XG4gICAgfVxuICAgIHJldHVybiBhc3Quc291cmNlU3Bhbi5zdGFydC5vZmZzZXQ7XG4gIH1cblxuICBwcml2YXRlIGRpYWdub3NlRXhwcmVzc2lvbihhc3Q6IEFTVCwgb2Zmc2V0OiBudW1iZXIsIGluY2x1ZGVFdmVudDogYm9vbGVhbikge1xuICAgIGNvbnN0IHNjb3BlID0gdGhpcy5nZXRFeHByZXNzaW9uU2NvcGUodGhpcy5wYXRoLCBpbmNsdWRlRXZlbnQpO1xuICAgIHRoaXMuZGlhZ25vc3RpY3MucHVzaCguLi5nZXRFeHByZXNzaW9uRGlhZ25vc3RpY3Moc2NvcGUsIGFzdCwgdGhpcy5pbmZvLnF1ZXJ5LCB7XG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgZXZlbnQ6IGluY2x1ZGVFdmVudFxuICAgICAgICAgICAgICAgICAgICAgICAgICB9KS5tYXAoZCA9PiAoe1xuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBzcGFuOiBvZmZzZXRTcGFuKGQuYXN0LnNwYW4sIG9mZnNldCArIHRoaXMuaW5mby5vZmZzZXQpLFxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBraW5kOiBkLmtpbmQsXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIG1lc3NhZ2U6IGQubWVzc2FnZVxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgfSkpKTtcbiAgfVxuXG4gIHByaXZhdGUgcHVzaChhc3Q6IFRlbXBsYXRlQXN0KSB7IHRoaXMucGF0aC5wdXNoKGFzdCk7IH1cblxuICBwcml2YXRlIHBvcCgpIHsgdGhpcy5wYXRoLnBvcCgpOyB9XG5cbiAgcHJpdmF0ZSByZXBvcnRFcnJvcihtZXNzYWdlOiBzdHJpbmcsIHNwYW46IFNwYW58dW5kZWZpbmVkKSB7XG4gICAgaWYgKHNwYW4pIHtcbiAgICAgIHRoaXMuZGlhZ25vc3RpY3MucHVzaChcbiAgICAgICAgICB7c3Bhbjogb2Zmc2V0U3BhbihzcGFuLCB0aGlzLmluZm8ub2Zmc2V0KSwga2luZDogRGlhZ25vc3RpY0tpbmQuRXJyb3IsIG1lc3NhZ2V9KTtcbiAgICB9XG4gIH1cblxuICBwcml2YXRlIHJlcG9ydFdhcm5pbmcobWVzc2FnZTogc3RyaW5nLCBzcGFuOiBTcGFuKSB7XG4gICAgdGhpcy5kaWFnbm9zdGljcy5wdXNoKFxuICAgICAgICB7c3Bhbjogb2Zmc2V0U3BhbihzcGFuLCB0aGlzLmluZm8ub2Zmc2V0KSwga2luZDogRGlhZ25vc3RpY0tpbmQuV2FybmluZywgbWVzc2FnZX0pO1xuICB9XG59XG5cbmZ1bmN0aW9uIGhhc1RlbXBsYXRlUmVmZXJlbmNlKHR5cGU6IENvbXBpbGVUeXBlTWV0YWRhdGEpOiBib29sZWFuIHtcbiAgaWYgKHR5cGUuZGlEZXBzKSB7XG4gICAgZm9yIChsZXQgZGlEZXAgb2YgdHlwZS5kaURlcHMpIHtcbiAgICAgIGlmIChkaURlcC50b2tlbiAmJiBkaURlcC50b2tlbi5pZGVudGlmaWVyICYmXG4gICAgICAgICAgaWRlbnRpZmllck5hbWUoZGlEZXAudG9rZW4gIS5pZGVudGlmaWVyICEpID09ICdUZW1wbGF0ZVJlZicpXG4gICAgICAgIHJldHVybiB0cnVlO1xuICAgIH1cbiAgfVxuICByZXR1cm4gZmFsc2U7XG59XG5cbmZ1bmN0aW9uIG9mZnNldFNwYW4oc3BhbjogU3BhbiwgYW1vdW50OiBudW1iZXIpOiBTcGFuIHtcbiAgcmV0dXJuIHtzdGFydDogc3Bhbi5zdGFydCArIGFtb3VudCwgZW5kOiBzcGFuLmVuZCArIGFtb3VudH07XG59XG5cbmZ1bmN0aW9uIHNwYW5PZihzb3VyY2VTcGFuOiBQYXJzZVNvdXJjZVNwYW4pOiBTcGFuIHtcbiAgcmV0dXJuIHtzdGFydDogc291cmNlU3Bhbi5zdGFydC5vZmZzZXQsIGVuZDogc291cmNlU3Bhbi5lbmQub2Zmc2V0fTtcbn1cbiJdfQ==