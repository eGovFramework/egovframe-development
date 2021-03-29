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
        define("@angular/language-service/src/ts_plugin", ["require", "exports", "tslib", "typescript/lib/tsserverlibrary", "@angular/language-service/src/language_service", "@angular/language-service/src/typescript_host"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var tslib_1 = require("tslib");
    var tss = require("typescript/lib/tsserverlibrary");
    var language_service_1 = require("@angular/language-service/src/language_service");
    var typescript_host_1 = require("@angular/language-service/src/typescript_host");
    /**
     * A note about importing TypeScript module.
     * The TypeScript module is supplied by tsserver at runtime to ensure version
     * compatibility. In Angular language service, the rollup output is augmented
     * with a "banner" shim that overwrites 'typescript' and
     * 'typescript/lib/tsserverlibrary' imports with the value supplied by tsserver.
     * This means import of either modules will not be "required", but they'll work
     * just like regular imports.
     */
    var projectHostMap = new WeakMap();
    /**
     * Return the external templates discovered through processing all NgModules in
     * the specified `project`.
     * This function is called in a few situations:
     * 1. When a ConfiguredProject is created
     *    https://github.com/microsoft/TypeScript/blob/c26c44d5fceb04ea14da20b6ed23449df777ff34/src/server/editorServices.ts#L1755
     * 2. When updateGraph() is called on a Project
     *    https://github.com/microsoft/TypeScript/blob/c26c44d5fceb04ea14da20b6ed23449df777ff34/src/server/project.ts#L915
     * @param project Most likely a ConfiguredProject
     */
    function getExternalFiles(project) {
        if (!project.hasRoots()) {
            // During project initialization where there is no root files yet we should
            // not do any work.
            return [];
        }
        var ngLSHost = projectHostMap.get(project);
        if (!ngLSHost) {
            // Without an Angular host there is no way to get template references.
            return [];
        }
        var templates = ngLSHost.getTemplateReferences();
        var logger = project.projectService.logger;
        if (logger.hasLevel(tss.server.LogLevel.verbose)) {
            // Log external files to help debugging.
            logger.info("External files in " + project.projectName + ": " + JSON.stringify(templates));
        }
        return templates;
    }
    exports.getExternalFiles = getExternalFiles;
    function create(info) {
        var project = info.project, tsLS = info.languageService, tsLSHost = info.languageServiceHost, config = info.config;
        // This plugin could operate under two different modes:
        // 1. TS + Angular
        //    Plugin augments TS language service to provide additional Angular
        //    information. This only works with inline templates and is meant to be
        //    used as a local plugin (configured via tsconfig.json)
        // 2. Angular only
        //    Plugin only provides information on Angular templates, no TS info at all.
        //    This effectively disables native TS features and is meant for internal
        //    use only.
        var angularOnly = config ? config.angularOnly === true : false;
        var ngLSHost = new typescript_host_1.TypeScriptServiceHost(tsLSHost, tsLS);
        var ngLS = language_service_1.createLanguageService(ngLSHost);
        projectHostMap.set(project, ngLSHost);
        function getCompletionsAtPosition(fileName, position, options) {
            if (!angularOnly) {
                var results = tsLS.getCompletionsAtPosition(fileName, position, options);
                if (results && results.entries.length) {
                    // If TS could answer the query, then return results immediately.
                    return results;
                }
            }
            return ngLS.getCompletionsAt(fileName, position);
        }
        function getQuickInfoAtPosition(fileName, position) {
            if (!angularOnly) {
                var result = tsLS.getQuickInfoAtPosition(fileName, position);
                if (result) {
                    // If TS could answer the query, then return results immediately.
                    return result;
                }
            }
            return ngLS.getHoverAt(fileName, position);
        }
        function getSemanticDiagnostics(fileName) {
            var results = [];
            if (!angularOnly) {
                results.push.apply(results, tslib_1.__spread(tsLS.getSemanticDiagnostics(fileName)));
            }
            // For semantic diagnostics we need to combine both TS + Angular results
            results.push.apply(results, tslib_1.__spread(ngLS.getDiagnostics(fileName)));
            return results;
        }
        function getDefinitionAtPosition(fileName, position) {
            if (!angularOnly) {
                var results = tsLS.getDefinitionAtPosition(fileName, position);
                if (results) {
                    // If TS could answer the query, then return results immediately.
                    return results;
                }
            }
            var result = ngLS.getDefinitionAt(fileName, position);
            if (!result || !result.definitions || !result.definitions.length) {
                return;
            }
            return result.definitions;
        }
        function getDefinitionAndBoundSpan(fileName, position) {
            if (!angularOnly) {
                var result = tsLS.getDefinitionAndBoundSpan(fileName, position);
                if (result) {
                    // If TS could answer the query, then return results immediately.
                    return result;
                }
            }
            return ngLS.getDefinitionAt(fileName, position);
        }
        var proxy = Object.assign(
        // First clone the original TS language service
        {}, tsLS, 
        // Then override the methods supported by Angular language service
        {
            getCompletionsAtPosition: getCompletionsAtPosition, getQuickInfoAtPosition: getQuickInfoAtPosition, getSemanticDiagnostics: getSemanticDiagnostics,
            getDefinitionAtPosition: getDefinitionAtPosition, getDefinitionAndBoundSpan: getDefinitionAndBoundSpan,
        });
        return proxy;
    }
    exports.create = create;
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidHNfcGx1Z2luLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXMiOlsiLi4vLi4vLi4vLi4vLi4vLi4vcGFja2FnZXMvbGFuZ3VhZ2Utc2VydmljZS9zcmMvdHNfcGx1Z2luLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBOzs7Ozs7R0FNRzs7Ozs7Ozs7Ozs7OztJQUVILG9EQUFzRDtJQUV0RCxtRkFBeUQ7SUFDekQsaUZBQXdEO0lBRXhEOzs7Ozs7OztPQVFHO0lBRUgsSUFBTSxjQUFjLEdBQUcsSUFBSSxPQUFPLEVBQTZDLENBQUM7SUFFaEY7Ozs7Ozs7OztPQVNHO0lBQ0gsU0FBZ0IsZ0JBQWdCLENBQUMsT0FBMkI7UUFDMUQsSUFBSSxDQUFDLE9BQU8sQ0FBQyxRQUFRLEVBQUUsRUFBRTtZQUN2QiwyRUFBMkU7WUFDM0UsbUJBQW1CO1lBQ25CLE9BQU8sRUFBRSxDQUFDO1NBQ1g7UUFDRCxJQUFNLFFBQVEsR0FBRyxjQUFjLENBQUMsR0FBRyxDQUFDLE9BQU8sQ0FBQyxDQUFDO1FBQzdDLElBQUksQ0FBQyxRQUFRLEVBQUU7WUFDYixzRUFBc0U7WUFDdEUsT0FBTyxFQUFFLENBQUM7U0FDWDtRQUNELElBQU0sU0FBUyxHQUFHLFFBQVEsQ0FBQyxxQkFBcUIsRUFBRSxDQUFDO1FBQ25ELElBQU0sTUFBTSxHQUFHLE9BQU8sQ0FBQyxjQUFjLENBQUMsTUFBTSxDQUFDO1FBQzdDLElBQUksTUFBTSxDQUFDLFFBQVEsQ0FBQyxHQUFHLENBQUMsTUFBTSxDQUFDLFFBQVEsQ0FBQyxPQUFPLENBQUMsRUFBRTtZQUNoRCx3Q0FBd0M7WUFDeEMsTUFBTSxDQUFDLElBQUksQ0FBQyx1QkFBcUIsT0FBTyxDQUFDLFdBQVcsVUFBSyxJQUFJLENBQUMsU0FBUyxDQUFDLFNBQVMsQ0FBRyxDQUFDLENBQUM7U0FDdkY7UUFDRCxPQUFPLFNBQVMsQ0FBQztJQUNuQixDQUFDO0lBbEJELDRDQWtCQztJQUVELFNBQWdCLE1BQU0sQ0FBQyxJQUFpQztRQUMvQyxJQUFB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Qyx5QkFBeUIsQ0FBQyxRQUFRLEVBQUUsUUFBUSxDQUFDLENBQUM7Z0JBQ2xFLElBQUksTUFBTSxFQUFFO29CQUNWLGlFQUFpRTtvQkFDakUsT0FBTyxNQUFNLENBQUM7aUJBQ2Y7YUFDRjtZQUNELE9BQU8sSUFBSSxDQUFDLGVBQWUsQ0FBQyxRQUFRLEVBQUUsUUFBUSxDQUFDLENBQUM7UUFDbEQsQ0FBQztRQUVELElBQU0sS0FBSyxHQUF3QixNQUFNLENBQUMsTUFBTTtRQUM1QywrQ0FBK0M7UUFDL0MsRUFBRSxFQUFFLElBQUk7UUFDUixrRUFBa0U7UUFDbEU7WUFDSSx3QkFBd0IsMEJBQUEsRUFBRSxzQkFBc0Isd0JBQUEsRUFBRSxzQkFBc0Isd0JBQUE7WUFDeEUsdUJBQXVCLHlCQUFBLEVBQUUseUJBQXlCLDJCQUFBO1NBQ3JELENBQUMsQ0FBQztRQUNQLE9BQU8sS0FBSyxDQUFDO0lBQ2YsQ0FBQztJQXZGRCx3QkF1RkMiLCJzb3VyY2VzQ29udGVudCI6WyIvKipcbiAqIEBsaWNlbnNlXG4gKiBDb3B5cmlnaHQgR29vZ2xlIEluYy4gQWxsIFJpZ2h0cyBSZXNlcnZlZC5cbiAqXG4gKiBVc2Ugb2YgdGhpcyBzb3VyY2UgY29kZSBpcyBnb3Zlcm5lZCBieSBhbiBNSVQtc3R5bGUgbGljZW5zZSB0aGF0IGNhbiBiZVxuICogZm91bmQgaW4gdGhlIExJQ0VOU0UgZmlsZSBhdCBodHRwczovL2FuZ3VsYXIuaW8vbGljZW5zZVxuICovXG5cbmltcG9ydCAqIGFzIHRzcyBmcm9tICd0eXBlc2NyaXB0L2xpYi90c3NlcnZlcmxpYnJhcnknO1xuXG5pbXBvcnQge2NyZWF0ZUxhbmd1YWdlU2VydmljZX0gZnJvbSAnLi9sYW5ndWFnZV9zZXJ2aWNlJztcbmltcG9ydCB7VHlwZVNjcmlwdFNlcnZpY2VIb3N0fSBmcm9tICcuL3R5cGVzY3JpcHRfaG9zdCc7XG5cbi8qKlxuICogQSBub3RlIGFib3V0IGltcG9ydGluZyBUeXBlU2NyaXB0IG1vZHVsZS5cbiAqIFRoZSBUeXBlU2NyaXB0IG1vZHVsZSBpcyBzdXBwbGllZCBieSB0c3NlcnZlciBhdCBydW50aW1lIHRvIGVuc3VyZSB2ZXJzaW9uXG4gKiBjb21wYXRpYmlsaXR5LiBJbiBBbmd1bGFyIGxhbmd1YWdlIHNlcnZpY2UsIHRoZSByb2xsdXAgb3V0cHV0IGlzIGF1Z21lbnRlZFxuICogd2l0aCBhIFwiYmFubmVyXCIgc2hpbSB0aGF0IG92ZXJ3cml0ZXMgJ3R5cGVzY3JpcHQnIGFuZFxuICogJ3R5cGVzY3JpcHQvbGliL3Rzc2VydmVybGlicmFyeScgaW1wb3J0cyB3aXRoIHRoZSB2YWx1ZSBzdXBwbGllZCBieSB0c3NlcnZlci5cbiAqIFRoaXMgbWVhbnMgaW1wb3J0IG9mIGVpdGhlciBtb2R1bGVzIHdpbGwgbm90IGJlIFwicmVxdWlyZWRcIiwgYnV0IHRoZXknbGwgd29ya1xuICoganVzdCBsaWtlIHJlZ3VsYXIgaW1wb3J0cy5cbiAqL1xuXG5jb25zdCBwcm9qZWN0SG9zdE1hcCA9IG5ldyBXZWFrTWFwPHRzcy5zZXJ2ZXIuUHJvamVjdCwgVHlwZVNjcmlwdFNlcnZpY2VIb3N0PigpO1xuXG4vKipcbiAqIFJldHVybiB0aGUgZXh0ZXJuYWwgdGVtcGxhdGVzIGRpc2NvdmVyZWQgdGhyb3VnaCBwcm9jZXNzaW5nIGFsbCBOZ01vZHVsZXMgaW5cbiAqIHRoZSBzcGVjaWZpZWQgYHByb2plY3RgLlxuICogVGhpcyBmdW5jdGlvbiBpcyBjYWxsZWQgaW4gYSBmZXcgc2l0dWF0aW9uczpcbiAqIDEuIFdoZW4gYSBDb25maWd1cmVkUHJvamVjdCBpcyBjcmVhdGVkXG4gKiAgICBodHRwczovL2dpdGh1Yi5jb20vbWljcm9zb2Z0L1R5cGVTY3JpcHQvYmxvYi9jMjZjNDRkNWZjZWIwNGVhMTRkYTIwYjZlZDIzNDQ5ZGY3NzdmZjM0L3NyYy9zZXJ2ZXIvZWRpdG9yU2VydmljZXMudHMjTDE3NTVcbiAqIDIuIFdoZW4gdXBkYXRlR3JhcGgoKSBpcyBjYWxsZWQgb24gYSBQcm9qZWN0XG4gKiAgICBodHRwczovL2dpdGh1Yi5jb20vbWljcm9zb2Z0L1R5cGVTY3JpcHQvYmxvYi9jMjZjNDRkNWZjZWIwNGVhMTRkYTIwYjZlZDIzNDQ5ZGY3NzdmZjM0L3NyYy9zZXJ2ZXIvcHJvamVjdC50cyNMOTE1XG4gKiBAcGFyYW0gcHJvamVjdCBNb3N0IGxpa2VseSBhIENvbmZpZ3VyZWRQcm9qZWN0XG4gKi9cbmV4cG9ydCBmdW5jdGlvbiBnZXRFeHRlcm5hbEZpbGVzKHByb2plY3Q6IHRzcy5zZXJ2ZXIuUHJvamVjdCk6IHN0cmluZ1tdIHtcbiAgaWYgKCFwcm9qZWN0Lmhhc1Jvb3RzKCkpIHtcbiAgICAvLyBEdXJpbmcgcHJvamVjdCBpbml0aWFsaXphdGlvbiB3aGVyZSB0aGVyZSBpcyBubyByb290IGZpbGVzIHlldCB3ZSBzaG91bGRcbiAgICAvLyBub3QgZG8gYW55IHdvcmsuXG4gICAgcmV0dXJuIFtdO1xuICB9XG4gIGNvbnN0IG5nTFNIb3N0ID0gcHJvamVjdEhvc3RNYXAuZ2V0KHByb2plY3QpO1xuICBpZiAoIW5nTFNIb3N0KSB7XG4gICAgLy8gV2l0aG91dCBhbiBBbmd1bGFyIGhvc3QgdGhlcmUgaXMgbm8gd2F5IHRvIGdldCB0ZW1wbGF0ZSByZWZlcmVuY2VzLlxuICAgIHJldHVybiBbXTtcbiAgfVxuICBjb25zdCB0ZW1wbGF0ZXMgPSBuZ0xTSG9zdC5nZXRUZW1wbGF0ZVJlZmVyZW5jZXMoKTtcbiAgY29uc3QgbG9nZ2VyID0gcHJvamVjdC5wcm9qZWN0U2VydmljZS5sb2dnZXI7XG4gIGlmIChsb2dnZXIuaGFzTGV2ZWwodHNzLnNlcnZlci5Mb2dMZXZlbC52ZXJib3NlKSkge1xuICAgIC8vIExvZyBleHRlcm5hbCBmaWxlcyB0byBoZWxwIGRlYnVnZ2luZy5cbiAgICBsb2dnZXIuaW5mbyhgRXh0ZXJuYWwgZmlsZXMgaW4gJHtwcm9qZWN0LnByb2plY3ROYW1lfTogJHtKU09OLnN0cmluZ2lmeSh0ZW1wbGF0ZXMpfWApO1xuICB9XG4gIHJldHVybiB0ZW1wbGF0ZXM7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBjcmVhdGUoaW5mbzogdHNzLnNlcnZlci5QbHVnaW5DcmVhdGVJbmZvKTogdHNzLkxhbmd1YWdlU2VydmljZSB7XG4gIGNvbnN0IHtwcm9qZWN0LCBsYW5ndWFnZVNlcnZpY2U6IHRzTFMsIGxhbmd1YWdlU2VydmljZUhvc3Q6IHRzTFNIb3N0LCBjb25maWd9ID0gaW5mbztcbiAgLy8gVGhpcyBwbHVnaW4gY291bGQgb3BlcmF0ZSB1bmRlciB0d28gZGlmZmVyZW50IG1vZGVzOlxuICAvLyAxLiBUUyArIEFuZ3VsYXJcbiAgLy8gICAgUGx1Z2luIGF1Z21lbnRzIFRTIGxhbmd1YWdlIHNlcnZpY2UgdG8gcHJvdmlkZSBhZGRpdGlvbmFsIEFuZ3VsYXJcbiAgLy8gICAgaW5mb3JtYXRpb24uIFRoaXMgb25seSB3b3JrcyB3aXRoIGlubGluZSB0ZW1wbGF0ZXMgYW5kIGlzIG1lYW50IHRvIGJlXG4gIC8vICAgIHVzZWQgYXMgYSBsb2NhbCBwbHVnaW4gKGNvbmZpZ3VyZWQgdmlhIHRzY29uZmlnLmpzb24pXG4gIC8vIDIuIEFuZ3VsYXIgb25seVxuICAvLyAgICBQbHVnaW4gb25seSBwcm92aWRlcyBpbmZvcm1hdGlvbiBvbiBBbmd1bGFyIHRlbXBsYXRlcywgbm8gVFMgaW5mbyBhdCBhbGwuXG4gIC8vICAgIFRoaXMgZWZmZWN0aXZlbHkgZGlzYWJsZXMgbmF0aXZlIFRTIGZlYXR1cmVzIGFuZCBpcyBtZWFudCBmb3IgaW50ZXJuYWxcbiAgLy8gICAgdXNlIG9ubHkuXG4gIGNvbnN0IGFuZ3VsYXJPbmx5ID0gY29uZmlnID8gY29uZmlnLmFuZ3VsYXJPbmx5ID09PSB0cnVlIDogZmFsc2U7XG4gIGNvbnN0IG5nTFNIb3N0ID0gbmV3IFR5cGVTY3JpcHRTZXJ2aWNlSG9zdCh0c0xTSG9zdCwgdHNMUyk7XG4gIGNvbnN0IG5nTFMgPSBjcmVhdGVMYW5ndWFnZVNlcnZpY2UobmdMU0hvc3QpO1xuICBwcm9qZWN0SG9zdE1hcC5zZXQocHJvamVjdCwgbmdMU0hvc3QpO1xuXG4gIGZ1bmN0aW9uIGdldENvbXBsZXRpb25zQXRQb3NpdGlvbihcbiAgICAgIGZpbGVOYW1lOiBzdHJpbmcsIHBvc2l0aW9uOiBudW1iZXIsXG4gICAgICBvcHRpb25zOiB0c3MuR2V0Q29tcGxldGlvbnNBdFBvc2l0aW9uT3B0aW9ucyB8IHVuZGVmaW5lZCkge1xuICAgIGlmICghYW5ndWxhck9ubHkpIHtcbiAgICAgIGNvbnN0IHJlc3VsdHMgPSB0c0xTLmdldENvbXBsZXRpb25zQXRQb3NpdGlvbihmaWxlTmFtZSwgcG9zaXRpb24sIG9wdGlvbnMpO1xuICAgICAgaWYgKHJlc3VsdHMgJiYgcmVzdWx0cy5lbnRyaWVzLmxlbmd0aCkge1xuICAgICAgICAvLyBJZiBUUyBjb3VsZCBhbnN3ZXIgdGhlIHF1ZXJ5LCB0aGVuIHJldHVybiByZXN1bHRzIGltbWVkaWF0ZWx5LlxuICAgICAgICByZXR1cm4gcmVzdWx0cztcbiAgICAgIH1cbiAgICB9XG4gICAgcmV0dXJuIG5nTFMuZ2V0Q29tcGxldGlvbnNBdChmaWxlTmFtZSwgcG9zaXRpb24pO1xuICB9XG5cbiAgZnVuY3Rpb24gZ2V0UXVpY2tJbmZvQXRQb3NpdGlvbihmaWxlTmFtZTogc3RyaW5nLCBwb3NpdGlvbjogbnVtYmVyKTogdHNzLlF1aWNrSW5mb3x1bmRlZmluZWQge1xuICAgIGlmICghYW5ndWxhck9ubHkpIHtcbiAgICAgIGNvbnN0IHJlc3VsdCA9IHRzTFMuZ2V0UXVpY2tJbmZvQXRQb3NpdGlvbihmaWxlTmFtZSwgcG9zaXRpb24pO1xuICAgICAgaWYgKHJlc3VsdCkge1xuICAgICAgICAvLyBJZiBUUyBjb3VsZCBhbnN3ZXIgdGhlIHF1ZXJ5LCB0aGVuIHJldHVybiByZXN1bHRzIGltbWVkaWF0ZWx5LlxuICAgICAgICByZXR1cm4gcmVzdWx0O1xuICAgICAgfVxuICAgIH1cbiAgICByZXR1cm4gbmdMUy5nZXRIb3ZlckF0KGZpbGVOYW1lLCBwb3NpdGlvbik7XG4gIH1cblxuICBmdW5jdGlvbiBnZXRTZW1hbnRpY0RpYWdub3N0aWNzKGZpbGVOYW1lOiBzdHJpbmcpOiB0c3MuRGlhZ25vc3RpY1tdIHtcbiAgICBjb25zdCByZXN1bHRzOiB0c3MuRGlhZ25vc3RpY1tdID0gW107XG4gICAgaWYgKCFhbmd1bGFyT25seSkge1xuICAgICAgcmVzdWx0cy5wdXNoKC4uLnRzTFMuZ2V0U2VtYW50aWNEaWFnbm9zdGljcyhmaWxlTmFtZSkpO1xuICAgIH1cbiAgICAvLyBGb3Igc2VtYW50aWMgZGlhZ25vc3RpY3Mgd2UgbmVlZCB0byBjb21iaW5lIGJvdGggVFMgKyBBbmd1bGFyIHJlc3VsdHNcbiAgICByZXN1bHRzLnB1c2goLi4ubmdMUy5nZXREaWFnbm9zdGljcyhmaWxlTmFtZSkpO1xuICAgIHJldHVybiByZXN1bHRzO1xuICB9XG5cbiAgZnVuY3Rpb24gZ2V0RGVmaW5pdGlvbkF0UG9zaXRpb24oXG4gICAgICBmaWxlTmFtZTogc3RyaW5nLCBwb3NpdGlvbjogbnVtYmVyKTogUmVhZG9ubHlBcnJheTx0c3MuRGVmaW5pdGlvbkluZm8+fHVuZGVmaW5lZCB7XG4gICAgaWYgKCFhbmd1bGFyT25seSkge1xuICAgICAgY29uc3QgcmVzdWx0cyA9IHRzTFMuZ2V0RGVmaW5pdGlvbkF0UG9zaXRpb24oZmlsZU5hbWUsIHBvc2l0aW9uKTtcbiAgICAgIGlmIChyZXN1bHRzKSB7XG4gICAgICAgIC8vIElmIFRTIGNvdWxkIGFuc3dlciB0aGUgcXVlcnksIHRoZW4gcmV0dXJuIHJlc3VsdHMgaW1tZWRpYXRlbHkuXG4gICAgICAgIHJldHVybiByZXN1bHRzO1xuICAgICAgfVxuICAgIH1cbiAgICBjb25zdCByZXN1bHQgPSBuZ0xTLmdldERlZmluaXRpb25BdChmaWxlTmFtZSwgcG9zaXRpb24pO1xuICAgIGlmICghcmVzdWx0IHx8ICFyZXN1bHQuZGVmaW5pdGlvbnMgfHwgIXJlc3VsdC5kZWZpbml0aW9ucy5sZW5ndGgpIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG4gICAgcmV0dXJuIHJlc3VsdC5kZWZpbml0aW9ucztcbiAgfVxuXG4gIGZ1bmN0aW9uIGdldERlZmluaXRpb25BbmRCb3VuZFNwYW4oXG4gICAgICBmaWxlTmFtZTogc3RyaW5nLCBwb3NpdGlvbjogbnVtYmVyKTogdHNzLkRlZmluaXRpb25JbmZvQW5kQm91bmRTcGFufHVuZGVmaW5lZCB7XG4gICAgaWYgKCFhbmd1bGFyT25seSkge1xuICAgICAgY29uc3QgcmVzdWx0ID0gdHNMUy5nZXREZWZpbml0aW9uQW5kQm91bmRTcGFuKGZpbGVOYW1lLCBwb3NpdGlvbik7XG4gICAgICBpZiAocmVzdWx0KSB7XG4gICAgICAgIC8vIElmIFRTIGNvdWxkIGFuc3dlciB0aGUgcXVlcnksIHRoZW4gcmV0dXJuIHJlc3VsdHMgaW1tZWRpYXRlbHkuXG4gICAgICAgIHJldHVybiByZXN1bHQ7XG4gICAgICB9XG4gICAgfVxuICAgIHJldHVybiBuZ0xTLmdldERlZmluaXRpb25BdChmaWxlTmFtZSwgcG9zaXRpb24pO1xuICB9XG5cbiAgY29uc3QgcHJveHk6IHRzcy5MYW5ndWFnZVNlcnZpY2UgPSBPYmplY3QuYXNzaWduKFxuICAgICAgLy8gRmlyc3QgY2xvbmUgdGhlIG9yaWdpbmFsIFRTIGxhbmd1YWdlIHNlcnZpY2VcbiAgICAgIHt9LCB0c0xTLFxuICAgICAgLy8gVGhlbiBvdmVycmlkZSB0aGUgbWV0aG9kcyBzdXBwb3J0ZWQgYnkgQW5ndWxhciBsYW5ndWFnZSBzZXJ2aWNlXG4gICAgICB7XG4gICAgICAgICAgZ2V0Q29tcGxldGlvbnNBdFBvc2l0aW9uLCBnZXRRdWlja0luZm9BdFBvc2l0aW9uLCBnZXRTZW1hbnRpY0RpYWdub3N0aWNzLFxuICAgICAgICAgIGdldERlZmluaXRpb25BdFBvc2l0aW9uLCBnZXREZWZpbml0aW9uQW5kQm91bmRTcGFuLFxuICAgICAgfSk7XG4gIHJldHVybiBwcm94eTtcbn1cbiJdfQ==