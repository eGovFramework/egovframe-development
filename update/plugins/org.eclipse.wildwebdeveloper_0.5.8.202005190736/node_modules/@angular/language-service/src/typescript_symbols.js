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
        define("@angular/language-service/src/typescript_symbols", ["require", "exports", "tslib", "path", "typescript", "@angular/language-service/src/symbols"], factory);
    }
})(function (require, exports) {
    "use strict";
    Object.defineProperty(exports, "__esModule", { value: true });
    var tslib_1 = require("tslib");
    var path = require("path");
    var ts = require("typescript");
    var symbols_1 = require("@angular/language-service/src/symbols");
    // In TypeScript 2.1 these flags moved
    // These helpers work for both 2.0 and 2.1.
    var isPrivate = ts.ModifierFlags ?
        (function (node) {
            return !!(ts.getCombinedModifierFlags(node) & ts.ModifierFlags.Private);
        }) :
        (function (node) { return !!(node.flags & ts.NodeFlags.Private); });
    var isReferenceType = ts.ObjectFlags ?
        (function (type) {
            return !!(type.flags & ts.TypeFlags.Object &&
                type.objectFlags & ts.ObjectFlags.Reference);
        }) :
        (function (type) { return !!(type.flags & ts.TypeFlags.Reference); });
    function getSymbolQuery(program, checker, source, fetchPipes) {
        return new TypeScriptSymbolQuery(program, checker, source, fetchPipes);
    }
    exports.getSymbolQuery = getSymbolQuery;
    function getClassMembers(program, checker, staticSymbol) {
        var declaration = getClassFromStaticSymbol(program, staticSymbol);
        if (declaration) {
            var type = checker.getTypeAtLocation(declaration);
            var node = program.getSourceFile(staticSymbol.filePath);
            if (node) {
                return new TypeWrapper(type, { node: node, program: program, checker: checker }).members();
            }
        }
    }
    exports.getClassMembers = getClassMembers;
    function getClassMembersFromDeclaration(program, checker, source, declaration) {
        var type = checker.getTypeAtLocation(declaration);
        return new TypeWrapper(type, { node: source, program: program, checker: checker }).members();
    }
    exports.getClassMembersFromDeclaration = getClassMembersFromDeclaration;
    function getClassFromStaticSymbol(program, type) {
        var source = program.getSourceFile(type.filePath);
        if (source) {
            return ts.forEachChild(source, function (child) {
                if (child.kind === ts.SyntaxKind.ClassDeclaration) {
                    var classDeclaration = child;
                    if (classDeclaration.name != null && classDeclaration.name.text === type.name) {
                        return classDeclaration;
                    }
                }
            });
        }
        return undefined;
    }
    exports.getClassFromStaticSymbol = getClassFromStaticSymbol;
    function getPipesTable(source, program, checker, pipes) {
        return new PipesTable(pipes, { program: program, checker: checker, node: source });
    }
    exports.getPipesTable = getPipesTable;
    var TypeScriptSymbolQuery = /** @class */ (function () {
        function TypeScriptSymbolQuery(program, checker, source, fetchPipes) {
            this.program = program;
            this.checker = checker;
            this.source = source;
            this.fetchPipes = fetchPipes;
            this.typeCache = new Map();
        }
        TypeScriptSymbolQuery.prototype.getTypeKind = function (symbol) { return typeKindOf(this.getTsTypeOf(symbol)); };
        TypeScriptSymbolQuery.prototype.getBuiltinType = function (kind) {
            var result = this.typeCache.get(kind);
            if (!result) {
                var type = getTsTypeFromBuiltinType(kind, {
                    checker: this.checker,
                    node: this.source,
                    program: this.program,
                });
                result =
                    new TypeWrapper(type, { program: this.program, checker: this.checker, node: this.source });
                this.typeCache.set(kind, result);
            }
            return result;
        };
        TypeScriptSymbolQuery.prototype.getTypeUnion = function () {
            var types = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                types[_i] = arguments[_i];
            }
            // No API exists so return any if the types are not all the same type.
            var result = undefined;
            if (types.length) {
                result = types[0];
                for (var i = 1; i < types.length; i++) {
                    if (types[i] != result) {
                        result = undefined;
                        break;
                    }
                }
            }
            return result || this.getBuiltinType(symbols_1.BuiltinType.Any);
        };
        TypeScriptSymbolQuery.prototype.getArrayType = function (type) { return this.getBuiltinType(symbols_1.BuiltinType.Any); };
        TypeScriptSymbolQuery.prototype.getElementType = function (type) {
            if (type instanceof TypeWrapper) {
                var elementType = getTypeParameterOf(type.tsType, 'Array');
                if (elementType) {
                    return new TypeWrapper(elementType, type.context);
                }
            }
        };
        TypeScriptSymbolQuery.prototype.getNonNullableType = function (symbol) {
            if (symbol instanceof TypeWrapper && (typeof this.checker.getNonNullableType == 'function')) {
                var tsType = symbol.tsType;
                var nonNullableType = this.checker.getNonNullableType(tsType);
                if (nonNullableType != tsType) {
                    return new TypeWrapper(nonNullableType, symbol.context);
                }
                else if (nonNullableType == tsType) {
                    return symbol;
                }
            }
            return this.getBuiltinType(symbols_1.BuiltinType.Any);
        };
        TypeScriptSymbolQuery.prototype.getPipes = function () {
            var result = this.pipesCache;
            if (!result) {
                result = this.pipesCache = this.fetchPipes();
            }
            return result;
        };
        TypeScriptSymbolQuery.prototype.getTemplateContext = function (type) {
            var context = { node: this.source, program: this.program, checker: this.checker };
            var typeSymbol = findClassSymbolInContext(type, context);
            if (typeSymbol) {
                var contextType = this.getTemplateRefContextType(typeSymbol);
                if (contextType)
                    return new SymbolWrapper(contextType, context).members();
            }
        };
        TypeScriptSymbolQuery.prototype.getTypeSymbol = function (type) {
            var context = { node: this.source, program: this.program, checker: this.checker };
            var typeSymbol = findClassSymbolInContext(type, context);
            return typeSymbol && new SymbolWrapper(typeSymbol, context);
        };
        TypeScriptSymbolQuery.prototype.createSymbolTable = function (symbols) {
            var result = new MapSymbolTable();
            result.addAll(symbols.map(function (s) { return new DeclaredSymbol(s); }));
            return result;
        };
        TypeScriptSymbolQuery.prototype.mergeSymbolTable = function (symbolTables) {
            var e_1, _a;
            var result = new MapSymbolTable();
            try {
                for (var symbolTables_1 = tslib_1.__values(symbolTables), symbolTables_1_1 = symbolTables_1.next(); !symbolTables_1_1.done; symbolTables_1_1 = symbolTables_1.next()) {
                    var symbolTable = symbolTables_1_1.value;
                    result.addAll(symbolTable.values());
                }
            }
            catch (e_1_1) { e_1 = { error: e_1_1 }; }
            finally {
                try {
                    if (symbolTables_1_1 && !symbolTables_1_1.done && (_a = symbolTables_1.return)) _a.call(symbolTables_1);
                }
                finally { if (e_1) throw e_1.error; }
            }
            return result;
        };
        TypeScriptSymbolQuery.prototype.getSpanAt = function (line, column) {
            return spanAt(this.source, line, column);
        };
        TypeScriptSymbolQuery.prototype.getTemplateRefContextType = function (typeSymbol) {
            var e_2, _a;
            var type = this.checker.getTypeOfSymbolAtLocation(typeSymbol, this.source);
            var constructor = type.symbol && type.symbol.members &&
                getFromSymbolTable(type.symbol.members, '__constructor');
            if (constructor) {
                var constructorDeclaration = constructor.declarations[0];
                try {
                    for (var _b = tslib_1.__values(constructorDeclaration.parameters), _c = _b.next(); !_c.done; _c = _b.next()) {
                        var parameter = _c.value;
                        var type_1 = this.checker.getTypeAtLocation(parameter.type);
                        if (type_1.symbol.name == 'TemplateRef' && isReferenceType(type_1)) {
                            var typeReference = type_1;
                            if (typeReference.typeArguments && typeReference.typeArguments.length === 1) {
                                return typeReference.typeArguments[0].symbol;
                            }
                        }
                    }
                }
                catch (e_2_1) { e_2 = { error: e_2_1 }; }
                finally {
                    try {
                        if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
                    }
                    finally { if (e_2) throw e_2.error; }
                }
            }
        };
        TypeScriptSymbolQuery.prototype.getTsTypeOf = function (symbol) {
            var type = this.getTypeWrapper(symbol);
            return type && type.tsType;
        };
        TypeScriptSymbolQuery.prototype.getTypeWrapper = function (symbol) {
            var type = undefined;
            if (symbol instanceof TypeWrapper) {
                type = symbol;
            }
            else if (symbol.type instanceof TypeWrapper) {
                type = symbol.type;
            }
            return type;
        };
        return TypeScriptSymbolQuery;
    }());
    function typeCallable(type) {
        var signatures = type.getCallSignatures();
        return signatures && signatures.length != 0;
    }
    function signaturesOf(type, context) {
        return type.getCallSignatures().map(function (s) { return new SignatureWrapper(s, context); });
    }
    function selectSignature(type, context, types) {
        // TODO: Do a better job of selecting the right signature.
        var signatures = type.getCallSignatures();
        return signatures.length ? new SignatureWrapper(signatures[0], context) : undefined;
    }
    var TypeWrapper = /** @class */ (function () {
        function TypeWrapper(tsType, context) {
            this.tsType = tsType;
            this.context = context;
            this.kind = 'type';
            this.language = 'typescript';
            this.type = undefined;
            this.container = undefined;
            this.public = true;
            if (!tsType) {
                throw Error('Internal: null type');
            }
        }
        Object.defineProperty(TypeWrapper.prototype, "name", {
            get: function () {
                var symbol = this.tsType.symbol;
                return (symbol && symbol.name) || '<anonymous>';
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(TypeWrapper.prototype, "callable", {
            get: function () { return typeCallable(this.tsType); },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(TypeWrapper.prototype, "nullable", {
            get: function () {
                return this.context.checker.getNonNullableType(this.tsType) != this.tsType;
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(TypeWrapper.prototype, "definition", {
            get: function () {
                var symbol = this.tsType.getSymbol();
                return symbol ? definitionFromTsSymbol(symbol) : undefined;
            },
            enumerable: true,
            configurable: true
        });
        TypeWrapper.prototype.members = function () {
            // Should call getApparentProperties() instead of getProperties() because
            // the former includes properties on the base class whereas the latter does
            // not. This provides properties like .bind(), .call(), .apply(), etc for
            // functions.
            return new SymbolTableWrapper(this.tsType.getApparentProperties(), this.context, this.tsType);
        };
        TypeWrapper.prototype.signatures = function () { return signaturesOf(this.tsType, this.context); };
        TypeWrapper.prototype.selectSignature = function (types) {
            return selectSignature(this.tsType, this.context, types);
        };
        TypeWrapper.prototype.indexed = function (argument, value) {
            var type = argument instanceof TypeWrapper ? argument : argument.type;
            if (!(type instanceof TypeWrapper))
                return;
            var typeKind = typeKindOf(type.tsType);
            switch (typeKind) {
                case symbols_1.BuiltinType.Number:
                    var nType = this.tsType.getNumberIndexType();
                    if (nType) {
                        // get the right tuple type by value, like 'var t: [number, string];'
                        if (nType.isUnion()) {
                            // return undefined if array index out of bound.
                            return nType.types[value] && new TypeWrapper(nType.types[value], this.context);
                        }
                        return new TypeWrapper(nType, this.context);
                    }
                    return undefined;
                case symbols_1.BuiltinType.String:
                    var sType = this.tsType.getStringIndexType();
                    return sType && new TypeWrapper(sType, this.context);
            }
        };
        return TypeWrapper;
    }());
    var SymbolWrapper = /** @class */ (function () {
        function SymbolWrapper(symbol, 
        /** TypeScript type context of the symbol. */
        context, 
        /** Type of the TypeScript symbol, if known. If not provided, the type of the symbol
        * will be determined dynamically; see `SymbolWrapper#tsType`. */
        _tsType) {
            this.context = context;
            this._tsType = _tsType;
            this.nullable = false;
            this.language = 'typescript';
            this.symbol = symbol && context && (symbol.flags & ts.SymbolFlags.Alias) ?
                context.checker.getAliasedSymbol(symbol) :
                symbol;
        }
        Object.defineProperty(SymbolWrapper.prototype, "name", {
            get: function () { return this.symbol.name; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(SymbolWrapper.prototype, "kind", {
            get: function () { return this.callable ? 'method' : 'property'; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(SymbolWrapper.prototype, "type", {
            get: function () { return new TypeWrapper(this.tsType, this.context); },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(SymbolWrapper.prototype, "container", {
            get: function () { return getContainerOf(this.symbol, this.context); },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(SymbolWrapper.prototype, "public", {
            get: function () {
                // Symbols that are not explicitly made private are public.
                return !isSymbolPrivate(this.symbol);
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(SymbolWrapper.prototype, "callable", {
            get: function () { return typeCallable(this.tsType); },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(SymbolWrapper.prototype, "definition", {
            get: function () { return definitionFromTsSymbol(this.symbol); },
            enumerable: true,
            configurable: true
        });
        SymbolWrapper.prototype.members = function () {
            if (!this._members) {
                if ((this.symbol.flags & (ts.SymbolFlags.Class | ts.SymbolFlags.Interface)) != 0) {
                    var declaredType = this.context.checker.getDeclaredTypeOfSymbol(this.symbol);
                    var typeWrapper = new TypeWrapper(declaredType, this.context);
                    this._members = typeWrapper.members();
                }
                else {
                    this._members = new SymbolTableWrapper(this.symbol.members, this.context, this.tsType);
                }
            }
            return this._members;
        };
        SymbolWrapper.prototype.signatures = function () { return signaturesOf(this.tsType, this.context); };
        SymbolWrapper.prototype.selectSignature = function (types) {
            return selectSignature(this.tsType, this.context, types);
        };
        SymbolWrapper.prototype.indexed = function (argument) { return undefined; };
        Object.defineProperty(SymbolWrapper.prototype, "tsType", {
            get: function () {
                var type = this._tsType;
                if (!type) {
                    type = this._tsType =
                        this.context.checker.getTypeOfSymbolAtLocation(this.symbol, this.context.node);
                }
                return type;
            },
            enumerable: true,
            configurable: true
        });
        return SymbolWrapper;
    }());
    var DeclaredSymbol = /** @class */ (function () {
        function DeclaredSymbol(declaration) {
            this.declaration = declaration;
            this.language = 'ng-template';
            this.nullable = false;
            this.public = true;
        }
        Object.defineProperty(DeclaredSymbol.prototype, "name", {
            get: function () { return this.declaration.name; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(DeclaredSymbol.prototype, "kind", {
            get: function () { return this.declaration.kind; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(DeclaredSymbol.prototype, "container", {
            get: function () { return undefined; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(DeclaredSymbol.prototype, "type", {
            get: function () { return this.declaration.type; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(DeclaredSymbol.prototype, "callable", {
            get: function () { return this.declaration.type.callable; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(DeclaredSymbol.prototype, "definition", {
            get: function () { return this.declaration.definition; },
            enumerable: true,
            configurable: true
        });
        DeclaredSymbol.prototype.members = function () { return this.declaration.type.members(); };
        DeclaredSymbol.prototype.signatures = function () { return this.declaration.type.signatures(); };
        DeclaredSymbol.prototype.selectSignature = function (types) {
            return this.declaration.type.selectSignature(types);
        };
        DeclaredSymbol.prototype.indexed = function (argument) { return undefined; };
        return DeclaredSymbol;
    }());
    var SignatureWrapper = /** @class */ (function () {
        function SignatureWrapper(signature, context) {
            this.signature = signature;
            this.context = context;
        }
        Object.defineProperty(SignatureWrapper.prototype, "arguments", {
            get: function () {
                return new SymbolTableWrapper(this.signature.getParameters(), this.context);
            },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(SignatureWrapper.prototype, "result", {
            get: function () { return new TypeWrapper(this.signature.getReturnType(), this.context); },
            enumerable: true,
            configurable: true
        });
        return SignatureWrapper;
    }());
    var SignatureResultOverride = /** @class */ (function () {
        function SignatureResultOverride(signature, resultType) {
            this.signature = signature;
            this.resultType = resultType;
        }
        Object.defineProperty(SignatureResultOverride.prototype, "arguments", {
            get: function () { return this.signature.arguments; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(SignatureResultOverride.prototype, "result", {
            get: function () { return this.resultType; },
            enumerable: true,
            configurable: true
        });
        return SignatureResultOverride;
    }());
    function toSymbolTableFactory(symbols) {
        var e_3, _a;
        // ∀ Typescript version >= 2.2, `SymbolTable` is implemented as an ES6 `Map`
        var result = new Map();
        try {
            for (var symbols_2 = tslib_1.__values(symbols), symbols_2_1 = symbols_2.next(); !symbols_2_1.done; symbols_2_1 = symbols_2.next()) {
                var symbol = symbols_2_1.value;
                result.set(symbol.name, symbol);
            }
        }
        catch (e_3_1) { e_3 = { error: e_3_1 }; }
        finally {
            try {
                if (symbols_2_1 && !symbols_2_1.done && (_a = symbols_2.return)) _a.call(symbols_2);
            }
            finally { if (e_3) throw e_3.error; }
        }
        // First, tell the compiler that `result` is of type `any`. Then, use a second type assertion
        // to `ts.SymbolTable`.
        // Otherwise, `Map<string, ts.Symbol>` and `ts.SymbolTable` will be considered as incompatible
        // types by the compiler
        return result;
    }
    exports.toSymbolTableFactory = toSymbolTableFactory;
    function toSymbols(symbolTable) {
        if (!symbolTable)
            return [];
        var table = symbolTable;
        if (typeof table.values === 'function') {
            return Array.from(table.values());
        }
        var result = [];
        var own = typeof table.hasOwnProperty === 'function' ?
            function (name) { return table.hasOwnProperty(name); } :
            function (name) { return !!table[name]; };
        for (var name_1 in table) {
            if (own(name_1)) {
                result.push(table[name_1]);
            }
        }
        return result;
    }
    var SymbolTableWrapper = /** @class */ (function () {
        /**
         * Creates a queryable table of symbols belonging to a TypeScript entity.
         * @param symbols symbols to query belonging to the entity
         * @param context program context
         * @param type original TypeScript type of entity owning the symbols, if known
         */
        function SymbolTableWrapper(symbols, context, type) {
            this.context = context;
            this.type = type;
            symbols = symbols || [];
            if (Array.isArray(symbols)) {
                this.symbols = symbols;
                this.symbolTable = toSymbolTableFactory(symbols);
            }
            else {
                this.symbols = toSymbols(symbols);
                this.symbolTable = symbols;
            }
            if (type) {
                this.stringIndexType = type.getStringIndexType();
            }
        }
        Object.defineProperty(SymbolTableWrapper.prototype, "size", {
            get: function () { return this.symbols.length; },
            enumerable: true,
            configurable: true
        });
        SymbolTableWrapper.prototype.get = function (key) {
            var symbol = getFromSymbolTable(this.symbolTable, key);
            if (symbol) {
                return new SymbolWrapper(symbol, this.context);
            }
            if (this.stringIndexType) {
                // If the key does not exist as an explicit symbol on the type, it may be accessing a string
                // index signature using dot notation:
                //
                //   const obj<T>: { [key: string]: T };
                //   obj.stringIndex // equivalent to obj['stringIndex'];
                //
                // In this case, return the type indexed by an arbitrary string key.
                var symbol_1 = this.stringIndexType.getSymbol();
                if (symbol_1) {
                    return new SymbolWrapper(symbol_1, this.context, this.stringIndexType);
                }
            }
            return undefined;
        };
        SymbolTableWrapper.prototype.has = function (key) {
            var table = this.symbolTable;
            return ((typeof table.has === 'function') ? table.has(key) : table[key] != null) ||
                this.stringIndexType !== undefined;
        };
        SymbolTableWrapper.prototype.values = function () {
            var _this = this;
            return this.symbols.map(function (s) { return new SymbolWrapper(s, _this.context); });
        };
        return SymbolTableWrapper;
    }());
    var MapSymbolTable = /** @class */ (function () {
        function MapSymbolTable() {
            this.map = new Map();
            this._values = [];
        }
        Object.defineProperty(MapSymbolTable.prototype, "size", {
            get: function () { return this.map.size; },
            enumerable: true,
            configurable: true
        });
        MapSymbolTable.prototype.get = function (key) { return this.map.get(key); };
        MapSymbolTable.prototype.add = function (symbol) {
            if (this.map.has(symbol.name)) {
                var previous = this.map.get(symbol.name);
                this._values[this._values.indexOf(previous)] = symbol;
            }
            this.map.set(symbol.name, symbol);
            this._values.push(symbol);
        };
        MapSymbolTable.prototype.addAll = function (symbols) {
            var e_4, _a;
            try {
                for (var symbols_3 = tslib_1.__values(symbols), symbols_3_1 = symbols_3.next(); !symbols_3_1.done; symbols_3_1 = symbols_3.next()) {
                    var symbol = symbols_3_1.value;
                    this.add(symbol);
                }
            }
            catch (e_4_1) { e_4 = { error: e_4_1 }; }
            finally {
                try {
                    if (symbols_3_1 && !symbols_3_1.done && (_a = symbols_3.return)) _a.call(symbols_3);
                }
                finally { if (e_4) throw e_4.error; }
            }
        };
        MapSymbolTable.prototype.has = function (key) { return this.map.has(key); };
        MapSymbolTable.prototype.values = function () {
            // Switch to this.map.values once iterables are supported by the target language.
            return this._values;
        };
        return MapSymbolTable;
    }());
    var PipesTable = /** @class */ (function () {
        function PipesTable(pipes, context) {
            this.pipes = pipes;
            this.context = context;
        }
        Object.defineProperty(PipesTable.prototype, "size", {
            get: function () { return this.pipes.length; },
            enumerable: true,
            configurable: true
        });
        PipesTable.prototype.get = function (key) {
            var pipe = this.pipes.find(function (pipe) { return pipe.name == key; });
            if (pipe) {
                return new PipeSymbol(pipe, this.context);
            }
        };
        PipesTable.prototype.has = function (key) { return this.pipes.find(function (pipe) { return pipe.name == key; }) != null; };
        PipesTable.prototype.values = function () {
            var _this = this;
            return this.pipes.map(function (pipe) { return new PipeSymbol(pipe, _this.context); });
        };
        return PipesTable;
    }());
    // This matches .d.ts files that look like ".../<package-name>/<package-name>.d.ts",
    var INDEX_PATTERN = /[\\/]([^\\/]+)[\\/]\1\.d\.ts$/;
    var PipeSymbol = /** @class */ (function () {
        function PipeSymbol(pipe, context) {
            this.pipe = pipe;
            this.context = context;
            this.kind = 'pipe';
            this.language = 'typescript';
            this.container = undefined;
            this.callable = true;
            this.nullable = false;
            this.public = true;
        }
        Object.defineProperty(PipeSymbol.prototype, "name", {
            get: function () { return this.pipe.name; },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(PipeSymbol.prototype, "type", {
            get: function () { return new TypeWrapper(this.tsType, this.context); },
            enumerable: true,
            configurable: true
        });
        Object.defineProperty(PipeSymbol.prototype, "definition", {
            get: function () {
                var symbol = this.tsType.getSymbol();
                return symbol ? definitionFromTsSymbol(symbol) : undefined;
            },
            enumerable: true,
            configurable: true
        });
        PipeSymbol.prototype.members = function () { return EmptyTable.instance; };
        PipeSymbol.prototype.signatures = function () { return signaturesOf(this.tsType, this.context); };
        PipeSymbol.prototype.selectSignature = function (types) {
            var signature = selectSignature(this.tsType, this.context, types);
            if (types.length > 0) {
                var parameterType = types[0];
                if (parameterType instanceof TypeWrapper) {
                    var resultType = undefined;
                    switch (this.name) {
                        case 'async':
                            switch (parameterType.name) {
                                case 'Observable':
                                case 'Promise':
                                case 'EventEmitter':
                                    resultType = getTypeParameterOf(parameterType.tsType, parameterType.name);
                                    break;
                                default:
                                    resultType = getTsTypeFromBuiltinType(symbols_1.BuiltinType.Any, this.context);
                                    break;
                            }
                            break;
                        case 'slice':
                            resultType = parameterType.tsType;
                            break;
                    }
                    if (resultType) {
                        signature = new SignatureResultOverride(signature, new TypeWrapper(resultType, parameterType.context));
                    }
                }
            }
            return signature;
        };
        PipeSymbol.prototype.indexed = function (argument) { return undefined; };
        Object.defineProperty(PipeSymbol.prototype, "tsType", {
            get: function () {
                var type = this._tsType;
                if (!type) {
                    var classSymbol = this.findClassSymbol(this.pipe.type.reference);
                    if (classSymbol) {
                        type = this._tsType = this.findTransformMethodType(classSymbol);
                    }
                    if (!type) {
                        type = this._tsType = getTsTypeFromBuiltinType(symbols_1.BuiltinType.Any, this.context);
                    }
                }
                return type;
            },
            enumerable: true,
            configurable: true
        });
        PipeSymbol.prototype.findClassSymbol = function (type) {
            return findClassSymbolInContext(type, this.context);
        };
        PipeSymbol.prototype.findTransformMethodType = function (classSymbol) {
            var classType = this.context.checker.getDeclaredTypeOfSymbol(classSymbol);
            if (classType) {
                var transform = classType.getProperty('transform');
                if (transform) {
                    return this.context.checker.getTypeOfSymbolAtLocation(transform, this.context.node);
                }
            }
        };
        return PipeSymbol;
    }());
    function findClassSymbolInContext(type, context) {
        var sourceFile = context.program.getSourceFile(type.filePath);
        if (!sourceFile) {
            // This handles a case where an <packageName>/index.d.ts and a <packageName>/<packageName>.d.ts
            // are in the same directory. If we are looking for <packageName>/<packageName> and didn't
            // find it, look for <packageName>/index.d.ts as the program might have found that instead.
            var p = type.filePath;
            var m = p.match(INDEX_PATTERN);
            if (m) {
                var indexVersion = path.join(path.dirname(p), 'index.d.ts');
                sourceFile = context.program.getSourceFile(indexVersion);
            }
        }
        if (sourceFile) {
            var moduleSymbol = sourceFile.module || sourceFile.symbol;
            var exports_1 = context.checker.getExportsOfModule(moduleSymbol);
            return (exports_1 || []).find(function (symbol) { return symbol.name == type.name; });
        }
    }
    var EmptyTable = /** @class */ (function () {
        function EmptyTable() {
            this.size = 0;
        }
        EmptyTable.prototype.get = function (key) { return undefined; };
        EmptyTable.prototype.has = function (key) { return false; };
        EmptyTable.prototype.values = function () { return []; };
        EmptyTable.instance = new EmptyTable();
        return EmptyTable;
    }());
    function isSymbolPrivate(s) {
        return !!s.valueDeclaration && isPrivate(s.valueDeclaration);
    }
    function getTsTypeFromBuiltinType(builtinType, ctx) {
        var syntaxKind;
        switch (builtinType) {
            case symbols_1.BuiltinType.Any:
                syntaxKind = ts.SyntaxKind.AnyKeyword;
                break;
            case symbols_1.BuiltinType.Boolean:
                syntaxKind = ts.SyntaxKind.BooleanKeyword;
                break;
            case symbols_1.BuiltinType.Null:
                syntaxKind = ts.SyntaxKind.NullKeyword;
                break;
            case symbols_1.BuiltinType.Number:
                syntaxKind = ts.SyntaxKind.NumberKeyword;
                break;
            case symbols_1.BuiltinType.String:
                syntaxKind = ts.SyntaxKind.StringKeyword;
                break;
            case symbols_1.BuiltinType.Undefined:
                syntaxKind = ts.SyntaxKind.UndefinedKeyword;
                break;
            default:
                throw new Error("Internal error, unhandled literal kind " + builtinType + ":" + symbols_1.BuiltinType[builtinType]);
        }
        var node = ts.createNode(syntaxKind);
        node.parent = ctx.node;
        return ctx.checker.getTypeAtLocation(node);
    }
    function spanAt(sourceFile, line, column) {
        if (line != null && column != null) {
            var position_1 = ts.getPositionOfLineAndCharacter(sourceFile, line, column);
            var findChild = function findChild(node) {
                if (node.kind > ts.SyntaxKind.LastToken && node.pos <= position_1 && node.end > position_1) {
                    var betterNode = ts.forEachChild(node, findChild);
                    return betterNode || node;
                }
            };
            var node = ts.forEachChild(sourceFile, findChild);
            if (node) {
                return { start: node.getStart(), end: node.getEnd() };
            }
        }
    }
    function definitionFromTsSymbol(symbol) {
        var declarations = symbol.declarations;
        if (declarations) {
            return declarations.map(function (declaration) {
                var sourceFile = declaration.getSourceFile();
                return {
                    fileName: sourceFile.fileName,
                    span: { start: declaration.getStart(), end: declaration.getEnd() }
                };
            });
        }
    }
    function parentDeclarationOf(node) {
        while (node) {
            switch (node.kind) {
                case ts.SyntaxKind.ClassDeclaration:
                case ts.SyntaxKind.InterfaceDeclaration:
                    return node;
                case ts.SyntaxKind.SourceFile:
                    return undefined;
            }
            node = node.parent;
        }
    }
    function getContainerOf(symbol, context) {
        var e_5, _a;
        if (symbol.getFlags() & ts.SymbolFlags.ClassMember && symbol.declarations) {
            try {
                for (var _b = tslib_1.__values(symbol.declarations), _c = _b.next(); !_c.done; _c = _b.next()) {
                    var declaration = _c.value;
                    var parent_1 = parentDeclarationOf(declaration);
                    if (parent_1) {
                        var type = context.checker.getTypeAtLocation(parent_1);
                        if (type) {
                            return new TypeWrapper(type, context);
                        }
                    }
                }
            }
            catch (e_5_1) { e_5 = { error: e_5_1 }; }
            finally {
                try {
                    if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
                }
                finally { if (e_5) throw e_5.error; }
            }
        }
    }
    function getTypeParameterOf(type, name) {
        if (type && type.symbol && type.symbol.name == name) {
            var typeArguments = type.typeArguments;
            if (typeArguments && typeArguments.length <= 1) {
                return typeArguments[0];
            }
        }
    }
    function typeKindOf(type) {
        var e_6, _a;
        if (type) {
            if (type.flags & ts.TypeFlags.Any) {
                return symbols_1.BuiltinType.Any;
            }
            else if (type.flags & (ts.TypeFlags.String | ts.TypeFlags.StringLike | ts.TypeFlags.StringLiteral)) {
                return symbols_1.BuiltinType.String;
            }
            else if (type.flags & (ts.TypeFlags.Number | ts.TypeFlags.NumberLike)) {
                return symbols_1.BuiltinType.Number;
            }
            else if (type.flags & (ts.TypeFlags.Undefined)) {
                return symbols_1.BuiltinType.Undefined;
            }
            else if (type.flags & (ts.TypeFlags.Null)) {
                return symbols_1.BuiltinType.Null;
            }
            else if (type.flags & ts.TypeFlags.Union) {
                // If all the constituent types of a union are the same kind, it is also that kind.
                var candidate = null;
                var unionType_1 = type;
                if (unionType_1.types.length > 0) {
                    candidate = typeKindOf(unionType_1.types[0]);
                    try {
                        for (var _b = tslib_1.__values(unionType_1.types), _c = _b.next(); !_c.done; _c = _b.next()) {
                            var subType = _c.value;
                            if (candidate != typeKindOf(subType)) {
                                return symbols_1.BuiltinType.Other;
                            }
                        }
                    }
                    catch (e_6_1) { e_6 = { error: e_6_1 }; }
                    finally {
                        try {
                            if (_c && !_c.done && (_a = _b.return)) _a.call(_b);
                        }
                        finally { if (e_6) throw e_6.error; }
                    }
                }
                if (candidate != null) {
                    return candidate;
                }
            }
            else if (type.flags & ts.TypeFlags.TypeParameter) {
                return symbols_1.BuiltinType.Unbound;
            }
        }
        return symbols_1.BuiltinType.Other;
    }
    function getFromSymbolTable(symbolTable, key) {
        var table = symbolTable;
        var symbol;
        if (typeof table.get === 'function') {
            // TS 2.2 uses a Map
            symbol = table.get(key);
        }
        else {
            // TS pre-2.2 uses an object
            symbol = table[key];
        }
        return symbol;
    }
});
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoidHlwZXNjcmlwdF9zeW1ib2xzLmpzIiwic291cmNlUm9vdCI6IiIsInNvdXJjZXMiOlsiLi4vLi4vLi4vLi4vLi4vLi4vcGFja2FnZXMvbGFuZ3VhZ2Utc2VydmljZS9zcmMvdHlwZXNjcmlwdF9zeW1ib2xzLnRzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBOzs7Ozs7R0FNRzs7Ozs7Ozs7Ozs7OztJQUdILDJCQUE2QjtJQUM3QiwrQkFBaUM7SUFFakMsaUVBQXlJO0lBRXpJLHNDQUFzQztJQUN0QywyQ0FBMkM7SUFDM0MsSUFBTSxTQUFTLEdBQUksRUFBVSxDQUFDLGFBQWEsQ0FBQyxDQUFDO1FBQ3pDLENBQUMsVUFBQyxJQUFhO1lBQ1YsT0FBQSxDQUFDLENBQUMsQ0FBRSxFQUFVLENBQUMsd0JBQXdCLENBQUMsSUFBSSxDQUFDLEdBQUksRUFBVSxDQUFDLGFBQWEsQ0FBQyxPQUFPLENBQUM7UUFBbEYsQ0FBa0YsQ0FBQyxDQUFDLENBQUM7UUFDMUYsQ0FBQyxVQUFDLElBQWEsSUFBSyxPQUFBLENBQUMsQ0FBQyxDQUFDLElBQUksQ0FBQyxLQUFLLEdBQUksRUFBVSxDQUFDLFNBQVMsQ0FBQyxPQUFPLENBQUMsRUFBOUMsQ0FBOEMsQ0FBQyxDQUFDO0lBRXhFLElBQU0sZUFBZSxHQUFJLEVBQVUsQ0FBQyxXQUFXLENBQUMsQ0FBQztRQUM3QyxDQUFDLFVBQUMsSUFBYTtZQUNWLE9BQUEsQ0FBQyxDQUFDLENBQUMsSUFBSSxDQUFDLEtBQUssR0FBSSxFQUFVLENBQUMsU0FBUyxDQUFDLE1BQU07Z0JBQ3hDLElBQVksQ0FBQyxXQUFXLEdBQUksRUFBVSxDQUFDLFdBQVcsQ0FBQyxTQUFTLENBQUM7UUFEakUsQ0FDaUUsQ0FBQyxDQUFDLENBQUM7UUFDekUsQ0FBQyxVQUFDLElBQWEsSUFBSyxPQUFBLENBQUMsQ0FBQyxDQUFDLElBQUksQ0FBQyxLQUFLLEdBQUksRUFBVSxDQUFDLFNBQVMsQ0FBQyxTQUFTLENBQUMsRUFBaEQsQ0FBZ0QsQ0FBQyxDQUFD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QzVCLE1BQU0sR0FBRyxLQUFLLENBQUMsR0FBRyxDQUFDLENBQUM7U0FDckI7UUFFRCxPQUFPLE1BQU0sQ0FBQztJQUNoQixDQUFDIiwic291cmNlc0NvbnRlbnQiOlsiLyoqXG4gKiBAbGljZW5zZVxuICogQ29weXJpZ2h0IEdvb2dsZSBJbmMuIEFsbCBSaWdodHMgUmVzZXJ2ZWQuXG4gKlxuICogVXNlIG9mIHRoaXMgc291cmNlIGNvZGUgaXMgZ292ZXJuZWQgYnkgYW4gTUlULXN0eWxlIGxpY2Vuc2UgdGhhdCBjYW4gYmVcbiAqIGZvdW5kIGluIHRoZSBMSUNFTlNFIGZpbGUgYXQgaHR0cHM6Ly9hbmd1bGFyLmlvL2xpY2Vuc2VcbiAqL1xuXG5pbXBvcnQge0NvbXBpbGVQaXBlU3VtbWFyeSwgU3RhdGljU3ltYm9sfSBmcm9tICdAYW5ndWxhci9jb21waWxlcic7XG5pbXBvcnQgKiBhcyBwYXRoIGZyb20gJ3BhdGgnO1xuaW1wb3J0ICogYXMgdHMgZnJvbSAndHlwZXNjcmlwdCc7XG5cbmltcG9ydCB7QnVpbHRpblR5cGUsIERlY2xhcmF0aW9uS2luZCwgRGVmaW5pdGlvbiwgU2lnbmF0dXJlLCBTcGFuLCBTeW1ib2wsIFN5bWJvbERlY2xhcmF0aW9uLCBTeW1ib2xRdWVyeSwgU3ltYm9sVGFibGV9IGZyb20gJy4vc3ltYm9scyc7XG5cbi8vIEluIFR5cGVTY3JpcHQgMi4xIHRoZXNlIGZsYWdzIG1vdmVkXG4vLyBUaGVzZSBoZWxwZXJzIHdvcmsgZm9yIGJvdGggMi4wIGFuZCAyLjEuXG5jb25zdCBpc1ByaXZhdGUgPSAodHMgYXMgYW55KS5Nb2RpZmllckZsYWdzID9cbiAgICAoKG5vZGU6IHRzLk5vZGUpID0+XG4gICAgICAgICAhISgodHMgYXMgYW55KS5nZXRDb21iaW5lZE1vZGlmaWVyRmxhZ3Mobm9kZSkgJiAodHMgYXMgYW55KS5Nb2RpZmllckZsYWdzLlByaXZhdGUpKSA6XG4gICAgKChub2RlOiB0cy5Ob2RlKSA9PiAhIShub2RlLmZsYWdzICYgKHRzIGFzIGFueSkuTm9kZUZsYWdzLlByaXZhdGUpKTtcblxuY29uc3QgaXNSZWZlcmVuY2VUeXBlID0gKHRzIGFzIGFueSkuT2JqZWN0RmxhZ3MgP1xuICAgICgodHlwZTogdHMuVHlwZSkgPT5cbiAgICAgICAgICEhKHR5cGUuZmxhZ3MgJiAodHMgYXMgYW55KS5UeXBlRmxhZ3MuT2JqZWN0ICYmXG4gICAgICAgICAgICAodHlwZSBhcyBhbnkpLm9iamVjdEZsYWdzICYgKHRzIGFzIGFueSkuT2JqZWN0RmxhZ3MuUmVmZXJlbmNlKSkgOlxuICAgICgodHlwZTogdHMuVHlwZSkgPT4gISEodHlwZS5mbGFncyAmICh0cyBhcyBhbnkpLlR5cGVGbGFncy5SZWZlcmVuY2UpKTtcblxuaW50ZXJmYWNlIFR5cGVDb250ZXh0IHtcbiAgbm9kZTogdHMuTm9kZTtcbiAgcHJvZ3JhbTogdHMuUHJvZ3JhbTtcbiAgY2hlY2tlcjogdHMuVHlwZUNoZWNrZXI7XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBnZXRTeW1ib2xRdWVyeShcbiAgICBwcm9ncmFtOiB0cy5Qcm9ncmFtLCBjaGVja2VyOiB0cy5UeXBlQ2hlY2tlciwgc291cmNlOiB0cy5Tb3VyY2VGaWxlLFxuICAgIGZldGNoUGlwZXM6ICgpID0+IFN5bWJvbFRhYmxlKTogU3ltYm9sUXVlcnkge1xuICByZXR1cm4gbmV3IFR5cGVTY3JpcHRTeW1ib2xRdWVyeShwcm9ncmFtLCBjaGVja2VyLCBzb3VyY2UsIGZldGNoUGlwZXMpO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gZ2V0Q2xhc3NNZW1iZXJzKFxuICAgIHByb2dyYW06IHRzLlByb2dyYW0sIGNoZWNrZXI6IHRzLlR5cGVDaGVja2VyLCBzdGF0aWNTeW1ib2w6IFN0YXRpY1N5bWJvbCk6IFN5bWJvbFRhYmxlfFxuICAgIHVuZGVmaW5lZCB7XG4gIGNvbnN0IGRlY2xhcmF0aW9uID0gZ2V0Q2xhc3NGcm9tU3RhdGljU3ltYm9sKHByb2dyYW0sIHN0YXRpY1N5bWJvbCk7XG4gIGlmIChkZWNsYXJhdGlvbikge1xuICAgIGNvbnN0IHR5cGUgPSBjaGVja2VyLmdldFR5cGVBdExvY2F0aW9uKGRlY2xhcmF0aW9uKTtcbiAgICBjb25zdCBub2RlID0gcHJvZ3JhbS5nZXRTb3VyY2VGaWxlKHN0YXRpY1N5bWJvbC5maWxlUGF0aCk7XG4gICAgaWYgKG5vZGUpIHtcbiAgICAgIHJldHVybiBuZXcgVHlwZVdyYXBwZXIodHlwZSwge25vZGUsIHByb2dyYW0sIGNoZWNrZXJ9KS5tZW1iZXJzKCk7XG4gICAgfVxuICB9XG59XG5cbmV4cG9ydCBmdW5jdGlvbiBnZXRDbGFzc01lbWJlcnNGcm9tRGVjbGFyYXRpb24oXG4gICAgcHJvZ3JhbTogdHMuUHJvZ3JhbSwgY2hlY2tlcjogdHMuVHlwZUNoZWNrZXIsIHNvdXJjZTogdHMuU291cmNlRmlsZSxcbiAgICBkZWNsYXJhdGlvbjogdHMuQ2xhc3NEZWNsYXJhdGlvbikge1xuICBjb25zdCB0eXBlID0gY2hlY2tlci5nZXRUeXBlQXRMb2NhdGlvbihkZWNsYXJhdGlvbik7XG4gIHJldHVybiBuZXcgVHlwZVdyYXBwZXIodHlwZSwge25vZGU6IHNvdXJjZSwgcHJvZ3JhbSwgY2hlY2tlcn0pLm1lbWJlcnMoKTtcbn1cblxuZXhwb3J0IGZ1bmN0aW9uIGdldENsYXNzRnJvbVN0YXRpY1N5bWJvbChcbiAgICBwcm9ncmFtOiB0cy5Qcm9ncmFtLCB0eXBlOiBTdGF0aWNTeW1ib2wpOiB0cy5DbGFzc0RlY2xhcmF0aW9ufHVuZGVmaW5lZCB7XG4gIGNvbnN0IHNvdXJjZSA9IHByb2dyYW0uZ2V0U291cmNlRmlsZSh0eXBlLmZpbGVQYXRoKTtcbiAgaWYgKHNvdXJjZSkge1xuICAgIHJldHVybiB0cy5mb3JFYWNoQ2hpbGQoc291cmNlLCBjaGlsZCA9PiB7XG4gICAgICBpZiAoY2hpbGQua2luZCA9PT0gdHMuU3ludGF4S2luZC5DbGFzc0RlY2xhcmF0aW9uKSB7XG4gICAgICAgIGNvbnN0IGNsYXNzRGVjbGFyYXRpb24gPSBjaGlsZCBhcyB0cy5DbGFzc0RlY2xhcmF0aW9uO1xuICAgICAgICBpZiAoY2xhc3NEZWNsYXJhdGlvbi5uYW1lICE9IG51bGwgJiYgY2xhc3NEZWNsYXJhdGlvbi5uYW1lLnRleHQgPT09IHR5cGUubmFtZSkge1xuICAgICAgICAgIHJldHVybiBjbGFzc0RlY2xhcmF0aW9uO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfSkgYXModHMuQ2xhc3NEZWNsYXJhdGlvbiB8IHVuZGVmaW5lZCk7XG4gIH1cblxuICByZXR1cm4gdW5kZWZpbmVkO1xufVxuXG5leHBvcnQgZnVuY3Rpb24gZ2V0UGlwZXNUYWJsZShcbiAgICBzb3VyY2U6IHRzLlNvdXJjZUZpbGUsIHByb2dyYW06IHRzLlByb2dyYW0sIGNoZWNrZXI6IHRzLlR5cGVDaGVja2VyLFxuICAgIHBpcGVzOiBDb21waWxlUGlwZVN1bW1hcnlbXSk6IFN5bWJvbFRhYmxlIHtcbiAgcmV0dXJuIG5ldyBQaXBlc1RhYmxlKHBpcGVzLCB7cHJvZ3JhbSwgY2hlY2tlciwgbm9kZTogc291cmNlfSk7XG59XG5cbmNsYXNzIFR5cGVTY3JpcHRTeW1ib2xRdWVyeSBpbXBsZW1lbnRzIFN5bWJvbFF1ZXJ5IHtcbiAgcHJpdmF0ZSB0eXBlQ2FjaGUgPSBuZXcgTWFwPEJ1aWx0aW5UeXBlLCBTeW1ib2w+KCk7XG4gIC8vIFRPRE8oaXNzdWUvMjQ1NzEpOiByZW1vdmUgJyEnLlxuICBwcml2YXRlIHBpcGVzQ2FjaGUgITogU3ltYm9sVGFibGU7XG5cbiAgY29uc3RydWN0b3IoXG4gICAgICBwcml2YXRlIHByb2dyYW06IHRzLlByb2dyYW0sIHByaXZhdGUgY2hlY2tlcjogdHMuVHlwZUNoZWNrZXIsIHByaXZhdGUgc291cmNlOiB0cy5Tb3VyY2VGaWxlLFxuICAgICAgcHJpdmF0ZSBmZXRjaFBpcGVzOiAoKSA9PiBTeW1ib2xUYWJsZSkge31cblxuICBnZXRUeXBlS2luZChzeW1ib2w6IFN5bWJvbCk6IEJ1aWx0aW5UeXBlIHsgcmV0dXJuIHR5cGVLaW5kT2YodGhpcy5nZXRUc1R5cGVPZihzeW1ib2wpKTsgfVxuXG4gIGdldEJ1aWx0aW5UeXBlKGtpbmQ6IEJ1aWx0aW5UeXBlKTogU3ltYm9sIHtcbiAgICBsZXQgcmVzdWx0ID0gdGhpcy50eXBlQ2FjaGUuZ2V0KGtpbmQpO1xuICAgIGlmICghcmVzdWx0KSB7XG4gICAgICBjb25zdCB0eXBlID0gZ2V0VHNUeXBlRnJvbUJ1aWx0aW5UeXBlKGtpbmQsIHtcbiAgICAgICAgY2hlY2tlcjogdGhpcy5jaGVja2VyLFxuICAgICAgICBub2RlOiB0aGlzLnNvdXJjZSxcbiAgICAgICAgcHJvZ3JhbTogdGhpcy5wcm9ncmFtLFxuICAgICAgfSk7XG4gICAgICByZXN1bHQgPVxuICAgICAgICAgIG5ldyBUeXBlV3JhcHBlcih0eXBlLCB7cHJvZ3JhbTogdGhpcy5wcm9ncmFtLCBjaGVja2VyOiB0aGlzLmNoZWNrZXIsIG5vZGU6IHRoaXMuc291cmNlfSk7XG4gICAgICB0aGlzLnR5cGVDYWNoZS5zZXQoa2luZCwgcmVzdWx0KTtcbiAgICB9XG4gICAgcmV0dXJuIHJlc3VsdDtcbiAgfVxuXG4gIGdldFR5cGVVbmlvbiguLi50eXBlczogU3ltYm9sW10pOiBTeW1ib2wge1xuICAgIC8vIE5vIEFQSSBleGlzdHMgc28gcmV0dXJuIGFueSBpZiB0aGUgdHlwZXMgYXJlIG5vdCBhbGwgdGhlIHNhbWUgdHlwZS5cbiAgICBsZXQgcmVzdWx0OiBTeW1ib2x8dW5kZWZpbmVkID0gdW5kZWZpbmVkO1xuICAgIGlmICh0eXBlcy5sZW5ndGgpIHtcbiAgICAgIHJlc3VsdCA9IHR5cGVzWzBdO1xuICAgICAgZm9yIChsZXQgaSA9IDE7IGkgPCB0eXBlcy5sZW5ndGg7IGkrKykge1xuICAgICAgICBpZiAodHlwZXNbaV0gIT0gcmVzdWx0KSB7XG4gICAgICAgICAgcmVzdWx0ID0gdW5kZWZpbmVkO1xuICAgICAgICAgIGJyZWFrO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuICAgIHJldHVybiByZXN1bHQgfHwgdGhpcy5nZXRCdWlsdGluVHlwZShCdWlsdGluVHlwZS5BbnkpO1xuICB9XG5cbiAgZ2V0QXJyYXlUeXBlKHR5cGU6IFN5bWJvbCk6IFN5bWJvbCB7IHJldHVybiB0aGlzLmdldEJ1aWx0aW5UeXBlKEJ1aWx0aW5UeXBlLkFueSk7IH1cblxuICBnZXRFbGVtZW50VHlwZSh0eXBlOiBTeW1ib2wpOiBTeW1ib2x8dW5kZWZpbmVkIHtcbiAgICBpZiAodHlwZSBpbnN0YW5jZW9mIFR5cGVXcmFwcGVyKSB7XG4gICAgICBjb25zdCBlbGVtZW50VHlwZSA9IGdldFR5cGVQYXJhbWV0ZXJPZih0eXBlLnRzVHlwZSwgJ0FycmF5Jyk7XG4gICAgICBpZiAoZWxlbWVudFR5cGUpIHtcbiAgICAgICAgcmV0dXJuIG5ldyBUeXBlV3JhcHBlcihlbGVtZW50VHlwZSwgdHlwZS5jb250ZXh0KTtcbiAgICAgIH1cbiAgICB9XG4gIH1cblxuICBnZXROb25OdWxsYWJsZVR5cGUoc3ltYm9sOiBTeW1ib2wpOiBTeW1ib2wge1xuICAgIGlmIChzeW1ib2wgaW5zdGFuY2VvZiBUeXBlV3JhcHBlciAmJiAodHlwZW9mIHRoaXMuY2hlY2tlci5nZXROb25OdWxsYWJsZVR5cGUgPT0gJ2Z1bmN0aW9uJykpIHtcbiAgICAgIGNvbnN0IHRzVHlwZSA9IHN5bWJvbC50c1R5cGU7XG4gICAgICBjb25zdCBub25OdWxsYWJsZVR5cGUgPSB0aGlzLmNoZWNrZXIuZ2V0Tm9uTnVsbGFibGVUeXBlKHRzVHlwZSk7XG4gICAgICBpZiAobm9uTnVsbGFibGVUeXBlICE9IHRzVHlwZSkge1xuICAgICAgICByZXR1cm4gbmV3IFR5cGVXcmFwcGVyKG5vbk51bGxhYmxlVHlwZSwgc3ltYm9sLmNvbnRleHQpO1xuICAgICAgfSBlbHNlIGlmIChub25OdWxsYWJsZVR5cGUgPT0gdHNUeXBlKSB7XG4gICAgICAgIHJldHVybiBzeW1ib2w7XG4gICAgICB9XG4gICAgfVxuICAgIHJldHVybiB0aGlzLmdldEJ1aWx0aW5UeXBlKEJ1aWx0aW5UeXBlLkFueSk7XG4gIH1cblxuICBnZXRQaXBlcygpOiBTeW1ib2xUYWJsZSB7XG4gICAgbGV0IHJlc3VsdCA9IHRoaXMucGlwZXNDYWNoZTtcbiAgICBpZiAoIXJlc3VsdCkge1xuICAgICAgcmVzdWx0ID0gdGhpcy5waXBlc0NhY2hlID0gdGhpcy5mZXRjaFBpcGVzKCk7XG4gICAgfVxuICAgIHJldHVybiByZXN1bHQ7XG4gIH1cblxuICBnZXRUZW1wbGF0ZUNvbnRleHQodHlwZTogU3RhdGljU3ltYm9sKTogU3ltYm9sVGFibGV8dW5kZWZpbmVkIHtcbiAgICBjb25zdCBjb250ZXh0OiBUeXBlQ29udGV4dCA9IHtub2RlOiB0aGlzLnNvdXJjZSwgcHJvZ3JhbTogdGhpcy5wcm9ncmFtLCBjaGVja2VyOiB0aGlzLmNoZWNrZXJ9O1xuICAgIGNvbnN0IHR5cGVTeW1ib2wgPSBmaW5kQ2xhc3NTeW1ib2xJbkNvbnRleHQodHlwZSwgY29udGV4dCk7XG4gICAgaWYgKHR5cGVTeW1ib2wpIHtcbiAgICAgIGNvbnN0IGNvbnRleHRUeXBlID0gdGhpcy5nZXRUZW1wbGF0ZVJlZkNvbnRleHRUeXBlKHR5cGVTeW1ib2wpO1xuICAgICAgaWYgKGNvbnRleHRUeXBlKSByZXR1cm4gbmV3IFN5bWJvbFdyYXBwZXIoY29udGV4dFR5cGUsIGNvbnRleHQpLm1lbWJlcnMoKTtcbiAgICB9XG4gIH1cblxuICBnZXRUeXBlU3ltYm9sKHR5cGU6IFN0YXRpY1N5bWJvbCk6IFN5bWJvbHx1bmRlZmluZWQge1xuICAgIGNvbnN0IGNvbnRleHQ6IFR5cGVDb250ZXh0ID0ge25vZGU6IHRoaXMuc291cmNlLCBwcm9ncmFtOiB0aGlzLnByb2dyYW0sIGNoZWNrZXI6IHRoaXMuY2hlY2tlcn07XG4gICAgY29uc3QgdHlwZVN5bWJvbCA9IGZpbmRDbGFzc1N5bWJvbEluQ29udGV4dCh0eXBlLCBjb250ZXh0KTtcbiAgICByZXR1cm4gdHlwZVN5bWJvbCAmJiBuZXcgU3ltYm9sV3JhcHBlcih0eXBlU3ltYm9sLCBjb250ZXh0KTtcbiAgfVxuXG4gIGNyZWF0ZVN5bWJvbFRhYmxlKHN5bWJvbHM6IFN5bWJvbERlY2xhcmF0aW9uW10pOiBTeW1ib2xUYWJsZSB7XG4gICAgY29uc3QgcmVzdWx0ID0gbmV3IE1hcFN5bWJvbFRhYmxlKCk7XG4gICAgcmVzdWx0LmFkZEFsbChzeW1ib2xzLm1hcChzID0+IG5ldyBEZWNsYXJlZFN5bWJvbChzKSkpO1xuICAgIHJldHVybiByZXN1bHQ7XG4gIH1cblxuICBtZXJnZVN5bWJvbFRhYmxlKHN5bWJvbFRhYmxlczogU3ltYm9sVGFibGVbXSk6IFN5bWJvbFRhYmxlIHtcbiAgICBjb25zdCByZXN1bHQgPSBuZXcgTWFwU3ltYm9sVGFibGUoKTtcbiAgICBmb3IgKGNvbnN0IHN5bWJvbFRhYmxlIG9mIHN5bWJvbFRhYmxlcykge1xuICAgICAgcmVzdWx0LmFkZEFsbChzeW1ib2xUYWJsZS52YWx1ZXMoKSk7XG4gICAgfVxuICAgIHJldHVybiByZXN1bHQ7XG4gIH1cblxuICBnZXRTcGFuQXQobGluZTogbnVtYmVyLCBjb2x1bW46IG51bWJlcik6IFNwYW58dW5kZWZpbmVkIHtcbiAgICByZXR1cm4gc3BhbkF0KHRoaXMuc291cmNlLCBsaW5lLCBjb2x1bW4pO1xuICB9XG5cbiAgcHJpdmF0ZSBnZXRUZW1wbGF0ZVJlZkNvbnRleHRUeXBlKHR5cGVTeW1ib2w6IHRzLlN5bWJvbCk6IHRzLlN5bWJvbHx1bmRlZmluZWQge1xuICAgIGNvbnN0IHR5cGUgPSB0aGlzLmNoZWNrZXIuZ2V0VHlwZU9mU3ltYm9sQXRMb2NhdGlvbih0eXBlU3ltYm9sLCB0aGlzLnNvdXJjZSk7XG4gICAgY29uc3QgY29uc3RydWN0b3IgPSB0eXBlLnN5bWJvbCAmJiB0eXBlLnN5bWJvbC5tZW1iZXJzICYmXG4gICAgICAgIGdldEZyb21TeW1ib2xUYWJsZSh0eXBlLnN5bWJvbC5tZW1iZXJzICEsICdfX2NvbnN0cnVjdG9yJyk7XG5cbiAgICBpZiAoY29uc3RydWN0b3IpIHtcbiAgICAgIGNvbnN0IGNvbnN0cnVjdG9yRGVjbGFyYXRpb24gPSBjb25zdHJ1Y3Rvci5kZWNsYXJhdGlvbnMgIVswXSBhcyB0cy5Db25zdHJ1Y3RvclR5cGVOb2RlO1xuICAgICAgZm9yIChjb25zdCBwYXJhbWV0ZXIgb2YgY29uc3RydWN0b3JEZWNsYXJhdGlvbi5wYXJhbWV0ZXJzKSB7XG4gICAgICAgIGNvbnN0IHR5cGUgPSB0aGlzLmNoZWNrZXIuZ2V0VHlwZUF0TG9jYXRpb24ocGFyYW1ldGVyLnR5cGUgISk7XG4gICAgICAgIGlmICh0eXBlLnN5bWJvbCAhLm5hbWUgPT0gJ1RlbXBsYXRlUmVmJyAmJiBpc1JlZmVyZW5jZVR5cGUodHlwZSkpIHtcbiAgICAgICAgICBjb25zdCB0eXBlUmVmZXJlbmNlID0gdHlwZSBhcyB0cy5UeXBlUmVmZXJlbmNlO1xuICAgICAgICAgIGlmICh0eXBlUmVmZXJlbmNlLnR5cGVBcmd1bWVudHMgJiYgdHlwZVJlZmVyZW5jZS50eXBlQXJndW1lbnRzLmxlbmd0aCA9PT0gMSkge1xuICAgICAgICAgICAgcmV0dXJuIHR5cGVSZWZlcmVuY2UudHlwZUFyZ3VtZW50c1swXS5zeW1ib2w7XG4gICAgICAgICAgfVxuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuICB9XG5cbiAgcHJpdmF0ZSBnZXRUc1R5cGVPZihzeW1ib2w6IFN5bWJvbCk6IHRzLlR5cGV8dW5kZWZpbmVkIHtcbiAgICBjb25zdCB0eXBlID0gdGhpcy5nZXRUeXBlV3JhcHBlcihzeW1ib2wpO1xuICAgIHJldHVybiB0eXBlICYmIHR5cGUudHNUeXBlO1xuICB9XG5cbiAgcHJpdmF0ZSBnZXRUeXBlV3JhcHBlcihzeW1ib2w6IFN5bWJvbCk6IFR5cGVXcmFwcGVyfHVuZGVmaW5lZCB7XG4gICAgbGV0IHR5cGU6IFR5cGVXcmFwcGVyfHVuZGVmaW5lZCA9IHVuZGVmaW5lZDtcbiAgICBpZiAoc3ltYm9sIGluc3RhbmNlb2YgVHlwZVdyYXBwZXIpIHtcbiAgICAgIHR5cGUgPSBzeW1ib2w7XG4gICAgfSBlbHNlIGlmIChzeW1ib2wudHlwZSBpbnN0YW5jZW9mIFR5cGVXcmFwcGVyKSB7XG4gICAgICB0eXBlID0gc3ltYm9sLnR5cGU7XG4gICAgfVxuICAgIHJldHVybiB0eXBlO1xuICB9XG59XG5cbmZ1bmN0aW9uIHR5cGVDYWxsYWJsZSh0eXBlOiB0cy5UeXBlKTogYm9vbGVhbiB7XG4gIGNvbnN0IHNpZ25hdHVyZXMgPSB0eXBlLmdldENhbGxTaWduYXR1cmVzKCk7XG4gIHJldHVybiBzaWduYXR1cmVzICYmIHNpZ25hdHVyZXMubGVuZ3RoICE9IDA7XG59XG5cbmZ1bmN0aW9uIHNpZ25hdHVyZXNPZih0eXBlOiB0cy5UeXBlLCBjb250ZXh0OiBUeXBlQ29udGV4dCk6IFNpZ25hdHVyZVtdIHtcbiAgcmV0dXJuIHR5cGUuZ2V0Q2FsbFNpZ25hdHVyZXMoKS5tYXAocyA9PiBuZXcgU2lnbmF0dXJlV3JhcHBlcihzLCBjb250ZXh0KSk7XG59XG5cbmZ1bmN0aW9uIHNlbGVjdFNpZ25hdHVyZSh0eXBlOiB0cy5UeXBlLCBjb250ZXh0OiBUeXBlQ29udGV4dCwgdHlwZXM6IFN5bWJvbFtdKTogU2lnbmF0dXJlfFxuICAgIHVuZGVmaW5lZCB7XG4gIC8vIFRPRE86IERvIGEgYmV0dGVyIGpvYiBvZiBzZWxlY3RpbmcgdGhlIHJpZ2h0IHNpZ25hdHVyZS5cbiAgY29uc3Qgc2lnbmF0dXJlcyA9IHR5cGUuZ2V0Q2FsbFNpZ25hdHVyZXMoKTtcbiAgcmV0dXJuIHNpZ25hdHVyZXMubGVuZ3RoID8gbmV3IFNpZ25hdHVyZVdyYXBwZXIoc2lnbmF0dXJlc1swXSwgY29udGV4dCkgOiB1bmRlZmluZWQ7XG59XG5cbmNsYXNzIFR5cGVXcmFwcGVyIGltcGxlbWVudHMgU3ltYm9sIHtcbiAgY29uc3RydWN0b3IocHVibGljIHRzVHlwZTogdHMuVHlwZSwgcHVibGljIGNvbnRleHQ6IFR5cGVDb250ZXh0KSB7XG4gICAgaWYgKCF0c1R5cGUpIHtcbiAgICAgIHRocm93IEVycm9yKCdJbnRlcm5hbDogbnVsbCB0eXBlJyk7XG4gICAgfVxuICB9XG5cbiAgZ2V0IG5hbWUoKTogc3RyaW5nIHtcbiAgICBjb25zdCBzeW1ib2wgPSB0aGlzLnRzVHlwZS5zeW1ib2w7XG4gICAgcmV0dXJuIChzeW1ib2wgJiYgc3ltYm9sLm5hbWUpIHx8ICc8YW5vbnltb3VzPic7XG4gIH1cblxuICBwdWJsaWMgcmVhZG9ubHkga2luZDogRGVjbGFyYXRpb25LaW5kID0gJ3R5cGUnO1xuXG4gIHB1YmxpYyByZWFkb25seSBsYW5ndWFnZTogc3RyaW5nID0gJ3R5cGVzY3JpcHQnO1xuXG4gIHB1YmxpYyByZWFkb25seSB0eXBlOiBTeW1ib2x8dW5kZWZpbmVkID0gdW5kZWZpbmVkO1xuXG4gIHB1YmxpYyByZWFkb25seSBjb250YWluZXI6IFN5bWJvbHx1bmRlZmluZWQgPSB1bmRlZmluZWQ7XG5cbiAgcHVibGljIHJlYWRvbmx5IHB1YmxpYzogYm9vbGVhbiA9IHRydWU7XG5cbiAgZ2V0IGNhbGxhYmxlKCk6IGJvb2xlYW4geyByZXR1cm4gdHlwZUNhbGxhYmxlKHRoaXMudHNUeXBlKTsgfVxuXG4gIGdldCBudWxsYWJsZSgpOiBib29sZWFuIHtcbiAgICByZXR1cm4gdGhpcy5jb250ZXh0LmNoZWNrZXIuZ2V0Tm9uTnVsbGFibGVUeXBlKHRoaXMudHNUeXBlKSAhPSB0aGlzLnRzVHlwZTtcbiAgfVxuXG4gIGdldCBkZWZpbml0aW9uKCk6IERlZmluaXRpb258dW5kZWZpbmVkIHtcbiAgICBjb25zdCBzeW1ib2wgPSB0aGlzLnRzVHlwZS5nZXRTeW1ib2woKTtcbiAgICByZXR1cm4gc3ltYm9sID8gZGVmaW5pdGlvbkZyb21Uc1N5bWJvbChzeW1ib2wpIDogdW5kZWZpbmVkO1xuICB9XG5cbiAgbWVtYmVycygpOiBTeW1ib2xUYWJsZSB7XG4gICAgLy8gU2hvdWxkIGNhbGwgZ2V0QXBwYXJlbnRQcm9wZXJ0aWVzKCkgaW5zdGVhZCBvZiBnZXRQcm9wZXJ0aWVzKCkgYmVjYXVzZVxuICAgIC8vIHRoZSBmb3JtZXIgaW5jbHVkZXMgcHJvcGVydGllcyBvbiB0aGUgYmFzZSBjbGFzcyB3aGVyZWFzIHRoZSBsYXR0ZXIgZG9lc1xuICAgIC8vIG5vdC4gVGhpcyBwcm92aWRlcyBwcm9wZXJ0aWVzIGxpa2UgLmJpbmQoKSwgLmNhbGwoKSwgLmFwcGx5KCksIGV0YyBmb3JcbiAgICAvLyBmdW5jdGlvbnMuXG4gICAgcmV0dXJuIG5ldyBTeW1ib2xUYWJsZVdyYXBwZXIodGhpcy50c1R5cGUuZ2V0QXBwYXJlbnRQcm9wZXJ0aWVzKCksIHRoaXMuY29udGV4dCwgdGhpcy50c1R5cGUpO1xuICB9XG5cbiAgc2lnbmF0dXJlcygpOiBTaWduYXR1cmVbXSB7IHJldHVybiBzaWduYXR1cmVzT2YodGhpcy50c1R5cGUsIHRoaXMuY29udGV4dCk7IH1cblxuICBzZWxlY3RTaWduYXR1cmUodHlwZXM6IFN5bWJvbFtdKTogU2lnbmF0dXJlfHVuZGVmaW5lZCB7XG4gICAgcmV0dXJuIHNlbGVjdFNpZ25hdHVyZSh0aGlzLnRzVHlwZSwgdGhpcy5jb250ZXh0LCB0eXBlcyk7XG4gIH1cblxuICBpbmRleGVkKGFyZ3VtZW50OiBTeW1ib2wsIHZhbHVlOiBhbnkpOiBTeW1ib2x8dW5kZWZpbmVkIHtcbiAgICBjb25zdCB0eXBlID0gYXJndW1lbnQgaW5zdGFuY2VvZiBUeXBlV3JhcHBlciA/IGFyZ3VtZW50IDogYXJndW1lbnQudHlwZTtcbiAgICBpZiAoISh0eXBlIGluc3RhbmNlb2YgVHlwZVdyYXBwZXIpKSByZXR1cm47XG5cbiAgICBjb25zdCB0eXBlS2luZCA9IHR5cGVLaW5kT2YodHlwZS50c1R5cGUpO1xuICAgIHN3aXRjaCAodHlwZUtpbmQpIHtcbiAgICAgIGNhc2UgQnVpbHRpblR5cGUuTnVtYmVyOlxuICAgICAgICBjb25zdCBuVHlwZSA9IHRoaXMudHNUeXBlLmdldE51bWJlckluZGV4VHlwZSgpO1xuICAgICAgICBpZiAoblR5cGUpIHtcbiAgICAgICAgICAvLyBnZXQgdGhlIHJpZ2h0IHR1cGxlIHR5cGUgYnkgdmFsdWUsIGxpa2UgJ3ZhciB0OiBbbnVtYmVyLCBzdHJpbmddOydcbiAgICAgICAgICBpZiAoblR5cGUuaXNVbmlvbigpKSB7XG4gICAgICAgICAgICAvLyByZXR1cm4gdW5kZWZpbmVkIGlmIGFycmF5IGluZGV4IG91dCBvZiBib3VuZC5cbiAgICAgICAgICAgIHJldHVybiBuVHlwZS50eXBlc1t2YWx1ZV0gJiYgbmV3IFR5cGVXcmFwcGVyKG5UeXBlLnR5cGVzW3ZhbHVlXSwgdGhpcy5jb250ZXh0KTtcbiAgICAgICAgICB9XG4gICAgICAgICAgcmV0dXJuIG5ldyBUeXBlV3JhcHBlcihuVHlwZSwgdGhpcy5jb250ZXh0KTtcbiAgICAgICAgfVxuICAgICAgICByZXR1cm4gdW5kZWZpbmVkO1xuICAgICAgY2FzZSBCdWlsdGluVHlwZS5TdHJpbmc6XG4gICAgICAgIGNvbnN0IHNUeXBlID0gdGhpcy50c1R5cGUuZ2V0U3RyaW5nSW5kZXhUeXBlKCk7XG4gICAgICAgIHJldHVybiBzVHlwZSAmJiBuZXcgVHlwZVdyYXBwZXIoc1R5cGUsIHRoaXMuY29udGV4dCk7XG4gICAgfVxuICB9XG59XG5cbmNsYXNzIFN5bWJvbFdyYXBwZXIgaW1wbGVtZW50cyBTeW1ib2wge1xuICBwcml2YXRlIHN5bWJvbDogdHMuU3ltYm9sO1xuICBwcml2YXRlIF9tZW1iZXJzPzogU3ltYm9sVGFibGU7XG5cbiAgcHVibGljIHJlYWRvbmx5IG51bGxhYmxlOiBib29sZWFuID0gZmFsc2U7XG4gIHB1YmxpYyByZWFkb25seSBsYW5ndWFnZTogc3RyaW5nID0gJ3R5cGVzY3JpcHQnO1xuXG4gIGNvbnN0cnVjdG9yKFxuICAgICAgc3ltYm9sOiB0cy5TeW1ib2wsXG4gICAgICAvKiogVHlwZVNjcmlwdCB0eXBlIGNvbnRleHQgb2YgdGhlIHN5bWJvbC4gKi9cbiAgICAgIHByaXZhdGUgY29udGV4dDogVHlwZUNvbnRleHQsXG4gICAgICAvKiogVHlwZSBvZiB0aGUgVHlwZVNjcmlwdCBzeW1ib2wsIGlmIGtub3duLiBJZiBub3QgcHJvdmlkZWQsIHRoZSB0eXBlIG9mIHRoZSBzeW1ib2xcbiAgICAgICogd2lsbCBiZSBkZXRlcm1pbmVkIGR5bmFtaWNhbGx5OyBzZWUgYFN5bWJvbFdyYXBwZXIjdHNUeXBlYC4gKi9cbiAgICAgIHByaXZhdGUgX3RzVHlwZT86IHRzLlR5cGUpIHtcbiAgICB0aGlzLnN5bWJvbCA9IHN5bWJvbCAmJiBjb250ZXh0ICYmIChzeW1ib2wuZmxhZ3MgJiB0cy5TeW1ib2xGbGFncy5BbGlhcykgP1xuICAgICAgICBjb250ZXh0LmNoZWNrZXIuZ2V0QWxpYXNlZFN5bWJvbChzeW1ib2wpIDpcbiAgICAgICAgc3ltYm9sO1xuICB9XG5cbiAgZ2V0IG5hbWUoKTogc3RyaW5nIHsgcmV0dXJuIHRoaXMuc3ltYm9sLm5hbWU7IH1cblxuICBnZXQga2luZCgpOiBEZWNsYXJhdGlvbktpbmQgeyByZXR1cm4gdGhpcy5jYWxsYWJsZSA/ICdtZXRob2QnIDogJ3Byb3BlcnR5JzsgfVxuXG4gIGdldCB0eXBlKCk6IFN5bWJvbHx1bmRlZmluZWQgeyByZXR1cm4gbmV3IFR5cGVXcmFwcGVyKHRoaXMudHNUeXBlLCB0aGlzLmNvbnRleHQpOyB9XG5cbiAgZ2V0IGNvbnRhaW5lcigpOiBTeW1ib2x8dW5kZWZpbmVkIHsgcmV0dXJuIGdldENvbnRhaW5lck9mKHRoaXMuc3ltYm9sLCB0aGlzLmNvbnRleHQpOyB9XG5cbiAgZ2V0IHB1YmxpYygpOiBib29sZWFuIHtcbiAgICAvLyBTeW1ib2xzIHRoYXQgYXJlIG5vdCBleHBsaWNpdGx5IG1hZGUgcHJpdmF0ZSBhcmUgcHVibGljLlxuICAgIHJldHVybiAhaXNTeW1ib2xQcml2YXRlKHRoaXMuc3ltYm9sKTtcbiAgfVxuXG4gIGdldCBjYWxsYWJsZSgpOiBib29sZWFuIHsgcmV0dXJuIHR5cGVDYWxsYWJsZSh0aGlzLnRzVHlwZSk7IH1cblxuICBnZXQgZGVmaW5pdGlvbigpOiBEZWZpbml0aW9uIHsgcmV0dXJuIGRlZmluaXRpb25Gcm9tVHNTeW1ib2wodGhpcy5zeW1ib2wpOyB9XG5cbiAgbWVtYmVycygpOiBTeW1ib2xUYWJsZSB7XG4gICAgaWYgKCF0aGlzLl9tZW1iZXJzKSB7XG4gICAgICBpZiAoKHRoaXMuc3ltYm9sLmZsYWdzICYgKHRzLlN5bWJvbEZsYWdzLkNsYXNzIHwgdHMuU3ltYm9sRmxhZ3MuSW50ZXJmYWNlKSkgIT0gMCkge1xuICAgICAgICBjb25zdCBkZWNsYXJlZFR5cGUgPSB0aGlzLmNvbnRleHQuY2hlY2tlci5nZXREZWNsYXJlZFR5cGVPZlN5bWJvbCh0aGlzLnN5bWJvbCk7XG4gICAgICAgIGNvbnN0IHR5cGVXcmFwcGVyID0gbmV3IFR5cGVXcmFwcGVyKGRlY2xhcmVkVHlwZSwgdGhpcy5jb250ZXh0KTtcbiAgICAgICAgdGhpcy5fbWVtYmVycyA9IHR5cGVXcmFwcGVyLm1lbWJlcnMoKTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIHRoaXMuX21lbWJlcnMgPSBuZXcgU3ltYm9sVGFibGVXcmFwcGVyKHRoaXMuc3ltYm9sLm1lbWJlcnMgISwgdGhpcy5jb250ZXh0LCB0aGlzLnRzVHlwZSk7XG4gICAgICB9XG4gICAgfVxuICAgIHJldHVybiB0aGlzLl9tZW1iZXJzO1xuICB9XG5cbiAgc2lnbmF0dXJlcygpOiBTaWduYXR1cmVbXSB7IHJldHVybiBzaWduYXR1cmVzT2YodGhpcy50c1R5cGUsIHRoaXMuY29udGV4dCk7IH1cblxuICBzZWxlY3RTaWduYXR1cmUodHlwZXM6IFN5bWJvbFtdKTogU2lnbmF0dXJlfHVuZGVmaW5lZCB7XG4gICAgcmV0dXJuIHNlbGVjdFNpZ25hdHVyZSh0aGlzLnRzVHlwZSwgdGhpcy5jb250ZXh0LCB0eXBlcyk7XG4gIH1cblxuICBpbmRleGVkKGFyZ3VtZW50OiBTeW1ib2wpOiBTeW1ib2x8dW5kZWZpbmVkIHsgcmV0dXJuIHVuZGVmaW5lZDsgfVxuXG4gIHByaXZhdGUgZ2V0IHRzVHlwZSgpOiB0cy5UeXBlIHtcbiAgICBsZXQgdHlwZSA9IHRoaXMuX3RzVHlwZTtcbiAgICBpZiAoIXR5cGUpIHtcbiAgICAgIHR5cGUgPSB0aGlzLl90c1R5cGUgPVxuICAgICAgICAgIHRoaXMuY29udGV4dC5jaGVja2VyLmdldFR5cGVPZlN5bWJvbEF0TG9jYXRpb24odGhpcy5zeW1ib2wsIHRoaXMuY29udGV4dC5ub2RlKTtcbiAgICB9XG4gICAgcmV0dXJuIHR5cGU7XG4gIH1cbn1cblxuY2xhc3MgRGVjbGFyZWRTeW1ib2wgaW1wbGVtZW50cyBTeW1ib2wge1xuICBwdWJsaWMgcmVhZG9ubHkgbGFuZ3VhZ2U6IHN0cmluZyA9ICduZy10ZW1wbGF0ZSc7XG5cbiAgcHVibGljIHJlYWRvbmx5IG51bGxhYmxlOiBib29sZWFuID0gZmFsc2U7XG5cbiAgcHVibGljIHJlYWRvbmx5IHB1YmxpYzogYm9vbGVhbiA9IHRydWU7XG5cbiAgY29uc3RydWN0b3IocHJpdmF0ZSBkZWNsYXJhdGlvbjogU3ltYm9sRGVjbGFyYXRpb24pIHt9XG5cbiAgZ2V0IG5hbWUoKSB7IHJldHVybiB0aGlzLmRlY2xhcmF0aW9uLm5hbWU7IH1cblxuICBnZXQga2luZCgpIHsgcmV0dXJuIHRoaXMuZGVjbGFyYXRpb24ua2luZDsgfVxuXG4gIGdldCBjb250YWluZXIoKTogU3ltYm9sfHVuZGVmaW5lZCB7IHJldHVybiB1bmRlZmluZWQ7IH1cblxuICBnZXQgdHlwZSgpIHsgcmV0dXJuIHRoaXMuZGVjbGFyYXRpb24udHlwZTsgfVxuXG4gIGdldCBjYWxsYWJsZSgpOiBib29sZWFuIHsgcmV0dXJuIHRoaXMuZGVjbGFyYXRpb24udHlwZS5jYWxsYWJsZTsgfVxuXG5cbiAgZ2V0IGRlZmluaXRpb24oKTogRGVmaW5pdGlvbiB7IHJldHVybiB0aGlzLmRlY2xhcmF0aW9uLmRlZmluaXRpb247IH1cblxuICBtZW1iZXJzKCk6IFN5bWJvbFRhYmxlIHsgcmV0dXJuIHRoaXMuZGVjbGFyYXRpb24udHlwZS5tZW1iZXJzKCk7IH1cblxuICBzaWduYXR1cmVzKCk6IFNpZ25hdHVyZVtdIHsgcmV0dXJuIHRoaXMuZGVjbGFyYXRpb24udHlwZS5zaWduYXR1cmVzKCk7IH1cblxuICBzZWxlY3RTaWduYXR1cmUodHlwZXM6IFN5bWJvbFtdKTogU2lnbmF0dXJlfHVuZGVmaW5lZCB7XG4gICAgcmV0dXJuIHRoaXMuZGVjbGFyYXRpb24udHlwZS5zZWxlY3RTaWduYXR1cmUodHlwZXMpO1xuICB9XG5cbiAgaW5kZXhlZChhcmd1bWVudDogU3ltYm9sKTogU3ltYm9sfHVuZGVmaW5lZCB7IHJldHVybiB1bmRlZmluZWQ7IH1cbn1cblxuY2xhc3MgU2lnbmF0dXJlV3JhcHBlciBpbXBsZW1lbnRzIFNpZ25hdHVyZSB7XG4gIGNvbnN0cnVjdG9yKHByaXZhdGUgc2lnbmF0dXJlOiB0cy5TaWduYXR1cmUsIHByaXZhdGUgY29udGV4dDogVHlwZUNvbnRleHQpIHt9XG5cbiAgZ2V0IGFyZ3VtZW50cygpOiBTeW1ib2xUYWJsZSB7XG4gICAgcmV0dXJuIG5ldyBTeW1ib2xUYWJsZVdyYXBwZXIodGhpcy5zaWduYXR1cmUuZ2V0UGFyYW1ldGVycygpLCB0aGlzLmNvbnRleHQpO1xuICB9XG5cbiAgZ2V0IHJlc3VsdCgpOiBTeW1ib2wgeyByZXR1cm4gbmV3IFR5cGVXcmFwcGVyKHRoaXMuc2lnbmF0dXJlLmdldFJldHVyblR5cGUoKSwgdGhpcy5jb250ZXh0KTsgfVxufVxuXG5jbGFzcyBTaWduYXR1cmVSZXN1bHRPdmVycmlkZSBpbXBsZW1lbnRzIFNpZ25hdHVyZSB7XG4gIGNvbnN0cnVjdG9yKHByaXZhdGUgc2lnbmF0dXJlOiBTaWduYXR1cmUsIHByaXZhdGUgcmVzdWx0VHlwZTogU3ltYm9sKSB7fVxuXG4gIGdldCBhcmd1bWVudHMoKTogU3ltYm9sVGFibGUgeyByZXR1cm4gdGhpcy5zaWduYXR1cmUuYXJndW1lbnRzOyB9XG5cbiAgZ2V0IHJlc3VsdCgpOiBTeW1ib2wgeyByZXR1cm4gdGhpcy5yZXN1bHRUeXBlOyB9XG59XG5cbmV4cG9ydCBmdW5jdGlvbiB0b1N5bWJvbFRhYmxlRmFjdG9yeShzeW1ib2xzOiB0cy5TeW1ib2xbXSkge1xuICAvLyDiiIAgVHlwZXNjcmlwdCB2ZXJzaW9uID49IDIuMiwgYFN5bWJvbFRhYmxlYCBpcyBpbXBsZW1lbnRlZCBhcyBhbiBFUzYgYE1hcGBcbiAgY29uc3QgcmVzdWx0ID0gbmV3IE1hcDxzdHJpbmcsIHRzLlN5bWJvbD4oKTtcbiAgZm9yIChjb25zdCBzeW1ib2wgb2Ygc3ltYm9scykge1xuICAgIHJlc3VsdC5zZXQoc3ltYm9sLm5hbWUsIHN5bWJvbCk7XG4gIH1cbiAgLy8gRmlyc3QsIHRlbGwgdGhlIGNvbXBpbGVyIHRoYXQgYHJlc3VsdGAgaXMgb2YgdHlwZSBgYW55YC4gVGhlbiwgdXNlIGEgc2Vjb25kIHR5cGUgYXNzZXJ0aW9uXG4gIC8vIHRvIGB0cy5TeW1ib2xUYWJsZWAuXG4gIC8vIE90aGVyd2lzZSwgYE1hcDxzdHJpbmcsIHRzLlN5bWJvbD5gIGFuZCBgdHMuU3ltYm9sVGFibGVgIHdpbGwgYmUgY29uc2lkZXJlZCBhcyBpbmNvbXBhdGlibGVcbiAgLy8gdHlwZXMgYnkgdGhlIGNvbXBpbGVyXG4gIHJldHVybiA8dHMuU3ltYm9sVGFibGU+KDxhbnk+cmVzdWx0KTtcbn1cblxuZnVuY3Rpb24gdG9TeW1ib2xzKHN5bWJvbFRhYmxlOiB0cy5TeW1ib2xUYWJsZSB8IHVuZGVmaW5lZCk6IHRzLlN5bWJvbFtdIHtcbiAgaWYgKCFzeW1ib2xUYWJsZSkgcmV0dXJuIFtdO1xuXG4gIGNvbnN0IHRhYmxlID0gc3ltYm9sVGFibGUgYXMgYW55O1xuXG4gIGlmICh0eXBlb2YgdGFibGUudmFsdWVzID09PSAnZnVuY3Rpb24nKSB7XG4gICAgcmV0dXJuIEFycmF5LmZyb20odGFibGUudmFsdWVzKCkpIGFzIHRzLlN5bWJvbFtdO1xuICB9XG5cbiAgY29uc3QgcmVzdWx0OiB0cy5TeW1ib2xbXSA9IFtdO1xuXG4gIGNvbnN0IG93biA9IHR5cGVvZiB0YWJsZS5oYXNPd25Qcm9wZXJ0eSA9PT0gJ2Z1bmN0aW9uJyA/XG4gICAgICAobmFtZTogc3RyaW5nKSA9PiB0YWJsZS5oYXNPd25Qcm9wZXJ0eShuYW1lKSA6XG4gICAgICAobmFtZTogc3RyaW5nKSA9PiAhIXRhYmxlW25hbWVdO1xuXG4gIGZvciAoY29uc3QgbmFtZSBpbiB0YWJsZSkge1xuICAgIGlmIChvd24obmFtZSkpIHtcbiAgICAgIHJlc3VsdC5wdXNoKHRhYmxlW25hbWVdKTtcbiAgICB9XG4gIH1cbiAgcmV0dXJuIHJlc3VsdDtcbn1cblxuY2xhc3MgU3ltYm9sVGFibGVXcmFwcGVyIGltcGxlbWVudHMgU3ltYm9sVGFibGUge1xuICBwcml2YXRlIHN5bWJvbHM6IHRzLlN5bWJvbFtdO1xuICBwcml2YXRlIHN5bWJvbFRhYmxlOiB0cy5TeW1ib2xUYWJsZTtcbiAgcHJpdmF0ZSBzdHJpbmdJbmRleFR5cGU/OiB0cy5UeXBlO1xuXG4gIC8qKlxuICAgKiBDcmVhdGVzIGEgcXVlcnlhYmxlIHRhYmxlIG9mIHN5bWJvbHMgYmVsb25naW5nIHRvIGEgVHlwZVNjcmlwdCBlbnRpdHkuXG4gICAqIEBwYXJhbSBzeW1ib2xzIHN5bWJvbHMgdG8gcXVlcnkgYmVsb25naW5nIHRvIHRoZSBlbnRpdHlcbiAgICogQHBhcmFtIGNvbnRleHQgcHJvZ3JhbSBjb250ZXh0XG4gICAqIEBwYXJhbSB0eXBlIG9yaWdpbmFsIFR5cGVTY3JpcHQgdHlwZSBvZiBlbnRpdHkgb3duaW5nIHRoZSBzeW1ib2xzLCBpZiBrbm93blxuICAgKi9cbiAgY29uc3RydWN0b3IoXG4gICAgICBzeW1ib2xzOiB0cy5TeW1ib2xUYWJsZXx0cy5TeW1ib2xbXSwgcHJpdmF0ZSBjb250ZXh0OiBUeXBlQ29udGV4dCwgcHJpdmF0ZSB0eXBlPzogdHMuVHlwZSkge1xuICAgIHN5bWJvbHMgPSBzeW1ib2xzIHx8IFtdO1xuXG4gICAgaWYgKEFycmF5LmlzQXJyYXkoc3ltYm9scykpIHtcbiAgICAgIHRoaXMuc3ltYm9scyA9IHN5bWJvbHM7XG4gICAgICB0aGlzLnN5bWJvbFRhYmxlID0gdG9TeW1ib2xUYWJsZUZhY3Rvcnkoc3ltYm9scyk7XG4gICAgfSBlbHNlIHtcbiAgICAgIHRoaXMuc3ltYm9scyA9IHRvU3ltYm9scyhzeW1ib2xzKTtcbiAgICAgIHRoaXMuc3ltYm9sVGFibGUgPSBzeW1ib2xzO1xuICAgIH1cblxuICAgIGlmICh0eXBlKSB7XG4gICAgICB0aGlzLnN0cmluZ0luZGV4VHlwZSA9IHR5cGUuZ2V0U3RyaW5nSW5kZXhUeXBlKCk7XG4gICAgfVxuICB9XG5cbiAgZ2V0IHNpemUoKTogbnVtYmVyIHsgcmV0dXJuIHRoaXMuc3ltYm9scy5sZW5ndGg7IH1cblxuICBnZXQoa2V5OiBzdHJpbmcpOiBTeW1ib2x8dW5kZWZpbmVkIHtcbiAgICBjb25zdCBzeW1ib2wgPSBnZXRGcm9tU3ltYm9sVGFibGUodGhpcy5zeW1ib2xUYWJsZSwga2V5KTtcbiAgICBpZiAoc3ltYm9sKSB7XG4gICAgICByZXR1cm4gbmV3IFN5bWJvbFdyYXBwZXIoc3ltYm9sLCB0aGlzLmNvbnRleHQpO1xuICAgIH1cblxuICAgIGlmICh0aGlzLnN0cmluZ0luZGV4VHlwZSkge1xuICAgICAgLy8gSWYgdGhlIGtleSBkb2VzIG5vdCBleGlzdCBhcyBhbiBleHBsaWNpdCBzeW1ib2wgb24gdGhlIHR5cGUsIGl0IG1heSBiZSBhY2Nlc3NpbmcgYSBzdHJpbmdcbiAgICAgIC8vIGluZGV4IHNpZ25hdHVyZSB1c2luZyBkb3Qgbm90YXRpb246XG4gICAgICAvL1xuICAgICAgLy8gICBjb25zdCBvYmo8VD46IHsgW2tleTogc3RyaW5nXTogVCB9O1xuICAgICAgLy8gICBvYmouc3RyaW5nSW5kZXggLy8gZXF1aXZhbGVudCB0byBvYmpbJ3N0cmluZ0luZGV4J107XG4gICAgICAvL1xuICAgICAgLy8gSW4gdGhpcyBjYXNlLCByZXR1cm4gdGhlIHR5cGUgaW5kZXhlZCBieSBhbiBhcmJpdHJhcnkgc3RyaW5nIGtleS5cbiAgICAgIGNvbnN0IHN5bWJvbCA9IHRoaXMuc3RyaW5nSW5kZXhUeXBlLmdldFN5bWJvbCgpO1xuICAgICAgaWYgKHN5bWJvbCkge1xuICAgICAgICByZXR1cm4gbmV3IFN5bWJvbFdyYXBwZXIoc3ltYm9sLCB0aGlzLmNvbnRleHQsIHRoaXMuc3RyaW5nSW5kZXhUeXBlKTtcbiAgICAgIH1cbiAgICB9XG5cbiAgICByZXR1cm4gdW5kZWZpbmVkO1xuICB9XG5cbiAgaGFzKGtleTogc3RyaW5nKTogYm9vbGVhbiB7XG4gICAgY29uc3QgdGFibGU6IGFueSA9IHRoaXMuc3ltYm9sVGFibGU7XG4gICAgcmV0dXJuICgodHlwZW9mIHRhYmxlLmhhcyA9PT0gJ2Z1bmN0aW9uJykgPyB0YWJsZS5oYXMoa2V5KSA6IHRhYmxlW2tleV0gIT0gbnVsbCkgfHxcbiAgICAgICAgdGhpcy5zdHJpbmdJbmRleFR5cGUgIT09IHVuZGVmaW5lZDtcbiAgfVxuXG4gIHZhbHVlcygpOiBTeW1ib2xbXSB7IHJldHVybiB0aGlzLnN5bWJvbHMubWFwKHMgPT4gbmV3IFN5bWJvbFdyYXBwZXIocywgdGhpcy5jb250ZXh0KSk7IH1cbn1cblxuY2xhc3MgTWFwU3ltYm9sVGFibGUgaW1wbGVtZW50cyBTeW1ib2xUYWJsZSB7XG4gIHByaXZhdGUgbWFwID0gbmV3IE1hcDxzdHJpbmcsIFN5bWJvbD4oKTtcbiAgcHJpdmF0ZSBfdmFsdWVzOiBTeW1ib2xbXSA9IFtdO1xuXG4gIGdldCBzaXplKCk6IG51bWJlciB7IHJldHVybiB0aGlzLm1hcC5zaXplOyB9XG5cbiAgZ2V0KGtleTogc3RyaW5nKTogU3ltYm9sfHVuZGVmaW5lZCB7IHJldHVybiB0aGlzLm1hcC5nZXQoa2V5KTsgfVxuXG4gIGFkZChzeW1ib2w6IFN5bWJvbCkge1xuICAgIGlmICh0aGlzLm1hcC5oYXMoc3ltYm9sLm5hbWUpKSB7XG4gICAgICBjb25zdCBwcmV2aW91cyA9IHRoaXMubWFwLmdldChzeW1ib2wubmFtZSkgITtcbiAgICAgIHRoaXMuX3ZhbHVlc1t0aGlzLl92YWx1ZXMuaW5kZXhPZihwcmV2aW91cyldID0gc3ltYm9sO1xuICAgIH1cbiAgICB0aGlzLm1hcC5zZXQoc3ltYm9sLm5hbWUsIHN5bWJvbCk7XG4gICAgdGhpcy5fdmFsdWVzLnB1c2goc3ltYm9sKTtcbiAgfVxuXG4gIGFkZEFsbChzeW1ib2xzOiBTeW1ib2xbXSkge1xuICAgIGZvciAoY29uc3Qgc3ltYm9sIG9mIHN5bWJvbHMpIHtcbiAgICAgIHRoaXMuYWRkKHN5bWJvbCk7XG4gICAgfVxuICB9XG5cbiAgaGFzKGtleTogc3RyaW5nKTogYm9vbGVhbiB7IHJldHVybiB0aGlzLm1hcC5oYXMoa2V5KTsgfVxuXG4gIHZhbHVlcygpOiBTeW1ib2xbXSB7XG4gICAgLy8gU3dpdGNoIHRvIHRoaXMubWFwLnZhbHVlcyBvbmNlIGl0ZXJhYmxlcyBhcmUgc3VwcG9ydGVkIGJ5IHRoZSB0YXJnZXQgbGFuZ3VhZ2UuXG4gICAgcmV0dXJuIHRoaXMuX3ZhbHVlcztcbiAgfVxufVxuXG5jbGFzcyBQaXBlc1RhYmxlIGltcGxlbWVudHMgU3ltYm9sVGFibGUge1xuICBjb25zdHJ1Y3Rvcihwcml2YXRlIHBpcGVzOiBDb21waWxlUGlwZVN1bW1hcnlbXSwgcHJpdmF0ZSBjb250ZXh0OiBUeXBlQ29udGV4dCkge31cblxuICBnZXQgc2l6ZSgpIHsgcmV0dXJuIHRoaXMucGlwZXMubGVuZ3RoOyB9XG5cbiAgZ2V0KGtleTogc3RyaW5nKTogU3ltYm9sfHVuZGVmaW5lZCB7XG4gICAgY29uc3QgcGlwZSA9IHRoaXMucGlwZXMuZmluZChwaXBlID0+IHBpcGUubmFtZSA9PSBrZXkpO1xuICAgIGlmIChwaXBlKSB7XG4gICAgICByZXR1cm4gbmV3IFBpcGVTeW1ib2wocGlwZSwgdGhpcy5jb250ZXh0KTtcbiAgICB9XG4gIH1cblxuICBoYXMoa2V5OiBzdHJpbmcpOiBib29sZWFuIHsgcmV0dXJuIHRoaXMucGlwZXMuZmluZChwaXBlID0+IHBpcGUubmFtZSA9PSBrZXkpICE9IG51bGw7IH1cblxuICB2YWx1ZXMoKTogU3ltYm9sW10geyByZXR1cm4gdGhpcy5waXBlcy5tYXAocGlwZSA9PiBuZXcgUGlwZVN5bWJvbChwaXBlLCB0aGlzLmNvbnRleHQpKTsgfVxufVxuXG4vLyBUaGlzIG1hdGNoZXMgLmQudHMgZmlsZXMgdGhhdCBsb29rIGxpa2UgXCIuLi4vPHBhY2thZ2UtbmFtZT4vPHBhY2thZ2UtbmFtZT4uZC50c1wiLFxuY29uc3QgSU5ERVhfUEFUVEVSTiA9IC9bXFxcXC9dKFteXFxcXC9dKylbXFxcXC9dXFwxXFwuZFxcLnRzJC87XG5cbmNsYXNzIFBpcGVTeW1ib2wgaW1wbGVtZW50cyBTeW1ib2wge1xuICAvLyBUT0RPKGlzc3VlLzI0NTcxKTogcmVtb3ZlICchJy5cbiAgcHJpdmF0ZSBfdHNUeXBlICE6IHRzLlR5cGU7XG4gIHB1YmxpYyByZWFkb25seSBraW5kOiBEZWNsYXJhdGlvbktpbmQgPSAncGlwZSc7XG4gIHB1YmxpYyByZWFkb25seSBsYW5ndWFnZTogc3RyaW5nID0gJ3R5cGVzY3JpcHQnO1xuICBwdWJsaWMgcmVhZG9ubHkgY29udGFpbmVyOiBTeW1ib2x8dW5kZWZpbmVkID0gdW5kZWZpbmVkO1xuICBwdWJsaWMgcmVhZG9ubHkgY2FsbGFibGU6IGJvb2xlYW4gPSB0cnVlO1xuICBwdWJsaWMgcmVhZG9ubHkgbnVsbGFibGU6IGJvb2xlYW4gPSBmYWxzZTtcbiAgcHVibGljIHJlYWRvbmx5IHB1YmxpYzogYm9vbGVhbiA9IHRydWU7XG5cbiAgY29uc3RydWN0b3IocHJpdmF0ZSBwaXBlOiBDb21waWxlUGlwZVN1bW1hcnksIHByaXZhdGUgY29udGV4dDogVHlwZUNvbnRleHQpIHt9XG5cbiAgZ2V0IG5hbWUoKTogc3RyaW5nIHsgcmV0dXJuIHRoaXMucGlwZS5uYW1lOyB9XG5cbiAgZ2V0IHR5cGUoKTogU3ltYm9sfHVuZGVmaW5lZCB7IHJldHVybiBuZXcgVHlwZVdyYXBwZXIodGhpcy50c1R5cGUsIHRoaXMuY29udGV4dCk7IH1cblxuICBnZXQgZGVmaW5pdGlvbigpOiBEZWZpbml0aW9ufHVuZGVmaW5lZCB7XG4gICAgY29uc3Qgc3ltYm9sID0gdGhpcy50c1R5cGUuZ2V0U3ltYm9sKCk7XG4gICAgcmV0dXJuIHN5bWJvbCA/IGRlZmluaXRpb25Gcm9tVHNTeW1ib2woc3ltYm9sKSA6IHVuZGVmaW5lZDtcbiAgfVxuXG4gIG1lbWJlcnMoKTogU3ltYm9sVGFibGUgeyByZXR1cm4gRW1wdHlUYWJsZS5pbnN0YW5jZTsgfVxuXG4gIHNpZ25hdHVyZXMoKTogU2lnbmF0dXJlW10geyByZXR1cm4gc2lnbmF0dXJlc09mKHRoaXMudHNUeXBlLCB0aGlzLmNvbnRleHQpOyB9XG5cbiAgc2VsZWN0U2lnbmF0dXJlKHR5cGVzOiBTeW1ib2xbXSk6IFNpZ25hdHVyZXx1bmRlZmluZWQge1xuICAgIGxldCBzaWduYXR1cmUgPSBzZWxlY3RTaWduYXR1cmUodGhpcy50c1R5cGUsIHRoaXMuY29udGV4dCwgdHlwZXMpICE7XG4gICAgaWYgKHR5cGVzLmxlbmd0aCA+IDApIHtcbiAgICAgIGNvbnN0IHBhcmFtZXRlclR5cGUgPSB0eXBlc1swXTtcbiAgICAgIGlmIChwYXJhbWV0ZXJUeXBlIGluc3RhbmNlb2YgVHlwZVdyYXBwZXIpIHtcbiAgICAgICAgbGV0IHJlc3VsdFR5cGU6IHRzLlR5cGV8dW5kZWZpbmVkID0gdW5kZWZpbmVkO1xuICAgICAgICBzd2l0Y2ggKHRoaXMubmFtZSkge1xuICAgICAgICAgIGNhc2UgJ2FzeW5jJzpcbiAgICAgICAgICAgIHN3aXRjaCAocGFyYW1ldGVyVHlwZS5uYW1lKSB7XG4gICAgICAgICAgICAgIGNhc2UgJ09ic2VydmFibGUnOlxuICAgICAgICAgICAgICBjYXNlICdQcm9taXNlJzpcbiAgICAgICAgICAgICAgY2FzZSAnRXZlbnRFbWl0dGVyJzpcbiAgICAgICAgICAgICAgICByZXN1bHRUeXBlID0gZ2V0VHlwZVBhcmFtZXRlck9mKHBhcmFtZXRlclR5cGUudHNUeXBlLCBwYXJhbWV0ZXJUeXBlLm5hbWUpO1xuICAgICAgICAgICAgICAgIGJyZWFrO1xuICAgICAgICAgICAgICBkZWZhdWx0OlxuICAgICAgICAgICAgICAgIHJlc3VsdFR5cGUgPSBnZXRUc1R5cGVGcm9tQnVpbHRpblR5cGUoQnVpbHRpblR5cGUuQW55LCB0aGlzLmNvbnRleHQpO1xuICAgICAgICAgICAgICAgIGJyZWFrO1xuICAgICAgICAgICAgfVxuICAgICAgICAgICAgYnJlYWs7XG4gICAgICAgICAgY2FzZSAnc2xpY2UnOlxuICAgICAgICAgICAgcmVzdWx0VHlwZSA9IHBhcmFtZXRlclR5cGUudHNUeXBlO1xuICAgICAgICAgICAgYnJlYWs7XG4gICAgICAgIH1cbiAgICAgICAgaWYgKHJlc3VsdFR5cGUpIHtcbiAgICAgICAgICBzaWduYXR1cmUgPSBuZXcgU2lnbmF0dXJlUmVzdWx0T3ZlcnJpZGUoXG4gICAgICAgICAgICAgIHNpZ25hdHVyZSwgbmV3IFR5cGVXcmFwcGVyKHJlc3VsdFR5cGUsIHBhcmFtZXRlclR5cGUuY29udGV4dCkpO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuICAgIHJldHVybiBzaWduYXR1cmU7XG4gIH1cblxuICBpbmRleGVkKGFyZ3VtZW50OiBTeW1ib2wpOiBTeW1ib2x8dW5kZWZpbmVkIHsgcmV0dXJuIHVuZGVmaW5lZDsgfVxuXG4gIHByaXZhdGUgZ2V0IHRzVHlwZSgpOiB0cy5UeXBlIHtcbiAgICBsZXQgdHlwZSA9IHRoaXMuX3RzVHlwZTtcbiAgICBpZiAoIXR5cGUpIHtcbiAgICAgIGNvbnN0IGNsYXNzU3ltYm9sID0gdGhpcy5maW5kQ2xhc3NTeW1ib2wodGhpcy5waXBlLnR5cGUucmVmZXJlbmNlKTtcbiAgICAgIGlmIChjbGFzc1N5bWJvbCkge1xuICAgICAgICB0eXBlID0gdGhpcy5fdHNUeXBlID0gdGhpcy5maW5kVHJhbnNmb3JtTWV0aG9kVHlwZShjbGFzc1N5bWJvbCkgITtcbiAgICAgIH1cbiAgICAgIGlmICghdHlwZSkge1xuICAgICAgICB0eXBlID0gdGhpcy5fdHNUeXBlID0gZ2V0VHNUeXBlRnJvbUJ1aWx0aW5UeXBlKEJ1aWx0aW5UeXBlLkFueSwgdGhpcy5jb250ZXh0KTtcbiAgICAgIH1cbiAgICB9XG4gICAgcmV0dXJuIHR5cGU7XG4gIH1cblxuICBwcml2YXRlIGZpbmRDbGFzc1N5bWJvbCh0eXBlOiBTdGF0aWNTeW1ib2wpOiB0cy5TeW1ib2x8dW5kZWZpbmVkIHtcbiAgICByZXR1cm4gZmluZENsYXNzU3ltYm9sSW5Db250ZXh0KHR5cGUsIHRoaXMuY29udGV4dCk7XG4gIH1cblxuICBwcml2YXRlIGZpbmRUcmFuc2Zvcm1NZXRob2RUeXBlKGNsYXNzU3ltYm9sOiB0cy5TeW1ib2wpOiB0cy5UeXBlfHVuZGVmaW5lZCB7XG4gICAgY29uc3QgY2xhc3NUeXBlID0gdGhpcy5jb250ZXh0LmNoZWNrZXIuZ2V0RGVjbGFyZWRUeXBlT2ZTeW1ib2woY2xhc3NTeW1ib2wpO1xuICAgIGlmIChjbGFzc1R5cGUpIHtcbiAgICAgIGNvbnN0IHRyYW5zZm9ybSA9IGNsYXNzVHlwZS5nZXRQcm9wZXJ0eSgndHJhbnNmb3JtJyk7XG4gICAgICBpZiAodHJhbnNmb3JtKSB7XG4gICAgICAgIHJldHVybiB0aGlzLmNvbnRleHQuY2hlY2tlci5nZXRUeXBlT2ZTeW1ib2xBdExvY2F0aW9uKHRyYW5zZm9ybSwgdGhpcy5jb250ZXh0Lm5vZGUpO1xuICAgICAgfVxuICAgIH1cbiAgfVxufVxuXG5mdW5jdGlvbiBmaW5kQ2xhc3NTeW1ib2xJbkNvbnRleHQodHlwZTogU3RhdGljU3ltYm9sLCBjb250ZXh0OiBUeXBlQ29udGV4dCk6IHRzLlN5bWJvbHx1bmRlZmluZWQge1xuICBsZXQgc291cmNlRmlsZSA9IGNvbnRleHQucHJvZ3JhbS5nZXRTb3VyY2VGaWxlKHR5cGUuZmlsZVBhdGgpO1xuICBpZiAoIXNvdXJjZUZpbGUpIHtcbiAgICAvLyBUaGlzIGhhbmRsZXMgYSBjYXNlIHdoZXJlIGFuIDxwYWNrYWdlTmFtZT4vaW5kZXguZC50cyBhbmQgYSA8cGFja2FnZU5hbWU+LzxwYWNrYWdlTmFtZT4uZC50c1xuICAgIC8vIGFyZSBpbiB0aGUgc2FtZSBkaXJlY3RvcnkuIElmIHdlIGFyZSBsb29raW5nIGZvciA8cGFja2FnZU5hbWU+LzxwYWNrYWdlTmFtZT4gYW5kIGRpZG4ndFxuICAgIC8vIGZpbmQgaXQsIGxvb2sgZm9yIDxwYWNrYWdlTmFtZT4vaW5kZXguZC50cyBhcyB0aGUgcHJvZ3JhbSBtaWdodCBoYXZlIGZvdW5kIHRoYXQgaW5zdGVhZC5cbiAgICBjb25zdCBwID0gdHlwZS5maWxlUGF0aDtcbiAgICBjb25zdCBtID0gcC5tYXRjaChJTkRFWF9QQVRURVJOKTtcbiAgICBpZiAobSkge1xuICAgICAgY29uc3QgaW5kZXhWZXJzaW9uID0gcGF0aC5qb2luKHBhdGguZGlybmFtZShwKSwgJ2luZGV4LmQudHMnKTtcbiAgICAgIHNvdXJjZUZpbGUgPSBjb250ZXh0LnByb2dyYW0uZ2V0U291cmNlRmlsZShpbmRleFZlcnNpb24pO1xuICAgIH1cbiAgfVxuICBpZiAoc291cmNlRmlsZSkge1xuICAgIGNvbnN0IG1vZHVsZVN5bWJvbCA9IChzb3VyY2VGaWxlIGFzIGFueSkubW9kdWxlIHx8IChzb3VyY2VGaWxlIGFzIGFueSkuc3ltYm9sO1xuICAgIGNvbnN0IGV4cG9ydHMgPSBjb250ZXh0LmNoZWNrZXIuZ2V0RXhwb3J0c09mTW9kdWxlKG1vZHVsZVN5bWJvbCk7XG4gICAgcmV0dXJuIChleHBvcnRzIHx8IFtdKS5maW5kKHN5bWJvbCA9PiBzeW1ib2wubmFtZSA9PSB0eXBlLm5hbWUpO1xuICB9XG59XG5cbmNsYXNzIEVtcHR5VGFibGUgaW1wbGVtZW50cyBTeW1ib2xUYWJsZSB7XG4gIHB1YmxpYyByZWFkb25seSBzaXplOiBudW1iZXIgPSAwO1xuICBnZXQoa2V5OiBzdHJpbmcpOiBTeW1ib2x8dW5kZWZpbmVkIHsgcmV0dXJuIHVuZGVmaW5lZDsgfVxuICBoYXMoa2V5OiBzdHJpbmcpOiBib29sZWFuIHsgcmV0dXJuIGZhbHNlOyB9XG4gIHZhbHVlcygpOiBTeW1ib2xbXSB7IHJldHVybiBbXTsgfVxuICBzdGF0aWMgaW5zdGFuY2UgPSBuZXcgRW1wdHlUYWJsZSgpO1xufVxuXG5mdW5jdGlvbiBpc1N5bWJvbFByaXZhdGUoczogdHMuU3ltYm9sKTogYm9vbGVhbiB7XG4gIHJldHVybiAhIXMudmFsdWVEZWNsYXJhdGlvbiAmJiBpc1ByaXZhdGUocy52YWx1ZURlY2xhcmF0aW9uKTtcbn1cblxuZnVuY3Rpb24gZ2V0VHNUeXBlRnJvbUJ1aWx0aW5UeXBlKGJ1aWx0aW5UeXBlOiBCdWlsdGluVHlwZSwgY3R4OiBUeXBlQ29udGV4dCk6IHRzLlR5cGUge1xuICBsZXQgc3ludGF4S2luZDogdHMuU3ludGF4S2luZDtcbiAgc3dpdGNoIChidWlsdGluVHlwZSkge1xuICAgIGNhc2UgQnVpbHRpblR5cGUuQW55OlxuICAgICAgc3ludGF4S2luZCA9IHRzLlN5bnRheEtpbmQuQW55S2V5d29yZDtcbiAgICAgIGJyZWFrO1xuICAgIGNhc2UgQnVpbHRpblR5cGUuQm9vbGVhbjpcbiAgICAgIHN5bnRheEtpbmQgPSB0cy5TeW50YXhLaW5kLkJvb2xlYW5LZXl3b3JkO1xuICAgICAgYnJlYWs7XG4gICAgY2FzZSBCdWlsdGluVHlwZS5OdWxsOlxuICAgICAgc3ludGF4S2luZCA9IHRzLlN5bnRheEtpbmQuTnVsbEtleXdvcmQ7XG4gICAgICBicmVhaztcbiAgICBjYXNlIEJ1aWx0aW5UeXBlLk51bWJlcjpcbiAgICAgIHN5bnRheEtpbmQgPSB0cy5TeW50YXhLaW5kLk51bWJlcktleXdvcmQ7XG4gICAgICBicmVhaztcbiAgICBjYXNlIEJ1aWx0aW5UeXBlLlN0cmluZzpcbiAgICAgIHN5bnRheEtpbmQgPSB0cy5TeW50YXhLaW5kLlN0cmluZ0tleXdvcmQ7XG4gICAgICBicmVhaztcbiAgICBjYXNlIEJ1aWx0aW5UeXBlLlVuZGVmaW5lZDpcbiAgICAgIHN5bnRheEtpbmQgPSB0cy5TeW50YXhLaW5kLlVuZGVmaW5lZEtleXdvcmQ7XG4gICAgICBicmVhaztcbiAgICBkZWZhdWx0OlxuICAgICAgdGhyb3cgbmV3IEVycm9yKFxuICAgICAgICAgIGBJbnRlcm5hbCBlcnJvciwgdW5oYW5kbGVkIGxpdGVyYWwga2luZCAke2J1aWx0aW5UeXBlfToke0J1aWx0aW5UeXBlW2J1aWx0aW5UeXBlXX1gKTtcbiAgfVxuICBjb25zdCBub2RlID0gdHMuY3JlYXRlTm9kZShzeW50YXhLaW5kKTtcbiAgbm9kZS5wYXJlbnQgPSBjdHgubm9kZTtcbiAgcmV0dXJuIGN0eC5jaGVja2VyLmdldFR5cGVBdExvY2F0aW9uKG5vZGUpO1xufVxuXG5mdW5jdGlvbiBzcGFuQXQoc291cmNlRmlsZTogdHMuU291cmNlRmlsZSwgbGluZTogbnVtYmVyLCBjb2x1bW46IG51bWJlcik6IFNwYW58dW5kZWZpbmVkIHtcbiAgaWYgKGxpbmUgIT0gbnVsbCAmJiBjb2x1bW4gIT0gbnVsbCkge1xuICAgIGNvbnN0IHBvc2l0aW9uID0gdHMuZ2V0UG9zaXRpb25PZkxpbmVBbmRDaGFyYWN0ZXIoc291cmNlRmlsZSwgbGluZSwgY29sdW1uKTtcbiAgICBjb25zdCBmaW5kQ2hpbGQgPSBmdW5jdGlvbiBmaW5kQ2hpbGQobm9kZTogdHMuTm9kZSk6IHRzLk5vZGUgfCB1bmRlZmluZWQge1xuICAgICAgaWYgKG5vZGUua2luZCA+IHRzLlN5bnRheEtpbmQuTGFzdFRva2VuICYmIG5vZGUucG9zIDw9IHBvc2l0aW9uICYmIG5vZGUuZW5kID4gcG9zaXRpb24pIHtcbiAgICAgICAgY29uc3QgYmV0dGVyTm9kZSA9IHRzLmZvckVhY2hDaGlsZChub2RlLCBmaW5kQ2hpbGQpO1xuICAgICAgICByZXR1cm4gYmV0dGVyTm9kZSB8fCBub2RlO1xuICAgICAgfVxuICAgIH07XG5cbiAgICBjb25zdCBub2RlID0gdHMuZm9yRWFjaENoaWxkKHNvdXJjZUZpbGUsIGZpbmRDaGlsZCk7XG4gICAgaWYgKG5vZGUpIHtcbiAgICAgIHJldHVybiB7c3RhcnQ6IG5vZGUuZ2V0U3RhcnQoKSwgZW5kOiBub2RlLmdldEVuZCgpfTtcbiAgICB9XG4gIH1cbn1cblxuZnVuY3Rpb24gZGVmaW5pdGlvbkZyb21Uc1N5bWJvbChzeW1ib2w6IHRzLlN5bWJvbCk6IERlZmluaXRpb24ge1xuICBjb25zdCBkZWNsYXJhdGlvbnMgPSBzeW1ib2wuZGVjbGFyYXRpb25zO1xuICBpZiAoZGVjbGFyYXRpb25zKSB7XG4gICAgcmV0dXJuIGRlY2xhcmF0aW9ucy5tYXAoZGVjbGFyYXRpb24gPT4ge1xuICAgICAgY29uc3Qgc291cmNlRmlsZSA9IGRlY2xhcmF0aW9uLmdldFNvdXJjZUZpbGUoKTtcbiAgICAgIHJldHVybiB7XG4gICAgICAgIGZpbGVOYW1lOiBzb3VyY2VGaWxlLmZpbGVOYW1lLFxuICAgICAgICBzcGFuOiB7c3RhcnQ6IGRlY2xhcmF0aW9uLmdldFN0YXJ0KCksIGVuZDogZGVjbGFyYXRpb24uZ2V0RW5kKCl9XG4gICAgICB9O1xuICAgIH0pO1xuICB9XG59XG5cbmZ1bmN0aW9uIHBhcmVudERlY2xhcmF0aW9uT2Yobm9kZTogdHMuTm9kZSk6IHRzLk5vZGV8dW5kZWZpbmVkIHtcbiAgd2hpbGUgKG5vZGUpIHtcbiAgICBzd2l0Y2ggKG5vZGUua2luZCkge1xuICAgICAgY2FzZSB0cy5TeW50YXhLaW5kLkNsYXNzRGVjbGFyYXRpb246XG4gICAgICBjYXNlIHRzLlN5bnRheEtpbmQuSW50ZXJmYWNlRGVjbGFyYXRpb246XG4gICAgICAgIHJldHVybiBub2RlO1xuICAgICAgY2FzZSB0cy5TeW50YXhLaW5kLlNvdXJjZUZpbGU6XG4gICAgICAgIHJldHVybiB1bmRlZmluZWQ7XG4gICAgfVxuICAgIG5vZGUgPSBub2RlLnBhcmVudCAhO1xuICB9XG59XG5cbmZ1bmN0aW9uIGdldENvbnRhaW5lck9mKHN5bWJvbDogdHMuU3ltYm9sLCBjb250ZXh0OiBUeXBlQ29udGV4dCk6IFN5bWJvbHx1bmRlZmluZWQge1xuICBpZiAoc3ltYm9sLmdldEZsYWdzKCkgJiB0cy5TeW1ib2xGbGFncy5DbGFzc01lbWJlciAmJiBzeW1ib2wuZGVjbGFyYXRpb25zKSB7XG4gICAgZm9yIChjb25zdCBkZWNsYXJhdGlvbiBvZiBzeW1ib2wuZGVjbGFyYXRpb25zKSB7XG4gICAgICBjb25zdCBwYXJlbnQgPSBwYXJlbnREZWNsYXJhdGlvbk9mKGRlY2xhcmF0aW9uKTtcbiAgICAgIGlmIChwYXJlbnQpIHtcbiAgICAgICAgY29uc3QgdHlwZSA9IGNvbnRleHQuY2hlY2tlci5nZXRUeXBlQXRMb2NhdGlvbihwYXJlbnQpO1xuICAgICAgICBpZiAodHlwZSkge1xuICAgICAgICAgIHJldHVybiBuZXcgVHlwZVdyYXBwZXIodHlwZSwgY29udGV4dCk7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICB9XG4gIH1cbn1cblxuZnVuY3Rpb24gZ2V0VHlwZVBhcmFtZXRlck9mKHR5cGU6IHRzLlR5cGUsIG5hbWU6IHN0cmluZyk6IHRzLlR5cGV8dW5kZWZpbmVkIHtcbiAgaWYgKHR5cGUgJiYgdHlwZS5zeW1ib2wgJiYgdHlwZS5zeW1ib2wubmFtZSA9PSBuYW1lKSB7XG4gICAgY29uc3QgdHlwZUFyZ3VtZW50czogdHMuVHlwZVtdID0gKHR5cGUgYXMgYW55KS50eXBlQXJndW1lbnRzO1xuICAgIGlmICh0eXBlQXJndW1lbnRzICYmIHR5cGVBcmd1bWVudHMubGVuZ3RoIDw9IDEpIHtcbiAgICAgIHJldHVybiB0eXBlQXJndW1lbnRzWzBdO1xuICAgIH1cbiAgfVxufVxuXG5mdW5jdGlvbiB0eXBlS2luZE9mKHR5cGU6IHRzLlR5cGUgfCB1bmRlZmluZWQpOiBCdWlsdGluVHlwZSB7XG4gIGlmICh0eXBlKSB7XG4gICAgaWYgKHR5cGUuZmxhZ3MgJiB0cy5UeXBlRmxhZ3MuQW55KSB7XG4gICAgICByZXR1cm4gQnVpbHRpblR5cGUuQW55O1xuICAgIH0gZWxzZSBpZiAoXG4gICAgICAgIHR5cGUuZmxhZ3MgJiAodHMuVHlwZUZsYWdzLlN0cmluZyB8IHRzLlR5cGVGbGFncy5TdHJpbmdMaWtlIHwgdHMuVHlwZUZsYWdzLlN0cmluZ0xpdGVyYWwpKSB7XG4gICAgICByZXR1cm4gQnVpbHRpblR5cGUuU3RyaW5nO1xuICAgIH0gZWxzZSBpZiAodHlwZS5mbGFncyAmICh0cy5UeXBlRmxhZ3MuTnVtYmVyIHwgdHMuVHlwZUZsYWdzLk51bWJlckxpa2UpKSB7XG4gICAgICByZXR1cm4gQnVpbHRpblR5cGUuTnVtYmVyO1xuICAgIH0gZWxzZSBpZiAodHlwZS5mbGFncyAmICh0cy5UeXBlRmxhZ3MuVW5kZWZpbmVkKSkge1xuICAgICAgcmV0dXJuIEJ1aWx0aW5UeXBlLlVuZGVmaW5lZDtcbiAgICB9IGVsc2UgaWYgKHR5cGUuZmxhZ3MgJiAodHMuVHlwZUZsYWdzLk51bGwpKSB7XG4gICAgICByZXR1cm4gQnVpbHRpblR5cGUuTnVsbDtcbiAgICB9IGVsc2UgaWYgKHR5cGUuZmxhZ3MgJiB0cy5UeXBlRmxhZ3MuVW5pb24pIHtcbiAgICAgIC8vIElmIGFsbCB0aGUgY29uc3RpdHVlbnQgdHlwZXMgb2YgYSB1bmlvbiBhcmUgdGhlIHNhbWUga2luZCwgaXQgaXMgYWxzbyB0aGF0IGtpbmQuXG4gICAgICBsZXQgY2FuZGlkYXRlOiBCdWlsdGluVHlwZXxudWxsID0gbnVsbDtcbiAgICAgIGNvbnN0IHVuaW9uVHlwZSA9IHR5cGUgYXMgdHMuVW5pb25UeXBlO1xuICAgICAgaWYgKHVuaW9uVHlwZS50eXBlcy5sZW5ndGggPiAwKSB7XG4gICAgICAgIGNhbmRpZGF0ZSA9IHR5cGVLaW5kT2YodW5pb25UeXBlLnR5cGVzWzBdKTtcbiAgICAgICAgZm9yIChjb25zdCBzdWJUeXBlIG9mIHVuaW9uVHlwZS50eXBlcykge1xuICAgICAgICAgIGlmIChjYW5kaWRhdGUgIT0gdHlwZUtpbmRPZihzdWJUeXBlKSkge1xuICAgICAgICAgICAgcmV0dXJuIEJ1aWx0aW5UeXBlLk90aGVyO1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgfVxuICAgICAgaWYgKGNhbmRpZGF0ZSAhPSBudWxsKSB7XG4gICAgICAgIHJldHVybiBjYW5kaWRhdGU7XG4gICAgICB9XG4gICAgfSBlbHNlIGlmICh0eXBlLmZsYWdzICYgdHMuVHlwZUZsYWdzLlR5cGVQYXJhbWV0ZXIpIHtcbiAgICAgIHJldHVybiBCdWlsdGluVHlwZS5VbmJvdW5kO1xuICAgIH1cbiAgfVxuICByZXR1cm4gQnVpbHRpblR5cGUuT3RoZXI7XG59XG5cbmZ1bmN0aW9uIGdldEZyb21TeW1ib2xUYWJsZShzeW1ib2xUYWJsZTogdHMuU3ltYm9sVGFibGUsIGtleTogc3RyaW5nKTogdHMuU3ltYm9sfHVuZGVmaW5lZCB7XG4gIGNvbnN0IHRhYmxlID0gc3ltYm9sVGFibGUgYXMgYW55O1xuICBsZXQgc3ltYm9sOiB0cy5TeW1ib2x8dW5kZWZpbmVkO1xuXG4gIGlmICh0eXBlb2YgdGFibGUuZ2V0ID09PSAnZnVuY3Rpb24nKSB7XG4gICAgLy8gVFMgMi4yIHVzZXMgYSBNYXBcbiAgICBzeW1ib2wgPSB0YWJsZS5nZXQoa2V5KTtcbiAgfSBlbHNlIHtcbiAgICAvLyBUUyBwcmUtMi4yIHVzZXMgYW4gb2JqZWN0XG4gICAgc3ltYm9sID0gdGFibGVba2V5XTtcbiAgfVxuXG4gIHJldHVybiBzeW1ib2w7XG59XG4iXX0=