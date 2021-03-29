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
        define("@angular/language-service/src/definitions", ["require", "exports", "path", "typescript", "@angular/language-service/src/locate_symbol", "@angular/language-service/src/template", "@angular/language-service/src/utils"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var path = require("path");
    var ts = require("typescript"); // used as value and is provided at runtime
    var locate_symbol_1 = require("@angular/language-service/src/locate_symbol");
    var template_1 = require("@angular/language-service/src/template");
    var utils_1 = require("@angular/language-service/src/utils");
    /**
     * Convert Angular Span to TypeScript TextSpan. Angular Span has 'start' and
     * 'end' whereas TS TextSpan has 'start' and 'length'.
     * @param span Angular Span
     */
    function ngSpanToTsTextSpan(span) {
        return {
            start: span.start,
            length: span.end - span.start,
        };
    }
    /**
     * Traverse the template AST and look for the symbol located at `position`, then
     * return its definition and span of bound text.
     * @param info
     * @param position
     */
    function getDefinitionAndBoundSpan(info, position) {
        var symbolInfo = locate_symbol_1.locateSymbol(info, position);
        if (!symbolInfo) {
            return;
        }
        var textSpan = ngSpanToTsTextSpan(symbolInfo.span);
        var symbol = symbolInfo.symbol;
        var container = symbol.container, locations = symbol.definition;
        if (!locations || !locations.length) {
            // symbol.definition is really the locations of the symbol. There could be
            // more than one. No meaningful info could be provided without any location.
            return { textSpan: textSpan };
        }
        var containerKind = container ? container.kind : ts.ScriptElementKind.unknown;
        var containerName = container ? container.name : '';
        var definitions = locations.map(function (location) {
            return {
                kind: symbol.kind,
                name: symbol.name,
                containerKind: containerKind,
                containerName: containerName,
                textSpan: ngSpanToTsTextSpan(location.span),
                fileName: location.fileName,
            };
        });
        return {
            definitions: definitions, textSpan: textSpan,
        };
    }
    exports.getDefinitionAndBoundSpan = getDefinitionAndBoundSpan;
    /**
     * Gets an Angular-specific definition in a TypeScript source file.
     */
    function getTsDefinitionAndBoundSpan(sf, position, tsLsHost) {
        var node = utils_1.findTightestNode(sf, position);
        if (!node)
            return;
        switch (node.kind) {
            case ts.SyntaxKind.StringLiteral:
            case ts.SyntaxKind.NoSubstitutionTemplateLiteral:
                // Attempt to extract definition of a URL in a property assignment.
                return getUrlFromProperty(node, tsLsHost);
            default:
                return undefined;
        }
    }
    exports.getTsDefinitionAndBoundSpan = getTsDefinitionAndBoundSpan;
    /**
     * Attempts to get the definition of a file whose URL is specified in a property assignment in a
     * directive decorator.
     * Currently applies to `templateUrl` and `styleUrls` properties.
     */
    function getUrlFromProperty(urlNode, tsLsHost) {
        // Get the property assignment node corresponding to the `templateUrl` or `styleUrls` assignment.
        // These assignments are specified differently; `templateUrl` is a string, and `styleUrls` is
        // an array of strings:
        //   {
        //        templateUrl: './template.ng.html',
        //        styleUrls: ['./style.css', './other-style.css']
        //   }
        // `templateUrl`'s property assignment can be found from the string literal node;
        // `styleUrls`'s property assignment can be found from the array (parent) node.
        //
        // First search for `templateUrl`.
        var asgn = template_1.getPropertyAssignmentFromValue(urlNode);
        if (!asgn || asgn.name.getText() !== 'templateUrl') {
            // `templateUrl` assignment not found; search for `styleUrls` array assignment.
            asgn = template_1.getPropertyAssignmentFromValue(urlNode.parent);
            if (!asgn || asgn.name.getText() !== 'styleUrls') {
                // Nothing found, bail.
                return;
            }
        }
        // If the property assignment is not a property of a class decorator, don't generate definitions
        // for it.
        if (!template_1.isClassDecoratorProperty(asgn))
            return;
        var sf = urlNode.getSourceFile();
        // Extract url path specified by the url node, which is relative to the TypeScript source file
        // the url node is defined in.
        var url = path.join(path.dirname(sf.fileName), urlNode.text);
        // If the file does not exist, bail. It is possible that the TypeScript language service host
        // does not have a `fileExists` method, in which case optimistically assume the file exists.
        if (tsLsHost.fileExists && !tsLsHost.fileExists(url))
            return;
        var templateDefinitions = [{
                kind: ts.ScriptElementKind.externalModuleName,
                name: url,
                containerKind: ts.ScriptElementKind.unknown,
                containerName: '',
                // Reading the template is expensive, so don't provide a preview.
                textSpan: { start: 0, length: 0 },
                fileName: url,
            }];
        return {
            definitions: templateDefinitions,
            textSpan: {
                // Exclude opening and closing quotes in the url span.
                start: urlNode.getStart() + 1,
                length: urlNode.getWidth() - 2,
            },
        };
    }
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZGVmaW5pdGlvbnMuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi8uLi8uLi9wYWNrYWdlcy9sYW5ndWFnZS1zZXJ2aWNlL3NyYy9kZWZpbml0aW9ucy50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTs7Ozs7O0dBTUc7Ozs7Ozs7Ozs7OztJQUVILDJCQUE2QjtJQUM3QiwrQkFBaUMsQ0FBQywyQ0FBMkM7SUFFN0UsNkVBQTZDO0lBQzdDLG1FQUFvRjtJQUVwRiw2REFBeUM7SUFFekM7Ozs7T0FJRztJQUNILFNBQVMsa0JBQWtCLENBQUMsSUFBVTtRQUNwQyxPQUFPO1lBQ0wsS0FBSyxFQUFFLElBQUksQ0FBQyxLQUFLO1lBQ2pCLE1BQU0sRUFBRSxJQUFJLENBQUMsR0FBRyxHQUFHLElBQUksQ0FBQyxLQUFLO1NBQzlCLENBQUM7SUFDSixDQUFDO0lBRUQ7Ozs7O09BS0c7SUFDSCxTQUFnQix5QkFBeUIsQ0FDckMsSUFBZSxFQUFFLFFBQWdCO1FBQ25DLElBQU0sVUFBVSxHQUFHLDRCQUFZLENBQUMsSUFBSSxFQUFFLFFBQVEsQ0FBQyxDQUFDO1FBQ2hELElBQUksQ0FBQyxVQUFVLEVBQUU7WUFDZixPQUFPO1NBQ1I7UUFDRCxJQUFNLFFBQVEsR0FBRyxrQkFBa0IsQ0FBQyxVQUFVLENBQUMsSUFBSSxDQUFDLENBQUM7UUFDOUMsSUFBQSwwQkFBTSxDQUFlO1FBQ3JCLElBQUEsNEJBQVMsRUFBRSw2QkFBcUIsQ0FBVztRQUNsRCxJQUFJLENBQUMsU0FBUyxJQUFJLENBQUMsU0FBUyxDQUFDLE1BQU0sRUFBRTtZQUNuQywwRUFBMEU7WUFDMUUsNEVBQTRFO1lBQzVFLE9BQU8sRUFBQyxRQUFRLFVBQUEsRUFBQyxDQUFDO1NBQ25CO1FBQ0QsSUFBTSxhQUFhLEdBQUcsU0FBUyxDQUFDLENBQUMsQ0FBQyxTQUFTLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQyxFQUFFLENBQUMsaUJBQWlCLENBQUMsT0FBTyxDQUFDO1FBQ2hG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LEVBQUUsRUFBRTtnQkFDakIsaUVBQWlFO2dCQUNqRSxRQUFRLEVBQUUsRUFBQyxLQUFLLEVBQUUsQ0FBQyxFQUFFLE1BQU0sRUFBRSxDQUFDLEVBQUM7Z0JBQy9CLFFBQVEsRUFBRSxHQUFHO2FBQ2QsQ0FBQyxDQUFDO1FBRUgsT0FBTztZQUNMLFdBQVcsRUFBRSxtQkFBbUI7WUFDaEMsUUFBUSxFQUFFO2dCQUNSLHNEQUFzRDtnQkFDdEQsS0FBSyxFQUFFLE9BQU8sQ0FBQyxRQUFRLEVBQUUsR0FBRyxDQUFDO2dCQUM3QixNQUFNLEVBQUUsT0FBTyxDQUFDLFFBQVEsRUFBRSxHQUFHLENBQUM7YUFDL0I7U0FDRixDQUFDO0lBQ0osQ0FBQyIsInNvdXJjZXNDb250ZW50IjpbIi8qKlxuICogQGxpY2Vuc2VcbiAqIENvcHlyaWdodCBHb29nbGUgSW5jLiBBbGwgUmlnaHRzIFJlc2VydmVkLlxuICpcbiAqIFVzZSBvZiB0aGlzIHNvdXJjZSBjb2RlIGlzIGdvdmVybmVkIGJ5IGFuIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vYW5ndWxhci5pby9saWNlbnNlXG4gKi9cblxuaW1wb3J0ICogYXMgcGF0aCBmcm9tICdwYXRoJztcbmltcG9ydCAqIGFzIHRzIGZyb20gJ3R5cGVzY3JpcHQnOyAvLyB1c2VkIGFzIHZhbHVlIGFuZCBpcyBwcm92aWRlZCBhdCBydW50aW1lXG5pbXBvcnQge0FzdFJlc3VsdH0gZnJvbSAnLi9jb21tb24nO1xuaW1wb3J0IHtsb2NhdGVTeW1ib2x9IGZyb20gJy4vbG9jYXRlX3N5bWJvbCc7XG5pbXBvcnQge2dldFByb3BlcnR5QXNzaWdubWVudEZyb21WYWx1ZSwgaXNDbGFzc0RlY29yYXRvclByb3BlcnR5fSBmcm9tICcuL3RlbXBsYXRlJztcbmltcG9ydCB7U3BhbiwgVGVtcGxhdGVTb3VyY2V9IGZyb20gJy4vdHlwZXMnO1xuaW1wb3J0IHtmaW5kVGlnaHRlc3ROb2RlfSBmcm9tICcuL3V0aWxzJztcblxuLyoqXG4gKiBDb252ZXJ0IEFuZ3VsYXIgU3BhbiB0byBUeXBlU2NyaXB0IFRleHRTcGFuLiBBbmd1bGFyIFNwYW4gaGFzICdzdGFydCcgYW5kXG4gKiAnZW5kJyB3aGVyZWFzIFRTIFRleHRTcGFuIGhhcyAnc3RhcnQnIGFuZCAnbGVuZ3RoJy5cbiAqIEBwYXJhbSBzcGFuIEFuZ3VsYXIgU3BhblxuICovXG5mdW5jdGlvbiBuZ1NwYW5Ub1RzVGV4dFNwYW4oc3BhbjogU3Bhbik6IHRzLlRleHRTcGFuIHtcbiAgcmV0dXJuIHtcbiAgICBzdGFydDogc3Bhbi5zdGFydCxcbiAgICBsZW5ndGg6IHNwYW4uZW5kIC0gc3Bhbi5zdGFydCxcbiAgfTtcbn1cblxuLyoqXG4gKiBUcmF2ZXJzZSB0aGUgdGVtcGxhdGUgQVNUIGFuZCBsb29rIGZvciB0aGUgc3ltYm9sIGxvY2F0ZWQgYXQgYHBvc2l0aW9uYCwgdGhlblxuICogcmV0dXJuIGl0cyBkZWZpbml0aW9uIGFuZCBzcGFuIG9mIGJvdW5kIHRleHQuXG4gKiBAcGFyYW0gaW5mb1xuICogQHBhcmFtIHBvc2l0aW9uXG4gKi9cbmV4cG9ydCBmdW5jdGlvbiBnZXREZWZpbml0aW9uQW5kQm91bmRTcGFuKFxuICAgIGluZm86IEFzdFJlc3VsdCwgcG9zaXRpb246IG51bWJlcik6IHRzLkRlZmluaXRpb25JbmZvQW5kQm91bmRTcGFufHVuZGVmaW5lZCB7XG4gIGNvbnN0IHN5bWJvbEluZm8gPSBsb2NhdGVTeW1ib2woaW5mbywgcG9zaXRpb24pO1xuICBpZiAoIXN5bWJvbEluZm8pIHtcbiAgICByZXR1cm47XG4gIH1cbiAgY29uc3QgdGV4dFNwYW4gPSBuZ1NwYW5Ub1RzVGV4dFNwYW4oc3ltYm9sSW5mby5zcGFuKTtcbiAgY29uc3Qge3N5bWJvbH0gPSBzeW1ib2xJbmZvO1xuICBjb25zdCB7Y29udGFpbmVyLCBkZWZpbml0aW9uOiBsb2NhdGlvbnN9ID0gc3ltYm9sO1xuICBpZiAoIWxvY2F0aW9ucyB8fCAhbG9jYXRpb25zLmxlbmd0aCkge1xuICAgIC8vIHN5bWJvbC5kZWZpbml0aW9uIGlzIHJlYWxseSB0aGUgbG9jYXRpb25zIG9mIHRoZSBzeW1ib2wuIFRoZXJlIGNvdWxkIGJlXG4gICAgLy8gbW9yZSB0aGFuIG9uZS4gTm8gbWVhbmluZ2Z1bCBpbmZvIGNvdWxkIGJlIHByb3ZpZGVkIHdpdGhvdXQgYW55IGxvY2F0aW9uLlxuICAgIHJldHVybiB7dGV4dFNwYW59O1xuICB9XG4gIGNvbnN0IGNvbnRhaW5lcktpbmQgPSBjb250YWluZXIgPyBjb250YWluZXIua2luZCA6IHRzLlNjcmlwdEVsZW1lbnRLaW5kLnVua25vd247XG4gIGNvbnN0IGNvbnRhaW5lck5hbWUgPSBjb250YWluZXIgPyBjb250YWluZXIubmFtZSA6ICcnO1xuICBjb25zdCBkZWZpbml0aW9ucyA9IGxvY2F0aW9ucy5tYXAoKGxvY2F0aW9uKSA9PiB7XG4gICAgcmV0dXJuIHtcbiAgICAgIGtpbmQ6IHN5bWJvbC5raW5kIGFzIHRzLlNjcmlwdEVsZW1lbnRLaW5kLFxuICAgICAgbmFtZTogc3ltYm9sLm5hbWUsXG4gICAgICBjb250YWluZXJLaW5kOiBjb250YWluZXJLaW5kIGFzIHRzLlNjcmlwdEVsZW1lbnRLaW5kLFxuICAgICAgY29udGFpbmVyTmFtZTogY29udGFpbmVyTmFtZSxcbiAgICAgIHRleHRTcGFuOiBuZ1NwYW5Ub1RzVGV4dFNwYW4obG9jYXRpb24uc3BhbiksXG4gICAgICBmaWxlTmFtZTogbG9jYXRpb24uZmlsZU5hbWUsXG4gICAgfTtcbiAgfSk7XG4gIHJldHVybiB7XG4gICAgICBkZWZpbml0aW9ucywgdGV4dFNwYW4sXG4gIH07XG59XG5cbi8qKlxuICogR2V0cyBhbiBBbmd1bGFyLXNwZWNpZmljIGRlZmluaXRpb24gaW4gYSBUeXBlU2NyaXB0IHNvdXJjZSBmaWxlLlxuICovXG5leHBvcnQgZnVuY3Rpb24gZ2V0VHNEZWZpbml0aW9uQW5kQm91bmRTcGFuKFxuICAgIHNmOiB0cy5Tb3VyY2VGaWxlLCBwb3NpdGlvbjogbnVtYmVyLFxuICAgIHRzTHNIb3N0OiBSZWFkb25seTx0cy5MYW5ndWFnZVNlcnZpY2VIb3N0Pik6IHRzLkRlZmluaXRpb25JbmZvQW5kQm91bmRTcGFufHVuZGVmaW5lZCB7XG4gIGNvbnN0IG5vZGUgPSBmaW5kVGlnaHRlc3ROb2RlKHNmLCBwb3NpdGlvbik7XG4gIGlmICghbm9kZSkgcmV0dXJuO1xuICBzd2l0Y2ggKG5vZGUua2luZCkge1xuICAgIGNhc2UgdHMuU3ludGF4S2luZC5TdHJpbmdMaXRlcmFsOlxuICAgIGNhc2UgdHMuU3ludGF4S2luZC5Ob1N1YnN0aXR1dGlvblRlbXBsYXRlTGl0ZXJhbDpcbiAgICAgIC8vIEF0dGVtcHQgdG8gZXh0cmFjdCBkZWZpbml0aW9uIG9mIGEgVVJMIGluIGEgcHJvcGVydHkgYXNzaWdubWVudC5cbiAgICAgIHJldHVybiBnZXRVcmxGcm9tUHJvcGVydHkobm9kZSBhcyB0cy5TdHJpbmdMaXRlcmFsTGlrZSwgdHNMc0hvc3QpO1xuICAgIGRlZmF1bHQ6XG4gICAgICByZXR1cm4gdW5kZWZpbmVkO1xuICB9XG59XG5cbi8qKlxuICogQXR0ZW1wdHMgdG8gZ2V0IHRoZSBkZWZpbml0aW9uIG9mIGEgZmlsZSB3aG9zZSBVUkwgaXMgc3BlY2lmaWVkIGluIGEgcHJvcGVydHkgYXNzaWdubWVudCBpbiBhXG4gKiBkaXJlY3RpdmUgZGVjb3JhdG9yLlxuICogQ3VycmVudGx5IGFwcGxpZXMgdG8gYHRlbXBsYXRlVXJsYCBhbmQgYHN0eWxlVXJsc2AgcHJvcGVydGllcy5cbiAqL1xuZnVuY3Rpb24gZ2V0VXJsRnJvbVByb3BlcnR5KFxuICAgIHVybE5vZGU6IHRzLlN0cmluZ0xpdGVyYWxMaWtlLFxuICAgIHRzTHNIb3N0OiBSZWFkb25seTx0cy5MYW5ndWFnZVNlcnZpY2VIb3N0Pik6IHRzLkRlZmluaXRpb25JbmZvQW5kQm91bmRTcGFufHVuZGVmaW5lZCB7XG4gIC8vIEdldCB0aGUgcHJvcGVydHkgYXNzaWdubWVudCBub2RlIGNvcnJlc3BvbmRpbmcgdG8gdGhlIGB0ZW1wbGF0ZVVybGAgb3IgYHN0eWxlVXJsc2AgYXNzaWdubWVudC5cbiAgLy8gVGhlc2UgYXNzaWdubWVudHMgYXJlIHNwZWNpZmllZCBkaWZmZXJlbnRseTsgYHRlbXBsYXRlVXJsYCBpcyBhIHN0cmluZywgYW5kIGBzdHlsZVVybHNgIGlzXG4gIC8vIGFuIGFycmF5IG9mIHN0cmluZ3M6XG4gIC8vICAge1xuICAvLyAgICAgICAgdGVtcGxhdGVVcmw6ICcuL3RlbXBsYXRlLm5nLmh0bWwnLFxuICAvLyAgICAgICAgc3R5bGVVcmxzOiBbJy4vc3R5bGUuY3NzJywgJy4vb3RoZXItc3R5bGUuY3NzJ11cbiAgLy8gICB9XG4gIC8vIGB0ZW1wbGF0ZVVybGAncyBwcm9wZXJ0eSBhc3NpZ25tZW50IGNhbiBiZSBmb3VuZCBmcm9tIHRoZSBzdHJpbmcgbGl0ZXJhbCBub2RlO1xuICAvLyBgc3R5bGVVcmxzYCdzIHByb3BlcnR5IGFzc2lnbm1lbnQgY2FuIGJlIGZvdW5kIGZyb20gdGhlIGFycmF5IChwYXJlbnQpIG5vZGUuXG4gIC8vXG4gIC8vIEZpcnN0IHNlYXJjaCBmb3IgYHRlbXBsYXRlVXJsYC5cbiAgbGV0IGFzZ24gPSBnZXRQcm9wZXJ0eUFzc2lnbm1lbnRGcm9tVmFsdWUodXJsTm9kZSk7XG4gIGlmICghYXNnbiB8fCBhc2duLm5hbWUuZ2V0VGV4dCgpICE9PSAndGVtcGxhdGVVcmwnKSB7XG4gICAgLy8gYHRlbXBsYXRlVXJsYCBhc3NpZ25tZW50IG5vdCBmb3VuZDsgc2VhcmNoIGZvciBgc3R5bGVVcmxzYCBhcnJheSBhc3NpZ25tZW50LlxuICAgIGFzZ24gPSBnZXRQcm9wZXJ0eUFzc2lnbm1lbnRGcm9tVmFsdWUodXJsTm9kZS5wYXJlbnQpO1xuICAgIGlmICghYXNnbiB8fCBhc2duLm5hbWUuZ2V0VGV4dCgpICE9PSAnc3R5bGVVcmxzJykge1xuICAgICAgLy8gTm90aGluZyBmb3VuZCwgYmFpbC5cbiAgICAgIHJldHVybjtcbiAgICB9XG4gIH1cblxuICAvLyBJZiB0aGUgcHJvcGVydHkgYXNzaWdubWVudCBpcyBub3QgYSBwcm9wZXJ0eSBvZiBhIGNsYXNzIGRlY29yYXRvciwgZG9uJ3QgZ2VuZXJhdGUgZGVmaW5pdGlvbnNcbiAgLy8gZm9yIGl0LlxuICBpZiAoIWlzQ2xhc3NEZWNvcmF0b3JQcm9wZXJ0eShhc2duKSkgcmV0dXJuO1xuXG4gIGNvbnN0IHNmID0gdXJsTm9kZS5nZXRTb3VyY2VGaWxlKCk7XG4gIC8vIEV4dHJhY3QgdXJsIHBhdGggc3BlY2lmaWVkIGJ5IHRoZSB1cmwgbm9kZSwgd2hpY2ggaXMgcmVsYXRpdmUgdG8gdGhlIFR5cGVTY3JpcHQgc291cmNlIGZpbGVcbiAgLy8gdGhlIHVybCBub2RlIGlzIGRlZmluZWQgaW4uXG4gIGNvbnN0IHVybCA9IHBhdGguam9pbihwYXRoLmRpcm5hbWUoc2YuZmlsZU5hbWUpLCB1cmxOb2RlLnRleHQpO1xuXG4gIC8vIElmIHRoZSBmaWxlIGRvZXMgbm90IGV4aXN0LCBiYWlsLiBJdCBpcyBwb3NzaWJsZSB0aGF0IHRoZSBUeXBlU2NyaXB0IGxhbmd1YWdlIHNlcnZpY2UgaG9zdFxuICAvLyBkb2VzIG5vdCBoYXZlIGEgYGZpbGVFeGlzdHNgIG1ldGhvZCwgaW4gd2hpY2ggY2FzZSBvcHRpbWlzdGljYWxseSBhc3N1bWUgdGhlIGZpbGUgZXhpc3RzLlxuICBpZiAodHNMc0hvc3QuZmlsZUV4aXN0cyAmJiAhdHNMc0hvc3QuZmlsZUV4aXN0cyh1cmwpKSByZXR1cm47XG5cbiAgY29uc3QgdGVtcGxhdGVEZWZpbml0aW9uczogdHMuRGVmaW5pdGlvbkluZm9bXSA9IFt7XG4gICAga2luZDogdHMuU2NyaXB0RWxlbWVudEtpbmQuZXh0ZXJuYWxNb2R1bGVOYW1lLFxuICAgIG5hbWU6IHVybCxcbiAgICBjb250YWluZXJLaW5kOiB0cy5TY3JpcHRFbGVtZW50S2luZC51bmtub3duLFxuICAgIGNvbnRhaW5lck5hbWU6ICcnLFxuICAgIC8vIFJlYWRpbmcgdGhlIHRlbXBsYXRlIGlzIGV4cGVuc2l2ZSwgc28gZG9uJ3QgcHJvdmlkZSBhIHByZXZpZXcuXG4gICAgdGV4dFNwYW46IHtzdGFydDogMCwgbGVuZ3RoOiAwfSxcbiAgICBmaWxlTmFtZTogdXJsLFxuICB9XTtcblxuICByZXR1cm4ge1xuICAgIGRlZmluaXRpb25zOiB0ZW1wbGF0ZURlZmluaXRpb25zLFxuICAgIHRleHRTcGFuOiB7XG4gICAgICAvLyBFeGNsdWRlIG9wZW5pbmcgYW5kIGNsb3NpbmcgcXVvdGVzIGluIHRoZSB1cmwgc3Bhbi5cbiAgICAgIHN0YXJ0OiB1cmxOb2RlLmdldFN0YXJ0KCkgKyAxLFxuICAgICAgbGVuZ3RoOiB1cmxOb2RlLmdldFdpZHRoKCkgLSAyLFxuICAgIH0sXG4gIH07XG59XG4iXX0=