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
        define("@angular/language-service/src/template", ["require", "exports", "tslib", "typescript", "@angular/language-service/src/common", "@angular/language-service/src/global_symbols", "@angular/language-service/src/typescript_symbols"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var tslib_1 = require("tslib");
    var ts = require("typescript");
    var common_1 = require("@angular/language-service/src/common");
    var global_symbols_1 = require("@angular/language-service/src/global_symbols");
    var typescript_symbols_1 = require("@angular/language-service/src/typescript_symbols");
    /**
     * A base class to represent a template and which component class it is
     * associated with. A template source could answer basic questions about
     * top-level declarations of its class through the members() and query()
     * methods.
     */
    var BaseTemplate = /** @class */ (function () {
        function BaseTemplate(host, classDeclNode, classSymbol) {
            this.host = host;
            this.classDeclNode = classDeclNode;
            this.classSymbol = classSymbol;
            this.program = host.program;
        }
        Object.defineProperty(BaseTemplate.prototype, "type", {
            /**
             * Return the Angular StaticSymbol for the class that contains this template.
             */
            get: function () { return this.classSymbol; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(BaseTemplate.prototype, "members", {
            /**
             * Return a Map-like data structure that allows users to retrieve some or all
             * top-level declarations in the associated component class.
             */
            get: function () {
                if (!this.membersTable) {
                    var typeChecker = this.program.getTypeChecker();
                    var sourceFile = this.classDeclNode.getSourceFile();
                    this.membersTable = this.query.mergeSymbolTable([
                        global_symbols_1.createGlobalSymbolTable(this.query),
                        typescript_symbols_1.getClassMembersFromDeclaration(this.program, typeChecker, sourceFile, this.classDeclNode),
                    ]);
                }
                return this.membersTable;
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(BaseTemplate.prototype, "query", {
            /**
             * Return an engine that provides more information about symbols in the
             * template.
             */
            get: function () {
                var _this = this;
                if (!this.queryCache) {
                    var program_1 = this.program;
                    var typeChecker_1 = program_1.getTypeChecker();
                    var sourceFile_1 = this.classDeclNode.getSourceFile();
                    this.queryCache = typescript_symbols_1.getSymbolQuery(program_1, typeChecker_1, sourceFile_1, function () {
                        // Computing the ast is relatively expensive. Do it only when absolutely
                        // necessary.
                        // TODO: There is circular dependency here between TemplateSource and
                        // TypeScriptHost. Consider refactoring the code to break this cycle.
                        var ast = _this.host.getTemplateAst(_this);
                        var pipes = common_1.isAstResult(ast) ? ast.pipes : [];
                        return typescript_symbols_1.getPipesTable(sourceFile_1, program_1, typeChecker_1, pipes);
                    });
                }
                return this.queryCache;
            },
            enumerable: true,
            configurable: true
        });
        return BaseTemplate;
    }());
    /**
     * An InlineTemplate represents template defined in a TS file through the
     * `template` attribute in the decorator.
     */
    var InlineTemplate = /** @class */ (function (_super) {
        tslib_1.__extends(InlineTemplate, _super);
        function InlineTemplate(templateNode, classDeclNode, classSymbol, host) {
            var _this = _super.call(this, host, classDeclNode, classSymbol) || this;
            var sourceFile = templateNode.getSourceFile();
            if (sourceFile !== classDeclNode.getSourceFile()) {
                throw new Error("Inline template and component class should belong to the same source file");
            }
            _this.fileName = sourceFile.fileName;
            _this.source = templateNode.text;
            _this.span = {
                // TS string literal includes surrounding quotes in the start/end offsets.
                start: templateNode.getStart() + 1,
                end: templateNode.getEnd() - 1,
            };
            return _this;
        }
        return InlineTemplate;
    }(BaseTemplate));
    exports.InlineTemplate = InlineTemplate;
    /**
     * An ExternalTemplate represents template defined in an external (most likely
     * HTML, but not necessarily) file through the `templateUrl` attribute in the
     * decorator.
     * Note that there is no ts.Node associated with the template because it's not
     * a TS file.
     */
    var ExternalTemplate = /** @class */ (function (_super) {
        tslib_1.__extends(ExternalTemplate, _super);
        function ExternalTemplate(source, fileName, classDeclNode, classSymbol, host) {
            var _this = _super.call(this, host, classDeclNode, classSymbol) || this;
            _this.source = source;
            _this.fileName = fileName;
            _this.span = {
                start: 0,
                end: source.length,
            };
            return _this;
        }
        return ExternalTemplate;
    }(BaseTemplate));
    exports.ExternalTemplate = ExternalTemplate;
    /**
     * Returns a property assignment from the assignment value, or `undefined` if there is no
     * assignment.
     */
    function getPropertyAssignmentFromValue(value) {
        if (!value.parent || !ts.isPropertyAssignment(value.parent)) {
            return;
        }
        return value.parent;
    }
    exports.getPropertyAssignmentFromValue = getPropertyAssignmentFromValue;
    /**
     * Given a decorator property assignment, return the ClassDeclaration node that corresponds to the
     * directive class the property applies to.
     * If the property assignment is not on a class decorator, no declaration is returned.
     *
     * For example,
     *
     * @Component({
     *   template: '<div></div>'
     *   ^^^^^^^^^^^^^^^^^^^^^^^---- property assignment
     * })
     * class AppComponent {}
     *           ^---- class declaration node
     *
     * @param propAsgn property assignment
     */
    function getClassDeclFromDecoratorProp(propAsgnNode) {
        if (!propAsgnNode.parent || !ts.isObjectLiteralExpression(propAsgnNode.parent)) {
            return;
        }
        var objLitExprNode = propAsgnNode.parent;
        if (!objLitExprNode.parent || !ts.isCallExpression(objLitExprNode.parent)) {
            return;
        }
        var callExprNode = objLitExprNode.parent;
        if (!callExprNode.parent || !ts.isDecorator(callExprNode.parent)) {
            return;
        }
        var decorator = callExprNode.parent;
        if (!decorator.parent || !ts.isClassDeclaration(decorator.parent)) {
            return;
        }
        var classDeclNode = decorator.parent;
        return classDeclNode;
    }
    exports.getClassDeclFromDecoratorProp = getClassDeclFromDecoratorProp;
    /**
     * Determines if a property assignment is on a class decorator.
     * See `getClassDeclFromDecoratorProperty`, which gets the class the decorator is applied to, for
     * more details.
     *
     * @param prop property assignment
     */
    function isClassDecoratorProperty(propAsgn) {
        return !!getClassDeclFromDecoratorProp(propAsgn);
    }
    exports.isClassDecoratorProperty = isClassDecoratorProperty;
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidGVtcGxhdGUuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi8uLi8uLi9wYWNrYWdlcy9sYW5ndWFnZS1zZXJ2aWNlL3NyYy90ZW1wbGF0ZS50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTs7Ozs7O0dBTUc7Ozs7Ozs7Ozs7Ozs7SUFFSCwrQkFBaUM7SUFFakMsK0RBQXFDO0lBQ3JDLCtFQUF5RDtJQUd6RCx1RkFBbUc7SUFHbkc7Ozs7O09BS0c7SUFDSDtRQUtFLHNCQUNxQixJQUEyQixFQUMzQixhQUFrQyxFQUNsQyxXQUE0QjtZQUY1QixTQUFJLEdBQUosSUFBSSxDQUF1QjtZQUMzQixrQkFBYSxHQUFiLGFBQWEsQ0FBcUI7WUFDbEMsZ0JBQVcsR0FBWCxXQUFXLENBQWlCO1lBQy9DLElBQUksQ0FBQyxPQUFPLEdBQUcsSUFBSSxDQUFDLE9BQU8sQ0FBQztRQUM5QixDQUFDO1FBU0Qsc0JBQUksOEJBQUk7WUFIUjs7ZUFFRztpQkFDSCxjQUFhLE9BQU8sSUFBSSxDQUFDLFdBQVcsQ0FBQyxDQUFDLENBQUM7OztXQUFBO1FBTXZDLHNCQUFJLGlDQUFPO1lBSlg7OztlQUdHO2lCQUNIO2dCQUNFLElBQUksQ0FBQyxJQUFJLENBQUMsWUFBWSxFQUFFO29CQUN0QixJQUFNLFdBQVcsR0FBRyxJQUFJLENBQUMsT0FBTyxDQUFDLGNBQWMsRUFBRSxDQUFDO29CQUNsRCxJQUFNLFVBQVUsR0FBRyxJQUFJLENBQUMsYUFBYSxDQUFDLGFBQWEsRUFBRSxDQUFDO29CQUN0RCxJQUFJLENBQUMsWUFBWSxHQUFHLElBQUksQ0FBQyxLQUFLLENBQUMsZ0JBQWdCLENBQUM7d0JBQzlDLHdDQUF1QixDQUFDLElBQUksQ0FBQyxLQUFLLENBQUM7d0JBQ25DLG1EQUE4QixDQUFDLElBQUksQ0FBQyxPQUFPLEVBQUUsV0FBVyxFQUFFLFVBQVUsRUFBRSxJQUFJLENBQUMsYUFBYSxDQUFDO3FCQUMxRixDQUFDLENBQUM7aUJBQ0o7Z0JBQ0Qs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cHlyaWdodCBHb29nbGUgSW5jLiBBbGwgUmlnaHRzIFJlc2VydmVkLlxuICpcbiAqIFVzZSBvZiB0aGlzIHNvdXJjZSBjb2RlIGlzIGdvdmVybmVkIGJ5IGFuIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vYW5ndWxhci5pby9saWNlbnNlXG4gKi9cblxuaW1wb3J0ICogYXMgdHMgZnJvbSAndHlwZXNjcmlwdCc7XG5cbmltcG9ydCB7aXNBc3RSZXN1bHR9IGZyb20gJy4vY29tbW9uJztcbmltcG9ydCB7Y3JlYXRlR2xvYmFsU3ltYm9sVGFibGV9IGZyb20gJy4vZ2xvYmFsX3N5bWJvbHMnO1xuaW1wb3J0ICogYXMgbmcgZnJvbSAnLi90eXBlcyc7XG5pbXBvcnQge1R5cGVTY3JpcHRTZXJ2aWNlSG9zdH0gZnJvbSAnLi90eXBlc2NyaXB0X2hvc3QnO1xuaW1wb3J0IHtnZXRDbGFzc01lbWJlcnNGcm9tRGVjbGFyYXRpb24sIGdldFBpcGVzVGFibGUsIGdldFN5bWJvbFF1ZXJ5fSBmcm9tICcuL3R5cGVzY3JpcHRfc3ltYm9scyc7XG5cblxuLyoqXG4gKiBBIGJhc2UgY2xhc3MgdG8gcmVwcmVzZW50IGEgdGVtcGxhdGUgYW5kIHdoaWNoIGNvbXBvbmVudCBjbGFzcyBpdCBpc1xuICogYXNzb2NpYXRlZCB3aXRoLiBBIHRlbXBsYXRlIHNvdXJjZSBjb3VsZCBhbnN3ZXIgYmFzaWMgcXVlc3Rpb25zIGFib3V0XG4gKiB0b3AtbGV2ZWwgZGVjbGFyYXRpb25zIG9mIGl0cyBjbGFzcyB0aHJvdWdoIHRoZSBtZW1iZXJzKCkgYW5kIHF1ZXJ5KClcbiAqIG1ldGhvZHMuXG4gKi9cbmFic3RyYWN0IGNsYXNzIEJhc2VUZW1wbGF0ZSBpbXBsZW1lbnRzIG5nLlRlbXBsYXRlU291cmNlIHtcbiAgcHJpdmF0ZSByZWFkb25seSBwcm9ncmFtOiB0cy5Qcm9ncmFtO1xuICBwcml2YXRlIG1lbWJlcnNUYWJsZTogbmcuU3ltYm9sVGFibGV8dW5kZWZpbmVkO1xuICBwcml2YXRlIHF1ZXJ5Q2FjaGU6IG5nLlN5bWJvbFF1ZXJ5fHVuZGVmaW5lZDtcblxuICBjb25zdHJ1Y3RvcihcbiAgICAgIHByaXZhdGUgcmVhZG9ubHkgaG9zdDogVHlwZVNjcmlwdFNlcnZpY2VIb3N0LFxuICAgICAgcHJpdmF0ZSByZWFkb25seSBjbGFzc0RlY2xOb2RlOiB0cy5DbGFzc0RlY2xhcmF0aW9uLFxuICAgICAgcHJpdmF0ZSByZWFkb25seSBjbGFzc1N5bWJvbDogbmcuU3RhdGljU3ltYm9sKSB7XG4gICAgdGhpcy5wcm9ncmFtID0gaG9zdC5wcm9ncmFtO1xuICB9XG5cbiAgYWJzdHJhY3QgZ2V0IHNwYW4oKTogbmcuU3BhbjtcbiAgYWJzdHJhY3QgZ2V0IGZpbGVOYW1lKCk6IHN0cmluZztcbiAgYWJzdHJhY3QgZ2V0IHNvdXJjZSgpOiBzdHJpbmc7XG5cbiAgLyoqXG4gICAqIFJldHVybiB0aGUgQW5ndWxhciBTdGF0aWNTeW1ib2wgZm9yIHRoZSBjbGFzcyB0aGF0IGNvbnRhaW5zIHRoaXMgdGVtcGxhdGUuXG4gICAqL1xuICBnZXQgdHlwZSgpIHsgcmV0dXJuIHRoaXMuY2xhc3NTeW1ib2w7IH1cblxuICAvKipcbiAgICogUmV0dXJuIGEgTWFwLWxpa2UgZGF0YSBzdHJ1Y3R1cmUgdGhhdCBhbGxvd3MgdXNlcnMgdG8gcmV0cmlldmUgc29tZSBvciBhbGxcbiAgICogdG9wLWxldmVsIGRlY2xhcmF0aW9ucyBpbiB0aGUgYXNzb2NpYXRlZCBjb21wb25lbnQgY2xhc3MuXG4gICAqL1xuICBnZXQgbWVtYmVycygpIHtcbiAgICBpZiAoIXRoaXMubWVtYmVyc1RhYmxlKSB7XG4gICAgICBjb25zdCB0eXBlQ2hlY2tlciA9IHRoaXMucHJvZ3JhbS5nZXRUeXBlQ2hlY2tlcigpO1xuICAgICAgY29uc3Qgc291cmNlRmlsZSA9IHRoaXMuY2xhc3NEZWNsTm9kZS5nZXRTb3VyY2VGaWxlKCk7XG4gICAgICB0aGlzLm1lbWJlcnNUYWJsZSA9IHRoaXMucXVlcnkubWVyZ2VTeW1ib2xUYWJsZShbXG4gICAgICAgIGNyZWF0ZUdsb2JhbFN5bWJvbFRhYmxlKHRoaXMucXVlcnkpLFxuICAgICAgICBnZXRDbGFzc01lbWJlcnNGcm9tRGVjbGFyYXRpb24odGhpcy5wcm9ncmFtLCB0eXBlQ2hlY2tlciwgc291cmNlRmlsZSwgdGhpcy5jbGFzc0RlY2xOb2RlKSxcbiAgICAgIF0pO1xuICAgIH1cbiAgICByZXR1cm4gdGhpcy5tZW1iZXJzVGFibGU7XG4gIH1cblxuICAvKipcbiAgICogUmV0dXJuIGFuIGVuZ2luZSB0aGF0IHByb3ZpZGVzIG1vcmUgaW5mb3JtYXRpb24gYWJvdXQgc3ltYm9scyBpbiB0aGVcbiAgICogdGVtcGxhdGUuXG4gICAqL1xuICBnZXQgcXVlcnkoKSB7XG4gICAgaWYgKCF0aGlzLnF1ZXJ5Q2FjaGUpIHtcbiAgICAgIGNvbnN0IHByb2dyYW0gPSB0aGlzLnByb2dyYW07XG4gICAgICBjb25zdCB0eXBlQ2hlY2tlciA9IHByb2dyYW0uZ2V0VHlwZUNoZWNrZXIoKTtcbiAgICAgIGNvbnN0IHNvdXJjZUZpbGUgPSB0aGlzLmNsYXNzRGVjbE5vZGUuZ2V0U291cmNlRmlsZSgpO1xuICAgICAgdGhpcy5xdWVyeUNhY2hlID0gZ2V0U3ltYm9sUXVlcnkocHJvZ3JhbSwgdHlwZUNoZWNrZXIsIHNvdXJjZUZpbGUsICgpID0+IHtcbiAgICAgICAgLy8gQ29tcHV0aW5nIHRoZSBhc3QgaXMgcmVsYXRpdmVseSBleHBlbnNpdmUuIERvIGl0IG9ubHkgd2hlbiBhYnNvbHV0ZWx5XG4gICAgICAgIC8vIG5lY2Vzc2FyeS5cbiAgICAgICAgLy8gVE9ETzogVGhlcmUgaXMgY2lyY3VsYXIgZGVwZW5kZW5jeSBoZXJlIGJldHdlZW4gVGVtcGxhdGVTb3VyY2UgYW5kXG4gICAgICAgIC8vIFR5cGVTY3JpcHRIb3N0LiBDb25zaWRlciByZWZhY3RvcmluZyB0aGUgY29kZSB0byBicmVhayB0aGlzIGN5Y2xlLlxuICAgICAgICBjb25zdCBhc3QgPSB0aGlzLmhvc3QuZ2V0VGVtcGxhdGVBc3QodGhpcyk7XG4gICAgICAgIGNvbnN0IHBpcGVzID0gaXNBc3RSZXN1bHQoYXN0KSA/IGFzdC5waXBlcyA6IFtdO1xuICAgICAgICByZXR1cm4gZ2V0UGlwZXNUYWJsZShzb3VyY2VGaWxlLCBwcm9ncmFtLCB0eXBlQ2hlY2tlciwgcGlwZXMpO1xuICAgICAgfSk7XG4gICAgfVxuICAgIHJldHVybiB0aGlzLnF1ZXJ5Q2FjaGU7XG4gIH1cbn1cblxuLyoqXG4gKiBBbiBJbmxpbmVUZW1wbGF0ZSByZXByZXNlbnRzIHRlbXBsYXRlIGRlZmluZWQgaW4gYSBUUyBmaWxlIHRocm91Z2ggdGhlXG4gKiBgdGVtcGxhdGVgIGF0dHJpYnV0ZSBpbiB0aGUgZGVjb3JhdG9yLlxuICovXG5leHBvcnQgY2xhc3MgSW5saW5lVGVtcGxhdGUgZXh0ZW5kcyBCYXNlVGVtcGxhdGUge1xuICBwdWJsaWMgcmVhZG9ubHkgZmlsZU5hbWU6IHN0cmluZztcbiAgcHVibGljIHJlYWRvbmx5IHNvdXJjZTogc3RyaW5nO1xuICBwdWJsaWMgcmVhZG9ubHkgc3BhbjogbmcuU3BhbjtcblxuICBjb25zdHJ1Y3RvcihcbiAgICAgIHRlbXBsYXRlTm9kZTogdHMuU3RyaW5nTGl0ZXJhbExpa2UsIGNsYXNzRGVjbE5vZGU6IHRzLkNsYXNzRGVjbGFyYXRpb24sXG4gICAgICBjbGFzc1N5bWJvbDogbmcuU3RhdGljU3ltYm9sLCBob3N0OiBUeXBlU2NyaXB0U2VydmljZUhvc3QpIHtcbiAgICBzdXBlcihob3N0LCBjbGFzc0RlY2xOb2RlLCBjbGFzc1N5bWJvbCk7XG4gICAgY29uc3Qgc291cmNlRmlsZSA9IHRlbXBsYXRlTm9kZS5nZXRTb3VyY2VGaWxlKCk7XG4gICAgaWYgKHNvdXJjZUZpbGUgIT09IGNsYXNzRGVjbE5vZGUuZ2V0U291cmNlRmlsZSgpKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoYElubGluZSB0ZW1wbGF0ZSBhbmQgY29tcG9uZW50IGNsYXNzIHNob3VsZCBiZWxvbmcgdG8gdGhlIHNhbWUgc291cmNlIGZpbGVgKTtcbiAgICB9XG4gICAgdGhpcy5maWxlTmFtZSA9IHNvdXJjZUZpbGUuZmlsZU5hbWU7XG4gICAgdGhpcy5zb3VyY2UgPSB0ZW1wbGF0ZU5vZGUudGV4dDtcbiAgICB0aGlzLnNwYW4gPSB7XG4gICAgICAvLyBUUyBzdHJpbmcgbGl0ZXJhbCBpbmNsdWRlcyBzdXJyb3VuZGluZyBxdW90ZXMgaW4gdGhlIHN0YXJ0L2VuZCBvZmZzZXRzLlxuICAgICAgc3RhcnQ6IHRlbXBsYXRlTm9kZS5nZXRTdGFydCgpICsgMSxcbiAgICAgIGVuZDogdGVtcGxhdGVOb2RlLmdldEVuZCgpIC0gMSxcbiAgICB9O1xuICB9XG59XG5cbi8qKlxuICogQW4gRXh0ZXJuYWxUZW1wbGF0ZSByZXByZXNlbnRzIHRlbXBsYXRlIGRlZmluZWQgaW4gYW4gZXh0ZXJuYWwgKG1vc3QgbGlrZWx5XG4gKiBIVE1MLCBidXQgbm90IG5lY2Vzc2FyaWx5KSBmaWxlIHRocm91Z2ggdGhlIGB0ZW1wbGF0ZVVybGAgYXR0cmlidXRlIGluIHRoZVxuICogZGVjb3JhdG9yLlxuICogTm90ZSB0aGF0IHRoZXJlIGlzIG5vIHRzLk5vZGUgYXNzb2NpYXRlZCB3aXRoIHRoZSB0ZW1wbGF0ZSBiZWNhdXNlIGl0J3Mgbm90XG4gKiBhIFRTIGZpbGUuXG4gKi9cbmV4cG9ydCBjbGFzcyBFeHRlcm5hbFRlbXBsYXRlIGV4dGVuZHMgQmFzZVRlbXBsYXRlIHtcbiAgcHVibGljIHJlYWRvbmx5IHNwYW46IG5nLlNwYW47XG5cbiAgY29uc3RydWN0b3IoXG4gICAgICBwdWJsaWMgcmVhZG9ubHkgc291cmNlOiBzdHJpbmcsIHB1YmxpYyByZWFkb25seSBmaWxlTmFtZTogc3RyaW5nLFxuICAgICAgY2xhc3NEZWNsTm9kZTogdHMuQ2xhc3NEZWNsYXJhdGlvbiwgY2xhc3NTeW1ib2w6IG5nLlN0YXRpY1N5bWJvbCxcbiAgICAgIGhvc3Q6IFR5cGVTY3JpcHRTZXJ2aWNlSG9zdCkge1xuICAgIHN1cGVyKGhvc3QsIGNsYXNzRGVjbE5vZGUsIGNsYXNzU3ltYm9sKTtcbiAgICB0aGlzLnNwYW4gPSB7XG4gICAgICBzdGFydDogMCxcbiAgICAgIGVuZDogc291cmNlLmxlbmd0aCxcbiAgICB9O1xuICB9XG59XG5cbi8qKlxuICogUmV0dXJucyBhIHByb3BlcnR5IGFzc2lnbm1lbnQgZnJvbSB0aGUgYXNzaWdubWVudCB2YWx1ZSwgb3IgYHVuZGVmaW5lZGAgaWYgdGhlcmUgaXMgbm9cbiAqIGFzc2lnbm1lbnQuXG4gKi9cbmV4cG9ydCBmdW5jdGlvbiBnZXRQcm9wZXJ0eUFzc2lnbm1lbnRGcm9tVmFsdWUodmFsdWU6IHRzLk5vZGUpOiB0cy5Qcm9wZXJ0eUFzc2lnbm1lbnR8dW5kZWZpbmVkIHtcbiAgaWYgKCF2YWx1ZS5wYXJlbnQgfHwgIXRzLmlzUHJvcGVydHlBc3NpZ25tZW50KHZhbHVlLnBhcmVudCkpIHtcbiAgICByZXR1cm47XG4gIH1cbiAgcmV0dXJuIHZhbHVlLnBhcmVudDtcbn1cblxuLyoqXG4gKiBHaXZlbiBhIGRlY29yYXRvciBwcm9wZXJ0eSBhc3NpZ25tZW50LCByZXR1cm4gdGhlIENsYXNzRGVjbGFyYXRpb24gbm9kZSB0aGF0IGNvcnJlc3BvbmRzIHRvIHRoZVxuICogZGlyZWN0aXZlIGNsYXNzIHRoZSBwcm9wZXJ0eSBhcHBsaWVzIHRvLlxuICogSWYgdGhlIHByb3BlcnR5IGFzc2lnbm1lbnQgaXMgbm90IG9uIGEgY2xhc3MgZGVjb3JhdG9yLCBubyBkZWNsYXJhdGlvbiBpcyByZXR1cm5lZC5cbiAqXG4gKiBGb3IgZXhhbXBsZSxcbiAqXG4gKiBAQ29tcG9uZW50KHtcbiAqICAgdGVtcGxhdGU6ICc8ZGl2PjwvZGl2PidcbiAqICAgXl5eXl5eXl5eXl5eXl5eXl5eXl5eXl4tLS0tIHByb3BlcnR5IGFzc2lnbm1lbnRcbiAqIH0pXG4gKiBjbGFzcyBBcHBDb21wb25lbnQge31cbiAqICAgICAgICAgICBeLS0tLSBjbGFzcyBkZWNsYXJhdGlvbiBub2RlXG4gKlxuICogQHBhcmFtIHByb3BBc2duIHByb3BlcnR5IGFzc2lnbm1lbnRcbiAqL1xuZXhwb3J0IGZ1bmN0aW9uIGdldENsYXNzRGVjbEZyb21EZWNvcmF0b3JQcm9wKHByb3BBc2duTm9kZTogdHMuUHJvcGVydHlBc3NpZ25tZW50KTpcbiAgICB0cy5DbGFzc0RlY2xhcmF0aW9ufHVuZGVmaW5lZCB7XG4gIGlmICghcHJvcEFzZ25Ob2RlLnBhcmVudCB8fCAhdHMuaXNPYmplY3RMaXRlcmFsRXhwcmVzc2lvbihwcm9wQXNnbk5vZGUucGFyZW50KSkge1xuICAgIHJldHVybjtcbiAgfVxuICBjb25zdCBvYmpMaXRFeHByTm9kZSA9IHByb3BBc2duTm9kZS5wYXJlbnQ7XG4gIGlmICghb2JqTGl0RXhwck5vZGUucGFyZW50IHx8ICF0cy5pc0NhbGxFeHByZXNzaW9uKG9iakxpdEV4cHJOb2RlLnBhcmVudCkpIHtcbiAgICByZXR1cm47XG4gIH1cbiAgY29uc3QgY2FsbEV4cHJOb2RlID0gb2JqTGl0RXhwck5vZGUucGFyZW50O1xuICBpZiAoIWNhbGxFeHByTm9kZS5wYXJlbnQgfHwgIXRzLmlzRGVjb3JhdG9yKGNhbGxFeHByTm9kZS5wYXJlbnQpKSB7XG4gICAgcmV0dXJuO1xuICB9XG4gIGNvbnN0IGRlY29yYXRvciA9IGNhbGxFeHByTm9kZS5wYXJlbnQ7XG4gIGlmICghZGVjb3JhdG9yLnBhcmVudCB8fCAhdHMuaXNDbGFzc0RlY2xhcmF0aW9uKGRlY29yYXRvci5wYXJlbnQpKSB7XG4gICAgcmV0dXJuO1xuICB9XG4gIGNvbnN0IGNsYXNzRGVjbE5vZGUgPSBkZWNvcmF0b3IucGFyZW50O1xuICByZXR1cm4gY2xhc3NEZWNsTm9kZTtcbn1cblxuLyoqXG4gKiBEZXRlcm1pbmVzIGlmIGEgcHJvcGVydHkgYXNzaWdubWVudCBpcyBvbiBhIGNsYXNzIGRlY29yYXRvci5cbiAqIFNlZSBgZ2V0Q2xhc3NEZWNsRnJvbURlY29yYXRvclByb3BlcnR5YCwgd2hpY2ggZ2V0cyB0aGUgY2xhc3MgdGhlIGRlY29yYXRvciBpcyBhcHBsaWVkIHRvLCBmb3JcbiAqIG1vcmUgZGV0YWlscy5cbiAqXG4gKiBAcGFyYW0gcHJvcCBwcm9wZXJ0eSBhc3NpZ25tZW50XG4gKi9cbmV4cG9ydCBmdW5jdGlvbiBpc0NsYXNzRGVjb3JhdG9yUHJvcGVydHkocHJvcEFzZ246IHRzLlByb3BlcnR5QXNzaWdubWVudCk6IGJvb2xlYW4ge1xuICByZXR1cm4gISFnZXRDbGFzc0RlY2xGcm9tRGVjb3JhdG9yUHJvcChwcm9wQXNnbik7XG59XG4iXX0=