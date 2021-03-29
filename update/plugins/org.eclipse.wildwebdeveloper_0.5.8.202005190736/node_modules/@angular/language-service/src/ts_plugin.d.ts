/**
 * @license
 * Copyright Google Inc. All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/// <amd-module name="@angular/language-service/src/ts_plugin" />
import * as tss from 'typescript/lib/tsserverlibrary';
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
export declare function getExternalFiles(project: tss.server.Project): string[];
export declare function create(info: tss.server.PluginCreateInfo): tss.LanguageService;
