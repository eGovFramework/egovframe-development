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
        define("@angular/language-service/src/diagnostics", ["require", "exports", "tslib", "path", "typescript", "@angular/language-service/src/expression_diagnostics", "@angular/language-service/src/types", "@angular/language-service/src/utils"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var tslib_1 = require("tslib");
    var path = require("path");
    var ts = require("typescript");
    var expression_diagnostics_1 = require("@angular/language-service/src/expression_diagnostics");
    var ng = require("@angular/language-service/src/types");
    var utils_1 = require("@angular/language-service/src/utils");
    /**
     * Return diagnostic information for the parsed AST of the template.
     * @param ast contains HTML and template AST
     */
    function getTemplateDiagnostics(ast) {
        var parseErrors = ast.parseErrors, templateAst = ast.templateAst, htmlAst = ast.htmlAst, template = ast.template;
        if (parseErrors && parseErrors.length) {
            return parseErrors.map(function (e) {
                return {
                    kind: ng.DiagnosticKind.Error,
                    span: utils_1.offsetSpan(utils_1.spanOf(e.span), template.span.start),
                    message: e.msg,
                };
            });
        }
        return expression_diagnostics_1.getTemplateExpressionDiagnostics({
            templateAst: templateAst,
            htmlAst: htmlAst,
            offset: template.span.start,
            query: template.query,
            members: template.members,
        });
    }
    exports.getTemplateDiagnostics = getTemplateDiagnostics;
    /**
     * Generate an error message that indicates a directive is not part of any
     * NgModule.
     * @param name class name
     * @param isComponent true if directive is an Angular Component
     */
    function missingDirective(name, isComponent) {
        var type = isComponent ? 'Component' : 'Directive';
        return type + " '" + name + "' is not included in a module and will not be " +
            'available inside a template. Consider adding it to a NgModule declaration.';
    }
    /**
     * Logs an error for an impossible state with a certain message.
     */
    function logImpossibleState(message) {
        console.error("Impossible state: " + message);
    }
    /**
     * Performs a variety diagnostics on directive declarations.
     *
     * @param declarations Angular directive declarations
     * @param modules NgModules in the project
     * @param host TypeScript service host used to perform TypeScript queries
     * @return diagnosed errors, if any
     */
    function getDeclarationDiagnostics(declarations, modules, host) {
        var e_1, _a, e_2, _b, e_3, _c, e_4, _d;
        var directives = new Set();
        try {
            for (var _e = tslib_1.__values(modules.ngModules), _f = _e.next(); !_f.done; _f = _e.next()) {
                var ngModule = _f.value;
                try {
                    for (var _g = (e_2 = void 0, tslib_1.__values(ngModule.declaredDirectives)), _h = _g.next(); !_h.done; _h = _g.next()) {
                        var directive = _h.value;
                        directives.add(directive.reference);
                    }
                }
                catch (e_2_1) { e_2 = { error: e_2_1 }; }
                finally {
                    try {
                        if (_h && !_h.done && (_b = _g.return)) _b.call(_g);
                    }
                    finally { if (e_2) throw e_2.error; }
                }
            }
        }
        catch (e_1_1) { e_1 = { error: e_1_1 }; }
        finally {
            try {
                if (_f && !_f.done && (_a = _e.return)) _a.call(_e);
            }
            finally { if (e_1) throw e_1.error; }
        }
        var results = [];
        try {
            for (var declarations_1 = tslib_1.__values(declarations), declarations_1_1 = declarations_1.next(); !declarations_1_1.done; declarations_1_1 = declarations_1.next()) {
                var declaration = declarations_1_1.value;
                var errors = declaration.errors, metadata = declaration.metadata, type = declaration.type, declarationSpan = declaration.declarationSpan;
                var sf = host.getSourceFile(type.filePath);
                if (!sf) {
                    logImpossibleState("directive " + type.name + " exists but has no source file");
                    return [];
                }
                // TypeScript identifier of the directive declaration annotation (e.g. "Component" or
                // "Directive") on a directive class.
                var directiveIdentifier = utils_1.findTightestNode(sf, declarationSpan.start);
                if (!directiveIdentifier) {
                    logImpossibleState("directive " + type.name + " exists but has no identifier");
                    return [];
                }
                try {
                    for (var errors_1 = (e_4 = void 0, tslib_1.__values(errors)), errors_1_1 = errors_1.next(); !errors_1_1.done; errors_1_1 = errors_1.next()) {
                        var error = errors_1_1.value;
                        results.push({
                            kind: ng.DiagnosticKind.Error,
                            message: error.message,
                            span: error.span,
                        });
                    }
                }
                catch (e_4_1) { e_4 = { error: e_4_1 }; }
                finally {
                    try {
                        if (errors_1_1 && !errors_1_1.done && (_d = errors_1.return)) _d.call(errors_1);
                    }
                    finally { if (e_4) throw e_4.error; }
                }
                if (!metadata) {
                    continue; // declaration is not an Angular directive
                }
                if (metadata.isComponent) {
                    if (!modules.ngModuleByPipeOrDirective.has(declaration.type)) {
                        results.push({
                            kind: ng.DiagnosticKind.Error,
                            message: missingDirective(type.name, metadata.isComponent),
                            span: declarationSpan,
                        });
                    }
                    var _j = metadata.template, template = _j.template, templateUrl = _j.templateUrl, styleUrls = _j.styleUrls;
                    if (template === null && !templateUrl) {
                        results.push({
                            kind: ng.DiagnosticKind.Error,
                            message: "Component '" + type.name + "' must have a template or templateUrl",
                            span: declarationSpan,
                        });
                    }
                    else if (templateUrl) {
                        if (template) {
                            results.push({
                                kind: ng.DiagnosticKind.Error,
                                message: "Component '" + type.name + "' must not have both template and templateUrl",
                                span: declarationSpan,
                            });
                        }
                        // Find templateUrl value from the directive call expression, which is the parent of the
                        // directive identifier.
                        //
                        // TODO: We should create an enum of the various properties a directive can have to use
                        // instead of string literals. We can then perform a mass migration of all literal usages.
                        var templateUrlNode = utils_1.findPropertyValueOfType(directiveIdentifier.parent, 'templateUrl', ts.isLiteralExpression);
                        if (!templateUrlNode) {
                            logImpossibleState("templateUrl " + templateUrl + " exists but its TypeScript node doesn't");
                            return [];
                        }
                        results.push.apply(results, tslib_1.__spread(validateUrls([templateUrlNode], host.tsLsHost)));
                    }
                    if (styleUrls.length > 0) {
                        // Find styleUrls value from the directive call expression, which is the parent of the
                        // directive identifier.
                        var styleUrlsNode = utils_1.findPropertyValueOfType(directiveIdentifier.parent, 'styleUrls', ts.isArrayLiteralExpression);
                        if (!styleUrlsNode) {
                            logImpossibleState("styleUrls property exists but its TypeScript node doesn't'");
                            return [];
                        }
                        results.push.apply(results, tslib_1.__spread(validateUrls(styleUrlsNode.elements, host.tsLsHost)));
                    }
                }
                else if (!directives.has(declaration.type)) {
                    results.push({
                        kind: ng.DiagnosticKind.Error,
                        message: missingDirective(type.name, metadata.isComponent),
                        span: declarationSpan,
                    });
                }
            }
        }
        catch (e_3_1) { e_3 = { error: e_3_1 }; }
        finally {
            try {
                if (declarations_1_1 && !declarations_1_1.done && (_c = declarations_1.return)) _c.call(declarations_1);
            }
            finally { if (e_3) throw e_3.error; }
        }
        return results;
    }
    exports.getDeclarationDiagnostics = getDeclarationDiagnostics;
    /**
     * Checks that URLs on a directive point to a valid file.
     * Note that this diagnostic check may require a filesystem hit, and thus may be slower than other
     * checks.
     *
     * @param urls urls to check for validity
     * @param tsLsHost TS LS host used for querying filesystem information
     * @return diagnosed url errors, if any
     */
    function validateUrls(urls, tsLsHost) {
        if (!tsLsHost.fileExists) {
            return [];
        }
        var allErrors = [];
        // TODO(ayazhafiz): most of this logic can be unified with the logic in
        // definitions.ts#getUrlFromProperty. Create a utility function to be used by both.
        for (var i = 0; i < urls.length; ++i) {
            var urlNode = urls[i];
            if (!ts.isStringLiteralLike(urlNode)) {
                // If a non-string value is assigned to a URL node (like `templateUrl`), a type error will be
                // picked up by the TS Language Server.
                continue;
            }
            var curPath = urlNode.getSourceFile().fileName;
            var url = path.join(path.dirname(curPath), urlNode.text);
            if (tsLsHost.fileExists(url))
                continue;
            allErrors.push({
                kind: ng.DiagnosticKind.Error,
                message: "URL does not point to a valid file",
                // Exclude opening and closing quotes in the url span.
                span: { start: urlNode.getStart() + 1, end: urlNode.end - 1 },
            });
        }
        return allErrors;
    }
    /**
     * Return a recursive data structure that chains diagnostic messages.
     * @param chain
     */
    function chainDiagnostics(chain) {
        return {
            messageText: chain.message,
            category: ts.DiagnosticCategory.Error,
            code: 0,
            next: chain.next ? chain.next.map(chainDiagnostics) : undefined
        };
    }
    /**
     * Convert ng.Diagnostic to ts.Diagnostic.
     * @param d diagnostic
     * @param file
     */
    function ngDiagnosticToTsDiagnostic(d, file) {
        return {
            file: file,
            start: d.span.start,
            length: d.span.end - d.span.start,
            messageText: typeof d.message === 'string' ? d.message : chainDiagnostics(d.message),
            category: ts.DiagnosticCategory.Error,
            code: 0,
            source: 'ng',
        };
    }
    exports.ngDiagnosticToTsDiagnostic = ngDiagnosticToTsDiagnostic;
    /**
     * Return elements filtered by unique span.
     * @param elements
     */
    function uniqueBySpan(elements) {
        var e_5, _a;
        var result = [];
        var map = new Map();
        try {
            for (var elements_1 = tslib_1.__values(elements), elements_1_1 = elements_1.next(); !elements_1_1.done; elements_1_1 = elements_1.next()) {
                var element = elements_1_1.value;
                var span = element.span;
                var set = map.get(span.start);
                if (!set) {
                    set = new Set();
                    map.set(span.start, set);
                }
                if (!set.has(span.end)) {
                    set.add(span.end);
                    result.push(element);
                }
            }
        }
        catch (e_5_1) { e_5 = { error: e_5_1 }; }
        finally {
            try {
                if (elements_1_1 && !elements_1_1.done && (_a = elements_1.return)) _a.call(elements_1);
            }
            finally { if (e_5) throw e_5.error; }
        }
        return result;
    }
    exports.uniqueBySpan = uniqueBySpan;
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZGlhZ25vc3RpY3MuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi8uLi8uLi9wYWNrYWdlcy9sYW5ndWFnZS1zZXJ2aWNlL3NyYy9kaWFnbm9zdGljcy50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTs7Ozs7O0dBTUc7Ozs7Ozs7Ozs7Ozs7SUFHSCwyQkFBNkI7SUFDN0IsK0JBQWlDO0lBR2pDLCtGQUEwRTtJQUMxRSx3REFBOEI7SUFFOUIsNkRBQXNGO0lBR3RGOzs7T0FHRztJQUNILFNBQWdCLHNCQUFzQixDQUFDLEdBQWM7UUFDNUMsSUFBQSw2QkFBVyxFQUFFLDZCQUFXLEVBQUUscUJBQU8sRUFBRSx1QkFBUSxDQUFRO1FBQzFELElBQUksV0FBVyxJQUFJLFdBQVcsQ0FBQyxNQUFNLEVBQUU7WUFDckMsT0FBTyxXQUFXLENBQUMsR0FBRyxDQUFDLFVBQUEsQ0FBQztnQkFDdEIsT0FBTztvQkFDTCxJQUFJLEVBQUUsRUFBRSxDQUFDLGNBQWMsQ0FBQyxLQUFLO29CQUM3QixJQUFJLEVBQUUsa0JBQVUsQ0FBQyxjQUFNLENBQUMsQ0FBQyxDQUFDLElBQUksQ0FBQyxFQUFFLFFBQVEsQ0FBQyxJQUFJLENBQUMsS0FBSyxDQUFDO29CQUNyRCxPQUFPLEVBQUUsQ0FBQyxDQUFDLEdBQUc7aUJBQ2YsQ0FBQztZQUNKLENBQUMsQ0FBQyxDQUFDO1NBQ0o7UUFDRCxPQUFPLHlEQUFnQyxDQUFDO1lBQ3RDLFdBQVcsRUFBRSxXQUFXO1lBQ3hCLE9BQU8sRUFBRSxPQUFPO1lBQ2hCLE1BQU0sRUFBRSxRQUFRLENBQUMsSUFBSSxDQUFDLEtBQUs7WUFDM0IsS0FBSyxFQUFFLFFBQVEsQ0FBQyxLQUFLO1lBQ3JCLE9BQU8sRUFBRSxRQUFRLENBQUMsT0FBTztTQUMxQixDQUFDLENBQUM7SUFDTCxDQUFDO0lBbEJELHdEQWtCQztJQUVEOzs7OztPQUtHO0lBQ0gsU0FBUyxnQkFBZ0IsQ0FBQyxJQUFZLEVBQUUsV0FBb0I7UUFDMUQs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RCxvQ0FnQkMiLCJzb3VyY2VzQ29udGVudCI6WyIvKipcbiAqIEBsaWNlbnNlXG4gKiBDb3B5cmlnaHQgR29vZ2xlIEluYy4gQWxsIFJpZ2h0cyBSZXNlcnZlZC5cbiAqXG4gKiBVc2Ugb2YgdGhpcyBzb3VyY2UgY29kZSBpcyBnb3Zlcm5lZCBieSBhbiBNSVQtc3R5bGUgbGljZW5zZSB0aGF0IGNhbiBiZVxuICogZm91bmQgaW4gdGhlIExJQ0VOU0UgZmlsZSBhdCBodHRwczovL2FuZ3VsYXIuaW8vbGljZW5zZVxuICovXG5cbmltcG9ydCB7TmdBbmFseXplZE1vZHVsZXN9IGZyb20gJ0Bhbmd1bGFyL2NvbXBpbGVyJztcbmltcG9ydCAqIGFzIHBhdGggZnJvbSAncGF0aCc7XG5pbXBvcnQgKiBhcyB0cyBmcm9tICd0eXBlc2NyaXB0JztcblxuaW1wb3J0IHtBc3RSZXN1bHR9IGZyb20gJy4vY29tbW9uJztcbmltcG9ydCB7Z2V0VGVtcGxhdGVFeHByZXNzaW9uRGlhZ25vc3RpY3N9IGZyb20gJy4vZXhwcmVzc2lvbl9kaWFnbm9zdGljcyc7XG5pbXBvcnQgKiBhcyBuZyBmcm9tICcuL3R5cGVzJztcbmltcG9ydCB7VHlwZVNjcmlwdFNlcnZpY2VIb3N0fSBmcm9tICcuL3R5cGVzY3JpcHRfaG9zdCc7XG5pbXBvcnQge2ZpbmRQcm9wZXJ0eVZhbHVlT2ZUeXBlLCBmaW5kVGlnaHRlc3ROb2RlLCBvZmZzZXRTcGFuLCBzcGFuT2Z9IGZyb20gJy4vdXRpbHMnO1xuXG5cbi8qKlxuICogUmV0dXJuIGRpYWdub3N0aWMgaW5mb3JtYXRpb24gZm9yIHRoZSBwYXJzZWQgQVNUIG9mIHRoZSB0ZW1wbGF0ZS5cbiAqIEBwYXJhbSBhc3QgY29udGFpbnMgSFRNTCBhbmQgdGVtcGxhdGUgQVNUXG4gKi9cbmV4cG9ydCBmdW5jdGlvbiBnZXRUZW1wbGF0ZURpYWdub3N0aWNzKGFzdDogQXN0UmVzdWx0KTogbmcuRGlhZ25vc3RpY1tdIHtcbiAgY29uc3Qge3BhcnNlRXJyb3JzLCB0ZW1wbGF0ZUFzdCwgaHRtbEFzdCwgdGVtcGxhdGV9ID0gYXN0O1xuICBpZiAocGFyc2VFcnJvcnMgJiYgcGFyc2VFcnJvcnMubGVuZ3RoKSB7XG4gICAgcmV0dXJuIHBhcnNlRXJyb3JzLm1hcChlID0+IHtcbiAgICAgIHJldHVybiB7XG4gICAgICAgIGtpbmQ6IG5nLkRpYWdub3N0aWNLaW5kLkVycm9yLFxuICAgICAgICBzcGFuOiBvZmZzZXRTcGFuKHNwYW5PZihlLnNwYW4pLCB0ZW1wbGF0ZS5zcGFuLnN0YXJ0KSxcbiAgICAgICAgbWVzc2FnZTogZS5tc2csXG4gICAgICB9O1xuICAgIH0pO1xuICB9XG4gIHJldHVybiBnZXRUZW1wbGF0ZUV4cHJlc3Npb25EaWFnbm9zdGljcyh7XG4gICAgdGVtcGxhdGVBc3Q6IHRlbXBsYXRlQXN0LFxuICAgIGh0bWxBc3Q6IGh0bWxBc3QsXG4gICAgb2Zmc2V0OiB0ZW1wbGF0ZS5zcGFuLnN0YXJ0LFxuICAgIHF1ZXJ5OiB0ZW1wbGF0ZS5xdWVyeSxcbiAgICBtZW1iZXJzOiB0ZW1wbGF0ZS5tZW1iZXJzLFxuICB9KTtcbn1cblxuLyoqXG4gKiBHZW5lcmF0ZSBhbiBlcnJvciBtZXNzYWdlIHRoYXQgaW5kaWNhdGVzIGEgZGlyZWN0aXZlIGlzIG5vdCBwYXJ0IG9mIGFueVxuICogTmdNb2R1bGUuXG4gKiBAcGFyYW0gbmFtZSBjbGFzcyBuYW1lXG4gKiBAcGFyYW0gaXNDb21wb25lbnQgdHJ1ZSBpZiBkaXJlY3RpdmUgaXMgYW4gQW5ndWxhciBDb21wb25lbnRcbiAqL1xuZnVuY3Rpb24gbWlzc2luZ0RpcmVjdGl2ZShuYW1lOiBzdHJpbmcsIGlzQ29tcG9uZW50OiBib29sZWFuKSB7XG4gIGNvbnN0IHR5cGUgPSBpc0NvbXBvbmVudCA/ICdDb21wb25lbnQnIDogJ0RpcmVjdGl2ZSc7XG4gIHJldHVybiBgJHt0eXBlfSAnJHtuYW1lfScgaXMgbm90IGluY2x1ZGVkIGluIGEgbW9kdWxlIGFuZCB3aWxsIG5vdCBiZSBgICtcbiAgICAgICdhdmFpbGFibGUgaW5zaWRlIGEgdGVtcGxhdGUuIENvbnNpZGVyIGFkZGluZyBpdCB0byBhIE5nTW9kdWxlIGRlY2xhcmF0aW9uLic7XG59XG5cbi8qKlxuICogTG9ncyBhbiBlcnJvciBmb3IgYW4gaW1wb3NzaWJsZSBzdGF0ZSB3aXRoIGEgY2VydGFpbiBtZXNzYWdlLlxuICovXG5mdW5jdGlvbiBsb2dJbXBvc3NpYmxlU3RhdGUobWVzc2FnZTogc3RyaW5nKSB7XG4gIGNvbnNvbGUuZXJyb3IoYEltcG9zc2libGUgc3RhdGU6ICR7bWVzc2FnZX1gKTtcbn1cblxuLyoqXG4gKiBQZXJmb3JtcyBhIHZhcmlldHkgZGlhZ25vc3RpY3Mgb24gZGlyZWN0aXZlIGRlY2xhcmF0aW9ucy5cbiAqXG4gKiBAcGFyYW0gZGVjbGFyYXRpb25zIEFuZ3VsYXIgZGlyZWN0aXZlIGRlY2xhcmF0aW9uc1xuICogQHBhcmFtIG1vZHVsZXMgTmdNb2R1bGVzIGluIHRoZSBwcm9qZWN0XG4gKiBAcGFyYW0gaG9zdCBUeXBlU2NyaXB0IHNlcnZpY2UgaG9zdCB1c2VkIHRvIHBlcmZvcm0gVHlwZVNjcmlwdCBxdWVyaWVzXG4gKiBAcmV0dXJuIGRpYWdub3NlZCBlcnJvcnMsIGlmIGFueVxuICovXG5leHBvcnQgZnVuY3Rpb24gZ2V0RGVjbGFyYXRpb25EaWFnbm9zdGljcyhcbiAgICBkZWNsYXJhdGlvbnM6IG5nLkRlY2xhcmF0aW9uW10sIG1vZHVsZXM6IE5nQW5hbHl6ZWRNb2R1bGVzLFxuICAgIGhvc3Q6IFJlYWRvbmx5PFR5cGVTY3JpcHRTZXJ2aWNlSG9zdD4pOiBuZy5EaWFnbm9zdGljW10ge1xuICBjb25zdCBkaXJlY3RpdmVzID0gbmV3IFNldDxuZy5TdGF0aWNTeW1ib2w+KCk7XG4gIGZvciAoY29uc3QgbmdNb2R1bGUgb2YgbW9kdWxlcy5uZ01vZHVsZXMpIHtcbiAgICBmb3IgKGNvbnN0IGRpcmVjdGl2ZSBvZiBuZ01vZHVsZS5kZWNsYXJlZERpcmVjdGl2ZXMpIHtcbiAgICAgIGRpcmVjdGl2ZXMuYWRkKGRpcmVjdGl2ZS5yZWZlcmVuY2UpO1xuICAgIH1cbiAgfVxuXG4gIGNvbnN0IHJlc3VsdHM6IG5nLkRpYWdub3N0aWNbXSA9IFtdO1xuXG4gIGZvciAoY29uc3QgZGVjbGFyYXRpb24gb2YgZGVjbGFyYXRpb25zKSB7XG4gICAgY29uc3Qge2Vycm9ycywgbWV0YWRhdGEsIHR5cGUsIGRlY2xhcmF0aW9uU3Bhbn0gPSBkZWNsYXJhdGlvbjtcblxuICAgIGNvbnN0IHNmID0gaG9zdC5nZXRTb3VyY2VGaWxlKHR5cGUuZmlsZVBhdGgpO1xuICAgIGlmICghc2YpIHtcbiAgICAgIGxvZ0ltcG9zc2libGVTdGF0ZShgZGlyZWN0aXZlICR7dHlwZS5uYW1lfSBleGlzdHMgYnV0IGhhcyBubyBzb3VyY2UgZmlsZWApO1xuICAgICAgcmV0dXJuIFtdO1xuICAgIH1cbiAgICAvLyBUeXBlU2NyaXB0IGlkZW50aWZpZXIgb2YgdGhlIGRpcmVjdGl2ZSBkZWNsYXJhdGlvbiBhbm5vdGF0aW9uIChlLmcuIFwiQ29tcG9uZW50XCIgb3JcbiAgICAvLyBcIkRpcmVjdGl2ZVwiKSBvbiBhIGRpcmVjdGl2ZSBjbGFzcy5cbiAgICBjb25zdCBkaXJlY3RpdmVJZGVudGlmaWVyID0gZmluZFRpZ2h0ZXN0Tm9kZShzZiwgZGVjbGFyYXRpb25TcGFuLnN0YXJ0KTtcbiAgICBpZiAoIWRpcmVjdGl2ZUlkZW50aWZpZXIpIHtcbiAgICAgIGxvZ0ltcG9zc2libGVTdGF0ZShgZGlyZWN0aXZlICR7dHlwZS5uYW1lfSBleGlzdHMgYnV0IGhhcyBubyBpZGVudGlmaWVyYCk7XG4gICAgICByZXR1cm4gW107XG4gICAgfVxuXG4gICAgZm9yIChjb25zdCBlcnJvciBvZiBlcnJvcnMpIHtcbiAgICAgIHJlc3VsdHMucHVzaCh7XG4gICAgICAgIGtpbmQ6IG5nLkRpYWdub3N0aWNLaW5kLkVycm9yLFxuICAgICAgICBtZXNzYWdlOiBlcnJvci5tZXNzYWdlLFxuICAgICAgICBzcGFuOiBlcnJvci5zcGFuLFxuICAgICAgfSk7XG4gICAgfVxuICAgIGlmICghbWV0YWRhdGEpIHtcbiAgICAgIGNvbnRpbnVlOyAgLy8gZGVjbGFyYXRpb24gaXMgbm90IGFuIEFuZ3VsYXIgZGlyZWN0aXZlXG4gICAgfVxuICAgIGlmIChtZXRhZGF0YS5pc0NvbXBvbmVudCkge1xuICAgICAgaWYgKCFtb2R1bGVzLm5nTW9kdWxlQnlQaXBlT3JEaXJlY3RpdmUuaGFzKGRlY2xhcmF0aW9uLnR5cGUpKSB7XG4gICAgICAgIHJlc3VsdHMucHVzaCh7XG4gICAgICAgICAga2luZDogbmcuRGlhZ25vc3RpY0tpbmQuRXJyb3IsXG4gICAgICAgICAgbWVzc2FnZTogbWlzc2luZ0RpcmVjdGl2ZSh0eXBlLm5hbWUsIG1ldGFkYXRhLmlzQ29tcG9uZW50KSxcbiAgICAgICAgICBzcGFuOiBkZWNsYXJhdGlvblNwYW4sXG4gICAgICAgIH0pO1xuICAgICAgfVxuICAgICAgY29uc3Qge3RlbXBsYXRlLCB0ZW1wbGF0ZVVybCwgc3R5bGVVcmxzfSA9IG1ldGFkYXRhLnRlbXBsYXRlICE7XG4gICAgICBpZiAodGVtcGxhdGUgPT09IG51bGwgJiYgIXRlbXBsYXRlVXJsKSB7XG4gICAgICAgIHJlc3VsdHMucHVzaCh7XG4gICAgICAgICAga2luZDogbmcuRGlhZ25vc3RpY0tpbmQuRXJyb3IsXG4gICAgICAgICAgbWVzc2FnZTogYENvbXBvbmVudCAnJHt0eXBlLm5hbWV9JyBtdXN0IGhhdmUgYSB0ZW1wbGF0ZSBvciB0ZW1wbGF0ZVVybGAsXG4gICAgICAgICAgc3BhbjogZGVjbGFyYXRpb25TcGFuLFxuICAgICAgICB9KTtcbiAgICAgIH0gZWxzZSBpZiAodGVtcGxhdGVVcmwpIHtcbiAgICAgICAgaWYgKHRlbXBsYXRlKSB7XG4gICAgICAgICAgcmVzdWx0cy5wdXNoKHtcbiAgICAgICAgICAgIGtpbmQ6IG5nLkRpYWdub3N0aWNLaW5kLkVycm9yLFxuICAgICAgICAgICAgbWVzc2FnZTogYENvbXBvbmVudCAnJHt0eXBlLm5hbWV9JyBtdXN0IG5vdCBoYXZlIGJvdGggdGVtcGxhdGUgYW5kIHRlbXBsYXRlVXJsYCxcbiAgICAgICAgICAgIHNwYW46IGRlY2xhcmF0aW9uU3BhbixcbiAgICAgICAgICB9KTtcbiAgICAgICAgfVxuXG4gICAgICAgIC8vIEZpbmQgdGVtcGxhdGVVcmwgdmFsdWUgZnJvbSB0aGUgZGlyZWN0aXZlIGNhbGwgZXhwcmVzc2lvbiwgd2hpY2ggaXMgdGhlIHBhcmVudCBvZiB0aGVcbiAgICAgICAgLy8gZGlyZWN0aXZlIGlkZW50aWZpZXIuXG4gICAgICAgIC8vXG4gICAgICAgIC8vIFRPRE86IFdlIHNob3VsZCBjcmVhdGUgYW4gZW51bSBvZiB0aGUgdmFyaW91cyBwcm9wZXJ0aWVzIGEgZGlyZWN0aXZlIGNhbiBoYXZlIHRvIHVzZVxuICAgICAgICAvLyBpbnN0ZWFkIG9mIHN0cmluZyBsaXRlcmFscy4gV2UgY2FuIHRoZW4gcGVyZm9ybSBhIG1hc3MgbWlncmF0aW9uIG9mIGFsbCBsaXRlcmFsIHVzYWdlcy5cbiAgICAgICAgY29uc3QgdGVtcGxhdGVVcmxOb2RlID0gZmluZFByb3BlcnR5VmFsdWVPZlR5cGUoXG4gICAgICAgICAgICBkaXJlY3RpdmVJZGVudGlmaWVyLnBhcmVudCwgJ3RlbXBsYXRlVXJsJywgdHMuaXNMaXRlcmFsRXhwcmVzc2lvbik7XG4gICAgICAgIGlmICghdGVtcGxhdGVVcmxOb2RlKSB7XG4gICAgICAgICAgbG9nSW1wb3NzaWJsZVN0YXRlKGB0ZW1wbGF0ZVVybCAke3RlbXBsYXRlVXJsfSBleGlzdHMgYnV0IGl0cyBUeXBlU2NyaXB0IG5vZGUgZG9lc24ndGApO1xuICAgICAgICAgIHJldHVybiBbXTtcbiAgICAgICAgfVxuXG4gICAgICAgIHJlc3VsdHMucHVzaCguLi52YWxpZGF0ZVVybHMoW3RlbXBsYXRlVXJsTm9kZV0sIGhvc3QudHNMc0hvc3QpKTtcbiAgICAgIH1cblxuICAgICAgaWYgKHN0eWxlVXJscy5sZW5ndGggPiAwKSB7XG4gICAgICAgIC8vIEZpbmQgc3R5bGVVcmxzIHZhbHVlIGZyb20gdGhlIGRpcmVjdGl2ZSBjYWxsIGV4cHJlc3Npb24sIHdoaWNoIGlzIHRoZSBwYXJlbnQgb2YgdGhlXG4gICAgICAgIC8vIGRpcmVjdGl2ZSBpZGVudGlmaWVyLlxuICAgICAgICBjb25zdCBzdHlsZVVybHNOb2RlID0gZmluZFByb3BlcnR5VmFsdWVPZlR5cGUoXG4gICAgICAgICAgICBkaXJlY3RpdmVJZGVudGlmaWVyLnBhcmVudCwgJ3N0eWxlVXJscycsIHRzLmlzQXJyYXlMaXRlcmFsRXhwcmVzc2lvbik7XG4gICAgICAgIGlmICghc3R5bGVVcmxzTm9kZSkge1xuICAgICAgICAgIGxvZ0ltcG9zc2libGVTdGF0ZShgc3R5bGVVcmxzIHByb3BlcnR5IGV4aXN0cyBidXQgaXRzIFR5cGVTY3JpcHQgbm9kZSBkb2Vzbid0J2ApO1xuICAgICAgICAgIHJldHVybiBbXTtcbiAgICAgICAgfVxuXG4gICAgICAgIHJlc3VsdHMucHVzaCguLi52YWxpZGF0ZVVybHMoc3R5bGVVcmxzTm9kZS5lbGVtZW50cywgaG9zdC50c0xzSG9zdCkpO1xuICAgICAgfVxuICAgIH0gZWxzZSBpZiAoIWRpcmVjdGl2ZXMuaGFzKGRlY2xhcmF0aW9uLnR5cGUpKSB7XG4gICAgICByZXN1bHRzLnB1c2goe1xuICAgICAgICBraW5kOiBuZy5EaWFnbm9zdGljS2luZC5FcnJvcixcbiAgICAgICAgbWVzc2FnZTogbWlzc2luZ0RpcmVjdGl2ZSh0eXBlLm5hbWUsIG1ldGFkYXRhLmlzQ29tcG9uZW50KSxcbiAgICAgICAgc3BhbjogZGVjbGFyYXRpb25TcGFuLFxuICAgICAgfSk7XG4gICAgfVxuICB9XG5cbiAgcmV0dXJuIHJlc3VsdHM7XG59XG5cbi8qKlxuICogQ2hlY2tzIHRoYXQgVVJMcyBvbiBhIGRpcmVjdGl2ZSBwb2ludCB0byBhIHZhbGlkIGZpbGUuXG4gKiBOb3RlIHRoYXQgdGhpcyBkaWFnbm9zdGljIGNoZWNrIG1heSByZXF1aXJlIGEgZmlsZXN5c3RlbSBoaXQsIGFuZCB0aHVzIG1heSBiZSBzbG93ZXIgdGhhbiBvdGhlclxuICogY2hlY2tzLlxuICpcbiAqIEBwYXJhbSB1cmxzIHVybHMgdG8gY2hlY2sgZm9yIHZhbGlkaXR5XG4gKiBAcGFyYW0gdHNMc0hvc3QgVFMgTFMgaG9zdCB1c2VkIGZvciBxdWVyeWluZyBmaWxlc3lzdGVtIGluZm9ybWF0aW9uXG4gKiBAcmV0dXJuIGRpYWdub3NlZCB1cmwgZXJyb3JzLCBpZiBhbnlcbiAqL1xuZnVuY3Rpb24gdmFsaWRhdGVVcmxzKFxuICAgIHVybHM6IEFycmF5TGlrZTx0cy5FeHByZXNzaW9uPiwgdHNMc0hvc3Q6IFJlYWRvbmx5PHRzLkxhbmd1YWdlU2VydmljZUhvc3Q+KTogbmcuRGlhZ25vc3RpY1tdIHtcbiAgaWYgKCF0c0xzSG9zdC5maWxlRXhpc3RzKSB7XG4gICAgcmV0dXJuIFtdO1xuICB9XG5cbiAgY29uc3QgYWxsRXJyb3JzOiBuZy5EaWFnbm9zdGljW10gPSBbXTtcbiAgLy8gVE9ETyhheWF6aGFmaXopOiBtb3N0IG9mIHRoaXMgbG9naWMgY2FuIGJlIHVuaWZpZWQgd2l0aCB0aGUgbG9naWMgaW5cbiAgLy8gZGVmaW5pdGlvbnMudHMjZ2V0VXJsRnJvbVByb3BlcnR5LiBDcmVhdGUgYSB1dGlsaXR5IGZ1bmN0aW9uIHRvIGJlIHVzZWQgYnkgYm90aC5cbiAgZm9yIChsZXQgaSA9IDA7IGkgPCB1cmxzLmxlbmd0aDsgKytpKSB7XG4gICAgY29uc3QgdXJsTm9kZSA9IHVybHNbaV07XG4gICAgaWYgKCF0cy5pc1N0cmluZ0xpdGVyYWxMaWtlKHVybE5vZGUpKSB7XG4gICAgICAvLyBJZiBhIG5vbi1zdHJpbmcgdmFsdWUgaXMgYXNzaWduZWQgdG8gYSBVUkwgbm9kZSAobGlrZSBgdGVtcGxhdGVVcmxgKSwgYSB0eXBlIGVycm9yIHdpbGwgYmVcbiAgICAgIC8vIHBpY2tlZCB1cCBieSB0aGUgVFMgTGFuZ3VhZ2UgU2VydmVyLlxuICAgICAgY29udGludWU7XG4gICAgfVxuICAgIGNvbnN0IGN1clBhdGggPSB1cmxOb2RlLmdldFNvdXJjZUZpbGUoKS5maWxlTmFtZTtcbiAgICBjb25zdCB1cmwgPSBwYXRoLmpvaW4ocGF0aC5kaXJuYW1lKGN1clBhdGgpLCB1cmxOb2RlLnRleHQpO1xuICAgIGlmICh0c0xzSG9zdC5maWxlRXhpc3RzKHVybCkpIGNvbnRpbnVlO1xuXG4gICAgYWxsRXJyb3JzLnB1c2goe1xuICAgICAga2luZDogbmcuRGlhZ25vc3RpY0tpbmQuRXJyb3IsXG4gICAgICBtZXNzYWdlOiBgVVJMIGRvZXMgbm90IHBvaW50IHRvIGEgdmFsaWQgZmlsZWAsXG4gICAgICAvLyBFeGNsdWRlIG9wZW5pbmcgYW5kIGNsb3NpbmcgcXVvdGVzIGluIHRoZSB1cmwgc3Bhbi5cbiAgICAgIHNwYW46IHtzdGFydDogdXJsTm9kZS5nZXRTdGFydCgpICsgMSwgZW5kOiB1cmxOb2RlLmVuZCAtIDF9LFxuICAgIH0pO1xuICB9XG4gIHJldHVybiBhbGxFcnJvcnM7XG59XG5cbi8qKlxuICogUmV0dXJuIGEgcmVjdXJzaXZlIGRhdGEgc3RydWN0dXJlIHRoYXQgY2hhaW5zIGRpYWdub3N0aWMgbWVzc2FnZXMuXG4gKiBAcGFyYW0gY2hhaW5cbiAqL1xuZnVuY3Rpb24gY2hhaW5EaWFnbm9zdGljcyhjaGFpbjogbmcuRGlhZ25vc3RpY01lc3NhZ2VDaGFpbik6IHRzLkRpYWdub3N0aWNNZXNzYWdlQ2hhaW4ge1xuICByZXR1cm4ge1xuICAgIG1lc3NhZ2VUZXh0OiBjaGFpbi5tZXNzYWdlLFxuICAgIGNhdGVnb3J5OiB0cy5EaWFnbm9zdGljQ2F0ZWdvcnkuRXJyb3IsXG4gICAgY29kZTogMCxcbiAgICBuZXh0OiBjaGFpbi5uZXh0ID8gY2hhaW4ubmV4dC5tYXAoY2hhaW5EaWFnbm9zdGljcykgOiB1bmRlZmluZWRcbiAgfTtcbn1cblxuLyoqXG4gKiBDb252ZXJ0IG5nLkRpYWdub3N0aWMgdG8gdHMuRGlhZ25vc3RpYy5cbiAqIEBwYXJhbSBkIGRpYWdub3N0aWNcbiAqIEBwYXJhbSBmaWxlXG4gKi9cbmV4cG9ydCBmdW5jdGlvbiBuZ0RpYWdub3N0aWNUb1RzRGlhZ25vc3RpYyhcbiAgICBkOiBuZy5EaWFnbm9zdGljLCBmaWxlOiB0cy5Tb3VyY2VGaWxlIHwgdW5kZWZpbmVkKTogdHMuRGlhZ25vc3RpYyB7XG4gIHJldHVybiB7XG4gICAgZmlsZSxcbiAgICBzdGFydDogZC5zcGFuLnN0YXJ0LFxuICAgIGxlbmd0aDogZC5zcGFuLmVuZCAtIGQuc3Bhbi5zdGFydCxcbiAgICBtZXNzYWdlVGV4dDogdHlwZW9mIGQubWVzc2FnZSA9PT0gJ3N0cmluZycgPyBkLm1lc3NhZ2UgOiBjaGFpbkRpYWdub3N0aWNzKGQubWVzc2FnZSksXG4gICAgY2F0ZWdvcnk6IHRzLkRpYWdub3N0aWNDYXRlZ29yeS5FcnJvcixcbiAgICBjb2RlOiAwLFxuICAgIHNvdXJjZTogJ25nJyxcbiAgfTtcbn1cblxuLyoqXG4gKiBSZXR1cm4gZWxlbWVudHMgZmlsdGVyZWQgYnkgdW5pcXVlIHNwYW4uXG4gKiBAcGFyYW0gZWxlbWVudHNcbiAqL1xuZXhwb3J0IGZ1bmN0aW9uIHVuaXF1ZUJ5U3BhbjxUIGV4dGVuZHN7c3BhbjogbmcuU3Bhbn0+KGVsZW1lbnRzOiBUW10pOiBUW10ge1xuICBjb25zdCByZXN1bHQ6IFRbXSA9IFtdO1xuICBjb25zdCBtYXAgPSBuZXcgTWFwPG51bWJlciwgU2V0PG51bWJlcj4+KCk7XG4gIGZvciAoY29uc3QgZWxlbWVudCBvZiBlbGVtZW50cykge1xuICAgIGNvbnN0IHtzcGFufSA9IGVsZW1lbnQ7XG4gICAgbGV0IHNldCA9IG1hcC5nZXQoc3Bhbi5zdGFydCk7XG4gICAgaWYgKCFzZXQpIHtcbiAgICAgIHNldCA9IG5ldyBTZXQoKTtcbiAgICAgIG1hcC5zZXQoc3Bhbi5zdGFydCwgc2V0KTtcbiAgICB9XG4gICAgaWYgKCFzZXQuaGFzKHNwYW4uZW5kKSkge1xuICAgICAgc2V0LmFkZChzcGFuLmVuZCk7XG4gICAgICByZXN1bHQucHVzaChlbGVtZW50KTtcbiAgICB9XG4gIH1cbiAgcmV0dXJuIHJlc3VsdDtcbn1cbiJdfQ==