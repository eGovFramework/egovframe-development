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
        define("@angular/language-service/src/utils", ["require", "exports", "tslib", "@angular/compiler", "typescript"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var tslib_1 = require("tslib");
    var compiler_1 = require("@angular/compiler");
    var ts = require("typescript");
    function isParseSourceSpan(value) {
        return value && !!value.start;
    }
    exports.isParseSourceSpan = isParseSourceSpan;
    function spanOf(span) {
        if (!span)
            return undefined;
        if (isParseSourceSpan(span)) {
            return { start: span.start.offset, end: span.end.offset };
        }
        else {
            if (span.endSourceSpan) {
                return { start: span.sourceSpan.start.offset, end: span.endSourceSpan.end.offset };
            }
            else if (span.children && span.children.length) {
                return {
                    start: span.sourceSpan.start.offset,
                    end: spanOf(span.children[span.children.length - 1]).end
                };
            }
            return { start: span.sourceSpan.start.offset, end: span.sourceSpan.end.offset };
        }
    }
    exports.spanOf = spanOf;
    function inSpan(position, span, exclusive) {
        return span != null && (exclusive ? position >= span.start && position < span.end :
            position >= span.start && position <= span.end);
    }
    exports.inSpan = inSpan;
    function offsetSpan(span, amount) {
        return { start: span.start + amount, end: span.end + amount };
    }
    exports.offsetSpan = offsetSpan;
    function isNarrower(spanA, spanB) {
        return spanA.start >= spanB.start && spanA.end <= spanB.end;
    }
    exports.isNarrower = isNarrower;
    function hasTemplateReference(type) {
        var e_1, _a;
        if (type.diDeps) {
            try {
                for (var _b = tslib_1.__values(type.diDeps), _c = _b.next(); !_c.done; _c = _b.next()) {
                    var diDep = _c.value;
                    if (diDep.token && diDep.token.identifier &&
                        compiler_1.identifierName(diDep.token.identifier) === 'TemplateRef')
                        return true;
                }
            }
            catch (e_1_1) { e_1 = { error: e_1_1 }; }
            finally {
                try {
                    if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
                }
                finally { if (e_1) throw e_1.error; }
            }
        }
        return false;
    }
    exports.hasTemplateReference = hasTemplateReference;
    function getSelectors(info) {
        var e_2, _a, e_3, _b;
        var map = new Map();
        var results = [];
        try {
            for (var _c = tslib_1.__values(info.directives), _d = _c.next(); !_d.done; _d = _c.next()) {
                var directive = _d.value;
                var selectors = compiler_1.CssSelector.parse(directive.selector);
                try {
                    for (var selectors_1 = (e_3 = void 0, tslib_1.__values(selectors)), selectors_1_1 = selectors_1.next(); !selectors_1_1.done; selectors_1_1 = selectors_1.next()) {
                        var selector = selectors_1_1.value;
                        results.push(selector);
                        map.set(selector, directive);
                    }
                }
                catch (e_3_1) { e_3 = { error: e_3_1 }; }
                finally {
                    try {
                        if (selectors_1_1 && !selectors_1_1.done && (_b = selectors_1.return)) _b.call(selectors_1);
                    }
                    finally { if (e_3) throw e_3.error; }
                }
            }
        }
        catch (e_2_1) { e_2 = { error: e_2_1 }; }
        finally {
            try {
                if (_d && !_d.done && (_a = _c.return)) _a.call(_c);
            }
            finally { if (e_2) throw e_2.error; }
        }
        return { selectors: results, map: map };
    }
    exports.getSelectors = getSelectors;
    function isTypescriptVersion(low, high) {
        var version = ts.version;
        if (version.substring(0, low.length) < low)
            return false;
        if (high && (version.substring(0, high.length) > high))
            return false;
        return true;
    }
    exports.isTypescriptVersion = isTypescriptVersion;
    function diagnosticInfoFromTemplateInfo(info) {
        return {
            fileName: info.template.fileName,
            offset: info.template.span.start,
            query: info.template.query,
            members: info.template.members,
            htmlAst: info.htmlAst,
            templateAst: info.templateAst
        };
    }
    exports.diagnosticInfoFromTemplateInfo = diagnosticInfoFromTemplateInfo;
    function findTemplateAstAt(ast, position, allowWidening) {
        if (allowWidening === void 0) { allowWidening = false; }
        var path = [];
        var visitor = new /** @class */ (function (_super) {
            tslib_1.__extends(class_1, _super);
            function class_1() {
                return _super !== null && _super.apply(this, arguments) || this;
            }
            class_1.prototype.visit = function (ast, context) {
                var span = spanOf(ast);
                if (inSpan(position, span)) {
                    var len = path.length;
                    if (!len || allowWidening || isNarrower(span, spanOf(path[len - 1]))) {
                        path.push(ast);
                    }
                }
                else {
                    // Returning a value here will result in the children being skipped.
                    return true;
                }
            };
            class_1.prototype.visitEmbeddedTemplate = function (ast, context) {
                return this.visitChildren(context, function (visit) {
                    // Ignore reference, variable and providers
                    visit(ast.attrs);
                    visit(ast.directives);
                    visit(ast.children);
                });
            };
            class_1.prototype.visitElement = function (ast, context) {
                return this.visitChildren(context, function (visit) {
                    // Ingnore providers
                    visit(ast.attrs);
                    visit(ast.inputs);
                    visit(ast.outputs);
                    visit(ast.references);
                    visit(ast.directives);
                    visit(ast.children);
                });
            };
            class_1.prototype.visitDirective = function (ast, context) {
                // Ignore the host properties of a directive
                var result = this.visitChildren(context, function (visit) { visit(ast.inputs); });
                // We never care about the diretive itself, just its inputs.
                if (path[path.length - 1] === ast) {
                    path.pop();
                }
                return result;
            };
            return class_1;
        }(compiler_1.RecursiveTemplateAstVisitor));
        compiler_1.templateVisitAll(visitor, ast);
        return new compiler_1.AstPath(path, position);
    }
    exports.findTemplateAstAt = findTemplateAstAt;
    /**
     * Return the node that most tightly encompass the specified `position`.
     * @param node
     * @param position
     */
    function findTightestNode(node, position) {
        if (node.getStart() <= position && position < node.getEnd()) {
            return node.forEachChild(function (c) { return findTightestNode(c, position); }) || node;
        }
    }
    exports.findTightestNode = findTightestNode;
    /**
     * Return metadata about `node` if it looks like an Angular directive class.
     * In this case, potential matches are `@NgModule`, `@Component`, `@Directive`,
     * `@Pipe`, etc.
     * These class declarations all share some common attributes, namely their
     * decorator takes exactly one parameter and the parameter must be an object
     * literal.
     *
     * For example,
     *     v---------- `decoratorId`
     * @NgModule({
     *   declarations: [],
     * })
     * class AppModule {}
     *          ^----- `classDecl`
     *
     * @param node Potential node that represents an Angular directive.
     */
    function getDirectiveClassLike(node) {
        var e_4, _a;
        if (!ts.isClassDeclaration(node) || !node.name || !node.decorators) {
            return;
        }
        try {
            for (var _b = tslib_1.__values(node.decorators), _c = _b.next(); !_c.done; _c = _b.next()) {
                var d = _c.value;
                var expr = d.expression;
                if (!ts.isCallExpression(expr) || expr.arguments.length !== 1 ||
                    !ts.isIdentifier(expr.expression)) {
                    continue;
                }
                var arg = expr.arguments[0];
                if (ts.isObjectLiteralExpression(arg)) {
                    return {
                        decoratorId: expr.expression,
                        classDecl: node,
                    };
                }
            }
        }
        catch (e_4_1) { e_4 = { error: e_4_1 }; }
        finally {
            try {
                if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
            }
            finally { if (e_4) throw e_4.error; }
        }
    }
    exports.getDirectiveClassLike = getDirectiveClassLike;
    /**
     * Finds the value of a property assignment that is nested in a TypeScript node and is of a certain
     * type T.
     *
     * @param startNode node to start searching for nested property assignment from
     * @param propName property assignment name
     * @param predicate function to verify that a node is of type T.
     * @return node property assignment value of type T, or undefined if none is found
     */
    function findPropertyValueOfType(startNode, propName, predicate) {
        if (ts.isPropertyAssignment(startNode) && startNode.name.getText() === propName) {
            var initializer = startNode.initializer;
            if (predicate(initializer))
                return initializer;
        }
        return startNode.forEachChild(function (c) { return findPropertyValueOfType(c, propName, predicate); });
    }
    exports.findPropertyValueOfType = findPropertyValueOfType;
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidXRpbHMuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi8uLi8uLi9wYWNrYWdlcy9sYW5ndWFnZS1zZXJ2aWNlL3NyYy91dGlscy50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTs7Ozs7O0dBTUc7Ozs7Ozs7Ozs7Ozs7SUFFSCw4Q0FBa1E7SUFDbFEsK0JBQWlDO0lBWWpDLFNBQWdCLGlCQUFpQixDQUFDLEtBQVU7UUFDMUMsT0FBTyxLQUFLLElBQUksQ0FBQyxDQUFDLEtBQUssQ0FBQyxLQUFLLENBQUM7SUFDaEMsQ0FBQztJQUZELDhDQUVDO0lBS0QsU0FBZ0IsTUFBTSxDQUFDLElBQW1DO1FBQ3hELElBQUksQ0FBQyxJQUFJO1lBQUUsT0FBTyxTQUFTLENBQUM7UUFDNUIsSUFBSSxpQkFBaUIsQ0FBQyxJQUFJLENBQUMsRUFBRTtZQUMzQixPQUFPLEVBQUMsS0FBSyxFQUFFLElBQUksQ0FBQyxLQUFLLENBQUMsTUFBTSxFQUFFLEdBQUcsRUFBRSxJQUFJLENBQUMsR0FBRyxDQUFDLE1BQU0sRUFBQyxDQUFDO1NBQ3pEO2FBQU07WUFDTCxJQUFJLElBQUksQ0FBQyxhQUFhLEVBQUU7Z0JBQ3RCLE9BQU8sRUFBQyxLQUFLLEVBQUUsSUFBSSxDQUFDLFVBQVUsQ0FBQyxLQUFLLENBQUMsTUFBTSxFQUFFLEdBQUcsRUFBRSxJQUFJLENBQUMsYUFBYSxDQUFDLEdBQUcsQ0FBQyxNQUFNLEVBQUMsQ0FBQzthQUNsRjtpQkFBTSxJQUFJLElBQUksQ0FBQyxRQUFRLElBQUksSUFBSSxDQUFDLFFBQVEsQ0FBQyxNQUFNLEVBQUU7Z0JBQ2hELE9BQU87b0JBQ0wsS0FBSyxFQUFFLElBQUksQ0FBQyxVQUFVLENBQUMsS0FBSyxDQUFDLE1BQU07b0JBQ25DLEdBQUcsRUFBRSxNQUFNLENBQUMsSUFBSSxDQUFDLFFBQVEsQ0FBQyxJQUFJLENBQUMsUUFBUSxDQUFDLE1BQU0sR0FBRyxDQUFDLENBQUMsQ0FBRyxDQUFDLEdBQUc7aUJB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QVUsQ0FBQztnQkFDMUIsSUFBSSxDQUFDLEVBQUUsQ0FBQyxnQkFBZ0IsQ0FBQyxJQUFJLENBQUMsSUFBSSxJQUFJLENBQUMsU0FBUyxDQUFDLE1BQU0sS0FBSyxDQUFDO29CQUN6RCxDQUFDLEVBQUUsQ0FBQyxZQUFZLENBQUMsSUFBSSxDQUFDLFVBQVUsQ0FBQyxFQUFFO29CQUNyQyxTQUFTO2lCQUNWO2dCQUNELElBQU0sR0FBRyxHQUFHLElBQUksQ0FBQyxTQUFTLENBQUMsQ0FBQyxDQUFDLENBQUM7Z0JBQzlCLElBQUksRUFBRSxDQUFDLHlCQUF5QixDQUFDLEdBQUcsQ0FBQyxFQUFFO29CQUNyQyxPQUFPO3dCQUNMLFdBQVcsRUFBRSxJQUFJLENBQUMsVUFBVTt3QkFDNUIsU0FBUyxFQUFFLElBQUk7cUJBQ2hCLENBQUM7aUJBQ0g7YUFDRjs7Ozs7Ozs7O0lBQ0gsQ0FBQztJQWxCRCxzREFrQkM7SUFFRDs7Ozs7Ozs7T0FRRztJQUNILFNBQWdCLHVCQUF1QixDQUNuQyxTQUFrQixFQUFFLFFBQWdCLEVBQUUsU0FBdUM7UUFDL0UsSUFBSSxFQUFFLENBQUMsb0JBQW9CLENBQUMsU0FBUyxDQUFDLElBQUksU0FBUyxDQUFDLElBQUksQ0FBQyxPQUFPLEVBQUUsS0FBSyxRQUFRLEVBQUU7WUFDeEUsSUFBQSxtQ0FBVyxDQUFjO1lBQ2hDLElBQUksU0FBUyxDQUFDLFdBQVcsQ0FBQztnQkFBRSxPQUFPLFdBQVcsQ0FBQztTQUNoRDtRQUNELE9BQU8sU0FBUyxDQUFDLFlBQVksQ0FBQyxVQUFBLENBQUMsSUFBSSxPQUFBLHVCQUF1QixDQUFDLENBQUMsRUFBRSxRQUFRLEVBQUUsU0FBUyxDQUFDLEVBQS9DLENBQStDLENBQUMsQ0FBQztJQUN0RixDQUFDO0lBUEQsMERBT0MiLCJzb3VyY2VzQ29udGVudCI6WyIvKipcbiAqIEBsaWNlbnNlXG4gKiBDb3B5cmlnaHQgR29vZ2xlIEluYy4gQWxsIFJpZ2h0cyBSZXNlcnZlZC5cbiAqXG4gKiBVc2Ugb2YgdGhpcyBzb3VyY2UgY29kZSBpcyBnb3Zlcm5lZCBieSBhbiBNSVQtc3R5bGUgbGljZW5zZSB0aGF0IGNhbiBiZVxuICogZm91bmQgaW4gdGhlIExJQ0VOU0UgZmlsZSBhdCBodHRwczovL2FuZ3VsYXIuaW8vbGljZW5zZVxuICovXG5cbmltcG9ydCB7QXN0UGF0aCwgQ29tcGlsZURpcmVjdGl2ZVN1bW1hcnksIENvbXBpbGVUeXBlTWV0YWRhdGEsIENzc1NlbGVjdG9yLCBEaXJlY3RpdmVBc3QsIEVsZW1lbnRBc3QsIEVtYmVkZGVkVGVtcGxhdGVBc3QsIFBhcnNlU291cmNlU3BhbiwgUmVjdXJzaXZlVGVtcGxhdGVBc3RWaXNpdG9yLCBUZW1wbGF0ZUFzdCwgVGVtcGxhdGVBc3RQYXRoLCBpZGVudGlmaWVyTmFtZSwgdGVtcGxhdGVWaXNpdEFsbH0gZnJvbSAnQGFuZ3VsYXIvY29tcGlsZXInO1xuaW1wb3J0ICogYXMgdHMgZnJvbSAndHlwZXNjcmlwdCc7XG5cbmltcG9ydCB7QXN0UmVzdWx0LCBTZWxlY3RvckluZm99IGZyb20gJy4vY29tbW9uJztcbmltcG9ydCB7RGlhZ25vc3RpY1RlbXBsYXRlSW5mb30gZnJvbSAnLi9leHByZXNzaW9uX2RpYWdub3N0aWNzJztcbmltcG9ydCB7U3Bhbn0gZnJvbSAnLi90eXBlcyc7XG5cbmV4cG9ydCBpbnRlcmZhY2UgU3BhbkhvbGRlciB7XG4gIHNvdXJjZVNwYW46IFBhcnNlU291cmNlU3BhbjtcbiAgZW5kU291cmNlU3Bhbj86IFBhcnNlU291cmNlU3BhbnxudWxsO1xuICBjaGlsZHJlbj86IFNwYW5Ib2xkZXJbXTtcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIGlzUGFyc2VTb3VyY2VTcGFuKHZhbHVlOiBhbnkpOiB2YWx1ZSBpcyBQYXJzZVNvdXJjZVNwYW4ge1xuICByZXR1cm4gdmFsdWUgJiYgISF2YWx1ZS5zdGFydDtcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIHNwYW5PZihzcGFuOiBTcGFuSG9sZGVyKTogU3BhbjtcbmV4cG9ydCBmdW5jdGlvbiBzcGFuT2Yoc3BhbjogUGFyc2VTb3VyY2VTcGFuKTogU3BhbjtcbmV4cG9ydCBmdW5jdGlvbiBzcGFuT2Yoc3BhbjogU3BhbkhvbGRlciB8IFBhcnNlU291cmNlU3BhbiB8IHVuZGVmaW5lZCk6IFNwYW58dW5kZWZpbmVkO1xuZXhwb3J0IGZ1bmN0aW9uIHNwYW5PZihzcGFuPzogU3BhbkhvbGRlciB8IFBhcnNlU291cmNlU3Bhbik6IFNwYW58dW5kZWZpbmVkIHtcbiAgaWYgKCFzcGFuKSByZXR1cm4gdW5kZWZpbmVkO1xuICBpZiAoaXNQYXJzZVNvdXJjZVNwYW4oc3BhbikpIHtcbiAgICByZXR1cm4ge3N0YXJ0OiBzcGFuLnN0YXJ0Lm9mZnNldCwgZW5kOiBzcGFuLmVuZC5vZmZzZXR9O1xuICB9IGVsc2Uge1xuICAgIGlmIChzcGFuLmVuZFNvdXJjZVNwYW4pIHtcbiAgICAgIHJldHVybiB7c3RhcnQ6IHNwYW4uc291cmNlU3Bhbi5zdGFydC5vZmZzZXQsIGVuZDogc3Bhbi5lbmRTb3VyY2VTcGFuLmVuZC5vZmZzZXR9O1xuICAgIH0gZWxzZSBpZiAoc3Bhbi5jaGlsZHJlbiAmJiBzcGFuLmNoaWxkcmVuLmxlbmd0aCkge1xuICAgICAgcmV0dXJuIHtcbiAgICAgICAgc3RhcnQ6IHNwYW4uc291cmNlU3Bhbi5zdGFydC5vZmZzZXQsXG4gICAgICAgIGVuZDogc3Bhbk9mKHNwYW4uY2hpbGRyZW5bc3Bhbi5jaGlsZHJlbi5sZW5ndGggLSAxXSkgIS5lbmRcbiAgICAgIH07XG4gICAgfVxuICAgIHJldHVybiB7c3RhcnQ6IHNwYW4uc291cmNlU3Bhbi5zdGFydC5vZmZzZXQsIGVuZDogc3Bhbi5zb3VyY2VTcGFuLmVuZC5vZmZzZXR9O1xuICB9XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBpblNwYW4ocG9zaXRpb246IG51bWJlciwgc3Bhbj86IFNwYW4sIGV4Y2x1c2l2ZT86IGJvb2xlYW4pOiBib29sZWFuIHtcbiAgcmV0dXJuIHNwYW4gIT0gbnVsbCAmJiAoZXhjbHVzaXZlID8gcG9zaXRpb24gPj0gc3Bhbi5zdGFydCAmJiBwb3NpdGlvbiA8IHNwYW4uZW5kIDpcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgcG9zaXRpb24gPj0gc3Bhbi5zdGFydCAmJiBwb3NpdGlvbiA8PSBzcGFuLmVuZCk7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBvZmZzZXRTcGFuKHNwYW46IFNwYW4sIGFtb3VudDogbnVtYmVyKTogU3BhbiB7XG4gIHJldHVybiB7c3RhcnQ6IHNwYW4uc3RhcnQgKyBhbW91bnQsIGVuZDogc3Bhbi5lbmQgKyBhbW91bnR9O1xufVxuXG5leHBvcnQgZnVuY3Rpb24gaXNOYXJyb3dlcihzcGFuQTogU3Bhbiwgc3BhbkI6IFNwYW4pOiBib29sZWFuIHtcbiAgcmV0dXJuIHNwYW5BLnN0YXJ0ID49IHNwYW5CLnN0YXJ0ICYmIHNwYW5BLmVuZCA8PSBzcGFuQi5lbmQ7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBoYXNUZW1wbGF0ZVJlZmVyZW5jZSh0eXBlOiBDb21waWxlVHlwZU1ldGFkYXRhKTogYm9vbGVhbiB7XG4gIGlmICh0eXBlLmRpRGVwcykge1xuICAgIGZvciAobGV0IGRpRGVwIG9mIHR5cGUuZGlEZXBzKSB7XG4gICAgICBpZiAoZGlEZXAudG9rZW4gJiYgZGlEZXAudG9rZW4uaWRlbnRpZmllciAmJlxuICAgICAgICAgIGlkZW50aWZpZXJOYW1lKGRpRGVwLnRva2VuICEuaWRlbnRpZmllciAhKSA9PT0gJ1RlbXBsYXRlUmVmJylcbiAgICAgICAgcmV0dXJuIHRydWU7XG4gICAgfVxuICB9XG4gIHJldHVybiBmYWxzZTtcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIGdldFNlbGVjdG9ycyhpbmZvOiBBc3RSZXN1bHQpOiBTZWxlY3RvckluZm8ge1xuICBjb25zdCBtYXAgPSBuZXcgTWFwPENzc1NlbGVjdG9yLCBDb21waWxlRGlyZWN0aXZlU3VtbWFyeT4oKTtcbiAgY29uc3QgcmVzdWx0czogQ3NzU2VsZWN0b3JbXSA9IFtdO1xuICBmb3IgKGNvbnN0IGRpcmVjdGl2ZSBvZiBpbmZvLmRpcmVjdGl2ZXMpIHtcbiAgICBjb25zdCBzZWxlY3RvcnM6IENzc1NlbGVjdG9yW10gPSBDc3NTZWxlY3Rvci5wYXJzZShkaXJlY3RpdmUuc2VsZWN0b3IgISk7XG4gICAgZm9yIChjb25zdCBzZWxlY3RvciBvZiBzZWxlY3RvcnMpIHtcbiAgICAgIHJlc3VsdHMucHVzaChzZWxlY3Rvcik7XG4gICAgICBtYXAuc2V0KHNlbGVjdG9yLCBkaXJlY3RpdmUpO1xuICAgIH1cbiAgfVxuICByZXR1cm4ge3NlbGVjdG9yczogcmVzdWx0cywgbWFwfTtcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIGlzVHlwZXNjcmlwdFZlcnNpb24obG93OiBzdHJpbmcsIGhpZ2g/OiBzdHJpbmcpIHtcbiAgY29uc3QgdmVyc2lvbiA9IHRzLnZlcnNpb247XG5cbiAgaWYgKHZlcnNpb24uc3Vic3RyaW5nKDAsIGxvdy5sZW5ndGgpIDwgbG93KSByZXR1cm4gZmFsc2U7XG5cbiAgaWYgKGhpZ2ggJiYgKHZlcnNpb24uc3Vic3RyaW5nKDAsIGhpZ2gubGVuZ3RoKSA+IGhpZ2gpKSByZXR1cm4gZmFsc2U7XG5cbiAgcmV0dXJuIHRydWU7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBkaWFnbm9zdGljSW5mb0Zyb21UZW1wbGF0ZUluZm8oaW5mbzogQXN0UmVzdWx0KTogRGlhZ25vc3RpY1RlbXBsYXRlSW5mbyB7XG4gIHJldHVybiB7XG4gICAgZmlsZU5hbWU6IGluZm8udGVtcGxhdGUuZmlsZU5hbWUsXG4gICAgb2Zmc2V0OiBpbmZvLnRlbXBsYXRlLnNwYW4uc3RhcnQsXG4gICAgcXVlcnk6IGluZm8udGVtcGxhdGUucXVlcnksXG4gICAgbWVtYmVyczogaW5mby50ZW1wbGF0ZS5tZW1iZXJzLFxuICAgIGh0bWxBc3Q6IGluZm8uaHRtbEFzdCxcbiAgICB0ZW1wbGF0ZUFzdDogaW5mby50ZW1wbGF0ZUFzdFxuICB9O1xufVxuXG5leHBvcnQgZnVuY3Rpb24gZmluZFRlbXBsYXRlQXN0QXQoXG4gICAgYXN0OiBUZW1wbGF0ZUFzdFtdLCBwb3NpdGlvbjogbnVtYmVyLCBhbGxvd1dpZGVuaW5nOiBib29sZWFuID0gZmFsc2UpOiBUZW1wbGF0ZUFzdFBhdGgge1xuICBjb25zdCBwYXRoOiBUZW1wbGF0ZUFzdFtdID0gW107XG4gIGNvbnN0IHZpc2l0b3IgPSBuZXcgY2xhc3MgZXh0ZW5kcyBSZWN1cnNpdmVUZW1wbGF0ZUFzdFZpc2l0b3Ige1xuICAgIHZpc2l0KGFzdDogVGVtcGxhdGVBc3QsIGNvbnRleHQ6IGFueSk6IGFueSB7XG4gICAgICBsZXQgc3BhbiA9IHNwYW5PZihhc3QpO1xuICAgICAgaWYgKGluU3Bhbihwb3NpdGlvbiwgc3BhbikpIHtcbiAgICAgICAgY29uc3QgbGVuID0gcGF0aC5sZW5ndGg7XG4gICAgICAgIGlmICghbGVuIHx8IGFsbG93V2lkZW5pbmcgfHwgaXNOYXJyb3dlcihzcGFuLCBzcGFuT2YocGF0aFtsZW4gLSAxXSkpKSB7XG4gICAgICAgICAgcGF0aC5wdXNoKGFzdCk7XG4gICAgICAgIH1cbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIC8vIFJldHVybmluZyBhIHZhbHVlIGhlcmUgd2lsbCByZXN1bHQgaW4gdGhlIGNoaWxkcmVuIGJlaW5nIHNraXBwZWQuXG4gICAgICAgIHJldHVybiB0cnVlO1xuICAgICAgfVxuICAgIH1cblxuICAgIHZpc2l0RW1iZWRkZWRUZW1wbGF0ZShhc3Q6IEVtYmVkZGVkVGVtcGxhdGVBc3QsIGNvbnRleHQ6IGFueSk6IGFueSB7XG4gICAgICByZXR1cm4gdGhpcy52aXNpdENoaWxkcmVuKGNvbnRleHQsIHZpc2l0ID0+IHtcbiAgICAgICAgLy8gSWdub3JlIHJlZmVyZW5jZSwgdmFyaWFibGUgYW5kIHByb3ZpZGVyc1xuICAgICAgICB2aXNpdChhc3QuYXR0cnMpO1xuICAgICAgICB2aXNpdChhc3QuZGlyZWN0aXZlcyk7XG4gICAgICAgIHZpc2l0KGFzdC5jaGlsZHJlbik7XG4gICAgICB9KTtcbiAgICB9XG5cbiAgICB2aXNpdEVsZW1lbnQoYXN0OiBFbGVtZW50QXN0LCBjb250ZXh0OiBhbnkpOiBhbnkge1xuICAgICAgcmV0dXJuIHRoaXMudmlzaXRDaGlsZHJlbihjb250ZXh0LCB2aXNpdCA9PiB7XG4gICAgICAgIC8vIEluZ25vcmUgcHJvdmlkZXJzXG4gICAgICAgIHZpc2l0KGFzdC5hdHRycyk7XG4gICAgICAgIHZpc2l0KGFzdC5pbnB1dHMpO1xuICAgICAgICB2aXNpdChhc3Qub3V0cHV0cyk7XG4gICAgICAgIHZpc2l0KGFzdC5yZWZlcmVuY2VzKTtcbiAgICAgICAgdmlzaXQoYXN0LmRpcmVjdGl2ZXMpO1xuICAgICAgICB2aXNpdChhc3QuY2hpbGRyZW4pO1xuICAgICAgfSk7XG4gICAgfVxuXG4gICAgdmlzaXREaXJlY3RpdmUoYXN0OiBEaXJlY3RpdmVBc3QsIGNvbnRleHQ6IGFueSk6IGFueSB7XG4gICAgICAvLyBJZ25vcmUgdGhlIGhvc3QgcHJvcGVydGllcyBvZiBhIGRpcmVjdGl2ZVxuICAgICAgY29uc3QgcmVzdWx0ID0gdGhpcy52aXNpdENoaWxkcmVuKGNvbnRleHQsIHZpc2l0ID0+IHsgdmlzaXQoYXN0LmlucHV0cyk7IH0pO1xuICAgICAgLy8gV2UgbmV2ZXIgY2FyZSBhYm91dCB0aGUgZGlyZXRpdmUgaXRzZWxmLCBqdXN0IGl0cyBpbnB1dHMuXG4gICAgICBpZiAocGF0aFtwYXRoLmxlbmd0aCAtIDFdID09PSBhc3QpIHtcbiAgICAgICAgcGF0aC5wb3AoKTtcbiAgICAgIH1cbiAgICAgIHJldHVybiByZXN1bHQ7XG4gICAgfVxuICB9O1xuXG4gIHRlbXBsYXRlVmlzaXRBbGwodmlzaXRvciwgYXN0KTtcblxuICByZXR1cm4gbmV3IEFzdFBhdGg8VGVtcGxhdGVBc3Q+KHBhdGgsIHBvc2l0aW9uKTtcbn1cblxuLyoqXG4gKiBSZXR1cm4gdGhlIG5vZGUgdGhhdCBtb3N0IHRpZ2h0bHkgZW5jb21wYXNzIHRoZSBzcGVjaWZpZWQgYHBvc2l0aW9uYC5cbiAqIEBwYXJhbSBub2RlXG4gKiBAcGFyYW0gcG9zaXRpb25cbiAqL1xuZXhwb3J0IGZ1bmN0aW9uIGZpbmRUaWdodGVzdE5vZGUobm9kZTogdHMuTm9kZSwgcG9zaXRpb246IG51bWJlcik6IHRzLk5vZGV8dW5kZWZpbmVkIHtcbiAgaWYgKG5vZGUuZ2V0U3RhcnQoKSA8PSBwb3NpdGlvbiAmJiBwb3NpdGlvbiA8IG5vZGUuZ2V0RW5kKCkpIHtcbiAgICByZXR1cm4gbm9kZS5mb3JFYWNoQ2hpbGQoYyA9PiBmaW5kVGlnaHRlc3ROb2RlKGMsIHBvc2l0aW9uKSkgfHwgbm9kZTtcbiAgfVxufVxuXG5pbnRlcmZhY2UgRGlyZWN0aXZlQ2xhc3NMaWtlIHtcbiAgZGVjb3JhdG9ySWQ6IHRzLklkZW50aWZpZXI7ICAvLyBkZWNvcmF0b3IgaWRlbnRpZmllclxuICBjbGFzc0RlY2w6IHRzLkNsYXNzRGVjbGFyYXRpb247XG59XG5cbi8qKlxuICogUmV0dXJuIG1ldGFkYXRhIGFib3V0IGBub2RlYCBpZiBpdCBsb29rcyBsaWtlIGFuIEFuZ3VsYXIgZGlyZWN0aXZlIGNsYXNzLlxuICogSW4gdGhpcyBjYXNlLCBwb3RlbnRpYWwgbWF0Y2hlcyBhcmUgYEBOZ01vZHVsZWAsIGBAQ29tcG9uZW50YCwgYEBEaXJlY3RpdmVgLFxuICogYEBQaXBlYCwgZXRjLlxuICogVGhlc2UgY2xhc3MgZGVjbGFyYXRpb25zIGFsbCBzaGFyZSBzb21lIGNvbW1vbiBhdHRyaWJ1dGVzLCBuYW1lbHkgdGhlaXJcbiAqIGRlY29yYXRvciB0YWtlcyBleGFjdGx5IG9uZSBwYXJhbWV0ZXIgYW5kIHRoZSBwYXJhbWV0ZXIgbXVzdCBiZSBhbiBvYmplY3RcbiAqIGxpdGVyYWwuXG4gKlxuICogRm9yIGV4YW1wbGUsXG4gKiAgICAgdi0tLS0tLS0tLS0gYGRlY29yYXRvcklkYFxuICogQE5nTW9kdWxlKHtcbiAqICAgZGVjbGFyYXRpb25zOiBbXSxcbiAqIH0pXG4gKiBjbGFzcyBBcHBNb2R1bGUge31cbiAqICAgICAgICAgIF4tLS0tLSBgY2xhc3NEZWNsYFxuICpcbiAqIEBwYXJhbSBub2RlIFBvdGVudGlhbCBub2RlIHRoYXQgcmVwcmVzZW50cyBhbiBBbmd1bGFyIGRpcmVjdGl2ZS5cbiAqL1xuZXhwb3J0IGZ1bmN0aW9uIGdldERpcmVjdGl2ZUNsYXNzTGlrZShub2RlOiB0cy5Ob2RlKTogRGlyZWN0aXZlQ2xhc3NMaWtlfHVuZGVmaW5lZCB7XG4gIGlmICghdHMuaXNDbGFzc0RlY2xhcmF0aW9uKG5vZGUpIHx8ICFub2RlLm5hbWUgfHwgIW5vZGUuZGVjb3JhdG9ycykge1xuICAgIHJldHVybjtcbiAgfVxuICBmb3IgKGNvbnN0IGQgb2Ygbm9kZS5kZWNvcmF0b3JzKSB7XG4gICAgY29uc3QgZXhwciA9IGQuZXhwcmVzc2lvbjtcbiAgICBpZiAoIXRzLmlzQ2FsbEV4cHJlc3Npb24oZXhwcikgfHwgZXhwci5hcmd1bWVudHMubGVuZ3RoICE9PSAxIHx8XG4gICAgICAgICF0cy5pc0lkZW50aWZpZXIoZXhwci5leHByZXNzaW9uKSkge1xuICAgICAgY29udGludWU7XG4gICAgfVxuICAgIGNvbnN0IGFyZyA9IGV4cHIuYXJndW1lbnRzWzBdO1xuICAgIGlmICh0cy5pc09iamVjdExpdGVyYWxFeHByZXNzaW9uKGFyZykpIHtcbiAgICAgIHJldHVybiB7XG4gICAgICAgIGRlY29yYXRvcklkOiBleHByLmV4cHJlc3Npb24sXG4gICAgICAgIGNsYXNzRGVjbDogbm9kZSxcbiAgICAgIH07XG4gICAgfVxuICB9XG59XG5cbi8qKlxuICogRmluZHMgdGhlIHZhbHVlIG9mIGEgcHJvcGVydHkgYXNzaWdubWVudCB0aGF0IGlzIG5lc3RlZCBpbiBhIFR5cGVTY3JpcHQgbm9kZSBhbmQgaXMgb2YgYSBjZXJ0YWluXG4gKiB0eXBlIFQuXG4gKlxuICogQHBhcmFtIHN0YXJ0Tm9kZSBub2RlIHRvIHN0YXJ0IHNlYXJjaGluZyBmb3IgbmVzdGVkIHByb3BlcnR5IGFzc2lnbm1lbnQgZnJvbVxuICogQHBhcmFtIHByb3BOYW1lIHByb3BlcnR5IGFzc2lnbm1lbnQgbmFtZVxuICogQHBhcmFtIHByZWRpY2F0ZSBmdW5jdGlvbiB0byB2ZXJpZnkgdGhhdCBhIG5vZGUgaXMgb2YgdHlwZSBULlxuICogQHJldHVybiBub2RlIHByb3BlcnR5IGFzc2lnbm1lbnQgdmFsdWUgb2YgdHlwZSBULCBvciB1bmRlZmluZWQgaWYgbm9uZSBpcyBmb3VuZFxuICovXG5leHBvcnQgZnVuY3Rpb24gZmluZFByb3BlcnR5VmFsdWVPZlR5cGU8VCBleHRlbmRzIHRzLk5vZGU+KFxuICAgIHN0YXJ0Tm9kZTogdHMuTm9kZSwgcHJvcE5hbWU6IHN0cmluZywgcHJlZGljYXRlOiAobm9kZTogdHMuTm9kZSkgPT4gbm9kZSBpcyBUKTogVHx1bmRlZmluZWQge1xuICBpZiAodHMuaXNQcm9wZXJ0eUFzc2lnbm1lbnQoc3RhcnROb2RlKSAmJiBzdGFydE5vZGUubmFtZS5nZXRUZXh0KCkgPT09IHByb3BOYW1lKSB7XG4gICAgY29uc3Qge2luaXRpYWxpemVyfSA9IHN0YXJ0Tm9kZTtcbiAgICBpZiAocHJlZGljYXRlKGluaXRpYWxpemVyKSkgcmV0dXJuIGluaXRpYWxpemVyO1xuICB9XG4gIHJldHVybiBzdGFydE5vZGUuZm9yRWFjaENoaWxkKGMgPT4gZmluZFByb3BlcnR5VmFsdWVPZlR5cGUoYywgcHJvcE5hbWUsIHByZWRpY2F0ZSkpO1xufVxuIl19