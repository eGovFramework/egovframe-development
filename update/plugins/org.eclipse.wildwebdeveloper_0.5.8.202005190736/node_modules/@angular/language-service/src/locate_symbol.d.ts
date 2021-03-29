/**
 * @license
 * Copyright Google Inc. All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 */
/// <amd-module name="@angular/language-service/src/locate_symbol" />
import { CompileTypeSummary } from '@angular/compiler';
import { AstResult } from './common';
import { Span, Symbol } from './types';
export interface SymbolInfo {
    symbol: Symbol;
    span: Span;
    compileTypeSummary: CompileTypeSummary | undefined;
}
/**
 * Traverse the template AST and locate the Symbol at the specified `position`.
 * @param info Ast and Template Source
 * @param position location to look for
 */
export declare function locateSymbol(info: AstResult, position: number): SymbolInfo | undefined;
