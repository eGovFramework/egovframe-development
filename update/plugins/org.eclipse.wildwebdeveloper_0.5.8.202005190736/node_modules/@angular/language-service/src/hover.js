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
        define("@angular/language-service/src/hover", ["require", "exports", "tslib", "@angular/compiler", "typescript", "@angular/language-service/src/locate_symbol", "@angular/language-service/src/utils"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var tslib_1 = require("tslib");
    var compiler_1 = require("@angular/compiler");
    var ts = require("typescript");
    var locate_symbol_1 = require("@angular/language-service/src/locate_symbol");
    var utils_1 = require("@angular/language-service/src/utils");
    // Reverse mappings of enum would generate strings
    var SYMBOL_SPACE = ts.SymbolDisplayPartKind[ts.SymbolDisplayPartKind.space];
    var SYMBOL_PUNC = ts.SymbolDisplayPartKind[ts.SymbolDisplayPartKind.punctuation];
    var SYMBOL_CLASS = ts.SymbolDisplayPartKind[ts.SymbolDisplayPartKind.className];
    var SYMBOL_TEXT = ts.SymbolDisplayPartKind[ts.SymbolDisplayPartKind.text];
    /**
     * Traverse the template AST and look for the symbol located at `position`, then
     * return the corresponding quick info.
     * @param info template AST
     * @param position location of the symbol
     * @param host Language Service host to query
     */
    function getHover(info, position, host) {
        var symbolInfo = locate_symbol_1.locateSymbol(info, position);
        if (!symbolInfo) {
            return;
        }
        var symbol = symbolInfo.symbol, span = symbolInfo.span, compileTypeSummary = symbolInfo.compileTypeSummary;
        var textSpan = { start: span.start, length: span.end - span.start };
        if (compileTypeSummary && compileTypeSummary.summaryKind === compiler_1.CompileSummaryKind.Directive) {
            return getDirectiveModule(compileTypeSummary.type.reference, textSpan, host);
        }
        var containerDisplayParts = symbol.container ?
            [
                { text: symbol.container.name, kind: symbol.container.kind },
                { text: '.', kind: SYMBOL_PUNC },
            ] :
            [];
        return {
            kind: symbol.kind,
            kindModifiers: '',
            textSpan: textSpan,
            // this would generate a string like '(property) ClassX.propY'
            // 'kind' in displayParts does not really matter because it's dropped when
            // displayParts get converted to string.
            displayParts: tslib_1.__spread([
                { text: '(', kind: SYMBOL_PUNC }, { text: symbol.kind, kind: symbol.kind },
                { text: ')', kind: SYMBOL_PUNC }, { text: ' ', kind: SYMBOL_SPACE }
            ], containerDisplayParts, [
                { text: symbol.name, kind: symbol.kind },
            ]),
        };
    }
    exports.getHover = getHover;
    /**
     * Get quick info for Angular semantic entities in TypeScript files, like Directives.
     * @param sf TypeScript source file an Angular symbol is in
     * @param position location of the symbol in the source file
     * @param host Language Service host to query
     */
    function getTsHover(sf, position, host) {
        var node = utils_1.findTightestNode(sf, position);
        if (!node)
            return;
        switch (node.kind) {
            case ts.SyntaxKind.Identifier:
                var directiveId = node;
                if (ts.isClassDeclaration(directiveId.parent)) {
                    var directiveName = directiveId.text;
                    var directiveSymbol = host.getStaticSymbol(node.getSourceFile().fileName, directiveName);
                    if (!directiveSymbol)
                        return;
                    return getDirectiveModule(directiveSymbol, { start: directiveId.getStart(), length: directiveId.end - directiveId.getStart() }, host);
                }
                break;
            default:
                break;
        }
        return undefined;
    }
    exports.getTsHover = getTsHover;
    /**
     * Attempts to get quick info for the NgModule a Directive is declared in.
     * @param directive identifier on a potential Directive class declaration
     * @param host Language Service host to query
     */
    function getDirectiveModule(directive, textSpan, host) {
        var analyzedModules = host.getAnalyzedModules(false);
        var ngModule = analyzedModules.ngModuleByPipeOrDirective.get(directive);
        if (!ngModule)
            return;
        var isComponent = host.getDeclarations(directive.filePath)
            .find(function (decl) { return decl.type === directive && decl.metadata && decl.metadata.isComponent; });
        var moduleName = ngModule.type.reference.name;
        return {
            kind: ts.ScriptElementKind.classElement,
            kindModifiers: ts.ScriptElementKindModifier.none,
            textSpan: textSpan,
            // This generates a string like '(directive) NgModule.Directive: class'
            // 'kind' in displayParts does not really matter because it's dropped when
            // displayParts get converted to string.
            displayParts: [
                { text: '(', kind: SYMBOL_PUNC },
                { text: isComponent ? 'component' : 'directive', kind: SYMBOL_TEXT },
                { text: ')', kind: SYMBOL_PUNC },
                { text: ' ', kind: SYMBOL_SPACE },
                { text: moduleName, kind: SYMBOL_CLASS },
                { text: '.', kind: SYMBOL_PUNC },
                { text: directive.name, kind: SYMBOL_CLASS },
                { text: ':', kind: SYMBOL_PUNC },
                { text: ' ', kind: SYMBOL_SPACE },
                { text: ts.ScriptElementKind.classElement, kind: SYMBOL_TEXT },
            ],
        };
    }
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiaG92ZXIuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi8uLi8uLi9wYWNrYWdlcy9sYW5ndWFnZS1zZXJ2aWNlL3NyYy9ob3Zlci50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTs7Ozs7O0dBTUc7Ozs7Ozs7Ozs7Ozs7SUFFSCw4Q0FBbUU7SUFDbkUsK0JBQWlDO0lBRWpDLDZFQUE2QztJQUU3Qyw2REFBeUM7SUFFekMsa0RBQWtEO0lBQ2xELElBQU0sWUFBWSxHQUFHLEVBQUUsQ0FBQyxxQkFBcUIsQ0FBQyxFQUFFLENBQUMscUJBQXFCLENBQUMsS0FBSyxDQUFDLENBQUM7SUFDOUUsSUFBTSxXQUFXLEdBQUcsRUFBRSxDQUFDLHFCQUFxQixDQUFDLEVBQUUsQ0FBQyxxQkFBcUIsQ0FBQyxXQUFXLENBQUMsQ0FBQztJQUNuRixJQUFNLFlBQVksR0FBRyxFQUFFLENBQUMscUJBQXFCLENBQUMsRUFBRSxDQUFDLHFCQUFxQixDQUFDLFNBQVMsQ0FBQyxDQUFDO0lBQ2xGLElBQU0sV0FBVyxHQUFHLEVBQUUsQ0FBQyxxQkFBcUIsQ0FBQyxFQUFFLENBQUMscUJBQXFCLENBQUMsSUFBSSxDQUFDLENBQUM7SUFFNUU7Ozs7OztPQU1HO0lBQ0gsU0FBZ0IsUUFBUSxDQUFDLElBQWUsRUFBRSxRQUFnQixFQUFFLElBQXFDO1FBRS9GLElBQU0sVUFBVSxHQUFHLDRCQUFZLENBQUMsSUFBSSxFQUFFLFFBQVEsQ0FBQyxDQUFDO1FBQ2hELElBQUksQ0FBQyxVQUFVLEVBQUU7WUFDZixPQUFPO1NBQ1I7UUFDTSxJQUFBLDBCQUFNLEVBQUUsc0JBQUksRUFBRSxrREFBa0IsQ0FBZTtRQUN0RCxJQUFNLFFBQVEsR0FBRyxFQUFDLEtBQUssRUFBRSxJQUFJLENBQUMsS0FBSyxFQUFFLE1BQU0sRUFBRSxJQUFJLENBQUMsR0FBRyxHQUFHLElBQUksQ0FBQyxLQUFLLEVBQUMsQ0FBQztRQUVwRSxJQUFJLGtCQUFrQixJQUFJLGtCQUFrQixD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QixFQUFDLElBQUksRUFBRSxFQUFFLENBQUMsaUJBQWlCLENBQUMsWUFBWSxFQUFFLElBQUksRUFBRSxXQUFXLEVBQUM7YUFDN0Q7U0FDRixDQUFDO0lBQ0osQ0FBQyIsInNvdXJjZXNDb250ZW50IjpbIi8qKlxuICogQGxpY2Vuc2VcbiAqIENvcHlyaWdodCBHb29nbGUgSW5jLiBBbGwgUmlnaHRzIFJlc2VydmVkLlxuICpcbiAqIFVzZSBvZiB0aGlzIHNvdXJjZSBjb2RlIGlzIGdvdmVybmVkIGJ5IGFuIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vYW5ndWxhci5pby9saWNlbnNlXG4gKi9cblxuaW1wb3J0IHtDb21waWxlU3VtbWFyeUtpbmQsIFN0YXRpY1N5bWJvbH0gZnJvbSAnQGFuZ3VsYXIvY29tcGlsZXInO1xuaW1wb3J0ICogYXMgdHMgZnJvbSAndHlwZXNjcmlwdCc7XG5pbXBvcnQge0FzdFJlc3VsdH0gZnJvbSAnLi9jb21tb24nO1xuaW1wb3J0IHtsb2NhdGVTeW1ib2x9IGZyb20gJy4vbG9jYXRlX3N5bWJvbCc7XG5pbXBvcnQge1R5cGVTY3JpcHRTZXJ2aWNlSG9zdH0gZnJvbSAnLi90eXBlc2NyaXB0X2hvc3QnO1xuaW1wb3J0IHtmaW5kVGlnaHRlc3ROb2RlfSBmcm9tICcuL3V0aWxzJztcblxuLy8gUmV2ZXJzZSBtYXBwaW5ncyBvZiBlbnVtIHdvdWxkIGdlbmVyYXRlIHN0cmluZ3NcbmNvbnN0IFNZTUJPTF9TUEFDRSA9IHRzLlN5bWJvbERpc3BsYXlQYXJ0S2luZFt0cy5TeW1ib2xEaXNwbGF5UGFydEtpbmQuc3BhY2VdO1xuY29uc3QgU1lNQk9MX1BVTkMgPSB0cy5TeW1ib2xEaXNwbGF5UGFydEtpbmRbdHMuU3ltYm9sRGlzcGxheVBhcnRLaW5kLnB1bmN0dWF0aW9uXTtcbmNvbnN0IFNZTUJPTF9DTEFTUyA9IHRzLlN5bWJvbERpc3BsYXlQYXJ0S2luZFt0cy5TeW1ib2xEaXNwbGF5UGFydEtpbmQuY2xhc3NOYW1lXTtcbmNvbnN0IFNZTUJPTF9URVhUID0gdHMuU3ltYm9sRGlzcGxheVBhcnRLaW5kW3RzLlN5bWJvbERpc3BsYXlQYXJ0S2luZC50ZXh0XTtcblxuLyoqXG4gKiBUcmF2ZXJzZSB0aGUgdGVtcGxhdGUgQVNUIGFuZCBsb29rIGZvciB0aGUgc3ltYm9sIGxvY2F0ZWQgYXQgYHBvc2l0aW9uYCwgdGhlblxuICogcmV0dXJuIHRoZSBjb3JyZXNwb25kaW5nIHF1aWNrIGluZm8uXG4gKiBAcGFyYW0gaW5mbyB0ZW1wbGF0ZSBBU1RcbiAqIEBwYXJhbSBwb3NpdGlvbiBsb2NhdGlvbiBvZiB0aGUgc3ltYm9sXG4gKiBAcGFyYW0gaG9zdCBMYW5ndWFnZSBTZXJ2aWNlIGhvc3QgdG8gcXVlcnlcbiAqL1xuZXhwb3J0IGZ1bmN0aW9uIGdldEhvdmVyKGluZm86IEFzdFJlc3VsdCwgcG9zaXRpb246IG51bWJlciwgaG9zdDogUmVhZG9ubHk8VHlwZVNjcmlwdFNlcnZpY2VIb3N0Pik6XG4gICAgdHMuUXVpY2tJbmZvfHVuZGVmaW5lZCB7XG4gIGNvbnN0IHN5bWJvbEluZm8gPSBsb2NhdGVTeW1ib2woaW5mbywgcG9zaXRpb24pO1xuICBpZiAoIXN5bWJvbEluZm8pIHtcbiAgICByZXR1cm47XG4gIH1cbiAgY29uc3Qge3N5bWJvbCwgc3BhbiwgY29tcGlsZVR5cGVTdW1tYXJ5fSA9IHN5bWJvbEluZm87XG4gIGNvbnN0IHRleHRTcGFuID0ge3N0YXJ0OiBzcGFuLnN0YXJ0LCBsZW5ndGg6IHNwYW4uZW5kIC0gc3Bhbi5zdGFydH07XG5cbiAgaWYgKGNvbXBpbGVUeXBlU3VtbWFyeSAmJiBjb21waWxlVHlwZVN1bW1hcnkuc3VtbWFyeUtpbmQgPT09IENvbXBpbGVTdW1tYXJ5S2luZC5EaXJlY3RpdmUpIHtcbiAgICByZXR1cm4gZ2V0RGlyZWN0aXZlTW9kdWxlKGNvbXBpbGVUeXBlU3VtbWFyeS50eXBlLnJlZmVyZW5jZSwgdGV4dFNwYW4sIGhvc3QpO1xuICB9XG5cbiAgY29uc3QgY29udGFpbmVyRGlzcGxheVBhcnRzOiB0cy5TeW1ib2xEaXNwbGF5UGFydFtdID0gc3ltYm9sLmNvbnRhaW5lciA/XG4gICAgICBbXG4gICAgICAgIHt0ZXh0OiBzeW1ib2wuY29udGFpbmVyLm5hbWUsIGtpbmQ6IHN5bWJvbC5jb250YWluZXIua2luZH0sXG4gICAgICAgIHt0ZXh0OiAnLicsIGtpbmQ6IFNZTUJPTF9QVU5DfSxcbiAgICAgIF0gOlxuICAgICAgW107XG4gIHJldHVybiB7XG4gICAga2luZDogc3ltYm9sLmtpbmQgYXMgdHMuU2NyaXB0RWxlbWVudEtpbmQsXG4gICAga2luZE1vZGlmaWVyczogJycsICAvLyBraW5kTW9kaWZpZXIgaW5mbyBub3QgYXZhaWxhYmxlIG9uICduZy5TeW1ib2wnXG4gICAgdGV4dFNwYW4sXG4gICAgLy8gdGhpcyB3b3VsZCBnZW5lcmF0ZSBhIHN0cmluZyBsaWtlICcocHJvcGVydHkpIENsYXNzWC5wcm9wWSdcbiAgICAvLyAna2luZCcgaW4gZGlzcGxheVBhcnRzIGRvZXMgbm90IHJlYWxseSBtYXR0ZXIgYmVjYXVzZSBpdCdzIGRyb3BwZWQgd2hlblxuICAgIC8vIGRpc3BsYXlQYXJ0cyBnZXQgY29udmVydGVkIHRvIHN0cmluZy5cbiAgICBkaXNwbGF5UGFydHM6IFtcbiAgICAgIHt0ZXh0OiAnKCcsIGtpbmQ6IFNZTUJPTF9QVU5DfSwge3RleHQ6IHN5bWJvbC5raW5kLCBraW5kOiBzeW1ib2wua2luZH0sXG4gICAgICB7dGV4dDogJyknLCBraW5kOiBTWU1CT0xfUFVOQ30sIHt0ZXh0OiAnICcsIGtpbmQ6IFNZTUJPTF9TUEFDRX0sIC4uLmNvbnRhaW5lckRpc3BsYXlQYXJ0cyxcbiAgICAgIHt0ZXh0OiBzeW1ib2wubmFtZSwga2luZDogc3ltYm9sLmtpbmR9LFxuICAgICAgLy8gVE9ETzogQXBwZW5kIHR5cGUgaW5mbyBhcyB3ZWxsLCBidXQgU3ltYm9sIGRvZXNuJ3QgZXhwb3NlIHRoYXQhXG4gICAgICAvLyBJZGVhbGx5IGhvdmVyIHRleHQgc2hvdWxkIGJlIGxpa2UgJyhwcm9wZXJ0eSkgQ2xhc3NYLnByb3BZOiBzdHJpbmcnXG4gICAgXSxcbiAgfTtcbn1cblxuLyoqXG4gKiBHZXQgcXVpY2sgaW5mbyBmb3IgQW5ndWxhciBzZW1hbnRpYyBlbnRpdGllcyBpbiBUeXBlU2NyaXB0IGZpbGVzLCBsaWtlIERpcmVjdGl2ZXMuXG4gKiBAcGFyYW0gc2YgVHlwZVNjcmlwdCBzb3VyY2UgZmlsZSBhbiBBbmd1bGFyIHN5bWJvbCBpcyBpblxuICogQHBhcmFtIHBvc2l0aW9uIGxvY2F0aW9uIG9mIHRoZSBzeW1ib2wgaW4gdGhlIHNvdXJjZSBmaWxlXG4gKiBAcGFyYW0gaG9zdCBMYW5ndWFnZSBTZXJ2aWNlIGhvc3QgdG8gcXVlcnlcbiAqL1xuZXhwb3J0IGZ1bmN0aW9uIGdldFRzSG92ZXIoXG4gICAgc2Y6IHRzLlNvdXJjZUZpbGUsIHBvc2l0aW9uOiBudW1iZXIsIGhvc3Q6IFJlYWRvbmx5PFR5cGVTY3JpcHRTZXJ2aWNlSG9zdD4pOiB0cy5RdWlja0luZm98XG4gICAgdW5kZWZpbmVkIHtcbiAgY29uc3Qgbm9kZSA9IGZpbmRUaWdodGVzdE5vZGUoc2YsIHBvc2l0aW9uKTtcbiAgaWYgKCFub2RlKSByZXR1cm47XG4gIHN3aXRjaCAobm9kZS5raW5kKSB7XG4gICAgY2FzZSB0cy5TeW50YXhLaW5kLklkZW50aWZpZXI6XG4gICAgICBjb25zdCBkaXJlY3RpdmVJZCA9IG5vZGUgYXMgdHMuSWRlbnRpZmllcjtcbiAgICAgIGlmICh0cy5pc0NsYXNzRGVjbGFyYXRpb24oZGlyZWN0aXZlSWQucGFyZW50KSkge1xuICAgICAgICBjb25zdCBkaXJlY3RpdmVOYW1lID0gZGlyZWN0aXZlSWQudGV4dDtcbiAgICAgICAgY29uc3QgZGlyZWN0aXZlU3ltYm9sID0gaG9zdC5nZXRTdGF0aWNTeW1ib2wobm9kZS5nZXRTb3VyY2VGaWxlKCkuZmlsZU5hbWUsIGRpcmVjdGl2ZU5hbWUpO1xuICAgICAgICBpZiAoIWRpcmVjdGl2ZVN5bWJvbCkgcmV0dXJuO1xuICAgICAgICByZXR1cm4gZ2V0RGlyZWN0aXZlTW9kdWxlKFxuICAgICAgICAgICAgZGlyZWN0aXZlU3ltYm9sLFxuICAgICAgICAgICAge3N0YXJ0OiBkaXJlY3RpdmVJZC5nZXRTdGFydCgpLCBsZW5ndGg6IGRpcmVjdGl2ZUlkLmVuZCAtIGRpcmVjdGl2ZUlkLmdldFN0YXJ0KCl9LFxuICAgICAgICAgICAgaG9zdCk7XG4gICAgICB9XG4gICAgICBicmVhaztcbiAgICBkZWZhdWx0OlxuICAgICAgYnJlYWs7XG4gIH1cbiAgcmV0dXJuIHVuZGVmaW5lZDtcbn1cblxuLyoqXG4gKiBBdHRlbXB0cyB0byBnZXQgcXVpY2sgaW5mbyBmb3IgdGhlIE5nTW9kdWxlIGEgRGlyZWN0aXZlIGlzIGRlY2xhcmVkIGluLlxuICogQHBhcmFtIGRpcmVjdGl2ZSBpZGVudGlmaWVyIG9uIGEgcG90ZW50aWFsIERpcmVjdGl2ZSBjbGFzcyBkZWNsYXJhdGlvblxuICogQHBhcmFtIGhvc3QgTGFuZ3VhZ2UgU2VydmljZSBob3N0IHRvIHF1ZXJ5XG4gKi9cbmZ1bmN0aW9uIGdldERpcmVjdGl2ZU1vZHVsZShcbiAgICBkaXJlY3RpdmU6IFN0YXRpY1N5bWJvbCwgdGV4dFNwYW46IHRzLlRleHRTcGFuLFxuICAgIGhvc3Q6IFJlYWRvbmx5PFR5cGVTY3JpcHRTZXJ2aWNlSG9zdD4pOiB0cy5RdWlja0luZm98dW5kZWZpbmVkIHtcbiAgY29uc3QgYW5hbHl6ZWRNb2R1bGVzID0gaG9zdC5nZXRBbmFseXplZE1vZHVsZXMoZmFsc2UpO1xuICBjb25zdCBuZ01vZHVsZSA9IGFuYWx5emVkTW9kdWxlcy5uZ01vZHVsZUJ5UGlwZU9yRGlyZWN0aXZlLmdldChkaXJlY3RpdmUpO1xuICBpZiAoIW5nTW9kdWxlKSByZXR1cm47XG5cbiAgY29uc3QgaXNDb21wb25lbnQgPVxuICAgICAgaG9zdC5nZXREZWNsYXJhdGlvbnMoZGlyZWN0aXZlLmZpbGVQYXRoKVxuICAgICAgICAgIC5maW5kKGRlY2wgPT4gZGVjbC50eXBlID09PSBkaXJlY3RpdmUgJiYgZGVjbC5tZXRhZGF0YSAmJiBkZWNsLm1ldGFkYXRhLmlzQ29tcG9uZW50KTtcblxuICBjb25zdCBtb2R1bGVOYW1lID0gbmdNb2R1bGUudHlwZS5yZWZlcmVuY2UubmFtZTtcbiAgcmV0dXJuIHtcbiAgICBraW5kOiB0cy5TY3JpcHRFbGVtZW50S2luZC5jbGFzc0VsZW1lbnQsXG4gICAga2luZE1vZGlmaWVyczpcbiAgICAgICAgdHMuU2NyaXB0RWxlbWVudEtpbmRNb2RpZmllci5ub25lLCAgLy8ga2luZE1vZGlmaWVyIGluZm8gbm90IGF2YWlsYWJsZSBvbiAnbmcuU3ltYm9sJ1xuICAgIHRleHRTcGFuLFxuICAgIC8vIFRoaXMgZ2VuZXJhdGVzIGEgc3RyaW5nIGxpa2UgJyhkaXJlY3RpdmUpIE5nTW9kdWxlLkRpcmVjdGl2ZTogY2xhc3MnXG4gICAgLy8gJ2tpbmQnIGluIGRpc3BsYXlQYXJ0cyBkb2VzIG5vdCByZWFsbHkgbWF0dGVyIGJlY2F1c2UgaXQncyBkcm9wcGVkIHdoZW5cbiAgICAvLyBkaXNwbGF5UGFydHMgZ2V0IGNvbnZlcnRlZCB0byBzdHJpbmcuXG4gICAgZGlzcGxheVBhcnRzOiBbXG4gICAgICB7dGV4dDogJygnLCBraW5kOiBTWU1CT0xfUFVOQ30sXG4gICAgICB7dGV4dDogaXNDb21wb25lbnQgPyAnY29tcG9uZW50JyA6ICdkaXJlY3RpdmUnLCBraW5kOiBTWU1CT0xfVEVYVH0sXG4gICAgICB7dGV4dDogJyknLCBraW5kOiBTWU1CT0xfUFVOQ30sXG4gICAgICB7dGV4dDogJyAnLCBraW5kOiBTWU1CT0xfU1BBQ0V9LFxuICAgICAge3RleHQ6IG1vZHVsZU5hbWUsIGtpbmQ6IFNZTUJPTF9DTEFTU30sXG4gICAgICB7dGV4dDogJy4nLCBraW5kOiBTWU1CT0xfUFVOQ30sXG4gICAgICB7dGV4dDogZGlyZWN0aXZlLm5hbWUsIGtpbmQ6IFNZTUJPTF9DTEFTU30sXG4gICAgICB7dGV4dDogJzonLCBraW5kOiBTWU1CT0xfUFVOQ30sXG4gICAgICB7dGV4dDogJyAnLCBraW5kOiBTWU1CT0xfU1BBQ0V9LFxuICAgICAge3RleHQ6IHRzLlNjcmlwdEVsZW1lbnRLaW5kLmNsYXNzRWxlbWVudCwga2luZDogU1lNQk9MX1RFWFR9LFxuICAgIF0sXG4gIH07XG59XG4iXX0=