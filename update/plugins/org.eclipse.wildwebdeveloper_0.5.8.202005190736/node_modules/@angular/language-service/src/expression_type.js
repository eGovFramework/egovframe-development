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
        define("@angular/language-service/src/expression_type", ["require", "exports", "tslib", "@angular/compiler", "@angular/language-service/src/symbols"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var tslib_1 = require("tslib");
    var compiler_1 = require("@angular/compiler");
    var symbols_1 = require("@angular/language-service/src/symbols");
    var DiagnosticKind;
    (function (DiagnosticKind) {
        DiagnosticKind[DiagnosticKind["Error"] = 0] = "Error";
        DiagnosticKind[DiagnosticKind["Warning"] = 1] = "Warning";
    })(DiagnosticKind = exports.DiagnosticKind || (exports.DiagnosticKind = {}));
    var TypeDiagnostic = /** @class */ (function () {
        function TypeDiagnostic(kind, message, ast) {
            this.kind = kind;
            this.message = message;
            this.ast = ast;
        }
        return TypeDiagnostic;
    }());
    exports.TypeDiagnostic = TypeDiagnostic;
    // AstType calculatetype of the ast given AST element.
    var AstType = /** @class */ (function () {
        function AstType(scope, query, context) {
            this.scope = scope;
            this.query = query;
            this.context = context;
        }
        AstType.prototype.getType = function (ast) { return ast.visit(this); };
        AstType.prototype.getDiagnostics = function (ast) {
            this.diagnostics = [];
            var type = ast.visit(this);
            if (this.context.event && type.callable) {
                this.reportWarning('Unexpected callable expression. Expected a method call', ast);
            }
            return this.diagnostics;
        };
        AstType.prototype.visitBinary = function (ast) {
            var _this_1 = this;
            // Treat undefined and null as other.
            function normalize(kind, other) {
                switch (kind) {
                    case symbols_1.BuiltinType.Undefined:
                    case symbols_1.BuiltinType.Null:
                        return normalize(other, symbols_1.BuiltinType.Other);
                }
                return kind;
            }
            var getType = function (ast, operation) {
                var type = _this_1.getType(ast);
                if (type.nullable) {
                    switch (operation) {
                        case '&&':
                        case '||':
                        case '==':
                        case '!=':
                        case '===':
                        case '!==':
                            // Nullable allowed.
                            break;
                        default:
                            _this_1.reportError("The expression might be null", ast);
                            break;
                    }
                    return _this_1.query.getNonNullableType(type);
                }
                return type;
            };
            var leftType = getType(ast.left, ast.operation);
            var rightType = getType(ast.right, ast.operation);
            var leftRawKind = this.query.getTypeKind(leftType);
            var rightRawKind = this.query.getTypeKind(rightType);
            var leftKind = normalize(leftRawKind, rightRawKind);
            var rightKind = normalize(rightRawKind, leftRawKind);
            // The following swtich implements operator typing similar to the
            // type production tables in the TypeScript specification.
            // https://github.com/Microsoft/TypeScript/blob/v1.8.10/doc/spec.md#4.19
            var operKind = leftKind << 8 | rightKind;
            switch (ast.operation) {
                case '*':
                case '/':
                case '%':
                case '-':
                case '<<':
                case '>>':
                case '>>>':
                case '&':
                case '^':
                case '|':
                    switch (operKind) {
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.Number << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.Number:
                        case symbols_1.BuiltinType.Number << 8 | symbols_1.BuiltinType.Number:
                            return this.query.getBuiltinType(symbols_1.BuiltinType.Number);
                        default:
                            var errorAst = ast.left;
                            switch (leftKind) {
                                case symbols_1.BuiltinType.Any:
                                case symbols_1.BuiltinType.Number:
                                    errorAst = ast.right;
                                    break;
                            }
                            return this.reportError('Expected a numeric type', errorAst);
                    }
                case '+':
                    switch (operKind) {
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.Boolean:
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.Number:
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.Other:
                        case symbols_1.BuiltinType.Boolean << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.Number << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.Other << 8 | symbols_1.BuiltinType.Any:
                            return this.anyType;
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.String:
                        case symbols_1.BuiltinType.Boolean << 8 | symbols_1.BuiltinType.String:
                        case symbols_1.BuiltinType.Number << 8 | symbols_1.BuiltinType.String:
                        case symbols_1.BuiltinType.String << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.String << 8 | symbols_1.BuiltinType.Boolean:
                        case symbols_1.BuiltinType.String << 8 | symbols_1.BuiltinType.Number:
                        case symbols_1.BuiltinType.String << 8 | symbols_1.BuiltinType.String:
                        case symbols_1.BuiltinType.String << 8 | symbols_1.BuiltinType.Other:
                        case symbols_1.BuiltinType.Other << 8 | symbols_1.BuiltinType.String:
                            return this.query.getBuiltinType(symbols_1.BuiltinType.String);
                        case symbols_1.BuiltinType.Number << 8 | symbols_1.BuiltinType.Number:
                            return this.query.getBuiltinType(symbols_1.BuiltinType.Number);
                        case symbols_1.BuiltinType.Boolean << 8 | symbols_1.BuiltinType.Number:
                        case symbols_1.BuiltinType.Other << 8 | symbols_1.BuiltinType.Number:
                            return this.reportError('Expected a number type', ast.left);
                        case symbols_1.BuiltinType.Number << 8 | symbols_1.BuiltinType.Boolean:
                        case symbols_1.BuiltinType.Number << 8 | symbols_1.BuiltinType.Other:
                            return this.reportError('Expected a number type', ast.right);
                        default:
                            return this.reportError('Expected operands to be a string or number type', ast);
                    }
                case '>':
                case '<':
                case '<=':
                case '>=':
                case '==':
                case '!=':
                case '===':
                case '!==':
                    switch (operKind) {
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.Boolean:
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.Number:
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.String:
                        case symbols_1.BuiltinType.Any << 8 | symbols_1.BuiltinType.Other:
                        case symbols_1.BuiltinType.Boolean << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.Boolean << 8 | symbols_1.BuiltinType.Boolean:
                        case symbols_1.BuiltinType.Number << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.Number << 8 | symbols_1.BuiltinType.Number:
                        case symbols_1.BuiltinType.String << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.String << 8 | symbols_1.BuiltinType.String:
                        case symbols_1.BuiltinType.Other << 8 | symbols_1.BuiltinType.Any:
                        case symbols_1.BuiltinType.Other << 8 | symbols_1.BuiltinType.Other:
                            return this.query.getBuiltinType(symbols_1.BuiltinType.Boolean);
                        default:
                            return this.reportError('Expected the operants to be of similar type or any', ast);
                    }
                case '&&':
                    return rightType;
                case '||':
                    return this.query.getTypeUnion(leftType, rightType);
            }
            return this.reportError("Unrecognized operator " + ast.operation, ast);
        };
        AstType.prototype.visitChain = function (ast) {
            if (this.diagnostics) {
                // If we are producing diagnostics, visit the children
                compiler_1.visitAstChildren(ast, this);
            }
            // The type of a chain is always undefined.
            return this.query.getBuiltinType(symbols_1.BuiltinType.Undefined);
        };
        AstType.prototype.visitConditional = function (ast) {
            // The type of a conditional is the union of the true and false conditions.
            if (this.diagnostics) {
                compiler_1.visitAstChildren(ast, this);
            }
            return this.query.getTypeUnion(this.getType(ast.trueExp), this.getType(ast.falseExp));
        };
        AstType.prototype.visitFunctionCall = function (ast) {
            var _this_1 = this;
            // The type of a function call is the return type of the selected signature.
            // The signature is selected based on the types of the arguments. Angular doesn't
            // support contextual typing of arguments so this is simpler than TypeScript's
            // version.
            var args = ast.args.map(function (arg) { return _this_1.getType(arg); });
            var target = this.getType(ast.target);
            if (!target || !target.callable)
                return this.reportError('Call target is not callable', ast);
            var signature = target.selectSignature(args);
            if (signature)
                return signature.result;
            // TODO: Consider a better error message here.
            return this.reportError('Unable no compatible signature found for call', ast);
        };
        AstType.prototype.visitImplicitReceiver = function (ast) {
            var _this = this;
            // Return a pseudo-symbol for the implicit receiver.
            // The members of the implicit receiver are what is defined by the
            // scope passed into this class.
            return {
                name: '$implicit',
                kind: 'component',
                language: 'ng-template',
                type: undefined,
                container: undefined,
                callable: false,
                nullable: false,
                public: true,
                definition: undefined,
                members: function () { return _this.scope; },
                signatures: function () { return []; },
                selectSignature: function (types) { return undefined; },
                indexed: function (argument) { return undefined; }
            };
        };
        AstType.prototype.visitInterpolation = function (ast) {
            // If we are producing diagnostics, visit the children.
            if (this.diagnostics) {
                compiler_1.visitAstChildren(ast, this);
            }
            return this.undefinedType;
        };
        AstType.prototype.visitKeyedRead = function (ast) {
            var targetType = this.getType(ast.obj);
            var keyType = this.getType(ast.key);
            var result = targetType.indexed(keyType, ast.key instanceof compiler_1.LiteralPrimitive ? ast.key.value : undefined);
            return result || this.anyType;
        };
        AstType.prototype.visitKeyedWrite = function (ast) {
            // The write of a type is the type of the value being written.
            return this.getType(ast.value);
        };
        AstType.prototype.visitLiteralArray = function (ast) {
            var _a;
            var _this_1 = this;
            // A type literal is an array type of the union of the elements
            return this.query.getArrayType((_a = this.query).getTypeUnion.apply(_a, tslib_1.__spread(ast.expressions.map(function (element) { return _this_1.getType(element); }))));
        };
        AstType.prototype.visitLiteralMap = function (ast) {
            // If we are producing diagnostics, visit the children
            if (this.diagnostics) {
                compiler_1.visitAstChildren(ast, this);
            }
            // TODO: Return a composite type.
            return this.anyType;
        };
        AstType.prototype.visitLiteralPrimitive = function (ast) {
            // The type of a literal primitive depends on the value of the literal.
            switch (ast.value) {
                case true:
                case false:
                    return this.query.getBuiltinType(symbols_1.BuiltinType.Boolean);
                case null:
                    return this.query.getBuiltinType(symbols_1.BuiltinType.Null);
                case undefined:
                    return this.query.getBuiltinType(symbols_1.BuiltinType.Undefined);
                default:
                    switch (typeof ast.value) {
                        case 'string':
                            return this.query.getBuiltinType(symbols_1.BuiltinType.String);
                        case 'number':
                            return this.query.getBuiltinType(symbols_1.BuiltinType.Number);
                        default:
                            return this.reportError('Unrecognized primitive', ast);
                    }
            }
        };
        AstType.prototype.visitMethodCall = function (ast) {
            return this.resolveMethodCall(this.getType(ast.receiver), ast);
        };
        AstType.prototype.visitPipe = function (ast) {
            var _this_1 = this;
            // The type of a pipe node is the return type of the pipe's transform method. The table returned
            // by getPipes() is expected to contain symbols with the corresponding transform method type.
            var pipe = this.query.getPipes().get(ast.name);
            if (!pipe)
                return this.reportError("No pipe by the name " + ast.name + " found", ast);
            var expType = this.getType(ast.exp);
            var signature = pipe.selectSignature([expType].concat(ast.args.map(function (arg) { return _this_1.getType(arg); })));
            if (!signature)
                return this.reportError('Unable to resolve signature for pipe invocation', ast);
            return signature.result;
        };
        AstType.prototype.visitPrefixNot = function (ast) {
            // If we are producing diagnostics, visit the children
            if (this.diagnostics) {
                compiler_1.visitAstChildren(ast, this);
            }
            // The type of a prefix ! is always boolean.
            return this.query.getBuiltinType(symbols_1.BuiltinType.Boolean);
        };
        AstType.prototype.visitNonNullAssert = function (ast) {
            var expressionType = this.getType(ast.expression);
            return this.query.getNonNullableType(expressionType);
        };
        AstType.prototype.visitPropertyRead = function (ast) {
            return this.resolvePropertyRead(this.getType(ast.receiver), ast);
        };
        AstType.prototype.visitPropertyWrite = function (ast) {
            // The type of a write is the type of the value being written.
            return this.getType(ast.value);
        };
        AstType.prototype.visitQuote = function (ast) {
            // The type of a quoted expression is any.
            return this.query.getBuiltinType(symbols_1.BuiltinType.Any);
        };
        AstType.prototype.visitSafeMethodCall = function (ast) {
            return this.resolveMethodCall(this.query.getNonNullableType(this.getType(ast.receiver)), ast);
        };
        AstType.prototype.visitSafePropertyRead = function (ast) {
            return this.resolvePropertyRead(this.query.getNonNullableType(this.getType(ast.receiver)), ast);
        };
        Object.defineProperty(AstType.prototype, "anyType", {
            get: function () {
                var result = this._anyType;
                if (!result) {
                    result = this._anyType = this.query.getBuiltinType(symbols_1.BuiltinType.Any);
                }
                return result;
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(AstType.prototype, "undefinedType", {
            get: function () {
                var result = this._undefinedType;
                if (!result) {
                    result = this._undefinedType = this.query.getBuiltinType(symbols_1.BuiltinType.Undefined);
                }
                return result;
            },
            enumerable: true,
            configurable: true
        });
        AstType.prototype.resolveMethodCall = function (receiverType, ast) {
            var _this_1 = this;
            if (this.isAny(receiverType)) {
                return this.anyType;
            }
            // The type of a method is the selected methods result type.
            var method = receiverType.members().get(ast.name);
            if (!method)
                return this.reportError("Unknown method '" + ast.name + "'", ast);
            if (!method.type)
                return this.reportError("Could not find a type for '" + ast.name + "'", ast);
            if (!method.type.callable)
                return this.reportError("Member '" + ast.name + "' is not callable", ast);
            var signature = method.type.selectSignature(ast.args.map(function (arg) { return _this_1.getType(arg); }));
            if (!signature)
                return this.reportError("Unable to resolve signature for call of method " + ast.name, ast);
            return signature.result;
        };
        AstType.prototype.resolvePropertyRead = function (receiverType, ast) {
            if (this.isAny(receiverType)) {
                return this.anyType;
            }
            // The type of a property read is the seelcted member's type.
            var member = receiverType.members().get(ast.name);
            if (!member) {
                var receiverInfo = receiverType.name;
                if (receiverInfo == '$implicit') {
                    receiverInfo =
                        'The component declaration, template variable declarations, and element references do';
                }
                else if (receiverType.nullable) {
                    return this.reportError("The expression might be null", ast.receiver);
                }
                else {
                    receiverInfo = "'" + receiverInfo + "' does";
                }
                return this.reportError("Identifier '" + ast.name + "' is not defined. " + receiverInfo + " not contain such a member", ast);
            }
            if (!member.public) {
                var receiverInfo = receiverType.name;
                if (receiverInfo == '$implicit') {
                    receiverInfo = 'the component';
                }
                else {
                    receiverInfo = "'" + receiverInfo + "'";
                }
                this.reportWarning("Identifier '" + ast.name + "' refers to a private member of " + receiverInfo, ast);
            }
            return member.type;
        };
        AstType.prototype.reportError = function (message, ast) {
            if (this.diagnostics) {
                this.diagnostics.push(new TypeDiagnostic(DiagnosticKind.Error, message, ast));
            }
            return this.anyType;
        };
        AstType.prototype.reportWarning = function (message, ast) {
            if (this.diagnostics) {
                this.diagnostics.push(new TypeDiagnostic(DiagnosticKind.Warning, message, ast));
            }
            return this.anyType;
        };
        AstType.prototype.isAny = function (symbol) {
            return !symbol || this.query.getTypeKind(symbol) == symbols_1.BuiltinType.Any ||
                (!!symbol.type && this.isAny(symbol.type));
        };
        return AstType;
    }());
    exports.AstType = AstType;
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiZXhwcmVzc2lvbl90eXBlLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXMiOlsiLi4vLi4vLi4vLi4vLi4vLi4vcGFja2FnZXMvbGFuZ3VhZ2Utc2VydmljZS9zcmMvZXhwcmVzc2lvbl90eXBlLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBOzs7Ozs7R0FNRzs7Ozs7Ozs7Ozs7OztJQUVILDhDQUEyVTtJQUUzVSxpRUFBeUY7SUFJekYsSUFBWSxjQUdYO0lBSEQsV0FBWSxjQUFjO1FBQ3hCLHFEQUFLLENBQUE7UUFDTCx5REFBTyxDQUFBO0lBQ1QsQ0FBQyxFQUhXLGNBQWMsR0FBZCxzQkFBYyxLQUFkLHNCQUFjLFFBR3pCO0lBRUQ7UUFDRSx3QkFBbUIsSUFBb0IsRUFBUyxPQUFlLEVBQVMsR0FBUTtZQUE3RCxTQUFJLEdBQUosSUFBSSxDQUFnQjtZQUFTLFlBQU8sR0FBUCxPQUFPLENBQVE7WUFBUyxRQUFHLEdBQUgsR0FBRyxDQUFLO1FBQUcsQ0FBQztRQUN0RixxQkFBQztJQUFELENBQUMsQUFGRCxJQUVDO0lBRlksd0NBQWM7SUFJM0Isc0RBQXNEO0lBQ3REO1FBSUUsaUJBQ1ksS0FBa0IsRUFBVSxLQUFrQixFQUM5QyxPQUFxQztZQURyQyxVQUFLLEdBQUwsS0FBSyxDQUFhO1lBQVUsVUFBSyxHQUFMLEtBQUssQ0FBYTtZQUM5QyxZQUFPLEdBQVAsT0FBTyxDQUE4QjtRQUFHLENBQUM7UUFFckQseUJBQU8sR0FBUCxVQUFRLEdBQVEsSUFBWSxPQUFPLEdBQUcsQ0FBQyxLQUFLLENBQUMsSUFBSSxDQUFDLENBQUMsQ0FBQyxDQUFDO1FBRXJELGdDQUFjLEdBQWQsVUFBZSxHQUFRO1lBQ3JCLElBQUksQ0FBQyxXQUFXLEdBQUcsRUFBRSxDQUFDO1lBQ3RCLElBQU0sSUFBSSxHQUFXLEdBQUcsQ0FBQyxLQUFLLENBQUMsSUFBSSxDQUFDLENBQUM7WUFDckMsSUFBSSxJQUFJLENBQUMsT0FBTyxDQUFDLEtBQUssSUFBSSxJQUFJLENBQUMsUUFB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RCxPQUFPLE1BQU0sQ0FBQyxJQUFJLENBQUM7UUFDckIsQ0FBQztRQUVPLDZCQUFXLEdBQW5CLFVBQW9CLE9BQWUsRUFBRSxHQUFRO1lBQzNDLElBQUksSUFBSSxDQUFDLFdBQVcsRUFBRTtnQkFDcEIsSUFBSSxDQUFDLFdBQVcsQ0FBQyxJQUFJLENBQUMsSUFBSSxjQUFjLENBQUMsY0FBYyxDQUFDLEtBQUssRUFBRSxPQUFPLEVBQUUsR0FBRyxDQUFDLENBQUMsQ0FBQzthQUMvRTtZQUNELE9BQU8sSUFBSSxDQUFDLE9BQU8sQ0FBQztRQUN0QixDQUFDO1FBRU8sK0JBQWEsR0FBckIsVUFBc0IsT0FBZSxFQUFFLEdBQVE7WUFDN0MsSUFBSSxJQUFJLENBQUMsV0FBVyxFQUFFO2dCQUNwQixJQUFJLENBQUMsV0FBVyxDQUFDLElBQUksQ0FBQyxJQUFJLGNBQWMsQ0FBQyxjQUFjLENBQUMsT0FBTyxFQUFFLE9BQU8sRUFBRSxHQUFHLENBQUMsQ0FBQyxDQUFDO2FBQ2pGO1lBQ0QsT0FBTyxJQUFJLENBQUMsT0FBTyxDQUFDO1FBQ3RCLENBQUM7UUFFTyx1QkFBSyxHQUFiLFVBQWMsTUFBYztZQUMxQixPQUFPLENBQUMsTUFBTSxJQUFJLElBQUksQ0FBQyxLQUFLLENBQUMsV0FBVyxDQUFDLE1BQU0sQ0FBQyxJQUFJLHFCQUFXLENBQUMsR0FBRztnQkFDL0QsQ0FBQyxDQUFDLENBQUMsTUFBTSxDQUFDLElBQUksSUFBSSxJQUFJLENBQUMsS0FBSyxDQUFDLE1BQU0sQ0FBQyxJQUFJLENBQUMsQ0FBQyxDQUFDO1FBQ2pELENBQUM7UUFDSCxjQUFDO0lBQUQsQ0FBQyxBQXRaRCxJQXNaQztJQXRaWSwwQkFBTyIsInNvdXJjZXNDb250ZW50IjpbIi8qKlxuICogQGxpY2Vuc2VcbiAqIENvcHlyaWdodCBHb29nbGUgSW5jLiBBbGwgUmlnaHRzIFJlc2VydmVkLlxuICpcbiAqIFVzZSBvZiB0aGlzIHNvdXJjZSBjb2RlIGlzIGdvdmVybmVkIGJ5IGFuIE1JVC1zdHlsZSBsaWNlbnNlIHRoYXQgY2FuIGJlXG4gKiBmb3VuZCBpbiB0aGUgTElDRU5TRSBmaWxlIGF0IGh0dHBzOi8vYW5ndWxhci5pby9saWNlbnNlXG4gKi9cblxuaW1wb3J0IHtBU1QsIEFzdFZpc2l0b3IsIEJpbmFyeSwgQmluZGluZ1BpcGUsIENoYWluLCBDb25kaXRpb25hbCwgRnVuY3Rpb25DYWxsLCBJbXBsaWNpdFJlY2VpdmVyLCBJbnRlcnBvbGF0aW9uLCBLZXllZFJlYWQsIEtleWVkV3JpdGUsIExpdGVyYWxBcnJheSwgTGl0ZXJhbE1hcCwgTGl0ZXJhbFByaW1pdGl2ZSwgTWV0aG9kQ2FsbCwgTm9uTnVsbEFzc2VydCwgUHJlZml4Tm90LCBQcm9wZXJ0eVJlYWQsIFByb3BlcnR5V3JpdGUsIFF1b3RlLCBTYWZlTWV0aG9kQ2FsbCwgU2FmZVByb3BlcnR5UmVhZCwgdmlzaXRBc3RDaGlsZHJlbn0gZnJvbSAnQGFuZ3VsYXIvY29tcGlsZXInO1xuXG5pbXBvcnQge0J1aWx0aW5UeXBlLCBTaWduYXR1cmUsIFNwYW4sIFN5bWJvbCwgU3ltYm9sUXVlcnksIFN5bWJvbFRhYmxlfSBmcm9tICcuL3N5bWJvbHMnO1xuXG5leHBvcnQgaW50ZXJmYWNlIEV4cHJlc3Npb25EaWFnbm9zdGljc0NvbnRleHQgeyBldmVudD86IGJvb2xlYW47IH1cblxuZXhwb3J0IGVudW0gRGlhZ25vc3RpY0tpbmQge1xuICBFcnJvcixcbiAgV2FybmluZyxcbn1cblxuZXhwb3J0IGNsYXNzIFR5cGVEaWFnbm9zdGljIHtcbiAgY29uc3RydWN0b3IocHVibGljIGtpbmQ6IERpYWdub3N0aWNLaW5kLCBwdWJsaWMgbWVzc2FnZTogc3RyaW5nLCBwdWJsaWMgYXN0OiBBU1QpIHt9XG59XG5cbi8vIEFzdFR5cGUgY2FsY3VsYXRldHlwZSBvZiB0aGUgYXN0IGdpdmVuIEFTVCBlbGVtZW50LlxuZXhwb3J0IGNsYXNzIEFzdFR5cGUgaW1wbGVtZW50cyBBc3RWaXNpdG9yIHtcbiAgLy8gVE9ETyhpc3N1ZS8yNDU3MSk6IHJlbW92ZSAnIScuXG4gIHB1YmxpYyBkaWFnbm9zdGljcyAhOiBUeXBlRGlhZ25vc3RpY1tdO1xuXG4gIGNvbnN0cnVjdG9yKFxuICAgICAgcHJpdmF0ZSBzY29wZTogU3ltYm9sVGFibGUsIHByaXZhdGUgcXVlcnk6IFN5bWJvbFF1ZXJ5LFxuICAgICAgcHJpdmF0ZSBjb250ZXh0OiBFeHByZXNzaW9uRGlhZ25vc3RpY3NDb250ZXh0KSB7fVxuXG4gIGdldFR5cGUoYXN0OiBBU1QpOiBTeW1ib2wgeyByZXR1cm4gYXN0LnZpc2l0KHRoaXMpOyB9XG5cbiAgZ2V0RGlhZ25vc3RpY3MoYXN0OiBBU1QpOiBUeXBlRGlhZ25vc3RpY1tdIHtcbiAgICB0aGlzLmRpYWdub3N0aWNzID0gW107XG4gICAgY29uc3QgdHlwZTogU3ltYm9sID0gYXN0LnZpc2l0KHRoaXMpO1xuICAgIGlmICh0aGlzLmNvbnRleHQuZXZlbnQgJiYgdHlwZS5jYWxsYWJsZSkge1xuICAgICAgdGhpcy5yZXBvcnRXYXJuaW5nKCdVbmV4cGVjdGVkIGNhbGxhYmxlIGV4cHJlc3Npb24uIEV4cGVjdGVkIGEgbWV0aG9kIGNhbGwnLCBhc3QpO1xuICAgIH1cbiAgICByZXR1cm4gdGhpcy5kaWFnbm9zdGljcztcbiAgfVxuXG4gIHZpc2l0QmluYXJ5KGFzdDogQmluYXJ5KTogU3ltYm9sIHtcbiAgICAvLyBUcmVhdCB1bmRlZmluZWQgYW5kIG51bGwgYXMgb3RoZXIuXG4gICAgZnVuY3Rpb24gbm9ybWFsaXplKGtpbmQ6IEJ1aWx0aW5UeXBlLCBvdGhlcjogQnVpbHRpblR5cGUpOiBCdWlsdGluVHlwZSB7XG4gICAgICBzd2l0Y2ggKGtpbmQpIHtcbiAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5VbmRlZmluZWQ6XG4gICAgICAgIGNhc2UgQnVpbHRpblR5cGUuTnVsbDpcbiAgICAgICAgICByZXR1cm4gbm9ybWFsaXplKG90aGVyLCBCdWlsdGluVHlwZS5PdGhlcik7XG4gICAgICB9XG4gICAgICByZXR1cm4ga2luZDtcbiAgICB9XG5cbiAgICBjb25zdCBnZXRUeXBlID0gKGFzdDogQVNULCBvcGVyYXRpb246IHN0cmluZyk6IFN5bWJvbCA9PiB7XG4gICAgICBjb25zdCB0eXBlID0gdGhpcy5nZXRUeXBlKGFzdCk7XG4gICAgICBpZiAodHlwZS5udWxsYWJsZSkge1xuICAgICAgICBzd2l0Y2ggKG9wZXJhdGlvbikge1xuICAgICAgICAgIGNhc2UgJyYmJzpcbiAgICAgICAgICBjYXNlICd8fCc6XG4gICAgICAgICAgY2FzZSAnPT0nOlxuICAgICAgICAgIGNhc2UgJyE9JzpcbiAgICAgICAgICBjYXNlICc9PT0nOlxuICAgICAgICAgIGNhc2UgJyE9PSc6XG4gICAgICAgICAgICAvLyBOdWxsYWJsZSBhbGxvd2VkLlxuICAgICAgICAgICAgYnJlYWs7XG4gICAgICAgICAgZGVmYXVsdDpcbiAgICAgICAgICAgIHRoaXMucmVwb3J0RXJyb3IoYFRoZSBleHByZXNzaW9uIG1pZ2h0IGJlIG51bGxgLCBhc3QpO1xuICAgICAgICAgICAgYnJlYWs7XG4gICAgICAgIH1cbiAgICAgICAgcmV0dXJuIHRoaXMucXVlcnkuZ2V0Tm9uTnVsbGFibGVUeXBlKHR5cGUpO1xuICAgICAgfVxuICAgICAgcmV0dXJuIHR5cGU7XG4gICAgfTtcblxuICAgIGNvbnN0IGxlZnRUeXBlID0gZ2V0VHlwZShhc3QubGVmdCwgYXN0Lm9wZXJhdGlvbik7XG4gICAgY29uc3QgcmlnaHRUeXBlID0gZ2V0VHlwZShhc3QucmlnaHQsIGFzdC5vcGVyYXRpb24pO1xuICAgIGNvbnN0IGxlZnRSYXdLaW5kID0gdGhpcy5xdWVyeS5nZXRUeXBlS2luZChsZWZ0VHlwZSk7XG4gICAgY29uc3QgcmlnaHRSYXdLaW5kID0gdGhpcy5xdWVyeS5nZXRUeXBlS2luZChyaWdodFR5cGUpO1xuICAgIGNvbnN0IGxlZnRLaW5kID0gbm9ybWFsaXplKGxlZnRSYXdLaW5kLCByaWdodFJhd0tpbmQpO1xuICAgIGNvbnN0IHJpZ2h0S2luZCA9IG5vcm1hbGl6ZShyaWdodFJhd0tpbmQsIGxlZnRSYXdLaW5kKTtcblxuICAgIC8vIFRoZSBmb2xsb3dpbmcgc3d0aWNoIGltcGxlbWVudHMgb3BlcmF0b3IgdHlwaW5nIHNpbWlsYXIgdG8gdGhlXG4gICAgLy8gdHlwZSBwcm9kdWN0aW9uIHRhYmxlcyBpbiB0aGUgVHlwZVNjcmlwdCBzcGVjaWZpY2F0aW9uLlxuICAgIC8vIGh0dHBzOi8vZ2l0aHViLmNvbS9NaWNyb3NvZnQvVHlwZVNjcmlwdC9ibG9iL3YxLjguMTAvZG9jL3NwZWMubWQjNC4xOVxuICAgIGNvbnN0IG9wZXJLaW5kID0gbGVmdEtpbmQgPDwgOCB8IHJpZ2h0S2luZDtcbiAgICBzd2l0Y2ggKGFzdC5vcGVyYXRpb24pIHtcbiAgICAgIGNhc2UgJyonOlxuICAgICAgY2FzZSAnLyc6XG4gICAgICBjYXNlICclJzpcbiAgICAgIGNhc2UgJy0nOlxuICAgICAgY2FzZSAnPDwnOlxuICAgICAgY2FzZSAnPj4nOlxuICAgICAgY2FzZSAnPj4+JzpcbiAgICAgIGNhc2UgJyYnOlxuICAgICAgY2FzZSAnXic6XG4gICAgICBjYXNlICd8JzpcbiAgICAgICAgc3dpdGNoIChvcGVyS2luZCkge1xuICAgICAgICAgIGNhc2UgQnVpbHRpblR5cGUuQW55IDw8IDggfCBCdWlsdGluVHlwZS5Bbnk6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5OdW1iZXIgPDwgOCB8IEJ1aWx0aW5UeXBlLkFueTpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLkFueSA8PCA4IHwgQnVpbHRpblR5cGUuTnVtYmVyOlxuICAgICAgICAgIGNhc2UgQnVpbHRpblR5cGUuTnVtYmVyIDw8IDggfCBCdWlsdGluVHlwZS5OdW1iZXI6XG4gICAgICAgICAgICByZXR1cm4gdGhpcy5xdWVyeS5nZXRCdWlsdGluVHlwZShCdWlsdGluVHlwZS5OdW1iZXIpO1xuICAgICAgICAgIGRlZmF1bHQ6XG4gICAgICAgICAgICBsZXQgZXJyb3JBc3QgPSBhc3QubGVmdDtcbiAgICAgICAgICAgIHN3aXRjaCAobGVmdEtpbmQpIHtcbiAgICAgICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5Bbnk6XG4gICAgICAgICAgICAgIGNhc2UgQnVpbHRpblR5cGUuTnVtYmVyOlxuICAgICAgICAgICAgICAgIGVycm9yQXN0ID0gYXN0LnJpZ2h0O1xuICAgICAgICAgICAgICAgIGJyZWFrO1xuICAgICAgICAgICAgfVxuICAgICAgICAgICAgcmV0dXJuIHRoaXMucmVwb3J0RXJyb3IoJ0V4cGVjdGVkIGEgbnVtZXJpYyB0eXBlJywgZXJyb3JBc3QpO1xuICAgICAgICB9XG4gICAgICBjYXNlICcrJzpcbiAgICAgICAgc3dpdGNoIChvcGVyS2luZCkge1xuICAgICAgICAgIGNhc2UgQnVpbHRpblR5cGUuQW55IDw8IDggfCBCdWlsdGluVHlwZS5Bbnk6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5BbnkgPDwgOCB8IEJ1aWx0aW5UeXBlLkJvb2xlYW46XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5BbnkgPDwgOCB8IEJ1aWx0aW5UeXBlLk51bWJlcjpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLkFueSA8PCA4IHwgQnVpbHRpblR5cGUuT3RoZXI6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5Cb29sZWFuIDw8IDggfCBCdWlsdGluVHlwZS5Bbnk6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5OdW1iZXIgPDwgOCB8IEJ1aWx0aW5UeXBlLkFueTpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLk90aGVyIDw8IDggfCBCdWlsdGluVHlwZS5Bbnk6XG4gICAgICAgICAgICByZXR1cm4gdGhpcy5hbnlUeXBlO1xuICAgICAgICAgIGNhc2UgQnVpbHRpblR5cGUuQW55IDw8IDggfCBCdWlsdGluVHlwZS5TdHJpbmc6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5Cb29sZWFuIDw8IDggfCBCdWlsdGluVHlwZS5TdHJpbmc6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5OdW1iZXIgPDwgOCB8IEJ1aWx0aW5UeXBlLlN0cmluZzpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLlN0cmluZyA8PCA4IHwgQnVpbHRpblR5cGUuQW55OlxuICAgICAgICAgIGNhc2UgQnVpbHRpblR5cGUuU3RyaW5nIDw8IDggfCBCdWlsdGluVHlwZS5Cb29sZWFuOlxuICAgICAgICAgIGNhc2UgQnVpbHRpblR5cGUuU3RyaW5nIDw8IDggfCBCdWlsdGluVHlwZS5OdW1iZXI6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5TdHJpbmcgPDwgOCB8IEJ1aWx0aW5UeXBlLlN0cmluZzpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLlN0cmluZyA8PCA4IHwgQnVpbHRpblR5cGUuT3RoZXI6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5PdGhlciA8PCA4IHwgQnVpbHRpblR5cGUuU3RyaW5nOlxuICAgICAgICAgICAgcmV0dXJuIHRoaXMucXVlcnkuZ2V0QnVpbHRpblR5cGUoQnVpbHRpblR5cGUuU3RyaW5nKTtcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLk51bWJlciA8PCA4IHwgQnVpbHRpblR5cGUuTnVtYmVyOlxuICAgICAgICAgICAgcmV0dXJuIHRoaXMucXVlcnkuZ2V0QnVpbHRpblR5cGUoQnVpbHRpblR5cGUuTnVtYmVyKTtcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLkJvb2xlYW4gPDwgOCB8IEJ1aWx0aW5UeXBlLk51bWJlcjpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLk90aGVyIDw8IDggfCBCdWlsdGluVHlwZS5OdW1iZXI6XG4gICAgICAgICAgICByZXR1cm4gdGhpcy5yZXBvcnRFcnJvcignRXhwZWN0ZWQgYSBudW1iZXIgdHlwZScsIGFzdC5sZWZ0KTtcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLk51bWJlciA8PCA4IHwgQnVpbHRpblR5cGUuQm9vbGVhbjpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLk51bWJlciA8PCA4IHwgQnVpbHRpblR5cGUuT3RoZXI6XG4gICAgICAgICAgICByZXR1cm4gdGhpcy5yZXBvcnRFcnJvcignRXhwZWN0ZWQgYSBudW1iZXIgdHlwZScsIGFzdC5yaWdodCk7XG4gICAgICAgICAgZGVmYXVsdDpcbiAgICAgICAgICAgIHJldHVybiB0aGlzLnJlcG9ydEVycm9yKCdFeHBlY3RlZCBvcGVyYW5kcyB0byBiZSBhIHN0cmluZyBvciBudW1iZXIgdHlwZScsIGFzdCk7XG4gICAgICAgIH1cbiAgICAgIGNhc2UgJz4nOlxuICAgICAgY2FzZSAnPCc6XG4gICAgICBjYXNlICc8PSc6XG4gICAgICBjYXNlICc+PSc6XG4gICAgICBjYXNlICc9PSc6XG4gICAgICBjYXNlICchPSc6XG4gICAgICBjYXNlICc9PT0nOlxuICAgICAgY2FzZSAnIT09JzpcbiAgICAgICAgc3dpdGNoIChvcGVyS2luZCkge1xuICAgICAgICAgIGNhc2UgQnVpbHRpblR5cGUuQW55IDw8IDggfCBCdWlsdGluVHlwZS5Bbnk6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5BbnkgPDwgOCB8IEJ1aWx0aW5UeXBlLkJvb2xlYW46XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5BbnkgPDwgOCB8IEJ1aWx0aW5UeXBlLk51bWJlcjpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLkFueSA8PCA4IHwgQnVpbHRpblR5cGUuU3RyaW5nOlxuICAgICAgICAgIGNhc2UgQnVpbHRpblR5cGUuQW55IDw8IDggfCBCdWlsdGluVHlwZS5PdGhlcjpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLkJvb2xlYW4gPDwgOCB8IEJ1aWx0aW5UeXBlLkFueTpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLkJvb2xlYW4gPDwgOCB8IEJ1aWx0aW5UeXBlLkJvb2xlYW46XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5OdW1iZXIgPDwgOCB8IEJ1aWx0aW5UeXBlLkFueTpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLk51bWJlciA8PCA4IHwgQnVpbHRpblR5cGUuTnVtYmVyOlxuICAgICAgICAgIGNhc2UgQnVpbHRpblR5cGUuU3RyaW5nIDw8IDggfCBCdWlsdGluVHlwZS5Bbnk6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5TdHJpbmcgPDwgOCB8IEJ1aWx0aW5UeXBlLlN0cmluZzpcbiAgICAgICAgICBjYXNlIEJ1aWx0aW5UeXBlLk90aGVyIDw8IDggfCBCdWlsdGluVHlwZS5Bbnk6XG4gICAgICAgICAgY2FzZSBCdWlsdGluVHlwZS5PdGhlciA8PCA4IHwgQnVpbHRpblR5cGUuT3RoZXI6XG4gICAgICAgICAgICByZXR1cm4gdGhpcy5xdWVyeS5nZXRCdWlsdGluVHlwZShCdWlsdGluVHlwZS5Cb29sZWFuKTtcbiAgICAgICAgICBkZWZhdWx0OlxuICAgICAgICAgICAgcmV0dXJuIHRoaXMucmVwb3J0RXJyb3IoJ0V4cGVjdGVkIHRoZSBvcGVyYW50cyB0byBiZSBvZiBzaW1pbGFyIHR5cGUgb3IgYW55JywgYXN0KTtcbiAgICAgICAgfVxuICAgICAgY2FzZSAnJiYnOlxuICAgICAgICByZXR1cm4gcmlnaHRUeXBlO1xuICAgICAgY2FzZSAnfHwnOlxuICAgICAgICByZXR1cm4gdGhpcy5xdWVyeS5nZXRUeXBlVW5pb24obGVmdFR5cGUsIHJpZ2h0VHlwZSk7XG4gICAgfVxuXG4gICAgcmV0dXJuIHRoaXMucmVwb3J0RXJyb3IoYFVucmVjb2duaXplZCBvcGVyYXRvciAke2FzdC5vcGVyYXRpb259YCwgYXN0KTtcbiAgfVxuXG4gIHZpc2l0Q2hhaW4oYXN0OiBDaGFpbikge1xuICAgIGlmICh0aGlzLmRpYWdub3N0aWNzKSB7XG4gICAgICAvLyBJZiB3ZSBhcmUgcHJvZHVjaW5nIGRpYWdub3N0aWNzLCB2aXNpdCB0aGUgY2hpbGRyZW5cbiAgICAgIHZpc2l0QXN0Q2hpbGRyZW4oYXN0LCB0aGlzKTtcbiAgICB9XG4gICAgLy8gVGhlIHR5cGUgb2YgYSBjaGFpbiBpcyBhbHdheXMgdW5kZWZpbmVkLlxuICAgIHJldHVybiB0aGlzLnF1ZXJ5LmdldEJ1aWx0aW5UeXBlKEJ1aWx0aW5UeXBlLlVuZGVmaW5lZCk7XG4gIH1cblxuICB2aXNpdENvbmRpdGlvbmFsKGFzdDogQ29uZGl0aW9uYWwpIHtcbiAgICAvLyBUaGUgdHlwZSBvZiBhIGNvbmRpdGlvbmFsIGlzIHRoZSB1bmlvbiBvZiB0aGUgdHJ1ZSBhbmQgZmFsc2UgY29uZGl0aW9ucy5cbiAgICBpZiAodGhpcy5kaWFnbm9zdGljcykge1xuICAgICAgdmlzaXRBc3RDaGlsZHJlbihhc3QsIHRoaXMpO1xuICAgIH1cbiAgICByZXR1cm4gdGhpcy5xdWVyeS5nZXRUeXBlVW5pb24odGhpcy5nZXRUeXBlKGFzdC50cnVlRXhwKSwgdGhpcy5nZXRUeXBlKGFzdC5mYWxzZUV4cCkpO1xuICB9XG5cbiAgdmlzaXRGdW5jdGlvbkNhbGwoYXN0OiBGdW5jdGlvbkNhbGwpIHtcbiAgICAvLyBUaGUgdHlwZSBvZiBhIGZ1bmN0aW9uIGNhbGwgaXMgdGhlIHJldHVybiB0eXBlIG9mIHRoZSBzZWxlY3RlZCBzaWduYXR1cmUuXG4gICAgLy8gVGhlIHNpZ25hdHVyZSBpcyBzZWxlY3RlZCBiYXNlZCBvbiB0aGUgdHlwZXMgb2YgdGhlIGFyZ3VtZW50cy4gQW5ndWxhciBkb2Vzbid0XG4gICAgLy8gc3VwcG9ydCBjb250ZXh0dWFsIHR5cGluZyBvZiBhcmd1bWVudHMgc28gdGhpcyBpcyBzaW1wbGVyIHRoYW4gVHlwZVNjcmlwdCdzXG4gICAgLy8gdmVyc2lvbi5cbiAgICBjb25zdCBhcmdzID0gYXN0LmFyZ3MubWFwKGFyZyA9PiB0aGlzLmdldFR5cGUoYXJnKSk7XG4gICAgY29uc3QgdGFyZ2V0ID0gdGhpcy5nZXRUeXBlKGFzdC50YXJnZXQgISk7XG4gICAgaWYgKCF0YXJnZXQgfHwgIXRhcmdldC5jYWxsYWJsZSkgcmV0dXJuIHRoaXMucmVwb3J0RXJyb3IoJ0NhbGwgdGFyZ2V0IGlzIG5vdCBjYWxsYWJsZScsIGFzdCk7XG4gICAgY29uc3Qgc2lnbmF0dXJlID0gdGFyZ2V0LnNlbGVjdFNpZ25hdHVyZShhcmdzKTtcbiAgICBpZiAoc2lnbmF0dXJlKSByZXR1cm4gc2lnbmF0dXJlLnJlc3VsdDtcbiAgICAvLyBUT0RPOiBDb25zaWRlciBhIGJldHRlciBlcnJvciBtZXNzYWdlIGhlcmUuXG4gICAgcmV0dXJuIHRoaXMucmVwb3J0RXJyb3IoJ1VuYWJsZSBubyBjb21wYXRpYmxlIHNpZ25hdHVyZSBmb3VuZCBmb3IgY2FsbCcsIGFzdCk7XG4gIH1cblxuICB2aXNpdEltcGxpY2l0UmVjZWl2ZXIoYXN0OiBJbXBsaWNpdFJlY2VpdmVyKTogU3ltYm9sIHtcbiAgICBjb25zdCBfdGhpcyA9IHRoaXM7XG4gICAgLy8gUmV0dXJuIGEgcHNldWRvLXN5bWJvbCBmb3IgdGhlIGltcGxpY2l0IHJlY2VpdmVyLlxuICAgIC8vIFRoZSBtZW1iZXJzIG9mIHRoZSBpbXBsaWNpdCByZWNlaXZlciBhcmUgd2hhdCBpcyBkZWZpbmVkIGJ5IHRoZVxuICAgIC8vIHNjb3BlIHBhc3NlZCBpbnRvIHRoaXMgY2xhc3MuXG4gICAgcmV0dXJuIHtcbiAgICAgIG5hbWU6ICckaW1wbGljaXQnLFxuICAgICAga2luZDogJ2NvbXBvbmVudCcsXG4gICAgICBsYW5ndWFnZTogJ25nLXRlbXBsYXRlJyxcbiAgICAgIHR5cGU6IHVuZGVmaW5lZCxcbiAgICAgIGNvbnRhaW5lcjogdW5kZWZpbmVkLFxuICAgICAgY2FsbGFibGU6IGZhbHNlLFxuICAgICAgbnVsbGFibGU6IGZhbHNlLFxuICAgICAgcHVibGljOiB0cnVlLFxuICAgICAgZGVmaW5pdGlvbjogdW5kZWZpbmVkLFxuICAgICAgbWVtYmVycygpOiBTeW1ib2xUYWJsZXtyZXR1cm4gX3RoaXMuc2NvcGU7fSxcbiAgICAgIHNpZ25hdHVyZXMoKTogU2lnbmF0dXJlW117cmV0dXJuIFtdO30sXG4gICAgICBzZWxlY3RTaWduYXR1cmUodHlwZXMpOiBTaWduYXR1cmUgfCB1bmRlZmluZWR7cmV0dXJuIHVuZGVmaW5lZDt9LFxuICAgICAgaW5kZXhlZChhcmd1bWVudCk6IFN5bWJvbCB8IHVuZGVmaW5lZHtyZXR1cm4gdW5kZWZpbmVkO31cbiAgICB9O1xuICB9XG5cbiAgdmlzaXRJbnRlcnBvbGF0aW9uKGFzdDogSW50ZXJwb2xhdGlvbik6IFN5bWJvbCB7XG4gICAgLy8gSWYgd2UgYXJlIHByb2R1Y2luZyBkaWFnbm9zdGljcywgdmlzaXQgdGhlIGNoaWxkcmVuLlxuICAgIGlmICh0aGlzLmRpYWdub3N0aWNzKSB7XG4gICAgICB2aXNpdEFzdENoaWxkcmVuKGFzdCwgdGhpcyk7XG4gICAgfVxuICAgIHJldHVybiB0aGlzLnVuZGVmaW5lZFR5cGU7XG4gIH1cblxuICB2aXNpdEtleWVkUmVhZChhc3Q6IEtleWVkUmVhZCk6IFN5bWJvbCB7XG4gICAgY29uc3QgdGFyZ2V0VHlwZSA9IHRoaXMuZ2V0VHlwZShhc3Qub2JqKTtcbiAgICBjb25zdCBrZXlUeXBlID0gdGhpcy5nZXRUeXBlKGFzdC5rZXkpO1xuICAgIGNvbnN0IHJlc3VsdCA9IHRhcmdldFR5cGUuaW5kZXhlZChcbiAgICAgICAga2V5VHlwZSwgYXN0LmtleSBpbnN0YW5jZW9mIExpdGVyYWxQcmltaXRpdmUgPyBhc3Qua2V5LnZhbHVlIDogdW5kZWZpbmVkKTtcbiAgICByZXR1cm4gcmVzdWx0IHx8IHRoaXMuYW55VHlwZTtcbiAgfVxuXG4gIHZpc2l0S2V5ZWRXcml0ZShhc3Q6IEtleWVkV3JpdGUpOiBTeW1ib2wge1xuICAgIC8vIFRoZSB3cml0ZSBvZiBhIHR5cGUgaXMgdGhlIHR5cGUgb2YgdGhlIHZhbHVlIGJlaW5nIHdyaXR0ZW4uXG4gICAgcmV0dXJuIHRoaXMuZ2V0VHlwZShhc3QudmFsdWUpO1xuICB9XG5cbiAgdmlzaXRMaXRlcmFsQXJyYXkoYXN0OiBMaXRlcmFsQXJyYXkpOiBTeW1ib2wge1xuICAgIC8vIEEgdHlwZSBsaXRlcmFsIGlzIGFuIGFycmF5IHR5cGUgb2YgdGhlIHVuaW9uIG9mIHRoZSBlbGVtZW50c1xuICAgIHJldHVybiB0aGlzLnF1ZXJ5LmdldEFycmF5VHlwZShcbiAgICAgICAgdGhpcy5xdWVyeS5nZXRUeXBlVW5pb24oLi4uYXN0LmV4cHJlc3Npb25zLm1hcChlbGVtZW50ID0+IHRoaXMuZ2V0VHlwZShlbGVtZW50KSkpKTtcbiAgfVxuXG4gIHZpc2l0TGl0ZXJhbE1hcChhc3Q6IExpdGVyYWxNYXApOiBTeW1ib2wge1xuICAgIC8vIElmIHdlIGFyZSBwcm9kdWNpbmcgZGlhZ25vc3RpY3MsIHZpc2l0IHRoZSBjaGlsZHJlblxuICAgIGlmICh0aGlzLmRpYWdub3N0aWNzKSB7XG4gICAgICB2aXNpdEFzdENoaWxkcmVuKGFzdCwgdGhpcyk7XG4gICAgfVxuICAgIC8vIFRPRE86IFJldHVybiBhIGNvbXBvc2l0ZSB0eXBlLlxuICAgIHJldHVybiB0aGlzLmFueVR5cGU7XG4gIH1cblxuICB2aXNpdExpdGVyYWxQcmltaXRpdmUoYXN0OiBMaXRlcmFsUHJpbWl0aXZlKSB7XG4gICAgLy8gVGhlIHR5cGUgb2YgYSBsaXRlcmFsIHByaW1pdGl2ZSBkZXBlbmRzIG9uIHRoZSB2YWx1ZSBvZiB0aGUgbGl0ZXJhbC5cbiAgICBzd2l0Y2ggKGFzdC52YWx1ZSkge1xuICAgICAgY2FzZSB0cnVlOlxuICAgICAgY2FzZSBmYWxzZTpcbiAgICAgICAgcmV0dXJuIHRoaXMucXVlcnkuZ2V0QnVpbHRpblR5cGUoQnVpbHRpblR5cGUuQm9vbGVhbik7XG4gICAgICBjYXNlIG51bGw6XG4gICAgICAgIHJldHVybiB0aGlzLnF1ZXJ5LmdldEJ1aWx0aW5UeXBlKEJ1aWx0aW5UeXBlLk51bGwpO1xuICAgICAgY2FzZSB1bmRlZmluZWQ6XG4gICAgICAgIHJldHVybiB0aGlzLnF1ZXJ5LmdldEJ1aWx0aW5UeXBlKEJ1aWx0aW5UeXBlLlVuZGVmaW5lZCk7XG4gICAgICBkZWZhdWx0OlxuICAgICAgICBzd2l0Y2ggKHR5cGVvZiBhc3QudmFsdWUpIHtcbiAgICAgICAgICBjYXNlICdzdHJpbmcnOlxuICAgICAgICAgICAgcmV0dXJuIHRoaXMucXVlcnkuZ2V0QnVpbHRpblR5cGUoQnVpbHRpblR5cGUuU3RyaW5nKTtcbiAgICAgICAgICBjYXNlICdudW1iZXInOlxuICAgICAgICAgICAgcmV0dXJuIHRoaXMucXVlcnkuZ2V0QnVpbHRpblR5cGUoQnVpbHRpblR5cGUuTnVtYmVyKTtcbiAgICAgICAgICBkZWZhdWx0OlxuICAgICAgICAgICAgcmV0dXJuIHRoaXMucmVwb3J0RXJyb3IoJ1VucmVjb2duaXplZCBwcmltaXRpdmUnLCBhc3QpO1xuICAgICAgICB9XG4gICAgfVxuICB9XG5cbiAgdmlzaXRNZXRob2RDYWxsKGFzdDogTWV0aG9kQ2FsbCkge1xuICAgIHJldHVybiB0aGlzLnJlc29sdmVNZXRob2RDYWxsKHRoaXMuZ2V0VHlwZShhc3QucmVjZWl2ZXIpLCBhc3QpO1xuICB9XG5cbiAgdmlzaXRQaXBlKGFzdDogQmluZGluZ1BpcGUpIHtcbiAgICAvLyBUaGUgdHlwZSBvZiBhIHBpcGUgbm9kZSBpcyB0aGUgcmV0dXJuIHR5cGUgb2YgdGhlIHBpcGUncyB0cmFuc2Zvcm0gbWV0aG9kLiBUaGUgdGFibGUgcmV0dXJuZWRcbiAgICAvLyBieSBnZXRQaXBlcygpIGlzIGV4cGVjdGVkIHRvIGNvbnRhaW4gc3ltYm9scyB3aXRoIHRoZSBjb3JyZXNwb25kaW5nIHRyYW5zZm9ybSBtZXRob2QgdHlwZS5cbiAgICBjb25zdCBwaXBlID0gdGhpcy5xdWVyeS5nZXRQaXBlcygpLmdldChhc3QubmFtZSk7XG4gICAgaWYgKCFwaXBlKSByZXR1cm4gdGhpcy5yZXBvcnRFcnJvcihgTm8gcGlwZSBieSB0aGUgbmFtZSAke2FzdC5uYW1lfSBmb3VuZGAsIGFzdCk7XG4gICAgY29uc3QgZXhwVHlwZSA9IHRoaXMuZ2V0VHlwZShhc3QuZXhwKTtcbiAgICBjb25zdCBzaWduYXR1cmUgPVxuICAgICAgICBwaXBlLnNlbGVjdFNpZ25hdHVyZShbZXhwVHlwZV0uY29uY2F0KGFzdC5hcmdzLm1hcChhcmcgPT4gdGhpcy5nZXRUeXBlKGFyZykpKSk7XG4gICAgaWYgKCFzaWduYXR1cmUpIHJldHVybiB0aGlzLnJlcG9ydEVycm9yKCdVbmFibGUgdG8gcmVzb2x2ZSBzaWduYXR1cmUgZm9yIHBpcGUgaW52b2NhdGlvbicsIGFzdCk7XG4gICAgcmV0dXJuIHNpZ25hdHVyZS5yZXN1bHQ7XG4gIH1cblxuICB2aXNpdFByZWZpeE5vdChhc3Q6IFByZWZpeE5vdCkge1xuICAgIC8vIElmIHdlIGFyZSBwcm9kdWNpbmcgZGlhZ25vc3RpY3MsIHZpc2l0IHRoZSBjaGlsZHJlblxuICAgIGlmICh0aGlzLmRpYWdub3N0aWNzKSB7XG4gICAgICB2aXNpdEFzdENoaWxkcmVuKGFzdCwgdGhpcyk7XG4gICAgfVxuICAgIC8vIFRoZSB0eXBlIG9mIGEgcHJlZml4ICEgaXMgYWx3YXlzIGJvb2xlYW4uXG4gICAgcmV0dXJuIHRoaXMucXVlcnkuZ2V0QnVpbHRpblR5cGUoQnVpbHRpblR5cGUuQm9vbGVhbik7XG4gIH1cblxuICB2aXNpdE5vbk51bGxBc3NlcnQoYXN0OiBOb25OdWxsQXNzZXJ0KSB7XG4gICAgY29uc3QgZXhwcmVzc2lvblR5cGUgPSB0aGlzLmdldFR5cGUoYXN0LmV4cHJlc3Npb24pO1xuICAgIHJldHVybiB0aGlzLnF1ZXJ5LmdldE5vbk51bGxhYmxlVHlwZShleHByZXNzaW9uVHlwZSk7XG4gIH1cblxuICB2aXNpdFByb3BlcnR5UmVhZChhc3Q6IFByb3BlcnR5UmVhZCkge1xuICAgIHJldHVybiB0aGlzLnJlc29sdmVQcm9wZXJ0eVJlYWQodGhpcy5nZXRUeXBlKGFzdC5yZWNlaXZlciksIGFzdCk7XG4gIH1cblxuICB2aXNpdFByb3BlcnR5V3JpdGUoYXN0OiBQcm9wZXJ0eVdyaXRlKSB7XG4gICAgLy8gVGhlIHR5cGUgb2YgYSB3cml0ZSBpcyB0aGUgdHlwZSBvZiB0aGUgdmFsdWUgYmVpbmcgd3JpdHRlbi5cbiAgICByZXR1cm4gdGhpcy5nZXRUeXBlKGFzdC52YWx1ZSk7XG4gIH1cblxuICB2aXNpdFF1b3RlKGFzdDogUXVvdGUpIHtcbiAgICAvLyBUaGUgdHlwZSBvZiBhIHF1b3RlZCBleHByZXNzaW9uIGlzIGFueS5cbiAgICByZXR1cm4gdGhpcy5xdWVyeS5nZXRCdWlsdGluVHlwZShCdWlsdGluVHlwZS5BbnkpO1xuICB9XG5cbiAgdmlzaXRTYWZlTWV0aG9kQ2FsbChhc3Q6IFNhZmVNZXRob2RDYWxsKSB7XG4gICAgcmV0dXJuIHRoaXMucmVzb2x2ZU1ldGhvZENhbGwodGhpcy5xdWVyeS5nZXROb25OdWxsYWJsZVR5cGUodGhpcy5nZXRUeXBlKGFzdC5yZWNlaXZlcikpLCBhc3QpO1xuICB9XG5cbiAgdmlzaXRTYWZlUHJvcGVydHlSZWFkKGFzdDogU2FmZVByb3BlcnR5UmVhZCkge1xuICAgIHJldHVybiB0aGlzLnJlc29sdmVQcm9wZXJ0eVJlYWQodGhpcy5xdWVyeS5nZXROb25OdWxsYWJsZVR5cGUodGhpcy5nZXRUeXBlKGFzdC5yZWNlaXZlcikpLCBhc3QpO1xuICB9XG5cbiAgLy8gVE9ETyhpc3N1ZS8yNDU3MSk6IHJlbW92ZSAnIScuXG4gIHByaXZhdGUgX2FueVR5cGUgITogU3ltYm9sO1xuICBwcml2YXRlIGdldCBhbnlUeXBlKCk6IFN5bWJvbCB7XG4gICAgbGV0IHJlc3VsdCA9IHRoaXMuX2FueVR5cGU7XG4gICAgaWYgKCFyZXN1bHQpIHtcbiAgICAgIHJlc3VsdCA9IHRoaXMuX2FueVR5cGUgPSB0aGlzLnF1ZXJ5LmdldEJ1aWx0aW5UeXBlKEJ1aWx0aW5UeXBlLkFueSk7XG4gICAgfVxuICAgIHJldHVybiByZXN1bHQ7XG4gIH1cblxuICAvLyBUT0RPKGlzc3VlLzI0NTcxKTogcmVtb3ZlICchJy5cbiAgcHJpdmF0ZSBfdW5kZWZpbmVkVHlwZSAhOiBTeW1ib2w7XG4gIHByaXZhdGUgZ2V0IHVuZGVmaW5lZFR5cGUoKTogU3ltYm9sIHtcbiAgICBsZXQgcmVzdWx0ID0gdGhpcy5fdW5kZWZpbmVkVHlwZTtcbiAgICBpZiAoIXJlc3VsdCkge1xuICAgICAgcmVzdWx0ID0gdGhpcy5fdW5kZWZpbmVkVHlwZSA9IHRoaXMucXVlcnkuZ2V0QnVpbHRpblR5cGUoQnVpbHRpblR5cGUuVW5kZWZpbmVkKTtcbiAgICB9XG4gICAgcmV0dXJuIHJlc3VsdDtcbiAgfVxuXG4gIHByaXZhdGUgcmVzb2x2ZU1ldGhvZENhbGwocmVjZWl2ZXJUeXBlOiBTeW1ib2wsIGFzdDogU2FmZU1ldGhvZENhbGx8TWV0aG9kQ2FsbCkge1xuICAgIGlmICh0aGlzLmlzQW55KHJlY2VpdmVyVHlwZSkpIHtcbiAgICAgIHJldHVybiB0aGlzLmFueVR5cGU7XG4gICAgfVxuXG4gICAgLy8gVGhlIHR5cGUgb2YgYSBtZXRob2QgaXMgdGhlIHNlbGVjdGVkIG1ldGhvZHMgcmVzdWx0IHR5cGUuXG4gICAgY29uc3QgbWV0aG9kID0gcmVjZWl2ZXJUeXBlLm1lbWJlcnMoKS5nZXQoYXN0Lm5hbWUpO1xuICAgIGlmICghbWV0aG9kKSByZXR1cm4gdGhpcy5yZXBvcnRFcnJvcihgVW5rbm93biBtZXRob2QgJyR7YXN0Lm5hbWV9J2AsIGFzdCk7XG4gICAgaWYgKCFtZXRob2QudHlwZSkgcmV0dXJuIHRoaXMucmVwb3J0RXJyb3IoYENvdWxkIG5vdCBmaW5kIGEgdHlwZSBmb3IgJyR7YXN0Lm5hbWV9J2AsIGFzdCk7XG4gICAgaWYgKCFtZXRob2QudHlwZS5jYWxsYWJsZSkgcmV0dXJuIHRoaXMucmVwb3J0RXJyb3IoYE1lbWJlciAnJHthc3QubmFtZX0nIGlzIG5vdCBjYWxsYWJsZWAsIGFzdCk7XG4gICAgY29uc3Qgc2lnbmF0dXJlID0gbWV0aG9kLnR5cGUuc2VsZWN0U2lnbmF0dXJlKGFzdC5hcmdzLm1hcChhcmcgPT4gdGhpcy5nZXRUeXBlKGFyZykpKTtcbiAgICBpZiAoIXNpZ25hdHVyZSlcbiAgICAgIHJldHVybiB0aGlzLnJlcG9ydEVycm9yKGBVbmFibGUgdG8gcmVzb2x2ZSBzaWduYXR1cmUgZm9yIGNhbGwgb2YgbWV0aG9kICR7YXN0Lm5hbWV9YCwgYXN0KTtcbiAgICByZXR1cm4gc2lnbmF0dXJlLnJlc3VsdDtcbiAgfVxuXG4gIHByaXZhdGUgcmVzb2x2ZVByb3BlcnR5UmVhZChyZWNlaXZlclR5cGU6IFN5bWJvbCwgYXN0OiBTYWZlUHJvcGVydHlSZWFkfFByb3BlcnR5UmVhZCkge1xuICAgIGlmICh0aGlzLmlzQW55KHJlY2VpdmVyVHlwZSkpIHtcbiAgICAgIHJldHVybiB0aGlzLmFueVR5cGU7XG4gICAgfVxuXG4gICAgLy8gVGhlIHR5cGUgb2YgYSBwcm9wZXJ0eSByZWFkIGlzIHRoZSBzZWVsY3RlZCBtZW1iZXIncyB0eXBlLlxuICAgIGNvbnN0IG1lbWJlciA9IHJlY2VpdmVyVHlwZS5tZW1iZXJzKCkuZ2V0KGFzdC5uYW1lKTtcbiAgICBpZiAoIW1lbWJlcikge1xuICAgICAgbGV0IHJlY2VpdmVySW5mbyA9IHJlY2VpdmVyVHlwZS5uYW1lO1xuICAgICAgaWYgKHJlY2VpdmVySW5mbyA9PSAnJGltcGxpY2l0Jykge1xuICAgICAgICByZWNlaXZlckluZm8gPVxuICAgICAgICAgICAgJ1RoZSBjb21wb25lbnQgZGVjbGFyYXRpb24sIHRlbXBsYXRlIHZhcmlhYmxlIGRlY2xhcmF0aW9ucywgYW5kIGVsZW1lbnQgcmVmZXJlbmNlcyBkbyc7XG4gICAgICB9IGVsc2UgaWYgKHJlY2VpdmVyVHlwZS5udWxsYWJsZSkge1xuICAgICAgICByZXR1cm4gdGhpcy5yZXBvcnRFcnJvcihgVGhlIGV4cHJlc3Npb24gbWlnaHQgYmUgbnVsbGAsIGFzdC5yZWNlaXZlcik7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICByZWNlaXZlckluZm8gPSBgJyR7cmVjZWl2ZXJJbmZvfScgZG9lc2A7XG4gICAgICB9XG4gICAgICByZXR1cm4gdGhpcy5yZXBvcnRFcnJvcihcbiAgICAgICAgICBgSWRlbnRpZmllciAnJHthc3QubmFtZX0nIGlzIG5vdCBkZWZpbmVkLiAke3JlY2VpdmVySW5mb30gbm90IGNvbnRhaW4gc3VjaCBhIG1lbWJlcmAsXG4gICAgICAgICAgYXN0KTtcbiAgICB9XG4gICAgaWYgKCFtZW1iZXIucHVibGljKSB7XG4gICAgICBsZXQgcmVjZWl2ZXJJbmZvID0gcmVjZWl2ZXJUeXBlLm5hbWU7XG4gICAgICBpZiAocmVjZWl2ZXJJbmZvID09ICckaW1wbGljaXQnKSB7XG4gICAgICAgIHJlY2VpdmVySW5mbyA9ICd0aGUgY29tcG9uZW50JztcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIHJlY2VpdmVySW5mbyA9IGAnJHtyZWNlaXZlckluZm99J2A7XG4gICAgICB9XG4gICAgICB0aGlzLnJlcG9ydFdhcm5pbmcoXG4gICAgICAgICAgYElkZW50aWZpZXIgJyR7YXN0Lm5hbWV9JyByZWZlcnMgdG8gYSBwcml2YXRlIG1lbWJlciBvZiAke3JlY2VpdmVySW5mb31gLCBhc3QpO1xuICAgIH1cbiAgICByZXR1cm4gbWVtYmVyLnR5cGU7XG4gIH1cblxuICBwcml2YXRlIHJlcG9ydEVycm9yKG1lc3NhZ2U6IHN0cmluZywgYXN0OiBBU1QpOiBTeW1ib2wge1xuICAgIGlmICh0aGlzLmRpYWdub3N0aWNzKSB7XG4gICAgICB0aGlzLmRpYWdub3N0aWNzLnB1c2gobmV3IFR5cGVEaWFnbm9zdGljKERpYWdub3N0aWNLaW5kLkVycm9yLCBtZXNzYWdlLCBhc3QpKTtcbiAgICB9XG4gICAgcmV0dXJuIHRoaXMuYW55VHlwZTtcbiAgfVxuXG4gIHByaXZhdGUgcmVwb3J0V2FybmluZyhtZXNzYWdlOiBzdHJpbmcsIGFzdDogQVNUKTogU3ltYm9sIHtcbiAgICBpZiAodGhpcy5kaWFnbm9zdGljcykge1xuICAgICAgdGhpcy5kaWFnbm9zdGljcy5wdXNoKG5ldyBUeXBlRGlhZ25vc3RpYyhEaWFnbm9zdGljS2luZC5XYXJuaW5nLCBtZXNzYWdlLCBhc3QpKTtcbiAgICB9XG4gICAgcmV0dXJuIHRoaXMuYW55VHlwZTtcbiAgfVxuXG4gIHByaXZhdGUgaXNBbnkoc3ltYm9sOiBTeW1ib2wpOiBib29sZWFuIHtcbiAgICByZXR1cm4gIXN5bWJvbCB8fCB0aGlzLnF1ZXJ5LmdldFR5cGVLaW5kKHN5bWJvbCkgPT0gQnVpbHRpblR5cGUuQW55IHx8XG4gICAgICAgICghIXN5bWJvbC50eXBlICYmIHRoaXMuaXNBbnkoc3ltYm9sLnR5cGUpKTtcbiAgfVxufVxuIl19