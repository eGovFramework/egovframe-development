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
        define("@angular/language-service/src/common", ["require", "exports"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    function isAstResult(result) {
        return result.hasOwnProperty('templateAst');
    }
    exports.isAstResult = isAstResult;
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY29tbW9uLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXMiOlsiLi4vLi4vLi4vLi4vLi4vLi4vcGFja2FnZXMvbGFuZ3VhZ2Utc2VydmljZS9zcmMvY29tbW9uLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBOzs7Ozs7R0FNRzs7Ozs7Ozs7Ozs7O0lBc0JILFNBQWdCLFdBQVcsQ0FBQyxNQUE4QjtRQUN4RCxPQUFPLE1BQU0sQ0FBQyxjQUFjLENBQUMsYUFBYSxDQUFDLENBQUM7SUFDOUMsQ0FBQztJQUZELGtDQUVDIiwic291cmNlc0NvbnRlbnQiOlsiLyoqXG4gKiBAbGljZW5zZVxuICogQ29weXJpZ2h0IEdvb2dsZSBJbmMuIEFsbCBSaWdodHMgUmVzZXJ2ZWQuXG4gKlxuICogVXNlIG9mIHRoaXMgc291cmNlIGNvZGUgaXMgZ292ZXJuZWQgYnkgYW4gTUlULXN0eWxlIGxpY2Vuc2UgdGhhdCBjYW4gYmVcbiAqIGZvdW5kIGluIHRoZSBMSUNFTlNFIGZpbGUgYXQgaHR0cHM6Ly9hbmd1bGFyLmlvL2xpY2Vuc2VcbiAqL1xuXG5pbXBvcnQge0NvbXBpbGVEaXJlY3RpdmVNZXRhZGF0YSwgQ29tcGlsZURpcmVjdGl2ZVN1bW1hcnksIENvbXBpbGVQaXBlU3VtbWFyeSwgQ3NzU2VsZWN0b3IsIE5vZGUgYXMgSHRtbEFzdCwgUGFyc2VFcnJvciwgUGFyc2VyLCBUZW1wbGF0ZUFzdH0gZnJvbSAnQGFuZ3VsYXIvY29tcGlsZXInO1xuXG5pbXBvcnQge0RpYWdub3N0aWMsIFRlbXBsYXRlU291cmNlfSBmcm9tICcuL3R5cGVzJztcblxuZXhwb3J0IGludGVyZmFjZSBBc3RSZXN1bHQge1xuICBodG1sQXN0OiBIdG1sQXN0W107XG4gIHRlbXBsYXRlQXN0OiBUZW1wbGF0ZUFzdFtdO1xuICBkaXJlY3RpdmU6IENvbXBpbGVEaXJlY3RpdmVNZXRhZGF0YTtcbiAgZGlyZWN0aXZlczogQ29tcGlsZURpcmVjdGl2ZVN1bW1hcnlbXTtcbiAgcGlwZXM6IENvbXBpbGVQaXBlU3VtbWFyeVtdO1xuICBwYXJzZUVycm9ycz86IFBhcnNlRXJyb3JbXTtcbiAgZXhwcmVzc2lvblBhcnNlcjogUGFyc2VyO1xuICB0ZW1wbGF0ZTogVGVtcGxhdGVTb3VyY2U7XG59XG5cbmV4cG9ydCB0eXBlIFNlbGVjdG9ySW5mbyA9IHtcbiAgc2VsZWN0b3JzOiBDc3NTZWxlY3RvcltdLFxuICBtYXA6IE1hcDxDc3NTZWxlY3RvciwgQ29tcGlsZURpcmVjdGl2ZVN1bW1hcnk+XG59O1xuXG5leHBvcnQgZnVuY3Rpb24gaXNBc3RSZXN1bHQocmVzdWx0OiBBc3RSZXN1bHQgfCBEaWFnbm9zdGljKTogcmVzdWx0IGlzIEFzdFJlc3VsdCB7XG4gIHJldHVybiByZXN1bHQuaGFzT3duUHJvcGVydHkoJ3RlbXBsYXRlQXN0Jyk7XG59XG4iXX0=