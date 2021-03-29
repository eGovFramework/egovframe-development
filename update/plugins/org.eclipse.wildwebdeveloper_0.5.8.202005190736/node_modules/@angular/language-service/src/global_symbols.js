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
        define("@angular/language-service/src/global_symbols", ["require", "exports", "@angular/language-service/src/types"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var ng = require("@angular/language-service/src/types");
    exports.EMPTY_SYMBOL_TABLE = {
        size: 0,
        get: function () { return undefined; },
        has: function () { return false; },
        values: function () { return []; },
    };
    /**
     * A factory function that returns a symbol table that contains all global symbols
     * available in an interpolation scope in a template.
     * This function creates the table the first time it is called, and return a cached
     * value for all subsequent calls.
     */
    exports.createGlobalSymbolTable = (function () {
        var GLOBAL_SYMBOL_TABLE;
        return function (query) {
            if (GLOBAL_SYMBOL_TABLE) {
                return GLOBAL_SYMBOL_TABLE;
            }
            GLOBAL_SYMBOL_TABLE = query.createSymbolTable([
                // The `$any()` method casts the type of an expression to `any`.
                // https://angular.io/guide/template-syntax#the-any-type-cast-function
                {
                    name: '$any',
                    kind: 'method',
                    type: {
                        name: '$any',
                        kind: 'method',
                        type: undefined,
                        language: 'typescript',
                        container: undefined,
                        public: true,
                        callable: true,
                        definition: undefined,
                        nullable: false,
                        members: function () { return exports.EMPTY_SYMBOL_TABLE; },
                        signatures: function () { return []; },
                        selectSignature: function (args) {
                            if (args.length !== 1) {
                                return;
                            }
                            return {
                                arguments: exports.EMPTY_SYMBOL_TABLE,
                                result: query.getBuiltinType(ng.BuiltinType.Any),
                            };
                        },
                        indexed: function () { return undefined; },
                    },
                },
            ]);
            return GLOBAL_SYMBOL_TABLE;
        };
    })();
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZ2xvYmFsX3N5bWJvbHMuanMiLCJzb3VyY2VSb290IjoiIiwic291cmNlcyI6WyIuLi8uLi8uLi8uLi8uLi8uLi9wYWNrYWdlcy9sYW5ndWFnZS1zZXJ2aWNlL3NyYy9nbG9iYWxfc3ltYm9scy50cyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTs7Ozs7O0dBTUc7Ozs7Ozs7Ozs7OztJQUVILHdEQUFtQztJQUV0QixRQUFBLGtCQUFrQixHQUE2QjtRQUMxRCxJQUFJLEVBQUUsQ0FBQztRQUNQLEdBQUcsRUFBRSxjQUFNLE9BQUEsU0FBUyxFQUFULENBQVM7UUFDcEIsR0FBRyxFQUFFLGNBQU0sT0FBQSxLQUFLLEVBQUwsQ0FBSztRQUNoQixNQUFNLEVBQUUsY0FBTSxPQUFBLEVBQUUsRUFBRixDQUFFO0tBQ2pCLENBQUM7SUFFRjs7Ozs7T0FLRztJQUNVLFFBQUEsdUJBQXVCLEdBQThDLENBQUM7UUFDakYsSUFBSSxtQkFBNkMsQ0FBQztRQUNsRCxPQUFPLFVBQVMsS0FBcUI7WUFDbkMsSUFBSSxtQkFBbUIsRUFBRTtnQkFDdkIsT0FBTyxtQkFBbUIsQ0FBQzthQUM1QjtZQUNELG1CQUFtQixHQUFHLEtBQUssQ0FBQyxpQkFBaUIsQ0FBQztnQkFDNUMsZ0VBQWdFO2dCQUNoRSxzRUFBc0U7Z0JBQ3RFO29CQUNFLElBQUksRUFBRSxNQUFNO29CQUNaLElBQUksRUFBRSxRQUFRO29CQUNkLElBQUksRUFBRTt3QkFDSixJQUFJLEVBQUUsTUFBTTt3QkFDWixJQUFJLEVBQUUsUUFBUTt3QkFDZCxJQUFJLEVBQUUsU0FBUzt3QkFDZixRQUFRLEVBQUUsWUFBWTt3QkFDdEIsU0FBUyxFQUFFLFNBQVM7d0JBQ3BCLE1BQU0sRUFBRSxJQUFJO3dCQUNaLFFBQVEsRUFBRSxJQUFJO3dCQUNkLFVBQVUsRUFBRSxTQUFTO3dCQUNyQixRQUFRLEVBQUUsS0FBSzt3QkFDZixPQUFPLEVBQUUsY0FBTSxPQUFBLDBCQUFrQixFQUFsQixDQUFrQjt3QkFDakMsVUFBVSxFQUFFLGNBQU0sT0FBQSxFQUFFLEVBQUYsQ0FBRTt3QkFDcEIsZUFBZSxFQUFmLFVBQWdCLElBQWlCOzRCQUMvQixJQUFJLElBQUksQ0FBQyxNQUFNLEtBQUssQ0FBQyxFQUFFO2dDQUNyQixPQUFPOzZCQUNSOzRCQUNELE9BQU87Z0NBQ0wsU0FBUyxFQUFFLDBCQUFrQjtnQ0FDN0IsTUFBTSxFQUFFLEtBQUssQ0FBQyxjQUFjLENBQUMsRUFBRSxDQUFDLFdBQVcsQ0FBQyxHQUFHLENBQUM7NkJBQ2pELENBQUM7d0JBQ0osQ0FBQzt3QkFDRCxPQUFPLEVBQUUsY0FBTSxPQUFBLFNBQVMsRUFBVCxDQUFTO3FCQUN6QjtpQkFDRjthQUNGLENBQUMsQ0FBQztZQUNILE9BQU8sbUJBQW1CLENBQUM7UUFDN0IsQ0FBQyxDQUFDO0lBQ0osQ0FBQyxDQUFDLEVBQUUsQ0FBQyIsInNvdXJjZXNDb250ZW50IjpbIi8qKlxuICogQGxpY2Vuc2VcbiAqIENvcHlyaWdodCBHb29nbGUgSW5jLiBBbGwgUmlnaHRzIFJlc2VydmVkLlxuICpcbiAqIFVzZSBvZiB0aGlzIHNvdXJjZSBjb2RlIGlzIGdvdmVybmVkIGJ5IGFuIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vYW5ndWxhci5pby9saWNlbnNlXG4gKi9cblxuaW1wb3J0ICogYXMgbmcgZnJvbSAnLi4vc3JjL3R5cGVzJztcblxuZXhwb3J0IGNvbnN0IEVNUFRZX1NZTUJPTF9UQUJMRTogUmVhZG9ubHk8bmcuU3ltYm9sVGFibGU+ID0ge1xuICBzaXplOiAwLFxuICBnZXQ6ICgpID0+IHVuZGVmaW5lZCxcbiAgaGFzOiAoKSA9PiBmYWxzZSxcbiAgdmFsdWVzOiAoKSA9PiBbXSxcbn07XG5cbi8qKlxuICogQSBmYWN0b3J5IGZ1bmN0aW9uIHRoYXQgcmV0dXJucyBhIHN5bWJvbCB0YWJsZSB0aGF0IGNvbnRhaW5zIGFsbCBnbG9iYWwgc3ltYm9sc1xuICogYXZhaWxhYmxlIGluIGFuIGludGVycG9sYXRpb24gc2NvcGUgaW4gYSB0ZW1wbGF0ZS5cbiAqIFRoaXMgZnVuY3Rpb24gY3JlYXRlcyB0aGUgdGFibGUgdGhlIGZpcnN0IHRpbWUgaXQgaXMgY2FsbGVkLCBhbmQgcmV0dXJuIGEgY2FjaGVkXG4gKiB2YWx1ZSBmb3IgYWxsIHN1YnNlcXVlbnQgY2FsbHMuXG4gKi9cbmV4cG9ydCBjb25zdCBjcmVhdGVHbG9iYWxTeW1ib2xUYWJsZTogKHF1ZXJ5OiBuZy5TeW1ib2xRdWVyeSkgPT4gbmcuU3ltYm9sVGFibGUgPSAoZnVuY3Rpb24oKSB7XG4gIGxldCBHTE9CQUxfU1lNQk9MX1RBQkxFOiBuZy5TeW1ib2xUYWJsZXx1bmRlZmluZWQ7XG4gIHJldHVybiBmdW5jdGlvbihxdWVyeTogbmcuU3ltYm9sUXVlcnkpIHtcbiAgICBpZiAoR0xPQkFMX1NZTUJPTF9UQUJMRSkge1xuICAgICAgcmV0dXJuIEdMT0JBTF9TWU1CT0xfVEFCTEU7XG4gICAgfVxuICAgIEdMT0JBTF9TWU1CT0xfVEFCTEUgPSBxdWVyeS5jcmVhdGVTeW1ib2xUYWJsZShbXG4gICAgICAvLyBUaGUgYCRhbnkoKWAgbWV0aG9kIGNhc3RzIHRoZSB0eXBlIG9mIGFuIGV4cHJlc3Npb24gdG8gYGFueWAuXG4gICAgICAvLyBodHRwczovL2FuZ3VsYXIuaW8vZ3VpZGUvdGVtcGxhdGUtc3ludGF4I3RoZS1hbnktdHlwZS1jYXN0LWZ1bmN0aW9uXG4gICAgICB7XG4gICAgICAgIG5hbWU6ICckYW55JyxcbiAgICAgICAga2luZDogJ21ldGhvZCcsXG4gICAgICAgIHR5cGU6IHtcbiAgICAgICAgICBuYW1lOiAnJGFueScsXG4gICAgICAgICAga2luZDogJ21ldGhvZCcsXG4gICAgICAgICAgdHlwZTogdW5kZWZpbmVkLFxuICAgICAgICAgIGxhbmd1YWdlOiAndHlwZXNjcmlwdCcsXG4gICAgICAgICAgY29udGFpbmVyOiB1bmRlZmluZWQsXG4gICAgICAgICAgcHVibGljOiB0cnVlLFxuICAgICAgICAgIGNhbGxhYmxlOiB0cnVlLFxuICAgICAgICAgIGRlZmluaXRpb246IHVuZGVmaW5lZCxcbiAgICAgICAgICBudWxsYWJsZTogZmFsc2UsXG4gICAgICAgICAgbWVtYmVyczogKCkgPT4gRU1QVFlfU1lNQk9MX1RBQkxFLFxuICAgICAgICAgIHNpZ25hdHVyZXM6ICgpID0+IFtdLFxuICAgICAgICAgIHNlbGVjdFNpZ25hdHVyZShhcmdzOiBuZy5TeW1ib2xbXSkge1xuICAgICAgICAgICAgaWYgKGFyZ3MubGVuZ3RoICE9PSAxKSB7XG4gICAgICAgICAgICAgIHJldHVybjtcbiAgICAgICAgICAgIH1cbiAgICAgICAgICAgIHJldHVybiB7XG4gICAgICAgICAgICAgIGFyZ3VtZW50czogRU1QVFlfU1lNQk9MX1RBQkxFLCAgLy8gbm90IHVzZWRcbiAgICAgICAgICAgICAgcmVzdWx0OiBxdWVyeS5nZXRCdWlsdGluVHlwZShuZy5CdWlsdGluVHlwZS5BbnkpLFxuICAgICAgICAgICAgfTtcbiAgICAgICAgICB9LFxuICAgICAgICAgIGluZGV4ZWQ6ICgpID0+IHVuZGVmaW5lZCxcbiAgICAgICAgfSxcbiAgICAgIH0sXG4gICAgXSk7XG4gICAgcmV0dXJuIEdMT0JBTF9TWU1CT0xfVEFCTEU7XG4gIH07XG59KSgpO1xuIl19