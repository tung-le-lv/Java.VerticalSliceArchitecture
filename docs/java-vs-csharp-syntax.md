# Java vs C# — Syntax Comparison (Latest Versions)

**Java 25** (LTS, Sept 2025) vs **C# 14** (.NET 10 LTS, Nov 2025)

> Written for someone coming from C# into Java. The **Since** column notes the version a feature first appeared, so you can tell modern syntax from classic syntax. `—` in a cell means the language has no direct equivalent.

---

## 1. Program structure & entry point

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Classic entry point | `public static void main(String[] args) { }` | `static void Main(string[] args) { }` | Both still valid. |
| Minimal entry point | `void main() { }` *(Since 25, JEP 512)* | Top-level statements: just write code in `Program.cs` *(Since 9)* | Java's is an implicit-class instance `main`; C# has no ceremony at all. |
| Script / single file | `java App.java` runs directly *(Since 11)* | `dotnet run App.cs` — file-based apps, no `.csproj` *(Since .NET 10)* | Both now support run-a-single-file. |
| Console output | `System.out.println(x);` — or `IO.println(x);` *(Since 25)* | `Console.WriteLine(x);` | |

---

## 2. Namespaces / packages & imports

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Grouping unit | `package com.acme.app;` (maps to folders) | `namespace Acme.App;` (file-scoped, Since 10) | Java package **must** mirror the directory; C# namespace is logical. |
| Block namespace | — | `namespace Acme.App { ... }` | |
| Import a type | `import java.util.List;` | `using System.Collections.Generic;` | Java imports a **type**; C# `using` imports a **namespace**. |
| Import everything | `import java.util.*;` | (a namespace `using` already pulls all its types) | |
| Import static members | `import static java.lang.Math.PI;` | `using static System.Math;` | |
| Alias | `import java.util.List;` *(no rename)* | `using Dict = System.Collections.Generic.Dictionary<int,int>;` | Java has no import alias. |
| Global / project-wide import | — *(modules aside)* | `global using System;` *(Since 10)* | |

---

## 3. Variables, type inference & constants

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Explicit local | `int x = 5;` | `int x = 5;` | |
| Inferred local | `var x = 5;` *(Since 10)* | `var x = 5;` *(Since 3)* | Both compile-time, statically typed. |
| Constant (compile-time) | `static final int MAX = 10;` | `const int Max = 10;` | |
| Immutable local | `final int x = 5;` | (no per-local keyword; `var`/type is enough) | |
| Runtime-set constant | `final` field set in constructor | `readonly int _x;` set in constructor | |
| Uninitialized-but-typed | `int x;` | `int x;` | |

---

## 4. Built-in / primitive types

| Purpose | Java 25 | C# 14 | Notes |
|---|---|---|---|
| 32-bit int | `int` | `int` (alias of `System.Int32`) | |
| 64-bit int | `long` | `long` | Java literal `100L`; C# `100L`. |
| 8/16-bit | `byte`, `short` | `sbyte`/`byte`, `short`/`ushort` | C# has unsigned variants; Java's `byte` is signed. |
| Unsigned ints | — *(only helper methods)* | `uint`, `ulong`, `ushort`, `byte` | Java has no native unsigned types. |
| Float / double | `float`, `double` | `float`, `double` | |
| High-precision decimal | `BigDecimal` (library) | `decimal` (built-in) | |
| Boolean | `boolean` | `bool` | |
| Character | `char` (UTF-16) | `char` (UTF-16) | |
| Text | `String` (class) | `string` (alias of `System.String`) | |
| Object root | `Object` | `object` | |
| Boxing | Wrapper types: `Integer`, `Double`… (auto-boxed) | Implicit box to `object` | Java distinguishes `int` vs `Integer`; C# has no wrapper classes. |

---

## 5. Strings

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Concatenation | `"a" + b` | `"a" + b` | |
| Interpolation | `STR."Hi \{name}"` was **removed**; use `String.format("Hi %s", name)` or `"Hi " + name` | `$"Hi {name}"` *(Since 6)* | Java's string-template preview was withdrawn; no interpolation in 25. |
| Formatted | `String.format("%d items", n)` / `"%d".formatted(n)` | `$"{n} items"` or `string.Format(...)` | |
| Multi-line / raw | Text block `"""` … `"""` *(Since 15)* | Raw string `"""` … `"""` *(Since 11)* | Same delimiter, very similar rules. |
| Verbatim (no escapes) | Text blocks handle most cases | `@"C:\path\no\escapes"` | |
| Char access | `s.charAt(0)` | `s[0]` | |
| Length | `s.length()` (method) | `s.Length` (property) | |
| Equality | `s.equals(t)` — **never** `==` for value compare | `s == t` (value compares) | Java `==` compares references. |
| Mutable builder | `StringBuilder` | `StringBuilder` | |

---

## 6. Null handling

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Null literal | `null` | `null` | |
| Nullable reference types | *(all references nullable; no compiler tracking)* | `string?` opt-in with `#nullable enable` *(Since 8)* | C# tracks nullability at compile time; Java relies on `@Nullable` annotations (tooling, not language). |
| Nullable value type | `Integer` (wrapper can be null) | `int?` = `Nullable<int>` | |
| Null-coalescing | `Objects.requireNonNullElse(a, b)` | `a ?? b` *(Since 8)* | |
| Null-coalescing assign | — | `a ??= b;` *(Since 8)* | |
| Null-conditional access | `Optional.ofNullable(a).map(...)` | `a?.b?.c` *(Since 6)* | Java has no `?.`; use `Optional` or manual checks. |
| Null-conditional assign | — | `a?.b = c;` *(Since 14)* | |
| "Maybe" container | `Optional<T>` | *(prefer `T?`)* | Idioms differ: Java wraps, C# annotates. |

---

## 7. Operators (the ones that differ)

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Integer division | `/` | `/` | Same. |
| Logical AND/OR | `&&`, `\|\|` | `&&`, `\|\|` | Same. |
| Bitwise | `&`, `\|`, `^`, `~`, `<<`, `>>`, `>>>` | `&`, `\|`, `^`, `~`, `<<`, `>>`, `>>>` *(`>>>` Since 11)* | Java's `>>>` = unsigned right shift. |
| Ternary | `cond ? a : b` | `cond ? a : b` | Same. |
| Type test + bind | `if (o instanceof String s)` *(Since 16)* | `if (o is string s)` *(Since 7)* | Both pattern-bind. |
| Cast | `(String) o` | `(string) o` / `o as string` | C# adds safe `as` (returns null on fail). |
| Range / index | — | `arr[^1]`, `arr[1..3]` *(Since 8)* | Java uses methods (`subList`, etc.). |
| Overloadable operators | — *(not supported)* | `+`, `-`, `==`, etc. definable | Java has no operator overloading. |

---

## 8. Control flow & pattern matching

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| if / else | `if (c) {} else {}` | `if (c) {} else {}` | Same. |
| Classic switch | `switch (x) { case 1: …; break; }` | `switch (x) { case 1: …; break; }` | |
| Switch expression | `var r = switch (x) { case A -> 1; default -> 0; };` *(Since 14)* | `var r = x switch { A => 1, _ => 0 };` *(Since 8)* | Java uses `->` and `default`; C# uses `=>` and `_`. |
| Type patterns in switch | `case String s -> …` *(Since 21)* | `case string s => …` *(Since 8)* | |
| Guarded pattern | `case Integer i when i > 0 -> …` | `Point { X: > 0 } p => …` | Both support guards/property patterns. |
| Record deconstruction | `case Point(int x, int y) -> …` *(Since 21)* | `case Point(var x, var y) => …` *(Since 8)* | |
| Null in switch | `case null -> …` *(Since 21)* | `null => …` | |
| Exhaustiveness | Enforced for sealed types + switch expr | Warned, not enforced | |

---

## 9. Loops

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| C-style for | `for (int i = 0; i < n; i++)` | `for (int i = 0; i < n; i++)` | Same. |
| For-each | `for (var item : list)` | `foreach (var item in list)` | Keyword/separator differ (`:` vs `in`). |
| While / do-while | `while (c) {}` / `do {} while (c);` | Same | |
| Break / continue | `break;` / `continue;` (+ labels) | `break;` / `continue;` (+ `goto`) | Java has labeled break; C# has `goto`. |

---

## 10. Methods & parameters

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Declaration | `int add(int a, int b) { return a + b; }` | `int Add(int a, int b) { return a + b; }` | C# convention: PascalCase methods. |
| Expression body | — | `int Add(int a, int b) => a + b;` *(Since 6)* | Java always needs a block. |
| Variable arity | `void f(int... xs)` (varargs) | `void F(params int[] xs)` / `params ReadOnlySpan<int>` *(collections Since 13)* | |
| Optional params | — *(use overloads)* | `void F(int x = 0)` | Java has no default parameter values. |
| Named arguments | — | `F(x: 1, y: 2)` | Java has no named args. |
| Pass by reference | — *(objects pass reference-by-value)* | `ref`, `out`, `in` | Java has no `ref`/`out`. |
| Local functions | — *(use lambdas)* | `int Helper() => …;` inside a method | |
| Overloading | Yes | Yes | Both support. |

---

## 11. Classes & constructors

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Class | `class Foo { }` | `class Foo { }` | |
| Constructor | `Foo(int x) { this.x = x; }` | `public Foo(int x) { this.x = x; }` | |
| Primary constructor | — *(use `record` or write it out)* | `class Foo(int x) { … }` *(Since 12)* | Java has **no** primary ctor for regular classes. |
| Statements before super() | Allowed *(Since 25, JEP 513)* | Always allowed | Java only recently relaxed this. |
| Call other ctor | `this(...)` | `: this(...)` | |
| Call base ctor | `super(...)` | `: base(...)` | |
| Object init | `new Foo(1)` | `new Foo(1)` or `new()` target-typed *(Since 9)* | |
| Object initializer | — | `new Foo { X = 1, Y = 2 }` | Java has no initializer blocks like this. |
| Static class | `final class` with private ctor *(idiom)* | `static class Foo` | |
| Partial class | — | `partial class Foo` | |
| Finalizer | `finalize()` *(deprecated)* — use `Cleaner` | `~Foo() { }` | |

---

## 12. Properties / accessors

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Property concept | — *(no language property)* | first-class | This is a **major** philosophical split. |
| Java idiom | `getX()` / `setX(v)` methods | — | |
| Auto property | — | `public int X { get; set; }` | |
| Read-only auto | `final` field + getter | `public int X { get; }` or `{ get; init; }` *(Since 9)* | |
| Computed / expression | `int getArea() { return w*h; }` | `public int Area => w * h;` | |
| Custom accessor + backing field | manual field + method | `public int X { get; set => field = value; }` — `field` keyword *(Since 14)* | C# 14 removes backing-field boilerplate. |
| Required on init | — | `public required int X { get; init; }` *(Since 11)* | |
| Indexer | — *(use `get(i)`)* | `public T this[int i] { get; set; }` | |

---

## 13. Records (data carriers)

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Declaration | `record Point(int x, int y) {}` *(Since 16)* | `record Point(int X, int Y);` *(Since 9)* | Very close mapping. |
| What's generated | ctor, accessors `x()`/`y()`, `equals`, `hashCode`, `toString` | ctor, props, `Equals`, `GetHashCode`, `ToString`, `Deconstruct` | |
| Mutability | **Always immutable** (final fields) | Immutable by default; `record` props are `init` | |
| Value equality | Yes | Yes | |
| Non-destructive copy | — *(write manually)* | `p with { X = 5 }` *(Since 9)* | Java has no `with`. |
| Record struct (value type) | — | `record struct Point(int X, int Y);` *(Since 10)* | |
| Extra members allowed | Yes (methods, static) | Yes | |
| Compact/validating ctor | `Point { if (x<0) throw…; }` | validate in body or `init` | |

---

## 14. Structs / value types

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Value type | — *(all classes are reference types; Project Valhalla pending)* | `struct Point { … }` | Java has no user-defined value types **yet**. |
| Readonly value type | — | `readonly struct Point` | |
| Ref struct (stack-only) | — | `ref struct Buffer` | |
| Records as structs | *(records are reference types)* | `record struct` | |

---

## 15. Interfaces

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Declaration | `interface Shape { double area(); }` | `interface IShape { double Area(); }` | C# convention prefixes `I`. |
| Default method | `default double perimeter() { … }` *(Since 8)* | `double Perimeter() => …;` *(Since 8)* | Both allow default implementations. |
| Static method | `static Shape unit() { … }` | `static IShape Unit() { … }` | |
| Private method | `private void helper() {}` *(Since 9)* | `private void Helper() {}` | |
| Constants | `int MAX = 10;` (implicitly public static final) | `const`/`static` members | |
| Static abstract members | — | `static abstract T Parse(string s);` *(Since 11)* | Enables generic math in C#. |
| Properties in interface | *(getter/setter methods)* | `int X { get; set; }` | |

---

## 16. Abstract classes, inheritance, sealing

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Abstract class | `abstract class Base { abstract void f(); }` | `abstract class Base { abstract void F(); }` | |
| Extend | `class D extends Base` | `class D : Base` | |
| Implement interface | `class D implements I1, I2` | `class D : I1, I2` | C# uses one `:` list for base + interfaces. |
| Override | `@Override void f() {}` (annotation optional) | `override void F() {}` (keyword required) | C# requires `virtual` on base + `override`. |
| Mark overridable | *(methods virtual by default)* | `virtual` keyword needed | Java opposite default: virtual unless `final`. |
| Prevent override | `final void f()` | `sealed override void F()` | |
| Prevent subclass | `final class Foo` | `sealed class Foo` | |
| Restrict subclasses | `sealed … permits A, B` *(Since 17)* | — *(no `permits` list; use `sealed`+internal ctor)* | Different meaning of "sealed"! |
| Hide (not override) | — | `new void F()` | |
| Call base member | `super.f()` | `base.F()` | |

> ⚠️ **False friend:** Java `sealed` = "restrict which classes may extend me (with `permits`)". C# `sealed` = "no one may extend me at all" (= Java's `final class`).

---

## 17. Enums

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Basic enum | `enum Color { RED, GREEN, BLUE }` | `enum Color { Red, Green, Blue }` | |
| Backing value | *(ordinal only; add fields manually)* | `enum Color : byte { Red = 1 }` | C# enums are named integers. |
| Fields & methods | Rich: `enum Planet { EARTH(5.97e24); … }` with ctors/methods | — *(enums are just constants)* | Java enums are full classes. |
| Behavior per constant | Yes (abstract methods per constant) | — *(use switch)* | |
| Flags | `EnumSet` | `[Flags] enum` + bitwise | |
| Iterate values | `Color.values()` | `Enum.GetValues<Color>()` | |

---

## 18. Generics

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Generic class | `class Box<T> { }` | `class Box<T> { }` | |
| Generic method | `<T> T id(T x) { return x; }` | `T Id<T>(T x) => x;` | |
| Constraint / bound | `<T extends Comparable<T>>` | `where T : IComparable<T>` | |
| Multiple bounds | `<T extends A & B>` | `where T : A, B` | |
| `new` constraint | — | `where T : new()` | |
| Reference/value constraint | — | `where T : class` / `where T : struct` | |
| Wildcards | `List<? extends Number>` / `List<? super Integer>` | *(use variance instead)* | Java = use-site variance. |
| Declaration-site variance | — | `interface IEnumerable<out T>`, `in T` | C# = declaration-site variance. |
| Runtime type info | **Erased** (no `T.class` at runtime) | **Reified** (`typeof(T)`, `T` available at runtime) | Big semantic difference. |
| Array of generic | Restricted (erasure) | `new T[n]` allowed | |

---

## 19. Lambdas & functional types

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Lambda | `(a, b) -> a + b` | `(a, b) => a + b` | Arrow token differs. |
| Function type | Functional interfaces: `Function<T,R>`, `Predicate<T>`, `Consumer<T>`, `Supplier<T>`, `Runnable` | Delegates: `Func<T,R>`, `Action<T>`, `Predicate<T>` | Java = interface with 1 abstract method; C# = delegate type. |
| Custom function type | Any `@FunctionalInterface` | `delegate int Op(int a, int b);` | |
| Method reference | `String::length` | `str.Length` via `s => s.Length` (method group: `Console.WriteLine`) | Both support method-group/reference. |
| Capture | Effectively-final locals only | Any local (can mutate captured) | Java forbids mutating captured vars. |
| Lambda param modifiers | — | `ref`/`in`/`out` allowed *(Since 14)* | |

---

## 20. Events / callbacks

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Event mechanism | Listener interfaces + `addXListener(...)` | `event` keyword + delegates | C# has first-class events. |
| Declare | *(interface + list of listeners)* | `public event EventHandler Clicked;` | |
| Raise | manual loop over listeners | `Clicked?.Invoke(this, e);` | |
| Subscribe | `btn.addActionListener(e -> …)` | `btn.Clicked += (s, e) => …;` | |
| Partial events | — | `partial event …` *(Since 14)* | |

---

## 21. Exceptions

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Try / catch / finally | `try {} catch (IOException e) {} finally {}` | `try {} catch (IOException e) {} finally {}` | Nearly identical. |
| Multi-catch | `catch (A \| B e)` *(Since 7)* | `catch (Exception e) when (…)` | C# uses filters instead. |
| Exception filter | *(none; use `if` in catch)* | `catch (E e) when (e.Code == 5)` | |
| Checked exceptions | **Yes** — must declare `throws IOException` | **No** — all exceptions unchecked | Major difference. |
| Throw | `throw new RuntimeException("x");` | `throw new Exception("x");` | |
| Rethrow | `throw e;` | `throw;` (preserves stack) | |
| Resource cleanup | try-with-resources: `try (var r = open()) {}` *(Since 7)* | `using (var r = Open()) {}` / `using var r = …;` *(Since 8)* | Java: `AutoCloseable`; C#: `IDisposable`. |
| Base type | `Throwable` / `Exception` / `RuntimeException` | `Exception` | |

---

## 22. Collections & literals

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Growable list | `List<T>` → `new ArrayList<>()` | `List<T>` → `new()` | |
| Map / dictionary | `Map<K,V>` → `HashMap<>` | `Dictionary<K,V>` | |
| Set | `Set<T>` → `HashSet<>` | `HashSet<T>` | |
| Array | `int[] a = {1, 2, 3};` | `int[] a = { 1, 2, 3 };` or `[1, 2, 3]` | |
| Collection literal | `List.of(1, 2, 3)` (immutable) *(Since 9)* | `[1, 2, 3]` collection expression *(Since 12)* | C# 12 literal works for many collection types. |
| Spread / merge | `Stream.concat(...)` | `[..a, ..b, 99]` spread *(Since 12)* | |
| Immutable map | `Map.of("a", 1)` | `ImmutableDictionary` / frozen | |
| Sequenced access | `list.getFirst()`, `getLast()`, `reversed()` *(Since 21)* | `list[0]`, `list[^1]` | |
| Indexer syntax | `list.get(i)` | `list[i]` | |

---

## 23. Querying data — Streams vs LINQ

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Pipeline start | `list.stream()` | `list` (LINQ extension methods) | |
| Map / select | `.map(x -> x * 2)` | `.Select(x => x * 2)` | |
| Filter / where | `.filter(x -> x > 0)` | `.Where(x => x > 0)` | |
| Reduce / aggregate | `.reduce(0, Integer::sum)` | `.Aggregate(0, (a, x) => a + x)` | |
| To list | `.collect(Collectors.toList())` / `.toList()` | `.ToList()` | |
| Grouping | `.collect(groupingBy(...))` | `.GroupBy(...)` | |
| Query syntax | — *(method chains only)* | `from x in xs where … select x` | C# has SQL-like query syntax. |
| Lazy / deferred | Yes (terminal ops trigger) | Yes (deferred execution) | |
| New in latest | Stream **gatherers** `.gather(...)` *(Since 24)* | — | Custom intermediate ops in Java. |

---

## 24. Async & concurrency

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Async model | **Virtual threads** — write blocking code, runs cheaply *(Since 21)* | **async/await** — `async Task<T>` + `await` *(Since 5)* | Fundamentally different approaches. |
| Async method | *(just block on a virtual thread)* | `async Task<int> F() { await …; }` | Java has no `async`/`await` keywords. |
| Future / task | `CompletableFuture<T>` | `Task<T>` / `ValueTask<T>` | |
| Await result | `future.get()` / `.join()` | `await task` | |
| Start lightweight task | `Thread.ofVirtual().start(...)` | `Task.Run(...)` | |
| Structured concurrency | `StructuredTaskScope` *(preview in 25)* | *(via `Task.WhenAll`, no built-in scope)* | |
| Cancellation | `Thread.interrupt()` / `Future.cancel()` | `CancellationToken` | |
| Async streams | *(reactive libs / Flow)* | `await foreach` over `IAsyncEnumerable<T>` *(Since 8)* | |
| Lock | `synchronized` / `ReentrantLock` | `lock (obj) { }` | |

---

## 25. Extension methods

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Add method to existing type | — *(not possible)* | `static int Double(this int x) => x*2;` *(Since 3)* | Java has no extension methods. |
| Extension properties/operators | — | `extension(string s) { public bool IsBlank => …; }` block *(Since 14)* | New unified extension members in C# 14. |
| Workaround in Java | Static utility methods: `Utils.doubleOf(x)` | — | |

---

## 26. Attributes / annotations

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Apply metadata | `@Override`, `@Deprecated`, `@MyAnno` | `[Obsolete]`, `[MyAttr]` | Syntax differs (`@` vs `[]`). |
| With arguments | `@Table(name = "users")` | `[Table(Name = "users")]` | |
| Define | `@interface MyAnno { String value(); }` | `class MyAttr : Attribute { … }` | |
| Read at runtime | Reflection + `@Retention(RUNTIME)` | Reflection (attributes are runtime by default) | |
| Common built-ins | `@Override`, `@FunctionalInterface`, `@SuppressWarnings` | `[Obsolete]`, `[Serializable]`, `[Flags]` | |

---

## 27. Access modifiers

| Visibility | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Everywhere | `public` | `public` | |
| Same class only | `private` | `private` | |
| Subclasses + same package/assembly | `protected` | `protected` | Java `protected` also = package; C# is subclass-only. |
| Same package / assembly | *(package-private = no modifier)* | `internal` | Java default; C# needs keyword. |
| Subclass **or** assembly | — | `protected internal` | |
| Subclass **within** assembly | — | `private protected` | |
| File only | — | `file class Foo` *(Since 11)* | |

---

## 28. Misc: tuples, deconstruction, nested & anonymous types

| Concept | Java 25 | C# 14 | Notes |
|---|---|---|---|
| Tuple | — *(use `record` or `Map.entry`)* | `(int, string) t = (1, "a");` | C# has built-in tuples. |
| Named tuple | — | `(int Id, string Name) t` | |
| Deconstruction | Via record patterns in `switch` | `var (a, b) = point;` | |
| Discard | `_` unnamed variable *(Since 21)* | `_` discard *(Since 7)* | |
| Nested class | `class Outer { class Inner {} }` (inner holds outer ref) | `class Outer { class Inner {} }` (no implicit ref) | Java inner classes capture the outer instance. |
| Static nested | `static class Inner` | (nested classes are static-like by default) | |
| Anonymous class | `new Runnable() { public void run() {} }` | — *(use lambdas)* | |
| Anonymous type | — | `var p = new { X = 1, Y = 2 };` | C# has anonymous objects. |
| Local class | `class Local {}` inside a method | — | |

---

## Quick mental-model summary

- **Properties:** C# has them as a language feature; Java uses `getX()/setX()` methods or `record` accessors.
- **Nullability:** C# tracks it in the type system (`string?`); Java leans on `Optional` and annotations.
- **Generics:** C# reifies (types survive to runtime); Java erases them.
- **Async:** C# = `async/await`; Java = virtual threads + write ordinary blocking code.
- **Value types:** C# has `struct`; Java doesn't (Valhalla is still in progress).
- **Operator overloading & extension methods:** C# yes, Java no.
- **Checked exceptions:** Java yes, C# no.
- **`sealed`:** opposite-ish meanings — verify before you assume.
- **Primary constructors:** C# on any class; Java only via `record`.

---

*Versions referenced: Java 25 (LTS, Sept 2025) · C# 14 on .NET 10 (LTS, Nov 2025). "Since" = the version each feature first shipped as a stable language feature.*
