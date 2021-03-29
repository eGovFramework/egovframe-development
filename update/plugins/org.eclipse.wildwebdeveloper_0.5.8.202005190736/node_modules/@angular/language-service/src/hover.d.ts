/**
 * @license
 * Copyright Google Inc. All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/// <amd-module name="@angular/language-service/src/hover" />
import * as ts from 'typescript';
import { AstResult } from './common';
import { TypeScriptServiceHost } from './typescript_host';
/**
 * Traverse the template AST and look for the symbol located at `position`, then
 * return the corresponding quick info.
 * @param info template AST
 * @param position location of the symbol
 * @param host Language Service host to query
 */
export declare function getHover(info: AstResult, position: number, host: Readonly<TypeScriptServiceHost>): ts.QuickInfo | undefined;
/**
 * Get quick info for Angular semantic entities in TypeScript files, like Directives.
 * @param sf TypeScript source file an Angular symbol is in
 * @param position location of the symbol in the source file
 * @param host Language Service host to query
 */
export declare function getTsHover(sf: ts.SourceFile, position: number, host: Readonly<TypeScriptServiceHost>): ts.QuickInfo | undefined;
